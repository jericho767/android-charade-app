package common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import data.PlayList;
import sprobe.training.miniproject.R;

import static android.view.View.SYSTEM_UI_FLAG_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_IMMERSIVE;

public class Util {
    /**
     * Adds the toolbar. Simple. No hassle.
     *
     * @param activity Current activity. Usually <code>this</code> value will be passed here.
     */
    public static void addToolbar(AppCompatActivity activity, boolean hasBackButton) {
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(hasBackButton);
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
    public static void nextActivity(Context baseContext,
                                    AppCompatActivity nextActivity, Bundle bundle) {
        Intent intent = new Intent(baseContext, nextActivity.getClass());
        intent.putExtras(bundle);
        baseContext.startActivity(intent);
    }

    // TODO: Add javadoc
    public static void goFullscreen(AppCompatActivity activity) {
        activity.getWindow().getDecorView().setSystemUiVisibility(SYSTEM_UI_FLAG_FULLSCREEN
                | SYSTEM_UI_FLAG_HIDE_NAVIGATION | SYSTEM_UI_FLAG_IMMERSIVE);
    }

    // TODO: Add javadoc
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
     * It's darn self-explanatory.
     *
     * @param playLists the list of PlayList
     *
     * @return Array of names
     */
    public static ArrayList<String> getNamesFromPlayLists(ArrayList<PlayList> playLists) {
        ArrayList<String> names = new ArrayList<>();

        for (PlayList playList : playLists) {
            names.add(playList.getName());
        }

        return names;
    }

    // TODO: Add javadoc
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

    // TODO: Add javadoc
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

}
