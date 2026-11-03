<template>
  <div class="space-y-6 sm:space-y-8 pb-12">
    <!-- Hero PK Banner -->
    <div class="relative overflow-hidden rounded-3xl bg-gradient-to-r from-emerald-950/40 via-slate-900 to-indigo-950/40 border border-slate-800 p-4 sm:p-8 shadow-2xl">
      <div class="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4 sm:gap-6">
        <div>
          <div class="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-indigo-500/10 border border-indigo-500/20 text-indigo-400 text-xs font-bold mb-2 sm:mb-3">
            <span>⚔️</span> AI 双雄巅峰对决
          </div>
          <h1 class="text-xl sm:text-3xl md:text-4xl font-extrabold text-white">
            GPT-4o vs 通义千问 胜率PK
          </h1>
          <p class="text-slate-400 text-xs sm:text-sm mt-1 max-w-xl">
            大数据全方位对比 OpenAI 旗舰模型与阿里云通义千问在足球比赛预测中的一致率、分歧战役表现及各大联赛胜率分布。
          </p>
        </div>

        <div class="w-full sm:w-auto">
          <el-select v-model="selectedSeason" placeholder="选择赛季" class="w-full sm:!w-44" @change="fetchComparison">
            <el-option label="全部赛季总计" value="" />
            <el-option label="2026-2027 赛季" value="2026-2027" />
            <el-option label="2024-2025 赛季" value="2024-2025" />
            <el-option label="2025-2026 赛季" value="2025-2026" />
          </el-select>
        </div>
      </div>

      <!-- Head to Head Big Stat Cards -->
      <div v-if="comparisonData" class="grid grid-cols-1 md:grid-cols-2 gap-4 sm:gap-6 mt-6 sm:mt-8">
        <!-- Model A: GPT-4o -->
        <div class="glass-card rounded-2xl p-4 sm:p-6 border border-emerald-500/30 bg-gradient-to-b from-emerald-950/20 to-slate-900 shadow-glow-gpt">
          <div class="flex items-center justify-between mb-3 sm:mb-4">
            <div class="flex items-center gap-2 sm:gap-2.5">
              <span class="w-2.5 h-2.5 sm:w-3 sm:h-3 rounded-full bg-emerald-400 shadow-glow-gpt"></span>
              <span class="font-extrabold text-base sm:text-lg text-white">OpenAI GPT-4o</span>
            </div>
            <span class="text-xs px-2.5 py-1 rounded-full bg-emerald-500/10 text-emerald-400 border border-emerald-500/30 font-bold">
              {{ comparisonData.modelAStat?.currentStreak > 0 ? `🔥 ${comparisonData.modelAStat.currentStreak}连红` : '模型A' }}
            </span>
          </div>

          <div class="grid grid-cols-2 gap-2.5 sm:gap-3 my-2.5 sm:my-3 p-3 sm:p-3.5 rounded-2xl bg-slate-950/70 border border-slate-800">
            <div>
              <div class="text-[10px] sm:text-[11px] text-slate-400 font-semibold mb-0.5">💰 累计净赚倍数</div>
              <div
                class="text-2xl sm:text-3xl font-black font-mono tracking-tight"
                :class="Number(comparisonData.modelAStat?.netProfit || 0) >= 0 ? 'text-emerald-400' : 'text-rose-400'"
              >
                {{ Number(comparisonData.modelAStat?.netProfit || 0) >= 0 ? '+' : '' }}{{ comparisonData.modelAStat?.netProfit || '0.00' }} 倍
              </div>
            </div>
            <div class="text-right">
              <div class="text-[10px] sm:text-[11px] text-slate-400 font-semibold mb-0.5">胜负胜率 / ROI</div>
              <div class="text-lg sm:text-xl font-black font-mono text-emerald-300">
                {{ comparisonData.modelAStat?.winRate || '0.00' }}%
              </div>
              <div class="text-[10px] sm:text-[11px] font-mono text-slate-400">
                ROI: {{ Number(comparisonData.modelAStat?.roiRate || 0) >= 0 ? '+' : '' }}{{ comparisonData.modelAStat?.roiRate || '0.00' }}%
              </div>
            </div>
          </div>

          <div class="grid grid-cols-2 sm:grid-cols-4 gap-2 mt-3 sm:mt-4 pt-3 sm:pt-4 border-t border-slate-800 text-center text-xs">
            <div class="p-1.5 rounded-lg bg-slate-900/40">
              <div class="text-[10px] text-slate-400">总场次</div>
              <div class="font-bold text-white mt-0.5">{{ comparisonData.modelAStat?.settledMatches || 0 }} 场</div>
            </div>
            <div class="p-1.5 rounded-lg bg-slate-900/40">
              <div class="text-[10px] text-slate-400">胜负红/黑</div>
              <div class="font-bold text-emerald-400 mt-0.5">{{ comparisonData.modelAStat?.wonMatches || 0 }}/{{ comparisonData.modelAStat?.lostMatches || 0 }}</div>
            </div>
            <div class="p-1.5 rounded-lg bg-slate-900/40">
              <div class="text-[10px] text-slate-400">比分命中</div>
              <div class="font-bold text-indigo-300 mt-0.5">{{ comparisonData.modelAStat?.scoreAccuracyRate || '0.00' }}%</div>
            </div>
            <div class="p-1.5 rounded-lg bg-slate-900/40">
              <div class="text-[10px] text-slate-400">进球命中</div>
              <div class="font-bold text-emerald-300 mt-0.5">{{ comparisonData.modelAStat?.goalsAccuracyRate || '0.00' }}%</div>
            </div>
          </div>
        </div>

        <!-- Model B: Qwen-Max -->
        <div class="glass-card rounded-2xl p-4 sm:p-6 border border-indigo-500/30 bg-gradient-to-b from-indigo-950/20 to-slate-900 shadow-glow-qwen">
          <div class="flex items-center justify-between mb-3 sm:mb-4">
            <div class="flex items-center gap-2 sm:gap-2.5">
              <span class="w-2.5 h-2.5 sm:w-3 sm:h-3 rounded-full bg-indigo-400 shadow-glow-qwen"></span>
              <span class="font-extrabold text-base sm:text-lg text-white">通义千问 Qwen-Max</span>
            </div>
            <span class="text-xs px-2.5 py-1 rounded-full bg-indigo-500/10 text-indigo-400 border border-indigo-500/30 font-bold">
              {{ comparisonData.modelBStat?.currentStreak > 0 ? `🔥 ${comparisonData.modelBStat.currentStreak}连红` : '模型B' }}
            </span>
          </div>

          <div class="grid grid-cols-2 gap-2.5 sm:gap-3 my-2.5 sm:my-3 p-3 sm:p-3.5 rounded-2xl bg-slate-950/70 border border-slate-800">
            <div>
              <div class="text-[10px] sm:text-[11px] text-slate-400 font-semibold mb-0.5">💰 累计净赚倍数</div>
              <div
                class="text-2xl sm:text-3xl font-black font-mono tracking-tight"
                :class="Number(comparisonData.modelBStat?.netProfit || 0) >= 0 ? 'text-indigo-400' : 'text-rose-400'"
              >
                {{ Number(comparisonData.modelBStat?.netProfit || 0) >= 0 ? '+' : '' }}{{ comparisonData.modelBStat?.netProfit || '0.00' }} 倍
              </div>
            </div>
            <div class="text-right">
              <div class="text-[10px] sm:text-[11px] text-slate-400 font-semibold mb-0.5">胜负胜率 / ROI</div>
              <div class="text-lg sm:text-xl font-black font-mono text-indigo-300">
                {{ comparisonData.modelBStat?.winRate || '0.00' }}%
              </div>
              <div class="text-[10px] sm:text-[11px] font-mono text-slate-400">
                ROI: {{ Number(comparisonData.modelBStat?.roiRate || 0) >= 0 ? '+' : '' }}{{ comparisonData.modelBStat?.roiRate || '0.00' }}%
              </div>
            </div>
          </div>

          <div class="grid grid-cols-2 sm:grid-cols-4 gap-2 mt-3 sm:mt-4 pt-3 sm:pt-4 border-t border-slate-800 text-center text-xs">
            <div class="p-1.5 rounded-lg bg-slate-900/40">
              <div class="text-[10px] text-slate-400">总场次</div>
              <div class="font-bold text-white mt-0.5">{{ comparisonData.modelBStat?.settledMatches || 0 }} 场</div>
            </div>
            <div class="p-1.5 rounded-lg bg-slate-900/40">
              <div class="text-[10px] text-slate-400">胜负红/黑</div>
              <div class="font-bold text-indigo-400 mt-0.5">{{ comparisonData.modelBStat?.wonMatches || 0 }}/{{ comparisonData.modelBStat?.lostMatches || 0 }}</div>
            </div>
            <div class="p-1.5 rounded-lg bg-slate-900/40">
              <div class="text-[10px] text-slate-400">比分命中</div>
              <div class="font-bold text-indigo-300 mt-0.5">{{ comparisonData.modelBStat?.scoreAccuracyRate || '0.00' }}%</div>
            </div>
            <div class="p-1.5 rounded-lg bg-slate-900/40">
              <div class="text-[10px] text-slate-400">进球命中</div>
              <div class="font-bold text-emerald-300 mt-0.5">{{ comparisonData.modelBStat?.goalsAccuracyRate || '0.00' }}%</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Agreement & Disagreement War Analytics -->
    <div v-if="comparisonData" class="grid grid-cols-1 md:grid-cols-3 gap-4 sm:gap-6">
      <!-- Agreement Card -->
      <div class="glass-panel rounded-3xl p-4 sm:p-6 border border-slate-800 flex flex-col justify-between">
        <div>
          <div class="flex items-center justify-between">
            <span class="text-xs font-bold text-slate-400 uppercase">共同预测一致率</span>
            <span class="text-xl">🤝</span>
          </div>
          <div class="mt-3 sm:mt-4 flex items-baseline gap-2">
            <span class="text-3xl font-black font-mono text-white">{{ comparisonData.agreeRate }}%</span>
          </div>
          <p class="text-xs text-slate-400 mt-1">
            在 {{ comparisonData.jointSettledMatches }} 场比赛中，两模型有 {{ comparisonData.agreeMatches }} 场赛果观点一致。
          </p>
        </div>

        <div class="mt-4 p-3 rounded-xl bg-emerald-950/20 border border-emerald-500/20 text-xs">
          <span class="text-slate-400">一致时双红场次：</span>
          <span class="font-bold text-emerald-400">{{ comparisonData.agreeWonMatches }} 场</span>
        </div>
      </div>

      <!-- Disagreement Winner Card -->
      <div class="glass-panel rounded-3xl p-4 sm:p-6 border border-slate-800 md:col-span-2 flex flex-col justify-between">
        <div>
          <div class="flex items-center justify-between">
            <div>
              <span class="text-xs font-bold text-slate-400 uppercase">观点分歧焦点战 (共 {{ comparisonData.disagreeMatches }} 场)</span>
              <h3 class="text-sm sm:text-base font-bold text-white mt-0.5">两模型意见不合时，谁更准确？</h3>
            </div>
            <span class="text-xl">⚡</span>
          </div>

          <div class="grid grid-cols-1 sm:grid-cols-3 gap-2.5 sm:gap-3 my-4 text-center">
            <div class="p-3 sm:p-3.5 rounded-2xl bg-emerald-950/30 border border-emerald-500/30">
              <div class="text-[11px] text-emerald-300 font-semibold">GPT 胜出 (千问失误)</div>
              <div class="text-xl sm:text-2xl font-black font-mono text-emerald-400 mt-1">{{ comparisonData.modelAWonDisagreeMatches }} 场</div>
            </div>

            <div class="p-3 sm:p-3.5 rounded-2xl bg-indigo-950/30 border border-indigo-500/30">
              <div class="text-[11px] text-indigo-300 font-semibold">千问 胜出 (GPT失误)</div>
              <div class="text-xl sm:text-2xl font-black font-mono text-indigo-400 mt-1">{{ comparisonData.modelBWonDisagreeMatches }} 场</div>
            </div>

            <div class="p-3 sm:p-3.5 rounded-2xl bg-slate-900 border border-slate-800">
              <div class="text-[11px] text-slate-400 font-semibold">双双失误 (冷门)</div>
              <div class="text-xl sm:text-2xl font-black font-mono text-slate-300 mt-1">{{ comparisonData.bothLostDisagreeMatches }} 场</div>
            </div>
          </div>
        </div>

        <p class="text-[11px] sm:text-xs text-slate-400">
          💡 当两模型预测一致时跟单置信度最高；当两模型分歧时，可结合各自在对应联赛的优势度进行决策。
        </p>
      </div>
    </div>

    <!-- League-by-League Breakdown -->
    <div v-if="comparisonData?.leagueBreakdown?.length > 0" class="glass-panel rounded-3xl p-4 sm:p-6 border border-slate-800 shadow-xl">
      <h2 class="text-base sm:text-lg font-bold text-white mb-4 flex items-center gap-2">
        <span>🏆</span> 各大联赛胜率与盈利分项对比 (五大联赛、欧冠、世界杯)
      </h2>

      <!-- Desktop Table (hidden on mobile) -->
      <div class="hidden md:block overflow-x-auto">
        <table class="w-full text-left text-sm">
          <thead>
            <tr class="border-b border-slate-800 text-xs font-semibold text-slate-400">
              <th class="py-3 px-4">赛事</th>
              <th class="py-3 px-4">GPT-4o 胜率</th>
              <th class="py-3 px-4">GPT-4o 净赚</th>
              <th class="py-3 px-4">通义千问 胜率</th>
              <th class="py-3 px-4">通义千问 净赚</th>
              <th class="py-3 px-4">胜出领跑模型</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-slate-800/60">
            <tr v-for="item in comparisonData.leagueBreakdown" :key="item.leagueCode" class="hover:bg-slate-800/30">
              <td class="py-4 px-4 font-bold text-slate-100 flex items-center gap-2">
                <span class="px-2 py-0.5 rounded bg-slate-800 text-xs text-emerald-400 font-mono">{{ item.leagueCode }}</span>
                <span>{{ item.leagueName }}</span>
              </td>
              <td class="py-4 px-4">
                <span class="text-base font-black font-mono text-emerald-400">{{ item.modelAWinRate }}%</span>
                <span class="text-xs text-slate-400 ml-1.5">({{ item.modelASettledMatches }} 场)</span>
              </td>
              <td class="py-4 px-4 font-mono font-bold" :class="Number(item.modelANetProfit || 0) >= 0 ? 'text-emerald-400' : 'text-rose-400'">
                {{ Number(item.modelANetProfit || 0) >= 0 ? '+' : '' }}{{ item.modelANetProfit || '0.00' }} 倍
              </td>
              <td class="py-4 px-4">
                <span class="text-base font-black font-mono text-indigo-400">{{ item.modelBWinRate }}%</span>
                <span class="text-xs text-slate-400 ml-1.5">({{ item.modelBSettledMatches }} 场)</span>
              </td>
              <td class="py-4 px-4 font-mono font-bold" :class="Number(item.modelBNetProfit || 0) >= 0 ? 'text-indigo-400' : 'text-rose-400'">
                {{ Number(item.modelBNetProfit || 0) >= 0 ? '+' : '' }}{{ item.modelBNetProfit || '0.00' }} 倍
              </td>
              <td class="py-4 px-4">
                <span
                  v-if="item.leaderModel === 'GPT'"
                  class="px-2.5 py-1 rounded-lg text-xs font-bold bg-emerald-500/20 text-emerald-400 border border-emerald-500/30"
                >
                  🟢 GPT 领跑
                </span>
                <span
                  v-else-if="item.leaderModel === 'QWEN'"
                  class="px-2.5 py-1 rounded-lg text-xs font-bold bg-indigo-500/20 text-indigo-400 border border-indigo-500/30"
                >
                  🟣 千问 领跑
                </span>
                <span
                  v-else
                  class="px-2.5 py-1 rounded-lg text-xs font-medium bg-slate-800 text-slate-400"
                >
                  🤝 战平
                </span>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- Mobile League Cards (visible on mobile) -->
      <div class="md:hidden space-y-3">
        <div
          v-for="item in comparisonData.leagueBreakdown"
          :key="item.leagueCode"
          class="p-4 rounded-2xl bg-slate-900/60 border border-slate-800 space-y-3"
        >
          <!-- League Header -->
          <div class="flex items-center justify-between">
            <div class="flex items-center gap-2">
              <span class="px-2 py-0.5 rounded bg-slate-800 text-xs text-emerald-400 font-mono font-bold">{{ item.leagueCode }}</span>
              <span class="font-bold text-white text-sm">{{ item.leagueName }}</span>
            </div>
            <div>
              <span
                v-if="item.leaderModel === 'GPT'"
                class="px-2 py-0.5 rounded-lg text-[11px] font-bold bg-emerald-500/20 text-emerald-400 border border-emerald-500/30"
              >
                🟢 GPT 领跑
              </span>
              <span
                v-else-if="item.leaderModel === 'QWEN'"
                class="px-2 py-0.5 rounded-lg text-[11px] font-bold bg-indigo-500/20 text-indigo-400 border border-indigo-500/30"
              >
                🟣 千问 领跑
              </span>
              <span
                v-else
                class="px-2 py-0.5 rounded-lg text-[11px] font-medium bg-slate-800 text-slate-400"
              >
                🤝 战平
              </span>
            </div>
          </div>

          <!-- GPT vs Qwen Side-by-Side -->
          <div class="grid grid-cols-2 gap-2.5 pt-1">
            <!-- GPT Column -->
            <div class="p-2.5 rounded-xl bg-emerald-950/20 border border-emerald-500/20">
              <div class="flex items-center justify-between text-[11px] text-emerald-400 font-bold mb-1">
                <span>GPT-4o</span>
                <span class="text-[10px] text-slate-400 font-normal">({{ item.modelASettledMatches }}场)</span>
              </div>
              <div class="text-sm font-black font-mono text-emerald-300">胜率 {{ item.modelAWinRate }}%</div>
              <div class="text-xs font-mono font-bold mt-0.5" :class="Number(item.modelANetProfit || 0) >= 0 ? 'text-emerald-400' : 'text-rose-400'">
                {{ Number(item.modelANetProfit || 0) >= 0 ? '+' : '' }}{{ item.modelANetProfit || '0.00' }} 倍
              </div>
            </div>

            <!-- Qwen Column -->
            <div class="p-2.5 rounded-xl bg-indigo-950/20 border border-indigo-500/20">
              <div class="flex items-center justify-between text-[11px] text-indigo-400 font-bold mb-1">
                <span>通义千问</span>
                <span class="text-[10px] text-slate-400 font-normal">({{ item.modelBSettledMatches }}场)</span>
              </div>
              <div class="text-sm font-black font-mono text-indigo-300">胜率 {{ item.modelBWinRate }}%</div>
              <div class="text-xs font-mono font-bold mt-0.5" :class="Number(item.modelBNetProfit || 0) >= 0 ? 'text-indigo-400' : 'text-rose-400'">
                {{ Number(item.modelBNetProfit || 0) >= 0 ? '+' : '' }}{{ item.modelBNetProfit || '0.00' }} 倍
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '@/api'

const selectedSeason = ref('2024-2025')
const comparisonData = ref(null)
const loading = ref(false)

const fetchComparison = async () => {
  loading.value = true
  try {
    const params = {
      modelA: 'GPT',
      modelB: 'QWEN',
      seasonName: selectedSeason.value || undefined
    }
    const data = await api.getAiComparison(params)
    comparisonData.value = data
  } catch (err) {
    console.error('Failed to fetch AI comparison', err)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchComparison()
})
</script>
