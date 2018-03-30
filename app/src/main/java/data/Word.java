package data;

/**
 * This is the Word class.<br>
 * I will let you figure it out, what it is.
 */
public class Word {
    private String mText;
    private int mPassCount;
    private int mCheckedAtRound;

    Word(String mText) {
        this.mText = mText;
        this.mPassCount = 0;
        this.mCheckedAtRound = 0;
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

    public boolean isChecked() {
        return mCheckedAtRound > 0;
    }

    int getCheckedAtRound() {
        return mCheckedAtRound;
    }
}