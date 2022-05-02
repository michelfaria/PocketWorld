package vc.andro.poketest.world.generation;

import com.badlogic.gdx.Gdx;
import org.jetbrains.annotations.NotNull;
import vc.andro.poketest.entity.FlowerEntity;
import vc.andro.poketest.entity.TreeEntity;
import vc.andro.poketest.util.BlueNoise;
import vc.andro.poketest.util.FastNoise;
import vc.andro.poketest.util.Pair;
import vc.andro.poketest.voxel.Voxels;
import vc.andro.poketest.world.World;
import vc.andro.poketest.world.WorldCreationParams;
import vc.andro.poketest.world.chunk.Chunk;
import vc.andro.poketest.world.generation.entity.SimpleEntitySpawner;
import vc.andro.poketest.world.generation.entity.SimpleVegetationSpawnProspector;
import vc.andro.poketest.world.generation.entity.SimpleVoxelSpawner;
import vc.andro.poketest.world.generation.entity.WorldGenSpawner;
import vc.andro.poketest.world.generation.map.AltitudeMapGenerator;
import vc.andro.poketest.world.generation.map.GrassPatchMapGenerator;
import vc.andro.poketest.world.generation.map.VegetationMapGenerator;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static vc.andro.poketest.world.World.LxWx;
import static vc.andro.poketest.world.World.LzWz;
import static vc.andro.poketest.world.chunk.Chunk.CHUNK_SIZE;

public class WorldGenerator {

    private final          WorldCreationParams  params;
    private final          Random               random;
    private final          AltitudeMapGenerator altitudeMapGenerator;
    private final          WorldGenSpawner<?>   treeSpawner;
    private final          WorldGenSpawner<?>   flowerSpawner;
    private final          WorldGenSpawner<?>   tallGrassSpawner;
    private final @NotNull World                world;

    // Chunks queued for generation
    private final Set<Pair<Integer, Integer>> chunksQueued = new HashSet<>();

    public WorldGenerator(WorldCreationParams params) {
        this.params = params;
        this.random = new Random(params.seed);

        altitudeMapGenerator = new AltitudeMapGenerator(new FastNoise(params.seed, FastNoise.NoiseType.Perlin), params);

        treeSpawner = new WorldGenSpawner<>(
                new SimpleVegetationSpawnProspector(new VegetationMapGenerator(new BlueNoise(params.seed)) {
                    @Override
                    public int getRValue() {
                        return params.treeMapRValue;
                    }
                }, 2, 2),
                new SimpleEntitySpawner<>(TreeEntity.class));

        flowerSpawner = new WorldGenSpawner<>(
                new SimpleVegetationSpawnProspector(
                        new VegetationMapGenerator(new BlueNoise(params.seed + 1)) {
                            @Override
                            public int getRValue() {
                                return params.flowerMapRValue;
                            }
                        }),
                new SimpleEntitySpawner<>(FlowerEntity.class));

        tallGrassSpawner = new WorldGenSpawner<>(
                new SimpleVegetationSpawnProspector(
                        new GrassPatchMapGenerator(new FastNoise(params.seed + 1, FastNoise.NoiseType.Perlin))),
                new SimpleVoxelSpawner(Voxels.TALL_GRASS));

        world = new World(this);
    }

    private void log(String message) {
        Gdx.app.log("WorldGen", message);
    }

    /**
     * Queues a chunk at the specified chunk coordinates to be generated.
     *
     * @param cx Chunk X
     * @param cz Chunk Z
     */
    public void queueChunkForGeneration(int cx, int cz) {
        synchronized (chunksQueued) {
            chunksQueued.add(new Pair<>(cx, cz));
        }
    }

    public void generateQueuedChunks() {
        if (chunksQueued.isEmpty()) {
            return;
        }

        for (Pair<Integer, Integer> queuedChunk : chunksQueued) {
            int cx = queuedChunk.left;
            int cz = queuedChunk.right;

            log("Generating chunk (%d, %d)".formatted(cx, cz));

            Chunk chunk = world.putEmptyChunkAt_CP(cx, cz);

            for (int lx = 0; lx < CHUNK_SIZE; lx++) {
                for (int lz = 0; lz < CHUNK_SIZE; lz++) {
                    generateColumn(chunk, lx, lz);
                }
            }

            chunk.slopifyVoxels(true);

            treeSpawner.spawnInChunk(chunk);
            flowerSpawner.spawnInChunk(chunk);
            tallGrassSpawner.spawnInChunk(chunk);

            world.updateChunk(cx, cz);

            log("Generated chunk (%d, %d)".formatted(cx, cz));
            world.chunkGenerationFinished.emit(chunk);
        }

        chunksQueued.clear();
    }

    private void generateColumn(Chunk chunk, int lx, int lz) {
        generateColumn(chunk, lx, lz, -1);
    }

    /**
     * Generates a column at the specified chunk (cx,cz) at the chunk pos (lx,lz), with y as its starting point,
     * working its way down.
     *
     * @param chunk Chunk
     * @param lx    Chunk local x
     * @param lz    Chunk local z
     * @param y     If -1, y will start at the altitude at the world position of lx,lz
     */
    private void generateColumn(Chunk chunk, int lx, int lz, int y) {
        {
            int wx = LxWx(chunk.getCx(), lx);
            int wz = LzWz(chunk.getCz(), lz);
            y = y < 0 ? altitudeMapGenerator.getAtPosition(wx, wz) : y;
        }

        if (y <= params.waterLevel) {
            // Is water tile
            chunk.putVoxelAt_LP(lx, params.waterLevel, lz, Voxels.WATER);
        } else if (y <= params.beachAltitude) {
            // Is sand tile
            chunk.putVoxelAt_LP(lx, y, lz, Voxels.SAND);
        } else {
            // Spawn grass
            chunk.putVoxelAt_LP(lx, y, lz, Voxels.GRASS);
        }

        if (y > 0) {
            generateColumn(chunk, lx, lz, y - 1);
        }
    }

    @NotNull
    public World getWorld() {
        return world;
    }
}
