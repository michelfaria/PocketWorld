package vc.andro.poketest.voxel;

import vc.andro.poketest.world.VertexArray;

public interface FaceGenerationStrategy {
    void createTopVertices(Voxel v, VertexArray vertices);

    void createBottomVertices(Voxel v, VertexArray vertices);

    void createNorthVertices(Voxel v, VertexArray vertices);

    void createSouthVertices(Voxel v, VertexArray vertices);

    void createWestVertices(Voxel v, VertexArray vertices);

    void createEastVertices(Voxel v, VertexArray vertices);
}
