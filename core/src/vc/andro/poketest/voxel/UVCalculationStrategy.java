package vc.andro.poketest.voxel;

public interface UVCalculationStrategy {

    /**
     * This must be called after a voxel's texture region or position is updated.
     */
    default void refresh() {
    }

    float getU();

    float getV();

    float getU2();

    float getV2();
}
