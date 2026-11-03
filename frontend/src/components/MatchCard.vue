<template>
  <div class="glass-card rounded-2xl p-3.5 sm:p-5 border border-slate-800 hover:border-slate-700/80 transition-all shadow-lg flex flex-col justify-between">
    <!-- Header: League, Round, Status -->
    <div class="flex items-center justify-between text-[11px] sm:text-xs pb-2.5 sm:pb-3 border-b border-slate-800/80">
      <div class="flex items-center gap-1.5 sm:gap-2">
        <span class="px-2 py-0.5 rounded-md font-bold bg-slate-800 text-emerald-400 border border-slate-700">
          {{ match.leagueName || match.leagueCode }}
        </span>
        <span class="text-slate-400 text-[10px] sm:text-xs" v-if="match.roundName">{{ match.roundName }}</span>
        <span class="text-slate-500 text-[10px]">•</span>
        <span class="text-slate-400 text-[10px] sm:text-xs">{{ formatTime(match.matchTime) }}</span>
      </div>

      <!-- Status Tag -->
      <div>
        <span
          v-if="match.status === 'FINISHED'"
          class="px-2 py-0.5 rounded-full font-semibold text-[10px] sm:text-[11px] bg-slate-800 text-slate-300 border border-slate-700"
        >
          已完赛
        </span>
        <span
          v-else-if="match.status === 'IN_PROGRESS'"
          class="px-2 py-0.5 rounded-full font-semibold text-[10px] sm:text-[11px] bg-amber-500/20 text-amber-400 border border-amber-500/30 animate-pulse"
        >
          进行中
        </span>
        <span
          v-else
          class="px-2 py-0.5 rounded-full font-semibold text-[10px] sm:text-[11px] bg-blue-500/20 text-blue-400 border border-blue-500/30"
        >
          未开赛
        </span>
      </div>
    </div>

    <!-- Match Teams & Score Area -->
    <div class="py-3 sm:py-4 flex items-center justify-between">
      <!-- Home Team -->
      <div class="flex-1 flex flex-col items-center text-center min-w-0">
        <TeamLogo :name="match.homeTeam" :league-code="match.leagueCode" size="md" />
        <span class="mt-1.5 sm:mt-2 text-xs sm:text-sm font-extrabold text-slate-100 truncate w-full px-1" :title="match.homeTeam">{{ match.homeTeam }}</span>
        <span class="text-[10px] sm:text-[11px] text-slate-500">主队</span>
      </div>

      <!-- Score / VS Center Area -->
      <div class="px-2 sm:px-4 flex flex-col items-center shrink-0">
        <div v-if="match.status === 'FINISHED'" class="text-center">
          <div class="text-xl sm:text-3xl font-black tracking-wider text-emerald-400 font-mono">
            {{ match.homeScore }} : {{ match.awayScore }}
          </div>
          <div class="flex items-center gap-1 sm:gap-1.5 mt-0.5 sm:mt-1">
            <span class="text-[10px] sm:text-[11px] font-semibold text-emerald-400 px-1.5 py-0.2 rounded bg-emerald-500/10">
              {{ match.resultDesc }}
            </span>
            <span class="text-[10px] sm:text-[11px] font-semibold text-slate-400 px-1.5 py-0.2 rounded bg-slate-800">
              进球: {{ (match.homeScore ?? 0) + (match.awayScore ?? 0) }}
            </span>
          </div>
        </div>
        <div v-else class="text-center">
          <div class="text-lg sm:text-xl font-extrabold text-slate-500">VS</div>
          <span class="text-[10px] text-slate-400 mt-0.5">{{ match.venue || '待定' }}</span>
        </div>
      </div>

      <!-- Away Team -->
      <div class="flex-1 flex flex-col items-center text-center min-w-0">
        <TeamLogo :name="match.awayTeam" :league-code="match.leagueCode" size="md" />
        <span class="mt-1.5 sm:mt-2 text-xs sm:text-sm font-extrabold text-slate-100 truncate w-full px-1" :title="match.awayTeam">{{ match.awayTeam }}</span>
        <span class="text-[10px] sm:text-[11px] text-slate-500">客队</span>
      </div>
    </div>

    <!-- AI Predictions Section -->
    <div class="pt-2.5 sm:pt-3 border-t border-slate-800/80">
      <div class="text-[10px] sm:text-[11px] font-bold text-slate-400 uppercase tracking-wider mb-2 flex items-center justify-between">
        <span class="truncate">🤖 AI 预测 (胜负+3比分+3进球)</span>
        <span v-if="isAgreed" class="text-emerald-400 font-normal shrink-0 ml-1">🤝 观点一致</span>
        <span v-else-if="match.predictions?.length >= 2" class="text-amber-400 font-normal shrink-0 ml-1">⚡ 巅峰分歧</span>
      </div>

      <div class="grid grid-cols-1 sm:grid-cols-2 gap-2.5 sm:gap-3">
        <div
          v-for="pred in match.predictions"
          :key="pred.id || pred.aiModelCode"
          class="p-2.5 sm:p-3.5 rounded-xl border transition-all text-xs flex flex-col justify-between"
          :class="[
            pred.hitImportance === 1
              ? 'bg-gradient-to-b from-amber-950/35 via-purple-950/25 to-slate-900/90 border-amber-500/40 shadow-lg shadow-amber-500/10 ring-1 ring-amber-500/30'
              : pred.hitImportance === 2
                ? 'bg-indigo-950/30 border-indigo-500/40 shadow-md shadow-indigo-500/10'
                : pred.hitImportance === 3
                  ? 'bg-emerald-950/20 border-emerald-500/30'
                  : pred.aiModelCode === 'GPT'
                    ? 'bg-emerald-950/10 border-emerald-500/20'
                    : 'bg-indigo-950/10 border-indigo-500/20'
          ]"
        >
          <!-- Model Header & Tier Badge -->
          <div class="flex flex-wrap items-center justify-between gap-1 pb-1.5 border-b border-slate-800/60">
            <div class="flex items-center gap-1.5">
              <span class="w-2 h-2 rounded-full shrink-0" :class="pred.aiModelCode === 'GPT' ? 'bg-emerald-400 shadow-glow-gpt' : 'bg-indigo-400 shadow-glow-qwen'"></span>
              <span class="font-bold text-slate-200 text-xs">{{ pred.aiModelName || pred.aiModelCode }}</span>
              <span class="text-[9px] px-1.5 py-0.2 rounded bg-slate-800/80 text-slate-400 border border-slate-700/60 font-mono">
                {{ pred.optionsCount || 7 }}项(初{{ pred.initialProfit ?? ('-' + (pred.optionsCount || 7)) }})
              </span>
            </div>

            <!-- Importance / Hit Tier Badges -->
            <div v-if="pred.settleStatus && pred.settleStatus !== 'PENDING'" class="flex items-center gap-1">
              <!-- Tier 1: 比分命中 (最重要) -->
              <span
                v-if="pred.hitImportance === 1"
                class="px-2 py-0.5 rounded-full text-[9px] sm:text-[10px] font-black bg-gradient-to-r from-amber-500/30 to-purple-500/30 text-amber-300 border border-amber-400/50 shadow-sm shadow-amber-500/30"
              >
                {{ pred.hitTierDesc || '🎯 精准比分' }}
              </span>
              <!-- Tier 2: 进球数命中 (次重要) -->
              <span
                v-else-if="pred.hitImportance === 2"
                class="px-2 py-0.5 rounded-full text-[9px] sm:text-[10px] font-extrabold bg-indigo-500/25 text-indigo-300 border border-indigo-500/40"
              >
                {{ pred.hitTierDesc || '⚽ 进球数命中' }}
              </span>
              <!-- Tier 3: 仅胜负命中 (基础) -->
              <span
                v-else-if="pred.hitImportance === 3"
                class="px-2 py-0.5 rounded-full text-[9px] sm:text-[10px] font-bold bg-emerald-500/20 text-emerald-400 border border-emerald-500/30"
              >
                {{ pred.hitTierDesc || '🚩 胜负命中' }}
              </span>
              <!-- Tier 0: 全未中 -->
              <span
                v-else
                class="px-2 py-0.5 rounded-full text-[9px] sm:text-[10px] font-bold bg-rose-500/10 text-rose-400 border border-rose-500/20"
              >
                ⚫ 未命中
              </span>
            </div>
            <span v-else class="text-[9px] text-slate-500 font-mono">待开赛</span>
          </div>

          <!-- Predicted Results (胜平负) -->
          <div class="my-1.5 space-y-1">
            <div class="text-[10px] text-slate-400 flex items-center justify-between">
              <span>预测胜平负:</span>
              <span v-if="pred.confidence" class="text-[9px] text-slate-400">置信: {{ pred.confidence }}%</span>
            </div>
            <div class="flex flex-wrap gap-1">
              <template v-if="pred.resultItems && pred.resultItems.length > 0">
                <span
                  v-for="(rItem, rIdx) in pred.resultItems"
                  :key="rIdx"
                  class="px-1.5 sm:px-2 py-0.5 rounded font-mono text-[10px] sm:text-[11px] font-bold flex items-center gap-1"
                  :class="[
                    match.status === 'FINISHED' && match.result === rItem.result
                      ? 'bg-emerald-500 text-slate-950 font-black shadow-sm shadow-emerald-500/40 ring-1 ring-emerald-300'
                      : 'bg-slate-800/90 text-slate-200 border border-slate-700/80'
                  ]"
                >
                  <span>{{ formatResultDesc(rItem.result) }}</span>
                  <span class="text-[9px] sm:text-[10px] opacity-85 font-mono">@{{ rItem.odds }}</span>
                  <span v-if="match.status === 'FINISHED' && match.result === rItem.result">✓</span>
                </span>
              </template>
              <template v-else>
                <span
                  class="px-1.5 sm:px-2 py-0.5 rounded font-mono text-[10px] sm:text-[11px] font-bold flex items-center gap-1"
                  :class="[
                    match.status === 'FINISHED' && match.result === pred.predictedResult
                      ? 'bg-emerald-500 text-slate-950 font-black ring-1 ring-emerald-300'
                      : 'bg-slate-800/90 text-slate-200 border border-slate-700/80'
                  ]"
                >
                  <span>{{ pred.predictedResultDesc || formatResultDesc(pred.predictedResult) }}</span>
                  <span class="text-[9px] sm:text-[10px] opacity-85 font-mono">@{{ pred.resultOdds || pred.predictedOdds || '1.85' }}</span>
                  <span v-if="match.status === 'FINISHED' && match.result === pred.predictedResult">✓</span>
                </span>
              </template>
            </div>
          </div>

          <!-- Predicted Scores (比分) -->
          <div class="mb-1.5">
            <div class="text-[10px] text-slate-400 mb-0.5 flex items-center justify-between">
              <span>预测比分 (最多3项):</span>
              <span v-if="pred.hitScore" class="text-amber-400 font-bold text-[9px] sm:text-[10px]">🎯 命中比分</span>
            </div>
            <div class="flex flex-wrap gap-1">
              <template v-if="pred.scoreItems && pred.scoreItems.length > 0">
                <span
                  v-for="(item, sIdx) in pred.scoreItems"
                  :key="sIdx"
                  class="px-1.5 py-0.5 rounded font-mono text-[10px] sm:text-[11px] font-bold flex items-center gap-0.5"
                  :class="[
                    pred.hitScore === item.score
                      ? 'bg-gradient-to-r from-amber-400 to-amber-300 text-slate-950 font-black shadow-md shadow-amber-500/40 ring-2 ring-amber-300 animate-pulse'
                      : 'bg-slate-800/90 text-slate-200 border border-slate-700/80'
                  ]"
                >
                  <span>{{ item.score }}</span>
                  <span class="text-[9px] opacity-85 font-normal">@{{ item.odds || '7.00' }}</span>
                  <span v-if="pred.hitScore === item.score">🎯</span>
                </span>
              </template>
              <template v-else-if="pred.predictedScores && pred.predictedScores.length > 0">
                <span
                  v-for="s in pred.predictedScores"
                  :key="s"
                  class="px-1.5 py-0.5 rounded font-mono text-[10px] sm:text-[11px] font-bold"
                  :class="[
                    pred.hitScore === s
                      ? 'bg-gradient-to-r from-amber-400 to-amber-300 text-slate-950 font-black shadow-md shadow-amber-500/40 ring-2 ring-amber-300'
                      : 'bg-slate-800/90 text-slate-200 border border-slate-700/80'
                  ]"
                >
                  {{ s }} <span v-if="pred.hitScore === s">🎯</span>
                </span>
              </template>
              <span v-else class="text-slate-500 text-[10px]">未提供</span>
            </div>
          </div>

          <!-- Predicted Goals (进球数) -->
          <div class="mb-2">
            <div class="text-[10px] text-slate-400 mb-0.5 flex items-center justify-between">
              <span>预测总进球数 (最多3项):</span>
              <span v-if="pred.hitGoals !== null && pred.hitGoals !== undefined" class="text-indigo-400 font-bold text-[9px] sm:text-[10px]">⚽ 命中进球</span>
            </div>
            <div class="flex flex-wrap gap-1">
              <template v-if="pred.goalItems && pred.goalItems.length > 0">
                <span
                  v-for="(item, gIdx) in pred.goalItems"
                  :key="gIdx"
                  class="px-1.5 py-0.5 rounded text-[10px] sm:text-[11px] font-bold flex items-center gap-0.5"
                  :class="[
                    pred.hitGoals === item.goals
                      ? 'bg-indigo-500 text-white font-black shadow-md shadow-indigo-500/40 ring-2 ring-indigo-300'
                      : 'bg-slate-800/90 text-slate-300 border border-slate-700/80'
                  ]"
                >
                  <span>{{ item.goals }}球</span>
                  <span class="text-[9px] opacity-85 font-normal">@{{ item.odds || '3.50' }}</span>
                  <span v-if="pred.hitGoals === item.goals">⚽</span>
                </span>
              </template>
              <template v-else-if="pred.predictedGoals && pred.predictedGoals.length > 0">
                <span
                  v-for="g in pred.predictedGoals"
                  :key="g"
                  class="px-1.5 py-0.5 rounded text-[10px] sm:text-[11px] font-bold"
                  :class="[
                    pred.hitGoals === g
                      ? 'bg-indigo-500 text-white font-black shadow-md shadow-indigo-500/40 ring-2 ring-indigo-300'
                      : 'bg-slate-800/90 text-slate-300 border border-slate-700/80'
                  ]"
                >
                  {{ g }} 球 <span v-if="pred.hitGoals === g">⚽</span>
                </span>
              </template>
              <span v-else class="text-slate-500 text-[10px]">未提供</span>
            </div>
          </div>

          <!-- Profit Details & Analysis Trigger -->
          <div class="flex flex-wrap items-center justify-between gap-1 pt-1.5 text-[10px] sm:text-[11px] text-slate-400 border-t border-slate-800/60">
            <div v-if="pred.settleStatus && pred.settleStatus !== 'PENDING' && pred.netProfit !== undefined && pred.netProfit !== null" class="flex flex-wrap items-center gap-1">
              <span
                class="px-1.5 sm:px-2 py-0.5 rounded font-mono font-black text-[10px] sm:text-[11px] flex items-center"
                :class="[
                  Number(pred.netProfit) > 0
                    ? 'bg-emerald-500/20 text-emerald-400 border border-emerald-500/40'
                    : Number(pred.netProfit) === 0
                      ? 'bg-slate-800 text-slate-300 border border-slate-700'
                      : 'bg-rose-500/20 text-rose-400 border border-rose-500/40'
                ]"
              >
                {{ Number(pred.netProfit) > 0 ? '💰 利润 +' : Number(pred.netProfit) === 0 ? '⚖️ 保本 ' : '💸 亏损 ' }}{{ pred.netProfit }}
              </span>
              <span class="text-[9px] text-slate-400 font-mono">
                (初{{ pred.initialProfit ?? ('-' + (pred.optionsCount || 7)) }}|返{{ pred.returnAmount }})
              </span>
            </div>
            <span v-else class="text-[9px] sm:text-[10px] text-slate-500 font-mono">
              初始: {{ pred.initialProfit ?? ('-' + (pred.optionsCount || 7)) }} (待开赛)
            </span>

            <button
              v-if="pred.detailedAnalysis || pred.analysisSummary"
              @click="$emit('view-analysis', pred)"
              class="text-[10px] sm:text-[11px] font-semibold text-emerald-400 hover:text-emerald-300 underline underline-offset-2 transition ml-auto"
            >
              战术理由 ➔
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Card Footer Actions -->
    <div class="mt-3 sm:mt-4 pt-2.5 sm:pt-3 border-t border-slate-800/80 flex items-center justify-between text-xs">
      <span class="text-slate-500 text-[10px] sm:text-xs">赛季: {{ match.seasonName }}</span>
      <button
        v-if="match.status !== 'FINISHED'"
        @click="$emit('settle-match', match)"
        class="px-2.5 sm:px-3 py-1 sm:py-1.5 rounded-lg bg-emerald-600 hover:bg-emerald-500 text-white text-[11px] sm:text-xs font-semibold transition flex items-center gap-1 shadow-sm shadow-emerald-600/30"
      >
        <span>⚡</span> 录入完场比分
      </button>
      <button
        v-else
        @click="$emit('settle-match', match)"
        class="text-[11px] sm:text-xs text-slate-400 hover:text-slate-200 underline transition"
      >
        修正比分
      </button>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import TeamLogo from '@/components/TeamLogo.vue'

const props = defineProps({
  match: {
    type: Object,
    required: true
  }
})

defineEmits(['view-analysis', 'settle-match'])

const isAgreed = computed(() => {
  const preds = props.match?.predictions || []
  if (preds.length < 2) return false
  return preds.every(p => p.predictedResult === preds[0].predictedResult)
})

const formatTime = (timeStr) => {
  if (!timeStr) return ''
  const d = new Date(timeStr)
  const month = (d.getMonth() + 1).toString().padStart(2, '0')
  const day = d.getDate().toString().padStart(2, '0')
  const hour = d.getHours().toString().padStart(2, '0')
  const min = d.getMinutes().toString().padStart(2, '0')
  return `${month}-${day} ${hour}:${min}`
}

const formatResultDesc = (res) => {
  if (!res) return ''
  switch (res) {
    case 'HOME_WIN': return '主胜'
    case 'DRAW': return '平局'
    case 'AWAY_WIN': return '客胜'
    default: return res
  }
}
</script>
