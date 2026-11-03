<template>
  <div class="space-y-6 sm:space-y-8 pb-12">
    <!-- Mode Switcher Tabs: 穿盘版 vs 进球数版 vs 水位翻车版 vs 水位串子版 -->
    <div class="flex items-center justify-between flex-wrap gap-3">
      <div class="flex items-center gap-1.5 p-1 rounded-2xl bg-slate-950/80 border border-slate-800">
        <router-link
          to="/daily-profit"
          class="px-4 py-2 rounded-xl text-xs sm:text-sm font-bold transition flex items-center gap-2 text-slate-400 hover:text-white hover:bg-slate-900"
        >
          <span>⚽ 让球穿盘战绩版</span>
        </router-link>
        <router-link
          to="/daily-goals-profit"
          class="px-4 py-2 rounded-xl text-xs sm:text-sm font-bold transition flex items-center gap-2 bg-gradient-to-r from-amber-500 to-orange-500 text-slate-950 shadow-md shadow-amber-500/20 font-extrabold"
        >
          <span>🥅 总进球数规律版</span>
        </router-link>
        <router-link
          to="/odds-water-profit"
          class="px-4 py-2 rounded-xl text-xs sm:text-sm font-bold transition flex items-center gap-2 text-slate-400 hover:text-white hover:bg-slate-900"
        >
          <span>🌊 水位冷门翻车版</span>
        </router-link>
        <router-link
          to="/water-combo-profit"
          class="px-4 py-2 rounded-xl text-xs sm:text-sm font-bold transition flex items-center gap-2 text-slate-400 hover:text-white hover:bg-slate-900"
        >
          <span>🎯 水位串子战绩版</span>
        </router-link>
      </div>

      <div class="text-xs text-slate-400 hidden sm:flex items-center gap-1.5">
        <span class="w-2 h-2 rounded-full bg-amber-400 animate-pulse"></span>
        <span>当前模式: 总进球数规律吻合度与双策略大数据分析</span>
      </div>
    </div>

    <!-- 1. Hero Banner -->
    <div class="relative overflow-hidden rounded-3xl bg-gradient-to-r from-slate-900 via-amber-950/20 to-slate-900 border border-slate-800 p-5 sm:p-8 shadow-2xl">
      <div class="absolute -right-10 -top-10 w-80 h-80 bg-amber-500/10 rounded-full blur-3xl pointer-events-none"></div>
      <div class="absolute -left-10 -bottom-10 w-80 h-80 bg-orange-500/10 rounded-full blur-3xl pointer-events-none"></div>

      <div class="relative z-10 flex flex-col lg:flex-row lg:items-center lg:justify-between gap-6">
        <div>
          <div class="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-amber-500/10 border border-amber-500/20 text-amber-400 text-xs font-bold mb-3">
            <span>🥅</span> 总进球数规律大数据战报
          </div>
          <h1 class="text-2xl sm:text-3xl md:text-4xl font-extrabold text-white tracking-tight flex items-center gap-3">
            每日总进球数成本利润与规律复盘
          </h1>
          <p class="text-slate-400 text-xs sm:text-sm mt-2 max-w-2xl leading-relaxed">
            统计每日<strong>「符合规律」</strong>、<strong>「稍微符合规律」</strong>、<strong>「完全不符合规律」</strong>比赛分布，并追踪<strong>主三串副二串 (3M2S)</strong> 与<strong>主二串副三串 (2M3S)</strong> 双策略收益演变。
          </p>
        </div>

        <!-- Quick Action & Date Range Filter -->
        <div class="flex flex-wrap items-center gap-2 sm:gap-3 bg-slate-950/70 p-3 rounded-2xl border border-slate-800 backdrop-blur-md">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始比赛日"
            end-placeholder="结束比赛日"
            value-format="YYYY-MM-DD"
            class="!w-64 sm:!w-72"
            :shortcuts="dateShortcuts"
            @change="handleDateRangeChange"
          />

          <button
            @click="handleOpenCreateDialog"
            class="px-4 py-2 rounded-xl text-xs sm:text-sm font-bold bg-gradient-to-r from-amber-400 to-orange-400 text-slate-950 hover:from-amber-300 hover:to-orange-300 shadow-lg shadow-amber-500/20 transition-all flex items-center gap-1.5 shrink-0"
          >
            <span>➕</span> 录入进球战绩
          </button>

          <button
            @click="fetchAllData"
            class="p-2 sm:px-3 sm:py-2 rounded-xl text-xs font-medium bg-slate-900 hover:bg-slate-800 text-slate-300 border border-slate-800 transition flex items-center gap-1"
            title="刷新数据"
          >
            <span :class="{ 'animate-spin': loading }">🔄</span>
            <span class="hidden sm:inline">刷新</span>
          </button>
        </div>
      </div>
    </div>

    <!-- 2. KPI Overview Cards (5 Cards) -->
    <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-5 gap-3 sm:gap-4">
      <!-- Card 1: 累计总净利润 -->
      <div class="glass-card rounded-2xl p-4 sm:p-5 border border-slate-800 bg-slate-900/60 relative overflow-hidden group hover:border-amber-500/40 transition-all">
        <div class="flex items-center justify-between text-slate-400 text-xs mb-2">
          <span class="font-semibold">💰 进球数总净利润</span>
          <span class="text-[10px] px-2 py-0.5 rounded-full bg-slate-800 text-slate-300">
            {{ summary.totalDays || 0 }} 比赛日
          </span>
        </div>
        <div
          class="text-2xl sm:text-3xl font-black font-mono tracking-tight"
          :class="Number(summary.totalProfit || 0) >= 0 ? 'text-amber-400' : 'text-rose-400'"
        >
          {{ Number(summary.totalProfit || 0) >= 0 ? '+' : '' }}¥{{ formatNumber(summary.totalProfit) }}
        </div>
        <div class="mt-2.5 pt-2.5 border-t border-slate-800/80 flex items-center justify-between text-[11px] text-slate-400">
          <span>累计总返还:</span>
          <span class="font-mono font-bold text-slate-200">¥{{ formatNumber(summary.totalReturn) }}</span>
        </div>
      </div>

      <!-- Card 2: 综合回报率 ROI -->
      <div class="glass-card rounded-2xl p-4 sm:p-5 border border-slate-800 bg-slate-900/60 relative overflow-hidden group hover:border-amber-500/40 transition-all">
        <div class="flex items-center justify-between text-slate-400 text-xs mb-2">
          <span class="font-semibold">📈 综合回报率 ROI</span>
          <span
            class="text-[10px] px-2 py-0.5 rounded-full font-bold"
            :class="Number(summary.overallRoi || 0) >= 20 ? 'bg-emerald-500/20 text-emerald-300 border border-emerald-500/30' : Number(summary.overallRoi || 0) >= 0 ? 'bg-amber-500/20 text-amber-300 border border-amber-500/30' : 'bg-rose-500/20 text-rose-300 border border-rose-500/30'"
          >
            {{ Number(summary.overallRoi || 0) >= 20 ? '🔥 极高收益' : Number(summary.overallRoi || 0) >= 0 ? '✨ 正收益' : '⚠️ 出现回撤' }}
          </span>
        </div>
        <div
          class="text-2xl sm:text-3xl font-black font-mono tracking-tight"
          :class="Number(summary.overallRoi || 0) >= 0 ? 'text-emerald-400' : 'text-rose-400'"
        >
          {{ Number(summary.overallRoi || 0) >= 0 ? '+' : '' }}{{ summary.overallRoi || '0.00' }}%
        </div>
        <div class="mt-2.5 pt-2.5 border-t border-slate-800/80 flex items-center justify-between text-[11px] text-slate-400">
          <span>盈利日胜率:</span>
          <span class="font-mono font-bold text-amber-400">{{ summary.winDaysRate || '0.00' }}% ({{ summary.profitableDays || 0 }}/{{ summary.totalDays || 0 }}天)</span>
        </div>
      </div>

      <!-- Card 3: 累计总成本 -->
      <div class="glass-card rounded-2xl p-4 sm:p-5 border border-slate-800 bg-slate-900/60 relative overflow-hidden group hover:border-slate-700 transition-all">
        <div class="flex items-center justify-between text-slate-400 text-xs mb-2">
          <span class="font-semibold">💵 累计投注总成本</span>
          <span class="text-[10px] px-2 py-0.5 rounded-full bg-slate-800 text-slate-400">总投入</span>
        </div>
        <div class="text-2xl sm:text-3xl font-black font-mono text-slate-200 tracking-tight">
          ¥{{ formatNumber(summary.totalCost) }}
        </div>
        <div class="mt-2.5 pt-2.5 border-t border-slate-800/80 flex items-center justify-between text-[11px] text-slate-400">
          <span>日均投入:</span>
          <span class="font-mono text-slate-300">
            ¥{{ summary.totalDays > 0 ? (Number(summary.totalCost || 0) / summary.totalDays).toFixed(2) : '0.00' }}
          </span>
        </div>
      </div>

      <!-- Card 4: 进球数规律大数据覆盖率 -->
      <div class="glass-card rounded-2xl p-4 sm:p-5 border border-slate-800 bg-slate-900/60 relative overflow-hidden group hover:border-amber-500/40 transition-all">
        <div class="flex items-center justify-between text-slate-400 text-xs mb-2">
          <span class="font-semibold">🎯 广义规律覆盖率</span>
          <span class="text-[10px] px-2 py-0.5 rounded-full bg-amber-500/10 text-amber-400 border border-amber-500/20 font-bold">
            泛规律命中
          </span>
        </div>
        <div class="text-2xl sm:text-3xl font-black font-mono text-amber-300 tracking-tight">
          {{ summary.avgGeneralizedFitRate || '0.00' }}%
        </div>
        <div class="mt-2.5 pt-2.5 border-t border-slate-800/80 flex items-center justify-between text-[11px] text-slate-400">
          <span>完全符合率:</span>
          <span class="font-mono text-slate-200">
            <span class="text-emerald-400 font-bold">{{ summary.avgFitRate || '0.00' }}%</span> ({{ summary.totalFitCount || 0 }}/{{ summary.totalMatches || 0 }}场)
          </span>
        </div>
      </div>

      <!-- Card 5: 双策略对冲 PK -->
      <div class="glass-card rounded-2xl p-4 sm:p-5 border border-amber-500/30 bg-gradient-to-b from-amber-950/20 to-slate-900 relative overflow-hidden group hover:border-amber-500/50 transition-all">
        <div class="flex items-center justify-between text-slate-400 text-xs mb-2">
          <span class="font-semibold text-amber-300">⚔️ 策略收益 PK</span>
          <span class="text-[10px] px-2 py-0.5 rounded-full bg-amber-500/20 text-amber-300 font-bold border border-amber-500/30">
            {{ summary.bestStrategy?.split(' ')[0] || '均衡' }}
          </span>
        </div>
        <div class="space-y-1.5 my-1">
          <div class="flex items-center justify-between text-xs font-mono">
            <span class="text-slate-300 font-medium">3串2副:</span>
            <span :class="Number(summary.total3m2sProfit || 0) >= 0 ? 'text-amber-400 font-bold' : 'text-rose-400 font-bold'">
              ¥{{ formatNumber(summary.total3m2sProfit) }}
              <span class="text-[10px] text-slate-400 font-normal">({{ summary.roi3m2s || '0.00' }}%)</span>
            </span>
          </div>
          <div class="flex items-center justify-between text-xs font-mono">
            <span class="text-slate-300 font-medium">2串3副:</span>
            <span :class="Number(summary.total2m3sProfit || 0) >= 0 ? 'text-orange-400 font-bold' : 'text-rose-400 font-bold'">
              ¥{{ formatNumber(summary.total2m3sProfit) }}
              <span class="text-[10px] text-slate-400 font-normal">({{ summary.roi2m3s || '0.00' }}%)</span>
            </span>
          </div>
        </div>
        <div class="mt-2 pt-2 border-t border-slate-800/80 text-[10px] text-slate-400 truncate">
          {{ summary.bestStrategy || '双策略对冲均衡' }}
        </div>
      </div>
    </div>

    <!-- 3. ECharts Analytics Panel -->
    <div class="grid grid-cols-1 lg:grid-cols-3 gap-5 sm:gap-6">
      <!-- Chart 1: 累计净利润与单日盈亏走势 (占 2 列) -->
      <div class="lg:col-span-2 glass-panel rounded-3xl p-4 sm:p-6 border border-slate-800 shadow-xl bg-slate-900/70">
        <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-2 mb-4">
          <div>
            <h2 class="text-base sm:text-lg font-bold text-white flex items-center gap-2">
              <span>📊</span> 总进球数净利润与累计收益走势
            </h2>
            <p class="text-slate-400 text-xs mt-0.5">每日净利柱状图与累计收益曲线双轴走势</p>
          </div>
          <div class="flex items-center gap-2 text-xs">
            <span class="inline-flex items-center gap-1 text-amber-400">
              <span class="w-2.5 h-2.5 rounded-sm bg-amber-500"></span> 单日盈利
            </span>
            <span class="inline-flex items-center gap-1 text-rose-400">
              <span class="w-2.5 h-2.5 rounded-sm bg-rose-500"></span> 单日亏损
            </span>
            <span class="inline-flex items-center gap-1 text-orange-400">
              <span class="w-2.5 h-0.5 bg-orange-400"></span> 累计净利润
            </span>
          </div>
        </div>
        <div ref="profitChartRef" class="w-full h-72 sm:h-80"></div>
      </div>

      <!-- Chart 2: 进球数规律分布态势 (占 1 列) -->
      <div class="glass-panel rounded-3xl p-4 sm:p-6 border border-slate-800 shadow-xl bg-slate-900/70">
        <div class="mb-4">
          <h2 class="text-base sm:text-lg font-bold text-white flex items-center gap-2">
            <span>🥅</span> 规律吻合度分布走势
          </h2>
          <p class="text-slate-400 text-xs mt-0.5">符合 vs 稍符合 vs 不符合场次堆叠与广义吻合率</p>
        </div>
        <div ref="patternChartRef" class="w-full h-72 sm:h-80"></div>
      </div>

      <!-- Chart 3: 3M2S vs 2M3S 策略收益对比走势 (全宽) -->
      <div class="lg:col-span-3 glass-panel rounded-3xl p-4 sm:p-6 border border-slate-800 shadow-xl bg-slate-900/70">
        <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-2 mb-4">
          <div>
            <h2 class="text-base sm:text-lg font-bold text-white flex items-center gap-2">
              <span>⚖️</span> 主三串副二串 vs 主二串副三串 进球数策略对决
            </h2>
            <p class="text-slate-400 text-xs mt-0.5">
              对比总进球数高赔进攻单（3M2S）与稳健兜底单（2M3S）的盈利弹性与对冲平滑效果
            </p>
          </div>
          <div class="flex items-center gap-3 text-xs">
            <span class="inline-flex items-center gap-1 text-amber-400 font-medium">
              <span class="w-3 h-1 bg-amber-400 rounded"></span> 主三串副二串 (3M2S)
            </span>
            <span class="inline-flex items-center gap-1 text-cyan-400 font-medium">
              <span class="w-3 h-1 bg-cyan-400 rounded"></span> 主二串副三串 (2M3S)
            </span>
          </div>
        </div>
        <div ref="strategyChartRef" class="w-full h-64 sm:h-72"></div>
      </div>
    </div>

    <!-- 4. Historical Records Table -->
    <div class="glass-panel rounded-3xl p-4 sm:p-6 border border-slate-800 shadow-xl bg-slate-900/80">
      <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3 mb-4">
        <div>
          <h2 class="text-base sm:text-lg font-bold text-white flex items-center gap-2">
            <span>📋</span> 每日总进球数战绩记录明细
          </h2>
          <p class="text-slate-400 text-xs mt-0.5">详细记录各比赛日规律符合度、双策略投入产出与复盘总结</p>
        </div>
        <div class="text-xs text-slate-400 font-mono">
          共收录 {{ recordList.length }} 笔进球数比赛日数据
        </div>
      </div>

      <!-- Desktop Table -->
      <div class="overflow-x-auto">
        <table class="w-full text-left text-sm">
          <thead>
            <tr class="border-b border-slate-800 text-xs font-semibold text-slate-400">
              <th class="py-3 px-3">比赛日</th>
              <th class="py-3 px-3">进球数规律分布</th>
              <th class="py-3 px-3 text-right">广义吻合率</th>
              <th class="py-3 px-3 text-right">主三副二 (3M2S)</th>
              <th class="py-3 px-3 text-right">主二副三 (2M3S)</th>
              <th class="py-3 px-3 text-right">当日总成本</th>
              <th class="py-3 px-3 text-right">当日总净利</th>
              <th class="py-3 px-3 text-right">综合 ROI</th>
              <th class="py-3 px-3 text-center">优势策略</th>
              <th class="py-3 px-3 text-center">规律态势</th>
              <th class="py-3 px-3">复盘备注</th>
              <th class="py-3 px-3 text-center">操作</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-slate-800/60">
            <tr
              v-for="record in recordList"
              :key="record.id"
              class="hover:bg-slate-800/40 transition-colors"
            >
              <!-- 比赛日 -->
              <td class="py-3.5 px-3 font-mono font-bold text-white whitespace-nowrap">
                {{ record.matchDate }}
              </td>

              <!-- 进球数规律分布 -->
              <td class="py-3.5 px-3 whitespace-nowrap">
                <div class="flex items-center gap-1.5 font-mono text-xs">
                  <span class="text-emerald-400 font-bold" title="符合规律">{{ record.patternFitCount }}符</span> /
                  <span class="text-amber-400 font-bold" title="稍微符合">{{ record.patternSlightCount }}微</span> /
                  <span class="text-slate-400" title="不符合">{{ record.patternMismatchCount }}否</span>
                  <span class="text-slate-500 text-[11px]">(共{{ record.totalMatches }}场)</span>
                </div>
              </td>

              <!-- 广义吻合率 -->
              <td class="py-3.5 px-3 text-right whitespace-nowrap font-mono text-xs font-bold">
                <span
                  class="px-2 py-0.5 rounded-full"
                  :class="Number(record.generalizedFitRate || 0) >= 80 ? 'bg-emerald-500/20 text-emerald-300 border border-emerald-500/30' : Number(record.generalizedFitRate || 0) >= 60 ? 'bg-amber-500/20 text-amber-300 border border-amber-500/30' : 'bg-rose-500/20 text-rose-300 border border-rose-500/30'"
                >
                  {{ record.generalizedFitRate }}%
                </span>
              </td>

              <!-- 主三副二 (3M2S) -->
              <td class="py-3.5 px-3 text-right whitespace-nowrap font-mono text-xs">
                <div class="text-slate-400">投: ¥{{ formatNumber(record.strategy3m2sCost) }}</div>
                <div
                  class="font-bold"
                  :class="Number(record.strategy3m2sProfit || 0) >= 0 ? 'text-amber-400' : 'text-rose-400'"
                >
                  {{ Number(record.strategy3m2sProfit || 0) >= 0 ? '+' : '' }}¥{{ formatNumber(record.strategy3m2sProfit) }}
                  <span class="text-[10px] font-normal">({{ record.strategy3m2sRoi }}%)</span>
                </div>
              </td>

              <!-- 主二副三 (2M3S) -->
              <td class="py-3.5 px-3 text-right whitespace-nowrap font-mono text-xs">
                <div class="text-slate-400">投: ¥{{ formatNumber(record.strategy2m3sCost) }}</div>
                <div
                  class="font-bold"
                  :class="Number(record.strategy2m3sProfit || 0) >= 0 ? 'text-cyan-400' : 'text-rose-400'"
                >
                  {{ Number(record.strategy2m3sProfit || 0) >= 0 ? '+' : '' }}¥{{ formatNumber(record.strategy2m3sProfit) }}
                  <span class="text-[10px] font-normal">({{ record.strategy2m3sRoi }}%)</span>
                </div>
              </td>

              <!-- 当日总成本 -->
              <td class="py-3.5 px-3 text-right whitespace-nowrap font-mono text-xs text-slate-300 font-semibold">
                ¥{{ formatNumber(record.totalCost) }}
              </td>

              <!-- 当日总净利 -->
              <td class="py-3.5 px-3 text-right whitespace-nowrap font-mono text-sm font-black">
                <span :class="Number(record.totalProfit || 0) >= 0 ? 'text-amber-400' : 'text-rose-400'">
                  {{ Number(record.totalProfit || 0) >= 0 ? '+' : '' }}¥{{ formatNumber(record.totalProfit) }}
                </span>
              </td>

              <!-- 综合 ROI -->
              <td class="py-3.5 px-3 text-right whitespace-nowrap font-mono text-xs font-bold">
                <span :class="Number(record.roiRate || 0) >= 0 ? 'text-amber-400' : 'text-rose-400'">
                  {{ Number(record.roiRate || 0) >= 0 ? '+' : '' }}{{ record.roiRate }}%
                </span>
              </td>

              <!-- 优势策略 -->
              <td class="py-3.5 px-3 text-center whitespace-nowrap">
                <span
                  class="text-[11px] px-2 py-0.5 rounded-full font-bold"
                  :class="record.dominantStrategy === 'STRATEGY_3M2S' ? 'bg-amber-500/20 text-amber-300 border border-amber-500/30' : record.dominantStrategy === 'STRATEGY_2M3S' ? 'bg-cyan-500/20 text-cyan-300 border border-cyan-500/30' : 'bg-slate-800 text-slate-400'"
                >
                  {{ record.dominantStrategy === 'STRATEGY_3M2S' ? '🔥 3串2副' : record.dominantStrategy === 'STRATEGY_2M3S' ? '🛡️ 2串3副' : '🤝 均衡' }}
                </span>
              </td>

              <!-- 规律态势 -->
              <td class="py-3.5 px-3 text-center whitespace-nowrap">
                <span
                  class="text-[10px] px-2 py-0.5 rounded-md font-bold"
                  :class="getTrendBadgeClass(record.goalsTrend)"
                >
                  {{ getTrendText(record.goalsTrend) }}
                </span>
              </td>

              <!-- 复盘备注 -->
              <td class="py-3.5 px-3 text-xs text-slate-300 max-w-xs truncate" :title="record.remarks">
                {{ record.remarks || '-' }}
              </td>

              <!-- 操作 -->
              <td class="py-3.5 px-3 text-center whitespace-nowrap">
                <div class="flex items-center justify-center gap-1.5">
                  <button
                    @click="handleEdit(record)"
                    class="p-1.5 rounded-lg bg-slate-800 hover:bg-slate-700 text-amber-400 text-xs transition"
                    title="编辑记录"
                  >
                    ✏️
                  </button>
                  <button
                    @click="handleDelete(record)"
                    class="p-1.5 rounded-lg bg-slate-800 hover:bg-rose-900/40 text-rose-400 text-xs transition"
                    title="删除记录"
                  >
                    🗑️
                  </button>
                </div>
              </td>
            </tr>

            <tr v-if="recordList.length === 0">
              <td colspan="12" class="text-center py-12 text-slate-500 text-sm">
                暂无进球数战绩记录，点击右上角「➕ 录入进球战绩」开始记录吧！
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- 5. Dialog: 录入 / 编辑总进球数战绩 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEditing ? '✏️ 修改比赛日总进球数战绩' : '➕ 录入比赛日总进球数战绩'"
      width="640px"
      destroy-on-close
      class="custom-dark-dialog"
    >
      <el-form :model="form" :rules="formRules" ref="formRef" label-position="top">
        <div class="grid grid-cols-1 sm:grid-cols-4 gap-3">
          <!-- 比赛日 -->
          <el-form-item label="比赛日" prop="matchDate" class="sm:col-span-1">
            <el-date-picker
              v-model="form.matchDate"
              type="date"
              placeholder="比赛日"
              value-format="YYYY-MM-DD"
              class="!w-full"
            />
          </el-form-item>

          <!-- 符合规律比赛数 -->
          <el-form-item label="符合规律数量" prop="patternFitCount" class="sm:col-span-1">
            <el-input-number
              v-model="form.patternFitCount"
              :min="0"
              :step="1"
              controls-position="right"
              class="!w-full"
              placeholder="完全符合"
            />
          </el-form-item>

          <!-- 稍微符合规律比赛数 -->
          <el-form-item label="稍微符合规律数量" prop="patternSlightCount" class="sm:col-span-1">
            <el-input-number
              v-model="form.patternSlightCount"
              :min="0"
              :step="1"
              controls-position="right"
              class="!w-full"
              placeholder="差1球/次选"
            />
          </el-form-item>

          <!-- 完全不符合规律比赛数 -->
          <el-form-item label="完全不符合数量" prop="patternMismatchCount" class="sm:col-span-1">
            <el-input-number
              v-model="form.patternMismatchCount"
              :min="0"
              :step="1"
              controls-position="right"
              class="!w-full"
              placeholder="反规律异常"
            />
          </el-form-item>
        </div>

        <!-- 策略一：主三串副二串 (3M2S) -->
        <div class="mt-2 p-3.5 rounded-2xl bg-slate-950/80 border border-amber-500/30">
          <div class="text-xs font-bold text-amber-400 mb-2 flex items-center gap-1.5">
            <span>⚡</span> 主三串副二串进球策略 (3M2S - 主推3串进球数，对冲2串关)
          </div>
          <div class="grid grid-cols-1 sm:grid-cols-2 gap-3">
            <el-form-item label="主三副二 投注成本(元)" prop="strategy3m2sCost">
              <el-input-number
                v-model="form.strategy3m2sCost"
                :min="0"
                :precision="2"
                :step="50"
                controls-position="right"
                class="!w-full"
                placeholder="成本金额"
              />
            </el-form-item>
            <el-form-item label="主三副二 净利润(元，亏损填负数)" prop="strategy3m2sProfit">
              <el-input-number
                v-model="form.strategy3m2sProfit"
                :precision="2"
                :step="50"
                controls-position="right"
                class="!w-full"
                placeholder="净利润(可为负)"
              />
            </el-form-item>
          </div>
        </div>

        <!-- 策略二：主二串副三串 (2M3S) -->
        <div class="mt-3 p-3.5 rounded-2xl bg-slate-950/80 border border-cyan-500/30">
          <div class="text-xs font-bold text-cyan-400 mb-2 flex items-center gap-1.5">
            <span>🛡️</span> 主二串副三串进球策略 (2M3S - 主推2串进球数，对冲3串关)
          </div>
          <div class="grid grid-cols-1 sm:grid-cols-2 gap-3">
            <el-form-item label="主二副三 投注成本(元)" prop="strategy2m3sCost">
              <el-input-number
                v-model="form.strategy2m3sCost"
                :min="0"
                :precision="2"
                :step="50"
                controls-position="right"
                class="!w-full"
                placeholder="成本金额"
              />
            </el-form-item>
            <el-form-item label="主二副三 净利润(元，亏损填负数)" prop="strategy2m3sProfit">
              <el-input-number
                v-model="form.strategy2m3sProfit"
                :precision="2"
                :step="50"
                controls-position="right"
                class="!w-full"
                placeholder="净利润(可为负)"
              />
            </el-form-item>
          </div>
        </div>

        <!-- 实时计算预览 Live Preview Card -->
        <div class="mt-3 p-3.5 rounded-2xl bg-slate-900 border border-slate-800 text-xs">
          <div class="font-bold text-slate-300 mb-2 flex items-center justify-between">
            <span>🧮 进球数大数据即时核算预览</span>
            <span class="text-[10px] text-slate-400 font-normal">系统自动计算</span>
          </div>
          <div class="grid grid-cols-2 sm:grid-cols-4 gap-2 text-center font-mono">
            <div class="p-2 rounded-xl bg-slate-950">
              <div class="text-[10px] text-slate-400">总比赛 / 广义吻合率</div>
              <div class="font-bold text-amber-300 mt-0.5">
                {{ computedPreview.totalMatches }} 场 ({{ computedPreview.generalizedFitRate }}%)
              </div>
            </div>
            <div class="p-2 rounded-xl bg-slate-950">
              <div class="text-[10px] text-slate-400">当日总成本</div>
              <div class="font-bold text-slate-200 mt-0.5">
                ¥{{ computedPreview.totalCost }}
              </div>
            </div>
            <div class="p-2 rounded-xl bg-slate-950">
              <div class="text-[10px] text-slate-400">当日总净利</div>
              <div
                class="font-black mt-0.5"
                :class="Number(computedPreview.totalProfit) >= 0 ? 'text-amber-400' : 'text-rose-400'"
              >
                {{ Number(computedPreview.totalProfit) >= 0 ? '+' : '' }}¥{{ computedPreview.totalProfit }}
              </div>
            </div>
            <div class="p-2 rounded-xl bg-slate-950">
              <div class="text-[10px] text-slate-400">综合 ROI</div>
              <div
                class="font-bold mt-0.5"
                :class="Number(computedPreview.roiRate) >= 0 ? 'text-amber-400' : 'text-rose-400'"
              >
                {{ Number(computedPreview.roiRate) >= 0 ? '+' : '' }}{{ computedPreview.roiRate }}%
              </div>
            </div>
          </div>
        </div>

        <!-- 复盘备注 -->
        <div class="mt-3">
          <el-form-item label="总进球数战报复盘与模式总结" prop="remarks">
            <el-input
              v-model="form.remarks"
              type="textarea"
              :rows="2"
              placeholder="例如：英超意甲进球数稳步落入2-3球区间，次选补防生效，双策略皆盈利"
            />
          </el-form-item>
        </div>
      </el-form>

      <template #footer>
        <div class="flex items-center justify-end gap-2 pt-2">
          <button
            @click="dialogVisible = false"
            class="px-4 py-2 rounded-xl text-xs font-semibold bg-slate-800 hover:bg-slate-700 text-slate-300 transition"
          >
            取消
          </button>
          <button
            @click="handleSubmitForm"
            :disabled="submitting"
            class="px-5 py-2 rounded-xl text-xs font-bold bg-gradient-to-r from-amber-400 to-orange-400 text-slate-950 hover:from-amber-300 hover:to-orange-300 shadow-md shadow-amber-500/20 transition flex items-center gap-1.5"
          >
            <span v-if="submitting" class="animate-spin">🔄</span>
            <span>{{ isEditing ? '保存修改' : '确认录入' }}</span>
          </button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, onUnmounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import * as echarts from 'echarts'
import api from '@/api'

// --- 状态定义 ---
const loading = ref(false)
const submitting = ref(false)
const dialogVisible = ref(false)
const isEditing = ref(false)
const formRef = ref(null)

const dateRange = ref([])
const summary = ref({})
const recordList = ref([])

// ECharts 引用与实例
const profitChartRef = ref(null)
const patternChartRef = ref(null)
const strategyChartRef = ref(null)
let profitChart = null
let patternChart = null
let strategyChart = null

// 日期快捷筛选
const dateShortcuts = [
  {
    text: '最近 7 天',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
      return [start, end]
    }
  },
  {
    text: '最近 30 天',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
      return [start, end]
    }
  },
  {
    text: '最近 90 天',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
      return [start, end]
    }
  }
]

// 录入表单
const form = reactive({
  id: null,
  matchDate: '',
  patternFitCount: 0,
  patternSlightCount: 0,
  patternMismatchCount: 0,
  strategy3m2sCost: 300,
  strategy3m2sProfit: 0,
  strategy2m3sCost: 200,
  strategy2m3sProfit: 0,
  remarks: ''
})

const formRules = {
  matchDate: [{ required: true, message: '请选择比赛日', trigger: 'change' }],
  patternFitCount: [{ required: true, message: '请输入符合规律场数', trigger: 'blur' }],
  patternSlightCount: [{ required: true, message: '请输入稍微符合规律场数', trigger: 'blur' }],
  patternMismatchCount: [{ required: true, message: '请输入完全不符合规律场数', trigger: 'blur' }],
  strategy3m2sCost: [{ required: true, message: '请输入3M2S成本', trigger: 'blur' }],
  strategy3m2sProfit: [{ required: true, message: '请输入3M2S利润', trigger: 'blur' }],
  strategy2m3sCost: [{ required: true, message: '请输入2M3S成本', trigger: 'blur' }],
  strategy2m3sProfit: [{ required: true, message: '请输入2M3S利润', trigger: 'blur' }]
}

// 弹窗内实时计算预览
const computedPreview = computed(() => {
  const fit = Number(form.patternFitCount) || 0
  const slight = Number(form.patternSlightCount) || 0
  const mismatch = Number(form.patternMismatchCount) || 0
  const totalMatches = fit + slight + mismatch
  const generalizedFitRate = totalMatches > 0 ? (((fit + slight) / totalMatches) * 100).toFixed(2) : '0.00'

  const c3 = Number(form.strategy3m2sCost) || 0
  const p3 = Number(form.strategy3m2sProfit) || 0
  const c2 = Number(form.strategy2m3sCost) || 0
  const p2 = Number(form.strategy2m3sProfit) || 0

  const totalCost = (c3 + c2).toFixed(2)
  const totalProfit = (p3 + p2).toFixed(2)
  const roiRate = Number(totalCost) > 0 ? (((p3 + p2) / (c3 + c2)) * 100).toFixed(2) : '0.00'

  return {
    totalMatches,
    generalizedFitRate,
    totalCost,
    totalProfit,
    roiRate
  }
})

// --- 数据获取 ---
const fetchAllData = async () => {
  loading.value = true
  try {
    const params = {}
    if (dateRange.value && dateRange.value.length === 2) {
      params.startDate = dateRange.value[0]
      params.endDate = dateRange.value[1]
    }

    const [summaryRes, listRes] = await Promise.all([
      api.getDailyGoalsProfitSummary(params),
      api.getDailyGoalsProfits(params)
    ])

    summary.value = summaryRes || {}
    recordList.value = listRes || []

    await nextTick()
    renderCharts()
  } catch (err) {
    console.error('获取总进球数成本利润数据失败:', err)
  } finally {
    loading.value = false
  }
}

const handleDateRangeChange = () => {
  fetchAllData()
}

// --- 图表渲染 ---
const renderCharts = () => {
  if (!recordList.value || recordList.value.length === 0) return

  const sorted = [...recordList.value].sort((a, b) => a.matchDate.localeCompare(b.matchDate))
  const dates = sorted.map(i => i.matchDate)
  const dailyProfits = sorted.map(i => Number(i.totalProfit || 0))

  let runningTotal = 0
  const cumulativeProfits = dailyProfits.map(p => {
    runningTotal += p
    return Number(runningTotal.toFixed(2))
  })

  // 1. Profit Trend Chart
  if (profitChartRef.value) {
    if (!profitChart) profitChart = echarts.init(profitChartRef.value)
    profitChart.setOption({
      backgroundColor: 'transparent',
      tooltip: {
        trigger: 'axis',
        backgroundColor: '#0f172a',
        borderColor: '#334155',
        textStyle: { color: '#f8fafc' },
        formatter: (params) => {
          let html = `<div class="font-bold mb-1">${params[0].name}</div>`
          params.forEach(p => {
            const val = p.value >= 0 ? `+¥${p.value}` : `-¥${Math.abs(p.value)}`
            html += `<div class="flex items-center justify-between gap-4 text-xs">
              <span style="color:${p.color}">● ${p.seriesName}:</span>
              <span class="font-mono font-bold">${val}</span>
            </div>`
          })
          return html
        }
      },
      legend: {
        data: ['单日净利润', '累计净利润'],
        textStyle: { color: '#94a3b8' },
        top: 0
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '8%',
        top: '15%',
        containLabel: true
      },
      xAxis: {
        type: 'category',
        data: dates,
        axisLine: { lineStyle: { color: '#334155' } },
        axisLabel: { color: '#94a3b8', fontSize: 11 }
      },
      yAxis: [
        {
          type: 'value',
          name: '单日净利(元)',
          nameTextStyle: { color: '#64748b' },
          splitLine: { lineStyle: { color: '#1e293b' } },
          axisLabel: { color: '#94a3b8', fontSize: 11 }
        },
        {
          type: 'value',
          name: '累计净利(元)',
          nameTextStyle: { color: '#64748b' },
          splitLine: { show: false },
          axisLabel: { color: '#94a3b8', fontSize: 11 }
        }
      ],
      series: [
        {
          name: '单日净利润',
          type: 'bar',
          data: dailyProfits.map(val => ({
            value: val,
            itemStyle: {
              color: val >= 0 ? '#f59e0b' : '#f43f5e',
              borderRadius: [4, 4, 0, 0]
            }
          })),
          barMaxWidth: 24
        },
        {
          name: '累计净利润',
          type: 'line',
          yAxisIndex: 1,
          data: cumulativeProfits,
          smooth: true,
          lineStyle: { width: 3, color: '#f97316' },
          itemStyle: { color: '#f97316' },
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: 'rgba(249, 115, 22, 0.35)' },
              { offset: 1, color: 'rgba(249, 115, 22, 0.0)' }
            ])
          }
        }
      ]
    })
  }

  // 2. Pattern Trend Chart (Stacked bar + Generalized fit rate line)
  if (patternChartRef.value) {
    if (!patternChart) patternChart = echarts.init(patternChartRef.value)
    const fitCounts = sorted.map(i => i.patternFitCount || 0)
    const slightCounts = sorted.map(i => i.patternSlightCount || 0)
    const mismatchCounts = sorted.map(i => i.patternMismatchCount || 0)
    const genFitRates = sorted.map(i => Number(i.generalizedFitRate || 0))

    patternChart.setOption({
      backgroundColor: 'transparent',
      tooltip: {
        trigger: 'axis',
        backgroundColor: '#0f172a',
        borderColor: '#334155',
        textStyle: { color: '#f8fafc' }
      },
      legend: {
        data: ['符合规律', '稍微符合', '完全不符', '广义吻合率(%)'],
        textStyle: { color: '#94a3b8' },
        top: 0
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '8%',
        top: '15%',
        containLabel: true
      },
      xAxis: {
        type: 'category',
        data: dates,
        axisLine: { lineStyle: { color: '#334155' } },
        axisLabel: { color: '#94a3b8', fontSize: 11 }
      },
      yAxis: [
        {
          type: 'value',
          name: '场次',
          nameTextStyle: { color: '#64748b' },
          splitLine: { lineStyle: { color: '#1e293b' } },
          axisLabel: { color: '#94a3b8' }
        },
        {
          type: 'value',
          name: '广义吻合率(%)',
          max: 100,
          min: 0,
          nameTextStyle: { color: '#64748b' },
          splitLine: { show: false },
          axisLabel: { color: '#94a3b8', formatter: '{value}%' }
        }
      ],
      series: [
        {
          name: '符合规律',
          type: 'bar',
          stack: 'pattern',
          data: fitCounts,
          itemStyle: { color: '#10b981' },
          barMaxWidth: 20
        },
        {
          name: '稍微符合',
          type: 'bar',
          stack: 'pattern',
          data: slightCounts,
          itemStyle: { color: '#f59e0b' },
          barMaxWidth: 20
        },
        {
          name: '完全不符',
          type: 'bar',
          stack: 'pattern',
          data: mismatchCounts,
          itemStyle: { color: '#475569', borderRadius: [4, 4, 0, 0] },
          barMaxWidth: 20
        },
        {
          name: '广义吻合率(%)',
          type: 'line',
          yAxisIndex: 1,
          data: genFitRates,
          smooth: true,
          lineStyle: { width: 2.5, color: '#38bdf8' },
          itemStyle: { color: '#38bdf8' }
        }
      ]
    })
  }

  // 3. Strategy PK Chart
  if (strategyChartRef.value) {
    if (!strategyChart) strategyChart = echarts.init(strategyChartRef.value)
    const profit3m2s = sorted.map(i => Number(i.strategy3m2sProfit || 0))
    const profit2m3s = sorted.map(i => Number(i.strategy2m3sProfit || 0))

    strategyChart.setOption({
      backgroundColor: 'transparent',
      tooltip: {
        trigger: 'axis',
        backgroundColor: '#0f172a',
        borderColor: '#334155',
        textStyle: { color: '#f8fafc' },
        formatter: (params) => {
          let html = `<div class="font-bold mb-1">${params[0].name}</div>`
          params.forEach(p => {
            const val = p.value >= 0 ? `+¥${p.value}` : `-¥${Math.abs(p.value)}`
            html += `<div class="flex items-center justify-between gap-4 text-xs">
              <span style="color:${p.color}">● ${p.seriesName}:</span>
              <span class="font-mono font-bold">${val}</span>
            </div>`
          })
          return html
        }
      },
      legend: {
        data: ['主三串副二串 (3M2S)', '主二串副三串 (2M3S)'],
        textStyle: { color: '#94a3b8' },
        top: 0
      },
      grid: {
        left: '2%',
        right: '2%',
        bottom: '8%',
        top: '15%',
        containLabel: true
      },
      xAxis: {
        type: 'category',
        data: dates,
        axisLine: { lineStyle: { color: '#334155' } },
        axisLabel: { color: '#94a3b8', fontSize: 11 }
      },
      yAxis: {
        type: 'value',
        name: '净利润(元)',
        nameTextStyle: { color: '#64748b' },
        splitLine: { lineStyle: { color: '#1e293b' } },
        axisLabel: { color: '#94a3b8' }
      },
      series: [
        {
          name: '主三串副二串 (3M2S)',
          type: 'line',
          data: profit3m2s,
          smooth: true,
          lineStyle: { width: 2.5, color: '#f59e0b' },
          itemStyle: { color: '#f59e0b' },
          symbol: 'circle',
          symbolSize: 6
        },
        {
          name: '主二串副三串 (2M3S)',
          type: 'line',
          data: profit2m3s,
          smooth: true,
          lineStyle: { width: 2.5, color: '#06b6d4' },
          itemStyle: { color: '#06b6d4' },
          symbol: 'diamond',
          symbolSize: 7
        }
      ]
    })
  }
}

// 响应式 Resize
const handleResize = () => {
  profitChart?.resize()
  patternChart?.resize()
  strategyChart?.resize()
}

// --- 表单提交与弹窗 ---
const handleOpenCreateDialog = () => {
  isEditing.value = false
  const today = new Date().toISOString().split('T')[0]
  Object.assign(form, {
    id: null,
    matchDate: today,
    patternFitCount: 0,
    patternSlightCount: 0,
    patternMismatchCount: 0,
    strategy3m2sCost: 300,
    strategy3m2sProfit: 0,
    strategy2m3sCost: 200,
    strategy2m3sProfit: 0,
    remarks: ''
  })
  dialogVisible.value = true
}

const handleEdit = (record) => {
  isEditing.value = true
  Object.assign(form, {
    id: record.id,
    matchDate: record.matchDate,
    patternFitCount: record.patternFitCount,
    patternSlightCount: record.patternSlightCount,
    patternMismatchCount: record.patternMismatchCount,
    strategy3m2sCost: record.strategy3m2sCost,
    strategy3m2sProfit: record.strategy3m2sProfit,
    strategy2m3sCost: record.strategy2m3sCost,
    strategy2m3sProfit: record.strategy2m3sProfit,
    remarks: record.remarks || ''
  })
  dialogVisible.value = true
}

const handleSubmitForm = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    submitting.value = true
    try {
      if (isEditing.value) {
        await api.updateDailyGoalsProfit(form.id, form)
        ElMessage.success('总进球数战绩修改成功')
      } else {
        await api.createDailyGoalsProfit(form)
        ElMessage.success('总进球数战绩录入成功')
      }
      dialogVisible.value = false
      fetchAllData()
    } catch (err) {
      console.error(err)
    } finally {
      submitting.value = false
    }
  })
}

const handleDelete = (record) => {
  ElMessageBox.confirm(
    `确定要删除比赛日「${record.matchDate}」的总进球数战绩记录吗？`,
    '删除确认',
    {
      confirmButtonText: '确定删除',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await api.deleteDailyGoalsProfit(record.id)
      ElMessage.success('记录已删除')
      fetchAllData()
    } catch (err) {
      console.error(err)
    }
  })
}

const formatNumber = (val) => {
  if (val === undefined || val === null || isNaN(val)) return '0.00'
  return Number(val).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

const getTrendBadgeClass = (trend) => {
  switch (trend) {
    case 'STABLE_PATTERN':
      return 'bg-emerald-500/20 text-emerald-300 border border-emerald-500/30'
    case 'SLIGHT_FLUCTUATION':
      return 'bg-amber-500/20 text-amber-300 border border-amber-500/30'
    case 'HIGH_CHAOS':
      return 'bg-rose-500/20 text-rose-300 border border-rose-500/30'
    default:
      return 'bg-slate-800 text-slate-400'
  }
}

const getTrendText = (trend) => {
  switch (trend) {
    case 'STABLE_PATTERN': return '🎯 规律稳定'
    case 'SLIGHT_FLUCTUATION': return '⚡ 偶有波动'
    case 'HIGH_CHAOS': return '🌪️ 暴走反常'
    default: return '均衡'
  }
}

onMounted(() => {
  fetchAllData()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  profitChart?.dispose()
  patternChart?.dispose()
  strategyChart?.dispose()
})
</script>

<style scoped>
.glass-panel {
  backdrop-filter: blur(12px);
}
.glass-card {
  backdrop-filter: blur(8px);
}

:deep(.custom-dark-dialog) {
  background-color: #090d16 !important;
  border: 1px solid #1e293b;
  border-radius: 1.25rem;
}

:deep(.el-dialog__title) {
  color: #f8fafc !important;
  font-weight: 800;
  font-size: 1.1rem;
}

:deep(.el-form-item__label) {
  color: #94a3b8 !important;
  font-size: 0.75rem;
  font-weight: 600;
}

:deep(.el-input__wrapper),
:deep(.el-textarea__inner) {
  background-color: #0f172a !important;
  border-color: #334155 !important;
  color: #f8fafc !important;
  box-shadow: none !important;
}

:deep(.el-input__inner) {
  color: #f8fafc !important;
}

:deep(.el-input-number__decrease),
:deep(.el-input-number__increase) {
  background-color: #1e293b !important;
  color: #94a3b8 !important;
  border-color: #334155 !important;
}
</style>
