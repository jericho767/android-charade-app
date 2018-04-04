package database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import common.Util;

@SuppressLint({"DefaultLocale", "Recycle"})
public class DatabaseHelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;

    public DatabaseHelper(Context context) {
        super(context, DatabaseContract.DATABASE_NAME
                , null, DatabaseContract.DATABASE_VERSION);
        db = getWritableDatabase();
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

    public ArrayList<DBWord> selectWordsByPlayListId(long playListId) {
        Cursor res = db.rawQuery(String.format(DatabaseContract.Word.SELECT_BY_PLAYLIST_ID
                , playListId), null);

        ArrayList<DBWord> words = new ArrayList<>();

        if (res.getCount() != 0) {
            while(res.moveToNext()) {
                words.add(DatabaseContract.Word.getWordFromCursor(res));
            }
        }

        return words;
    }

    public ArrayList<DBPlayList> selectPlayListById(ArrayList<Long> playListIds) {
        Cursor res = db.rawQuery(
                String.format(DatabaseContract.PlayList.SELECT_IN_IDs
                        , Util.implode(playListIds))
                , null);

        ArrayList<DBPlayList> playLists = new ArrayList<>();
        DBPlayList playList;

        if (res.getCount() != 0) {
            while (res.moveToNext()) {
                playList = DatabaseContract.PlayList.getPlayListFromCursor(res);

                // TODO: Soon implement condition if to add words or not
                playList.setWords(selectWordsByPlayListId(playList.getId()));

                playLists.add(playList);
            }
        }

        return playLists;
    }

    public ArrayList<DBPlayList> selectAllPlayLists() {
        Cursor res = db.rawQuery(DatabaseContract.PlayList.SELECT_ALL, null);

        ArrayList<DBPlayList> playLists = new ArrayList<>();

        if (res.getCount() != 0) {
            while (res.moveToNext()) {
                playLists.add(DatabaseContract.PlayList.getPlayListFromCursor(res));
            }
        }

        return playLists;
    }

    public DBPlayList selectPlayListById(long id) {
        Cursor res = db.rawQuery(String.format(DatabaseContract.PlayList.SELECT_BY_ID
                , id), null);

        if (res.getCount() != 0) {
            DBPlayList playList = null;

            while (res.moveToNext()) {
                playList = DatabaseContract.PlayList.getPlayListFromCursor(res);
            }

            return playList;
        } else {
            return null;
        }
    }

    public Integer deletePlayListById(long id) {
        deleteWordsByPlayListId(id);

        return db.delete(DatabaseContract.PlayList.TABLE_NAME
                , DatabaseContract.PlayList.COLUMN_ID + " = ?"
                , new String[] {String.valueOf(id)});
    }

    public Integer deleteWordById(long id) {
        return db.delete(DatabaseContract.Word.TABLE_NAME
                , DatabaseContract.Word.COLUMN_ID + " = ?"
                , new String[] {String.valueOf(id)});
    }

    private void deleteWordsByPlayListId(long playlistId) {
        db.delete(DatabaseContract.Word.TABLE_NAME
                , DatabaseContract.Word.COLUMN_PLAYLIST_ID + " = ?"
                , new String[] {String.valueOf(playlistId)});
    }

}
