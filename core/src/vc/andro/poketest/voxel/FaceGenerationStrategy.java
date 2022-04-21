package vc.andro.poketest.voxel;

import vc.andro.poketest.world.VertexArray;

public interface FaceGenerationStrategy {
    void createTopVertices(VertexArray vertices, byte voxel, int wx, int wy, int wz);

    void createBottomVertices(VertexArray vertices, byte voxel, int wx, int wy, int wz);

    void createNorthVertices(VertexArray vertices, byte voxel, int wx, int wy, int wz);

    void createSouthVertices(VertexArray vertices, byte voxel, int wx, int wy, int wz);

    void createWestVertices(VertexArray vertices, byte voxel, int wx, int wy, int wz);

    void createEastVertices(VertexArray vertices, byte voxel, int wx, int wy, int wz);
}
