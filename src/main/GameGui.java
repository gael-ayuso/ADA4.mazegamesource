package src.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

import src.main.gui.elements.MapElements;
import src.main.gui.elements.MazeObject;

// ANTES: public class GameGui extends JFrame implements ActionListener
public class GameGui extends JFrame {

    final HighScore highScore; //se le quito el private
    int catFileName = 1; //se le quito el private
    private final Container cp;
    final FileLoader fl = new FileLoader(); //se le quito el private

    Action updateCursorAction = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            ix -= 1;
            jx += 1;
            if (ix < 0) {
                ix = 60;
                timeLeft -= 1;
            }
            if (timeLeft <= 0 && ix <= 0) {
                timely.stop();
                catFileName -= 1;
                if (catFileName < 1) {
                    handleTimeExpired();
                } else {
                    loadMatrixGui(GuiEvents.NEW_LOAD);
                }
                return;
            }
            progressBar.setValue(jx);
            progressBar.setString(timeLeft + ":" + ix);
        }//end actionPerformed
    }; //end class
    //end create menu items

    private final JLabel shagLabel;
    private int ix;
    private int jx;
    private int timeLeft;
    private JPanel progBarPanel;
    private MazeObject[][] labelMatrix;
    private JProgressBar progressBar;
    private JPanel newPanel;// = new JPanel();
    TheArchitect theArc = new TheArchitect(); //se le quito el private
    private String[][] scrapMatrix;
    Timer timely; //se le quito el private
    final TimeKeeper timeKeeper; //se le quito el private
    String playerName; //se le quito el private
    int levelNum = 1; //se le quito el private
    File currentLevelDirectory; //se le quito el private
    private JLabel diamondsLabel;

    public GameGui() {
        super("Maze, a game of wondering"); //call super to initilize title bar of G.U.I.
        cp = getContentPane();
        shagLabel = new JLabel("", new ImageIcon("src/resources/assets/yeababyyea.jpg"), JLabel.LEFT);//GUI background for initial load
        cp.add(shagLabel);

        // Creamos la instancia del nuevo controlador de menu
        MenuController menuController = new MenuController(this);

        //Add Exit & New Game Menu Items
        JMenuItem itemExit = new JMenuItem("Exit");
        itemExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_MASK));//press CTRL+X to exit if you want
        JMenuItem itemSaveScore = new JMenuItem("Save High Score");
        itemSaveScore.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK));//press CTRL+S to save high score if you want
        JMenuItem itemHighScore = new JMenuItem("High Score");
        itemHighScore.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.CTRL_MASK));//press CTRL+H to view high score if you want
        JMenuItem itemEnterName = new JMenuItem("Enter Player Name");
        itemEnterName.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_MASK));//press CTRL+N to enter your name if you want
        JMenuItem newGameItem = new JMenuItem("New Game");
        JMenuItem openFileItem = new JMenuItem("Open Maze File.");
        openFileItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_MASK));//press CTRL+O to open a level if you want

        // Asignar los eventos al MenuController
        newGameItem.setActionCommand("New Game");
        newGameItem.addActionListener(menuController);
        itemEnterName.setActionCommand("EnterName");
        itemEnterName.addActionListener(menuController);
        itemSaveScore.setActionCommand("SaveScore");
        itemSaveScore.addActionListener(menuController);
        itemHighScore.setActionCommand("src.main.HighScore");
        itemHighScore.addActionListener(menuController);
        itemExit.setActionCommand("Exit");
        itemExit.addActionListener(menuController);
        openFileItem.setActionCommand("Open");
        openFileItem.addActionListener(menuController);

        JMenu newMenu = new JMenu("File");
        newMenu.add(newGameItem);
        newMenu.add(itemEnterName);
        newMenu.add(openFileItem);
        newMenu.add(itemHighScore);
        newMenu.add(itemSaveScore);
        newMenu.add(itemExit);

        //Add Exit Menu Item
        //Add Menu Bar
        //create menu items
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(newMenu);
        setJMenuBar(menuBar);
        //Add Menu Bar
        newPanel = new JPanel();
        highScore = new HighScore();
        timeKeeper = new TimeKeeper();

        // Inicializacion del panel de diamantes
        diamondsLabel = new JLabel("Total Diamonds Left to Collect: 0", JLabel.CENTER);
        JPanel dimondsPanel = new JPanel();
        dimondsPanel.add(diamondsLabel);
        cp.add(dimondsPanel, BorderLayout.SOUTH);

        pack();
        setVisible(true);//show our menu bar and shagLabel.. Yea baby Yea! Whoa.. to much java.
    }//end constructor

    private void handleTimeExpired() {
        timely.stop();
        //the game is over, here we must tell our high score method to recond the details.
        highScore.addHighScore(playerName, timeKeeper.getMinutes(), timeKeeper.getSeconds(), levelNum);

        remove(newPanel);
        if (progBarPanel != null) {
            remove(progBarPanel);
        }
        JLabel yousuckLabel = new JLabel("", new ImageIcon("src/resources/assets/yousuck.jpg"), JLabel.LEFT);
        cp.add(yousuckLabel);
        pack();
        setVisible(true);

        JFrame frame = new JFrame("Warning");
        JOptionPane.showMessageDialog(frame, "You Stupid Ass, Did you eat to much for dinner?  Move Faster!");//the entire game has ended.
    }

    public static void main(String[] args) {
        new GameGui();
    }


    public void loadMatrixGui(GuiEvents event) {
        if (event == GuiEvents.NEW_LOAD) {
            remove(newPanel);//remove the previous level's game from the screen
            if (progBarPanel != null)//remove the progress bar from the gui as long as its already been created.
                remove(progBarPanel);
            char[][] temp = fl.getGameMatrix();
            if (temp == null) {
                return;
            }
            scrapMatrix = new String[fl.getMatrixSizeRow()][fl.getMatrixSizeColumn()];
            for (int i = 0; i < scrapMatrix.length; i++) {
                //create a new matrix so we dont have a refrence to another objects matrix!
                for (int j = 0; j < scrapMatrix[i].length; j++) {
                    scrapMatrix[i][j] = String.valueOf(temp[i][j]);
                }
            }//end double for loop
            TimeCalculator timeCalc = new TimeCalculator();//create the time calculator used to determine how much time each level is given.
            timeCalc.calcTimeforMaze(fl.dimondCount(), fl.getMatrixSizeRow(), fl.getMatrixSizeColumn());//let time calculator know the parameters of the game
            timeLeft = timeCalc.getMinutes();//get the minutes allowed for the level
            ix = timeCalc.getSeconds();//get the seconds allowed for the level;
            jx = 0;//reset the variable used for keeping time to zero since its a new level
            timely = new Timer(1000, updateCursorAction);//create a timer to update the progress bar
            timely.start();//start the timer
            progBarPanel = new JPanel();//panel for progress bar
            progressBar = new JProgressBar(0, timeCalc.getMinutes() * 100);//minutes returns a single digit, we have to multiply it for Bar.
            progressBar.setStringPainted(true);
            progBarPanel.add(progressBar);
            cp.add(progBarPanel, BorderLayout.NORTH);
            newPanel = new JPanel();
            newPanel.setLayout(new GridLayout(fl.getMatrixSizeRow(), fl.getMatrixSizeColumn()));//set our panel for the game to the size of the matrix
            labelMatrix = new MazeObject[fl.getMatrixSizeRow()][fl.getMatrixSizeColumn()];
            for (int i = 0; i < labelMatrix.length; i++) {
                for (int j = 0; j < labelMatrix[i].length; j++) {
                    MapElements element = MapElements.getMapElementFromChar(scrapMatrix[i][j].charAt(0));
                    labelMatrix[i][j] = new MazeObject(element);
                    newPanel.add(labelMatrix[i][j]);//add our maze images into the gui
                }
            }
            newPanel.addKeyListener(new MyKeyHandler());
            cp.add(newPanel);
            remove(shagLabel);//remove the constructors initial background
            pack();
            setVisible(true);
            newPanel.grabFocus();
        }//end if
        else if (event == GuiEvents.UPDATE_LOAD)//every time the player moves the gui must be updated.
        {
            scrapMatrix = theArc.getUpdatedMatrix();//get the new matrix to be displayed from the architect
            for (int i = 0; i < labelMatrix.length; i++) {
                for (int j = 0; j < labelMatrix[i].length; j++) {
                    MapElements element = MapElements.getMapElementFromChar(scrapMatrix[i][j].charAt(0));
                    labelMatrix[i][j].setElement(element);
                }
            }
        }
    }//end loadMatrixGui method

    public void nextLevelLoad() {
        levelNum += 1;
        catFileName = levelNum;
        timeKeeper.timeTracker(timeLeft, ix);//The src.main.TimeKeeper object keeps a running tab of the total time the player has used.(for high score)
        timely.stop();//dont count while we are loading the next level.
        theArc = new TheArchitect();//flush everything from src.main.TheArchitect so we dont get goffee results

        String nextFileName = "level" + levelNum + ".maz";
        File nextFile = (currentLevelDirectory != null) 
                ? new File(currentLevelDirectory, nextFileName) 
                : new File(nextFileName);

        if (fl.loadFile(nextFile.getAbsolutePath())) {//load the file we need
            theArc.setExit(fl.ExitXCord(), fl.ExitYCord());
            loadMatrixGui(GuiEvents.NEW_LOAD);
        } else {
            // Fin de los niveles disponibles
            highScore.addHighScore(playerName, timeKeeper.getMinutes(), timeKeeper.getSeconds(), levelNum - 1);
            JOptionPane.showMessageDialog(
                this, 
                "¡Felicidades! Has completado todos los laberintos.\nTiempo total: " 
                + timeKeeper.getMinutes() + "m " + timeKeeper.getSeconds() + "s",
                "¡Victoria!", 
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private class MyKeyHandler extends KeyAdapter //captures arrow keys movement
    {
        public void keyPressed(KeyEvent theEvent) {
            switch (theEvent.getKeyCode()) {
                case KeyEvent.VK_UP: {
                    theArc.playerMove(-1, 0, scrapMatrix, fl.dimondCount());//let the Architect know we moved, along with the current matrix
                    loadMatrixGui(GuiEvents.UPDATE_LOAD);//reload the gui to show the move
                    if (theArc.getLevel()) {
                        nextLevelLoad();//if the player hit an exit door, load the next level
                    }
                    break;
                }
                case KeyEvent.VK_DOWN: {
                    theArc.playerMove(1, 0, scrapMatrix, fl.dimondCount());//see above
                    loadMatrixGui(GuiEvents.UPDATE_LOAD);//see above
                    if (theArc.getLevel())//see above
                    {
                        nextLevelLoad();//see above
                    }
                    break;
                }
                case KeyEvent.VK_LEFT: {
                    theArc.playerMove(0, -1, scrapMatrix, fl.dimondCount());//see above
                    loadMatrixGui(GuiEvents.UPDATE_LOAD);//see above
                    if (theArc.getLevel())//see above
                    {
                        nextLevelLoad();//see above
                    }
                    break;
                }
                case KeyEvent.VK_RIGHT: {
                    theArc.playerMove(0, 1, scrapMatrix, fl.dimondCount()); //see above
                    loadMatrixGui(GuiEvents.UPDATE_LOAD);//see above
                    if (theArc.getLevel()) {
                        nextLevelLoad();//see above
                    }
                    break;
                }
            }//end switch
            diamondsLabel.setText("Total Diamonds Left to Collect: " + theArc.getDimondsLeft());
        }//end method
    }//end inner class


}//end class    