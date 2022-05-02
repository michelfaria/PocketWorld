package vc.andro.poketest.voxel;

import vc.andro.poketest.Direction;

import static vc.andro.poketest.Direction.*;

public class VoxelAttributes {

    public static final int BYTE_SLOPE_FACING_DIRECTION = 0;
    public static final int BOOL_IS_INNER_CORNER_SLOPE = 0;

    private final boolean[] bools;
    private final byte[] bytes;

    @SuppressWarnings("ConstantConditions")
    public VoxelAttributes() {
        bools = new boolean[1];
        bytes = new byte[1];

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
    @SuppressWarnings("DuplicatedCode")
    public float getHeightInDirection(byte direction) {
        if (getSlopeFacingDirection() == Direction.NA) {
            return 1.0f;
        }

        boolean isInnerCorner = getIsInnerCornerSlope();

        switch (getSlopeFacingDirection()) {
            case NORTHWEST -> {
                if (isInnerCorner) {
                    switch (direction) {
                        case NORTHWEST -> {
                            return 0;
                        }
                        case NORTH, SOUTH, EAST, WEST -> {
                            return 0.5f;
                        }
                        case NORTHEAST, SOUTHEAST, SOUTHWEST -> {
                            return 1;
                        }
                        default -> throw new AssertionError();
                    }
                } else {
                    switch (direction) {
                        case NORTHWEST, NORTHEAST, SOUTHWEST -> {
                            return 0;
                        }
                        case WEST, NORTH, EAST, SOUTH -> {
                            return 0.5f;
                        }
                        case SOUTHEAST -> {
                            return 1;
                        }
                        default -> throw new AssertionError();
                    }
                }
            }
            case NORTHEAST -> {
                if (isInnerCorner) {
                    switch (direction) {
                        case NORTHEAST -> {
                            return 0;
                        }
                        case NORTH, SOUTH, EAST, WEST -> {
                            return 0.5f;
                        }
                        case NORTHWEST, SOUTHWEST, SOUTHEAST -> {
                            return 1;
                        }
                        default -> throw new AssertionError();
                    }
                } else {
                    switch (direction) {
                        case NORTHEAST, NORTHWEST, SOUTHEAST -> {
                            return 0;
                        }
                        case WEST, NORTH, EAST, SOUTH -> {
                            return 0.5f;
                        }
                        case SOUTHWEST -> {
                            return 1;
                        }
                        default -> throw new AssertionError();
                    }
                }
            }
            case SOUTHWEST -> {
                if (isInnerCorner) {
                    switch (direction) {
                        case SOUTHWEST -> {
                            return 0;
                        }
                        case NORTH, SOUTH, EAST, WEST -> {
                            return 0.5f;
                        }
                        case SOUTHEAST, NORTHEAST, NORTHWEST -> {
                            return 1;
                        }
                        default -> throw new AssertionError();
                    }
                } else {
                    switch (direction) {
                        case SOUTHWEST, SOUTHEAST, NORTHWEST -> {
                            return 0;
                        }
                        case WEST, NORTH, EAST, SOUTH -> {
                            return 0.5f;
                        }
                        case NORTHEAST -> {
                            return 1;
                        }
                        default -> throw new AssertionError();
                    }
                }
            }
            case SOUTHEAST -> {
                if (isInnerCorner) {
                    switch (direction) {
                        case SOUTHEAST -> {
                            return 0;
                        }
                        case NORTH, SOUTH, EAST, WEST -> {
                            return 0.5f;
                        }
                        case SOUTHWEST, NORTHWEST, NORTHEAST -> {
                            return 1;
                        }
                        default -> throw new AssertionError();
                    }
                } else {
                    switch (direction) {
                        case SOUTHEAST, SOUTHWEST, NORTHEAST -> {
                            return 0;
                        }
                        case WEST, NORTH, EAST, SOUTH -> {
                            return 0.5f;
                        }
                        case NORTHWEST -> {
                            return 1;
                        }
                        default -> throw new AssertionError();
                    }
                }
            }
            case NORTH -> {
                switch (direction) {
                    case NORTHWEST, NORTH, NORTHEAST -> {
                        return 0;
                    }
                    case WEST, EAST -> {
                        return 0.5f;
                    }
                    case SOUTHWEST, SOUTH, SOUTHEAST -> {
                        return 1;
                    }
                    default -> throw new AssertionError();
                }
            }
            case SOUTH -> {
                switch (direction) {
                    case SOUTHWEST, SOUTH, SOUTHEAST -> {
                        return 0;
                    }
                    case WEST, EAST -> {
                        return 0.5f;
                    }
                    case NORTHWEST, NORTH, NORTHEAST -> {
                        return 1;
                    }
                    default -> throw new AssertionError();
                }
            }
            case WEST -> {
                switch (direction) {
                    case NORTHWEST, WEST, SOUTHWEST -> {
                        return 0;
                    }
                    case NORTH, SOUTH -> {
                        return 0.5f;
                    }
                    case NORTHEAST, EAST, SOUTHEAST -> {
                        return 1;
                    }
                    default -> throw new AssertionError();
                }
            }
            case EAST -> {
                switch (direction) {
                    case NORTHEAST, EAST, SOUTHEAST -> {
                        return 0;
                    }
                    case NORTH, SOUTH -> {
                        return 0.5f;
                    }
                    case NORTHWEST, WEST, SOUTHWEST -> {
                        return 1;
                    }
                    default -> throw new AssertionError();
                }
            }
            default -> throw new AssertionError();
        }
    }
}
