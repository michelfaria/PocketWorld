package vc.andro.poketest.voxel;

import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import vc.andro.poketest.Direction;

public class VoxelAttributes implements Pool.Poolable {

    public static final Pool<VoxelAttributes> POOL = Pools.get(VoxelAttributes.class);

    public static final int BYTE_SLOPE_FACING_DIRECTION = 0;
    public static final int BOOL_IS_INNER_CORNER_SLOPE = 0;

    private final boolean[] bools;
    private final byte[] bytes;

    private VoxelAttributes() {
        bools = new boolean[1];
        bytes = new byte[1];

        reset();
    }

    @Override
    public void reset() {
        bools[BOOL_IS_INNER_CORNER_SLOPE] = false;
        bytes[BYTE_SLOPE_FACING_DIRECTION] = Direction.NA;
    }

    public void configureSlope(byte facingDirection, boolean isInnerCorner) {
        if (isInnerCorner
                && facingDirection != Direction.NORTHWEST
                && facingDirection != Direction.NORTHEAST
                && facingDirection != Direction.SOUTHWEST
                && facingDirection != Direction.SOUTHEAST
        ) {
            throw new IllegalArgumentException("An inner corner tile must face an intercardinal direction");
        }
        //noinspection ConstantConditions
        assert !isInnerCorner
                || (facingDirection != Direction.NORTH && facingDirection != Direction.WEST
                && facingDirection != Direction.SOUTH && facingDirection != Direction.EAST)
                : "A non-corner slope cannot be an inner corner";
        setSlopeFacingDirection(facingDirection);
        setBoolIsInnerCornerSlope(isInnerCorner);
    }

    public byte getSlopeFacingDirection() {
        return bytes[BYTE_SLOPE_FACING_DIRECTION];
    }

    public boolean getIsInnerCornerSlope() {
        return bools[BOOL_IS_INNER_CORNER_SLOPE];
    }

    // This method can break encapsulation
    @Deprecated
    public void setBool(int variable, boolean value) {
        bools[variable] = value;
    }

    // This method can break encapsulation
    @Deprecated
    public void setByte(int variable, byte byte_) {
        bytes[variable] = byte_;
    }

    private void setSlopeFacingDirection(byte value) {
        bytes[BYTE_SLOPE_FACING_DIRECTION] = value;
    }

    private void setBoolIsInnerCornerSlope(boolean value) {
        bools[BOOL_IS_INNER_CORNER_SLOPE] = value;
    }

    public boolean isSlope() {
        return bytes[BYTE_SLOPE_FACING_DIRECTION] != Direction.NA;
    }
}
