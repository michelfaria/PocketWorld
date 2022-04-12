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
import static vc.andro.poketest.world.World.chunkLocalXToWorldX;
import static vc.andro.poketest.world.World.chunkLocalZToWorldZ;

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
                final int worldX = chunkLocalXToWorldX(chunkX, chunkLocalX);
                final int worldZ = chunkLocalZToWorldZ(chunkZ, chunkLocalZ);
                final float altitude = altitudeMapGenerator.altitudeAtPos(worldX, worldZ);

                if (altitude <= world.getCreationParams().waterLevel) {
                    // Is water tile
                    world.putTileAt(worldX, worldZ, new BasicTile(TileType.WATER, altitude));
                    continue;
                }

                if (generateWalls(worldX, worldZ, altitude)) {
                    // Is wall tile
                    continue;
                }

                if (altitude <= params.beachAltitude) {
                    // Is sand tile
                    world.putTileAt(worldX, worldZ, new BasicTile(TileType.SAND, altitude));
                    continue;
                }

                // Spawn grass
                world.putTileAt(worldX, worldZ, new BasicTile(TileType.GRASS, altitude));
            }
        }
        spawnTrees(chunkX, chunkZ);
        world.updateChunk(chunkX, chunkZ);
    }

    private void spawnTrees(int chunkX, int chunkZ) {
        assert world != null;
        for (int chunkLocalX = 0; chunkLocalX < CHUNK_SIZE; chunkLocalX++) {
            for (int chunkLocalZ = 0; chunkLocalZ < CHUNK_SIZE; chunkLocalZ++) {
                final int worldX = chunkLocalXToWorldX(chunkX, chunkLocalX);
                final int worldZ = chunkLocalZToWorldZ(chunkZ, chunkLocalZ);

                if (shouldTreeBeSpawnedAtPosition(worldX, worldZ)) {
                    var tree = new TreeEntity();
                    tree.worldX = worldX;
                    tree.worldZ = worldZ;
                    world.addEntity(tree);
                }
            }
        }
    }

    private boolean shouldTreeBeSpawnedAtPosition(int worldX, int worldZ) {
        if (!isTreeAllowedInPosition(worldX, worldZ)) {
            return false;
        }
        int treeType = treeMapGenerator.getTreeAtPos(worldX, worldZ);
        return treeType > 0;
    }

    private boolean isTreeAllowedInPosition(int worldX, int worldZ) {
        assert world != null;
        BasicTile tile = world.getTileOrGenerateAt(worldX, worldZ);
        return tile != null && tile.type.equals(TileType.GRASS);
    }

    @SuppressWarnings("DuplicatedCode")
    private boolean generateWalls(int worldX, int worldZ, float altitude) {
        assert world != null;

        final float topLeftAltitude = altitudeMapGenerator.altitudeAtPos(worldX - 1, worldZ + 1);
        final float topAltitude = altitudeMapGenerator.altitudeAtPos(worldX, worldZ + 1);
        final float topRightAltitude = altitudeMapGenerator.altitudeAtPos(worldX + 1, worldZ + 1);
        final float leftAltitude = altitudeMapGenerator.altitudeAtPos(worldX - 1, worldZ);
        final float rightAltitude = altitudeMapGenerator.altitudeAtPos(worldX + 1, worldZ);
        final float bottomLeftAltitude = altitudeMapGenerator.altitudeAtPos(worldX - 1, worldZ - 1);
        final float bottomAltitude = altitudeMapGenerator.altitudeAtPos(worldX, worldZ - 1);
        final float bottomRightAltitude = altitudeMapGenerator.altitudeAtPos(worldX + 1, worldZ - 1);

        if (altitude > topLeftAltitude && altitude > leftAltitude && altitude > topAltitude) {
            world.putTileAt(worldX, worldZ, new WallTile(altitude, TOP_LEFT_CORNER));
            return true;
        }


        if (altitude > topRightAltitude && altitude > topAltitude && altitude > rightAltitude) {
            world.putTileAt(worldX, worldZ, new WallTile(altitude, TOP_RIGHT_CORNER));
            return true;
        }

        if (altitude > bottomLeftAltitude && altitude > leftAltitude && altitude > bottomAltitude) {
            world.putTileAt(worldX, worldZ, new WallTile(altitude, BOTTOM_LEFT_CORNER));
            return true;
        }

        if (altitude > bottomRightAltitude && altitude > rightAltitude && altitude > bottomAltitude) {
            world.putTileAt(worldX, worldZ, new WallTile(altitude, BOTTOM_RIGHT_CORNER));
            return true;
        }

        if (altitude > bottomLeftAltitude && MathUtils.isEqual(altitude, leftAltitude) && MathUtils.isEqual(altitude, bottomAltitude)) {
            world.putTileAt(worldX, worldZ, new WallTile(altitude, BOTTOM_LEFT_INNER_CORNER));
            return true;
        }

        if (altitude > bottomRightAltitude && MathUtils.isEqual(altitude, rightAltitude) && MathUtils.isEqual(altitude, bottomAltitude)) {
            world.putTileAt(worldX, worldZ, new WallTile(altitude, BOTTOM_RIGHT_INNER_CORNER));
            return true;
        }

        if (altitude > topLeftAltitude && MathUtils.isEqual(altitude, leftAltitude) && MathUtils.isEqual(altitude, topAltitude)) {
            world.putTileAt(worldX, worldZ, new WallTile(altitude, TOP_LEFT_INNER_CORNER));
            return true;
        }

        if (altitude > topRightAltitude && MathUtils.isEqual(altitude, rightAltitude) && MathUtils.isEqual(altitude, topAltitude)) {
            world.putTileAt(worldX, worldZ, new WallTile(altitude, TOP_RIGHT_INNER_CORNER));
            return true;
        }


        if (leftAltitude < altitude) {
            world.putTileAt(worldX, worldZ, new WallTile(altitude, LEFT_EDGE));
            return true;
        }

        if (rightAltitude < altitude) {
            world.putTileAt(worldX, worldZ, new WallTile(altitude, RIGHT_EDGE));
            return true;
        }

        if (bottomAltitude < altitude) {
            world.putTileAt(worldX, worldZ, new WallTile(altitude, BOTTOM_EDGE));
            return true;
        }

        if (topAltitude < altitude) {
            world.putTileAt(worldX, worldZ, new WallTile(altitude, TOP_EDGE));
            return true;
        }

        return false;
    }
}
