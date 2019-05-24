package GUI;

public enum Color {
    BLACK("Black", java.awt.Color.BLACK),
    RED("Red", java.awt.Color.RED),
    WHITE("White", java.awt.Color.WHITE),
    LIGHT_GRAY("Light Gray", java.awt.Color.LIGHT_GRAY),
    GRAY("Gray", java.awt.Color.GRAY),
    DARK_GRAY("Dark Gray", java.awt.Color.DARK_GRAY),
    PINK("Pink", java.awt.Color.PINK),
    ORANGE("Orange", java.awt.Color.ORANGE),
    YELLOW("Yellow", java.awt.Color.YELLOW),
    GREEN("Green", java.awt.Color.GREEN),
    MAGENTA("Magenta", java.awt.Color.MAGENTA),
    CYAN("Cyan", java.awt.Color.CYAN),
    BLUE("Blue", java.awt.Color.BLUE);

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
