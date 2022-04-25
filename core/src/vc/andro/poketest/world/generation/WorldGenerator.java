package vc.andro.poketest.world.generation;

import com.badlogic.gdx.Gdx;
import org.jetbrains.annotations.NotNull;
import vc.andro.poketest.entity.FlowerEntity;
import vc.andro.poketest.entity.TallGrassEntity;
import vc.andro.poketest.entity.TreeEntity;
import vc.andro.poketest.util.BlueNoise;
import vc.andro.poketest.util.FastNoise;
import vc.andro.poketest.voxel.VoxelSpecs;
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

import static vc.andro.poketest.world.Chunk.CHUNK_SIZE;
import static vc.andro.poketest.world.World.*;

public class WorldGenerator {

    private final WorldCreationParams params;
    private final Random random;
    private final AltitudeMapGenerator altitudeMapGenerator;
    private final WorldGenEntitySpawner<?> treeSpawner;
    private final WorldGenEntitySpawner<?> flowerSpawner;
    private final WorldGenEntitySpawner<?> tallGrassSpawner;
    private final @NotNull World world;

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
                        2, 2),
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
                        new GrassPatchMapGenerator(new FastNoise(params.seed + 1, FastNoise.NoiseType.Perlin))),
                new SimpleEntitySpawner<>(TallGrassEntity.class)
        );
        world = new World(this);
    }

    private void log(String message) {
        Gdx.app.log("WorldGen", message);
    }

    public void generateChunk(int cx, int cz) {
        log("Generating chunk: " + cx + ", " + cz);
        Chunk chunk = world.createBlankChunkAt_CP(cx, cz);

        for (int lx = 0; lx < CHUNK_SIZE; lx++) {
            for (int lz = 0; lz < CHUNK_SIZE; lz++) {
                generateColumn(chunk, lx, lz);
            }
        }

        chunk.slopifyVoxels(true);

        treeSpawner.spawnEntitiesInChunk(chunk);
        flowerSpawner.spawnEntitiesInChunk(chunk);
        tallGrassSpawner.spawnEntitiesInChunk(chunk);

        world.updateChunk(cx, cz);
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
            int wx = LxWx(chunk.cx, lx);
            int wz = LzWz(chunk.cz, lz);
            y = y < 0 ? altitudeMapGenerator.getAtPosition(wx, wz) : y;
        }

        if (y <= params.waterLevel) {
            // Is water tile
            chunk.putVoxelAt_LP(lx, params.waterLevel, lz, VoxelSpecs.getVoxelId(VoxelSpecs.WATER));
        } else if (y <= params.beachAltitude) {
            // Is sand tile
            chunk.putVoxelAt_LP(lx, y, lz, VoxelSpecs.getVoxelId(VoxelSpecs.SAND));
        } else {
            // Spawn grass
            chunk.putVoxelAt_LP(lx, y, lz, VoxelSpecs.getVoxelId(VoxelSpecs.GRASS));
        }

        if (y > 0) {
            generateColumn(chunk, lx, lz, y - 1);
        }
    }

    public World getWorld() {
        return world;
    }
}
