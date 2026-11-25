import client from './client'

export const api = {
  // 1. 基础字典接口
  getLeagues: (params) => client.get('/leagues', { params }),
  getSeasons: (leagueCode) => client.get('/seasons', { params: { leagueCode } }),
  getAiModels: () => client.get('/ai-models'),

  // 2. 预测导入接口
  importSinglePrediction: (data) => client.post('/predictions/import', data),
  batchImportPredictions: (data) => client.post('/predictions/batch-import', data),

  // 3. 比赛与结算接口
  queryMatches: (params) => client.get('/matches', { params }),
  getMatchDetail: (id) => client.get(`/matches/${id}`),
  settleMatch: (data) => client.post('/matches/settle', data),

  // 4. 排行榜接口
  getLeaderboard: (params) => client.get('/leaderboard', { params }),

  // 5. 模型对比统计接口
  getAiComparison: (params) => client.get('/statistics/ai-comparison', { params }),

  // 6. 每日成本利润与大数据分析接口 (让球穿盘版)
  getDailyProfits: (params) => client.get('/daily-profit', { params }),
  getDailyProfitPage: (params) => client.get('/daily-profit/page', { params }),
  getDailyProfitSummary: (params) => client.get('/daily-profit/summary', { params }),
  getDailyProfitDetail: (id) => client.get(`/daily-profit/${id}`),
  createDailyProfit: (data) => client.post('/daily-profit', data),
  updateDailyProfit: (id, data) => client.put(`/daily-profit/${id}`, data),
  deleteDailyProfit: (id) => client.delete(`/daily-profit/${id}`),

  // 7. 每日总进球数成本利润与规律大数据分析接口 (总进球数版)
  getDailyGoalsProfits: (params) => client.get('/daily-goals-profit', { params }),
  getDailyGoalsProfitPage: (params) => client.get('/daily-goals-profit/page', { params }),
  getDailyGoalsProfitSummary: (params) => client.get('/daily-goals-profit/summary', { params }),
  getDailyGoalsProfitDetail: (id) => client.get(`/daily-goals-profit/${id}`),
  createDailyGoalsProfit: (data) => client.post('/daily-goals-profit', data),
  updateDailyGoalsProfit: (id, data) => client.put(`/daily-goals-profit/${id}`, data),
  deleteDailyGoalsProfit: (id) => client.delete(`/daily-goals-profit/${id}`),

  // 8. 盘口水位冷门翻车与大数据分析接口 (水位冷门版)
  getOddsWaterRecords: (params) => client.get('/odds-water-record', { params }),
  getOddsWaterRecordPage: (params) => client.get('/odds-water-record/page', { params }),
  getOddsWaterRecordSummary: (params) => client.get('/odds-water-record/summary', { params }),
  getOddsWaterRecordDetail: (id) => client.get(`/odds-water-record/${id}`),
  createOddsWaterRecord: (data) => client.post('/odds-water-record', data),
  updateOddsWaterRecord: (id, data) => client.put(`/odds-water-record/${id}`, data),
  deleteOddsWaterRecord: (id) => client.delete(`/odds-water-record/${id}`),

  // 9. 每日水位规律串子战绩与大数据分析接口 (串子战绩版)
  getDailyWaterCombos: (params) => client.get('/water-combo-record', { params }),
  getDailyWaterComboPage: (params) => client.get('/water-combo-record/page', { params }),
  getDailyWaterComboSummary: (params) => client.get('/water-combo-record/summary', { params }),
  getDailyWaterComboDetail: (id) => client.get(`/water-combo-record/${id}`),
  createDailyWaterCombo: (data) => client.post('/water-combo-record', data),
  updateDailyWaterCombo: (id, data) => client.put(`/water-combo-record/${id}`, data),
  deleteDailyWaterCombo: (id) => client.delete(`/water-combo-record/${id}`),

  // 10. 让球获胜赔率六区间胜平负与穿盘率大数据统计接口 (赔率穿盘版)
  getHandicapOddsStatsList: (params) => client.get('/handicap-odds-stats/list', { params }),
  getHandicapOddsStatsPage: (params) => client.get('/handicap-odds-stats/page', { params }),
  getHandicapOddsStatsSummary: (params) => client.get('/handicap-odds-stats/summary', { params }),
  getHandicapOddsStatsDetail: (id) => client.get(`/handicap-odds-stats/${id}`),
  createHandicapOddsStats: (data) => client.post('/handicap-odds-stats', data),
  updateHandicapOddsStats: (id, data) => client.put(`/handicap-odds-stats/${id}`, data),
  deleteHandicapOddsStats: (id) => client.delete(`/handicap-odds-stats/${id}`),

  // 11. 单场精细化比赛明细与多步幅开奖记录接口
  getHandicapMatchRecords: (params) => client.get('/handicap-match-record/list', { params }),
  getHandicapMatchRecordPage: (params) => client.get('/handicap-match-record/page', { params }),
  getHandicapMatchRecordDetail: (id) => client.get(`/handicap-match-record/${id}`),
  createHandicapMatchRecord: (data) => client.post('/handicap-match-record', data),
  batchCreateHandicapMatchRecords: (data) => client.post('/handicap-match-record/batch', data),
  updateHandicapMatchRecord: (id, data) => client.put(`/handicap-match-record/${id}`, data),
  deleteHandicapMatchRecord: (id) => client.delete(`/handicap-match-record/${id}`),
  batchDeleteHandicapMatchRecords: (ids) => client.post('/handicap-match-record/batch-delete', ids),
  getHandicapMatchLeagues: () => client.get('/handicap-match-record/leagues'),
  simulateHandicapMatch: (data) => client.post('/handicap-match-record/simulate', data),


  // 12. 0球与7球两极进球赔率大数据分析接口
  getExtremeGoalsOddsList: (params) => client.get('/extreme-goals-odds/list', { params }),
  getExtremeGoalsOddsPage: (params) => client.get('/extreme-goals-odds/page', { params }),
  getExtremeGoalsOddsSummary: (params) => client.get('/extreme-goals-odds/summary', { params }),
  getExtremeGoalsOddsLeagues: () => client.get('/extreme-goals-odds/leagues'),
  getExtremeGoalsOddsDetail: (id) => client.get(`/extreme-goals-odds/${id}`),
  createExtremeGoalsOdds: (data) => client.post('/extreme-goals-odds', data),
  batchCreateExtremeGoalsOdds: (data) => client.post('/extreme-goals-odds/batch', data),
  updateExtremeGoalsOdds: (id, data) => client.put(`/extreme-goals-odds/${id}`, data),
  deleteExtremeGoalsOdds: (id) => client.delete(`/extreme-goals-odds/${id}`),
  batchDeleteExtremeGoalsOdds: (ids) => client.post('/extreme-goals-odds/batch-delete', ids),
  simulateExtremeGoals: (data) => client.post('/extreme-goals-odds/simulate', data)
}

export default api


