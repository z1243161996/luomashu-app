package com.mispec.luomashu.data

import androidx.compose.ui.graphics.Color
import com.mispec.luomashu.ui.theme.AppColors

enum class Category(
    val id: String,
    val label: String,
    val icon: String,
    val desc: String,
    val color: Color,
) {
    NEURO("neuro","神经反应","Speed","反应速度 · 协调性",AppColors.catNeuro),
    COGNITIVE("cognitive","思维认知","Psychology","记忆 · 专注 · 推理",AppColors.catCognitive),
    PERSONALITY("personality","性格镜像","SelfImprovement","MBTI · 九型 · 大五",AppColors.catPersonality),
    KNOWLEDGE("knowledge","知识百科","MenuBook","常识 · 成语 · 谜语",AppColors.catKnowledge),
    SENSORY("sensory","感官挑战","Visibility","视觉 · 听觉 · 色觉",AppColors.catSensory),
    PLAYGROUND("playground","趣味游乐","Casino","扫雷 · 猜数字 · 追按钮",AppColors.catPlayground),
}

fun categoryTools(cat: Category): List<ToolDef> = when (cat) {
    Category.NEURO -> NeuroTools
    Category.COGNITIVE -> CognitiveTools
    Category.PERSONALITY -> PersonalityTools
    Category.KNOWLEDGE -> KnowledgeTools
    Category.SENSORY -> SensoryTools
    Category.PLAYGROUND -> PlaygroundTools
}
