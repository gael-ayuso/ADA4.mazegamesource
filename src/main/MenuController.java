package src.main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MenuController implements ActionListener {

    private final GameGui gameGui;

    public MenuController(GameGui gameGui) {
        this.gameGui = gameGui;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Exit" -> {
                //exit on the menu bar
                if (gameGui.timely != null) {
                    gameGui.timely.stop();
                }
                System.exit(0); //exit the system.
            }
            case "New Game" -> {
                /**
                 * Se implementó la funcionalidad para que al
                 * presionar en "New game" te abra en automatico el nivel 1
                 * */
                // 1. Detener el temporizador previo si ya había una partida en curso
                if (gameGui.timely != null) {
                    gameGui.timely.stop();
                }

                // 2. Reiniciar los contadores de nivel y el estado del juego
                gameGui.levelNum = 1;
                gameGui.catFileName = 1;
                gameGui.theArc = new TheArchitect();

                // 3. Cargar el nivel 1
                File level1File = new File("src/resources/levels/level1.maz");
                gameGui.currentLevelDirectory = level1File.getParentFile();

                if (level1File.exists() && gameGui.fl.loadFile(level1File.getAbsolutePath())) {
                    gameGui.theArc.setExit(gameGui.fl.ExitXCord(), gameGui.fl.ExitYCord());
                    gameGui.loadMatrixGui(GuiEvents.NEW_LOAD);
                } else {
                    JOptionPane.showMessageDialog(
                            gameGui,
                            "No se encontró el archivo del nivel 1 en:\n" + level1File.getAbsolutePath(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
            case "EnterName" -> {
                //Allows user to enter their name for high score
                JOptionPane optionPane = new JOptionPane();
                gameGui.playerName = JOptionPane.showInputDialog("Please Enter your Earth Name");
            }
            case "src.main.HighScore" -> {
                //Displays the high scores
                ScoreGui sg = new ScoreGui();
                sg.ScoreGui();
            }
            case "SaveScore" -> {
                //allows the user to save their score at any time.
                gameGui.highScore.addHighScore(
                        gameGui.playerName,
                        gameGui.timeKeeper.getMinutes(),
                        gameGui.timeKeeper.getSeconds(),
                        gameGui.levelNum
                );
            }
            case "Open" -> {
                //to start the game you have to open a maze file. this is on the menu
                JFileChooser chooser = new JFileChooser(".");
                int returnVal = chooser.showOpenDialog(gameGui);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = chooser.getSelectedFile();
                    gameGui.currentLevelDirectory = selectedFile.getParentFile();

                    // Extraer el número de nivel desde el nombre (ej. "level5.maz" -> 5)
                    String numericPart = selectedFile.getName().replaceAll("\\D+", "");
                    if (!numericPart.isEmpty()) {
                        gameGui.levelNum = Integer.parseInt(numericPart);
                    } else {
                        gameGui.levelNum = 1;
                    }
                    gameGui.catFileName = gameGui.levelNum;

                    if (gameGui.fl.loadFile(selectedFile.getAbsolutePath())) {//load the file we need using absolute path
                        gameGui.theArc.setExit(gameGui.fl.ExitXCord(), gameGui.fl.ExitYCord());
                        gameGui.loadMatrixGui(GuiEvents.NEW_LOAD);
                    }
                }
            }
        }
    }
}