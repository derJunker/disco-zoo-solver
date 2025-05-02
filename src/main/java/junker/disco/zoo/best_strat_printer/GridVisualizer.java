package junker.disco.zoo.best_strat_printer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import junker.disco.zoo.solver.board.Coords;

public class GridVisualizer {

    public static void drawGridWithCoords(List<Coords> coords, String filename) throws IOException {
        int gridSize = 5;
        int cellSize = 100;
        int imgSize = gridSize * cellSize;

        BufferedImage image = new BufferedImage(imgSize, imgSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        // Enable anti-aliasing for text
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw grid lines
        g.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i <= gridSize; i++) {
            g.drawLine(0, i * cellSize, imgSize, i * cellSize); // horizontal lines
            g.drawLine(i * cellSize, 0, i * cellSize, imgSize); // vertical lines
        }

        // Draw numbers for coords
        g.setFont(new Font("Arial", Font.BOLD, 36));
        g.setColor(Color.BLACK);

        for (int i = 0; i < coords.size(); i++) {
            Coords p = coords.get(i); // assuming p.x = col, p.y = row
            int x = p.x() * cellSize;
            int y = p.y() * cellSize;
            String label = String.valueOf(i + 1);

            // Center the text in the cell
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(label);
            int textHeight = fm.getAscent();

            g.drawString(label, x + (cellSize - textWidth) / 2, y + (cellSize + textHeight) / 2 - 5);
        }

        g.dispose();
        ImageIO.write(image, "png", new File(filename));
    }
}
