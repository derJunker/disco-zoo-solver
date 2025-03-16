import {Animal} from "@/types/Animal";

const rarityMap: { [key: string]: number } = {
    'common': 1,
    'rare': 2,
    'timeless': 3,
    'epic': 4,
};

export function sortAnimalsByRarity(animals: Animal[]) {
    animals.sort((a, b) => {
        return rarityMap[a.rarity.toLowerCase()] - rarityMap[b.rarity.toLowerCase()];
    });
}