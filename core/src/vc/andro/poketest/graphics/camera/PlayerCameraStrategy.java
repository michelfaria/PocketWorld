package vc.andro.poketest.graphics.camera;

import com.badlogic.gdx.math.MathUtils;
import org.jetbrains.annotations.Nullable;
import vc.andro.poketest.entity.Player;

public class PlayerCameraStrategy implements CameraStrategy {

    private static final float DISTANCE_FROM_PLAYER = 5.0f;
    private static final float ANGLE_FROM_PLAYER    = 45.0f;

    private @Nullable Player  followPlayer;

    public PlayerCameraStrategy(@Nullable Player followPlayer) {
        this.followPlayer = followPlayer;
    }

    public void setFollowPlayer(@Nullable Player followPlayer) {
        this.followPlayer = followPlayer;
    }

    @Override
    public void init(PocketCamera camera) {
    }

    @Override
    public void updateCamera(PocketCamera camera) {
        if (followPlayer == null) {
            return;
        }
        camera.getPosition()
                .set(followPlayer.getPositionWpCopy())
                .add(0.5f, 0.0f, 0.5f)
                .sub(followPlayer.getForwardCopy().scl(DISTANCE_FROM_PLAYER))
                .add(0.0f, (float) Math.tan(ANGLE_FROM_PLAYER * MathUtils.degreesToRadians) * DISTANCE_FROM_PLAYER, 0.0f);
        camera.lookAt(followPlayer.getPositionWpCopy().add(0.5f, 0.0f, 0.5f));
        camera.getWorld().setViewpoint(camera.getPosition());
    }
}
