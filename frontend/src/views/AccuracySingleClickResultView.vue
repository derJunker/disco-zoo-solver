<template>
  <div class="accuracy-single-click-result-view">
    <div class="accuracy-single-click-result-content">
      <h1>Accuracy Single Click Result</h1>
      <p>{{overallAccuracy}}</p>
      <p>{{grade}}</p>
    </div>
    <menu-bar :on-first-button-click="console.log" first-color-class="color-action-neutral-1" first-button-name="sth"
              :on-second-button-click="console.log" second-color-class="color-action-neutral-2" second-button-name="sth"/>
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
</style>

<script lang="ts">
import {defineComponent} from 'vue'
import MenuBar from "@/components/MenuBar.vue";
import {useAccuracyState} from "@/store/useState";
import router from "@/router";

const accuracyState = useAccuracyState()

export default defineComponent({
  name: "AccuracySingleClickResultView",
  components: {MenuBar},
  data() {
    return {
      overallAccuracy: null as number | null,
      percentageBestClicks: null as number | null,
      grade: "" as string
    }
  },

  methods: {
    calculateGrade(overallAccuracy: number, percentageBestClicks: number): string {
      // 89% of the score is based on overallAccuracy, 11% from percentageBestClicks
      const score = (overallAccuracy * 0.89) + (percentageBestClicks * 0.11);

      // Determine grade based on score with user's specified thresholds
      if (score >= 1) return "S+";
      if (score >= 0.90) return "S";
      if (score >= 0.80) return "A";
      if (score >= 0.70) return "B";
      if (score >= 0.50) return "C";
      if (score >= 0.35) return "D";
      return "F";
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