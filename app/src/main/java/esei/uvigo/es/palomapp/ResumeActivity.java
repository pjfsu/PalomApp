package esei.uvigo.es.palomapp;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class ResumeActivity extends AppCompatActivity {
    private DBManager dbManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume);
        getSupportActionBar().setTitle("Resume");

        dbManager = new DBManager(getApplicationContext());

        String date = getIntent().getExtras().getString("date");

        TextView textViewWhitePlayer = findViewById(R.id.white_player);
        TextView textViewBlackPlayer = findViewById(R.id.black_player);
        Cursor cursor = dbManager.getGame(date);
        if (cursor.moveToFirst()) {
            textViewWhitePlayer.setText(cursor.getString(cursor.getColumnIndex(DBManager.GAME_COL_WHITES)));
            textViewBlackPlayer.setText(cursor.getString(cursor.getColumnIndex(DBManager.GAME_COL_BLACKS)));
        }

        TextView textViewTotalTakenBlackPawns = findViewById(R.id.taken_black_pawns);
        cursor = dbManager.getTotalTakenPieces(date, BLACKS.PAWN.getIcon());
        if (cursor.moveToFirst()) {
            textViewTotalTakenBlackPawns.setText(Integer.toString(cursor.getCount()));
        }
        TextView textViewTotalTakenWhitePawns = findViewById(R.id.taken_white_pawns);
        cursor = dbManager.getTotalTakenPieces(date, WHITES.PAWN.getIcon());
        if (cursor.moveToFirst()) {
            textViewTotalTakenWhitePawns.setText(Integer.toString(cursor.getCount()));
        }

        TextView textViewTotalTakenBlackBishops = findViewById(R.id.taken_black_bishops);
        cursor = dbManager.getTotalTakenPieces(date, BLACKS.BISHOP.getIcon());
        if (cursor.moveToFirst()) {
            textViewTotalTakenBlackBishops.setText(Integer.toString(cursor.getCount()));
        }
        TextView textViewTotalTakenWhiteBishops = findViewById(R.id.taken_white_bishops);
        cursor = dbManager.getTotalTakenPieces(date, WHITES.BISHOP.getIcon());
        if (cursor.moveToFirst()) {
            textViewTotalTakenWhiteBishops.setText(Integer.toString(cursor.getCount()));
        }

        TextView textViewTotalTakenBlackKings = findViewById(R.id.taken_black_kings);
        cursor = dbManager.getTotalTakenPieces(date, BLACKS.KING.getIcon());
        if (cursor.moveToFirst()) {
            textViewTotalTakenBlackKings.setText(Integer.toString(cursor.getCount()));
        }
        TextView textViewTotalTakenWhiteKings = findViewById(R.id.taken_white_kings);
        cursor = dbManager.getTotalTakenPieces(date, WHITES.KING.getIcon());
        if (cursor.moveToFirst()) {
            textViewTotalTakenWhiteKings.setText(Integer.toString(cursor.getCount()));
        }

        TextView textViewTotalTakenBlackKnights = findViewById(R.id.taken_black_knights);
        cursor = dbManager.getTotalTakenPieces(date, BLACKS.KNIGHT.getIcon());
        if (cursor.moveToFirst()) {
            textViewTotalTakenBlackKnights.setText(Integer.toString(cursor.getCount()));
        }
        TextView textViewTotalTakenWhiteKnights = findViewById(R.id.taken_white_knights);
        cursor = dbManager.getTotalTakenPieces(date, WHITES.KNIGHT.getIcon());
        if (cursor.moveToFirst()) {
            textViewTotalTakenWhiteKnights.setText(Integer.toString(cursor.getCount()));
        }

        TextView textViewTotalTakenBlackQueens = findViewById(R.id.taken_black_queens);
        cursor = dbManager.getTotalTakenPieces(date, BLACKS.QUEEN.getIcon());
        if (cursor.moveToFirst()) {
            textViewTotalTakenBlackQueens.setText(Integer.toString(cursor.getCount()));
        }
        TextView textViewTotalTakenWhiteQueen = findViewById(R.id.taken_white_queens);
        cursor = dbManager.getTotalTakenPieces(date, WHITES.QUEEN.getIcon());
        if (cursor.moveToFirst()) {
            textViewTotalTakenWhiteQueen.setText(Integer.toString(cursor.getCount()));
        }

        TextView textViewTotalTakenBlackRooks = findViewById(R.id.taken_black_rooks);
        cursor = dbManager.getTotalTakenPieces(date, BLACKS.ROOK.getIcon());
        if (cursor.moveToFirst()) {
            textViewTotalTakenBlackRooks.setText(Integer.toString(cursor.getCount()));
        }
        TextView textViewTotalTakenWhiteRooks = findViewById(R.id.taken_white_rooks);
        cursor = dbManager.getTotalTakenPieces(date, WHITES.ROOK.getIcon());
        if (cursor.moveToFirst()) {
            textViewTotalTakenWhiteRooks.setText(Integer.toString(cursor.getCount()));
        }

        String whitePlayer = "";
        String blackPlayer = "";
        cursor = dbManager.getGame(date);
        if (cursor.moveToFirst()) {
            whitePlayer = cursor.getString(cursor.getColumnIndex(DBManager.GAME_COL_WHITES));
            blackPlayer = cursor.getString(cursor.getColumnIndex(DBManager.GAME_COL_BLACKS));
        }

        TextView textViewWhiteGames = findViewById(R.id.white_games);
        cursor = dbManager.getTotalGames(whitePlayer);
        if (cursor.moveToFirst()) {
            textViewWhiteGames.setText(Integer.toString(cursor.getCount()));
        }
        TextView textViewBlackGames = findViewById(R.id.black_games);
        cursor = dbManager.getTotalGames(blackPlayer);
        if (cursor.moveToFirst()) {
            textViewBlackGames.setText(Integer.toString(cursor.getCount()));
        }

        TextView textViewWhiteWins = findViewById(R.id.white_wins);
        cursor = dbManager.getWhiteWins(whitePlayer, blackPlayer);
        if (cursor.moveToFirst()) {
            textViewWhiteWins.setText(Integer.toString(cursor.getCount()));
        }
        TextView textViewBlackWins = findViewById(R.id.black_wins);
        cursor = dbManager.getBlackWins(whitePlayer, blackPlayer);
        if (cursor.moveToFirst()) {
            textViewBlackWins.setText(Integer.toString(cursor.getCount()));
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
