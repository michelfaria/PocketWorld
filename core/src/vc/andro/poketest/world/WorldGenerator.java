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
import static vc.andro.poketest.world.World.LxWx;
import static vc.andro.poketest.world.World.LzWz;

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
                generateTileAtPosition(chunkX, chunkZ, chunkLocalX, chunkLocalZ);
            }
        }
        spawnTrees(chunkX, chunkZ);
        world.updateChunk(chunkX, chunkZ);
    }

    private void generateTileAtPosition(int chunkX, int chunkZ, int chunkLocalX, int chunkLocalZ) {
        generateTileAtPosition(chunkX, chunkZ, chunkLocalX, chunkLocalZ, -1);
    }

    private void generateTileAtPosition(int chunkX, int chunkZ, int chunkLocalX, int chunkLocalZ, int y) {
        assert world != null;
        int worldX = LxWx(chunkX, chunkLocalX);
        int worldZ = LzWz(chunkZ, chunkLocalZ);
        y = y < 0 ? altitudeMapGenerator.altitudeAtPos(worldX, worldZ) : y;

        if (y <= params.waterLevel) {
            // Is water tile
            world.putTileAt_WP(worldX, params.waterLevel, worldZ, new BasicTile(TileType.WATER));
            return;
        }

        if (generateWalls(worldX, y, worldZ)) {
            // Wall tile generated
            generateTileAtPosition(chunkX, chunkZ, chunkLocalX, chunkLocalZ, y - 1);
            return;
        }

        if (y <= params.beachAltitude) {
            // Is sand tile
            world.putTileAt_WP(worldX, y, worldZ, new BasicTile(TileType.SAND));
            return;
        }

        // Spawn grass
        world.putTileAt_WP(worldX, y, worldZ, new BasicTile(TileType.GRASS));
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

        float southwestY = altitudeMapGenerator.altitudeAtPos(worldX - 1, worldZ + 1);
        float southY = altitudeMapGenerator.altitudeAtPos(worldX, worldZ + 1);
        float southeastY = altitudeMapGenerator.altitudeAtPos(worldX + 1, worldZ + 1);
        float westY = altitudeMapGenerator.altitudeAtPos(worldX - 1, worldZ);
        float eastY = altitudeMapGenerator.altitudeAtPos(worldX + 1, worldZ);
        float northwestY = altitudeMapGenerator.altitudeAtPos(worldX - 1, worldZ - 1);
        float northY = altitudeMapGenerator.altitudeAtPos(worldX, worldZ - 1);
        float northeastY = altitudeMapGenerator.altitudeAtPos(worldX + 1, worldZ - 1);

        if (y > northwestY && y > westY && y > northY) {
            world.putTileAt_WP(worldX, y, worldZ, new WallTile(NORTHWEST_CORNER));
            return true;
        }

        if (y > northeastY && y > northY && y > eastY) {
            world.putTileAt_WP(worldX, y, worldZ, new WallTile(NORTHEAST_CORNER));
            return true;
        }

        if (y > southwestY && y > westY && y > southY) {
            world.putTileAt_WP(worldX, y, worldZ, new WallTile(SOUTHWEST_CORNER));
            return true;
        }

        if (y > southeastY && y > eastY && y > southY) {
            world.putTileAt_WP(worldX, y, worldZ, new WallTile(SOUTHEAST_CORNER));
            return true;
        }

        if (y > southwestY && MathUtils.isEqual(y, westY) && MathUtils.isEqual(y, southY)) {
            world.putTileAt_WP(worldX, y, worldZ, new WallTile(SOUTHWEST_INNER_CORNER));
            return true;
        }

        if (y > southeastY && MathUtils.isEqual(y, eastY) && MathUtils.isEqual(y, southY)) {
            world.putTileAt_WP(worldX, y, worldZ, new WallTile(SOUTHEAST_INNER_CORNER));
            return true;
        }

        if (y > northwestY && MathUtils.isEqual(y, westY) && MathUtils.isEqual(y, northY)) {
            world.putTileAt_WP(worldX, y, worldZ, new WallTile(NORTHWEST_INNER_CORNER));
            return true;
        }

        if (y > northeastY && MathUtils.isEqual(y, eastY) && MathUtils.isEqual(y, northY)) {
            world.putTileAt_WP(worldX, y, worldZ, new WallTile(NORTHEAST_INNER_CORNER));
            return true;
        }

        if (westY < y) {
            world.putTileAt_WP(worldX, y, worldZ, new WallTile(WEST_EDGE));
            return true;
        }

        if (eastY < y) {
            world.putTileAt_WP(worldX, y, worldZ, new WallTile(EAST_EDGE));
            return true;
        }

        if (southY < y) {
            world.putTileAt_WP(worldX, y, worldZ, new WallTile(SOUTH_EDGE));
            return true;
        }

        if (northY < y) {
            world.putTileAt_WP(worldX, y, worldZ, new WallTile(NORTH_EDGE));
            return true;
        }

        return false;
    }
}
