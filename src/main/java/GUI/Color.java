package GUI;

public enum Color {
    BLACK("black", java.awt.Color.BLACK),
    RED("red", java.awt.Color.RED),
    WHITE("white", java.awt.Color.WHITE),
    LIGHT_GRAY("lightGray", java.awt.Color.LIGHT_GRAY),
    GRAY("gray", java.awt.Color.GRAY),
    DARK_GRAY("darkGray", java.awt.Color.DARK_GRAY),
    PINK("pink", java.awt.Color.PINK),
    ORANGE("orange", java.awt.Color.ORANGE),
    YELLOW("yellow", java.awt.Color.YELLOW),
    GREEN("green", java.awt.Color.GREEN),
    MAGENTA("magenta", java.awt.Color.MAGENTA),
    CYAN("cyan", java.awt.Color.CYAN),
    BLUE("blue", java.awt.Color.BLUE);

    private String colorName;
    private java.awt.Color color;

    Color(String colorName, java.awt.Color color) {
        this.colorName = colorName;
        this.color = color;
    }

    public java.awt.Color getColor() {
        return this.color;
    }

    @Override
    public String toString() {
        return colorName;
    }
}
