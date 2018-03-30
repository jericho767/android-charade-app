package data;

import android.content.Context;

import database.DatabaseHelper;

public class PlayList {
    private DatabaseHelper db;
    private int mId;
    private String mName;

    // Use to RETRIEVE a PlayList instance using the playlist ID
    public PlayList(Context context, long id) {
        db = new DatabaseHelper(context);
        // TODO: Implement here retrieving
    }
}
