# Kotlin Pong with LibGDX

A modern implementation of the classic Pong game built with Kotlin and LibGDX. This project demonstrates how to create a game with LibGDX while leveraging Kotlin's concise syntax and powerful features.

## Overview

This project showcases:
- Integration between Kotlin and the LibGDX game development framework
- A versatile bouncing ball with realistic physics simulation
- Competitive two-player Pong gameplay with scoring system
- Interactive menu system with multiple navigation options
- Consistent visual design across all screens
- Proper game architecture following LibGDX best practices
- Clean code organization with proper separation of concerns

## Features

- **Interactive Main Menu**:
  - Keyboard navigation with arrow keys (UP/DOWN)
  - Mouse/touch input support for direct selection
  - Multiple menu options (Play Game, Settings, Exit)
  - Dynamic bouncing ball animation in the background
  - Animated title with floating letters and particle effects
  - Looping background music for immersive experience

- **Classic Pong Gameplay**:
  - Two-player paddling action (W/S for left paddle, UP/DOWN for right)
  - Accurate ball physics with angle-based paddle bouncing
  - Scoring system when balls pass paddles
  - Center line divider for visual court separation
  - Pause functionality with SPACE key or touch
  - Return to menu with ESC key

- **Visual Effects**:
  - Particle system for dynamic visual enhancements
  - Bouncing ball with optional dynamic color changes on collision
  - Smooth animations and paddle movements
  - High-quality font rendering with FreeType
  - Multiple font sizes and styles for different UI elements

- **Quality Implementation**:
  - Proper screen management and transitions
  - Resource handling with automatic cleanup
  - Efficient rendering using LibGDX's graphics pipeline
  - Configurable ball behavior for different contexts
  - Modular code organization with clearly defined responsibilities

## Project Architecture

The project follows a modular architecture with clear separation of concerns:

- **PongGame.kt**: Core game class that manages screen transitions
- **MainMenu.kt**: Interactive menu screen with options and navigation
- **PongLevel.kt**: Main game screen with paddles, ball, and scoring
- **Ball.kt**: Versatile physics and rendering implementation for the ball
- **Paddle.kt**: Paddle movement, rendering, and player controls
- **ParticleSystem.kt**: Manages particle effects for visual enhancements
- **App.kt**: Application entry point that configures and launches the game

Each class is designed with:
- Clear constants and companion objects for magic values
- Small, single-responsibility methods
- Consistent formatting and naming conventions
- Comprehensive documentation for all public methods

## Controls

- **Main Menu**:
  - UP/DOWN: Navigate menu options
  - ENTER: Select highlighted option
  - Mouse/Touch: Click directly on menu options

- **Game Level**:
  - W/S: Move left paddle up/down
  - UP/DOWN: Move right paddle up/down
  - SPACE: Pause/unpause game
  - ESC: Return to main menu

## Requirements

- JDK 21 or higher
- Gradle (included via wrapper)
- LibGDX dependencies (managed through Gradle)

## Asset Files

The game expects the following asset files to be present:
- `fonts/roboto.ttf` - Font file for all text rendering
- `audio/menu_theme.wav` - Looping background music for the main menu

## Building and Running

This project uses Gradle for build automation. You can run it with the following commands: