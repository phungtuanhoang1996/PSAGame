package com.thechallengers.psagame.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.thechallengers.psagame.Menu.MenuScreen;
import com.thechallengers.psagame.Shop.ShopScreen;
import com.thechallengers.psagame.SinglePlayer.SinglePlayerGameScreen;


public class PSAGame extends Game {
	public static final float LONG_EDGE = 1920;
	public static final float SHORT_EDGE = 1080;

	public static enum Screen {
		MenuScreen, SinglePlayerGameScreen, ShopScreen
	}

	public static Screen CURRENT_SCREEN;


	@Override
	public void create() {
		//Load game preferences

		Preferences prefs = Gdx.app.getPreferences("prefs");
		if (!prefs.contains("music volume")) prefs.putFloat("music volume", 1);
		if (!prefs.contains("sfx volume")) prefs.putFloat("sfx volume", 0.5f);
		if (!prefs.contains("level1_starLevel")) prefs.putInteger("level1_starLevel", 0);
		if (!prefs.contains("crane_level")) prefs.putInteger("crane_level", 1);
		if (!prefs.contains("moneyBalance")) prefs.putInteger("moneyBalance", 100);
		if (!prefs.contains("crane_present")) prefs.putInteger("crane_present", 1);
		if (!prefs.contains("craneLv2_purchased")) prefs.putBoolean("craneLv2_purchased", false);
		if (!prefs.contains("craneLv3_purchased")) prefs.putBoolean("craneLv3_purchased", false);

		prefs.flush();

		//Open menu
		CURRENT_SCREEN = Screen.MenuScreen;
		this.setScreen(new MenuScreen(this));
	}

	@Override
	public void render() {
		super.render();
		//updateScreen();

	}

	@Override
	public void dispose() {

	}

	public void updateScreen() {
		if (!this.getScreen().getClass().getSimpleName().equals(CURRENT_SCREEN.toString())) {
			this.getScreen().dispose();
			switch (CURRENT_SCREEN) {
				case MenuScreen: {
					setScreen(new MenuScreen(this));
				}
				case SinglePlayerGameScreen: {
					setScreen(new SinglePlayerGameScreen(this));
				}
				case ShopScreen: {
					setScreen(new ShopScreen(this));
				}
				default:
			}
		}
	}
}
