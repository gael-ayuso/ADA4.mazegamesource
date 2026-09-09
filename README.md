# Cambios

## 1
Reemplazar en `TimeCalculator.java` y `TimeKeeper.java` los atributos `minutes` y `seconds` por el tipo `Duration`.
## 2 
Separar MazeObjects en `MazeObject.java` y crea un enum con los tipos de objetos que pueden existir en el mapa.
## 3
Se agregó el ENUM `GuiEvents` 
(public enum GuiEvents {
NEW_LOAD,
UPDATE_LOAD
}) 
para mejorar la lectura del codigo
## 4
En `GameGui.java` en el caso de **New Game** se agregó la funcionalidad de abrir el nivel 1 en automatico

## 5
Se eliminó la creación repetitiva de instancias de `JPanel` y `JLabel` (en la zona de diamantes) dentro de cada movimiento capturado por `MyKeyHandler`. Ahora el panel y la etiqueta se inicializan una sola vez en el constructor y durante el juego solo se actualiza su texto mediante `.setText(...)`.
Eficiencia

## 6
Se retiró la interfaz `ActionListener` de `GameGui` y se pasó lo de `actionPerformed` hacia la nueva clase `MenuController`.SRP

# 7
Eliminación del control de flujo por excepciones (`SlowAssPlayer`) el cual se utilizaba para controlar el flujo del juego al agotarse el tiempo. Se reemplazó por el método `handleTimeExpired()` invocado desde la acción del temporizador (`updateCursorAction`). 
Mantenibilidad

