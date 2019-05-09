package GUI.Welcome;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;

public class WelcomePanel extends JPanel {
    private JLabel title;
    private JLabel madeBy;
    private JButton generateBtn;
    private JButton statsBtn;
    private JButton playBtn;

    public WelcomePanel() {
    }

    public void init() {
        String encodedColumnSpecs = "fill:165px:noGrow, left:4dlu:noGrow, fill:d:grow, left:4dlu:noGrow, fill:155px:noGrow";
        String encodedRowSpecs = "center:d:grow, top:4dlu:noGrow, center:max(d;4px):noGrow, top:4dlu:noGrow, center:d:grow";

        this.setLayout(new FormLayout(encodedColumnSpecs, encodedRowSpecs));
        this.setEnabled(true);
        this.setMaximumSize(new Dimension(530, 133));
        this.setMinimumSize(new Dimension(530, 133));
        this.setPreferredSize(new Dimension(530, 133));
        this.setVisible(true);
    }

    public void initComponents() {
        createUIComponents();
    }
    private void createUIComponents() {
        CellConstraints cc = new CellConstraints();

        initGenerateBtn();
        createStatsBtn();
        createPlayBtn(cc);
        createTitle(cc);
        createMadeBy(cc);
    }

    private void initGenerateBtn() {
        generateBtn = new JButton();

        Font generateBtnFont = this.getFont("Fira Code", -1, -1, generateBtn.getFont());
        if (generateBtnFont != null) {
            generateBtn.setFont(generateBtnFont);
        }

        generateBtn.setHideActionText(false);
        generateBtn.setText("Generate");
        generateBtn.setToolTipText("Generate maze and export it to image");
        generateBtn.putClientProperty("hideActionText", Boolean.FALSE);
        generateBtn.putClientProperty("html.disable", Boolean.FALSE);

        this.add(generateBtn, new CellConstraints(1, 5, 1, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets(0, 10, 0, 0)));
    }

    private void createStatsBtn() {
        statsBtn = new JButton();

        Font statsBtnFont = this.getFont("Fira Code", -1, -1, statsBtn.getFont());
        if (statsBtnFont != null) {
            statsBtn.setFont(statsBtnFont);
        }

        statsBtn.setText("Stats");
        statsBtn.setToolTipText("User Statics");

        this.add(statsBtn, new CellConstraints(5, 5, 1, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets(0, 0, 0, 10)));
    }

    private void createPlayBtn(CellConstraints cc) {
        playBtn = new JButton();

        Font playBtnFont = this.getFont("Fira Code", -1, -1, playBtn.getFont());
        if (playBtnFont != null) {
            playBtn.setFont(playBtnFont);
        }

        playBtn.setText("Play");
        playBtn.setToolTipText("Start Playing");
        playBtn.putClientProperty("hideActionText", Boolean.FALSE);

        this.add(playBtn, cc.xy(3, 5));
    }

    private void createTitle(CellConstraints cc) {
        title = new JLabel();

        Font titleFont = this.getFont("Source Code Pro", Font.BOLD, 18, title.getFont());
        if (titleFont != null) {
            title.setFont(titleFont);
        }

        title.setHorizontalAlignment(0);
        title.setHorizontalTextPosition(0);
        title.setText("Maze");

        this.add(title, cc.xy(3, 1));
    }

    private void createMadeBy(CellConstraints cc) {
        madeBy = new JLabel();

        Font madeByFont = this.getFont("Fira Code", -1, -1, madeBy.getFont());
        if (madeByFont != null) {
            madeBy.setFont(madeByFont);
        }

        madeBy.setForeground(new Color(-9276814));
        madeBy.setHorizontalAlignment(0);
        madeBy.setText("Made by Raz Luvaton");

        this.add(madeBy, cc.xy(3, 3));
    }

    private Font getFont(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) {
            return null;
        }
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

}