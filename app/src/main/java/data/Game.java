package data;

import java.util.ArrayList;
import java.util.Random;

public class Game {
    // The words poll
    private ArrayList<Word> mWords;

    // Index of the current word in the poll of words
    private int mCurrentWordIndex;

    private int mCurrentRound;
    private int mCurrentRoundPassCount;

    /***
     * Settings
     * */
    // Should include passed words through out the game
    private boolean mIsIncludePassed;
    // number of passes
    private int mMaxPasses;

    public Game(int mMaxPasses, ArrayList<String> words, boolean mIsIncludePassed) {
        this.mMaxPasses = mMaxPasses;
        this.mWords = createWords(words);
        this.mIsIncludePassed = mIsIncludePassed;
    }

    private ArrayList<Word> createWords(ArrayList<String> words) {
        ArrayList<Word> mWords = new ArrayList<>();

        for (String word : words) {
            mWords.add(new Word(word));
        }

        return mWords;
    }

    public String startRound() {
        if (mCurrentRound < 1) {
            mCurrentRound = 1;
        } else {
            mCurrentRound++;
        }

        mCurrentRoundPassCount = 0;
        return getWord();
    }

    public void endRound() {
        mCurrentRoundPassCount = 0;
    }

    // TODO: Add javadoc
    public String pass() {
        mCurrentRoundPassCount++;
        mWords.get(mCurrentWordIndex).incrementPassCount();
        return getWord();
    }

    // TODO: Add javadoc
    public String check() {
        mWords.get(mCurrentWordIndex).setmCheckedAtRound(mCurrentRound);
        return getWord();
    }

    public boolean canPass() {
        if (mCurrentRoundPassCount < mMaxPasses) {
            return true;
        }

        return false;
    }

    // TODO: Add javadoc
    public String getWord() {
        Random rand = new Random();
        int wordsCount = mWords.size();

        if (wordsCount == 0) {
            if (mIsIncludePassed) {
                ArrayList<Word> passedWords = fetchWordsFromPassedWords();
                if (passedWords.size() < 1) {
                    // No more words
                    return null;
                } else {
                    this.mWords = passedWords;
                    getWord();
                }
            } else {
                // No more words
                return null;
            }
        }

        mCurrentWordIndex = rand.nextInt(wordsCount - 1);
        return mWords.get(mCurrentWordIndex).getmWord();
    }

    private ArrayList<Word> fetchWordsFromPassedWords() {
        ArrayList<Word> words = new ArrayList<>();

        for (Word word : mWords) {
            if (word.mCheckedAtRound == 0) {
                words.add(word);
            }
        }

        return words;
    }

    public class Word {
        private String mWord;
        private int mPassCount;
        private int mCheckedAtRound;

        public Word(String mWord) {
            this.mWord = mWord;
            this.mPassCount = 0;
            this.mCheckedAtRound = 0;
        }

        public String getmWord() {
            return mWord;
        }

        public int getmPassCount() {
            return mPassCount;
        }

        public void incrementPassCount() {
            this.mPassCount++;
        }

        public void setmCheckedAtRound(int mCheckedAtRound) {
            this.mCheckedAtRound = mCheckedAtRound;
        }

        public int getmCheckedAtRound() {
            return mCheckedAtRound;
        }
    }
}
