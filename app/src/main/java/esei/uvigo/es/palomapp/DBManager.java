package esei.uvigo.es.palomapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBManager extends SQLiteOpenHelper {
    public static final String DB_NAME = "Chess";
    public static final int DB_VERSION = 1;

    public static final String TABLE_GAME = "game";
    public static final String GAME_COL_DATE = "_id";
    public static final String GAME_COL_BOARD_COLOR = "board_color";
    public static final String GAME_COL_WHITES = "whites";
    public static final String GAME_COL_BLACKS = "blacks";
    public static final String GAME_COL_WINNER = "winner";

    public static final String TABLE_HISTORY = "history";
    public static final String HISTORY_COL_ID = "_id";
    public static final String HISTORY_COL_DATE = "date";
    public static final String HISTORY_COL_SOURCE_PIECE_ICON = "source_piece_icon";
    public static final String HISTORY_COL_SOURCE_PIECE_ROW = "source_piece_row";
    public static final String HISTORY_COL_SOURCE_PIECE_COLUMN = "source_piece_column";
    public static final String HISTORY_COL_DESTINY_PIECE_ICON = "destiny_piece_icon";
    public static final String HISTORY_COL_DESTINY_PIECE_ROW = "destiny_piece_row";
    public static final String HISTORY_COL_DESTINY_PIECE_COLUMN = "destiny_piece_column";

    public DBManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("DBManager", "Creating Data Base " + DB_NAME + " v" + DB_VERSION);
        try {
            db.beginTransaction();

            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_GAME + "( "
                    + GAME_COL_DATE + " TEXT NOT NULL PRIMARY KEY DEFAULT 'yyyy-MM-dd HH:mm:ss', "
                    + GAME_COL_BOARD_COLOR + " INTEGER NOT NULL DEFAULT 0, "
                    + GAME_COL_WHITES + " TEXT NOT NULL DEFAULT 'Whites', "
                    + GAME_COL_BLACKS + " TEXT NOT NULL DEFAULT 'Blacks', "
                    + GAME_COL_WINNER + " TEXT NOT NULL DEFAULT 'In progress')");

            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_HISTORY + "( "
                    + HISTORY_COL_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    + HISTORY_COL_DATE + " TEXT NOT NULL, "

                    + HISTORY_COL_SOURCE_PIECE_ICON + " INTEGER NOT NULL DEFAULT 0, "
                    + HISTORY_COL_SOURCE_PIECE_ROW + " INTEGER NOT NULL DEFAULT 0, "
                    + HISTORY_COL_SOURCE_PIECE_COLUMN + " INTEGER NOT NULL DEFAULT 0, "

                    + HISTORY_COL_DESTINY_PIECE_ICON + " INTEGER NOT NULL DEFAULT 0, "
                    + HISTORY_COL_DESTINY_PIECE_ROW + " INTEGER NOT NULL DEFAULT 0, "
                    + HISTORY_COL_DESTINY_PIECE_COLUMN + " INTEGER NOT NULL DEFAULT 0, "

                    + "FOREIGN KEY(" + HISTORY_COL_DATE + ") "
                    + "REFERENCES " + TABLE_GAME + "(" + GAME_COL_DATE + "))");

            db.setTransactionSuccessful();
        } catch (SQLException exc) {
            Log.e("DBManager.onCreate", exc.getMessage());
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public Cursor getGames() {
        return getReadableDatabase().query(TABLE_GAME,
                null, null, null, null, null, null);
    }

    public Cursor getGames(String player1, String player) {
        return getReadableDatabase().query(TABLE_GAME,
                null,
                GAME_COL_WHITES + "=? AND " +
                        GAME_COL_BLACKS + "=? OR " +
                        GAME_COL_WHITES + "=? AND " +
                        GAME_COL_BLACKS + "=?",
                new String[]{player1, player,
                        player, player1},
                null, null, null);
    }

    public Cursor getGame(String date) {
        return this.getReadableDatabase().query(TABLE_GAME,
                null,
                GAME_COL_DATE + "=?", new String[]{date},
                null, null, null);
    }

    public Cursor getGames(String player) {
        return getReadableDatabase().query(TABLE_GAME,
                null,
                GAME_COL_WHITES + "=? OR " +
                        GAME_COL_BLACKS + "=?",
                new String[]{player, player},
                null, null, null);
    }

    public Cursor getHistory(String date) {
        return this.getReadableDatabase().query(TABLE_HISTORY,
                null,
                HISTORY_COL_DATE + "=?", new String[]{date},
                null, null, null);
    }

    public boolean addGame(String date, int boardColor, String whites, String blacks) {
        boolean insertSuccess = false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GAME_COL_DATE, date);
        values.put(GAME_COL_BOARD_COLOR, boardColor);
        values.put(GAME_COL_WHITES, whites);
        values.put(GAME_COL_BLACKS, blacks);
        try {
            db.beginTransaction();
            db.insert(TABLE_GAME, null, values);
            db.setTransactionSuccessful();
            insertSuccess = true;
        } catch (SQLException exc) {
            Log.e("DBManager.insertGame", exc.getMessage());
        } finally {
            db.endTransaction();
        }
        return insertSuccess;
    }


    public boolean deleteGame(String date) {
        boolean success = false;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            db.delete(TABLE_GAME, GAME_COL_DATE + "=?", new String[]{date});
            db.setTransactionSuccessful();
            success = true;
        } catch (SQLException exc) {
            Log.e("DBManager.deleteGame", exc.getMessage());
        } finally {
            db.endTransaction();
        }
        return success;
    }

    public boolean updateHistory(String date, int destinyPieceIcon, int destinyPieceRow, int destinyPieceColumn) {
        boolean success = false;
        Cursor cursor = getHistory(date);
        cursor.moveToLast();
        int rowId = cursor.getInt(cursor.getColumnIndex(HISTORY_COL_ID));
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HISTORY_COL_DESTINY_PIECE_ICON, destinyPieceIcon);
        values.put(HISTORY_COL_DESTINY_PIECE_ROW, destinyPieceRow);
        values.put(HISTORY_COL_DESTINY_PIECE_COLUMN, destinyPieceColumn);
        try {
            db.beginTransaction();
            db.update(TABLE_HISTORY, values, HISTORY_COL_ID + "=?", new String[]{Integer.toString(rowId)});
            db.setTransactionSuccessful();
            success = true;
        } catch (SQLException exc) {
            Log.e("DBManager.addHistory", exc.getMessage());
        } finally {
            db.endTransaction();
        }
        return success;
    }

    public boolean addHistory(String date, int sourcePieceIcon, int sourcePieceRow, int sourcePieceColumn) {
        boolean success = false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HISTORY_COL_DATE, date);
        values.put(HISTORY_COL_SOURCE_PIECE_ICON, sourcePieceIcon);
        values.put(HISTORY_COL_SOURCE_PIECE_ROW, sourcePieceRow);
        values.put(HISTORY_COL_SOURCE_PIECE_COLUMN, sourcePieceColumn);
        try {
            db.beginTransaction();
            db.insert(TABLE_HISTORY, null, values);
            db.setTransactionSuccessful();
            success = true;
        } catch (SQLException exc) {
            Log.e("DBManager.addHistory", exc.getMessage());
        } finally {
            db.endTransaction();
        }
        return success;
    }

    public boolean deleteHistory(int rowId) {
        boolean success = false;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            db.delete(TABLE_HISTORY, HISTORY_COL_ID + "=?", new String[]{Integer.toString(rowId)});
            db.setTransactionSuccessful();
            success = true;
        } catch (SQLException exc) {
            Log.e("DBManager.deleteGame", exc.getMessage());
        } finally {
            db.endTransaction();
        }
        return success;
    }

    public Cursor getTotalTakenPieces(String date, int pieceIcon) {
        return this.getReadableDatabase().query(TABLE_HISTORY,
                null,
                HISTORY_COL_DATE + "=? AND " +
                        HISTORY_COL_DESTINY_PIECE_ICON + "=?",
                new String[]{date,
                        Integer.toString(pieceIcon)},
                null, null, null);
    }

    public boolean updateWinner(String date, String player) {
        boolean success = false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GAME_COL_WINNER, player);
        try {
            db.beginTransaction();
            db.update(TABLE_GAME, values, GAME_COL_DATE + "=?", new String[]{date});
            db.setTransactionSuccessful();
            success = true;
        } catch (SQLException exc) {
            Log.e("DBManager.addHistory", exc.getMessage());
        } finally {
            db.endTransaction();
        }
        return success;
    }

    public Cursor getRanking() {
        return this.getReadableDatabase().rawQuery("SELECT _id, winner, count(winner) as total_wins " +
                        "from game " +
                        "group by winner " +
                        "having winner not in ('In progress', 'Draw') " +
                        "order by total_wins desc;",
                null);
    }

    public Cursor getWhiteWins(String whitePlayer, String blackPlayer) {
        return this.getReadableDatabase().query(TABLE_GAME,
                null,
                GAME_COL_WHITES + "=? AND " +
                        GAME_COL_BLACKS + "=? AND " +
                        GAME_COL_WINNER + "=?",
                new String[]{whitePlayer,
                        blackPlayer,
                        whitePlayer},
                null, null, null);
    }

    public Cursor getBlackWins(String whitePlayer, String blackPlayer) {
        return this.getReadableDatabase().query(TABLE_GAME,
                null,
                GAME_COL_WHITES + "=? AND " +
                        GAME_COL_BLACKS + "=? AND " +
                        GAME_COL_WINNER + "=?",
                new String[]{whitePlayer,
                        blackPlayer,
                        blackPlayer},
                null, null, null);
    }

    public Cursor getTotalGames(String player) {
        return this.getReadableDatabase().query(TABLE_GAME,
                null,
                GAME_COL_WHITES + "=? OR " +
                        GAME_COL_BLACKS + "=?",
                new String[]{player,
                        player},
                null, null, null);
    }

    public Cursor getWins(String player) {
        return this.getReadableDatabase().query(TABLE_GAME,
                null,
                GAME_COL_WINNER + "=?",
                new String[]{player},
                null, null, null);
    }

    public Cursor getInProgress(String player) {
        return this.getReadableDatabase().query(TABLE_GAME,
                null,
                "(" + GAME_COL_WHITES + "=? OR " +
                        GAME_COL_BLACKS + "=? ) AND " +
                        GAME_COL_WINNER + "= 'In progress'",
                new String[]{player,
                        player},
                null, null, null);
    }

    public Cursor getDraws(String player) {
        return this.getReadableDatabase().query(TABLE_GAME,
                null,
                "(" + GAME_COL_WHITES + "=? OR " +
                        GAME_COL_BLACKS + "=? ) AND " +
                        GAME_COL_WINNER + "= 'Draw'",
                new String[]{player,
                        player},
                null, null, null);
    }

    public Cursor getWins(String player1, String player2) {
        return this.getReadableDatabase().query(TABLE_GAME,
                null, GAME_COL_WINNER + " =? AND (" +
                        "( " + GAME_COL_WHITES + " =? AND " + GAME_COL_BLACKS + " =? ) OR " +
                        "( " + GAME_COL_BLACKS + " =? AND " + GAME_COL_WHITES + " =? ))",
                new String[]{player1,
                        player1, player2,
                        player1, player2},
                null, null, null);
    }

    public Cursor getDraws(String player1, String player2) {
        return this.getReadableDatabase().query(TABLE_GAME,
                null, GAME_COL_WINNER + " = 'Draw' AND (" +
                        "( " + GAME_COL_WHITES + " =? AND " + GAME_COL_BLACKS + " =? ) OR " +
                        "( " + GAME_COL_BLACKS + " =? AND " + GAME_COL_WHITES + " =? ))",
                new String[]{player1, player2,
                        player1, player2},
                null, null, null);
    }

    public Cursor getInProgress(String player1, String player2) {
        return this.getReadableDatabase().query(TABLE_GAME,
                null, GAME_COL_WINNER + " = 'In progress' AND (" +
                        "( " + GAME_COL_WHITES + " =? AND " + GAME_COL_BLACKS + " =? ) OR " +
                        "( " + GAME_COL_BLACKS + " =? AND " + GAME_COL_WHITES + " =? ))",
                new String[]{player1, player2,
                        player1, player2},
                null, null, null);
    }
}
