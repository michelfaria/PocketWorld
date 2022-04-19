package vc.andro.poketest.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import org.jetbrains.annotations.Nullable;
import vc.andro.poketest.entity.FlowerEntity;
import vc.andro.poketest.entity.TreeEntity;
import vc.andro.poketest.tile.BasicVoxel;
import vc.andro.poketest.tile.SlopeVoxel;
import vc.andro.poketest.tile.VoxelType;
import vc.andro.poketest.util.BlueNoise;
import vc.andro.poketest.util.FastNoise;
import vc.andro.poketest.world.map.AltitudeMapGenerator;
import vc.andro.poketest.world.map.FlowerMapGenerator;
import vc.andro.poketest.world.map.TreeMapGenerator;
import vc.andro.poketest.world.map.VegetationMapGenerator;

import java.util.Random;

import static vc.andro.poketest.tile.SlopeType.*;
import static vc.andro.poketest.world.Chunk.CHUNK_SIZE;
import static vc.andro.poketest.world.World.LxWx;
import static vc.andro.poketest.world.World.LzWz;

public class WorldGenerator {
    public final WorldCreationParams params;
    public final Random random;
    public final FastNoise perlinNoiseGenerator;
    public final BlueNoise blueNoiseGenerator;
    public final AltitudeMapGenerator altitudeMapGenerator;

    public final VegetationMapGenerator treeMapGenerator;
    public final VegetationMapGenerator flowerMapGenerator;

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
        flowerMapGenerator = new FlowerMapGenerator(blueNoiseGenerator, params);
    }

    public World createWorld() {
        log("Generating world...");
        world = new World(params, this);
        for (int cx = 0; cx < 10; cx++) {
            for (int cz = 0; cz < 10; cz++) {
                generateChunk(cx, cz);
            }
        }
        log("World gen is done!");
        return world;
    }

    private void log(String message) {
        Gdx.app.log("WorldGen", message);
    }

    public void generateChunk(int cx, int cz) {
        log("Generating chunk: " + cx + ", " + cz);
        assert world != null;
        for (int chunkLocalX = 0; chunkLocalX < CHUNK_SIZE; chunkLocalX++) {
            for (int chunkLocalZ = 0; chunkLocalZ < CHUNK_SIZE; chunkLocalZ++) {
                generateTileAtPosition(cx, cz, chunkLocalX, chunkLocalZ);
            }
        }

        Chunk chunk = world.getChunkAt_CP(cx, cz);
        assert chunk != null;

        spawnTrees(chunk);
        spawnFlowers(chunk);
        world.updateChunk(cx, cz);
    }

    private void generateTileAtPosition(int cx, int cz, int lx, int lz) {
        generateTileAtPosition(cx, cz, lx, lz, -1);
    }

    private void generateTileAtPosition(int cx, int cz, int lx, int lz, int y) {
        assert world != null;
        int wx = LxWx(cx, lx);
        int wz = LzWz(cz, lz);
        y = y < 0 ? altitudeMapGenerator.altitudeAtPos(wx, wz) : y;

        if (y <= params.waterLevel) {
            // Is water tile
            world.putTileAt_WP(wx, params.waterLevel, wz, new BasicVoxel(VoxelType.WATER));
            return;
        }

        if (generateWalls(wx, y, wz)) {
            // Wall tile generated
            generateTileAtPosition(cx, cz, lx, lz, y - 1);
            return;
        }

        if (y <= params.beachAltitude) {
            // Is sand tile
            world.putTileAt_WP(wx, y, wz, new BasicVoxel(VoxelType.SAND));
            return;
        }

        // Spawn grass
        world.putTileAt_WP(wx, y, wz, new BasicVoxel(VoxelType.GRASS));
    }

    private void spawnTrees(Chunk chunk) {
        assert world != null;
        for (int chunkLocalX = 0; chunkLocalX < CHUNK_SIZE; chunkLocalX++) {
            for (int chunkLocalZ = 0; chunkLocalZ < CHUNK_SIZE; chunkLocalZ++) {
                BasicVoxel surfaceTile = chunk.getSurfaceTile(chunkLocalX, chunkLocalZ);
                if (surfaceTile == null) {
                    continue;
                }
                int wx = LxWx(chunk.cx, chunkLocalX);
                int wz = LzWz(chunk.cz, chunkLocalZ);
                if (shouldTreeBeSpawnedAtPosition(wx, surfaceTile.y + 1, wz)) {
                    var tree = new TreeEntity();
                    tree.setPosition(wx, surfaceTile.y + 1, wz);
                    world.addEntity(tree);
                }
            }
        }
    }

    private void spawnFlowers(Chunk chunk) {
        assert world != null;

        for (int lx = 0; lx < CHUNK_SIZE; lx++) {
            for (int lz = 0; lz < CHUNK_SIZE; lz++) {
                BasicVoxel surfaceTile = chunk.getSurfaceTile(lx, lz);
                if (surfaceTile == null) {
                    continue;
                }
                int wx = LxWx(chunk.cx, lx);
                int wz = LzWz(chunk.cz, lz);
                if (shouldFlowerBeSpawnedAtPosition(wx, surfaceTile.y + 1, wz)) {
                    var flower = new FlowerEntity();
                    flower.setPosition(wx, surfaceTile.y + 1, wz);
                    world.addEntity(flower);
                }
            }
        }
    }

    private boolean shouldTreeBeSpawnedAtPosition(int wx, int y, int wz) {
        assert world != null;
        for (int ix = 0; ix < TreeEntity.COLLISION_WIDTH; ix++) {
            for (int iz = 0; iz < TreeEntity.COLLISION_HEIGHT; iz++) {
                BasicVoxel tileY0 = world.getTileAt_WP(wx + ix, y - 1, wz + iz);
                BasicVoxel tileY1 = world.getTileAt_WP(wx + ix, y, wz + iz);
                if (tileY0 == null || !tileY0.type.equals(VoxelType.GRASS)) {
                    return false;
                }
                if (tileY1 != null) {
                    return false;
                }
            }
        }
        return treeMapGenerator.getAtPosition(wx, wz) > 0;
    }

    private boolean shouldFlowerBeSpawnedAtPosition(int wx, int y, int wz) {
        assert world != null;
        BasicVoxel ty0 = world.getTileAt_WP(wx, y, wz);
        if (ty0 != null) {
            return false;
        }
        BasicVoxel ty1 = world.getTileAt_WP(wx, y - 1, wz);
        if (ty1 == null || !ty1.type.equals(VoxelType.GRASS)) {
            return false;
        }
        return flowerMapGenerator.getAtPosition(wx, wz) > 0;
    }

    @SuppressWarnings("DuplicatedCode")
    private boolean generateWalls(int wx, int y, int wz) {
        assert world != null;

        float southwestY = altitudeMapGenerator.altitudeAtPos(wx - 1, wz + 1);
        float southY = altitudeMapGenerator.altitudeAtPos(wx, wz + 1);
        float southeastY = altitudeMapGenerator.altitudeAtPos(wx + 1, wz + 1);
        float westY = altitudeMapGenerator.altitudeAtPos(wx - 1, wz);
        float eastY = altitudeMapGenerator.altitudeAtPos(wx + 1, wz);
        float northwestY = altitudeMapGenerator.altitudeAtPos(wx - 1, wz - 1);
        float northY = altitudeMapGenerator.altitudeAtPos(wx, wz - 1);
        float northeastY = altitudeMapGenerator.altitudeAtPos(wx + 1, wz - 1);

        if (y > northwestY && y > westY && y > northY) {
            world.putTileAt_WP(wx, y, wz, new SlopeVoxel(NORTHWEST_CORNER));
            return true;
        }

        if (y > northeastY && y > northY && y > eastY) {
            world.putTileAt_WP(wx, y, wz, new SlopeVoxel(NORTHEAST_CORNER));
            return true;
        }

        if (y > southwestY && y > westY && y > southY) {
            world.putTileAt_WP(wx, y, wz, new SlopeVoxel(SOUTHWEST_CORNER));
            return true;
        }

        if (y > southeastY && y > eastY && y > southY) {
            world.putTileAt_WP(wx, y, wz, new SlopeVoxel(SOUTHEAST_CORNER));
            return true;
        }

        if (y > southwestY && MathUtils.isEqual(y, westY) && MathUtils.isEqual(y, southY)) {
            world.putTileAt_WP(wx, y, wz, new SlopeVoxel(SOUTHWEST_INNER_CORNER));
            return true;
        }

        if (y > southeastY && MathUtils.isEqual(y, eastY) && MathUtils.isEqual(y, southY)) {
            world.putTileAt_WP(wx, y, wz, new SlopeVoxel(SOUTHEAST_INNER_CORNER));
            return true;
        }

        if (y > northwestY && MathUtils.isEqual(y, westY) && MathUtils.isEqual(y, northY)) {
            world.putTileAt_WP(wx, y, wz, new SlopeVoxel(NORTHWEST_INNER_CORNER));
            return true;
        }

        if (y > northeastY && MathUtils.isEqual(y, eastY) && MathUtils.isEqual(y, northY)) {
            world.putTileAt_WP(wx, y, wz, new SlopeVoxel(NORTHEAST_INNER_CORNER));
            return true;
        }

        if (westY < y) {
            world.putTileAt_WP(wx, y, wz, new SlopeVoxel(WEST_EDGE));
            return true;
        }

        if (eastY < y) {
            world.putTileAt_WP(wx, y, wz, new SlopeVoxel(EAST_EDGE));
            return true;
        }

        if (southY < y) {
            world.putTileAt_WP(wx, y, wz, new SlopeVoxel(SOUTH_EDGE));
            return true;
        }

        if (northY < y) {
            world.putTileAt_WP(wx, y, wz, new SlopeVoxel(NORTH_EDGE));
            return true;
        }

        return false;
    }
}
