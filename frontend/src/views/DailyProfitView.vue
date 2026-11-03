<template>
  <div class="space-y-6 sm:space-y-8 pb-12">
    <!-- Mode Switcher Tabs: 穿盘版 vs 进球数版 vs 水位翻车版 vs 水位串子版 -->
    <div class="flex items-center justify-between flex-wrap gap-3">
      <div class="flex items-center gap-1.5 p-1 rounded-2xl bg-slate-950/80 border border-slate-800">
        <router-link
          to="/daily-profit"
          class="px-4 py-2 rounded-xl text-xs sm:text-sm font-bold transition flex items-center gap-2 bg-emerald-500 text-slate-950 shadow-md shadow-emerald-500/20 font-extrabold"
        >
          <span>⚽ 让球穿盘战绩版</span>
        </router-link>
        <router-link
          to="/daily-goals-profit"
          class="px-4 py-2 rounded-xl text-xs sm:text-sm font-bold transition flex items-center gap-2 text-slate-400 hover:text-white hover:bg-slate-900"
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
        <span class="w-2 h-2 rounded-full bg-emerald-400 animate-pulse"></span>
        <span>当前模式: 足球让球穿盘与双串关策略复盘</span>
      </div>
    </div>

    <!-- 1. Hero Banner -->
    <div class="relative overflow-hidden rounded-3xl bg-gradient-to-r from-slate-900 via-emerald-950/30 to-slate-900 border border-slate-800 p-5 sm:p-8 shadow-2xl">
      <div class="absolute -right-10 -top-10 w-80 h-80 bg-emerald-500/10 rounded-full blur-3xl pointer-events-none"></div>
      <div class="absolute -left-10 -bottom-10 w-80 h-80 bg-cyan-500/10 rounded-full blur-3xl pointer-events-none"></div>

      <div class="relative z-10 flex flex-col lg:flex-row lg:items-center lg:justify-between gap-6">
        <div>
          <div class="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-emerald-500/10 border border-emerald-500/20 text-emerald-400 text-xs font-bold mb-3">
            <span>📈</span> 大数据实战战报
          </div>
          <h1 class="text-2xl sm:text-3xl md:text-4xl font-extrabold text-white tracking-tight flex items-center gap-3">
            每日成本利润与串关策略复盘
          </h1>
          <p class="text-slate-400 text-xs sm:text-sm mt-2 max-w-2xl leading-relaxed">
            记录每日投注成本、净利润、穿盘率与<strong>主三串副二串 (3M2S)</strong>、<strong>主二串副三串 (2M3S)</strong> 双策略攻守对冲的大数据收益演进。
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
            class="px-4 py-2 rounded-xl text-xs sm:text-sm font-bold bg-gradient-to-r from-emerald-500 to-teal-400 text-slate-950 hover:from-emerald-400 hover:to-teal-300 shadow-lg shadow-emerald-500/20 transition-all flex items-center gap-1.5 shrink-0"
          >
            <span>➕</span> 录入今日战绩
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
      <!-- Card 1: 累计净利润 -->
      <div class="glass-card rounded-2xl p-4 sm:p-5 border border-slate-800 bg-slate-900/60 relative overflow-hidden group hover:border-emerald-500/40 transition-all">
        <div class="flex items-center justify-between text-slate-400 text-xs mb-2">
          <span class="font-semibold">💰 累计净利润</span>
          <span class="text-[10px] px-2 py-0.5 rounded-full bg-slate-800 text-slate-300">
            {{ summary.totalDays || 0 }} 比赛日
          </span>
        </div>
        <div
          class="text-2xl sm:text-3xl font-black font-mono tracking-tight"
          :class="Number(summary.totalProfit || 0) >= 0 ? 'text-emerald-400' : 'text-rose-400'"
        >
          {{ Number(summary.totalProfit || 0) >= 0 ? '+' : '' }}¥{{ formatNumber(summary.totalProfit) }}
        </div>
        <div class="mt-2.5 pt-2.5 border-t border-slate-800/80 flex items-center justify-between text-[11px] text-slate-400">
          <span>总派彩返还:</span>
          <span class="font-mono font-bold text-slate-200">¥{{ formatNumber(summary.totalReturn) }}</span>
        </div>
      </div>

      <!-- Card 2: 综合投资回报率 ROI -->
      <div class="glass-card rounded-2xl p-4 sm:p-5 border border-slate-800 bg-slate-900/60 relative overflow-hidden group hover:border-emerald-500/40 transition-all">
        <div class="flex items-center justify-between text-slate-400 text-xs mb-2">
          <span class="font-semibold">📈 综合回报率 ROI</span>
          <span
            class="text-[10px] px-2 py-0.5 rounded-full font-bold"
            :class="Number(summary.overallRoi || 0) >= 20 ? 'bg-emerald-500/20 text-emerald-300 border border-emerald-500/30' : Number(summary.overallRoi || 0) >= 0 ? 'bg-amber-500/20 text-amber-300 border border-amber-500/30' : 'bg-rose-500/20 text-rose-300 border border-rose-500/30'"
          >
            {{ Number(summary.overallRoi || 0) >= 20 ? '🔥 卓越盈利' : Number(summary.overallRoi || 0) >= 0 ? '✨ 正收益' : '⚠️ 回撤状态' }}
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
          <span class="font-mono font-bold text-emerald-400">{{ summary.winDaysRate || '0.00' }}% ({{ summary.profitableDays || 0 }}/{{ summary.totalDays || 0 }}天)</span>
        </div>
      </div>

      <!-- Card 3: 累计总投注成本 -->
      <div class="glass-card rounded-2xl p-4 sm:p-5 border border-slate-800 bg-slate-900/60 relative overflow-hidden group hover:border-slate-700 transition-all">
        <div class="flex items-center justify-between text-slate-400 text-xs mb-2">
          <span class="font-semibold">💵 累计投注总成本</span>
          <span class="text-[10px] px-2 py-0.5 rounded-full bg-slate-800 text-slate-400">本金沉淀</span>
        </div>
        <div class="text-2xl sm:text-3xl font-black font-mono text-slate-200 tracking-tight">
          ¥{{ formatNumber(summary.totalCost) }}
        </div>
        <div class="mt-2.5 pt-2.5 border-t border-slate-800/80 flex items-center justify-between text-[11px] text-slate-400">
          <span>单日均投:</span>
          <span class="font-mono text-slate-300">
            ¥{{ summary.totalDays > 0 ? (Number(summary.totalCost || 0) / summary.totalDays).toFixed(2) : '0.00' }}
          </span>
        </div>
      </div>

      <!-- Card 4: 穿盘大数据概览 -->
      <div class="glass-card rounded-2xl p-4 sm:p-5 border border-slate-800 bg-slate-900/60 relative overflow-hidden group hover:border-cyan-500/40 transition-all">
        <div class="flex items-center justify-between text-slate-400 text-xs mb-2">
          <span class="font-semibold">🎯 整体穿盘表现</span>
          <span class="text-[10px] px-2 py-0.5 rounded-full bg-cyan-500/10 text-cyan-400 border border-cyan-500/20 font-bold">
            {{ Number(summary.avgBreakRate || 0) >= 50 ? '大盘偏深' : '冷门频出' }}
          </span>
        </div>
        <div class="text-2xl sm:text-3xl font-black font-mono text-cyan-300 tracking-tight">
          {{ summary.avgBreakRate || '0.00' }}%
        </div>
        <div class="mt-2.5 pt-2.5 border-t border-slate-800/80 flex items-center justify-between text-[11px] text-slate-400">
          <span>穿盘 / 未穿:</span>
          <span class="font-mono text-slate-200">
            <span class="text-cyan-400 font-bold">{{ summary.totalBreakCount || 0 }}</span> 穿 /
            <span class="text-slate-400">{{ summary.totalUnbreakCount || 0 }}</span> 未穿 (共{{ summary.totalMatches || 0 }}场)
          </span>
        </div>
      </div>

      <!-- Card 5: 双策略攻守对冲 PK -->
      <div class="glass-card rounded-2xl p-4 sm:p-5 border border-indigo-500/30 bg-gradient-to-b from-indigo-950/30 to-slate-900 relative overflow-hidden group hover:border-indigo-500/50 transition-all">
        <div class="flex items-center justify-between text-slate-400 text-xs mb-2">
          <span class="font-semibold text-indigo-300">⚔️ 策略对冲 PK</span>
          <span class="text-[10px] px-2 py-0.5 rounded-full bg-indigo-500/20 text-indigo-300 font-bold border border-indigo-500/30">
            最优: {{ summary.bestStrategy?.split(' ')[0] || '均衡' }}
          </span>
        </div>
        <div class="space-y-1.5 my-1">
          <div class="flex items-center justify-between text-xs font-mono">
            <span class="text-slate-300 font-medium">3串2副:</span>
            <span :class="Number(summary.total3m2sProfit || 0) >= 0 ? 'text-emerald-400 font-bold' : 'text-rose-400 font-bold'">
              ¥{{ formatNumber(summary.total3m2sProfit) }}
              <span class="text-[10px] text-slate-400 font-normal">({{ summary.roi3m2s || '0.00' }}%)</span>
            </span>
          </div>
          <div class="flex items-center justify-between text-xs font-mono">
            <span class="text-slate-300 font-medium">2串3副:</span>
            <span :class="Number(summary.total2m3sProfit || 0) >= 0 ? 'text-teal-400 font-bold' : 'text-rose-400 font-bold'">
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
              <span>📊</span> 收益与回报走势大数据
            </h2>
            <p class="text-slate-400 text-xs mt-0.5">每日净利润柱状图与累计净利润走势演进</p>
          </div>
          <div class="flex items-center gap-2 text-xs">
            <span class="inline-flex items-center gap-1 text-emerald-400">
              <span class="w-2.5 h-2.5 rounded-sm bg-emerald-500"></span> 单日盈利
            </span>
            <span class="inline-flex items-center gap-1 text-rose-400">
              <span class="w-2.5 h-2.5 rounded-sm bg-rose-500"></span> 单日亏损
            </span>
            <span class="inline-flex items-center gap-1 text-cyan-400">
              <span class="w-2.5 h-0.5 bg-cyan-400"></span> 累计净利润
            </span>
          </div>
        </div>
        <div ref="profitChartRef" class="w-full h-72 sm:h-80"></div>
      </div>

      <!-- Chart 2: 穿盘分布与穿盘率走势 (占 1 列) -->
      <div class="glass-panel rounded-3xl p-4 sm:p-6 border border-slate-800 shadow-xl bg-slate-900/70">
        <div class="mb-4">
          <h2 class="text-base sm:text-lg font-bold text-white flex items-center gap-2">
            <span>🎯</span> 穿盘比赛分布与穿盘率
          </h2>
          <p class="text-slate-400 text-xs mt-0.5">盘口穿透趋势与冷门分布指标</p>
        </div>
        <div ref="breakChartRef" class="w-full h-72 sm:h-80"></div>
      </div>

      <!-- Chart 3: 3M2S vs 2M3S 策略收益对比走势 (全宽) -->
      <div class="lg:col-span-3 glass-panel rounded-3xl p-4 sm:p-6 border border-slate-800 shadow-xl bg-slate-900/70">
        <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-2 mb-4">
          <div>
            <h2 class="text-base sm:text-lg font-bold text-white flex items-center gap-2">
              <span>⚖️</span> 主三串副二串 vs 主二串副三串 策略对战演进
            </h2>
            <p class="text-slate-400 text-xs mt-0.5">
              对比进攻型高赔组合（3M2S）与防守稳健型组合（2M3S）在不同赛程日期的对冲收益表现
            </p>
          </div>
          <div class="flex items-center gap-3 text-xs">
            <span class="inline-flex items-center gap-1 text-indigo-400 font-medium">
              <span class="w-3 h-1 bg-indigo-500 rounded"></span> 主三串副二串 (3M2S)
            </span>
            <span class="inline-flex items-center gap-1 text-teal-400 font-medium">
              <span class="w-3 h-1 bg-teal-400 rounded"></span> 主二串副三串 (2M3S)
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
            <span>📋</span> 每日成本利润详细记录明细
          </h2>
          <p class="text-slate-400 text-xs mt-0.5">包含每场次穿盘指标、双策略投入产出及复盘总结</p>
        </div>
        <div class="text-xs text-slate-400 font-mono">
          共收录 {{ recordList.length }} 笔比赛日数据
        </div>
      </div>

      <!-- Desktop & Tablet Table -->
      <div class="overflow-x-auto">
        <table class="w-full text-left text-sm">
          <thead>
            <tr class="border-b border-slate-800 text-xs font-semibold text-slate-400">
              <th class="py-3 px-3">比赛日</th>
              <th class="py-3 px-3">穿盘赛果</th>
              <th class="py-3 px-3 text-right">主三副二 (3M2S)</th>
              <th class="py-3 px-3 text-right">主二副三 (2M3S)</th>
              <th class="py-3 px-3 text-right">当日总成本</th>
              <th class="py-3 px-3 text-right">当日总净利</th>
              <th class="py-3 px-3 text-right">综合 ROI</th>
              <th class="py-3 px-3 text-center">优势策略</th>
              <th class="py-3 px-3 text-center">评级</th>
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

              <!-- 穿盘赛果 -->
              <td class="py-3.5 px-3 whitespace-nowrap">
                <div class="flex items-center gap-1.5">
                  <span class="font-mono text-xs text-slate-300">
                    <span class="text-cyan-400 font-bold">{{ record.breakCount }}</span> 穿 /
                    <span class="text-slate-400">{{ record.unbreakCount }}</span> 否
                  </span>
                  <span
                    class="text-[10px] px-1.5 py-0.5 rounded font-bold font-mono"
                    :class="Number(record.breakRate || 0) >= 50 ? 'bg-cyan-500/20 text-cyan-300 border border-cyan-500/30' : 'bg-slate-800 text-slate-400'"
                  >
                    {{ record.breakRate }}%
                  </span>
                </div>
              </td>

              <!-- 主三副二 (3M2S) -->
              <td class="py-3.5 px-3 text-right whitespace-nowrap font-mono text-xs">
                <div class="text-slate-400">投: ¥{{ formatNumber(record.strategy3m2sCost) }}</div>
                <div
                  class="font-bold"
                  :class="Number(record.strategy3m2sProfit || 0) >= 0 ? 'text-emerald-400' : 'text-rose-400'"
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
                  :class="Number(record.strategy2m3sProfit || 0) >= 0 ? 'text-teal-400' : 'text-rose-400'"
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
                <span :class="Number(record.totalProfit || 0) >= 0 ? 'text-emerald-400' : 'text-rose-400'">
                  {{ Number(record.totalProfit || 0) >= 0 ? '+' : '' }}¥{{ formatNumber(record.totalProfit) }}
                </span>
              </td>

              <!-- 综合 ROI -->
              <td class="py-3.5 px-3 text-right whitespace-nowrap font-mono text-xs font-bold">
                <span :class="Number(record.roiRate || 0) >= 0 ? 'text-emerald-400' : 'text-rose-400'">
                  {{ Number(record.roiRate || 0) >= 0 ? '+' : '' }}{{ record.roiRate }}%
                </span>
              </td>

              <!-- 优势策略 -->
              <td class="py-3.5 px-3 text-center whitespace-nowrap">
                <span
                  class="text-[11px] px-2 py-0.5 rounded-full font-bold"
                  :class="record.dominantStrategy === 'STRATEGY_3M2S' ? 'bg-indigo-500/20 text-indigo-300 border border-indigo-500/30' : record.dominantStrategy === 'STRATEGY_2M3S' ? 'bg-teal-500/20 text-teal-300 border border-teal-500/30' : 'bg-slate-800 text-slate-400'"
                >
                  {{ record.dominantStrategy === 'STRATEGY_3M2S' ? '🔥 3串2副' : record.dominantStrategy === 'STRATEGY_2M3S' ? '🛡️ 2串3副' : '🤝 均衡' }}
                </span>
              </td>

              <!-- 评级 -->
              <td class="py-3.5 px-3 text-center whitespace-nowrap">
                <span
                  class="text-[10px] px-2 py-0.5 rounded-md font-bold"
                  :class="getPerformanceBadgeClass(record.performanceLevel)"
                >
                  {{ getPerformanceText(record.performanceLevel) }}
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
                    class="p-1.5 rounded-lg bg-slate-800 hover:bg-slate-700 text-emerald-400 text-xs transition"
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
              <td colspan="11" class="text-center py-12 text-slate-500 text-sm">
                暂无战绩记录，点击右上角「➕ 录入今日战绩」开始记录吧！
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- 5. Dialog: 录入 / 编辑成本利润战绩 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEditing ? '✏️ 修改比赛日战绩' : '➕ 录入比赛日成本利润战绩'"
      width="640px"
      destroy-on-close
      class="custom-dark-dialog"
    >
      <el-form :model="form" :rules="formRules" ref="formRef" label-position="top">
        <div class="grid grid-cols-1 sm:grid-cols-3 gap-3">
          <!-- 比赛日 -->
          <el-form-item label="比赛日" prop="matchDate" class="sm:col-span-1">
            <el-date-picker
              v-model="form.matchDate"
              type="date"
              placeholder="选择比赛日"
              value-format="YYYY-MM-DD"
              class="!w-full"
            />
          </el-form-item>

          <!-- 穿盘比赛数 -->
          <el-form-item label="穿盘比赛数量" prop="breakCount" class="sm:col-span-1">
            <el-input-number
              v-model="form.breakCount"
              :min="0"
              :step="1"
              controls-position="right"
              class="!w-full"
              placeholder="如 5"
            />
          </el-form-item>

          <!-- 未穿盘比赛数 -->
          <el-form-item label="未穿盘比赛数量" prop="unbreakCount" class="sm:col-span-1">
            <el-input-number
              v-model="form.unbreakCount"
              :min="0"
              :step="1"
              controls-position="right"
              class="!w-full"
              placeholder="如 3"
            />
          </el-form-item>
        </div>

        <!-- 策略一：主三串副二串 (3M2S) -->
        <div class="mt-2 p-3.5 rounded-2xl bg-slate-950/80 border border-indigo-500/30">
          <div class="text-xs font-bold text-indigo-400 mb-2 flex items-center gap-1.5">
            <span>⚡</span> 主三串副二串策略 (3M2S - 主推3串关，对冲2串关)
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
        <div class="mt-3 p-3.5 rounded-2xl bg-slate-950/80 border border-teal-500/30">
          <div class="text-xs font-bold text-teal-400 mb-2 flex items-center gap-1.5">
            <span>🛡️</span> 主二串副三串策略 (2M3S - 主推2串关，对冲3串关)
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
            <span>🧮 战绩大数据即时核算预览</span>
            <span class="text-[10px] text-slate-400 font-normal">系统自动计算</span>
          </div>
          <div class="grid grid-cols-2 sm:grid-cols-4 gap-2 text-center font-mono">
            <div class="p-2 rounded-xl bg-slate-950">
              <div class="text-[10px] text-slate-400">总比赛 / 穿盘率</div>
              <div class="font-bold text-cyan-300 mt-0.5">
                {{ computedPreview.totalMatches }} 场 ({{ computedPreview.breakRate }}%)
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
                :class="Number(computedPreview.totalProfit) >= 0 ? 'text-emerald-400' : 'text-rose-400'"
              >
                {{ Number(computedPreview.totalProfit) >= 0 ? '+' : '' }}¥{{ computedPreview.totalProfit }}
              </div>
            </div>
            <div class="p-2 rounded-xl bg-slate-950">
              <div class="text-[10px] text-slate-400">综合 ROI</div>
              <div
                class="font-bold mt-0.5"
                :class="Number(computedPreview.roiRate) >= 0 ? 'text-emerald-400' : 'text-rose-400'"
              >
                {{ Number(computedPreview.roiRate) >= 0 ? '+' : '' }}{{ computedPreview.roiRate }}%
              </div>
            </div>
          </div>
        </div>

        <!-- 复盘备注 -->
        <div class="mt-3">
          <el-form-item label="战绩复盘与特征备注" prop="remarks">
            <el-input
              v-model="form.remarks"
              type="textarea"
              :rows="2"
              placeholder="例如：周六五大联赛深盘全出，3M2S攻势大胜，防守单微盈"
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
            class="px-5 py-2 rounded-xl text-xs font-bold bg-gradient-to-r from-emerald-500 to-teal-400 text-slate-950 hover:from-emerald-400 hover:to-teal-300 shadow-md shadow-emerald-500/20 transition flex items-center gap-1.5"
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
const breakChartRef = ref(null)
const strategyChartRef = ref(null)
let profitChart = null
let breakChart = null
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
  breakCount: 0,
  unbreakCount: 0,
  strategy3m2sCost: 0,
  strategy3m2sProfit: 0,
  strategy2m3sCost: 0,
  strategy2m3sProfit: 0,
  remarks: ''
})

const formRules = {
  matchDate: [{ required: true, message: '请选择比赛日', trigger: 'change' }],
  breakCount: [{ required: true, message: '请输入穿盘比赛数', trigger: 'blur' }],
  unbreakCount: [{ required: true, message: '请输入未穿盘比赛数', trigger: 'blur' }],
  strategy3m2sCost: [{ required: true, message: '请输入3M2S成本', trigger: 'blur' }],
  strategy3m2sProfit: [{ required: true, message: '请输入3M2S利润', trigger: 'blur' }],
  strategy2m3sCost: [{ required: true, message: '请输入2M3S成本', trigger: 'blur' }],
  strategy2m3sProfit: [{ required: true, message: '请输入2M3S利润', trigger: 'blur' }]
}

// 弹窗内实时计算预览
const computedPreview = computed(() => {
  const b = Number(form.breakCount) || 0
  const ub = Number(form.unbreakCount) || 0
  const totalMatches = b + ub
  const breakRate = totalMatches > 0 ? ((b / totalMatches) * 100).toFixed(2) : '0.00'

  const c3 = Number(form.strategy3m2sCost) || 0
  const p3 = Number(form.strategy3m2sProfit) || 0
  const c2 = Number(form.strategy2m3sCost) || 0
  const p2 = Number(form.strategy2m3sProfit) || 0

  const totalCost = (c3 + c2).toFixed(2)
  const totalProfit = (p3 + p2).toFixed(2)
  const roiRate = Number(totalCost) > 0 ? (((p3 + p2) / (c3 + c2)) * 100).toFixed(2) : '0.00'

  return {
    totalMatches,
    breakRate,
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
      api.getDailyProfitSummary(params),
      api.getDailyProfits(params)
    ])

    summary.value = summaryRes || {}
    recordList.value = listRes || []

    await nextTick()
    renderCharts()
  } catch (err) {
    console.error('获取每日成本利润数据失败:', err)
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

  // 按日期升序排列渲染图表
  const sorted = [...recordList.value].sort((a, b) => a.matchDate.localeCompare(b.matchDate))
  const dates = sorted.map(i => i.matchDate)
  const dailyProfits = sorted.map(i => Number(i.totalProfit || 0))

  // 计算累计利润曲线
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
              color: val >= 0 ? '#10b981' : '#f43f5e',
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
          lineStyle: { width: 3, color: '#06b6d4' },
          itemStyle: { color: '#06b6d4' },
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: 'rgba(6, 182, 212, 0.35)' },
              { offset: 1, color: 'rgba(6, 182, 212, 0.0)' }
            ])
          }
        }
      ]
    })
  }

  // 2. Break Rate Chart
  if (breakChartRef.value) {
    if (!breakChart) breakChart = echarts.init(breakChartRef.value)
    const breakCounts = sorted.map(i => i.breakCount || 0)
    const unbreakCounts = sorted.map(i => i.unbreakCount || 0)
    const breakRates = sorted.map(i => Number(i.breakRate || 0))

    breakChart.setOption({
      backgroundColor: 'transparent',
      tooltip: {
        trigger: 'axis',
        backgroundColor: '#0f172a',
        borderColor: '#334155',
        textStyle: { color: '#f8fafc' }
      },
      legend: {
        data: ['穿盘场次', '未穿盘场次', '穿盘率(%)'],
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
          name: '穿盘率(%)',
          max: 100,
          min: 0,
          nameTextStyle: { color: '#64748b' },
          splitLine: { show: false },
          axisLabel: { color: '#94a3b8', formatter: '{value}%' }
        }
      ],
      series: [
        {
          name: '穿盘场次',
          type: 'bar',
          stack: 'matches',
          data: breakCounts,
          itemStyle: { color: '#06b6d4', borderRadius: [0, 0, 0, 0] },
          barMaxWidth: 20
        },
        {
          name: '未穿盘场次',
          type: 'bar',
          stack: 'matches',
          data: unbreakCounts,
          itemStyle: { color: '#475569', borderRadius: [4, 4, 0, 0] },
          barMaxWidth: 20
        },
        {
          name: '穿盘率(%)',
          type: 'line',
          yAxisIndex: 1,
          data: breakRates,
          smooth: true,
          lineStyle: { width: 2.5, color: '#eab308' },
          itemStyle: { color: '#eab308' }
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
          lineStyle: { width: 2.5, color: '#6366f1' },
          itemStyle: { color: '#6366f1' },
          symbol: 'circle',
          symbolSize: 6
        },
        {
          name: '主二串副三串 (2M3S)',
          type: 'line',
          data: profit2m3s,
          smooth: true,
          lineStyle: { width: 2.5, color: '#14b8a6' },
          itemStyle: { color: '#14b8a6' },
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
  breakChart?.resize()
  strategyChart?.resize()
}

// --- 表单提交与弹窗 ---
const handleOpenCreateDialog = () => {
  isEditing.value = false
  // 默认填充今天日期与0值
  const today = new Date().toISOString().split('T')[0]
  Object.assign(form, {
    id: null,
    matchDate: today,
    breakCount: 0,
    unbreakCount: 0,
    strategy3m2sCost: 200,
    strategy3m2sProfit: 0,
    strategy2m3sCost: 300,
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
    breakCount: record.breakCount,
    unbreakCount: record.unbreakCount,
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
        await api.updateDailyProfit(form.id, form)
        ElMessage.success('每日战绩记录修改成功')
      } else {
        await api.createDailyProfit(form)
        ElMessage.success('每日战绩录入成功')
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
    `确定要删除比赛日「${record.matchDate}」的战绩记录吗？`,
    '删除确认',
    {
      confirmButtonText: '确定删除',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await api.deleteDailyProfit(record.id)
      ElMessage.success('记录已删除')
      fetchAllData()
    } catch (err) {
      console.error(err)
    }
  })
}

// 格式化数字
const formatNumber = (val) => {
  if (val === undefined || val === null || isNaN(val)) return '0.00'
  return Number(val).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

// 评级颜色与文字
const getPerformanceBadgeClass = (level) => {
  switch (level) {
    case 'GREAT_PROFIT':
      return 'bg-emerald-500/20 text-emerald-300 border border-emerald-500/30'
    case 'PROFIT':
      return 'bg-teal-500/20 text-teal-300 border border-teal-500/30'
    case 'BREAKEVEN':
      return 'bg-slate-800 text-slate-300'
    case 'LOSS':
      return 'bg-amber-500/20 text-amber-300 border border-amber-500/30'
    case 'HEAVY_LOSS':
      return 'bg-rose-500/20 text-rose-300 border border-rose-500/30'
    default:
      return 'bg-slate-800 text-slate-400'
  }
}

const getPerformanceText = (level) => {
  switch (level) {
    case 'GREAT_PROFIT': return '🔥 大红大赚'
    case 'PROFIT': return '🟢 稳健收红'
    case 'BREAKEVEN': return '⚪ 保本微利'
    case 'LOSS': return '🟡 小幅回撤'
    case 'HEAVY_LOSS': return '🔴 深度黑单'
    default: return '未定'
  }
}

// 挂载与卸载
onMounted(() => {
  fetchAllData()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  profitChart?.dispose()
  breakChart?.dispose()
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
