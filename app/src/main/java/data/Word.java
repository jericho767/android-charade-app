package data;

import java.util.ArrayList;

/**
 * This is the Word class.<br>
 * I will let you figure it out, what it is.
 */
public class Word {
    private String mText;
    private int mPassCount;
    private int mCheckedAtRound;

    private Word(String mText) {
        this.mText = mText;
        this.mPassCount = 0;
        this.mCheckedAtRound = 0;
    }

    static ArrayList<Word> createWords(ArrayList<String> texts) {
        ArrayList<Word> words = new ArrayList<>();

        for (String text : texts) {
            words.add(new Word(text));
        }

        return words;
    }

    public String getText() {
        return mText;
    }

    int getPassCount() {
        return mPassCount;
    }

    void incrementPassCount() {
        this.mPassCount++;
    }

    void setCheckedAtRound(int mCheckedAtRound) {
        this.mCheckedAtRound = mCheckedAtRound;
    }

    boolean isChecked() {
        return mCheckedAtRound > 0;
    }

    int getCheckedAtRound() {
        return mCheckedAtRound;
    }
}