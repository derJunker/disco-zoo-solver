export function getRarityColor(rarity: string) {
    if ([ 'common', 'rare', 'timeless', 'epic', 'pet'].indexOf(rarity.toLowerCase()) === -1) {
        return 'red';
    }
    return 'var(--rarity-' + rarity.toLowerCase() + ')';
}