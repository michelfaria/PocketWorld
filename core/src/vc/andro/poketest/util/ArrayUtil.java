package vc.andro.poketest.util;

public final class ArrayUtil {
    private ArrayUtil() {
    }

    /**
     * Converts a (x,y,z) position into an index for a 3D array flattened into an 1D array.
     *
     * @apiNote This function assumes that the 3D array has width == height.
     */
    public static int xyzToI(int width, int depth, int x, int y, int z) {
        return x + width * (y + depth * z);
    }
}
