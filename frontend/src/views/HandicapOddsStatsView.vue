<template>
  <div class="space-y-6 sm:space-y-8 pb-12">
    <!-- Mode Switcher Tabs: 5 模式切换 -->
    <div class="flex items-center justify-between flex-wrap gap-3">
      <div class="flex items-center gap-1.5 p-1 rounded-2xl bg-slate-950/80 border border-slate-800 overflow-x-auto max-w-full">
        <router-link
          to="/daily-profit"
          class="px-3.5 py-2 rounded-xl text-xs sm:text-sm font-bold transition flex items-center gap-1.5 whitespace-nowrap text-slate-400 hover:text-white hover:bg-slate-900"
        >
          <span>⚽ 让球穿盘战绩版</span>
        </router-link>
        <router-link
          to="/daily-goals-profit"
          class="px-3.5 py-2 rounded-xl text-xs sm:text-sm font-bold transition flex items-center gap-1.5 whitespace-nowrap text-slate-400 hover:text-white hover:bg-slate-900"
        >
          <span>🥅 总进球数规律版</span>
        </router-link>
        <router-link
          to="/odds-water-profit"
          class="px-3.5 py-2 rounded-xl text-xs sm:text-sm font-bold transition flex items-center gap-1.5 whitespace-nowrap text-slate-400 hover:text-white hover:bg-slate-900"
        >
          <span>🌊 水位冷门翻车版</span>
        </router-link>
        <router-link
          to="/water-combo-profit"
          class="px-3.5 py-2 rounded-xl text-xs sm:text-sm font-bold transition flex items-center gap-1.5 whitespace-nowrap text-slate-400 hover:text-white hover:bg-slate-900"
        >
          <span>🎯 水位串子战绩版</span>
        </router-link>
        <router-link
          to="/handicap-odds-stats"
          class="px-3.5 py-2 rounded-xl text-xs sm:text-sm font-bold transition flex items-center gap-1.5 whitespace-nowrap bg-gradient-to-r from-amber-500 to-orange-600 text-white shadow-md shadow-orange-500/20 font-extrabold"
        >
          <span>📊 赔率六区间版</span>
        </router-link>
        <router-link
          to="/match-odds-stepped"
          class="px-3.5 py-2 rounded-xl text-xs sm:text-sm font-bold transition flex items-center gap-1.5 whitespace-nowrap text-slate-400 hover:text-white hover:bg-slate-900"
        >
          <span>🔬 单场精细步幅版</span>
        </router-link>
        <router-link
          to="/extreme-goals-odds"
          class="px-3.5 py-2 rounded-xl text-xs sm:text-sm font-bold transition flex items-center gap-1.5 whitespace-nowrap text-slate-400 hover:text-white hover:bg-slate-900"
        >
          <span>🎲 两极进球赔率版</span>
        </router-link>
      </div>

      <div class="text-xs text-slate-400 hidden lg:flex items-center gap-1.5">
        <span class="w-2 h-2 rounded-full bg-amber-400 animate-pulse"></span>
        <span>当前模式: 让球方获胜赔率六区间胜平负数与穿盘率大数据</span>
      </div>
    </div>

    <!-- 1. Hero Banner -->
    <div class="relative overflow-hidden rounded-3xl bg-gradient-to-r from-slate-900 via-amber-950/20 to-slate-900 border border-slate-800 p-5 sm:p-8 shadow-2xl">
      <div class="absolute -right-10 -top-10 w-80 h-80 bg-amber-500/10 rounded-full blur-3xl pointer-events-none"></div>
      <div class="absolute -left-10 -bottom-10 w-80 h-80 bg-orange-500/10 rounded-full blur-3xl pointer-events-none"></div>

      <div class="relative z-10 flex flex-col lg:flex-row lg:items-center lg:justify-between gap-6">
        <div>
          <div class="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-amber-500/10 border border-amber-500/20 text-amber-400 text-xs font-bold mb-3">
            <span>📈</span> 让球方获胜赔率六区间大数据
          </div>
          <h1 class="text-2xl sm:text-3xl md:text-4xl font-extrabold text-white tracking-tight flex items-center gap-3">
            让球赔率六区间胜平负与穿盘率大数据统计
          </h1>
          <p class="text-slate-400 text-xs sm:text-sm mt-2 max-w-2xl leading-relaxed">
            全维度追踪<strong>让球方获胜赔率六大区间</strong>（1.4及以下、1.41-1.6、1.61-1.8、1.81-2、2.01-2.2、2.21以上）的<strong>胜平负场数</strong>，深度解构<strong>赢球时的打穿盘口概率</strong>与卡盘走水风险，严格区分<strong>主让球</strong>与<strong>客让球</strong>。
          </p>
        </div>

        <!-- 顶部操作区与日期筛选 -->
        <div class="flex flex-wrap items-center gap-2 sm:gap-3 bg-slate-950/70 p-3 rounded-2xl border border-slate-800 backdrop-blur-md">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
            size="default"
            class="custom-datepicker"
            @change="handleDateRangeChange"
          />

          <button
            @click="fetchData"
            class="px-3.5 py-2 rounded-xl text-xs font-semibold bg-slate-800/80 hover:bg-slate-700 text-slate-200 transition flex items-center gap-1.5"
            title="刷新数据"
          >
            <span>🔄</span> 刷新
          </button>

          <button
            @click="openSingleCalculatorModal"
            class="px-3.5 py-2 rounded-xl text-xs font-semibold bg-indigo-600/80 hover:bg-indigo-500 text-white shadow-sm shadow-indigo-600/30 transition flex items-center gap-1.5"
          >
            <span>🧮</span> 单场测算
          </button>

          <button
            @click="openCreateModal"
            class="px-4 py-2 rounded-xl text-xs font-bold bg-gradient-to-r from-amber-500 to-orange-600 hover:from-amber-400 hover:to-orange-500 text-slate-950 shadow-lg shadow-orange-500/25 transition flex items-center gap-1.5"
          >
            <span>➕</span> 录入新战绩
          </button>
        </div>
      </div>
    </div>

    <!-- 2. 大数据打单策略量化提示横幅 -->
    <div class="rounded-2xl bg-amber-500/10 border border-amber-500/25 p-4 sm:p-5 flex items-start gap-3.5">

      <div class="text-2xl mt-0.5">💡</div>
      <div class="text-xs sm:text-sm text-amber-200/90 leading-relaxed space-y-1.5">
        <div class="font-bold text-amber-300 flex items-center gap-2">
          <span>大数据打单实战量化策略指引</span>
          <span class="text-[10px] px-2 py-0.5 rounded bg-amber-500/20 text-amber-300 font-mono">ODDS HANDICAP LOGIC</span>
        </div>
        <p>
          <strong>① ≤1.40 极低水区间：</strong>让球方优势碾压，统计胜率超高且穿盘率极佳，是穿盘重仓核心；
          <strong>② 1.41-1.60 低水区间：</strong>胜率稳健，若结合排名近况可稳定买胜或搏穿盘；
          <strong>③ 1.61-1.80 中低水区间：</strong>多为一球或半一盘，卡盘走水概率陡增，防一球小胜；
          <strong>④ 1.81-2.00 中水区间：</strong>胜负五五开，建议买低方赢或直接走受让；
          <strong>⑤ 2.01-2.20 高水区间：</strong>平局与下盘翻车多发区，穿盘难度高；
          <strong>⑥ ≥2.21 超高水区间：</strong>让球方赢球已属不易，打穿概率极低，坚决反向买让球负。
        </p>
      </div>
    </div>

    <!-- 3. 五大核心 KPI 宏观指标卡片 -->
    <div class="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-5 gap-3.5 sm:gap-5">
      <!-- KPI 1: 累计比赛总数与胜平负 -->
      <div class="rounded-2xl p-4 bg-slate-900/80 border border-slate-800 shadow-lg relative overflow-hidden group hover:border-slate-700 transition">
        <div class="text-[11px] font-semibold text-slate-400 flex items-center justify-between">
          <span>累计统计比赛</span>
          <span class="text-slate-500">⚽</span>
        </div>
        <div class="mt-2 text-2xl sm:text-3xl font-extrabold text-white tracking-tight">
          {{ summary.totalMatches || 0 }} <span class="text-xs font-normal text-slate-400">场</span>
        </div>
        <div class="mt-2 text-[11px] text-slate-400 flex items-center gap-1.5 flex-wrap">
          <span class="text-emerald-400 font-bold">胜 {{ summary.totalWinCount || 0 }}</span>
          <span>·</span>
          <span class="text-amber-400 font-bold">平 {{ summary.totalDrawCount || 0 }}</span>
          <span>·</span>
          <span class="text-rose-400 font-bold">负 {{ summary.totalLossCount || 0 }}</span>
        </div>
        <div class="mt-1 text-[10px] text-slate-500">累计 {{ summary.totalRecords || 0 }} 个统计批次</div>
      </div>

      <!-- KPI 2: 全盘综合穿盘率 -->
      <div class="rounded-2xl p-4 bg-slate-900/80 border border-slate-800 shadow-lg relative overflow-hidden group hover:border-slate-700 transition">
        <div class="text-[11px] font-semibold text-slate-400 flex items-center justify-between">
          <span>全盘综合穿盘率</span>
          <span class="text-amber-400">🎯</span>
        </div>
        <div class="mt-2 text-2xl sm:text-3xl font-extrabold text-amber-400 tracking-tight">
          {{ summary.overallBreakRate || 0 }}<span class="text-sm font-semibold">%</span>
        </div>
        <div class="mt-2 text-[11px] text-slate-400">
          打穿让球盘 <strong class="text-white">{{ summary.totalBreakCount || 0 }}</strong> 场 / 占比全盘
        </div>
        <div class="mt-1 text-[10px] text-slate-500">
          全盘综合胜率: {{ summary.overallWinRate || 0 }}%
        </div>
      </div>

      <!-- KPI 3: 赢球时的打穿率 (纯度) -->
      <div class="rounded-2xl p-4 bg-slate-900/80 border border-slate-800 shadow-lg relative overflow-hidden group hover:border-slate-700 transition">
        <div class="text-[11px] font-semibold text-slate-400 flex items-center justify-between">
          <span>赢球穿盘纯度</span>
          <span class="text-emerald-400">⚡</span>
        </div>
        <div class="mt-2 text-2xl sm:text-3xl font-extrabold text-emerald-400 tracking-tight">
          {{ summary.overallBreakRateOnWin || 0 }}<span class="text-sm font-semibold">%</span>
        </div>
        <div class="mt-2 text-[11px] text-slate-400">
          赢球走水/未穿 <strong class="text-amber-400">{{ summary.totalUnbreakCount || 0 }}</strong> 场
        </div>
        <div class="mt-1 text-[10px] text-slate-500">
          获胜时打穿让球盘的比率
        </div>
      </div>

      <!-- KPI 4: 主让 vs 客让穿盘对比 -->
      <div class="rounded-2xl p-4 bg-slate-900/80 border border-slate-800 shadow-lg relative overflow-hidden group hover:border-slate-700 transition">
        <div class="text-[11px] font-semibold text-slate-400 flex items-center justify-between">
          <span>主客让穿盘对比</span>
          <span class="text-purple-400">⚖️</span>
        </div>
        <div class="mt-2 flex items-baseline gap-2">
          <div class="text-lg sm:text-xl font-black text-cyan-400">
            主 {{ summary.homeBreakRate || 0 }}%
          </div>
          <span class="text-slate-500 text-xs font-bold">vs</span>
          <div class="text-lg sm:text-xl font-black text-purple-400">
            客 {{ summary.awayBreakRate || 0 }}%
          </div>
        </div>
        <div class="mt-2 text-[11px] text-slate-300 font-semibold">
          主让胜率: {{ summary.homeWinRate || 0 }}% | 客让胜率: {{ summary.awayWinRate || 0 }}%
        </div>
        <div class="mt-1 text-[10px]" :class="summary.dominantSide === 'HOME_DOMINANT' ? 'text-cyan-400 font-bold' : summary.dominantSide === 'AWAY_DOMINANT' ? 'text-purple-400 font-bold' : 'text-slate-400'">
          {{ summary.dominantSide === 'HOME_DOMINANT' ? '🔥 主让球打穿显著占优' : summary.dominantSide === 'AWAY_DOMINANT' ? '🔥 客让球打穿显著占优' : '⚖️ 主客让盘态势均衡' }}
        </div>
      </div>

      <!-- KPI 5: 穿盘最高/最低区间研判 -->
      <div class="rounded-2xl p-4 bg-slate-900/80 border border-slate-800 shadow-lg relative overflow-hidden group hover:border-slate-700 transition col-span-2 sm:col-span-1">
        <div class="text-[11px] font-semibold text-slate-400 flex items-center justify-between">
          <span>极值区间研判</span>
          <span class="text-rose-400">🚨</span>
        </div>
        <div class="mt-2 text-xs text-slate-200">
          <div class="flex items-center justify-between">
            <span class="text-emerald-400 font-bold">👑 最高打穿:</span>
            <span class="font-extrabold text-white text-[13px]">{{ summary.bestBreakRate || 0 }}%</span>
          </div>
          <div class="text-[10px] text-emerald-300/80 truncate mt-0.5" :title="summary.bestBreakInterval">
            {{ summary.bestBreakInterval || '暂无数据' }}
          </div>
        </div>
        <div class="mt-2 pt-2 border-t border-slate-800/80 text-xs text-slate-200">
          <div class="flex items-center justify-between">
            <span class="text-rose-400 font-bold">⚠️ 最低打穿:</span>
            <span class="font-extrabold text-white text-[13px]">{{ summary.lowestBreakRate || 0 }}%</span>
          </div>
          <div class="text-[10px] text-rose-300/80 truncate mt-0.5" :title="summary.lowestBreakInterval">
            {{ summary.lowestBreakInterval || '暂无数据' }}
          </div>
        </div>
      </div>
    </div>

    <!-- 4. 智能研判与策略诊断卡片 -->
    <div v-if="summary.adviceSummary" class="rounded-2xl bg-gradient-to-r from-slate-900 via-slate-850 to-slate-900 border border-slate-800 p-4 sm:p-5 shadow-lg flex items-center gap-3">
      <div class="w-10 h-10 rounded-xl bg-orange-500/10 border border-orange-500/20 text-orange-400 flex items-center justify-center text-xl shrink-0">
        🤖
      </div>
      <div class="text-xs sm:text-sm text-slate-300 leading-relaxed">
        <span class="font-bold text-amber-400 mr-1.5">[AI 大数据复盘研判]</span>
        {{ summary.adviceSummary }}
      </div>
    </div>

    <!-- 5. 六大赔率区间多维矩阵大数据分析看板 (核心功能) -->
    <div class="rounded-3xl bg-slate-900/90 border border-slate-800 p-4 sm:p-6 shadow-2xl space-y-5">
      <!-- 头部切换与控制 -->
      <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 pb-4 border-b border-slate-800">
        <div>
          <h2 class="text-lg sm:text-xl font-black text-white flex items-center gap-2">
            <span>📊</span> 六大赔率区间胜平负数与穿盘率矩阵
          </h2>
          <p class="text-xs text-slate-400 mt-1">
            横跨 R1 至 R6 赔率梯度，直观洞悉让球方胜平负分布、赢球打穿让球盘数与真实穿盘率
          </p>
        </div>

        <!-- 视角切换: 全盘 / 主让 / 客让 -->
        <div class="flex items-center gap-1 p-1 rounded-xl bg-slate-950 border border-slate-800 self-start sm:self-auto">
          <button
            @click="activeSideTab = 'TOTAL'"
            class="px-3 py-1.5 rounded-lg text-xs font-bold transition"
            :class="activeSideTab === 'TOTAL' ? 'bg-amber-500 text-slate-950 shadow' : 'text-slate-400 hover:text-white'"
          >
            全部让球 (Total)
          </button>
          <button
            @click="activeSideTab = 'HOME'"
            class="px-3 py-1.5 rounded-lg text-xs font-bold transition"
            :class="activeSideTab === 'HOME' ? 'bg-cyan-500 text-slate-950 shadow' : 'text-slate-400 hover:text-white'"
          >
            主让球 (Home)
          </button>
          <button
            @click="activeSideTab = 'AWAY'"
            class="px-3 py-1.5 rounded-lg text-xs font-bold transition"
            :class="activeSideTab === 'AWAY' ? 'bg-purple-500 text-white shadow' : 'text-slate-400 hover:text-white'"
          >
            客让球 (Away)
          </button>
        </div>
      </div>

      <!-- 区间卡片网格 -->
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        <div
          v-for="item in currentIntervalList"
          :key="item.intervalCode"
          class="rounded-2xl p-4 bg-slate-950/70 border border-slate-800/90 hover:border-slate-700 transition relative overflow-hidden group shadow-md"
        >
          <!-- 顶部区间标头 -->
          <div class="flex items-center justify-between pb-3 border-b border-slate-800/60">
            <div class="flex items-center gap-2">
              <span class="px-2 py-0.5 rounded text-xs font-black bg-slate-800 text-amber-400 font-mono">
                {{ item.intervalCode }}
              </span>
              <span class="text-sm font-extrabold text-white">
                {{ item.intervalName }}
              </span>
            </div>
            <span
              class="text-[10px] px-2 py-0.5 rounded-full font-bold uppercase tracking-wider"
              :class="getIntervalTagClass(item)"
            >
              {{ getIntervalTagLabel(item) }}
            </span>
          </div>

          <!-- 核心指标大数字 -->
          <div class="grid grid-cols-2 gap-3 my-3">
            <div class="bg-slate-900/80 p-2.5 rounded-xl border border-slate-800/50">
              <div class="text-[10px] text-slate-400 font-medium">综合穿盘率 (打穿/总数)</div>
              <div class="text-xl sm:text-2xl font-black mt-0.5" :class="item.overallBreakRate >= 45 ? 'text-amber-400' : item.overallBreakRate >= 30 ? 'text-emerald-400' : 'text-slate-400'">
                {{ item.overallBreakRate || 0 }}%
              </div>
              <div class="text-[10px] text-slate-500 mt-0.5">
                穿盘 {{ item.breakCount || 0 }} / 总 {{ item.totalMatches || 0 }} 场
              </div>
            </div>

            <div class="bg-slate-900/80 p-2.5 rounded-xl border border-slate-800/50">
              <div class="text-[10px] text-slate-400 font-medium">赢球打穿率 (穿盘/获胜)</div>
              <div class="text-xl sm:text-2xl font-black mt-0.5 text-emerald-400">
                {{ item.breakRateOnWin || 0 }}%
              </div>
              <div class="text-[10px] text-slate-500 mt-0.5">
                赢球未穿: {{ item.unbreakCount || 0 }} 场 (走水/赢半)
              </div>
            </div>
          </div>

          <!-- 胜平负三项进度条分布 -->
          <div class="space-y-1.5 pt-1">
            <div class="flex items-center justify-between text-[11px] text-slate-400">
              <span>胜平负分布 (总 {{ item.totalMatches }} 场)</span>
              <span class="text-slate-300 font-semibold">胜率 {{ item.winRate || 0 }}%</span>
            </div>

            <!-- 条形图 -->
            <div class="h-2.5 w-full bg-slate-900 rounded-full overflow-hidden flex">
              <div
                class="bg-emerald-500 transition-all duration-500"
                :style="{ width: `${item.totalMatches ? (item.winCount / item.totalMatches) * 100 : 0}%` }"
                :title="`胜: ${item.winCount}场 (${item.winRate}%)`"
              ></div>
              <div
                class="bg-amber-500 transition-all duration-500"
                :style="{ width: `${item.totalMatches ? (item.drawCount / item.totalMatches) * 100 : 0}%` }"
                :title="`平: ${item.drawCount}场 (${item.drawRate}%)`"
              ></div>
              <div
                class="bg-rose-500 transition-all duration-500"
                :style="{ width: `${item.totalMatches ? (item.lossCount / item.totalMatches) * 100 : 0}%` }"
                :title="`负: ${item.lossCount}场 (${item.lossRate}%)`"
              ></div>
            </div>

            <!-- 胜平负图例数字 -->
            <div class="grid grid-cols-3 gap-1 pt-1 text-center text-[10px]">
              <div class="bg-emerald-950/40 border border-emerald-500/20 rounded py-1 text-emerald-300">
                胜: <strong class="text-white">{{ item.winCount }}</strong> ({{ item.winRate }}%)
              </div>
              <div class="bg-amber-950/40 border border-amber-500/20 rounded py-1 text-amber-300">
                平: <strong class="text-white">{{ item.drawCount }}</strong> ({{ item.drawRate }}%)
              </div>
              <div class="bg-rose-950/40 border border-rose-500/20 rounded py-1 text-rose-300">
                负: <strong class="text-white">{{ item.lossCount }}</strong> ({{ item.lossRate }}%)
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 6. ECharts 大数据可视化图表系统 (下注参考专用看板) -->
    <div class="space-y-5">
      <!-- 图表 1 (重点核心): 主客让球方胜赔最终赛果全景堆叠条形图 (方便下注快速决策) -->
      <div class="rounded-3xl bg-slate-900/90 border border-slate-800 p-4 sm:p-6 shadow-2xl">
        <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3 mb-4 pb-3 border-b border-slate-800/80">
          <div>
            <h3 class="text-base sm:text-lg font-bold text-white flex items-center gap-2">
              <span>🎯</span> 主客让球方胜赔最终赛果全景图 (打穿 · 卡盘走水 · 平局 · 输球)
            </h3>
            <p class="text-xs text-slate-400 mt-0.5">
              直观透视不同胜赔下让球方的 4 种最终结局：<strong>净胜穿盘</strong>、<strong>赢球走水卡盘</strong>、<strong>双方打平</strong>与<strong>爆冷输球</strong>
            </p>
          </div>

          <!-- 视角切换按钮 -->
          <div class="flex items-center gap-1 p-1 rounded-xl bg-slate-950 border border-slate-800 self-start sm:self-auto">
            <button
              @click="switchBettingView('ALL')"
              class="px-3 py-1 rounded-lg text-xs font-bold transition"
              :class="bettingOutcomeView === 'ALL' ? 'bg-amber-500 text-slate-950 shadow' : 'text-slate-400 hover:text-white'"
            >
              全部对比 (12区间)
            </button>
            <button
              @click="switchBettingView('HOME')"
              class="px-3 py-1 rounded-lg text-xs font-bold transition"
              :class="bettingOutcomeView === 'HOME' ? 'bg-cyan-500 text-slate-950 shadow' : 'text-slate-400 hover:text-white'"
            >
              仅看主场让球
            </button>
            <button
              @click="switchBettingView('AWAY')"
              class="px-3 py-1 rounded-lg text-xs font-bold transition"
              :class="bettingOutcomeView === 'AWAY' ? 'bg-purple-500 text-white shadow' : 'text-slate-400 hover:text-white'"
            >
              仅看客场让球
            </button>
          </div>
        </div>

        <!-- 横向 100% 堆叠条形图 -->
        <div ref="bettingOutcomeChartRef" class="w-full h-96 sm:h-[420px]"></div>

        <!-- 图表底部图例与打单参考说明 -->
        <div class="grid grid-cols-2 sm:grid-cols-4 gap-2.5 pt-3 mt-3 border-t border-slate-800/60 text-center text-xs">
          <div class="p-2 rounded-xl bg-emerald-950/30 border border-emerald-500/20 text-emerald-300">
            <div class="font-bold flex items-center justify-center gap-1.5">
              <span class="w-2.5 h-2.5 rounded bg-emerald-500"></span> 赢球打穿让球盘
            </div>
            <div class="text-[10px] text-slate-400 mt-0.5">买让球胜的核心红单盈利区</div>
          </div>
          <div class="p-2 rounded-xl bg-amber-950/30 border border-amber-500/20 text-amber-300">
            <div class="font-bold flex items-center justify-center gap-1.5">
              <span class="w-2.5 h-2.5 rounded bg-amber-500"></span> 赢球卡盘走水 / 输半
            </div>
            <div class="text-[10px] text-slate-400 mt-0.5">一球小胜卡盘，买让胜走水/输球</div>
          </div>
          <div class="p-2 rounded-xl bg-sky-950/30 border border-sky-500/20 text-sky-300">
            <div class="font-bold flex items-center justify-center gap-1.5">
              <span class="w-2.5 h-2.5 rounded bg-sky-500"></span> 双方打平 (平局)
            </div>
            <div class="text-[10px] text-slate-400 mt-0.5">让球方未胜，受让方下盘打出</div>
          </div>
          <div class="p-2 rounded-xl bg-rose-950/30 border border-rose-500/20 text-rose-300">
            <div class="font-bold flex items-center justify-center gap-1.5">
              <span class="w-2.5 h-2.5 rounded bg-rose-500"></span> 爆冷输球 (客/主胜下盘)
            </div>
            <div class="text-[10px] text-slate-400 mt-0.5">直接翻车，受让方独赢大冷</div>
          </div>
        </div>
      </div>

      <!-- 双图并列: 六区间分布图 + 主客对比雷达图 -->
      <div class="grid grid-cols-1 lg:grid-cols-2 gap-5">
        <!-- 图表 2: 六区间胜平负分布与穿盘对比复合柱状图 -->
        <div class="rounded-3xl bg-slate-900/90 border border-slate-800 p-4 sm:p-6 shadow-2xl">
          <div class="flex items-center justify-between mb-4">
            <div>
              <h3 class="text-base sm:text-lg font-bold text-white flex items-center gap-2">
                <span>📊</span> 六区间胜平负分布与穿盘场数图
              </h3>
              <p class="text-xs text-slate-400 mt-0.5">对比各区间的胜/平/负场数与打穿盘口场数</p>
            </div>
          </div>
          <div ref="distributionChartRef" class="w-full h-80"></div>
        </div>

        <!-- 图表 3: 主客让六区间胜率与穿盘率雷达对比图 -->
        <div class="rounded-3xl bg-slate-900/90 border border-slate-800 p-4 sm:p-6 shadow-2xl">
          <div class="flex items-center justify-between mb-4">
            <div>
              <h3 class="text-base sm:text-lg font-bold text-white flex items-center gap-2">
                <span>🕸️</span> 主让 vs 客让 六区间胜率与穿盘雷达图
              </h3>
              <p class="text-xs text-slate-400 mt-0.5">多维雷达透视同一胜赔下主让与客让的统治力与穿盘分化</p>
            </div>
          </div>
          <div ref="radarComparisonChartRef" class="w-full h-80"></div>
        </div>
      </div>

      <!-- 实战下注决策速查参考看板 (Betting Strategy Reference Sheet) -->
      <div class="rounded-3xl bg-slate-900/90 border border-slate-800 p-4 sm:p-6 shadow-2xl space-y-4">
        <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3 pb-3 border-b border-slate-800">
          <div>
            <h3 class="text-base sm:text-lg font-black text-amber-400 flex items-center gap-2">
              <span>💡</span> 实战下注参考建议决策表 (基于胜赔区间大数据推演)
            </h3>
            <p class="text-xs text-slate-400 mt-0.5">下注时先看让球方是主让还是客让，再对齐其获胜赔率所在区间，即可直观参考打法方向</p>
          </div>
          <div class="flex items-center gap-2">
            <span class="text-xs text-slate-400">切换参考表:</span>
            <button
              @click="advisorTab = 'HOME'"
              class="px-3 py-1 rounded-lg text-xs font-bold transition"
              :class="advisorTab === 'HOME' ? 'bg-cyan-500 text-slate-950 shadow' : 'bg-slate-800 text-slate-300'"
            >
              🏠 主场让球下注参考
            </button>
            <button
              @click="advisorTab = 'AWAY'"
              class="px-3 py-1 rounded-lg text-xs font-bold transition"
              :class="advisorTab === 'AWAY' ? 'bg-purple-500 text-white shadow' : 'bg-slate-800 text-slate-300'"
            >
              ✈️ 客场让球下注参考
            </button>
          </div>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          <div
            v-for="adv in currentAdvisorList"
            :key="adv.code"
            class="p-4 rounded-2xl bg-slate-950/80 border border-slate-800 hover:border-slate-700 transition space-y-3"
          >
            <div class="flex items-center justify-between">
              <div class="flex items-center gap-2">
                <span class="px-2 py-0.5 rounded text-xs font-mono font-bold" :class="advisorTab === 'HOME' ? 'bg-cyan-950 text-cyan-300 border border-cyan-500/30' : 'bg-purple-950 text-purple-300 border border-purple-500/30'">
                  {{ adv.code }}
                </span>
                <span class="text-sm font-extrabold text-white">{{ adv.name }}</span>
              </div>
              <span class="text-[10px] px-2 py-0.5 rounded font-bold" :class="adv.riskClass">
                {{ adv.riskLabel }}
              </span>
            </div>

            <!-- 数据概览 -->
            <div class="grid grid-cols-3 gap-1.5 text-center text-[11px] bg-slate-900/60 p-2 rounded-xl">
              <div>
                <span class="text-slate-400 block text-[10px]">历史胜率</span>
                <strong class="text-emerald-400">{{ adv.winRate }}%</strong>
              </div>
              <div>
                <span class="text-slate-400 block text-[10px]">综合穿盘率</span>
                <strong class="text-amber-400">{{ adv.breakRate }}%</strong>
              </div>
              <div>
                <span class="text-slate-400 block text-[10px]">卡盘走水率</span>
                <strong class="text-sky-400">{{ adv.unbreakRate }}%</strong>
              </div>
            </div>

            <!-- 推荐下注决策 -->
            <div class="text-xs space-y-1">
              <div class="flex items-center gap-1.5 font-bold" :class="adv.actionColor">
                <span>{{ adv.actionIcon }}</span>
                <span>推荐买法: {{ adv.actionTitle }}</span>
              </div>
              <p class="text-[11px] text-slate-300 leading-relaxed bg-slate-900/40 p-2 rounded-lg border border-slate-800/60">
                {{ adv.strategyDesc }}
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 7. 历史战绩明细数据表格 (含主客明细展开与增删改查) -->
    <div class="rounded-3xl bg-slate-900/90 border border-slate-800 p-4 sm:p-6 shadow-2xl space-y-4">
      <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3">
        <div>
          <h2 class="text-lg sm:text-xl font-black text-white flex items-center gap-2">
            <span>📑</span> 历史让球赔率战绩归档明细
          </h2>
          <p class="text-xs text-slate-400 mt-0.5">支持按批次日期查看详细的六区间胜平负数与穿盘率指标</p>
        </div>

        <div class="text-xs text-slate-400">
          共 <strong class="text-amber-400">{{ totalRecordsCount }}</strong> 条战绩记录
        </div>
      </div>

      <!-- 表格 -->
      <div class="overflow-x-auto rounded-2xl border border-slate-800">
        <table class="w-full text-left text-xs text-slate-300 divide-y divide-slate-800">
          <thead class="bg-slate-950/80 text-slate-400 font-bold uppercase text-[11px]">
            <tr>
              <th class="py-3 px-3">日期 / 批次</th>
              <th class="py-3 px-3 text-center">总场数</th>
              <th class="py-3 px-3 text-center">胜/平/负</th>
              <th class="py-3 px-3 text-center">打穿数</th>
              <th class="py-3 px-3 text-center">全盘穿盘率</th>
              <th class="py-3 px-3 text-center">赢球打穿率</th>
              <th class="py-3 px-3 text-center">主让穿盘率</th>
              <th class="py-3 px-3 text-center">客让穿盘率</th>
              <th class="py-3 px-3">研判与标签</th>
              <th class="py-3 px-3 text-right">操作</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-slate-800/60 bg-slate-900/40 font-medium">
            <tr
              v-for="record in recordList"
              :key="record.id"
              class="hover:bg-slate-800/40 transition"
            >
              <!-- 日期与批次 -->
              <td class="py-3 px-3">
                <div class="font-bold text-white text-sm font-mono">{{ record.recordDate }}</div>
                <div class="text-[11px] text-slate-400 truncate max-w-[160px]" :title="record.batchTitle">
                  {{ record.batchTitle || '未命名批次' }}
                </div>
              </td>

              <!-- 总场数 -->
              <td class="py-3 px-3 text-center font-bold text-white">
                {{ record.totalMatches }}
              </td>

              <!-- 胜/平/负 -->
              <td class="py-3 px-3 text-center">
                <span class="text-emerald-400 font-bold">{{ record.totalWinCount }}</span> /
                <span class="text-amber-400 font-bold">{{ record.totalDrawCount }}</span> /
                <span class="text-rose-400 font-bold">{{ record.totalLossCount }}</span>
              </td>

              <!-- 打穿数 -->
              <td class="py-3 px-3 text-center font-black text-amber-400">
                {{ record.totalBreakCount }}
              </td>

              <!-- 全盘穿盘率 -->
              <td class="py-3 px-3 text-center font-bold" :class="record.overallBreakRate >= 45 ? 'text-amber-400' : 'text-slate-300'">
                {{ record.overallBreakRate }}%
              </td>

              <!-- 赢球穿盘率 -->
              <td class="py-3 px-3 text-center font-bold text-emerald-400">
                {{ record.overallBreakRateOnWin }}%
              </td>

              <!-- 主让穿盘率 -->
              <td class="py-3 px-3 text-center text-cyan-400 font-semibold">
                {{ record.homeBreakRate }}%
                <span class="text-[10px] text-slate-500 block">({{ record.homeTotalBreak }}/{{ record.homeTotalMatches }})</span>
              </td>

              <!-- 客让穿盘率 -->
              <td class="py-3 px-3 text-center text-purple-400 font-semibold">
                {{ record.awayBreakRate }}%
                <span class="text-[10px] text-slate-500 block">({{ record.awayTotalBreak }}/{{ record.awayTotalMatches }})</span>
              </td>

              <!-- 研判与标签 -->
              <td class="py-3 px-3">
                <div class="flex items-center gap-1.5 flex-wrap">
                  <span
                    class="text-[10px] px-2 py-0.5 rounded font-bold"
                    :class="record.homeAwayBias === 'HOME_BIAS' ? 'bg-cyan-500/20 text-cyan-300 border border-cyan-500/30' : record.homeAwayBias === 'AWAY_BIAS' ? 'bg-purple-500/20 text-purple-300 border border-purple-500/30' : 'bg-slate-800 text-slate-400'"
                  >
                    {{ record.homeAwayBias === 'HOME_BIAS' ? '主让占优' : record.homeAwayBias === 'AWAY_BIAS' ? '客让占优' : '主客均衡' }}
                  </span>
                  <span class="text-[10px] px-1.5 py-0.5 rounded bg-amber-500/10 text-amber-300 border border-amber-500/20">
                    最强: {{ record.bestBreakInterval }}
                  </span>
                </div>
                <div v-if="record.remarks" class="text-[10px] text-slate-400 truncate max-w-[200px] mt-1" :title="record.remarks">
                  {{ record.remarks }}
                </div>
              </td>

              <!-- 操作 -->
              <td class="py-3 px-3 text-right whitespace-nowrap">
                <button
                  @click="openEditModal(record)"
                  class="px-2.5 py-1 text-xs font-semibold rounded bg-slate-800 hover:bg-slate-700 text-amber-400 mr-1.5 transition"
                >
                  编辑
                </button>
                <button
                  @click="handleDelete(record.id)"
                  class="px-2.5 py-1 text-xs font-semibold rounded bg-rose-950/40 hover:bg-rose-900/60 text-rose-400 transition"
                >
                  删除
                </button>
              </td>
            </tr>

            <tr v-if="recordList.length === 0">
              <td colspan="10" class="py-12 text-center text-slate-500">
                暂无让球赔率战绩数据，点击右上角“录入新战绩”开启分析
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- 分页组件 -->
      <div class="flex items-center justify-between pt-2">
        <span class="text-xs text-slate-500">
          第 {{ currentPage }} 页 / 共 {{ totalPages }} 页
        </span>
        <div class="flex items-center gap-2">
          <button
            @click="changePage(currentPage - 1)"
            :disabled="currentPage <= 1"
            class="px-3 py-1.5 rounded-lg text-xs font-semibold bg-slate-800 hover:bg-slate-700 text-slate-300 disabled:opacity-40 disabled:cursor-not-allowed transition"
          >
            上一页
          </button>
          <button
            @click="changePage(currentPage + 1)"
            :disabled="currentPage >= totalPages"
            class="px-3 py-1.5 rounded-lg text-xs font-semibold bg-slate-800 hover:bg-slate-700 text-slate-300 disabled:opacity-40 disabled:cursor-not-allowed transition"
          >
            下一页
          </button>
        </div>
      </div>
    </div>

    <!-- 8. 录入 / 编辑战绩数据弹窗 (包含六区间胜平负穿盘矩阵填报) -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑让球赔率区间战绩' : '录入新一批次让球赔率战绩'"
      width="90%"
      class="custom-modal max-w-4xl"
      :before-close="closeDialog"
    >
      <div class="space-y-5 py-2">
        <!-- 基础信息 -->
        <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
          <div>
            <label class="block text-xs font-bold text-slate-300 mb-1.5">战绩日期 <span class="text-rose-400">*</span></label>
            <el-date-picker
              v-model="formData.recordDate"
              type="date"
              placeholder="选择战绩日期"
              value-format="YYYY-MM-DD"
              class="w-full"
            />
          </div>
          <div>
            <label class="block text-xs font-bold text-slate-300 mb-1.5">批次标题 / 赛事描述</label>
            <el-input
              v-model="formData.batchTitle"
              placeholder="例如: 欧冠小组赛第一轮让球盘口统计"
            />
          </div>
        </div>

        <!-- 主让球六区间数据录入 -->
        <div class="rounded-2xl bg-slate-950 p-4 border border-cyan-500/30 space-y-3">
          <div class="flex items-center justify-between">
            <h4 class="text-sm font-extrabold text-cyan-400 flex items-center gap-2">
              <span>🏠</span> 1. 主让球方六区间胜平负与穿盘数
            </h4>
            <span class="text-xs text-slate-400">注意: 穿盘数不得大于胜场数</span>
          </div>

          <div class="overflow-x-auto">
            <table class="w-full text-xs text-slate-300">
              <thead class="bg-slate-900 text-slate-400">
                <tr>
                  <th class="p-2 text-left">区间代码与范围</th>
                  <th class="p-2 text-center text-emerald-400">胜场数</th>
                  <th class="p-2 text-center text-amber-400">平场数</th>
                  <th class="p-2 text-center text-rose-400">负场数</th>
                  <th class="p-2 text-center text-cyan-400">赢球穿盘数</th>
                </tr>
              </thead>
              <tbody class="divide-y divide-slate-800">
                <tr v-for="c in intervalConfigs" :key="'home_' + c.key">
                  <td class="p-2 font-bold text-white">
                    <span class="font-mono text-cyan-400 mr-1.5">{{ c.code }}</span>
                    {{ c.name }}
                  </td>
                  <td class="p-2 text-center">
                    <el-input-number v-model="formData['home' + c.key + 'Win']" :min="0" size="small" class="w-24" />
                  </td>
                  <td class="p-2 text-center">
                    <el-input-number v-model="formData['home' + c.key + 'Draw']" :min="0" size="small" class="w-24" />
                  </td>
                  <td class="p-2 text-center">
                    <el-input-number v-model="formData['home' + c.key + 'Loss']" :min="0" size="small" class="w-24" />
                  </td>
                  <td class="p-2 text-center">
                    <el-input-number v-model="formData['home' + c.key + 'Break']" :min="0" size="small" class="w-24" />
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <!-- 客让球六区间数据录入 -->
        <div class="rounded-2xl bg-slate-950 p-4 border border-purple-500/30 space-y-3">
          <div class="flex items-center justify-between">
            <h4 class="text-sm font-extrabold text-purple-400 flex items-center gap-2">
              <span>✈️</span> 2. 客让球方六区间胜平负与穿盘数
            </h4>
            <span class="text-xs text-slate-400">注意: 穿盘数不得大于胜场数</span>
          </div>

          <div class="overflow-x-auto">
            <table class="w-full text-xs text-slate-300">
              <thead class="bg-slate-900 text-slate-400">
                <tr>
                  <th class="p-2 text-left">区间代码与范围</th>
                  <th class="p-2 text-center text-emerald-400">胜场数</th>
                  <th class="p-2 text-center text-amber-400">平场数</th>
                  <th class="p-2 text-center text-rose-400">负场数</th>
                  <th class="p-2 text-center text-purple-400">赢球穿盘数</th>
                </tr>
              </thead>
              <tbody class="divide-y divide-slate-800">
                <tr v-for="c in intervalConfigs" :key="'away_' + c.key">
                  <td class="p-2 font-bold text-white">
                    <span class="font-mono text-purple-400 mr-1.5">{{ c.code }}</span>
                    {{ c.name }}
                  </td>
                  <td class="p-2 text-center">
                    <el-input-number v-model="formData['away' + c.key + 'Win']" :min="0" size="small" class="w-24" />
                  </td>
                  <td class="p-2 text-center">
                    <el-input-number v-model="formData['away' + c.key + 'Draw']" :min="0" size="small" class="w-24" />
                  </td>
                  <td class="p-2 text-center">
                    <el-input-number v-model="formData['away' + c.key + 'Loss']" :min="0" size="small" class="w-24" />
                  </td>
                  <td class="p-2 text-center">
                    <el-input-number v-model="formData['away' + c.key + 'Break']" :min="0" size="small" class="w-24" />
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <!-- 备注复盘 -->
        <div>
          <label class="block text-xs font-bold text-slate-300 mb-1.5">复盘备忘与心得</label>
          <el-input
            v-model="formData.remarks"
            type="textarea"
            :rows="3"
            placeholder="记录打单逻辑、冷门原因、赢球不穿盘走水反思等..."
          />
        </div>
      </div>

      <template #footer>
        <div class="flex items-center justify-end gap-3">
          <button
            @click="closeDialog"
            class="px-4 py-2 rounded-xl text-xs font-semibold bg-slate-800 hover:bg-slate-700 text-slate-300 transition"
          >
            取消
          </button>
          <button
            @click="submitForm"
            :disabled="submitting"
            class="px-5 py-2 rounded-xl text-xs font-bold bg-gradient-to-r from-amber-500 to-orange-600 hover:from-amber-400 hover:to-orange-500 text-slate-950 transition disabled:opacity-50"
          >
            {{ submitting ? '保存中...' : '确认保存战绩' }}
          </button>
        </div>
      </template>
    </el-dialog>

    <!-- 9. 单场赛事赔率与穿盘智能辅助测算器弹窗 -->
    <el-dialog
      v-model="calcDialogVisible"
      title="🧮 单场比赛赔率区间与穿盘概率智能辅助测算"
      width="90%"
      class="custom-modal max-w-2xl"
    >
      <div class="space-y-4 py-2">
        <p class="text-xs text-slate-400 leading-relaxed">
          输入您正在关注的比赛让球方获胜赔率及主客让属性，系统将自动映射到对应历史区间，依据大数据测算该区间的历史穿盘概率，并提供量化投注指引。
        </p>

        <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
          <div>
            <label class="block text-xs font-bold text-slate-300 mb-1.5">让球方主客场</label>
            <el-select v-model="calcForm.side" class="w-full">
              <el-option label="主场让球 (主让)" value="HOME" />
              <el-option label="客场让球 (客让)" value="AWAY" />
            </el-select>
          </div>

          <div>
            <label class="block text-xs font-bold text-slate-300 mb-1.5">让球方获胜赔率 (胜赔)</label>
            <el-input-number
              v-model="calcForm.odds"
              :precision="2"
              :step="0.05"
              :min="1.01"
              :max="10.0"
              class="w-full"
            />
          </div>
        </div>

        <!-- 智能测算结果卡片 -->
        <div class="rounded-2xl bg-slate-950 p-4 border border-amber-500/40 space-y-3 mt-4">
          <div class="flex items-center justify-between">
            <span class="text-xs text-slate-400">对应赔率区间</span>
            <span class="text-sm font-extrabold text-amber-400 font-mono">
              {{ calcResult.intervalCode }} ({{ calcResult.intervalName }})
            </span>
          </div>

          <div class="grid grid-cols-3 gap-2 text-center">
            <div class="bg-slate-900 p-2 rounded-xl">
              <div class="text-[10px] text-slate-400">历史样本场数</div>
              <div class="text-base font-bold text-white mt-0.5">{{ calcResult.matches }} 场</div>
            </div>
            <div class="bg-slate-900 p-2 rounded-xl">
              <div class="text-[10px] text-slate-400">历史胜率</div>
              <div class="text-base font-bold text-emerald-400 mt-0.5">{{ calcResult.winRate }}%</div>
            </div>
            <div class="bg-slate-900 p-2 rounded-xl">
              <div class="text-[10px] text-slate-400">历史综合穿盘率</div>
              <div class="text-base font-bold text-amber-400 mt-0.5">{{ calcResult.breakRate }}%</div>
            </div>
          </div>

          <div class="p-3 rounded-xl bg-amber-500/10 border border-amber-500/20 text-xs text-amber-200 leading-relaxed">
            <strong>🎯 大数据打单推荐：</strong> {{ calcResult.advice }}
          </div>
        </div>
      </div>

      <template #footer>
        <button
          @click="calcDialogVisible = false"
          class="px-4 py-2 rounded-xl text-xs font-semibold bg-slate-800 hover:bg-slate-700 text-slate-300 transition"
        >
          关闭测算器
        </button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, onUnmounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import * as echarts from 'echarts'
import api from '../api'

// 1. 日期筛选与基础状态
const dateRange = ref([])

const loading = ref(false)
const submitting = ref(false)
const activeSideTab = ref('TOTAL') // TOTAL, HOME, AWAY

// 2. 统计数据与列表
const summary = ref({
  totalRecords: 0,
  totalMatches: 0,
  totalWinCount: 0,
  totalDrawCount: 0,
  totalLossCount: 0,
  totalBreakCount: 0,
  totalUnbreakCount: 0,
  overallWinRate: 0,
  overallBreakRateOnWin: 0,
  overallBreakRate: 0,
  homeTotalMatches: 0,
  homeBreakRate: 0,
  homeWinRate: 0,
  awayTotalMatches: 0,
  awayBreakRate: 0,
  awayWinRate: 0,
  bestBreakInterval: '暂无数据',
  bestBreakRate: 0,
  lowestBreakInterval: '暂无数据',
  lowestBreakRate: 0,
  dominantSide: 'BALANCED',
  adviceSummary: '',
  homeIntervals: [],
  awayIntervals: [],
  totalIntervals: []
})

const recordList = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const totalRecordsCount = ref(0)
const totalPages = computed(() => Math.ceil(totalRecordsCount.value / pageSize.value) || 1)

// 3. 图表 DOM 引用
const distributionChartRef = ref(null)
const comparisonChartRef = ref(null)
const bettingOutcomeChartRef = ref(null)
const radarComparisonChartRef = ref(null)

let distributionChart = null
let comparisonChart = null
let bettingOutcomeChart = null
let radarComparisonChart = null

const bettingOutcomeView = ref('ALL') // ALL, HOME, AWAY
const advisorTab = ref('HOME') // HOME, AWAY

const switchBettingView = (view) => {
  bettingOutcomeView.value = view
  nextTick(() => {
    renderBettingOutcomeChart()
  })
}

// 4. 六区间通用映射配置
const intervalConfigs = [
  { key: 'R1', code: 'R1', name: '1.40及以下' },
  { key: 'R2', code: 'R2', name: '1.41-1.60' },
  { key: 'R3', code: 'R3', name: '1.61-1.80' },
  { key: 'R4', code: 'R4', name: '1.81-2.00' },
  { key: 'R5', code: 'R5', name: '2.01-2.20' },
  { key: 'R6', code: 'R6', name: '2.21以上' }
]

// 5. 当前选中的区间数据列表 (依据 TOTAL / HOME / AWAY 切换)
const currentIntervalList = computed(() => {
  if (activeSideTab.value === 'HOME') {
    return summary.value.homeIntervals || []
  } else if (activeSideTab.value === 'AWAY') {
    return summary.value.awayIntervals || []
  }
  return summary.value.totalIntervals || []
})

// 5.1 实战下注建议计算属性 (根据胜赔区间大数据动态生成决策)
const currentAdvisorList = computed(() => {
  const isHome = advisorTab.value === 'HOME'
  const sourceList = isHome ? summary.value.homeIntervals : summary.value.awayIntervals
  const prefix = isHome ? '主让' : '客让'

  return intervalConfigs.map((c) => {
    const item = (sourceList || []).find((i) => i.intervalCode === c.code)
    const matches = item ? item.totalMatches : 0
    const winRate = item ? item.winRate : 0
    const breakRate = item ? item.overallBreakRate : 0
    const unbreakRate = matches > 0 ? (((item.unbreakCount || 0) / matches) * 100).toFixed(1) : '0.0'
    const drawRate = item ? item.drawRate : 0
    const lossRate = item ? item.lossRate : 0

    let actionTitle = ''
    let actionIcon = '🎯'
    let actionColor = 'text-amber-400'
    let riskLabel = '中等风险'
    let riskClass = 'bg-amber-500/20 text-amber-300 border border-amber-500/30'
    let strategyDesc = ''

    if (c.code === 'R1') {
      actionTitle = '重仓让球胜 / 博大比分打穿'
      actionIcon = '🔥'
      actionColor = 'text-emerald-400'
      riskLabel = '极高稳健 · 低风险'
      riskClass = 'bg-emerald-500/20 text-emerald-300 border border-emerald-500/30'
      strategyDesc = `${prefix}胜赔≤1.40属于强弱悬殊局。历史综合打穿率高达 ${breakRate}%，极少走水，让球胜打出率首屈一指，建议重点打穿！`
    } else if (c.code === 'R2') {
      actionTitle = isHome ? '买独赢胜 / 防一球小胜走水' : '客胜独赢 / 浅让可搏'
      actionIcon = '⚠️'
      actionColor = 'text-sky-400'
      riskLabel = '卡盘高发 · 赢球走水'
      riskClass = 'bg-sky-500/20 text-sky-300 border border-sky-500/30'
      strategyDesc = `${prefix}1.41-1.60胜率有保障 (${winRate}%)，但易出现1球小胜走水或卡半一盘赢半（卡盘走水率${unbreakRate}%）。若让深盘建议防走水，稳健者直接买不让球独赢。`
    } else if (c.code === 'R3') {
      actionTitle = '重点反买让球负 / 走受让下盘'
      actionIcon = '🛡️'
      actionColor = 'text-amber-400'
      riskLabel = '均势阻力 · 中度风险'
      riskClass = 'bg-amber-500/20 text-amber-300 border border-amber-500/30'
      strategyDesc = `${prefix}1.61-1.80多让半一或一球盘，打穿阻力骤增（穿盘率仅${breakRate}%）。让球方极易被逼平或仅赢一球，下注买让球方的让球输（受让下盘）收益极好！`
    } else if (c.code === 'R4') {
      actionTitle = '单挑低赔方直接赢 / 勿博让球'
      actionIcon = '⚖️'
      actionColor = 'text-purple-400'
      riskLabel = '五五开五五分成'
      riskClass = 'bg-purple-500/20 text-purple-300 border border-purple-500/30'
      strategyDesc = `${prefix}1.81-2.00属于胶着死磕局，平局率 (${drawRate}%) 与冷门率走高。切忌盲目博让球打穿，建议直接买独赢胜或直接受让方不败。`
    } else if (c.code === 'R5') {
      actionTitle = '坚决买受让下盘 / 防冷平局'
      actionIcon = '🛑'
      actionColor = 'text-rose-400'
      riskLabel = '高水诱盘 · 高危翻车'
      riskClass = 'bg-rose-500/20 text-rose-300 border border-rose-500/30'
      strategyDesc = `${prefix}2.01-2.20让球方信心羸弱，属于庄家高水诱上盘典型区间，下盘翻车与平局多发。坚决走受让方，或选平局高赔博冷。`
    } else {
      actionTitle = '极坚决买让球负 / 受让方稳健'
      actionIcon = '⛔'
      actionColor = 'text-rose-500'
      riskLabel = '超高水极险'
      riskClass = 'bg-rose-950 text-rose-300 border border-rose-600/40'
      strategyDesc = `${prefix}胜赔≥2.21让球方自身难保，打穿概率逼近于 0，输球翻车率高达 ${lossRate}%。下注逻辑坚定执行：反向买让球负！`
    }

    return {
      code: c.code,
      name: c.name,
      matches,
      winRate,
      breakRate,
      unbreakRate,
      drawRate,
      lossRate,
      actionTitle,
      actionIcon,
      actionColor,
      riskLabel,
      riskClass,
      strategyDesc
    }
  })
})

// 6. 表单对话框状态
const dialogVisible = ref(false)
const isEdit = ref(false)
const editId = ref(null)

const formData = reactive({
  recordDate: '',
  batchTitle: '',
  remarks: '',
  homeR1Win: 0, homeR1Draw: 0, homeR1Loss: 0, homeR1Break: 0,
  homeR2Win: 0, homeR2Draw: 0, homeR2Loss: 0, homeR2Break: 0,
  homeR3Win: 0, homeR3Draw: 0, homeR3Loss: 0, homeR3Break: 0,
  homeR4Win: 0, homeR4Draw: 0, homeR4Loss: 0, homeR4Break: 0,
  homeR5Win: 0, homeR5Draw: 0, homeR5Loss: 0, homeR5Break: 0,
  homeR6Win: 0, homeR6Draw: 0, homeR6Loss: 0, homeR6Break: 0,
  awayR1Win: 0, awayR1Draw: 0, awayR1Loss: 0, awayR1Break: 0,
  awayR2Win: 0, awayR2Draw: 0, awayR2Loss: 0, awayR2Break: 0,
  awayR3Win: 0, awayR3Draw: 0, awayR3Loss: 0, awayR3Break: 0,
  awayR4Win: 0, awayR4Draw: 0, awayR4Loss: 0, awayR4Break: 0,
  awayR5Win: 0, awayR5Draw: 0, awayR5Loss: 0, awayR5Break: 0,
  awayR6Win: 0, awayR6Draw: 0, awayR6Loss: 0, awayR6Break: 0
})

// 7. 单场智能测算器状态
const calcDialogVisible = ref(false)
const calcForm = reactive({
  side: 'HOME',
  odds: 1.55
})

const calcResult = computed(() => {
  const odds = calcForm.odds || 1.5
  let code = 'R1'
  let name = '1.40及以下'
  let advice = ''

  if (odds <= 1.40) {
    code = 'R1'
    name = '1.40及以下'
    advice = '极低水深盘，打穿盘口概率极高，建议重仓让球胜，搏穿盘高收益。'
  } else if (odds <= 1.60) {
    code = 'R2'
    name = '1.41-1.60'
    advice = '低赔正路区，胜率极高，可防一球小胜走水，胜或让球负组合打出率较好。'
  } else if (odds <= 1.80) {
    code = 'R3'
    name = '1.61-1.80'
    advice = '卡盘高发区！让球方打出易卡一球，建议重点买让球的一方让球输（受让下盘）。'
  } else if (odds <= 2.00) {
    code = 'R4'
    name = '1.81-2.00'
    advice = '中水平衡区间，胜负胶着，建议直接买赔率较低的一方直接赢球，不宜博深让。'
  } else if (odds <= 2.20) {
    code = 'R5'
    name = '2.01-2.20'
    advice = '高水风险区，容易翻车，穿盘率低，建议多考虑平局或走受让方。'
  } else {
    code = 'R6'
    name = '2.21以上'
    advice = '超高赔区间，让球方赢球穿盘难度极大，坚决买让球方的让球输！'
  }

  // 从当前汇总数据中寻找该区间的历史样本
  const list = calcForm.side === 'HOME' ? summary.value.homeIntervals : summary.value.awayIntervals
  const matched = (list || []).find(i => i.intervalCode === code)

  return {
    intervalCode: code,
    intervalName: name,
    advice,
    matches: matched ? matched.totalMatches : 0,
    winRate: matched ? matched.winRate : 0,
    breakRate: matched ? matched.overallBreakRate : 0
  }
})

// 8. 辅助标签判断
const getIntervalTagLabel = (item) => {
  if (item.overallBreakRate >= 50) return '穿盘王 🚀'
  if (item.overallBreakRate >= 35) return '表现均衡 ⚖️'
  if (item.winRate >= 65 && item.overallBreakRate < 35) return '卡盘走水 🛡️'
  return '高危翻车 ⚠️'
}

const getIntervalTagClass = (item) => {
  if (item.overallBreakRate >= 50) return 'bg-amber-500/20 text-amber-300 border border-amber-500/30'
  if (item.overallBreakRate >= 35) return 'bg-emerald-500/20 text-emerald-300 border border-emerald-500/30'
  if (item.winRate >= 65 && item.overallBreakRate < 35) return 'bg-cyan-500/20 text-cyan-300 border border-cyan-500/30'
  return 'bg-rose-500/20 text-rose-300 border border-rose-500/30'
}

// 9. API 请求数据
const fetchData = async () => {
  loading.value = true
  try {
    const params = {}
    if (dateRange.value && dateRange.value.length === 2) {
      params.startDate = dateRange.value[0]
      params.endDate = dateRange.value[1]
    }

    // 1. 获取大数据宏观汇总 (client.js 拦截器已默认解包 res.data)
    const sumRes = await api.getHandicapOddsStatsSummary(params)
    if (sumRes) {
      summary.value = sumRes.data || sumRes
    }

    // 2. 获取分页明细数据
    const pageParams = {
      ...params,
      page: currentPage.value,
      size: pageSize.value
    }
    const pageRes = await api.getHandicapOddsStatsPage(pageParams)
    if (pageRes) {
      const pageData = pageRes.data || pageRes
      recordList.value = pageData.content || pageData || []
      totalRecordsCount.value = pageData.totalElements ?? (Array.isArray(pageData) ? pageData.length : (recordList.value.length || 0))
    }

    await nextTick()
    renderCharts()
  } catch (err) {
    console.error('获取让球赔率大数据战绩失败:', err)
    ElMessage.error(err.response?.data?.message || '获取数据失败，请重试')
  } finally {
    loading.value = false
  }
}

const handleDateRangeChange = () => {
  currentPage.value = 1
  fetchData()
}

const changePage = (page) => {
  currentPage.value = page
  fetchData()
}

// 10. ECharts 图表渲染系统 (下注参考专用看板)
const renderCharts = () => {
  renderBettingOutcomeChart()
  renderDistributionChart()
  renderRadarComparisonChart()
}

// 图表 1 (核心): 主客让球方胜赔最终赛果全景图 (100% 堆叠条形图)
const renderBettingOutcomeChart = () => {
  if (!bettingOutcomeChartRef.value) return
  if (!bettingOutcomeChart) {
    bettingOutcomeChart = echarts.init(bettingOutcomeChartRef.value)
  }

  const hList = summary.value.homeIntervals || []
  const aList = summary.value.awayIntervals || []
  let rawList = []

  if (bettingOutcomeView.value === 'HOME') {
    rawList = hList.map(i => ({ ...i, displayName: `🏠 主让 ${i.intervalCode} (${i.intervalName})` }))
  } else if (bettingOutcomeView.value === 'AWAY') {
    rawList = aList.map(i => ({ ...i, displayName: `✈️ 客让 ${i.intervalCode} (${i.intervalName})` }))
  } else {
    rawList = [
      ...hList.map(i => ({ ...i, displayName: `🏠 主让 ${i.intervalCode} (${i.intervalName})` })),
      ...aList.map(i => ({ ...i, displayName: `✈️ 客让 ${i.intervalCode} (${i.intervalName})` }))
    ]
  }

  // 倒序排列，使图表从上到下为 R1 -> R6
  const list = [...rawList].reverse()
  const yCategories = list.map(i => i.displayName)

  const breakData = []
  const unbreakData = []
  const drawData = []
  const lossData = []

  list.forEach(i => {
    const total = i.totalMatches || 0
    if (total > 0) {
      const bPct = Number(((i.breakCount / total) * 100).toFixed(1))
      const uPct = Number(((i.unbreakCount / total) * 100).toFixed(1))
      const dPct = Number(((i.drawCount / total) * 100).toFixed(1))
      const lPct = Number(((i.lossCount / total) * 100).toFixed(1))
      breakData.push({ value: bPct, rawCount: i.breakCount, total })
      unbreakData.push({ value: uPct, rawCount: i.unbreakCount, total })
      drawData.push({ value: dPct, rawCount: i.drawCount, total })
      lossData.push({ value: lPct, rawCount: i.lossCount, total })
    } else {
      breakData.push({ value: 0, rawCount: 0, total: 0 })
      unbreakData.push({ value: 0, rawCount: 0, total: 0 })
      drawData.push({ value: 0, rawCount: 0, total: 0 })
      lossData.push({ value: 0, rawCount: 0, total: 0 })
    }
  })

  const option = {
    backgroundColor: 'transparent',
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'shadow' },
      backgroundColor: '#0f172a',
      borderColor: '#334155',
      textStyle: { color: '#f8fafc', fontSize: 12 },
      formatter: (params) => {
        if (!params || !params.length) return ''
        const idx = params[0].dataIndex
        const item = list[idx]
        const total = item.totalMatches || 0

        let tip = `<div class="font-bold text-amber-400 mb-1.5 pb-1 border-b border-slate-700">${item.displayName} (总 ${total} 场)</div>`
        params.forEach(p => {
          const raw = p.data.rawCount || 0
          tip += `<div class="flex items-center justify-between gap-5 text-xs py-0.5">
            <span style="color:${p.color}">● ${p.seriesName}:</span>
            <strong class="text-white font-mono">${raw} 场 (${p.value}%)</strong>
          </div>`
        })

        // 下注直观参考评语
        let advice = '暂无足够样本'
        if (total > 0) {
          const breakRate = item.overallBreakRate || 0
          const winRate = item.winRate || 0
          if (breakRate >= 50) {
            advice = '🔥 穿盘率超50%，极具打穿价值，下注重点买【让球胜】！'
          } else if (winRate >= 65 && breakRate < 35) {
            advice = '⚠️ 胜率高但穿盘率低，卡盘走水严重，建议【买独赢】或【防让平】！'
          } else if (breakRate < 25 && item.lossRate >= 35) {
            advice = '🛑 让球方极易翻车爆冷，坚决买【让球负】或【受让方独赢】！'
          } else {
            advice = '⚖️ 均势对决，建议结合交战历史或直接【买低水独赢】！'
          }
        }
        tip += `<div class="mt-2 pt-1.5 border-t border-slate-800 text-[11px] text-amber-300 font-medium">💡 下注建议: ${advice}</div>`
        return tip
      }
    },
    legend: {
      data: ['赢球打穿让球盘', '赢球卡盘走水 / 输半', '双方打平 (平局)', '爆冷输球 (下盘打出)'],
      textStyle: { color: '#94a3b8', fontSize: 11 },
      top: 0
    },
    grid: { left: '3%', right: '5%', bottom: '5%', top: '10%', containLabel: true },
    xAxis: {
      type: 'value',
      max: 100,
      axisLabel: { color: '#94a3b8', formatter: '{value}%' },
      splitLine: { lineStyle: { color: '#1e293b' } }
    },
    yAxis: {
      type: 'category',
      data: yCategories,
      axisLine: { lineStyle: { color: '#334155' } },
      axisLabel: { color: '#cbd5e1', fontSize: 11, fontWeight: 'bold' }
    },
    series: [
      {
        name: '赢球打穿让球盘',
        type: 'bar',
        stack: 'outcome',
        data: breakData,
        itemStyle: { color: '#10b981' }
      },
      {
        name: '赢球卡盘走水 / 输半',
        type: 'bar',
        stack: 'outcome',
        data: unbreakData,
        itemStyle: { color: '#f59e0b' }
      },
      {
        name: '双方打平 (平局)',
        type: 'bar',
        stack: 'outcome',
        data: drawData,
        itemStyle: { color: '#0ea5e9' }
      },
      {
        name: '爆冷输球 (下盘打出)',
        type: 'bar',
        stack: 'outcome',
        data: lossData,
        itemStyle: { color: '#ef4444' }
      }
    ]
  }

  bettingOutcomeChart.setOption(option)
}

// 图表 2: 六区间胜平负分布复合柱状图
const renderDistributionChart = () => {
  if (!distributionChartRef.value) return
  if (!distributionChart) {
    distributionChart = echarts.init(distributionChartRef.value)
  }

  const list = summary.value.totalIntervals || []
  const xData = list.map(i => `${i.intervalCode}\n(${i.intervalName})`)
  const winData = list.map(i => i.winCount)
  const drawData = list.map(i => i.drawCount)
  const lossData = list.map(i => i.lossCount)
  const breakData = list.map(i => i.breakCount)

  const option = {
    backgroundColor: 'transparent',
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'shadow' },
      backgroundColor: '#0f172a',
      borderColor: '#334155',
      textStyle: { color: '#f8fafc', fontSize: 12 }
    },
    legend: {
      data: ['胜场数', '平场数', '负场数', '打穿让球盘数'],
      textStyle: { color: '#94a3b8' },
      top: 0
    },
    grid: { left: '3%', right: '4%', bottom: '10%', top: '15%', containLabel: true },
    xAxis: {
      type: 'category',
      data: xData,
      axisLine: { lineStyle: { color: '#334155' } },
      axisLabel: { color: '#cbd5e1', fontSize: 11 }
    },
    yAxis: {
      type: 'value',
      splitLine: { lineStyle: { color: '#1e293b' } },
      axisLabel: { color: '#94a3b8' }
    },
    series: [
      {
        name: '胜场数',
        type: 'bar',
        stack: 'total',
        data: winData,
        itemStyle: { color: '#10b981' }
      },
      {
        name: '平场数',
        type: 'bar',
        stack: 'total',
        data: drawData,
        itemStyle: { color: '#f59e0b' }
      },
      {
        name: '负场数',
        type: 'bar',
        stack: 'total',
        data: lossData,
        itemStyle: { color: '#ef4444' }
      },
      {
        name: '打穿让球盘数',
        type: 'line',
        data: breakData,
        itemStyle: { color: '#38bdf8' },
        lineStyle: { width: 3, color: '#38bdf8' },
        symbol: 'circle',
        symbolSize: 8
      }
    ]
  }

  distributionChart.setOption(option)
}

// 图表 3: 主让 vs 客让 六区间胜率与穿盘率雷达对比图
const renderRadarComparisonChart = () => {
  if (!radarComparisonChartRef.value) return
  if (!radarComparisonChart) {
    radarComparisonChart = echarts.init(radarComparisonChartRef.value)
  }

  const hList = summary.value.homeIntervals || []
  const aList = summary.value.awayIntervals || []

  const indicators = intervalConfigs.map(c => ({
    name: `${c.code}\n${c.name}`,
    max: 100
  }))

  const hBreakRates = hList.map(i => Number(i.overallBreakRate) || 0)
  const aBreakRates = aList.map(i => Number(i.overallBreakRate) || 0)
  const hWinRates = hList.map(i => Number(i.winRate) || 0)
  const aWinRates = aList.map(i => Number(i.winRate) || 0)

  const option = {
    backgroundColor: 'transparent',
    tooltip: {
      backgroundColor: '#0f172a',
      borderColor: '#334155',
      textStyle: { color: '#f8fafc', fontSize: 12 }
    },
    legend: {
      data: ['主让综合穿盘率', '主让综合胜率', '客让综合穿盘率', '客让综合胜率'],
      textStyle: { color: '#94a3b8', fontSize: 11 },
      top: 0
    },
    radar: {
      indicator: indicators,
      radius: '65%',
      splitNumber: 4,
      axisName: { color: '#cbd5e1', fontSize: 11, fontWeight: 'bold' },
      splitLine: { lineStyle: { color: '#334155' } },
      splitArea: { areaStyle: { color: ['rgba(30, 41, 59, 0.5)', 'rgba(15, 23, 42, 0.8)'] } },
      axisLine: { lineStyle: { color: '#334155' } }
    },
    series: [
      {
        name: '主客让球胜赔多维对比',
        type: 'radar',
        data: [
          {
            value: hBreakRates,
            name: '主让综合穿盘率',
            itemStyle: { color: '#22d3ee' },
            lineStyle: { width: 2.5, color: '#22d3ee' },
            areaStyle: { color: 'rgba(34, 211, 238, 0.25)' }
          },
          {
            value: hWinRates,
            name: '主让综合胜率',
            itemStyle: { color: '#10b981' },
            lineStyle: { width: 1.5, type: 'dashed', color: '#10b981' }
          },
          {
            value: aBreakRates,
            name: '客让综合穿盘率',
            itemStyle: { color: '#c084fc' },
            lineStyle: { width: 2.5, color: '#c084fc' },
            areaStyle: { color: 'rgba(192, 132, 252, 0.25)' }
          },
          {
            value: aWinRates,
            name: '客让综合胜率',
            itemStyle: { color: '#fbbf24' },
            lineStyle: { width: 1.5, type: 'dashed', color: '#fbbf24' }
          }
        ]
      }
    ]
  }

  radarComparisonChart.setOption(option)
}

const handleResize = () => {
  if (bettingOutcomeChart) bettingOutcomeChart.resize()
  if (distributionChart) distributionChart.resize()
  if (radarComparisonChart) radarComparisonChart.resize()
}

// 11. 对话框表单操作
const resetForm = () => {
  formData.recordDate = new Date().toISOString().split('T')[0]
  formData.batchTitle = ''
  formData.remarks = ''
  intervalConfigs.forEach(c => {
    formData['home' + c.key + 'Win'] = 0
    formData['home' + c.key + 'Draw'] = 0
    formData['home' + c.key + 'Loss'] = 0
    formData['home' + c.key + 'Break'] = 0
    formData['away' + c.key + 'Win'] = 0
    formData['away' + c.key + 'Draw'] = 0
    formData['away' + c.key + 'Loss'] = 0
    formData['away' + c.key + 'Break'] = 0
  })
}

const openCreateModal = () => {
  isEdit.value = false
  editId.value = null
  resetForm()
  dialogVisible.value = true
}

const openEditModal = (record) => {
  isEdit.value = true
  editId.value = record.id
  formData.recordDate = record.recordDate
  formData.batchTitle = record.batchTitle || ''
  formData.remarks = record.remarks || ''

  intervalConfigs.forEach(c => {
    formData['home' + c.key + 'Win'] = record['home' + c.key + 'Win'] || 0
    formData['home' + c.key + 'Draw'] = record['home' + c.key + 'Draw'] || 0
    formData['home' + c.key + 'Loss'] = record['home' + c.key + 'Loss'] || 0
    formData['home' + c.key + 'Break'] = record['home' + c.key + 'Break'] || 0

    formData['away' + c.key + 'Win'] = record['away' + c.key + 'Win'] || 0
    formData['away' + c.key + 'Draw'] = record['away' + c.key + 'Draw'] || 0
    formData['away' + c.key + 'Loss'] = record['away' + c.key + 'Loss'] || 0
    formData['away' + c.key + 'Break'] = record['away' + c.key + 'Break'] || 0
  })

  dialogVisible.value = true
}

const closeDialog = () => {
  dialogVisible.value = false
}

const submitForm = async () => {
  if (!formData.recordDate) {
    ElMessage.warning('请选择战绩日期')
    return
  }

  // 校验穿盘数 <= 胜场数
  for (const c of intervalConfigs) {
    if (formData['home' + c.key + 'Break'] > formData['home' + c.key + 'Win']) {
      ElMessage.error(`主让 ${c.code}(${c.name}) 的穿盘数不能大于胜场数`)
      return
    }
    if (formData['away' + c.key + 'Break'] > formData['away' + c.key + 'Win']) {
      ElMessage.error(`客让 ${c.code}(${c.name}) 的穿盘数不能大于胜场数`)
      return
    }
  }

  submitting.value = true
  try {
    if (isEdit.value && editId.value) {
      await api.updateHandicapOddsStats(editId.value, formData)
      ElMessage.success('更新让球赔率战绩记录成功')
    } else {
      await api.createHandicapOddsStats(formData)
      ElMessage.success('新增让球赔率战绩记录成功')
    }
    dialogVisible.value = false
    fetchData()
  } catch (err) {
    console.error('提交失败:', err)
    ElMessage.error(err.response?.data?.message || '保存失败，请检查数据')
  } finally {
    submitting.value = false
  }
}

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这条让球赔率战绩记录吗？删除后不可恢复。', '删除确认', {
      type: 'warning',
      confirmButtonText: '确定删除',
      cancelButtonText: '取消'
    })
    await api.deleteHandicapOddsStats(id)
    ElMessage.success('删除记录成功')
    fetchData()
  } catch (err) {
    if (err !== 'cancel') {
      console.error('删除记录失败:', err)
      ElMessage.error(err.response?.data?.message || '删除失败')
    }
  }
}

const openSingleCalculatorModal = () => {
  calcDialogVisible.value = true
}

onMounted(() => {
  fetchData()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  if (bettingOutcomeChart) bettingOutcomeChart.dispose()
  if (distributionChart) distributionChart.dispose()
  if (radarComparisonChart) radarComparisonChart.dispose()
})
</script>

<style scoped>
.custom-datepicker :deep(.el-input__wrapper) {
  background-color: rgba(15, 23, 42, 0.8) !important;
  border: 1px solid #334155 !important;
  box-shadow: none !important;
}

.custom-modal :deep(.el-dialog) {
  background-color: #0b0f19 !important;
  border: 1px solid #1e293b !important;
  border-radius: 1.25rem !important;
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.7) !important;
}

.custom-modal :deep(.el-dialog__title) {
  color: #f8fafc !important;
  font-weight: 800 !important;
  font-size: 1.1rem !important;
}

.custom-modal :deep(.el-dialog__body) {
  color: #cbd5e1 !important;
}

:deep(.el-input-number .el-input__wrapper) {
  background-color: #0f172a !important;
  box-shadow: none !important;
  border: 1px solid #334155 !important;
}

:deep(.el-input__inner) {
  color: #f8fafc !important;
}

:deep(.el-textarea__inner) {
  background-color: #0f172a !important;
  color: #f8fafc !important;
  border: 1px solid #334155 !important;
  border-radius: 0.75rem !important;
}
</style>
