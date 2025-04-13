package junker.disco.zoo.solver.model.solver;

import java.util.List;

import junker.disco.zoo.solver.board.Click;

public record Solution(
        List<Click> clicks
) {
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Solution{").append("\n");
        var clickStrings =
                clicks.stream().map(click -> click.coords() + " P=" + "%.4f".formatted(click.expectedProbability())).toList();
        sb.append(clickStrings).append("\n");
        sb.append("}");
        return sb.toString();
    }
}
