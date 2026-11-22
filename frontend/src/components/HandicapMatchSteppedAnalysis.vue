<template>
  <div class="space-y-6 sm:space-y-8">
    <!-- 1. 前端操控多维控制台 (步幅 + 主客让 + 赛制 + 时间四大维度正交叠加) -->
    <div class="rounded-3xl bg-slate-900/90 border border-slate-800 p-4 sm:p-6 shadow-2xl relative overflow-hidden backdrop-blur-xl space-y-4">
      <div class="absolute -right-16 -top-16 w-64 h-64 bg-amber-500/10 rounded-full blur-3xl pointer-events-none"></div>

      <!-- 第一行：步幅操控器与操作按钮组 -->
      <div class="flex flex-col lg:flex-row lg:items-center lg:justify-between gap-4 relative z-10 pb-3 border-b border-slate-800/80">
        <!-- 核心步幅选择 -->
        <div class="space-y-1.5">
          <div class="flex items-center gap-2">
            <span class="text-xs font-extrabold uppercase tracking-widest text-amber-400 bg-amber-500/15 px-2.5 py-0.5 rounded-full border border-amber-500/20">
              ⚡ 步幅操控引擎
            </span>
            <span class="text-xs text-slate-400">点击步幅实时重新分桶计算</span>
          </div>

          <div class="flex items-center gap-2 flex-wrap">
            <span class="text-xs font-semibold text-slate-300">赔率步幅:</span>
            <div class="inline-flex p-1 rounded-2xl bg-slate-950/90 border border-slate-800">
              <button
                v-for="step in stepOptions"
                :key="step.value"
                @click="changeStep(step.value)"
                class="px-3.5 py-1.5 rounded-xl text-xs font-extrabold transition-all duration-200 flex items-center gap-1.5"
                :class="[
                  currentStep === step.value
                    ? 'bg-gradient-to-r from-amber-500 to-orange-500 text-slate-950 shadow-md shadow-orange-500/25 scale-105'
                    : 'text-slate-400 hover:text-white hover:bg-slate-850'
                ]"
              >
                <span>{{ step.icon }}</span>
                <span>{{ step.label }}</span>
                <span class="text-[10px] opacity-75 font-mono">({{ step.desc }})</span>
              </button>
            </div>
          </div>
        </div>

        <!-- 右侧快速操作按钮组 -->
        <div class="flex items-center gap-2.5 flex-wrap">
          <button
            @click="fetchMatchList"
            class="px-3 py-2 rounded-xl text-xs font-semibold bg-slate-800/80 hover:bg-slate-700 text-slate-200 transition flex items-center gap-1.5"
            title="刷新数据"
          >
            <span>🔄</span>
            <span>刷新</span>
          </button>

          <button
            @click="openSimulateModal"
            class="px-3.5 py-2 rounded-xl text-xs font-extrabold bg-gradient-to-r from-cyan-500 via-sky-500 to-blue-600 hover:from-cyan-400 hover:to-blue-500 text-slate-950 shadow-lg shadow-cyan-500/25 transition flex items-center gap-1.5 border border-cyan-400/30"
          >
            <span>🧮</span>
            <span>单场测算 (ROI)</span>
          </button>

          <button
            @click="openBatchModal"
            class="px-3 py-2 rounded-xl text-xs font-semibold bg-indigo-600/80 hover:bg-indigo-500 text-white shadow-sm shadow-indigo-600/25 transition flex items-center gap-1.5"
          >
            <span>📑</span>
            <span>批量录入</span>
          </button>

          <button
            @click="openCreateModal"
            class="px-3.5 py-2 rounded-xl text-xs font-bold bg-gradient-to-r from-emerald-500 to-teal-500 hover:from-emerald-400 hover:to-teal-400 text-slate-950 shadow-lg shadow-emerald-500/25 transition flex items-center gap-1.5"
          >
            <span>➕</span>
            <span>录入单场比赛</span>
          </button>
        </div>
      </div>

      <!-- 第二行：主客场让球 + 赛制类型 + 时间跨度（最近1个月/最近3个月/全部）叠加筛选条 -->
      <div class="flex flex-wrap items-center justify-between gap-3 text-xs pt-1">
        <!-- 维度 1: 主客场让球 -->
        <div class="flex items-center gap-1.5 flex-wrap">
          <span class="text-slate-400 font-semibold">让球方:</span>
          <div class="inline-flex p-0.5 rounded-xl bg-slate-950/80 border border-slate-800">
            <button
              v-for="opt in sideOptions"
              :key="opt.value"
              @click="sideFilter = opt.value"
              class="px-2.5 py-1 rounded-lg transition text-xs"
              :class="[
                sideFilter === opt.value
                  ? 'bg-amber-500/20 text-amber-300 font-bold border border-amber-500/30'
                  : 'text-slate-400 hover:text-white'
              ]"
            >
              {{ opt.label }}
            </button>
          </div>
        </div>

        <!-- 维度 2: 赛制类型叠加 (联赛 / 杯赛淘汰赛 / 友谊赛) -->
        <div class="flex items-center gap-1.5 flex-wrap">
          <span class="text-slate-400 font-semibold">赛制:</span>
          <div class="inline-flex p-0.5 rounded-xl bg-slate-950/80 border border-slate-800">
            <button
              v-for="sys in systemOptions"
              :key="sys.value"
              @click="systemFilter = sys.value"
              class="px-2.5 py-1 rounded-lg transition text-xs flex items-center gap-1"
              :class="[
                systemFilter === sys.value
                  ? 'bg-teal-500/20 text-teal-300 font-bold border border-teal-500/30'
                  : 'text-slate-400 hover:text-white'
              ]"
            >
              <span>{{ sys.icon }}</span>
              <span>{{ sys.label }}</span>
            </button>
          </div>
        </div>

        <!-- 维度 3: 时间范围快捷预设 (全部 / 最近一个月 / 最近3个月) + 自定义日历 -->
        <div class="flex items-center gap-1.5 flex-wrap">
          <span class="text-slate-400 font-semibold">时间:</span>
          <div class="inline-flex p-0.5 rounded-xl bg-slate-950/80 border border-slate-800">
            <button
              v-for="t in timePresets"
              :key="t.value"
              @click="setTimePreset(t.value)"
              class="px-2.5 py-1 rounded-lg transition text-xs font-semibold"
              :class="[
                timePreset === t.value
                  ? 'bg-sky-500/20 text-sky-300 font-bold border border-sky-500/30'
                  : 'text-slate-400 hover:text-white'
              ]"
            >
              {{ t.label }}
            </button>
          </div>

          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始"
            end-placeholder="结束"
            value-format="YYYY-MM-DD"
            size="small"
            class="custom-datepicker !w-52"
            @change="onManualDateChange"
          />
        </div>
      </div>
    </div>

    <!-- 2. 当前步幅动态汇总 KPI 指标卡片 (随4大维度联动) -->
    <div class="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-6 gap-3 sm:gap-4">
      <div class="rounded-2xl p-4 bg-slate-900/80 border border-slate-800 shadow-md">
        <div class="text-[11px] font-semibold text-slate-400 flex items-center justify-between">
          <span>当前有效区间</span>
          <span class="text-amber-400">📊</span>
        </div>
        <div class="mt-2 text-2xl font-black text-amber-400">
          {{ steppedBuckets.length }} <span class="text-xs font-normal text-slate-400">个区间</span>
        </div>
        <div class="mt-1 text-[10px] text-slate-500 font-mono">步幅 Step: {{ currentStep }}</div>
      </div>

      <div class="rounded-2xl p-4 bg-slate-900/80 border border-slate-800 shadow-md">
        <div class="text-[11px] font-semibold text-slate-400 flex items-center justify-between">
          <span>筛选样本场次</span>
          <span class="text-emerald-400">⚽</span>
        </div>
        <div class="mt-2 text-2xl font-black text-white">
          {{ filteredMatches.length }} <span class="text-xs font-normal text-slate-400">场</span>
        </div>
        <div class="mt-1 text-[10px] text-slate-400">
          胜 {{ totalStats.win }} · 平 {{ totalStats.draw }} · 负 {{ totalStats.loss }}
        </div>
      </div>

      <div class="rounded-2xl p-4 bg-slate-900/80 border border-slate-800 shadow-md">
        <div class="text-[11px] font-semibold text-slate-400 flex items-center justify-between">
          <span>让球方胜率</span>
          <span class="text-sky-400">📈</span>
        </div>
        <div class="mt-2 text-2xl font-black text-sky-400">
          {{ totalStats.winRate }}<span class="text-sm font-semibold">%</span>
        </div>
        <div class="mt-1 text-[10px] text-slate-400">
          正路赢球 {{ totalStats.win }} 场
        </div>
      </div>

      <div class="rounded-2xl p-4 bg-slate-900/80 border border-slate-800 shadow-md">
        <div class="text-[11px] font-semibold text-slate-400 flex items-center justify-between">
          <span>全盘穿盘率</span>
          <span class="text-orange-400">🎯</span>
        </div>
        <div class="mt-2 text-2xl font-black text-orange-400">
          {{ totalStats.overallBreakRate }}<span class="text-sm font-semibold">%</span>
        </div>
        <div class="mt-1 text-[10px] text-slate-400">
          打穿让球 {{ totalStats.breakCount }} 场
        </div>
      </div>

      <div class="rounded-2xl p-4 bg-slate-900/80 border border-slate-800 shadow-md">
        <div class="text-[11px] font-semibold text-slate-400 flex items-center justify-between">
          <span>赢球穿盘纯度</span>
          <span class="text-purple-400">💎</span>
        </div>
        <div class="mt-2 text-2xl font-black text-purple-400">
          {{ totalStats.breakRateOnWin }}<span class="text-sm font-semibold">%</span>
        </div>
        <div class="mt-1 text-[10px] text-slate-400">
          赢球中打穿占 {{ totalStats.breakCount }}/{{ totalStats.win }}
        </div>
      </div>

      <div class="rounded-2xl p-4 bg-slate-900/80 border border-slate-800 shadow-md">
        <div class="text-[11px] font-semibold text-slate-400 flex items-center justify-between">
          <span>当前最高穿盘区间</span>
          <span class="text-amber-300">👑</span>
        </div>
        <div class="mt-2 text-base font-extrabold text-amber-300 truncate" :title="totalStats.bestBreakInterval">
          {{ totalStats.bestBreakInterval }}
        </div>
        <div class="mt-1 text-[10px] text-slate-400">最高穿盘胜率领跑</div>
      </div>
    </div>

    <!-- 3. ECharts 动态双维度可视化大图表 -->
    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
      <!-- 动态步幅区间胜平负分布堆叠柱状图 + 穿盘率折线双Y轴 (占2列) -->
      <div class="lg:col-span-2 rounded-3xl bg-slate-900/85 border border-slate-800 p-5 shadow-xl flex flex-col justify-between">
        <div class="flex items-center justify-between flex-wrap gap-2 mb-3">
          <div>
            <h3 class="text-base font-extrabold text-white flex items-center gap-2">
              <span>📈 动态步幅区间胜平负分布与穿盘率走势</span>
              <span class="text-[10px] px-2 py-0.5 rounded bg-amber-500/10 text-amber-400 border border-amber-500/20 font-mono">
                步长: {{ currentStep }}
              </span>
            </h3>
            <p class="text-xs text-slate-400 mt-0.5">柱状图展示胜/平/负场次堆叠，折线双Y轴展示综合穿盘率与赢球打穿纯度</p>
          </div>
          <div class="flex items-center gap-3 text-xs">
            <span class="flex items-center gap-1.5"><span class="w-3 h-3 rounded bg-emerald-500"></span>让球方胜</span>
            <span class="flex items-center gap-1.5"><span class="w-3 h-3 rounded bg-amber-400"></span>平局</span>
            <span class="flex items-center gap-1.5"><span class="w-3 h-3 rounded bg-rose-500"></span>负场</span>
            <span class="flex items-center gap-1.5"><span class="w-2.5 h-0.5 bg-orange-400"></span>穿盘率(%)</span>
          </div>
        </div>

        <div ref="steppedChartRef" class="w-full h-80 sm:h-96"></div>
      </div>

      <!-- 动态区间总进球数结构雷达/柱状图 (占1列) -->
      <div class="rounded-3xl bg-slate-900/85 border border-slate-800 p-5 shadow-xl flex flex-col justify-between">
        <div class="mb-3">
          <h3 class="text-base font-extrabold text-white flex items-center gap-2">
            <span>🥅 进球数与半全场热力图</span>
          </h3>
          <p class="text-xs text-slate-400 mt-0.5">洞察不同让球赔率下的总进球数分布格局</p>
        </div>

        <div ref="goalsChartRef" class="w-full h-80 sm:h-96"></div>
      </div>
    </div>

    <!-- 4. 动态步幅区间明细数据表 (前端操控实时计算) -->
    <div class="rounded-3xl bg-slate-900/90 border border-slate-800 p-5 sm:p-6 shadow-2xl space-y-4">
      <div class="flex items-center justify-between flex-wrap gap-3">
        <div>
          <h2 class="text-lg font-extrabold text-white flex items-center gap-2">
            <span>📊 步幅为 {{ currentStep }} 的动态赔率区间聚合统计表</span>
          </h2>
          <p class="text-xs text-slate-400 mt-1">
            实时按每 {{ currentStep }} 划分胜平负分布、穿盘率、赛果平均打出赔、让球赔均值及最常见比分特征
          </p>
        </div>

        <div class="text-xs text-slate-400 flex items-center gap-3">
          <span>共 <strong class="text-amber-400">{{ steppedBuckets.length }}</strong> 个动态分桶</span>
          <span>样本场次: <strong class="text-emerald-400">{{ filteredMatches.length }}</strong></span>
        </div>
      </div>

      <!-- 表格内容 -->
      <div class="overflow-x-auto rounded-2xl border border-slate-800/80">
        <table class="w-full text-left text-xs text-slate-300">
          <thead class="bg-slate-950/90 text-slate-400 text-[11px] uppercase tracking-wider border-b border-slate-800 font-bold">
            <tr>
              <th class="py-3 px-3">赔率区间 (让球方)</th>
              <th class="py-3 px-3 text-center">样本场次</th>
              <th class="py-3 px-3 text-center">胜 / 平 / 负</th>
              <th class="py-3 px-3 text-center">胜率 (%)</th>
              <th class="py-3 px-3 text-center">穿盘场数</th>
              <th class="py-3 px-3 text-center">全盘穿盘率</th>
              <th class="py-3 px-3 text-center">赢球打穿纯度</th>
              <th class="py-3 px-3 text-center">平均打出赔</th>
              <th class="py-3 px-3 text-center">平均让球赔</th>
              <th class="py-3 px-3 text-center">常见总进球</th>
              <th class="py-3 px-3 text-center">高发半全场</th>
              <th class="py-3 px-3 text-center">研判标签</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-slate-850">
            <tr
              v-for="b in steppedBuckets"
              :key="b.key"
              class="hover:bg-slate-800/50 transition cursor-pointer"
              @click="selectBucket(b)"
            >
              <!-- 赔率区间 -->
              <td class="py-3 px-3 font-extrabold text-amber-300 font-mono text-sm">
                {{ b.label }}
              </td>

              <!-- 样本场次 -->
              <td class="py-3 px-3 text-center font-bold text-white">
                {{ b.totalCount }}
              </td>

              <!-- 胜平负 -->
              <td class="py-3 px-3 text-center font-mono">
                <span class="text-emerald-400 font-bold">{{ b.winCount }}</span>
                <span class="text-slate-600"> / </span>
                <span class="text-amber-400 font-bold">{{ b.drawCount }}</span>
                <span class="text-slate-600"> / </span>
                <span class="text-rose-400 font-bold">{{ b.lossCount }}</span>
              </td>

              <!-- 胜率 -->
              <td class="py-3 px-3 text-center">
                <span
                  class="px-2 py-0.5 rounded font-extrabold text-[11px]"
                  :class="[
                    b.winRate >= 70 ? 'bg-emerald-500/20 text-emerald-400 border border-emerald-500/30' :
                    b.winRate >= 50 ? 'bg-amber-500/20 text-amber-300 border border-amber-500/30' :
                    'bg-rose-500/20 text-rose-400 border border-rose-500/30'
                  ]"
                >
                  {{ b.winRate }}%
                </span>
              </td>

              <!-- 穿盘场数 -->
              <td class="py-3 px-3 text-center font-bold text-orange-400">
                {{ b.breakCount }} 场
              </td>

              <!-- 全盘穿盘率 -->
              <td class="py-3 px-3 text-center">
                <span
                  class="font-black"
                  :class="b.overallBreakRate >= 50 ? 'text-orange-400' : 'text-slate-400'"
                >
                  {{ b.overallBreakRate }}%
                </span>
              </td>

              <!-- 赢球打穿纯度 -->
              <td class="py-3 px-3 text-center font-bold text-purple-400">
                {{ b.breakRateOnWin }}%
              </td>

              <!-- 平均打出赔 -->
              <td class="py-3 px-3 text-center font-mono text-slate-300">
                {{ b.avgResultOdds > 0 ? b.avgResultOdds.toFixed(2) : '-' }}
              </td>

              <!-- 平均让球赔 -->
              <td class="py-3 px-3 text-center font-mono text-slate-300">
                {{ b.avgHandicapOdds > 0 ? b.avgHandicapOdds.toFixed(2) : '-' }}
              </td>

              <!-- 常见总进球 (显示前两个常见进球数及占比) -->
              <td class="py-3 px-3 text-center">
                <div v-if="b.topCommonGoals && b.topCommonGoals.length > 0" class="flex items-center justify-center gap-1.5 flex-wrap">
                  <span
                    v-for="(item, idx) in b.topCommonGoals"
                    :key="idx"
                    class="px-2 py-0.5 rounded text-[11px] inline-flex items-center gap-1 whitespace-nowrap"
                    :class="[
                      idx === 0
                        ? 'bg-sky-500/20 text-sky-300 border border-sky-500/30 font-semibold'
                        : 'bg-indigo-500/15 text-indigo-300 border border-indigo-500/25 font-normal'
                    ]"
                  >
                    <span>{{ item.goals }}球</span>
                    <span class="font-mono text-[10px] opacity-90">({{ item.rate }}%)</span>
                  </span>
                </div>
                <span v-else class="text-slate-500">-</span>
              </td>

              <!-- 高发半全场 -->
              <td class="py-3 px-3 text-center">
                <span class="px-2 py-0.5 rounded bg-slate-800 text-teal-300 text-[11px] font-medium">
                  {{ b.mostCommonHalfFull }}
                </span>
              </td>

              <!-- 研判标签 -->
              <td class="py-3 px-3 text-center">
                <span
                  v-if="b.overallBreakRate >= 60"
                  class="px-2 py-0.5 rounded bg-orange-500/20 text-orange-400 border border-orange-500/30 text-[10px] font-bold"
                >
                  🔥 强力穿盘
                </span>
                <span
                  v-else-if="b.winRate >= 70 && b.overallBreakRate < 40"
                  class="px-2 py-0.5 rounded bg-amber-500/20 text-amber-300 border border-amber-500/30 text-[10px] font-bold"
                >
                  ⚠️ 易卡盘小胜
                </span>
                <span
                  v-else-if="b.lossCount >= b.winCount"
                  class="px-2 py-0.5 rounded bg-rose-500/20 text-rose-400 border border-rose-500/30 text-[10px] font-bold"
                >
                  🚨 翻车高危
                </span>
                <span
                  v-else
                  class="px-2 py-0.5 rounded bg-slate-800 text-slate-400 text-[10px]"
                >
                  平稳走势
                </span>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- 5. 详细录入的单场比赛数据库全量列表 -->
    <div class="rounded-3xl bg-slate-900/90 border border-slate-800 p-5 sm:p-6 shadow-2xl space-y-4">
      <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3">
        <div>
          <h2 class="text-lg font-extrabold text-white flex items-center gap-2">
            <span>📑 单场比赛明细记录数据库</span>
            <span class="text-xs px-2 py-0.5 rounded-full bg-slate-800 text-slate-400 font-mono">
              当前筛选 {{ filteredMatches.length }} / 库总 {{ matches.length }} 场
            </span>
          </h2>
          <p class="text-xs text-slate-400 mt-0.5">
            详细记录赛制、主客胜赔、全场比分、赛果、结果赔、让球赔、半全场、总进球及穿盘状态
          </p>
        </div>

        <div class="flex items-center gap-2 flex-wrap">
          <input
            v-model="searchKeyword"
            type="text"
            placeholder="搜索球队、联赛或批次..."
            class="px-3 py-1.5 rounded-xl text-xs bg-slate-950 border border-slate-800 text-white placeholder-slate-500 focus:outline-none focus:border-amber-500 w-48 sm:w-60"
          />
          <button
            @click="openCreateModal"
            class="px-3 py-1.5 rounded-xl text-xs font-bold bg-emerald-600 hover:bg-emerald-500 text-white transition flex items-center gap-1"
          >
            <span>➕</span> 录入新比赛
          </button>
        </div>
      </div>

      <!-- 单场比赛表格 -->
      <div class="overflow-x-auto rounded-2xl border border-slate-800/80">
        <table class="w-full text-left text-xs text-slate-300">
          <thead class="bg-slate-950/90 text-slate-400 text-[11px] uppercase tracking-wider border-b border-slate-800 font-bold">
            <tr>
              <th class="py-3 px-3">日期 / 赛事</th>
              <th class="py-3 px-3 text-center">赛制</th>
              <th class="py-3 px-3">对阵双方</th>
              <th class="py-3 px-3 text-center">让球方 / 盘口</th>
              <th class="py-3 px-3 text-center">主胜 / 客胜赔</th>
              <th class="py-3 px-3 text-center">让方胜赔</th>
              <th class="py-3 px-3 text-center">比分(半/全)</th>
              <th class="py-3 px-3 text-center">赛果 & 结果赔</th>
              <th class="py-3 px-3 text-center">让球开奖 & 赔</th>
              <th class="py-3 px-3 text-center">半全场</th>
              <th class="py-3 px-3 text-center">总进球</th>
              <th class="py-3 px-3 text-center">是否穿盘</th>
              <th class="py-3 px-3 text-center">操作</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-slate-850">
            <tr
              v-for="m in pagedMatches"
              :key="m.id"
              class="hover:bg-slate-800/40 transition"
            >
              <!-- 日期 / 赛事 -->
              <td class="py-3 px-3">
                <div class="font-mono text-white font-semibold">{{ m.matchDate }}</div>
                <div class="text-[10px] text-slate-400 flex items-center gap-1">
                  <span class="px-1.5 py-0.2 rounded bg-slate-800 text-slate-300">{{ m.leagueName || '赛事' }}</span>
                  <span class="truncate max-w-[80px]" :title="m.batchTitle">{{ m.batchTitle }}</span>
                </div>
              </td>

              <!-- 赛制标签 -->
              <td class="py-3 px-3 text-center">
                <span
                  class="px-2 py-0.5 rounded text-[10px] font-bold"
                  :class="[
                    m.matchSystem === 'CUP' ? 'bg-purple-500/20 text-purple-300 border border-purple-500/30' :
                    m.matchSystem === 'FRIENDLY' ? 'bg-emerald-500/20 text-emerald-300 border border-emerald-500/30' :
                    'bg-sky-500/20 text-sky-300 border border-sky-500/30'
                  ]"
                >
                  {{ m.matchSystem === 'CUP' ? '🥊 杯赛' : m.matchSystem === 'FRIENDLY' ? '🤝 友谊' : '🏆 联赛' }}
                </span>
              </td>

              <!-- 对阵双方 -->
              <td class="py-3 px-3">
                <div class="font-bold flex items-center gap-1.5">
                  <span :class="m.handicapSide === 'HOME' ? 'text-amber-400 underline decoration-amber-500/40' : 'text-white'">
                    {{ m.homeTeam }}
                  </span>
                  <span class="text-slate-500 text-[10px]">vs</span>
                  <span :class="m.handicapSide === 'AWAY' ? 'text-amber-400 underline decoration-amber-500/40' : 'text-white'">
                    {{ m.awayTeam }}
                  </span>
                </div>
              </td>

              <!-- 让球方 / 盘口 -->
              <td class="py-3 px-3 text-center">
                <span
                  class="px-2 py-0.5 rounded text-[11px] font-bold"
                  :class="m.handicapSide === 'HOME' ? 'bg-amber-500/20 text-amber-300' : 'bg-sky-500/20 text-sky-300'"
                >
                  {{ m.handicapSide === 'HOME' ? '主让' : '客让' }} {{ m.handicapValue }}
                </span>
              </td>

              <!-- 主胜 / 客胜赔 -->
              <td class="py-3 px-3 text-center font-mono">
                <span :class="m.handicapSide === 'HOME' ? 'text-amber-400 font-bold' : 'text-slate-300'">
                  {{ m.homeWinOdds }}
                </span>
                <span class="text-slate-600"> / </span>
                <span :class="m.handicapSide === 'AWAY' ? 'text-amber-400 font-bold' : 'text-slate-300'">
                  {{ m.awayWinOdds }}
                </span>
              </td>

              <!-- 让方胜赔 (分桶依据) -->
              <td class="py-3 px-3 text-center font-mono font-black text-amber-400 text-sm">
                {{ m.handicapOdds }}
              </td>

              <!-- 比分 (半/全) -->
              <td class="py-3 px-3 text-center">
                <div class="font-black text-white font-mono text-sm tracking-wide">{{ m.fullScore || '-' }}</div>
                <div class="text-[10px] text-slate-400 font-mono">半场 {{ m.halfScore || '-' }}</div>
              </td>

              <!-- 赛果 & 结果赔 -->
              <td class="py-3 px-3 text-center">
                <div>
                  <span
                    class="px-2 py-0.5 rounded text-[10px] font-bold"
                    :class="[
                      m.matchResult === 'HOME_WIN' ? 'bg-emerald-500/20 text-emerald-400' :
                      m.matchResult === 'DRAW' ? 'bg-amber-500/20 text-amber-300' :
                      'bg-rose-500/20 text-rose-400'
                    ]"
                  >
                    {{ m.matchResult === 'HOME_WIN' ? '主胜' : m.matchResult === 'DRAW' ? '平局' : '客胜' }}
                  </span>
                </div>
                <div class="text-[10px] text-slate-400 font-mono mt-0.5">
                  赔: {{ m.resultOdds }}
                </div>
              </td>

              <!-- 让球开奖 & 赔 -->
              <td class="py-3 px-3 text-center">
                <span
                  class="px-2 py-0.5 rounded text-[10px] font-bold"
                  :class="[
                    m.handicapResult === 'WIN' ? 'bg-orange-500/20 text-orange-400' :
                    m.handicapResult === 'DRAW' ? 'bg-slate-700 text-slate-300' :
                    'bg-slate-800 text-slate-400'
                  ]"
                >
                  {{ m.handicapResult === 'WIN' ? '让胜' : m.handicapResult === 'DRAW' ? '走水' : '让负' }}
                </span>
                <div class="text-[10px] text-slate-400 font-mono mt-0.5">
                  {{ m.handicapResultOdds ? `赔: ${m.handicapResultOdds}` : '-' }}
                </div>
              </td>

              <!-- 半全场 -->
              <td class="py-3 px-3 text-center">
                <span class="px-2 py-0.5 rounded bg-slate-800 text-teal-300 font-semibold text-[11px]">
                  {{ m.halfFullResult || '-' }}
                </span>
              </td>

              <!-- 总进球 -->
              <td class="py-3 px-3 text-center font-bold text-sky-400 font-mono">
                {{ m.totalGoals != null ? `${m.totalGoals}球` : '-' }}
              </td>

              <!-- 是否穿盘 (亮眼 Badge) -->
              <td class="py-3 px-3 text-center">
                <span
                  v-if="m.isBreak"
                  class="px-2.5 py-1 rounded-full bg-gradient-to-r from-orange-500 to-amber-500 text-slate-950 font-black text-[11px] shadow-sm shadow-orange-500/30 inline-flex items-center gap-1"
                >
                  <span>🔥</span> 打穿
                </span>
                <span
                  v-else
                  class="px-2 py-0.5 rounded-full bg-slate-800 text-slate-500 text-[10px]"
                >
                  未穿盘
                </span>
              </td>

              <!-- 操作 -->
              <td class="py-3 px-3 text-center whitespace-nowrap">
                <div class="flex items-center justify-center gap-1.5">
                  <button
                    @click="openEditModal(m)"
                    class="p-1.5 rounded-lg bg-slate-800 hover:bg-slate-700 text-slate-300 hover:text-white transition"
                    title="编辑比赛"
                  >
                    ✏️
                  </button>
                  <button
                    @click="handleDeleteMatch(m)"
                    class="p-1.5 rounded-lg bg-slate-800 hover:bg-rose-950 text-slate-400 hover:text-rose-400 transition"
                    title="删除"
                  >
                    🗑️
                  </button>
                </div>
              </td>
            </tr>

            <tr v-if="filteredMatches.length === 0">
              <td colspan="13" class="py-8 text-center text-slate-500">
                暂无符合当前筛选（步幅/让球方/赛制/时间）的比赛明细记录，点击右上角【录入单场比赛】快速录入
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- 分页栏 -->
      <div class="flex items-center justify-between flex-wrap gap-2 pt-2 text-xs text-slate-400">
        <div>
          显示 {{ (currentPage - 1) * pageSize + 1 }} - {{ Math.min(currentPage * pageSize, filteredMatches.length) }} 条 / 共 {{ filteredMatches.length }} 条
        </div>
        <div class="flex items-center gap-2">
          <button
            :disabled="currentPage === 1"
            @click="currentPage--"
            class="px-3 py-1.5 rounded-lg bg-slate-800 hover:bg-slate-700 disabled:opacity-40 disabled:cursor-not-allowed text-white"
          >
            上一页
          </button>
          <span class="font-mono text-amber-400">{{ currentPage }} / {{ maxPage }}</span>
          <button
            :disabled="currentPage >= maxPage"
            @click="currentPage++"
            class="px-3 py-1.5 rounded-lg bg-slate-800 hover:bg-slate-700 disabled:opacity-40 disabled:cursor-not-allowed text-white"
          >
            下一页
          </button>
        </div>
      </div>
    </div>

    <!-- 6. 单场比赛智能录入 / 编辑弹窗 (包含赛制选择) -->
    <el-dialog
      v-model="matchModalVisible"
      :title="isEditMode ? '✏️ 编辑单场比赛开奖明细' : '➕ 录入单场比赛与开奖明细'"
      width="90%"
      class="custom-dark-dialog max-w-2xl"
      destroy-on-close
    >
      <div class="space-y-4 max-h-[75vh] overflow-y-auto pr-1">
        <!-- 比赛基本信息 -->
        <div class="p-3.5 rounded-2xl bg-slate-950/70 border border-slate-800 space-y-3">
          <div class="text-xs font-bold text-amber-300 flex items-center gap-1.5">
            <span>⚽ 比赛基础信息</span>
          </div>
          <div class="grid grid-cols-1 sm:grid-cols-4 gap-3">
            <div>
              <label class="block text-[11px] text-slate-400 mb-1">比赛日期 *</label>
              <el-date-picker
                v-model="form.matchDate"
                type="date"
                placeholder="选择日期"
                value-format="YYYY-MM-DD"
                class="!w-full custom-input"
              />
            </div>
            <div>
              <label class="block text-[11px] text-slate-400 mb-1">赛制类型 *</label>
              <el-select v-model="form.matchSystem" class="!w-full custom-select">
                <el-option label="🏆 联赛积分制" value="LEAGUE" />
                <el-option label="🥊 杯赛/淘汰赛" value="CUP" />
                <el-option label="🤝 友谊热身赛" value="FRIENDLY" />
                <el-option label="其他赛事" value="OTHER" />
              </el-select>
            </div>
            <div>
              <label class="block text-[11px] text-slate-400 mb-1">联赛名称</label>
              <el-input
                v-model="form.leagueName"
                placeholder="如: 英超, 欧冠"
                class="custom-input"
                @input="onLeagueNameInput"
              />
            </div>
            <div>
              <label class="block text-[11px] text-slate-400 mb-1">批次/组别</label>
              <el-input v-model="form.batchTitle" placeholder="如: 周六精选" class="custom-input" />
            </div>
          </div>

          <div class="grid grid-cols-1 sm:grid-cols-2 gap-3">
            <div>
              <label class="block text-[11px] text-slate-400 mb-1">主队名称 *</label>
              <el-input v-model="form.homeTeam" placeholder="如: 曼城" class="custom-input" />
            </div>
            <div>
              <label class="block text-[11px] text-slate-400 mb-1">客队名称 *</label>
              <el-input v-model="form.awayTeam" placeholder="如: 埃弗顿" class="custom-input" />
            </div>
          </div>
        </div>

        <!-- 赔率与让球设置 (胜赔分主客) -->
        <div class="p-3.5 rounded-2xl bg-slate-950/70 border border-slate-800 space-y-3">
          <div class="text-xs font-bold text-amber-300 flex items-center gap-1.5">
            <span>💰 胜赔 (分主客) 与让球设定</span>
          </div>

          <div class="grid grid-cols-1 sm:grid-cols-3 gap-3">
            <div>
              <label class="block text-[11px] text-slate-400 mb-1">主胜赔率 *</label>
              <el-input-number
                v-model="form.homeWinOdds"
                :precision="2"
                :step="0.01"
                :min="1.01"
                class="!w-full"
                @change="onOddsOrSideChange"
              />
            </div>
            <div>
              <label class="block text-[11px] text-slate-400 mb-1">平局赔率</label>
              <el-input-number
                v-model="form.drawOdds"
                :precision="2"
                :step="0.01"
                :min="1.01"
                class="!w-full"
                @change="onOddsOrSideChange"
              />
            </div>
            <div>
              <label class="block text-[11px] text-slate-400 mb-1">客胜赔率 *</label>
              <el-input-number
                v-model="form.awayWinOdds"
                :precision="2"
                :step="0.01"
                :min="1.01"
                class="!w-full"
                @change="onOddsOrSideChange"
              />
            </div>
          </div>

          <div class="grid grid-cols-1 sm:grid-cols-3 gap-3 items-center">
            <div>
              <label class="block text-[11px] text-slate-400 mb-1">让球方 *</label>
              <el-radio-group v-model="form.handicapSide" size="small" @change="onOddsOrSideChange">
                <el-radio-button label="HOME">主让球</el-radio-button>
                <el-radio-button label="AWAY">客让球</el-radio-button>
              </el-radio-group>
            </div>
            <div>
              <label class="block text-[11px] text-slate-400 mb-1">让球盘口数值</label>
              <el-input-number
                v-model="form.handicapValue"
                :precision="2"
                :step="0.25"
                class="!w-full"
                @change="triggerAutoDeduce"
              />
            </div>
            <div>
              <label class="block text-[11px] text-slate-400 mb-1">让球方胜赔 (分桶依据)</label>
              <el-input-number
                v-model="form.handicapOdds"
                :precision="2"
                :step="0.01"
                :min="1.01"
                class="!w-full"
              />
            </div>
          </div>
        </div>

        <!-- 最终开奖结果 (比分输入联动推算) -->
        <div class="p-3.5 rounded-2xl bg-amber-950/20 border border-amber-500/30 space-y-3">
          <div class="flex items-center justify-between">
            <span class="text-xs font-bold text-amber-300 flex items-center gap-1.5">
              <span>🎯 最终开奖结果 (输比分自动推断)</span>
            </span>
            <button
              type="button"
              @click="triggerAutoDeduce"
              class="text-[11px] text-amber-400 hover:text-amber-200 underline"
            >
              🔄 重新推导
            </button>
          </div>

          <div class="grid grid-cols-1 sm:grid-cols-2 gap-3">
            <div>
              <label class="block text-[11px] text-slate-400 mb-1">半场比分 (如 1:0)</label>
              <el-input
                v-model="form.halfScore"
                placeholder="1:0"
                class="custom-input font-mono"
                @input="triggerAutoDeduce"
              />
            </div>
            <div>
              <label class="block text-[11px] text-slate-400 mb-1">全场比分 (如 2:0) *</label>
              <el-input
                v-model="form.fullScore"
                placeholder="2:0"
                class="custom-input font-mono"
                @input="triggerAutoDeduce"
              />
            </div>
          </div>

          <!-- 自动推导智能状态提示栏 -->
          <div v-if="deducedHint" class="p-2.5 rounded-xl bg-amber-500/10 border border-amber-500/20 text-xs text-amber-300 flex items-center gap-2">
            <span>✨</span>
            <span>{{ deducedHint }}</span>
          </div>

          <div class="grid grid-cols-2 sm:grid-cols-4 gap-3">
            <div>
              <label class="block text-[11px] text-slate-400 mb-1">赛果</label>
              <el-select v-model="form.matchResult" class="!w-full custom-select">
                <el-option label="主胜 (HOME_WIN)" value="HOME_WIN" />
                <el-option label="平局 (DRAW)" value="DRAW" />
                <el-option label="客胜 (AWAY_WIN)" value="AWAY_WIN" />
              </el-select>
            </div>
            <div>
              <label class="block text-[11px] text-slate-400 mb-1">结果对应打出赔</label>
              <el-input-number v-model="form.resultOdds" :precision="2" :step="0.01" class="!w-full" />
            </div>
            <div>
              <label class="block text-[11px] text-slate-400 mb-1">让球赛果</label>
              <el-select v-model="form.handicapResult" class="!w-full custom-select">
                <el-option label="让胜 (WIN)" value="WIN" />
                <el-option label="让平/走水 (DRAW)" value="DRAW" />
                <el-option label="让负 (LOSS)" value="LOSS" />
              </el-select>
            </div>
            <div>
              <label class="block text-[11px] text-slate-400 mb-1">让球对应打出赔</label>
              <el-input-number v-model="form.handicapResultOdds" :precision="2" :step="0.01" class="!w-full" />
            </div>
          </div>

          <div class="grid grid-cols-1 sm:grid-cols-3 gap-3 items-center">
            <div>
              <label class="block text-[11px] text-slate-400 mb-1">半全场 (如 胜胜, 平胜)</label>
              <el-input v-model="form.halfFullResult" placeholder="胜胜" class="custom-input" />
            </div>
            <div>
              <label class="block text-[11px] text-slate-400 mb-1">全场总进球</label>
              <el-input-number v-model="form.totalGoals" :min="0" :max="20" class="!w-full" />
            </div>
            <div class="pt-4">
              <el-checkbox v-model="form.isBreak" label="🔥 是否穿盘 (打穿盘口)" size="large" />
            </div>
          </div>
        </div>

        <!-- 备注心得 -->
        <div>
          <label class="block text-[11px] text-slate-400 mb-1">复盘备忘心得</label>
          <el-input
            v-model="form.remarks"
            type="textarea"
            :rows="2"
            placeholder="记录盘口变动、临场降水或卡盘原因..."
          />
        </div>
      </div>

      <template #footer>
        <div class="flex items-center justify-end gap-2 pt-2">
          <button
            @click="matchModalVisible = false"
            class="px-4 py-2 rounded-xl text-xs font-semibold bg-slate-800 text-slate-300 hover:bg-slate-700 transition"
          >
            取消
          </button>
          <button
            @click="saveMatchRecord"
            class="px-5 py-2 rounded-xl text-xs font-extrabold bg-gradient-to-r from-emerald-500 to-teal-500 hover:from-emerald-400 hover:to-teal-400 text-slate-950 shadow-md shadow-emerald-500/20 transition"
          >
            {{ isEditMode ? '保存修改' : '确认录入' }}
          </button>
        </div>
      </template>
    </el-dialog>

    <!-- 7. 批量快速录入弹窗 -->
    <el-dialog
      v-model="batchModalVisible"
      title="📑 批量录入多场比赛"
      width="90%"
      class="custom-dark-dialog max-w-4xl"
      destroy-on-close
    >
      <div class="space-y-4 max-h-[70vh] overflow-y-auto pr-1">
        <p class="text-xs text-slate-400">
          逐行录入多场比赛，支持赛制设置及比分输入后自动推算赛果、半全场与穿盘状态，最后统一保存。
        </p>

        <div class="overflow-x-auto rounded-xl border border-slate-800">
          <table class="w-full text-left text-xs text-slate-300">
            <thead class="bg-slate-950 text-slate-400 font-bold border-b border-slate-800">
              <tr>
                <th class="p-2">日期</th>
                <th class="p-2">赛制</th>
                <th class="p-2">联赛</th>
                <th class="p-2">主队 vs 客队</th>
                <th class="p-2">主/客胜赔</th>
                <th class="p-2">让球方/盘口</th>
                <th class="p-2">半/全比分</th>
                <th class="p-2 text-center">穿盘</th>
                <th class="p-2 text-center">操作</th>
              </tr>
            </thead>
            <tbody class="divide-y divide-slate-850">
              <tr v-for="(item, idx) in batchList" :key="idx">
                <td class="p-2">
                  <el-date-picker v-model="item.matchDate" type="date" value-format="YYYY-MM-DD" size="small" class="!w-28" />
                </td>
                <td class="p-2">
                  <select v-model="item.matchSystem" class="px-1 py-1 rounded bg-slate-950 border border-slate-800 text-xs text-white">
                    <option value="LEAGUE">联赛</option>
                    <option value="CUP">杯赛</option>
                    <option value="FRIENDLY">友谊</option>
                  </select>
                </td>
                <td class="p-2">
                  <input v-model="item.leagueName" placeholder="英超" class="w-16 px-1.5 py-1 rounded bg-slate-950 border border-slate-800 text-white text-xs" />
                </td>
                <td class="p-2">
                  <div class="flex items-center gap-1">
                    <input v-model="item.homeTeam" placeholder="主队" class="w-20 px-1.5 py-1 rounded bg-slate-950 border border-slate-800 text-white text-xs" />
                    <span>-</span>
                    <input v-model="item.awayTeam" placeholder="客队" class="w-20 px-1.5 py-1 rounded bg-slate-950 border border-slate-800 text-white text-xs" />
                  </div>
                </td>
                <td class="p-2">
                  <div class="flex items-center gap-1 font-mono">
                    <input v-model.number="item.homeWinOdds" placeholder="主赔" class="w-14 px-1.5 py-1 rounded bg-slate-950 border border-slate-800 text-white text-xs" @change="batchItemDeduce(item)" />
                    <input v-model.number="item.awayWinOdds" placeholder="客赔" class="w-14 px-1.5 py-1 rounded bg-slate-950 border border-slate-800 text-white text-xs" @change="batchItemDeduce(item)" />
                  </div>
                </td>
                <td class="p-2">
                  <div class="flex items-center gap-1">
                    <select v-model="item.handicapSide" class="px-1 py-1 rounded bg-slate-950 border border-slate-800 text-xs text-white" @change="batchItemDeduce(item)">
                      <option value="HOME">主让</option>
                      <option value="AWAY">客让</option>
                    </select>
                    <input v-model.number="item.handicapValue" placeholder="-1" class="w-14 px-1.5 py-1 rounded bg-slate-950 border border-slate-800 text-white text-xs" @change="batchItemDeduce(item)" />
                  </div>
                </td>
                <td class="p-2">
                  <div class="flex items-center gap-1 font-mono">
                    <input v-model="item.halfScore" placeholder="1:0" class="w-14 px-1.5 py-1 rounded bg-slate-950 border border-slate-800 text-white text-xs" @input="batchItemDeduce(item)" />
                    <input v-model="item.fullScore" placeholder="2:0" class="w-14 px-1.5 py-1 rounded bg-slate-950 border border-slate-800 text-white text-xs font-bold" @input="batchItemDeduce(item)" />
                  </div>
                </td>
                <td class="p-2 text-center">
                  <input type="checkbox" v-model="item.isBreak" class="rounded text-amber-500" />
                </td>
                <td class="p-2 text-center">
                  <button @click="removeBatchRow(idx)" class="text-rose-400 hover:text-rose-300">✕</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <button
          @click="addBatchRow"
          class="px-3 py-1.5 rounded-lg bg-slate-800 hover:bg-slate-700 text-slate-200 text-xs font-semibold flex items-center gap-1"
        >
          <span>➕ 增加一行</span>
        </button>
      </div>

      <template #footer>
        <div class="flex items-center justify-end gap-2 pt-2">
          <button
            @click="batchModalVisible = false"
            class="px-4 py-2 rounded-xl text-xs font-semibold bg-slate-800 text-slate-300"
          >
            取消
          </button>
          <button
            @click="saveBatchRecords"
            class="px-5 py-2 rounded-xl text-xs font-extrabold bg-gradient-to-r from-indigo-500 to-purple-500 text-white shadow-md shadow-indigo-500/20"
          >
            确认批量保存 (共 {{ batchList.length }} 场)
          </button>
        </div>
      </template>
    </el-dialog>

    <!-- 4. 单场智能测算 (胜平负概率与期望ROI) 交互弹窗 -->
    <el-dialog
      v-model="simulateModalVisible"
      title="🧮 单场比赛开奖概率与期望 ROI 智能测算引擎"
      width="92%"
      class="custom-dark-dialog max-w-5xl"
      destroy-on-close
    >
      <div class="space-y-4 max-h-[80vh] overflow-y-auto pr-1 text-slate-200">
        <!-- 顶部引导与说明 -->
        <div class="p-3 rounded-2xl bg-gradient-to-r from-cyan-950/40 via-sky-950/30 to-blue-950/40 border border-cyan-800/40 flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3 text-xs">
          <div class="flex items-center gap-2">
            <span class="text-base">💡</span>
            <div>
              <span class="font-bold text-cyan-300">量化原理：</span>
              <span class="text-slate-300">根据输入的比赛赛制、让球方及胜平负赔率，自动检索历史同赔同赛制真实样本，通过打出概率与赔率推算单项期望回报率 (ROI = (胜率 × 赔率 - 1) × 100%)。</span>
            </div>
          </div>
          <div class="flex items-center gap-1.5 flex-shrink-0">
            <span class="px-2 py-0.5 rounded-full text-[11px] font-mono font-bold bg-cyan-500/20 text-cyan-300 border border-cyan-500/30">
              ROI > 0 : 正期望超额价值
            </span>
          </div>
        </div>

        <!-- 快速预设模板 Chip 选择 -->
        <div class="flex items-center gap-2 flex-wrap text-xs">
          <span class="text-slate-400 font-semibold flex items-center gap-1">
            <span>⚡ 快捷模板:</span>
          </span>
          <button
            v-for="(p, idx) in simPresets"
            :key="idx"
            @click="applySimPreset(p)"
            class="px-2.5 py-1 rounded-lg bg-slate-800/80 hover:bg-slate-700 text-slate-300 border border-slate-700 hover:border-cyan-500/50 transition text-[11px] flex items-center gap-1"
          >
            <span>{{ p.name }}</span>
          </button>
        </div>

        <div class="grid grid-cols-1 lg:grid-cols-12 gap-5">
          <!-- 左侧输入栏 (5 列) -->
          <div class="lg:col-span-5 space-y-3.5">
            <div class="p-4 rounded-2xl bg-slate-950/70 border border-slate-800 space-y-3.5">
              <div class="text-xs font-bold text-cyan-400 flex items-center justify-between">
                <span class="flex items-center gap-1.5">
                  <span>⚙️</span>
                  <span>输入比赛盘口与赔率</span>
                </span>
                <span class="text-[10px] text-slate-400 font-mono">实时联动测算</span>
              </div>

              <!-- 赛制选择 -->
              <div>
                <label class="block text-[11px] font-semibold text-slate-400 mb-1.5">赛制类型</label>
                <div class="grid grid-cols-4 gap-1.5 text-xs">
                  <button
                    v-for="sys in simSystemOptions"
                    :key="sys.value"
                    @click="simulateForm.matchSystem = sys.value; runSimulation()"
                    class="py-1.5 px-2 rounded-xl border text-center transition font-semibold text-[11px]"
                    :class="[
                      simulateForm.matchSystem === sys.value
                        ? 'bg-cyan-500/20 text-cyan-300 border-cyan-500/50 font-bold'
                        : 'bg-slate-900/80 text-slate-400 border-slate-800 hover:text-white'
                    ]"
                  >
                    {{ sys.label }}
                  </button>
                </div>
              </div>

              <!-- 让球方与盘口 -->
              <div class="grid grid-cols-2 gap-3">
                <div>
                  <label class="block text-[11px] font-semibold text-slate-400 mb-1.5">让球方</label>
                  <div class="grid grid-cols-2 gap-1.5">
                    <button
                      @click="simulateForm.handicapSide = 'HOME'; runSimulation()"
                      class="py-1.5 rounded-xl border text-xs font-bold transition flex items-center justify-center gap-1"
                      :class="[
                        simulateForm.handicapSide === 'HOME'
                          ? 'bg-amber-500/20 text-amber-300 border-amber-500/50'
                          : 'bg-slate-900/80 text-slate-400 border-slate-800 hover:text-white'
                      ]"
                    >
                      <span>🏠 主让</span>
                    </button>
                    <button
                      @click="simulateForm.handicapSide = 'AWAY'; runSimulation()"
                      class="py-1.5 rounded-xl border text-xs font-bold transition flex items-center justify-center gap-1"
                      :class="[
                        simulateForm.handicapSide === 'AWAY'
                          ? 'bg-blue-500/20 text-blue-300 border-blue-500/50'
                          : 'bg-slate-900/80 text-slate-400 border-slate-800 hover:text-white'
                      ]"
                    >
                      <span>✈️ 客让</span>
                    </button>
                  </div>
                </div>

                <div>
                  <label class="block text-[11px] font-semibold text-slate-400 mb-1.5">让球盘口</label>
                  <el-select
                    v-model="simulateForm.handicapValue"
                    class="!w-full custom-select"
                    @change="runSimulation"
                  >
                    <el-option label="让半球 (-0.50)" :value="-0.50" />
                    <el-option label="半一 (-0.75)" :value="-0.75" />
                    <el-option label="让一球 (-1.00)" :value="-1.00" />
                    <el-option label="一球球半 (-1.25)" :value="-1.25" />
                    <el-option label="让球半 (-1.50)" :value="-1.50" />
                    <el-option label="两球 (-2.00)" :value="-2.00" />
                  </el-select>
                </div>
              </div>

              <!-- 胜平负三项赔率 -->
              <div class="space-y-2">
                <div class="text-[11px] font-semibold text-slate-400 flex items-center justify-between">
                  <span>胜平负赔率 (请按实际盘口输入)</span>
                  <span class="text-[10px] text-amber-400/80">
                    {{ simulateForm.handicapSide === 'HOME' ? '基准胜赔: 主胜' : '基准胜赔: 客胜' }}
                  </span>
                </div>
                <div class="grid grid-cols-3 gap-2">
                  <div class="p-2 rounded-xl bg-slate-900/90 border border-slate-800/80 space-y-1">
                    <span class="text-[10px] font-bold text-emerald-400 block">主胜赔率</span>
                    <el-input-number
                      v-model="simulateForm.homeWinOdds"
                      :precision="2"
                      :step="0.05"
                      :min="1.01"
                      :max="25.0"
                      class="!w-full"
                      size="small"
                      @change="runSimulation"
                    />
                  </div>
                  <div class="p-2 rounded-xl bg-slate-900/90 border border-slate-800/80 space-y-1">
                    <span class="text-[10px] font-bold text-amber-400 block">平局赔率</span>
                    <el-input-number
                      v-model="simulateForm.drawOdds"
                      :precision="2"
                      :step="0.05"
                      :min="1.01"
                      :max="25.0"
                      class="!w-full"
                      size="small"
                      @change="runSimulation"
                    />
                  </div>
                  <div class="p-2 rounded-xl bg-slate-900/90 border border-slate-800/80 space-y-1">
                    <span class="text-[10px] font-bold text-rose-400 block">客胜赔率</span>
                    <el-input-number
                      v-model="simulateForm.awayWinOdds"
                      :precision="2"
                      :step="0.05"
                      :min="1.01"
                      :max="25.0"
                      class="!w-full"
                      size="small"
                      @change="runSimulation"
                    />
                  </div>
                </div>
              </div>

              <!-- 样本步幅容差选择 -->
              <div>
                <div class="flex items-center justify-between mb-1.5">
                  <label class="text-[11px] font-semibold text-slate-400">样本步幅容差</label>
                  <span class="text-[10px] text-cyan-300 font-mono">
                    赔率匹配: [{{ simOddsRangeText }}]
                  </span>
                </div>
                <div class="grid grid-cols-4 gap-1.5 text-xs">
                  <button
                    v-for="t in [0.05, 0.10, 0.15, 0.20]"
                    :key="t"
                    @click="simulateForm.tolerance = t; runSimulation()"
                    class="py-1.5 rounded-xl border font-bold transition text-center"
                    :class="[
                      simulateForm.tolerance === t
                        ? 'bg-amber-500/20 text-amber-300 border-amber-500/50'
                        : 'bg-slate-900/80 text-slate-400 border-slate-800 hover:text-white'
                    ]"
                  >
                    ±{{ t.toFixed(2) }}
                  </button>
                </div>
              </div>

              <!-- 重新计算触发按钮 -->
              <button
                @click="runSimulation"
                :disabled="simulateLoading"
                class="w-full py-2.5 rounded-xl text-xs font-extrabold bg-gradient-to-r from-cyan-500 via-sky-500 to-blue-600 hover:from-cyan-400 hover:to-blue-500 text-slate-950 shadow-lg shadow-cyan-500/20 transition flex items-center justify-center gap-1.5"
              >
                <span v-if="simulateLoading" class="animate-spin">⏳</span>
                <span v-else>⚡</span>
                <span>{{ simulateLoading ? '正在检索库内样本计算...' : '立即测算胜平负概率与期望 ROI' }}</span>
              </button>
            </div>
          </div>

          <!-- 右侧测算结果看板 (7 列) -->
          <div class="lg:col-span-7 space-y-3.5">
            <!-- 样本匹配概况 Bar -->
            <div class="p-3.5 rounded-2xl bg-slate-950/80 border border-slate-800 flex items-center justify-between flex-wrap gap-2">
              <div class="flex items-center gap-2">
                <span class="text-xs text-slate-400">库内历史同类样本:</span>
                <span class="text-lg font-black font-mono text-cyan-300">
                  {{ simulateResult?.sampleCount ?? 0 }}
                </span>
                <span class="text-xs text-slate-400">场</span>
                <span
                  class="text-[10px] px-2 py-0.5 rounded-full font-bold uppercase tracking-wider"
                  :class="[
                    (simulateResult?.sampleCount || 0) >= 10
                      ? 'bg-emerald-500/15 text-emerald-400 border border-emerald-500/30'
                      : ((simulateResult?.sampleCount || 0) >= 4
                          ? 'bg-amber-500/15 text-amber-300 border border-amber-500/30'
                          : 'bg-rose-500/15 text-rose-300 border border-rose-500/30')
                  ]"
                >
                  {{ (simulateResult?.sampleCount || 0) >= 10 ? '样本充足 (高置信)' : ((simulateResult?.sampleCount || 0) >= 4 ? '样本中等 (供参考)' : '样本偏少 (谨慎)') }}
                </span>
              </div>

              <div class="text-[11px] text-slate-400 font-mono">
                匹配区间: <span class="text-slate-200 font-bold">{{ simulateResult?.oddsRangeDesc || '--' }}</span>
              </div>
            </div>

            <!-- 核心：胜平负三大选项概率 & ROI 卡片 -->
            <div class="grid grid-cols-1 sm:grid-cols-3 gap-3">
              <!-- 1. 主胜卡片 -->
              <div
                class="p-3.5 rounded-2xl border transition relative overflow-hidden flex flex-col justify-between"
                :class="[
                  simulateResult?.bestChoice === 'HOME_WIN'
                    ? 'bg-gradient-to-b from-emerald-950/40 to-slate-950/80 border-emerald-500/60 shadow-lg shadow-emerald-500/15'
                    : 'bg-slate-950/70 border-slate-800/80'
                ]"
              >
                <div v-if="simulateResult?.bestChoice === 'HOME_WIN'" class="absolute -right-6 -top-6 w-16 h-16 bg-emerald-500/20 rounded-full blur-xl pointer-events-none"></div>
                <div>
                  <div class="flex items-center justify-between mb-2">
                    <span class="text-xs font-extrabold text-emerald-400 flex items-center gap-1">
                      <span>🏠 主胜</span>
                      <span v-if="simulateResult?.bestChoice === 'HOME_WIN'" class="text-[9px] bg-emerald-500 text-slate-950 px-1 rounded font-black">
                        BEST
                      </span>
                    </span>
                    <span class="text-[11px] font-mono text-slate-400">
                      赔率 {{ simulateForm.homeWinOdds }}
                    </span>
                  </div>

                  <div class="space-y-1 mb-2.5">
                    <div class="flex items-baseline justify-between">
                      <span class="text-[10px] text-slate-400">历史打出率</span>
                      <span class="text-base font-black font-mono text-emerald-300">
                        {{ simulateResult?.homeWinProb ?? 0 }}%
                      </span>
                    </div>
                    <!-- 进度条 -->
                    <div class="w-full h-1.5 rounded-full bg-slate-800 overflow-hidden">
                      <div
                        class="h-full bg-gradient-to-r from-emerald-500 to-teal-400 rounded-full transition-all duration-500"
                        :style="{ width: `${simulateResult?.homeWinProb || 0}%` }"
                      ></div>
                    </div>
                    <div class="text-[10px] text-slate-500 font-mono text-right">
                      {{ simulateResult?.homeWinCount ?? 0 }} 场 / {{ simulateResult?.sampleCount ?? 0 }} 场
                    </div>
                  </div>
                </div>

                <!-- ROI 收益率 -->
                <div class="pt-2 border-t border-slate-800/70 flex items-center justify-between">
                  <span class="text-[11px] text-slate-400 font-semibold">期望 ROI:</span>
                  <div class="text-right">
                    <span
                      class="text-sm font-black font-mono"
                      :class="[
                        (simulateResult?.homeWinRoi || 0) > 0 ? 'text-emerald-400' : 'text-slate-300'
                      ]"
                    >
                      {{ (simulateResult?.homeWinRoi || 0) > 0 ? '+' : '' }}{{ simulateResult?.homeWinRoi ?? 0 }}%
                    </span>
                  </div>
                </div>
              </div>

              <!-- 2. 平局卡片 -->
              <div
                class="p-3.5 rounded-2xl border transition relative overflow-hidden flex flex-col justify-between"
                :class="[
                  simulateResult?.bestChoice === 'DRAW'
                    ? 'bg-gradient-to-b from-amber-950/40 to-slate-950/80 border-amber-500/60 shadow-lg shadow-amber-500/15'
                    : 'bg-slate-950/70 border-slate-800/80'
                ]"
              >
                <div v-if="simulateResult?.bestChoice === 'DRAW'" class="absolute -right-6 -top-6 w-16 h-16 bg-amber-500/20 rounded-full blur-xl pointer-events-none"></div>
                <div>
                  <div class="flex items-center justify-between mb-2">
                    <span class="text-xs font-extrabold text-amber-300 flex items-center gap-1">
                      <span>🤝 平局</span>
                      <span v-if="simulateResult?.bestChoice === 'DRAW'" class="text-[9px] bg-amber-400 text-slate-950 px-1 rounded font-black">
                        BEST
                      </span>
                    </span>
                    <span class="text-[11px] font-mono text-slate-400">
                      赔率 {{ simulateForm.drawOdds }}
                    </span>
                  </div>

                  <div class="space-y-1 mb-2.5">
                    <div class="flex items-baseline justify-between">
                      <span class="text-[10px] text-slate-400">历史打出率</span>
                      <span class="text-base font-black font-mono text-amber-300">
                        {{ simulateResult?.drawProb ?? 0 }}%
                      </span>
                    </div>
                    <div class="w-full h-1.5 rounded-full bg-slate-800 overflow-hidden">
                      <div
                        class="h-full bg-gradient-to-r from-amber-500 to-orange-400 rounded-full transition-all duration-500"
                        :style="{ width: `${simulateResult?.drawProb || 0}%` }"
                      ></div>
                    </div>
                    <div class="text-[10px] text-slate-500 font-mono text-right">
                      {{ simulateResult?.drawCount ?? 0 }} 场 / {{ simulateResult?.sampleCount ?? 0 }} 场
                    </div>
                  </div>
                </div>

                <div class="pt-2 border-t border-slate-800/70 flex items-center justify-between">
                  <span class="text-[11px] text-slate-400 font-semibold">期望 ROI:</span>
                  <div class="text-right">
                    <span
                      class="text-sm font-black font-mono"
                      :class="[
                        (simulateResult?.drawRoi || 0) > 0 ? 'text-emerald-400' : 'text-slate-300'
                      ]"
                    >
                      {{ (simulateResult?.drawRoi || 0) > 0 ? '+' : '' }}{{ simulateResult?.drawRoi ?? 0 }}%
                    </span>
                  </div>
                </div>
              </div>

              <!-- 3. 客胜卡片 -->
              <div
                class="p-3.5 rounded-2xl border transition relative overflow-hidden flex flex-col justify-between"
                :class="[
                  simulateResult?.bestChoice === 'AWAY_WIN'
                    ? 'bg-gradient-to-b from-rose-950/40 to-slate-950/80 border-rose-500/60 shadow-lg shadow-rose-500/15'
                    : 'bg-slate-950/70 border-slate-800/80'
                ]"
              >
                <div v-if="simulateResult?.bestChoice === 'AWAY_WIN'" class="absolute -right-6 -top-6 w-16 h-16 bg-rose-500/20 rounded-full blur-xl pointer-events-none"></div>
                <div>
                  <div class="flex items-center justify-between mb-2">
                    <span class="text-xs font-extrabold text-rose-400 flex items-center gap-1">
                      <span>✈️ 客胜</span>
                      <span v-if="simulateResult?.bestChoice === 'AWAY_WIN'" class="text-[9px] bg-rose-500 text-white px-1 rounded font-black">
                        BEST
                      </span>
                    </span>
                    <span class="text-[11px] font-mono text-slate-400">
                      赔率 {{ simulateForm.awayWinOdds }}
                    </span>
                  </div>

                  <div class="space-y-1 mb-2.5">
                    <div class="flex items-baseline justify-between">
                      <span class="text-[10px] text-slate-400">历史打出率</span>
                      <span class="text-base font-black font-mono text-rose-300">
                        {{ simulateResult?.awayWinProb ?? 0 }}%
                      </span>
                    </div>
                    <div class="w-full h-1.5 rounded-full bg-slate-800 overflow-hidden">
                      <div
                        class="h-full bg-gradient-to-r from-rose-500 to-pink-500 rounded-full transition-all duration-500"
                        :style="{ width: `${simulateResult?.awayWinProb || 0}%` }"
                      ></div>
                    </div>
                    <div class="text-[10px] text-slate-500 font-mono text-right">
                      {{ simulateResult?.awayWinCount ?? 0 }} 场 / {{ simulateResult?.sampleCount ?? 0 }} 场
                    </div>
                  </div>
                </div>

                <div class="pt-2 border-t border-slate-800/70 flex items-center justify-between">
                  <span class="text-[11px] text-slate-400 font-semibold">期望 ROI:</span>
                  <div class="text-right">
                    <span
                      class="text-sm font-black font-mono"
                      :class="[
                        (simulateResult?.awayWinRoi || 0) > 0 ? 'text-emerald-400' : 'text-slate-300'
                      ]"
                    >
                      {{ (simulateResult?.awayWinRoi || 0) > 0 ? '+' : '' }}{{ simulateResult?.awayWinRoi ?? 0 }}%
                    </span>
                  </div>
                </div>
              </div>
            </div>

            <!-- 穿盘指标 Bar -->
            <div class="p-3 rounded-2xl bg-slate-950/70 border border-slate-800 flex items-center justify-between flex-wrap gap-2 text-xs">
              <div class="flex items-center gap-2">
                <span class="text-amber-400 font-bold">🎯 让球穿盘概率:</span>
                <span class="text-base font-black font-mono text-amber-300">
                  {{ simulateResult?.breakProb ?? 0 }}%
                </span>
                <span class="text-[11px] text-slate-400 font-mono">
                  ({{ simulateResult?.breakCount ?? 0 }} 场打穿 / {{ simulateResult?.sampleCount ?? 0 }} 场)
                </span>
              </div>
              <div class="text-[11px] text-slate-400">
                <span v-if="(simulateResult?.breakProb || 0) >= 50" class="text-emerald-400 font-bold">
                  ✓ 穿盘能力强，可考虑让球胜
                </span>
                <span v-else class="text-rose-400 font-bold">
                  ✗ 穿盘率偏低，谨防赢球输盘或平局下盘
                </span>
              </div>
            </div>

            <!-- 策略建议卡片 (AI Value Analysis) -->
            <div class="p-3.5 rounded-2xl bg-gradient-to-r from-slate-950 via-slate-900 to-slate-950 border border-slate-800/90 space-y-2">
              <div class="flex items-center justify-between">
                <span class="text-xs font-bold text-amber-300 flex items-center gap-1.5">
                  <span>🤖 量化策略与打单建议</span>
                </span>
                <span v-if="simulateResult?.bestChoiceName" class="text-[11px] px-2 py-0.5 rounded-lg bg-slate-800 text-slate-300 font-mono">
                  推荐关注: <b class="text-cyan-300">{{ simulateResult.bestChoiceName }}</b>
                </span>
              </div>
              <p class="text-xs text-slate-300 leading-relaxed">
                {{ simulateResult?.advice || '正在等待测算输入...' }}
              </p>
            </div>

            <!-- 辅助统计：高发进球数分布 & 半全场 Top -->
            <div class="grid grid-cols-1 sm:grid-cols-2 gap-3 text-xs">
              <div class="p-3 rounded-2xl bg-slate-950/60 border border-slate-800 space-y-1.5">
                <div class="text-[11px] font-bold text-slate-400 flex items-center gap-1">
                  <span>⚽ 样本高发总进球</span>
                </div>
                <div v-if="simulateResult?.goalsDistribution && Object.keys(simulateResult.goalsDistribution).length" class="flex items-center gap-2 flex-wrap">
                  <span
                    v-for="(count, g) in simulateResult.goalsDistribution"
                    :key="g"
                    class="px-2 py-0.5 rounded-lg bg-slate-900 border border-slate-800 text-[11px] font-mono text-slate-300"
                  >
                    {{ g }}: <b class="text-amber-400">{{ count }}</b> 场
                  </span>
                </div>
                <span v-else class="text-[11px] text-slate-500">暂无进球数据</span>
              </div>

              <div class="p-3 rounded-2xl bg-slate-950/60 border border-slate-800 space-y-1.5">
                <div class="text-[11px] font-bold text-slate-400 flex items-center gap-1">
                  <span>🌓 样本高发半全场</span>
                </div>
                <div v-if="simulateResult?.halfFullDistribution && Object.keys(simulateResult.halfFullDistribution).length" class="flex items-center gap-2 flex-wrap">
                  <span
                    v-for="(count, hf) in simulateResult.halfFullDistribution"
                    :key="hf"
                    class="px-2 py-0.5 rounded-lg bg-slate-900 border border-slate-800 text-[11px] font-mono text-slate-300"
                  >
                    {{ hf }}: <b class="text-cyan-400">{{ count }}</b> 场
                  </span>
                </div>
                <span v-else class="text-[11px] text-slate-500">暂无半全场数据</span>
              </div>
            </div>

            <!-- 历史样本比赛折叠明细列表 -->
            <div class="border border-slate-800 rounded-2xl overflow-hidden">
              <button
                @click="showMatchedDetails = !showMatchedDetails"
                class="w-full px-3.5 py-2.5 bg-slate-950 hover:bg-slate-900 text-xs font-semibold text-slate-300 flex items-center justify-between transition"
              >
                <span class="flex items-center gap-1.5">
                  <span>📋</span>
                  <span>查看匹配的历史样本比赛明细 ({{ simulateResult?.sampleMatches?.length || 0 }} 场)</span>
                </span>
                <span class="text-slate-500 text-[10px] font-mono">{{ showMatchedDetails ? '收起 ▲' : '展开 ▼' }}</span>
              </button>

              <div v-if="showMatchedDetails" class="p-2 bg-slate-950/90 max-h-48 overflow-y-auto">
                <table v-if="simulateResult?.sampleMatches?.length" class="w-full text-[11px] text-left">
                  <thead class="text-slate-400 border-b border-slate-800">
                    <tr>
                      <th class="p-1.5">日期</th>
                      <th class="p-1.5">联赛</th>
                      <th class="p-1.5">对阵</th>
                      <th class="p-1.5">比分</th>
                      <th class="p-1.5">赛果</th>
                      <th class="p-1.5">让球胜赔</th>
                      <th class="p-1.5">穿盘</th>
                    </tr>
                  </thead>
                  <tbody class="divide-y divide-slate-800/60 text-slate-300">
                    <tr v-for="m in simulateResult.sampleMatches" :key="m.id" class="hover:bg-slate-900/50">
                      <td class="p-1.5 font-mono text-slate-400">{{ m.matchDate }}</td>
                      <td class="p-1.5">{{ m.leagueName }}</td>
                      <td class="p-1.5 font-semibold text-white">{{ m.homeTeam }} vs {{ m.awayTeam }}</td>
                      <td class="p-1.5 font-mono font-bold text-amber-300">{{ m.fullScore || '--' }}</td>
                      <td class="p-1.5">
                        <span
                          class="px-1.5 py-0.5 rounded text-[10px] font-bold"
                          :class="[
                            m.matchResult === 'HOME_WIN' ? 'bg-emerald-500/20 text-emerald-300' :
                            (m.matchResult === 'DRAW' ? 'bg-amber-500/20 text-amber-300' : 'bg-rose-500/20 text-rose-300')
                          ]"
                        >
                          {{ m.matchResultName || m.matchResult }}
                        </span>
                      </td>
                      <td class="p-1.5 font-mono text-cyan-300">{{ m.handicapOdds }}</td>
                      <td class="p-1.5">
                        <span :class="m.isBreak ? 'text-amber-400 font-bold' : 'text-slate-500'">
                          {{ m.isBreak ? '🔥 穿' : '未穿' }}
                        </span>
                      </td>
                    </tr>
                  </tbody>
                </table>
                <div v-else class="p-4 text-center text-xs text-slate-500">
                  当前区间内暂无样本比赛明细
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <template #footer>
        <div class="flex items-center justify-between pt-2">
          <span class="text-xs text-slate-500">
            * 测算结果仅供博弈与量化投资复盘参考，请理性评估
          </span>
          <button
            @click="simulateModalVisible = false"
            class="px-5 py-2 rounded-xl text-xs font-semibold bg-slate-800 hover:bg-slate-700 text-slate-200 transition"
          >
            关闭测算
          </button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, nextTick, watch, onBeforeUnmount } from 'vue'
import * as echarts from 'echarts'
import { ElMessage, ElMessageBox } from 'element-plus'
import api from '../api'

// 1. 步幅选项配置 (用户核心需求: 0.1, 0.15, 0.05, 0.2)
const stepOptions = [
  { label: '0.05 步幅', value: 0.05, icon: '🔬', desc: '微观极精细' },
  { label: '0.10 步幅', value: 0.10, icon: '🎯', desc: '标准精细' },
  { label: '0.15 步幅', value: 0.15, icon: '📊', desc: '中度区间' },
  { label: '0.20 步幅', value: 0.20, icon: '🔭', desc: '稳健宽幅' }
]

const currentStep = ref(0.10) // 默认 0.10

// 2. 主客场让球筛选 (维度 1)
const sideFilter = ref('ALL') // ALL, HOME, AWAY
const sideOptions = [
  { label: '全部让球', value: 'ALL' },
  { label: '仅主让球', value: 'HOME' },
  { label: '仅客让球', value: 'AWAY' }
]

// 3. 赛制类型筛选 (维度 2: 联赛 / 杯赛淘汰赛 / 友谊热身赛)
const systemFilter = ref('ALL') // ALL, LEAGUE, CUP, FRIENDLY
const systemOptions = [
  { label: '全部赛制', value: 'ALL', icon: '🌐' },
  { label: '联赛积分制', value: 'LEAGUE', icon: '🏆' },
  { label: '杯赛/淘汰赛', value: 'CUP', icon: '🥊' },
  { label: '友谊热身赛', value: 'FRIENDLY', icon: '🤝' }
]

// 4. 时间范围筛选 (维度 3: 全部 / 最近1个月 / 最近3个月)
const timePreset = ref('ALL') // ALL, 1M, 3M, CUSTOM
const timePresets = [
  { label: '全部时间', value: 'ALL' },
  { label: '最近1个月', value: '1M' },
  { label: '最近3个月', value: '3M' }
]

const dateRange = ref([])
const searchKeyword = ref('')
const matches = ref([])
const loading = ref(false)

// 分页状态
const currentPage = ref(1)
const pageSize = ref(15)

// 图表 DOM 引用
const steppedChartRef = ref(null)
const goalsChartRef = ref(null)
let steppedChartInstance = null
let goalsChartInstance = null

// 弹窗状态
const matchModalVisible = ref(false)
const batchModalVisible = ref(false)
const simulateModalVisible = ref(false)
const simulateLoading = ref(false)
const showMatchedDetails = ref(false)
const isEditMode = ref(false)
const deducedHint = ref('')

// 单场测算表单与结果状态
const simulateForm = ref({
  matchSystem: 'LEAGUE',
  handicapSide: 'HOME',
  handicapValue: -1.0,
  homeWinOdds: 1.55,
  drawOdds: 3.80,
  awayWinOdds: 5.20,
  tolerance: 0.10
})
const simulateResult = ref(null)

const simSystemOptions = [
  { label: '全部', value: 'ALL' },
  { label: '联赛', value: 'LEAGUE' },
  { label: '杯赛', value: 'CUP' },
  { label: '友谊赛', value: 'FRIENDLY' }
]

const simPresets = [
  { name: '🔥 主让强强对话', system: 'LEAGUE', side: 'HOME', home: 1.55, draw: 3.80, away: 5.20, tol: 0.10, val: -1.0 },
  { name: '💎 主让深盘虐菜', system: 'LEAGUE', side: 'HOME', home: 1.28, draw: 5.20, away: 9.80, tol: 0.08, val: -1.5 },
  { name: '⚖️ 均势平半胶着', system: 'LEAGUE', side: 'HOME', home: 1.95, draw: 3.40, away: 3.60, tol: 0.10, val: -0.5 },
  { name: '✈️ 客让实力碾压', system: 'LEAGUE', side: 'AWAY', home: 5.50, draw: 3.90, away: 1.52, tol: 0.10, val: -1.0 },
  { name: '🏆 杯赛淘汰赛激战', system: 'CUP', side: 'HOME', home: 1.68, draw: 3.60, away: 4.50, tol: 0.12, val: -1.0 }
]

const simOddsRangeText = computed(() => {
  const base = simulateForm.value.handicapSide === 'HOME'
    ? Number(simulateForm.value.homeWinOdds || 1.50)
    : Number(simulateForm.value.awayWinOdds || 1.50)
  const tol = Number(simulateForm.value.tolerance || 0.10)
  return `${(base - tol).toFixed(2)} ~ ${(base + tol).toFixed(2)}`
})

const form = ref({
  id: null,
  matchDate: new Date().toISOString().split('T')[0],
  batchTitle: '',
  leagueName: '',
  matchSystem: 'LEAGUE',
  homeTeam: '',
  awayTeam: '',
  handicapSide: 'HOME',
  handicapValue: -1.0,
  homeWinOdds: 1.50,
  drawOdds: 3.80,
  awayWinOdds: 5.50,
  handicapOdds: 1.50,
  halfScore: '',
  fullScore: '',
  matchResult: 'HOME_WIN',
  resultOdds: 1.50,
  handicapResult: 'WIN',
  handicapResultOdds: 2.35,
  halfFullResult: '胜胜',
  totalGoals: 2,
  isBreak: true,
  remarks: ''
})

const batchList = ref([])

// 改变步幅
const changeStep = (stepVal) => {
  currentStep.value = stepVal
  nextTick(() => {
    updateCharts()
  })
}

// 时间范围快捷切换 (最近一个月 / 最近3个月 / 全部)
const setTimePreset = (preset) => {
  timePreset.value = preset
  const now = new Date()
  const formatDate = (d) => {
    const y = d.getFullYear()
    const m = String(d.getMonth() + 1).padStart(2, '0')
    const day = String(d.getDate()).padStart(2, '0')
    return `${y}-${m}-${day}`
  }

  if (preset === 'ALL') {
    dateRange.value = []
  } else if (preset === '1M') {
    const past = new Date()
    past.setDate(now.getDate() - 30)
    dateRange.value = [formatDate(past), formatDate(now)]
  } else if (preset === '3M') {
    const past = new Date()
    past.setDate(now.getDate() - 90)
    dateRange.value = [formatDate(past), formatDate(now)]
  }
}

// 手动调整日期选择器
const onManualDateChange = (val) => {
  if (!val || val.length === 0) {
    timePreset.value = 'ALL'
  } else {
    timePreset.value = 'CUSTOM'
  }
}

// 获取单场明细列表 (无条件全部拉取，由前端在内存中极速响应多维切换)
const fetchMatchList = async () => {
  loading.value = true
  try {
    const res = await api.getHandicapMatchRecords({})
    const list = res.data || res || []
    matches.value = Array.isArray(list) ? list : []
    nextTick(() => {
      updateCharts()
    })
  } catch (err) {
    console.error('获取单场比赛列表失败:', err)
    ElMessage.error('加载比赛记录失败: ' + (err.message || '网络异常'))
  } finally {
    loading.value = false
  }
}

// ==================== 4维正交叠加过滤 (步幅 + 让球方 + 赛制 + 时间) ====================
const filteredMatches = computed(() => {
  let list = matches.value

  // 1. 让球方筛选 (HOME/AWAY/ALL)
  if (sideFilter.value !== 'ALL') {
    list = list.filter(m => m.handicapSide === sideFilter.value)
  }

  // 2. 赛制类型筛选 (LEAGUE/CUP/FRIENDLY/ALL)
  if (systemFilter.value !== 'ALL') {
    list = list.filter(m => {
      let sys = m.matchSystem
      if (!sys) {
        // 兼容未填赛制的历史数据，按联赛名称智能推断
        const ln = m.leagueName || ''
        if (ln.includes('杯') || ln.includes('冠') || ln.includes('欧联') || ln.includes('淘汰') || ln.includes('锦标')) {
          sys = 'CUP'
        } else if (ln.includes('友谊') || ln.includes('热身')) {
          sys = 'FRIENDLY'
        } else {
          sys = 'LEAGUE'
        }
      }
      return sys === systemFilter.value
    })
  }

  // 3. 时间跨度筛选 (全部 / 最近1个月 / 最近3个月 / 自定义)
  if (dateRange.value && dateRange.value.length === 2) {
    const [start, end] = dateRange.value
    list = list.filter(m => m.matchDate >= start && m.matchDate <= end)
  }

  // 4. 关键字搜索
  if (searchKeyword.value.trim()) {
    const kw = searchKeyword.value.trim().toLowerCase()
    list = list.filter(m => 
      (m.homeTeam && m.homeTeam.toLowerCase().includes(kw)) ||
      (m.awayTeam && m.awayTeam.toLowerCase().includes(kw)) ||
      (m.leagueName && m.leagueName.toLowerCase().includes(kw)) ||
      (m.batchTitle && m.batchTitle.toLowerCase().includes(kw))
    )
  }

  return list
})

// 分页列表
const maxPage = computed(() => Math.max(1, Math.ceil(filteredMatches.value.length / pageSize.value)))
const pagedMatches = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  return filteredMatches.value.slice(start, start + pageSize.value)
})

// ==================== 核心算法：按动态步幅进行分桶 ====================
const steppedBuckets = computed(() => {
  const step = Number(currentStep.value)
  const list = filteredMatches.value
  if (!list.length) return []

  const bucketMap = new Map()

  // 划分区间桶
  list.forEach(m => {
    const o = Number(m.handicapOdds || m.homeWinOdds || 1.5)
    const bMin = Math.floor(o / step) * step
    const bMinR = Math.round(bMin * 100) / 100
    const bMaxR = Math.round((bMinR + step) * 100) / 100
    const key = `${bMinR.toFixed(2)}-${bMaxR.toFixed(2)}`

    if (!bucketMap.has(key)) {
      bucketMap.set(key, {
        key,
        min: bMinR,
        max: bMaxR,
        label: `${bMinR.toFixed(2)} - ${bMaxR.toFixed(2)}`,
        matches: [],
        totalCount: 0,
        winCount: 0,
        drawCount: 0,
        lossCount: 0,
        breakCount: 0,
        unbreakCount: 0,
        winRate: 0,
        breakRateOnWin: 0,
        overallBreakRate: 0,
        avgResultOdds: 0,
        avgHandicapOdds: 0,
        goalsDistribution: {},
        halfFullDistribution: {},
        mostCommonGoals: '-',
        topCommonGoals: [],
        mostCommonHalfFull: '-'
      })
    }

    const b = bucketMap.get(key)
    b.matches.push(m)
    b.totalCount++

    // 判断让球方胜平负
    const isHome = m.handicapSide === 'HOME'
    const res = m.matchResult
    if ((isHome && res === 'HOME_WIN') || (!isHome && res === 'AWAY_WIN')) {
      b.winCount++
    } else if (res === 'DRAW') {
      b.drawCount++
    } else {
      b.lossCount++
    }

    if (m.isBreak) {
      b.breakCount++
    } else {
      b.unbreakCount++
    }

    // 统计总进球
    const g = m.totalGoals != null ? m.totalGoals : 0
    b.goalsDistribution[g] = (b.goalsDistribution[g] || 0) + 1

    // 统计半全场
    if (m.halfFullResult) {
      b.halfFullDistribution[m.halfFullResult] = (b.halfFullDistribution[m.halfFullResult] || 0) + 1
    }
  })

  // 聚合各指标
  const result = []
  bucketMap.forEach(b => {
    if (b.totalCount > 0) {
      b.winRate = Number(((b.winCount / b.totalCount) * 100).toFixed(1))
      b.breakRateOnWin = b.winCount > 0 ? Number(((b.breakCount / b.winCount) * 100).toFixed(1)) : 0
      b.overallBreakRate = Number(((b.breakCount / b.totalCount) * 100).toFixed(1))

      // 平均打出赔
      const sumResultOdds = b.matches.reduce((acc, cur) => acc + Number(cur.resultOdds || 0), 0)
      b.avgResultOdds = Number((sumResultOdds / b.totalCount).toFixed(2))

      // 平均让球开奖赔
      const hrMatches = b.matches.filter(m => m.handicapResultOdds != null)
      if (hrMatches.length > 0) {
        const sumHrOdds = hrMatches.reduce((acc, cur) => acc + Number(cur.handicapResultOdds), 0)
        b.avgHandicapOdds = Number((sumHrOdds / hrMatches.length).toFixed(2))
      }

      // 常见进球众数 (统计最常见的前两个进球数及各自占比)
      const sortedGoals = Object.entries(b.goalsDistribution)
        .map(([goals, cnt]) => ({
          goals: String(goals),
          cnt,
          rate: Number(((cnt / b.totalCount) * 100).toFixed(1))
        }))
        .sort((x, y) => {
          if (y.cnt !== x.cnt) return y.cnt - x.cnt
          return Number(x.goals) - Number(y.goals)
        })

      b.topCommonGoals = sortedGoals.slice(0, 2)
      if (b.topCommonGoals.length > 0) {
        b.mostCommonGoals = b.topCommonGoals
          .map(item => `${item.goals}球 (${item.rate}%)`)
          .join(' · ')
      } else {
        b.mostCommonGoals = '-'
      }

      // 高发半全场众数
      let maxHFCnt = -1
      let bestHF = '-'
      Object.entries(b.halfFullDistribution).forEach(([hf, cnt]) => {
        if (cnt > maxHFCnt) {
          maxHFCnt = cnt
          bestHF = `${hf} (${cnt}场)`
        }
      })
      b.mostCommonHalfFull = bestHF

      result.push(b)
    }
  })

  return result.sort((a, b) => a.min - b.min)
})

// 总体汇总 KPI
const totalStats = computed(() => {
  const list = filteredMatches.value
  const total = list.length
  if (total === 0) {
    return {
      win: 0, draw: 0, loss: 0, breakCount: 0,
      winRate: 0, overallBreakRate: 0, breakRateOnWin: 0,
      bestBreakInterval: '-'
    }
  }

  let win = 0
  let draw = 0
  let loss = 0
  let breakCount = 0

  list.forEach(m => {
    const isHome = m.handicapSide === 'HOME'
    const res = m.matchResult
    if ((isHome && res === 'HOME_WIN') || (!isHome && res === 'AWAY_WIN')) {
      win++
    } else if (res === 'DRAW') {
      draw++
    } else {
      loss++
    }
    if (m.isBreak) breakCount++
  })

  const winRate = Number(((win / total) * 100).toFixed(1))
  const overallBreakRate = Number(((breakCount / total) * 100).toFixed(1))
  const breakRateOnWin = win > 0 ? Number(((breakCount / win) * 100).toFixed(1)) : 0

  // 寻找最佳穿盘区间
  let bestInterval = '-'
  let maxRate = -1
  steppedBuckets.value.forEach(b => {
    if (b.totalCount >= 2 && b.overallBreakRate > maxRate) {
      maxRate = b.overallBreakRate
      bestInterval = `${b.label} (${b.overallBreakRate}%)`
    }
  })

  return {
    win, draw, loss, breakCount,
    winRate, overallBreakRate, breakRateOnWin,
    bestBreakInterval: bestInterval
  }
})

// ==================== ECharts 图表渲染 ====================
const updateCharts = () => {
  renderSteppedChart()
  renderGoalsChart()
}

const renderSteppedChart = () => {
  if (!steppedChartRef.value) return
  if (!steppedChartInstance) {
    steppedChartInstance = echarts.init(steppedChartRef.value)
  }

  const buckets = steppedBuckets.value
  const xLabels = buckets.map(b => b.label)
  const winData = buckets.map(b => b.winCount)
  const drawData = buckets.map(b => b.drawCount)
  const lossData = buckets.map(b => b.lossCount)
  const breakRateData = buckets.map(b => b.overallBreakRate)
  const winBreakRateData = buckets.map(b => b.breakRateOnWin)

  const option = {
    backgroundColor: 'transparent',
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'cross' },
      backgroundColor: '#090d16',
      borderColor: '#334155',
      textStyle: { color: '#e2e8f0', fontSize: 12 },
      formatter: (params) => {
        const title = params[0].name
        const b = buckets.find(item => item.label === title)
        let html = `<div class="font-bold text-amber-300 mb-1">赔率区间: ${title}</div>`
        if (b) {
          html += `<div class="text-[11px] text-slate-300">总样本: <strong>${b.totalCount}</strong> 场 | 胜率: <strong>${b.winRate}%</strong></div>`
          html += `<div class="text-[11px] text-emerald-400">胜: ${b.winCount} · 平: ${b.drawCount} · 负: ${b.lossCount}</div>`
          html += `<div class="text-[11px] text-orange-400">穿盘: <strong>${b.breakCount}</strong> 场 | 综合穿盘率: <strong>${b.overallBreakRate}%</strong></div>`
          html += `<div class="text-[11px] text-purple-300">赢球穿盘纯度: <strong>${b.breakRateOnWin}%</strong></div>`
          html += `<div class="text-[10px] text-slate-400 mt-1">高发进球: ${b.mostCommonGoals} | 半全场: ${b.mostCommonHalfFull}</div>`
        }
        return html
      }
    },
    grid: { top: '15%', left: '3%', right: '4%', bottom: '10%', containLabel: true },
    xAxis: {
      type: 'category',
      data: xLabels,
      axisLabel: { color: '#94a3b8', fontSize: 11, rotate: xLabels.length > 8 ? 30 : 0 },
      axisLine: { lineStyle: { color: '#334155' } }
    },
    yAxis: [
      {
        type: 'value',
        name: '比赛场数',
        nameTextStyle: { color: '#94a3b8', fontSize: 11 },
        splitLine: { lineStyle: { color: '#1e293b', type: 'dashed' } },
        axisLabel: { color: '#94a3b8' }
      },
      {
        type: 'value',
        name: '穿盘率 (%)',
        min: 0,
        max: 100,
        nameTextStyle: { color: '#fb923c', fontSize: 11 },
        splitLine: { show: false },
        axisLabel: { color: '#fb923c', formatter: '{value}%' }
      }
    ],
    series: [
      {
        name: '让球方胜',
        type: 'bar',
        stack: 'matches',
        data: winData,
        itemStyle: { color: '#10b981' }
      },
      {
        name: '平局',
        type: 'bar',
        stack: 'matches',
        data: drawData,
        itemStyle: { color: '#fbbf24' }
      },
      {
        name: '让球方负',
        type: 'bar',
        stack: 'matches',
        data: lossData,
        itemStyle: { color: '#f43f5e' }
      },
      {
        name: '全盘穿盘率',
        type: 'line',
        yAxisIndex: 1,
        smooth: true,
        data: breakRateData,
        itemStyle: { color: '#fb923c' },
        lineStyle: { width: 3, shadowColor: 'rgba(251, 146, 60, 0.4)', shadowBlur: 10 },
        symbolSize: 6
      },
      {
        name: '赢球穿盘纯度',
        type: 'line',
        yAxisIndex: 1,
        smooth: true,
        data: winBreakRateData,
        itemStyle: { color: '#c084fc' },
        lineStyle: { width: 2, type: 'dashed' },
        symbolSize: 5
      }
    ]
  }

  steppedChartInstance.setOption(option, true)
}

const renderGoalsChart = () => {
  if (!goalsChartRef.value) return
  if (!goalsChartInstance) {
    goalsChartInstance = echarts.init(goalsChartRef.value)
  }

  const goalsMap = { '0球': 0, '1球': 0, '2球': 0, '3球': 0, '4球': 0, '5球+': 0 }
  filteredMatches.value.forEach(m => {
    const g = m.totalGoals != null ? m.totalGoals : 0
    if (g >= 5) {
      goalsMap['5球+']++
    } else {
      goalsMap[`${g}球`]++
    }
  })

  const categories = Object.keys(goalsMap)
  const values = Object.values(goalsMap)

  const option = {
    backgroundColor: 'transparent',
    tooltip: {
      trigger: 'item',
      backgroundColor: '#090d16',
      borderColor: '#334155',
      textStyle: { color: '#e2e8f0', fontSize: 12 },
      formatter: '{b}: <strong>{c}</strong> 场 ({d}%)'
    },
    series: [
      {
        name: '总进球数分布',
        type: 'pie',
        radius: ['45%', '70%'],
        center: ['50%', '50%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 8,
          borderColor: '#0f172a',
          borderWidth: 2
        },
        label: {
          show: true,
          color: '#cbd5e1',
          formatter: '{b}: {c}场'
        },
        emphasis: {
          label: { show: true, fontSize: 13, fontWeight: 'bold' }
        },
        data: categories.map((name, i) => ({
          name,
          value: values[i],
          itemStyle: {
            color: ['#38bdf8', '#34d399', '#facc15', '#fb923c', '#f43f5e', '#a855f7'][i % 6]
          }
        }))
      }
    ]
  }

  goalsChartInstance.setOption(option, true)
}

// 选中某个分桶
const selectBucket = (b) => {
  ElMessage.info(`已选中赔率区间: ${b.label}，当前筛选下共有 ${b.totalCount} 场比赛`)
}

// 联赛名称输入时，自动识别赛制
const onLeagueNameInput = (val) => {
  if (!val) return
  const ln = val.trim()
  if (ln.includes('杯') || ln.includes('冠') || ln.includes('欧联') || ln.includes('淘汰') || ln.includes('锦标')) {
    form.value.matchSystem = 'CUP'
  } else if (ln.includes('友谊') || ln.includes('热身')) {
    form.value.matchSystem = 'FRIENDLY'
  } else {
    form.value.matchSystem = 'LEAGUE'
  }
}

// ==================== 智能联动推导逻辑 ====================
const onOddsOrSideChange = () => {
  if (form.value.handicapSide === 'HOME') {
    form.value.handicapOdds = form.value.homeWinOdds
  } else {
    form.value.handicapOdds = form.value.awayWinOdds
  }
  triggerAutoDeduce()
}

const triggerAutoDeduce = () => {
  const full = form.value.fullScore
  if (!full || !full.includes(':')) {
    deducedHint.value = ''
    return
  }

  const parts = full.split(':')
  const hg = parseInt(parts[0])
  const ag = parseInt(parts[1])
  if (isNaN(hg) || isNaN(ag)) return

  // 1. 总进球
  form.value.totalGoals = hg + ag

  // 2. 赛果
  if (hg > ag) {
    form.value.matchResult = 'HOME_WIN'
    form.value.resultOdds = form.value.homeWinOdds
  } else if (hg === ag) {
    form.value.matchResult = 'DRAW'
    form.value.resultOdds = form.value.drawOdds || 3.50
  } else {
    form.value.matchResult = 'AWAY_WIN'
    form.value.resultOdds = form.value.awayWinOdds
  }

  // 3. 让球赛果与穿盘
  const hVal = form.value.handicapValue != null ? Number(form.value.handicapValue) : -1.0
  let diff = 0
  if (form.value.handicapSide === 'HOME') {
    diff = (hg - ag) + hVal
  } else {
    diff = (ag - hg) + hVal
  }

  if (diff > 0.001) {
    form.value.handicapResult = 'WIN'
    form.value.isBreak = true
  } else if (Math.abs(diff) <= 0.001) {
    form.value.handicapResult = 'DRAW'
    form.value.isBreak = false
  } else {
    form.value.handicapResult = 'LOSS'
    form.value.isBreak = false
  }

  // 4. 半全场推断
  const half = form.value.halfScore
  if (half && half.includes(':')) {
    const hParts = half.split(':')
    const hh = parseInt(hParts[0])
    const ha = parseInt(hParts[1])
    if (!isNaN(hh) && !isNaN(ha)) {
      const halfRes = hh > ha ? '胜' : (hh === ha ? '平' : '负')
      const fullRes = hg > ag ? '胜' : (hg === ag ? '平' : '负')
      form.value.halfFullResult = halfRes + fullRes
    }
  }

  const resText = form.value.matchResult === 'HOME_WIN' ? '主胜' : form.value.matchResult === 'DRAW' ? '平局' : '客胜'
  const hrText = form.value.handicapResult === 'WIN' ? '让胜' : form.value.handicapResult === 'DRAW' ? '让平(走水)' : '让负'
  const breakText = form.value.isBreak ? '🔥 穿盘成功' : '未穿盘'
  deducedHint.value = `[智能推断] 赛果: ${resText} | 让球: ${hrText} | 半全场: ${form.value.halfFullResult || '-'} | 总进球: ${form.value.totalGoals}球 | 状态: ${breakText}`
}

// 批量录入单行推断
const batchItemDeduce = (item) => {
  if (item.handicapSide === 'HOME') {
    item.handicapOdds = item.homeWinOdds
  } else {
    item.handicapOdds = item.awayWinOdds
  }

  const full = item.fullScore
  if (!full || !full.includes(':')) return
  const parts = full.split(':')
  const hg = parseInt(parts[0])
  const ag = parseInt(parts[1])
  if (isNaN(hg) || isNaN(ag)) return

  item.totalGoals = hg + ag
  if (hg > ag) {
    item.matchResult = 'HOME_WIN'
    item.resultOdds = item.homeWinOdds
  } else if (hg === ag) {
    item.matchResult = 'DRAW'
    item.resultOdds = 3.50
  } else {
    item.matchResult = 'AWAY_WIN'
    item.resultOdds = item.awayWinOdds
  }

  const hVal = item.handicapValue != null ? Number(item.handicapValue) : -1.0
  const diff = item.handicapSide === 'HOME' ? ((hg - ag) + hVal) : ((ag - hg) + hVal)
  if (diff > 0.001) {
    item.handicapResult = 'WIN'
    item.isBreak = true
  } else {
    item.handicapResult = diff === 0 ? 'DRAW' : 'LOSS'
    item.isBreak = false
  }

  const half = item.halfScore
  if (half && half.includes(':')) {
    const hParts = half.split(':')
    const hh = parseInt(hParts[0])
    const ha = parseInt(hParts[1])
    if (!isNaN(hh) && !isNaN(ha)) {
      const hRes = hh > ha ? '胜' : (hh === ha ? '平' : '负')
      const fRes = hg > ag ? '胜' : (hg === ag ? '平' : '负')
      item.halfFullResult = hRes + fRes
    }
  }
}

// ==================== 弹窗打开与保存 ====================
const openCreateModal = () => {
  isEditMode.value = false
  form.value = {
    id: null,
    matchDate: new Date().toISOString().split('T')[0],
    batchTitle: '',
    leagueName: '英超',
    matchSystem: 'LEAGUE',
    homeTeam: '',
    awayTeam: '',
    handicapSide: 'HOME',
    handicapValue: -1.0,
    homeWinOdds: 1.50,
    drawOdds: 3.80,
    awayWinOdds: 5.50,
    handicapOdds: 1.50,
    halfScore: '',
    fullScore: '',
    matchResult: 'HOME_WIN',
    resultOdds: 1.50,
    handicapResult: 'WIN',
    handicapResultOdds: 2.35,
    halfFullResult: '',
    totalGoals: null,
    isBreak: false,
    remarks: ''
  }
  deducedHint.value = ''
  matchModalVisible.value = true
}

const openEditModal = (m) => {
  isEditMode.value = true
  form.value = { ...m }
  if (!form.value.matchSystem) {
    form.value.matchSystem = 'LEAGUE'
  }
  triggerAutoDeduce()
  matchModalVisible.value = true
}

const saveMatchRecord = async () => {
  if (!form.value.matchDate || !form.value.homeTeam || !form.value.awayTeam) {
    ElMessage.warning('请完整填写比赛日期和主客队名称')
    return
  }
  try {
    if (isEditMode.value) {
      await api.updateHandicapMatchRecord(form.value.id, form.value)
      ElMessage.success('比赛记录更新成功！')
    } else {
      await api.createHandicapMatchRecord(form.value)
      ElMessage.success('单场比赛开奖记录录入成功！')
    }
    matchModalVisible.value = false
    fetchMatchList()
  } catch (err) {
    console.error('保存比赛失败:', err)
    ElMessage.error('保存失败: ' + (err.message || '系统异常'))
  }
}

const handleDeleteMatch = (m) => {
  ElMessageBox.confirm(`确认删除 ${m.matchDate} ${m.homeTeam} vs ${m.awayTeam} 的比赛记录吗？`, '删除确认', {
    confirmButtonText: '确定删除',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await api.deleteHandicapMatchRecord(m.id)
      ElMessage.success('删除成功')
      fetchMatchList()
    } catch (err) {
      ElMessage.error('删除失败: ' + err.message)
    }
  }).catch(() => {})
}

// 批量录入
const openBatchModal = () => {
  batchList.value = [
    {
      matchDate: new Date().toISOString().split('T')[0],
      matchSystem: 'LEAGUE',
      leagueName: '英超',
      homeTeam: '',
      awayTeam: '',
      homeWinOdds: 1.50,
      awayWinOdds: 5.50,
      handicapSide: 'HOME',
      handicapValue: -1.0,
      handicapOdds: 1.50,
      halfScore: '',
      fullScore: '',
      isBreak: false
    }
  ]
  batchModalVisible.value = true
}

const addBatchRow = () => {
  batchList.value.push({
    matchDate: new Date().toISOString().split('T')[0],
    matchSystem: 'LEAGUE',
    leagueName: '英超',
    homeTeam: '',
    awayTeam: '',
    homeWinOdds: 1.50,
    awayWinOdds: 5.50,
    handicapSide: 'HOME',
    handicapValue: -1.0,
    handicapOdds: 1.50,
    halfScore: '',
    fullScore: '',
    isBreak: false
  })
}

const removeBatchRow = (idx) => {
  batchList.value.splice(idx, 1)
}

const saveBatchRecords = async () => {
  const validList = batchList.value.filter(item => item.homeTeam && item.awayTeam)
  if (validList.length === 0) {
    ElMessage.warning('请至少填写一场有效的主客队比赛')
    return
  }
  try {
    await api.batchCreateHandicapMatchRecords(validList)
    ElMessage.success(`成功批量录入 ${validList.length} 场比赛！`)
    batchModalVisible.value = false
    fetchMatchList()
  } catch (err) {
    ElMessage.error('批量保存失败: ' + (err.message || '系统异常'))
  }
}

// ==================== 单场胜平负打出概率与期望 ROI 测算逻辑 ====================
const openSimulateModal = () => {
  simulateModalVisible.value = true
  runSimulation()
}

const applySimPreset = (p) => {
  simulateForm.value.matchSystem = p.system
  simulateForm.value.handicapSide = p.side
  simulateForm.value.homeWinOdds = p.home
  simulateForm.value.drawOdds = p.draw
  simulateForm.value.awayWinOdds = p.away
  simulateForm.value.tolerance = p.tol
  simulateForm.value.handicapValue = p.val
  runSimulation()
}

const computeLocalSimulation = () => {
  const f = simulateForm.value
  const targetOdds = f.handicapSide === 'HOME'
    ? Number(f.homeWinOdds || 1.50)
    : Number(f.awayWinOdds || 1.50)
  const tol = Number(f.tolerance || 0.10)
  const minOdds = targetOdds - tol
  const maxOdds = targetOdds + tol

  // 从 matches.value 匹配
  const matched = (matches.value || []).filter(m => {
    // 匹配让球方
    if (f.handicapSide && m.handicapSide !== f.handicapSide) {
      return false
    }
    // 匹配赛制
    if (f.matchSystem && f.matchSystem !== 'ALL' && m.matchSystem !== f.matchSystem) {
      return false
    }
    // 匹配赔率区间
    const o = Number(m.handicapOdds)
    if (isNaN(o) || o < minOdds - 0.0001 || o > maxOdds + 0.0001) {
      return false
    }
    return true
  })

  const sampleCount = matched.length
  if (sampleCount === 0) {
    simulateResult.value = {
      sampleCount: 0,
      oddsRangeDesc: `${minOdds.toFixed(2)} ~ ${maxOdds.toFixed(2)} (基准胜赔 ${targetOdds.toFixed(2)} ± ${tol.toFixed(2)})`,
      matchSystem: f.matchSystem,
      handicapSide: f.handicapSide,
      homeWinProb: 0,
      drawProb: 0,
      awayWinProb: 0,
      breakProb: 0,
      homeWinCount: 0,
      drawCount: 0,
      awayWinCount: 0,
      breakCount: 0,
      homeWinRoi: 0,
      drawRoi: 0,
      awayWinRoi: 0,
      bestChoice: 'NONE',
      bestChoiceName: '暂无样本',
      maxRoi: 0,
      advice: `当前赔率区间 [${minOdds.toFixed(2)} ~ ${maxOdds.toFixed(2)}] 库内暂无样本，建议调大步幅容差或录入历史比赛。`,
      goalsDistribution: {},
      halfFullDistribution: {},
      sampleMatches: []
    }
    return
  }

  let hw = 0
  let dr = 0
  let aw = 0
  let brk = 0
  const goalsMap = {}
  const halfFullMap = {}

  matched.forEach(m => {
    if (m.matchResult === 'HOME_WIN') hw++
    else if (m.matchResult === 'DRAW') dr++
    else if (m.matchResult === 'AWAY_WIN') aw++

    if (m.isBreak) brk++

    if (m.totalGoals !== null && m.totalGoals !== undefined) {
      const g = `${m.totalGoals}球`
      goalsMap[g] = (goalsMap[g] || 0) + 1
    }
    if (m.halfFullResult) {
      const hf = m.halfFullResult.trim()
      halfFullMap[hf] = (halfFullMap[hf] || 0) + 1
    }
  })

  const hwProb = Number(((hw / sampleCount) * 100).toFixed(2))
  const drProb = Number(((dr / sampleCount) * 100).toFixed(2))
  const awProb = Number(((aw / sampleCount) * 100).toFixed(2))
  const brkProb = Number(((brk / sampleCount) * 100).toFixed(2))

  const hOdds = Number(f.homeWinOdds || 1.50)
  const dOdds = Number(f.drawOdds || 3.50)
  const aOdds = Number(f.awayWinOdds || 5.00)

  // ROI = (P * odds - 1) * 100
  const hwRoi = Number((((hw / sampleCount) * hOdds - 1) * 100).toFixed(2))
  const drRoi = Number((((dr / sampleCount) * dOdds - 1) * 100).toFixed(2))
  const awRoi = Number((((aw / sampleCount) * aOdds - 1) * 100).toFixed(2))

  let bestChoice = 'HOME_WIN'
  let bestChoiceName = '主胜'
  let maxRoi = hwRoi

  if (drRoi > maxRoi) {
    maxRoi = drRoi
    bestChoice = 'DRAW'
    bestChoiceName = '平局'
  }
  if (awRoi > maxRoi) {
    maxRoi = awRoi
    bestChoice = 'AWAY_WIN'
    bestChoiceName = '客胜'
  }

  let advice = ''
  if (maxRoi > 0) {
    advice = `🔥 【高价值发现】历史样本中【${bestChoiceName}】期望回报率(ROI)为 +${maxRoi}%，存在超额正向数学期望（Value Bet）！`
  } else {
    advice = `⚠️ 【负期望规避】当前胜平负三项期望回报率均为负（受庄家抽水与实际胜率影响），其中【${bestChoiceName}】相对损耗最低 (ROI: ${maxRoi}%)。`
  }
  advice += ` 历史样本开奖分布：主胜 ${hwProb}%，平局 ${drProb}%，客胜 ${awProb}%。`
  if (brkProb >= 50) {
    advice += ` 穿盘率达 ${brkProb}%，让球方打穿能力显著，可优先参考让球胜盘口。`
  } else {
    advice += ` 穿盘率为 ${brkProb}%，卡盘/冷门下盘风险偏高，让球玩法宜谨慎防平/防下。`
  }

  simulateResult.value = {
    sampleCount,
    oddsRangeDesc: `${minOdds.toFixed(2)} ~ ${maxOdds.toFixed(2)} (基准胜赔 ${targetOdds.toFixed(2)} ± ${tol.toFixed(2)})`,
    homeWinProb: hwProb,
    drawProb: drProb,
    awayWinProb: awProb,
    breakProb: brkProb,
    homeWinCount: hw,
    drawCount: dr,
    awayWinCount: aw,
    breakCount: brk,
    homeWinRoi: hwRoi,
    drawRoi: drRoi,
    awayWinRoi: awRoi,
    bestChoice,
    bestChoiceName,
    maxRoi,
    advice,
    goalsDistribution: goalsMap,
    halfFullDistribution: halfFullMap,
    sampleMatches: matched.slice(0, 20).map(m => ({
      id: m.id,
      matchDate: m.matchDate,
      leagueName: m.leagueName,
      homeTeam: m.homeTeam,
      awayTeam: m.awayTeam,
      fullScore: m.fullScore,
      matchResult: m.matchResult,
      matchResultName: m.matchResult === 'HOME_WIN' ? '主胜' : (m.matchResult === 'DRAW' ? '平局' : '客胜'),
      handicapOdds: m.handicapOdds,
      isBreak: m.isBreak
    }))
  }
}

const runSimulation = async () => {
  // 优先执行本地计算，毫秒级即时响应
  computeLocalSimulation()

  // 随后调用后端接口
  simulateLoading.value = true
  try {
    const payload = {
      matchSystem: simulateForm.value.matchSystem,
      handicapSide: simulateForm.value.handicapSide,
      handicapValue: Number(simulateForm.value.handicapValue || -1.0),
      homeWinOdds: Number(simulateForm.value.homeWinOdds || 1.50),
      drawOdds: Number(simulateForm.value.drawOdds || 3.50),
      awayWinOdds: Number(simulateForm.value.awayWinOdds || 5.00),
      tolerance: Number(simulateForm.value.tolerance || 0.10)
    }
    const res = await api.simulateHandicapMatch(payload)
    if (res && res.data) {
      simulateResult.value = res.data
    }
  } catch (err) {
    console.warn('后端测算接口暂缓，已使用本地引擎即时计算:', err)
  } finally {
    simulateLoading.value = false
  }
}

// 窗口大小变化时自适应
const handleResize = () => {
  steppedChartInstance?.resize()
  goalsChartInstance?.resize()
}

onMounted(() => {
  fetchMatchList()
  window.addEventListener('resize', handleResize)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
  steppedChartInstance?.dispose()
  goalsChartInstance?.dispose()
})

// 监听4大维度变化自适应更新图表
watch([steppedBuckets, filteredMatches, sideFilter, systemFilter, timePreset, dateRange], () => {
  nextTick(() => {
    updateCharts()
  })
})
</script>

<style scoped>
.custom-input :deep(.el-input__wrapper),
.custom-select :deep(.el-select__wrapper) {
  background-color: #020617 !important;
  border: 1px solid #1e293b !important;
  box-shadow: none !important;
  color: #f8fafc !important;
}

.custom-input :deep(input) {
  color: #f8fafc !important;
}

:deep(.custom-datepicker) {
  background-color: #020617 !important;
  border: 1px solid #1e293b !important;
  box-shadow: none !important;
}

:deep(.el-dialog.custom-dark-dialog) {
  background-color: #090d16 !important;
  border: 1px solid #1e293b !important;
  border-radius: 1.5rem !important;
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.7) !important;
}

:deep(.el-dialog__title) {
  color: #f8fafc !important;
  font-weight: 800 !important;
}
</style>
