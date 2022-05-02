package vc.andro.poketest.graphics.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vc.andro.poketest.entity.Entity;
import vc.andro.poketest.entity.Player;
import vc.andro.poketest.graphics.MyCameraGroupStrategy;
import vc.andro.poketest.world.World;

import static vc.andro.poketest.world.World.CxWx;
import static vc.andro.poketest.world.World.CzWz;
import static vc.andro.poketest.world.chunk.Chunk.CHUNK_DEPTH;
import static vc.andro.poketest.world.chunk.Chunk.CHUNK_SIZE;

public class PocketCamera extends InputAdapter {

    private final PerspectiveCamera underlying;
    private final World             world;

    private final CameraStrategy cameraStrategy;

    public PocketCamera(World world, CameraStrategy cameraStrategy) {
        this.world = world;
        this.cameraStrategy = cameraStrategy;
        underlying = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        underlying.near = 0.5f;
        underlying.far = 3000f;
        cameraStrategy.init(this);
    }

    public void update() {
        cameraStrategy.updateCamera(this);
        underlying.update();
    }

    public void resize(int width, int height) {
        underlying.viewportWidth = width;
        underlying.viewportHeight = height;
        underlying.update();
    }

    public Vector3 getPosition() {
        return underlying.position;
    }

    public void setPosition(float x, float y, float z) {
        underlying.position.set(x, y, z);
    }

    public Vector3 getDirection() {
        return underlying.direction;
    }

    public Vector3 getUp() {
        return underlying.up;
    }

    public boolean isChunkVisible(int cx, int cz) {
        return underlying.frustum.boundsInFrustum(
                CxWx(cx) + CHUNK_SIZE / 2.0f,
                CHUNK_DEPTH / 2.0f,
                CzWz(cz) + CHUNK_SIZE / 2.0f,
                CHUNK_SIZE / 2.0f,
                CHUNK_DEPTH / 2.0f,
                CHUNK_SIZE / 2.0f
        );
    }

    public boolean isVisible(Entity entity) {
        return underlying.frustum.boundsInFrustum(
                entity.getWx() + entity.dimensions.x / 2.0f,
                entity.getWy() + entity.dimensions.y / 2.0f,
                entity.getWz() + entity.dimensions.z / 2.0f,
                entity.dimensions.x / 2.0f,
                entity.dimensions.y / 2.0f,
                entity.dimensions.z / 2.0f
        );
    }

    public MyCameraGroupStrategy createCameraGroupStrategy() {
        return new MyCameraGroupStrategy(this.underlying);
    }


    public World getWorld() {
        return world;
    }

    public void setFollowPlayer(@Nullable Player player) {
        if (cameraStrategy instanceof PlayerCameraStrategy s) {
            s.setFollowPlayer(player);
        }
    }

    public void beginModelBatch(@NotNull ModelBatch modelBatch) {
        modelBatch.begin(this.underlying);
    }

    /** Recalculates the direction of the camera to look at the point (x, y, z). This function assumes the up vector is normalized.
     * @param x the x-coordinate of the point to look at
     * @param y the y-coordinate of the point to look at
     * @param z the z-coordinate of the point to look at  */
    public void lookAt(float x, float y, float z) {
        underlying.lookAt(x, y, z);
    }

    /** Recalculates the direction of the camera to look at the point (x, y, z).
     * @param target the point to look at */
    public void lookAt(Vector3 target) {
        underlying.lookAt(target);
    }

    /** Normalizes the up vector by first calculating the right vector via a cross product between direction and up, and then
     * recalculating the up vector via a cross product between right and direction. */
    public void normalizeUp() {
        underlying.normalizeUp();
    }

    /** Rotates the direction and up vector of this camera by the given angle around the given axis. The direction and up vector
     * will not be orthogonalized.
     *  @param angle the angle
     * @param axisX the x-component of the axis
     * @param axisY the y-component of the axis
     * @param axisZ the z-component of the axis   */
    public void rotate(float angle, float axisX, float axisY, float axisZ) {
        underlying.rotate(angle, axisX, axisY, axisZ);
    }

    /** Rotates the direction and up vector of this camera by the given angle around the given axis. The direction and up vector
     * will not be orthogonalized.
     *  @param axis the axis to rotate around
     * @param angle the angle, in degrees */
    public void rotate(Vector3 axis, float angle) {
        underlying.rotate(axis, angle);
    }

    /** Rotates the direction and up vector of this camera by the given rotation matrix. The direction and up vector will not be
     * orthogonalized.
     *
     * @param transform The rotation matrix */
    public void rotate(Matrix4 transform) {
        underlying.rotate(transform);
    }

    /** Rotates the direction and up vector of this camera by the given {@link Quaternion}. The direction and up vector will not be
     * orthogonalized.
     *
     * @param quat The quaternion */
    public void rotate(Quaternion quat) {
        underlying.rotate(quat);
    }

    /** Rotates the direction and up vector of this camera by the given angle around the given axis, with the axis attached to
     * given point. The direction and up vector will not be orthogonalized.
     *  @param point the point to attach the axis to
     * @param axis the axis to rotate around
     * @param angle the angle, in degrees  */
    public void rotateAround(Vector3 point, Vector3 axis, float angle) {
        underlying.rotateAround(point, axis, angle);
    }

    /** Transform the position, direction and up vector by the given matrix
     *
     * @param transform The transform matrix */
    public void transform(Matrix4 transform) {
        underlying.transform(transform);
    }

    /** Moves the camera by the given amount on each axis.
     * @param x the displacement on the x-axis
     * @param y the displacement on the y-axis
     * @param z the displacement on the z-axis  */
    public void translate(float x, float y, float z) {
        underlying.translate(x, y, z);
    }

    /** Moves the camera by the given vector.
     * @param vec the displacement vector */
    public void translate(Vector3 vec) {
        underlying.translate(vec);
    }

    /** Function to translate a point given in screen coordinates to world space. It's the same as GLU gluUnProject, but does not
     * rely on OpenGL. The x- and y-coordinate of vec are assumed to be in screen coordinates (origin is the top left corner, y
     * pointing down, x pointing to the right) as reported by the touch methods in {@link Input}. A z-coordinate of 0 will return a
     * point on the near plane, a z-coordinate of 1 will return a point on the far plane. This method allows you to specify the
     * viewport position and dimensions in the coordinate system expected by {@link GL20#glViewport(int, int, int, int)}, with the
     * origin in the bottom left corner of the screen.
     * @param screenCoords the point in screen coordinates (origin top left)
     * @param viewportX the coordinate of the bottom left corner of the viewport in glViewport coordinates.
     * @param viewportY the coordinate of the bottom left corner of the viewport in glViewport coordinates.
     * @param viewportWidth the width of the viewport in pixels
     * @param viewportHeight the height of the viewport in pixels
     * @return the mutated and unprojected screenCoords {@link Vector3} */
    public Vector3 unproject(Vector3 screenCoords, float viewportX, float viewportY, float viewportWidth, float viewportHeight) {
        return underlying.unproject(screenCoords, viewportX, viewportY, viewportWidth, viewportHeight);
    }

    /** Function to translate a point given in screen coordinates to world space. It's the same as GLU gluUnProject but does not
     * rely on OpenGL. The viewport is assumed to span the whole screen and is fetched from {@link Graphics#getWidth()} and
     * {@link Graphics#getHeight()}. The x- and y-coordinate of vec are assumed to be in screen coordinates (origin is the top left
     * corner, y pointing down, x pointing to the right) as reported by the touch methods in {@link Input}. A z-coordinate of 0
     * will return a point on the near plane, a z-coordinate of 1 will return a point on the far plane.
     * @param screenCoords the point in screen coordinates
     * @return the mutated and unprojected screenCoords {@link Vector3} */
    public Vector3 unproject(Vector3 screenCoords) {
        return underlying.unproject(screenCoords);
    }

    /** Projects the {@link Vector3} given in world space to screen coordinates. It's the same as GLU gluProject with one small
     * deviation: The viewport is assumed to span the whole screen. The screen coordinate system has its origin in the
     * <b>bottom</b> left, with the y-axis pointing <b>upwards</b> and the x-axis pointing to the right. This makes it easily
     * useable in conjunction with {@link Batch} and similar classes.
     * @return the mutated and projected worldCoords {@link Vector3}
     * @param worldCoords*/
    public Vector3 project(Vector3 worldCoords) {
        return underlying.project(worldCoords);
    }

    /** Projects the {@link Vector3} given in world space to screen coordinates. It's the same as GLU gluProject with one small
     * deviation: The viewport is assumed to span the whole screen. The screen coordinate system has its origin in the
     * <b>bottom</b> left, with the y-axis pointing <b>upwards</b> and the x-axis pointing to the right. This makes it easily
     * useable in conjunction with {@link Batch} and similar classes. This method allows you to specify the viewport position and
     * dimensions in the coordinate system expected by {@link GL20#glViewport(int, int, int, int)}, with the origin in the bottom
     * left corner of the screen.
     *
     * @param worldCoords
     * @param viewportX the coordinate of the bottom left corner of the viewport in glViewport coordinates.
     * @param viewportY the coordinate of the bottom left corner of the viewport in glViewport coordinates.
     * @param viewportWidth the width of the viewport in pixels
     * @param viewportHeight the height of the viewport in pixels
     * @return the mutated and projected worldCoords {@link Vector3} */
    public Vector3 project(Vector3 worldCoords, float viewportX, float viewportY, float viewportWidth, float viewportHeight) {
        return underlying.project(worldCoords, viewportX, viewportY, viewportWidth, viewportHeight);
    }

    /** Creates a picking {@link Ray} from the coordinates given in screen coordinates. It is assumed that the viewport spans the
     * whole screen. The screen coordinates origin is assumed to be in the top left corner, its y-axis pointing down, the x-axis
     * pointing to the right. The returned instance is not a new instance but an internal member only accessible via this function.
     *
     * @param screenX
     * @param screenY
     * @param viewportX the coordinate of the bottom left corner of the viewport in glViewport coordinates.
     * @param viewportY the coordinate of the bottom left corner of the viewport in glViewport coordinates.
     * @param viewportWidth the width of the viewport in pixels
     * @param viewportHeight the height of the viewport in pixels
     * @return the picking Ray. */
    public Ray getPickRay(float screenX, float screenY, float viewportX, float viewportY, float viewportWidth, float viewportHeight) {
        return underlying.getPickRay(screenX, screenY, viewportX, viewportY, viewportWidth, viewportHeight);
    }

    /** Creates a picking {@link Ray} from the coordinates given in screen coordinates. It is assumed that the viewport spans the
     * whole screen. The screen coordinates origin is assumed to be in the top left corner, its y-axis pointing down, the x-axis
     * pointing to the right. The returned instance is not a new instance but an internal member only accessible via this function.
     * @return the picking Ray.
     * @param screenX
     * @param screenY*/
    public Ray getPickRay(float screenX, float screenY) {
        return underlying.getPickRay(screenX, screenY);
    }
}
