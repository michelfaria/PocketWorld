package vc.andro.poketest.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import org.jetbrains.annotations.Nullable;
import vc.andro.poketest.entity.TreeEntity;
import vc.andro.poketest.tile.BasicTile;
import vc.andro.poketest.tile.TileType;
import vc.andro.poketest.tile.WallTile;
import vc.andro.poketest.util.BlueNoise;
import vc.andro.poketest.util.FastNoise;

import java.util.Random;

import static vc.andro.poketest.tile.WallType.*;
import static vc.andro.poketest.world.Chunk.CHUNK_SIZE;
import static vc.andro.poketest.world.World.*;

public class WorldGenerator {
    public final WorldCreationParams params;
    public final Random random;
    public final FastNoise perlinNoiseGenerator;
    public final BlueNoise blueNoiseGenerator;
    public final AltitudeMapGenerator altitudeMapGenerator;
    public final TreeMapGenerator treeMapGenerator;

    private @Nullable
    World world = null;

    public WorldGenerator(WorldCreationParams params) {
        this.params = params;
        random = new Random(params.seed);
        perlinNoiseGenerator = new FastNoise(params.seed);
        perlinNoiseGenerator.SetNoiseType(FastNoise.NoiseType.Perlin);
        blueNoiseGenerator = new BlueNoise();
        altitudeMapGenerator = new AltitudeMapGenerator(perlinNoiseGenerator, params);
        treeMapGenerator = new TreeMapGenerator(blueNoiseGenerator, params);
    }

    public World createWorld() {
        log("Generating world...");
        world = new World(params, this);
        for (int chunkX = 0; chunkX < 10; chunkX++) {
            for (int chunkZ = 0; chunkZ < 10; chunkZ++) {
                generateChunk(chunkX, chunkZ);
            }
        }
        log("World gen is done!");
        return world;
    }

    private void log(String message) {
        Gdx.app.log("WorldGen", message);
    }

    public void generateChunk(int chunkX, int chunkZ) {
        log("Generating chunk: " + chunkX + ", " + chunkZ);
        assert world != null;
        for (int chunkLocalX = 0; chunkLocalX < CHUNK_SIZE; chunkLocalX++) {
            for (int chunkLocalZ = 0; chunkLocalZ < CHUNK_SIZE; chunkLocalZ++) {
                int worldX = LxWx(chunkX, chunkLocalX);
                int worldZ = LzWz(chunkZ, chunkLocalZ);
                int y = altitudeMapGenerator.altitudeAtPos(worldX, worldZ);

                if (y <= world.getCreationParams().waterLevel) {
                    // Is water tile
                    world.putTileAt_WP(worldX, y, worldZ, new BasicTile(TileType.WATER));
                    continue;
                }

                if (generateWalls(worldX, y, worldZ)) {
                    // Is wall tile
                    continue;
                }

                if (y <= params.beachAltitude) {
                    // Is sand tile
                    world.putTileAt_WP(worldX, y, worldZ, new BasicTile(TileType.SAND));
                    continue;
                }

                // Spawn grass
                world.putTileAt_WP(worldX, y, worldZ, new BasicTile(TileType.GRASS));
            }
        }
        spawnTrees(chunkX, chunkZ);
        world.updateChunk(chunkX, chunkZ);
    }

    private void spawnTrees(int chunkX, int chunkZ) {
        assert world != null;
        Chunk chunk = world.getChunkAt_CP(chunkX, chunkZ);
        assert chunk != null;
        for (int chunkLocalX = 0; chunkLocalX < CHUNK_SIZE; chunkLocalX++) {
            for (int chunkLocalZ = 0; chunkLocalZ < CHUNK_SIZE; chunkLocalZ++) {
                int worldX = LxWx(chunkX, chunkLocalX);
                BasicTile surfaceTile = chunk.getSurfaceTile(chunkLocalX, chunkLocalZ);
                if (surfaceTile == null) {
                    continue;
                }
                int worldZ = LzWz(chunkZ, chunkLocalZ);

                if (shouldTreeBeSpawnedAtPosition(worldX, surfaceTile.y, worldZ)) {
                    var tree = new TreeEntity();
                    tree.worldX = worldX;
                    tree.worldZ = worldZ;
                    world.addEntity(tree);
                }
            }
        }
    }

    private boolean shouldTreeBeSpawnedAtPosition(int worldX, int y, int worldZ) {
        assert world != null;
        BasicTile tile = world.getTileAt_WP(worldX, y, worldZ);
        if (tile == null || !tile.type.equals(TileType.GRASS)) {
            return false;
        }
        int treeType = treeMapGenerator.getTreeAtPos(worldX, worldZ);
        return treeType > 0;
    }

    @SuppressWarnings("DuplicatedCode")
    private boolean generateWalls(int worldX, int y, int worldZ) {
        assert world != null;

        float topLeftY = altitudeMapGenerator.altitudeAtPos(worldX - 1, worldZ + 1);
        float topY = altitudeMapGenerator.altitudeAtPos(worldX, worldZ + 1);
        float topRightY = altitudeMapGenerator.altitudeAtPos(worldX + 1, worldZ + 1);
        float leftY = altitudeMapGenerator.altitudeAtPos(worldX - 1, worldZ);
        float rightY = altitudeMapGenerator.altitudeAtPos(worldX + 1, worldZ);
        float bottomLeftY = altitudeMapGenerator.altitudeAtPos(worldX - 1, worldZ - 1);
        float bottomY = altitudeMapGenerator.altitudeAtPos(worldX, worldZ - 1);
        float bottomRightY = altitudeMapGenerator.altitudeAtPos(worldX + 1, worldZ - 1);

        if (y > topLeftY && y > leftY && y > topY) {
            world.putTileAt_WP(worldX, y, worldZ, new WallTile(TOP_LEFT_CORNER));
            return true;
        }


        if (y > topRightY && y > topY && y > rightY) {
            world.putTileAt_WP(worldX, y, worldZ, new WallTile(TOP_RIGHT_CORNER));
            return true;
        }

        if (y > bottomLeftY && y > leftY && y > bottomY) {
            world.putTileAt_WP(worldX, y, worldZ, new WallTile(BOTTOM_LEFT_CORNER));
            return true;
        }

        if (y > bottomRightY && y > rightY && y > bottomY) {
            world.putTileAt_WP(worldX, y, worldZ, new WallTile(BOTTOM_RIGHT_CORNER));
            return true;
        }

        if (y > bottomLeftY && MathUtils.isEqual(y, leftY) && MathUtils.isEqual(y, bottomY)) {
            world.putTileAt_WP(worldX, y, worldZ, new WallTile(BOTTOM_LEFT_INNER_CORNER));
            return true;
        }

        if (y > bottomRightY && MathUtils.isEqual(y, rightY) && MathUtils.isEqual(y, bottomY)) {
            world.putTileAt_WP(worldX, y, worldZ, new WallTile(BOTTOM_RIGHT_INNER_CORNER));
            return true;
        }

        if (y > topLeftY && MathUtils.isEqual(y, leftY) && MathUtils.isEqual(y, topY)) {
            world.putTileAt_WP(worldX, y, worldZ, new WallTile(TOP_LEFT_INNER_CORNER));
            return true;
        }

        if (y > topRightY && MathUtils.isEqual(y, rightY) && MathUtils.isEqual(y, topY)) {
            world.putTileAt_WP(worldX, y, worldZ, new WallTile(TOP_RIGHT_INNER_CORNER));
            return true;
        }

        if (leftY < y) {
            world.putTileAt_WP(worldX, y, worldZ, new WallTile(LEFT_EDGE));
            return true;
        }

        if (rightY < y) {
            world.putTileAt_WP(worldX, y, worldZ, new WallTile(RIGHT_EDGE));
            return true;
        }

        if (bottomY < y) {
            world.putTileAt_WP(worldX, y, worldZ, new WallTile(BOTTOM_EDGE));
            return true;
        }

        if (topY < y) {
            world.putTileAt_WP(worldX, y, worldZ, new WallTile(TOP_EDGE));
            return true;
        }

        return false;
    }
}
