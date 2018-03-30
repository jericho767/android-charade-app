package common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Locale;

import data.Game;
import sprobe.training.miniproject.R;

import static android.view.View.SYSTEM_UI_FLAG_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_IMMERSIVE;

public class Util {
    /**
     * Adds the toolbar. Simple. No hassle. <br><br>
     *
     * But really there's still something you need to do
     * like, include the toolbar layout to your <code>activity.xml</code>.
     *
     * @param activity Current activity. Usually <code>this</code> value will be passed here.
     * @param hasBackButton Should toolbar have back button?
     */
    public static void addToolbar(AppCompatActivity activity, boolean hasBackButton) {
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);

        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(hasBackButton);
        }
    }

    /**
     * Sets the title of the toolbar. Duh.
     *
     * @param activity Current activity. Usually <code>this</code> value will be passed here.
     * @param title I think this one is obvious. Or should I link you to StackOverflow?
     */
    public static void setToolbarTitle(AppCompatActivity activity, String title) {
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setTitle(title);
        }
    }

    /**
     * Go to next activity. Duh.
     *
     * @param baseContext Current activity. Usually <code>this</code> value will be passed here.
     * @param nextActivity Where will I go activity. Pass here: <code>new ClassActivity()</code>.
     */
    public static void nextActivity(Context baseContext, AppCompatActivity nextActivity) {
        Intent intent = new Intent(baseContext, nextActivity.getClass());
        baseContext.startActivity(intent);
    }

    /**
     * Go to next activity. Duh.
     *
     * @param baseContext Current activity. Usually <code>this</code> value will be passed here.
     * @param nextActivity Where will I go activity. Pass here: <code>new ClassActivity()</code>.
     * @param bundle The bundle you are passing. Is it not obvious?
     */
    public static void nextActivity(Context baseContext
            , AppCompatActivity nextActivity, Bundle bundle) {
        Intent intent = new Intent(baseContext, nextActivity.getClass());
        intent.putExtras(bundle);
        baseContext.startActivity(intent);
    }

    /**
     * I don't know about this one. I remembered this in a dream and forgot about it
     * in another dream.
     *
     * @param activity the current activity (usually you give it a <code>Activity.this</code>
     */
    public static void goFullscreen(AppCompatActivity activity) {
        activity.getWindow().getDecorView().setSystemUiVisibility(SYSTEM_UI_FLAG_FULLSCREEN
                | SYSTEM_UI_FLAG_HIDE_NAVIGATION | SYSTEM_UI_FLAG_IMMERSIVE);
    }

    /**
     * Want to tell something? This is your guy.
     *
     * @param baseContext it's just <code>Activity.this</code> or a <code>this</code>
     * @param toast a <code>Toast</code> class
     * @param message what you want to shout and tell. Like how was your day? Was it awesome?
     *
     * @return remember that <code>Toast</code> class you've given? I'm giving it back.
     */
    public static Toast showToast(Context baseContext, Toast toast, String message) {
        try {
            toast.setText(message);

            if (!toast.getView().isShown()) {
                toast.show();
            }
        } catch (Exception e) {
            toast.cancel();
            toast = Toast.makeText(baseContext, message, Toast.LENGTH_LONG);
            toast.show();
        }

        return toast;
    }

    /**
     * Use this when that annoying system UI shows. It will hide it for you. FOR GOOD!<br>
     * HAHAHA! I'm just kidding. What I mean hide is, just hide it. Like: "Hey! you shouldn't
     * be here", that kind of hide? You know what I mean.
     *
     * @param activity the current activity (usually you give it a <code>Activity.this</code>
     */
    public static void hideTheSystemUiWhenShown(final AppCompatActivity activity) {
        final int TIME_HIDE_AFTER = 2000;
        final View.OnSystemUiVisibilityChangeListener listener = new
                View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    Util.goFullscreen(activity);
                                }
                            };

                            new Handler().postDelayed(runnable, TIME_HIDE_AFTER);
                        }
                    }
                };

        activity.getWindow().getDecorView()
                .setOnSystemUiVisibilityChangeListener(listener);
    }

    /**
     * Humans like to have it divided. Like: seconds, minutes, hours, days, weeks, etc, etc.
     *
     * @param seconds the number of seconds that will be divided
     *
     * @return give that human what it wants. One that is not a sore in his eyes.
     */
    public static String formatSecondsToTime(long seconds) {
        String output = "";
        int min = Integer.parseInt(String.valueOf(seconds / 60));
        int sec = Integer.parseInt(String.valueOf(seconds % 60));

        if (min < 10) { // Pad a zero in the minute mark
            output += "0";
        }

        output += min + ":";

        if (sec < 10) { // Pad a zero in the second mark
            output += "0";
        }

        output += sec;

        return output;
    }

    /**
     * Give me <code>Game</code> instance, i give you string.
     *
     * @param game <code>Game</code> instance you giving me
     *
     * @return Json string i give you
     */
    public static String gameToJson(Game game) {
        return new Gson().toJson(game);
    }

    /**
     * You want <code>Game</code> back? Okay, give me back my string.
     *
     * @param gameJson json string of game
     *
     * @return <code>Game</code> class
     */
    public static Game jsonToGame(String gameJson) {
        return new Gson().fromJson(gameJson, Game.class);
    }

    /**
     * Gets the locale. Like really. HAHAHA! I don't know about this one seriously *sobs*
     *
     * @return returns that locale.
     */
    public static Locale getLocale() {
        return Locale.getDefault();
    }

}
