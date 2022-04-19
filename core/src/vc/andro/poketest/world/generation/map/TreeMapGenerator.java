package vc.andro.poketest.world.generation.map;

import vc.andro.poketest.world.generation.NoiseGenerator;
import vc.andro.poketest.world.WorldCreationParams;

public class TreeMapGenerator extends VegetationMapGenerator {

    private final WorldCreationParams creationParams;

    public TreeMapGenerator(NoiseGenerator noiseGenerator, WorldCreationParams creationParams) {
        super(noiseGenerator);
        this.creationParams = creationParams;
    }

    @Override
    public int getRValue() {
        return creationParams.treeMapRValue;
    }
}
