package Maze.MazeBuilder;

import Helpers.Coordinate;
import Helpers.Direction;
import Maze.Maze;

public interface IMazeBuilder {

    IMazeBuilder buildMazeSkeleton(int height, int width);
    IMazeBuilder buildAllCellsAsEmpty();
    IMazeBuilder buildCell(Coordinate position);
    IMazeBuilder buildDoor(Coordinate cell1Pos, Coordinate cell2Pos);
    IMazeBuilder buildDoor(Coordinate cell1Pos, Direction doorDirection);

    IMazeBuilder buildEntrance(Coordinate pos, Direction direction);
    IMazeBuilder buildExit(Coordinate pos, Direction direction);

    Maze getMaze();
}
