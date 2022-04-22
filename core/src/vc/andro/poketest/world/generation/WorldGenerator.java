package vc.andro.poketest.world.generation;

import com.badlogic.gdx.Gdx;
import org.jetbrains.annotations.Nullable;
import vc.andro.poketest.Direction;
import vc.andro.poketest.entity.FlowerEntity;
import vc.andro.poketest.entity.TallGrassEntity;
import vc.andro.poketest.entity.TreeEntity;
import vc.andro.poketest.util.BlueNoise;
import vc.andro.poketest.util.FastNoise;
import vc.andro.poketest.voxel.VoxelAttributes;
import vc.andro.poketest.voxel.VoxelSpec;
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

import static vc.andro.poketest.world.Chunk.CHUNK_DEPTH;
import static vc.andro.poketest.world.Chunk.CHUNK_SIZE;
import static vc.andro.poketest.world.World.*;

public class WorldGenerator {
    private final WorldCreationParams params;
    private final Random random;
    private final AltitudeMapGenerator altitudeMapGenerator;
    private final WorldGenEntitySpawner<?> treeSpawner;
    private final WorldGenEntitySpawner<?> flowerSpawner;
    private final WorldGenEntitySpawner<?> tallGrassSpawner;
    private @Nullable World world = null;

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
    }

    public World createNewWorld() {
        log("Generating world...");
        world = new World(this);
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
        assert world != null : "Missing world";
        log("Generating chunk: " + cx + ", " + cz);
        Chunk chunk = world.createBlankChunkAt_CP(cx, cz);

        for (int lx = 0; lx < CHUNK_SIZE; lx++) {
            for (int lz = 0; lz < CHUNK_SIZE; lz++) {
                generateColumn(chunk, lx, lz);
            }
        }

        shaveVoxelsInChunk(chunk);

        treeSpawner.spawnEntitiesInChunk(chunk);
        flowerSpawner.spawnEntitiesInChunk(chunk);
        tallGrassSpawner.spawnEntitiesInChunk(chunk);
        world.updateChunk(cx, cz);
    }

    private void shaveVoxelsInChunk(Chunk chunk) {
        assert world != null : "Missing world";
        int startWx = CxWx(chunk.cx);
        int startWz = CzWz(chunk.cz);
        for (int lx = 0, wx = startWx; lx < CHUNK_SIZE; lx++, wx++) {
            for (int y = 0; y < CHUNK_DEPTH; y++) {
                for (int lz = 0, wz = startWz; lz < CHUNK_SIZE; lz++, wz++) {
                    byte voxel = chunk.getVoxelAt_LP(lx, y, lz);
                    if (voxel == 0) continue;
                    VoxelSpec voxelSpec = VoxelSpecs.VOXEL_TYPES[voxel];
                    if (!voxelSpec.canBeSloped) continue;

                    boolean isVoxelAbove = y < CHUNK_DEPTH - 1 && world.getVoxelAt_WP(wx, y + 1, wz) > 0;
                    if (isVoxelAbove) {
                        continue;
                    }

                    boolean isVoxelBelow = y > 0 && world.getVoxelAt_WP(wx, y - 1, wz) > 0;

                    boolean isVoxelWest = world.getVoxelAt_WP(wx - 1, y, wz) > 0;
                    boolean isVoxelEast = world.getVoxelAt_WP(wx + 1, y, wz) > 0;
                    boolean isVoxelSouth = world.getVoxelAt_WP(wx, y, wz + 1) > 0;
                    boolean isVoxelNorth = world.getVoxelAt_WP(wx, y, wz - 1) > 0;

                    boolean isVoxelNorthwest = world.getVoxelAt_WP(wx - 1, y, wz - 1) > 0;
                    boolean isVoxelNortheast = world.getVoxelAt_WP(wx + 1, y, wz - 1) > 0;
                    boolean isVoxelSouthwest = world.getVoxelAt_WP(wx - 1, y, wz + 1) > 0;
                    boolean isVoxelSoutheast = world.getVoxelAt_WP(wx + 1, y, wz + 1) > 0;

                    /*
                      Southwest-facing corner
                      ▢ ▢
                      ▢ ◢
                     */
                    if (!isVoxelNorthwest && !isVoxelWest && !isVoxelNorth) {
                        VoxelAttributes attrs = chunk.getVoxelAttrsAt_G_LP(lx, y, lz);
                        attrs.configureSlope(Direction.NORTHWEST, false);
                        continue;
                    }

                    /*
                      Northeast-facing corner
                      ▢ ▢
                      ◣ ▢
                     */
                    if (!isVoxelNorth && !isVoxelNortheast && !isVoxelEast) {
                        VoxelAttributes attrs = chunk.getVoxelAttrsAt_G_LP(lx, y, lz);
                        attrs.configureSlope(Direction.NORTHEAST, false);
                        continue;
                    }

                    /*
                      Southwest-facing corner
                      ▢ ◥
                      ▢ ▢
                     */
                    if (!isVoxelSouthwest && !isVoxelWest && !isVoxelSouth) {
                        VoxelAttributes attrs = chunk.getVoxelAttrsAt_G_LP(lx, y, lz);
                        attrs.configureSlope(Direction.SOUTHWEST, false);
                        continue;
                    }

                    /*
                      Southeast-facing corner
                      ◤ ▢
                      ▢ ▢
                     */
                    if (!isVoxelSoutheast && !isVoxelEast && !isVoxelSouth) {
                        VoxelAttributes attrs = chunk.getVoxelAttrsAt_G_LP(lx, y, lz);
                        attrs.configureSlope(Direction.SOUTHEAST, false);
                        continue;
                    }


                    /*
                      Northwest-facing inner corner
                      ▢ |
                      _ 」←
                     */
                    if (!isVoxelNorthwest && isVoxelWest && isVoxelNorth) {
                        VoxelAttributes attrs = chunk.getVoxelAttrsAt_G_LP(lx, y, lz);
                        attrs.configureSlope(Direction.NORTHWEST, true);
                        continue;
                    }

                    /*
                     Northeast-facing inner corner
                       | ▢
                     → ⌞ _
                     */
                    if (!isVoxelNortheast && isVoxelEast && isVoxelNorth) {
                        VoxelAttributes attrs = chunk.getVoxelAttrsAt_G_LP(lx, y, lz);
                        attrs.configureSlope(Direction.NORTHEAST, true);
                        continue;
                    }

                    /*
                     Southwest-facing inner corner
                        ̅ ⌝ ←
                       ▢ |
                     */
                    if (!isVoxelSouthwest && isVoxelWest && isVoxelSouth) {
                        VoxelAttributes attrs = chunk.getVoxelAttrsAt_G_LP(lx, y, lz);
                        attrs.configureSlope(Direction.SOUTHWEST, true);
                        continue;
                    }

                    /*
                     Southeast-facing inner corner
                      → ⌜  ̅
                        | ▢
                     */
                    if (!isVoxelSoutheast && isVoxelEast && isVoxelSouth) {
                        VoxelAttributes attrs = chunk.getVoxelAttrsAt_G_LP(lx, y, lz);
                        attrs.configureSlope(Direction.SOUTHEAST, true);
                        continue;
                    }

                    /*  West edge */
                    if (!isVoxelWest) {
                        VoxelAttributes attrs = chunk.getVoxelAttrsAt_G_LP(lx, y, lz);
                        attrs.configureSlope(Direction.WEST, false);
                        continue;
                    }

                    /* East edge */
                    if (!isVoxelEast) {
                        VoxelAttributes attrs = chunk.getVoxelAttrsAt_G_LP(lx, y, lz);
                        attrs.configureSlope(Direction.EAST, false);
                        continue;
                    }

                    /* South edge */
                    if (!isVoxelSouth) {
                        VoxelAttributes attrs = chunk.getVoxelAttrsAt_G_LP(lx, y, lz);
                        attrs.configureSlope(Direction.SOUTH, false);
                        continue;
                    }

                    /* North edge */
                    if (!isVoxelNorth) {
                        VoxelAttributes attrs = chunk.getVoxelAttrsAt_G_LP(lx, y, lz);
                        attrs.configureSlope(Direction.NORTH, false);
                        //noinspection UnnecessaryContinue
                        continue;
                    }
                }
            }
        }
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
        assert world != null : "Missing world";
        {
            int wx = LxWx(chunk.cx, lx);
            int wz = LzWz(chunk.cz, lz);
            y = y < 0 ? altitudeMapGenerator.getAtPosition(wx, wz) : y;
        }

        if (y <= params.waterLevel) {
            // Is water tile
            chunk.putVoxelAt_LP(lx, params.waterLevel, lz, VoxelSpecs.getId(VoxelSpecs.WATER));
        } else if (y <= params.beachAltitude) {
            // Is sand tile
            chunk.putVoxelAt_LP(lx, y, lz, VoxelSpecs.getId(VoxelSpecs.SAND));
        } else {
            // Spawn grass
            chunk.putVoxelAt_LP(lx, y, lz, VoxelSpecs.getId(VoxelSpecs.GRASS));
        }

        if (y > 0) {
            generateColumn(chunk, lx, lz, y - 1);
        }
    }
}
