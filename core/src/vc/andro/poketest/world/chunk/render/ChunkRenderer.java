package vc.andro.poketest.world.chunk.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import vc.andro.poketest.Assets;
import vc.andro.poketest.PocketWorld;
import vc.andro.poketest.util.IndexArray;
import vc.andro.poketest.util.VertexArray;
import vc.andro.poketest.voxel.VoxelAttributes;
import vc.andro.poketest.voxel.Voxel;
import vc.andro.poketest.voxel.Voxels;
import vc.andro.poketest.world.chunk.Chunk;

import java.util.function.Consumer;

import static vc.andro.poketest.util.VertexArray.VERTEX_SIZE;
import static vc.andro.poketest.world.World.LxWx;
import static vc.andro.poketest.world.World.LzWz;
import static vc.andro.poketest.world.chunk.Chunk.CHUNK_DEPTH;
import static vc.andro.poketest.world.chunk.Chunk.CHUNK_SIZE;

public class ChunkRenderer implements RenderableProvider, Disposable {

    public static final int VOXELS_PER_CHUNK       = CHUNK_SIZE * CHUNK_DEPTH * CHUNK_SIZE;
    public static final int FACES_IN_A_CUBE        = 6;
    public static final int POINTS_IN_A_SQUARE     = 4;
    public static final int SIDES_IN_A_TRIANGLE    = 3;
    public static final int MAX_VERTICES_PER_CHUNK = VOXELS_PER_CHUNK * FACES_IN_A_CUBE * POINTS_IN_A_SQUARE;
    public static final int MAX_INDICES_PER_CHUNK  = VOXELS_PER_CHUNK * (int) Math.pow(FACES_IN_A_CUBE, 2) / SIDES_IN_A_TRIANGLE;

    private final Chunk chunk;

    private final Mesh     mesh;
    private final Material material;
    private       int      cachedVertexCount;

    public ChunkRenderer(Chunk chunk) {
        this.chunk = chunk;
        mesh = new Mesh(
                true,
                MAX_VERTICES_PER_CHUNK,
                MAX_INDICES_PER_CHUNK,
                VertexAttribute.Position(),
                VertexAttribute.Normal(),
                VertexAttribute.TexCoords(0));
        material = new Material(
                new TextureAttribute(
                        TextureAttribute.Diffuse,
                        PocketWorld.getAssetManager().get(Assets.tileAtlas).getTextures().first()),
                new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA),
                new FloatAttribute(FloatAttribute.AlphaTest, 0.0f));
    }

    private void rerenderIfNeeded(Consumer<Integer> consumer) {
        if (chunk.needsRenderingUpdate()) {
            var vertexArray8f = new VertexArray();
            var indicesArray = new IndexArray();
            for (int ly = 0; ly < CHUNK_DEPTH; ly++) {
                for (int lz = 0; lz < CHUNK_SIZE; lz++) {
                    for (int lx = 0; lx < CHUNK_SIZE; lx++) {
                        Voxel voxel = chunk.getVoxelAt_LP(lx, ly, lz);
                        if (voxel == Voxels.AIR) {
                            continue;
                        }
                        int wx = LxWx(chunk.getCx(), lx);
                        int wz = LzWz(chunk.getCz(), lz);
                        VoxelRenderer renderStrat = voxel.getVoxelRenderer();
                        if (renderStrat != null) {
                            VoxelAttributes attrs = chunk.getVoxelAttrsAt_LP(lx, ly, lz);
                            renderStrat.render(chunk, voxel, lx, ly, lz, wx, wz, vertexArray8f, indicesArray, attrs);
                        }
                    }
                }
            }
            this.mesh.setVertices(vertexArray8f.getItems(), 0, vertexArray8f.getItems().length);
            this.mesh.setIndices(indicesArray.getItems(), 0, indicesArray.getItems().length);
            this.chunk.setNeedsRenderingUpdate(false);
            Gdx.app.log("Chunk", "Amount vertices: " + vertexArray8f.getAmountVertices()
                    + ", amount attributes: " + vertexArray8f.getItems().length);

            this.cachedVertexCount = vertexArray8f.getItems().length;
        }

        consumer.accept(cachedVertexCount);
    }

    @Override
    public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
        rerenderIfNeeded((vertexCount) -> {
            if (vertexCount == 0) {
                return;
            }
            Renderable r = pool.obtain();
            r.material = material;
            r.meshPart.mesh = mesh;
            r.meshPart.offset = 0;
            r.meshPart.size = vertexCount / VERTEX_SIZE / POINTS_IN_A_SQUARE * FACES_IN_A_CUBE;
            r.meshPart.primitiveType = GL20.GL_TRIANGLES;
            renderables.add(r);
        });
    }

    @Override
    public void dispose() {
        mesh.dispose();
    }
}
