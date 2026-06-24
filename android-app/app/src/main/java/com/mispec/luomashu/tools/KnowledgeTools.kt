package com.mispec.luomashu.tools

import androidx.compose.runtime.Composable
import com.mispec.luomashu.ui.components.QuizQuestion
import com.mispec.luomashu.ui.components.QuizToolScreen

private val triviaQ = listOf(
    QuizQuestion("世界上最大的大陆是？", listOf("亚欧大陆","非洲大陆","北美大陆","南美大陆"), 0),
    QuizQuestion("光年是什么单位？", listOf("距离","时间","速度","亮度"), 0),
    QuizQuestion("人体最大的器官是？", listOf("皮肤","肝脏","大脑","心脏"), 0),
    QuizQuestion("地球绕太阳一周大约需要？", listOf("365天","30天","24小时","7天"), 0),
    QuizQuestion("世界上人口最多的国家是？", listOf("印度","中国","美国","印尼"), 0),
    QuizQuestion("最快的陆地动物是？", listOf("猎豹","狮子","羚羊","马"), 0),
    QuizQuestion("DNA的全称是？", listOf("脱氧核糖核酸","核糖核酸","氨基酸","蛋白质"), 0),
    QuizQuestion("太阳系中最大的行星是？", listOf("木星","土星","天王星","海王星"), 0),
    QuizQuestion("第一次世界大战爆发于哪年？", listOf("1914","1918","1939","1900"), 0),
    QuizQuestion("黄金的化学符号是？", listOf("Au","Ag","Cu","Fe"), 0),
    QuizQuestion("世界上最高的山峰是？", listOf("珠穆朗玛峰","K2","洛子峰","马卡鲁峰"), 0),
    QuizQuestion("WiFi 使用的频段通常是？", listOf("2.4GHz","1GHz","5.6GHz","10GHz"), 0),
)

private val riddleQ = listOf(
    QuizQuestion("什么东西越洗越脏？", listOf("水","毛巾","肥皂","海绵"), 0),
    QuizQuestion("什么桥不能过？", listOf("彩虹桥","独木桥","大桥","小桥"), 0),
    QuizQuestion("什么门没有把手？", listOf("球门","铁门","木门","铝门"), 0),
    QuizQuestion("什么书不能读？", listOf("秘书","天书","兵书","圣贤书"), 0),
    QuizQuestion("什么蛋不能吃？", listOf("脸蛋","鸡蛋","鸭蛋","鹅蛋"), 0),
    QuizQuestion("什么马不能骑？", listOf("斑马","海马","河马","木马"), 1),
    QuizQuestion("什么牛不会耕田？", listOf("蜗牛","水牛","黄牛","奶牛"), 0),
    QuizQuestion("什么花不能浇水？", listOf("火花","烟花","梅花","兰花"), 0),
    QuizQuestion("什么车最长？", listOf("堵车","火车","卡车","轿车"), 0),
    QuizQuestion("什么猫不抓老鼠？", listOf("熊猫","狸猫","花猫","黑猫"), 0),
    QuizQuestion("什么东西越用越亮？", listOf("灯泡","眼睛","镜子","钻石"), 0),
    QuizQuestion("什么人不吃饭？", listOf("植物人","机器人","外星人","圣人"), 1),
)

private val idiomQ = listOf(
    QuizQuestion("画蛇添足比喻什么？", listOf("多此一举","画得好","快速","懒惰"), 0),
    QuizQuestion("守株待兔讽刺什么？", listOf("侥幸心理","勤劳","聪明","勇敢"), 0),
    QuizQuestion("对牛弹琴的意思是？", listOf("白费口舌","弹琴好听","牛听音乐","耕地"), 0),
    QuizQuestion("卧薪尝胆讲的是谁？", listOf("勾践","秦始皇","刘邦","韩信"), 0),
    QuizQuestion("一箭双雕表示？", listOf("一举两得","射箭技术","打猎","武功"), 0),
    QuizQuestion("掩耳盗铃讽刺什么行为？", listOf("自欺欺人","偷盗","创意","聪明"), 0),
    QuizQuestion("杯弓蛇影比喻？", listOf("疑神疑鬼","喝酒","射箭","战争"), 0),
    QuizQuestion("破釜沉舟表示什么决心？", listOf("义无反顾","破坏","造船","逃跑"), 0),
    QuizQuestion("画龙点睛说明什么重要？", listOf("关键之笔","画画","龙","传说"), 0),
    QuizQuestion("亡羊补牢告诉我们要？", listOf("及时补救","养羊","建房","放牧"), 0),
    QuizQuestion("指鹿为马是？", listOf("颠倒黑白","养动物","幽默","变魔术"), 0),
    QuizQuestion("叶公好龙讽刺的是？", listOf("表面喜欢","真正热爱","养龙","勇敢"), 0),
)

private val scienceQ = listOf(
    QuizQuestion("水的化学式是？", listOf("H2O","CO2","O2","NaCl"), 0),
    QuizQuestion("光在真空中的速度约是多少？", listOf("30万km/s","3万km/s","300km/s","3000km/s"), 0),
    QuizQuestion("DNA双螺旋结构由谁发现？", listOf("沃森和克里克","达尔文","孟德尔","爱因斯坦"), 0),
    QuizQuestion("哪种气体占大气的78%？", listOf("氮气","氧气","二氧化碳","氦气"), 0),
    QuizQuestion("元素周期表有多少个周期？", listOf("7","6","8","5"), 0),
    QuizQuestion("重力加速度约等于？", listOf("9.8m/s2","1m/s2","100m/s2","5m/s2"), 0),
    QuizQuestion("哪种维生素缺乏会导致坏血病？", listOf("维生素C","维生素A","维生素D","维生素B"), 0),
    QuizQuestion("pH值7表示？", listOf("中性","酸性","碱性","强酸"), 0),
    QuizQuestion("大陆漂移说由谁提出？", listOf("魏格纳","达尔文","牛顿","爱因斯坦"), 0),
    QuizQuestion("电流的单位是？", listOf("安培","伏特","瓦特","欧姆"), 0),
    QuizQuestion("声音不能在什么中传播？", listOf("真空","空气","水","金属"), 0),
    QuizQuestion("地球有几颗卫星？", listOf("1颗","0颗","2颗","3颗"), 0),
)

private val historyQ = listOf(
    QuizQuestion("中国第一个皇帝是谁？", listOf("秦始皇","汉高祖","唐太宗","周武王"), 0),
    QuizQuestion("二战结束于哪一年？", listOf("1945","1944","1946","1943"), 0),
    QuizQuestion("文艺复兴起源于哪个国家？", listOf("意大利","法国","英国","德国"), 0),
    QuizQuestion("《独立宣言》是哪国文件？", listOf("美国","英国","法国","德国"), 0),
    QuizQuestion("法国大革命发生在哪一年？", listOf("1789","1776","1848","1917"), 0),
    QuizQuestion("第一个登上月球的人是？", listOf("阿姆斯特朗","加加林","奥尔德林","科林斯"), 0),
    QuizQuestion("工业革命起源于哪个国家？", listOf("英国","美国","德国","法国"), 0),
    QuizQuestion("《周易》成书于哪个朝代？", listOf("周朝","商朝","秦朝","汉朝"), 0),
    QuizQuestion("柏林墙倒塌于哪一年？", listOf("1989","1991","1985","1990"), 0),
    QuizQuestion("发现新大陆的是？", listOf("哥伦布","麦哲伦","达伽马","郑和"), 0),
    QuizQuestion("罗马帝国建立者是？", listOf("奥古斯都","凯撒","尼禄","卡拉卡拉"), 0),
    QuizQuestion("金字塔是哪国古文明的象征？", listOf("埃及","印度","中国","巴比伦"), 0),
)

private val geographyQ = listOf(
    QuizQuestion("世界上最大的洋是？", listOf("太平洋","大西洋","印度洋","北冰洋"), 0),
    QuizQuestion("赤道穿过哪个大洲最多？", listOf("非洲","亚洲","南美洲","大洋洲"), 0),
    QuizQuestion("亚马孙雨林在哪个大洲？", listOf("南美洲","非洲","亚洲","大洋洲"), 0),
    QuizQuestion("中国最大的岛屿是？", listOf("台湾岛","海南岛","崇明岛","舟山群岛"), 0),
    QuizQuestion("长江全长约多少公里？", listOf("6300","5000","7000","8000"), 0),
    QuizQuestion("世界上最大的沙漠是？", listOf("撒哈拉沙漠","戈壁沙漠","阿拉伯沙漠","卡拉哈里"), 0),
    QuizQuestion("死海位于哪两国之间？", listOf("约旦和以色列","埃及和以色列","伊朗和伊拉克","沙特和也门"), 0),
    QuizQuestion("哪条运河连接太平洋和大西洋？", listOf("巴拿马运河","苏伊士运河","京杭运河","伊利运河"), 0),
    QuizQuestion("世界屋脊指的是？", listOf("青藏高原","喜马拉雅山","安第斯山","洛基山"), 0),
    QuizQuestion("澳大利亚的首都是？", listOf("堪培拉","悉尼","墨尔本","布里斯班"), 0),
    QuizQuestion("黄河注入哪个海？", listOf("渤海","黄海","东海","南海"), 0),
    QuizQuestion("格林尼治天文台在哪个城市？", listOf("伦敦","巴黎","纽约","罗马"), 0),
)

private val englishQ = listOf(
    QuizQuestion("apple 的中文意思是？", listOf("苹果","香蕉","橙子","葡萄"), 0),
    QuizQuestion("beautiful 的反义词是？", listOf("ugly","pretty","nice","cute"), 0),
    QuizQuestion("He ___ a student. 填空", listOf("is","are","am","be"), 0),
    QuizQuestion("yesterday 是什么时态的标志词？", listOf("过去时","现在时","将来时","进行时"), 0),
    QuizQuestion("Book 的复数形式是？", listOf("Books","Bookes","Bookies","Booken"), 0),
    QuizQuestion("rain cats and dogs 意思是？", listOf("倾盆大雨","猫狗打架","小雨","多云"), 0),
    QuizQuestion("I have ___ apple. 填冠词", listOf("an","a","the","不填"), 0),
    QuizQuestion("fast 的比较级是？", listOf("faster","more fast","fastest","fastly"), 0),
    QuizQuestion("She ___ to school every day. 动词形式", listOf("goes","go","going","went"), 0),
    QuizQuestion("children 是哪个词的不规则复数？", listOf("child","man","woman","person"), 0),
    QuizQuestion("There ___ many people. be动词", listOf("are","is","am","be"), 0),
    QuizQuestion("water 是可数还是不可数？", listOf("不可数","可数","都可以","看情况"), 0),
)

private val vocabularyQ = listOf(
    QuizQuestion("璀璨的意思是？", listOf("光彩夺目","暗淡","模糊","混乱"), 0),
    QuizQuestion("踌躇的意思接近？", listOf("犹豫","果断","快速","快乐"), 0),
    QuizQuestion("饕餮通常形容什么？", listOf("贪吃","勇敢","智慧","懒惰"), 0),
    QuizQuestion("袅袅描述的是什么？", listOf("轻盈柔美","粗壮","笨重","快"), 0),
    QuizQuestion("旖旎常用于描写？", listOf("风景优美","声音洪亮","动作快","味道香"), 0),
    QuizQuestion("窈窕形容？", listOf("女子体态美","男子强壮","年老","年幼"), 0),
    QuizQuestion("磅礴通常形容？", listOf("气势宏大","细小","颜色","味道"), 0),
    QuizQuestion("下列哪个是贬义词？", listOf("狡猾","聪明","机智","灵活"), 0),
    QuizQuestion("缱绻表达什么情感？", listOf("情意缠绵","愤怒","高兴","悲伤"), 0),
    QuizQuestion("氤氲描述的是什么？", listOf("烟雾弥漫","声音","颜色","味道"), 0),
    QuizQuestion("孜孜不倦的意思是？", listOf("勤奋不懈","懒惰","快速","聪明"), 0),
    QuizQuestion("蔚为大观形容什么？", listOf("盛大壮观","细小","单调","混乱"), 0),
)

private val poetryQ = listOf(
    QuizQuestion("床前明月光是哪位诗人的作品？", listOf("李白","杜甫","白居易","王维"), 0),
    QuizQuestion("锄禾日当午出自？", listOf("悯农","静夜思","春晓","登高"), 0),
    QuizQuestion("春眠不觉晓下句是？", listOf("处处闻啼鸟","花落知多少","夜来风雨声","春风吹又生"), 0),
    QuizQuestion("苏轼号什么？", listOf("东坡居士","青莲居士","六一居士","香山居士"), 0),
    QuizQuestion("两个黄鹂鸣翠柳的诗人是？", listOf("杜甫","李白","白居易","王之涣"), 0),
    QuizQuestion("《诗经》共多少篇？", listOf("305","300","310","200"), 0),
    QuizQuestion("落霞与孤鹜齐飞出自？", listOf("滕王阁序","岳阳楼记","醉翁亭记","赤壁赋"), 0),
    QuizQuestion("但愿人长久出自哪首词？", listOf("水调歌头","念奴娇","江城子","蝶恋花"), 0),
    QuizQuestion("李清照是哪个朝代的？", listOf("宋代","唐代","元代","明代"), 0),
    QuizQuestion("莫愁前路无知己下句？", listOf("天下谁人不识君","西出阳关无故人","春风不度玉门关","一片冰心在玉壶"), 0),
    QuizQuestion("《红楼梦》作者是？", listOf("曹雪芹","罗贯中","施耐庵","吴承恩"), 0),
    QuizQuestion("夕阳无限好下句？", listOf("只是近黄昏","千里共婵娟","一览众山小","润物细无声"), 0),
)

private val chemistryQ = listOf(
    QuizQuestion("元素周期表中第一个元素是？", listOf("氢","氦","锂","碳"), 0),
    QuizQuestion("Na是哪一种元素的符号？", listOf("钠","铁","钾","钙"), 0),
    QuizQuestion("pH试纸遇酸变什么颜色？", listOf("红色","蓝色","绿色","黑色"), 0),
    QuizQuestion("食盐的主要成分是？", listOf("NaCl","CaCO3","H2SO4","NaOH"), 0),
    QuizQuestion("金刚石由什么元素组成？", listOf("碳","硅","铁","铜"), 0),
    QuizQuestion("燃烧三要素不包括？", listOf("催化剂","可燃物","氧气","温度"), 0),
    QuizQuestion("哪种金属在常温下是液态？", listOf("汞","铁","铜","铝"), 0),
    QuizQuestion("空气中含量最多的气体是？", listOf("氮气","氧气","氢气","二氧化碳"), 0),
    QuizQuestion("干冰是固态的？", listOf("二氧化碳","水","氧气","氮气"), 0),
    QuizQuestion("原子核由什么组成？", listOf("质子和中子","电子和质子","电子和中子","只有质子"), 0),
    QuizQuestion("酸性溶液的pH值？", listOf("小于7","等于7","大于7","等于0"), 0),
    QuizQuestion("什么气体会导致温室效应？", listOf("二氧化碳","氧气","氮气","氢气"), 0),
)

private val sportsQ = listOf(
    QuizQuestion("足球比赛每队上场几人？", listOf("11","10","9","12"), 0),
    QuizQuestion("篮球场上有几个篮筐？", listOf("2","1","4","3"), 0),
    QuizQuestion("奥运会的格言是？", listOf("更快更高更强","友谊第一","重在参与","体育强国"), 0),
    QuizQuestion("乒乓球直径是多少毫米？", listOf("40mm","38mm","42mm","50mm"), 0),
    QuizQuestion("网球四大满贯不包括？", listOf("中国公开赛","温网","法网","澳网"), 0),
    QuizQuestion("百米世界纪录保持者是？", listOf("博尔特","刘易斯","鲍威尔","盖伊"), 0),
    QuizQuestion("马拉松的官方距离约为？", listOf("42公里","40公里","50公里","30公里"), 0),
    QuizQuestion("NBA一共有多少支球队？", listOf("30","28","32","26"), 0),
    QuizQuestion("羽毛球单打场地的宽度？", listOf("5.18m","6.1m","4m","7m"), 0),
    QuizQuestion("首届现代奥运会在哪个城市？", listOf("雅典","巴黎","伦敦","罗马"), 0),
    QuizQuestion("游泳比赛中最长的泳姿是？", listOf("1500米自由泳","100米蝶泳","200米混合","400米自由"), 0),
    QuizQuestion("排球女将的发源地是？", listOf("中国","美国","日本","巴西"), 0),
)

private val animalQ = listOf(
    QuizQuestion("世界上最大的哺乳动物是？", listOf("蓝鲸","大象","长颈鹿","鲸鲨"), 0),
    QuizQuestion("企鹅生活在哪个极地？", listOf("南极","北极","赤道","温带"), 0),
    QuizQuestion("哪种动物每年迁徙最长距离？", listOf("北极燕鸥","大雁","帝王蝶","角马"), 0),
    QuizQuestion("骆驼的驼峰储存什么？", listOf("脂肪","水","食物","空气"), 0),
    QuizQuestion("蜜蜂跳摇摆舞表示什么？", listOf("食物方向","危险","集合","休息"), 0),
    QuizQuestion("哪种动物被称为丛林之王？", listOf("老虎","狮子","熊","狼"), 0),
    QuizQuestion("蝙蝠是唯一能飞的？", listOf("哺乳动物","鸟类","爬行动物","昆虫"), 0),
    QuizQuestion("章鱼有几颗心脏？", listOf("3","1","2","4"), 0),
    QuizQuestion("鸭嘴兽的独特特征是？", listOf("卵生哺乳","有翅膀","有鳞片","有羽毛"), 0),
    QuizQuestion("变色龙变色的主要原因是？", listOf("伪装和情绪","温度","食物","光线"), 0),
    QuizQuestion("鲨鱼能感知什么？", listOf("电场","磁场","温度","声音"), 0),
    QuizQuestion("大熊猫主要以什么为食？", listOf("竹子","肉","水果","昆虫"), 0),
)

@Composable fun TriviaScreen() = QuizToolScreen("百科知识测试", triviaQ, toolId = "trivia")
@Composable fun RiddleScreen() = QuizToolScreen("谜语测试", riddleQ, toolId = "riddle")
@Composable fun IdiomScreen() = QuizToolScreen("成语测试", idiomQ, toolId = "idiom")
@Composable fun ScienceScreen() = QuizToolScreen("科学知识测试", scienceQ, toolId = "science")
@Composable fun HistoryScreen() = QuizToolScreen("历史知识测试", historyQ, toolId = "history")
@Composable fun GeographyScreen() = QuizToolScreen("地理知识测试", geographyQ, toolId = "geography")
@Composable fun EnglishScreen() = QuizToolScreen("英语词汇测试", englishQ, toolId = "english-vocabulary")
@Composable fun VocabularyScreen() = QuizToolScreen("汉语词汇测试", vocabularyQ, toolId = "vocabulary")
@Composable fun PoetryScreen() = QuizToolScreen("诗词测试", poetryQ, toolId = "poetry")
@Composable fun ChemistryScreen() = QuizToolScreen("化学知识测试", chemistryQ, toolId = "chemistry")
@Composable fun SportsScreen() = QuizToolScreen("体育知识测试", sportsQ, toolId = "sports")
@Composable fun AnimalFactScreen() = QuizToolScreen("动物知识测试", animalQ, toolId = "animalfact")
