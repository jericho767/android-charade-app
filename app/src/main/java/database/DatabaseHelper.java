package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "miniproject-1";

    public static final String TABLE_WORD_NAME = "words";
    public static final String COL_WORD_ID = "id";
    public static final String COL_WORD_TEXT = "text";
    public static final String COL_WORD_PLAYLIST_ID = "playlist_id";

    public static final String TABLE_PLAYLIST_NAME = "playlists";
    public static final String COL_PLAYLIST_ID = "id";
    public static final String COL_PLAYLIST_NAME = "name";

    private SQLiteDatabase db;

    public DatabaseHelper(Context context, int version) {
        super(context, DATABASE_NAME, null, version);
        db = this.getWritableDatabase();
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        db = this.getWritableDatabase();
    }

    public DatabaseHelper(Context context, String name
            , SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(String.format("CREATE TABLE %s " +
                "(%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "%s TEXT, %s INTEGER FOREIGN KEY)",
                TABLE_WORD_NAME, COL_WORD_ID, COL_WORD_TEXT, COL_WORD_PLAYLIST_ID));

        sqLiteDatabase.execSQL(String.format("CREATE TABLE %s " +
                "(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT)",
                TABLE_PLAYLIST_NAME, COL_PLAYLIST_ID, COL_PLAYLIST_NAME));
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(String.format("DROP TABLE IF EXISTS %s", TABLE_WORD_NAME));
        sqLiteDatabase.execSQL(String.format("DROP TABLE IF EXISTS %s", TABLE_PLAYLIST_NAME));
        onCreate(sqLiteDatabase);
    }

    public long insertPlaylist(String name) {
        ContentValues values = new ContentValues();
        values.put(COL_PLAYLIST_NAME, name);

        return db.insert(TABLE_PLAYLIST_NAME, null, values);
    }

    public long insertWord(String text, String playlistId) {
        ContentValues values = new ContentValues();
        values.put(COL_WORD_TEXT, text);
        values.put(COL_WORD_PLAYLIST_ID, playlistId);

        return db.insert(TABLE_WORD_NAME, null, values);
    }
}
