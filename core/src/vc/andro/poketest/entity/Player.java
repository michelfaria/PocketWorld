package vc.andro.poketest.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.jetbrains.annotations.NotNull;
import vc.andro.poketest.Assets;
import vc.andro.poketest.Direction;
import vc.andro.poketest.PokeTest;
import vc.andro.poketest.util.AtlasUtil;

public class Player extends Entity {

    private static final float WALK_ANIMATION_FRAME_DURATION = 0.10f;

    private final TextureRegion idleUp;
    private final TextureRegion idleDown;
    private final TextureRegion idleLeft;
    private final TextureRegion idleRight;

    private final Animation<TextureRegion> walkLeftAnimation;
    private final Animation<TextureRegion> walkUpAnimation;
    private final Animation<TextureRegion> walkDownAnimation;
    private final Animation<TextureRegion> walkRightAnimation;

    private boolean playerMoving;
    private @NotNull
    Direction direction;
    private TextureRegion currentTexture;
    private float animationTime;

    public Player() {
        super("entity/player/idle-down");
        direction = Direction.SOUTH;

        TextureAtlas textureAtlas = PokeTest.assetManager.get(Assets.entityAtlas);

        idleUp = AtlasUtil.findRegion(textureAtlas, "entity/player/idle-up");
        idleDown = AtlasUtil.findRegion(textureAtlas, "entity/player/idle-down");
        idleLeft = AtlasUtil.findRegion(textureAtlas, "entity/player/idle-left");
        idleRight = AtlasUtil.findRegion(textureAtlas, "entity/player/idle-right");

        walkLeftAnimation = new Animation<>(
                WALK_ANIMATION_FRAME_DURATION,
                AtlasUtil.findRegions(textureAtlas, "entity/player/walk-left"),
                Animation.PlayMode.LOOP_PINGPONG);
        walkUpAnimation = new Animation<>(
                WALK_ANIMATION_FRAME_DURATION,
                AtlasUtil.findRegions(textureAtlas, "entity/player/walk-up"),
                Animation.PlayMode.LOOP_PINGPONG);
        walkDownAnimation = new Animation<>(
                WALK_ANIMATION_FRAME_DURATION,
                AtlasUtil.findRegions(textureAtlas, "entity/player/walk-down"),
                Animation.PlayMode.LOOP_PINGPONG);
        walkRightAnimation = new Animation<>(
                WALK_ANIMATION_FRAME_DURATION,
                AtlasUtil.findRegions(textureAtlas, "entity/player/walk-right"),
                Animation.PlayMode.LOOP_PINGPONG);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        updateMovement();
        updateSprite(delta);
    }

    private void updateSprite(float delta) {
        Animation<TextureRegion> currentAnimation;
        if (playerMoving) {
            switch (direction) {
                case NORTH -> currentAnimation = walkUpAnimation;
                case WEST -> currentAnimation = walkLeftAnimation;
                case EAST -> currentAnimation = walkRightAnimation;
                case SOUTH -> currentAnimation = walkDownAnimation;
                default -> throw new AssertionError();
            }
        } else {
            currentAnimation = null;
        }

        if (currentAnimation != null) {
            currentTexture = currentAnimation.getKeyFrame(animationTime);
            animationTime += delta;
            return;
        }

        switch (direction) {
            case NORTH -> currentTexture = idleUp;
            case WEST -> currentTexture = idleLeft;
            case EAST -> currentTexture = idleRight;
            case SOUTH -> currentTexture = idleDown;
        }
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        assert currentTexture != null : "no texture set for player";
        draw(spriteBatch, currentTexture);
    }

    public void updateMovement() {
        float worldX = getWorldX();
        float worldZ = getWorldZ();
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            direction = Direction.WEST;
            worldX -= 0.5f;
            playerMoving = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            direction = Direction.EAST;
            worldX += 0.5f;
            playerMoving = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            direction = Direction.SOUTH;
            playerMoving = true;
            worldZ -= 0.5f;
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            direction = Direction.NORTH;
            playerMoving = true;
            worldZ += 0.5f;
        } else {
            playerMoving = false;
        }
        setPosition(worldX, getY(), worldZ);
    }
}
