package vc.andro.poketest.voxel;

import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

public final class VoxelPool {

    private static final Pool<Voxel> VOXEL_POOL = Pools.get(Voxel.class);

    private VoxelPool() {
    }

    public static Voxel obtain() {
        return VOXEL_POOL.obtain();
    }

    public static void free(Voxel voxel) {
        VOXEL_POOL.free(voxel);
    }
}
