package esei.uvigo.es.palomapp;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Iterator;

public class RankingActivity extends AppCompatActivity {
    private DBManager dbManager;
    private SimpleCursorAdapter simpleCursorAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raking);
        dbManager = new DBManager(getApplicationContext());
        getSupportActionBar().setTitle("Ranking");
    }

    @Override
    protected void onResume() {
        super.onResume();
        ListView listViewRanking = findViewById(R.id.ranking);
        simpleCursorAdapter = new SimpleCursorAdapter(
                this,
                R.layout.ranking_item,
                null,
                new String[]{DBManager.GAME_COL_WINNER,
                        "total_wins"},
                new int[]{R.id.player,
                        R.id.total_wins}
        );
        listViewRanking.setAdapter(simpleCursorAdapter);
        updateRanking();

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private void updateRanking() {
        simpleCursorAdapter.changeCursor(dbManager.getRanking());
    }
}
