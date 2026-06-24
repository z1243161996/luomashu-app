package com.mispec.luomashu.ui.home

import com.mispec.luomashu.util.sem
import com.mispec.luomashu.util.RecentStore
import com.mispec.luomashu.util.OnboardingStore

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mispec.luomashu.data.*
import com.mispec.luomashu.tools.*
import com.mispec.luomashu.ui.theme.AppColors

enum class Screen { HOME, CATEGORY, TOOL }

fun iconForCategory(name: String) = when (name) {
    "Speed" -> Icons.Filled.Speed
    "Psychology" -> Icons.Filled.Psychology
    "SelfImprovement" -> Icons.Filled.SelfImprovement
    "MenuBook" -> Icons.AutoMirrored.Filled.MenuBook
    "Visibility" -> Icons.Filled.Visibility
    "Casino" -> Icons.Filled.Casino
    else -> Icons.Filled.Category
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeScreen() {
    var currentScreen by remember { mutableStateOf(Screen.HOME) }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var selectedTool by remember { mutableStateOf<ToolDef?>(null) }
    var goingForward by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val onboardingShown by OnboardingStore.hasShown(context).collectAsState(initial = true)

    fun navigateTo(target: Screen) {
        goingForward = true
        currentScreen = target
    }

    /** Record tool usage and navigate to the tool. */
    fun openTool(tool: ToolDef) {
        selectedTool = tool
        // Find and set the tool's category so back navigation works correctly
        selectedCategory = Category.entries.find { cat -> categoryTools(cat).any { it.id == tool.id } }
        scope.launch { RecentStore.recordUse(context, tool.id) }
        navigateTo(Screen.TOOL)
    }

    var showBackConfirm by remember { mutableStateOf(false) }

    fun navigateBack() {
        goingForward = false
        if (currentScreen == Screen.CATEGORY) currentScreen = Screen.HOME
        else if (currentScreen == Screen.TOOL) { showBackConfirm = true; return }
    }

    BackHandler(enabled = currentScreen != Screen.HOME, onBack = ::navigateBack)

    if (showBackConfirm) {
        AlertDialog(
            onDismissRequest = { showBackConfirm = false },
            title = { Text("确认退出") },
            text = { Text("当前测试进度将丢失，确定要退出吗？") },
            confirmButton = { TextButton(onClick = { showBackConfirm = false; currentScreen = Screen.CATEGORY }) { Text("退出", color = AppColors.danger) } },
            dismissButton = { TextButton(onClick = { showBackConfirm = false }) { Text("继续测试") } }
        )
    }

    // Show onboarding on first launch
    if (!onboardingShown) {
        OnboardingScreen(onComplete = {
            scope.launch { OnboardingStore.markShown(context) }
        })
        return
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            when (currentScreen) {
                Screen.HOME -> HomeTopBar()
                Screen.CATEGORY -> BackTopBar(
                    title = selectedCategory?.label ?: "",
                    onBack = ::navigateBack
                )
                Screen.TOOL -> BackTopBar(
                    title = selectedTool?.name ?: "",
                    onBack = ::navigateBack
                )
            }
        }
    ) { padding ->
        AnimatedContent(
            targetState = currentScreen,
            transitionSpec = {
                val dir = if (goingForward) 280 else -280
                slideInHorizontally(
                    animationSpec = spring(stiffness = Spring.StiffnessMediumLow, dampingRatio = Spring.DampingRatioMediumBouncy)
                ) { w -> dir / 3 } +
                    fadeIn(spring(stiffness = Spring.StiffnessMediumLow)) togetherWith
                slideOutHorizontally(
                    animationSpec = spring(stiffness = Spring.StiffnessMediumLow, dampingRatio = Spring.DampingRatioMediumBouncy)
                ) { w -> -dir / 3 } +
                    fadeOut(spring(stiffness = Spring.StiffnessMediumLow))
            },
            label = "screen"
        ) { screen ->
            Box(Modifier.padding(padding).fillMaxSize()) {
                when (screen) {
                    Screen.HOME -> HomeContent(
                        onCategoryClick = { cat ->
                            selectedCategory = cat; navigateTo(Screen.CATEGORY)
                        },
                        onToolDirectClick = { tool -> openTool(tool) }
                    )
                    Screen.CATEGORY -> selectedCategory?.let { cat ->
                        CategoryContent(cat, onToolClick = { tool -> openTool(tool) })
                    }
                    Screen.TOOL -> selectedTool?.let { tool -> ToolShell(tool) }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar() {
    CenterAlignedTopAppBar(
        modifier = Modifier.sem("app-header"),
        title = { Text("脑洞测试", fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.sem("header-title")) },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackTopBar(title: String, onBack: () -> Unit) {
    TopAppBar(
        modifier = Modifier.sem("app-header"),
        title = { Text(title, fontWeight = FontWeight.SemiBold, fontSize = 18.sp, modifier = Modifier.sem("header-title")) },
        navigationIcon = {
            IconButton(onClick = onBack, modifier = Modifier.sem("btn-back")) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

// ── Home Content ──

/** Flat map of toolId -> ToolDef for quick lookup. */
private val allToolsById: Map<String, ToolDef> by lazy {
    Category.entries.flatMap { categoryTools(it) }.associateBy { it.id }
}

/** All tools flattened with their category, for search. */
private val allToolsWithCategory: List<Pair<ToolDef, Category>> by lazy {
    Category.entries.flatMap { cat -> categoryTools(cat).map { it to cat } }
}

@Composable
fun HomeContent(onCategoryClick: (Category) -> Unit, onToolDirectClick: ((ToolDef) -> Unit)? = null) {
    val categories = remember { Category.entries.toList() }
    val totalTools = remember { Category.entries.sumOf { categoryTools(it).size } }
    val context = LocalContext.current
    val recentIds by RecentStore.recentIds(context).collectAsState(initial = emptyList())
    val recentTools = remember(recentIds) { recentIds.mapNotNull { allToolsById[it] }.take(6) }
    var searchQuery by remember { mutableStateOf("") }
    var debouncedQuery by remember { mutableStateOf("") }
    // Debounce search input by 300ms
    LaunchedEffect(searchQuery) {
        if (searchQuery.isEmpty()) { debouncedQuery = ""; return@LaunchedEffect }
        kotlinx.coroutines.delay(300L)
        debouncedQuery = searchQuery
    }
    val isSearching = debouncedQuery.isNotBlank()
    val filteredTools = remember(debouncedQuery) {
        if (!isSearching) emptyList()
        else allToolsWithCategory.filter { (tool, cat) ->
            tool.name.contains(debouncedQuery, ignoreCase = true) ||
            cat.label.contains(debouncedQuery, ignoreCase = true) ||
            cat.desc.contains(debouncedQuery, ignoreCase = true)
        }
    }

    Column(Modifier.fillMaxSize().sem("app-shell", "应用主界面")) {
        // ── Search bar ──
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            placeholder = { Text("搜索测试…") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "搜索") },
            trailingIcon = {
                if (isSearching) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Filled.Clear, contentDescription = "清除")
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        // ── Search results ──
        if (isSearching) {
            if (filteredTools.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("没有找到匹配的测试", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
                    items(filteredTools) { (tool, cat) ->
                        Surface(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp).clickable { onToolDirectClick?.invoke(tool) },
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surface
                        ) {
                            Row(Modifier.padding(horizontal = 16.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Column(Modifier.weight(1f)) {
                                    Text(tool.name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                                    Text(cat.label, style = MaterialTheme.typography.labelSmall, color = cat.color)
                                }
                                val dur = if (tool.estimatedSeconds < 60) "~${tool.estimatedSeconds}秒" else "~${tool.estimatedSeconds / 60}分钟"
                                Text(dur, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                            }
                        }
                    }
                }
            }
        } else {
            // ── Recently played ──
            if (recentTools.isNotEmpty()) {
                Text("最近玩过", modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp), style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(recentTools) { tool ->
                        Card(
                            onClick = { onToolDirectClick?.invoke(tool) },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                            modifier = Modifier.width(120.dp).height(64.dp)
                        ) {
                            Column(Modifier.padding(horizontal = 14.dp, vertical = 10.dp).fillMaxSize(), verticalArrangement = Arrangement.Center) {
                                Text(tool.name, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Spacer(Modifier.height(2.dp))
                                val dur = if (tool.estimatedSeconds < 60) "~${tool.estimatedSeconds}秒" else "~${tool.estimatedSeconds / 60}分钟"
                                Text(dur, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                            }
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
            }

            // ── Category section header ──
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp).sem("section-title"),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("选择一项测试", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Surface(color = MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(6.dp), modifier = Modifier.sem("tool-count-badge")) {
                    Text("$totalTools 个工具", modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(categories) { cat -> CategoryCard(cat, onClick = { onCategoryClick(cat) }) }
            }
        }
    }
}

@Composable
fun CategoryCard(cat: Category, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().sem("cat-card-cat-${cat.id}").clickable(onClickLabel = "进入${cat.label}", onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Surface(color = cat.color.copy(alpha = 0.12f), shape = RoundedCornerShape(12.dp), modifier = Modifier.size(44.dp)) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(iconForCategory(cat.icon), contentDescription = cat.label, tint = cat.color, modifier = Modifier.size(24.dp))
                    }
                }
                Surface(color = cat.color.copy(alpha = 0.10f), shape = RoundedCornerShape(6.dp)) {
                    Text("${categoryTools(cat).size}", modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp).sem("cat-count-${cat.id}"), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = cat.color)
                }
            }
            Spacer(Modifier.height(12.dp))
            Text(cat.label, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Spacer(Modifier.height(4.dp))
            Text(cat.desc, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
fun CategoryContent(category: Category, onToolClick: (ToolDef) -> Unit) {
    val tools = remember { categoryTools(category) }
    Column(Modifier.fillMaxSize().sem("app-shell", "应用主界面")) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 10.dp).sem("section-title"), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Text("共 ${tools.size} 个工具", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Medium)
        }
        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
        LazyColumn(Modifier.sem("tool-list"), contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
            items(tools) { tool -> ToolListItem(tool, onClick = { onToolClick(tool) }) }
            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun ToolListItem(tool: ToolDef, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp).sem("tool-item-${tool.id}").clickable(onClickLabel = "打开${tool.name}", onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(Modifier.padding(horizontal = 16.dp, vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(color = MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(8.dp), modifier = Modifier.size(36.dp)) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(imageVector = when (tool.type) {
                        ToolType.CLICK -> Icons.Filled.TouchApp; ToolType.QUIZ -> Icons.Filled.Quiz
                        ToolType.SCALE -> Icons.Filled.ThumbsUpDown; else -> Icons.Filled.PlayArrow
                    }, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp))
                }
            }
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(tool.name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                Text(when (tool.type) { ToolType.CLICK -> "点击测试"; ToolType.QUIZ -> "知识问答"; ToolType.SCALE -> "量表评估"; else -> "交互工具" }, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                val duration = if (tool.estimatedSeconds < 60) "~${tool.estimatedSeconds}秒" else "~${tool.estimatedSeconds / 60}分钟"
                Text(duration, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
            }
            Icon(Icons.Filled.ChevronRight, contentDescription = "进入", tint = MaterialTheme.colorScheme.outline, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
fun ToolShell(tool: ToolDef) {
    var hasError by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf("") }
    var retryKey by remember { mutableIntStateOf(0) }
    Box(Modifier.fillMaxSize().sem("app-shell", "应用主界面")) {
        if (hasError) {
            Column(Modifier.fillMaxSize().padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Icon(Icons.Filled.Warning, contentDescription = "错误", tint = AppColors.danger, modifier = Modifier.size(48.dp))
                Spacer(Modifier.height(16.dp))
                Text("工具加载失败", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text(errorMsg, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(20.dp))
                FilledTonalButton(onClick = { hasError = false; retryKey++ }) { Text("重试") }
            }
        } else {
            key(tool.id, retryKey) {
                ToolRouter(tool)
            }
        }
    }
}

private val toolScreens: Map<String, @Composable () -> Unit> = mapOf(
        // Neuro
        "apm" to { ApmScreen() }, "cps" to { CpsScreen() },
        "timing-challenge" to { com.mispec.luomashu.tools.TimingChallengeScreen2() },
        "choice-reaction" to { com.mispec.luomashu.tools.ChoiceReactionScreen2() },
        "gaming-talent" to { GamingTalentScreen() },
        "reaction-time" to { com.mispec.luomashu.tools.ReactionTimeScreen2() },
        // Cognitive
        "memory" to { com.mispec.luomashu.tools.MemoryScreen2() },
        "aim-trainer" to { com.mispec.luomashu.tools.AimTrainerScreen2() },
        "schulte-grid" to { com.mispec.luomashu.tools.SchulteGridScreen2() },
        "stroop" to { com.mispec.luomashu.tools.StroopTestScreen2() },
        "iq" to { IqScreen() }, "logic" to { LogicScreen() }, "math-speed" to { MathSpeedScreen() },
        "number-sense" to { NumberSenseScreen() }, "sequence" to { SequenceScreen() },
        "focus-training" to { com.mispec.luomashu.tools.FocusScreen2() },
        "inhibition" to { com.mispec.luomashu.tools.InhibitionScreen2() },
        "chimp" to { com.mispec.luomashu.tools.ChimpTestScreen2() },
        "multitasking" to { com.mispec.luomashu.tools.MultiTaskingScreen2() },
        // Personality
        "mbti" to { MbtiScreen() }, "bigfive" to { BigFiveScreen() }, "enneagram" to { EnneagramScreen() },
        "eq" to { EqScreen() }, "jung" to { JungScreen() }, "holland" to { HollandScreen() },
        "mentalage" to { MentalAgeScreen() }, "animal-personality" to { AnimalScreen() },
        "rpi" to { RpiScreen() }, "talent" to { TalentScreen() }, "values" to { ValuesScreen() },
        "personality-color" to { PersonalityColorScreen() },
        // Knowledge
        "trivia" to { TriviaScreen() }, "riddle" to { RiddleScreen() }, "idiom" to { IdiomScreen() },
        "science" to { ScienceScreen() }, "history" to { HistoryScreen() },
        "geography" to { GeographyScreen() }, "english-vocabulary" to { EnglishScreen() },
        "vocabulary" to { VocabularyScreen() }, "poetry" to { PoetryScreen() },
        "chemistry" to { ChemistryScreen() }, "sports" to { SportsScreen() },
        "animalfact" to { AnimalFactScreen() },
        // Sensory
        "vision" to { VisionScreen() }, "hearing" to { HearingScreen() }, "pitch" to { PitchScreen() },
        "rhythm" to { RhythmScreen() }, "color-sensitivity" to { ColorSensitivityScreen() },
        "angle" to { AngleScreen() }, "spot-difference" to { SpotDiffScreen() },
        "dynamic-vision" to { DynamicVisionScreen() },
        // Playground
        "minesweeper" to { MinesweeperScreen() }, "guess-number" to { com.mispec.luomashu.tools.GuessNumberScreen() },
        "chase-button" to { com.mispec.luomashu.tools.ChaseButtonScreen() }, "dont-press" to { DontPressScreen() },
        "circle-challenge" to { CircleChallengeScreen() }, "dnd-alignment" to { DndScreen() },
        "emoji-quiz" to { EmojiQuizScreen() },
    )

@Composable
fun ToolRouter(tool: ToolDef) {
    val screen = toolScreens[tool.id]
    if (screen != null) screen()
    else Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("即将上线", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }

}
