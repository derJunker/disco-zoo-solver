import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/reconstruct',
    name: 'reconstruct',
    component: () => import(/* webpackChunkName: "about" */ '../views/ReconstructView.vue')
  },
  {
    path: '/accuracy',
    name: 'accuracy',
    component: () => import(/* webpackChunkName: "about" */ '../views/AccuracyView.vue')
  },
  {
    path: '/battle',
    name: 'battle',
    component: () => import(/* webpackChunkName: "about" */ '../views/BattleView.vue')
  },
  {
    path: '/stats',
    name: 'stats',
    component: () => import(/* webpackChunkName: "about" */ '../views/StatsView.vue')
  },
  {
    path: '/about',
    name: 'about',
    component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  },
  {
    path: '/',
    name: 'home',
    component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
