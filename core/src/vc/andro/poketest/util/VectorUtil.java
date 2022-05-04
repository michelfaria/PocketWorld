package vc.andro.poketest.util;

import com.badlogic.gdx.math.Vector3;

public final class VectorUtil {
    private VectorUtil() {
    }

    public static Vector3 round(Vector3 v ) {
        v.set(Math.round(v.x), Math.round(v.y), Math.round(v.z));
        return v;
    }
}
