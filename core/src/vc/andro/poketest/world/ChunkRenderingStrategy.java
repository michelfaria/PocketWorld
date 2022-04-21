package vc.andro.poketest.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import vc.andro.poketest.Assets;
import vc.andro.poketest.PocketWorld;
import vc.andro.poketest.voxel.BasicVoxel;

import static vc.andro.poketest.world.Chunk.CHUNK_DEPTH;
import static vc.andro.poketest.world.Chunk.CHUNK_SIZE;
import static vc.andro.poketest.world.VertexArray.VERTEX_SIZE;

public class ChunkRenderingStrategy implements RenderableProvider {

    public static final int VOXELS_PER_CHUNK = CHUNK_SIZE * CHUNK_DEPTH * CHUNK_SIZE;
    public static final int FACES_IN_A_CUBE = 6;
    public static final int POINTS_IN_A_SQUARE = 4;
    public static final int SIDES_IN_A_TRIANGLE = 3;
    public static final int MAX_VERTICES_PER_CHUNK = VOXELS_PER_CHUNK * FACES_IN_A_CUBE * POINTS_IN_A_SQUARE;
    public static final int MAX_INDICES_PER_CHUNK = VOXELS_PER_CHUNK * (int) Math.pow(FACES_IN_A_CUBE, 2) / SIDES_IN_A_TRIANGLE;

    private final Chunk chunk;

    private final VertexArray vertexArray8f;
    private final IndexArray indicesArray;
    private final Mesh mesh;
    private final Material material;
    public boolean needsRenderingUpdate;

    public ChunkRenderingStrategy(Chunk chunk) {
        this.chunk = chunk;

        vertexArray8f = new VertexArray();
        indicesArray = new IndexArray();
        mesh = new Mesh(
                true,
                MAX_VERTICES_PER_CHUNK,
                MAX_INDICES_PER_CHUNK,
                VertexAttribute.Position(),
                VertexAttribute.Normal(),
                VertexAttribute.TexCoords(0)
        );
        material = new Material(
                new TextureAttribute(
                        TextureAttribute.Diffuse,
                        // TODO: This might be insufficient when the Tile atlas grows to have more than one texture
                        PocketWorld.assetManager.get(Assets.tileAtlas).getTextures().first()
                ),
                new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
        );
        needsRenderingUpdate = true;
    }


    private void updateVerticesIfDirty() {
        if (needsRenderingUpdate) {
            BasicVoxel[][][] voxels = chunk.voxels;
            vertexArray8f.clear();
            indicesArray.clear();
            for (int y = 0; y < CHUNK_DEPTH; y++) {
                for (int z = 0; z < CHUNK_SIZE; z++) {
                    for (int x = 0; x < CHUNK_SIZE; x++) {
                        BasicVoxel voxel = voxels[x][y][z];
                        if (voxel == null) {
                            continue;
                        }
                        if (voxel.isTransparent() || (y < CHUNK_DEPTH - 1 && (voxels[x][y + 1][z] == null || voxels[x][y + 1][z].isTransparent()))) {
                            voxel.createTopVertices(vertexArray8f);
                            indicesArray.addSquare();
                        }
                        if (voxel.isTransparent() || (y > 0 && (voxels[x][y - 1][z] == null || voxels[x][y - 1][z].isTransparent()))) {
                            voxel.createBottomVertices(vertexArray8f);
                            indicesArray.addSquare();
                        }
                        if (voxel.isTransparent() || (x > 0 && (voxels[x - 1][y][z] == null || voxels[x - 1][y][z].isTransparent()))) {
                            voxel.createLeftVertices(vertexArray8f);
                            indicesArray.addSquare();
                        }
                        if (voxel.isTransparent() || (x < CHUNK_SIZE - 1 && (voxels[x + 1][y][z] == null || voxels[x + 1][y][z].isTransparent()))) {
                            voxel.createRightVertices(vertexArray8f);
                            indicesArray.addSquare();
                        }
                        if (voxel.isTransparent() || (z > 0 && (voxels[x][y][z - 1] == null || voxels[x][y][z - 1].isTransparent()))) {
                            voxel.createFrontVertices(vertexArray8f);
                            indicesArray.addSquare();
                        }
                        if (voxel.isTransparent() || (z < CHUNK_SIZE - 1 && (voxels[x][y][z + 1] == null || voxels[x][y][z + 1].isTransparent()))) {
                            voxel.createBackVertices(vertexArray8f);
                            indicesArray.addSquare();
                        }
                    }
                }
            }
            mesh.setVertices(vertexArray8f.getItems(), 0, vertexArray8f.getItems().length);
            mesh.setIndices(indicesArray.getItems(), 0, indicesArray.getItems().length);
            needsRenderingUpdate = false;
            Gdx.app.log("Chunk",
                    "Amount vertices: " + vertexArray8f.getAmountVertices()
                            + ", amount attributes: " + vertexArray8f.getItems().length);
        }
    }

    @Override
    public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
        updateVerticesIfDirty();
        if (vertexArray8f.getAmountVertices() == 0) {
            return;
        }
        Renderable r = pool.obtain();
        r.material = material;
        r.meshPart.mesh = mesh;
        r.meshPart.offset = 0;
        r.meshPart.size = vertexArray8f.getItems().length / VERTEX_SIZE / POINTS_IN_A_SQUARE * FACES_IN_A_CUBE;
        r.meshPart.primitiveType = GL20.GL_TRIANGLES;
        renderables.add(r);
    }
}
