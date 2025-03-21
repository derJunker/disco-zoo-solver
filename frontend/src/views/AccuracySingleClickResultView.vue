<template>
  <div class="accuracy-single-click-result-view">
    <div class="accuracy-single-click-result-content">
      <div id="overview-menu" class="wood-menu dock-bottom menu-bottom">
        <h1>Results</h1>
        <div class="wood-menu-group">
          <h2>Grade</h2>
          <div id="grade" :style="gradeColor()">{{grade}}</div>
          <div id="accuracy">Accuracy: {{(score*100).toFixed(2)}}%</div>
        </div>
        <div class="nav-buttons">
          <div class="btn btn-gradient color-action-good" @click="onRetryClick">
            Retry
          </div>
          <div class="btn btn-gradient color-action-neutral-2" @click="onDetailsClick">
            Details
          </div>
        </div>
      </div>
    </div>
    <menu-bar :on-first-button-click="onHomeClick" first-color-class="color-action-neutral-1" first-button-name="Home"
              :on-second-button-click="console.log" second-color-class="color-action-neutral-2"
              second-button-name="Back"/>
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

#overview-menu {
  max-width: min(90%, 400px);
}

.wood-menu-group {
  margin-inline: 1.5rem;
}

#grade {
  font-size: 10rem;
  line-height: 8rem;
  padding-bottom: 2rem;
  text-align: center;
}

#accuracy {
  text-align: center;
}

h2 {
  text-align: center;
}

.nav-buttons {
  display: flex;
  justify-content: space-around;
  margin-top: 3rem;
  margin-bottom: 1rem;
}
.btn {
  padding: 1rem;
}
</style>

<script lang="ts">
import {defineComponent} from 'vue'
import MenuBar from "@/components/MenuBar.vue";
import {useAccuracyState} from "@/store/useState";
import router from "@/router";
import {AccuracyGameType} from "@/types/AccuracyGameType";
import {calculateScore} from "@/util/score-calculator";

const accuracyState = useAccuracyState()

export default defineComponent({
  name: "AccuracySingleClickResultView",
  components: {MenuBar},
  data() {
    return {
      overallAccuracy: null as number | null,
      percentageBestClicks: null as number | null,
      grade: "" as string,
      score: null as number | null
    }
  },

  methods: {
    calculateGrade(overallAccuracy: number, percentageBestClicks: number): string {
      // 89% of the score is based on overallAccuracy, 11% from percentageBestClicks
      this.score = calculateScore(overallAccuracy, percentageBestClicks)

      // Determine grade based on score with user's specified thresholds
      if (this.score >= 1) return "S+";
      if (this.score >= 0.90) return "S";
      if (this.score >= 0.80) return "A";
      if (this.score >= 0.70) return "B";
      if (this.score >= 0.50) return "C";
      if (this.score >= 0.35) return "D";
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
      router.push({name: 'accuracy'})
    }
  },

  created() {
    const clickHistory = accuracyState.singleClickHistory

    if (clickHistory.length === 0) {
      router.push({name: 'accuracy'})
      return
    }
    this.overallAccuracy = clickHistory.reduce((acc, curr) => acc + curr.performance.accuracy, 0) / clickHistory.length
    this.percentageBestClicks = clickHistory.filter(e => e.performance.wasBestClick).length / clickHistory.length
    this.grade = this.calculateGrade(this.overallAccuracy, this.percentageBestClicks)
  },
})
</script>