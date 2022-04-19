package vc.andro.poketest

import com.badlogic.gdx.Game
import com.badlogic.gdx.assets.AssetManager
import vc.andro.poketest.world.WorldCreationParams

class PocketWorld : Game() {

    override fun create() {
        assetManager = AssetManager().apply {
            load(Assets.tileAtlas)
            load(Assets.entityAtlas)
            load(Assets.hackFont8pt)
            finishLoading()
        }
        setScreen(PlayScreen(WorldCreationParams()))
    }

    override fun dispose() {
        assetManager.dispose()
    }

    companion object {
        const val TILE_SIZE = 16
        lateinit var assetManager: AssetManager
    }
}
