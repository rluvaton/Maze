package Helpers.Builder;

public class BuilderException extends Exception {

    public BuilderException(String whatBuilding) {
        super(parseWhatBuilding(whatBuilding));
    }

    public BuilderException(String whatBuilding, String message) {
        super(parseWhatBuilding(whatBuilding) + message);
    }

    private static String parseWhatBuilding(String whatBuilding) {
        return whatBuilding != null ? "[" + whatBuilding + " Builder Error] " : "";
    }

}
