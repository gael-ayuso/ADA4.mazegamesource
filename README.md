# Cambios

# 1
Reemplazar en `TimeCalculator.java` y `TimeKeeper.java` los atributos `minutes` y `seconds` por el tipo `Duration`.
# 2 
Separar MazeObjects en `MazeObject.java` y crea un enum con los tipos de objetos que pueden existir en el mapa.
# 3
Se agregó el ENUM `GuiEvents` 
(public enum GuiEvents {
NEW_LOAD,
UPDATE_LOAD
}) 
para mejorar la lectura del codigo
# 4
En `GameGui.java` en el caso de **New Game** se agregó la funcionalidad de abrir el nivel 1 en automatico