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
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

// import android.media.midi.*;

/**
 * Created by yhxle on 10/30/2017.
 */

public class PlayFragment extends Fragment implements View.OnClickListener {


    public static final String TAG = "PlayFragment";
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
    public static final Handler myHandler = new Handler();

    private static InputStream inputStream = null;
    private static Sequence sequence = null;

    public static MediaPlayer mpp = new MediaPlayer();
    private Uri myUri = null;
    private View myView;

    private boolean songIsPlaying = false;
    private int songPosition = -1;

    private static final int FILE_SELECT_CODE = 0;

    public static int dpToPx(int dp)
    {
        Log.e(TAG, Float.toString(Resources.getSystem().getDisplayMetrics().density));
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(getContext(), "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
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
                    HandleFile();

                    // Get the path
                    try {
                        String path = getPath(getContext(), uri);
                        Log.d(TAG, "File Path: " + path);
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }

                    // Get the file instance
                    // File file = new File(path);
                    // Initiate the upload
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


        public void MyRead(InputStream inputStream, int currentTick, Sequence sequence) {

                    currentTick = (currentTick * 16) / 500;

                    // Sequence sequence = null;
                    try {
                        // sequence = MidiSystem.getSequence(inputStream);

                        int trackNumber = 0;

                        for (Track track : sequence.getTracks())

                        {
                            trackNumber++;
                            Log.e(TAG, "Track " + trackNumber + ": size = " + track.size());
                            for (int i = 0; i < track.size(); i++) {
                                MidiEvent event = track.get(i);
                                Log.e(TAG, "@" + event.getTick() + " ");
                                MidiMessage message = event.getMessage();
                                if (message instanceof ShortMessage) {
                                    ShortMessage sm = (ShortMessage) message;
                                    Log.e(TAG, "Channel: " + sm.getChannel() + " ");
                                    if (sm.getCommand() == NOTE_ON) {
                                        int key = sm.getData1();
                                        int octave = (key / 12) - 1;
                                        int note = key % 12;
                                        String noteName = NOTE_NAMES[note];
                                        int velocity = sm.getData2();
                                        Log.e(TAG, "Note on, " + noteName + octave + " key=" + key + " velocity: " + velocity);

                                        final TextView textView = (TextView) myView.findViewById(R.id.show);
                                        final ImageView note_imageview = (ImageView) myView.findViewById(R.id.note);
                                        switch (key) {
                                            case C4: {
                                                if (currentTick <= event.getTick()) {
                                                    myHandler.postDelayed( new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            textView.setText("Middle C");
                                                        }
                                                    }, (event.getTick() - currentTick) * 500/16);
                                                    myHandler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            note_imageview.setImageBitmap(mydraw_c());
                                                        }
                                                    }, (event.getTick() - currentTick) * 500/16);
                                                }
                                                break;
                                            }
                                            case D4: {
                                                if (currentTick <= event.getTick()) {
                                                    myHandler.postDelayed( new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            textView.setText("Middle D");
                                                        }
                                                    }, (event.getTick() - currentTick) * 500/16);
                                                    myHandler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            note_imageview.setImageBitmap(mydraw(5));
                                                        }
                                                    }, (event.getTick() - currentTick) * 500/16);
                                                }
                                                break;
                                            }
                                            case E4: {
                                                if (currentTick <= event.getTick()) {
                                                    myHandler.postDelayed( new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            textView.setText("Middle E");
                                                        }
                                                    }, (event.getTick() - currentTick) * 500/16);
                                                    myHandler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            note_imageview.setImageBitmap(mydraw(4));
                                                        }
                                                    }, (event.getTick() - currentTick) * 500/16);
                                                }
                                                break;
                                            }
                                            case F4: {
                                                if (currentTick <= event.getTick()) {
                                                    myHandler.postDelayed( new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            textView.setText("Middle F");
                                                        }
                                                    }, (event.getTick() - currentTick) * 500/16);
                                                    myHandler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            note_imageview.setImageBitmap(mydraw(3));
                                                        }
                                                    }, (event.getTick() - currentTick) * 500/16);
                                                }
                                                break;
                                            }
                                            case G4: {
                                                if (currentTick <= event.getTick()) {
                                                    myHandler.postDelayed( new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            textView.setText("Middle G");
                                                        }
                                                    }, (event.getTick() - currentTick) * 500/16);
                                                    myHandler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            note_imageview.setImageBitmap(mydraw(2));
                                                        }
                                                    }, (event.getTick() - currentTick) * 500/16);
                                                }
                                                break;
                                            }
                                            case A4: {
                                                if (currentTick <= event.getTick()) {
                                                    myHandler.postDelayed( new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            textView.setText("Middle A");
                                                        }
                                                    }, (event.getTick() - currentTick) * 500/16);
                                                    myHandler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            note_imageview.setImageBitmap(mydraw(1));
                                                        }
                                                    }, (event.getTick() - currentTick) * 500/16);
                                                }
                                                break;
                                            }
                                            case B4: {
                                                if (currentTick <= event.getTick()) {
                                                    myHandler.postDelayed( new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            textView.setText("Middle B");
                                                        }
                                                    }, (event.getTick() - currentTick) * 500/16);
                                                    myHandler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            note_imageview.setImageBitmap(mydraw(0));
                                                        }
                                                    }, (event.getTick() - currentTick) * 500/16);
                                                }
                                                break;
                                            }
                                            case C5: {
                                                if (currentTick <= event.getTick()) {
                                                    myHandler.postDelayed( new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            textView.setText("High C");
                                                        }
                                                    }, (event.getTick() - currentTick) * 500/16);
                                                    myHandler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            note_imageview.setImageBitmap(mydraw_high(3));
                                                        }
                                                    }, (event.getTick() - currentTick) * 500/16);
                                                }
                                                break;
                                            }
                                            case D5: {
                                                if (currentTick <= event.getTick()) {
                                                    myHandler.postDelayed( new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            textView.setText("High D");
                                                        }
                                                    }, (event.getTick() - currentTick) * 500/16);
                                                    myHandler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            note_imageview.setImageBitmap(mydraw_high(2));
                                                        }
                                                    }, (event.getTick() - currentTick) * 500/16);
                                                }
                                                break;
                                            }
                                            case E5: {
                                                if (currentTick <= event.getTick()) {
                                                    myHandler.postDelayed( new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            textView.setText("High E");
                                                        }
                                                    }, (event.getTick() - currentTick) * 500/16);
                                                    myHandler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            note_imageview.setImageBitmap(mydraw_high(1));
                                                        }
                                                    }, (event.getTick() - currentTick) * 500/16);
                                                }
                                                break;
                                            }
                                            case F5: {
                                                if (currentTick <= event.getTick()) {
                                                        myHandler.postDelayed( new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                textView.setText("High F");
                                                            }
                                                        }, (event.getTick() - currentTick) * 500/16);
                                                        myHandler.postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                note_imageview.setImageBitmap(mydraw_high(0));
                                                            }
                                                        }, (event.getTick() - currentTick) * 500/16);
                                                    }
                                                break;
                                            }
                                            default:
                                                break;
                                        }
                                    } else if (sm.getCommand() == NOTE_OFF) {
                                        int key = sm.getData1();
                                        int octave = (key / 12) - 1;
                                        int note = key % 12;
                                        String noteName = NOTE_NAMES[note];
                                        int velocity = sm.getData2();
                                        Log.e(TAG, "Note off, " + noteName + octave + " key=" + key + " velocity: " + velocity);
                                    } else {
                                        Log.e(TAG, "Command:" + sm.getCommand());
                                    }
                                } else {
                                    Log.e(TAG, "Other message: " + message.getClass());
                                }
                            }

                        }
                    }
                    catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }

        }


    public PlayFragment() { super(); }

    public static PlayFragment newInstance() {
        PlayFragment fragment = new PlayFragment();
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
        View view = inflater.inflate(R.layout.fragment_play, container, false);
        myView = view;

        Log.e(TAG, Build.MODEL);
        if (false) {
            ImageButton L = (ImageButton) view.findViewById(R.id.L0);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) L.getLayoutParams();
            params.setMargins(0, 500, 0, 0);
            L.setLayoutParams(params);

            Log.e(TAG, "true");
        }

        Bitmap bmp = Bitmap.createBitmap(dpToPx(333), dpToPx(33),  Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        Canvas c = new Canvas(bmp);
        c.drawRect(0,dpToPx(15),dpToPx(333),dpToPx(18), paint);

        Bitmap bmp_g = Bitmap.createBitmap(dpToPx(333), dpToPx(33),  Bitmap.Config.ARGB_8888);
        Paint paint_g = new Paint();
        paint_g.setAntiAlias(true);
        paint_g.setColor(Color.TRANSPARENT);
        Canvas c_g = new Canvas(bmp_g);
        c_g.drawRect(0,dpToPx(15),dpToPx(333),dpToPx(18), paint_g);

        ImageButton l1 = (ImageButton) view.findViewById(R.id.L1);
        l1.setOnClickListener(this);
        l1.setImageBitmap(bmp);
        ImageButton l2 = (ImageButton) view.findViewById(R.id.L2);
        l2.setOnClickListener(this);
        ImageButton l3 = (ImageButton) view.findViewById(R.id.L3);
        l3.setOnClickListener(this);
        l3.setImageBitmap(bmp);
        ImageButton l4 = (ImageButton) view.findViewById(R.id.L4);
        l4.setOnClickListener(this);
        ImageButton l5 = (ImageButton) view.findViewById(R.id.L5);
        l5.setOnClickListener(this);
        l5.setImageBitmap(bmp);
        ImageButton l6 = (ImageButton) view.findViewById(R.id.L6);
        l6.setOnClickListener(this);
        ImageButton l7 = (ImageButton) view.findViewById(R.id.L7);
        l7.setOnClickListener(this);
        l7.setImageBitmap(bmp);
        ImageButton l8 = (ImageButton) view.findViewById(R.id.L8);
        l8.setOnClickListener(this);
        ImageButton l9 = (ImageButton) view.findViewById(R.id.L9);
        l9.setOnClickListener(this);
        l9.setImageBitmap(bmp);
        ImageButton l10 = (ImageButton) view.findViewById(R.id.L10);
        l10.setOnClickListener(this);
        ImageButton l11 = (ImageButton) view.findViewById(R.id.L11);
        l11.setOnClickListener(this);
        l11.setImageBitmap(bmp_g);
        Button record = (Button) view.findViewById(R.id.record);
        record.setOnClickListener(this);
        SVG svg;

        Button stop = (Button) view.findViewById(R.id.stop);
        stop.setOnClickListener(this);

        svg = SVGParser.getSVGFromResource (getResources (), R.raw.light_treble_clef);
        ImageView clef_imageview  = (ImageView) view.findViewById (R.id.clef);
        clef_imageview.setImageDrawable (svg.createPictureDrawable ());
        svg = SVGParser.getSVGFromResource (getResources (), R.raw.light_note07);
        return view;
    }
    Bitmap mydraw_high(int i) {
        Bitmap bmp_oval = Bitmap.createBitmap(dpToPx(400), dpToPx(400), Bitmap.Config.ARGB_8888);
        Paint paint_oval = new Paint();
        paint_oval.setAntiAlias(true);
        paint_oval.setColor(Color.BLACK);
        Canvas c_oval = new Canvas(bmp_oval);
        c_oval.drawRect(dpToPx(160), dpToPx(23*i+77), dpToPx(165), dpToPx(23*i+175), paint_oval);
        RectF rect = new RectF(dpToPx(160), dpToPx(22*i+52), dpToPx(222), dpToPx(22*i+98));
        c_oval.drawOval(rect, paint_oval);
        return bmp_oval;
    }
    Bitmap mydraw(int i) {
        Bitmap bmp_oval = Bitmap.createBitmap(dpToPx(400), dpToPx(400), Bitmap.Config.ARGB_8888);
        Paint paint_oval = new Paint();
        paint_oval.setAntiAlias(true);
        paint_oval.setColor(Color.BLACK);
        Canvas c_oval = new Canvas(bmp_oval);
        c_oval.drawRect(dpToPx(217), dpToPx(23*i+62), dpToPx(222), dpToPx(23*i+162), paint_oval);
        RectF rect = new RectF(dpToPx(160), dpToPx(23*i+144), dpToPx(222), dpToPx(23*i+190));
        c_oval.drawOval(rect, paint_oval);
        return bmp_oval;
    }
    Bitmap mydraw_c() {
        Bitmap bmp_oval = Bitmap.createBitmap(dpToPx(400), dpToPx(400), Bitmap.Config.ARGB_8888);
        Paint paint_oval = new Paint();
        paint_oval.setAntiAlias(true);
        paint_oval.setColor(Color.BLACK);
        Canvas c_oval = new Canvas(bmp_oval);
        c_oval.drawRect(dpToPx(217), dpToPx(200), dpToPx(222), dpToPx(300), paint_oval);
        c_oval.drawRect(dpToPx(150), dpToPx(303), dpToPx(232), dpToPx(307), paint_oval);
        RectF rect = new RectF(dpToPx(160), dpToPx(282), dpToPx(222), dpToPx(328));
        c_oval.drawOval(rect, paint_oval);
        return bmp_oval;
    }
    @TargetApi(23)
    @Override
    public void onClick(final View v) {
        TextView textView = (TextView) v.getRootView().findViewById(R.id.show);
        ImageView note_imageview  = (ImageView) v.getRootView().findViewById (R.id.note);
        Button myStopButton = (Button) v.getRootView().findViewById(R.id.stop);
        switch (v.getId()) {
            case R.id.L1: {
                textView.setText("High F");
                note_imageview.setImageBitmap(mydraw_high(0));
                mpp.reset();
                mpp.release();
                mpp = MediaPlayer.create(getContext(), R.raw.piano_ff_057);
                mpp.start();
                break;
            }
            case R.id.L2: {
                textView.setText("High E");
                note_imageview.setImageBitmap(mydraw_high(1));
                mpp.reset();
                mpp.release();
                mpp = MediaPlayer.create(getContext(), R.raw.piano_ff_056);
                mpp.start();
                break;
            }
            case R.id.L3: {
                textView.setText("High D");
                note_imageview.setImageBitmap(mydraw_high(2));
                mpp.reset();
                mpp.release();
                mpp = MediaPlayer.create(getContext(), R.raw.piano_ff_054);
                mpp.start();
                break;
            }
            case R.id.L4: {
                textView.setText("High C");
                note_imageview.setImageBitmap(mydraw_high(3));
                mpp.reset();
                mpp.release();
                mpp = MediaPlayer.create(getContext(), R.raw.piano_ff_052);
                mpp.start();
                break;
            }
            case R.id.L5: {
                textView.setText("Middle B");
                note_imageview.setImageBitmap(mydraw(0));
                mpp.reset();
                mpp.release();
                mpp = MediaPlayer.create(getContext(), R.raw.piano_ff_051);
                mpp.start();
                break;
            }
            case R.id.L6: {
                textView.setText("Middle A");
                note_imageview.setImageBitmap(mydraw(1));
                mpp.reset();
                mpp.release();
                mpp = MediaPlayer.create(getContext(), R.raw.piano_ff_049);
                mpp.start();
                break;
            }
            case R.id.L7: {
                textView.setText("Middle G");
                note_imageview.setImageBitmap(mydraw(2));
                mpp.reset();
                mpp.release();
                mpp = MediaPlayer.create(getContext(), R.raw.piano_ff_047);
                mpp.start();
                break;
            }
            case R.id.L8: {
                textView.setText("Middle F");
                note_imageview.setImageBitmap(mydraw(3));
                mpp.reset();
                mpp.release();
                mpp = MediaPlayer.create(getContext(), R.raw.piano_ff_045);
                mpp.start();
                break;
            }
            case R.id.L9: {
                textView.setText("Middle E");
                note_imageview.setImageBitmap(mydraw(4));
                mpp.reset();
                mpp.release();
                mpp = MediaPlayer.create(getContext(), R.raw.piano_ff_044);
                mpp.start();
                break;
            }
            case R.id.L10: {
                textView.setText("Middle D");
                note_imageview.setImageBitmap(mydraw(5));
                mpp.reset();
                mpp.release();
                mpp = MediaPlayer.create(getContext(), R.raw.piano_ff_042);
                mpp.start();
                break;
            }
            case R.id.L11: {
                textView.setText("Middle C");
                note_imageview.setImageBitmap(mydraw_c());
                mpp.reset();
                mpp.release();
                mpp = MediaPlayer.create(getContext(), R.raw.piano_ff_040);
                mpp.start();
                break;
            }
            case R.id.record: {
                Log.e(TAG,"midifile begin ");
                try {
                    int instrument = 0;
                    int tempo = 120;

                    // Parse the options
                    // -i <instrument number> default 0, a piano.  Allowed values: 0-127
                    // -t <beats per minute>  default tempo is 120 quarter notes per minute
                    // -o <filename>          save to a midi file instead of playing
                    //int a = 0;
                    instrument = 0;
                    //a+=2;

                    tempo = 120;
                    //a+=2;

                    char[  ] notes = "/2E /4G + /kD /2C - /4G /kF /2E /4E E F G /kA G /2E /4G + /kD /2C - /4G /kF /2E /4G G A B + /kC C /8D . . - G /4G B A G /2E /4G + /kC - /2A + /4C /2D /4C - /kB G /2E /4G + /kD /2C - /4G /kF /2E /4G G A B + /kC C".toCharArray( );

                    // 16 ticks per quarter note.
                    Sequence sequence = new Sequence(Sequence.PPQ, 16);

                    // Add the specified notes to the track
                    addTrack(sequence, instrument, tempo, notes);

                    // A file name was specified, so save the notes
                    int[  ] allowedTypes = MidiSystem.getMidiFileTypes(sequence);
                    File f = new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_MUSIC), "Edelweiss.mid");
                    f.createNewFile();

                    MidiSystem.write(sequence, allowedTypes[0], f);
                        if (ContextCompat.checkSelfPermission(getActivity(),
                                Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            // Should we show an explanation?
                            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                Toast.makeText(getContext(),"Ask for READ permission", Toast.LENGTH_SHORT).show();

                            } else {

                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                        MY_PERMISSIONS_READ_EXTERNAL_FILE);

                            }
                        } else {
                            showFileChooser();

                        }
                }
                catch(Exception e) {

                    Toast.makeText(getContext(), "Exception caught " + e.toString(),Toast.LENGTH_SHORT).show();
                }
/*
                try
                {
//****  Create a new MIDI sequence with 24 ticks per beat  ****
                    Sequence s = new Sequence(Sequence.PPQ,24);

//****  Obtain a MIDI track from the sequence  ****
                    Track t = s.createTrack();

                    ShortMessage myMsg = new ShortMessage();
                    // Play the note Middle C (60) moderately loud
                    // (velocity = 93)on channel 4 (zero-based).
                    myMsg.setMessage(ShortMessage.NOTE_ON, 4, 60, 93);

                    MidiEvent md = new MidiEvent(myMsg, 0);
                    t.add(md);


//****  write the MIDI sequence to a MIDI file  ****
                    Toast.makeText(getContext(),
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString(),
                            Toast.LENGTH_SHORT).show();
                    File f = new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES), "mididi.mid");
                    f.createNewFile();
                    MidiSystem.write(s,1,f);

                    Uri myUri = Uri.fromFile(f);
                    MediaPlayer mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setDataSource(getContext(), myUri);
                    mediaPlayer.prepare();
                    mediaPlayer.start();


                } //try
                catch(Exception e)
                {
                    Toast.makeText(getContext(), "Exception caught " + e.toString(),Toast.LENGTH_SHORT).show();
                } //catch
                Log.e(TAG,ln("midifile end ");


                MediaPlayer mediaPlayer = MediaPlayer.create(getContext(), R.raw.midifile);
                mediaPlayer.start(); // no need to call prepare(); create() does that for you
                */
                break;
            }
            case R.id.stop: {
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

                    MyRead(inputStream, mp.getCurrentPosition(), sequence);
                    myStopButton.setText("Stop");
                    songIsPlaying = true;
                }
            }

            default:
                break;
        }
    }
    public void HandleFile (){
        try {
            // Uri myUri = Uri.fromFile(f);
            mp.reset();
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.setDataSource(getContext(), myUri);
            mp.prepare();
            mp.start();
            songIsPlaying = true;
            Button myStop = (Button) myView.findViewById(R.id.stop);
            myStop.setText("STOP");
            //      mediaPlayer.release();
            //      f.delete();
            inputStream = getContext().getContentResolver().openInputStream(myUri);
            sequence = MidiSystem.getSequence(inputStream);

            MyRead(inputStream, mp.getCurrentPosition(), sequence);
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
                    showFileChooser();

                } else {

                    Toast.makeText(getContext(), "Read permission denied", Toast.LENGTH_SHORT).show();

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    static final int[  ] offsets = {  // add these amounts to the base value
            // A   B  C  D  E  F  G
               9, 11, 0, 2, 4, 5, 7
    };

    /*
     * This method parses the specified char[  ] of notes into a Track.
     * The musical notation is the following:
     * A-G:   A named note; Add b for flat and # for sharp.
     * +:     Move up one octave. Persists.
     * -:     Move down one octave.  Persists.
     * /1:    Notes are whole notes.  Persists 'till changed
     * /2:    Half notes
     * /4:    Quarter notes
     * /n:    N can also be 8, 16, 32, 64.
     * s:     Toggle sustain pedal on or off (initially off)
     *
     * >:     Louder.  Persists
     * <:     Softer.  Persists
     * .:     Rest. Length depends on current length setting
     * Space: Play the previous note or notes; notes not separated by spaces
     *        are played at the same time
     */
    public static void addTrack(Sequence s, int instrument, int tempo,
                                char[  ] notes)
            throws InvalidMidiDataException
    {
        Track track = s.createTrack( );  // Begin with a new track

        // Set the instrument on channel 0
        ShortMessage sm = new ShortMessage( );
        sm.setMessage(ShortMessage.PROGRAM_CHANGE, 0, instrument, 0);
        track.add(new MidiEvent(sm, 0));

        int n = 0; // current character in notes[  ] array
        int t = 0; // time in ticks for the composition

        // These values persist and apply to all notes 'till changed
        int notelength = 16; // default to quarter notes
        int velocity = 64;   // default to middle volume
        int basekey = 60;    // 60 is middle C. Adjusted up and down by octave
        boolean sustain = false;   // is the sustain pedal depressed?
        int numnotes = 0;    // How many notes in current chord?

        while(n < notes.length) {
            char c = notes[n++];

            if (c == '+') basekey += 12;        // increase octave
            else if (c == '-') basekey -= 12;   // decrease octave
            else if (c == '>') velocity += 16;  // increase volume;
            else if (c == '<') velocity -= 16;  // decrease volume;
            else if (c == '/') {
                char d = notes[n++];
                if (d == '2') notelength = 32;  // half note
                else if (d == 'k') notelength = 48;
                else if (d == '4') notelength = 16;  // quarter note
                else if (d == '8') notelength = 8;   // eighth note
                else if (d == '3' && notes[n++] == '2') notelength = 2;
                else if (d == '6' && notes[n++] == '4') notelength = 1;
                else if (d == '1') {
                    if (n < notes.length && notes[n] == '6')
                        notelength = 4;    // 1/16th note
                    else notelength = 64;  // whole note
                }
            }
            else if (c == 's') {
                sustain = !sustain;
                // Change the sustain setting for channel 0
                ShortMessage m = new ShortMessage( );
                m.setMessage(ShortMessage.CONTROL_CHANGE, 0,
                        DAMPER_PEDAL, sustain?DAMPER_ON:DAMPER_OFF);
                track.add(new MidiEvent(m, t));
            }
            else if (c >= 'A' && c <= 'G') {
                int key = basekey + offsets[c - 'A'];
                if (n < notes.length) {
                    if (notes[n] == 'b') { // flat
                        key--;
                        n++;
                    }
                    else if (notes[n] == '#') { // sharp
                        key++;
                        n++;
                    }
                }

                addNote(track, t, notelength, key, velocity);
                numnotes++;
            }
            else if (c == ' ') {
                // Spaces separate groups of notes played at the same time.
                // But we ignore them unless they follow a note or notes.
                if (numnotes > 0) {
                    t += notelength;
                    numnotes = 0;
                }
            }
            else if (c == '.') {
                // Rests are like spaces in that they force any previous
                // note to be output (since they are never part of chords)
                if (numnotes > 0) {
                    t += notelength;
                    numnotes = 0;
                }
                // Now add additional rest time
                t += notelength;
            }
        }
    }

    // A convenience method to add a note to the track on channel 0
    public static void addNote(Track track, int startTick,
                               int tickLength, int key, int velocity)
            throws InvalidMidiDataException
    {
        ShortMessage on = new ShortMessage( );
        on.setMessage(ShortMessage.NOTE_ON,  0, key, velocity);
        ShortMessage off = new ShortMessage( );
        off.setMessage(ShortMessage.NOTE_OFF, 0, key, velocity);
        track.add(new MidiEvent(on, startTick));
        track.add(new MidiEvent(off, startTick + tickLength));
    }


}

