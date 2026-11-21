<template>
  <div class="space-y-6 sm:space-y-8">
    <!-- 1. 前端多维操控控制台 (联赛筛选 + 赛制类型 + 时间跨度 + 快捷操作) -->
    <div class="rounded-3xl bg-slate-900/90 border border-slate-800 p-4 sm:p-6 shadow-2xl relative overflow-hidden backdrop-blur-xl space-y-4">
      <div class="absolute -right-16 -top-16 w-64 h-64 bg-amber-500/10 rounded-full blur-3xl pointer-events-none"></div>

      <!-- 第一行：常用操作与测算按钮 -->
      <div class="flex flex-col lg:flex-row lg:items-center lg:justify-between gap-4 relative z-10 pb-3 border-b border-slate-800/80">
        <div>
          <div class="flex items-center gap-2">
            <span class="text-xs font-extrabold uppercase tracking-widest text-amber-400 bg-amber-500/15 px-2.5 py-0.5 rounded-full border border-amber-500/20">
              🎲 两极进球赔率引擎
            </span>
            <span class="text-xs text-slate-400">0球与7球+赔率对比 · 全场实际打出总进球数大数据分析</span>
          </div>
          <p class="text-[11px] text-slate-400 mt-1">
            专注研究 0球赔率（防守小球标杆）与 7球+赔率（进攻大球标杆）的比值与区间关联规律
          </p>
        </div>

        <!-- 快速操作按钮组 -->
        <div class="flex items-center gap-2.5 flex-wrap">
          <button
            @click="fetchData"
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
            <span>单场预测测算 (AI)</span>
          </button>

          <button
            @click="openBatchModal"
            class="px-3 py-2 rounded-xl text-xs font-semibold bg-indigo-600/80 hover:bg-indigo-500 text-white shadow-sm shadow-indigo-600/25 transition flex items-center gap-1.5"
          >
            <span>📑</span>
            <span>按天批量录入</span>
          </button>

          <button
            @click="openCreateModal"
            class="px-3.5 py-2 rounded-xl text-xs font-bold bg-gradient-to-r from-emerald-500 to-teal-500 hover:from-emerald-400 hover:to-teal-400 text-slate-950 shadow-lg shadow-emerald-500/25 transition flex items-center gap-1.5"
          >
            <span>➕</span>
            <span>录入单场数据</span>
          </button>
        </div>
      </div>

      <!-- 第二行：联赛分类筛选 + 赛制类型 + 时间预设 -->
      <div class="flex flex-wrap items-center justify-between gap-3 text-xs pt-1">
        <!-- 联赛选择器 -->
        <div class="flex items-center gap-1.5 flex-wrap">
          <span class="text-slate-400 font-semibold">主流联赛:</span>
          <div class="inline-flex p-0.5 rounded-xl bg-slate-950/80 border border-slate-800 flex-wrap">
            <button
              v-for="l in quickLeagueFilters"
              :key="l.value"
              @click="leagueFilter = l.value"
              class="px-2.5 py-1 rounded-lg transition text-xs font-medium"
              :class="[
                leagueFilter === l.value
                  ? 'bg-amber-500/20 text-amber-300 font-bold border border-amber-500/30'
                  : 'text-slate-400 hover:text-white'
              ]"
            >
              {{ l.label }}
            </button>
          </div>
          <!-- 自由选择其他更多联赛 -->
          <el-select
            v-model="leagueFilter"
            placeholder="更多联赛"
            size="small"
            filterable
            class="!w-32 custom-select"
          >
            <el-option label="全部联赛" value="ALL" />
            <el-option-group
              v-for="group in categorizedLeagues"
              :key="group.label"
              :label="group.label"
            >
              <el-option
                v-for="item in group.options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-option-group>
          </el-select>
        </div>

        <!-- 赛制类型 (全部 / 联赛 / 杯赛 / 友谊赛) -->
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

        <!-- 时间跨度预设 -->
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
                  ? 'bg-indigo-500/20 text-indigo-300 font-bold border border-indigo-500/30'
                  : 'text-slate-400 hover:text-white'
              ]"
            >
              {{ t.label }}
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- 2. 大数据核心指标卡片 (总场次、两极均赔、平均进球数、四大区间占比) -->
    <div class="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-6 gap-3 sm:gap-4">
      <div class="rounded-2xl bg-slate-900/80 border border-slate-800 p-3 sm:p-4 shadow-lg space-y-1">
        <span class="text-[11px] text-slate-400 font-medium block">样本总比赛场次</span>
        <div class="flex items-baseline gap-1">
          <span class="text-xl sm:text-2xl font-black font-mono text-white">{{ statsSummary.totalMatches || 0 }}</span>
          <span class="text-xs text-slate-400">场</span>
        </div>
        <span class="text-[10px] text-emerald-400 block font-mono">
          涵盖 {{ leagueCount }} 个主流联赛
        </span>
      </div>

      <div class="rounded-2xl bg-slate-900/80 border border-slate-800 p-3 sm:p-4 shadow-lg space-y-1">
        <span class="text-[11px] text-slate-400 font-medium block">0球 平均赔率</span>
        <div class="flex items-baseline gap-1">
          <span class="text-xl sm:text-2xl font-black font-mono text-cyan-400">{{ statsSummary.avgZeroGoalsOdds || '0.00' }}</span>
        </div>
        <span class="text-[10px] text-slate-500 block">防守与0-0预期基准</span>
      </div>

      <div class="rounded-2xl bg-slate-900/80 border border-slate-800 p-3 sm:p-4 shadow-lg space-y-1">
        <span class="text-[11px] text-slate-400 font-medium block">7球+ 平均赔率</span>
        <div class="flex items-baseline gap-1">
          <span class="text-xl sm:text-2xl font-black font-mono text-rose-400">{{ statsSummary.avgSevenGoalsOdds || '0.00' }}</span>
        </div>
        <span class="text-[10px] text-slate-500 block">大比分进攻预期基准</span>
      </div>

      <div class="rounded-2xl bg-slate-900/80 border border-slate-800 p-3 sm:p-4 shadow-lg space-y-1">
        <span class="text-[11px] text-slate-400 font-medium block">实际场均总进球</span>
        <div class="flex items-baseline gap-1">
          <span class="text-xl sm:text-2xl font-black font-mono text-amber-400">{{ statsSummary.avgTotalGoals || '0.00' }}</span>
          <span class="text-xs text-slate-400">球</span>
        </div>
        <span class="text-[10px] text-amber-300/80 block font-mono">
          赔率比率 (7+/0): {{ statsSummary.avgOddsRatio || '0.00' }}
        </span>
      </div>

      <div class="rounded-2xl bg-slate-900/80 border border-slate-800 p-3 sm:p-4 shadow-lg space-y-1">
        <span class="text-[11px] text-slate-400 font-medium block">0-1球 (小球率)</span>
        <div class="flex items-baseline gap-1">
          <span class="text-xl sm:text-2xl font-black font-mono text-sky-400">{{ statsSummary.zeroOneRate || '0.00' }}%</span>
        </div>
        <span class="text-[10px] text-slate-400 block font-mono">
          共 {{ statsSummary.zeroOneCount || 0 }} 场打出
        </span>
      </div>

      <div class="rounded-2xl bg-slate-900/80 border border-slate-800 p-3 sm:p-4 shadow-lg space-y-1">
        <span class="text-[11px] text-slate-400 font-medium block">2-3球 (黄金中球率)</span>
        <div class="flex items-baseline gap-1">
          <span class="text-xl sm:text-2xl font-black font-mono text-emerald-400">{{ statsSummary.twoThreeRate || '0.00' }}%</span>
        </div>
        <span class="text-[10px] text-slate-400 block font-mono">
          共 {{ statsSummary.twoThreeCount || 0 }} 场打出
        </span>
      </div>
    </div>

    <!-- 3. ECharts 大数据关联可视化图表 (双栏) -->
    <div class="grid grid-cols-1 lg:grid-cols-12 gap-5">
      <!-- 图表 1: 0球 vs 7+球赔率 气泡散点图 (展示赔率分布与实际打出进球关系) -->
      <div class="lg:col-span-7 rounded-3xl bg-slate-900/85 border border-slate-800 p-4 sm:p-6 shadow-xl space-y-3">
        <div class="flex items-center justify-between flex-wrap gap-2">
          <div class="flex items-center gap-2">
            <span class="text-xs font-bold text-cyan-400">📈 两极赔率 (0球 vs 7+球) 气泡分布规律</span>
            <span class="text-[10px] text-slate-500 font-mono">横轴: 0球赔率 · 纵轴: 7球+赔率 · 颜色: 实际进球数</span>
          </div>
          <span class="text-[11px] text-slate-400 font-mono">共 {{ filteredMatches.length }} 场样本</span>
        </div>

        <div ref="scatterChartRef" class="w-full h-80"></div>
      </div>

      <!-- 图表 2: 实际总进球数 (0~7+球) 各项分布直方柱状图 -->
      <div class="lg:col-span-5 rounded-3xl bg-slate-900/85 border border-slate-800 p-4 sm:p-6 shadow-xl space-y-3">
        <div class="flex items-center justify-between flex-wrap gap-2">
          <div class="flex items-center gap-2">
            <span class="text-xs font-bold text-amber-400">📊 实际开奖全场总进球数分布</span>
          </div>
          <span class="text-[11px] text-slate-400 font-mono">0球至7+各进球数频次</span>
        </div>

        <div ref="goalsBarChartRef" class="w-full h-80"></div>
      </div>
    </div>

    <!-- 4. 步幅统计引擎：步幅 1-10 自由调节 · 0球与7球赔率区间切分 · 明确显示各个区间 1~7球落点概率 -->
    <div class="rounded-3xl bg-slate-900/90 border border-slate-800 p-4 sm:p-6 shadow-2xl space-y-5 relative overflow-hidden backdrop-blur-xl">
      <div class="absolute -left-16 -top-16 w-64 h-64 bg-cyan-500/10 rounded-full blur-3xl pointer-events-none"></div>
      <div class="absolute -right-16 -bottom-16 w-64 h-64 bg-amber-500/10 rounded-full blur-3xl pointer-events-none"></div>

      <!-- 头部：标题与切分目标选择 -->
      <div class="flex flex-col lg:flex-row lg:items-center lg:justify-between gap-4 relative z-10 pb-4 border-b border-slate-800">
        <div>
          <div class="flex items-center gap-2">
            <span class="text-xs font-black uppercase tracking-wider text-cyan-400 bg-cyan-500/15 px-2.5 py-0.5 rounded-full border border-cyan-500/20">
              ⚡ 步幅切分与落点概率大盘
            </span>
            <span class="text-sm font-extrabold text-white">0球与7球赔率步幅(1-10)连续区间 · 1-7球落点全概率统计</span>
          </div>
          <p class="text-xs text-slate-400 mt-1">
            动态按步幅 (1~10) 将赔率划分为多段连续区间，精确测算并高亮每个区间内 0球、1球至7+球各大落点的概率占比与进球风格
          </p>
        </div>

        <!-- 切分目标 Tab -->
        <div class="flex items-center gap-2 flex-wrap">
          <span class="text-xs text-slate-400 font-semibold">切分目标:</span>
          <div class="inline-flex p-1 rounded-2xl bg-slate-950 border border-slate-800">
            <button
              @click="setSteppedTarget('zero')"
              class="px-3.5 py-1.5 rounded-xl text-xs font-bold transition flex items-center gap-1.5"
              :class="[
                steppedTarget === 'zero'
                  ? 'bg-gradient-to-r from-cyan-500 to-sky-600 text-slate-950 font-extrabold shadow-md shadow-cyan-500/20'
                  : 'text-slate-400 hover:text-white'
              ]"
            >
              <span>🎯 0球赔率</span>
              <span class="text-[10px] opacity-75 font-mono">(标杆防守盘)</span>
            </button>
            <button
              @click="setSteppedTarget('seven')"
              class="px-3.5 py-1.5 rounded-xl text-xs font-bold transition flex items-center gap-1.5"
              :class="[
                steppedTarget === 'seven'
                  ? 'bg-gradient-to-r from-rose-500 to-pink-600 text-white font-extrabold shadow-md shadow-rose-500/20'
                  : 'text-slate-400 hover:text-white'
              ]"
            >
              <span>🚀 7球+赔率</span>
              <span class="text-[10px] opacity-75 font-mono">(大球对攻盘)</span>
            </button>
            <button
              @click="setSteppedTarget('ratio')"
              class="px-3.5 py-1.5 rounded-xl text-xs font-bold transition flex items-center gap-1.5"
              :class="[
                steppedTarget === 'ratio'
                  ? 'bg-gradient-to-r from-amber-500 to-orange-500 text-slate-950 font-extrabold shadow-md shadow-amber-500/20'
                  : 'text-slate-400 hover:text-white'
              ]"
            >
              <span>⚖️ 两极比值</span>
              <span class="text-[10px] opacity-75 font-mono">(7+/0 比率)</span>
            </button>
          </div>
        </div>
      </div>

      <!-- 步幅操控条：1-10 按钮与滑块双重调节 -->
      <div class="bg-slate-950/70 border border-slate-800/90 rounded-2xl p-4 space-y-3 relative z-10">
        <div class="flex flex-col md:flex-row md:items-center md:justify-between gap-3">
          <!-- 步幅调节按钮 1-10 -->
          <div class="flex items-center gap-2 flex-wrap">
            <div class="flex items-center gap-1 text-xs font-bold text-amber-300">
              <span>📏 调节步幅:</span>
              <span class="px-2 py-0.5 rounded bg-amber-500/20 border border-amber-500/30 font-mono text-sm font-black text-amber-300">
                {{ steppedStep }}
              </span>
            </div>

            <!-- 1 到 10 快捷按钮组 -->
            <div class="inline-flex p-1 rounded-xl bg-slate-900 border border-slate-800 gap-1 flex-wrap">
              <button
                v-for="st in [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]"
                :key="st"
                @click="steppedStep = st"
                class="w-7 h-7 sm:w-8 sm:h-8 rounded-lg text-xs font-extrabold transition flex items-center justify-center font-mono"
                :class="[
                  steppedStep === st
                    ? 'bg-amber-400 text-slate-950 shadow-md shadow-amber-500/30 scale-105'
                    : 'text-slate-400 hover:text-white hover:bg-slate-800'
                ]"
              >
                {{ st }}
              </button>
            </div>
          </div>

          <!-- 右侧视图切换与筛选开关 -->
          <div class="flex items-center gap-3 text-xs">
            <label class="flex items-center gap-1.5 cursor-pointer text-slate-400 hover:text-slate-200">
              <input
                type="checkbox"
                v-model="onlyShowSampleBuckets"
                class="rounded bg-slate-900 border-slate-700 text-amber-500 focus:ring-0"
              />
              <span>仅显示有样本区间</span>
            </label>

            <!-- 视图模式: 表格 / 柱状图 / 走势图 -->
            <div class="inline-flex p-0.5 rounded-xl bg-slate-900 border border-slate-800">
              <button
                @click="steppedViewMode = 'table'"
                class="px-2.5 py-1 rounded-lg font-bold transition flex items-center gap-1"
                :class="[
                  steppedViewMode === 'table'
                    ? 'bg-slate-800 text-cyan-300'
                    : 'text-slate-400 hover:text-white'
                ]"
              >
                <span>📋</span>
                <span>全景概率表</span>
              </button>
              <button
                @click="steppedViewMode = 'chart'"
                class="px-2.5 py-1 rounded-lg font-bold transition flex items-center gap-1"
                :class="[
                  steppedViewMode === 'chart'
                    ? 'bg-slate-800 text-cyan-300'
                    : 'text-slate-400 hover:text-white'
                ]"
              >
                <span>📊</span>
                <span>堆叠概率柱图</span>
              </button>
              <button
                @click="steppedViewMode = 'trend'"
                class="px-2.5 py-1 rounded-lg font-bold transition flex items-center gap-1"
                :class="[
                  steppedViewMode === 'trend'
                    ? 'bg-slate-800 text-cyan-300'
                    : 'text-slate-400 hover:text-white'
                ]"
              >
                <span>📈</span>
                <span>落点走势图</span>
              </button>
            </div>
          </div>
        </div>

        <!-- 滑块控制条 -->
        <div class="flex items-center gap-4 pt-1">
          <span class="text-[11px] text-slate-400 whitespace-nowrap">步幅滑动微调 (1~10):</span>
          <el-slider
            v-model="steppedStep"
            :min="1"
            :max="10"
            :step="1"
            :marks="{ 1: '1', 2: '2', 3: '3', 5: '5', 8: '8', 10: '10' }"
            class="custom-stepped-slider flex-1"
          />
          <span class="text-xs text-slate-500 font-mono hidden sm:inline">
            当前切分为 {{ steppedBuckets.length }} 个赔率区间
          </span>
        </div>
      </div>

      <!-- 内容区 1: 全景落点概率大盘表格 (Table View) -->
      <div v-show="steppedViewMode === 'table'" class="space-y-4">
        <div class="overflow-x-auto rounded-2xl border border-slate-800 bg-slate-950/60">
          <table class="w-full text-left text-xs whitespace-nowrap">
            <thead class="text-[11px] text-slate-400 bg-slate-900/90 border-b border-slate-800">
              <tr>
                <th class="py-3 px-3.5 font-bold">赔率步幅区间</th>
                <th class="py-3 px-3 font-bold text-center">样本场次</th>
                <th class="py-3 px-3 font-bold text-center">场均进球</th>
                <th class="py-3 px-3 font-bold text-center">最高落点 (众数)</th>
                <!-- 0球至7+球 各自概率列 -->
                <th class="py-3 px-2.5 font-bold text-center bg-slate-800/40 text-slate-300">0球</th>
                <th class="py-3 px-2.5 font-bold text-center bg-sky-950/30 text-sky-300">1球</th>
                <th class="py-3 px-2.5 font-bold text-center bg-emerald-950/30 text-emerald-300">2球</th>
                <th class="py-3 px-2.5 font-bold text-center bg-teal-950/30 text-teal-300">3球</th>
                <th class="py-3 px-2.5 font-bold text-center bg-amber-950/30 text-amber-300">4球</th>
                <th class="py-3 px-2.5 font-bold text-center bg-orange-950/30 text-orange-300">5球</th>
                <th class="py-3 px-2.5 font-bold text-center bg-rose-950/30 text-rose-300">6球</th>
                <th class="py-3 px-2.5 font-bold text-center bg-purple-950/30 text-purple-300">7+球</th>
                <!-- 综合条与风格 -->
                <th class="py-3 px-3.5 font-bold text-center">1~7球落点全景分布</th>
                <th class="py-3 px-3 font-bold text-center">区间进球偏向</th>
                <th class="py-3 px-3 font-bold text-center">明细</th>
              </tr>
            </thead>
            <tbody class="divide-y divide-slate-800/80 font-mono">
              <template v-for="b in steppedBuckets" :key="b.key">
                <tr class="hover:bg-slate-900/60 transition group cursor-pointer" @click="toggleBucketExpand(b.key)">
                  <!-- 赔率区间 -->
                  <td class="py-3 px-3.5 font-black text-white flex items-center gap-1.5">
                    <span class="w-1.5 h-1.5 rounded-full" :class="b.count > 0 ? 'bg-cyan-400' : 'bg-slate-600'"></span>
                    <span class="text-xs">{{ b.label }}</span>
                  </td>

                  <!-- 样本场次 -->
                  <td class="py-3 px-3 text-center">
                    <span class="text-amber-300 font-bold">{{ b.count }} 场</span>
                    <span class="text-[10px] text-slate-500 ml-1">({{ b.countRate }}%)</span>
                  </td>

                  <!-- 场均进球 -->
                  <td class="py-3 px-3 text-center">
                    <span class="font-extrabold" :class="Number(b.avgGoals) >= 3 ? 'text-rose-400' : Number(b.avgGoals) <= 2 ? 'text-sky-400' : 'text-emerald-400'">
                      {{ b.avgGoals }} 球
                    </span>
                  </td>

                  <!-- 最高落点 (众数) -->
                  <td class="py-3 px-3 text-center">
                    <span class="inline-flex items-center gap-1 px-2 py-0.5 rounded-md bg-amber-500/15 border border-amber-500/30 text-amber-300 text-xs font-black">
                      <span>👑</span>
                      <span>{{ b.dominant.goal }}</span>
                      <span class="text-[10px] opacity-80 font-normal">({{ b.dominant.rate }}%)</span>
                    </span>
                  </td>

                  <!-- 各球落点单元格 0~7 -->
                  <!-- 0球 -->
                  <td class="py-2 px-2 text-center" :class="getGoalCellClass(b, 0)">
                    <div class="text-xs font-black">{{ b.goalRates[0] }}%</div>
                    <div class="text-[10px] text-slate-500 font-normal">{{ b.goalCounts[0] }}次</div>
                  </td>
                  <!-- 1球 -->
                  <td class="py-2 px-2 text-center" :class="getGoalCellClass(b, 1)">
                    <div class="text-xs font-black">{{ b.goalRates[1] }}%</div>
                    <div class="text-[10px] text-slate-500 font-normal">{{ b.goalCounts[1] }}次</div>
                  </td>
                  <!-- 2球 -->
                  <td class="py-2 px-2 text-center" :class="getGoalCellClass(b, 2)">
                    <div class="text-xs font-black">{{ b.goalRates[2] }}%</div>
                    <div class="text-[10px] text-slate-500 font-normal">{{ b.goalCounts[2] }}次</div>
                  </td>
                  <!-- 3球 -->
                  <td class="py-2 px-2 text-center" :class="getGoalCellClass(b, 3)">
                    <div class="text-xs font-black">{{ b.goalRates[3] }}%</div>
                    <div class="text-[10px] text-slate-500 font-normal">{{ b.goalCounts[3] }}次</div>
                  </td>
                  <!-- 4球 -->
                  <td class="py-2 px-2 text-center" :class="getGoalCellClass(b, 4)">
                    <div class="text-xs font-black">{{ b.goalRates[4] }}%</div>
                    <div class="text-[10px] text-slate-500 font-normal">{{ b.goalCounts[4] }}次</div>
                  </td>
                  <!-- 5球 -->
                  <td class="py-2 px-2 text-center" :class="getGoalCellClass(b, 5)">
                    <div class="text-xs font-black">{{ b.goalRates[5] }}%</div>
                    <div class="text-[10px] text-slate-500 font-normal">{{ b.goalCounts[5] }}次</div>
                  </td>
                  <!-- 6球 -->
                  <td class="py-2 px-2 text-center" :class="getGoalCellClass(b, 6)">
                    <div class="text-xs font-black">{{ b.goalRates[6] }}%</div>
                    <div class="text-[10px] text-slate-500 font-normal">{{ b.goalCounts[6] }}次</div>
                  </td>
                  <!-- 7+球 -->
                  <td class="py-2 px-2 text-center" :class="getGoalCellClass(b, 7)">
                    <div class="text-xs font-black">{{ b.goalRates[7] }}%</div>
                    <div class="text-[10px] text-slate-500 font-normal">{{ b.goalCounts[7] }}次</div>
                  </td>

                  <!-- 综合进度条 -->
                  <td class="py-3 px-3.5 min-w-[140px]">
                    <div class="w-full h-2 rounded-full bg-slate-900 overflow-hidden flex" :title="`0球:${b.goalRates[0]}% 1球:${b.goalRates[1]}% 2球:${b.goalRates[2]}% 3球:${b.goalRates[3]}% 4球:${b.goalRates[4]}% 5球:${b.goalRates[5]}% 6球:${b.goalRates[6]}% 7+球:${b.goalRates[7]}%`">
                      <div class="h-full bg-slate-500" :style="{ width: `${b.goalRates[0]}%` }"></div>
                      <div class="h-full bg-sky-400" :style="{ width: `${b.goalRates[1]}%` }"></div>
                      <div class="h-full bg-emerald-400" :style="{ width: `${b.goalRates[2]}%` }"></div>
                      <div class="h-full bg-teal-400" :style="{ width: `${b.goalRates[3]}%` }"></div>
                      <div class="h-full bg-amber-400" :style="{ width: `${b.goalRates[4]}%` }"></div>
                      <div class="h-full bg-orange-400" :style="{ width: `${b.goalRates[5]}%` }"></div>
                      <div class="h-full bg-rose-400" :style="{ width: `${b.goalRates[6]}%` }"></div>
                      <div class="h-full bg-purple-500" :style="{ width: `${b.goalRates[7]}%` }"></div>
                    </div>
                  </td>

                  <!-- 进球风格 -->
                  <td class="py-3 px-3 text-center">
                    <span class="px-2 py-0.5 rounded text-[10px] font-bold border" :class="b.styleColor">
                      {{ b.styleTag }}
                    </span>
                  </td>

                  <!-- 展开/折叠按钮 -->
                  <td class="py-3 px-3 text-center text-slate-400">
                    <button class="px-2 py-0.5 rounded hover:bg-slate-800 text-[11px] font-sans flex items-center gap-1 mx-auto">
                      <span>{{ expandedBucketKey === b.key ? '收起' : '查看' }}</span>
                      <span class="text-[9px]">{{ expandedBucketKey === b.key ? '▲' : '▼' }}</span>
                    </button>
                  </td>
                </tr>

                <!-- 展开的明细子表格 -->
                <tr v-if="expandedBucketKey === b.key" class="bg-slate-950/95 border-b border-slate-800/80">
                  <td colspan="15" class="p-4">
                    <div class="rounded-xl border border-slate-800 bg-slate-900/60 p-3 space-y-2">
                      <div class="flex items-center justify-between text-xs font-bold text-slate-300">
                        <span>🎯 区间「{{ b.label }}」包含的所有样本比赛 (共 {{ b.records.length }} 场)</span>
                        <span class="text-[11px] text-slate-500 font-normal">点击任意比赛可在下方表格或弹窗中复盘</span>
                      </div>
                      <div class="max-h-60 overflow-y-auto space-y-1 text-xs">
                        <div
                          v-for="m in b.records"
                          :key="m.id"
                          class="flex items-center justify-between p-2 rounded-lg bg-slate-950/80 border border-slate-800/80 text-[11px] font-mono hover:border-slate-700"
                        >
                          <div class="flex items-center gap-3">
                            <span class="text-slate-400">{{ m.matchDate }}</span>
                            <span class="px-2 py-0.5 rounded bg-slate-900 text-amber-300 font-sans font-bold border border-slate-800">
                              {{ m.leagueName }}
                            </span>
                            <span class="text-slate-300 font-sans">
                              {{ m.homeTeam || '主队' }} <span class="text-slate-500">vs</span> {{ m.awayTeam || '客队' }}
                            </span>
                            <span v-if="m.fullScore" class="px-1.5 py-0.2 rounded bg-slate-800 text-slate-200">
                              {{ m.fullScore }}
                            </span>
                          </div>
                          <div class="flex items-center gap-4">
                            <span class="text-slate-400">0球赔: <b class="text-cyan-300">{{ Number(m.zeroGoalsOdds).toFixed(2) }}</b></span>
                            <span class="text-slate-400">7球赔: <b class="text-rose-300">{{ Number(m.sevenGoalsOdds).toFixed(2) }}</b></span>
                            <span class="text-slate-400">比值: <b class="text-amber-300">{{ Number(m.oddsRatio || 0).toFixed(2) }}</b></span>
                            <span class="px-2 py-0.5 rounded text-slate-950 font-black" :class="Number(m.totalGoals) >= 7 ? 'bg-purple-400' : Number(m.totalGoals) >= 4 ? 'bg-amber-400' : Number(m.totalGoals) >= 2 ? 'bg-emerald-400' : 'bg-sky-400'">
                              开出: {{ m.totalGoals >= 7 ? '7+球' : `${m.totalGoals}球` }}
                            </span>
                          </div>
                        </div>
                      </div>
                    </div>
                  </td>
                </tr>
              </template>
            </tbody>
          </table>
        </div>
      </div>

      <!-- 内容区 2: ECharts 堆叠概率柱图 (Chart View) -->
      <div v-show="steppedViewMode === 'chart'" class="space-y-3">
        <div class="flex items-center justify-between text-xs text-slate-400">
          <span>📊 各步幅区间 0~7+ 球落点概率堆叠柱状图 (总和 100%)</span>
          <span class="text-[11px] font-mono text-slate-500">悬停查看每个区间各球数的详细打出概率</span>
        </div>
        <div ref="steppedBarChartRef" class="w-full h-80 rounded-2xl bg-slate-950/70 border border-slate-800 p-2"></div>
      </div>

      <!-- 内容区 3: ECharts 各球数走势折线图 (Trend View) -->
      <div v-show="steppedViewMode === 'trend'" class="space-y-3">
        <div class="flex items-center justify-between text-xs text-slate-400">
          <span>📈 随赔率步幅梯级递增，1~7球落点概率的漂移与转化走势</span>
          <span class="text-[11px] font-mono text-slate-500">直观洞察高发落点球数随赔率变化的演化轨迹</span>
        </div>
        <div ref="steppedTrendChartRef" class="w-full h-80 rounded-2xl bg-slate-950/70 border border-slate-800 p-2"></div>
      </div>
    </div>

    <!-- 5. 深度规律：0球分档 / 7球分档 / 赔率比值规律 (三 Tab 切换) -->
    <div class="rounded-3xl bg-slate-900/90 border border-slate-800 p-4 sm:p-6 shadow-2xl space-y-4">
      <div class="flex items-center justify-between flex-wrap gap-3 pb-2 border-b border-slate-800">
        <div class="flex items-center gap-2">
          <span class="text-sm font-extrabold text-white">🔬 两极赔率区间分档深度规律分析</span>
          <span class="text-xs text-slate-400 hidden sm:inline">观察不同赔率梯度下实际进球特征与众数趋势</span>
        </div>

        <div class="inline-flex p-1 rounded-xl bg-slate-950 border border-slate-800">
          <button
            @click="activeTierTab = 'zero'"
            class="px-3 py-1.5 rounded-lg text-xs font-bold transition"
            :class="[
              activeTierTab === 'zero'
                ? 'bg-cyan-500/20 text-cyan-300 border border-cyan-500/30'
                : 'text-slate-400 hover:text-white'
            ]"
          >
            🎯 0球赔率分档
          </button>
          <button
            @click="activeTierTab = 'seven'"
            class="px-3 py-1.5 rounded-lg text-xs font-bold transition"
            :class="[
              activeTierTab === 'seven'
                ? 'bg-rose-500/20 text-rose-300 border border-rose-500/30'
                : 'text-slate-400 hover:text-white'
            ]"
          >
            🚀 7球+赔率分档
          </button>
          <button
            @click="activeTierTab = 'ratio'"
            class="px-3 py-1.5 rounded-lg text-xs font-bold transition"
            :class="[
              activeTierTab === 'ratio'
                ? 'bg-amber-500/20 text-amber-300 border border-amber-500/30'
                : 'text-slate-400 hover:text-white'
            ]"
          >
            ⚖️ 两极赔率比值 (7+/0)
          </button>
        </div>
      </div>

      <!-- 分档卡片大盘 -->
      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-5 gap-3.5">
        <div
          v-for="item in currentTierList"
          :key="item.tierName"
          class="rounded-2xl bg-slate-950/80 border border-slate-800/90 p-4 space-y-2.5 hover:border-slate-700 transition"
        >
          <div class="flex items-center justify-between">
            <span class="text-xs font-extrabold text-white">{{ item.tierName }}</span>
            <span class="text-[10px] font-mono text-slate-400">{{ item.tierRange }}</span>
          </div>

          <div class="flex items-baseline justify-between pt-1 border-t border-slate-900">
            <span class="text-[11px] text-slate-400">样本场次:</span>
            <span class="text-sm font-black font-mono text-amber-300">
              {{ item.matchCount }} 场 <span class="text-[10px] text-slate-500 font-normal">({{ item.countRate }}%)</span>
            </span>
          </div>

          <div class="flex items-baseline justify-between">
            <span class="text-[11px] text-slate-400">场均进球数:</span>
            <span class="text-sm font-black font-mono text-emerald-400">{{ item.avgGoals }} 球</span>
          </div>

          <div class="flex items-baseline justify-between">
            <span class="text-[11px] text-slate-400">最常打出进球 (众数):</span>
            <span class="text-xs font-black font-mono px-2 py-0.5 rounded bg-slate-900 text-cyan-300 border border-slate-800">
              {{ item.dominantGoals }}
            </span>
          </div>

          <!-- 各进球区间比例柱条 -->
          <div class="space-y-1 pt-1.5 text-[10px] font-mono">
            <div class="flex justify-between text-slate-400">
              <span>0-1球: {{ item.zeroOneRate }}%</span>
              <span>2-3球: {{ item.twoThreeRate }}%</span>
              <span>4+球: {{ item.fourPlusRate }}%</span>
            </div>
            <div class="w-full h-1.5 rounded-full bg-slate-900 overflow-hidden flex">
              <div class="h-full bg-sky-400" :style="{ width: `${item.zeroOneRate}%` }" title="0-1球"></div>
              <div class="h-full bg-emerald-400" :style="{ width: `${item.twoThreeRate}%` }" title="2-3球"></div>
              <div class="h-full bg-rose-400" :style="{ width: `${item.fourPlusRate}%` }" title="4+球"></div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 5. 主流联赛进球风格排行榜 (日职、韩职、澳超、巴甲、荷甲等进球特征) -->
    <div class="rounded-3xl bg-slate-900/90 border border-slate-800 p-4 sm:p-6 shadow-2xl space-y-4">
      <div class="flex items-center justify-between flex-wrap gap-2 pb-2 border-b border-slate-800">
        <div>
          <span class="text-sm font-extrabold text-white">🏆 主流联赛进球风格特征排行榜</span>
          <p class="text-xs text-slate-400 mt-0.5">对比日职联、韩职K联、澳超、巴甲、葡超、挪超、荷甲、五大联赛等进球风格与两极赔率偏好</p>
        </div>
        <span class="text-xs text-slate-500 font-mono">基于库内当前筛选数据实时汇聚</span>
      </div>

      <div class="overflow-x-auto">
        <table class="w-full text-left text-xs">
          <thead class="text-[11px] text-slate-400 border-b border-slate-800">
            <tr>
              <th class="py-2.5 px-3">联赛名称</th>
              <th class="py-2.5 px-3">样本场次</th>
              <th class="py-2.5 px-3">场均进球数</th>
              <th class="py-2.5 px-3">0球均赔</th>
              <th class="py-2.5 px-3">7+均赔</th>
              <th class="py-2.5 px-3">0-1球 (小球率)</th>
              <th class="py-2.5 px-3">2-3球 (黄金区)</th>
              <th class="py-2.5 px-3">4+球 (大球率)</th>
              <th class="py-2.5 px-3 text-center">风格标签</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-slate-800/60 font-mono text-slate-300">
            <tr
              v-for="rank in statsSummary.leagueRankings || []"
              :key="rank.leagueName"
              class="hover:bg-slate-850/60 transition"
            >
              <td class="py-3 px-3 font-sans font-bold text-white flex items-center gap-1.5">
                <span>⚽</span>
                <span>{{ rank.leagueName }}</span>
              </td>
              <td class="py-3 px-3">{{ rank.matchCount }} 场</td>
              <td class="py-3 px-3 font-black" :class="rank.avgGoals >= 3.0 ? 'text-amber-400' : 'text-emerald-400'">
                {{ rank.avgGoals }} 球
              </td>
              <td class="py-3 px-3 text-cyan-300">{{ rank.avgZeroOdds }}</td>
              <td class="py-3 px-3 text-rose-300">{{ rank.avgSevenOdds }}</td>
              <td class="py-3 px-3 text-sky-400">{{ rank.zeroOneRate }}%</td>
              <td class="py-3 px-3 text-emerald-400 font-bold">{{ rank.twoThreeRate }}%</td>
              <td class="py-3 px-3 text-rose-400">{{ rank.fourPlusRate }}%</td>
              <td class="py-3 px-3 text-center">
                <span class="px-2 py-0.5 rounded-full text-[10px] font-sans font-bold bg-slate-800 text-slate-200 border border-slate-700">
                  {{ rank.styleTag }}
                </span>
              </td>
            </tr>
            <tr v-if="!statsSummary.leagueRankings || statsSummary.leagueRankings.length === 0">
              <td colspan="9" class="py-6 text-center text-slate-500 font-sans">
                暂无联赛统计样本
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- 6. 比赛明细大盘表格与分页 (支持多选批量删除) -->
    <div class="rounded-3xl bg-slate-900/90 border border-slate-800 p-4 sm:p-6 shadow-2xl space-y-4">
      <div class="flex items-center justify-between flex-wrap gap-3 pb-2 border-b border-slate-800">
        <div class="flex items-center gap-2">
          <span class="text-sm font-extrabold text-white">📋 单场两极赔率与开奖明细列表</span>
          <span class="text-xs text-slate-400">共 {{ filteredMatches.length }} 场</span>
        </div>

        <div class="flex items-center gap-2">
          <button
            v-if="selectedIds.length > 0"
            @click="batchDeleteRecords"
            class="px-3 py-1.5 rounded-xl text-xs font-bold bg-rose-600 hover:bg-rose-500 text-white shadow transition flex items-center gap-1"
          >
            <span>🗑️ 批量删除 ({{ selectedIds.length }})</span>
          </button>
        </div>
      </div>

      <div class="overflow-x-auto">
        <table class="w-full text-left text-xs">
          <thead class="text-[11px] text-slate-400 border-b border-slate-800">
            <tr>
              <th class="py-2.5 px-3 w-8">
                <input
                  type="checkbox"
                  :checked="isAllSelected"
                  @change="toggleSelectAll"
                  class="rounded bg-slate-900 border-slate-700"
                />
              </th>
              <th class="py-2.5 px-3">日期</th>
              <th class="py-2.5 px-3">联赛 / 赛制</th>
              <th class="py-2.5 px-3">对阵球队</th>
              <th class="py-2.5 px-3">0球赔率</th>
              <th class="py-2.5 px-3">7球+赔率</th>
              <th class="py-2.5 px-3">两极比值 (7+/0)</th>
              <th class="py-2.5 px-3">总进球数</th>
              <th class="py-2.5 px-3">全场比分</th>
              <th class="py-2.5 px-3">进球形态</th>
              <th class="py-2.5 px-3">备注</th>
              <th class="py-2.5 px-3 text-right">操作</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-slate-800/60 text-slate-300">
            <tr
              v-for="row in pagedMatches"
              :key="row.id"
              class="hover:bg-slate-850/60 transition"
            >
              <td class="py-3 px-3">
                <input
                  type="checkbox"
                  :value="row.id"
                  v-model="selectedIds"
                  class="rounded bg-slate-900 border-slate-700"
                />
              </td>
              <td class="py-3 px-3 font-mono text-slate-400">{{ row.matchDate }}</td>
              <td class="py-3 px-3">
                <span class="font-bold text-white mr-1.5">{{ row.leagueName }}</span>
                <span class="text-[10px] px-1.5 py-0.5 rounded bg-slate-800 text-slate-400 font-mono">
                  {{ row.matchSystem === 'CUP' ? '杯赛' : (row.matchSystem === 'FRIENDLY' ? '友谊' : '联赛') }}
                </span>
              </td>
              <td class="py-3 px-3 font-semibold text-slate-200">
                <span v-if="row.homeTeam || row.awayTeam">
                  {{ row.homeTeam || '主队' }} vs {{ row.awayTeam || '客队' }}
                </span>
                <span v-else class="text-slate-500 font-normal">--</span>
              </td>
              <td class="py-3 px-3 font-mono font-bold text-cyan-300">{{ row.zeroGoalsOdds }}</td>
              <td class="py-3 px-3 font-mono font-bold text-rose-300">{{ row.sevenGoalsOdds }}</td>
              <td class="py-3 px-3 font-mono text-amber-300">{{ row.oddsRatio || (row.sevenGoalsOdds && row.zeroGoalsOdds ? (row.sevenGoalsOdds / row.zeroGoalsOdds).toFixed(2) : '--') }}</td>
              <td class="py-3 px-3 font-mono font-black text-amber-400 text-sm">
                {{ row.totalGoals >= 7 ? '7+球' : row.totalGoals + '球' }}
              </td>
              <td class="py-3 px-3 font-mono text-slate-400">{{ row.fullScore || '--' }}</td>
              <td class="py-3 px-3">
                <span
                  class="px-2 py-0.5 rounded-full text-[10px] font-bold"
                  :class="[
                    row.totalGoals <= 1 ? 'bg-sky-500/15 text-sky-400 border border-sky-500/30' :
                    (row.totalGoals <= 3 ? 'bg-emerald-500/15 text-emerald-400 border border-emerald-500/30' :
                    (row.totalGoals <= 6 ? 'bg-rose-500/15 text-rose-400 border border-rose-500/30' : 'bg-purple-500/15 text-purple-400 border border-purple-500/30'))
                  ]"
                >
                  {{ row.totalGoals <= 1 ? '0-1小球' : (row.totalGoals <= 3 ? '2-3黄金区' : (row.totalGoals <= 6 ? '4-6大球' : '7+超大球')) }}
                </span>
              </td>
              <td class="py-3 px-3 text-slate-400 max-w-xs truncate" :title="row.remarks">{{ row.remarks || '--' }}</td>
              <td class="py-3 px-3 text-right space-x-2">
                <button
                  @click="openEditModal(row)"
                  class="text-xs text-cyan-400 hover:text-cyan-300 transition"
                >
                  编辑
                </button>
                <button
                  @click="deleteRecord(row.id)"
                  class="text-xs text-rose-400 hover:text-rose-300 transition"
                >
                  删除
                </button>
              </td>
            </tr>
            <tr v-if="pagedMatches.length === 0">
              <td colspan="12" class="py-8 text-center text-slate-500">
                暂无符合筛选条件的两极进球赔率比赛数据，请点击上方「录入单场数据」或「按天批量录入」
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- 分页控制 -->
      <div class="flex items-center justify-between flex-wrap gap-3 pt-3 border-t border-slate-800 text-xs">
        <span class="text-slate-400">
          显示第 {{ (currentPage - 1) * pageSize + 1 }} 至 {{ Math.min(currentPage * pageSize, filteredMatches.length) }} 条，共 {{ filteredMatches.length }} 条
        </span>
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[15, 30, 50, 100]"
          layout="sizes, prev, pager, next"
          :total="filteredMatches.length"
          class="custom-pagination"
        />
      </div>
    </div>

    <!-- ==================== 弹窗 1: 单条比赛录入/修改弹窗 ==================== -->
    <el-dialog
      v-model="recordModalVisible"
      :title="isEditMode ? '✏️ 修改单场两极进球赔率数据' : '➕ 录入单场两极进球赔率数据'"
      width="90%"
      class="custom-dark-dialog max-w-2xl"
      destroy-on-close
    >
      <div class="space-y-4 max-h-[75vh] overflow-y-auto pr-1 text-slate-200">
        <div class="p-3.5 rounded-2xl bg-slate-950/70 border border-slate-800 space-y-3">
          <div class="text-xs font-bold text-amber-300 flex items-center gap-1.5">
            <span>⚽ 比赛与联赛设置</span>
          </div>
          <div class="grid grid-cols-1 sm:grid-cols-3 gap-3">
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
              <label class="block text-[11px] text-slate-400 mb-1">联赛名称 *</label>
              <el-select
                v-model="form.leagueName"
                placeholder="选择或输入联赛"
                filterable
                allow-create
                default-first-option
                class="!w-full custom-select"
              >
                <el-option-group
                  v-for="group in categorizedLeagues"
                  :key="group.label"
                  :label="group.label"
                >
                  <el-option
                    v-for="item in group.options"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-option-group>
              </el-select>
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
          </div>

          <div class="grid grid-cols-1 sm:grid-cols-2 gap-3">
            <div>
              <label class="block text-[11px] text-slate-400 mb-1">主队名称 (可选)</label>
              <el-input v-model="form.homeTeam" placeholder="如: 神户胜利船" class="custom-input" />
            </div>
            <div>
              <label class="block text-[11px] text-slate-400 mb-1">客队名称 (可选)</label>
              <el-input v-model="form.awayTeam" placeholder="如: 横滨水手" class="custom-input" />
            </div>
          </div>
        </div>

        <!-- 赔率与总进球核心区域 -->
        <div class="p-3.5 rounded-2xl bg-slate-950/70 border border-slate-800 space-y-3">
          <div class="text-xs font-bold text-cyan-400 flex items-center justify-between">
            <span>🎲 两极赔率与开奖总进球数</span>
            <span class="text-[10px] text-slate-400">0球防守赔率 vs 7+进攻大球赔率</span>
          </div>

          <div class="grid grid-cols-1 sm:grid-cols-3 gap-3">
            <div>
              <label class="block text-[11px] text-cyan-300 font-bold mb-1">0球 打出赔率 *</label>
              <el-input-number
                v-model="form.zeroGoalsOdds"
                :precision="2"
                :step="0.1"
                :min="1.5"
                :max="50.0"
                class="!w-full"
              />
              <span class="text-[10px] text-slate-500 mt-1 block">常规范围 8.00 ~ 20.00</span>
            </div>

            <div>
              <label class="block text-[11px] text-rose-300 font-bold mb-1">7球+ 打出赔率 *</label>
              <el-input-number
                v-model="form.sevenGoalsOdds"
                :precision="2"
                :step="0.5"
                :min="5.0"
                :max="100.0"
                class="!w-full"
              />
              <span class="text-[10px] text-slate-500 mt-1 block">常规范围 18.00 ~ 60.00</span>
            </div>

            <div>
              <label class="block text-[11px] text-amber-300 font-bold mb-1">全场实际开奖总进球 *</label>
              <el-select v-model="form.totalGoals" class="!w-full custom-select">
                <el-option label="0球" :value="0" />
                <el-option label="1球" :value="1" />
                <el-option label="2球" :value="2" />
                <el-option label="3球" :value="3" />
                <el-option label="4球" :value="4" />
                <el-option label="5球" :value="5" />
                <el-option label="6球" :value="6" />
                <el-option label="7球及以上 (7+)" :value="7" />
              </el-select>
              <span class="text-[10px] text-slate-500 mt-1 block">也可填下方比分自动换算</span>
            </div>
          </div>

          <div class="grid grid-cols-1 sm:grid-cols-2 gap-3">
            <div>
              <label class="block text-[11px] text-slate-400 mb-1">全场比分 (填后自动算总球数)</label>
              <el-input
                v-model="form.fullScore"
                placeholder="如: 2:1, 0:0, 3:2"
                class="custom-input"
                @input="onScoreInput"
              />
            </div>
            <div>
              <label class="block text-[11px] text-slate-400 mb-1">批次/比赛组</label>
              <el-input v-model="form.batchTitle" placeholder="如: 日职联精选" class="custom-input" />
            </div>
          </div>

          <div>
            <label class="block text-[11px] text-slate-400 mb-1">复盘心得 / 备忘</label>
            <el-input
              v-model="form.remarks"
              type="textarea"
              rows="2"
              placeholder="记录该场比赛盘口与进球的特点..."
              class="custom-input"
            />
          </div>
        </div>
      </div>

      <template #footer>
        <div class="flex items-center justify-end gap-2 pt-2">
          <button
            @click="recordModalVisible = false"
            class="px-4 py-2 rounded-xl text-xs font-semibold bg-slate-800 text-slate-300"
          >
            取消
          </button>
          <button
            @click="saveRecord"
            class="px-5 py-2 rounded-xl text-xs font-extrabold bg-gradient-to-r from-emerald-500 to-teal-500 text-slate-950 shadow-md shadow-emerald-500/20"
          >
            确认保存
          </button>
        </div>
      </template>
    </el-dialog>

    <!-- ==================== 弹窗 2: 按天批量表格录入弹窗 ==================== -->
    <el-dialog
      v-model="batchModalVisible"
      title="📑 按天批量录入两极进球赔率数据"
      width="95%"
      class="custom-dark-dialog max-w-5xl"
      destroy-on-close
    >
      <div class="space-y-4 max-h-[75vh] overflow-y-auto pr-1 text-slate-200">
        <div class="flex items-center gap-3 p-3 rounded-xl bg-slate-950/80 border border-slate-800 text-xs">
          <span>统一录入比赛日期:</span>
          <el-date-picker
            v-model="batchDate"
            type="date"
            placeholder="统一比赛日期"
            value-format="YYYY-MM-DD"
            size="small"
            class="custom-input"
            @change="applyBatchDate"
          />
          <span class="text-slate-400 text-[11px]">（点击下方「增加一行」即可连续录入多场赛事）</span>
        </div>

        <div class="overflow-x-auto border border-slate-800 rounded-xl">
          <table class="w-full text-left text-xs">
            <thead class="text-[11px] text-slate-400 bg-slate-950 border-b border-slate-800">
              <tr>
                <th class="p-2 w-10 text-center">#</th>
                <th class="p-2 w-32">联赛 *</th>
                <th class="p-2 w-28">赛制</th>
                <th class="p-2 w-32">主队</th>
                <th class="p-2 w-32">客队</th>
                <th class="p-2 w-28 text-cyan-400">0球赔率 *</th>
                <th class="p-2 w-28 text-rose-400">7+赔率 *</th>
                <th class="p-2 w-24">比分</th>
                <th class="p-2 w-28 text-amber-400">总进球数 *</th>
                <th class="p-2 w-36">备忘</th>
                <th class="p-2 w-12 text-center">操作</th>
              </tr>
            </thead>
            <tbody class="divide-y divide-slate-800/80 text-slate-200 bg-slate-900/60">
              <tr v-for="(row, idx) in batchList" :key="idx" class="hover:bg-slate-850">
                <td class="p-2 text-center font-mono text-slate-500">{{ idx + 1 }}</td>
                <td class="p-2">
                  <el-select
                    v-model="row.leagueName"
                    placeholder="选择联赛"
                    filterable
                    allow-create
                    default-first-option
                    size="small"
                    class="!w-full custom-select"
                  >
                    <el-option-group
                      v-for="group in categorizedLeagues"
                      :key="group.label"
                      :label="group.label"
                    >
                      <el-option
                        v-for="item in group.options"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value"
                      />
                    </el-option-group>
                  </el-select>
                </td>
                <td class="p-2">
                  <el-select v-model="row.matchSystem" size="small" class="!w-full custom-select">
                    <el-option label="联赛" value="LEAGUE" />
                    <el-option label="杯赛" value="CUP" />
                    <el-option label="友谊" value="FRIENDLY" />
                  </el-select>
                </td>
                <td class="p-2">
                  <el-input v-model="row.homeTeam" placeholder="主队" size="small" class="custom-input" />
                </td>
                <td class="p-2">
                  <el-input v-model="row.awayTeam" placeholder="客队" size="small" class="custom-input" />
                </td>
                <td class="p-2">
                  <el-input-number
                    v-model="row.zeroGoalsOdds"
                    :precision="2"
                    :step="0.1"
                    :min="1.5"
                    size="small"
                    class="!w-full"
                  />
                </td>
                <td class="p-2">
                  <el-input-number
                    v-model="row.sevenGoalsOdds"
                    :precision="2"
                    :step="0.5"
                    :min="5.0"
                    size="small"
                    class="!w-full"
                  />
                </td>
                <td class="p-2">
                  <el-input
                    v-model="row.fullScore"
                    placeholder="如2:1"
                    size="small"
                    class="custom-input"
                    @input="(v) => onBatchScoreInput(row, v)"
                  />
                </td>
                <td class="p-2">
                  <el-select v-model="row.totalGoals" size="small" class="!w-full custom-select">
                    <el-option label="0球" :value="0" />
                    <el-option label="1球" :value="1" />
                    <el-option label="2球" :value="2" />
                    <el-option label="3球" :value="3" />
                    <el-option label="4球" :value="4" />
                    <el-option label="5球" :value="5" />
                    <el-option label="6球" :value="6" />
                    <el-option label="7+球" :value="7" />
                  </el-select>
                </td>
                <td class="p-2">
                  <el-input v-model="row.remarks" placeholder="备忘" size="small" class="custom-input" />
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

    <!-- ==================== 弹窗 3: 单场智能测算与进球数预测弹窗 ==================== -->
    <el-dialog
      v-model="simulateModalVisible"
      title="🧮 单场 0球与7球赔率大数据测算与进球数预测"
      width="92%"
      class="custom-dark-dialog max-w-4xl"
      destroy-on-close
    >
      <div class="space-y-4 max-h-[80vh] overflow-y-auto pr-1 text-slate-200">
        <!-- 引导说明条 -->
        <div class="p-3 rounded-2xl bg-gradient-to-r from-cyan-950/40 via-sky-950/30 to-blue-950/40 border border-cyan-800/40 flex items-center justify-between text-xs">
          <div class="flex items-center gap-2">
            <span class="text-base">💡</span>
            <span class="text-slate-300">
              输入比赛的联赛、赛制、0球赔率与7球赔率，在库内检索相似两极赔率历史样本，实时推算各进球数打出概率与AI策略。
            </span>
          </div>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-12 gap-5">
          <!-- 左侧输入栏 (5 列) -->
          <div class="md:col-span-5 space-y-3.5">
            <div class="p-4 rounded-2xl bg-slate-950/70 border border-slate-800 space-y-3">
              <span class="text-xs font-bold text-cyan-400 block">⚙️ 输入测算参数</span>

              <div>
                <label class="block text-[11px] text-slate-400 mb-1">联赛限定 (可选)</label>
                <el-select
                  v-model="simForm.leagueName"
                  class="!w-full custom-select"
                  @change="runSimulate"
                >
                  <el-option label="全部主流联赛 (ALL)" value="ALL" />
                  <el-option-group
                    v-for="group in categorizedLeagues"
                    :key="group.label"
                    :label="group.label"
                  >
                    <el-option
                      v-for="item in group.options"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    />
                  </el-option-group>
                </el-select>
              </div>

              <div>
                <label class="block text-[11px] text-slate-400 mb-1">赛制类型</label>
                <el-select
                  v-model="simForm.matchSystem"
                  class="!w-full custom-select"
                  @change="runSimulate"
                >
                  <el-option label="全部赛制 (ALL)" value="ALL" />
                  <el-option label="联赛积分制 (LEAGUE)" value="LEAGUE" />
                  <el-option label="杯赛淘汰赛 (CUP)" value="CUP" />
                </el-select>
              </div>

              <div>
                <label class="block text-[11px] text-cyan-300 font-bold mb-1">0球 打出赔率 *</label>
                <el-input-number
                  v-model="simForm.zeroGoalsOdds"
                  :precision="2"
                  :step="0.1"
                  :min="1.5"
                  class="!w-full"
                  @change="runSimulate"
                />
              </div>

              <div>
                <label class="block text-[11px] text-rose-300 font-bold mb-1">7球+ 打出赔率 *</label>
                <el-input-number
                  v-model="simForm.sevenGoalsOdds"
                  :precision="2"
                  :step="0.5"
                  :min="5.0"
                  class="!w-full"
                  @change="runSimulate"
                />
              </div>

              <div class="grid grid-cols-2 gap-2 text-[11px]">
                <div>
                  <label class="block text-slate-400 mb-1">0球容差步幅</label>
                  <el-select v-model="simForm.zeroTolerance" class="!w-full custom-select" @change="runSimulate">
                    <el-option label="±0.50" :value="0.5" />
                    <el-option label="±1.00 (标准)" :value="1.0" />
                    <el-option label="±1.50" :value="1.5" />
                  </el-select>
                </div>
                <div>
                  <label class="block text-slate-400 mb-1">7球容差步幅</label>
                  <el-select v-model="simForm.sevenTolerance" class="!w-full custom-select" @change="runSimulate">
                    <el-option label="±2.00" :value="2.0" />
                    <el-option label="±4.00 (标准)" :value="4.0" />
                    <el-option label="±6.00" :value="6.0" />
                  </el-select>
                </div>
              </div>

              <button
                @click="runSimulate"
                :disabled="simLoading"
                class="w-full py-2 rounded-xl text-xs font-bold bg-gradient-to-r from-cyan-500 to-blue-600 text-slate-950 shadow transition flex items-center justify-center gap-1.5"
              >
                <span>⚡ 立即重新测算</span>
              </button>
            </div>
          </div>

          <!-- 右侧测算结果呈现 (7 列) -->
          <div class="md:col-span-7 space-y-3.5">
            <!-- 匹配样本 Bar -->
            <div class="p-3 rounded-2xl bg-slate-950/80 border border-slate-800 flex items-center justify-between text-xs">
              <div class="flex items-center gap-2">
                <span class="text-slate-400">同类样本数:</span>
                <span class="text-base font-black font-mono text-cyan-300">{{ simResult?.matchedCount || 0 }} 场</span>
                <span
                  class="text-[10px] px-2 py-0.5 rounded-full font-bold"
                  :class="[
                    (simResult?.matchedCount || 0) >= 10 ? 'bg-emerald-500/20 text-emerald-300' :
                    ((simResult?.matchedCount || 0) >= 4 ? 'bg-amber-500/20 text-amber-300' : 'bg-rose-500/20 text-rose-300')
                  ]"
                >
                  {{ (simResult?.matchedCount || 0) >= 10 ? '样本充足 (高置信)' : ((simResult?.matchedCount || 0) >= 4 ? '样本中等 (参考)' : '样本偏少') }}
                </span>
              </div>
              <span class="text-[11px] font-mono text-slate-400">
                场均进球: <b class="text-amber-400">{{ simResult?.avgGoalsInSample || '0.00' }}</b> 球
              </span>
            </div>

            <!-- Top 3 最可能打出总进球数 -->
            <div class="p-3.5 rounded-2xl bg-slate-950/70 border border-slate-800 space-y-2">
              <span class="text-xs font-bold text-amber-300 block">🎯 预测最可能打出总进球数 Top 3</span>
              <div class="grid grid-cols-3 gap-2.5">
                <div
                  v-for="(top, idx) in simResult?.topGoalsRanking || []"
                  :key="top.goalText"
                  class="p-2.5 rounded-xl bg-slate-900 border border-slate-800 text-center space-y-1"
                >
                  <span class="text-[10px] text-slate-400 font-bold block">
                    {{ idx === 0 ? '🥇 第一高频' : (idx === 1 ? '🥈 第二高频' : '🥉 第三高频') }}
                  </span>
                  <span class="text-base font-black font-mono text-white block">{{ top.goalText }}</span>
                  <span class="text-xs font-bold font-mono text-emerald-400 block">{{ top.rate }}%</span>
                  <span class="text-[10px] text-slate-500 font-mono block">({{ top.count }} 场打出)</span>
                </div>
              </div>
            </div>

            <!-- 三大进球区间结构 -->
            <div class="grid grid-cols-3 gap-2 text-center text-xs font-mono">
              <div class="p-2.5 rounded-xl bg-slate-950/70 border border-slate-800">
                <span class="text-[10px] text-slate-400 font-sans block">0-1球 小球率</span>
                <span class="text-sm font-bold text-sky-400">{{ simResult?.predZeroOneRate || '0.00' }}%</span>
              </div>
              <div class="p-2.5 rounded-xl bg-slate-950/70 border border-slate-800">
                <span class="text-[10px] text-slate-400 font-sans block">2-3球 黄金区</span>
                <span class="text-sm font-bold text-emerald-400">{{ simResult?.predTwoThreeRate || '0.00' }}%</span>
              </div>
              <div class="p-2.5 rounded-xl bg-slate-950/70 border border-slate-800">
                <span class="text-[10px] text-slate-400 font-sans block">4+球 大球率</span>
                <span class="text-sm font-bold text-rose-400">{{ simResult?.predFourPlusRate || '0.00' }}%</span>
              </div>
            </div>

            <!-- AI 决策策略建议 -->
            <div class="p-3 rounded-2xl bg-slate-950/90 border border-slate-800 space-y-1">
              <span class="text-xs font-bold text-amber-300 block">🤖 量化打单策略建议</span>
              <p class="text-xs text-slate-300 leading-relaxed font-sans">
                {{ simResult?.advice || '等待测算...' }}
              </p>
            </div>
          </div>
        </div>
      </div>

      <template #footer>
        <div class="flex items-center justify-end pt-2">
          <button
            @click="simulateModalVisible = false"
            class="px-5 py-2 rounded-xl text-xs font-semibold bg-slate-800 text-slate-200"
          >
            关闭
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

// 1. 常见主流联赛分类字典 (涵盖日职、韩职、澳超、巴甲、葡超、挪超、荷甲、五大联赛、欧冠等)
const categorizedLeagues = [
  {
    label: '亚洲 & 大洋洲赛事',
    options: [
      { label: '日职联 (J1)', value: '日职联' },
      { label: '日乙 (J2)', value: '日乙' },
      { label: '韩职K联', value: '韩职K联' },
      { label: '澳超', value: '澳超' },
      { label: '亚冠', value: '亚冠' },
      { label: '沙职', value: '沙职' },
    ]
  },
  {
    label: '欧洲主流与次级联赛',
    options: [
      { label: '荷甲', value: '荷甲' },
      { label: '葡超', value: '葡超' },
      { label: '挪超', value: '挪超' },
      { label: '瑞典超', value: '瑞典超' },
      { label: '芬超', value: '芬超' },
      { label: '英冠', value: '英冠' }
    ]
  },
  {
    label: '欧洲五大联赛',
    options: [
      { label: '英超', value: '英超' },
      { label: '西甲', value: '西甲' },
      { label: '意甲', value: '意甲' },
      { label: '德甲', value: '德甲' },
      { label: '法甲', value: '法甲' }
    ]
  },
  {
    label: '洲际冠军杯与淘汰赛',
    options: [
      { label: '欧冠', value: '欧冠' },
      { label: '欧联杯', value: '欧联杯' },
      { label: '欧协联', value: '欧协联' },
      { label: '足总杯/国王杯', value: '国内杯赛' }
    ]
  },
  {
    label: '美洲主要赛事',
    options: [
      { label: '巴甲', value: '巴甲' },
      { label: '阿甲', value: '阿甲' },
      { label: '美职联 (MLS)', value: '美职联' },
      { label: '南美解放者杯', value: '南美解放者杯' }
    ]
  }
]

// 顶栏快捷联赛 Chips
const quickLeagueFilters = [
  { label: '全部联赛', value: 'ALL' },
  { label: '日职联', value: '日职联' },
  { label: '韩职K联', value: '韩职K联' },
  { label: '澳超', value: '澳超' },
  { label: '巴甲', value: '巴甲' },
  { label: '荷甲', value: '荷甲' },
  { label: '葡超', value: '葡超' },
  { label: '挪超', value: '挪超' },
  { label: '五大联赛', value: '五大联赛' },
  { label: '欧冠', value: '欧冠' }
]

const systemOptions = [
  { label: '全部赛制', value: 'ALL', icon: '🌐' },
  { label: '联赛制', value: 'LEAGUE', icon: '🏆' },
  { label: '杯赛淘汰', value: 'CUP', icon: '🥊' },
  { label: '友谊赛', value: 'FRIENDLY', icon: '🤝' }
]

const timePresets = [
  { label: '全部时间', value: 'ALL' },
  { label: '最近1个月', value: '1M' },
  { label: '最近3个月', value: '3M' }
]

// 筛选状态
const leagueFilter = ref('ALL')
const systemFilter = ref('ALL')
const timePreset = ref('ALL')
const dateRange = ref([])

// 数据与分页
const allRecords = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(15)
const selectedIds = ref([])

// 大数据分析汇总对象
const statsSummary = ref({
  totalMatches: 0,
  avgZeroGoalsOdds: '0.00',
  avgSevenGoalsOdds: '0.00',
  avgTotalGoals: '0.00',
  avgOddsRatio: '0.00',
  zeroOneCount: 0,
  zeroOneRate: '0.00',
  twoThreeCount: 0,
  twoThreeRate: '0.00',
  fourSixCount: 0,
  fourSixRate: '0.00',
  sevenPlusCount: 0,
  sevenPlusRate: '0.00',
  goalsDistribution: {},
  zeroOddsTiers: [],
  sevenOddsTiers: [],
  ratioTiers: [],
  leagueRankings: []
})

// 分档 Tab
const activeTierTab = ref('zero') // 'zero' | 'seven' | 'ratio'
const currentTierList = computed(() => {
  if (activeTierTab.value === 'zero') return statsSummary.value.zeroOddsTiers || []
  if (activeTierTab.value === 'seven') return statsSummary.value.sevenOddsTiers || []
  return statsSummary.value.ratioTiers || []
})

// 图表 DOM
const scatterChartRef = ref(null)
const goalsBarChartRef = ref(null)
let scatterChartInstance = null
let goalsBarChartInstance = null

// 弹窗状态
const recordModalVisible = ref(false)
const batchModalVisible = ref(false)
const simulateModalVisible = ref(false)
const isEditMode = ref(false)

// 单条表单
const form = ref({
  id: null,
  matchDate: new Date().toISOString().split('T')[0],
  batchTitle: '',
  leagueName: '日职联',
  matchSystem: 'LEAGUE',
  homeTeam: '',
  awayTeam: '',
  zeroGoalsOdds: 9.50,
  sevenGoalsOdds: 28.00,
  totalGoals: 2,
  fullScore: '2:0',
  remarks: ''
})

// 批量表单
const batchDate = ref(new Date().toISOString().split('T')[0])
const batchList = ref([])

// 测算表单
const simForm = ref({
  leagueName: 'ALL',
  matchSystem: 'ALL',
  zeroGoalsOdds: 9.50,
  sevenGoalsOdds: 28.00,
  zeroTolerance: 1.0,
  sevenTolerance: 4.0
})
const simResult = ref(null)
const simLoading = ref(false)

// 过滤后的比赛数据
const filteredMatches = computed(() => {
  let list = allRecords.value

  // 1. 联赛筛选
  if (leagueFilter.value !== 'ALL') {
    if (leagueFilter.value === '五大联赛') {
      const bigFive = ['英超', '西甲', '意甲', '德甲', '法甲']
      list = list.filter(m => bigFive.some(b => m.leagueName && m.leagueName.includes(b)))
    } else {
      list = list.filter(m => m.leagueName && m.leagueName.includes(leagueFilter.value))
    }
  }

  // 2. 赛制筛选
  if (systemFilter.value !== 'ALL') {
    list = list.filter(m => m.matchSystem === systemFilter.value)
  }

  // 3. 日期筛选
  if (dateRange.value && dateRange.value.length === 2) {
    const start = dateRange.value[0]
    const end = dateRange.value[1]
    list = list.filter(m => m.matchDate >= start && m.matchDate <= end)
  }

  return list
})

const pagedMatches = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  return filteredMatches.value.slice(start, start + pageSize.value)
})

const leagueCount = computed(() => {
  const set = new Set(filteredMatches.value.map(m => m.leagueName).filter(Boolean))
  return set.size
})

// ==========================================
// 步幅统计引擎 (1-10 步幅调节 · 0球与7球区间切分 · 0~7+球落点概率)
// ==========================================
const steppedTarget = ref('zero') // 'zero' | 'seven' | 'ratio'
const steppedStep = ref(2) // 1 到 10
const onlyShowSampleBuckets = ref(true)
const steppedViewMode = ref('table') // 'table' | 'chart' | 'trend'
const expandedBucketKey = ref(null)

const steppedBarChartRef = ref(null)
const steppedTrendChartRef = ref(null)
let steppedBarChartInstance = null
let steppedTrendChartInstance = null

const setSteppedTarget = (target) => {
  steppedTarget.value = target
  if (target === 'zero') {
    steppedStep.value = 2
  } else if (target === 'seven') {
    steppedStep.value = 5
  } else {
    steppedStep.value = 1
  }
}

const toggleBucketExpand = (key) => {
  expandedBucketKey.value = expandedBucketKey.value === key ? null : key
}

const getGoalCellClass = (bucket, goalIndex) => {
  if (bucket.count === 0) return 'text-slate-600'
  const rate = bucket.goalRates[goalIndex]
  const isDominant = bucket.dominant.goal === (goalIndex === 7 ? '7+球' : `${goalIndex}球`)

  if (isDominant) {
    return 'bg-amber-500/20 text-amber-300 font-black border-x border-amber-500/30'
  }
  if (rate >= 30) {
    return 'bg-emerald-500/15 text-emerald-300 font-extrabold'
  }
  if (rate >= 15) {
    return 'text-slate-200 font-bold'
  }
  if (rate > 0) {
    return 'text-slate-400'
  }
  return 'text-slate-600'
}

// 动态步幅区间计算 (反应式瞬时响应)
const steppedBuckets = computed(() => {
  const matches = filteredMatches.value
  if (!matches || matches.length === 0) return []

  const step = Math.max(1, Math.min(10, Number(steppedStep.value) || 1))
  const target = steppedTarget.value

  const values = matches.map(m => {
    if (target === 'zero') return Number(m.zeroGoalsOdds)
    if (target === 'seven') return Number(m.sevenGoalsOdds)
    return Number(m.oddsRatio || (m.sevenGoalsOdds / m.zeroGoalsOdds))
  }).filter(v => !isNaN(v) && v > 0)

  if (values.length === 0) return []

  const minVal = Math.min(...values)
  const maxVal = Math.max(...values)

  let start = Math.floor(minVal / step) * step
  let end = Math.ceil(maxVal / step) * step
  if (start === end) end = start + step

  const buckets = []
  const totalMatchesCount = matches.length

  for (let low = start; low < end; low = Number((low + step).toFixed(2))) {
    const high = Number((low + step).toFixed(2))
    const isLast = (low + step >= end)

    const bucketRecords = matches.filter(m => {
      let val = target === 'zero'
        ? Number(m.zeroGoalsOdds)
        : target === 'seven'
          ? Number(m.sevenGoalsOdds)
          : Number(m.oddsRatio || (m.sevenGoalsOdds / m.zeroGoalsOdds))
      if (isNaN(val)) return false
      if (isLast) {
        return val >= low && val <= high + 0.0001
      }
      return val >= low && val < high
    })

    const count = bucketRecords.length
    if (onlyShowSampleBuckets.value && count === 0) {
      continue
    }

    const goalCounts = { 0: 0, 1: 0, 2: 0, 3: 0, 4: 0, 5: 0, 6: 0, 7: 0 }
    let totalGoalsSum = 0

    bucketRecords.forEach(r => {
      const g = Number(r.totalGoals) || 0
      totalGoalsSum += g
      if (g >= 7) {
        goalCounts[7]++
      } else if (g >= 0 && g <= 6) {
        goalCounts[g]++
      }
    })

    const goalRates = {}
    for (let i = 0; i <= 7; i++) {
      goalRates[i] = count > 0 ? Number(((goalCounts[i] / count) * 100).toFixed(1)) : 0
    }

    let maxGoal = '0球'
    let maxCount = -1
    for (let i = 0; i <= 7; i++) {
      if (goalCounts[i] > maxCount) {
        maxCount = goalCounts[i]
        maxGoal = i === 7 ? '7+球' : `${i}球`
      }
    }
    const dominantRate = count > 0 ? ((maxCount / count) * 100).toFixed(1) : 0

    const zeroOneRate = Number((goalRates[0] + goalRates[1]).toFixed(1))
    const twoThreeRate = Number((goalRates[2] + goalRates[3]).toFixed(1))
    const fourSixRate = Number((goalRates[4] + goalRates[5] + goalRates[6]).toFixed(1))
    const sevenPlusRate = goalRates[7]

    let styleTag = '均衡均值'
    let styleColor = 'text-amber-300 bg-amber-500/10 border-amber-500/20'
    if (zeroOneRate >= 50) {
      styleTag = '小球防守偏向'
      styleColor = 'text-sky-300 bg-sky-500/10 border-sky-500/20'
    } else if (twoThreeRate >= 50) {
      styleTag = '黄金2-3球黄金区'
      styleColor = 'text-emerald-300 bg-emerald-500/10 border-emerald-500/20'
    } else if (fourSixRate + sevenPlusRate >= 40) {
      styleTag = '大球对攻狂轰'
      styleColor = 'text-rose-300 bg-rose-500/10 border-rose-500/20'
    }

    buckets.push({
      key: `bucket_${low}_${high}`,
      low,
      high,
      label: `[${low.toFixed(1)} ~ ${high.toFixed(1)})`,
      count,
      countRate: totalMatchesCount > 0 ? ((count / totalMatchesCount) * 100).toFixed(1) : '0.0',
      avgGoals: count > 0 ? (totalGoalsSum / count).toFixed(2) : '0.00',
      goalCounts,
      goalRates,
      dominant: {
        goal: maxGoal,
        count: maxCount,
        rate: dominantRate
      },
      zeroOneRate,
      twoThreeRate,
      fourSixRate,
      sevenPlusRate,
      styleTag,
      styleColor,
      records: bucketRecords
    })
  }

  return buckets
})

// 步幅 ECharts 图表渲染 (堆叠柱状图与走势折线图)
const renderSteppedCharts = () => {
  const buckets = steppedBuckets.value
  if (!buckets || buckets.length === 0) return

  const categories = buckets.map(b => b.label)
  const goalNames = ['0球', '1球', '2球', '3球', '4球', '5球', '6球', '7+球']
  const goalColors = [
    '#64748b', // 0球
    '#38bdf8', // 1球
    '#10b981', // 2球
    '#14b8a6', // 3球
    '#f59e0b', // 4球
    '#f97316', // 5球
    '#ec4899', // 6球
    '#a855f7'  // 7+球
  ]

  // 1. 堆叠概率柱图
  if (steppedBarChartRef.value) {
    if (!steppedBarChartInstance) {
      steppedBarChartInstance = echarts.init(steppedBarChartRef.value)
    }
    const barSeries = goalNames.map((name, idx) => ({
      name,
      type: 'bar',
      stack: 'total',
      emphasis: { focus: 'series' },
      data: buckets.map(b => b.goalRates[idx]),
      itemStyle: { color: goalColors[idx] }
    }))

    steppedBarChartInstance.setOption({
      backgroundColor: 'transparent',
      tooltip: {
        trigger: 'axis',
        axisPointer: { type: 'shadow' },
        backgroundColor: '#090d16',
        borderColor: '#1e293b',
        textStyle: { color: '#f8fafc', fontSize: 11 },
        formatter: (params) => {
          let str = `<div style="font-weight:bold;color:#38bdf8;margin-bottom:4px;">赔率区间: ${params[0].axisValue}</div>`
          params.forEach(p => {
            if (p.value > 0) {
              str += `<div style="display:flex;justify-content:space-between;gap:12px;font-size:11px;">
                <span>${p.marker} ${p.seriesName}</span>
                <b style="color:#f8fafc;">${p.value}%</b>
              </div>`
            }
          })
          return str
        }
      },
      legend: {
        data: goalNames,
        textStyle: { color: '#94a3b8', fontSize: 11 },
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
        data: categories,
        axisLine: { lineStyle: { color: '#334155' } },
        axisLabel: { color: '#94a3b8', fontSize: 10, rotate: categories.length > 8 ? 25 : 0 }
      },
      yAxis: {
        type: 'value',
        max: 100,
        axisLine: { lineStyle: { color: '#334155' } },
        splitLine: { lineStyle: { color: '#1e293b', type: 'dashed' } },
        axisLabel: { color: '#64748b', formatter: '{value}%' }
      },
      series: barSeries
    })
  }

  // 2. 走势折线图
  if (steppedTrendChartRef.value) {
    if (!steppedTrendChartInstance) {
      steppedTrendChartInstance = echarts.init(steppedTrendChartRef.value)
    }
    const lineSeries = goalNames.map((name, idx) => ({
      name,
      type: 'line',
      smooth: true,
      symbol: 'circle',
      symbolSize: 6,
      data: buckets.map(b => b.goalRates[idx]),
      itemStyle: { color: goalColors[idx] },
      lineStyle: { width: 2.5 }
    }))

    steppedTrendChartInstance.setOption({
      backgroundColor: 'transparent',
      tooltip: {
        trigger: 'axis',
        backgroundColor: '#090d16',
        borderColor: '#1e293b',
        textStyle: { color: '#f8fafc', fontSize: 11 },
        formatter: (params) => {
          let str = `<div style="font-weight:bold;color:#38bdf8;margin-bottom:4px;">赔率区间: ${params[0].axisValue}</div>`
          params.forEach(p => {
            str += `<div style="display:flex;justify-content:space-between;gap:12px;font-size:11px;">
              <span>${p.marker} ${p.seriesName}</span>
              <b style="color:#f8fafc;">${p.value}%</b>
            </div>`
          })
          return str
        }
      },
      legend: {
        data: goalNames,
        textStyle: { color: '#94a3b8', fontSize: 11 },
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
        data: categories,
        axisLine: { lineStyle: { color: '#334155' } },
        axisLabel: { color: '#94a3b8', fontSize: 10, rotate: categories.length > 8 ? 25 : 0 }
      },
      yAxis: {
        type: 'value',
        axisLine: { lineStyle: { color: '#334155' } },
        splitLine: { lineStyle: { color: '#1e293b', type: 'dashed' } },
        axisLabel: { color: '#64748b', formatter: '{value}%' }
      },
      series: lineSeries
    })
  }
}

const isAllSelected = computed(() => {
  return pagedMatches.value.length > 0 && pagedMatches.value.every(r => selectedIds.value.includes(r.id))
})

const toggleSelectAll = (e) => {
  if (e.target.checked) {
    pagedMatches.value.forEach(r => {
      if (!selectedIds.value.includes(r.id)) selectedIds.value.push(r.id)
    })
  } else {
    const pageIds = pagedMatches.value.map(r => r.id)
    selectedIds.value = selectedIds.value.filter(id => !pageIds.includes(id))
  }
}

// 时间预设切换
const setTimePreset = (preset) => {
  timePreset.value = preset
  const now = new Date()
  const formatDate = (d) => {
    const y = d.getFullYear()
    const m = String(d.getMonth() + 1).padStart(2, '0')
    const day = String(d.getDate()).padStart(2, '0')
    return `${y}-${m}-${day}`
  }
  const end = formatDate(now)

  if (preset === 'ALL') {
    dateRange.value = []
  } else if (preset === '1M') {
    const startD = new Date()
    startD.setMonth(startD.getMonth() - 1)
    dateRange.value = [formatDate(startD), end]
  } else if (preset === '3M') {
    const startD = new Date()
    startD.setMonth(startD.getMonth() - 3)
    dateRange.value = [formatDate(startD), end]
  }
}

// 拉取列表与统计
const fetchData = async () => {
  loading.value = true
  try {
    const params = {}
    if (dateRange.value && dateRange.value.length === 2) {
      params.startDate = dateRange.value[0]
      params.endDate = dateRange.value[1]
    }
    if (leagueFilter.value !== 'ALL' && leagueFilter.value !== '五大联赛') {
      params.leagueName = leagueFilter.value
    }
    if (systemFilter.value !== 'ALL') {
      params.matchSystem = systemFilter.value
    }

    const [listRes, summaryRes] = await Promise.all([
      api.getExtremeGoalsOddsList(params),
      api.getExtremeGoalsOddsSummary(params)
    ])

    const list = listRes.data || listRes || []
    allRecords.value = Array.isArray(list) ? list : []

    if (summaryRes && summaryRes.data) {
      statsSummary.value = summaryRes.data
    } else {
      computeLocalSummary()
    }

    nextTick(() => {
      renderCharts()
    })
  } catch (err) {
    console.warn('接口加载异常，执行本地推导:', err)
    computeLocalSummary()
  } finally {
    loading.value = false
  }
}

// 本地统计计算兜底
const computeLocalSummary = () => {
  const list = filteredMatches.value
  const total = list.length
  if (total === 0) {
    statsSummary.value = {
      totalMatches: 0,
      avgZeroGoalsOdds: '0.00',
      avgSevenGoalsOdds: '0.00',
      avgTotalGoals: '0.00',
      avgOddsRatio: '0.00',
      zeroOneCount: 0,
      zeroOneRate: '0.00',
      twoThreeCount: 0,
      twoThreeRate: '0.00',
      fourSixCount: 0,
      fourSixRate: '0.00',
      sevenPlusCount: 0,
      sevenPlusRate: '0.00',
      goalsDistribution: {},
      zeroOddsTiers: [],
      sevenOddsTiers: [],
      ratioTiers: [],
      leagueRankings: []
    }
    return
  }

  let sZero = 0
  let sSeven = 0
  let sGoals = 0
  let sRatio = 0
  let zo = 0
  let tt = 0
  let fs = 0
  let sp = 0
  const gMap = { '0球': 0, '1球': 0, '2球': 0, '3球': 0, '4球': 0, '5球': 0, '6球': 0, '7+球': 0 }

  list.forEach(r => {
    const z = Number(r.zeroGoalsOdds) || 0
    const s = Number(r.sevenGoalsOdds) || 0
    const g = Number(r.totalGoals) || 0
    sZero += z
    sSeven += s
    sGoals += g
    sRatio += z > 0 ? s / z : 0

    if (g <= 1) zo++
    else if (g <= 3) tt++
    else if (g <= 6) fs++
    else sp++

    const key = g >= 7 ? '7+球' : `${g}球`
    gMap[key] = (gMap[key] || 0) + 1
  })

  statsSummary.value.totalMatches = total
  statsSummary.value.avgZeroGoalsOdds = (sZero / total).toFixed(2)
  statsSummary.value.avgSevenGoalsOdds = (sSeven / total).toFixed(2)
  statsSummary.value.avgTotalGoals = (sGoals / total).toFixed(2)
  statsSummary.value.avgOddsRatio = (sRatio / total).toFixed(2)
  statsSummary.value.zeroOneCount = zo
  statsSummary.value.zeroOneRate = ((zo / total) * 100).toFixed(2)
  statsSummary.value.twoThreeCount = tt
  statsSummary.value.twoThreeRate = ((tt / total) * 100).toFixed(2)
  statsSummary.value.fourSixCount = fs
  statsSummary.value.fourSixRate = ((fs / total) * 100).toFixed(2)
  statsSummary.value.sevenPlusCount = sp
  statsSummary.value.sevenPlusRate = ((sp / total) * 100).toFixed(2)
  statsSummary.value.goalsDistribution = gMap
}

// 渲染 ECharts 图表
const renderCharts = () => {
  renderScatterChart()
  renderGoalsBarChart()
}

const renderScatterChart = () => {
  if (!scatterChartRef.value) return
  if (!scatterChartInstance) {
    scatterChartInstance = echarts.init(scatterChartRef.value)
  }

  const list = filteredMatches.value
  // 点格式: [zeroOdds, sevenOdds, totalGoals, homeTeam, awayTeam, leagueName]
  const data01 = []
  const data23 = []
  const data4plus = []

  list.forEach(r => {
    const pt = [
      Number(r.zeroGoalsOdds),
      Number(r.sevenGoalsOdds),
      r.totalGoals,
      r.homeTeam || '主队',
      r.awayTeam || '客队',
      r.leagueName
    ]
    if (r.totalGoals <= 1) data01.push(pt)
    else if (r.totalGoals <= 3) data23.push(pt)
    else data4plus.push(pt)
  })

  const option = {
    backgroundColor: 'transparent',
    tooltip: {
      backgroundColor: '#090d16',
      borderColor: '#1e293b',
      textStyle: { color: '#f8fafc', fontSize: 11 },
      formatter: (param) => {
        const d = param.data
        return `
          <div style="font-weight:bold;color:#38bdf8;">${d[5]} | ${d[3]} vs ${d[4]}</div>
          <div style="margin-top:4px;">0球赔率: <b style="color:#38bdf8;">${d[0]}</b></div>
          <div>7+赔率: <b style="color:#fb7185;">${d[1]}</b></div>
          <div>开奖进球: <b style="color:#fbbf24;">${d[2]} 球</b></div>
        `
      }
    },
    legend: {
      data: ['0-1球 (小球)', '2-3球 (黄金中球)', '4球及以上 (大球)'],
      textStyle: { color: '#94a3b8', fontSize: 11 },
      top: 0
    },
    grid: {
      left: '4%',
      right: '6%',
      top: '14%',
      bottom: '12%',
      containLabel: true
    },
    xAxis: {
      name: '0球赔率',
      nameTextStyle: { color: '#64748b', fontSize: 10 },
      type: 'value',
      min: 7,
      max: 22,
      splitLine: { lineStyle: { color: '#1e293b', type: 'dashed' } },
      axisLine: { lineStyle: { color: '#334155' } },
      axisLabel: { color: '#94a3b8', fontSize: 10 }
    },
    yAxis: {
      name: '7球+赔率',
      nameTextStyle: { color: '#64748b', fontSize: 10 },
      type: 'value',
      min: 10,
      max: 60,
      splitLine: { lineStyle: { color: '#1e293b', type: 'dashed' } },
      axisLine: { lineStyle: { color: '#334155' } },
      axisLabel: { color: '#94a3b8', fontSize: 10 }
    },
    series: [
      {
        name: '0-1球 (小球)',
        type: 'scatter',
        data: data01,
        symbolSize: 14,
        itemStyle: { color: '#38bdf8', opacity: 0.85, shadowBlur: 6, shadowColor: '#38bdf8' }
      },
      {
        name: '2-3球 (黄金中球)',
        type: 'scatter',
        data: data23,
        symbolSize: 16,
        itemStyle: { color: '#34d399', opacity: 0.85, shadowBlur: 6, shadowColor: '#34d399' }
      },
      {
        name: '4球及以上 (大球)',
        type: 'scatter',
        data: data4plus,
        symbolSize: 18,
        itemStyle: { color: '#f43f5e', opacity: 0.9, shadowBlur: 8, shadowColor: '#f43f5e' }
      }
    ]
  }

  scatterChartInstance.setOption(option)
}

const renderGoalsBarChart = () => {
  if (!goalsBarChartRef.value) return
  if (!goalsBarChartInstance) {
    goalsBarChartInstance = echarts.init(goalsBarChartRef.value)
  }

  const gMap = statsSummary.value.goalsDistribution || {}
  const xData = ['0球', '1球', '2球', '3球', '4球', '5球', '6球', '7+球']
  const yData = xData.map(k => gMap[k] || 0)

  const option = {
    backgroundColor: 'transparent',
    tooltip: {
      trigger: 'axis',
      backgroundColor: '#090d16',
      borderColor: '#1e293b',
      textStyle: { color: '#f8fafc', fontSize: 11 }
    },
    grid: {
      left: '3%',
      right: '4%',
      top: '10%',
      bottom: '12%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: xData,
      axisLine: { lineStyle: { color: '#334155' } },
      axisLabel: { color: '#cbd5e1', fontSize: 10 }
    },
    yAxis: {
      type: 'value',
      splitLine: { lineStyle: { color: '#1e293b', type: 'dashed' } },
      axisLabel: { color: '#64748b', fontSize: 10 }
    },
    series: [
      {
        name: '打出场次',
        type: 'bar',
        barWidth: '55%',
        data: yData,
        itemStyle: {
          color: (param) => {
            const idx = param.dataIndex
            if (idx <= 1) return '#38bdf8'
            if (idx <= 3) return '#10b981'
            if (idx <= 6) return '#fbbf24'
            return '#f43f5e'
          },
          borderRadius: [6, 6, 0, 0]
        },
        label: {
          show: true,
          position: 'top',
          color: '#e2e8f0',
          fontSize: 10,
          fontWeight: 'bold'
        }
      }
    ]
  }

  goalsBarChartInstance.setOption(option)
}

// 自动根据比分输入推算进球数
const onScoreInput = (val) => {
  if (val && val.includes(':')) {
    const parts = val.split(':')
    if (parts.length === 2) {
      const h = parseInt(parts[0].trim())
      const a = parseInt(parts[1].trim())
      if (!isNaN(h) && !isNaN(a)) {
        form.value.totalGoals = Math.min(7, h + a)
      }
    }
  }
}

const onBatchScoreInput = (row, val) => {
  if (val && val.includes(':')) {
    const parts = val.split(':')
    if (parts.length === 2) {
      const h = parseInt(parts[0].trim())
      const a = parseInt(parts[1].trim())
      if (!isNaN(h) && !isNaN(a)) {
        row.totalGoals = Math.min(7, h + a)
      }
    }
  }
}

// 录入弹窗
const openCreateModal = () => {
  isEditMode.value = false
  form.value = {
    id: null,
    matchDate: new Date().toISOString().split('T')[0],
    batchTitle: '',
    leagueName: leagueFilter.value !== 'ALL' && leagueFilter.value !== '五大联赛' ? leagueFilter.value : '日职联',
    matchSystem: 'LEAGUE',
    homeTeam: '',
    awayTeam: '',
    zeroGoalsOdds: 9.50,
    sevenGoalsOdds: 28.00,
    totalGoals: 2,
    fullScore: '2:0',
    remarks: ''
  }
  recordModalVisible.value = true
}

const openEditModal = (row) => {
  isEditMode.value = true
  form.value = { ...row }
  recordModalVisible.value = true
}

const saveRecord = async () => {
  if (!form.value.leagueName || !form.value.matchDate) {
    ElMessage.warning('请填写比赛日期和联赛名称')
    return
  }
  try {
    if (isEditMode.value && form.value.id) {
      await api.updateExtremeGoalsOdds(form.value.id, form.value)
      ElMessage.success('更新两极进球赔率记录成功！')
    } else {
      await api.createExtremeGoalsOdds(form.value)
      ElMessage.success('录入两极进球赔率记录成功！')
    }
    recordModalVisible.value = false
    fetchData()
  } catch (err) {
    ElMessage.error('保存失败: ' + (err.message || '系统异常'))
  }
}

const deleteRecord = async (id) => {
  try {
    await ElMessageBox.confirm('确定删除该场两极进球赔率记录吗？', '提示', {
      type: 'warning',
      confirmButtonText: '确定删除',
      cancelButtonText: '取消'
    })
    await api.deleteExtremeGoalsOdds(id)
    ElMessage.success('删除成功！')
    fetchData()
  } catch (err) {
    if (err !== 'cancel') {
      ElMessage.error('删除失败: ' + (err.message || '系统异常'))
    }
  }
}

const batchDeleteRecords = async () => {
  if (selectedIds.value.length === 0) return
  try {
    await ElMessageBox.confirm(`确定批量删除选中的 ${selectedIds.value.length} 条记录吗？`, '批量删除警告', {
      type: 'warning'
    })
    await api.batchDeleteExtremeGoalsOdds(selectedIds.value)
    ElMessage.success('批量删除成功！')
    selectedIds.value = []
    fetchData()
  } catch (err) {
    if (err !== 'cancel') {
      ElMessage.error('批量删除失败: ' + (err.message || '系统异常'))
    }
  }
}

// 批量录入
const openBatchModal = () => {
  batchDate.value = new Date().toISOString().split('T')[0]
  batchList.value = [
    { matchDate: batchDate.value, leagueName: '日职联', matchSystem: 'LEAGUE', homeTeam: '', awayTeam: '', zeroGoalsOdds: 9.50, sevenGoalsOdds: 28.00, totalGoals: 2, fullScore: '', remarks: '' },
    { matchDate: batchDate.value, leagueName: '韩职K联', matchSystem: 'LEAGUE', homeTeam: '', awayTeam: '', zeroGoalsOdds: 9.00, sevenGoalsOdds: 32.00, totalGoals: 2, fullScore: '', remarks: '' },
    { matchDate: batchDate.value, leagueName: '澳超', matchSystem: 'LEAGUE', homeTeam: '', awayTeam: '', zeroGoalsOdds: 14.00, sevenGoalsOdds: 19.00, totalGoals: 4, fullScore: '', remarks: '' }
  ]
  batchModalVisible.value = true
}

const addBatchRow = () => {
  batchList.value.push({
    matchDate: batchDate.value,
    leagueName: '荷甲',
    matchSystem: 'LEAGUE',
    homeTeam: '',
    awayTeam: '',
    zeroGoalsOdds: 12.00,
    sevenGoalsOdds: 22.00,
    totalGoals: 3,
    fullScore: '',
    remarks: ''
  })
}

const removeBatchRow = (idx) => {
  batchList.value.splice(idx, 1)
}

const applyBatchDate = (val) => {
  batchList.value.forEach(row => {
    row.matchDate = val
  })
}

const saveBatchRecords = async () => {
  const valid = batchList.value.filter(r => r.leagueName && r.zeroGoalsOdds && r.sevenGoalsOdds)
  if (valid.length === 0) {
    ElMessage.warning('请至少填写一条有效数据')
    return
  }
  try {
    await api.batchCreateExtremeGoalsOdds(valid)
    ElMessage.success(`成功批量录入 ${valid.length} 场两极进球赔率数据！`)
    batchModalVisible.value = false
    fetchData()
  } catch (err) {
    ElMessage.error('批量录入失败: ' + (err.message || '系统异常'))
  }
}

// 单场测算与进球数预测
const openSimulateModal = () => {
  simulateModalVisible.value = true
  runSimulate()
}

const runSimulate = async () => {
  simLoading.value = true
  try {
    const res = await api.simulateExtremeGoals({
      leagueName: simForm.value.leagueName,
      matchSystem: simForm.value.matchSystem,
      zeroGoalsOdds: simForm.value.zeroGoalsOdds,
      sevenGoalsOdds: simForm.value.sevenGoalsOdds,
      zeroTolerance: simForm.value.zeroTolerance,
      sevenTolerance: simForm.value.sevenTolerance
    })
    if (res && res.data) {
      simResult.value = res.data
    } else {
      computeLocalSimulate()
    }
  } catch (err) {
    console.warn('后端测算请求异常，启用本地历史匹配:', err)
    computeLocalSimulate()
  } finally {
    simLoading.value = false
  }
}

const computeLocalSimulate = () => {
  const z = Number(simForm.value.zeroGoalsOdds)
  const s = Number(simForm.value.sevenGoalsOdds)
  const zTol = Number(simForm.value.zeroTolerance)
  const sTol = Number(simForm.value.sevenTolerance)

  const matched = allRecords.value.filter(r => {
    const rZ = Number(r.zeroGoalsOdds)
    const rS = Number(r.sevenGoalsOdds)
    if (isNaN(rZ) || isNaN(rS)) return false
    if (Math.abs(rZ - z) > zTol) return false
    if (Math.abs(rS - s) > sTol) return false
    if (simForm.value.leagueName !== 'ALL' && !r.leagueName.includes(simForm.value.leagueName)) return false
    if (simForm.value.matchSystem !== 'ALL' && r.matchSystem !== simForm.value.matchSystem) return false
    return true
  })

  const count = matched.length
  if (count === 0) {
    simResult.value = {
      matchedCount: 0,
      avgGoalsInSample: '0.00',
      predZeroOneRate: '0.00',
      predTwoThreeRate: '0.00',
      predFourPlusRate: '0.00',
      topGoalsRanking: [],
      advice: '库内暂无相似两极赔率样本，建议适当放宽容差或补充录入。'
    }
    return
  }

  let zo = 0
  let tt = 0
  let fp = 0
  let sumG = 0
  const freq = {}

  matched.forEach(r => {
    const g = Number(r.totalGoals) || 0
    sumG += g
    if (g <= 1) zo++
    else if (g <= 3) tt++
    else fp++
    freq[g] = (freq[g] || 0) + 1
  })

  const sorted = Object.entries(freq).sort((a, b) => b[1] - a[1]).slice(0, 3)
  const topList = sorted.map(e => ({
    goalText: Number(e[0]) >= 7 ? '7+球' : `${e[0]}球`,
    count: e[1],
    rate: ((e[1] / count) * 100).toFixed(2)
  }))

  simResult.value = {
    matchedCount: count,
    avgGoalsInSample: (sumG / count).toFixed(2),
    predZeroOneRate: ((zo / count) * 100).toFixed(2),
    predTwoThreeRate: ((tt / count) * 100).toFixed(2),
    predFourPlusRate: ((fp / count) * 100).toFixed(2),
    topGoalsRanking: topList,
    advice: `历史同类赔率样本中，最常打出的是【${topList[0]?.goalText || '2球'}】（打出率 ${topList[0]?.rate || '50'}%）。`
  }
}

const handleResize = () => {
  scatterChartInstance?.resize()
  goalsBarChartInstance?.resize()
  steppedBarChartInstance?.resize()
  steppedTrendChartInstance?.resize()
}

onMounted(() => {
  fetchData()
  window.addEventListener('resize', handleResize)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
  scatterChartInstance?.dispose()
  goalsBarChartInstance?.dispose()
  steppedBarChartInstance?.dispose()
  steppedTrendChartInstance?.dispose()
})

watch([leagueFilter, systemFilter, timePreset, dateRange], () => {
  currentPage.value = 1
  fetchData()
})

// 监听步幅与视图变化，自动重新渲染步幅图表
watch([steppedStep, steppedTarget, steppedViewMode, onlyShowSampleBuckets, filteredMatches], () => {
  nextTick(() => {
    if (steppedViewMode.value !== 'table') {
      renderSteppedCharts()
    }
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

:deep(.custom-pagination .el-pager li) {
  background-color: #020617 !important;
  color: #94a3b8 !important;
  border: 1px solid #1e293b !important;
}

:deep(.custom-pagination .el-pager li.is-active) {
  background-color: #f59e0b !important;
  color: #020617 !important;
  font-weight: bold !important;
}

/* 步幅滑块暗黑科技风定制 */
:deep(.custom-stepped-slider .el-slider__runway) {
  background-color: #1e293b !important;
  height: 6px;
  border-radius: 4px;
}

:deep(.custom-stepped-slider .el-slider__bar) {
  background: linear-gradient(to right, #06b6d4, #f59e0b) !important;
  height: 6px;
  border-radius: 4px;
}

:deep(.custom-stepped-slider .el-slider__button) {
  border: 2px solid #f59e0b !important;
  background-color: #020617 !important;
  width: 16px;
  height: 16px;
  box-shadow: 0 0 10px rgba(245, 158, 11, 0.5) !important;
}

:deep(.custom-stepped-slider .el-slider__marks-text) {
  color: #64748b !important;
  font-size: 10px !important;
  margin-top: 6px;
  font-family: monospace;
}
</style>
