package vc.andro.poketest.tile;

public class WallTile extends BasicTile {

    public WallTile(WallType wallType) {
        super(TileType.WALL);
        updateWallType(wallType);
        transparent = true;
    }

    public void updateWallType(WallType newType) {
        setSprite(newType.getSpriteId());
    }
}
