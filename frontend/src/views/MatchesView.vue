<template>
    <div class="space-y-6 pb-12">
        <!-- Header & Filter Toolbar -->
        <div class="glass-panel rounded-3xl p-4 sm:p-6 border border-slate-800 shadow-xl">
            <div class="flex flex-col sm:flex-row sm:items-center justify-between gap-4 pb-5 border-b border-slate-800">
                <div>
                    <h1 class="text-xl sm:text-2xl font-black text-white flex items-center gap-2">
                        <span>⚽</span> 足球比赛大厅与 AI 预测并排看板
                    </h1>
                    <p class="text-xs text-slate-400 mt-1">
                        浏览各联赛比赛档案，直观并排对比 OpenAI GPT-4o 与 通义千问 的赛前预测与赛后结算红黑单。
                    </p>
                </div>

                <router-link to="/admin"
                    class="inline-flex items-center justify-center gap-2 px-4 py-2.5 rounded-xl bg-emerald-600 hover:bg-emerald-500 text-white text-xs font-bold transition shadow-lg shadow-emerald-600/25 w-full sm:w-auto">
                    <span>➕</span> 导入新预测
                </router-link>
            </div>

            <!-- Filters Grid -->
            <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-2.5 sm:gap-3.5 mt-4 sm:mt-5">
                <div>
                    <label class="block text-[11px] font-semibold text-slate-400 mb-1">选择联赛</label>
                    <el-select v-model="query.leagueCode" placeholder="全部联赛" clearable class="w-full"
                        @change="fetchMatches">
                        <el-option label="全部赛事" value="" />
                        <el-option v-for="l in leagues" :key="l.code" :label="l.shortName" :value="l.code" />
                    </el-select>
                </div>

                <div>
                    <label class="block text-[11px] font-semibold text-slate-400 mb-1">选择赛季</label>
                    <el-select v-model="query.seasonName" placeholder="全部赛季" clearable class="w-full"
                        @change="fetchMatches">
                        <el-option label="2026-2027" value="2026-2027" />
                        <el-option label="2024-2025" value="2024-2025" />
                        <el-option label="2025-2026" value="2025-2026" />
                    </el-select>
                </div>

                <div>
                    <label class="block text-[11px] font-semibold text-slate-400 mb-1">比赛状态</label>
                    <el-select v-model="query.status" placeholder="全部状态" clearable class="w-full"
                        @change="fetchMatches">
                        <el-option label="全部状态" value="" />
                        <el-option label="未开赛 (待结算)" value="SCHEDULED" />
                        <el-option label="进行中" value="IN_PROGRESS" />
                        <el-option label="已完赛 (已结算)" value="FINISHED" />
                    </el-select>
                </div>

                <div>
                    <label class="block text-[11px] font-semibold text-slate-400 mb-1">球队搜索</label>
                    <el-autocomplete
                        v-model="query.teamName"
                        :fetch-suggestions="querySearchTeam"
                        placeholder="输入球队名 (如 阿森纳 / 皇马)"
                        clearable
                        class="w-full"
                        @select="handleSelectTeam"
                        @keyup.enter="fetchMatches"
                        @clear="fetchMatches"
                    >
                        <template #default="{ item }">
                            <div class="flex items-center gap-2 py-1">
                                <TeamLogo :name="item.team.nameZh" size="xs" :badge="false" />
                                <span class="font-bold text-slate-100 text-xs">{{ item.team.nameZh }}</span>
                                <span class="text-[10px] text-slate-400 font-mono">{{ item.team.nameEn }}</span>
                                <span class="ml-auto text-[10px] px-1.5 py-0.2 rounded bg-slate-800 text-emerald-400">{{ item.team.leagueNameZh }}</span>
                            </div>
                        </template>
                        <template #append>
                            <el-button @click="fetchMatches">🔍</el-button>
                        </template>
                    </el-autocomplete>
                </div>
            </div>
        </div>

        <!-- Match Cards Grid -->
        <div v-if="loading" class="py-20 text-center text-slate-400">
            <div class="text-3xl animate-spin mb-2">⚽</div>
            <p class="text-xs">加载比赛数据中...</p>
        </div>

        <div v-else-if="matches.length > 0" class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-4 sm:gap-6">
            <MatchCard v-for="match in matches" :key="match.id" :match="match" @view-analysis="handleViewAnalysis"
                @settle-match="handleOpenSettle" />
        </div>

        <!-- Empty State -->
        <div v-else class="glass-panel rounded-3xl p-8 sm:p-16 text-center border border-slate-800">
            <div class="text-4xl sm:text-5xl mb-3">🏟️</div>
            <div class="text-slate-200 text-base sm:text-lg font-bold">暂无匹配的足球比赛档案</div>
            <p class="text-slate-400 text-xs mt-1 max-w-sm mx-auto">
                没有检索到符合条件的比赛，您可以尝试清除筛选条件，或直接前往录入工作台导入新比赛。
            </p>
            <router-link to="/admin"
                class="mt-5 inline-flex items-center gap-2 px-4 py-2 rounded-xl bg-emerald-600 text-white text-xs font-bold hover:bg-emerald-500 transition">
                <span>📥</span> 立即导入第一场比赛
            </router-link>
        </div>

        <!-- AI Analysis Detail Drawer -->
        <el-drawer v-model="drawerVisible"
            :title="currentPrediction ? `${currentPrediction.aiModelName || currentPrediction.aiModelCode} 战术推演分析报告` : 'AI 预测分析'"
            :direction="isMobile ? 'btt' : 'rtl'" :size="isMobile ? '88%' : '480px'" class="custom-mobile-drawer">
            <div v-if="currentPrediction" class="space-y-4 sm:space-y-5 text-sm pb-6">
                <!-- Model Header Tag -->
                <div class="p-3.5 sm:p-4 rounded-2xl bg-slate-900 border border-slate-800 flex items-center justify-between">
                    <div>
                        <div class="text-sm sm:text-base font-extrabold text-white">{{ currentPrediction.aiModelName }}</div>
                        <div class="text-xs text-slate-400 mt-0.5">预测方向: <span class="font-bold text-emerald-400">{{
                                currentPrediction.predictedResultDesc }}</span></div>
                    </div>
                    <div class="text-right">
                        <div class="text-xs text-slate-400">推荐比分</div>
                        <div class="text-base sm:text-lg font-bold font-mono text-white">{{ currentPrediction.predictedScoreStr ||
                            '未给出' }}
                        </div>
                    </div>
                </div>

                <!-- Confidence & Odds -->
                <div class="grid grid-cols-2 gap-2.5 sm:gap-3">
                    <div class="p-3 rounded-xl bg-slate-900/60 border border-slate-800 text-center">
                        <div class="text-[11px] text-slate-400">AI 置信度</div>
                        <div class="text-sm sm:text-base font-extrabold text-emerald-400 mt-0.5">{{ currentPrediction.confidence ?
                            `${currentPrediction.confidence}%` : '未评估' }}</div>
                    </div>
                    <div class="p-3 rounded-xl bg-slate-900/60 border border-slate-800 text-center">
                        <div class="text-[11px] text-slate-400">预测参考赔率</div>
                        <div class="text-sm sm:text-base font-extrabold text-indigo-300 mt-0.5">{{ currentPrediction.predictedOdds
                            ||
                            '1.85' }}</div>
                    </div>
                </div>

                <!-- Summary -->
                <div v-if="currentPrediction.analysisSummary"
                    class="p-3.5 sm:p-4 rounded-2xl bg-slate-900/40 border border-slate-800">
                    <div class="text-xs font-bold text-slate-300 mb-1.5 flex items-center gap-1.5">
                        <span>💡</span> 核心论据摘要
                    </div>
                    <p class="text-xs text-slate-300 leading-relaxed">{{ currentPrediction.analysisSummary }}</p>
                </div>

                <!-- Full Detailed Analysis -->
                <div v-if="currentPrediction.detailedAnalysis"
                    class="p-3.5 sm:p-4 rounded-2xl bg-slate-900/40 border border-slate-800">
                    <div class="text-xs font-bold text-slate-300 mb-1.5 flex items-center gap-1.5">
                        <span>📝</span> 完整技战术与阵容分析
                    </div>
                    <p class="text-xs text-slate-300 leading-relaxed whitespace-pre-line">{{
                        currentPrediction.detailedAnalysis
                        }}</p>
                </div>
            </div>
        </el-drawer>

        <!-- Settle Match Modal -->
        <SettleModal v-model="settleModalVisible" :match="selectedMatchForSettle" @settled="handleSettled" />
    </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import api from '@/api'
import MatchCard from '@/components/MatchCard.vue'
import SettleModal from '@/components/SettleModal.vue'
import TeamLogo from '@/components/TeamLogo.vue'
import { searchTeams } from '@/utils/teamLogos'

const leagues = ref([])
const matches = ref([])
const loading = ref(false)

const isMobile = ref(typeof window !== 'undefined' ? window.innerWidth < 640 : false)

const handleResize = () => {
    isMobile.value = window.innerWidth < 640
}

const query = reactive({
    leagueCode: '',
    seasonName: '2026-2027',
    status: '',
    teamName: '',
    page: 0,
    size: 30
})

const drawerVisible = ref(false)
const currentPrediction = ref(null)

const settleModalVisible = ref(false)
const selectedMatchForSettle = ref(null)

const querySearchTeam = (queryString, cb) => {
    const results = searchTeams(queryString, query.leagueCode, 10)
    cb(results.map(t => ({ value: t.nameZh, team: t })))
}

const handleSelectTeam = (item) => {
    query.teamName = item.value
    fetchMatches()
}

const fetchMatches = async () => {
    loading.value = true
    try {
        const params = {
            leagueCode: query.leagueCode || undefined,
            seasonName: query.seasonName || undefined,
            status: query.status || undefined,
            teamName: query.teamName || undefined,
            page: query.page,
            size: query.size
        }
        const pageData = await api.queryMatches(params)
        matches.value = pageData.content || []
    } catch (err) {
        console.error('Failed to query matches', err)
    } finally {
        loading.value = false
    }
}

const handleViewAnalysis = (prediction) => {
    currentPrediction.value = prediction
    drawerVisible.value = true
}

const handleOpenSettle = (match) => {
    selectedMatchForSettle.value = match
    settleModalVisible.value = true
}

const handleSettled = () => {
    fetchMatches()
}

onMounted(async () => {
    window.addEventListener('resize', handleResize)
    try {
        const leagueList = await api.getLeagues()
        if (leagueList) leagues.value = leagueList
    } catch (e) { }
    fetchMatches()
})

onUnmounted(() => {
    window.removeEventListener('resize', handleResize)
})
</script>
