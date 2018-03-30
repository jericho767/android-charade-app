package data;

import android.content.Context;

import database.DatabaseHelper;

public class PlayList {
    private DatabaseHelper db;
    private int mId;
    private String mName;

    public static final int NAME_LENGTH_MIN = 1;
    public static final int NAME_LENGTH_MAX = 16;

    // Use to RETRIEVE a PlayList instance using the playlist ID
    public PlayList(Context context, long id) {
        db = new DatabaseHelper(context);
        // TODO: Implement here retrieving
    }

}
