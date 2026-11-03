<template>
  <div
    class="relative inline-flex items-center justify-center select-none shrink-0 transition-transform duration-200"
    :class="[containerSizeClass, badgeClass]"
    :title="tooltipText"
  >
    <!-- Logo Image -->
    <img
      v-if="logoSrc && !hasError"
      :src="logoSrc"
      :alt="displayName"
      class="w-full h-full object-contain filter drop-shadow-md transition-all duration-300 group-hover:scale-105"
      :class="imagePaddingClass"
      loading="lazy"
      @error="handleImgError"
    />

    <!-- Fallback when logo not found or image error -->
    <div
      v-else
      class="w-full h-full rounded-xl flex items-center justify-center font-bold text-slate-300 bg-gradient-to-br from-slate-800 to-slate-900 border border-slate-700/80 shadow-inner"
      :class="fallbackTextSizeClass"
    >
      <span>{{ fallbackText }}</span>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { matchTeam, getTeamLogo } from '@/utils/teamLogos'

const props = defineProps({
  name: {
    type: String,
    default: ''
  },
  leagueCode: {
    type: String,
    default: null
  },
  size: {
    type: String,
    default: 'md', // xs | sm | md | lg | xl
    validator: (v) => ['xs', 'sm', 'md', 'lg', 'xl'].includes(v)
  },
  badge: {
    type: Boolean,
    default: true
  },
  showTooltip: {
    type: Boolean,
    default: true
  }
})

const hasError = ref(false)

// Reset error state when name changes
watch(() => props.name, () => {
  hasError.value = false
})

const matchedTeam = computed(() => {
  return matchTeam(props.name, props.leagueCode)
})

const logoSrc = computed(() => {
  if (matchedTeam.value) {
    return matchedTeam.value.logoUrl || matchedTeam.value.svgUrl || ''
  }
  return getTeamLogo(props.name, props.leagueCode)
})

const displayName = computed(() => {
  if (matchedTeam.value) {
    return `${matchedTeam.value.nameZh} (${matchedTeam.value.nameEn})`
  }
  return props.name || '球队'
})

const tooltipText = computed(() => {
  if (!props.showTooltip) return ''
  if (matchedTeam.value) {
    return `${matchedTeam.value.nameZh} · ${matchedTeam.value.nameEn} [${matchedTeam.value.leagueNameZh}]`
  }
  return props.name || '足球队'
})

const fallbackText = computed(() => {
  if (props.name && props.name.trim()) {
    // If Chinese, take first 1-2 characters; if English take 2-3 letters
    const trimmed = props.name.trim()
    if (/^[\u4e00-\u9fa5]/.test(trimmed)) {
      return trimmed.slice(0, 1)
    }
    return trimmed.slice(0, 2).toUpperCase()
  }
  return '⚽'
})

const handleImgError = () => {
  hasError.value = true
}

// Preset Sizes
const containerSizeClass = computed(() => {
  switch (props.size) {
    case 'xs':
      return 'w-6 h-6'
    case 'sm':
      return 'w-8 h-8 sm:w-9 sm:h-9'
    case 'md':
      return 'w-11 h-11 sm:w-13 sm:h-13'
    case 'lg':
      return 'w-14 h-14 sm:w-16 sm:h-16'
    case 'xl':
      return 'w-20 h-20 sm:w-24 sm:h-24'
    default:
      return 'w-11 h-11 sm:w-12 sm:h-12'
  }
})

const imagePaddingClass = computed(() => {
  if (!props.badge) return 'p-0.5'
  switch (props.size) {
    case 'xs':
      return 'p-0.5'
    case 'sm':
      return 'p-1'
    case 'md':
      return 'p-1.5'
    case 'lg':
      return 'p-2'
    case 'xl':
      return 'p-2.5'
    default:
      return 'p-1.5'
  }
})

const badgeClass = computed(() => {
  if (!props.badge) return ''
  return 'rounded-xl sm:rounded-2xl bg-gradient-to-br from-slate-900/90 via-slate-800/80 to-slate-900/90 border border-slate-700/60 shadow-lg shadow-black/30 backdrop-blur-sm hover:border-slate-500/80'
})

const fallbackTextSizeClass = computed(() => {
  switch (props.size) {
    case 'xs':
      return 'text-[10px]'
    case 'sm':
      return 'text-xs'
    case 'md':
      return 'text-sm font-extrabold'
    case 'lg':
      return 'text-base font-extrabold'
    case 'xl':
      return 'text-xl font-black'
    default:
      return 'text-sm'
  }
})
</script>
