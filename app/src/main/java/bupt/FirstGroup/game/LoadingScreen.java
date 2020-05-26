package bupt.FirstGroup.game;

import android.content.res.AssetManager;
import android.util.Log;

import bupt.FirstGroup.framework.Game;
import bupt.FirstGroup.framework.Graphics;
import bupt.FirstGroup.framework.Screen;
import bupt.FirstGroup.framework.impl.RTGame;
import bupt.FirstGroup.models.Difficulty;


public class LoadingScreen extends Screen {
    private Difficulty _diff;
    private static final String IMAGE_PATH = "img/";
    private static final String SOUND_EFFECTS_PATH = "audio/";
    private static final String MUSIC_PATH = "music/";


    public LoadingScreen(Game game, Difficulty difficulty) {
        super(game);
        this._diff = difficulty;
    }


    @Override
    public void update(float deltaTime) {
        Graphics g = game.getGraphics();
        //加载特效资源，暂空

        //加载按钮资源
        Assets.flower_key1=g.newImage(IMAGE_PATH+"flower_key_1.png",Graphics.ImageFormat.ARGB4444);
        //加载图片资源
        Assets.scale = g.newImage(IMAGE_PATH+"scale_1.png",Graphics.ImageFormat.RGB565);
        Assets.background = g.newImage(IMAGE_PATH + "background4.png", Graphics.ImageFormat.RGB565);
        Assets.placeholder =g.newImage(IMAGE_PATH+"placeholder.png",Graphics.ImageFormat.RGB565);
        Assets.hpframe= g.newImage(IMAGE_PATH+"hp.png",Graphics.ImageFormat.RGB565);
        Assets.hp = g.newImage(IMAGE_PATH+"hp_2.png",Graphics.ImageFormat.RGB565);
        Assets.toprect=g.newImage(IMAGE_PATH+"top.png",Graphics.ImageFormat.RGB565);
        Assets.gameover = g.newImage(IMAGE_PATH + "gameover.png", Graphics.ImageFormat.RGB565);
        Assets.pause = g.newImage(IMAGE_PATH + "pause.png", Graphics.ImageFormat.RGB565);
        Assets.pauseclicked = g.newImage(IMAGE_PATH + "pauseclicked.png", Graphics.ImageFormat.RGB565);
        Assets.score = g.newImage(IMAGE_PATH + "score.png", Graphics.ImageFormat.RGB565);
        Assets.streak = g.newImage(IMAGE_PATH + "streak.png", Graphics.ImageFormat.RGB565);

        Assets.ballNormal = g.newImage(IMAGE_PATH + "key_cut.png", Graphics.ImageFormat.RGB565);
        Assets.ballMultiplier = g.newImage(IMAGE_PATH + "ball_multiplier.png", Graphics.ImageFormat.RGB565);
        Assets.ballOneUp = g.newImage(IMAGE_PATH + "ball_oneup.png", Graphics.ImageFormat.RGB565);
        Assets.ballSpeeder = g.newImage(IMAGE_PATH + "ball_speeder.png", Graphics.ImageFormat.RGB565);
        Assets.ballBomb = g.newImage(IMAGE_PATH + "ball_bomb.png", Graphics.ImageFormat.RGB565);
        Assets.explosion = g.newImage(IMAGE_PATH + "explosion.png", Graphics.ImageFormat.RGB565);
        Assets.explosionBright = g.newImage(IMAGE_PATH + "explosion_bright.png", Graphics.ImageFormat.RGB565);
        Assets.ballSkull = g.newImage(IMAGE_PATH + "skull-ball-icon.png", Graphics.ImageFormat.RGB565);
        Assets.sirens = g.newImage(IMAGE_PATH + "sirens.png", Graphics.ImageFormat.RGB565);
        //加载音频资源
        Assets.soundClick = game.getAudio().createSound(SOUND_EFFECTS_PATH + "sound_guiclick.ogg");
        Assets.soundExplosion = game.getAudio().createSound(SOUND_EFFECTS_PATH + "sound_explosion.ogg");
        Assets.soundCreepyLaugh = game.getAudio().createSound(SOUND_EFFECTS_PATH + "sound_creepy_laugh.mp3");
        //加载音乐资源
        Assets.musicTrack = game.getAudio().createMusic(MUSIC_PATH + _diff.getMusic());

        game.setScreen(new GameScreen((RTGame)game, _diff));
    }
    @Override
    public void paint(float deltaTime) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void backButton() {

    }
}
