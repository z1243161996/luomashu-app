package com.mispec.luomashu.util

import kotlin.random.Random

private val phrases = listOf(
    "你的大脑今天状态不错!",
    "再试一次可能会有惊喜",
    "这个成绩已经很棒了",
    "每个人都有自己的节奏",
    "坚持就是胜利",
    "你的潜力远不止于此",
    "今天比昨天进步了一点",
    "专注力是你的秘密武器",
    "你的反应速度让人印象深刻",
    "每一次尝试都是一次成长",
    "保持好奇心, 继续探索",
    "你的大脑正在变得更强大",
    "享受过程比结果更重要",
    "你比自己想象的更优秀",
    "今天的努力是明天的基石",
    "继续挑战, 你正在进步",
    "你的专注力令人赞叹",
    "每一次练习都有意义",
    "你的潜力是无限的",
    "放松心态, 发挥会更好",
    "你的进步有目共睹",
    "相信自己, 你能做到",
    "享受思考的乐趣吧",
    "你的大脑正在形成新的连接",
    "持之以恒, 终将绽放"
)

/**
 * Get a random encouragement phrase.
 * @param seed optional seed for reproducible randomness
 */
fun randomEncouragement(seed: Long? = null): String {
    val rng = if (seed != null) Random(seed) else Random
    return phrases[rng.nextInt(phrases.size)]
}
