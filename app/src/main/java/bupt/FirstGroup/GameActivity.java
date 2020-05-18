package bupt.FirstGroup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import bupt.FirstGroup.framework.Screen;
import bupt.FirstGroup.framework.impl.RTGame;
import bupt.FirstGroup.game.LoadingScreen;
import bupt.FirstGroup.models.Difficulty;

public class GameActivity extends RTGame {

    private Difficulty _diff;

    @Override
    public Screen getInitScreen() {
        // get passed difficulty object
        _diff = (Difficulty)this.getIntent().getSerializableExtra("difficulty");
        //加载的游戏资源
        return new LoadingScreen(this, _diff);
    }

}
