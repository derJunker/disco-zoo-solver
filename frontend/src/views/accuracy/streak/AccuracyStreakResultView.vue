<template>
  <div class="accuracy-streak-result-view footer-view">
    <div class="accuracy-streak-result-content footer-view-content">
      <transition name="overlay">
        <div v-if="show" id="overview-menu" class="wood-menu dock-bottom menu-bottom">
          <h1>Results - Streak</h1>
          <div class="wood-menu-group">
            <h2>🔥 Streak 🔥</h2>
            <div id="streak">{{bestClickCount}}</div>
          </div>
          <div class="wood-menu-group">
            <div>Score: {{(score)}}</div>
          </div>
          <div class="wood-menu-group">
            <div>Region: {{ region }}</div>
            <div>Difficulty: {{ difficulty }}</div>
          </div>
          <div class="nav-buttons">
            <button class="btn btn-gradient color-action-info" @click="onShare">
              Share
            </button>
            <button class="btn btn-gradient color-action-neutral-2" @click="onDetailsClick">
              Details
            </button>
          </div>
        </div>
      </transition>
      <div style="position: relative">
        <div id="overview-menu-screenshot" class="wood-menu" hidden>
          <h1>Results - Streak</h1>
          <div class="wood-menu-group-compatible">
            <h2>🔥 Streak 🔥</h2>
            <div id="streak">{{bestClickCount}}</div>
          </div>
          <div class="wood-menu-group-compatible">
            <div>Score: {{(score)}}</div>
          </div>
          <div class="wood-menu-group-compatible">
            <div>Region: {{ region }}</div>
            <div>Difficulty: {{ difficulty }}</div>
          </div>
        </div>
        <div style="background-color: var(--background-color); width: 100%; height: 100%; position: absolute; top:0;"/>
      </div>
    </div>
    <menu-bar :on-first-button-click="onHomeClick" first-color-class="color-action-neutral-1" first-button-name="Home"
              :on-second-button-click="onRetryClick" second-color-class="color-action-good"
              second-button-name="Retry"/>
  </div>
</template>

<style scoped>
.accuracy-streak-result-content {
  display: grid;
  place-items: center;
}

#overview-menu, #overview-menu-screenshot {
  max-width: min(90%, 400px);
}

.wood-menu-group, .wood-menu-group-compatible {
  margin-inline: 1.5rem;
  margin-bottom: .81rem;
}

#streak {
  font-size: 8rem;
  line-height: 8rem;
  padding-bottom: 2rem;
  text-align: center;
  position: relative;
}

h2 {
  text-align: center;
}

.nav-buttons {
  display: flex;
  justify-content: space-between;
  margin-inline: 1.5rem;
  margin-top: 1.5rem;
  margin-bottom: 1rem;
}
.btn {
  width: 45%;
  min-width: fit-content;
  text-align: center;
  padding: 1rem 0;
}

#overview-menu-screenshot {
  z-index: -1;
}
</style>

<script lang="ts">
import {defineComponent, ref} from 'vue'
import MenuBar from "@/components/MenuBar.vue";
import {useAccuracyState} from "@/store/useState";
import router from "@/router";
import {AccuracyGameType} from "@/types/accuracy/AccuracyGameType";
import {calculateScore} from "@/util/score-calculator";
import html2canvas from "html2canvas";
import {generateSeed} from "@/util/seed-generator";

const state = useAccuracyState()

export default defineComponent({
  name: "AccuracyStreakResultView",
  components: {MenuBar},

  setup() {
    const show = ref(false)

    return {show}
  },

  mounted() {
    this.show = true
  },

  created() {
    const clickHistory = state.singleClickHistory

    if (clickHistory.length === 0) {
      router.push({name: 'accuracy'})
      return
    }
    this.bestClickCount = clickHistory.filter(e => e.wasBestClick).length
    this.gameAmount = clickHistory.length
    this.score = calculateScore(clickHistory, state.withTimeless)
    this.region = state.region
    this.difficulty = state.difficulty
  },

  data() {
    return {
      overallAccuracy: null as number | null,
      bestClickCount: null as number | null,
      gameAmount: null as number | null,
      region: null as string | null,
      score: null as number | null,
      difficulty: null as string | null,
    }
  },

  methods: {
    onHomeClick() {
      router.push({name: 'home'})
    },

    onRetryClick() {
      router.push({name: 'accuracy-' + AccuracyGameType.STREAK + '-play',
        params: {seed: generateSeed(), region: this.region, difficulty: this.difficulty},
        query: {timeless: state.withTimeless + ""}})
    },

    onShare() {
      this.screenshotThenShare()
    },

    onDetailsClick() {
      router.push({name: 'accuracy-' + AccuracyGameType.STREAK + '-stats-details'})
    },

    screenshotThenShare() {
      const element: HTMLElement | null = document.querySelector("#overview-menu-screenshot");

      if (element) {
        element.hidden = false
        html2canvas(element).then((canvas) => {
          element.hidden = true
          this.shareCanvas(canvas);
        })
      }
    },

    shareCanvas(canvas: HTMLCanvasElement) {
      const dataUrl = canvas.toDataURL("image/png");
      fetch(dataUrl)
          .then(res => res.blob())
          .then(blob => {
            const file = new File([blob], "screenshot.png", { type: "image/png" });
            const shareData = {
              title: 'Item 1',
              text: 'This is the first item',
              files: [file],
              url: router.currentRoute.value.fullPath,
            };
            if (navigator.canShare && navigator.canShare({ files: [file] })) {
              navigator.share(shareData).catch(console.error);
            } else {
              console.error("Sharing not supported");
            }
          });
    }
  },
})
</script>