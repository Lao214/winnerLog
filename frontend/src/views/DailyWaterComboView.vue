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
          class="px-4 py-2 rounded-xl text-xs sm:text-sm font-bold transition flex items-center gap-2 bg-gradient-to-r from-purple-500 to-indigo-600 text-white shadow-md shadow-purple-500/20 font-extrabold"
        >
          <span>🎯 水位串子战绩版</span>
        </router-link>
      </div>

      <div class="text-xs text-slate-400 hidden sm:flex items-center gap-1.5">
        <span class="w-2 h-2 rounded-full bg-purple-400 animate-pulse"></span>
        <span>当前模式: 每日水位规律串关战绩复盘与大数据投资回报ROI分析</span>
      </div>
    </div>

    <!-- 1. Hero Banner -->
    <div class="relative overflow-hidden rounded-3xl bg-gradient-to-r from-slate-900 via-purple-950/25 to-slate-900 border border-slate-800 p-5 sm:p-8 shadow-2xl">
      <div class="absolute -right-10 -top-10 w-80 h-80 bg-purple-500/10 rounded-full blur-3xl pointer-events-none"></div>
      <div class="absolute -left-10 -bottom-10 w-80 h-80 bg-indigo-500/10 rounded-full blur-3xl pointer-events-none"></div>

      <div class="relative z-10 flex flex-col lg:flex-row lg:items-center lg:justify-between gap-6">
        <div>
          <div class="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-purple-500/10 border border-purple-500/20 text-purple-400 text-xs font-bold mb-3">
            <span>🎯</span> 水位规律串子战绩实录
          </div>
          <h1 class="text-2xl sm:text-3xl md:text-4xl font-extrabold text-white tracking-tight flex items-center gap-3">
            每日水位规律串关收益与大数据分析
          </h1>
          <p class="text-slate-400 text-xs sm:text-sm mt-2 max-w-2xl leading-relaxed">
            手动记录每日<strong>比赛场数</strong>、<strong>常规成本</strong>、<strong>常规利润</strong>与<strong>长串成本</strong>、<strong>长串利润</strong>，实时洞悉<strong>常规/长串/综合 ROI</strong>、<strong>回款演进</strong>、<strong>红单胜率</strong>与资金曲线。
          </p>
        </div>

        <!-- Quick Action & Date Range Filter -->
        <div class="flex flex-wrap items-center gap-2 sm:gap-3 bg-slate-950/70 p-3 rounded-2xl border border-slate-800 backdrop-blur-md">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
            class="!w-64 sm:!w-72"
            :shortcuts="dateShortcuts"
            @change="handleDateRangeChange"
          />

          <button
            @click="handleOpenCreateDialog"
            class="px-4 py-2 rounded-xl text-xs sm:text-sm font-bold bg-gradient-to-r from-purple-400 to-indigo-500 text-slate-950 hover:from-purple-300 hover:to-indigo-400 shadow-lg shadow-purple-500/20 transition-all flex items-center gap-1.5 shrink-0"
          >
            <span>➕</span> 录入串子战绩
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

    <!-- 2. KPI Overview Cards (5 Cards: 综合净利, 综合ROI, 打票总成本, 🚀长串专项战绩, 战绩极值与评级) -->
    <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-5 gap-3 sm:gap-4">
      <!-- Card 1: 累计综合最终净利润 -->
      <div class="glass-card rounded-2xl p-4 sm:p-5 border border-slate-800 bg-slate-900/60 relative overflow-hidden group hover:border-purple-500/40 transition-all">
        <div class="flex items-center justify-between text-slate-400 text-xs mb-2">
          <span class="font-semibold">💰 累计综合最终净利</span>
          <span class="text-[10px] px-2 py-0.5 rounded-full bg-slate-800 text-slate-300 font-mono">
            {{ summary.totalDays || 0 }} 记录日
          </span>
        </div>
        <div
          class="text-2xl sm:text-3xl font-black font-mono tracking-tight"
          :class="Number(summary.totalProfit || 0) >= 0 ? 'text-purple-400' : 'text-rose-400'"
        >
          {{ Number(summary.totalProfit || 0) >= 0 ? '+' : '' }}¥{{ formatNumber(summary.totalProfit) }}
        </div>
        <div class="mt-2.5 pt-2.5 border-t border-slate-800/80 flex items-center justify-between text-[11px] text-slate-400 font-mono">
          <span>常规: <strong class="text-slate-300">¥{{ formatNumber(summary.totalRegularProfit) }}</strong></span>
          <span>长串: <strong :class="Number(summary.totalLongComboProfit || 0) >= 0 ? 'text-indigo-300' : 'text-rose-400'">{{ Number(summary.totalLongComboProfit || 0) >= 0 ? '+' : '' }}¥{{ formatNumber(summary.totalLongComboProfit) }}</strong></span>
        </div>
      </div>

      <!-- Card 2: 综合回报率 ROI -->
      <div class="glass-card rounded-2xl p-4 sm:p-5 border border-slate-800 bg-slate-900/60 relative overflow-hidden group hover:border-purple-500/40 transition-all">
        <div class="flex items-center justify-between text-slate-400 text-xs mb-2">
          <span class="font-semibold">📈 综合回报率 ROI</span>
          <span
            class="text-[10px] px-2 py-0.5 rounded-full font-bold font-mono"
            :class="Number(summary.overallRoi || 0) >= 20 ? 'bg-emerald-500/20 text-emerald-300 border border-emerald-500/30' : Number(summary.overallRoi || 0) >= 0 ? 'bg-amber-500/20 text-amber-300 border border-amber-500/30' : 'bg-rose-500/20 text-rose-300 border border-rose-500/30'"
          >
            {{ Number(summary.overallRoi || 0) >= 20 ? '🔥 高效红单' : Number(summary.overallRoi || 0) >= 0 ? '✨ 正收益' : '⚠️ 回撤状态' }}
          </span>
        </div>
        <div
          class="text-2xl sm:text-3xl font-black font-mono tracking-tight"
          :class="Number(summary.overallRoi || 0) >= 0 ? 'text-emerald-400' : 'text-rose-400'"
        >
          {{ Number(summary.overallRoi || 0) >= 0 ? '+' : '' }}{{ summary.overallRoi || '0.00' }}%
        </div>
        <div class="mt-2.5 pt-2.5 border-t border-slate-800/80 flex items-center justify-between text-[11px] text-slate-400 font-mono">
          <span>常规 ROI: <strong class="text-emerald-400">{{ summary.regularRoi || '0.00' }}%</strong></span>
          <span>长串 ROI: <strong :class="Number(summary.longComboRoi || 0) >= 0 ? 'text-indigo-300' : 'text-rose-400'">{{ summary.longComboRoi || '0.00' }}%</strong></span>
        </div>
      </div>

      <!-- Card 3: 累计打票总成本 -->
      <div class="glass-card rounded-2xl p-4 sm:p-5 border border-slate-800 bg-slate-900/60 relative overflow-hidden group hover:border-slate-700 transition-all">
        <div class="flex items-center justify-between text-slate-400 text-xs mb-2">
          <span class="font-semibold">💵 累计打票总成本</span>
          <span class="text-[10px] px-2 py-0.5 rounded-full bg-slate-800 text-slate-400 font-mono">常规+长串</span>
        </div>
        <div class="text-2xl sm:text-3xl font-black font-mono text-slate-200 tracking-tight">
          ¥{{ formatNumber(summary.totalCost) }}
        </div>
        <div class="mt-2.5 pt-2.5 border-t border-slate-800/80 flex items-center justify-between text-[11px] text-slate-400 font-mono">
          <span>常规: <strong class="text-slate-300">¥{{ formatNumber(summary.totalRegularCost) }}</strong></span>
          <span>长串: <strong class="text-indigo-300">¥{{ formatNumber(summary.totalLongComboCost) }}</strong></span>
        </div>
      </div>

      <!-- Card 4: 🚀 长串战绩专项指标 -->
      <div class="glass-card rounded-2xl p-4 sm:p-5 border border-indigo-500/30 bg-gradient-to-b from-indigo-950/25 to-slate-900 relative overflow-hidden group hover:border-indigo-500/50 transition-all">
        <div class="flex items-center justify-between text-slate-400 text-xs mb-2">
          <span class="font-semibold text-indigo-300">🚀 长串专属战绩</span>
          <span class="text-[10px] px-2 py-0.5 rounded-full bg-indigo-500/20 text-indigo-300 font-bold border border-indigo-500/30 font-mono">
            {{ summary.longComboWinDays || 0 }} 命中日
          </span>
        </div>
        <div
          class="text-2xl sm:text-3xl font-black font-mono tracking-tight"
          :class="Number(summary.totalLongComboProfit || 0) >= 0 ? 'text-indigo-300' : 'text-rose-400'"
        >
          {{ Number(summary.totalLongComboProfit || 0) >= 0 ? '+' : '' }}¥{{ formatNumber(summary.totalLongComboProfit) }}
        </div>
        <div class="mt-2.5 pt-2.5 border-t border-slate-800/80 flex items-center justify-between text-[11px] text-slate-400 font-mono">
          <span>投入: <strong class="text-slate-300">¥{{ formatNumber(summary.totalLongComboCost) }}</strong></span>
          <span>长串ROI: <strong :class="Number(summary.longComboRoi || 0) >= 0 ? 'text-indigo-400' : 'text-rose-400'">{{ summary.longComboRoi || '0.00' }}%</strong></span>
        </div>
      </div>

      <!-- Card 5: 战绩极值与评级 -->
      <div class="glass-card rounded-2xl p-4 sm:p-5 border border-purple-500/30 bg-gradient-to-b from-purple-950/20 to-slate-900 relative overflow-hidden group hover:border-purple-500/50 transition-all">
        <div class="flex items-center justify-between text-slate-400 text-xs mb-2">
          <span class="font-semibold text-purple-300">🏆 战绩极值与评级</span>
          <span class="text-[10px] px-2 py-0.5 rounded-full bg-purple-500/20 text-purple-300 font-bold border border-purple-500/30 font-mono">
            {{ summary.profitableDays || 0 }}红 / {{ summary.lossDays || 0 }}黑
          </span>
        </div>
        <div class="space-y-1 my-1">
          <div class="flex items-center justify-between text-xs font-mono">
            <span class="text-slate-300">单日最高综合盈利:</span>
            <span class="text-emerald-400 font-bold">+¥{{ formatNumber(summary.maxSingleDayProfit) }}</span>
          </div>
          <div class="flex items-center justify-between text-xs font-mono">
            <span class="text-slate-300">单日最大综合回撤:</span>
            <span class="text-rose-400 font-bold">¥{{ formatNumber(summary.maxSingleDayLoss) }}</span>
          </div>
        </div>
        <div class="mt-2 pt-2 border-t border-slate-800/80 text-[10px] text-slate-400 truncate" :title="summary.riskEvaluation">
          {{ summary.riskEvaluation || '实战战绩积累中' }}
        </div>
      </div>
    </div>

    <!-- 3. 独家打单实战操盘决策体系 (My Betting Strategy Model & Guide) -->
    <div class="glass-panel rounded-3xl p-5 sm:p-7 border border-purple-500/30 shadow-2xl bg-gradient-to-b from-slate-900/95 via-purple-950/15 to-slate-950 relative overflow-hidden">
      <!-- Glow effects -->
      <div class="absolute -right-16 -top-16 w-64 h-64 bg-purple-500/10 rounded-full blur-3xl pointer-events-none"></div>
      <div class="absolute -left-16 -bottom-16 w-64 h-64 bg-indigo-500/10 rounded-full blur-3xl pointer-events-none"></div>

      <!-- Header & Toggle -->
      <div class="relative z-10 flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 pb-4 border-b border-slate-800/80">
        <div class="flex items-center gap-3">
          <div class="w-11 h-11 rounded-2xl bg-gradient-to-br from-purple-500/25 to-indigo-500/25 text-purple-300 border border-purple-500/40 flex items-center justify-center text-2xl shrink-0 shadow-lg shadow-purple-500/10">
            🧠
          </div>
          <div>
            <div class="flex items-center gap-2 flex-wrap">
              <h2 class="text-base sm:text-lg font-black text-white tracking-tight flex items-center gap-2">
                独家水位打单实战决策模型
              </h2>
              <span class="text-[11px] px-2.5 py-0.5 rounded-full bg-purple-500/20 text-purple-300 border border-purple-500/30 font-bold">
                胜平负低赔锚定法
              </span>
            </div>
            <p class="text-slate-400 text-xs mt-1">
              核心原则：先看全场胜平负<strong>赔率较低的一方</strong>（盘口优势方/机构看好方），根据水位区间执行纪律化操盘决策。
            </p>
          </div>
        </div>

        <button
          @click="showStrategyGuide = !showStrategyGuide"
          class="px-3.5 py-2 rounded-xl text-xs font-bold bg-slate-800 hover:bg-slate-700 text-slate-200 border border-slate-700 transition flex items-center gap-2 self-start sm:self-auto shrink-0 shadow-sm"
        >
          <span>{{ showStrategyGuide ? '收起操盘体系' : '展开操盘体系' }}</span>
          <span class="text-[10px] transform transition-transform duration-200" :class="{ 'rotate-180': !showStrategyGuide }">▲</span>
        </button>
      </div>

      <!-- Strategy Cards & Mnemonic (Collapsible) -->
      <div v-show="showStrategyGuide" class="relative z-10 mt-5 space-y-4">
        <!-- 5 Strategy Cards Grid -->
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-5 gap-3.5">
          <!-- Card 1: <= 1.50 -->
          <div class="p-4 rounded-2xl bg-slate-900/80 border border-emerald-500/30 flex flex-col justify-between hover:border-emerald-500/50 hover:bg-slate-900 transition group shadow-md">
            <div>
              <div class="flex items-center justify-between gap-1 mb-2">
                <span class="text-[10px] font-bold px-2 py-0.5 rounded-full bg-emerald-500/20 text-emerald-300 border border-emerald-500/30">
                  超低水稳胆
                </span>
                <span class="font-mono text-xs font-bold text-slate-400">≤ 1.50</span>
              </div>
              <div class="text-xs text-slate-400 font-semibold mb-1">操盘策略:</div>
              <div class="text-sm font-black text-emerald-400 flex items-center gap-1 mb-2">
                <span>🎯</span>
                <span>直接买让球方独赢</span>
              </div>
              <p class="text-[11px] text-slate-400 leading-relaxed">
                实力层面断层碾压，机构底仓深度护盘，正路胜率极高，作为多串保底的核心胆材。
              </p>
            </div>
            <div class="mt-3 pt-2.5 border-t border-slate-800/80 text-[10px] text-emerald-400/80 font-mono flex items-center justify-between">
              <span>核心基石稳胆</span>
              <span>确定性高</span>
            </div>
          </div>

          <!-- Card 2: 1.51 ~ 1.69 -->
          <div class="p-4 rounded-2xl bg-slate-900/80 border border-amber-500/30 flex flex-col justify-between hover:border-amber-500/50 hover:bg-slate-900 transition group shadow-md">
            <div>
              <div class="flex items-center justify-between gap-1 mb-2">
                <span class="text-[10px] font-bold px-2 py-0.5 rounded-full bg-amber-500/20 text-amber-300 border border-amber-500/30">
                  中低水多维研判
                </span>
                <span class="font-mono text-xs font-bold text-slate-400">1.51 - 1.69</span>
              </div>
              <div class="text-xs text-slate-400 font-semibold mb-1">操盘策略:</div>
              <div class="text-sm font-black text-amber-400 flex items-center gap-1 mb-2">
                <span>🔍</span>
                <span>买独赢 或 让球负</span>
              </div>
              <p class="text-[11px] text-slate-400 leading-relaxed">
                强队占优但需防“赢球输盘”。结合<strong>历史交战、近期状态、联赛即时排名</strong>研判：状态火热单选独赢，状态疲软或遇克星防让负。
              </p>
            </div>
            <div class="mt-3 pt-2.5 border-t border-slate-800/80 text-[10px] text-amber-400/80 font-mono flex items-center justify-between">
              <span>交锋/近况/战意</span>
              <span>防卡盘倒灶</span>
            </div>
          </div>

          <!-- Card 3: 1.70 ~ 1.79 -->
          <div class="p-4 rounded-2xl bg-slate-900/80 border border-rose-500/30 flex flex-col justify-between hover:border-rose-500/50 hover:bg-slate-900 transition group shadow-md">
            <div>
              <div class="flex items-center justify-between gap-1 mb-2">
                <span class="text-[10px] font-bold px-2 py-0.5 rounded-full bg-rose-500/20 text-rose-300 border border-rose-500/30">
                  中水谨防诱盘
                </span>
                <span class="font-mono text-xs font-bold text-slate-400">1.70 - 1.79</span>
              </div>
              <div class="text-xs text-slate-400 font-semibold mb-1">操盘策略:</div>
              <div class="text-sm font-black text-rose-400 flex items-center gap-1 mb-2">
                <span>🛡️</span>
                <span>买让球方的让球负</span>
              </div>
              <p class="text-[11px] text-slate-400 leading-relaxed">
                机构经典“表面便宜”诱上区间，让球方赢一球走水或直接打平翻车几率大，买让负安全边际强。
              </p>
            </div>
            <div class="mt-3 pt-2.5 border-t border-slate-800/80 text-[10px] text-rose-400/80 font-mono flex items-center justify-between">
              <span>逆向博受让不败</span>
              <span>防诱盘陷阱</span>
            </div>
          </div>

          <!-- Card 4: 1.80 ~ 2.19 -->
          <div class="p-4 rounded-2xl bg-slate-900/80 border border-purple-500/30 flex flex-col justify-between hover:border-purple-500/50 hover:bg-slate-900 transition group shadow-md">
            <div>
              <div class="flex items-center justify-between gap-1 mb-2">
                <span class="text-[10px] font-bold px-2 py-0.5 rounded-full bg-purple-500/20 text-purple-300 border border-purple-500/30">
                  黄金博弈区间
                </span>
                <span class="font-mono text-xs font-bold text-slate-400">1.80 - 2.19</span>
              </div>
              <div class="text-xs text-slate-400 font-semibold mb-1">操盘策略:</div>
              <div class="text-sm font-black text-purple-400 flex items-center gap-1 mb-2">
                <span>🔥</span>
                <span>直接买低赔方独赢</span>
              </div>
              <p class="text-[11px] text-slate-400 leading-relaxed">
                均势博弈中低赔方略占先手，此水位打出回报率极其丰厚（2.0倍左右），是博取高投资回报比(ROI)的核心价值区间。
              </p>
            </div>
            <div class="mt-3 pt-2.5 border-t border-slate-800/80 text-[10px] text-purple-400/80 font-mono flex items-center justify-between">
              <span>黄金价值甜点区</span>
              <span>高ROI来源</span>
            </div>
          </div>

          <!-- Card 5: >= 2.20 -->
          <div class="p-4 rounded-2xl bg-slate-900/80 border border-blue-500/30 flex flex-col justify-between hover:border-blue-500/50 hover:bg-slate-900 transition group shadow-md">
            <div>
              <div class="flex items-center justify-between gap-1 mb-2">
                <span class="text-[10px] font-bold px-2 py-0.5 rounded-full bg-blue-500/20 text-blue-300 border border-blue-500/30">
                  高水让步乏力
                </span>
                <span class="font-mono text-xs font-bold text-slate-400">≥ 2.20</span>
              </div>
              <div class="text-xs text-slate-400 font-semibold mb-1">操盘策略:</div>
              <div class="text-sm font-black text-blue-400 flex items-center gap-1 mb-2">
                <span>⚡</span>
                <span>买让球方的让球负</span>
              </div>
              <p class="text-[11px] text-slate-400 leading-relaxed">
                例如主胜赔 2.23，主队让球 (-1)，直接买<strong>主让球负</strong>。让球方胜赔超2.20支撑无力，逆向博受让下盘胜率极佳。
              </p>
            </div>
            <div class="mt-3 pt-2.5 border-t border-slate-800/80 text-[10px] text-blue-400/80 font-mono flex items-center justify-between">
              <span>例:主2.23让球买让负</span>
              <span>逆向博高胜率</span>
            </div>
          </div>
        </div>

        <!-- Mnemonic Bar -->
        <div class="p-3 sm:p-4 rounded-2xl bg-gradient-to-r from-purple-950/40 via-slate-900 to-indigo-950/40 border border-purple-500/20 flex flex-col sm:flex-row sm:items-center justify-between gap-3 text-xs">
          <div class="flex items-center gap-2 flex-wrap">
            <span class="text-base">💡</span>
            <span class="font-bold text-purple-300">实战操盘口诀速记：</span>
            <span class="text-slate-300 leading-normal">
              超低稳胆买让赢 · 中低多维看交锋 · 一七诱盘买让负 · 一八二一买低赢 · 二二以上果断让负！
            </span>
          </div>
          <div class="text-[11px] font-mono text-slate-400 shrink-0">
            锚定规则：<strong>以胜平负较低方为基准</strong>
          </div>
        </div>
      </div>
    </div>

    <!-- 4. 大数据洞察智能横幅 (Insight Banner) -->
    <div class="p-4 rounded-2xl bg-slate-900/90 border border-slate-800 flex flex-col md:flex-row md:items-center justify-between gap-3 shadow-lg">
      <div class="flex items-center gap-3">
        <div class="w-10 h-10 rounded-xl bg-purple-500/20 text-purple-400 border border-purple-500/30 flex items-center justify-center text-lg shrink-0">
          💡
        </div>
        <div>
          <div class="text-xs font-semibold text-slate-300">大数据串关战绩研判</div>
          <div class="text-xs sm:text-sm text-purple-300 font-medium mt-0.5">
            {{ summary.riskEvaluation || '根据每日打票成本与净利润自动分析长周期收益与回撤表现' }}
          </div>
        </div>
      </div>
      <div class="flex items-center gap-4 text-xs font-mono text-slate-400 shrink-0 self-end md:self-center">
        <div>
          <span class="text-slate-500">红单天数: </span>
          <span class="text-emerald-400 font-bold">{{ summary.profitableDays || 0 }} 天</span>
        </div>
        <div>
          <span class="text-slate-500">黑单天数: </span>
          <span class="text-rose-400 font-bold">{{ summary.lossDays || 0 }} 天</span>
        </div>
        <div>
          <span class="text-slate-500">保本天数: </span>
          <span class="text-amber-400 font-bold">{{ summary.breakevenDays || 0 }} 天</span>
        </div>
      </div>
    </div>

    <!-- 4. ECharts 大数据看板 (2 Charts) -->
    <div class="grid grid-cols-1 lg:grid-cols-3 gap-5 sm:gap-6">
      <!-- Chart 1: 累计净利润曲线与常规/长串单日盈亏柱状图 (占2列) -->
      <div class="lg:col-span-2 glass-panel rounded-3xl p-4 sm:p-6 border border-slate-800 shadow-xl bg-slate-900/70">
        <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-2 mb-4">
          <div>
            <h2 class="text-base sm:text-lg font-bold text-white flex items-center gap-2">
              <span>📈</span> 累计资金曲线与常规/长串盈亏双轨走势
            </h2>
            <p class="text-slate-400 text-xs mt-0.5">柱状代表单日常规与长串净利分布，折线代表资金累计综合净利润演进</p>
          </div>
          <div class="flex flex-wrap items-center gap-2.5 text-xs">
            <span class="inline-flex items-center gap-1 text-purple-400 font-mono">
              <span class="w-2.5 h-2.5 rounded-full bg-purple-500"></span> 累计总净利
            </span>
            <span class="inline-flex items-center gap-1 text-emerald-400 font-mono">
              <span class="w-2.5 h-2.5 rounded-sm bg-emerald-500"></span> 常规净利
            </span>
            <span class="inline-flex items-center gap-1 text-indigo-400 font-mono">
              <span class="w-2.5 h-2.5 rounded-sm bg-indigo-500"></span> 长串净利
            </span>
          </div>
        </div>
        <div ref="profitTrendChartRef" class="w-full h-72 sm:h-80"></div>
      </div>

      <!-- Chart 2: 盈亏天数与红单胜率分布 (占1列) -->
      <div class="glass-panel rounded-3xl p-4 sm:p-6 border border-slate-800 shadow-xl bg-slate-900/70">
        <div class="mb-4">
          <h2 class="text-base sm:text-lg font-bold text-white flex items-center gap-2">
            <span>🎯</span> 串关红单胜率分布
          </h2>
          <p class="text-slate-400 text-xs mt-0.5">红单盈利日 vs 黑单亏损日比例</p>
        </div>
        <div ref="winLossPieChartRef" class="w-full h-72 sm:h-80"></div>
      </div>
    </div>

    <!-- 5. 每日水位串子战绩表格 (Table) -->
    <div class="glass-panel rounded-3xl p-4 sm:p-6 border border-slate-800 shadow-xl bg-slate-900/70">
      <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 mb-5">
        <div>
          <h2 class="text-base sm:text-lg font-bold text-white flex items-center gap-2">
            <span>📋</span> 每日水位串关战绩明细档案
          </h2>
          <p class="text-slate-400 text-xs mt-0.5">记录常规成本利润与长串成本利润，自动聚合综合总成本、总净利与投资回报率ROI</p>
        </div>
        <div class="text-xs text-slate-400 font-mono">
          共 <span class="font-bold text-purple-400">{{ recordList.length }}</span> 条战绩档案
        </div>
      </div>

      <div class="overflow-x-auto">
        <table class="w-full text-left text-xs text-slate-300">
          <thead class="bg-slate-950/60 text-slate-400 uppercase text-[11px] font-semibold border-b border-slate-800">
            <tr>
              <th class="py-3 px-3">比赛日期</th>
              <th class="py-3 px-3 text-center">比赛场数</th>
              <th class="py-3 px-3">串关策略 / 备注</th>
              <th class="py-3 px-3 text-right">打票成本 (综合 / 常规+长串)</th>
              <th class="py-3 px-3 text-right font-bold">最终净利润 (综合 / 常规+长串)</th>
              <th class="py-3 px-3 text-right">总回款/派彩</th>
              <th class="py-3 px-3 text-center">回报率 (综合 / 长串)</th>
              <th class="py-3 px-3 text-center">收益评级</th>
              <th class="py-3 px-3 text-right">操作</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-slate-800/60">
            <tr
              v-for="row in recordList"
              :key="row.id"
              class="hover:bg-slate-800/40 transition group"
            >
              <!-- 日期 -->
              <td class="py-3 px-3 font-mono">
                <div class="font-bold text-slate-200">{{ row.recordDate }}</div>
              </td>

              <!-- 比赛场数 -->
              <td class="py-3 px-3 text-center font-mono">
                <span class="px-2 py-0.5 rounded-md bg-slate-800 text-slate-200 font-bold">
                  {{ row.matchCount }} 场
                </span>
                <div class="text-[10px] text-slate-500 mt-0.5">
                  场均成本 ¥{{ formatNumber(row.avgMatchCost) }}
                </div>
              </td>

              <!-- 串关类型与备注 -->
              <td class="py-3 px-3">
                <div class="font-semibold text-purple-300">{{ row.comboType || '水位对冲串关' }}</div>
                <div class="text-[10px] text-slate-400 max-w-[200px] truncate" :title="row.remarks">
                  {{ row.remarks || '无复盘备注' }}
                </div>
              </td>

              <!-- 打票成本 (综合 / 常规+长串) -->
              <td class="py-3 px-3 text-right font-mono">
                <div class="font-bold text-slate-100 text-sm">
                  ¥{{ formatNumber(row.totalCost || (Number(row.ticketCost) + Number(row.longComboCost || 0))) }}
                </div>
                <div class="text-[10px] text-slate-400 flex items-center justify-end gap-1 mt-0.5">
                  <span class="text-slate-400">常规:¥{{ formatNumber(row.ticketCost) }}</span>
                  <span class="text-slate-600">|</span>
                  <span class="text-indigo-400 font-semibold">长串:¥{{ formatNumber(row.longComboCost || 0) }}</span>
                </div>
              </td>

              <!-- 最终利润 (综合 / 常规+长串) -->
              <td class="py-3 px-3 text-right font-mono">
                <div
                  class="font-black text-sm"
                  :class="Number(row.totalProfit != null ? row.totalProfit : (Number(row.finalProfit) + Number(row.longComboProfit || 0))) > 0 ? 'text-emerald-400' : Number(row.totalProfit != null ? row.totalProfit : (Number(row.finalProfit) + Number(row.longComboProfit || 0))) < 0 ? 'text-rose-400' : 'text-slate-300'"
                >
                  {{ Number(row.totalProfit != null ? row.totalProfit : (Number(row.finalProfit) + Number(row.longComboProfit || 0))) > 0 ? '+' : '' }}¥{{ formatNumber(row.totalProfit != null ? row.totalProfit : (Number(row.finalProfit) + Number(row.longComboProfit || 0))) }}
                </div>
                <div class="text-[10px] flex items-center justify-end gap-1 mt-0.5">
                  <span :class="Number(row.finalProfit) >= 0 ? 'text-emerald-400/80' : 'text-rose-400/80'">
                    常规:{{ Number(row.finalProfit) >= 0 ? '+' : '' }}¥{{ formatNumber(row.finalProfit) }}
                  </span>
                  <span class="text-slate-600">|</span>
                  <span :class="Number(row.longComboProfit || 0) >= 0 ? 'text-indigo-400 font-semibold' : 'text-rose-400/90'">
                    长串:{{ Number(row.longComboProfit || 0) >= 0 ? '+' : '' }}¥{{ formatNumber(row.longComboProfit || 0) }}
                  </span>
                </div>
              </td>

              <!-- 打票回款 -->
              <td class="py-3 px-3 text-right font-mono text-slate-200 font-bold">
                ¥{{ formatNumber(row.totalReturn) }}
              </td>

              <!-- 单日 ROI (综合 / 长串) -->
              <td class="py-3 px-3 text-center font-mono">
                <div>
                  <span
                    class="px-2 py-0.5 rounded-md text-xs font-bold inline-block"
                    :class="Number(row.roiRate) > 0 ? 'bg-emerald-500/20 text-emerald-300 border border-emerald-500/30' : Number(row.roiRate) < 0 ? 'bg-rose-500/20 text-rose-300 border border-rose-500/30' : 'bg-slate-800 text-slate-400'"
                  >
                    {{ Number(row.roiRate) > 0 ? '+' : '' }}{{ row.roiRate }}%
                  </span>
                </div>
                <div v-if="Number(row.longComboCost || 0) > 0" class="text-[10px] mt-0.5">
                  <span :class="Number(row.longComboRoi || 0) >= 0 ? 'text-indigo-300' : 'text-rose-400'">
                    长串ROI: {{ Number(row.longComboRoi || 0) >= 0 ? '+' : '' }}{{ row.longComboRoi }}%
                  </span>
                </div>
                <div v-else class="text-[10px] text-slate-600 mt-0.5">
                  无长串
                </div>
              </td>

              <!-- 评级徽章 -->
              <td class="py-3 px-3 text-center">
                <span
                  class="inline-block px-2 py-0.5 rounded-full text-[10px] font-bold"
                  :class="row.profitLevel === 'GREAT_PROFIT' ? 'bg-purple-500/20 text-purple-300 border border-purple-500/30' : row.profitLevel === 'PROFIT' ? 'bg-emerald-500/20 text-emerald-300 border border-emerald-500/30' : row.profitLevel === 'BREAKEVEN' ? 'bg-amber-500/20 text-amber-300 border border-amber-500/30' : 'bg-rose-500/20 text-rose-300 border border-rose-500/30'"
                >
                  {{ row.profitLevel === 'GREAT_PROFIT' ? '🔥 翻倍大红' : row.profitLevel === 'PROFIT' ? '✨ 稳健收米' : row.profitLevel === 'BREAKEVEN' ? '🛡️ 保本走水' : row.profitLevel === 'LOSS' ? '⚡ 小幅回撤' : '⚠️ 深度黑单' }}
                </span>
              </td>

              <!-- 操作 -->
              <td class="py-3 px-3 text-right">
                <div class="flex items-center justify-end gap-1.5">
                  <button
                    @click="handleEdit(row)"
                    class="p-1.5 rounded-lg bg-slate-800 hover:bg-slate-700 text-slate-300 transition"
                    title="编辑记录"
                  >
                    ✏️
                  </button>
                  <button
                    @click="handleDelete(row)"
                    class="p-1.5 rounded-lg bg-rose-950/40 hover:bg-rose-900/60 text-rose-400 transition"
                    title="删除记录"
                  >
                    🗑️
                  </button>
                </div>
              </td>
            </tr>
            <tr v-if="recordList.length === 0">
              <td colspan="9" class="text-center py-12 text-slate-500 text-xs">
                暂无每日水位串子战绩记录，点击右上角「➕ 录入串子战绩」开始记录吧
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- 6. 录入/编辑弹窗 (Dialog) - 包含实时动态计算面板 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEditing ? '✏️ 修改每日水位串子战绩' : '➕ 录入每日水位串子战绩'"
      width="90%"
      class="max-w-2xl !rounded-3xl !bg-slate-900 border !border-slate-800 !text-slate-100"
      destroy-on-close
    >
      <div class="space-y-6">
        <!-- 实时计算预览面板 (Live Preview) -->
        <div class="p-4 rounded-2xl bg-gradient-to-r from-slate-950 via-purple-950/30 to-slate-950 border border-purple-500/30 shadow-lg">
          <div class="flex items-center justify-between text-xs text-purple-400 font-bold mb-3">
            <span class="flex items-center gap-1.5">
              <span class="w-2 h-2 rounded-full bg-purple-400 animate-ping"></span>
              <span>⚡ 实时动态计算面板 (常规 + 长串联动)</span>
            </span>
            <span class="font-mono text-slate-400 text-[11px]">
              综合评级: <strong class="text-white">{{ livePreview.profitLevelText }}</strong>
            </span>
          </div>

          <!-- 第一排：常规 vs 长串细分指标 -->
          <div class="grid grid-cols-2 gap-3 mb-3">
            <!-- 常规串指标 -->
            <div class="bg-slate-900/90 p-2.5 rounded-xl border border-slate-800/80">
              <div class="flex items-center justify-between text-[11px] text-slate-400 mb-1">
                <span>🎯 常规串指标</span>
                <span
                  class="font-mono font-bold"
                  :class="Number(livePreview.regularRoi) >= 0 ? 'text-emerald-400' : 'text-rose-400'"
                >
                  ROI: {{ Number(livePreview.regularRoi) >= 0 ? '+' : '' }}{{ livePreview.regularRoi }}%
                </span>
              </div>
              <div class="flex items-center justify-between text-xs font-mono">
                <span class="text-slate-400">成本: ¥{{ livePreview.regularCost }}</span>
                <span :class="Number(livePreview.regularProfit) >= 0 ? 'text-emerald-400 font-bold' : 'text-rose-400 font-bold'">
                  净利: {{ Number(livePreview.regularProfit) >= 0 ? '+' : '' }}¥{{ livePreview.regularProfit }}
                </span>
              </div>
            </div>

            <!-- 长串指标 -->
            <div class="bg-slate-900/90 p-2.5 rounded-xl border border-indigo-500/30">
              <div class="flex items-center justify-between text-[11px] text-indigo-300 mb-1">
                <span>🚀 梦想长串指标</span>
                <span
                  class="font-mono font-bold"
                  :class="Number(livePreview.longComboRoi) >= 0 ? 'text-indigo-400' : 'text-rose-400'"
                >
                  ROI: {{ Number(livePreview.longComboRoi) >= 0 ? '+' : '' }}{{ livePreview.longComboRoi }}%
                </span>
              </div>
              <div class="flex items-center justify-between text-xs font-mono">
                <span class="text-slate-400">成本: ¥{{ livePreview.longComboCost }}</span>
                <span :class="Number(livePreview.longComboProfit) >= 0 ? 'text-indigo-300 font-bold' : 'text-rose-400 font-bold'">
                  净利: {{ Number(livePreview.longComboProfit) >= 0 ? '+' : '' }}¥{{ livePreview.longComboProfit }}
                </span>
              </div>
            </div>
          </div>

          <!-- 第二排：综合总指标 -->
          <div class="grid grid-cols-2 sm:grid-cols-4 gap-3">
            <!-- 单日打票总成本 -->
            <div class="bg-slate-900/90 p-3 rounded-xl border border-slate-800">
              <div class="text-[11px] text-slate-400">综合总成本</div>
              <div class="text-lg font-black font-mono text-slate-200 mt-1">
                ¥{{ livePreview.totalCost }}
              </div>
              <div class="text-[10px] text-slate-500 mt-0.5">常规+长串</div>
            </div>

            <!-- 综合总利润 -->
            <div class="bg-slate-900/90 p-3 rounded-xl border border-slate-800">
              <div class="text-[11px] text-slate-400">综合总净利</div>
              <div
                class="text-lg font-black font-mono mt-1"
                :class="Number(livePreview.totalProfit) >= 0 ? 'text-emerald-400' : 'text-rose-400'"
              >
                {{ Number(livePreview.totalProfit) >= 0 ? '+' : '' }}¥{{ livePreview.totalProfit }}
              </div>
              <div class="text-[10px] text-slate-500 mt-0.5">单日综合盈亏</div>
            </div>

            <!-- 单日总派彩回款 -->
            <div class="bg-slate-900/90 p-3 rounded-xl border border-slate-800">
              <div class="text-[11px] text-slate-400">综合总回款</div>
              <div class="text-lg font-black font-mono text-purple-300 mt-1">
                ¥{{ livePreview.totalReturn }}
              </div>
              <div class="text-[10px] text-slate-500 mt-0.5">总成本+总净利</div>
            </div>

            <!-- 综合投资回报率 ROI -->
            <div class="bg-slate-900/90 p-3 rounded-xl border border-slate-800">
              <div class="text-[11px] text-slate-400">综合 ROI</div>
              <div
                class="text-lg font-black font-mono mt-1"
                :class="Number(livePreview.roiRate) >= 0 ? 'text-emerald-400' : 'text-rose-400'"
              >
                {{ Number(livePreview.roiRate) >= 0 ? '+' : '' }}{{ livePreview.roiRate }}%
              </div>
              <div class="text-[10px] text-slate-500 mt-0.5">总利润 / 总成本</div>
            </div>
          </div>
        </div>

        <!-- 弹窗内打单操盘口诀速查折叠面板 -->
        <div class="rounded-2xl bg-slate-950/70 border border-purple-500/30 overflow-hidden shadow-md">
          <button
            type="button"
            @click="showModalStrategyGuide = !showModalStrategyGuide"
            class="w-full px-4 py-2.5 flex items-center justify-between text-xs font-bold text-purple-300 hover:bg-purple-950/30 transition"
          >
            <span class="flex items-center gap-2">
              <span class="text-sm">💡</span>
              <span>打单赔率区间口诀速查 (1.8-2.19买低方 / ≥2.2买让负 / 1.7-1.79买让负 / 1.51-1.69看状态 / ≤1.5买让赢)</span>
            </span>
            <span class="text-[11px] text-purple-400 font-mono flex items-center gap-1">
              <span>{{ showModalStrategyGuide ? '收起' : '展开对照' }}</span>
              <span class="transform transition-transform text-[10px]" :class="{ 'rotate-180': showModalStrategyGuide }">▼</span>
            </span>
          </button>

          <div v-show="showModalStrategyGuide" class="p-3 pt-0 border-t border-slate-800/80">
            <div class="grid grid-cols-1 sm:grid-cols-5 gap-2 mt-2">
              <!-- 1. <= 1.50 -->
              <div class="p-2 rounded-xl bg-slate-900/90 border border-emerald-500/30">
                <div class="text-[10px] font-bold text-emerald-400 font-mono">≤ 1.50 超低水</div>
                <div class="text-xs font-black text-slate-100 mt-0.5">🎯 买让球方独赢</div>
                <div class="text-[10px] text-slate-400 mt-0.5">绝对优势·基石稳胆</div>
              </div>
              <!-- 2. 1.51 - 1.69 -->
              <div class="p-2 rounded-xl bg-slate-900/90 border border-amber-500/30">
                <div class="text-[10px] font-bold text-amber-400 font-mono">1.51 - 1.69 中低水</div>
                <div class="text-xs font-black text-slate-100 mt-0.5">🔍 买独赢 或 让负</div>
                <div class="text-[10px] text-slate-400 mt-0.5">看交锋/近况/排名定</div>
              </div>
              <!-- 3. 1.70 - 1.79 -->
              <div class="p-2 rounded-xl bg-slate-900/90 border border-rose-500/30">
                <div class="text-[10px] font-bold text-rose-400 font-mono">1.70 - 1.79 中水</div>
                <div class="text-xs font-black text-slate-100 mt-0.5">🛡️ 买让球方让负</div>
                <div class="text-[10px] text-slate-400 mt-0.5">谨防诱上·博受让</div>
              </div>
              <!-- 4. 1.80 - 2.19 -->
              <div class="p-2 rounded-xl bg-slate-900/90 border border-purple-500/30">
                <div class="text-[10px] font-bold text-purple-400 font-mono">1.80 - 2.19 黄金区</div>
                <div class="text-xs font-black text-slate-100 mt-0.5">🔥 直接买低方赢</div>
                <div class="text-[10px] text-slate-400 mt-0.5">均势博弈·ROI最丰厚</div>
              </div>
              <!-- 5. >= 2.20 -->
              <div class="p-2 rounded-xl bg-slate-900/90 border border-blue-500/30">
                <div class="text-[10px] font-bold text-blue-400 font-mono">≥ 2.20 高水</div>
                <div class="text-xs font-black text-slate-100 mt-0.5">⚡ 买让球方让负</div>
                <div class="text-[10px] text-slate-400 mt-0.5">如主2.23主-1买主让负</div>
              </div>
            </div>
            <div class="mt-2 text-[10px] text-slate-400 flex items-center justify-between">
              <span>基准原则：以全场胜平负赔率较低一方为研判基准</span>
              <span class="text-purple-300 font-mono">超低买赢 · 中低看近况 · 一七让负 · 一八买低 · 二二让负</span>
            </div>
          </div>
        </div>

        <!-- 录入表单 -->
        <el-form
          ref="formRef"
          :model="form"
          :rules="formRules"
          label-position="top"
          class="custom-form space-y-4"
        >
          <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <!-- 比赛日期 -->
            <el-form-item label="比赛日期" prop="recordDate">
              <el-date-picker
                v-model="form.recordDate"
                type="date"
                placeholder="选择比赛日"
                value-format="YYYY-MM-DD"
                class="!w-full"
              />
            </el-form-item>

            <!-- 比赛场数 -->
            <el-form-item label="下注比赛场数" prop="matchCount">
              <el-input-number
                v-model="form.matchCount"
                :min="0"
                class="!w-full"
                placeholder="请输入分析比赛场数"
              />
            </el-form-item>
          </div>

          <!-- 常规串关成本与利润 -->
          <div class="p-3.5 rounded-2xl bg-slate-950/50 border border-slate-800/80 space-y-3">
            <div class="text-xs font-bold text-slate-300 flex items-center gap-1.5">
              <span>🎯</span>
              <span>常规串关录入 (主单)</span>
            </div>
            <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <!-- 打票成本 -->
              <el-form-item label="常规打票成本 (元)" prop="ticketCost">
                <el-input-number
                  v-model="form.ticketCost"
                  :min="0"
                  :precision="2"
                  :step="50"
                  class="!w-full"
                  placeholder="例如：500.00"
                />
              </el-form-item>

              <!-- 最终利润 -->
              <el-form-item label="常规最终利润 (元，正为盈利，负为亏损)" prop="finalProfit">
                <el-input-number
                  v-model="form.finalProfit"
                  :precision="2"
                  :step="50"
                  class="!w-full"
                  placeholder="正数盈利，负数亏损"
                />
              </el-form-item>
            </div>
          </div>

          <!-- 长串成本与利润 -->
          <div class="p-3.5 rounded-2xl bg-indigo-950/20 border border-indigo-500/30 space-y-3">
            <div class="text-xs font-bold text-indigo-300 flex items-center justify-between">
              <span class="flex items-center gap-1.5">
                <span>🚀</span>
                <span>长串打票录入 (梦想高赔长串，无则填0)</span>
              </span>
              <span class="text-[10px] text-indigo-400/80 font-normal">若未打长串可保持为0</span>
            </div>
            <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <!-- 长串成本 -->
              <el-form-item label="长串成本 (元)" prop="longComboCost">
                <el-input-number
                  v-model="form.longComboCost"
                  :min="0"
                  :precision="2"
                  :step="50"
                  class="!w-full"
                  placeholder="例如：100.00 (无则填0)"
                />
              </el-form-item>

              <!-- 长串利润 -->
              <el-form-item label="长串利润 (元，正为盈利，负为亏损)" prop="longComboProfit">
                <el-input-number
                  v-model="form.longComboProfit"
                  :precision="2"
                  :step="50"
                  class="!w-full"
                  placeholder="正数盈利，负数亏损 (如 -100 或 850)"
                />
              </el-form-item>
            </div>
          </div>

          <!-- 串关类型 -->
          <el-form-item label="串关策略 / 玩法类型">
            <el-input
              v-model="form.comboType"
              placeholder="例如：2串1稳健串、3串4防冷串、水位高低配+梦想长串等"
            />
          </el-form-item>

          <!-- 复盘笔记 -->
          <el-form-item label="复盘心得与打票备忘">
            <el-input
              v-model="form.remarks"
              type="textarea"
              :rows="3"
              placeholder="记录今日水位特征，例如高水抓冷成功、长串命中高赔红单、低水正路打出等"
            />
          </el-form-item>
        </el-form>
      </div>

      <template #footer>
        <div class="flex items-center justify-end gap-3 pt-3 border-t border-slate-800">
          <button
            @click="dialogVisible = false"
            class="px-4 py-2 rounded-xl text-xs font-medium text-slate-400 hover:text-white hover:bg-slate-800 transition"
          >
            取消
          </button>
          <button
            @click="handleSubmit"
            :disabled="submitting"
            class="px-5 py-2 rounded-xl text-xs font-bold bg-gradient-to-r from-purple-400 to-indigo-500 text-slate-950 hover:from-purple-300 hover:to-indigo-400 shadow-md shadow-purple-500/20 transition flex items-center gap-1.5"
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
const showStrategyGuide = ref(true)
const showModalStrategyGuide = ref(false)

const dateRange = ref([])
const summary = ref({})
const recordList = ref([])

// ECharts 引用与实例
const profitTrendChartRef = ref(null)
const winLossPieChartRef = ref(null)
let profitTrendChart = null
let winLossPieChart = null

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
  recordDate: '',
  matchCount: 6,
  ticketCost: 500,
  finalProfit: 0,
  longComboCost: 0,
  longComboProfit: 0,
  comboType: '水位对冲串关',
  remarks: ''
})

const formRules = {
  recordDate: [{ required: true, message: '请选择比赛日期', trigger: 'change' }],
  matchCount: [{ required: true, message: '请输入比赛场数', trigger: 'blur' }],
  ticketCost: [{ required: true, message: '请输入常规打票成本', trigger: 'blur' }],
  finalProfit: [{ required: true, message: '请输入常规最终利润', trigger: 'blur' }]
}

// 弹窗实时动态联动计算 (Live Preview)
const livePreview = computed(() => {
  const regularCost = Number(form.ticketCost) || 0
  const regularProfit = Number(form.finalProfit) || 0
  const longCost = Number(form.longComboCost) || 0
  const longProfit = Number(form.longComboProfit) || 0
  const matches = Number(form.matchCount) || 0

  const regularRoi = regularCost > 0 ? ((regularProfit / regularCost) * 100).toFixed(2) : '0.00'
  const longComboRoi = longCost > 0 ? ((longProfit / longCost) * 100).toFixed(2) : '0.00'

  const totalCost = (regularCost + longCost).toFixed(2)
  const totalProfit = (regularProfit + longProfit).toFixed(2)
  const totalReturn = (Number(totalCost) + Number(totalProfit)).toFixed(2)
  const roiRate = Number(totalCost) > 0 ? ((Number(totalProfit) / Number(totalCost)) * 100).toFixed(2) : '0.00'

  const avgMatchCost = matches > 0 ? (Number(totalCost) / matches).toFixed(2) : '0.00'
  const avgMatchProfit = matches > 0 ? (Number(totalProfit) / matches).toFixed(2) : '0.00'

  let profitLevelText = '稳健收米'
  if (Number(roiRate) >= 100) {
    profitLevelText = '🔥 翻倍大红暴击'
  } else if (Number(roiRate) > 0) {
    profitLevelText = '✨ 稳健收米'
  } else if (Number(roiRate) === 0) {
    profitLevelText = '🛡️ 保本走水'
  } else if (Number(roiRate) >= -50) {
    profitLevelText = '⚡ 小幅回撤'
  } else {
    profitLevelText = '⚠️ 深度黑单'
  }

  return {
    regularCost: regularCost.toFixed(2),
    regularProfit: regularProfit.toFixed(2),
    regularRoi,
    longComboCost: longCost.toFixed(2),
    longComboProfit: longProfit.toFixed(2),
    longComboRoi,
    totalCost,
    totalProfit,
    totalReturn,
    roiRate,
    avgMatchCost,
    avgMatchProfit,
    profitLevelText
  }
})

// 数字格式化辅助
const formatNumber = (val) => {
  if (val === undefined || val === null) return '0.00'
  return Number(val).toLocaleString('zh-CN', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  })
}

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
      api.getDailyWaterComboSummary(params),
      api.getDailyWaterCombos(params)
    ])

    summary.value = summaryRes || {}
    recordList.value = listRes || []

    await nextTick()
    renderCharts()
  } catch (err) {
    console.error('获取每日水位串子战绩失败:', err)
  } finally {
    loading.value = false
  }
}

const handleDateRangeChange = () => {
  fetchAllData()
}

// --- 图表渲染 ---
const renderCharts = () => {
  // Chart 1: 累计综合净利润曲线 & 常规/长串单日盈亏柱状图
  if (profitTrendChartRef.value) {
    if (!profitTrendChart) {
      profitTrendChart = echarts.init(profitTrendChartRef.value)
    }

    // 正序按日期排序用于时间轴走势
    const sorted = [...recordList.value].sort((a, b) => new Date(a.recordDate) - new Date(b.recordDate))
    const dates = sorted.map((r) => r.recordDate)
    const dailyRegularProfits = sorted.map((r) => Number(r.finalProfit || 0))
    const dailyLongProfits = sorted.map((r) => Number(r.longComboProfit || 0))

    // 计算累计综合资金曲线
    let runningTotal = 0
    const cumProfits = sorted.map((r) => {
      const dayNet = r.totalProfit != null
        ? Number(r.totalProfit)
        : (Number(r.finalProfit || 0) + Number(r.longComboProfit || 0))
      runningTotal += dayNet
      return Number(runningTotal.toFixed(2))
    })

    const option = {
      backgroundColor: 'transparent',
      tooltip: {
        trigger: 'axis',
        axisPointer: { type: 'cross' },
        formatter: (params) => {
          let tip = `<div class="font-bold mb-1">${params[0].axisValue}</div>`
          params.forEach((item) => {
            const val = Number(item.value)
            tip += `<div class="flex items-center justify-between gap-4 text-xs">
              <span style="color:${item.color}">${item.seriesName}:</span>
              <span class="font-mono font-bold" style="color:${item.color}">${val >= 0 ? '+' : ''}¥${formatNumber(val)}</span>
            </div>`
          })
          return tip
        }
      },
      legend: {
        textStyle: { color: '#94a3b8', fontSize: 11 },
        top: 0
      },
      grid: {
        top: 36,
        left: '3%',
        right: '4%',
        bottom: '8%',
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
          name: '单日净利 (元)',
          nameTextStyle: { color: '#64748b', fontSize: 10 },
          axisLabel: { color: '#64748b', formatter: '¥{value}' },
          splitLine: { lineStyle: { color: 'rgba(51, 65, 85, 0.4)' } }
        },
        {
          type: 'value',
          name: '累计总净利 (元)',
          nameTextStyle: { color: '#c084fc', fontSize: 10 },
          axisLabel: { color: '#c084fc', formatter: '¥{value}' },
          splitLine: { show: false }
        }
      ],
      series: [
        {
          name: '常规净利',
          type: 'bar',
          data: dailyRegularProfits,
          barWidth: 12,
          itemStyle: {
            borderRadius: [3, 3, 0, 0],
            color: (params) => {
              return params.value >= 0 ? '#10b981' : '#f43f5e'
            }
          }
        },
        {
          name: '长串净利',
          type: 'bar',
          data: dailyLongProfits,
          barWidth: 12,
          itemStyle: {
            borderRadius: [3, 3, 0, 0],
            color: (params) => {
              return params.value >= 0 ? '#818cf8' : '#fb7185'
            }
          }
        },
        {
          name: '累计总净利',
          type: 'line',
          yAxisIndex: 1,
          data: cumProfits,
          smooth: true,
          symbol: 'circle',
          symbolSize: 6,
          lineStyle: { width: 3, color: '#c084fc' },
          itemStyle: { color: '#c084fc' },
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: 'rgba(192, 132, 252, 0.3)' },
              { offset: 1, color: 'rgba(192, 132, 252, 0.0)' }
            ])
          }
        }
      ]
    }
    profitTrendChart.setOption(option)
  }

  // Chart 2: 盈利天数 (红单) vs 亏损天数 (黑单) vs 保本天数
  if (winLossPieChartRef.value) {
    if (!winLossPieChart) {
      winLossPieChart = echarts.init(winLossPieChartRef.value)
    }

    const profitDays = Number(summary.value.profitableDays) || 0
    const lossDays = Number(summary.value.lossDays) || 0
    const breakevenDays = Number(summary.value.breakevenDays) || 0

    const option2 = {
      backgroundColor: 'transparent',
      tooltip: {
        trigger: 'item',
        formatter: '{b}: {c} 天 ({d}%)'
      },
      legend: {
        bottom: 0,
        textStyle: { color: '#94a3b8', fontSize: 11 }
      },
      series: [
        {
          name: '盈亏天数分布',
          type: 'pie',
          radius: ['45%', '70%'],
          center: ['50%', '45%'],
          avoidLabelOverlap: false,
          itemStyle: {
            borderRadius: 8,
            borderColor: '#0f172a',
            borderWidth: 2
          },
          label: {
            show: true,
            color: '#cbd5e1',
            formatter: '{b}\n{d}%',
            fontSize: 11
          },
          data: [
            {
              value: profitDays,
              name: '红单盈利日',
              itemStyle: { color: '#10b981' }
            },
            {
              value: lossDays,
              name: '黑单亏损日',
              itemStyle: { color: '#f43f5e' }
            },
            {
              value: breakevenDays,
              name: '保本走水日',
              itemStyle: { color: '#f59e0b' }
            }
          ]
        }
      ]
    }
    winLossPieChart.setOption(option2)
  }
}

// 响应窗口尺寸变化
const handleResize = () => {
  profitTrendChart?.resize()
  winLossPieChart?.resize()
}

// --- 弹窗逻辑 ---
const handleOpenCreateDialog = () => {
  isEditing.value = false
  const today = new Date().toISOString().split('T')[0]
  form.id = null
  form.recordDate = today
  form.matchCount = 6
  form.ticketCost = 500
  form.finalProfit = 0
  form.longComboCost = 0
  form.longComboProfit = 0
  form.comboType = '水位对冲串关'
  form.remarks = ''
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEditing.value = true
  form.id = row.id
  form.recordDate = row.recordDate
  form.matchCount = row.matchCount
  form.ticketCost = Number(row.ticketCost)
  form.finalProfit = Number(row.finalProfit)
  form.longComboCost = Number(row.longComboCost || 0)
  form.longComboProfit = Number(row.longComboProfit || 0)
  form.comboType = row.comboType || '水位对冲串关'
  form.remarks = row.remarks || ''
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm(
    `确定要删除 ${row.recordDate} 的水位串子战绩记录吗？`,
    '删除确认',
    {
      confirmButtonText: '确定删除',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await api.deleteDailyWaterCombo(row.id)
      ElMessage.success('记录删除成功')
      fetchAllData()
    } catch (err) {
      console.error(err)
    }
  })
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    submitting.value = true
    try {
      if (isEditing.value) {
        await api.updateDailyWaterCombo(form.id, form)
        ElMessage.success('水位串子战绩更新成功')
      } else {
        await api.createDailyWaterCombo(form)
        ElMessage.success('水位串子战绩录入成功')
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

onMounted(() => {
  fetchAllData()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  profitTrendChart?.dispose()
  winLossPieChart?.dispose()
})
</script>

<style scoped>
.glass-panel {
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
}
.glass-card {
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
}
:deep(.el-input__wrapper),
:deep(.el-input-number__decrease),
:deep(.el-input-number__increase) {
  background-color: #0b1120 !important;
  border-color: #1e293b !important;
  color: #f1f5f9 !important;
}
:deep(.el-input__inner) {
  color: #f1f5f9 !important;
}
:deep(.el-form-item__label) {
  color: #94a3b8 !important;
  font-size: 11px !important;
  font-weight: 600 !important;
}
</style>
