<template>
  <el-dialog
    v-model="visible"
    title="录入比赛完场结果并结算"
    width="92%"
    class="max-w-[480px] custom-dialog"
    destroy-on-close
    :append-to-body="true"
  >
    <div v-if="match" class="py-1">
      <!-- Match Info Banner -->
      <div class="p-3 sm:p-3.5 rounded-xl bg-slate-900 border border-slate-800 text-center mb-4 sm:mb-5">
        <div class="text-[11px] sm:text-xs text-slate-400 mb-1.5">
          {{ match.leagueName || match.leagueCode }} • {{ match.seasonName }}
        </div>
        <div class="flex items-center justify-center gap-3 sm:gap-4 py-1">
          <div class="flex items-center gap-2 min-w-0">
            <TeamLogo :name="match.homeTeam" :league-code="match.leagueCode" size="sm" />
            <span class="text-sm sm:text-base font-bold text-emerald-400 truncate">{{ match.homeTeam }}</span>
          </div>
          <span class="text-slate-500 text-xs font-bold px-2 py-0.5 rounded bg-slate-800 shrink-0">VS</span>
          <div class="flex items-center gap-2 min-w-0">
            <span class="text-sm sm:text-base font-bold text-indigo-400 truncate">{{ match.awayTeam }}</span>
            <TeamLogo :name="match.awayTeam" :league-code="match.leagueCode" size="sm" />
          </div>
        </div>
      </div>

      <!-- Score Inputs -->
      <div class="space-y-3.5 sm:space-y-4">
        <div>
          <label class="block text-xs font-semibold text-slate-300 mb-1.5">完场全场比分 (主队 : 客队) *</label>
          <div class="grid grid-cols-2 gap-2.5 sm:gap-3">
            <el-input-number
              v-model="form.homeScore"
              :min="0"
              :max="30"
              placeholder="主队进球"
              class="!w-full"
            />
            <el-input-number
              v-model="form.awayScore"
              :min="0"
              :max="30"
              placeholder="客队进球"
              class="!w-full"
            />
          </div>
        </div>

        <div>
          <label class="block text-xs font-semibold text-slate-400 mb-1.5">半场比分 (可选)</label>
          <div class="grid grid-cols-2 gap-2.5 sm:gap-3">
            <el-input-number
              v-model="form.halfHomeScore"
              :min="0"
              :max="20"
              placeholder="半场主队"
              class="!w-full"
            />
            <el-input-number
              v-model="form.halfAwayScore"
              :min="0"
              :max="20"
              placeholder="半场客队"
              class="!w-full"
            />
          </div>
        </div>

        <div class="p-2.5 sm:p-3 rounded-lg bg-emerald-950/20 border border-emerald-500/20 text-[11px] sm:text-xs text-emerald-300 leading-relaxed">
          💡 提交后，系统将自动核对 GPT 与 通义千问 的预测红黑单、精准比分命中，并更新排行榜与连红纪录！
        </div>
      </div>
    </div>

    <template #footer>
      <div class="flex flex-col-reverse sm:flex-row justify-end gap-2 w-full pt-1">
        <el-button @click="visible = false" class="w-full sm:w-auto">取消</el-button>
        <el-button type="primary" :loading="loading" @click="handleSettle" class="w-full sm:w-auto">
          确认结算赛果
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, watch } from 'vue'
import { ElMessage } from 'element-plus'
import api from '@/api'
import TeamLogo from '@/components/TeamLogo.vue'

const props = defineProps({
  modelValue: Boolean,
  match: Object
})

const emit = defineEmits(['update:modelValue', 'settled'])

const visible = ref(props.modelValue)
const loading = ref(false)

const form = reactive({
  homeScore: 0,
  awayScore: 0,
  halfHomeScore: null,
  halfAwayScore: null
})

watch(() => props.modelValue, (val) => {
  visible.value = val
  if (val && props.match) {
    form.homeScore = props.match.homeScore ?? 0
    form.awayScore = props.match.awayScore ?? 0
    form.halfHomeScore = props.match.halfHomeScore ?? null
    form.halfAwayScore = props.match.halfAwayScore ?? null
  }
})

watch(visible, (val) => {
  emit('update:modelValue', val)
})

const handleSettle = async () => {
  if (form.homeScore === null || form.awayScore === null) {
    ElMessage.warning('请输入完整的主客队得分')
    return
  }

  loading.value = true
  try {
    const res = await api.settleMatch({
      matchId: props.match.id,
      homeScore: form.homeScore,
      awayScore: form.awayScore,
      halfHomeScore: form.halfHomeScore,
      halfAwayScore: form.halfAwayScore,
      status: 'FINISHED'
    })
    ElMessage.success('比赛结算成功！AI 胜率与排行榜已实时刷新')
    visible.value = false
    emit('settled', res)
  } catch (err) {
    // handled by axios interceptor
  } finally {
    loading.value = false
  }
}
</script>
