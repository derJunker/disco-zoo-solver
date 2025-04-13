<template>
  <div class="reconstruct-play-view footer-view" :style="getBackgroundStyle()">
    {{loadPathVariables($route.params.region, $route.query.animals, $route.query.pet, $route.query.heatmap)}}
    <div class="reconstruct-content footer-view-content">
      <top-info-bar :region-colors="regionColors">
        <div id="attempts">
          <span id="attemptNum">
            {{ attempts }}
          </span>
          Attempts
        </div>
      </top-info-bar>
      <AnimalDisplay v-if="game" :animals="game?.containedAnimals" :tracker="animalTracker" class="animal-display"
                     @animal-click="onPlaceSelectChange" :animal-to-place="animalToPlace"
                     :region-colors="regionColors"/>
      <div class="disco-board-wrapper">
        <disco-board
            :game="game" :best-clicks="bestClicks" :region-colors="regionColors"
            :probabilities="probabilities" :min-prob="minProb" :max-prob="maxProb"
            @clicked-coords="onCoordsClicked"
            @right-clicked-coords="rightClickedCoords" class="disco-board" :loading="loadingData"/>
      </div>
      <transition name="overlay">
        <reconstruct-play-config v-if="showConfig"
                                 class="reconstruct-play-config dock-bottom dock-bottom-shadow"
                     :animals="game?.containedAnimals" :heat-map-animal="animalForHeatmap" :place-animal="animalToPlace"
                     :can-add-attempts="canAddAttempts()" :can-remove-attempts="canRemoveAttempts()"
                     @animal-heatmap-select="onHeatMapSelectChange" @animal-place-select="onPlaceSelectChange"
                     @add-attempts="addAttempts()" @remove-attempts="removeAttempts()"
                    @bux-find="onBuxFind()" ref="playConfig"/>
      </transition>
    </div>
    <menu-bar :on-first-button-click="onBack" first-color-class="color-action-neutral-1" first-button-name="back"
              :on-second-button-click="onConfig" :second-color-class="getConfigMenuColorClass()"
              :second-button-name="getConfigMenuName()" ref="menuBar"/>
  </div>
</template>

<style scoped>
.reconstruct-content {
  display: flex;
  justify-content: space-between;
  flex-direction: column;
}

.disco-board {
  max-width: min(90%, 400px, 55dvh);
  margin-inline: auto;
  margin-top: 3rem;
  aspect-ratio: 1;
}

.disco-board-wrapper {
  flex-grow: 1;
  align-content: center;
}



.reconstruct-play-config {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  margin: auto;
  max-width: min(90%, 600px);
  z-index: 10;
}

#attemptNum {
  font-size: 2rem;
  font-weight: bold;
  position: absolute;
  right: 7.5rem;
  bottom: -.2rem;
  transform: scaleY(1.15);

}

#attempts {
  position: relative;
}


</style>

<script lang="ts">
import {defineComponent} from 'vue'
import {Animal} from "@/types/Animal";
import AnimalDisplay from "@/components/AnimalDisplay.vue";
import MenuBar from "@/components/MenuBar.vue";
import router from "@/router";
import {useGame} from "@/store/useGame";
import {ClickChangeInfo, Game} from "@/types/Game";
import {Coords} from "@/types/Coords";
import {getRegionColors} from "@/util/region-colors";
import {useSolver} from "@/store/useSolver";
import ReconstructPlayConfig from "@/components/Overlays/ReconstructPlayConfig.vue";
import {sortAnimalsByRarity} from "@/util/animal-sorter";
import DiscoBoard from "@/components/Basic/DiscoBoard.vue";
import {useAnimals} from "@/store/useAnimals";
import TopInfoBar from "@/components/TopInfoBar.vue";
import {useErrors} from "@/store/useErrors";
import {RegionColors} from "@/types/RegionColors";


const gameStore = useGame()
const animalStore = useAnimals()
const solver = useSolver()
const errorState = useErrors()

export default defineComponent({
  name: "ReconstructPlayView",
  components: {ReconstructPlayConfig, TopInfoBar, DiscoBoard, MenuBar, AnimalDisplay},
  data() {
    return {
      animals: [] as Animal[],
      animalNames: [] as string[],
      petName: null as string | null,
      region: null as string | null,
      regionColors: null as RegionColors | null,
      game: null as Game | null,
      probabilities: null as number[][] | null,
      maxProb: null as number | null,
      minProb: null as number | null,
      bestClicks: [] as Coords[],
      showConfig: false,
      animalToPlace: null as Animal | null,
      animalForHeatmap: null as Animal | null,
      initialHeatMapAnimalName: null as string | null,
      animalTracker: new Map<Animal, number>(),
      attempts: process.env.VUE_APP_DEFAULT_ATTEMPTS,

      loadingData: false
    }
  },

  watch: {
    game: {
      async handler(game: Game | null) {
        if (game) {
          this.loadingData = true
          await this.updateProbabilityInfo()
          this.loadingData = false
          this.updateTracker()
        }
      },
      immediate: true
    },
    region: {
      handler(region: string | null) {
        if (region) {
          this.regionColors = getRegionColors(region)
        }
      },
      immediate: true
    }
  },

  async mounted() {
    if ((this.animalNames.length === 0 && !this.petName) || this.checkRareSneakyCases()) {
      await router.push({name: "reconstruct"})
      return
    }
    document.addEventListener('click', this.handleClickOutside);

    this.animals = await animalStore.getAnimalsByNames(this.animalNames)

    if (this.animals.length == 0 && this.animals.length != this.animalNames.length) {
      await router.push({name: "reconstruct"})
      return;
    }

    gameStore.startReconstruct(this.animals, this.region!, this.petName).then(game => {
      if (!game) {
        router.push({name: "reconstruct"})
        return
      }
      this.game = game
      this.animals = game?.containedAnimals || []
      this.animalForHeatmap = this.initialHeatMapAnimalName? this.animals.filter(animal => animal.name ===
              this.initialHeatMapAnimalName)[0] || this.animals[this.animals.length - 1] : this.animals[this.animals.length - 1]
    })
    sortAnimalsByRarity(this.animals)

    this.animalToPlace = null
  },

  beforeUnmount() {
    document.removeEventListener('click', this.handleClickOutside);
  },

  methods: {
    loadPathVariables(region: string, animalList: string, petName: string|null, heatmap: string|null) {
      this.animalNames = this.animalNames = animalList.split(",").filter(name => name !== "")
      this.initialHeatMapAnimalName = heatmap
      this.petName = petName
      this.region = region
      return ''
    },

    handleClickOutside(event:any) {
      if (this.showConfig
          && !(this.$refs.menuBar as any).$el.contains(event.target)
          && !(this.$refs.playConfig as any).$el.contains(event.target)) {
        this.showConfig = false
      }
    },

    checkRareSneakyCases() {
      if (this.animalNames.length === 0)
        return
      const animalAndPets = [...this.animalNames, this.petName]
      const rareCase = (this.petName && this.animalNames.every(animal =>
              ["zebra", "hippo"].includes(animal.toLowerCase())) && this.petName.toLowerCase() ===
          "hamster" && this.animalNames.length === 2)
      const discobuxSneak = this.animalNames.some(animal =>
          ["discobux"].includes(animal.toLowerCase()))
      const superSneakyCase = animalAndPets.every(animal =>
          ["zebra", "hippo", "hamster"].includes(animal?.toLowerCase()||"") && animalAndPets.length === 3)
      if (rareCase) {
        errorState.addError("Wow you went behind my back and tried anyways! Here are 5 Cookies 🍪🍪🍪🍪🍪")
      }
      else if (superSneakyCase)
        errorState.addError("🍪🍪🍪🍪🍪🍪🍪 This was very smart! You get all the cookies you want ;) 🍪🍪🍪🍪🍪🍪🍪🍪🍪")
      else if(discobuxSneak)
        errorState.addError("You earned yourself some discobux! respect :D")
      return rareCase || superSneakyCase || discobuxSneak
    },

    async updateProbabilityInfo() {
      if (!this.game || this.animalForHeatmap == null) {
        this.probabilities = null;
        this.bestClicks = []
        return;
      }
      const info = await solver.solve(this.game!, this.animalForHeatmap!)
      this.probabilities = info.probabilities
      this.bestClicks = info.bestClicks
      if (!info.probabilities) {
        this.maxProb = 0
        this.minProb = 0
      } else {
        this.maxProb = Math.max(...info.probabilities.flat())
        this.minProb = Math.min(...info.probabilities.flat())
      }
    },

    updateTracker() {
      this.animalTracker = new Map()
      for (const animal of this.animals) {
        this.animalTracker.set(animal, this.game?.board.flat().filter(tile => tile.occupied &&
            tile.animalBoardInstance.animal.name === animal.name).length || 0)
      }
    },


    onBack() {
      router.push({name: "reconstruct"})
    },
    onConfig() {
      this.showConfig = !this.showConfig
    },

    async onHeatMapSelectChange(animal: Animal) {
      if (this.animalForHeatmap === animal) {
        return;
      }
      this.animalForHeatmap = animal
      this.loadingData = true
      await this.updateProbabilityInfo()
      this.loadingData = false
    },

    onPlaceSelectChange(animal: Animal | null) {
      if (this.animalToPlace === animal) {
        animal = null
      }
      this.animalToPlace = animal
    },

    async onCoordsClicked(coords: Coords) {
      this.loadingData = true
      let clickInfo = await gameStore.clickReconstruct(this.game!, this.animalToPlace, coords)
      if (!clickInfo)
        return
      await this.updateGame(clickInfo, coords)
      this.loadingData = false
    },

    async rightClickedCoords(coords: Coords) {
      this.loadingData = true
      let clickInfo = await gameStore.clickReconstruct(this.game!, null, coords)
      if (!clickInfo)
        return
      await this.updateGame(clickInfo, coords)
      this.loadingData = false
    },

    async updateGame(clickInfo : ClickChangeInfo, coords: Coords) {
      if (!this.game)
        return
      if (clickInfo.wasValidClick) {
        this.updateAttemptCounter(clickInfo, coords)
        if (this.attempts < 0) {
          this.attempts = 0
          return
        }
        this.game.board[coords.x][coords.y] = clickInfo.updatedTile

        if (this.game.notCompletelyRevealedAnimalsWithoutBux.length == 0 &&
            clickInfo.notCompletelyRevealedAnimalsWithoutBux.length != 0)
          this.animalForHeatmap = clickInfo.notCompletelyRevealedAnimalsWithoutBux[0]

        this.game.completelyRevealedAnimals = clickInfo.completelyRevealedAnimals
        this.game.notCompletelyRevealedAnimalsWithoutBux = clickInfo.notCompletelyRevealedAnimalsWithoutBux

        if (this.game.completelyRevealedAnimals.filter(animal => animal.name === this.animalForHeatmap?.name).length > 0) {
          if (this.game.notCompletelyRevealedAnimalsWithoutBux.length > 0)
            this.animalForHeatmap = this.game.notCompletelyRevealedAnimalsWithoutBux[0]
          else
            this.animalForHeatmap = null
        }
        if (this.game.completelyRevealedAnimals.filter(animal => animal.name === this.animalToPlace?.name).length > 0
            && this.game.notCompletelyRevealedAnimalsWithoutBux.length > 0)
          this.animalToPlace = null

        await this.updateProbabilityInfo()
        this.updateTracker()

      }
    },

    updateAttemptCounter(clickInfo: ClickChangeInfo, coords: Coords) {
      if (!this.game)
        return
      const oldTile = this.game!.board[coords.x][coords.y]
      const newTile = clickInfo.updatedTile!
      if (oldTile?.revealed === newTile.revealed)
        return;
      if (newTile.revealed)
        this.attempts--
      else
        this.attempts++
    },

    getBackgroundStyle() {
      if (!this.regionColors) {
        return {}
      }
      return {backgroundColor: this.regionColors.light}
    },

    getConfigMenuName() {
      return this.showConfig ? "close" : "config"
    },

    getConfigMenuColorClass() {
      return this.showConfig ? "color-action-bad" : "color-action-neutral-2"
    },

    addAttempts() {
      if (!this.game)
        return
      this.attempts = Math.min(this.attempts + 5, this.game.board.length*this.game.board[0].length)
    },
    removeAttempts() {
      if (!this.game)
        return
      this.attempts = Math.max(this.attempts - 5, 0)
    },

    async onBuxFind() {
      this.animalForHeatmap = {name: "Discobux", rarity: "BUX", region: "ANY", pattern: [{x:0, y:0}]}
      this.loadingData = true
      await this.updateProbabilityInfo()
      this.loadingData = false
    },

    canAddAttempts() {
      if (!this.game)
        return
      return this.attempts < this.game!.board.length*this.game!.board[0].length
    },

    canRemoveAttempts() {
      if (!this.game)
        return
      return this.attempts > 0
    }
  }
})
</script>