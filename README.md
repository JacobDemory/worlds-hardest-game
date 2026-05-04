# World's Hardest Game (Java)

A Java desktop game inspired by *The World's Hardest Game*, built with AWT/Swing-style rendering, keyboard input, collision detection, enemy movement, collectibles, and skill-based gameplay.

This project began as an early college programming assignment and was refactored into a cleaner game development and object-oriented programming showcase.

---

## Features
Recent additions are in **bold**
- Player movement using keyboard input
- Moving enemy obstacles with vertical and horizontal patrol patterns
- Spinning obstacle hazards
- Collision detection between the player and hazards
- Respawn behavior after collisions
- Win condition when the player collects all coins and reaches the goal area
- Timer tracking elapsed play time
- Death tracking instead of negative score values
- **Start screen with instructions**
- **Restart functionality using the `R` key**
- **Cleaner HUD showing deaths, time, coins, and controls**
- **Updated board design with start/goal zones and a grid-based playfield**
- **Refactored Java classes for clearer game state, movement, and rendering logic**

---

## Tech Stack
- Java
- AWT / Swing-style desktop rendering
- Object-Oriented Programming
- Collision detection
- Game loop logic

---

## Gameplay

Navigate the red player square from the starting safe zone to the goal zone while avoiding moving enemies and spinning obstacles. Collect all coins before reaching the goal to win.

Controls:
```txt
Arrow Keys - Move player
Enter      - Start game
R          - Restart game
```

> Screenshot coming soon.

---

## How to Run

1. **Prerequisites**: Ensure Java is installed.
   ```bash
   java -version
   ```

2. **Compile the project**:
   ```bash
   javac src/game/*.java
   ```

3. **Run the game**:
   ```bash
   java -cp src game.WorldsHardestGame
   ```

---

## Project Structure

```txt
worlds-hardest-game/
├── src/
│   └── game/
│       ├── Enemy.java
│       ├── Game.java
│       ├── IntersectionDetectable.java
│       ├── Player.java
│       ├── Point.java
│       ├── Polygon.java
│       ├── SpinningRectangle.java
│       └── WorldsHardestGame.java
├── assets/
├── README.md
└── .gitignore
```

---

## Main Classes

### `WorldsHardestGame`
Controls the main game setup, game loop, rendering, win condition, timer, deaths, coins, enemies, and keyboard input.

### `Player`
Represents the controllable red square, including movement flags, boundary checks, respawn behavior, and drawing logic.

### `Enemy`
Represents circular hazards that patrol horizontally or vertically and collide with the player.

### `SpinningRectangle`
Represents rotating rectangular hazards that add additional obstacle variety.

### `Game`
Handles the desktop window and double-buffered drawing canvas.

---

## What I Learned

- How to build a basic Java desktop game
- How game loops update and repaint the screen
- How to handle keyboard input in Java
- How collision detection works with custom game objects
- How object-oriented design can be used to model players, enemies, hazards, and gameplay state
- How an early programming project can be refactored into cleaner, more maintainable code

---

## Future Improvements

- Add multiple levels with increasing difficulty
- Add additional enemy path patterns
- Add sound effects and background music
- Add a menu and level-select screen
- Add persistent best times and lowest-death records
- Add screenshots or a short gameplay demo
- Improve collision precision for spinning obstacles
- Add more polished visual assets
