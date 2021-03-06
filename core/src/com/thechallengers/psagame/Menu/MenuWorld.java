package com.thechallengers.psagame.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.thechallengers.psagame.Menu.Objects.Background;
import com.thechallengers.psagame.Menu.Objects.Cloud;
import com.thechallengers.psagame.Menu.Objects.Containers;
import com.thechallengers.psagame.Menu.Objects.MenuCrane;
import com.thechallengers.psagame.Menu.Objects.MenuTitle;
import com.thechallengers.psagame.base_classes_and_interfaces.ScreenWorld;
import com.thechallengers.psagame.game.PSAGame;
import com.thechallengers.psagame.helpers.AssetLoader;
import com.thechallengers.psagame.helpers.SoundLoader;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.removeActor;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.thechallengers.psagame.game.PSAGame.CURRENT_SCREEN;
import static com.thechallengers.psagame.game.PSAGame.SFX_VOLUME;
import static com.thechallengers.psagame.game.PSAGame.playSound;

/**
 * Created by Phung Tuan Hoang on 9/6/2017.
 */

public class MenuWorld implements ScreenWorld {

    private final float PLAY_TUTORIAL_BUTTON_Y_OFFSET = 250f;
    private final float SINGLEPLAY_MULTIPLAY_BUTTON_Y_OFFSET = 585f;
    private final float SINGLEPLAY_MULTIPLAY_BUTTON_Y_MARGIN = 50f;
    private final float PLAY_MODE_BUBBLE_Y_OFFSET = 450f;
    private final float SHOP_SETTINGS_LEADERBOARD_ICON_Y_OFFSET = 50f;
    private final float SHOP_SETTINGS_LEADERBOARD_ICON_X_OFFSET = 100f;
    private final float SHOP_SETTINGS_LEADERBOARD_ICON_X_MARGIN = 50f;
    private final float WIDTH = 1080f;
    private final float HEIGHT = 1920f;

    private TextButton tutorial_button, play_button, single_player_button, multi_player_button, overlay_button, setting_button, setting_overlay, setting_box;
    private TextButton.TextButtonStyle play_button_style, tutorial_button_style, single_player_button_style, multi_player_button_style,
            overlay_button_style, setting_button_style, setting_overlay_style, setting_box_style;
    private Slider.SliderStyle music_slider_style, sfx_slider_style;
    private Slider music_slider, sfx_slider;
    private MenuCrane menu_crane;
    private Background background;
    private MenuTitle menu_title;
    private Containers containers;
    private Stage stage;
    private int zoomTime = 0;
    private boolean isZooming = false;
    private Array<Cloud> cloudArray;

    private boolean isInTransition = false;

    //for shop
    private TextButton shop_button;
    private TextButton.TextButtonStyle shop_button_style;

    //for leaderboard
    private TextButton leaderboard_button;
    private TextButton.TextButtonStyle leaderboard_button_style;

    //for sorry message
    private ImageButton sorry_message;
    private ImageButton.ImageButtonStyle sorry_message_style;

    //for exit message
    private ImageButton exit_message;
    private ImageButton.ImageButtonStyle exit_message_style;
    private ImageButton yes_button;
    private ImageButton.ImageButtonStyle yes_button_style;
    private ImageButton no_button;
    private ImageButton.ImageButtonStyle no_button_style;


    private boolean isShowingExitButton = false;
    private PSAGame game;

    //constructor
    public MenuWorld(PSAGame game) {
        this.game = game;
        stage = new Stage() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.BACK && isShowingExitButton == false) {
                    //EXIT THE GAME
                    isShowingExitButton = true;
                    createExitMessage();
                    stage.addActor(exit_message);
                    stage.addActor(yes_button);
                    stage.addActor(no_button);

                    yes_button.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            playSound("click.wav");
                            isShowingExitButton = false;
                            removeExitOptions();
                            Gdx.app.exit(); //exit the game
                        }
                    });

                    no_button.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            playSound("click.wav");
                            isShowingExitButton = false;
                            removeExitOptions();
                        }
                    });

                    exit_message.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            playSound("click.wav");
                            removeExitOptions();
                        }
                    });
                }
                return super.keyDown(keyCode);
            }
        };

        background = new Background();
        menu_title = new MenuTitle();

        createPlayButton();
        createMultiPlayerButton();
        createSinglePlayerButton();
        createOverlayButton();
        createTutorialButton();
        createShopButton();
        createSettingButton();
        createLeaderboardButton();
        createExitMessage();


        //add all actors to stage
        stage.addActor(background);
        stage.addActor(menu_title);
        //stage.addActor(menu_crane);
        stage.addActor(play_button);
        stage.addActor(tutorial_button);
        stage.addActor(shop_button);
        stage.addActor(setting_button);
        stage.addActor(leaderboard_button);

        //clouds and containers
        cloudArray = new Array<Cloud>();
        createContainers();
        createClouds();

        //lookAt();

    }

    @Override
    public void update(float delta) {
        stage.act(delta);

        if (zoomTime == 57) {
            CURRENT_SCREEN = PSAGame.Screen.LevelSelectionScreen;
        }

        if (isZooming && zoomTime < 57) {
            translate();
            zoomIn();
            zoomTime ++;
        }

        checkForExit();
    }

    public void createExitMessage() {
        exit_message_style = new ImageButton.ImageButtonStyle();
        exit_message_style.imageUp = new TextureRegionDrawable(new TextureRegion(AssetLoader.exit_message));
        exit_message_style.imageDown = new TextureRegionDrawable(new TextureRegion(AssetLoader.exit_message));
        exit_message = new ImageButton(exit_message_style);
        exit_message.setPosition(100, 850);
//
        yes_button_style = new ImageButton.ImageButtonStyle();
        yes_button_style.imageUp = new TextureRegionDrawable(new TextureRegion(AssetLoader.yes_button));
        yes_button_style.imageDown = new TextureRegionDrawable(new TextureRegion(AssetLoader.yes_button));
        yes_button = new ImageButton(yes_button_style);
        yes_button.setPosition(230, 850);
//
        no_button_style = new ImageButton.ImageButtonStyle();
        no_button_style.imageUp = new TextureRegionDrawable(new TextureRegion(AssetLoader.no_button));
        no_button_style.imageDown = new TextureRegionDrawable(new TextureRegion(AssetLoader.no_button));
        no_button = new ImageButton(no_button_style);
        no_button.setPosition(630, 850);



    }

    public void checkForExit() {

    }

    public Stage getStage() {
        return stage;
    }

    public void createClouds() {
        for (int i = 0; i < 5; i++) {
            Cloud cloud = new Cloud();
            stage.addActor(cloud);
            cloudArray.add(cloud);
        }
    }

    public void createContainers() {
        containers = new Containers();
        stage.addActor(containers);
    }

    //SHOP BUTTON
    public void createShopButtonStyle() {
        shop_button_style = new TextButton.TextButtonStyle();
        shop_button_style.up = new TextureRegionDrawable(new TextureRegion(AssetLoader.shop_button));
        shop_button_style.down = new TextureRegionDrawable(new TextureRegion(AssetLoader.shop_button_pressed));
        shop_button_style.font = AssetLoader.arial;
    }

    public void createShopButton() {
        createShopButtonStyle();

        shop_button = new TextButton("", shop_button_style);
        shop_button.setPosition(SHOP_SETTINGS_LEADERBOARD_ICON_X_OFFSET,
                SHOP_SETTINGS_LEADERBOARD_ICON_Y_OFFSET);
        addListenerToShopButton();
    }

    public void addListenerToShopButton() {
        shop_button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                shop_button.addAction(sequence(fadeOut(0.6f), fadeIn(0.6f)));
                playSound("click.wav");
                CURRENT_SCREEN = PSAGame.Screen.ShopScreen;
            }
        });
    }

    //LEADERBOARD BUTTON
    public void createLeaderboardButtonStyle() {
        leaderboard_button_style = new TextButton.TextButtonStyle();
        leaderboard_button_style.up = new TextureRegionDrawable(new TextureRegion(AssetLoader.leaderboard_button));
        leaderboard_button_style.down = new TextureRegionDrawable(new TextureRegion(AssetLoader.leaderboard_button_pressed));
        leaderboard_button_style.font = AssetLoader.arial;
    }

    public void createLeaderboardButton() {
        createLeaderboardButtonStyle();

        leaderboard_button = new TextButton("", leaderboard_button_style);
        leaderboard_button.setPosition(SHOP_SETTINGS_LEADERBOARD_ICON_X_OFFSET
                + (shop_button.getWidth() + SHOP_SETTINGS_LEADERBOARD_ICON_X_MARGIN)
                + (setting_button.getWidth() + SHOP_SETTINGS_LEADERBOARD_ICON_X_MARGIN),
                SHOP_SETTINGS_LEADERBOARD_ICON_Y_OFFSET);
        addListenerToLeaderboardButton();
    }

    public void addListenerToLeaderboardButton() {
        leaderboard_button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playSound("click.wav");
                leaderboard_button.addAction(sequence(fadeOut(0.6f), fadeIn(0.6f)));
                CURRENT_SCREEN = PSAGame.Screen.LeaderboardScreen;
            }
        });
    }

    //PLAY BUTTON
    public void createPlayButtonStyle() {
        play_button_style = new TextButton.TextButtonStyle();
        play_button_style.up = new TextureRegionDrawable(new TextureRegion(AssetLoader.play_button));
        play_button_style.down = new TextureRegionDrawable(new TextureRegion(AssetLoader.play_button_pressed));
        play_button_style.font = AssetLoader.arial;
    }

    public void createPlayButton() {
        createPlayButtonStyle();

        play_button = new TextButton("", play_button_style);
        play_button.setPosition(WIDTH/4-play_button.getWidth()/2, PLAY_TUTORIAL_BUTTON_Y_OFFSET);
        addListenerToPlayButton();

        //addListenerToPlayButton();
    }

    public void addListenerToPlayButton() {
        play_button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage.addActor(overlay_button);
                stage.addActor(single_player_button);
                stage.addActor(multi_player_button);
                playSound("click.wav");
            }
        });
    }

    //TUTORIAL BUTTON
    public void createTutorialButtonStyle() {
        tutorial_button_style = new TextButton.TextButtonStyle();
        tutorial_button_style.up = new TextureRegionDrawable(new TextureRegion(AssetLoader.tutorial_button));
        tutorial_button_style.down = new TextureRegionDrawable(new TextureRegion(AssetLoader.tutorial_button_pressed));
        tutorial_button_style.font = AssetLoader.arial;
    }

    public void createTutorialButton() {
        createTutorialButtonStyle();

        tutorial_button = new TextButton("", tutorial_button_style);
        tutorial_button.setPosition(WIDTH*0.75f - tutorial_button.getWidth()/2, PLAY_TUTORIAL_BUTTON_Y_OFFSET);

        addListenerToTutorialButton();
    }

    public void addListenerToTutorialButton() {
        tutorial_button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                removePlayOptions();
                CURRENT_SCREEN = PSAGame.Screen.TutorialScreen;
                //move to multi player code here
                playSound("click.wav");
            }
        });
    }

    //SINGLE PLAYER BUTTON
    public void createSinglePlayerButtonStyle() {
        single_player_button_style = new TextButton.TextButtonStyle();
        single_player_button_style.up = new TextureRegionDrawable(new TextureRegion(AssetLoader.single_player_button));
        single_player_button_style.down = new TextureRegionDrawable(new TextureRegion(AssetLoader.single_player_button_pressed));
        single_player_button_style.font = AssetLoader.arial;
    }

    public void createSinglePlayerButton() {
        createSinglePlayerButtonStyle();

        single_player_button = new TextButton("", single_player_button_style);
        single_player_button.setPosition(WIDTH/2 - single_player_button.getWidth()/2,
                SINGLEPLAY_MULTIPLAY_BUTTON_Y_OFFSET
                        + SINGLEPLAY_MULTIPLAY_BUTTON_Y_MARGIN + multi_player_button.getHeight());
        single_player_button.getColor().a = 0;
        single_player_button.addAction(fadeIn(0.1f));

        addListenerToSinglePlayerButton();
    }

    public void addListenerToSinglePlayerButton() {
        single_player_button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                removePlayOptions();

                //move to single player code here
                containers.addAction(fadeOut(0.6f));

                for (int i = 0; i < cloudArray.size; i++) {
                    cloudArray.get(i).clearActions();
                    cloudArray.get(i).addAction(fadeOut(0.6f));
                }

                playSound("click.wav");

                isZooming = true;
            }
        });
    }

    //MULTIPLAYER BUTTON
    public void createMultiPlayerButtonStyle() {
        multi_player_button_style = new TextButton.TextButtonStyle();
        multi_player_button_style.up = new TextureRegionDrawable(new TextureRegion(AssetLoader.multi_player_button));
        multi_player_button_style.down = new TextureRegionDrawable(new TextureRegion(AssetLoader.multi_player_button_pressed));
        multi_player_button_style.font = AssetLoader.arial;
    }

    public void createMultiPlayerButton() {
        createMultiPlayerButtonStyle();

        multi_player_button = new TextButton("", multi_player_button_style);
        multi_player_button.setPosition(WIDTH/2 - multi_player_button.getWidth()/2,
                SINGLEPLAY_MULTIPLAY_BUTTON_Y_OFFSET);
        multi_player_button.getColor().a = 0;
        multi_player_button.addAction(fadeIn(0.1f));

        addListenerToMultiPlayerButton();
    }

    public void addListenerToMultiPlayerButton() {
        multi_player_button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                removePlayOptions();
//                CURRENT_SCREEN = PSAGame.Screen.TutorialScreen;
                createSorryMessage();
                //move to multi player code here
                playSound("click.wav");
            }
        });
    }

    public void createSorryMessage(){
        sorry_message_style = new ImageButton.ImageButtonStyle();
        sorry_message_style.imageUp = new TextureRegionDrawable(new TextureRegion(AssetLoader.sorry_message));
        sorry_message_style.imageDown = new TextureRegionDrawable(new TextureRegion(AssetLoader.sorry_message));
        sorry_message = new ImageButton(sorry_message_style);
        sorry_message.setPosition(100+40, 650);
        stage.addActor(sorry_message);
        sorry_message.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sorry_message.remove();
            }
        });
    }
    //OVERLAY (BACKGROUND FOR SP AND MP)
    public void createOverlayButtonStyle() {
        overlay_button_style = new TextButton.TextButtonStyle();
        overlay_button_style.up = new TextureRegionDrawable(new TextureRegion(AssetLoader.overlay_button));
        overlay_button_style.down = new TextureRegionDrawable(new TextureRegion(AssetLoader.overlay_button_pressed));
        overlay_button_style.font = AssetLoader.arial;
    }

    public void createOverlayButton() {
        createOverlayButtonStyle();

        overlay_button = new TextButton("", overlay_button_style);
        overlay_button.setPosition(0,0);
        overlay_button.getColor().a = 0;
        overlay_button.addAction(fadeIn(0.1f));

        addListenerToOverlayButton();
    }

    public void addListenerToOverlayButton() {
        overlay_button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                removePlayOptions();
                playSound("click.wav");
            }
        });
    }

    //SETTING BUTTON
    public void createSettingButtonStyle() {
        setting_button_style = new TextButton.TextButtonStyle();
        setting_button_style.up = new TextureRegionDrawable(new TextureRegion(AssetLoader.setting_button));
        setting_button_style.down = new TextureRegionDrawable(new TextureRegion(AssetLoader.setting_button_pressed));
        setting_button_style.font = AssetLoader.arial;
    }

    public void createSettingButton() {
        createSettingButtonStyle();

        setting_button = new TextButton("", setting_button_style);
        setting_button.setPosition(SHOP_SETTINGS_LEADERBOARD_ICON_X_OFFSET + (shop_button.getWidth()
                + SHOP_SETTINGS_LEADERBOARD_ICON_X_MARGIN),
                SHOP_SETTINGS_LEADERBOARD_ICON_Y_OFFSET);

        addListenerToSettingButton();
    }

    public void addListenerToSettingButton() {
        setting_button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                createSettingOverlay();
                createSettingBox();
                createSFXSlider();
                createMusicSlider();

                stage.addActor(setting_overlay);
                stage.addActor(setting_box);
                stage.addActor(music_slider);
                stage.addActor(sfx_slider);
                playSound("click.wav");
            }
        });
    }

    //OVERLAY (FOR SETTING)
    public void createSettingOverlay() {
        setting_overlay_style = new TextButton.TextButtonStyle();
        setting_overlay_style.up = new TextureRegionDrawable(new TextureRegion(AssetLoader.setting_overlay));
        setting_overlay_style.down = new TextureRegionDrawable(new TextureRegion(AssetLoader.setting_overlay));
        setting_overlay_style.font = AssetLoader.arial;

        setting_overlay = new TextButton("", setting_overlay_style);
        setting_overlay.setPosition(0, 0);

        setting_overlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                removeSettingOptions();
                playSound("click.wav");
            }
        });
    }

    //SETTING BOX - BASICALLY BOX THAT CONTAINS SETTINGS OPTIONS
    public void createSettingBox() {
        setting_box_style = new TextButton.TextButtonStyle();
        setting_box_style.up = new TextureRegionDrawable(new TextureRegion(AssetLoader.setting_box));
        setting_box_style.down = new TextureRegionDrawable(new TextureRegion(AssetLoader.setting_box));
        setting_box_style.font = AssetLoader.arial;

        setting_box = new TextButton("", setting_box_style);
        setting_box.setPosition(165-40, 510);
        setting_box.getColor().a = 0;
        setting_box.addAction(fadeIn(0.1f));
    }

    //MUSIC SLIDER
    public void createMusicSlider() {
        music_slider_style = new Slider.SliderStyle(new TextureRegionDrawable(new TextureRegion(AssetLoader.slider_bg)),
                                                    new TextureRegionDrawable(new TextureRegion(AssetLoader.slider_knob)));
        music_slider = new Slider(0f, 1f, 0.01f, false, music_slider_style);
        music_slider.setHeight(88);
        music_slider.setWidth(588);
        music_slider.setPosition(165 + 85, 1177-100-10);
        music_slider.setValue(Gdx.app.getPreferences("prefs").getFloat("music volume"));
        music_slider.getColor().a = 0;
        music_slider.addAction(fadeIn(0.1f));

        music_slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.getPreferences("prefs").putFloat("music volume", music_slider.getValue()).flush();
            }
        });
    }

    public void createSFXSlider() {
        sfx_slider_style = new Slider.SliderStyle(new TextureRegionDrawable(new TextureRegion(AssetLoader.slider_bg)),
                           new TextureRegionDrawable(new TextureRegion(AssetLoader.slider_knob)));
        sfx_slider = new Slider(0f, 1f, 0.01f, false, sfx_slider_style);
        sfx_slider.setHeight(88);
        sfx_slider.setWidth(588);
        sfx_slider.setPosition(165 + 85, 983-100);
        sfx_slider.setValue(Gdx.app.getPreferences("prefs").getFloat("sfx volume"));
        sfx_slider.getColor().a = 0;
        sfx_slider.addAction(fadeIn(0.1f));

        sfx_slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.getPreferences("prefs").putFloat("sfx volume", sfx_slider.getValue()).flush();
            }
        });
    }

    //
    public void removeSettingOptions() {
        setting_box.addAction(Actions.sequence(fadeOut(0.1f), removeActor()));
        setting_overlay.addAction(Actions.sequence(fadeOut(0.1f), removeActor()));
        music_slider.addAction(Actions.sequence(fadeOut(0.1f), removeActor()));
        sfx_slider.addAction(Actions.sequence(fadeOut(0.1f), removeActor()));
    }

    public void removeExitOptions() {
        exit_message.addAction(Actions.sequence(fadeOut(0.1f), removeActor()));
        yes_button.addAction(Actions.sequence(fadeOut(0.1f), removeActor()));
        no_button.addAction(Actions.sequence(fadeOut(0.1f), removeActor()));
    }


    //
    public void removePlayOptions() {
        single_player_button.addAction(sequence(fadeOut(0.1f), removeActor(), fadeIn(0.1f)));
        multi_player_button.addAction(sequence(fadeOut(0.1f), removeActor(), fadeIn(0.1f)));
        overlay_button.addAction(sequence(fadeOut(0.1f), removeActor(), fadeIn(0.1f)));
    }

    public void zoomIn() {
        ((OrthographicCamera) stage.getCamera()).zoom -= 0.01f;
    }

    public void translate() {
        stage.getCamera().translate(-2.75f, +2f, 0);
    }


}
