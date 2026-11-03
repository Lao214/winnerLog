<template>
  <div class="min-h-screen bg-[#0b0f19] text-slate-100 flex flex-col font-sans selection:bg-emerald-500 selection:text-white">
    <Navbar />
    <main class="flex-1 max-w-7xl w-full mx-auto px-3 sm:px-6 lg:px-8 pt-4 sm:pt-6 pb-24 md:pb-10">
      <router-view />
    </main>
    <footer class="hidden md:block border-t border-slate-900/80 bg-slate-950/40 py-6 text-center text-xs text-slate-500">
      <p>© 2025-2026 WinnerLog AI Sports Analytics • Powered by Spring Boot 3 & Vue 3</p>
    </footer>
  </div>
</template>

<script setup>
import Navbar from './components/Navbar.vue'
</script>
