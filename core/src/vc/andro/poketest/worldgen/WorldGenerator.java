package vc.andro.poketest.worldgen;

import com.badlogic.gdx.utils.Array;
import org.jetbrains.annotations.NotNull;
import vc.andro.poketest.world.*;

import java.util.Random;

public class WorldGenerator {
    private final WorldBase worldBase;
    private final Random random;

    public WorldGenerator(WorldBase worldBase) {
        this.worldBase = worldBase;
        random = new Random(worldBase.getSeed());
    }

    public World createWorld() {
        System.out.println("Generating world...");
        Tile[][] tiles = generateWorldTiles();

        var trees = new Array<Entity>(Entity.class);
        spawnTrees(tiles, trees);

        var world = new World(worldBase, tiles);
        world.addEntities(trees);
        return world;
    }

    @NotNull
    private Tile[][] generateWorldTiles() {
        Tile[][] tiles = new Tile[worldBase.getWidth()][];
        for (int i = 0; i < worldBase.getWidth(); i++) {
            tiles[i] = new Tile[worldBase.getHeight()];
        }

            for (int x = 0; x < worldBase.getWidth(); x++) {
        for (int y = 0; y < worldBase.getHeight(); y++) {
                float altitude = worldBase.getAltitudeMap()[x][y];

                if (altitude <= worldBase.getWaterLevel()) {
                    // Is water tile
                    tiles[x][y] = new Tile(TileType.WATER, altitude, x, y);
                    continue;
                }

                if (generateWalls(tiles, x, y, altitude)) {
                    // Is wall tile
                    continue;
                }

                if (altitude <= worldBase.getBeachAltitude()) {
                    // Is sand tile
                    tiles[x][y] = new Tile(TileType.SAND, altitude, x, y);
                    continue;
                }

                // Spawn grass
                tiles[x][y] = new Tile(TileType.GRASS, altitude, x, y);
            }
        }
        return tiles;
    }

    private void spawnTrees(Tile[][] tiles, Array<Entity> entities) {
            for (int x = 0; x < worldBase.getHeight(); x++) {
        for (int y = 0; y < worldBase.getWidth(); y++) {
                if (!isTreeAllowedInPosition(tiles, y, x)) {
                    continue;
                }
                if (shouldTreeBeSpawnedAtPosition(x, y)) {
                    var tree = new TreeEntity();
                    tree.x = x;
                    tree.y = y;
                    entities.add(tree);
                }
            }
        }
    }

    private boolean shouldTreeBeSpawnedAtPosition(int x, int y) {
        int treeType = worldBase.getTreeMap()[x][y];
        return treeType > 0;
    }

    private boolean isTreeAllowedInPosition(Tile[][] tiles, int y, int x) {
        worldBase.getWidth();
        Tile currentTile = tiles[x][y];
        return currentTile.getType().equals(TileType.GRASS);
    }

    private boolean generateWalls(Tile[][] tiles, int x, int y, float altitude) {
        if (x > 0) {
            worldBase.getWidth();
            float leftAltitude = worldBase.getAltitudeMap()[x - 1][y];
            if (leftAltitude < altitude) {
                tiles[x][y] = new Tile(TileType.WALL, altitude, x, y);
                return true;
            }
        }
        if (x < worldBase.getWidth() - 1) {
            worldBase.getWidth();
            float rightAltitude = worldBase.getAltitudeMap()[x + 1][y];
            if (rightAltitude < altitude) {
                tiles[x][y] = new Tile(TileType.WALL, altitude, x, y);
                return true;
            }
        }
        if (y > 0) {
            worldBase.getWidth();
            float belowAltitude = worldBase.getAltitudeMap()[x][y - 1];
            if (belowAltitude < altitude) {
                tiles[x][y] = new Tile(TileType.WALL, altitude, x, y);
                return true;
            }
        }
        if (y < worldBase.getHeight() - 1) {
            worldBase.getWidth();
            float aboveAltitude = worldBase.getAltitudeMap()[x][y + 1];
            if (aboveAltitude < altitude) {
                tiles[x][y] = new Tile(TileType.WALL, altitude, x, y);
                return true;
            }
        }
        return false;
    }

    private boolean tryPlaceSlope(Tile[] tiles, int x, int y, float altitude) {
        if (random.nextFloat() <= worldBase.getSlopeChance()) {
            tiles[y * worldBase.getWidth() + x] = new Tile(TileType.SLOPE, altitude, x, y);
            return true;
        }
        return false;
    }

}
