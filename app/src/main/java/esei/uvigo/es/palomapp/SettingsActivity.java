package esei.uvigo.es.palomapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class SettingsActivity extends AppCompatActivity {
    private int player1Color;
    private String player1Name;

    private int player2Color;
    private String player2Name;

    private int boardColor;
    private String date;

    private DBManager dbManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        dbManager = new DBManager(getApplicationContext());

        RadioButton radioButtonPlayer1Whites = findViewById(R.id.player_1_whites);
        radioButtonPlayer1Whites.setChecked(true);
        player1Color = Color.WHITE;

        RadioButton radioButtonPlayer1Blacks = findViewById(R.id.player_1_blacks);
        radioButtonPlayer1Blacks.setChecked(false);

        RadioButton radioButtonPlayer2Whites = findViewById(R.id.player_2_whites);
        radioButtonPlayer2Whites.setChecked(false);

        RadioButton radioButtonPlayer2Blacks = findViewById(R.id.player_2_blacks);
        radioButtonPlayer2Blacks.setChecked(true);
        player2Color = Color.BLACK;

        RadioGroup radioGroupPlayer1Color = findViewById(R.id.player_1_color);
        radioGroupPlayer1Color.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.player_1_whites:
                        player1Color = Color.WHITE;
                        player2Color = Color.BLACK;
                        radioButtonPlayer2Whites.setChecked(false);
                        radioButtonPlayer2Blacks.setChecked(true);
                        break;
                    case R.id.player_1_blacks:
                        player1Color = Color.BLACK;
                        player2Color = Color.WHITE;
                        radioButtonPlayer2Whites.setChecked(true);
                        radioButtonPlayer2Blacks.setChecked(false);
                        break;
                }
            }
        });

        RadioGroup radioGroupPlayer2Color = findViewById(R.id.player_2_color);
        radioGroupPlayer2Color.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.player_2_whites:
                        player2Color = Color.WHITE;
                        player1Color = Color.BLACK;
                        radioButtonPlayer1Whites.setChecked(false);
                        radioButtonPlayer1Blacks.setChecked(true);
                        break;
                    case R.id.player_2_blacks:
                        player2Color = Color.BLACK;
                        player1Color = Color.WHITE;
                        radioButtonPlayer1Whites.setChecked(true);
                        radioButtonPlayer1Blacks.setChecked(false);
                        break;
                }
            }
        });

        RadioButton boardColorDefault = findViewById(R.id.board_color_default);
        boardColorDefault.setChecked(true);
        boardColor = Color.GRAY;

        RadioGroup radioGroupBoardColor = findViewById(R.id.board_color);
        radioGroupBoardColor.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = findViewById(i);
                boardColor = radioButton.getCurrentTextColor();
            }
        });

        EditText editTextPlayer1Name = findViewById(R.id.player_1_name);
        EditText editTextPlayer2Name = findViewById(R.id.player_2_name);

        Button buttonOK = findViewById(R.id.ok);
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                date = simpleDateFormat.format(timestamp);
                player1Name = editTextPlayer1Name.getText().toString();
                if (player1Name.isEmpty()) {
                    player1Name = "WHITES";
                }
                player2Name = editTextPlayer2Name.getText().toString();
                if (player2Name.isEmpty()) {
                    player2Name = "BLACKS";
                }
                if (player1Color == Color.WHITE && player2Color == Color.BLACK) {
                    dbManager.addGame(date, boardColor, player1Name, player2Name);
                } else if (player2Color == Color.WHITE && player1Color == Color.BLACK) {
                    dbManager.addGame(date, boardColor, player2Name, player1Name);
                }
                Intent intent = new Intent(SettingsActivity.this, GameActivity.class);
                intent.putExtra("date", date);
                SettingsActivity.this.startActivity(intent);
            }
        });
        getSupportActionBar().setTitle("Settings");
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
