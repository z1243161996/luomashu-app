package com.mispec.luomashu.tools

import androidx.compose.runtime.Composable
import com.mispec.luomashu.ui.components.*

// ── Interactive ──
@Composable fun MemoryScreen2() = MemoryTestScreen(toolId = "memory")
@Composable fun AimTrainerScreen2() = AimTrainerScreen(toolId = "aim-trainer")
@Composable fun SchulteGridScreen2() = SchulteGridScreen(toolId = "schulte-grid")
@Composable fun StroopTestScreen2() = StroopTestScreen(toolId = "stroop")
@Composable fun FocusScreen2() = FocusTrainingScreen(toolId = "focus-training")
@Composable fun InhibitionScreen2() = InhibitionTestScreen(toolId = "inhibition")
@Composable fun ChimpTestScreen2() = ChimpTestScreen(toolId = "chimp")
@Composable fun MultiTaskingScreen2() = MultiTaskingTestScreen(toolId = "multitasking")

// ── Quiz-based ──
private val iqQuestions = (0..19).mapIndexed { i, _ ->
    val a = (2..99).random(); val b = (2..99).random(); val c = (2..99).random(); val d = (2..99).random()
    val prime = listOf(17, 29, 37, 41, 53, 61, 73, 79, 89, 97).random()
    val patterns = listOf(
        QuizQuestion("$a + $b × $c = ?", listOf("${a+b*c}", "${(a+b)*c}", "${a*b+c}", "${a*c+b}"), 0),
        QuizQuestion("选出不同的一个", listOf("苹果","香蕉","橘子","土豆"), 3),
        QuizQuestion("如果 A>B 且 B>C，那么", listOf("A<C","A>C","A=C","不确定"), 1),
        QuizQuestion("12, 24, 36, 48, ?", listOf("52","60","72","96"), 1),
        QuizQuestion("红色在可见光谱中波长约为", listOf("700nm","500nm","400nm","300nm"), 0),
        QuizQuestion("48÷2÷3÷4 = ?", listOf("2","3","4","6"), 0),
        QuizQuestion("他山之石可以攻玉的成语是", listOf("他山之石","抄家之石","落井下石","一石二鸟"), 0),
        QuizQuestion("三角形内角和是", listOf("180°","360°","90°","270°"), 0),
        QuizQuestion("1kg = ?g", listOf("1000","100","500","10000"), 0),
        QuizQuestion("植物进行光合作用的细胞器是", listOf("叶绿体","线粒体","细胞核","细胞膜"), 0),
        QuizQuestion("144÷12 = ?", listOf("12","14","11","13"), 0),
        QuizQuestion("擂台上进行的比赛是", listOf("拳击","抽狗","篮球","乒乓球"), 0),
        QuizQuestion("红楼梦的作者是", listOf("曹雪芹","施耐庵","吴承恩","罗贯中"), 0),
        QuizQuestion("哪个是质数？$prime $a $b $c", listOf("$prime","$a","$b","$c"), 0),
        QuizQuestion("福尔摩斯的原则是排除", listOf("不可能的","最简单的","最难的","最慢的"), 0),
        QuizQuestion("long 去掉字母l...g后的意思是", listOf("渴望","长期","孤独","火热"), 0),
        QuizQuestion("3² + 4² = ?", listOf("25","12","7","24"), 0),
        QuizQuestion("蒸笼里小笼包是指", listOf("小型蒸笼","包子","馄馒","饭团"), 0),
        QuizQuestion("CARDIO的I是什么单词", listOf("Idiot","Intelligent","Ideal","Iron"), 0),
    )
    patterns[i % patterns.size]
}

private val logicQuestions = (0..19).mapIndexed { i, _ ->
    val a = (2..20).random(); val b = (20..99).random(); val c = (2..9).random()
    val base = (2..9).random()
    val patterns = listOf(
        QuizQuestion("1,1,2,3,5,8,下一个是?", listOf("13","11","14","15"), 0),
        QuizQuestion("2,4,8,16,32,下一个是?", listOf("64","48","40","56"), 0),
        QuizQuestion("1,4,9,16,25,36,下一个是?", listOf("49","42","48","50"), 0),
        QuizQuestion("3,6,12,24,48,下一个是?", listOf("96","72","60","84"), 0),
        QuizQuestion("1,3,6,10,15,下一个是?", listOf("21","18","19","20"), 0),
        QuizQuestion("2,3,5,7,11,下一个是?", listOf("13","12","14","15"), 0),
        QuizQuestion("$a,${a*2},${a*3},${a*4},下一个是?", listOf("${a*5}","${a*6}","${a*4+a}","${a*5-1}"), 0),
        QuizQuestion("$base,${base*base},${base*base*base},下一个是?", listOf("${base*base*base*base}","${base*base*base+base}","${base*base*base*2}","${base*base*base+base*base}"), 0),
        QuizQuestion("$b,${b-2},${b-4},${b-6},下一个是?", listOf("${b-8}","${b-10}","${b-6}","${b-7}"), 0),
        QuizQuestion("B,D,F,H,下一个字母是?", listOf("J","I","G","K"), 0),
        QuizQuestion("1,2,4,8,16,下一个是?", listOf("32","24","20","28"), 0),
        QuizQuestion("100,90,80,70,下一个是?", listOf("60","50","65","75"), 0),
        QuizQuestion("$c,${c*2},${c*3},${c*4},下一个是?", listOf("${c*5}","${c*6}","${c*3+c}","${c*5-1}"), 0),
        QuizQuestion("A,C,E,G,下一个是?", listOf("I","H","F","J"), 0),
        QuizQuestion("1,2,6,24,120,下一个是?", listOf("720","240","360","480"), 0),
    )
    patterns[i % patterns.size]
}

private val mathQuestions = (0..19).map { i ->
    val a = (11..999).random(); val b = (11..999).random(); val c = (2..9).random(); val d = (10..99).random()
    val patterns = listOf(
        QuizQuestion("$a + $b = ?", listOf("${a+b}", "${a+b+1}", "${a+b+a%10}", "${a+b-b%10}"), 0),
        QuizQuestion("$a - $b = ?", listOf("${a-b}", "${b-a}", "${a+b}", "${(a*b)%1000}"), 0),
        QuizQuestion("$a × $c = ?", listOf("${a*c}", "${a*c+c}", "${a*c-c}", "${a*c+a}"), 0),
        QuizQuestion("$d × $d = ?", listOf("${d*d}", "${d*10+d}", "${d+d*10}", "${d*d+d}"), 0),
        QuizQuestion("$a ÷ $c ≈ ? (整数)", listOf("${a/c}", "${a/c+1}", "${a/c-1}", "${a/c*2}"), 0),
        QuizQuestion("$a × 11 = ?", listOf("${a*11}", "${a*10+a}", "${a*10+a+1}", "${a*12}"), 0),
        QuizQuestion("$d × 9 = ?", listOf("${d*9}", "${d*10-d}", "${d*8+d}", "${d*9+d}"), 0),
        QuizQuestion("($a + $b) ÷ 2 ≈ ?", listOf("${(a+b)/2}", "${(a+b)/2+1}", "${(a+b)/2-1}", "${(a+b)}"), 0),
        QuizQuestion("$b - $a = ?", listOf("${b-a}", "${a-b}", "${a+b}", "${b+a}"), 0),
        QuizQuestion("$a × $c + $b = ?", listOf("${a*c+b}", "${a*(c+b)}", "${a*c*b}", "${a+b*c}"), 0),
    )
    patterns[i % patterns.size]
}

private val numberQuestions = (0..19).map { i ->
    val n = (100..9999).random(); val m = (10..999).random()
    val patterns = listOf(
        QuizQuestion("将 $n 四舍五入到百位", listOf("${(n+50)/100*100}", "${n/100*100}", "${n/10*10}", "${n}"), 0),
        QuizQuestion("将 $n 四舍五入到十位", listOf("${(n+5)/10*10}", "${n/10*10}", "${n/100*100}", "${n}"), 0),
        QuizQuestion("$m 的立方是?", listOf("${m*m*m}", "${m*m}", "${m*3}", "${m+m+m}"), 0),
        QuizQuestion("$m × 25 = ?", listOf("${m*25}", "${m*20+5}", "${m*30-5}", "${m*100/4}"), 0),
        QuizQuestion("36% 表示分数是?", listOf("9/25","1/3","3/10","36/100"), 0),
        QuizQuestion("$n ÷ 5 = ?", listOf("${n/5}", "${n%5}", "${n/5+1}", "${n*5}"), 0),
        QuizQuestion("pi = ? (取整)", listOf("3","4","5","2"), 0),
        QuizQuestion("$m × 99 = ? (快速算法)", listOf("${m*100-m}", "${m*90+9}", "${m*100-1}", "${(m-1)*100}"), 0),
        QuizQuestion("将 $n 四舍五入到千位", listOf("${(n+500)/1000*1000}", "${n/1000*1000}", "${n/100*100}", "${(n+50)/100*100}"), 0),
        QuizQuestion("$n 的各位数字之和是?", listOf("${n.toString().sumOf { it.code - 48 }}", "${n%10}", "${n/10%10}", "${n/100}"), 0),
    )
    patterns[i % patterns.size]
}

@Composable fun IqScreen() = QuizToolScreen("IQ 测试", iqQuestions.take(15), toolId = "iq")
@Composable fun LogicScreen() = QuizToolScreen("逻辑测试", logicQuestions.take(12), toolId = "logic")
@Composable fun MathSpeedScreen() = QuizToolScreen("速算测试", mathQuestions.take(12), toolId = "math-speed")
@Composable fun NumberSenseScreen() = QuizToolScreen("数感测试", numberQuestions.take(12), toolId = "number-sense")
@Composable fun SequenceScreen() = MemoryTestScreen(toolId = "sequence")  // real sequence memory
