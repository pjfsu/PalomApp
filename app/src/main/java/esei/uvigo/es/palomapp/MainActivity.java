package esei.uvigo.es.palomapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private DBManager dbManager;
    private SimpleCursorAdapter simpleCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.play).setOnClickListener(view -> play());
        registerForContextMenu(findViewById(R.id.games));
        dbManager = new DBManager(getApplicationContext());
    }

    @Override
    protected void onStart() {
        super.onStart();
        ListView listViewGames = findViewById(R.id.games);
        simpleCursorAdapter = new SimpleCursorAdapter(
                this,
                R.layout.game_item,
                null,
                new String[]{DBManager.GAME_COL_DATE,
                        DBManager.GAME_COL_WHITES,
                        DBManager.GAME_COL_BLACKS,
                        DBManager.GAME_COL_WINNER},
                new int[]{R.id.date,
                        R.id.whites,
                        R.id.blacks,
                        R.id.winner}
        );
        listViewGames.setAdapter(simpleCursorAdapter);
        getAllGames();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dbManager.close();
        simpleCursorAdapter.getCursor().close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        boolean selected = false;
        switch (item.getItemId()) {
            case R.id.search_main_option:
                search();
                selected = true;
                break;
            case R.id.ranking_main_option:
                ranking();
                selected = true;
                break;
        }
        return selected;
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.games) {
            getMenuInflater().inflate(R.menu.main_context_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        boolean selected = super.onContextItemSelected(item);
        int position = ((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position;
        Cursor cursor = simpleCursorAdapter.getCursor();
        switch (item.getItemId()) {
            case R.id.load_main_context:
                if (cursor.moveToPosition(position)) {
                    String date = cursor.getString(0);
                    load(date);
                } else {
                    Toast.makeText(this, "¯\\_(ツ)_/¯", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.history_main_context:
                if (cursor.moveToPosition(position)) {
                    String date = cursor.getString(0);
                    history(date);
                } else {
                    Toast.makeText(this, "¯\\_(ツ)_/¯", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.resume_main_context:
                if (cursor.moveToPosition(position)) {
                    String date = cursor.getString(0);
                    resume(date);
                } else {
                    Toast.makeText(this, "¯\\_(ツ)_/¯", Toast.LENGTH_LONG).show();
                }
                break;
        }
        return selected;
    }


    private void search() {
        String[] players = new String[2];
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_search, null);
        RadioButton radioButtonPlayer1All = view.findViewById(R.id.custom_search_player_1_all);
        radioButtonPlayer1All.setChecked(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String player1 = ((EditText) view.findViewById(R.id.custom_search_player_1)).getText().toString();
                String player2 = ((EditText) view.findViewById(R.id.custom_search_player_2)).getText().toString();

                int checkedRadioButtonId = ((RadioGroup) view.findViewById(R.id.custom_search_radio_group)).getCheckedRadioButtonId();
                switch (checkedRadioButtonId) {
                    case R.id.custom_search_player_1_all:
                        getGames(player1);
                        break;
                    case R.id.custom_search_player_1_wins:
                        getWins(player1);
                        break;
                    case R.id.custom_search_player_1_in_progress:
                        getInProgress(player1);
                        break;
                    case R.id.custom_search_player_1_draws:
                        getDraws(player1);
                        break;
                    case R.id.custom_search_players_all:
                        getGames(player1, player2);
                        break;
                    case R.id.custom_search_players_wins:
                        getWins(player1, player2);
                        break;
                    case R.id.custom_search_players_in_progress:
                        getInProgress(player1, player2);
                        break;
                    case R.id.custom_search_players_draws:
                        getDraws(player1, player2);
                        break;
                }
            }
        });
        builder.setNegativeButton("Clean", (dialog, which) -> getAllGames());
        builder.create().show();
    }

    private void getDraws(String player1, String player2) {
        simpleCursorAdapter.changeCursor(dbManager.getDraws(player1, player2));
    }

    private void getInProgress(String player1, String player2) {
        simpleCursorAdapter.changeCursor(dbManager.getInProgress(player1, player2));
    }

    private void getWins(String player1, String player2) {
        simpleCursorAdapter.changeCursor(dbManager.getWins(player1, player2));
    }

    private void getDraws(String player) {
        simpleCursorAdapter.changeCursor(dbManager.getDraws(player));
    }

    private void getInProgress(String player) {
        simpleCursorAdapter.changeCursor(dbManager.getInProgress(player));
    }

    private void getWins(String player) {
        simpleCursorAdapter.changeCursor(dbManager.getWins(player));
    }

    private void getGames(String player) {
        simpleCursorAdapter.changeCursor(dbManager.getGames(player));
    }

    private void getAllGames() {
        simpleCursorAdapter.changeCursor(dbManager.getGames());
    }

    private void getGames(String player1, String player2) {
        simpleCursorAdapter.changeCursor(dbManager.getGames(player1, player2));
    }

    private void play() {
        Intent intent = new Intent(this, SettingsActivity.class);
        MainActivity.this.startActivity(intent);
    }

    private void ranking() {
        Intent intent = new Intent(this, RankingActivity.class);
        MainActivity.this.startActivity(intent);
    }

    private void load(String date) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("date", date);
        MainActivity.this.startActivity(intent);
    }

    private void history(String date) {
        Intent intent = new Intent(this, HistoryActivity.class);
        intent.putExtra("date", date);
        MainActivity.this.startActivity(intent);
    }

    private void resume(String date) {
        Intent intent = new Intent(this, ResumeActivity.class);
        intent.putExtra("date", date);
        MainActivity.this.startActivity(intent);
    }
}