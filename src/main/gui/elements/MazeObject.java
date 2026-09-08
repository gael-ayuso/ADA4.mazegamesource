package src.main.gui.elements;

import javax.swing.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

// Esta clase es el reemplazo de mazeObject en GameGui
public class MazeObject extends JLabel {
    private static final Map<MapElements, ImageIcon> ICONS = new HashMap<>();

    private static void loadIcons() {
        for (MapElements element : MapElements.values()) {
            URL url = MazeObject.class.getResource("/src/resources/assets/" + element.getFileName());
            System.out.println(url);

            if (url != null) {
                ICONS.put(element, new ImageIcon(url));
            } else {
                System.err.println("No icon found for " + element + " (" + element.getFileName() + ")");
            }
        }
    }
    static {
        loadIcons();
    }

    public MazeObject(MapElements element) {
        super(ICONS.get(element), JLabel.LEFT);
    }

    public void setElement(MapElements element){
        ImageIcon icon = ICONS.get(element);
        if(icon != null){
            super.setIcon(icon);
        }
    }
}
