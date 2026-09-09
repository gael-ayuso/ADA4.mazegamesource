package src.main.controllers;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import src.main.GameGui;
import src.main.GuiEvents;

// El resto de la clase queda exactamente igual

public class MovementController extends KeyAdapter //captures arrow keys movement
{
    private final GameGui gameGui;

    public MovementController(GameGui gameGui) {
        this.gameGui = gameGui;
    }

    @Override
    public void keyPressed(KeyEvent theEvent) {
        switch (theEvent.getKeyCode()) {
            case KeyEvent.VK_UP: {
                gameGui.theArc.playerMove(-1, 0, gameGui.scrapMatrix, gameGui.fileLoader.dimondCount());//let the Architect know we moved, along with the current matrix
                gameGui.loadMatrixGui(GuiEvents.UPDATE_LOAD);//reload the gui to show the move
                if (gameGui.theArc.getLevel()) {
                    gameGui.nextLevelLoad();//if the player hit an exit door, load the next level
                }
                break;
            }
            case KeyEvent.VK_DOWN: {
                gameGui.theArc.playerMove(1, 0, gameGui.scrapMatrix, gameGui.fileLoader.dimondCount());//see above
                gameGui.loadMatrixGui(GuiEvents.UPDATE_LOAD);//see above
                if (gameGui.theArc.getLevel())//see above
                {
                    gameGui.nextLevelLoad();//see above
                }
                break;
            }
            case KeyEvent.VK_LEFT: {
                gameGui.theArc.playerMove(0, -1, gameGui.scrapMatrix, gameGui.fileLoader.dimondCount());//see above
                gameGui.loadMatrixGui(GuiEvents.UPDATE_LOAD);//see above
                if (gameGui.theArc.getLevel())//see above
                {
                    gameGui.nextLevelLoad();//see above
                }
                break;
            }
            case KeyEvent.VK_RIGHT: {
                gameGui.theArc.playerMove(0, 1, gameGui.scrapMatrix, gameGui.fileLoader.dimondCount()); //see above
                gameGui.loadMatrixGui(GuiEvents.UPDATE_LOAD);//see above
                if (gameGui.theArc.getLevel()) {
                    gameGui.nextLevelLoad();//see above
                }
                break;
            }
        }//end switch
        gameGui.diamondsLabel.setText("Total Diamonds Left to Collect: " + gameGui.theArc.getDimondsLeft());
    }//end method
}//end inner class