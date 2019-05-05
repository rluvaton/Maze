package Maze.MazeBuilder;

import Helpers.Coordinate;
import Helpers.Direction;
import Maze.Maze;
import Maze.ELocation;
import Maze.ELocationType;
import Maze.Cell;

import java.util.Arrays;
import java.util.Collection;

import static Helpers.Utils.Instance;

public class RectangleMaze extends BaseMazeBuilder {
    private Cell[][] mazeData;

    private int height;
    private int width;

    @Override
    public IMazeBuilder buildMazeSkeleton(int height, int width) {
        this.mazeData = new Cell[height][width];

        this.height = height;
        this.width = width;

        return this;
    }

    @Override
    public IMazeBuilder buildAllCellsAsEmpty() {

        // Init the mazeData with empty cubes
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                this.mazeData[i][j] = new Cell(i, j);
            }
        }

        return this;
    }

    @Override
    public IMazeBuilder buildCell(Coordinate position) {
        this.mazeData[position.getRow()][position.getColumn()] = new Cell(position);

        return this;
    }

    @Override
    public IMazeBuilder buildDoor(Coordinate cell1Pos, Coordinate cell2Pos) throws Exception {
        buildDoor(cell1Pos, cell2Pos, Instance.getDirectionOfMove(cell1Pos, cell2Pos));
        return this;
    }

    @Override
    public IMazeBuilder buildDoor(Coordinate cell1Pos, Direction doorDirection) throws Exception {
        buildDoor(cell1Pos, Instance.moveCoordinatesToDirection(cell1Pos, doorDirection), doorDirection);

        return this;
    }

    private void buildDoor(Coordinate cell1Pos, Coordinate cell2Pos, Direction direction) throws Exception {
        Cell cell = this.getCellAtPosition(cell1Pos);
        cell.setCellAtDirection(direction, this.getCellAtPosition(cell2Pos));
    }

    @Override
    public IMazeBuilder buildEntrance(Coordinate pos, Direction direction) {
        buildELocation(pos, direction, ELocationType.Entrance);
        return this;
    }

    @Override
    public IMazeBuilder buildExit(Coordinate pos, Direction direction) {
        buildELocation(pos, direction, ELocationType.Exit);
        return this;
    }

    private void buildELocation(Coordinate pos, Direction direction, ELocationType exit) {
        assert pos != null && direction != null;
        Cell cell = this.getCellAtPosition(pos);
        ELocation entrance = new ELocation(pos, direction, exit);
        cell.setELocationAsNeighbor(entrance);
    }

    @Override
    public Maze getMaze() {
        // TODO - add ELocations and more
        return new Maze(this.mazeData);
    }

    private Cell getCellAtPosition(Coordinate pos) {
        return this.mazeData[pos.getRow()][pos.getColumn()];
    }
}
