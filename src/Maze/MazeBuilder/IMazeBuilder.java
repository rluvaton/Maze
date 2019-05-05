package Maze.MazeBuilder;

import Helpers.Coordinate;
import Helpers.Direction;
import Maze.Maze;

import java.util.Collection;

public interface IMazeBuilder {

    IMazeBuilder buildMazeSkeleton(int height, int width);

    IMazeBuilder buildAllCellsAsEmpty();

    IMazeBuilder buildCell(Coordinate position);

    IMazeBuilder buildDoor(Coordinate cell1Pos, Coordinate cell2Pos) throws Exception;

    IMazeBuilder buildDoor(Coordinate cell1Pos, Direction doorDirection) throws Exception;

    IMazeBuilder buildEntrance(Coordinate pos, Direction direction);

    IMazeBuilder buildEntrance(ELocationBaseData entrance);

    IMazeBuilder buildManyEntrances(ELocationBaseData[] entrances);

    IMazeBuilder buildManyEntrances(Collection<ELocationBaseData> entrances);

    IMazeBuilder buildExit(Coordinate pos, Direction direction);

    IMazeBuilder buildExit(ELocationBaseData exit);

    IMazeBuilder buildManyExits(ELocationBaseData[] exits);

    IMazeBuilder buildManyExits(Collection<ELocationBaseData> exits);

    Maze getMaze();

    class ELocationBaseData {
        private Coordinate pos;
        private Direction direction;

        public ELocationBaseData(Coordinate pos, Direction direction) {
            this.pos = pos;
            this.direction = direction;
        }

        public Coordinate getPos() {
            return pos;
        }

        public Direction getDirection() {
            return direction;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof ELocationBaseData
                    && this.direction == ((ELocationBaseData) obj).direction
                    && this.pos.equals(((ELocationBaseData) obj).pos);
        }
    }
}
