package vc.andro.poketest.world;

import com.badlogic.gdx.math.MathUtils;
import vc.andro.poketest.entity.TreeEntity;
import vc.andro.poketest.tile.BasicTile;
import vc.andro.poketest.tile.TileType;
import vc.andro.poketest.tile.WallTile;

import java.util.Random;

import static vc.andro.poketest.tile.WallType.*;

public class WorldGenerator {
    private final WorldBase worldBase;
    private final Random random;

    public WorldGenerator(WorldBase worldBase) {
        this.worldBase = worldBase;
        random = new Random(worldBase.getSeed());
    }

    public World createWorld() {
        System.out.println("Generating world...");
        var world = new World(worldBase);
        generateWorldTiles(world);
        spawnTrees(world);
        world.updateAllTiles();
        return world;
    }

    private void generateWorldTiles(World world) {
        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                float altitude = world.getBase().getAltitudeMap()[x][y];

                if (altitude <= world.getBase().getWaterLevel()) {
                    // Is water tile
                    world.setTile(x, y, new BasicTile(world, TileType.WATER, x, y, altitude));
                    continue;
                }

                if (generateWalls(world, x, y, altitude)) {
                    // Is wall tile
                    continue;
                }

                if (altitude <= worldBase.getBeachAltitude()) {
                    // Is sand tile
                    world.setTile(x, y, new BasicTile(world, TileType.SAND, x, y, altitude));
                    continue;
                }

                // Spawn grass
                world.setTile(x, y, new BasicTile(world, TileType.GRASS, x, y, altitude));
            }
        }
    }

    private void spawnTrees(World world) {
        for (int x = 0; x < worldBase.getHeight(); x++) {
            for (int y = 0; y < worldBase.getWidth(); y++) {
                if (!isTreeAllowedInPosition(world.getTiles(), y, x)) {
                    continue;
                }
                if (shouldTreeBeSpawnedAtPosition(x, y)) {
                    var tree = new TreeEntity();
                    tree.x = x;
                    tree.y = y;
                    world.addEntity(tree);
                }
            }
        }
    }

    private boolean shouldTreeBeSpawnedAtPosition(int x, int y) {
        int treeType = worldBase.getTreeMap()[x][y];
        return treeType > 0;
    }

    private boolean isTreeAllowedInPosition(BasicTile[][] tiles, int y, int x) {
        worldBase.getWidth();
        BasicTile currentTile = tiles[x][y];
        return currentTile.getType().equals(TileType.GRASS);
    }

    @SuppressWarnings("DuplicatedCode")
    private boolean generateWalls(World world, int x, int y, float altitude) {
        final float[][] altitudeMap = worldBase.getAltitudeMap();
        float topLeftAltitude = Float.NaN;
        float topAltitude = Float.NaN;
        float topRightAltitude = Float.NaN;
        float leftAltitude = Float.NaN;
        float rightAltitude = Float.NaN;
        float bottomLeftAltitude = Float.NaN;
        float bottomAltitude = Float.NaN;
        float bottomRightAltitude = Float.NaN;

        final int worldHeight = worldBase.getHeight();
        final int worldWidth = worldBase.getWidth();

        if (x > 0 && y < worldHeight - 1) topLeftAltitude = altitudeMap[x - 1][y + 1];
        if (y < worldHeight - 1) topAltitude = altitudeMap[x][y + 1];
        if (x < worldWidth - 1 && y < worldHeight - 1) topRightAltitude = altitudeMap[x + 1][y + 1];
        if (x > 0) leftAltitude = altitudeMap[x - 1][y];
        if (x < worldWidth - 1) rightAltitude = altitudeMap[x + 1][y];
        if (x > 0 && y > 0) bottomLeftAltitude = altitudeMap[x - 1][y - 1];
        if (y > 0) bottomAltitude = altitudeMap[x][y - 1];
        if (x < worldWidth - 1 && y > 0) bottomRightAltitude = altitudeMap[x + 1][y - 1];

        final boolean topLeftAltitudeSet = !Float.isNaN(topLeftAltitude);
        final boolean topAltitudeSet = !Float.isNaN(topAltitude);
        final boolean topRightAltitudeSet = !Float.isNaN(topRightAltitude);
        final boolean leftAltitudeSet = !Float.isNaN(leftAltitude);
        final boolean rightAltitudeSet = !Float.isNaN(rightAltitude);
        final boolean bottomLeftAltitudeSet = !Float.isNaN(bottomLeftAltitude);
        final boolean bottomAltitudeSet = !Float.isNaN(bottomAltitude);
        final boolean bottomRightAltitudeSet = !Float.isNaN(bottomRightAltitude);

        if (topLeftAltitudeSet && altitude > topLeftAltitude
                && leftAltitudeSet && altitude > leftAltitude
                && topAltitudeSet && altitude > topAltitude) {
            world.setTile(x, y, new WallTile(world, x, y, altitude, TOP_LEFT_CORNER));
            return true;
        }


        if (topRightAltitudeSet && altitude > topRightAltitude
                && topAltitudeSet && altitude > topAltitude
                && rightAltitudeSet && altitude > rightAltitude) {
            world.setTile(x, y, new WallTile(world, x, y, altitude, TOP_RIGHT_CORNER));
            return true;
        }

        if (bottomLeftAltitudeSet && altitude > bottomLeftAltitude
                && leftAltitudeSet && altitude > leftAltitude
                && bottomAltitudeSet && altitude > bottomAltitude) {
            world.setTile(x, y, new WallTile(world, x, y, altitude, BOTTOM_LEFT_CORNER));
            return true;
        }

        if (bottomRightAltitudeSet && altitude > bottomRightAltitude
                && rightAltitudeSet && altitude > rightAltitude
                && bottomAltitudeSet && altitude > bottomAltitude) {
            world.setTile(x, y, new WallTile(world, x, y, altitude, BOTTOM_RIGHT_CORNER));
            return true;
        }

        if (bottomLeftAltitudeSet && altitude > bottomLeftAltitude
                && leftAltitudeSet && MathUtils.isEqual(altitude, leftAltitude)
                && bottomAltitudeSet && MathUtils.isEqual(altitude, bottomAltitude)) {
            world.setTile(x, y, new WallTile(world, x, y, altitude, BOTTOM_LEFT_INNER_CORNER));
            return true;
        }

        if (bottomRightAltitudeSet && altitude > bottomRightAltitude
                && rightAltitudeSet && MathUtils.isEqual(altitude, rightAltitude)
                && bottomAltitudeSet && MathUtils.isEqual(altitude, bottomAltitude)) {
            world.setTile(x, y, new WallTile(world, x, y, altitude, BOTTOM_RIGHT_INNER_CORNER));
            return true;
        }

        if (topLeftAltitudeSet && altitude > topLeftAltitude
                && leftAltitudeSet && MathUtils.isEqual(altitude, leftAltitude)
                && topAltitudeSet && MathUtils.isEqual(altitude, topAltitude)) {
            world.setTile(x, y, new WallTile(world, x, y, altitude, TOP_LEFT_INNER_CORNER));
            return true;
        }

        if (topRightAltitudeSet && altitude > topRightAltitude
                && rightAltitudeSet && MathUtils.isEqual(altitude, rightAltitude)
                && topAltitudeSet && MathUtils.isEqual(altitude, topAltitude)) {
            world.setTile(x, y, new WallTile(world, x, y, altitude, TOP_RIGHT_INNER_CORNER));
            return true;
        }


        if (leftAltitudeSet && leftAltitude < altitude) {
            world.setTile(x, y, new WallTile(world, x, y, altitude, LEFT_EDGE));
            return true;
        }

        if (rightAltitudeSet && rightAltitude < altitude) {
            world.setTile(x, y, new WallTile(world, x, y, altitude, RIGHT_EDGE));
            return true;
        }

        if (bottomAltitudeSet && bottomAltitude < altitude) {
            world.setTile(x, y, new WallTile(world, x, y, altitude, BOTTOM_EDGE));
            return true;
        }

        if (topAltitudeSet && topAltitude < altitude) {
            world.setTile(x, y, new WallTile(world, x, y, altitude, TOP_EDGE));
            return true;
        }

        return false;
    }
}
