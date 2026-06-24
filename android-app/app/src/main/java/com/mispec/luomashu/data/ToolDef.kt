package com.mispec.luomashu.data

/** Represents a single tool/test in the app. @param id unique tool identifier, @param name display name, @param type interaction mode */
data class ToolDef(val id: String, val name: String, val type: ToolType, val estimatedSeconds: Int = 30)

/** Interaction mode for each tool screen. */
enum class ToolType { CLICK, QUIZ, SCALE, TARGET, CUSTOM }

val NeuroTools = listOf(
    ToolDef("apm","APM 测试",ToolType.CLICK,10),
    ToolDef("cps","CPS 连点测试",ToolType.CLICK,10),
    ToolDef("timing-challenge","计时挑战",ToolType.CLICK,15),
    ToolDef("choice-reaction","选择反应测试",ToolType.CLICK,10),
    ToolDef("gaming-talent","游戏天赋测试",ToolType.CLICK,10),
    ToolDef("reaction-time","反应速度测试",ToolType.CLICK,10),
)

val CognitiveTools = listOf(
    ToolDef("memory","记忆力测试",ToolType.CLICK,15),
    ToolDef("aim-trainer","瞄准训练",ToolType.CLICK,15),
    ToolDef("schulte-grid","舒尔特方格",ToolType.CLICK,15),
    ToolDef("stroop","斯特鲁普测试",ToolType.CLICK,10),
    ToolDef("iq","IQ 测试",ToolType.QUIZ,90),
    ToolDef("logic","逻辑测试",ToolType.QUIZ,72),
    ToolDef("math-speed","速算测试",ToolType.QUIZ,72),
    ToolDef("number-sense","数感测试",ToolType.QUIZ,72),
    ToolDef("sequence","序列记忆",ToolType.CLICK,15),
    ToolDef("focus-training","专注力训练",ToolType.CLICK,15),
    ToolDef("inhibition","抑制控制测试",ToolType.CLICK,10),
    ToolDef("chimp","黑猩猩测试",ToolType.CLICK,15),
    ToolDef("multitasking","多任务测试",ToolType.CLICK,15),
)

val PersonalityTools = listOf(
    ToolDef("mbti","MBTI 测试",ToolType.SCALE,60),
    ToolDef("bigfive","大五人格测试",ToolType.SCALE,60),
    ToolDef("enneagram","九型人格测试",ToolType.SCALE,60),
    ToolDef("eq","情商测试",ToolType.SCALE,60),
    ToolDef("jung","荣格人格测试",ToolType.SCALE,60),
    ToolDef("holland","霍兰德职业测试",ToolType.SCALE,60),
    ToolDef("mentalage","心理年龄测试",ToolType.SCALE,60),
    ToolDef("animal-personality","动物人格测试",ToolType.SCALE,60),
    ToolDef("rpi","人格镜像测试",ToolType.SCALE,60),
    ToolDef("talent","天赋测试",ToolType.SCALE,60),
    ToolDef("values","价值观测试",ToolType.SCALE,60),
    ToolDef("personality-color","性格色彩测试",ToolType.SCALE,60),
)

val KnowledgeTools = listOf(
    ToolDef("trivia","百科知识测试",ToolType.QUIZ,72),
    ToolDef("riddle","谜语测试",ToolType.QUIZ,72),
    ToolDef("idiom","成语测试",ToolType.QUIZ,72),
    ToolDef("science","科学知识测试",ToolType.QUIZ,72),
    ToolDef("history","历史知识测试",ToolType.QUIZ,72),
    ToolDef("geography","地理知识测试",ToolType.QUIZ,72),
    ToolDef("english-vocabulary","英语词汇测试",ToolType.QUIZ,72),
    ToolDef("vocabulary","汉语词汇测试",ToolType.QUIZ,72),
    ToolDef("poetry","诗词测试",ToolType.QUIZ,72),
    ToolDef("chemistry","化学知识测试",ToolType.QUIZ,72),
    ToolDef("sports","体育知识测试",ToolType.QUIZ,72),
    ToolDef("animalfact","动物知识测试",ToolType.QUIZ,72),
)

val SensoryTools = listOf(
    ToolDef("vision","视力测试",ToolType.CLICK,15),
    ToolDef("hearing","听力测试",ToolType.CLICK,15),
    ToolDef("pitch","音高辨别测试",ToolType.CLICK,15),
    ToolDef("rhythm","节奏感测试",ToolType.CLICK,15),
    ToolDef("color-sensitivity","色觉敏感度测试",ToolType.CLICK,15),
    ToolDef("angle","角度辨别测试",ToolType.CLICK,15),
    ToolDef("spot-difference","找不同",ToolType.CLICK,20),
    ToolDef("dynamic-vision","动态视力测试",ToolType.CLICK,15),
)

val PlaygroundTools = listOf(
    ToolDef("minesweeper","扫雷",ToolType.CLICK,30),
    ToolDef("guess-number","猜数字",ToolType.CLICK,15),
    ToolDef("chase-button","追按钮",ToolType.CLICK,15),
    ToolDef("dont-press","别按它",ToolType.CLICK,15),
    ToolDef("circle-challenge","圆圈挑战",ToolType.CLICK,15),
    ToolDef("dnd-alignment","D&D 阵营测试",ToolType.QUIZ,72),
    ToolDef("emoji-quiz","Emoji 猜谜",ToolType.QUIZ,72),
)
