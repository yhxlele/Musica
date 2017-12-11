package com.teammatch.yhxle.musica;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.webkit.WebView;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;

import jp.kshoji.javax.sound.midi.InvalidMidiDataException;
import jp.kshoji.javax.sound.midi.MidiEvent;
import jp.kshoji.javax.sound.midi.MidiMessage;
import jp.kshoji.javax.sound.midi.MidiSystem;
import jp.kshoji.javax.sound.midi.Sequence;
import jp.kshoji.javax.sound.midi.ShortMessage;
import jp.kshoji.javax.sound.midi.Track;

import static android.app.Activity.RESULT_OK;
import static com.teammatch.yhxle.musica.PlayFragment.myHandler;

// import android.media.midi.*;

/**
 * Created by yhxle on 10/30/2017.
 */

public class MuseScoreFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "MuseScoreFragment";
    public static final int MY_PERMISSIONS_READ_EXTERNAL_FILE = 0;
    public static final int MY_PERMISSIONS_WRITE_EXTERNAL_FILE = 1;
    public static final int DAMPER_PEDAL = 64;
    public static final int DAMPER_ON = 127;
    public static final int DAMPER_OFF = 0;
    public static final int END_OF_TRACK = 47;
    public static final int C4 = 60;
    public static final int D4 = 62;
    public static final int E4 = 64;
    public static final int F4 = 65;
    public static final int G4 = 67;
    public static final int A4 = 69;
    public static final int B4 = 71;
    public static final int C5 = 72;
    public static final int D5 = 74;
    public static final int E5 = 76;
    public static final int F5 = 77;
    public static final MediaPlayer mp = new MediaPlayer();
    public static File f2 = null;

    public static MediaPlayer mpp = new MediaPlayer();
    private Uri myUri = null;
    private View myView;

    private static boolean songIsPlaying = false;
    private static int songPosition = -1;

    private static boolean isSelectingMid = false;
    private static String displayName = null;

    private static final int FILE_SELECT_CODE = 0;

    public static int dpToPx(int dp)
    {
        Log.e(TAG, Float.toString(Resources.getSystem().getDisplayMetrics().density));
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
    private void showFileChooser(boolean isPDF) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        if (isPDF) {
            intent.setType("application/pdf");
        }
        else {
            intent.setType("audio/midi");
        }

        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(getContext(), "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Log.d(TAG, "File Uri: " + uri.toString());
                    myUri = uri;

                    String tempDisplayName = null;

                    try {
                        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            tempDisplayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

                            Log.e(TAG, "The temp display name is: " + tempDisplayName);

                            if (tempDisplayName.endsWith(".pdf")) {
                                displayName = tempDisplayName;
                            }
                        }

                        cursor.close();
                    }
                    catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }

                    final WebView museScoreWebView = (WebView) myView.findViewById(R.id.conversionImage);
                    museScoreWebView.getSettings().setJavaScriptEnabled(true);

                    String urlString = null;

                    switch (displayName) {
                        case "shutter.pdf": {
                            urlString = "<iframe src=\"https://drive.google.com/file/d/1byV_8OnEWUFtDWoVDLOjHUGUhr_3dXAU/preview\" width=\"640\" height=\"480\"></iframe>";
                            break;
                        }
                        case "dawn.pdf": {
                            urlString = "<iframe src=\"https://drive.google.com/file/d/1Dlh80Zu8E5gnYRvYMVyYlO11dSsApjTf/preview\" width=\"640\" height=\"480\"></iframe>";
                            break;
                        }
                        case "white.pdf": {
                            urlString = "<iframe src=\"https://drive.google.com/file/d/1noRd-kfRpeT1CsFbTZAX_qC2C_ptOc2Z/preview\" width=\"640\" height=\"480\"></iframe>";
                        }
                    }

                    museScoreWebView.loadData(urlString, "text/html", null);

                    museScoreWebView.setWebViewClient(new WebViewClient(){
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url){
                            view.loadUrl(url);
                            return super.shouldOverrideUrlLoading(view, url);
                        }
                    });

                    if (!isSelectingMid) {
                        myHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("https://musescore.com/import"));
                                startActivity(browserIntent);
                            }
                        }, 5000);
                    }
                    else {
                        HandleFile();
                    }

                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static final int NOTE_ON = 0x90;
    public static final int NOTE_OFF = 0x80;
    public static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};



    public MuseScoreFragment() { super(); }

    public static MuseScoreFragment newInstance() {
        MuseScoreFragment fragment = new MuseScoreFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.muse_score_browser, container, false);
        myView = view;

        Button browserButton = (Button) myView.findViewById(R.id.browser);
        browserButton.setOnClickListener(this);

        Button stopButton = (Button) myView.findViewById(R.id.stop2);
        stopButton.setOnClickListener(this);

        Button playConversionButton = (Button) myView.findViewById(R.id.playConversion);
        playConversionButton.setOnClickListener(this);

        return view;
    }


    @TargetApi(23)
    @Override
    public void onClick(final View v) {
        Button myStopButton = (Button) v.getRootView().findViewById(R.id.stop2);
        switch (v.getId()) {
            case R.id.stop2: {
                if (songIsPlaying) {
                    mp.pause();
                    songPosition = mp.getCurrentPosition();
                    myStopButton.setText("Resume");
                    songIsPlaying = false;
                    myHandler.removeCallbacksAndMessages(null);
                }
                else {
                    mp.seekTo(songPosition);
                    mp.start();

                    myStopButton.setText("Stop");
                    songIsPlaying = true;
                }
                break;
            }
            case R.id.browser: {

                isSelectingMid = false;

                try {

                    showFileChooser(true);

                } catch (Exception e) {
                    Log.e(TAG, String.valueOf(e));
                }


//                final WebView museScoreWebView = (WebView) myView.findViewById(R.id.museScoreWebView);
//                museScoreWebView.getSettings().setJavaScriptEnabled(true);
//                museScoreWebView.getSettings().setDomStorageEnabled(true);
//                museScoreWebView.loadUrl("https://musescore.com/import");
//
//                museScoreWebView.setWebViewClient(new WebViewClient(){
//                    @Override
//                    public boolean shouldOverrideUrlLoading(WebView view, String url){
////                        view.loadUrl(url);
////                        return super.shouldOverrideUrlLoading(view, url);
//
//                        String url2="www.musescore.com";
//                        String url3 = "dialog.filepicker.io";
//
//                        if (Uri.parse(url).getHost().equals(url2) ) {
//                            return false;
//                        }  else if (Uri.parse(url).getHost().equals(url3) ) {
////                            museScoreWebView.loadUrl(url);
////
////                            museScoreWebView.setWebViewClient(new WebViewClient() {
////                                @Override
////                                public boolean shouldOverrideUrlLoading(WebView view, String url) {
////                                    return false;
////                                }
////                            });
//
//                            return false;
//                        }
//                        else  {
//                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                            startActivity(intent);
//
//                            // view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
//                            return true;
//                        }
//                    }
//                });
                break;
            }
            case R.id.playConversion: {

                isSelectingMid = true;

                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // Should we show an explanation
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        Toast.makeText(getContext(),"Ask for READ permission", Toast.LENGTH_SHORT).show();
                    } else {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_READ_EXTERNAL_FILE);
                    }
                } else {
                    showFileChooser(false);

                }
            }

            default:
                break;
        }
    }

    public void HandleFile (){
        try {
            // myUri = Uri.fromFile(f2);
            mp.reset();
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.setDataSource(getContext(), myUri);
            mp.prepare();
            mp.start();

            songIsPlaying = true;
            Button myStop = (Button) myView.findViewById(R.id.stop2);
            myStop.setText("STOP");
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_READ_EXTERNAL_FILE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "Read permission accessed", Toast.LENGTH_SHORT).show();
                    showFileChooser(false);

                } else {

                    Toast.makeText(getContext(), "Read permission denied", Toast.LENGTH_SHORT).show();

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}

