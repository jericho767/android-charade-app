package common;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import data.Game;
import database.DBPlayList;
import sprobe.training.miniproject.R;

import static android.view.View.SYSTEM_UI_FLAG_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_IMMERSIVE;

public class Util {
    public static final class SHARED_PREF {
        public static final String KEY = "sprobe.training.miniproject.PREF_FILE";

        public static final String SETTINGS_KEY_TIME_LIMIT = "settings.tl";
        public static final String SETTINGS_KEY_NUM_PASSES = "settings.mp";
        public static final String SETTINGS_KEY_INCLUDE_PASSED = "settings.ip";

        public static final int SETTINGS_MIN_TIME_LIMIT = 30;
        public static final int SETTINGS_MAX_TIME_LIMIT = 999;
        public static final int SETTINGS_MIN_NUM_PASSES = 0;
        public static final int SETTINGS_MAX_NUM_PASSES = 99;

        public static final int SETTINGS_DEFAULT_TIME_LIMIT = 120;
        public static final int SETTINGS_DEFAULT_NUM_PASSES = 3;
        public static final boolean SETTINGS_DEFAULT_INCLUDE_PASSED = true;
    }

    public static final class BUNDLE_KEYS {
        public static final String PLAYLIST_ID = "id";
        public static final String GAME_JSON = "game";
    }

    public static final int RESULT_LOAD_LIST_FILE = 1;

    private static final String[] PERMISSIONS_FILE_HANDLING = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private static final String WHAT_FILE_EXTENSION = "what";
    private static final String WHAT_FILE_MIMETYPE = "application/octet-stream";

    public static final Type PLAYLISTS_TYPE = new TypeToken<ArrayList<DBPlayList>>(){}.getType();

    /**
     * Refresh activity.
     *
     * @param activity pass here <code>this</code> or <code>CurrentActivity.this</code>
     */
    public static void refreshActivity(AppCompatActivity activity) {
        activity.finish();
        activity.startActivity(activity.getIntent());
    }

    /**
     * Will you go out on a PROM with me? Like THAT! Ask permission.
     * Difference is just that this method asks permission for writing/reading files
     * into storage.
     *
     * @param activity pass here <code>this</code> or <code>CurrentActivity.this</code>
     */
    public static void askFilePermissions(AppCompatActivity activity) {
        ActivityCompat.requestPermissions(activity, Util.PERMISSIONS_FILE_HANDLING
                , Util.RESULT_LOAD_LIST_FILE);
    }

    /**
     * Uploaded playlist WILL have different name, they will be appended with the
     * date they are uploaded. <br><br>
     *
     * So does this function was born. *cue baby cries*
     *
     * @param name the name of the playlist
     *
     * @return name of the playlist that will be used to insert in Database
     */
    public static String createPlayListNameFromUpload(String name) {
        return name + "_" + generateDatetimeString();
    }

    /**
     * Generate filename for downloaded files.
     *
     * @param prefix usually this is a filename, then what the function does is
     *               just add the identifier, like the date.
     *
     * @return generated filename. Get it? HAHAHA
     */
    public static String generateFilename(String prefix) {
        return prefix + generateDatetimeString() + "." + WHAT_FILE_EXTENSION;
    }

    /**
     * Formats the current datetime into a string.
     *
     * @return formatted current datetime INTO a string. Read the description.
     */
    private static String generateDatetimeString() {
        return new SimpleDateFormat("yyyyMMddHHmmss"
                , Util.getLocale()).format(new Date());
    }

    /**
     * KABOOM! Concatinates an <code>ArrayList</code> into a string
     * separated with the delimiter.
     *
     * @param list list of something
     *
     * @return I don't know what to say... aside from... READ THE DESCRIPTION!
     */
    public static String implode(ArrayList<?> list) {
        StringBuilder stringBuilder = new StringBuilder();
        String delimiter = ",";

        for (int i = 0 ; i < list.size() ; i++) {
            stringBuilder.append(list.get(i).toString());

            if (i != list.size() - 1) {
                stringBuilder.append(delimiter);
            }
        }

        return stringBuilder.toString();
    }

    /**
     * Pass an input stream, and this method will return you the
     * content of the file you are opening.
     *
     * @param is the <code>InputStream</code> you are opening
     *
     * @return contents of the file
     * @throws IOException Boom!
     */
    public static String convertStreamToString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    /**
     * Is this extension acceptable or not?
     * This method will judge.
     *
     * @param ext the extension that will be judged
     *
     * @return TO BE or NOT TO BE. That is the question.
     */
    public static boolean isAcceptableFileExtension(String ext) {
        String extension = ext.toLowerCase();

        return extension.equals(WHAT_FILE_EXTENSION.toLowerCase());
    }

    /**
     * Read the method name OUT LOUD.
     * Then tell me what you understand.
     *
     * @param uri this is the selected file.
     *
     * @return Again. Read the method name OUT LOUD.
     */
    public static String getFileExtensionFromUri(Uri uri) {
        return MimeTypeMap.getFileExtensionFromUrl(uri.toString());
    }

    /**
     * Gets the path of... of... that.. Uri.
     * Yeah! the path of that URI!
     * Actually just copied this from the internet. LOL!
     *
     * @param context pass here <code>this</code> or <code>CurrentActivity.this</code>
     * @param uri this is the selected file.
     *
     * @return Read the method name. PLEASE! PLEASE?
     */
    public static String getPath(final Context context, final Uri uri) {
        final boolean isMarshmallow = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;

        // DocumentProvider
        if (isMarshmallow && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if ("com.android.externalstorage.documents".equals(uri.getAuthority())) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                // This is for checking Main Memory
                if ("primary".equalsIgnoreCase(type)) {
                    if (split.length > 1) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1] + "/";
                    } else {
                        return Environment.getExternalStorageDirectory() + "/";
                    }
                    // This is for checking SD Card
                } else {
                    return "storage" + "/" + docId.replace(":", "/");
                }

            }
        }
        return null;
    }

    /**
     * Opens the gallery for uploading files.
     *
     * @param activity pass here <code>this</code> or <code>CurrentActivity.this</code>
     */
    public static void openFileChooser(AppCompatActivity activity) {
        Intent fileChooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
        fileChooserIntent.setType(WHAT_FILE_MIMETYPE);
        fileChooserIntent.addCategory(Intent.CATEGORY_OPENABLE);

        activity.startActivityForResult(
                Intent.createChooser(fileChooserIntent
                        , activity.getResources().getString(R.string.button_select_file))
                , RESULT_LOAD_LIST_FILE);
    }

    /**
     * Dialog listener that will show the confirm dialog.
     *
     * @param activity pass here <code>this</code> or <code>CurrentActivity.this</code>
     * @param listenerConfirm what to do if confirm?
     *
     * @return listener that will show confirm dialog
     */
    public static DialogInterface.OnClickListener dialogListenerShowConfirmDialog(
            final AppCompatActivity activity
            , final DialogInterface.OnClickListener listenerConfirm) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Util.showConfirmDialog(activity, listenerConfirm);
            }
        };
    }

    /**
     * Dialog listener that will close the dialog.
     *
     * @return listener that will close the dialog
     */
    public static DialogInterface.OnClickListener dialogListenerDismissDialog() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };
    }

    /**
     * Shows that annoying confirm dialog that keeps on confirming.
     *
     * @param activity pass here <code>this</code> or <code>CurrentActivity.this</code>
     * @param onConfirmListener what to do when confirm?
     */
    private static void showConfirmDialog(AppCompatActivity activity
            , DialogInterface.OnClickListener onConfirmListener) {

        Util.createAlertDialog(activity
                , activity.getResources().getString(R.string.button_confirm)
                , activity.getResources().getString(R.string.msg_conf)
                , activity.getResources().getString(R.string.button_yes_positive)
                , onConfirmListener
                , activity.getResources().getString(R.string.button_abort)
                , Util.dialogListenerDismissDialog()).show();
    }

    /**
     * Create an alert dialog. Duh.
     *
     * @param activity pass here <code>this</code> or <code>CurrentActivity.this</code>
     * @param title the title of the dialog
     * @param message what's your message?
     * @param btnPositiveText what text should be displayed on positive button
     * @param onPositiveListener what to do when that button is clicked?
     * @param btnNegativeText what text should be displayed on negative button
     * @param onNegativeListener what to do when that button is clicked?
     *
     * @return of course the <code>AlertDialog</code> you asked for.
     */
    public static AlertDialog.Builder createAlertDialog(AppCompatActivity activity
            , String title
            , String message
            , String btnPositiveText
            , DialogInterface.OnClickListener onPositiveListener
            , String btnNegativeText
            , DialogInterface.OnClickListener onNegativeListener) {

        return new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(btnPositiveText, onPositiveListener)
                .setNegativeButton(btnNegativeText, onNegativeListener);
    }

    /**
     * The method name says it ALL! And I mean ALL!!!
     *
     * @param activity the current activity. Pass here <code>this</code>.
     */
    public static void showKeyboard(AppCompatActivity activity) {
        activity.getWindow()
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    /**
     * The method name says it ALL! And I mean ALL!!!
     *
     * @param window TO THE WINDOW!!!!
     */
    public static void showKeyboard(Window window) {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

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
