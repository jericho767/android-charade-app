package data;

import java.util.ArrayList;

import database.DBWord;

/**
 * This is the Word class.<br>
 * I will let you figure it out, what it is.
 */
public class Word {
    private String mText;
    private long mId;
    private int mPassCount;
    private int mCheckedAtRound;

    private Word(String text, long id) {
        mText = text;
        mId = id;
        mPassCount = 0;
        mCheckedAtRound = 0;
    }

    static ArrayList<Word> createWords(ArrayList<DBWord> dbWords) {
        ArrayList<Word> words = new ArrayList<>();

        for (DBWord dbWord : dbWords) {
            words.add(new Word(dbWord.getText(), dbWord.getId()));
        }

        return words;
    }

    public long getId() {
        return mId;
    }

    public String getText() {
        return mText;
    }

    int getPassCount() {
        return mPassCount;
    }

    void incrementPassCount() {
        mPassCount++;
    }

    void setCheckedAtRound(int checkedAtRound) {
        mCheckedAtRound = checkedAtRound;
    }

    boolean isChecked() {
        return mCheckedAtRound > 0;
    }

    int getCheckedAtRound() {
        return mCheckedAtRound;
    }
}