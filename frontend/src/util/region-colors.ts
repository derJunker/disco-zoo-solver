import {RegionColors} from "@/types/RegionColors";

export function getRegionColors(region: string): RegionColors {
    if (!region)
        return {
            primary: "white",
            dark: "black",
            light: "grey",
        }
    const formattedRegion = region.toLowerCase()
        .replace("_", "-")
        .replace(" ", "-")
    return {
        primary: "var(--" + formattedRegion + "-primary)",
        dark: "var(--" + formattedRegion + "-dark)",
        light: "var(--" + formattedRegion + "-light)",
    }
}