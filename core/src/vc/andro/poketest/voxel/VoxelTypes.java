package vc.andro.poketest.voxel;

public class VoxelTypes {
    public static final VoxelType GRASS = new VoxelType("tile/bwgrass", "tile/bwwall", "tile/bwwall", "tile/bwwall", "tile/bwwall", "tile/bwwall");
    public static final VoxelType WATER = new VoxelType("tile/water");
    public static final VoxelType SAND = new VoxelType("tile/sand");
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

    public static final VoxelType[] VOXEL_TYPES = new VoxelType[]{
            /*   0 */ null,
            /*   1 */ GRASS,
            /*   2 */ WATER,
            /*   3 */ SAND,
            /*   4 */ NORTHWEST_CORNER,
            /*   5 */ NORTH_EDGE,
            /*   6 */ NORTHEAST_CORNER,
            /*   7 */ WEST_EDGE,
            /*   8 */ EAST_EDGE,
            /*   9 */ SOUTHWEST_CORNER,
            /*  10 */ SOUTH_EDGE,
            /*  11 */ SOUTHEAST_CORNER,
            /*  12 */ NORTHWEST_INNER_CORNER,
            /*  13 */ NORTHEAST_INNER_CORNER,
            /*  14 */ SOUTHWEST_INNER_CORNER,
            /*  15 */ SOUTHEAST_INNER_CORNER,
            /*  16 */ null,
            /*  17 */ null,
            /*  18 */ null,
            /*  19 */ null,
            /*  20 */ null,
            /*  21 */ null,
            /*  22 */ null,
            /*  23 */ null,
            /*  24 */ null,
            /*  25 */ null,
            /*  26 */ null,
            /*  27 */ null,
            /*  28 */ null,
            /*  29 */ null,
            /*  30 */ null,
            /*  31 */ null,
            /*  32 */ null,
            /*  33 */ null,
            /*  34 */ null,
            /*  35 */ null,
            /*  36 */ null,
            /*  37 */ null,
            /*  38 */ null,
            /*  39 */ null,
            /*  40 */ null,
            /*  41 */ null,
            /*  42 */ null,
            /*  43 */ null,
            /*  44 */ null,
            /*  45 */ null,
            /*  46 */ null,
            /*  47 */ null,
            /*  48 */ null,
            /*  49 */ null,
            /*  50 */ null,
            /*  51 */ null,
            /*  52 */ null,
            /*  53 */ null,
            /*  54 */ null,
            /*  55 */ null,
            /*  56 */ null,
            /*  57 */ null,
            /*  58 */ null,
            /*  59 */ null,
            /*  60 */ null,
            /*  61 */ null,
            /*  62 */ null,
            /*  63 */ null,
            /*  64 */ null,
            /*  65 */ null,
            /*  66 */ null,
            /*  67 */ null,
            /*  68 */ null,
            /*  69 */ null,
            /*  70 */ null,
            /*  71 */ null,
            /*  72 */ null,
            /*  73 */ null,
            /*  74 */ null,
            /*  75 */ null,
            /*  76 */ null,
            /*  77 */ null,
            /*  78 */ null,
            /*  79 */ null,
            /*  80 */ null,
            /*  81 */ null,
            /*  82 */ null,
            /*  83 */ null,
            /*  84 */ null,
            /*  85 */ null,
            /*  86 */ null,
            /*  87 */ null,
            /*  88 */ null,
            /*  89 */ null,
            /*  90 */ null,
            /*  91 */ null,
            /*  92 */ null,
            /*  93 */ null,
            /*  94 */ null,
            /*  95 */ null,
            /*  96 */ null,
            /*  97 */ null,
            /*  98 */ null,
            /*  99 */ null,
            /* 100 */ null,
            /* 101 */ null,
            /* 102 */ null,
            /* 103 */ null,
            /* 104 */ null,
            /* 105 */ null,
            /* 106 */ null,
            /* 107 */ null,
            /* 108 */ null,
            /* 109 */ null,
            /* 110 */ null,
            /* 111 */ null,
            /* 112 */ null,
            /* 113 */ null,
            /* 114 */ null,
            /* 115 */ null,
            /* 116 */ null,
            /* 117 */ null,
            /* 118 */ null,
            /* 119 */ null,
            /* 120 */ null,
            /* 121 */ null,
            /* 122 */ null,
            /* 123 */ null,
            /* 124 */ null,
            /* 125 */ null,
            /* 126 */ null,
            /* 127 */ null
    };
    static {
        //noinspection ConstantConditions
        assert VOXEL_TYPES.length == 128 : "Size of VOXEL_TYPES seems incorrect, should be 128 but is " + VOXEL_TYPES.length;
    }

    public static byte getId(VoxelType voxelType) {
        for (int i = 0; i < VOXEL_TYPES.length; i++) {
            VoxelType type = VOXEL_TYPES[i];
            if (type == voxelType) {
                return (byte)i;
            }
        }
        return -1;
    }
}
