import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import {AccuracyGameType} from "@/types/AccuracyGameType";


const routes: Array<RouteRecordRaw> = [
  {
    path: '/reconstruct',
    name: 'reconstruct',
    component: () => import(/* webpackChunkName: "about" */ '../views/reconstruct/ReconstructView.vue')
  },
  {
    path: '/reconstruct/:region',
    name: 'reconstruct-play',
    component: () => import('../views/reconstruct/ReconstructPlayView.vue')
  },
  {
    path: '/accuracy',
    name: 'accuracy',
    component: () => import(/* webpackChunkName: "about" */ '../views/accuracy/AccuracyView.vue')
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

for (const gameType in AccuracyGameType) {
  const routeShortName = gameType.replace(/_/g, '-').toLowerCase()
  const vueFileShortName = routeShortName
      .replace(/[-_](.)/g, (_, char) => char.toUpperCase())
      .replace(/^[a-z]/, char => char.toUpperCase())
  routes.push(
      {
        path: `/accuracy/${routeShortName}/:region/:seed`,
        name: `accuracy-${routeShortName}-play`,
        component: () => import(`../views/accuracy/${routeShortName}/Accuracy${vueFileShortName}View.vue`)
      },
      {
        path: `/accuracy/${routeShortName}/stats`,
        name: `accuracy-${routeShortName}-result`,
        component: () => import(`../views/accuracy/${routeShortName}/Accuracy${vueFileShortName}ResultView.vue`)
      },
      {
        path: `/accuracy/${routeShortName}/stats/details`,
        name: `accuracy-${routeShortName}-stats-details`,
        component: () => import(`../views/accuracy/${routeShortName}/Accuracy${vueFileShortName}ResultDetailsView.vue`)
      },
  )
}

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
