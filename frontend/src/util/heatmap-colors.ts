export function getHeatmapColor(value: number, minVal: number, maxVal: number): string {

    const adjustValue = 0.15

    const lowerBound = minVal > 0 ? Math.min(adjustValue, minVal) : 0
    const upperBound = maxVal < 1 ? Math.max(1-adjustValue, maxVal): 1
    // Ensure value is within range
    const normalized = Math.max(lowerBound, Math.min(upperBound, (value - minVal) / (maxVal - minVal)));

    // Define color stops
    const colors = [
        { stop: 0, color: [0, 255, 0] },   // Bright Green
        { stop: 0.33, color: [255, 255, 0] }, // Bright Yellow
        { stop: 0.66, color: [255, 128, 0] }, // Bright Orange
        { stop: 1, color: [255, 0, 0] }    // Bright Red
    ];

    // Find the two nearest color stops
    for (let i = 0; i < colors.length - 1; i++) {
        const c1 = colors[i], c2 = colors[i + 1];
        if (normalized >= c1.stop && normalized <= c2.stop) {
            const t = (normalized - c1.stop) / (c2.stop - c1.stop); // Interpolation factor
            const r = Math.round(c1.color[0] + t * (c2.color[0] - c1.color[0]));
            const g = Math.round(c1.color[1] + t * (c2.color[1] - c1.color[1]));
            const b = Math.round(c1.color[2] + t * (c2.color[2] - c1.color[2]));
            return `rgb(${r},${g},${b})`;
        }
    }

    return "rgb(0,255,0)"; // Fallback bright green
}