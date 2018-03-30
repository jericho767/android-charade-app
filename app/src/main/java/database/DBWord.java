package database;

public class DBWord {
    public static final int TEXT_LENGTH_MIN = 1;
    public static final int TEXT_LENGTH_MAX = 60;

    private long mId;
    private String mText;
    private long mPlayListId;

    public DBWord(String text) {
        mText = text;
        mId = 0;
        mPlayListId = 0;
    }

    DBWord(long id, String text, long playListId) {
        mId = id;
        mText = text;
        mPlayListId = playListId;
    }

    public long getId() {
        return mId;
    }

    public String getText() {
        return mText;
    }

}
