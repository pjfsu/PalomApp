package esei.uvigo.es.palomapp;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class HistoryActivity extends AppCompatActivity {
    private DBManager dbManager;
    private SimpleCursorAdapter simpleCursorAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        dbManager = new DBManager(this);
        String date = getIntent().getExtras().getString("date");
        ListView listViewHistory = findViewById(R.id.history);
        simpleCursorAdapter = new SimpleCursorAdapter(
                this,
                R.layout.history_item,
                null,
                new String[]{DBManager.HISTORY_COL_SOURCE_PIECE_ICON,
                        DBManager.HISTORY_COL_SOURCE_PIECE_ROW,
                        DBManager.HISTORY_COL_SOURCE_PIECE_COLUMN,
                        DBManager.HISTORY_COL_DESTINY_PIECE_ROW,
                        DBManager.HISTORY_COL_DESTINY_PIECE_COLUMN,
                        DBManager.HISTORY_COL_DESTINY_PIECE_ICON},
                new int[]{R.id.source_piece_icon,
                        R.id.source_piece_row,
                        R.id.source_piece_column,
                        R.id.destiny_piece_row,
                        R.id.destiny_piece_column,
                        R.id.destiny_piece_icon}
        );
        listViewHistory.setAdapter(simpleCursorAdapter);
        updateHistory(date);
        getSupportActionBar().setTitle("History");
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private void updateHistory(String date) {
        simpleCursorAdapter.changeCursor(dbManager.getHistory(date));
    }
}
