package database;

import android.provider.BaseColumns;

final class DatabaseContract {

    static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "miniproject";

    // Prevent accidentally initializing the class
    private DatabaseContract() {}

    static abstract class PlayList implements BaseColumns {
        static final String TABLE_NAME = "playlist";
        static final String COLUMN_ID = "id";
        static final String COLUMN_NAME = "name";

        static final String CREATE_TABLE = String.format("CREATE TABLE %s " +
                "(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT)"
                , TABLE_NAME, COLUMN_ID, COLUMN_NAME);
        static final String DROP_TABLE = String.format("DROP TABLE IF EXISTS %s",
                TABLE_NAME);
        static final String SELECT_ALL = String.format("SELECT * FROM %s", TABLE_NAME);
        static final String SELECT_BY_ID = "SELECT * FROM " + TABLE_NAME + " WHERE id = %d";
    }

    static abstract class Word implements BaseColumns {
        static final String TABLE_NAME = "word";
        static final String COLUMN_ID = "id";
        static final String COLUMN_TEXT = "text";
        static final String COLUMN_PLAYLIST_ID = "playlist_id";

        static final String CREATE_TABLE = String.format("CREATE TABLE %s " +
                "(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s INTEGER" +
                ", FOREIGN KEY (%s) REFERENCES %s(%s))"
                , TABLE_NAME, COLUMN_ID, COLUMN_TEXT, COLUMN_PLAYLIST_ID
                , COLUMN_PLAYLIST_ID, PlayList.TABLE_NAME, PlayList.COLUMN_ID);
        static final String DROP_TABLE = String.format("DROP TABLE IF EXISTS %s",
                TABLE_NAME);
        static final String SELECT_BY_PLAYLIST_ID = "SELECT * FROM " + TABLE_NAME +
                " WHERE " + COLUMN_PLAYLIST_ID + " = %d";
    }
}
