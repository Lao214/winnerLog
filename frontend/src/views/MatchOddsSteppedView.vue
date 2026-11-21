<template>
  <div class="space-y-6 sm:space-y-8 pb-12">
    <!-- Mode Switcher Tabs: 6 模式完整切换 -->
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
          class="px-3.5 py-2 rounded-xl text-xs sm:text-sm font-bold transition flex items-center gap-1.5 whitespace-nowrap text-slate-400 hover:text-white hover:bg-slate-900"
        >
          <span>📊 赔率六区间版</span>
        </router-link>
        <router-link
          to="/match-odds-stepped"
          class="px-3.5 py-2 rounded-xl text-xs sm:text-sm font-bold transition flex items-center gap-1.5 whitespace-nowrap bg-gradient-to-r from-emerald-500 to-teal-500 text-slate-950 shadow-md shadow-emerald-500/20 font-extrabold"
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
        <span class="w-2 h-2 rounded-full bg-emerald-400 animate-pulse"></span>
        <span>当前模式: 单场比赛胜赔开奖明细与多步幅（0.05/0.1/0.15/0.2）动态切分分析</span>
      </div>
    </div>

    <!-- 1. Hero Banner -->
    <div class="relative overflow-hidden rounded-3xl bg-gradient-to-r from-slate-900 via-teal-950/20 to-slate-900 border border-slate-800 p-5 sm:p-8 shadow-2xl">
      <div class="absolute -right-10 -top-10 w-80 h-80 bg-teal-500/10 rounded-full blur-3xl pointer-events-none"></div>
      <div class="absolute -left-10 -bottom-10 w-80 h-80 bg-emerald-500/10 rounded-full blur-3xl pointer-events-none"></div>

      <div class="relative z-10 flex flex-col lg:flex-row lg:items-center lg:justify-between gap-6">
        <div>
          <div class="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-emerald-500/10 border border-emerald-500/20 text-emerald-400 text-xs font-bold mb-3">
            <span>🔬</span> 精细化单场录入 & 多步幅动态切分分析
          </div>
          <h1 class="text-2xl sm:text-3xl md:text-4xl font-extrabold text-white tracking-tight flex items-center gap-3">
            单场胜赔与开奖明细多步幅分析版
          </h1>
          <p class="text-slate-400 text-xs sm:text-sm mt-2 max-w-2xl leading-relaxed">
            支持精细录入每场比赛的<strong>主胜/客胜赔率</strong>、<strong>实际开奖赛果</strong>、<strong>打出赔率</strong>、<strong>让球赔</strong>、<strong>半全场</strong>、<strong>总进球数</strong>及<strong>是否穿盘</strong>。在前端可自由操控按 <strong>0.05、0.10、0.15、0.20</strong> 步幅进行毫秒级动态分桶汇总分析！
          </p>
        </div>

        <div class="bg-slate-950/80 p-4 rounded-2xl border border-slate-800 text-xs space-y-1.5 max-w-xs">
          <div class="font-bold text-emerald-400 flex items-center gap-1.5">
            <span>⚡ 前端操控特性</span>
          </div>
          <p class="text-slate-300 text-[11px] leading-relaxed">
            无需后端重新统计，切换步幅按钮即刻动态分桶，胜平负分布、穿盘率与 ECharts 图表瞬间重算响应！
          </p>
        </div>
      </div>
    </div>

    <!-- 2. 单场明细与多步幅分析核心组件 -->
    <HandicapMatchSteppedAnalysis />
  </div>
</template>

<script setup>
import HandicapMatchSteppedAnalysis from '../components/HandicapMatchSteppedAnalysis.vue'
</script>
