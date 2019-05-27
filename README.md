# Maze Game

[![CodeFactor](https://www.codefactor.io/repository/github/rluvaton/maze/badge)](https://www.codefactor.io/repository/github/rluvaton/maze)  [![Build Status](https://travis-ci.com/rluvaton/Maze.svg?branch=master)](https://travis-ci.com/rluvaton/Maze)

> Written in OpenJDK 11 and use Gradle

## About

This project started as collage assignment to build _multithreaded game_, I choose **Multiplayer Maze**

I created this as an _Open Source_ Project so that people in the future can contribute.



## Features

The game have 2 modes:


<details><summary>Step Mode</summary>
<p>

Can choose between different steps, and play with friends

Available Steps:
- Very Easy
- Easy
- Medium
- Hard
- Very Hard

</p>
</details>

<details><summary>Custom Mode</summary>
<p>

In the custom mode you can generate maze by this configuration:
- Maze shape (_Currently only rectangular shape available_)
- Width of the maze
- Height of the maze

-  Number of entrances
-  Number of exits
-  Minimum distance between the exit and the entrances
-  Candy configuration:
	- Select candies types that will be in the maze
	- Select the number of candies that will be created
	- Choose if the candies will only be good candies
	- Choose if the candies be expired candies 
- Create players to play:
	- Create Computer player to play against or human player

	- Choose the name of the player and his color
	- In case of human player you can set the navigation keys, speed increase key and exit key
	- Select human player speed

</p>
</details>

### Candies
- Point Candy: increase / decrease the user points (_Currently doesn't have any effect see in development_)
- Time Candy: increase / decrease the user running time until expired (_Currently doesn't have any effect see in development_)
- Location Candy: Teleport you to different location in the maze


- Point and time candy can be either good or bad

- Each candy can be expired after certain time (not mandatory)

## Currently In Development

- The Points and Time candy doesn't have any effect on the game (in the future they will increase/decrease user time/points)
- Different Maze shapes
- Generate button - should generate maze at wanted configuration and export it
- Users Stats - Currently the user statistics use demo data, in the future it will contain data from the game
