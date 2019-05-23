package Maze.MazeBuilder;

import java.util.Collection;

public abstract class BaseMazeBuilder implements IMazeBuilder {

    @Override
    public IMazeBuilder buildEntrance(ELocationBaseData entrance) {
        return this.buildEntrance(entrance.getPos(), entrance.getDirection());
    }

    @Override
    public IMazeBuilder buildManyEntrances(ELocationBaseData[] entrances) {
        for (ELocationBaseData entrance : entrances) {
            this.buildEntrance(entrance);
        }

        return this;
    }

    @Override
    public IMazeBuilder buildManyEntrances(Collection<ELocationBaseData> entrances) {
        entrances.forEach(this::buildEntrance);
        return this;
    }

    @Override
    public IMazeBuilder buildExit(ELocationBaseData exit) {
        return this.buildExit(exit.getPos(), exit.getDirection());
    }

    @Override
    public IMazeBuilder buildManyExits(ELocationBaseData[] exits) {
        for (ELocationBaseData exit : exits) {
            this.buildExit(exit);
        }

        return this;
    }

    @Override
    public IMazeBuilder buildManyExits(Collection<ELocationBaseData> exits) {
        exits.forEach(this::buildExit);
        return this;
    }

    public abstract IMazeBuilder clone();
}
