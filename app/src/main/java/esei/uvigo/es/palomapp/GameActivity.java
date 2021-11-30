package esei.uvigo.es.palomapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {
    private DBManager dbManager;

    private String date;
    private ImageButton[][] board;
    private boolean areOptionsAllowed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess);
        dbManager = new DBManager(getApplicationContext());
        date = getIntent().getExtras().getString("date");
        initGame();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.game_option_menu, menu);
        areOptionsAllowed = true;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        boolean selected = false;
        if (areOptionsAllowed) {
            switch (item.getItemId()) {
                case R.id.undo_game_option:
                    undo();
                    selected = true;
                    break;
                case R.id.history_game_option:
                    history();
                    selected = true;
                    break;
                case R.id.resume_game_option:
                    resume();
                    selected = true;
                    break;
                case R.id.finish_game_option:
                    finishGame();
                    selected = true;
                    break;
                case R.id.delete_game_option:
                    delete();
                    selected = true;
                    break;
            }
        }
        return selected;
    }

    private void finishGame() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("WHO WINS?");
        Cursor cursor = dbManager.getGame(date);
        if (cursor.moveToFirst()) {
            builder.setNegativeButton(cursor.getString(2), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dbManager.updateWinner(date, cursor.getString(2));
                    GameActivity.this.finish();
                }
            });
            builder.setPositiveButton(cursor.getString(3), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dbManager.updateWinner(date, cursor.getString(3));
                    GameActivity.this.finish();
                }
            });
            builder.setNeutralButton("DRAW", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dbManager.updateWinner(date, "Draw");
                    GameActivity.this.finish();
                }
            });
        }
        builder.create().show();
    }

    private void initGame() {
        initBoard();
        paintBoard();
        fillBoard();
        initActionBar();
    }

    private void initActionBar() {
        Cursor cursor = dbManager.getGame(date);
        if (cursor.moveToFirst()) {
            getSupportActionBar().setTitle(cursor.getString(2) + " VS " + cursor.getString(3));
        } else {
            getSupportActionBar().setTitle("WHITES VS BLACKS");
        }
    }

    private void fillBoard() {
        WHITES[] whiteRow = {
                WHITES.ROOK,
                WHITES.KNIGHT,
                WHITES.BISHOP,
                WHITES.KING,
                WHITES.QUEEN,
                WHITES.BISHOP,
                WHITES.KNIGHT,
                WHITES.ROOK
        };
        BLACKS[] blackRow = {
                BLACKS.ROOK,
                BLACKS.KNIGHT,
                BLACKS.BISHOP,
                BLACKS.KING,
                BLACKS.QUEEN,
                BLACKS.BISHOP,
                BLACKS.KNIGHT,
                BLACKS.ROOK
        };
        for (int i = 0; i < board.length; i++) {
            board[0][i].setImageResource(whiteRow[i].getIcon());
            board[0][i].setTag(R.string.piece_icon, whiteRow[i].getIcon());

            board[1][i].setImageResource(WHITES.PAWN.getIcon());
            board[1][i].setTag(R.string.piece_icon, WHITES.PAWN.getIcon());

            board[6][i].setImageResource(BLACKS.PAWN.getIcon());
            board[6][i].setTag(R.string.piece_icon, BLACKS.PAWN.getIcon());

            board[7][i].setImageResource(blackRow[i].getIcon());
            board[7][i].setTag(R.string.piece_icon, blackRow[i].getIcon());
        }
        Cursor cursor = dbManager.getHistory(date);
        if (cursor.moveToFirst()) {
            do {
                int sourcePieceIcon = cursor.getInt(cursor.getColumnIndex(DBManager.HISTORY_COL_SOURCE_PIECE_ICON));
                int sourcePieceRow = cursor.getInt(cursor.getColumnIndex(DBManager.HISTORY_COL_SOURCE_PIECE_ROW));
                int sourcePieceColumn = cursor.getInt(cursor.getColumnIndex(DBManager.HISTORY_COL_SOURCE_PIECE_COLUMN));

                int destinyPieceRow = cursor.getInt(cursor.getColumnIndex(DBManager.HISTORY_COL_DESTINY_PIECE_ROW));
                int destinyPieceColumn = cursor.getInt(cursor.getColumnIndex(DBManager.HISTORY_COL_DESTINY_PIECE_COLUMN));

                board[sourcePieceRow][sourcePieceColumn].setImageResource(WHITES.EMPTY.getIcon());
                board[sourcePieceRow][sourcePieceColumn].setTag(R.string.piece_icon, WHITES.EMPTY.getIcon());

                board[destinyPieceRow][destinyPieceColumn].setImageResource(sourcePieceIcon);
                board[destinyPieceRow][destinyPieceColumn].setTag(R.string.piece_icon, sourcePieceIcon);
            } while (cursor.moveToNext());
        }
    }

    private void initBoard() {
        final ImageButton takenPiece = new ImageButton(this);
        takenPiece.setTag(R.string.piece_icon, WHITES.EMPTY.getIcon());
        board = new ImageButton[8][8];
        LinearLayout linearLayoutBoard = findViewById(R.id.board);
        LinearLayout linearLayoutRow;
        for (int i = 0; i < board.length; i++) {
            linearLayoutRow = (LinearLayout) linearLayoutBoard.getChildAt(i);
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = (ImageButton) linearLayoutRow.getChildAt(j);
                board[i][j].setTag(R.string.row, i);
                board[i][j].setTag(R.string.column, j);
                board[i][j].setImageResource(WHITES.EMPTY.getIcon());
                board[i][j].setTag(R.string.piece_icon, WHITES.EMPTY.getIcon());
                board[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ImageButton imageButton = (ImageButton) view;
                        if (!imageButton.getTag(R.string.piece_icon).equals(WHITES.EMPTY.getIcon())
                                && takenPiece.getTag(R.string.piece_icon).equals(WHITES.EMPTY.getIcon())) {
                            areOptionsAllowed = false;
                            dbManager.addHistory(
                                    date,
                                    (Integer) imageButton.getTag(R.string.piece_icon),
                                    (Integer) imageButton.getTag(R.string.row),
                                    (Integer) imageButton.getTag(R.string.column));
                            takenPiece.setTag(R.string.piece_icon, (Integer) imageButton.getTag(R.string.piece_icon));
                            imageButton.setImageResource(WHITES.EMPTY.getIcon());
                            imageButton.setTag(R.string.piece_icon, WHITES.EMPTY.getIcon());
                        } else if (!takenPiece.getTag(R.string.piece_icon).equals(WHITES.EMPTY.getIcon())) {
                            areOptionsAllowed = true;
                            dbManager.updateHistory(
                                    date,
                                    (Integer) imageButton.getTag(R.string.piece_icon),
                                    (Integer) imageButton.getTag(R.string.row),
                                    (Integer) imageButton.getTag(R.string.column));
                            imageButton.setImageResource((Integer) takenPiece.getTag(R.string.piece_icon));
                            imageButton.setTag(R.string.piece_icon, (Integer) takenPiece.getTag(R.string.piece_icon));
                            takenPiece.setTag(R.string.piece_icon, WHITES.EMPTY.getIcon());
                        }
                    }
                });
            }
        }
    }

    private void paintBoard() {
        Cursor cursor = dbManager.getGame(date);
        int customColor;
        if (cursor.moveToFirst()) {
            customColor = cursor.getInt(cursor.getColumnIndex(DBManager.GAME_COL_BOARD_COLOR));
        } else {
            customColor = Color.GRAY;
        }
        for (int i = 0; i < board.length; i += 2) {
            for (int j = 0; j < board[i].length; j += 2) {
                board[i][j].setBackgroundColor(Color.WHITE);
            }
            for (int j = 1; j < board[i].length; j += 2) {
                board[i][j].setBackgroundColor(customColor);
            }
        }
        for (int i = 1; i < board.length; i += 2) {
            for (int j = 0; j < board[i].length; j += 2) {
                board[i][j].setBackgroundColor(customColor);
            }
            for (int j = 1; j < board[i].length; j += 2) {
                board[i][j].setBackgroundColor(Color.WHITE);
            }
        }
    }

    private void history() {
        Intent intent = new Intent(this, HistoryActivity.class);
        intent.putExtra("date", date);
        GameActivity.this.startActivity(intent);
    }

    private void resume() {
        Intent intent = new Intent(this, ResumeActivity.class);
        intent.putExtra("date", date);
        GameActivity.this.startActivity(intent);
    }

    private void undo() {
        Cursor cursor = dbManager.getHistory(date);
        if (cursor.moveToLast()) {
            int rowId = cursor.getInt(cursor.getColumnIndex(DBManager.HISTORY_COL_ID));

            int sourcePieceIcon = cursor.getInt(cursor.getColumnIndex(DBManager.HISTORY_COL_SOURCE_PIECE_ICON));
            int sourcePieceRow = cursor.getInt(cursor.getColumnIndex(DBManager.HISTORY_COL_SOURCE_PIECE_ROW));
            int sourcePieceColumn = cursor.getInt(cursor.getColumnIndex(DBManager.HISTORY_COL_SOURCE_PIECE_COLUMN));

            int destinyPieceIcon = cursor.getInt(cursor.getColumnIndex(DBManager.HISTORY_COL_DESTINY_PIECE_ICON));
            int destinyPieceRow = cursor.getInt(cursor.getColumnIndex(DBManager.HISTORY_COL_DESTINY_PIECE_ROW));
            int destinyPieceColumn = cursor.getInt(cursor.getColumnIndex(DBManager.HISTORY_COL_DESTINY_PIECE_COLUMN));

            board[sourcePieceRow][sourcePieceColumn].setImageResource(sourcePieceIcon);
            board[sourcePieceRow][sourcePieceColumn].setTag(R.string.piece_icon, sourcePieceIcon);

            board[destinyPieceRow][destinyPieceColumn].setImageResource(destinyPieceIcon);
            board[destinyPieceRow][destinyPieceColumn].setTag(R.string.piece_icon, destinyPieceIcon);

            dbManager.deleteHistory(rowId);
        }
    }

    private void delete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure about delete this games?");
        builder.setNegativeButton("No", null);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dbManager.deleteGame(date);
                GameActivity.this.finish();
            }
        });
        builder.create().show();
    }
}

