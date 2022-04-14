package vc.andro.poketest.world;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;
import org.jetbrains.annotations.Nullable;
import vc.andro.poketest.tile.BasicTile;

public class Chunk {

    public static final int CHUNK_SIZE = 16; // in tiles
    public static final int CHUNK_DEPTH = 128;
    private static final int VERTEX_SIZE = 6;
    public static final int VOXELS_PER_CHUNK = CHUNK_SIZE * CHUNK_DEPTH * CHUNK_SIZE;
    public static final int MAX_VERTICES_PER_CHUNK = VOXELS_PER_CHUNK * 6 * 4;
    public static final int INDICES_PER_CHUNK = VOXELS_PER_CHUNK * 6 * 6 / 3;

    private static short[] INDICES = new short[INDICES_PER_CHUNK];

    static {
        short j = 0;
        for (int i = 0; i < INDICES_PER_CHUNK; i += 6, j += 4) {
            INDICES[i] = j;
            INDICES[i + 1] = (short) (j + 1);
            INDICES[i + 2] = (short) (j + 2);
            INDICES[i + 3] = (short) (j + 2);
            INDICES[i + 4] = (short) (j + 3);
            INDICES[i + 5] = j;
        }

    }

    public final World world;
    public final int chunkX;
    public final int chunkZ;
    public final BasicTile[][][] voxels;
    public float[] vertexAttributes;
    private int vertexAttributes_i;
    private final Vector3 worldOffsetPos;
    public int amountVertices;
    private final Mesh mesh;
    private final Material material;
    public boolean needsRenderingUpdate;

    public Chunk(World world, int chunkX, int chunkZ) {
        this.world = world;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        voxels = new BasicTile[CHUNK_SIZE][CHUNK_DEPTH][CHUNK_SIZE];
        vertexAttributes = new float[VERTEX_SIZE * VERTEX_SIZE * VOXELS_PER_CHUNK];
        vertexAttributes_i = 0;
        worldOffsetPos = new Vector3(World.CxWx(chunkX), 0, World.CzWz(chunkZ));
        amountVertices = 0;
        {
            mesh = new Mesh(true, MAX_VERTICES_PER_CHUNK, INDICES_PER_CHUNK, VertexAttribute.Position(), VertexAttribute.Normal());
            mesh.setIndices(INDICES);
        }
        material = new Material(
                new ColorAttribute(ColorAttribute.Diffuse,
                        MathUtils.random(0.5f, 1.0f),
                        MathUtils.random(0.5f, 1.0f),
                        MathUtils.random(0.5f, 1.0f), 1));
        needsRenderingUpdate = true;
    }

    public @Nullable
    BasicTile getTileAt(int chunkLocalX, int y, int chunkLocalZ) {
        return voxels[chunkLocalX][y][chunkLocalZ];
    }

    public void putTileAt(int chunkLocalX, int y, int chunkLocalZ, BasicTile tile) {
        tile.world = world;
        tile.chunk = this;
        tile.worldX = chunkX * CHUNK_SIZE + chunkLocalX;
        tile.worldZ = chunkZ * CHUNK_SIZE + chunkLocalZ;
        tile.y = y;
        tile.chunkLocalX = chunkLocalX;
        tile.chunkLocalZ = chunkLocalZ;
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
            vertexAttributes_i = 0;
            for (int y = 0; y < CHUNK_DEPTH; y++) {
                for (int z = 0; z < CHUNK_SIZE; z++) {
                    for (int x = 0; x < CHUNK_SIZE; x++) {
                        BasicTile voxel = voxels[x][y][z];
                        if (voxel == null) {
                            continue;
                        }
                        if (y < CHUNK_DEPTH - 1) {
                            if (voxels[x][y + 1][z] == null) {
                                createTop(x, y, z);
                            }
                        } else {
                            createTop(x, y, z);
                        }
                        if (y > 0) {
                            if (voxels[x][y - 1][z] == null) {
                                createBottom(x, y, z);
                            }
                        } else {
                            createBottom(x, y, z);
                        }
                        if (x > 0) {
                            if (voxels[x - 1][y][z] == null) {
                                createLeft(x, y, z);
                            }
                        } else {
                            createLeft(x, y, z);
                        }
                        if (x < CHUNK_SIZE - 1) {
                            if (voxels[x + 1][y][z] == null) {
                                createRight(x, y, z);
                            }
                        } else {
                            createRight(x, y, z);
                        }
                        if (z > 0) {
                            if (voxels[x][y][z - 1] == null) createFront(x, y, z);
                        } else {
                            createFront(x, y, z);
                        }
                        if (z < CHUNK_SIZE - 1) {
                            if (voxels[x][y][z + 1] == null) {
                                createBack(x, y, z);
                            }
                        } else {
                            createBack(x, y, z);
                        }
                    }
                }
            }
            amountVertices = vertexAttributes_i / VERTEX_SIZE;
            amountVertices = amountVertices / 4 * 6;
            mesh.setVertices(vertexAttributes, 0, vertexAttributes_i);
            needsRenderingUpdate = false;
        }
    }

    private void createTop(int x, int y, int z) {
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.x + x;
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.y + y + 1;
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.z + z;
        vertexAttributes[vertexAttributes_i++] = 0;
        vertexAttributes[vertexAttributes_i++] = 1;
        vertexAttributes[vertexAttributes_i++] = 0;

        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.x + x + 1;
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.y + y + 1;
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.z + z;
        vertexAttributes[vertexAttributes_i++] = 0;
        vertexAttributes[vertexAttributes_i++] = 1;
        vertexAttributes[vertexAttributes_i++] = 0;

        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.x + x + 1;
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.y + y + 1;
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.z + z + 1;
        vertexAttributes[vertexAttributes_i++] = 0;
        vertexAttributes[vertexAttributes_i++] = 1;
        vertexAttributes[vertexAttributes_i++] = 0;

        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.x + x;
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.y + y + 1;
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.z + z + 1;
        vertexAttributes[vertexAttributes_i++] = 0;
        vertexAttributes[vertexAttributes_i++] = 1;
        vertexAttributes[vertexAttributes_i++] = 0;
    }

    private void createBottom(int x, int y, int z) {
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.x + x;
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.y + y;
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.z + z;
        vertexAttributes[vertexAttributes_i++] = 0;
        vertexAttributes[vertexAttributes_i++] = -1;
        vertexAttributes[vertexAttributes_i++] = 0;

        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.x + x;
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.y + y;
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.z + z + 1;
        vertexAttributes[vertexAttributes_i++] = 0;
        vertexAttributes[vertexAttributes_i++] = -1;
        vertexAttributes[vertexAttributes_i++] = 0;

        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.x + x + 1;
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.y + y;
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.z + z + 1;
        vertexAttributes[vertexAttributes_i++] = 0;
        vertexAttributes[vertexAttributes_i++] = -1;
        vertexAttributes[vertexAttributes_i++] = 0;

        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.x + x + 1;
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.y + y;
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.z + z;
        vertexAttributes[vertexAttributes_i++] = 0;
        vertexAttributes[vertexAttributes_i++] = -1;
        vertexAttributes[vertexAttributes_i++] = 0;
    }

    private void createLeft(int x, int y, int z) {
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.x + x;
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.y + y;
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.z + z;
        vertexAttributes[vertexAttributes_i++] = -1;
        vertexAttributes[vertexAttributes_i++] = 0;
        vertexAttributes[vertexAttributes_i++] = 0;

        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.x + x;
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.y + y + 1;
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.z + z;
        vertexAttributes[vertexAttributes_i++] = -1;
        vertexAttributes[vertexAttributes_i++] = 0;
        vertexAttributes[vertexAttributes_i++] = 0;

        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.x + x;
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.y + y + 1;
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.z + z + 1;
        vertexAttributes[vertexAttributes_i++] = -1;
        vertexAttributes[vertexAttributes_i++] = 0;
        vertexAttributes[vertexAttributes_i++] = 0;

        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.x + x;
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.y + y;
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.z + z + 1;
        vertexAttributes[vertexAttributes_i++] = -1;
        vertexAttributes[vertexAttributes_i++] = 0;
        vertexAttributes[vertexAttributes_i++] = 0;
    }

    private void createRight(int x, int y, int z) {
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.x + x + 1;
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.y + y;
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.z + z;
        vertexAttributes[vertexAttributes_i++] = 1;
        vertexAttributes[vertexAttributes_i++] = 0;
        vertexAttributes[vertexAttributes_i++] = 0;

        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.x + x + 1;
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.y + y;
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.z + z + 1;
        vertexAttributes[vertexAttributes_i++] = 1;
        vertexAttributes[vertexAttributes_i++] = 0;
        vertexAttributes[vertexAttributes_i++] = 0;

        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.x + x + 1;
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.y + y + 1;
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.z + z + 1;
        vertexAttributes[vertexAttributes_i++] = 1;
        vertexAttributes[vertexAttributes_i++] = 0;
        vertexAttributes[vertexAttributes_i++] = 0;

        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.x + x + 1;
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.y + y + 1;
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.z + z;
        vertexAttributes[vertexAttributes_i++] = 1;
        vertexAttributes[vertexAttributes_i++] = 0;
        vertexAttributes[vertexAttributes_i++] = 0;
    }

    private void createFront(int x, int y, int z) {
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.x + x;
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.y + y;
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.z + z;
        vertexAttributes[vertexAttributes_i++] = 0;
        vertexAttributes[vertexAttributes_i++] = 0;
        vertexAttributes[vertexAttributes_i++] = 1;

        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.x + x + 1;
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.y + y;
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.z + z;
        vertexAttributes[vertexAttributes_i++] = 0;
        vertexAttributes[vertexAttributes_i++] = 0;
        vertexAttributes[vertexAttributes_i++] = 1;

        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.x + x + 1;
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.y + y + 1;
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.z + z;
        vertexAttributes[vertexAttributes_i++] = 0;
        vertexAttributes[vertexAttributes_i++] = 0;
        vertexAttributes[vertexAttributes_i++] = 1;

        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.x + x;
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.y + y + 1;
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.z + z;
        vertexAttributes[vertexAttributes_i++] = 0;
        vertexAttributes[vertexAttributes_i++] = 0;
        vertexAttributes[vertexAttributes_i++] = 1;
    }

    private void createBack(int x, int y, int z) {
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.x + x;
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.y + y;
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.z + z + 1;
        vertexAttributes[vertexAttributes_i++] = 0;
        vertexAttributes[vertexAttributes_i++] = 0;
        vertexAttributes[vertexAttributes_i++] = -1;

        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.x + x;
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.y + y + 1;
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.z + z + 1;
        vertexAttributes[vertexAttributes_i++] = 0;
        vertexAttributes[vertexAttributes_i++] = 0;
        vertexAttributes[vertexAttributes_i++] = -1;

        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.x + x + 1;
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.y + y + 1;
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.z + z + 1;
        vertexAttributes[vertexAttributes_i++] = 0;
        vertexAttributes[vertexAttributes_i++] = 0;
        vertexAttributes[vertexAttributes_i++] = -1;

        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.x + x + 1;
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.y + y;
        vertexAttributes[vertexAttributes_i++] = worldOffsetPos.z + z + 1;
        vertexAttributes[vertexAttributes_i++] = 0;
        vertexAttributes[vertexAttributes_i++] = 0;
        vertexAttributes[vertexAttributes_i++] = -1;
    }

    public Renderable getRenderable(Pool<Renderable> pool) {
        Renderable r = pool.obtain();
        r.material = material;
        r.meshPart.mesh = mesh;
        r.meshPart.offset = 0;
        r.meshPart.size = amountVertices;
        r.meshPart.primitiveType = GL20.GL_TRIANGLES;
        return r;
    }
}
