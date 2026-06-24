#!/bin/bash
# E2E UI 测试 - 使用 keyevent + agent-device (Android 16 兼容)
# keyevent: Tab(61) + Enter(66) 导航, Back(4) 返回
# agent-device: 基于 content-desc 精确点击

set -euo pipefail

PACKAGE="com.mispec.luomashu"
R="/tmp/e2e-results.txt"
P=0; F=0

ok()  { echo "  ✅ $1" | tee -a $R; P=$((P+1)); }
fail() { echo "  ❌ $1" | tee -a $R; F=$((F+1)); }

dump() {
    adb shell uiautomator dump /sdcard/_t.xml 2>/dev/null
    adb pull /sdcard/_t.xml /tmp/_t.xml 2>/dev/null
}

check_text() {
    local t="$1" d="${2:-$t}"
    dump
    grep -q "text=\"$t\"" /tmp/_t.xml 2>/dev/null && ok "$d" || fail "$d"
}

# 从 UI dump 提取元素中心并用 agent-device 点击
# 用 sed 分割 XML 元素，避免 [^>]* 跨越边界
tap_text() {
    local t="$1"
    dump
    # 先用 sed 提取包含目标文本的节点，再提取 bounds
    local node=$(sed 's/>/>\n/g' /tmp/_t.xml | grep "text=\"$t\"" | head -1)
    local b=$(echo "$node" | grep -o 'bounds="\[[0-9,]*\]\[[0-9,]*\]"' | head -1)
    [ -z "$b" ] && return 1
    local nums=$(echo $b | sed 's/bounds="//;s/"//;s/\]\[/,/;s/\[//g;s/\]//g')
    local x1=$(echo $nums | cut -d, -f1) y1=$(echo $nums | cut -d, -f2)
    local x2=$(echo $nums | cut -d, -f3) y2=$(echo $nums | cut -d, -f4)
    agent-device click $(( (x1+x2)/2 )) $(( (y1+y2)/2 )) 2>/dev/null
    sleep 1.5
}

tap_desc() {
    local d="$1"
    dump
    local node=$(sed 's/>/>\n/g' /tmp/_t.xml | grep "content-desc=\"$d\"" | head -1)
    local b=$(echo "$node" | grep -o 'bounds="\[[0-9,]*\]\[[0-9,]*\]"' | head -1)
    [ -z "$b" ] && return 1
    local nums=$(echo $b | sed 's/bounds="//;s/"//;s/\]\[/,/;s/\[//g;s/\]//g')
    local x1=$(echo $nums | cut -d, -f1) y1=$(echo $nums | cut -d, -f2)
    local x2=$(echo $nums | cut -d, -f3) y2=$(echo $nums | cut -d, -f4)
    agent-device click $(( (x1+x2)/2 )) $(( (y1+y2)/2 )) 2>/dev/null
    sleep 1.5
}

back() { adb shell input keyevent 4; sleep 1; }

# ═══ Setup ═══
adb shell am force-stop $PACKAGE 2>/dev/null
adb shell pm clear $PACKAGE 2>/dev/null
sleep 1
adb shell am start -n $PACKAGE/.MainActivity 2>&1 | tee -a $R
sleep 5

echo "=== E2E UI 测试 ===" | tee $R
echo "Start: $(date)" | tee -a $R

# ═══ T1: Onboarding - 用 keyevent Tab+Enter 穿越3页 ═══
echo "" | tee -a $R
echo "=== T1: Onboarding ===" | tee -a $R
# Page1 → Tab+Enter → Page2 → Tab+Enter → Page3 → Tab+Enter → Home
adb shell input keyevent 61; adb shell input keyevent 66; sleep 1
adb shell input keyevent 61; adb shell input keyevent 66; sleep 1
adb shell input keyevent 61; adb shell input keyevent 66; sleep 1
adb shell input keyevent 4; sleep 1  # dismiss keyboard
check_text "神经反应" "Home screen loaded"

# ═══ T2: 6 Categories ═══
echo "" | tee -a $R
echo "=== T2: Categories ===" | tee -a $R
dump
for c in "神经反应" "思维认知" "性格镜像" "知识百科" "感官挑战" "趣味游乐"; do
    grep -q "text=\"$c\"" /tmp/_t.xml 2>/dev/null && ok "$c" || fail "$c"
done
grep -q "text=\"58 个工具\"" /tmp/_t.xml 2>/dev/null && ok "tool count" || fail "tool count"

# ═══ T3: Neuro → Tool List ═══
echo "" | tee -a $R
echo "=== T3: Neuro → Tool List ===" | tee -a $R
tap_text "神经反应"
check_text "APM 测试" "APM visible"
check_text "CPS 连点测试" "CPS visible"
check_text "计时挑战" "timing visible"
check_text "共 6 个工具" "count correct"

# ═══ T4: APM Tool Flow - 验证 Result 状态修复 ═══
echo "" | tee -a $R
echo "=== T4: APM → Start → Result ===" | tee -a $R
tap_desc "tool-item-apm" || true
check_text "开始测试" "start button"
check_text "10秒内尽可能多点" "description"
# 点击开始测试
tap_text "开始测试" || true
sleep 2
# 验证 Running 状态
dump
if grep -q 'content-desc="interact-area"' /tmp/_t.xml 2>/dev/null; then
    ok "Running state: interact-area visible"
else
    fail "Running state: interact-area not found"
fi
# 等待测试完成 (10秒 + 2秒缓冲)
echo "  ⏳ Waiting 12s for test to complete..." | tee -a $R
sleep 12
# 验证 Result 状态 - 修复验证点
dump
# 1. 结果卡片应该存在
if grep -q 'content-desc="测试结果"' /tmp/_t.xml 2>/dev/null; then
    ok "Result: result card visible"
else
    fail "Result: result card not found"
fi
# 2. 结果图标应该存在
if grep -q 'content-desc="result-icon"' /tmp/_t.xml 2>/dev/null; then
    ok "Result: result-icon visible"
else
    fail "Result: result-icon not found"
fi
# 3. Running 状态的 interact-area 应该被隐藏
if grep -q 'content-desc="interact-area"' /tmp/_t.xml 2>/dev/null; then
    fail "Result: interact-area still visible (should be hidden)"
else
    ok "Result: interact-area hidden (correct)"
fi
# 4. "再来一次" 按钮应该存在
if grep -q 'content-desc="再来一次按钮"' /tmp/_t.xml 2>/dev/null; then
    ok "Result: retry button visible"
else
    fail "Result: retry button not found"
fi
# 5. "分享成绩" 按钮应该存在
if grep -q 'content-desc="分享结果"' /tmp/_t.xml 2>/dev/null; then
    ok "Result: share button visible"
else
    fail "Result: share button not found"
fi
# 6. 鼓励文本应该存在
if grep -q '你的' /tmp/_t.xml 2>/dev/null || grep -q '加油' /tmp/_t.xml 2>/dev/null || grep -q '太棒了' /tmp/_t.xml 2>/dev/null; then
    ok "Result: encouragement text visible"
else
    fail "Result: encouragement text not found"
fi

# ═══ T5: Back to Home ═══
echo "" | tee -a $R
echo "=== T5: Back Navigation ===" | tee -a $R
back; sleep 1
# TOOL → 确认退出对话框
tap_text "退出" || true; sleep 1
back; sleep 1
check_text "神经反应" "back to home"

# ═══ T6: Cognitive ═══
echo "" | tee -a $R
echo "=== T6: Cognitive → IQ ===" | tee -a $R
tap_text "思维认知"
check_text "IQ 测试" "IQ visible"
back

# ═══ T7: Personality ═══
echo "" | tee -a $R
echo "=== T7: Personality → MBTI ===" | tee -a $R
tap_text "性格镜像"
check_text "MBTI 测试" "MBTI visible"
back

# ═══ T8: Knowledge ═══
echo "" | tee -a $R
echo "=== T8: Knowledge ===" | tee -a $R
tap_text "知识百科"
check_text "百科知识测试" "trivia visible"
back

# ═══ T9: Sensory ═══
echo "" | tee -a $R
echo "=== T9: Sensory ===" | tee -a $R
tap_text "感官挑战"
check_text "视力测试" "vision visible"
back

# ═══ T10: Playground ═══
echo "" | tee -a $R
echo "=== T10: Playground ===" | tee -a $R
tap_text "趣味游乐"
check_text "扫雷" "minesweeper visible"
back

# ═══ Summary ═══
echo "" | tee -a $R
echo "=== Summary ===" | tee -a $R
echo "  Passed: $P" | tee -a $R
echo "  Failed: $F" | tee -a $R
T=$((P+F))
[ $T -gt 0 ] && echo "  Pass Rate: $((P*100/T))%" | tee -a $R
echo "  End: $(date)" | tee -a $R

adb shell am force-stop $PACKAGE 2>/dev/null
