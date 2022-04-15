package vc.andro.poketest.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.ShortArray;
import org.jetbrains.annotations.Nullable;
import vc.andro.poketest.Assets;
import vc.andro.poketest.PokeTest;
import vc.andro.poketest.tile.BasicTile;

import static vc.andro.poketest.world.VertexArray.VERTEX_SIZE;

public class Chunk {

    public static final int CHUNK_SIZE = 16; // in tiles
    public static final int CHUNK_DEPTH = 128;
    public static final int VOXELS_PER_CHUNK = CHUNK_SIZE * CHUNK_DEPTH * CHUNK_SIZE;
    public static final int FACES_IN_A_CUBE = 6;
    public static final int POINTS_IN_A_SQUARE = 4;
    public static final int SIDES_IN_A_TRIANGLE = 3;
    public static final int MAX_VERTICES_PER_CHUNK = VOXELS_PER_CHUNK * FACES_IN_A_CUBE * POINTS_IN_A_SQUARE;
    public static final int MAX_INDICES_PER_CHUNK = VOXELS_PER_CHUNK * (int)Math.pow(FACES_IN_A_CUBE, 2) / SIDES_IN_A_TRIANGLE;

    public final World world;
    public final int chunkX;
    public final int chunkZ;
    public final BasicTile[][][] voxels;
    public int voxelCount; // Amount of voxels that exist in this chunk

    private final VertexArray vertexArray;
    private final IndexArray indicesArray;
    private final Mesh mesh;
    private final Material material;

    public boolean needsRenderingUpdate;

    public Chunk(World world, int chunkX, int chunkZ) {
        this.world = world;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        voxels = new BasicTile[CHUNK_SIZE][CHUNK_DEPTH][CHUNK_SIZE];
        vertexArray = new VertexArray();
        indicesArray = new IndexArray();
        mesh = new Mesh(true, MAX_VERTICES_PER_CHUNK, MAX_INDICES_PER_CHUNK,
                VertexAttribute.Position(),
                VertexAttribute.Normal(),
                VertexAttribute.TexCoords(0));
        material = new Material(
                new TextureAttribute(TextureAttribute.Diffuse,
                        PokeTest.assetManager.get(Assets.tileAtlas).getTextures().first()),
                new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
        needsRenderingUpdate = true;
    }

    public @Nullable
    BasicTile getTileAt(int chunkLocalX, int y, int chunkLocalZ) {
        return voxels[chunkLocalX][y][chunkLocalZ];
    }

    public void putTileAt(int chunkLocalX, int y, int chunkLocalZ, @Nullable BasicTile tile) {
        BasicTile previousTile = getTileAt(chunkLocalX, y, chunkLocalZ);
        if (previousTile == null && tile != null) {
            voxelCount++;
        } else if (previousTile != null && tile == null) {
            voxelCount--;
        }
        if (tile != null) {
            tile.world = world;
            tile.chunk = this;
            tile.worldX = chunkX * CHUNK_SIZE + chunkLocalX;
            tile.worldZ = chunkZ * CHUNK_SIZE + chunkLocalZ;
            tile.y = y;
            tile.chunkLocalX = chunkLocalX;
            tile.chunkLocalZ = chunkLocalZ;
        }
        voxels[chunkLocalX][y][chunkLocalZ] = tile;
    }

    public void updateTiles() {
        for (int chunkLocalX = 0; chunkLocalX < CHUNK_SIZE; chunkLocalX++) {
            for (int y = 0; y < CHUNK_DEPTH; y++) {
                for (int chunkLocalZ = 0; chunkLocalZ < CHUNK_SIZE; chunkLocalZ++) {
                    BasicTile tile = getTileAt(chunkLocalX, y, chunkLocalZ);
                    if (tile == null) {
                        continue;
                    }
                    tile.doTileUpdate();
                }
            }
        }
    }

    public @Nullable
    BasicTile getSurfaceTile(int localChunkX, int localChunkZ) {
        for (int y = CHUNK_DEPTH - 1; y >= 0; y--) {
            BasicTile tile = getTileAt(localChunkX, y, localChunkZ);
            if (tile != null) {
                return tile;
            }
        }
        return null;
    }

    public void updateVerticesIfDirty() {
        if (needsRenderingUpdate) {
            vertexArray.clear();
            indicesArray.clear();
            for (int y = 0; y < CHUNK_DEPTH; y++) {
                for (int z = 0; z < CHUNK_SIZE; z++) {
                    for (int x = 0; x < CHUNK_SIZE; x++) {
                        BasicTile voxel = voxels[x][y][z];
                        if (voxel == null) {
                            continue;
                        }
                        if (y < CHUNK_DEPTH - 1) {
                            if (voxels[x][y + 1][z] == null || voxels[x][y + 1][z].transparent) {
                                voxel.createTopVertices(vertexArray);
                                indicesArray.addSquare();
                            }
                        } else {
                            voxel.createTopVertices(vertexArray);
                            indicesArray.addSquare();
                        }
                        if (y > 0) {
                            if (voxels[x][y - 1][z] == null || voxels[x][y - 1][z].transparent) {
                                voxel.createBottomVertices(vertexArray);
                                indicesArray.addSquare();
                            }
                        } else {
                            voxel.createBottomVertices(vertexArray);
                            indicesArray.addSquare();
                        }
                        if (x > 0) {
                            if (voxels[x - 1][y][z] == null || voxels[x - 1][y][z].transparent) {
                                voxel.createLeftVertices(vertexArray);
                                indicesArray.addSquare();
                            }
                        } else {
                            voxel.createLeftVertices(vertexArray);
                            indicesArray.addSquare();
                        }
                        if (x < CHUNK_SIZE - 1) {
                            if (voxels[x + 1][y][z] == null || voxels[x + 1][y][z].transparent) {
                                voxel.createRightVertices(vertexArray);
                                indicesArray.addSquare();
                            }
                        } else {
                            voxel.createRightVertices(vertexArray);
                            indicesArray.addSquare();
                        }
                        if (z > 0) {
                            if (voxels[x][y][z - 1] == null || voxels[x][y][z - 1].transparent) {
                                voxel.createFrontVertices(vertexArray);
                                indicesArray.addSquare();
                            }
                        } else {
                            voxel.createFrontVertices(vertexArray);
                            indicesArray.addSquare();
                        }
                        if (z < CHUNK_SIZE - 1) {
                            if (voxels[x][y][z + 1] == null || voxels[x][y][z + 1].transparent) {
                                voxel.createBackVertices(vertexArray);
                                indicesArray.addSquare();
                            }
                        } else {
                            voxel.createBackVertices(vertexArray);
                            indicesArray.addSquare();
                        }
                    }
                }
            }
            mesh.setVertices(vertexArray.getItems(), 0, vertexArray.getItems().length);
            mesh.setIndices(indicesArray.getItems(), 0, indicesArray.getItems().length);
            needsRenderingUpdate = false;
            Gdx.app.log("Chunk",
                    "Amount vertices: " + vertexArray.getAmountVertices()
                            + ", amount attributes: " + vertexArray.getItems().length);
        }
    }

    public Renderable getRenderable(Pool<Renderable> pool) {
        Renderable r = pool.obtain();
        r.material = material;
        r.meshPart.mesh = mesh;
        r.meshPart.offset = 0;
        r.meshPart.size = (vertexArray.getItems().length / VERTEX_SIZE) / POINTS_IN_A_SQUARE * FACES_IN_A_CUBE;
        r.meshPart.primitiveType = GL20.GL_TRIANGLES;
        return r;
    }

    public int getAmountVertices() {
        return vertexArray.getAmountVertices();
    }
}
