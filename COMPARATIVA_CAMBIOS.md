# Comparativa y Registro de Mejoras Implementadas: MazeGame

Este documento detalla los cambios relevantes y las mejoras arquitectónicas, técnicas y de rendimiento implementadas en el proyecto actual con respecto a la versión original contenida en `mazegamesource.zip`.

---

## 1. Tabla Comparativa General

| Área / Característica | Proyecto Original (`mazegamesource.zip`) | Proyecto Actual (`src/`) |
| :--- | :--- | :--- |
| **Organización y Paquetes** | Monolítico sin paquetes (todo en el paquete por defecto raíz junto a imágenes y niveles). | Modularizado en paquetes estructurados (`src.main`, `src.main.controllers`, `src.main.gui.elements`, `src.resources`). |
| **Control de Eventos del Menú** | `GameGui` implementaba directamente `ActionListener` (tipo *God Class*). | Delegado a `MenuController`, desacoplado de Swing y de `GameGui` mediante la interfaz `MenuActions`. |
| **Control de Movimiento del Jugador** | Clase interna `MyKeyHandler` dentro de `GameGui` con acceso directo a variables de estado. | Delegado a `MovementController`, desacoplado mediante la interfaz `MovementActions`. |
| **Representación de Elementos del Mapa** | Caracteres y cadenas mágicas literales dispersas (`"P"`, `"W"`, `"D"`, `"H"`, `"M"`, `"N"`, `"E"`). | Tipo seguro y centralizado a través del enum `MapElements` con símbolos y nombres de archivos de textura. |
| **Renderizado y Gestión Gráfica** | Clase interna `mazeObject`. Destrucción y recreación completa del `JPanel` y sus `JLabel` en cada movimiento, forzando `System.gc()`. | Clase independiente `MazeObject` con caché estático de `ImageIcon` (`Map<MapElements, ImageIcon>`) y actualización en sitio con `.setElement()`. |
| **Eventos de Actualización de Interfaz** | Strings mágicos (`"newLoad"`, `"updateLoad"`) con comparaciones frágiles por identidad (`event == "newLoad"`). | Tipo seguro en tiempo de compilación utilizando el enum `GuiEvents` (`NEW_LOAD`, `UPDATE_LOAD`). |
| **Manejo del Tiempo** | Variables primitivas `int minutes` e `int seconds` con operaciones aritméticas manuales de acarreo. | Uso de la API moderna `java.time.Duration` en `TimeKeeper` y `TimeCalculator`. |
| **Gestión de Memoria en el HUD** | Fuga de memoria: en cada pulsación de tecla se creaba un nuevo `JPanel` y `JLabel` de diamantes apilados en el layout sur. | Instancia única y fija de `diamondsLabel` en el constructor, actualizada dinámicamente con `.setText(...)`. |
| **Manejo de Errores y Excepciones** | Antipatrón de control de flujo por excepciones (`StupidAssMove` y `SlowAssPlayer`) que abrían diálogos en sus constructores. | Eliminación de excepciones de flujo. Control estructurado mediante condicionales normales y el método `handleTimeExpired()`. |
| **Flujo de Niveles y Fin de Juego** | Opción "New Game" sin implementar (`return`). Transición de niveles rígida sin manejo de victoria al terminar el juego. | "New Game" inicia y carga automáticamente el Nivel 1. Soporte de directorio activo y pantalla de victoria al superar todos los niveles. |

---

## 2. Desglose de Cambios Relevantes y Mejoras Implementadas

### 2.1. Arquitectura Modular y Separación por Paquetes
En la versión original, todos los archivos `.java`, recursos multimedia (`.png`, `.jpg`), mapas de nivel (`.maz`) y datos de puntuaciones (`scores.txt`) se encontraban en el directorio raíz sin ningún empaquetado. 

En la versión actual se organizó el proyecto en paquetes con responsabilidades delimitadas:
- **`src.main`**: Contiene el núcleo del juego, modelo, lógica de tiempo y persistencia (`GameGui`, `TheArchitect`, `FileLoader`, `TimeCalculator`, `TimeKeeper`, `HighScore`, `ScoreGui`, `GuiEvents`).
- **`src.main.controllers`**: Contiene los controladores de eventos y sus interfaces abstractas (`MenuController`, `MovementController`, `MenuActions`, `MovementActions`).
- **`src.main.gui.elements`**: Contiene la abstracción y renderizado de los componentes visuales del laberinto (`MapElements`, `MazeObject`).
- **`src.resources`**: Separa los activos estáticos en `assets` (gráficos e imágenes) y `levels` (archivos `.maz` de cada laberinto).

---

### 2.2. Desacoplamiento de Controladores (Principios SOLID: SRP, DIP e ISP)
- **Principio de Responsabilidad Única (SRP):** Se retiró la implementación de `ActionListener` de `GameGui`. El procesamiento de las opciones de menú se trasladó a `MenuController`, y la captura de eventos de teclado se extrajo a `MovementController`.
- **Inversión de Dependencias (DIP) y Segregación de Interfaces (ISP):**
  - Se crearon las interfaces `MenuActions` y `MovementActions`. Los controladores dependen exclusivamente de estas abstracciones y no conocen la implementación concreta de la interfaz gráfica (`GameGui`).
  - `MovementActions` incorpora métodos por defecto (`moveUp()`, `moveDown()`, `moveLeft()`, `moveRight()`) que mapean los desplazamientos hacia `movePlayer(rowDelta, colDelta)`.
- **Restauración del Encapsulamiento:** Al no necesitar exponer sus atributos internos a clases escuchadoras externas ni clases internas invasivas, `GameGui` declaró nuevamente como `private` (y en varios casos `final`) sus atributos de estado (`theArc`, `scrapMatrix`, `fileLoader`, `timely`, `timeKeeper`, `levelNum`, etc.).

---

### 2.3. Modelado de Dominio Seguro y Renderizado Eficiente (`MapElements` y `MazeObject`)
- **Enum `MapElements`:** Sustituyó las cadenas y caracteres literales mágicos dispersos por una definición tipada (`EMPTY`, `WALL`, `PLAYER`, `DIAMOND`, `HIDDEN_DIAMOND`, `MOVABLE_WALL`, `EXIT`). Cada constante encapsula su carácter identificador (`symbol`) y el nombre de su archivo de imagen (`fileName`), además de proveer el método de fábrica estático `getMapElementFromChar(char)`.
- **Clase `MazeObject`:** 
  - Se independizó de `GameGui` convirtiéndose en una clase pública que extiende `JLabel`.
  - **Caché estático de iconos:** Implementa un `Map<MapElements, ImageIcon>` que precarga una sola vez las texturas desde los recursos, evitando leer y decodificar imágenes del disco en cada movimiento.
  - **Reutilización de componentes en caliente:** Incorpora el método `setElement(MapElements element)`, el cual permite cambiar el icono del elemento existente sobre la cuadrícula en lugar de destruir y recrear todo el panel en cada desplazamiento del jugador.

---

### 2.4. Seguridad de Tipos en Eventos de la Interfaz (`GuiEvents`)
En el código original, el método `loadMatrixGui(String event)` utilizaba comparaciones por identidad de strings (`event == "newLoad"`), lo cual representaba un riesgo de errores de ejecución sutiles. Se creó el enum:
```java
public enum GuiEvents {
    NEW_LOAD,
    UPDATE_LOAD
}
```
permitiendo que `loadMatrixGui(GuiEvents event)` verifique los tipos de manera estricta durante la compilación.

---

### 2.5. Corrección de Fuga de Memoria (Memory Leak) en el HUD de Diamantes
En el código original, dentro del manejador de teclado, cada vez que el usuario presionaba una tecla se ejecutaban las siguientes líneas:
```java
JLabel mainLabel = new JLabel("Total Dimonds Left to Collect" + theArc.getDimondsLeft() + "", JLabel.CENTER);
JPanel dimondsPanel = new JPanel();
dimondsPanel.add(mainLabel);
cp.add(dimondsPanel, BorderLayout.SOUTH);
```
Esto creaba y acumulaba indefinidamente componentes en el contenedor sur de la ventana.

En la versión actual:
- Se inicializa una sola vez el componente `diamondsLabel` en el constructor de `GameGui`.
- En cada movimiento solo se actualiza su texto mediante:
```java
diamondsLabel.setText("Total Diamonds Left to Collect: " + theArc.getDimondsLeft());
```
eliminando la sobrecarga de memoria y la creación innecesaria de objetos.

---

### 2.6. Erradicación del Antipatrón de Control de Flujo por Excepciones
En el diseño original se crearon clases que extendían `RuntimeException` para controlar eventos normales y previsibles del juego:
- `StupidAssMove`: Se lanzaba cuando el jugador intentaba moverse hacia una pared.
- `SlowAssPlayer`: Se lanzaba cuando el temporizador de nivel llegaba a cero.
Ambas clases abrían cuadros de diálogo `JOptionPane` dentro de sus propios constructores.

En la versión actual:
- Se eliminaron por completo ambas clases.
- La colisión contra obstáculos se maneja como una rama condicional directa dentro del método `playerMove` de `TheArchitect`.
- El vencimiento del tiempo se gestiona limpiamente a través del método estructurado `handleTimeExpired()` en `GameGui`, deteniendo el reloj, registrando la puntuación y actualizando la vista.

---

### 2.7. Modernización del Manejo de Tiempo (`java.time.Duration`)
- En `TimeKeeper` y `TimeCalculator`, los campos primitivos `int minutes` e `int seconds` se sustituyeron por instancias de `java.time.Duration`.
- Se aprovecha la inmutabilidad y precisión de los métodos `.plusMinutes()`, `.plusSeconds()`, `.toMinutesPart()` y `.toSecondsPart()`.
- En `TimeKeeper`, se corrigió el método con sintaxis errónea `public void TimeKeeper(int min, int sec)` (que coincidía con el nombre de la clase pero retornaba `void`), renombrándolo formalmente a `timeTracker(int min, int sec)`.

---

### 2.8. Flujo de Juego, Gestión de Rutas y Condición de Victoria
- **Funcionalidad "New Game":** En el proyecto original la acción estaba vacía (`return`). En la versión actual reinicia los puntajes, reinicializa el estado, detiene temporizadores activos y carga automáticamente el nivel 1 (`src/resources/levels/level1.maz`).
- **Navegación Dinámica de Niveles:** Al abrir un mapa mediante `JFileChooser`, se preserva `currentLevelDirectory`, lo que permite que las cargas de los siguientes niveles (`level2.maz`, `level3.maz`, etc.) se resuelvan dinámicamente en el mismo directorio sin depender rígidamente del directorio de trabajo del sistema.
- **Condición de Fin de Juego (Victoria):** En el original, al superar el último nivel el juego intentaba leer un archivo inexistente y fallaba. En la nueva versión se detecta cuando no existen más niveles, registrando el récord final y presentando un diálogo informativo con el tiempo total transcurrido.
