package vc.andro.poketest.util;

public final class ArrayUtil {
    private ArrayUtil() {
    }

    /**
     * Use this for converting a (x, y, z) position into a flattened 3D array index.
     * (This function assumes that the 3D array has width = height)
     */
    public static int xyzToI(int width, int depth, int x, int y, int z) {
        return x + width * (y + depth * z);
    }
}
