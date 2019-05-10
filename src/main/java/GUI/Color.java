package GUI;

public enum Color {
    BLACK("black"),
    RED("red"),
    WHITE("white"),
    LIGHT_GRAY("lightGray"),
    GRAY("gray"),
    DARK_GRAY("darkGray"),
    PINK("pink"),
    ORANGE("orange"),
    YELLOW("yellow"),
    GREEN("green"),
    MAGENTA("magenta"),
    CYAN("cyan"),
    BLUE("blue");

    private String colorName;

    Color(String colorName) {
        this.colorName = colorName;
    }

    public java.awt.Color getColor() {
        return GuiHelper.getColorByName(this.colorName);
    }

    @Override
    public String toString() {
        return colorName;
    }
}
