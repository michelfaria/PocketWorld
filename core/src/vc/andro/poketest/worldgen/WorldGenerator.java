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
        Tile[] tiles = generateWorldTiles();
        var entities = new Array<Entity>(Entity.class);
        spawnTrees(tiles, entities);
        return new World(tiles, worldBase.getWidth(), worldBase.getHeight(), entities);
    }

    @NotNull
    private Tile[] generateWorldTiles() {
        var tiles = new Tile[worldBase.getWidth() * worldBase.getHeight()];

        for (int y = 0; y < worldBase.getHeight(); y++) {
            for (int x = 0; x < worldBase.getWidth(); x++) {
                int idx = y * worldBase.getWidth() + x;
                float altitude = worldBase.getAltitudeMap()[idx];

                if (altitude <= worldBase.getWaterLevel()) {
                    // Is water tile
                    tiles[idx] = new Tile(TileType.WATER, altitude, x, y);
                    continue;
                }

                if (generateWalls(tiles, x, y, idx, altitude)) {
                    // Is wall tile
                    continue;
                }

                if (altitude <= worldBase.getBeachAltitude()) {
                    // Is sand tile
                    tiles[idx] = new Tile(TileType.SAND, altitude, x, y);
                    continue;
                }

                // Spawn grass
                tiles[idx] = new Tile(TileType.GRASS, altitude, x, y);
            }
        }
        return tiles;
    }

    private void spawnTrees(Tile[] tiles, Array<Entity> entities) {
        for (int y = 0; y < worldBase.getWidth(); y++) {
            for (int x = 0; x < worldBase.getHeight(); x++) {
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
        int treeType = worldBase.getTreeMap()[y * worldBase.getWidth() + x];
        return treeType > 0;
    }

    private boolean isTreeAllowedInPosition(Tile[] tiles, int y, int x) {
        Tile currentTile = getAtPosition(tiles, x, y, worldBase.getWidth());
        return currentTile.getType().equals(TileType.GRASS);
    }

    private boolean generateWalls(Tile[] tiles, int x, int y, int idx, float altitude) {
        if (x > 0) {
            float leftAltitude = getAtPosition(worldBase.getAltitudeMap(), x - 1, y, worldBase.getWidth());
            if (leftAltitude < altitude) {
                tiles[idx] = new Tile(TileType.WALL, altitude, x, y);
                return true;
            }
        }
        if (x < worldBase.getWidth() - 1) {
            float rightAltitude = getAtPosition(worldBase.getAltitudeMap(), x + 1, y, worldBase.getWidth());
            if (rightAltitude < altitude) {
                tiles[idx] = new Tile(TileType.WALL, altitude, x, y);
                return true;
            }
        }
        if (y > 0) {
            float belowAltitude = getAtPosition(worldBase.getAltitudeMap(), x, y - 1, worldBase.getWidth());
            if (belowAltitude < altitude) {
                tiles[idx] = new Tile(TileType.WALL, altitude, x, y);
                return true;
            }
        }
        if (y < worldBase.getHeight() - 1) {
            float aboveAltitude = getAtPosition(worldBase.getAltitudeMap(), x, y + 1, worldBase.getWidth());
            if (aboveAltitude < altitude) {
                tiles[idx] = new Tile(TileType.WALL, altitude, x, y);
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

    private float getAtPosition(float[] arr, int x, int y, int width) {
        return arr[y * width + x];
    }

    private <T> T getAtPosition(T[] arr, int x, int y, int width) {
        return arr[y * width + x];
    }
}
