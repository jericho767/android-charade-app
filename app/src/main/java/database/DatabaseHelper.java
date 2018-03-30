package database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

@SuppressLint({"DefaultLocale", "Recycle"})
public class DatabaseHelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;

    public DatabaseHelper(Context context) {
        super(context, DatabaseContract.DATABASE_NAME
                , null, DatabaseContract.DATABASE_VERSION);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DatabaseContract.PlayList.CREATE_TABLE);
        sqLiteDatabase.execSQL(DatabaseContract.Word.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DatabaseContract.PlayList.DROP_TABLE);
        sqLiteDatabase.execSQL(DatabaseContract.Word.DROP_TABLE);
        onCreate(sqLiteDatabase);
    }

    public long insertPlaylist(String name) {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.PlayList.COLUMN_NAME, name);

        return db.insert(DatabaseContract.PlayList.TABLE_NAME, null, values);
    }

    public long insertWord(String text, long playlistId) {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.Word.COLUMN_TEXT, text);
        values.put(DatabaseContract.Word.COLUMN_PLAYLIST_ID, playlistId);

        return db.insert(DatabaseContract.Word.TABLE_NAME, null, values);
    }

    public ArrayList<DBWord> selectWordsFromPlayList(long playListId) {
        Cursor res = db.rawQuery(String.format(DatabaseContract.Word.SELECT_BY_PLAYLIST_ID
                , playListId), null);

        ArrayList<DBWord> words = new ArrayList<>();

        if (res.getCount() != 0) {
            while(res.moveToNext()) {
                words.add(new DBWord(res.getLong(0), res.getString(1), res.getLong(2)));
            }
        }

        return words;
    }

    public ArrayList<DBPlayList> selectAllPlayLists() {
        Cursor res = db.rawQuery(DatabaseContract.PlayList.SELECT_ALL, null);

        ArrayList<DBPlayList> playLists = new ArrayList<>();

        if (res.getCount() != 0) {
            while (res.moveToNext()) {
                playLists.add(new DBPlayList(res.getLong(0), res.getString(1)));
            }
        }

        return playLists;
    }

    public DBPlayList selectPlayListById(long id) {
        Cursor res = db.rawQuery(String.format(DatabaseContract.PlayList.SELECT_BY_ID
                , id), null);

        if (res.getCount() != 0) {
            return new DBPlayList(res.getLong(0), res.getString(1));
        } else {
            return null;
        }
    }
}
