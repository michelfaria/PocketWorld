package vc.andro.poketest.voxel;

public final class NullUVCalculationStrategy implements UVCalculationStrategy {

    private static NullUVCalculationStrategy INSTANCE;

    private NullUVCalculationStrategy() {
    }

    public static synchronized NullUVCalculationStrategy getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NullUVCalculationStrategy();
        }
        return INSTANCE;
    }

    @Override
    public float getU() {
        return 0;
    }

    @Override
    public float getV() {
        return 0;
    }

    @Override
    public float getU2() {
        return 0;
    }

    @Override
    public float getV2() {
        return 0;
    }
}
