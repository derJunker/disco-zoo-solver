export function getRegionColors(region: string): { primary: string, dark: string, light: string } {
    if (!region)
        return {
            primary: "white",
            dark: "black",
            light: "grey",
        }
    const formattedRegion = region.toLowerCase()
        .replace("_", "-")
        .replace(" ", "-")
    console.log("formattedRegion", formattedRegion)
    return {
        primary: "var(--" + formattedRegion + "-primary)",
        dark: "var(--" + formattedRegion + "-dark)",
        light: "var(--" + formattedRegion + "-light)",
    }
}