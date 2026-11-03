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
          class="px-4 py-2 rounded-xl text-xs sm:text-sm font-bold transition flex items-center gap-2 bg-gradient-to-r from-cyan-500 to-blue-600 text-white shadow-md shadow-cyan-500/20 font-extrabold"
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
        <span class="w-2 h-2 rounded-full bg-cyan-400 animate-pulse"></span>
        <span>当前模式: 盘口高中低水位与主客场冷门翻车大数据规律</span>
      </div>
    </div>

    <!-- 1. Hero Banner -->
    <div class="relative overflow-hidden rounded-3xl bg-gradient-to-r from-slate-900 via-cyan-950/20 to-slate-900 border border-slate-800 p-5 sm:p-8 shadow-2xl">
      <div class="absolute -right-10 -top-10 w-80 h-80 bg-cyan-500/10 rounded-full blur-3xl pointer-events-none"></div>
      <div class="absolute -left-10 -bottom-10 w-80 h-80 bg-blue-500/10 rounded-full blur-3xl pointer-events-none"></div>

      <div class="relative z-10 flex flex-col lg:flex-row lg:items-center lg:justify-between gap-6">
        <div>
          <div class="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-cyan-500/10 border border-cyan-500/20 text-cyan-400 text-xs font-bold mb-3">
            <span>🌊</span> 盘口水位冷门大数据
          </div>
          <h1 class="text-2xl sm:text-3xl md:text-4xl font-extrabold text-white tracking-tight flex items-center gap-3">
            历史场次与高中低水翻车复盘
          </h1>
          <p class="text-slate-400 text-xs sm:text-sm mt-2 max-w-2xl leading-relaxed">
            实时记录<strong>历史总场数</strong>、<strong>高水</strong>、<strong>中水</strong>、<strong>低水</strong>场次及各自主客场翻车数，深度研判<strong>高中低水翻车率</strong>与<strong>主客场爆冷规律</strong>。
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
            class="px-4 py-2 rounded-xl text-xs sm:text-sm font-bold bg-gradient-to-r from-cyan-400 to-blue-500 text-slate-950 hover:from-cyan-300 hover:to-blue-400 shadow-lg shadow-cyan-500/20 transition-all flex items-center gap-1.5 shrink-0"
          >
            <span>➕</span> 录入水位战绩
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
      <!-- Card 1: 历史总场数 & 综合总翻车率 -->
      <div class="glass-card rounded-2xl p-4 sm:p-5 border border-slate-800 bg-slate-900/60 relative overflow-hidden group hover:border-cyan-500/40 transition-all">
        <div class="flex items-center justify-between text-slate-400 text-xs mb-2">
          <span class="font-semibold">📊 历史总场数</span>
          <span class="text-[10px] px-2 py-0.5 rounded-full bg-slate-800 text-slate-300">
            {{ summary.totalRecords || 0 }} 批次/天
          </span>
        </div>
        <div class="text-2xl sm:text-3xl font-black font-mono tracking-tight text-white flex items-baseline gap-1.5">
          <span>{{ summary.totalMatches || 0 }}</span>
          <span class="text-xs font-normal text-slate-400">场</span>
        </div>
        <div class="mt-2.5 pt-2.5 border-t border-slate-800/80 flex items-center justify-between text-[11px] text-slate-400">
          <span>总翻车场数:</span>
          <span class="font-mono font-bold" :class="Number(summary.overallUpsetRate || 0) >= 35 ? 'text-rose-400' : 'text-emerald-400'">
            {{ summary.totalUpsetCount || 0 }} 场 ({{ summary.overallUpsetRate || '0.00' }}%)
          </span>
        </div>
      </div>

      <!-- Card 2: 高水场数与翻车率 -->
      <div class="glass-card rounded-2xl p-4 sm:p-5 border border-rose-500/20 bg-slate-900/60 relative overflow-hidden group hover:border-rose-500/40 transition-all">
        <div class="flex items-center justify-between text-slate-400 text-xs mb-2">
          <span class="font-semibold text-rose-300">🔴 高水盘口</span>
          <span class="text-[10px] px-2 py-0.5 rounded-full bg-rose-500/20 text-rose-300 border border-rose-500/30">
            占比 {{ summary.highWaterShareRate || '0.00' }}%
          </span>
        </div>
        <div class="text-2xl sm:text-3xl font-black font-mono tracking-tight text-rose-400 flex items-baseline gap-1">
          <span>{{ summary.avgHighWaterUpsetRate || '0.00' }}%</span>
          <span class="text-xs font-normal text-slate-400">翻车率</span>
        </div>
        <div class="mt-2.5 pt-2.5 border-t border-slate-800/80 text-[11px] text-slate-400 flex items-center justify-between">
          <span>主翻: {{ summary.totalHighWaterHomeUpsetCount || 0 }}场</span>
          <span class="text-slate-500">|</span>
          <span>客翻: {{ summary.totalHighWaterAwayUpsetCount || 0 }}场</span>
          <span class="text-slate-500">|</span>
          <span class="text-slate-300 font-bold">共{{ summary.totalHighWaterMatches || 0 }}场</span>
        </div>
      </div>

      <!-- Card 3: 中水场数与翻车率 -->
      <div class="glass-card rounded-2xl p-4 sm:p-5 border border-amber-500/20 bg-slate-900/60 relative overflow-hidden group hover:border-amber-500/40 transition-all">
        <div class="flex items-center justify-between text-slate-400 text-xs mb-2">
          <span class="font-semibold text-amber-300">🟡 中水盘口</span>
          <span class="text-[10px] px-2 py-0.5 rounded-full bg-amber-500/20 text-amber-300 border border-amber-500/30">
            占比 {{ summary.midWaterShareRate || '0.00' }}%
          </span>
        </div>
        <div class="text-2xl sm:text-3xl font-black font-mono tracking-tight text-amber-400 flex items-baseline gap-1">
          <span>{{ summary.avgMidWaterUpsetRate || '0.00' }}%</span>
          <span class="text-xs font-normal text-slate-400">翻车率</span>
        </div>
        <div class="mt-2.5 pt-2.5 border-t border-slate-800/80 text-[11px] text-slate-400 flex items-center justify-between">
          <span>主翻: {{ summary.totalMidWaterHomeUpsetCount || 0 }}场</span>
          <span class="text-slate-500">|</span>
          <span>客翻: {{ summary.totalMidWaterAwayUpsetCount || 0 }}场</span>
          <span class="text-slate-500">|</span>
          <span class="text-slate-300 font-bold">共{{ summary.totalMidWaterMatches || 0 }}场</span>
        </div>
      </div>

      <!-- Card 4: 低水场数与翻车率 -->
      <div class="glass-card rounded-2xl p-4 sm:p-5 border border-cyan-500/20 bg-slate-900/60 relative overflow-hidden group hover:border-cyan-500/40 transition-all">
        <div class="flex items-center justify-between text-slate-400 text-xs mb-2">
          <span class="font-semibold text-cyan-300">🟢 低水盘口</span>
          <span class="text-[10px] px-2 py-0.5 rounded-full bg-cyan-500/20 text-cyan-300 border border-cyan-500/30">
            占比 {{ summary.lowWaterShareRate || '0.00' }}%
          </span>
        </div>
        <div class="text-2xl sm:text-3xl font-black font-mono tracking-tight text-cyan-400 flex items-baseline gap-1">
          <span>{{ summary.avgLowWaterUpsetRate || '0.00' }}%</span>
          <span class="text-xs font-normal text-slate-400">翻车率</span>
        </div>
        <div class="mt-2.5 pt-2.5 border-t border-slate-800/80 text-[11px] text-slate-400 flex items-center justify-between">
          <span>主翻: {{ summary.totalLowWaterHomeUpsetCount || 0 }}场</span>
          <span class="text-slate-500">|</span>
          <span>客翻: {{ summary.totalLowWaterAwayUpsetCount || 0 }}场</span>
          <span class="text-slate-500">|</span>
          <span class="text-slate-300 font-bold">共{{ summary.totalLowWaterMatches || 0 }}场</span>
        </div>
      </div>

      <!-- Card 5: 主客场爆冷倾向对比 -->
      <div class="glass-card rounded-2xl p-4 sm:p-5 border border-blue-500/30 bg-gradient-to-b from-blue-950/20 to-slate-900 relative overflow-hidden group hover:border-blue-500/50 transition-all">
        <div class="flex items-center justify-between text-slate-400 text-xs mb-2">
          <span class="font-semibold text-blue-300">⚖️ 主客爆冷对比</span>
          <span class="text-[10px] px-2 py-0.5 rounded-full bg-blue-500/20 text-blue-300 font-bold border border-blue-500/30">
            {{ summary.upsetTendency?.split(' ')[0] || '均衡' }}
          </span>
        </div>
        <div class="space-y-1 my-1">
          <div class="flex items-center justify-between text-xs font-mono">
            <span class="text-slate-300">主场翻车率:</span>
            <span class="text-blue-400 font-bold">
              {{ summary.overallHomeUpsetRate || '0.00' }}%
              <span class="text-[10px] text-slate-400 font-normal">({{ summary.totalHomeUpsetCount || 0 }}场)</span>
            </span>
          </div>
          <div class="flex items-center justify-between text-xs font-mono">
            <span class="text-slate-300">客场翻车率:</span>
            <span class="text-purple-400 font-bold">
              {{ summary.overallAwayUpsetRate || '0.00' }}%
              <span class="text-[10px] text-slate-400 font-normal">({{ summary.totalAwayUpsetCount || 0 }}场)</span>
            </span>
          </div>
        </div>
        <div class="mt-2 pt-2 border-t border-slate-800/80 text-[10px] text-slate-400 truncate">
          最危险: {{ summary.highestRiskWaterLevel || '数据收集中' }}
        </div>
      </div>
    </div>

    <!-- 3. 水位正邪路与翻车评判准则 (Water Level Upset & Normal Rules Guide) -->
    <div class="glass-panel rounded-3xl p-5 sm:p-7 border border-cyan-500/30 shadow-2xl bg-gradient-to-b from-slate-900/95 via-cyan-950/15 to-slate-950 relative overflow-hidden">
      <!-- Glow effects -->
      <div class="absolute -right-16 -top-16 w-64 h-64 bg-cyan-500/10 rounded-full blur-3xl pointer-events-none"></div>
      <div class="absolute -left-16 -bottom-16 w-64 h-64 bg-blue-500/10 rounded-full blur-3xl pointer-events-none"></div>

      <!-- Header & Toggle -->
      <div class="relative z-10 flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 pb-4 border-b border-slate-800/80">
        <div class="flex items-center gap-3">
          <div class="w-11 h-11 rounded-2xl bg-gradient-to-br from-cyan-500/25 to-blue-500/25 text-cyan-300 border border-cyan-500/40 flex items-center justify-center text-2xl shrink-0 shadow-lg shadow-cyan-500/10">
            ⚖️
          </div>
          <div>
            <div class="flex items-center gap-2 flex-wrap">
              <h2 class="text-base sm:text-lg font-black text-white tracking-tight flex items-center gap-2">
                盘口水位正邪路与翻车研判标准
              </h2>
              <span class="text-[11px] px-2.5 py-0.5 rounded-full bg-cyan-500/20 text-cyan-300 border border-cyan-500/30 font-bold">
                高位 vs 低位界定法则
              </span>
            </div>
            <p class="text-slate-400 text-xs mt-1">
              以让球方赔率水位<strong>1.80</strong>为界限分水岭，实行科学化正邪路与爆冷翻车归类
            </p>
          </div>
        </div>

        <button
          @click="showWaterRuleGuide = !showWaterRuleGuide"
          class="px-3.5 py-2 rounded-xl text-xs font-bold bg-slate-800 hover:bg-slate-700 text-slate-200 border border-slate-700 transition flex items-center gap-2 self-start sm:self-auto shrink-0 shadow-sm"
        >
          <span>{{ showWaterRuleGuide ? '收起判定标准' : '展开判定标准' }}</span>
          <span class="text-[10px] transform transition-transform duration-200" :class="{ 'rotate-180': !showWaterRuleGuide }">▲</span>
        </button>
      </div>

      <!-- Strategy Content (Collapsible) -->
      <div v-show="showWaterRuleGuide" class="relative z-10 mt-5 space-y-4">
        <!-- Two Contrast Cards Grid -->
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <!-- Card 1: 高位盘口 (>= 1.80) -->
          <div class="p-5 rounded-2xl bg-slate-900/85 border border-rose-500/30 flex flex-col justify-between hover:border-rose-500/50 hover:bg-slate-900 transition shadow-lg">
            <div>
              <div class="flex items-center justify-between gap-2 mb-3">
                <span class="text-xs font-bold px-3 py-1 rounded-full bg-rose-500/20 text-rose-300 border border-rose-500/30">
                  🔴 高位盘口（赔率 1.80 以上）
                </span>
                <span class="font-mono text-xs font-bold text-rose-400">让球方胜赔 ≥ 1.80</span>
              </div>

              <div class="space-y-2.5 text-xs">
                <!-- 正路判定 -->
                <div class="p-3 rounded-xl bg-emerald-950/20 border border-emerald-500/25">
                  <div class="flex items-center gap-2 text-emerald-400 font-bold mb-1">
                    <span>✅ 正路打出判定：</span>
                    <span class="text-white bg-emerald-500/20 px-2 py-0.5 rounded-md font-mono">让球方赢球 或 打平（让球方不败）</span>
                  </div>
                  <p class="text-[11px] text-slate-400 leading-relaxed">
                    高位水位下双方实力较为均衡，机构让球力度偏浅，让球方只要守住不败（胜或平）均符合正路预期，不属于翻车。
                  </p>
                </div>

                <!-- 翻车判定 -->
                <div class="p-3 rounded-xl bg-rose-950/20 border border-rose-500/25">
                  <div class="flex items-center gap-2 text-rose-400 font-bold mb-1">
                    <span>❌ 邪路翻车判定：</span>
                    <span class="text-white bg-rose-500/20 px-2 py-0.5 rounded-md font-mono">让球方直接输球</span>
                  </div>
                  <p class="text-[11px] text-slate-400 leading-relaxed">
                    让球方在自身主导的盘口下被客队或受让方直接逆转击败，打出下盘胜负大冷门，方判定为真正的<strong>邪路翻车</strong>。
                  </p>
                </div>
              </div>
            </div>

            <div class="mt-3 pt-2.5 border-t border-slate-800/80 text-[10px] text-slate-400 flex items-center justify-between font-mono">
              <span>高位准则：让球方胜/平 = 正路</span>
              <span class="text-rose-400 font-bold">让球方输球 = 翻车</span>
            </div>
          </div>

          <!-- Card 2: 低位/中低位盘口 (< 1.80) -->
          <div class="p-5 rounded-2xl bg-slate-900/85 border border-cyan-500/30 flex flex-col justify-between hover:border-cyan-500/50 hover:bg-slate-900 transition shadow-lg">
            <div>
              <div class="flex items-center justify-between gap-2 mb-3">
                <span class="text-xs font-bold px-3 py-1 rounded-full bg-cyan-500/20 text-cyan-300 border border-cyan-500/30">
                  🟢 低位 / 中低位盘口（赔率 1.80 以下）
                </span>
                <span class="font-mono text-xs font-bold text-cyan-400">让球方胜赔 &lt; 1.80</span>
              </div>

              <div class="space-y-2.5 text-xs">
                <!-- 正路判定 -->
                <div class="p-3 rounded-xl bg-emerald-950/20 border border-emerald-500/25">
                  <div class="flex items-center gap-2 text-emerald-400 font-bold mb-1">
                    <span>✅ 正路打出判定：</span>
                    <span class="text-white bg-emerald-500/20 px-2 py-0.5 rounded-md font-mono">让球方必须取胜赢球</span>
                  </div>
                  <p class="text-[11px] text-slate-400 leading-relaxed">
                    让球方赔率低于1.80，机构和市场已给予绝对优势倾斜和赢球期望，唯有全取3分赢下比赛才算合格的正路打出。
                  </p>
                </div>

                <!-- 翻车判定 -->
                <div class="p-3 rounded-xl bg-rose-950/20 border border-rose-500/25">
                  <div class="flex items-center gap-2 text-rose-400 font-bold mb-1">
                    <span>❌ 爆冷翻车判定：</span>
                    <span class="text-white bg-rose-500/20 px-2 py-0.5 rounded-md font-mono">没能赢球（打平 或 输球）</span>
                  </div>
                  <p class="text-[11px] text-slate-400 leading-relaxed">
                    1.80以下深盘或低水盘口，让球方<strong>只要没能赢（无论是被逼平还是直接输球）</strong>，均视为大热倒灶、未能穿盘的<strong>爆冷翻车</strong>！
                  </p>
                </div>
              </div>
            </div>

            <div class="mt-3 pt-2.5 border-t border-slate-800/80 text-[10px] text-slate-400 flex items-center justify-between font-mono">
              <span>低位准则：让球方赢球 = 正路</span>
              <span class="text-rose-400 font-bold">未能取胜(平/负) = 翻车</span>
            </div>
          </div>
        </div>

        <!-- Mnemonic Bar -->
        <div class="p-3 sm:p-4 rounded-2xl bg-gradient-to-r from-cyan-950/40 via-slate-900 to-blue-950/40 border border-cyan-500/20 flex flex-col sm:flex-row sm:items-center justify-between gap-3 text-xs">
          <div class="flex items-center gap-2 flex-wrap">
            <span class="text-base">💡</span>
            <span class="font-bold text-cyan-300">翻车判定口诀：</span>
            <span class="text-slate-200">
              高位1.80以上让球方<strong>赢或平为正路，输球算邪路翻车</strong>；1.80以下让球方<strong>没能赢球（平或负）一律算翻车</strong>！
            </span>
          </div>
          <div class="text-[11px] font-mono text-cyan-400/80 shrink-0">
            基准锚点：让球方赔率 1.80 分水岭
          </div>
        </div>
      </div>
    </div>

    <!-- 4. 大数据洞察智能横幅 (Insight Bar) -->
    <div class="p-4 rounded-2xl bg-slate-900/90 border border-slate-800 flex flex-col md:flex-row md:items-center justify-between gap-3 shadow-lg">
      <div class="flex items-center gap-3">
        <div class="w-10 h-10 rounded-xl bg-cyan-500/20 text-cyan-400 border border-cyan-500/30 flex items-center justify-center text-lg shrink-0">
          💡
        </div>
        <div>
          <div class="text-xs font-semibold text-slate-300">大数据研判决策结论</div>
          <div class="text-xs sm:text-sm text-cyan-300 font-medium mt-0.5">
            {{ summary.riskAssessmentSummary || '录入战绩后自动生成大数据冷门研判与策略建议' }}
          </div>
        </div>
      </div>
      <div class="flex items-center gap-4 text-xs font-mono text-slate-400 shrink-0 self-end md:self-center">
        <div>
          <span class="text-slate-500">主场爆冷占比: </span>
          <span class="text-blue-400 font-bold">{{ summary.homeUpsetRatio || '0.00' }}%</span>
        </div>
        <div>
          <span class="text-slate-500">客场爆冷占比: </span>
          <span class="text-purple-400 font-bold">{{ summary.awayUpsetRatio || '0.00' }}%</span>
        </div>
      </div>
    </div>

    <!-- 4. ECharts 大数据对比看板 (2 Charts) -->
    <div class="grid grid-cols-1 lg:grid-cols-3 gap-5 sm:gap-6">
      <!-- Chart 1: 高中低水位翻车率横向对比与正路率 (占2列) -->
      <div class="lg:col-span-2 glass-panel rounded-3xl p-4 sm:p-6 border border-slate-800 shadow-xl bg-slate-900/70">
        <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-2 mb-4">
          <div>
            <h2 class="text-base sm:text-lg font-bold text-white flex items-center gap-2">
              <span>📈</span> 高中低水位翻车率与正路分布对比
            </h2>
            <p class="text-slate-400 text-xs mt-0.5">直观比较各水位翻车率、正路打出率以及主客场爆冷差异</p>
          </div>
          <div class="flex items-center gap-2 text-xs">
            <span class="inline-flex items-center gap-1 text-rose-400">
              <span class="w-2.5 h-2.5 rounded-sm bg-rose-500"></span> 高水翻车
            </span>
            <span class="inline-flex items-center gap-1 text-amber-400">
              <span class="w-2.5 h-2.5 rounded-sm bg-amber-500"></span> 中水翻车
            </span>
            <span class="inline-flex items-center gap-1 text-cyan-400">
              <span class="w-2.5 h-2.5 rounded-sm bg-cyan-500"></span> 低水翻车
            </span>
          </div>
        </div>
        <div ref="waterRatesChartRef" class="w-full h-72 sm:h-80"></div>
      </div>

      <!-- Chart 2: 翻车主客场分布与水位占比 (占1列) -->
      <div class="glass-panel rounded-3xl p-4 sm:p-6 border border-slate-800 shadow-xl bg-slate-900/70">
        <div class="mb-4">
          <h2 class="text-base sm:text-lg font-bold text-white flex items-center gap-2">
            <span>🎯</span> 冷门主客场分布环形图
          </h2>
          <p class="text-slate-400 text-xs mt-0.5">探寻冷门是主场翻车频繁还是客场容易爆冷</p>
        </div>
        <div ref="upsetDistChartRef" class="w-full h-72 sm:h-80"></div>
      </div>
    </div>

    <!-- 5. 历史水位战绩数据表格 (Table) -->
    <div class="glass-panel rounded-3xl p-4 sm:p-6 border border-slate-800 shadow-xl bg-slate-900/70">
      <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 mb-5">
        <div>
          <h2 class="text-base sm:text-lg font-bold text-white flex items-center gap-2">
            <span>📋</span> 历史水位翻车战绩明细档案
          </h2>
          <p class="text-slate-400 text-xs mt-0.5">支持查看单日/批次高水中水低水场次、主客场翻车数及各项实时翻车率</p>
        </div>
        <div class="text-xs text-slate-400">
          共 <span class="font-bold text-cyan-400 font-mono">{{ recordList.length }}</span> 条战绩档案
        </div>
      </div>

      <div class="overflow-x-auto">
        <table class="w-full text-left text-xs text-slate-300">
          <thead class="bg-slate-950/60 text-slate-400 uppercase text-[11px] font-semibold border-b border-slate-800">
            <tr>
              <th class="py-3 px-3">日期 / 批次</th>
              <th class="py-3 px-3 text-center">历史总场数</th>
              <th class="py-3 px-3 text-center text-rose-300">高水 (场/主翻/客翻/翻车率)</th>
              <th class="py-3 px-3 text-center text-amber-300">中水 (场/主翻/客翻/翻车率)</th>
              <th class="py-3 px-3 text-center text-cyan-300">低水 (场/主翻/客翻/翻车率)</th>
              <th class="py-3 px-3 text-center">总翻车 / 实时翻车率</th>
              <th class="py-3 px-3 text-center">冷门倾向与评级</th>
              <th class="py-3 px-3 text-right">操作</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-slate-800/60">
            <tr
              v-for="row in recordList"
              :key="row.id"
              class="hover:bg-slate-800/40 transition group"
            >
              <!-- 日期与批次 -->
              <td class="py-3 px-3 font-mono">
                <div class="font-bold text-slate-200">{{ row.recordDate }}</div>
                <div class="text-[10px] text-slate-400 max-w-[140px] truncate" :title="row.batchTitle || row.remarks">
                  {{ row.batchTitle || row.remarks || '无标题批次' }}
                </div>
              </td>

              <!-- 历史总场数 -->
              <td class="py-3 px-3 text-center font-mono">
                <span class="px-2 py-0.5 rounded-md bg-slate-800 text-slate-200 font-bold">
                  {{ row.totalMatches }} 场
                </span>
              </td>

              <!-- 高水数据 -->
              <td class="py-3 px-3 text-center font-mono">
                <div class="font-bold text-rose-400">
                  {{ row.highWaterUpsetRate }}%
                </div>
                <div class="text-[10px] text-slate-400">
                  总{{ row.highWaterMatches }}场 (主{{ row.highWaterHomeUpsetCount }} / 客{{ row.highWaterAwayUpsetCount }})
                </div>
              </td>

              <!-- 中水数据 -->
              <td class="py-3 px-3 text-center font-mono">
                <div class="font-bold text-amber-400">
                  {{ row.midWaterUpsetRate }}%
                </div>
                <div class="text-[10px] text-slate-400">
                  总{{ row.midWaterMatches }}场 (主{{ row.midWaterHomeUpsetCount }} / 客{{ row.midWaterAwayUpsetCount }})
                </div>
              </td>

              <!-- 低水数据 -->
              <td class="py-3 px-3 text-center font-mono">
                <div class="font-bold text-cyan-400">
                  {{ row.lowWaterUpsetRate }}%
                </div>
                <div class="text-[10px] text-slate-400">
                  总{{ row.lowWaterMatches }}场 (主{{ row.lowWaterHomeUpsetCount }} / 客{{ row.lowWaterAwayUpsetCount }})
                </div>
              </td>

              <!-- 综合总翻车数与实时翻车率 -->
              <td class="py-3 px-3 text-center font-mono">
                <div class="font-black text-sm" :class="Number(row.overallUpsetRate) >= 40 ? 'text-rose-400' : Number(row.overallUpsetRate) >= 25 ? 'text-amber-400' : 'text-emerald-400'">
                  {{ row.overallUpsetRate }}%
                </div>
                <div class="text-[10px] text-slate-400">
                  翻车 {{ row.totalUpsetCount }} / 正路 {{ row.totalNormalCount }}
                </div>
              </td>

              <!-- 爆冷倾向与风险评级 -->
              <td class="py-3 px-3 text-center">
                <div class="inline-flex items-center gap-1 px-2 py-0.5 rounded-full text-[10px] font-bold"
                  :class="row.riskLevel === 'HIGH_RISK' ? 'bg-rose-500/20 text-rose-300 border border-rose-500/30' : row.riskLevel === 'MODERATE' ? 'bg-amber-500/20 text-amber-300 border border-amber-500/30' : 'bg-emerald-500/20 text-emerald-300 border border-emerald-500/30'"
                >
                  <span>{{ row.riskLevel === 'HIGH_RISK' ? '⚠️ 高度爆冷' : row.riskLevel === 'MODERATE' ? '⚡ 中度冷门' : '🛡️ 正路为主' }}</span>
                </div>
                <div class="text-[10px] text-slate-400 mt-0.5">
                  {{ row.upsetTendency === 'HOME_BIAS' ? '主场易冷' : row.upsetTendency === 'AWAY_BIAS' ? '客场易冷' : '主客均衡' }}
                </div>
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
              <td colspan="8" class="text-center py-12 text-slate-500 text-xs">
                暂无盘口水位战绩记录，点击右上角「➕ 录入水位战绩」开始记录吧
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- 6. 录入/编辑弹窗 (Dialog) - 附带实时动态联动计算面板 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEditing ? '✏️ 修改水位翻车战绩' : '➕ 录入盘口水位翻车战绩'"
      width="90%"
      class="max-w-4xl !rounded-3xl !bg-slate-900 border !border-slate-800 !text-slate-100"
      destroy-on-close
    >
      <div class="space-y-6">
        <!-- 弹窗实时动态联动计算卡片 (Live Real-Time Preview) -->
        <div class="p-4 rounded-2xl bg-gradient-to-r from-slate-950 via-cyan-950/30 to-slate-950 border border-cyan-500/30 shadow-lg">
          <div class="flex items-center justify-between text-xs text-cyan-400 font-bold mb-3">
            <span class="flex items-center gap-1.5">
              <span class="w-2 h-2 rounded-full bg-cyan-400 animate-ping"></span>
              <span>⚡ 实时动态计算面板 (输入即时刷新)</span>
            </span>
            <span class="font-mono text-slate-400 text-[11px]">
              动态总场数: <strong class="text-white">{{ livePreview.totalMatches }}</strong> 场
            </span>
          </div>

          <div class="grid grid-cols-2 sm:grid-cols-4 gap-3">
            <!-- 实时总翻车率 -->
            <div class="bg-slate-900/90 p-3 rounded-xl border border-slate-800">
              <div class="text-[11px] text-slate-400">实时综合翻车率</div>
              <div class="text-lg font-black font-mono text-cyan-400 mt-1">
                {{ livePreview.overallUpsetRate }}%
              </div>
              <div class="text-[10px] text-slate-500 mt-0.5">
                翻车 {{ livePreview.totalUpsetCount }} / 正路 {{ livePreview.totalNormalCount }}
              </div>
            </div>

            <!-- 实时高水翻车率 -->
            <div class="bg-slate-900/90 p-3 rounded-xl border border-rose-500/30">
              <div class="text-[11px] text-rose-300">高水综合翻车率</div>
              <div class="text-lg font-black font-mono text-rose-400 mt-1">
                {{ livePreview.highWaterUpsetRate }}%
              </div>
              <div class="text-[10px] text-slate-500 mt-0.5">
                主翻 {{ livePreview.highHomeRate }}% | 客翻 {{ livePreview.highAwayRate }}%
              </div>
            </div>

            <!-- 实时中水翻车率 -->
            <div class="bg-slate-900/90 p-3 rounded-xl border border-amber-500/30">
              <div class="text-[11px] text-amber-300">中水综合翻车率</div>
              <div class="text-lg font-black font-mono text-amber-400 mt-1">
                {{ livePreview.midWaterUpsetRate }}%
              </div>
              <div class="text-[10px] text-slate-500 mt-0.5">
                主翻 {{ livePreview.midHomeRate }}% | 客翻 {{ livePreview.midAwayRate }}%
              </div>
            </div>

            <!-- 实时低水翻车率 -->
            <div class="bg-slate-900/90 p-3 rounded-xl border border-cyan-500/30">
              <div class="text-[11px] text-cyan-300">低水综合翻车率</div>
              <div class="text-lg font-black font-mono text-cyan-400 mt-1">
                {{ livePreview.lowWaterUpsetRate }}%
              </div>
              <div class="text-[10px] text-slate-500 mt-0.5">
                主翻 {{ livePreview.lowHomeRate }}% | 客翻 {{ livePreview.lowAwayRate }}%
              </div>
            </div>
          </div>
        </div>

        <!-- 弹窗内翻车判定准则速查折叠面板 -->
        <div class="rounded-2xl bg-slate-950/70 border border-cyan-500/30 overflow-hidden shadow-md">
          <button
            type="button"
            @click="showModalRuleGuide = !showModalRuleGuide"
            class="w-full px-4 py-2.5 flex items-center justify-between text-xs font-bold text-cyan-300 hover:bg-cyan-950/30 transition"
          >
            <span class="flex items-center gap-2">
              <span class="text-sm">💡</span>
              <span>水位翻车判定准则速查 (高位≥1.80胜平为正路、输球算翻车；低位&lt;1.80没赢一律算翻车)</span>
            </span>
            <span class="text-[11px] text-cyan-400 font-mono flex items-center gap-1">
              <span>{{ showModalRuleGuide ? '收起' : '展开对照' }}</span>
              <span class="transform transition-transform text-[10px]" :class="{ 'rotate-180': showModalRuleGuide }">▼</span>
            </span>
          </button>

          <div v-show="showModalRuleGuide" class="p-3 pt-0 border-t border-slate-800/80">
            <div class="grid grid-cols-1 sm:grid-cols-2 gap-3 mt-2">
              <div class="p-2.5 rounded-xl bg-slate-900/90 border border-rose-500/30 text-xs">
                <div class="text-rose-400 font-bold mb-1 flex items-center justify-between">
                  <span>🔴 高位盘口 (让球方赔率 ≥ 1.80)</span>
                  <span class="text-[10px] text-slate-400 font-mono">均势/浅让</span>
                </div>
                <div class="text-slate-300 text-[11px] space-y-1">
                  <div>• <strong class="text-emerald-400">正路判定</strong>：让球方<strong>赢球</strong> 或 <strong>打平</strong>（不败即正路）</div>
                  <div>• <strong class="text-rose-400">翻车判定</strong>：让球方<strong>直接输球</strong>（判定为邪路翻车）</div>
                </div>
              </div>
              <div class="p-2.5 rounded-xl bg-slate-900/90 border border-cyan-500/30 text-xs">
                <div class="text-cyan-400 font-bold mb-1 flex items-center justify-between">
                  <span>🟢 低位/中低位盘口 (让球方赔率 &lt; 1.80)</span>
                  <span class="text-[10px] text-slate-400 font-mono">深让/优势</span>
                </div>
                <div class="text-slate-300 text-[11px] space-y-1">
                  <div>• <strong class="text-emerald-400">正路判定</strong>：让球方<strong>必须赢球取胜</strong>（全取3分）</div>
                  <div>• <strong class="text-rose-400">翻车判定</strong>：让球方<strong>没能赢球（包括打平 或 输球）</strong>一律算爆冷翻车！</div>
                </div>
              </div>
            </div>
            <div class="mt-2 text-[10px] text-slate-400 flex items-center justify-between">
              <span>基准界限：以让球方赔率 1.80 分水岭划分</span>
              <span class="text-cyan-300 font-mono">高位输球算翻车 · 低位没赢就算翻车</span>
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
          <!-- 基础信息行 -->
          <div class="grid grid-cols-1 sm:grid-cols-3 gap-4">
            <el-form-item label="记录日期 / 比赛日" prop="recordDate">
              <el-date-picker
                v-model="form.recordDate"
                type="date"
                placeholder="请选择日期"
                value-format="YYYY-MM-DD"
                class="!w-full"
              />
            </el-form-item>

            <el-form-item label="批次标题 / 统计阶段说明">
              <el-input
                v-model="form.batchTitle"
                placeholder="例如：2026历史盘口大数据总览"
              />
            </el-form-item>

            <el-form-item label="历史总场数 (留空或0则自动求和)">
              <el-input-number
                v-model="form.totalMatches"
                :min="0"
                class="!w-full"
                placeholder="自动计算"
              />
            </el-form-item>
          </div>

          <!-- 高水盘口属性区域 (Rose) -->
          <div class="p-4 rounded-2xl bg-rose-950/20 border border-rose-500/30 space-y-3">
            <div class="flex items-center justify-between">
              <span class="text-xs font-bold text-rose-300 flex items-center gap-1.5">
                <span>🔴</span> 高水盘口统计
              </span>
              <span class="text-[10px] text-rose-400 font-mono">
                当前高水翻车率: <strong>{{ livePreview.highWaterUpsetRate }}%</strong>
              </span>
            </div>
            <div class="grid grid-cols-1 sm:grid-cols-3 gap-3">
              <el-form-item label="高水场数" prop="highWaterMatches">
                <el-input-number v-model="form.highWaterMatches" :min="0" class="!w-full" />
              </el-form-item>
              <el-form-item label="高水主场翻车场数" prop="highWaterHomeUpsetCount">
                <el-input-number v-model="form.highWaterHomeUpsetCount" :min="0" class="!w-full" />
              </el-form-item>
              <el-form-item label="高水客场翻车场数" prop="highWaterAwayUpsetCount">
                <el-input-number v-model="form.highWaterAwayUpsetCount" :min="0" class="!w-full" />
              </el-form-item>
            </div>
          </div>

          <!-- 中水盘口属性区域 (Amber) -->
          <div class="p-4 rounded-2xl bg-amber-950/20 border border-amber-500/30 space-y-3">
            <div class="flex items-center justify-between">
              <span class="text-xs font-bold text-amber-300 flex items-center gap-1.5">
                <span>🟡</span> 中水盘口统计
              </span>
              <span class="text-[10px] text-amber-400 font-mono">
                当前中水翻车率: <strong>{{ livePreview.midWaterUpsetRate }}%</strong>
              </span>
            </div>
            <div class="grid grid-cols-1 sm:grid-cols-3 gap-3">
              <el-form-item label="中水场数" prop="midWaterMatches">
                <el-input-number v-model="form.midWaterMatches" :min="0" class="!w-full" />
              </el-form-item>
              <el-form-item label="中水主场翻车场数" prop="midWaterHomeUpsetCount">
                <el-input-number v-model="form.midWaterHomeUpsetCount" :min="0" class="!w-full" />
              </el-form-item>
              <el-form-item label="中水客场翻车场数" prop="midWaterAwayUpsetCount">
                <el-input-number v-model="form.midWaterAwayUpsetCount" :min="0" class="!w-full" />
              </el-form-item>
            </div>
          </div>

          <!-- 低水盘口属性区域 (Cyan) -->
          <div class="p-4 rounded-2xl bg-cyan-950/20 border border-cyan-500/30 space-y-3">
            <div class="flex items-center justify-between">
              <span class="text-xs font-bold text-cyan-300 flex items-center gap-1.5">
                <span>🟢</span> 低水盘口统计 (防大热必死)
              </span>
              <span class="text-[10px] text-cyan-400 font-mono">
                当前低水翻车率: <strong>{{ livePreview.lowWaterUpsetRate }}%</strong>
              </span>
            </div>
            <div class="grid grid-cols-1 sm:grid-cols-3 gap-3">
              <el-form-item label="低水场数" prop="lowWaterMatches">
                <el-input-number v-model="form.lowWaterMatches" :min="0" class="!w-full" />
              </el-form-item>
              <el-form-item label="低水主场翻车场数" prop="lowWaterHomeUpsetCount">
                <el-input-number v-model="form.lowWaterHomeUpsetCount" :min="0" class="!w-full" />
              </el-form-item>
              <el-form-item label="低水客场翻车场数" prop="lowWaterAwayUpsetCount">
                <el-input-number v-model="form.lowWaterAwayUpsetCount" :min="0" class="!w-full" />
              </el-form-item>
            </div>
          </div>

          <!-- 复盘笔记 -->
          <el-form-item label="复盘笔记与策略洞察">
            <el-input
              v-model="form.remarks"
              type="textarea"
              :rows="3"
              placeholder="记录本轮水位操盘特征，如高水主场诱盘、低水受热打出/翻车等"
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
            class="px-5 py-2 rounded-xl text-xs font-bold bg-gradient-to-r from-cyan-400 to-blue-500 text-slate-950 hover:from-cyan-300 hover:to-blue-400 shadow-md shadow-cyan-500/20 transition flex items-center gap-1.5"
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
const showWaterRuleGuide = ref(true)
const showModalRuleGuide = ref(false)

const dateRange = ref([])
const summary = ref({})
const recordList = ref([])

// ECharts 引用与实例
const waterRatesChartRef = ref(null)
const upsetDistChartRef = ref(null)
let waterRatesChart = null
let upsetDistChart = null

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
  batchTitle: '',
  totalMatches: null,
  highWaterMatches: 0,
  highWaterHomeUpsetCount: 0,
  highWaterAwayUpsetCount: 0,
  midWaterMatches: 0,
  midWaterHomeUpsetCount: 0,
  midWaterAwayUpsetCount: 0,
  lowWaterMatches: 0,
  lowWaterHomeUpsetCount: 0,
  lowWaterAwayUpsetCount: 0,
  remarks: ''
})

const formRules = {
  recordDate: [{ required: true, message: '请选择记录日期', trigger: 'change' }],
  highWaterMatches: [{ required: true, message: '请输入高水场数', trigger: 'blur' }],
  highWaterHomeUpsetCount: [{ required: true, message: '请输入高水主场翻车场数', trigger: 'blur' }],
  highWaterAwayUpsetCount: [{ required: true, message: '请输入高水客场翻车场数', trigger: 'blur' }],
  midWaterMatches: [{ required: true, message: '请输入中水场数', trigger: 'blur' }],
  midWaterHomeUpsetCount: [{ required: true, message: '请输入中水主场翻车场数', trigger: 'blur' }],
  midWaterAwayUpsetCount: [{ required: true, message: '请输入中水客场翻车场数', trigger: 'blur' }],
  lowWaterMatches: [{ required: true, message: '请输入低水场数', trigger: 'blur' }],
  lowWaterHomeUpsetCount: [{ required: true, message: '请输入低水主场翻车场数', trigger: 'blur' }],
  lowWaterAwayUpsetCount: [{ required: true, message: '请输入低水客场翻车场数', trigger: 'blur' }]
}

// 弹窗实时动态联动计算 (Live Preview)
const livePreview = computed(() => {
  const hwM = Number(form.highWaterMatches) || 0
  const hwH = Number(form.highWaterHomeUpsetCount) || 0
  const hwA = Number(form.highWaterAwayUpsetCount) || 0
  const hwUpset = hwH + hwA

  const mwM = Number(form.midWaterMatches) || 0
  const mwH = Number(form.midWaterHomeUpsetCount) || 0
  const mwA = Number(form.midWaterAwayUpsetCount) || 0
  const mwUpset = mwH + mwA

  const lwM = Number(form.lowWaterMatches) || 0
  const lwH = Number(form.lowWaterHomeUpsetCount) || 0
  const lwA = Number(form.lowWaterAwayUpsetCount) || 0
  const lwUpset = lwH + lwA

  const sumWaterMatches = hwM + mwM + lwM
  const enteredTotal = Number(form.totalMatches) || 0
  const totalMatches = enteredTotal > 0 ? Math.max(enteredTotal, sumWaterMatches) : sumWaterMatches

  const totalUpsetCount = hwUpset + mwUpset + lwUpset
  const totalNormalCount = Math.max(0, totalMatches - totalUpsetCount)

  const overallUpsetRate = totalMatches > 0 ? ((totalUpsetCount / totalMatches) * 100).toFixed(2) : '0.00'

  const highWaterUpsetRate = hwM > 0 ? ((hwUpset / hwM) * 100).toFixed(2) : '0.00'
  const highHomeRate = hwM > 0 ? ((hwH / hwM) * 100).toFixed(2) : '0.00'
  const highAwayRate = hwM > 0 ? ((hwA / hwM) * 100).toFixed(2) : '0.00'

  const midWaterUpsetRate = mwM > 0 ? ((mwUpset / mwM) * 100).toFixed(2) : '0.00'
  const midHomeRate = mwM > 0 ? ((mwH / mwM) * 100).toFixed(2) : '0.00'
  const midAwayRate = mwM > 0 ? ((mwA / mwM) * 100).toFixed(2) : '0.00'

  const lowWaterUpsetRate = lwM > 0 ? ((lwUpset / lwM) * 100).toFixed(2) : '0.00'
  const lowHomeRate = lwM > 0 ? ((lwH / lwM) * 100).toFixed(2) : '0.00'
  const lowAwayRate = lwM > 0 ? ((lwA / lwM) * 100).toFixed(2) : '0.00'

  return {
    totalMatches,
    totalUpsetCount,
    totalNormalCount,
    overallUpsetRate,
    highWaterUpsetRate,
    highHomeRate,
    highAwayRate,
    midWaterUpsetRate,
    midHomeRate,
    midAwayRate,
    lowWaterUpsetRate,
    lowHomeRate,
    lowAwayRate
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
      api.getOddsWaterRecordSummary(params),
      api.getOddsWaterRecords(params)
    ])

    summary.value = summaryRes || {}
    recordList.value = listRes || []

    await nextTick()
    renderCharts()
  } catch (err) {
    console.error('获取盘口水位翻车数据失败:', err)
  } finally {
    loading.value = false
  }
}

const handleDateRangeChange = () => {
  fetchAllData()
}

// --- 图表渲染 ---
const renderCharts = () => {
  if (waterRatesChartRef.value) {
    if (!waterRatesChart) {
      waterRatesChart = echarts.init(waterRatesChartRef.value)
    }

    const hwRate = Number(summary.value.avgHighWaterUpsetRate) || 0
    const mwRate = Number(summary.value.avgMidWaterUpsetRate) || 0
    const lwRate = Number(summary.value.avgLowWaterUpsetRate) || 0

    const hwHomeRate = Number(summary.value.avgHighWaterHomeUpsetRate) || 0
    const hwAwayRate = Number(summary.value.avgHighWaterAwayUpsetRate) || 0
    const mwHomeRate = Number(summary.value.avgMidWaterHomeUpsetRate) || 0
    const mwAwayRate = Number(summary.value.avgMidWaterAwayUpsetRate) || 0
    const lwHomeRate = Number(summary.value.avgLowWaterHomeUpsetRate) || 0
    const lwAwayRate = Number(summary.value.avgLowWaterAwayUpsetRate) || 0

    const option = {
      backgroundColor: 'transparent',
      tooltip: {
        trigger: 'axis',
        axisPointer: { type: 'shadow' },
        formatter: (params) => {
          let tip = `<div class="font-bold mb-1">${params[0].axisValue}</div>`
          params.forEach((item) => {
            tip += `<div class="flex items-center justify-between gap-4 text-xs">
              <span style="color:${item.color}">${item.seriesName}:</span>
              <span class="font-bold font-mono">${item.value}%</span>
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
        data: ['高水盘口 (High)', '中水盘口 (Mid)', '低水盘口 (Low)'],
        axisLine: { lineStyle: { color: '#334155' } },
        axisLabel: { color: '#94a3b8', fontSize: 12, fontWeight: 'bold' }
      },
      yAxis: {
        type: 'value',
        axisLabel: { formatter: '{value}%', color: '#64748b' },
        splitLine: { lineStyle: { color: 'rgba(51, 65, 85, 0.4)' } }
      },
      series: [
        {
          name: '综合翻车率',
          type: 'bar',
          data: [hwRate, mwRate, lwRate],
          barWidth: 26,
          itemStyle: {
            borderRadius: [6, 6, 0, 0],
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#f43f5e' },
              { offset: 1, color: '#be123c' }
            ])
          },
          label: {
            show: true,
            position: 'top',
            formatter: '{c}%',
            color: '#cbd5e1',
            fontSize: 10
          }
        },
        {
          name: '主场翻车率',
          type: 'bar',
          data: [hwHomeRate, mwHomeRate, lwHomeRate],
          barWidth: 18,
          itemStyle: {
            borderRadius: [4, 4, 0, 0],
            color: '#3b82f6'
          }
        },
        {
          name: '客场翻车率',
          type: 'bar',
          data: [hwAwayRate, mwAwayRate, lwAwayRate],
          barWidth: 18,
          itemStyle: {
            borderRadius: [4, 4, 0, 0],
            color: '#a855f7'
          }
        }
      ]
    }
    waterRatesChart.setOption(option)
  }

  if (upsetDistChartRef.value) {
    if (!upsetDistChart) {
      upsetDistChart = echarts.init(upsetDistChartRef.value)
    }

    const homeUpset = Number(summary.value.totalHomeUpsetCount) || 0
    const awayUpset = Number(summary.value.totalAwayUpsetCount) || 0

    const option2 = {
      backgroundColor: 'transparent',
      tooltip: {
        trigger: 'item',
        formatter: '{b}: {c} 场 ({d}%)'
      },
      legend: {
        bottom: 0,
        textStyle: { color: '#94a3b8', fontSize: 11 }
      },
      series: [
        {
          name: '翻车场地分布',
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
              value: homeUpset,
              name: '主场翻车',
              itemStyle: { color: '#3b82f6' }
            },
            {
              value: awayUpset,
              name: '客场翻车',
              itemStyle: { color: '#a855f7' }
            }
          ]
        }
      ]
    }
    upsetDistChart.setOption(option2)
  }
}

// 响应窗口尺寸变化
const handleResize = () => {
  waterRatesChart?.resize()
  upsetDistChart?.resize()
}

// --- 弹窗逻辑 ---
const handleOpenCreateDialog = () => {
  isEditing.value = false
  const today = new Date().toISOString().split('T')[0]
  form.id = null
  form.recordDate = today
  form.batchTitle = ''
  form.totalMatches = null
  form.highWaterMatches = 0
  form.highWaterHomeUpsetCount = 0
  form.highWaterAwayUpsetCount = 0
  form.midWaterMatches = 0
  form.midWaterHomeUpsetCount = 0
  form.midWaterAwayUpsetCount = 0
  form.lowWaterMatches = 0
  form.lowWaterHomeUpsetCount = 0
  form.lowWaterAwayUpsetCount = 0
  form.remarks = ''
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEditing.value = true
  form.id = row.id
  form.recordDate = row.recordDate
  form.batchTitle = row.batchTitle || ''
  form.totalMatches = row.totalMatches
  form.highWaterMatches = row.highWaterMatches
  form.highWaterHomeUpsetCount = row.highWaterHomeUpsetCount
  form.highWaterAwayUpsetCount = row.highWaterAwayUpsetCount
  form.midWaterMatches = row.midWaterMatches
  form.midWaterHomeUpsetCount = row.midWaterHomeUpsetCount
  form.midWaterAwayUpsetCount = row.midWaterAwayUpsetCount
  form.lowWaterMatches = row.lowWaterMatches
  form.lowWaterHomeUpsetCount = row.lowWaterHomeUpsetCount
  form.lowWaterAwayUpsetCount = row.lowWaterAwayUpsetCount
  form.remarks = row.remarks || ''
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm(
    `确定要删除 ${row.recordDate} 的水位翻车战绩记录吗？`,
    '删除确认',
    {
      confirmButtonText: '确定删除',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await api.deleteOddsWaterRecord(row.id)
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
        await api.updateOddsWaterRecord(form.id, form)
        ElMessage.success('水位战绩更新成功')
      } else {
        await api.createOddsWaterRecord(form)
        ElMessage.success('水位战绩录入成功')
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
  waterRatesChart?.dispose()
  upsetDistChart?.dispose()
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
