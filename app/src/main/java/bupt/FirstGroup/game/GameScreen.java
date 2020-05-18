package bupt.FirstGroup.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Vibrator;
import android.util.Log;

import androidx.core.content.res.ResourcesCompat;

import bupt.FirstGroup.MainActivity;
import bupt.FirstGroup.R;
import bupt.FirstGroup.framework.FileIO;
import bupt.FirstGroup.framework.Game;
import bupt.FirstGroup.framework.Graphics;
import bupt.FirstGroup.framework.Input;
import bupt.FirstGroup.framework.Music;
import bupt.FirstGroup.framework.Screen;
import bupt.FirstGroup.framework.Input.TouchEvent;
import bupt.FirstGroup.framework.impl.RTGame;
import bupt.FirstGroup.game.models.Ball;
import bupt.FirstGroup.models.Difficulty;

//加载的游戏场景
public class GameScreen extends Screen {
    private static final String TAG = "GameScreenTag";

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    //游戏状态
    enum GameState {
        Ready, Running, Paused, GameOver
    }

    // game and device
    //游戏高度
    private int _gameHeight;
    //游戏宽度
    private int _gameWidth;
    //随机种子？
    private Random _rand;
    //难度字段
    private Difficulty _difficulty;
    //生命值
    private int _lifes;
    //振动器
    private Vibrator _vibrator;
    //游戏是否结束
    private boolean _isEnding;

    // score
    //分数
    private int _score;
    //2倍分数
    private int _multiplier;
    //？？？
    private int _streak;

    // tickers
    //计数器？？
    private int _tick;
    //2倍计数器？？
    private int _doubleMultiplierTicker;
    //炸弹计数器
    private int _explosionTicker;
    //当前时间
    private float _currentTime;
    //结束计数器
    private int _endTicker;

    // balls
    //左边音符
    private List<Ball> _ballsLeft;
    //中间音符
    private List<Ball> _ballsMiddle;
    //右边音符
    private List<Ball> _ballsRight;

    // lane miss indicators
    //左边轨道透明度
    private int _laneHitAlphaLeft;
    //中间轨道透明度
    private int _laneHitAlphaMiddle;
    //右边轨道透明度
    private int _laneHitAlphaRight;

    // difficulty params
    //产生音符间隔
    private float _spawnInterval;
    //音符速度
    private int _ballSpeed;
    //产生正常音符概率
    private final double _spawnChance_normal = 0.26; // TODO dynamic
    //产生加命音符概率
    private final double _spawnChance_oneup = _spawnChance_normal + 0.003;
    //产生双倍分数音符概率
    private final double _spawnChance_multiplier = _spawnChance_oneup + 0.001;
    //产生加速音符概率
    private final double _spawnChance_speeder = _spawnChance_multiplier + 0.003;
    //产生炸弹概率
    private final double _spawnChance_bomb = _spawnChance_speeder + 0.0005;
    //产生骷髅概率
    private final double _spawnChance_skull = _spawnChance_bomb + 0.014;

    // audio
    //背景音乐
    private Music _currentTrack;

    // ui
    //分数显示界面
    private Paint _paintScore;
    //游戏结束界面
    private Paint _paintGameover;

    // constants
    // how far the screen should scroll after the track ends
    //结束位置
    private static final int END_TIME = 1800;
    // initial y coordinate of spawned balls
    //产生音符的位置
    private static final int BALL_INITIAL_Y = -50;
    // hitbox is the y-range within a ball can be hit by a press in its lane
    //击中音符的区间
    private static final int HITBOX_CENTER = 1760;
    private static final int HITBOX_HEIGHT = 280;
    // if no ball is in the hitbox when pressed, remove the lowest ball in the
    // miss zone right above the hitbox (it still counts as a miss)
    //如果没击中某音符，将它删除，设为一次miss
    private static final int MISS_ZONE_HEIGHT = 150;
    private static final int MISS_FLASH_INITIAL_ALPHA = 240;
    private static final int DOUBLE_MULTIPLIER_TIME = 600;
    // explosion
    //爆炸的顶部位置？
    private static final int EXPLOSION_TOP = 600;
    //爆炸时间
    private static final int EXPLOSION_TIME = 150;

    //初始状态为游戏准备中
    private GameState state = GameState.Ready;

    //游戏上下文
    private Context context;

    GameScreen(Game game, Difficulty difficulty) {
        super(game);
//        context=game.get();

        _difficulty = difficulty;
        // init difficulty parameters
        _ballSpeed = _difficulty.getBallSpeed();
        _spawnInterval = _difficulty.getSpawnInterval();

        // Initialize game objects
        _gameHeight = game.getGraphics().getHeight();
        _gameWidth = game.getGraphics().getWidth();
        Log.i("gameObj","="+_gameHeight);
        Log.i("gameObj","="+_gameWidth);
        _vibrator = game.getVibrator();
        _multiplier = 1;
        _doubleMultiplierTicker = 0;
        _score = 0;
        _streak = 0;
        _ballsLeft = new ArrayList<>();
        _ballsMiddle = new ArrayList<>();
        _ballsRight = new ArrayList<>();
        _rand = new Random();
        _tick = 0;
        _endTicker = END_TIME / _difficulty.getBallSpeed();
        _currentTime = 0f;
        _explosionTicker = 0;
        _lifes = 10;
        _laneHitAlphaLeft = 0;
        _laneHitAlphaMiddle = 0;
        _laneHitAlphaRight = 0;
        _currentTrack = Assets.musicTrack;
        _isEnding = false;

        // paints for text

        _paintScore = new Paint();
        _paintScore.setTextSize(150);
		_paintScore.setTextSkewX(-0.5f);
		_paintScore.setFakeBoldText(true);
        _paintScore.setTextAlign(Paint.Align.CENTER);
        _paintScore.setAntiAlias(true);
        _paintScore.setColor(Color.WHITE);
        //Typeface typeface=Typeface.createFromAsset(getContext().getAssets(),"font/st.ttf");
       // _paintScore.setTypeface(typeface);


        _paintGameover = new Paint();
        _paintGameover.setTextSize(50);
        _paintGameover.setTextAlign(Paint.Align.CENTER);
        _paintGameover.setAntiAlias(true);
        _paintGameover.setColor(Color.BLACK);
    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

        if (state == GameState.Ready)
            updateReady(touchEvents);
        if (state == GameState.Running)
            updateRunning(touchEvents, deltaTime);
        if (state == GameState.Paused)
            updatePaused(touchEvents);
        if (state == GameState.GameOver)
            updateGameOver(touchEvents);
    }

    private void updateReady(List<TouchEvent> touchEvents) {
        if (touchEvents.size() > 0) {
            state = GameState.Running;
            touchEvents.clear();
            _currentTrack.setLooping(false);
            _currentTrack.setVolume(0.25f);
            _currentTrack.play();
        }
    }

    private void updateRunning(List<TouchEvent> touchEvents, float deltaTime) {
        // 1. All touch input is handled here:
        handleTouchEvents(touchEvents);

        // 2. Check miscellaneous events like death:
        checkDeath();
        checkEnd();

        // 3. Individual update() methods.
        // 球下落
        updateVariables(deltaTime);
    }

    private void checkEnd() {
        if (_currentTrack.isStopped()) {
            _isEnding = true;
        }
    }

    private void explosion(List<Ball> balls) {
        Iterator<Ball> iter = balls.iterator();
        while (iter.hasNext()) {
            Ball b = iter.next();
            if (b.y > EXPLOSION_TOP) {
                iter.remove();
                _score += 10 * _multiplier
                        * (_doubleMultiplierTicker > 0 ? 2 : 1);
            }
        }
    }

    private void checkDeath() {
        if (_lifes <= 0) {
            endGame();
        }
    }

    private void endGame() {
        state = GameState.GameOver;
        // update highscore
        FileIO fileIO = game.getFileIO();
        SharedPreferences prefs = fileIO.getSharedPref();
        int oldScore;

        switch(_difficulty.getMode()) {
            case Difficulty.EASY_TAG:
                oldScore = prefs.getInt(Difficulty.EASY_TAG,0);
                break;
            case Difficulty.MED_TAG:
                oldScore = prefs.getInt(Difficulty.MED_TAG,0);
                break;
            case Difficulty.HARD_TAG:
                oldScore = prefs.getInt(Difficulty.HARD_TAG,0);
                break;
            default:
                oldScore = 0;
                break;
        }

        if(_score > oldScore) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(_difficulty.getMode(), _score);
            editor.apply();
        }
    }

    private void handleTouchEvents(List<TouchEvent> touchEvents) {
        int len = touchEvents.size();

        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);

            if (event.type == TouchEvent.TOUCH_DOWN) {
                if (event.y > 300) {
                    // ball hit area
                    if (event.x < _gameWidth / 3) {
                        if (!hitLane(_ballsLeft)) {
                            // if no ball was hit
                            _laneHitAlphaLeft = MISS_FLASH_INITIAL_ALPHA;
                        }
                    }
                    else if (event.x < _gameWidth / 3 * 2) {
                        if (!hitLane(_ballsMiddle))
                        {
                            _laneHitAlphaMiddle = MISS_FLASH_INITIAL_ALPHA;
                        }
                    }
                    else {
                        if (!hitLane(_ballsRight)) {
                            _laneHitAlphaRight = MISS_FLASH_INITIAL_ALPHA;
                        }
                    }
                }
                else {
                    // pause area
                    touchEvents.clear();
                    pause();
                    break;
                }
            }
        }
    }

    // update all the games variables each tick
    private void updateVariables(float deltatime) {
        // update timer
        _currentTime += deltatime;

        // update ball position
        for (Ball b: _ballsLeft) {
            b.update((int) (_ballSpeed * deltatime));
        }

        for (Ball b: _ballsMiddle) {
            b.update((int) (_ballSpeed * deltatime));
        }

        for (Ball b: _ballsRight) {
            b.update((int) (_ballSpeed * deltatime));
        }

        // remove missed balls
        if (removeMissed(_ballsLeft.iterator())) {
            _laneHitAlphaLeft = MISS_FLASH_INITIAL_ALPHA;
        }

        if (removeMissed(_ballsMiddle.iterator())) {
            _laneHitAlphaMiddle = MISS_FLASH_INITIAL_ALPHA;
        }

        if (removeMissed(_ballsRight.iterator())) {
            _laneHitAlphaRight = MISS_FLASH_INITIAL_ALPHA;
        }

        // spawn new balls
        if (!_isEnding && _currentTime % _spawnInterval <= deltatime) {
            spawnBalls();
        }

        // decrease miss flash intensities
        _laneHitAlphaLeft -= Math.min(_laneHitAlphaLeft, 10);
        _laneHitAlphaMiddle -= Math.min(_laneHitAlphaMiddle, 10);
        _laneHitAlphaRight -= Math.min(_laneHitAlphaRight, 10);

        // atom explosion ticker
        if (_explosionTicker > 0) {
            explosion(_ballsLeft);
            explosion(_ballsMiddle);
            explosion(_ballsRight);
        }

        // update tickers
        _doubleMultiplierTicker -= Math.min(1, _doubleMultiplierTicker);
        _explosionTicker -= Math.min(1, _explosionTicker);
        _tick = (_tick + 1) % 100000;

        if (_isEnding) {
            _endTicker -= Math.min(1, _endTicker);

            if (_endTicker <= 0) {
                endGame();
            }
        }
    }

    // remove the balls from an iterator that have fallen through the hitbox
    private boolean removeMissed(Iterator<Ball> iterator) {
        while (iterator.hasNext()) {
            Ball b = iterator.next();
            if (b.y > HITBOX_CENTER + HITBOX_HEIGHT / 2) {
                iterator.remove();
                Log.d(TAG, "fail press");
                onMiss(b);

                return b.type != Ball.BallType.Skull;
            }
        }
        return false;
    }

    // handles a TouchEvent on a certain lane
    private boolean hitLane(List<Ball> balls) {
        Iterator<Ball> iter = balls.iterator();
        Ball lowestBall = null;
        while (iter.hasNext()) {
            Ball b = iter.next();
            if (lowestBall == null || b.y > lowestBall.y) {
                lowestBall = b;
            }
        }

        if (lowestBall != null && lowestBall.y > HITBOX_CENTER - HITBOX_HEIGHT / 2) {
            balls.remove(lowestBall);
            onHit(lowestBall);
            return lowestBall.type != Ball.BallType.Skull;
        } else {
            if (lowestBall != null && lowestBall.y > HITBOX_CENTER - HITBOX_HEIGHT / 2 - MISS_ZONE_HEIGHT) {
                balls.remove(lowestBall);
            }
            onMiss(null);

            return false;
        }
    }

    // triggers when a lane gets tapped that has currently no ball in its hitbox
    private void onMiss(Ball b) {
        if(b != null && b.type == Ball.BallType.Skull) {
            return;
        }
        _vibrator.vibrate(100);
        _streak = 0;
        _score -= Math.min(_score, 50);
        _multiplier = 1;
        --_lifes;
        updateMultipliers();
    }

    // triggers when a lane gets tapped that currently has a ball in its hitbox
    private void onHit(Ball b) {
        _streak++;
        switch(b.type) {
            case OneUp: {
                ++_lifes;
            } break;
            case Multiplier: {
                _doubleMultiplierTicker = DOUBLE_MULTIPLIER_TIME;
            } break;
            case Bomb: {
                _explosionTicker = EXPLOSION_TIME;
                Assets.soundExplosion.play(0.7f);
            } break;
            case Skull: {
                onMiss(null); // hitting a skull counts as a miss
                Assets.soundCreepyLaugh.play(1);
                return;
            }
        }

        updateMultipliers();
        _score += 10 * _multiplier
                * (_doubleMultiplierTicker > 0 ? 2 : 1);
    }

    // triggers after a touch event was handled by hitLane()
    private void updateMultipliers() {
        if (_streak > 80) {
            _multiplier = 10;
        }
        else if (_streak > 40) {
            _multiplier = 5;
        }
        else if (_streak > 30) {
            _multiplier = 4;
        }
        else if (_streak > 20) {
            _multiplier = 3;
        }
        else if (_streak > 10) {
            _multiplier = 2;
        }
        else {
            _multiplier = 1;
        }
    }

    private void spawnBalls() {
        float randFloat = _rand.nextFloat();
        final int ballY = BALL_INITIAL_Y;
        int ballX = _gameWidth / 3 / 2;
        spawnBall(_ballsLeft, randFloat, ballX, ballY);

        randFloat = _rand.nextFloat();
        ballX = _gameWidth / 2;
        spawnBall(_ballsMiddle, randFloat, ballX, ballY);

        randFloat = _rand.nextFloat();
        ballX = _gameWidth - _gameWidth / 3 / 2;
        spawnBall(_ballsRight, randFloat, ballX, ballY);

    }

    private void spawnBall(List<Ball> balls, float randFloat, int ballX, int ballY) {
        if (randFloat < _spawnChance_normal) {
            balls.add(0, new Ball(ballX, ballY, Ball.BallType.Normal));
        } else if (randFloat < _spawnChance_oneup) {
            balls.add(0, new Ball(ballX, ballY, Ball.BallType.OneUp));
        } else if (randFloat < _spawnChance_multiplier) {
            balls.add(0, new Ball(ballX, ballY, Ball.BallType.Multiplier));
        } else if (randFloat < _spawnChance_speeder) {
            balls.add(0, new Ball(ballX, ballY, Ball.BallType.Speeder));
        } else if (randFloat < _spawnChance_bomb) {
            balls.add(0, new Ball(ballX, ballY, Ball.BallType.Bomb));
        } else if (randFloat < _spawnChance_skull) {
            balls.add(0, new Ball(ballX, ballY, Ball.BallType.Skull));
        }
    }

    private void updatePaused(List<TouchEvent> touchEvents) {
        if (_currentTrack.isPlaying()) {
            _currentTrack.pause();
        }

        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_DOWN) {
                resume();
                return;
            }
        }
    }

    private void updateGameOver(List<TouchEvent> touchEvents) {
        if (!_currentTrack.isStopped()) {
            _currentTrack.stop();
        }

        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
                if (event.x > 300 && event.x < 540 && event.y > 845
                        && event.y < 1100) {
                    game.goToActivity(MainActivity.class);
                    return;
                } else if (event.x >= 540 && event.x < 780 && event.y > 845
                        && event.y < 1100) {
                    game.setScreen(new LoadingScreen(game, _difficulty));
                }
            }
        }

    }

    @Override
    public void paint(float deltaTime) {
        Graphics g = game.getGraphics();
        // First draw the game elements.
        // Example:
        g.drawImage(Assets.background, 0, 0);
        g.drawImage(Assets.placeholder,_gameWidth/2-Assets.placeholder.getWidth()/2,0);

        for (Ball b: _ballsLeft) {
            paintBall(g, b);
        }

        for (Ball b: _ballsMiddle) {
            paintBall(g, b);
        }

        for (Ball b: _ballsRight) {
            paintBall(g, b);
        }

        if (_explosionTicker > 0) {
            if (_rand.nextDouble() > 0.05) {
                g.drawImage(Assets.explosion, 0, 680);
            } else {
                g.drawImage(Assets.explosionBright, 0, 680);
            }
            g.drawARGB((int)((double)_explosionTicker/EXPLOSION_TIME * 255), 255, 255, 255);
        }
        // Secondly, draw the UI above the game elements.
        if (state == GameState.Ready)
            drawReadyUI();
        if (state == GameState.Running)
            drawRunningUI();
        if (state == GameState.Paused)
            drawPausedUI();
        if (state == GameState.GameOver)
            drawGameOverUI();
    }

    private void paintBall(Graphics g, Ball b) {
        switch(b.type) {
            case Normal:
                g.drawImage(Assets.ballNormal, b.x - 90, b.y - 90);
                break;
            case OneUp:
                g.drawImage(Assets.ballOneUp, b.x - 90, b.y - 90);
                break;
            case Multiplier:
                g.drawImage(Assets.ballMultiplier, b.x - 90, b.y - 90);
                break;
            case Speeder:
                g.drawImage(Assets.ballSpeeder, b.x - 90, b.y - 90);
                break;
            case Bomb:
                g.drawImage(Assets.ballBomb,  b.x - 90, b.y - 90);
                break;
            case Skull:
                g.drawImage(Assets.ballSkull, b.x - 90, b.y - 90);
                break;
        }
    }

    private void nullify() {

        // Set all variables to null. You will be recreating them in the
        // constructor.
        _paintScore = null;

        // Call garbage collector to clean up memory.
        System.gc();
    }

    private void drawReadyUI() {
        Graphics g = game.getGraphics();

        g.drawARGB(155, 0, 0, 0);
        g.drawString("Tap to start!", _gameWidth/2, _gameHeight/2, _paintScore);
    }

    private void drawRunningUI() {
        Graphics g = game.getGraphics();
        g.drawImage(Assets.toprect,_gameWidth/2-Assets.toprect.getWidth()/2,0);
        g.drawImage(Assets.hpframe,Assets.placeholder.getWidth()/8,Assets.toprect.getHeight()/3);
        g.drawImage(Assets.hp,Assets.placeholder.getWidth()/8,Assets.toprect.getHeight()/3);
        g.drawImage(Assets.score,_gameWidth/2+Assets.placeholder.getWidth(),Assets.toprect.getHeight()/6);
        g.drawImage(Assets.pause,_gameWidth/2-Assets.pause.getWidth()/2,0);
        if (_doubleMultiplierTicker > 0) {
            g.drawImage(Assets.sirens, 0, 100);
        }

      //  g.drawRect(0, 0, _gameWidth, 100, Color.BLACK);

      //String s = "Score: " + _score +"   Multiplier: " + _multiplier * (_doubleMultiplierTicker > 0 ? 2 : 1) + "x" +"   Lifes remaining: " + _lifes;
        //g.drawString(s, 600, 80, _paintScore);
    }

    private void drawPausedUI() {
        Graphics g = game.getGraphics();
        g.drawARGB(155, 0, 0, 0);
        g.drawImage(Assets.pause, 200, 500);
        g.drawString("TAP TO CONTINUE", 540, 845, _paintGameover);
    }

    private void drawGameOverUI() {
        Graphics g = game.getGraphics();
        //g.drawARGB(205, 0, 0, 0);
        //g.drawImage(Assets.gameover, 200, 500);
       // g.drawString("FINAL SCORE: " + _score, 540, 845, _paintGameover);
    }

    @Override
    public void pause() {
        if (state == GameState.Running) {
            state = GameState.Paused;
            _currentTrack.pause();
        }

    }

    @Override
    public void resume() {
        if (state == GameState.Paused) {
            state = GameState.Running;
            _currentTrack.play();
        }
    }

    @Override
    public void dispose() {
        if(_currentTrack.isPlaying()) {
            _currentTrack.stop();
        }
    }

    @Override
    public void backButton() {
        dispose();
    }
}
