export function getHeatmapColor(
    value: number,
    minVal: number,
    maxVal: number,
    absoluteThreshold = 0.4
): string {
    if (value == 0)
        return `rgb(0,255,0)`; // green
    let normalized: number;

    const isFlat = maxVal === minVal;

    if (isFlat) {
        normalized = 1;
    } else {
        // Case 1: Small data range – stretch color scale to fit full range
        if (maxVal < absoluteThreshold) {
            normalized = (value - minVal) / (maxVal - minVal); // full range from 0 to 1
        } else {
            // Case 2: Normal behavior – only highest value hits red
            const domainMax = Math.max(maxVal, absoluteThreshold);
            normalized = (value - minVal) / (domainMax - minVal);
        }

        normalized = Math.max(0, Math.min(1, normalized)); // clamp
    }

    // Same colors — full gradient
    const colors = [
        { stop: 0,    color: [180, 255, 0  ] }, // green
        { stop: 0.33, color: [255, 255, 0  ] }, // yellow
        { stop: 0.66, color: [255, 128, 0  ] }, // orange
        { stop: 1,    color: [255, 0,   0  ] }  // red
    ];

    for (let i = 0; i < colors.length - 1; i++) {
        const c1 = colors[i], c2 = colors[i + 1];
        if (normalized >= c1.stop && normalized <= c2.stop) {
            const t = (normalized - c1.stop) / (c2.stop - c1.stop);
            const r = Math.round(c1.color[0] + t * (c2.color[0] - c1.color[0]));
            const g = Math.round(c1.color[1] + t * (c2.color[1] - c1.color[1]));
            const b = Math.round(c1.color[2] + t * (c2.color[2] - c1.color[2]));
            return `rgb(${r},${g},${b})`;
        }
    }

    return `rgb(0,255,0)`; // fallback
}
