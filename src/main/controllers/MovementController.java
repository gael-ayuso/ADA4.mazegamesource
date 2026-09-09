package src.main.controllers;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MovementController extends KeyAdapter {

    private final MovementActions movementActions;

    public MovementController(MovementActions movementActions) {
        this.movementActions = movementActions;
    }

    @Override
    public void keyPressed(KeyEvent theEvent) {
        switch (theEvent.getKeyCode()) {
            case KeyEvent.VK_UP -> movementActions.moveUp();
            case KeyEvent.VK_DOWN -> movementActions.moveDown();
            case KeyEvent.VK_LEFT -> movementActions.moveLeft();
            case KeyEvent.VK_RIGHT -> movementActions.moveRight();
        }
    }
}