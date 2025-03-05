import {Coords} from "@/types/Coords";

export interface Animal {
    name: string;
    rarity: string;
    pattern: Coords[];
    region: string;
}