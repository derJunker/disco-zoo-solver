import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import {AccuracyGameType} from "@/types/AccuracyGameType";


const routes: Array<RouteRecordRaw> = [
  {
    path: '/reconstruct',
    name: 'reconstruct',
    component: () => import(/* webpackChunkName: "about" */ '../views/ReconstructView.vue')
  },
  {
    path: '/reconstruct/:region',
    name: 'reconstruct-region',
    component: () => import('../views/ReconstructView.vue')
  },
  {
    path: '/reconstruct/:region/play',
    name: 'reconstruct-play',
    component: () => import('../views/ReconstructPlayView.vue')
  },
  {
    path: '/accuracy',
    name: 'accuracy',
    component: () => import(/* webpackChunkName: "about" */ '../views/AccuracyView.vue')
  },
  {
    path: '/accuracy/' + AccuracyGameType.SINGLE_CLICK + '/:region/:seed',
    name: 'accuracy-' + AccuracyGameType.SINGLE_CLICK +'-play',
    component: () => import('../views/AccuracySingleClickView.vue')
  },
  {
    path: '/accuracy/' + AccuracyGameType.SINGLE_GAME + '/:region/:seed',
    name: 'accuracy-' + AccuracyGameType.SINGLE_GAME +'-play',
    component: () => import('../views/AccuracySingleGameView.vue')
  },
  {
    path: '/accuracy/' + AccuracyGameType.SINGLE_CLICK + '/stats',
    name: 'accuracy-' + AccuracyGameType.SINGLE_CLICK + '-result',
    component: () => import('../views/AccuracySingleClickResultView.vue')
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
    component: () => import(/* webpackChunkName: "about" */ '../views/HomeView.vue')
  },
  {
    path: '/settings',
    name: 'settings',
    component: () => import(/* webpackChunkName: "about" */ '../views/SettingsView.vue')
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
