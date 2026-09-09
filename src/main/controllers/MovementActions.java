package src.main.controllers;

public interface MovementActions {
    void movePlayer(int rowDelta, int colDelta);

    default void moveUp() {
        movePlayer(-1, 0);
    }

    default void moveDown() {
        movePlayer(1, 0);
    }

    default void moveLeft() {
        movePlayer(0, -1);
    }

    default void moveRight() {
        movePlayer(0, 1);
    }
}
