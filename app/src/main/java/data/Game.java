package data;

import java.util.ArrayList;
import java.util.Random;

public class Game {
    public static final String MESSAGE_CANNOT_PASS = "You shall not pass!";

    private ArrayList<Word> mWords;

    // Index of the current word in the poll of words
    private int mCurrentWordIndex;

    private int mCurrentRoundCounter;
    private ArrayList<Integer> mCurrentRoundPassedWordIndeces;

    /***
     * Settings
     * */
    // Should include passed words through out the game
    private boolean mIsIncludePassed;
    // number of passes
    private int mMaxPasses;

    public Game(int mMaxPasses, String[] words, boolean mIsIncludePassed) {
        this.mMaxPasses = mMaxPasses;
        this.mWords = createWords(words);
        this.mIsIncludePassed = mIsIncludePassed;
    }

    private ArrayList<Word> createWords(String[] words) {
        ArrayList<Word> mWords = new ArrayList<>();

        for (String word : words) {
            mWords.add(new Word(word));
        }

        return mWords;
    }

    public void startRound() {
        if (mCurrentRoundCounter < 1) {
            mCurrentRoundCounter = 1;
        } else {
            mCurrentRoundCounter++;
        }

        mCurrentRoundPassedWordIndeces = new ArrayList<>();
    }

    public void endRound() {
        mCurrentRoundPassedWordIndeces = new ArrayList<>();
    }

    public Word getCurentWord() {
        return mWords.get(mCurrentWordIndex);
    }

    // TODO: Add javadoc
    public Word getWord() {
        ArrayList<Integer> pollOfWords = new ArrayList<>();
        Word word;

        for (int i = 0; i < mWords.size() ; i++) {
            word = mWords.get(i);
            if ((!word.isChecked() && word.getPassCount() < 1) ||
                    (!word.isChecked() && mIsIncludePassed)) {
                pollOfWords.add(i);
            }
        }

        int countOfPollOfWords = pollOfWords.size();

        if (countOfPollOfWords < 1) {
            // No more words
            return null;
        } else {
            Random rand = new Random();
            mCurrentWordIndex = pollOfWords.get(rand.nextInt(countOfPollOfWords));
            return mWords.get(mCurrentWordIndex);
        }
    }

    // TODO: Add javadoc
    public void pass() {
        mCurrentRoundPassedWordIndeces.add(mCurrentWordIndex);
        mWords.get(mCurrentWordIndex).incrementPassCount();
    }

    // TODO: Add javadoc
    public void check() {
        mWords.get(mCurrentWordIndex).setCheckedAtRound(mCurrentRoundCounter);
    }

    public ArrayList<Word> getCheckedWordsInCurrentRound() {
        ArrayList<Word> checkedWords = new ArrayList<>();

        for (Word word : mWords) {
            if (word.getCheckedAtRound() == mCurrentRoundCounter) {
                checkedWords.add(word);
            }
        }

        return checkedWords;
    }

    public ArrayList<Word> getPassedWordsInCurrentRound() {
        ArrayList<Word> passedWords = new ArrayList<>();

        for (Integer index : mCurrentRoundPassedWordIndeces) {
            passedWords.add(mWords.get(index));
        }

        return passedWords;
    }

    public int getRemainingPasses() {
        return mMaxPasses - mCurrentRoundPassedWordIndeces.size();
    }

    /**
     * I don't know about this one... it's just too complicated.
     * But yeah, it just returns a boolean, just run through the code.
     *
     * @return
     *  <code>true</code> - can pass<br>
     *  <code>false</code> - cannot pass
     */
    public boolean canPass() {
        return getRemainingPasses() > 0;
    }

}
