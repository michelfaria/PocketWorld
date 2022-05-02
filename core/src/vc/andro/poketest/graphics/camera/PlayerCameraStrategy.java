package vc.andro.poketest.graphics.camera;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import org.jetbrains.annotations.Nullable;
import vc.andro.poketest.entity.Player;

public class PlayerCameraStrategy implements CameraStrategy {

    private static final float DISTANCE_FROM_PLAYER = 5.0f;
    private static final float ANGLE_FROM_PLAYER    = 45.0f;

    private @Nullable Player  followPlayer;
    private final     Vector3 tmp = new Vector3();

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

        Vector3 camPos = camera.getPosition();
        followPlayer.getPositionWp_out(camPos);
        camPos.add(0.5f, 0.0f, 0.5f);
        camPos.sub(tmp.set(followPlayer.getForward()).scl(DISTANCE_FROM_PLAYER));
        camPos.add(0.0f, (float) Math.tan(ANGLE_FROM_PLAYER * MathUtils.degreesToRadians) * DISTANCE_FROM_PLAYER, 0.0f);

        followPlayer.getPositionWp_out(tmp);
        tmp.add(0.5f, 0.0f, 0.5f);
        camera.lookAt(tmp);

        camera.getWorld().setViewpoint(camera.getPosition());
    }
}
