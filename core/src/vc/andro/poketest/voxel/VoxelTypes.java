package vc.andro.poketest.voxel;

public class VoxelTypes {
    public static final VoxelType GRASS = new VoxelType(
            "tile/bwgrass",
            "tile/bwwall",
            "tile/bwwall",
            "tile/bwwall",
            "tile/bwwall",
            "tile/bwwall"
    );

    @Deprecated public static final VoxelType WATER = new VoxelType("tile/water");
    @Deprecated public static final VoxelType SAND = new VoxelType("tile/sand");
    @Deprecated public static final VoxelType NORTHWEST_CORNER = new VoxelType("tile/wall-top-left-corner", SlopeFaceGenerationStrategy.getInstance());
    @Deprecated public static final VoxelType NORTH_EDGE = new VoxelType("tile/wall-top-edge", SlopeFaceGenerationStrategy.getInstance());
    @Deprecated public static final VoxelType NORTHEAST_CORNER = new VoxelType("tile/wall-top-right-corner", SlopeFaceGenerationStrategy.getInstance());
    @Deprecated public static final VoxelType WEST_EDGE = new VoxelType("tile/wall-left-edge", SlopeFaceGenerationStrategy.getInstance());
    @Deprecated public static final VoxelType EAST_EDGE = new VoxelType("tile/wall-right-edge", SlopeFaceGenerationStrategy.getInstance());
    @Deprecated public static final VoxelType SOUTHWEST_CORNER = new VoxelType("tile/wall-bottom-left-corner", SlopeFaceGenerationStrategy.getInstance());
    @Deprecated public static final VoxelType SOUTH_EDGE = new VoxelType("tile/wall-bottom-edge", SlopeFaceGenerationStrategy.getInstance());
    @Deprecated public static final VoxelType SOUTHEAST_CORNER = new VoxelType("tile/wall-bottom-right-corner", SlopeFaceGenerationStrategy.getInstance());
    @Deprecated public static final VoxelType NORTHWEST_INNER_CORNER = new VoxelType("tile/wall-top-left-inner-corner", SlopeFaceGenerationStrategy.getInstance());
    @Deprecated public static final VoxelType NORTHEAST_INNER_CORNER = new VoxelType("tile/wall-top-right-inner-corner", SlopeFaceGenerationStrategy.getInstance());
    @Deprecated public static final VoxelType SOUTHWEST_INNER_CORNER = new VoxelType("tile/wall-bottom-left-inner-corner", SlopeFaceGenerationStrategy.getInstance());
    @Deprecated public static final VoxelType SOUTHEAST_INNER_CORNER = new VoxelType("tile/wall-bottom-right-inner-corner", SlopeFaceGenerationStrategy.getInstance());

    static {
        NORTHWEST_CORNER.setSlopeType(SlopeType.NORTHWEST_CORNER);
        NORTH_EDGE.setSlopeType(SlopeType.NORTH_EDGE);
        NORTHEAST_CORNER.setSlopeType(SlopeType.NORTHEAST_CORNER);
        WEST_EDGE.setSlopeType(SlopeType.WEST_EDGE);
        EAST_EDGE.setSlopeType(SlopeType.EAST_EDGE);
        SOUTHWEST_CORNER.setSlopeType(SlopeType.SOUTHWEST_CORNER);
        SOUTH_EDGE.setSlopeType(SlopeType.SOUTH_EDGE);
        SOUTHEAST_CORNER.setSlopeType(SlopeType.SOUTHEAST_CORNER);
        NORTHWEST_INNER_CORNER.setSlopeType(SlopeType.NORTHWEST_INNER_CORNER);
        NORTHEAST_INNER_CORNER.setSlopeType(SlopeType.NORTHEAST_INNER_CORNER);
        SOUTHWEST_INNER_CORNER.setSlopeType(SlopeType.SOUTHWEST_INNER_CORNER);
        SOUTHEAST_INNER_CORNER.setSlopeType(SlopeType.SOUTHEAST_INNER_CORNER);

    }
}
