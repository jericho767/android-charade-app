package database;

import java.util.ArrayList;

public class DBPlayList {
    public static final int NAME_LENGTH_MIN = 1;
    public static final int NAME_LENGTH_MAX = 16;

    private long mId;
    private String mName;
    private ArrayList<DBWord> mWords;

    DBPlayList(long id, String name) {
        mId = id;
        mName = name;
    }

    void setWords(ArrayList<DBWord> words) {
        mWords = words;
    }

    public ArrayList<DBWord> getWords() {
        return mWords;
    }

    public String getName() {
        return mName;
    }

    public long getId() {
        return mId;
    }
}
