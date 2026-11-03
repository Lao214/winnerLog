<template>
  <div class="space-y-6 pb-12 max-w-5xl mx-auto">
    <!-- Header -->
    <div class="glass-panel rounded-3xl p-4 sm:p-6 border border-slate-800 shadow-xl">
      <div class="flex items-center justify-between">
        <div>
          <h1 class="text-xl sm:text-2xl font-black text-white flex items-center gap-2">
            <span>📥</span> 预测导入与赛果录入工作台
          </h1>
          <p class="text-xs text-slate-400 mt-1">
            支持同时录入胜负、最多3个比分、最多3个总进球数预测，自动建档与结算。
          </p>
        </div>
      </div>
    </div>

    <!-- Main Tabs Card -->
    <div class="glass-card rounded-3xl p-4 sm:p-6 border border-slate-800 shadow-2xl">
      <el-tabs v-model="activeTab" class="custom-tabs">
        <!-- Tab 1: AI 预测录入 -->
        <el-tab-pane label="📝 导入比赛与AI预测" name="import">
          <div class="space-y-6 pt-2">
            <!-- Mode Switcher -->
            <div class="flex flex-wrap items-center gap-2 sm:gap-3 bg-slate-900/60 p-1.5 sm:p-2 rounded-xl border border-slate-800 w-full sm:w-fit">
              <button
                @click="importMode = 'form'"
                class="flex-1 sm:flex-none px-3.5 py-1.5 rounded-lg text-xs font-bold transition text-center"
                :class="importMode === 'form' ? 'bg-emerald-600 text-white shadow' : 'text-slate-400 hover:text-slate-200'"
              >
                可视化表单录入
              </button>
              <button
                @click="importMode = 'json'"
                class="flex-1 sm:flex-none px-3.5 py-1.5 rounded-lg text-xs font-bold transition text-center"
                :class="importMode === 'json' ? 'bg-emerald-600 text-white shadow' : 'text-slate-400 hover:text-slate-200'"
              >
                JSON / 文本粘贴导入
              </button>
            </div>

            <!-- Form Mode -->
            <div v-if="importMode === 'form'" class="space-y-5 sm:space-y-6">
              <!-- Basic Match Info -->
              <div class="p-4 sm:p-5 rounded-2xl bg-slate-900/50 border border-slate-800 space-y-4">
                <h3 class="text-sm font-bold text-slate-200 flex items-center gap-2">
                  <span>🏟️</span> 1. 比赛基础信息
                </h3>

                <div class="grid grid-cols-1 sm:grid-cols-3 gap-3 sm:gap-4">
                  <div>
                    <label class="block text-xs font-semibold text-slate-400 mb-1">所属联赛 *</label>
                    <el-select v-model="form.leagueCode" placeholder="选择联赛" class="w-full">
                      <el-option v-for="l in leagues" :key="l.code" :label="`${l.shortName} (${l.code})`" :value="l.code" />
                    </el-select>
                  </div>

                  <div>
                    <label class="block text-xs font-semibold text-slate-400 mb-1">所属赛季 *</label>
                    <el-input v-model="form.seasonName" placeholder="如 2026-2027" />
                  </div>

                  <div>
                    <label class="block text-xs font-semibold text-slate-400 mb-1">比赛轮次/阶段</label>
                    <el-input v-model="form.roundName" placeholder="如 第28轮 / 1/8决赛" />
                  </div>
                </div>

                <div class="grid grid-cols-1 sm:grid-cols-3 gap-3 sm:gap-4">
                  <!-- Home Team Input with Live Logo Badge -->
                  <div>
                    <div class="flex items-center justify-between mb-1">
                      <label class="block text-xs font-semibold text-slate-400">主队名称 *</label>
                      <button
                        type="button"
                        @click="openTeamPicker('home')"
                        class="text-[11px] text-emerald-400 hover:text-emerald-300 font-medium flex items-center gap-1 transition"
                      >
                        <span>🏆</span> 从联赛库选队
                      </button>
                    </div>

                    <el-autocomplete
                      v-model="form.homeTeam"
                      :fetch-suggestions="querySearchHome"
                      placeholder="如 阿森纳 / 皇马 / 拜仁"
                      clearable
                      class="w-full custom-autocomplete"
                      @select="handleSelectHome"
                    >
                      <template #default="{ item }">
                        <div class="flex items-center gap-2 py-1">
                          <TeamLogo :name="item.team.nameZh" size="xs" :badge="false" />
                          <span class="font-bold text-slate-100 text-xs">{{ item.team.nameZh }}</span>
                          <span class="text-[10px] text-slate-400 font-mono">{{ item.team.nameEn }}</span>
                          <span class="ml-auto text-[10px] px-1.5 py-0.2 rounded bg-slate-800 text-emerald-400 font-semibold">{{ item.team.leagueNameZh }}</span>
                        </div>
                      </template>
                    </el-autocomplete>

                    <!-- Matched Home Team Card -->
                    <div v-if="matchedHomeTeam" class="mt-2 p-2 rounded-xl bg-slate-800/60 border border-slate-700/60 flex items-center justify-between gap-2 text-xs">
                      <div class="flex items-center gap-2 min-w-0">
                        <TeamLogo :name="matchedHomeTeam.nameZh" size="sm" />
                        <div class="min-w-0">
                          <div class="font-bold text-slate-100 truncate flex items-center gap-1">
                            <span>{{ matchedHomeTeam.nameZh }}</span>
                            <span class="text-[10px] text-emerald-400 font-semibold">✓ 队标已就绪</span>
                          </div>
                          <div class="text-[10px] text-slate-400 truncate">{{ matchedHomeTeam.nameEn }} · {{ matchedHomeTeam.leagueNameZh }}</div>
                        </div>
                      </div>
                      <button
                        v-if="form.homeTeam !== matchedHomeTeam.nameZh"
                        type="button"
                        @click="form.homeTeam = matchedHomeTeam.nameZh"
                        class="text-[10px] px-2 py-0.5 rounded bg-emerald-500/20 text-emerald-300 hover:bg-emerald-500/30 font-medium shrink-0 transition"
                        title="填入规范中文名称"
                      >
                        填入规范名
                      </button>
                    </div>
                  </div>

                  <!-- Away Team Input with Live Logo Badge -->
                  <div>
                    <div class="flex items-center justify-between mb-1">
                      <label class="block text-xs font-semibold text-slate-400">客队名称 *</label>
                      <button
                        type="button"
                        @click="openTeamPicker('away')"
                        class="text-[11px] text-indigo-400 hover:text-indigo-300 font-medium flex items-center gap-1 transition"
                      >
                        <span>🏆</span> 从联赛库选队
                      </button>
                    </div>

                    <el-autocomplete
                      v-model="form.awayTeam"
                      :fetch-suggestions="querySearchAway"
                      placeholder="如 切尔西 / 巴萨 / 多特"
                      clearable
                      class="w-full custom-autocomplete"
                      @select="handleSelectAway"
                    >
                      <template #default="{ item }">
                        <div class="flex items-center gap-2 py-1">
                          <TeamLogo :name="item.team.nameZh" size="xs" :badge="false" />
                          <span class="font-bold text-slate-100 text-xs">{{ item.team.nameZh }}</span>
                          <span class="text-[10px] text-slate-400 font-mono">{{ item.team.nameEn }}</span>
                          <span class="ml-auto text-[10px] px-1.5 py-0.2 rounded bg-slate-800 text-indigo-400 font-semibold">{{ item.team.leagueNameZh }}</span>
                        </div>
                      </template>
                    </el-autocomplete>

                    <!-- Matched Away Team Card -->
                    <div v-if="matchedAwayTeam" class="mt-2 p-2 rounded-xl bg-slate-800/60 border border-slate-700/60 flex items-center justify-between gap-2 text-xs">
                      <div class="flex items-center gap-2 min-w-0">
                        <TeamLogo :name="matchedAwayTeam.nameZh" size="sm" />
                        <div class="min-w-0">
                          <div class="font-bold text-slate-100 truncate flex items-center gap-1">
                            <span>{{ matchedAwayTeam.nameZh }}</span>
                            <span class="text-[10px] text-indigo-400 font-semibold">✓ 队标已就绪</span>
                          </div>
                          <div class="text-[10px] text-slate-400 truncate">{{ matchedAwayTeam.nameEn }} · {{ matchedAwayTeam.leagueNameZh }}</div>
                        </div>
                      </div>
                      <button
                        v-if="form.awayTeam !== matchedAwayTeam.nameZh"
                        type="button"
                        @click="form.awayTeam = matchedAwayTeam.nameZh"
                        class="text-[10px] px-2 py-0.5 rounded bg-indigo-500/20 text-indigo-300 hover:bg-indigo-500/30 font-medium shrink-0 transition"
                        title="填入规范中文名称"
                      >
                        填入规范名
                      </button>
                    </div>
                  </div>

                  <div>
                    <label class="block text-xs font-semibold text-slate-400 mb-1">开球时间 *</label>
                    <el-date-picker
                      v-model="form.matchTime"
                      type="datetime"
                      format="YYYY-MM-DD HH:mm:ss"
                      value-format="YYYY-MM-DD HH:mm:ss"
                      placeholder="选择开赛时间"
                      class="!w-full"
                    />
                  </div>
                </div>

                <!-- Real-time Matchup VS Live Preview Banner -->
                <div v-if="form.homeTeam || form.awayTeam" class="p-3.5 sm:p-4 rounded-2xl bg-gradient-to-r from-emerald-950/20 via-slate-900 to-indigo-950/20 border border-slate-800 shadow-xl transition-all">
                  <div class="flex items-center justify-between text-[11px] pb-2 mb-2 border-b border-slate-800/80">
                    <span class="font-bold text-slate-300 flex items-center gap-1.5">
                      <span>⚽</span> 比赛双方队标实时渲染预览
                    </span>
                    <span v-if="matchedHomeTeam && matchedAwayTeam" class="text-emerald-400 font-bold flex items-center gap-1">
                      <span>✨</span> 双方队标已就绪
                    </span>
                    <span v-else class="text-slate-400 text-[10px]">
                      支持输入中文名或常见简称（如阿森纳、皇马、拜仁）
                    </span>
                  </div>

                  <div class="flex items-center justify-between py-1">
                    <!-- Home Team -->
                    <div class="flex-1 flex items-center gap-2.5 sm:gap-3 min-w-0">
                      <TeamLogo :name="form.homeTeam" :league-code="form.leagueCode" size="md" />
                      <div class="min-w-0">
                        <div class="text-[10px] text-slate-500 font-semibold uppercase tracking-wider">HOME 主队</div>
                        <div class="text-sm sm:text-base font-black text-slate-100 truncate">
                          {{ form.homeTeam || '待录入主队' }}
                        </div>
                        <div v-if="matchedHomeTeam" class="text-[11px] text-emerald-400 font-medium truncate">
                          {{ matchedHomeTeam.nameEn }} · {{ matchedHomeTeam.leagueNameZh }}
                        </div>
                        <div v-else-if="form.homeTeam" class="text-[10px] text-slate-500 truncate">
                          五大联赛库未识别对应队标
                        </div>
                      </div>
                    </div>

                    <!-- Center VS Badge -->
                    <div class="px-3 sm:px-6 flex flex-col items-center shrink-0">
                      <div class="text-xs sm:text-sm font-black text-slate-300 px-3 py-0.5 rounded-full bg-slate-800/80 border border-slate-700/80 shadow">
                        VS
                      </div>
                      <span class="text-[10px] text-slate-500 mt-1 font-mono" v-if="form.roundName">{{ form.roundName }}</span>
                    </div>

                    <!-- Away Team -->
                    <div class="flex-1 flex items-center justify-end gap-2.5 sm:gap-3 min-w-0 text-right">
                      <div class="min-w-0">
                        <div class="text-[10px] text-slate-500 font-semibold uppercase tracking-wider">AWAY 客队</div>
                        <div class="text-sm sm:text-base font-black text-slate-100 truncate">
                          {{ form.awayTeam || '待录入客队' }}
                        </div>
                        <div v-if="matchedAwayTeam" class="text-[11px] text-indigo-400 font-medium truncate">
                          {{ matchedAwayTeam.nameEn }} · {{ matchedAwayTeam.leagueNameZh }}
                        </div>
                        <div v-else-if="form.awayTeam" class="text-[10px] text-slate-500 truncate">
                          五大联赛库未识别对应队标
                        </div>
                      </div>
                      <TeamLogo :name="form.awayTeam" :league-code="form.leagueCode" size="md" />
                    </div>
                  </div>
                </div>
              </div>

              <!-- AI Predictions Inputs -->
              <div class="grid grid-cols-1 md:grid-cols-2 gap-4 sm:gap-5">
                <!-- GPT-4o Prediction Box -->
                <div class="p-4 sm:p-5 rounded-2xl bg-emerald-950/20 border border-emerald-500/30 space-y-4 shadow-lg shadow-emerald-950/20">
                  <div class="flex flex-col sm:flex-row sm:items-center justify-between gap-1.5">
                    <span class="font-extrabold text-sm text-emerald-400">🤖 OpenAI GPT-4o 预测</span>
                    <span class="text-[11px] px-2 py-0.5 rounded-full font-mono font-bold bg-emerald-500/20 text-emerald-300 border border-emerald-500/40 w-fit">
                      总选项: {{ gptTotalOptions }}项 | 初始利润: -{{ gptTotalOptions }}.00
                    </span>
                  </div>

                  <!-- Result & Result Odds (支持单选与双选不败) -->
                  <div>
                    <label class="block text-xs text-slate-300 mb-1.5 font-semibold">胜平负预测及赔率 (支持单选或双选不败)</label>
                    <div class="space-y-2">
                      <div v-for="(item, idx) in gptResultItems" :key="idx" class="flex items-center gap-1.5 sm:gap-2">
                        <el-select v-model="item.result" placeholder="选择赛果" class="!w-28 sm:!w-36 shrink-0">
                          <el-option label="主胜" value="HOME_WIN" />
                          <el-option label="平局" value="DRAW" />
                          <el-option label="客胜" value="AWAY_WIN" />
                        </el-select>
                        <span class="text-xs text-slate-400 font-mono">@</span>
                        <el-input-number v-model="item.odds" controls-position="right" :precision="2" :step="0.05" :min="1.01" placeholder="赔率" class="!w-36 sm:!w-44 shrink-0" />
                        <button v-if="gptResultItems.length > 1" @click="gptResultItems.splice(idx, 1)" class="text-rose-400 hover:text-rose-300 text-xs px-1.5 py-1">✕</button>
                      </div>
                      <button v-if="gptResultItems.length < 2" @click="gptResultItems.push({ result: 'DRAW', odds: 3.40 })" class="text-xs text-emerald-400 hover:text-emerald-300 font-semibold flex items-center gap-1 mt-1">
                        + 添加双选/不败选项 (每项计1个选项)
                      </button>
                    </div>
                  </div>

                  <!-- Scores with Odds -->
                  <div>
                    <label class="block text-xs text-slate-300 mb-1.5 font-semibold">预测比分及赔率 (最多3项，最高权重)</label>
                    <div class="space-y-2">
                      <div v-for="(item, idx) in gptScoreItems" :key="idx" class="flex items-center gap-1.5 sm:gap-2">
                        <el-input v-model="item.score" placeholder="如 2-1" class="!w-28 sm:!w-36 shrink-0" />
                        <span class="text-xs text-slate-400 font-mono">@</span>
                        <el-input-number v-model="item.odds" controls-position="right" :precision="2" :step="0.5" :min="1.01" placeholder="赔率" class="!w-36 sm:!w-44 shrink-0" />
                        <button v-if="gptScoreItems.length > 1" @click="gptScoreItems.splice(idx, 1)" class="text-rose-400 hover:text-rose-300 text-xs px-1.5 py-1">✕</button>
                      </div>
                      <button v-if="gptScoreItems.length < 3" @click="gptScoreItems.push({ score: '', odds: 7.00 })" class="text-xs text-emerald-400 hover:text-emerald-300 font-semibold flex items-center gap-1 mt-1">
                        + 添加比分选项 (最多3个)
                      </button>
                    </div>
                  </div>

                  <!-- Goals with Odds -->
                  <div>
                    <label class="block text-xs text-slate-300 mb-1.5 font-semibold">预测总进球数及赔率 (最多3项，次高权重)</label>
                    <div class="space-y-2">
                      <div v-for="(item, idx) in gptGoalItems" :key="idx" class="flex items-center gap-1.5 sm:gap-2">
                        <el-input-number v-model="item.goals" :min="0" :max="15" placeholder="进球" class="!w-28 sm:!w-36 shrink-0" />
                        <span class="text-xs text-slate-400 font-mono">@</span>
                        <el-input-number v-model="item.odds" controls-position="right" :precision="2" :step="0.2" :min="1.01" placeholder="赔率" class="!w-36 sm:!w-44 shrink-0" />
                        <button v-if="gptGoalItems.length > 1" @click="gptGoalItems.splice(idx, 1)" class="text-rose-400 hover:text-rose-300 text-xs px-1.5 py-1">✕</button>
                      </div>
                      <button v-if="gptGoalItems.length < 3" @click="gptGoalItems.push({ goals: 3, odds: 3.50 })" class="text-xs text-emerald-400 hover:text-emerald-300 font-semibold flex items-center gap-1 mt-1">
                        + 添加进球数选项 (最多3个)
                      </button>
                    </div>
                  </div>

                  <div>
                    <label class="block text-xs text-slate-400 mb-1">置信度 (0-100%)</label>
                    <el-input-number v-model="gptConfidence" :min="0" :max="100" :precision="1" class="w-full" />
                  </div>

                  <div>
                    <label class="block text-xs text-slate-400 mb-1">GPT 预测简述理由</label>
                    <el-input v-model="gptSummary" type="textarea" :rows="2" placeholder="输入核心预测理由..." />
                  </div>
                </div>

                <!-- Qwen Prediction Box -->
                <div class="p-4 sm:p-5 rounded-2xl bg-indigo-950/20 border border-indigo-500/30 space-y-4 shadow-lg shadow-indigo-950/20">
                  <div class="flex flex-col sm:flex-row sm:items-center justify-between gap-1.5">
                    <span class="font-extrabold text-sm text-indigo-400">🤖 通义千问 Qwen 预测</span>
                    <span class="text-[11px] px-2 py-0.5 rounded-full font-mono font-bold bg-indigo-500/20 text-indigo-300 border border-indigo-500/40 w-fit">
                      总选项: {{ qwenTotalOptions }}项 | 初始利润: -{{ qwenTotalOptions }}.00
                    </span>
                  </div>

                  <!-- Result & Result Odds -->
                  <div>
                    <label class="block text-xs text-slate-300 mb-1.5 font-semibold">胜平负预测及赔率 (支持单选或双选不败)</label>
                    <div class="space-y-2">
                      <div v-for="(item, idx) in qwenResultItems" :key="idx" class="flex items-center gap-1.5 sm:gap-2">
                        <el-select v-model="item.result" placeholder="选择赛果" class="!w-28 sm:!w-36 shrink-0">
                          <el-option label="主胜" value="HOME_WIN" />
                          <el-option label="平局" value="DRAW" />
                          <el-option label="客胜" value="AWAY_WIN" />
                        </el-select>
                        <span class="text-xs text-slate-400 font-mono">@</span>
                        <el-input-number v-model="item.odds" controls-position="right" :precision="2" :step="0.05" :min="1.01" placeholder="赔率" class="!w-36 sm:!w-44 shrink-0" />
                        <button v-if="qwenResultItems.length > 1" @click="qwenResultItems.splice(idx, 1)" class="text-rose-400 hover:text-rose-300 text-xs px-1.5 py-1">✕</button>
                      </div>
                      <button v-if="qwenResultItems.length < 2" @click="qwenResultItems.push({ result: 'DRAW', odds: 3.40 })" class="text-xs text-indigo-400 hover:text-indigo-300 font-semibold flex items-center gap-1 mt-1">
                        + 添加双选/不败选项 (每项计1个选项)
                      </button>
                    </div>
                  </div>

                  <!-- Scores with Odds -->
                  <div>
                    <label class="block text-xs text-slate-300 mb-1.5 font-semibold">预测比分及赔率 (最多3项，最高权重)</label>
                    <div class="space-y-2">
                      <div v-for="(item, idx) in qwenScoreItems" :key="idx" class="flex items-center gap-1.5 sm:gap-2">
                        <el-input v-model="item.score" placeholder="如 3-1" class="!w-28 sm:!w-36 shrink-0" />
                        <span class="text-xs text-slate-400 font-mono">@</span>
                        <el-input-number v-model="item.odds" controls-position="right" :precision="2" :step="0.5" :min="1.01" placeholder="赔率" class="!w-36 sm:!w-44 shrink-0" />
                        <button v-if="qwenScoreItems.length > 1" @click="qwenScoreItems.splice(idx, 1)" class="text-rose-400 hover:text-rose-300 text-xs px-1.5 py-1">✕</button>
                      </div>
                      <button v-if="qwenScoreItems.length < 3" @click="qwenScoreItems.push({ score: '', odds: 7.00 })" class="text-xs text-indigo-400 hover:text-indigo-300 font-semibold flex items-center gap-1 mt-1">
                        + 添加比分选项 (最多3个)
                      </button>
                    </div>
                  </div>

                  <!-- Goals with Odds -->
                  <div>
                    <label class="block text-xs text-slate-300 mb-1.5 font-semibold">预测总进球数及赔率 (最多3项，次高权重)</label>
                    <div class="space-y-2">
                      <div v-for="(item, idx) in qwenGoalItems" :key="idx" class="flex items-center gap-1.5 sm:gap-2">
                        <el-input-number v-model="item.goals" :min="0" :max="15" placeholder="进球" class="!w-28 sm:!w-36 shrink-0" />
                        <span class="text-xs text-slate-400 font-mono">@</span>
                        <el-input-number v-model="item.odds" controls-position="right" :precision="2" :step="0.2" :min="1.01" placeholder="赔率" class="!w-36 sm:!w-44 shrink-0" />
                        <button v-if="qwenGoalItems.length > 1" @click="qwenGoalItems.splice(idx, 1)" class="text-rose-400 hover:text-rose-300 text-xs px-1.5 py-1">✕</button>
                      </div>
                      <button v-if="qwenGoalItems.length < 3" @click="qwenGoalItems.push({ goals: 3, odds: 3.50 })" class="text-xs text-indigo-400 hover:text-indigo-300 font-semibold flex items-center gap-1 mt-1">
                        + 添加进球数选项 (最多3个)
                      </button>
                    </div>
                  </div>

                  <div>
                    <label class="block text-xs text-slate-400 mb-1">置信度 (0-100%)</label>
                    <el-input-number v-model="qwenConfidence" :min="0" :max="100" :precision="1" class="w-full" />
                  </div>

                  <div>
                    <label class="block text-xs text-slate-400 mb-1">千问 预测简述理由</label>
                    <el-input v-model="qwenSummary" type="textarea" :rows="2" placeholder="输入核心预测理由..." />
                  </div>
                </div>
              </div>

              <!-- Submit Form Button -->
              <div class="flex justify-end pt-2">
                <el-button type="primary" size="large" :loading="importing" class="w-full sm:w-auto" @click="handleFormSubmit">
                  🚀 确认导入并建档
                </el-button>
              </div>
            </div>

            <!-- JSON Mode -->
            <div v-else class="space-y-4">
              <div class="flex flex-col sm:flex-row sm:items-center justify-between gap-2">
                <span class="text-xs text-slate-400">直接粘贴从 GPT/千问 输出的标准 JSON 数据 (支持输入赔率)：</span>
                <el-button size="small" class="w-full sm:w-auto" @click="loadSampleJson">
                  📋 加载示例 JSON (含赔率与3个比分/进球数)
                </el-button>
              </div>

              <!-- JSON Live Parsed Teams Preview -->
              <div v-if="jsonPreviewMatches.length > 0" class="p-3 sm:p-4 rounded-2xl bg-slate-900/90 border border-slate-800 space-y-2">
                <div class="text-xs font-bold text-slate-300 flex items-center justify-between">
                  <span class="flex items-center gap-1.5">
                    <span>✨</span> JSON 解析比赛与队标实时呈现 (共 {{ jsonPreviewMatches.length }} 场)
                  </span>
                  <span class="text-[10px] px-2 py-0.5 rounded-full bg-emerald-500/20 text-emerald-400 border border-emerald-500/30">
                    JSON 语法有效
                  </span>
                </div>
                <div class="grid grid-cols-1 md:grid-cols-2 gap-2.5">
                  <div
                    v-for="(m, idx) in jsonPreviewMatches"
                    :key="idx"
                    class="p-2.5 rounded-xl bg-slate-950/70 border border-slate-800 flex items-center justify-between text-xs"
                  >
                    <div class="flex items-center gap-2 min-w-0">
                      <TeamLogo :name="m.homeTeam" size="xs" />
                      <span class="font-bold text-slate-200 truncate">{{ m.homeTeam }}</span>
                    </div>
                    <span class="text-slate-500 font-mono text-[11px] px-2">VS</span>
                    <div class="flex items-center gap-2 min-w-0">
                      <span class="font-bold text-slate-200 truncate">{{ m.awayTeam }}</span>
                      <TeamLogo :name="m.awayTeam" size="xs" />
                    </div>
                  </div>
                </div>
              </div>

              <el-input
                v-model="jsonText"
                type="textarea"
                :rows="12"
                placeholder="请粘贴导入 JSON 数据..."
                class="font-mono text-xs"
              />

              <div class="flex justify-end">
                <el-button type="primary" size="large" :loading="importing" class="w-full sm:w-auto" @click="handleJsonSubmit">
                  ⚡ 解析并一键导入
                </el-button>
              </div>
            </div>
          </div>
        </el-tab-pane>

        <!-- Tab 2: 快捷测试数据生成 -->
        <el-tab-pane label="🧪 一键注入演示数据" name="seed">
          <div class="p-4 sm:p-8 text-center space-y-4">
            <div class="text-4xl">⚡</div>
            <h3 class="text-base font-bold text-white">快速注入测试比赛与结算数据</h3>
            <p class="text-xs text-slate-400 max-w-md mx-auto">
              一键注入包含胜负、最多3个比分、最多3个进球数的经典赛事预测与真实赛果，并自动更新胜负、比分、进球数排行榜。
            </p>
            <el-button type="success" size="large" :loading="seeding" class="w-full sm:w-auto" @click="handleInjectDemoData">
              注入经典测试数据
            </el-button>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <!-- 五大联赛俱乐部快捷选择弹窗 -->
    <el-dialog
      v-model="teamPickerVisible"
      :title="`选择${pickerTarget === 'home' ? '主场球队 (主队)' : '客场球队 (客队)'}`"
      width="92%"
      class="max-w-2xl custom-dialog"
      destroy-on-close
    >
      <div class="space-y-4 py-1">
        <!-- Search and League Filter -->
        <div class="flex flex-col sm:flex-row gap-2.5">
          <el-input
            v-model="pickerSearch"
            placeholder="搜索球队中文名/英文/别名 (如 阿森纳 / 皇马 / 拜仁)..."
            clearable
            class="flex-1"
          >
            <template #prefix>🔍</template>
          </el-input>
          <el-select v-model="pickerLeague" placeholder="筛选联赛" class="w-full sm:w-44">
            <el-option label="全部联赛 (114队)" value="" />
            <el-option v-for="l in LEAGUES" :key="l.code" :label="`${l.icon} ${l.nameZh} (${l.code})`" :value="l.code" />
          </el-select>
        </div>

        <!-- League Tabs Pills -->
        <div class="flex items-center gap-1.5 overflow-x-auto pb-1 scrollbar-none">
          <button
            type="button"
            @click="pickerLeague = ''"
            class="px-2.5 py-1 rounded-lg text-xs font-bold transition whitespace-nowrap"
            :class="pickerLeague === '' ? 'bg-emerald-600 text-white' : 'bg-slate-800 text-slate-400 hover:text-slate-200'"
          >
            全部
          </button>
          <button
            v-for="l in LEAGUES"
            :key="l.code"
            type="button"
            @click="pickerLeague = l.code"
            class="px-2.5 py-1 rounded-lg text-xs font-bold transition whitespace-nowrap flex items-center gap-1"
            :class="pickerLeague === l.code ? 'bg-emerald-600 text-white' : 'bg-slate-800 text-slate-400 hover:text-slate-200'"
          >
            <span>{{ l.icon }}</span>
            <span>{{ l.nameZh }}</span>
          </button>
        </div>

        <!-- Team Grid -->
        <div class="max-h-[360px] overflow-y-auto pr-1 grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 gap-2.5">
          <div
            v-for="team in filteredPickerTeams"
            :key="team.slug"
            @click="selectTeamFromPicker(team)"
            class="p-2.5 rounded-xl bg-slate-900/90 hover:bg-slate-800 border border-slate-800 hover:border-emerald-500/50 cursor-pointer transition flex flex-col items-center text-center group shadow-sm"
          >
            <TeamLogo :name="team.nameZh" size="sm" />
            <span class="mt-1.5 text-xs font-bold text-slate-200 group-hover:text-emerald-400 truncate w-full">{{ team.nameZh }}</span>
            <span class="text-[10px] text-slate-400 truncate w-full font-mono">{{ team.nameEn }}</span>
            <span class="mt-1 text-[9px] px-1.5 py-0.2 rounded bg-slate-950 text-slate-400 border border-slate-800">{{ team.leagueNameZh }}</span>
          </div>
          <div v-if="filteredPickerTeams.length === 0" class="col-span-full py-8 text-center text-xs text-slate-400">
            未检索到匹配的球队
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import api from '@/api'
import TeamLogo from '@/components/TeamLogo.vue'
import { matchTeam, searchTeams, LEAGUES } from '@/utils/teamLogos'

const activeTab = ref('import')
const importMode = ref('form')
const importing = ref(false)
const seeding = ref(false)
const leagues = ref([])

const form = reactive({
  leagueCode: 'PL',
  seasonName: '2026-2027',
  roundName: '第5轮',
  homeTeam: '',
  awayTeam: '',
  matchTime: '',
  venue: ''
})

// 球队匹配与队标实时解析
const matchedHomeTeam = computed(() => {
  return matchTeam(form.homeTeam, form.leagueCode)
})

const matchedAwayTeam = computed(() => {
  return matchTeam(form.awayTeam, form.leagueCode)
})

const querySearchHome = (queryString, cb) => {
  const results = searchTeams(queryString, form.leagueCode, 15)
  cb(results.map(t => ({ value: t.nameZh, team: t })))
}

const querySearchAway = (queryString, cb) => {
  const results = searchTeams(queryString, form.leagueCode, 15)
  cb(results.map(t => ({ value: t.nameZh, team: t })))
}

const handleSelectHome = (item) => {
  form.homeTeam = item.value
}

const handleSelectAway = (item) => {
  form.awayTeam = item.value
}

// 球队库快捷弹窗选择
const teamPickerVisible = ref(false)
const pickerTarget = ref('home') // 'home' | 'away'
const pickerLeague = ref('PL')
const pickerSearch = ref('')

const openTeamPicker = (target) => {
  pickerTarget.value = target
  pickerLeague.value = form.leagueCode || 'PL'
  pickerSearch.value = ''
  teamPickerVisible.value = true
}

const selectTeamFromPicker = (team) => {
  if (pickerTarget.value === 'home') {
    form.homeTeam = team.nameZh
  } else {
    form.awayTeam = team.nameZh
  }
  teamPickerVisible.value = false
  ElMessage.success(`已选定${pickerTarget.value === 'home' ? '主队' : '客队'}：${team.nameZh}`)
}

const filteredPickerTeams = computed(() => {
  return searchTeams(pickerSearch.value, pickerLeague.value, 60)
})

// GPT Form State
const gptResultItems = ref([
  { result: 'HOME_WIN', odds: 1.90 }
])
const gptScoreItems = ref([
  { score: '2-1', odds: 8.50 },
  { score: '1-0', odds: 6.00 },
  { score: '2-0', odds: 7.00 }
])
const gptGoalItems = ref([
  { goals: 2, odds: 3.20 },
  { goals: 3, odds: 3.80 },
  { goals: 4, odds: 5.50 }
])
const gptConfidence = ref(82.5)
const gptSummary = ref('阿森纳主场战绩强势，看好胜出')

const gptTotalOptions = computed(() => {
  return gptResultItems.value.length + gptScoreItems.value.length + gptGoalItems.value.length
})

// Qwen Form State
const qwenResultItems = ref([
  { result: 'HOME_WIN', odds: 1.90 }
])
const qwenScoreItems = ref([
  { score: '3-1', odds: 12.00 },
  { score: '3-0', odds: 10.00 },
  { score: '4-1', odds: 18.00 }
])
const qwenGoalItems = ref([
  { goals: 3, odds: 3.80 },
  { goals: 4, odds: 5.50 },
  { goals: 5, odds: 9.00 }
])
const qwenConfidence = ref(78.0)
const qwenSummary = ref('千问大数据看好进攻端优势')

const qwenTotalOptions = computed(() => {
  return qwenResultItems.value.length + qwenScoreItems.value.length + qwenGoalItems.value.length
})

const jsonText = ref('')

const jsonPreviewMatches = computed(() => {
  if (!jsonText.value || !jsonText.value.trim()) return []
  try {
    const data = JSON.parse(jsonText.value)
    if (data.matches && Array.isArray(data.matches)) {
      return data.matches.filter(m => m.homeTeam && m.awayTeam)
    }
    if (data.homeTeam && data.awayTeam) {
      return [data]
    }
  } catch (e) {}
  return []
})

const sampleJsonData = {
  leagueCode: "PL",
  seasonName: "2024-2025",
  roundName: "第28轮",
  homeTeam: "阿森纳",
  awayTeam: "切尔西",
  matchTime: "2025-03-15 23:00:00",
  venue: "酋长球场",
  predictions: [
    {
      modelCode: "GPT",
      resultItems: [
        { result: "HOME_WIN", odds: 1.90 },
        { result: "DRAW", odds: 3.40 }
      ],
      scoreItems: [
        { score: "2-1", odds: 8.50 },
        { score: "1-0", odds: 6.00 },
        { score: "2-0", odds: 7.00 }
      ],
      goalItems: [
        { goals: 2, odds: 3.20 },
        { goals: 3, odds: 3.80 },
        { goals: 4, odds: 5.50 }
      ],
      confidence: 82.5,
      analysisSummary: "阿森纳主场强势，双选不败(8项选项，初始利润-8.00)"
    },
    {
      modelCode: "QWEN",
      predictedResult: "HOME_WIN",
      resultOdds: 1.90,
      scoreItems: [
        { score: "3-1", odds: 12.00 },
        { score: "3-0", odds: 10.00 },
        { score: "4-1", odds: 18.00 }
      ],
      goalItems: [
        { goals: 3, odds: 3.80 },
        { goals: 4, odds: 5.50 },
        { goals: 5, odds: 9.00 }
      ],
      confidence: 78.0,
      analysisSummary: "千问看好大比分胜出(7项选项，初始利润-7.00)"
    }
  ]
}

const loadSampleJson = () => {
  jsonText.value = JSON.stringify(sampleJsonData, null, 2)
}

const handleFormSubmit = async () => {
  if (!form.leagueCode || !form.homeTeam || !form.awayTeam || !form.matchTime) {
    ElMessage.warning('请补全联赛、主客队与比赛时间等必填项')
    return
  }

  const payload = {
    ...form,
    predictions: [
      {
        modelCode: 'GPT',
        resultItems: gptResultItems.value.filter(r => r.result),
        scoreItems: gptScoreItems.value.filter(s => s.score && s.score.trim()),
        goalItems: gptGoalItems.value.filter(g => g.goals !== undefined && g.goals !== null),
        confidence: gptConfidence.value,
        analysisSummary: gptSummary.value
      },
      {
        modelCode: 'QWEN',
        resultItems: qwenResultItems.value.filter(r => r.result),
        scoreItems: qwenScoreItems.value.filter(s => s.score && s.score.trim()),
        goalItems: qwenGoalItems.value.filter(g => g.goals !== undefined && g.goals !== null),
        confidence: qwenConfidence.value,
        analysisSummary: qwenSummary.value
      }
    ]
  }

  importing.value = true
  try {
    await api.importSinglePrediction(payload)
    ElMessage.success('比赛预测与赔率导入建档成功！')
    form.homeTeam = ''
    form.awayTeam = ''
  } catch (err) {
    // error handled
  } finally {
    importing.value = false
  }
}

const handleJsonSubmit = async () => {
  if (!jsonText.value.trim()) {
    ElMessage.warning('请输入待导入的 JSON 文本')
    return
  }

  let payload
  try {
    payload = JSON.parse(jsonText.value)
  } catch (e) {
    ElMessage.error('JSON 格式错误，请检查语法')
    return
  }

  importing.value = true
  try {
    if (payload.matches && Array.isArray(payload.matches)) {
      await api.batchImportPredictions(payload)
      ElMessage.success('批量导入成功！')
    } else {
      await api.importSinglePrediction(payload)
      ElMessage.success('单场比赛预测与赔率导入建档成功！')
    }
  } catch (err) {
    // error handled
  } finally {
    importing.value = false
  }
}

const handleInjectDemoData = async () => {
  seeding.value = true
  try {
    // 注入欧洲冠军联赛 (皇马 VS 拜仁)
    // GPT 预测不败双选(主胜1.90+平局3.40=2项) + 3比分 + 3进球 = 8项 (初始利润-8.00)
    // 命中比分 2-1(6.50) + 进球数 3(3.50) = 10.00返还 -> 最终利润 +2.00！等级1(🎯 精准比分)
    const match1 = await api.importSinglePrediction({
      leagueCode: 'UCL',
      seasonName: '2024-2025',
      roundName: '1/4决赛',
      homeTeam: '皇家马德里',
      awayTeam: '拜仁慕尼黑',
      matchTime: '2025-04-01 20:00:00',
      venue: '伯纳乌球场',
      predictions: [
        {
          modelCode: 'GPT',
          resultItems: [
            { result: 'AWAY_WIN', odds: 3.20 },
            { result: 'DRAW', odds: 3.40 }
          ],
          scoreItems: [
            { score: '2-1', odds: 6.50 },
            { score: '1-1', odds: 7.00 },
            { score: '0-0', odds: 9.00 }
          ],
          goalItems: [
            { goals: 3, odds: 3.50 },
            { goals: 2, odds: 3.40 },
            { goals: 4, odds: 5.00 }
          ],
          confidence: 85.0,
          analysisSummary: '预测防守僵持，小比分防平，选项总计8项(初始利润-8)'
        },
        {
          modelCode: 'QWEN',
          predictedResult: 'AWAY_WIN',
          resultOdds: 3.00,
          scoreItems: [
            { score: '0-3', odds: 25.00 },
            { score: '1-2', odds: 12.00 },
            { score: '0-2', odds: 15.00 }
          ],
          goalItems: [
            { goals: 3, odds: 3.80 },
            { goals: 5, odds: 9.00 },
            { goals: 6, odds: 18.00 }
          ],
          confidence: 70.0,
          analysisSummary: '看好拜仁客场反扑进球'
        }
      ]
    })

    // 结算欧冠 (2-1，总进球=3)
    await api.settleMatch({
      matchId: match1.id,
      homeScore: 2,
      awayScore: 1,
      status: 'FINISHED'
    })

    // 注入英超比赛 (阿森纳 VS 切尔西)，带详细赔率
    const match2 = await api.importSinglePrediction({
      leagueCode: 'PL',
      seasonName: '2024-2025',
      roundName: '第28轮',
      homeTeam: '阿森纳',
      awayTeam: '切尔西',
      matchTime: '2025-03-15 23:00:00',
      venue: '酋长球场',
      predictions: [
        {
          modelCode: 'GPT',
          predictedResult: 'HOME_WIN',
          resultOdds: 1.90,
          scoreItems: [
            { score: '2-1', odds: 8.50 },
            { score: '1-0', odds: 6.00 },
            { score: '2-0', odds: 7.00 }
          ],
          goalItems: [
            { goals: 2, odds: 3.20 },
            { goals: 3, odds: 3.80 },
            { goals: 4, odds: 5.50 }
          ],
          confidence: 82.5,
          analysisSummary: '主场强势，看好小胜'
        },
        {
          modelCode: 'QWEN',
          predictedResult: 'HOME_WIN',
          resultOdds: 1.90,
          scoreItems: [
            { score: '3-1', odds: 12.00 },
            { score: '3-0', odds: 10.00 },
            { score: '4-1', odds: 18.00 }
          ],
          goalItems: [
            { goals: 3, odds: 3.80 },
            { goals: 4, odds: 5.50 },
            { goals: 5, odds: 9.00 }
          ],
          confidence: 78.0,
          analysisSummary: '大数据推演进攻占优'
        }
      ]
    })

    // 结算英超比赛 (2-1，总进球=3)
    await api.settleMatch({
      matchId: match2.id,
      homeScore: 2,
      awayScore: 1,
      status: 'FINISHED'
    })

    // 注入西甲比赛 (皇马 VS 巴萨)，带详细赔率
    const match3 = await api.importSinglePrediction({
      leagueCode: 'LA_LIGA',
      seasonName: '2024-2025',
      roundName: '第29轮',
      homeTeam: '皇家马德里',
      awayTeam: '巴塞罗那',
      matchTime: '2025-03-16 21:00:00',
      venue: '伯纳乌球场',
      predictions: [
        {
          modelCode: 'GPT',
          predictedResult: 'HOME_WIN',
          resultOdds: 2.10,
          scoreItems: [
            { score: '2-1', odds: 8.00 },
            { score: '2-0', odds: 11.00 },
            { score: '1-0', odds: 9.00 }
          ],
          goalItems: [
            { goals: 2, odds: 3.50 },
            { goals: 3, odds: 4.00 },
            { goals: 1, odds: 4.50 }
          ],
          confidence: 65.0,
          analysisSummary: '看好皇马主场'
        },
        {
          modelCode: 'QWEN',
          predictedResult: 'AWAY_WIN',
          resultOdds: 3.20,
          scoreItems: [
            { score: '0-4', odds: 45.00 },
            { score: '1-3', odds: 22.00 },
            { score: '1-2', odds: 11.00 }
          ],
          goalItems: [
            { goals: 4, odds: 5.50 },
            { goals: 3, odds: 4.00 },
            { goals: 5, odds: 8.50 }
          ],
          confidence: 72.0,
          analysisSummary: '巴萨反击犀利'
        }
      ]
    })

    // 结算西甲比赛 (0-4，总进球=4)
    await api.settleMatch({
      matchId: match3.id,
      homeScore: 0,
      awayScore: 4,
      status: 'FINISHED'
    })

    ElMessage.success('测试数据注入完成！8选项(-8初始利润)、返还10利润+2、重要性等级(比分>进球>胜负)均已同步！')
  } catch (e) {
    ElMessage.error('注入失败: ' + e.message)
  } finally {
    seeding.value = false
  }
}

onMounted(async () => {
  try {
    const list = await api.getLeagues()
    if (list) leagues.value = list
  } catch (e) {}
})
</script>
