package vc.andro.poketest.voxel;

import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

public final class VoxelPools {

    private VoxelPools() {
    }

    public static <T extends Voxel> T obtain(Class<T> voxelClass) {
        Pool<T> pool = Pools.get(voxelClass);
        return pool.obtain();
    }

    private static final Pool<Voxel> BASE_VOXEL_POOL = Pools.get(Voxel.class);
    private static final Pool<SlopeVoxel> SLOPE_VOXEL_POOL = Pools.get(SlopeVoxel.class);

    public static <T extends Voxel> void free(T voxel) {
        if (voxel instanceof SlopeVoxel v) {
            SLOPE_VOXEL_POOL.free(v);
        } else {
            BASE_VOXEL_POOL.free(voxel);
        }
    }
}
