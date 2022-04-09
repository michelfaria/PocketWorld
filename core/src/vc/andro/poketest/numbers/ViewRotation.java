package vc.andro.poketest.numbers;

public class ViewRotation {
    private int rotation = 0;

    public float rotate90CW() {
        rotation = (rotation + 90) % 360;
        return rotation;
    }

    public float rotate90CCW() {
        if (rotation == 0) {
            rotation = 270;
        } else {
            rotation = rotation - 90;
        }
        return rotation;
    }

    public float getValue() {
        return rotation;
    }
}
