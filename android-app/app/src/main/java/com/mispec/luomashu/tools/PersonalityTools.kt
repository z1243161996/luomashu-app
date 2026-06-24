package com.mispec.luomashu.tools

import androidx.compose.runtime.Composable
import com.mispec.luomashu.ui.components.*

// ── Independent question banks for every scale tool ──
private val mbtiQ = listOf(
    "聚会后我需要独处来恢复精力", "我更喜欢提前计划而非随机应变",
    "我做决定更多依赖逻辑而非感受", "我喜欢有条理的生活方式",
    "我容易被新想法吸引", "我更关注整体而非细节",
    "我在团队中更喜欢领导", "我容易察觉他人的情绪变化",
    "我喜欢尝试新事物", "我说话前会思考",
    "我更相信实际经验", "截止日期能让我更高效"
)

private val bigfiveQ = listOf(
    "我是派对的灵魂人物", "我对他人富有同情心",
    "我做事井井有条", "我经常感到焦虑",
    "我对艺术有浓厚兴趣", "我容易信任他人",
    "我更喜欢自己一个人", "我做事有始有终",
    "我很少感到悲伤", "我喜欢冒险",
    "我待人礼貌周到", "我在压力下很冷静"
)

private val enneagramQ = listOf(
    "我对自己有很高的标准", "我乐于帮助身边的人",
    "成功对我来说很重要", "我常常感到与众不同",
    "我喜欢观察和分析事物", "我做事前习惯考虑最坏情况",
    "我容易感到快乐和满足", "我敢于表达自己的立场",
    "我倾向于避免冲突", "我在人群中感到自在",
    "我追求完美", "我容易受他人情绪影响"
)

private val eqQ = listOf(
    "我能识别自己的情绪变化", "我会站在他人角度考虑问题",
    "我能控制自己的冲动", "面对挫折我能迅速恢复",
    "我能察觉他人的非语言信号", "在冲突中我能保持冷静",
    "我善于调节团队气氛", "我做决定时会考虑他人感受",
    "我能准确表达自己的情绪", "我接受批评时不会产生防御心理",
    "我善于倾听", "我有清晰的自我认知"
)

private val jungQ = listOf(
    "我更看重事实而非理论", "我做决定凭直觉多于分析",
    "我关注当下多于未来", "我做事靠逻辑多于情感",
    "我更喜欢具体的任务", "我常思考抽象的概念",
    "我凭感觉判断对错", "我偏好理性的讨论",
    "我注重实际经验", "我喜欢探索可能性",
    "我在意他人的感受", "我按原则行事"
)

private val hollandQ = listOf(
    "我喜欢动手操作工具", "我对科学实验感到好奇",
    "我热爱绘画和音乐", "我乐于帮助他人解决问题",
    "我喜欢领导团队", "我擅长整理数据和文件",
    "我喜欢户外活动", "我对数学和物理感兴趣",
    "我享受创作的过程", "我关心他人的福祉",
    "我善于说服别人", "我享受有条理的工作"
)

private val mentalAgeQ = listOf(
    "我喜欢尝试新鲜事物", "我习惯早睡早起",
    "我对流行文化很了解", "我喜欢安静的环境",
    "我在社交中很活跃", "我做事更稳重",
    "我喜欢冒险", "我对新技术接受度高",
    "我心态比实际年龄年轻", "我面对变化适应快",
    "我更喜欢传统方式", "我对未来充满期待"
)

private val animalQ = listOf(
    "我在团队中喜欢领导位置", "我善于独立思考",
    "我关注集体和谐", "我做事勤奋认真",
    "我性格独立", "我稳重可靠",
    "我有强烈的正义感", "我性格温和",
    "我机智灵活", "我优雅从容",
    "我勇敢无畏", "我忠诚耿直"
)

private val rpiQ = listOf(
    "我了解自己的优点和缺点", "我在不同场合表现不同",
    "我有时会感到矛盾", "我的行为和价值观一致",
    "我能客观评价自己", "他人的反馈常让我惊讶",
    "我的自我认知比较稳定", "我有时会怀疑自己",
    "我清楚自己想要什么", "我的情绪和自我认知相关",
    "我善于反思", "我对自己的评价很准确"
)

private val talentQ = listOf(
    "我对数字敏感", "我擅长表达和写作",
    "我有音乐方面的天分", "我空间想象力丰富",
    "我的运动协调性很好", "我善于与人交流",
    "我喜欢自我反思", "我对自然界很感兴趣",
    "我逻辑推理能力强", "我讲故事很生动",
    "我能听出音乐中的细微差别", "我有设计感"
)

private val valuesQ = listOf(
    "家庭对我而言非常重要", "事业成功是我的追求",
    "个人自由不可或缺", "知识增长让我满足",
    "健康是一切的基础", "财富能带来安全感",
    "真挚友情是生命之光", "公平正义是我的信念",
    "家庭和睦比金钱更重要", "我愿为事业牺牲娱乐时间",
    "自由比安全更重要", "终身学习是我的目标"
)

private val colorQ = listOf(
    "我做事果断直接", "我习惯深思熟虑",
    "我追求人际和谐", "我天生乐观开朗",
    "我的情绪容易影响他人", "我更喜欢独处思考",
    "我尽量避免冲突", "我总能找到快乐",
    "我有时显得急躁", "我做事讲究逻辑",
    "我善于倾听他人", "我对未来充满信心"
)

@Composable fun MbtiScreen() = ScaleToolScreen("MBTI 测试", mbtiQ, toolId = "mbti") { s ->
    val ei = s[0]+s[5]+s[9]; val sn = s[1]+s[4]+s[11]; val tf = s[2]+s[10]+s[7]; val jp = s[3]+s[6]+s[8]
    val type = (if (ei >= 9) "E" else "I") + (if (sn >= 9) "N" else "S") + (if (tf >= 9) "T" else "F") + (if (jp >= 9) "J" else "P")
    val desc = when (type) {
        "INTJ" -> "独立的战略家，善于规划长远。你有深刻的洞察力和强大的执行力，喜欢独自工作并追求卓越。"
        "INTP" -> "好奇的思想家，热爱探索理论。你善于分析复杂问题，追求知识的深度和逻辑的完美。"
        "ENTJ" -> "天生的领导者，果断且高效。你有强大的组织能力和决策力，善于带领团队实现目标。"
        "ENTP" -> "机智的辩论者，喜欢挑战常规。你思维敏捷，善于发现新可能性，不喜欢被规则束缚。"
        "INFJ" -> "安静的理想主义者，洞察力强。你有深刻的同理心和远见，追求有意义的生活和工作。"
        "INFP" -> "富有同情心的梦想家，忠于价值观。你内心世界丰富，追求真实和有意义的事物。"
        "ENFJ" -> "富有魅力的引导者，善于激励他人。你有天生的领导力，关心他人成长，是团队的灵魂。"
        "ENFP" -> "热情洋溢的创新者，充满可能性。你乐观开朗，善于发现机会，喜欢探索新事物。"
        "ISTJ" -> "务实可靠的责任者，注重细节。你有强烈的责任感，做事有条理，值得信赖。"
        "ISFJ" -> "温暖体贴的守护者，默默奉献。你关心他人，细心周到，是朋友和家人的可靠支柱。"
        "ESTJ" -> "高效的组织者，注重秩序。你有强大的组织能力，善于制定规则并确保执行。"
        "ESFJ" -> "热心的合作者，关心他人需求。你善于社交，乐于助人，是团队的润滑剂。"
        "ISTP" -> "灵活的问题解决者，动手能力强。你善于分析和解决实际问题，喜欢独立工作。"
        "ISFP" -> "温和的艺术家，活在当下。你有敏锐的美感，追求和谐，善于表达内心世界。"
        "ESTP" -> "大胆的实践者，喜欢刺激。你行动力强，善于把握机会，喜欢挑战和冒险。"
        "ESFP" -> "天生的表演者，带来欢乐。你热情开朗，善于社交，是聚会的焦点。"
        else -> "你的MBTI类型"
    }
    type to desc
}
@Composable fun BigFiveScreen() = ScaleToolScreen("大五人格测试", bigfiveQ, toolId = "bigfive") { s ->
    val o = s[4]+s[9]; val c = s[2]+s[7]; val e = s[0]+s[6]; val a = s[1]+s[5]+s[10]; val n = s[3]+s[8]+s[11]
    val oDesc = if (o >= 7) "高开放性：你富有想象力，对新事物充满好奇" else "低开放性：你务实稳重，偏好熟悉的事物"
    val cDesc = if (c >= 7) "高尽责性：你做事有条理，自律性强" else "低尽责性：你灵活随性，不喜欢被规则束缚"
    val eDesc = if (e >= 7) "高外向性：你善于社交，精力充沛" else "低外向性：你安静内敛，享受独处"
    val aDesc = if (a >= 9) "高宜人性：你善良友善，善于合作" else "低宜人性：你独立自主，坚持己见"
    val nDesc = if (n >= 7) "高神经质：你情感丰富，容易焦虑" else "低神经质：你情绪稳定，心态平和"
    "大五人格画像" to "$oDesc\n$cDesc\n$eDesc\n$aDesc\n$nDesc"
}
@Composable fun EnneagramScreen() = ScaleToolScreen("九型人格测试", enneagramQ, toolId = "enneagram") { s ->
    val types = listOf("完美主义者","助人者","成就者","艺术家","观察者","忠诚者","享乐者","挑战者","和平者")
    val descs = listOf(
        "追求完美，有强烈的责任感和道德标准",
        "热心助人，善于理解他人需求",
        "追求成功，注重效率和成就",
        "富有创造力，追求独特和真实",
        "善于观察分析，追求知识和真理",
        "忠诚可靠，注重安全和稳定",
        "乐观开朗，追求快乐和自由",
        "果断自信，敢于挑战和竞争",
        "温和随和，追求和谐与平静"
    )
    val scores = listOf(s[0]+s[10], s[1]+s[9], s[2]+s[11], s[3], s[4], s[5], s[6], s[7], s[8])
    val idx = scores.indexOf(scores.maxOrNull() ?: s[0])
    "${types[idx]}型" to "你是${types[idx]}：${descs[idx]}"
}
@Composable fun EqScreen() = ScaleToolScreen("情商测试", eqQ, toolId = "eq") { s ->
    val eq = s.sum()
    when {
        eq > 45 -> "高情商" to "你擅长理解和管理情绪，在人际交往中游刃有余。你能敏锐地察觉他人情绪，并做出恰当回应。"
        eq > 30 -> "中情商" to "你基本能处理情绪挑战，但在某些情况下可能需要更多练习。建议多关注自己和他人的情绪变化。"
        else -> "情商发展型" to "每个人都有提升空间！建议多练习情绪识别和管理，这将帮助你在生活和工作中更加顺利。"
    }
}
@Composable fun JungScreen() = ScaleToolScreen("荣格人格测试", jungQ, toolId = "jung") { s ->
    val thinking = s[0]+s[8]+s[10]; val feeling = s[1]+s[9]+s[11]; val sensing = s[2]+s[4]+s[6]; val intuition = s[3]+s[5]+s[7]
    val dominant = maxOf(thinking, feeling, sensing, intuition)
    val (type, desc) = when (dominant) {
        thinking -> "思维型" to "你善于逻辑分析，做决定时更依赖理性思考。你追求客观真理，注重效率和逻辑。"
        feeling -> "情感型" to "你善于理解他人感受，做决定时更考虑人际和谐。你重视价值观和情感联系。"
        sensing -> "感觉型" to "你关注具体事实和当下体验，善于处理实际问题。你务实可靠，注重细节。"
        else -> "直觉型" to "你善于把握整体和未来可能性，喜欢探索抽象概念。你富有想象力，追求创新。"
    }
    "荣格类型：$type" to desc
}
@Composable fun HollandScreen() = ScaleToolScreen("霍兰德职业测试", hollandQ, toolId = "holland") { s ->
    val codes = listOf("R 现实型","I 研究型","A 艺术型","S 社会型","E 企业型","C 常规型")
    val descs = listOf(
        "你适合从事需要动手能力和技术技能的工作，如工程师、技师、运动员",
        "你适合从事研究和分析工作，如科学家、医生、程序员",
        "你适合从事创造性工作，如设计师、艺术家、作家",
        "你适合从事服务和帮助他人的工作，如教师、医生、社工",
        "你适合从事领导和管理工作，如企业家、经理、律师",
        "你适合从事有条理的工作，如会计、行政、数据分析师"
    )
    val idx = s.subList(0,6).mapIndexed { i, v -> i to v }.maxByOrNull { it.second }?.first ?: 0
    "${codes[idx]}" to "你是${codes[idx]}类型：${descs[idx]}"
}
@Composable fun MentalAgeScreen() = ScaleToolScreen("心理年龄测试", mentalAgeQ, toolId = "mentalage") { s ->
    val age = 12 + s.sum() / 3
    val desc = when {
        age < 15 -> "你拥有一颗充满活力的童心，对世界充满好奇和热情"
        age < 20 -> "你年轻有活力，敢于尝试新事物，充满可能性"
        age < 30 -> "你成熟稳重，既有年轻人的热情，又有成熟者的智慧"
        age < 40 -> "你心态成熟，善于平衡工作和生活，处事沉稳"
        else -> "你拥有丰富的人生经验和智慧，是值得信赖的顾问"
    }
    "心理年龄: $age 岁" to desc
}
@Composable fun AnimalScreen() = ScaleToolScreen("动物人格测试", animalQ, toolId = "animal-personality") { s ->
    val animals = listOf("狼","鹰","海豚","蜜蜂","猫","大象","狮子","熊猫","狐狸","鹿")
    val descs = listOf(
        "你有狼的团结精神和领导力，善于团队协作",
        "你有鹰的敏锐洞察力，善于发现机会",
        "你有海豚的友善和智慧，善于社交",
        "你有蜜蜂的勤劳和团队精神，注重效率",
        "你有猫的独立和优雅，享受独处",
        "你有大象的稳重和可靠，值得信赖",
        "你有狮子的勇气和领导力，敢于挑战",
        "你有熊猫的温和和乐观，带来欢乐",
        "你有狐狸的聪明和机智，善于应变",
        "你有鹿的优雅和敏感，善于观察"
    )
    val idx = s.subList(0,10).mapIndexed { i, v -> i to v }.maxByOrNull { it.second }?.first ?: 0
    "${animals[idx]}型人格" to "你是${animals[idx]}：${descs[idx]}"
}
@Composable fun RpiScreen() = ScaleToolScreen("人格镜像测试", rpiQ, toolId = "rpi") { s ->
    val score = s.sum()
    val desc = when {
        score >= 40 -> "你有很强的自我认知能力，能够客观地看待自己的优缺点"
        score >= 30 -> "你的自我认知能力良好，但有时可能需要他人反馈来更全面地了解自己"
        else -> "建议多关注他人反馈，这将帮助你更全面地认识自己"
    }
    "镜像指数: $score" to "你的自我认知能力：$desc"
}
@Composable fun TalentScreen() = ScaleToolScreen("天赋测试", talentQ, toolId = "talent") { s ->
    val talents = listOf("逻辑数学","语言表达","音乐节奏","空间想象","运动能力","人际交往","内省","自然探索")
    val descs = listOf(
        "你善于逻辑推理和数学计算，适合从事科学研究、编程等工作",
        "你善于语言表达和沟通，适合从事写作、教学、销售等工作",
        "你有音乐天赋，善于节奏和旋律，适合从事音乐相关工作",
        "你有丰富的空间想象力，适合从事设计、建筑、艺术等工作",
        "你运动协调性好，适合从事体育、舞蹈、手工艺等工作",
        "你善于与人交往，适合从事管理、销售、教育等工作",
        "你善于自我反思和内省，适合从事心理学、哲学等工作",
        "你对自然界充满好奇，适合从事环保、农业、生物等工作"
    )
    val idx = s.subList(0,8).mapIndexed { i, v -> i to v }.maxByOrNull { it.second }?.first ?: 0
    "天赋：${talents[idx]}" to "你的天赋领域：${descs[idx]}"
}
@Composable fun ValuesScreen() = ScaleToolScreen("价值观测试", valuesQ, toolId = "values") { s ->
    val values = listOf("家庭","事业","自由","知识","健康","财富","友情","正义")
    val descs = listOf(
        "家庭是你最重要的支柱，你重视亲情和家庭和谐",
        "事业是你追求的目标，你希望通过工作实现自我价值",
        "自由是你最看重的，你追求独立和自主的生活方式",
        "知识是你不断追求的，你热爱学习和探索",
        "健康是你最关心的，你注重身心健康和生活品质",
        "财富给你安全感，你追求经济独立和物质保障",
        "友情是你珍视的，你重视真诚的友谊和人际关系",
        "正义是你坚持的，你追求公平和正义"
    )
    val idx = s.subList(0,8).mapIndexed { i, v -> i to v }.maxByOrNull { it.second }?.first ?: 0
    "核心价值观：${values[idx]}" to "你的价值追求：${descs[idx]}"
}
@Composable fun PersonalityColorScreen() = ScaleToolScreen("性格色彩测试", colorQ, toolId = "personality-color") { s ->
    val colors = listOf("红色","蓝色","绿色","黄色")
    val descs = listOf(
        "红色性格：行动派，果断直接，追求效率。你做事雷厉风行，不喜欢拖沓，是天生的领导者。",
        "蓝色性格：思考派，深思熟虑，追求完美。你做事严谨细致，注重逻辑和细节，是优秀的分析师。",
        "绿色性格：和平派，温和友善，追求和谐。你善于倾听和协调，是团队的稳定剂。",
        "黄色性格：乐观派，积极开朗，追求快乐。你充满活力，善于激励他人，是团队的开心果。"
    )
    val idx = s.subList(0,4).mapIndexed { i, v -> i to v * 3 }.maxByOrNull { it.second }?.first ?: 0
    "${colors[idx]}性格" to descs[idx]
}
