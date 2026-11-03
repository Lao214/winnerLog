<template>
  <div>
    <!-- Desktop & Mobile Top Header -->
    <header class="sticky top-0 z-40 backdrop-blur-xl bg-slate-950/85 border-b border-slate-800/80 shadow-lg">
      <div class="max-w-7xl mx-auto px-3 sm:px-6 lg:px-8">
        <div class="flex items-center justify-between h-14 sm:h-16">
          <!-- Logo -->
          <router-link to="/" class="flex items-center gap-2.5 sm:gap-3 group">
            <div class="w-8 h-8 sm:w-10 sm:h-10 rounded-xl bg-gradient-to-tr from-emerald-500 to-teal-400 flex items-center justify-center shadow-lg shadow-emerald-500/20 text-base sm:text-xl font-bold text-slate-950 group-hover:scale-105 transition-transform">
              ⚽
            </div>
            <div>
              <div class="flex items-center gap-1.5 sm:gap-2">
                <span class="text-lg sm:text-xl font-extrabold tracking-tight bg-gradient-to-r from-white via-slate-100 to-emerald-400 bg-clip-text text-transparent">胜负手</span>
                <span class="text-[9px] sm:text-[10px] uppercase font-bold tracking-widest px-1.5 py-0.5 rounded bg-emerald-500/10 text-emerald-400 border border-emerald-500/20">AI Sports</span>
              </div>
              <p class="text-[10px] sm:text-xs text-slate-400 hidden sm:block">GPT & 通义千问 足球赛事预测胜率战报</p>
            </div>
          </router-link>

          <!-- Desktop Nav Links -->
          <nav class="hidden md:flex items-center gap-1.5 lg:gap-2">
            <router-link
              v-for="item in navItems"
              :key="item.path"
              :to="item.path"
              class="px-3.5 py-2 rounded-xl text-sm font-medium transition-all duration-200 flex items-center gap-2"
              :class="[
                isItemActive(item.path)
                  ? 'bg-emerald-500/15 text-emerald-400 border border-emerald-500/30 shadow-sm shadow-emerald-500/10 font-bold'
                  : 'text-slate-300 hover:text-white hover:bg-slate-800/60'
              ]"
            >
              <span>{{ item.icon }}</span>
              <span>{{ item.title }}</span>
            </router-link>
          </nav>

          <!-- Right Action (Quick Import on desktop and mobile) -->
          <div class="flex items-center gap-2">
            <router-link
              to="/admin"
              class="inline-flex items-center gap-1 text-xs font-semibold px-2.5 sm:px-3 py-1.5 rounded-lg bg-emerald-600/90 hover:bg-emerald-500 text-white shadow-sm shadow-emerald-600/30 transition"
            >
              <span>➕</span>
              <span class="hidden sm:inline">导入预测</span>
              <span class="sm:hidden">导入</span>
            </router-link>
          </div>
        </div>
      </div>
    </header>

    <!-- Mobile Bottom Navigation Bar (Fixed at bottom for smartphones) -->
    <nav class="md:hidden fixed bottom-0 left-0 right-0 z-50 bg-slate-950/95 backdrop-blur-xl border-t border-slate-800/90 shadow-2xl safe-area-bottom">
      <div class="grid grid-cols-5 h-15 px-1 items-center">
        <router-link
          v-for="item in navItems"
          :key="item.path"
          :to="item.path"
          class="flex flex-col items-center justify-center py-1.5 px-1 rounded-xl transition-all duration-200"
          :class="[
            isItemActive(item.path)
              ? 'text-emerald-400 font-bold'
              : 'text-slate-400 hover:text-slate-200'
          ]"
        >
          <span class="text-lg leading-none transition-transform" :class="{ 'scale-110 -translate-y-0.5': isItemActive(item.path) }">
            {{ item.icon }}
          </span>
          <span class="text-[10px] mt-1 font-medium tracking-tight">
            {{ item.mobileTitle || item.title }}
          </span>
          <span
            v-if="isItemActive(item.path)"
            class="w-4 h-0.5 rounded-full bg-emerald-400 mt-0.5 shadow-sm shadow-emerald-400"
          ></span>
        </router-link>
      </div>
    </nav>
  </div>
</template>

<script setup>
import { useRoute } from 'vue-router'

const route = useRoute()

const navItems = [
  { title: '胜率风云榜', mobileTitle: '胜率榜', path: '/', icon: '🏆' },
  { title: '比赛大厅', mobileTitle: '比赛', path: '/matches', icon: '⚽' },
  { title: 'GPT vs 千问', mobileTitle: '模型PK', path: '/comparison', icon: '⚔️' },
  { title: '每日盈亏', mobileTitle: '每日盈亏', path: '/daily-profit', icon: '💰' },
  { title: '录入工作台', mobileTitle: '工作台', path: '/admin', icon: '⚙️' }
]

const isItemActive = (path) => {
  if (path === '/daily-profit') {
    return route.path === '/daily-profit' || route.path === '/daily-goals-profit' || route.path === '/odds-water-profit' || route.path === '/odds-water-record' || route.path === '/water-combo-profit' || route.path === '/daily-water-combo'
  }
  return route.path === path
}
</script>

<style scoped>
.safe-area-bottom {
  padding-bottom: max(0.25rem, env(safe-area-inset-bottom));
}
</style>
