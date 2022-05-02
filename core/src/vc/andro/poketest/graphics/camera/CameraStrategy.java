package vc.andro.poketest.graphics.camera;

public interface CameraStrategy {

    default void init(PocketCamera camera) {
    }

    default void updateCamera(PocketCamera camera) {
    };
}
