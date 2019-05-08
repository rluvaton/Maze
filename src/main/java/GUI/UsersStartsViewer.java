package GUI;

import javax.swing.*;

public class UsersStartsViewer {
    public JPanel panel1;
    private JPanel singlePlayerStatPanel;
    private JRadioButton colorForUserRadioBtn;
    private JPanel panel2;

    public static void main(String[] args) {
        JFrame frame = new JFrame("UsersStartsViewer");
        frame.setContentPane(new UsersStartsViewer().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
