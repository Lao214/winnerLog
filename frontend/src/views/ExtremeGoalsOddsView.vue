<template>
  <div class="space-y-6 sm:space-y-8 pb-12">
    <!-- Mode Switcher Tabs: 7 模式完整切换 -->
    <div class="flex items-center justify-between flex-wrap gap-3">
      <div class="flex items-center gap-1.5 p-1 rounded-2xl bg-slate-950/80 border border-slate-800 overflow-x-auto max-w-full">
        <router-link
          to="/daily-profit"
          class="px-3 py-2 rounded-xl text-xs sm:text-sm font-bold transition flex items-center gap-1.5 whitespace-nowrap text-slate-400 hover:text-white hover:bg-slate-900"
        >
          <span>⚽ 让球穿盘战绩版</span>
        </router-link>
        <router-link
          to="/daily-goals-profit"
          class="px-3 py-2 rounded-xl text-xs sm:text-sm font-bold transition flex items-center gap-1.5 whitespace-nowrap text-slate-400 hover:text-white hover:bg-slate-900"
        >
          <span>🥅 总进球数规律版</span>
        </router-link>
        <router-link
          to="/odds-water-profit"
          class="px-3 py-2 rounded-xl text-xs sm:text-sm font-bold transition flex items-center gap-1.5 whitespace-nowrap text-slate-400 hover:text-white hover:bg-slate-900"
        >
          <span>🌊 水位冷门翻车版</span>
        </router-link>
        <router-link
          to="/water-combo-profit"
          class="px-3 py-2 rounded-xl text-xs sm:text-sm font-bold transition flex items-center gap-1.5 whitespace-nowrap text-slate-400 hover:text-white hover:bg-slate-900"
        >
          <span>🎯 水位串子战绩版</span>
        </router-link>
        <router-link
          to="/handicap-odds-stats"
          class="px-3 py-2 rounded-xl text-xs sm:text-sm font-bold transition flex items-center gap-1.5 whitespace-nowrap text-slate-400 hover:text-white hover:bg-slate-900"
        >
          <span>📊 赔率六区间版</span>
        </router-link>
        <router-link
          to="/match-odds-stepped"
          class="px-3 py-2 rounded-xl text-xs sm:text-sm font-bold transition flex items-center gap-1.5 whitespace-nowrap text-slate-400 hover:text-white hover:bg-slate-900"
        >
          <span>🔬 单场精细步幅版</span>
        </router-link>
        <router-link
          to="/extreme-goals-odds"
          class="px-3.5 py-2 rounded-xl text-xs sm:text-sm font-bold transition flex items-center gap-1.5 whitespace-nowrap bg-gradient-to-r from-amber-500 to-orange-500 text-slate-950 shadow-md shadow-amber-500/20 font-extrabold"
        >
          <span>🎲 两极进球赔率版</span>
        </router-link>
      </div>

      <div class="text-xs text-slate-400 hidden lg:flex items-center gap-1.5">
        <span class="w-2 h-2 rounded-full bg-amber-400 animate-pulse"></span>
        <span>当前模式: 0球赔率与7球+赔率两极对照 · 各主流联赛全场总进球大数据规律分析</span>
      </div>
    </div>

    <!-- 1. Hero Banner -->
    <div class="relative overflow-hidden rounded-3xl bg-gradient-to-r from-slate-900 via-amber-950/20 to-slate-900 border border-slate-800 p-5 sm:p-8 shadow-2xl">
      <div class="absolute -right-10 -top-10 w-80 h-80 bg-amber-500/10 rounded-full blur-3xl pointer-events-none"></div>
      <div class="absolute -left-10 -bottom-10 w-80 h-80 bg-orange-500/10 rounded-full blur-3xl pointer-events-none"></div>

      <div class="relative z-10 flex flex-col lg:flex-row lg:items-center lg:justify-between gap-6">
        <div>
          <div class="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-amber-500/10 border border-amber-500/20 text-amber-400 text-xs font-bold mb-3">
            <span>🎲</span> 0球赔率 vs 7球+赔率 两极进球规律大数据分析
          </div>
          <h1 class="text-2xl sm:text-3xl md:text-4xl font-extrabold text-white tracking-tight flex items-center gap-3">
            两极进球赔率与主流联赛大数据统计版
          </h1>
          <p class="text-slate-400 text-xs sm:text-sm mt-2 max-w-2xl leading-relaxed">
            支持按天快速录入<strong>日职联、韩职K联、澳超、巴甲、葡超、挪超、荷甲、五大联赛、欧冠</strong>等主流赛事的 <strong>0球赔率</strong> 与 <strong>7球+赔率</strong> 及全场实际开奖总进球数。深度剖析机构对极致小球与超大球防范赔率与最终打出结果的相关性规律！
          </p>
        </div>

        <div class="bg-slate-950/80 p-4 rounded-2xl border border-slate-800 text-xs space-y-1.5 max-w-xs">
          <div class="font-bold text-amber-400 flex items-center gap-1.5">
            <span>💡 核心实战洞察</span>
          </div>
          <p class="text-slate-300 text-[11px] leading-relaxed">
            两极比值（7+赔率 / 0球赔率）是洞悉机构防守倾向的敏锐雷达：比值越小大球动能越强，比值越高小球防范越严密。
          </p>
        </div>
      </div>
    </div>

    <!-- 2. 大数据分析核心组件 -->
    <ExtremeGoalsOddsAnalysis />
  </div>
</template>

<script setup>
import ExtremeGoalsOddsAnalysis from '../components/ExtremeGoalsOddsAnalysis.vue'
</script>
