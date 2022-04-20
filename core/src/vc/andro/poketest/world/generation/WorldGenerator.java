package vc.andro.poketest.world.generation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import org.jetbrains.annotations.Nullable;
import vc.andro.poketest.entity.FlowerEntity;
import vc.andro.poketest.entity.TallGrassEntity;
import vc.andro.poketest.entity.TreeEntity;
import vc.andro.poketest.tile.BasicVoxel;
import vc.andro.poketest.tile.SlopeVoxel;
import vc.andro.poketest.tile.VoxelType;
import vc.andro.poketest.util.BlueNoise;
import vc.andro.poketest.util.FastNoise;
import vc.andro.poketest.world.Chunk;
import vc.andro.poketest.world.World;
import vc.andro.poketest.world.WorldCreationParams;
import vc.andro.poketest.world.generation.entity.SimpleEntitySpawner;
import vc.andro.poketest.world.generation.entity.SimpleVegetationEntitySpawnProspector;
import vc.andro.poketest.world.generation.entity.WorldGenEntitySpawner;
import vc.andro.poketest.world.generation.map.AltitudeMapGenerator;
import vc.andro.poketest.world.generation.map.GrassPatchMapGenerator;
import vc.andro.poketest.world.generation.map.VegetationMapGenerator;

import java.util.Random;

import static vc.andro.poketest.tile.SlopeType.*;
import static vc.andro.poketest.world.Chunk.CHUNK_SIZE;
import static vc.andro.poketest.world.World.LxWx;
import static vc.andro.poketest.world.World.LzWz;

public class WorldGenerator {
    public final WorldCreationParams params;
    public final Random random;
    public final AltitudeMapGenerator altitudeMapGenerator;

    public final WorldGenEntitySpawner<?> treeSpawner;
    public final WorldGenEntitySpawner<?> flowerSpawner;
    public final WorldGenEntitySpawner<?> tallGrassSpawner;

    private @Nullable
    World world = null;

    public WorldGenerator(WorldCreationParams params) {
        this.params = params;
        random = new Random(params.seed);

        altitudeMapGenerator = new AltitudeMapGenerator(
                new FastNoise(params.seed, FastNoise.NoiseType.Perlin),
                params
        );
        treeSpawner = new WorldGenEntitySpawner<>(
                new SimpleVegetationEntitySpawnProspector(
                        new VegetationMapGenerator(new BlueNoise(params.seed)) {
                            @Override
                            public int getRValue() {
                                return params.treeMapRValue;
                            }
                        },
                        2, 2
                ),
                new SimpleEntitySpawner<>(TreeEntity.class)
        );
        flowerSpawner = new WorldGenEntitySpawner<>(
                new SimpleVegetationEntitySpawnProspector(
                        new VegetationMapGenerator(new BlueNoise(params.seed + 1)) {
                            @Override
                            public int getRValue() {
                                return params.flowerMapRValue;
                            }
                        }),
                new SimpleEntitySpawner<>(FlowerEntity.class)
        );
        tallGrassSpawner = new WorldGenEntitySpawner<>(
                new SimpleVegetationEntitySpawnProspector(
                        new GrassPatchMapGenerator(
                                new FastNoise(params.seed + 1, FastNoise.NoiseType.Perlin),
                                params
                        )
                ),
                new SimpleEntitySpawner<>(TallGrassEntity.class)
        );
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

        treeSpawner.spawnEntitiesInChunk(chunk);
        flowerSpawner.spawnEntitiesInChunk(chunk);
        tallGrassSpawner.spawnEntitiesInChunk(chunk);

        world.updateChunk(cx, cz);
    }

    private void generateTileAtPosition(int cx, int cz, int lx, int lz) {
        generateTileAtPosition(cx, cz, lx, lz, -1);
    }

    private void generateTileAtPosition(int cx, int cz, int lx, int lz, int y) {
        assert world != null;
        int wx = LxWx(cx, lx);
        int wz = LzWz(cz, lz);
        y = y < 0 ? altitudeMapGenerator.getAtPosition(wx, wz) : y;

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

    @SuppressWarnings("DuplicatedCode")
    private boolean generateWalls(int wx, int y, int wz) {
        assert world != null;

        float southwestY = altitudeMapGenerator.getAtPosition(wx - 1, wz + 1);
        float southY = altitudeMapGenerator.getAtPosition(wx, wz + 1);
        float southeastY = altitudeMapGenerator.getAtPosition(wx + 1, wz + 1);
        float westY = altitudeMapGenerator.getAtPosition(wx - 1, wz);
        float eastY = altitudeMapGenerator.getAtPosition(wx + 1, wz);
        float northwestY = altitudeMapGenerator.getAtPosition(wx - 1, wz - 1);
        float northY = altitudeMapGenerator.getAtPosition(wx, wz - 1);
        float northeastY = altitudeMapGenerator.getAtPosition(wx + 1, wz - 1);

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
