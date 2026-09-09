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

## 7
Eliminación del control de flujo por excepciones (`SlowAssPlayer` y `StupidAssMove`).
Se suprimieron las clases `SlowAssPlayer` (en `GameGui`) y `StupidAssMove` (en `TheArchitect`). Estas clases violaban las buenas prácticas para situaciones normales del juego (quedarse sin tiempo o chocar contra una pared). 
Robustez y Mantenibilidad.

## 8
Creación del paquete `src.main.controllers` y desacoplamiento de eventos del teclado en `MovementController.java`.
Se extrajo la clase `MyKeyHandler` de `GameGui` hacia `MovementController`, esta asume la responsabilidad del movimiento del jugador.
Mantenibilidad y Separación de Responsabilidades

## 9
Desacoplamiento de `MenuController` y `MovementController` respecto a `GameGui` mediante interfaces de acción.
- **Interfaces `MenuActions` y `MovementActions`:** Se crearon interfaces específicas en `src.main.controllers` aplicando el Principio de Inversión de Dependencias (DIP) y Segregación de Interfaces (ISP). Los controladores ahora dependen únicamente de estas abstracciones y no de la clase concreta `GameGui`.
- **Eliminación de dependencias y manipulación interna:** Se eliminó de `MenuController` y `MovementController` todo acoplamiento con `TheArchitect`, `FileLoader`, `GuiEvents`, temporizadores y componentes gráficos de Swing. Su función se limita a capturar eventos y delegar acciones.
- **Restauración del encapsulamiento en `GameGui`:** `GameGui` implementa `MenuActions` y `MovementActions`, concentrando la ejecución de las acciones y permitiendo volver a declarar como `private` todos sus atributos internos (`theArc`, `scrapMatrix`, `fileLoader`, `timely`, `highScore`, `timeKeeper`, `levelNum`, etc.).