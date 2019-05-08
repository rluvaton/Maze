package player;

public class DirectionKeys {
    public static int NO_KEY = -1;

    private int upKeyCode = NO_KEY;
    private int downKeyCode = NO_KEY;
    private int rightKeyCode = NO_KEY;
    private int leftKeyCode = NO_KEY;

    public DirectionKeys() {
    }

    public DirectionKeys(int upKeyCode, int downKeyCode, int rightKeyCode, int leftKeyCode) {
        this.upKeyCode = upKeyCode;
        this.downKeyCode = downKeyCode;
        this.rightKeyCode = rightKeyCode;
        this.leftKeyCode = leftKeyCode;
    }

    public boolean isAllKeysSet() {
        return this.upKeyCode != DirectionKeys.NO_KEY &&
                this.downKeyCode != DirectionKeys.NO_KEY &&
                this.rightKeyCode != DirectionKeys.NO_KEY &&
                this.leftKeyCode != DirectionKeys.NO_KEY;
    }

    public int getUpKeyCode() {
        return upKeyCode;
    }

    public void setUpKeyCode(int upKeyCode) {
        this.upKeyCode = upKeyCode;
    }

    public int getDownKeyCode() {
        return downKeyCode;
    }

    public void setDownKeyCode(int downKeyCode) {
        this.downKeyCode = downKeyCode;
    }

    public int getRightKeyCode() {
        return rightKeyCode;
    }

    public void setRightKeyCode(int rightKeyCode) {
        this.rightKeyCode = rightKeyCode;
    }

    public int getLeftKeyCode() {
        return leftKeyCode;
    }

    public void setLeftKeyCode(int leftKeyCode) {
        this.leftKeyCode = leftKeyCode;
    }
}
