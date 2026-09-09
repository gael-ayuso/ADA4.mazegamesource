package src.main.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuController implements ActionListener {

    private final MenuActions menuActions;

    public MenuController(MenuActions menuActions) {
        this.menuActions = menuActions;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Exit" -> menuActions.exitGame();
            case "New Game" -> menuActions.startNewGame();
            case "EnterName" -> menuActions.enterPlayerName();
            case "src.main.HighScore" -> menuActions.showHighScores();
            case "SaveScore" -> menuActions.saveHighScore();
            case "Open" -> menuActions.openMazeFile();
        }
    }
}