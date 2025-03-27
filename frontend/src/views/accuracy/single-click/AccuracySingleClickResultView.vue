<template>
  <div class="accuracy-single-click-result-view">
    <div class="accuracy-single-click-result-content">
      <transition name="overlay">
        <div v-if="show" id="overview-menu" class="wood-menu dock-bottom menu-bottom">
          <h1>Results - Single Click</h1>
          <div class="wood-menu-group">
            <h2>Grade</h2>
            <div id="grade" :style="gradeColor()">{{grade}}</div>
          </div>
          <div class="wood-menu-group">
            <div>Score: {{(score)}}</div>
            <div>Perfect Clicks: {{bestClickCount}}/{{gameAmount}}</div>
            <div>Accuracy: {{(overallAccuracy*100).toFixed(2)}}%</div>
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

      <div id="overview-menu-screenshot" class="wood-menu" hidden>
        <h1>Results - Single Click</h1>
        <div class="wood-menu-group-compatible">
          <h2>Grade</h2>
          <div id="grade" :style="gradeColor()">{{grade}}</div>
        </div>
        <div class="wood-menu-group-compatible">
          <div>Score: {{(score)}}</div>
          <div>Perfect Clicks: {{bestClickCount}}/{{gameAmount}}</div>
          <div>Accuracy: {{(overallAccuracy*100).toFixed(2)}}%</div>
        </div>
        <div class="wood-menu-group-compatible">
          <div>Region: {{ region }}</div>
          <div>Difficulty: {{ difficulty }}</div>
        </div>
      </div>

    </div>
    <menu-bar :on-first-button-click="onHomeClick" first-color-class="color-action-neutral-1" first-button-name="Home"
              :on-second-button-click="onRetryClick" second-color-class="color-action-good"
              second-button-name="Retry"/>
  </div>
</template>

<style scoped>
.accuracy-single-click-result-view {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.accuracy-single-click-result-content {
  position: relative;
  flex: 1;
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

#grade {
  font-size: 10rem;
  line-height: 8rem;
  padding-bottom: 2rem;
  text-align: center;
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
import {calculateAccuracy, calculateScore} from "@/util/score-calculator";
import html2canvas from "html2canvas";
import {generateSeed} from "@/util/seed-generator";

const state = useAccuracyState()

export default defineComponent({
  name: "AccuracySingleClickResultView",
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
    this.overallAccuracy = clickHistory.reduce((acc, curr) => acc + curr.accuracy, 0) / clickHistory.length
    this.bestClickCount = clickHistory.filter(e => e.wasBestClick).length
    this.gameAmount = clickHistory.length
    this.grade = this.calculateGrade()
    this.score = calculateScore(clickHistory, state.withTimeless)
    this.region = state.region
    this.difficulty = state.difficulty
  },

  data() {
    return {
      overallAccuracy: null as number | null,
      bestClickCount: null as number | null,
      gameAmount: null as number | null,
      grade: "" as string,
      region: null as string | null,
      score: null as number | null,
      difficulty: null as string | null,
    }
  },

  methods: {
    calculateGrade(): string {
      const accuracy = calculateAccuracy(this.overallAccuracy!, this.bestClickCount!/this.gameAmount!)

      // Determine grade based on score with user's specified thresholds
      if (accuracy >= 1) return "S+";
      if (accuracy >= 0.90) return "S";
      if (accuracy >= 0.80) return "A";
      if (accuracy >= 0.70) return "B";
      if (accuracy >= 0.50) return "C";
      if (accuracy >= 0.35) return "D";
      return "F";
    },

    onDetailsClick() {
      router.push({name: 'accuracy-' + AccuracyGameType.SINGLE_CLICK + '-stats-details'})
    },

    gradeColor() {
      switch (this.grade) {
        case "S+":
        case "S":
          return {color: "#FFD700"}; // golden
        case "A":
          return {color: "#32CD32"}; // green
        case "B":
          return {color: "#1E90FF"}; // blue
        case "C":
          return {color: "#9370DB"}; // purple
        case "D":
          return {color: "#FFA500"}; // orange
        case "F":
          return {color: "#FF6347"}; // red
      }
    },
    onHomeClick() {
      router.push({name: 'home'})
    },

    onRetryClick() {
      router.push({name: 'accuracy-' + AccuracyGameType.SINGLE_CLICK + '-play',
        params: {seed: generateSeed(), region: this.region, difficulty: this.difficulty},
        query: {timeless: state.withTimeless + ""}})
    },

    onShare() {
      this.screenshotThenShare()
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