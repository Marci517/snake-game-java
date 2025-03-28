# Java Snake Game

This is a simple Snake game implemented using Java Swing. It was created as a basic project by following tutorials to understand Java graphics, event handling, and game loops.

## Features

- Custom image used as the snake's food (e.g. `flower.png`)
- Background music playback (`example.wav`)
- Score tracking during gameplay
- Save your score to a file after the game ends
- Simple keyboard controls (arrow keys)
- Help dialog with instructions for playing

## How to Play

- Use the arrow keys to move the snake:
  - Up: ↑
  - Down: ↓
  - Left: ←
  - Right: →
- Eat the flower to grow longer and earn points.
- Avoid hitting the walls or your own body.
- When the game ends:
  - You can click the **Help** button to view the rules again.
  - You can save your score via the **File > Save your score** menu.

## Requirements

- Java JDK 8 or higher, the project uses Java Streams (e.g. for checking collisions), so Java 8 or later is required.
- An image file named `flower.png` in the project root (used as the food)
- An audio file named `example.wav` in the project root (used for background music)

## Notes

This is a very basic project and was made primarily for practice. The food image and background music are loaded from files, so make sure `flower.png` and `example.wav` are placed in the same directory as the compiled classes.
