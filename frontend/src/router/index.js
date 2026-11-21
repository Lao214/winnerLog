import { createRouter, createWebHistory } from 'vue-router'
import LeaderboardView from '../views/LeaderboardView.vue'
import MatchesView from '../views/MatchesView.vue'
import ComparisonView from '../views/ComparisonView.vue'
import AdminView from '../views/AdminView.vue'
import DailyProfitView from '../views/DailyProfitView.vue'
import DailyGoalsProfitView from '../views/DailyGoalsProfitView.vue'
import OddsWaterUpsetView from '../views/OddsWaterUpsetView.vue'
import DailyWaterComboView from '../views/DailyWaterComboView.vue'
import HandicapOddsStatsView from '../views/HandicapOddsStatsView.vue'
import MatchOddsSteppedView from '../views/MatchOddsSteppedView.vue'

const routes = [
  {
    path: '/',
    name: 'Leaderboard',
    component: LeaderboardView
  },
  {
    path: '/matches',
    name: 'Matches',
    component: MatchesView
  },
  {
    path: '/comparison',
    name: 'Comparison',
    component: ComparisonView
  },
  {
    path: '/daily-profit',
    name: 'DailyProfit',
    component: DailyProfitView
  },
  {
    path: '/daily-goals-profit',
    name: 'DailyGoalsProfit',
    component: DailyGoalsProfitView
  },
  {
    path: '/odds-water-profit',
    alias: '/odds-water-record',
    name: 'OddsWaterUpset',
    component: OddsWaterUpsetView
  },
  {
    path: '/water-combo-profit',
    alias: '/daily-water-combo',
    name: 'DailyWaterCombo',
    component: DailyWaterComboView
  },
  {
    path: '/handicap-odds-stats',
    alias: '/handicap-odds-record',
    name: 'HandicapOddsStats',
    component: HandicapOddsStatsView
  },
  {
    path: '/match-odds-stepped',
    alias: ['/match-stepped-analysis', '/handicap-match-analysis'],
    name: 'MatchOddsStepped',
    component: MatchOddsSteppedView
  },
  {
    path: '/extreme-goals-odds',
    alias: ['/goals-extreme-odds', '/extreme-odds-analysis'],
    name: 'ExtremeGoalsOdds',
    component: () => import('../views/ExtremeGoalsOddsView.vue')
  },
  {
    path: '/admin',
    name: 'Admin',
    component: AdminView
  }
]


const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
