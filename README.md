# Kotlin Pong with LibGDX

A modern implementation of the classic Pong game built with Kotlin and LibGDX. This project demonstrates how to create a game with LibGDX while leveraging Kotlin's concise syntax and powerful features.

## Overview

This project showcases:
- Integration between Kotlin and the LibGDX game development framework
- A colorful bouncing ball with realistic physics simulation
- Interactive menu system with multiple navigation options
- Proper game architecture following LibGDX best practices

## Features

- **Interactive Main Menu**:
  - Keyboard navigation with arrow keys
  - Mouse/touch input support
  - Multiple menu options (Play, Settings, Exit)

- **Visual Effects**:
  - Bouncing ball with dynamic color changes on collision
  - Smooth animations and transitions
  - High-quality font rendering with FreeType

- **Quality Implementation**:
  - Proper screen management
  - Resource handling with automatic cleanup
  - Efficient rendering using LibGDX's graphics pipeline

## Project Architecture

The project follows a modular architecture with clear separation of concerns:

- **PongGame.kt**: Core game class that manages screen transitions
- **MainMenu.kt**: Interactive menu screen with options and navigation
- **Ball.kt**: Physics and rendering implementation for the bouncing ball
- **App.kt**: Application entry point that configures and launches the game

## Requirements

- JDK 21 or higher
- Gradle (included via wrapper)

## Building and Running

This project uses Gradle for build automation. You can run it with the following commands: