package data;

import java.util.ArrayList;
import java.util.Random;

import database.DBWord;

public class Game {
    private ArrayList<Word> mWords;

    // Index of the current word in the poll of words
    private int mCurrentWordIndex;

    private int mCurrentRoundCounter;
    private ArrayList<Integer> mCurrentRoundPassedWordIndeces;
    private ArrayList<Integer> mCurrentRoundCheckedWordIndeces;

    /**
     * Settings
     * */
    // Should include passed words through out the game
    private boolean mIsIncludePassed;
    // number of passes
    private int mMaxPasses;

    public Game(int maxPasses, ArrayList<DBWord> dbWords, boolean isIncludePassed) {
        mMaxPasses = maxPasses;
        mWords = Word.createWords(dbWords);
        mIsIncludePassed = isIncludePassed;
        mCurrentWordIndex = -1;
        mCurrentRoundCounter = 0;
        mCurrentRoundPassedWordIndeces = new ArrayList<>();
        mCurrentRoundCheckedWordIndeces = new ArrayList<>();
    }

    public int getMaxPasses() {
        return mMaxPasses;
    }

    public int getCurrentRoundNumber() {
        return mCurrentRoundCounter;
    }

    public void startRound() {
        if (mCurrentRoundCounter < 1) {
            mCurrentRoundCounter = 1;
        } else {
            mCurrentRoundCounter++;
        }

        mCurrentRoundPassedWordIndeces = new ArrayList<>();
        mCurrentRoundCheckedWordIndeces = new ArrayList<>();
    }

    public void endRound() {
        mCurrentWordIndex = -1;
        mCurrentRoundPassedWordIndeces = new ArrayList<>();
        mCurrentRoundCheckedWordIndeces = new ArrayList<>();
    }

    public int getCurrentWordIndex () {
        return mCurrentWordIndex;
    }

    public Word getCurrentWord() {
        return mWords.get(mCurrentWordIndex);
    }

    /**
     * Yow! Word.
     *
     * @return returns a word.
     */
    public Word getWord() {
        ArrayList<Integer> pollOfWords = getPollOfWords();
        int countOfPollOfWords = pollOfWords.size();

        if (countOfPollOfWords < 1) {
            // No more words
            return null;
        } else {
            if (countOfPollOfWords < 2) {
                // There's only 1 word index left, get that.
                mCurrentWordIndex = pollOfWords.get(0);
            } else {
                Random rand = new Random();
                int index;

                do {
                    index = rand.nextInt(countOfPollOfWords);
                } while (pollOfWords.get(index) == mCurrentWordIndex);

                mCurrentWordIndex = pollOfWords.get(index);
            }

            return mWords.get(mCurrentWordIndex);
        }
    }

    /**
     * Uhhmm... Ahh.. PASS!
     */
    public void pass() {
        mCurrentRoundPassedWordIndeces.add(mCurrentWordIndex);
        mWords.get(mCurrentWordIndex).incrementPassCount();
    }

    /**
     * Make a fist. Tap chest with the fist TWICE! Kiss that fist. And point up.
     */
    public void check() {
        if (!mWords.get(mCurrentWordIndex).isChecked()) {
            mCurrentRoundCheckedWordIndeces.add(mCurrentWordIndex);
            mWords.get(mCurrentWordIndex).setCheckedAtRound(mCurrentRoundCounter);
        }
    }

    public ArrayList<Word> getUncheckedWordsInCurrentRound() {
        ArrayList<Word> words = getPassedWordsInCurrentRound();

        if (!mWords.get(mCurrentWordIndex).isChecked()) {
            // Include last word in the unchecked words
            words.add(mWords.get(mCurrentWordIndex));
        }

        return words;
    }

    public ArrayList<Word> getCheckedWordsInCurrentRound() {
        ArrayList<Word> checkedWords = new ArrayList<>();

        for (Integer index : mCurrentRoundCheckedWordIndeces) {
            checkedWords.add(mWords.get(index));
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
     * But yeah, it just returns a boolean, just run through the code, help yourself.
     *
     * @return
     *  <code>true</code> - can pass<br>
     *  <code>false</code> - cannot pass
     */
    public boolean canPass() {
        return getRemainingPasses() > 0;
    }

    /**
     * Count the remaining words that needs to be guessed
     *
     * @return Guess what?
     */
    public int pollOfWordsCount() {
        return getPollOfWords().size();
    }

    /**
     * Gets the indeces of the words that are still on the poll
     *
     * @return Read description. :)
     */
    private ArrayList<Integer> getPollOfWords() {
        ArrayList<Integer> pollOfWords = new ArrayList<>();
        Word word;

        for (int i = 0; i < mWords.size() ; i++) {
            word = mWords.get(i);
            if ((!word.isChecked() && word.getPassCount() < 1) ||
                    (!word.isChecked() && mIsIncludePassed)) {
                pollOfWords.add(i);
            }
        }

        return pollOfWords;
    }
}
