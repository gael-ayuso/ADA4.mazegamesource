package src.main.gui.elements;

public enum MapElements {
    EMPTY( 'N', "empty.png"),
    WALL( 'W', "wall.png"),
    PLAYER( 'P', "player.png"),
    DIAMOND( 'D', "diamond.png"),
    HIDDEN_DIAMOND( 'H', "hiddenDiamond.png"),
    MOVABLE_WALL( 'M', "movableWall.png"),
    EXIT( 'E', "exit.png"),
    ;

    private final char symbol;
    private final String fileName;

    private MapElements(char symbol, String fileName) {
        this.symbol = symbol;
        this.fileName = fileName;
    }

    public char getSymbol() {
        return symbol;
    }
    public String getFileName() {
        return fileName;
    }

    public static MapElements getMapElementFromChar(char symbol) {
        for (MapElements element : MapElements.values()) {
            if (element.getSymbol() == symbol) {
                return element;
            }
        }
        return EMPTY;

    }
}
