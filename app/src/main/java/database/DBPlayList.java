package database;

public class DBPlayList {
    public static final int NAME_LENGTH_MIN = 1;
    public static final int NAME_LENGTH_MAX = 16;

    private long mId;
    private String mName;

    DBPlayList(long id, String name) {
        mId = id;
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public long getId() {
        return mId;
    }
}
