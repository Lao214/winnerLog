<template>
  <div class="space-y-5 sm:space-y-8">
    <!-- Hero Banner -->
    <div class="relative overflow-hidden rounded-2xl sm:rounded-3xl bg-gradient-to-br from-slate-900 via-slate-900 to-emerald-950/40 border border-slate-800 p-4 sm:p-8 shadow-2xl">
      <div class="absolute -right-10 -top-10 w-72 h-72 bg-emerald-500/10 rounded-full blur-3xl pointer-events-none"></div>
      <div class="absolute -left-10 -bottom-10 w-72 h-72 bg-indigo-500/10 rounded-full blur-3xl pointer-events-none"></div>

      <div class="relative z-10 flex flex-col md:flex-row md:items-center md:justify-between gap-4 sm:gap-6">
        <div>
          <div class="inline-flex items-center gap-1.5 px-2.5 py-0.5 sm:px-3 sm:py-1 rounded-full bg-emerald-500/10 border border-emerald-500/20 text-emerald-400 text-[11px] sm:text-xs font-bold mb-2 sm:mb-3">
            <span>🏆</span> 实时 AI 预测战绩榜
          </div>
          <h1 class="text-xl sm:text-3xl font-black tracking-tight text-white">
            足球 AI 胜率风云榜
          </h1>
          <p class="text-slate-400 text-xs sm:text-sm mt-1 sm:mt-2 max-w-xl leading-relaxed">
            追踪并对比 OpenAI GPT-4o、通义千问在胜负赛果、比分预测（最多3项）、总进球数（最多3项）的真实命中率与盈利走势。
          </p>
        </div>

        <!-- Filter & Sort Controls in Hero -->
        <div class="grid grid-cols-3 gap-2 bg-slate-950/70 p-2.5 sm:p-3 rounded-2xl border border-slate-800 w-full md:w-auto">
          <div>
            <label class="block text-[10px] sm:text-[11px] font-semibold text-slate-400 mb-1">选择赛季</label>
            <el-select v-model="selectedSeason" placeholder="赛季" class="!w-full md:!w-36" @change="fetchLeaderboard">
              <el-option label="2026-2027" value="2026-2027" />
              <el-option label="2024-2025" value="2024-2025" />
              <el-option label="2025-2026" value="2025-2026" />
              <el-option label="2026 世界杯" value="2026" />
            </el-select>
          </div>

          <div>
            <label class="block text-[10px] sm:text-[11px] font-semibold text-slate-400 mb-1">统计范围</label>
            <el-select v-model="selectedScope" placeholder="维度" class="!w-full md:!w-32" @change="handleScopeChange">
              <el-option label="赛季总榜" value="SEASON" />
              <el-option label="联赛赛季榜" value="LEAGUE_SEASON" />
              <el-option label="全局总榜" value="OVERALL" />
              <el-option label="联赛历史榜" value="LEAGUE" />
            </el-select>
          </div>

          <div>
            <label class="block text-[10px] sm:text-[11px] font-semibold text-slate-400 mb-1">排序榜单</label>
            <el-select v-model="selectedSort" placeholder="排序" class="!w-full md:!w-36" @change="fetchLeaderboard">
              <el-option label="💰 净赚倍数" value="netProfit" />
              <el-option label="📈 盈利率 ROI" value="roiRate" />
              <el-option label="🏆 胜负胜率" value="winRate" />
              <el-option label="🎯 比分命中率" value="scoreAccuracyRate" />
              <el-option label="⚽ 进球数命中率" value="goalsAccuracyRate" />
            </el-select>
          </div>
        </div>
      </div>
    </div>

    <!-- League Switcher Pills (Horizontal Scrolling on Mobile) -->
    <div v-if="selectedScope === 'LEAGUE_SEASON' || selectedScope === 'LEAGUE'" class="flex items-center gap-1.5 sm:gap-2 overflow-x-auto pb-1 scrollbar-none -mx-3 px-3 sm:mx-0 sm:px-0">
      <button
        v-for="league in leagues"
        :key="league.code"
        @click="selectLeague(league.code)"
        class="px-3 sm:px-4 py-1.5 sm:py-2 rounded-xl text-xs font-bold whitespace-nowrap transition-all flex items-center gap-1 shrink-0"
        :class="[
          selectedLeague === league.code
            ? 'bg-emerald-500 text-slate-950 shadow-lg shadow-emerald-500/25 font-extrabold scale-105'
            : 'bg-slate-900/80 hover:bg-slate-800 text-slate-300 border border-slate-800'
        ]"
      >
        <span>{{ league.shortName }}</span>
      </button>
    </div>

    <!-- Podium Top Models (Top 1, 2, 3 Highlight Cards) -->
    <div v-if="leaderboard.length > 0" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 sm:gap-6">
      <div
        v-for="(item, idx) in leaderboard.slice(0, 3)"
        :key="item.aiModelCode"
        class="glass-card rounded-2xl sm:rounded-3xl p-4 sm:p-6 border relative overflow-hidden flex flex-col justify-between"
        :class="[
          idx === 0
            ? 'border-amber-500/40 bg-gradient-to-b from-slate-900 via-slate-900 to-amber-950/20 shadow-glow-gold'
            : idx === 1
              ? 'border-indigo-500/40 bg-gradient-to-b from-slate-900 via-slate-900 to-indigo-950/20'
              : 'border-slate-800'
        ]"
      >
        <!-- Rank Badge Ribbon -->
        <div class="flex items-center justify-between">
          <div class="flex items-center gap-2">
            <span class="text-2xl">{{ idx === 0 ? '👑' : idx === 1 ? '🥈' : '🥉' }}</span>
            <span class="text-xs font-bold uppercase tracking-wider text-slate-400">
              Rank #{{ item.rank }}
            </span>
          </div>

          <!-- Streak Tag -->
          <div
            v-if="item.currentStreak > 0"
            class="px-2.5 py-0.5 rounded-full bg-amber-500/15 border border-amber-500/30 text-amber-400 font-extrabold text-[11px] flex items-center gap-1"
          >
            <span>🔥</span> {{ item.currentStreak }} 连红
          </div>
          <div
            v-else-if="item.currentStreak < 0"
            class="px-2.5 py-0.5 rounded-full bg-slate-800 text-slate-400 text-[11px]"
          >
            {{ Math.abs(item.currentStreak) }} 连黑
          </div>
        </div>

        <!-- AI Model Details & Profit Multiplier Stat -->
        <div class="my-3 sm:my-5">
          <div class="text-lg sm:text-xl font-extrabold text-white flex items-center gap-2">
            <span>{{ item.aiModelName || item.aiModelCode }}</span>
          </div>
          <p class="text-[11px] sm:text-xs text-slate-400 mt-0.5">{{ item.provider || 'AI Sports Predictor' }}</p>

          <!-- Profit Big Stat -->
          <div class="mt-3 sm:mt-4 p-3 sm:p-3.5 rounded-xl sm:rounded-2xl bg-slate-950/70 border border-slate-800 flex items-baseline justify-between">
            <div>
              <div class="text-[10px] sm:text-[11px] text-slate-400 font-semibold mb-0.5">💰 累计净赚倍数</div>
              <div
                class="text-2xl sm:text-3xl font-black font-mono tracking-tight"
                :class="Number(item.netProfit || 0) >= 0 ? 'text-emerald-400' : 'text-rose-400'"
              >
                {{ Number(item.netProfit || 0) >= 0 ? '+' : '' }}{{ item.netProfit || '0.00' }} 倍
              </div>
            </div>
            <div class="text-right">
              <div class="text-[10px] sm:text-[11px] text-slate-400 font-semibold mb-0.5">盈利率 ROI</div>
              <div
                class="text-base sm:text-lg font-black font-mono"
                :class="Number(item.roiRate || 0) >= 0 ? 'text-emerald-300' : 'text-rose-300'"
              >
                {{ Number(item.roiRate || 0) >= 0 ? '+' : '' }}{{ item.roiRate || '0.00' }}%
              </div>
            </div>
          </div>
        </div>

        <!-- Metrics Grid (4 items) -->
        <div class="grid grid-cols-2 gap-1.5 sm:gap-2 p-2.5 sm:p-3 rounded-xl sm:rounded-2xl bg-slate-950/50 border border-slate-800/80 text-center text-xs">
          <div class="p-1.5 rounded-lg bg-slate-900/60">
            <div class="text-slate-400 text-[10px]">胜负胜率</div>
            <div class="font-bold text-emerald-400 mt-0.5 text-[11px] sm:text-xs">
              {{ item.winRate }}%
              <span class="text-[9px] sm:text-[10px] text-slate-400 font-normal">({{ item.wonMatches }}红/{{ item.lostMatches }}黑)</span>
            </div>
          </div>
          <div class="p-1.5 rounded-lg bg-slate-900/60">
            <div class="text-slate-400 text-[10px]">比分命中率</div>
            <div class="font-bold text-indigo-300 mt-0.5 text-[11px] sm:text-xs">
              {{ item.scoreAccuracyRate }}%
              <span class="text-[9px] sm:text-[10px] text-slate-400 font-normal">({{ item.exactScoreMatches }}场)</span>
            </div>
          </div>
          <div class="p-1.5 rounded-lg bg-slate-900/60">
            <div class="text-slate-400 text-[10px]">进球数命中率</div>
            <div class="font-bold text-emerald-300 mt-0.5 text-[11px] sm:text-xs">
              {{ item.goalsAccuracyRate || '0.00' }}%
              <span class="text-[9px] sm:text-[10px] text-slate-400 font-normal">({{ item.goalsMatches || 0 }}场)</span>
            </div>
          </div>
          <div class="p-1.5 rounded-lg bg-slate-900/60">
            <div class="text-slate-400 text-[10px]">总结算场次</div>
            <div class="font-bold text-slate-200 mt-0.5 text-[11px] sm:text-xs">{{ item.settledMatches }} 场</div>
          </div>
        </div>

        <!-- Recent 5 Games Form -->
        <div class="mt-3 pt-2.5 sm:mt-4 sm:pt-3 border-t border-slate-800/80 flex items-center justify-between text-xs">
          <span class="text-slate-400 text-[10px] sm:text-[11px]">近5场胜负走势</span>
          <div class="flex items-center gap-1">
            <span
              v-for="(f, fIdx) in item.recentForm"
              :key="fIdx"
              class="w-4 h-4 sm:w-5 sm:h-5 rounded-full text-[9px] sm:text-[10px] font-bold flex items-center justify-center"
              :class="f === 'WON' ? 'bg-emerald-500 text-slate-950' : 'bg-slate-800 text-slate-400 border border-slate-700'"
            >
              {{ f === 'WON' ? '红' : '黑' }}
            </span>
          </div>
        </div>
      </div>
    </div>

    <!-- Detailed Leaderboard Section -->
    <div class="glass-panel rounded-2xl sm:rounded-3xl p-4 sm:p-6 border border-slate-800 shadow-xl">
      <div class="flex items-center justify-between mb-4 sm:mb-5">
        <div>
          <h2 class="text-base sm:text-lg font-bold text-white flex items-center gap-2">
            <span>📊</span> AI 预测模型战绩全览清单
          </h2>
          <p class="text-[11px] sm:text-xs text-slate-400 mt-0.5">按均注模型统计AI赚取本金倍数、投资回报率及各项命中率</p>
        </div>
      </div>

      <!-- Mobile Leaderboard Cards (Visible on screens < 768px) -->
      <div class="block md:hidden space-y-3">
        <div
          v-for="item in leaderboard"
          :key="item.aiModelCode"
          class="p-3.5 rounded-xl bg-slate-900/90 border border-slate-800/90 space-y-3"
        >
          <!-- Top Row: Rank, Model Name, Streak -->
          <div class="flex items-center justify-between">
            <div class="flex items-center gap-2.5">
              <span
                class="w-6 h-6 rounded-lg inline-flex items-center justify-center font-black text-xs"
                :class="[
                  item.rank === 1 ? 'bg-amber-500/20 text-amber-400 border border-amber-500/40' :
                  item.rank === 2 ? 'bg-indigo-500/20 text-indigo-400 border border-indigo-500/40' :
                  item.rank === 3 ? 'bg-slate-700 text-slate-300' : 'bg-slate-800 text-slate-400'
                ]"
              >
                {{ item.rank }}
              </span>
              <div>
                <div class="font-extrabold text-white text-sm">{{ item.aiModelName || item.aiModelCode }}</div>
                <div class="text-[10px] text-slate-400">{{ item.provider }}</div>
              </div>
            </div>

            <!-- Streak Badge -->
            <span
              v-if="item.currentStreak > 0"
              class="px-2 py-0.5 rounded-full text-[10px] font-bold bg-amber-500/20 text-amber-400 border border-amber-500/30"
            >
              🔥 {{ item.currentStreak }} 连红
            </span>
            <span
              v-else-if="item.currentStreak < 0"
              class="px-2 py-0.5 rounded-full text-[10px] bg-slate-800 text-slate-400"
            >
              {{ Math.abs(item.currentStreak) }} 连黑
            </span>
          </div>

          <!-- Profit & ROI Highlight Box -->
          <div class="p-2.5 rounded-lg bg-slate-950/80 border border-slate-800/80 flex items-center justify-between">
            <div>
              <div class="text-[10px] text-slate-400">💰 累计净赚倍数</div>
              <div
                class="text-lg font-black font-mono"
                :class="Number(item.netProfit || 0) >= 0 ? 'text-emerald-400' : 'text-rose-400'"
              >
                {{ Number(item.netProfit || 0) >= 0 ? '+' : '' }}{{ item.netProfit || '0.00' }} 倍
              </div>
            </div>
            <div class="text-right">
              <div class="text-[10px] text-slate-400">盈利率 ROI</div>
              <span
                class="px-2 py-0.5 rounded font-mono text-xs font-bold inline-block"
                :class="Number(item.roiRate || 0) >= 0 ? 'bg-emerald-500/15 text-emerald-400 border border-emerald-500/25' : 'bg-rose-500/15 text-rose-400 border border-rose-500/25'"
              >
                {{ Number(item.roiRate || 0) >= 0 ? '+' : '' }}{{ item.roiRate || '0.00' }}%
              </span>
            </div>
          </div>

          <!-- Win Rate Bar & Stats -->
          <div>
            <div class="flex items-center justify-between text-xs mb-1">
              <span class="text-slate-400 text-[11px]">胜负胜率</span>
              <span class="font-bold text-emerald-400 font-mono">{{ item.winRate }}% <span class="text-slate-400 font-normal">({{ item.wonMatches }}红/{{ item.lostMatches }}黑)</span></span>
            </div>
            <div class="w-full bg-slate-800 h-1.5 rounded-full overflow-hidden">
              <div class="bg-emerald-400 h-full rounded-full transition-all duration-500" :style="{ width: `${item.winRate}%` }"></div>
            </div>
          </div>

          <!-- Accuracy Sub-grid -->
          <div class="grid grid-cols-3 gap-2 pt-1 text-center text-xs">
            <div class="p-1.5 rounded-lg bg-slate-950/50 border border-slate-800/60">
              <div class="text-[10px] text-slate-400">🎯 比分命中</div>
              <div class="font-bold text-indigo-300 mt-0.5 text-[11px]">{{ item.scoreAccuracyRate }}%</div>
            </div>
            <div class="p-1.5 rounded-lg bg-slate-950/50 border border-slate-800/60">
              <div class="text-[10px] text-slate-400">⚽ 进球命中</div>
              <div class="font-bold text-emerald-300 mt-0.5 text-[11px]">{{ item.goalsAccuracyRate || '0.00' }}%</div>
            </div>
            <div class="p-1.5 rounded-lg bg-slate-950/50 border border-slate-800/60">
              <div class="text-[10px] text-slate-400">🏟️ 总结算</div>
              <div class="font-bold text-slate-200 mt-0.5 text-[11px]">{{ item.settledMatches }}场</div>
            </div>
          </div>

          <!-- Bottom: Recent Form -->
          <div class="pt-2 border-t border-slate-800/60 flex items-center justify-between text-xs">
            <span class="text-slate-400 text-[10px]">近5场走势</span>
            <div class="flex items-center gap-1">
              <span
                v-for="(f, fIdx) in item.recentForm"
                :key="fIdx"
                class="w-4 h-4 rounded-full text-[9px] font-bold flex items-center justify-center"
                :class="f === 'WON' ? 'bg-emerald-500 text-slate-950' : 'bg-slate-800 text-slate-400 border border-slate-700'"
              >
                {{ f === 'WON' ? '红' : '黑' }}
              </span>
            </div>
          </div>
        </div>
      </div>

      <!-- Desktop Table (Visible on screens >= 768px) -->
      <div class="hidden md:block overflow-x-auto">
        <table class="w-full text-left text-sm">
          <thead>
            <tr class="border-b border-slate-800 text-xs font-semibold text-slate-400">
              <th class="py-3 px-4">排名</th>
              <th class="py-3 px-4">AI 模型</th>
              <th class="py-3 px-4">💰 净赚倍数</th>
              <th class="py-3 px-4">盈利率 ROI</th>
              <th class="py-3 px-4">胜负胜率</th>
              <th class="py-3 px-4">比分命中率 (3选1)</th>
              <th class="py-3 px-4">进球数命中率 (3选1)</th>
              <th class="py-3 px-4">已结算场次</th>
              <th class="py-3 px-4">胜负红/黑</th>
              <th class="py-3 px-4">当前连红</th>
              <th class="py-3 px-4">近5场走势</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-slate-800/60">
            <tr v-for="item in leaderboard" :key="item.aiModelCode" class="hover:bg-slate-800/30 transition-colors">
              <td class="py-4 px-4 font-bold text-slate-300">
                <span
                  class="w-7 h-7 rounded-lg inline-flex items-center justify-center font-extrabold text-xs"
                  :class="[
                    item.rank === 1 ? 'bg-amber-500/20 text-amber-400 border border-amber-500/30' :
                    item.rank === 2 ? 'bg-indigo-500/20 text-indigo-400 border border-indigo-500/30' :
                    item.rank === 3 ? 'bg-slate-700 text-slate-300' : 'text-slate-500'
                  ]"
                >
                  {{ item.rank }}
                </span>
              </td>
              <td class="py-4 px-4">
                <div class="font-bold text-white">{{ item.aiModelName || item.aiModelCode }}</div>
                <div class="text-[11px] text-slate-400">{{ item.provider }}</div>
              </td>
              <td class="py-4 px-4">
                <div
                  class="text-base font-black font-mono"
                  :class="Number(item.netProfit || 0) >= 0 ? 'text-emerald-400' : 'text-rose-400'"
                >
                  {{ Number(item.netProfit || 0) >= 0 ? '+' : '' }}{{ item.netProfit || '0.00' }} 倍
                </div>
                <div class="text-[10px] text-slate-500">
                  投{{ item.totalStake || 0 }} 返{{ item.totalReturn || 0 }}
                </div>
              </td>
              <td class="py-4 px-4">
                <span
                  class="px-2 py-0.5 rounded font-mono text-xs font-bold"
                  :class="Number(item.roiRate || 0) >= 0 ? 'bg-emerald-500/10 text-emerald-400 border border-emerald-500/20' : 'bg-rose-500/10 text-rose-400 border border-rose-500/20'"
                >
                  {{ Number(item.roiRate || 0) >= 0 ? '+' : '' }}{{ item.roiRate || '0.00' }}%
                </span>
              </td>
              <td class="py-4 px-4">
                <div class="text-sm font-black font-mono text-slate-200">{{ item.winRate }}%</div>
                <div class="w-16 bg-slate-800 h-1.5 rounded-full overflow-hidden mt-1">
                  <div class="bg-emerald-400 h-full rounded-full" :style="{ width: `${item.winRate}%` }"></div>
                </div>
              </td>
              <td class="py-4 px-4 font-semibold text-indigo-300">
                {{ item.scoreAccuracyRate }}%
                <span class="text-[11px] text-slate-400">({{ item.exactScoreMatches }}场)</span>
              </td>
              <td class="py-4 px-4 font-semibold text-emerald-300">
                {{ item.goalsAccuracyRate || '0.00' }}%
                <span class="text-[11px] text-slate-400">({{ item.goalsMatches || 0 }}场)</span>
              </td>
              <td class="py-4 px-4 font-semibold text-slate-200">{{ item.settledMatches }} 场</td>
              <td class="py-4 px-4 font-bold">
                <span class="text-emerald-400">{{ item.wonMatches }}</span> / <span class="text-rose-400">{{ item.lostMatches }}</span>
              </td>
              <td class="py-4 px-4">
                <span
                  v-if="item.currentStreak > 0"
                  class="px-2 py-0.5 rounded text-xs font-bold bg-amber-500/20 text-amber-400 border border-amber-500/30"
                >
                  🔥 {{ item.currentStreak }} 连红
                </span>
                <span
                  v-else-if="item.currentStreak < 0"
                  class="px-2 py-0.5 rounded text-xs bg-slate-800 text-slate-400"
                >
                  {{ Math.abs(item.currentStreak) }} 连黑
                </span>
                <span v-else class="text-slate-500">-</span>
              </td>
              <td class="py-4 px-4">
                <div class="flex items-center gap-1">
                  <span
                    v-for="(f, fIdx) in item.recentForm"
                    :key="fIdx"
                    class="w-4 h-4 rounded-full text-[9px] font-bold flex items-center justify-center"
                    :class="f === 'WON' ? 'bg-emerald-500 text-slate-950' : 'bg-slate-800 text-slate-400 border border-slate-700'"
                  >
                    {{ f === 'WON' ? '红' : '黑' }}
                  </span>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- Empty State -->
      <div v-if="leaderboard.length === 0" class="py-12 sm:py-16 text-center">
        <div class="text-4xl mb-3">⚽</div>
        <div class="text-slate-300 font-bold">该维度暂无已结算的预测数据</div>
        <p class="text-slate-500 text-xs mt-1">请先前往“录入工作台”导入比赛预测并结算完场比分</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '@/api'

const selectedSeason = ref('2026-2027')
const selectedScope = ref('SEASON')
const selectedLeague = ref('PL')
const selectedSort = ref('netProfit')

const leagues = ref([
  { code: 'PL', shortName: '英超' },
  { code: 'LA_LIGA', shortName: '西甲' },
  { code: 'SERIE_A', shortName: '意甲' },
  { code: 'BUNDESLIGA', shortName: '德甲' },
  { code: 'LIGUE_1', shortName: '法甲' },
  { code: 'UCL', shortName: '欧冠' },
  { code: 'WORLD_CUP', shortName: '世界杯' },
  { code: 'EURO', shortName: '欧洲杯' }
])

const leaderboard = ref([])
const loading = ref(false)

const handleScopeChange = () => {
  if (selectedScope.value === 'LEAGUE_SEASON' && !selectedLeague.value) {
    selectedLeague.value = 'PL'
  }
  fetchLeaderboard()
}

const selectLeague = (code) => {
  selectedLeague.value = code
  fetchLeaderboard()
}

const fetchLeaderboard = async () => {
  loading.value = true
  try {
    const params = {
      scopeType: selectedScope.value,
      seasonName: selectedScope.value.includes('SEASON') ? selectedSeason.value : undefined,
      leagueCode: (selectedScope.value === 'LEAGUE_SEASON' || selectedScope.value === 'LEAGUE') ? selectedLeague.value : undefined,
      sortBy: selectedSort.value,
      sortOrder: 'desc'
    }
    const data = await api.getLeaderboard(params)
    leaderboard.value = data || []
  } catch (err) {
    console.error('Failed to fetch leaderboard', err)
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  try {
    const leagueList = await api.getLeagues()
    if (leagueList && leagueList.length > 0) {
      leagues.value = leagueList
    }
  } catch (e) {}
  fetchLeaderboard()
})
</script>
