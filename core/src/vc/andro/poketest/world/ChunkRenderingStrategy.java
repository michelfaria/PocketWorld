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
import vc.andro.poketest.voxel.Voxel;

import static vc.andro.poketest.world.Chunk.CHUNK_DEPTH;
import static vc.andro.poketest.world.Chunk.CHUNK_SIZE;
import static vc.andro.poketest.world.VertexArray.VERTEX_SIZE;
import static vc.andro.poketest.world.World.LxWx;
import static vc.andro.poketest.world.World.LzWz;

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
            Voxel[][][] voxels = chunk.voxels;
            vertexArray8f.clear();
            indicesArray.clear();
            for (int wy = 0; wy < CHUNK_DEPTH; wy++) {
                for (int lz = 0; lz < CHUNK_SIZE; lz++) {
                    for (int lx = 0; lx < CHUNK_SIZE; lx++) {
                        Voxel voxel = voxels[lx][wy][lz];
                        if (voxel == null) {
                            continue;
                        }

                        Voxel voxelAbove = wy < CHUNK_DEPTH - 1 ? voxels[lx][wy + 1][lz] : null;
                        Voxel voxelUnder = wy > 0 ? voxels[lx][wy - 1][lz] : null;
                        Voxel voxelEast = chunk.world.getTileAt_WP(LxWx(chunk.cx, lx) + 1, wy, LzWz(chunk.cz, lz));
                        Voxel voxelWest = chunk.world.getTileAt_WP(LxWx(chunk.cx, lx) - 1, wy, LzWz(chunk.cz, lz));
                        Voxel voxelNorth = chunk.world.getTileAt_WP(LxWx(chunk.cx, lx), wy, LzWz(chunk.cz, lz) - 1);
                        Voxel voxelSouth = chunk.world.getTileAt_WP(LxWx(chunk.cx, lx), wy, LzWz(chunk.cz, lz) + 1);

                        if (voxelAbove == null || voxelAbove.isTransparent()) {
                            voxel.createTopVertices(vertexArray8f);
                            indicesArray.addSquare();
                        }
                        if (voxelUnder == null || voxelUnder.isTransparent()) {
                            voxel.createBottomVertices(vertexArray8f);
                            indicesArray.addSquare();
                        }
                        if (voxelWest == null || voxelWest.isTransparent()) {
                            voxel.createWestVertices(vertexArray8f);
                            indicesArray.addSquare();
                        }
                        if (voxelEast == null || voxelEast.isTransparent()) {
                            voxel.createEastVertices(vertexArray8f);
                            indicesArray.addSquare();
                        }
                        if (voxelNorth == null || voxelNorth.isTransparent()) {
                            voxel.createNorthVertices(vertexArray8f);
                            indicesArray.addSquare();
                        }
                        if (voxelSouth == null || voxelSouth.isTransparent()) {
                            voxel.createSouthVertices(vertexArray8f);
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
