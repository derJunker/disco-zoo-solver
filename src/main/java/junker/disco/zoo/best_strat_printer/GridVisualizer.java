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

        // === Configurable Colors ===
        Color gridColor = new Color(230, 230, 230); // background color of grid tiles
        Color tileColor = new Color(200, 200 , 200); // highlight color for numbered tiles
        Color borderColor = Color.GRAY;             // border color of each tile
        // ===========================

        BufferedImage image = new BufferedImage(imgSize, imgSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw the grid with configurable background color
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                int x = col * cellSize;
                int y = row * cellSize;
                g.setColor(gridColor);
                g.fillRect(x, y, cellSize, cellSize);
                g.setColor(borderColor);
                g.drawRect(x, y, cellSize, cellSize);
            }
        }

        // Draw numbered tiles with different color
        g.setFont(new Font("Arial", Font.BOLD, 36));
        g.setColor(Color.BLACK);

        for (int i = 0; i < coords.size(); i++) {
            Coords p = coords.get(i); // assuming p.x = col, p.y = row
            int x = p.x() * cellSize;
            int y = p.y() * cellSize;
            String label = String.valueOf(i + 1);

            // Fill the tile with the tileColor
            g.setColor(tileColor);
            g.fillRect(x, y, cellSize, cellSize);
            g.setColor(borderColor);
            g.drawRect(x, y, cellSize, cellSize);

            // Draw centered label
            g.setColor(Color.BLACK);
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(label);
            int textHeight = fm.getAscent();
            g.drawString(label, x + (cellSize - textWidth) / 2, y + (cellSize + textHeight) / 2 - 5);
        }

        g.dispose();
        var file = new File("solution_graphics/" + filename + ".png");
        file.mkdirs();
        ImageIO.write(image, "png", file);
    }


}
