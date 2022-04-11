package vc.andro.poketest.tile;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.jetbrains.annotations.Nullable;
import vc.andro.poketest.world.World;

public class WallTile extends BasicTile {

    private WallType wallType;

    private @Nullable
    BasicTile neighborTile = null;

    public WallTile(World world, int x, int y, float altitude, WallType wallType) {
        super(world, TileType.WALL, x, y, altitude);
        updateWallType(wallType);
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        if (neighborTile != null) {
            neighborTile.draw(spriteBatch, x, y);
        }
        super.draw(spriteBatch);
    }

    public void updateWallType(WallType newType) {
        spriteId = newType.getSpriteId();
        wallType = newType;
    }

    @Override
    public void receiveTileUpdate(BasicTile updateOrigin) {
        super.receiveTileUpdate(updateOrigin);

        if (!updateOrigin.type.isFloorTile) {
            return;
        }

        int dirX = x - updateOrigin.x;
        int dirY = y - updateOrigin.y;
        boolean acceptNeighbor;

        switch (wallType) {
            case TOP_LEFT_CORNER -> acceptNeighbor = dirX > 0 || dirY < 0;
            case TOP_EDGE -> acceptNeighbor = dirX == 0 && dirY < 0;
            case TOP_RIGHT_CORNER -> acceptNeighbor = dirX < 0 || dirY < 0 ;
            case LEFT_EDGE -> acceptNeighbor = dirX > 0 && dirY == 0;
            case RIGHT_EDGE -> acceptNeighbor = dirX < 0 && dirY == 0;
            case BOTTOM_LEFT_CORNER -> acceptNeighbor = dirX > 0 || dirY > 0;
            case BOTTOM_EDGE -> acceptNeighbor = dirX == 0 && dirY > 0;
            case BOTTOM_RIGHT_CORNER -> acceptNeighbor = dirX < 0 || dirY > 0;
            case TOP_LEFT_INNER_CORNER -> acceptNeighbor = dirX > 0 && dirY < 0;
            case TOP_RIGHT_INNER_CORNER -> acceptNeighbor = dirX < 0 && dirY < 0;
            case BOTTOM_LEFT_INNER_CORNER -> acceptNeighbor = dirX > 0 && dirY > 0;
            case BOTTOM_RIGHT_INNER_CORNER -> acceptNeighbor = dirX < 0 && dirY > 0;
            default -> throw new AssertionError();
        }

        if (acceptNeighbor) {
            neighborTile = updateOrigin;
        }
    }
}
