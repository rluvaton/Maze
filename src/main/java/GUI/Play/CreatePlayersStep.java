package GUI.Play;

import GUI.Color;
import GUI.GuiHelper;
import GUI.Play.Exceptions.NotFinishedStepException;
import Helpers.Coordinate;
import Maze.MazeGenerator.MazeGenerator;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import player.ActionsKeys;
import player.HumanPlayer.HumanPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;

public class CreatePlayersStep extends JPanel implements IPlayConfigStep {

    private JTextField nameIn;
    private JComboBox<Color> colorComboBox;
    private JTextField upIn;
    private JPanel keyStrokePanel;
    private JTextField downIn;
    private JTextField rightIn;
    private JTextField leftIn;
    private JButton createNewPlayerButton;

    private ActionsKeys actionsKeys = new ActionsKeys();

    java.util.List<HumanPlayer> playerList = new LinkedList<>();
    
    @Override
    public void init() {
       this.setLayout(new FormLayout("left:26dlu:noGrow,fill:max(d;4px):noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:d:grow", "center:31px:noGrow,top:4dlu:noGrow,top:13dlu:noGrow,top:13dlu:noGrow,top:10dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,top:4dlu:noGrow,top:15dlu:noGrow,center:max(d;4px):noGrow,top:12dlu:noGrow,center:max(d;4px):noGrow"));
        
    }

    @Override
    public void initComponents() {
        CellConstraints cc = new CellConstraints();

        initName(cc);
        initColor(cc);
        initKeyStrokePanel(cc);
        initUp();
        initDown();
        initRight();
        initLeft();

        initKeyStrokeHeader(cc);
        initCreateNewPlayer(cc);

        initListeners();
    }

    private void initListeners() {
        initDirectionKeysInput();

        this.createNewPlayerButton.addActionListener((e) -> {
            if(this.canContinue()) {
                this.playerList.add(this.createNewPlayer());
            }
        });
    }

    private HumanPlayer createNewPlayer() {
        String userName = this.nameIn.getText();
        if(userName == null || userName.equals("")) {
            userName = null;
        }

        return new HumanPlayer(new Coordinate(0, 0), userName, this.actionsKeys);
    }

    private void initDirectionKeysInput() {
        this.downIn.addKeyListener(new GuiHelper.OnlyKeyPressed() {
            @Override
            public void keyPressed(KeyEvent e) {
                actionsKeys.setDownKeyCode(e.getKeyCode());
                downIn.setText(e.getKeyChar() + "");
            }
        });

        this.upIn.addKeyListener(new GuiHelper.OnlyKeyPressed() {
            @Override
            public void keyPressed(KeyEvent e) {
                actionsKeys.setUpKeyCode(e.getKeyCode());
                upIn.setText(e.getKeyChar() + "");
            }
        });

        this.leftIn.addKeyListener(new GuiHelper.OnlyKeyPressed() {
            @Override
            public void keyPressed(KeyEvent e) {
                actionsKeys.setLeftKeyCode(e.getKeyCode());
                leftIn.setText(e.getKeyChar() + "");
            }
        });

        this.rightIn.addKeyListener(new GuiHelper.OnlyKeyPressed() {
            @Override
            public void keyPressed(KeyEvent e) {
                actionsKeys.setRightKeyCode(e.getKeyCode());
                rightIn.setText(e.getKeyChar() + "");
            }
        });
    }

    private void initCreateNewPlayer(CellConstraints cc) {
        createNewPlayerButton = new JButton();
        createNewPlayerButton.setText("Create New Player");
        this.add(createNewPlayerButton, cc.xyw(1, 1, 5));
    }

    private void initKeyStrokeHeader(CellConstraints cc) {
        final JLabel label8 = new JLabel();
        label8.setText("Key Stroke Assigntment");
        this.add(label8, cc.xywh(1, 5, 5, 2, CellConstraints.CENTER, CellConstraints.DEFAULT));
    }

    private void initLeft() {
        final JLabel label7 = new JLabel();
        label7.setText("Left");
        keyStrokePanel.add(label7);

        leftIn = createDirectionInput();
    }

    private void initRight() {
        final JLabel label6 = new JLabel();
        label6.setText("right");
        keyStrokePanel.add(label6);

        rightIn = createDirectionInput();
    }

    private void initDown() {
        final JLabel label5 = new JLabel();
        label5.setText("Down");
        keyStrokePanel.add(label5);

        downIn = createDirectionInput();
    }

    private JTextField createDirectionInput() {
        JTextField directionIn = new JTextField();
        directionIn.setMinimumSize(new Dimension(20, 24));
        directionIn.setPreferredSize(new Dimension(30, 24));
        keyStrokePanel.add(directionIn);
        return directionIn;
    }

    private void initUp() {
        final JLabel label4 = new JLabel();
        label4.setText("up");
        keyStrokePanel.add(label4);

        upIn = createDirectionInput();
    }

    private void initKeyStrokePanel(CellConstraints cc) {
        keyStrokePanel = new JPanel();
        keyStrokePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        this.add(keyStrokePanel, cc.xywh(1, 5, 5, 7));
    }

    private void initColor(CellConstraints cc) {
        colorComboBox = new JComboBox<>();
        colorComboBox.setModel(new DefaultComboBoxModel<>(Color.values()));
        this.add(colorComboBox, cc.xy(5, 4));

        final JLabel label3 = new JLabel();
        label3.setText("Color");
        this.add(label3, new CellConstraints(1, 4, 1, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets(0, 10, 0, 0)));
    }

    private void initName(CellConstraints cc) {
        final JLabel label2 = new JLabel();
        label2.setText("Name");
        this.add(label2, new CellConstraints(1, 3, 3, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets(0, 10, 0, 0)));

        nameIn = new JTextField();
        this.add(nameIn, cc.xy(5, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
    }

    @Override
    public boolean canContinue() {
        return this.actionsKeys.areAllDirectionsKeysSet() &&
                this.nameIn.getText() != null;
    }

    @Override
    public PlayStep getPlayStep() {
        return PlayStep.CREATE_PLAYERS;
    }

    @Override
    public MazeGenerator.Builder appendData(MazeGenerator.Builder builder) throws NotFinishedStepException {
        if (!canContinue()) {
            throw new NotFinishedStepException(this);
        }
        return builder;
    }

    public List<HumanPlayer> getPlayerList() {
        return playerList;
    }
}
