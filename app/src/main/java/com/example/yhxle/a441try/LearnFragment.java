package com.example.yhxle.a441try;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.midi.*;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;
/**
 * Created by yhxle on 10/30/2017.
 */

public class LearnFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = "LoginFragment";

    public LearnFragment() { super(); }

    public static LearnFragment newInstance() {
        LearnFragment fragment = new LearnFragment();
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
        View view = inflater.inflate(R.layout.fragment_learn, container, false);
        Bitmap bmp = Bitmap.createBitmap(1000, 100,  Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        Canvas c = new Canvas(bmp);
        c.drawRect(0,45,1000,55, paint);

        Bitmap bmp_g = Bitmap.createBitmap(1000, 100,  Bitmap.Config.ARGB_8888);
        Paint paint_g = new Paint();
        paint_g.setAntiAlias(true);
        paint_g.setColor(Color.GRAY);
        Canvas c_g = new Canvas(bmp_g);
        c_g.drawRect(0,45,1000,55, paint_g);

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
        svg = SVGParser.getSVGFromResource (getResources (), R.raw.light_treble_clef);
        ImageView clef_imageview  = (ImageView) view.findViewById (R.id.clef);
        clef_imageview.setImageDrawable (svg.createPictureDrawable ());
        svg = SVGParser.getSVGFromResource (getResources (), R.raw.light_note07);
        return view;
    }

    @TargetApi(23)
    @Override
    public void onClick(View v) {
        TextView textView = (TextView) v.getRootView().findViewById(R.id.show);
        ImageView note_imageview  = (ImageView) v.getRootView().findViewById (R.id.note);
        switch (v.getId()) {
            case R.id.L1: {
                textView.setText("High F");
                Bitmap bmp_oval = Bitmap.createBitmap(1000, 520, Bitmap.Config.ARGB_8888);
                Paint paint_oval = new Paint();
                paint_oval.setAntiAlias(true);
                paint_oval.setColor(Color.BLACK);
                Canvas c_oval = new Canvas(bmp_oval);
                c_oval.drawRect(460, 110, 470, 320, paint_oval);
                RectF rect = new RectF(460, 80, 540, 140);
                c_oval.drawOval(rect, paint_oval);
                note_imageview.setImageBitmap(bmp_oval);
                break;
            }
            case R.id.L2: {
                textView.setText("High E");
                Bitmap bmp_oval = Bitmap.createBitmap(1000, 520, Bitmap.Config.ARGB_8888);
                Paint paint_oval = new Paint();
                paint_oval.setAntiAlias(true);
                paint_oval.setColor(Color.BLACK);
                Canvas c_oval = new Canvas(bmp_oval);
                c_oval.drawRect(460, 140, 470, 350, paint_oval);
                RectF rect = new RectF(460, 110, 540, 170);
                c_oval.drawOval(rect, paint_oval);
                note_imageview.setImageBitmap(bmp_oval);
                break;
            }
            case R.id.L3: {
                textView.setText("High D");
                Bitmap bmp_oval = Bitmap.createBitmap(1000, 520, Bitmap.Config.ARGB_8888);
                Paint paint_oval = new Paint();
                paint_oval.setAntiAlias(true);
                paint_oval.setColor(Color.BLACK);
                Canvas c_oval = new Canvas(bmp_oval);
                c_oval.drawRect(460, 170, 470, 380, paint_oval);
                RectF rect = new RectF(460, 140, 540, 200);
                c_oval.drawOval(rect, paint_oval);
                note_imageview.setImageBitmap(bmp_oval);
                break;
            }
            case R.id.L4: {
                textView.setText("High C");
                Bitmap bmp_oval = Bitmap.createBitmap(1000, 520, Bitmap.Config.ARGB_8888);
                Paint paint_oval = new Paint();
                paint_oval.setAntiAlias(true);
                paint_oval.setColor(Color.BLACK);
                Canvas c_oval = new Canvas(bmp_oval);
                c_oval.drawRect(460, 200, 470, 410, paint_oval);
                RectF rect = new RectF(460, 170, 540, 230);
                c_oval.drawOval(rect, paint_oval);
                note_imageview.setImageBitmap(bmp_oval);
                break;
            }
            case R.id.L5: {
                textView.setText("Middle B");
                Bitmap bmp_oval = Bitmap.createBitmap(1000, 520, Bitmap.Config.ARGB_8888);
                Paint paint_oval = new Paint();
                paint_oval.setAntiAlias(true);
                paint_oval.setColor(Color.BLACK);
                Canvas c_oval = new Canvas(bmp_oval);
                c_oval.drawRect(530, 20, 540, 230, paint_oval);
                RectF rect = new RectF(460, 200, 540, 260);
                c_oval.drawOval(rect, paint_oval);
                note_imageview.setImageBitmap(bmp_oval);
                break;
            }
            case R.id.L6: {
                textView.setText("Middle A");
                Bitmap bmp_oval = Bitmap.createBitmap(1000, 460, Bitmap.Config.ARGB_8888);
                Paint paint_oval = new Paint();
                paint_oval.setAntiAlias(true);
                paint_oval.setColor(Color.BLACK);
                Canvas c_oval = new Canvas(bmp_oval);
                c_oval.drawRect(530, 20, 540, 230, paint_oval);
                RectF rect = new RectF(460, 200, 540, 260);
                c_oval.drawOval(rect, paint_oval);
                note_imageview.setImageBitmap(bmp_oval);
                break;
            }
            case R.id.L7: {
                textView.setText("Middle G");
                Bitmap bmp_oval = Bitmap.createBitmap(1000, 400, Bitmap.Config.ARGB_8888);
                Paint paint_oval = new Paint();
                paint_oval.setAntiAlias(true);
                paint_oval.setColor(Color.BLACK);
                Canvas c_oval = new Canvas(bmp_oval);
                c_oval.drawRect(530, 20, 540, 230, paint_oval);
                RectF rect = new RectF(460, 200, 540, 260);
                c_oval.drawOval(rect, paint_oval);
                note_imageview.setImageBitmap(bmp_oval);
                break;
            }
            case R.id.L8: {
                textView.setText("Middle F");
                Bitmap bmp_oval = Bitmap.createBitmap(1000, 400, Bitmap.Config.ARGB_8888);
                Paint paint_oval = new Paint();
                paint_oval.setAntiAlias(true);
                paint_oval.setColor(Color.BLACK);
                Canvas c_oval = new Canvas(bmp_oval);
                c_oval.drawRect(530, 50, 540, 260, paint_oval);
                RectF rect = new RectF(460, 230, 540, 290);
                c_oval.drawOval(rect, paint_oval);
                note_imageview.setImageBitmap(bmp_oval);
                break;
            }
            case R.id.L9: {
                textView.setText("Middle E");
                Bitmap bmp_oval = Bitmap.createBitmap(1000, 400, Bitmap.Config.ARGB_8888);
                Paint paint_oval = new Paint();
                paint_oval.setAntiAlias(true);
                paint_oval.setColor(Color.BLACK);
                Canvas c_oval = new Canvas(bmp_oval);
                c_oval.drawRect(530, 80, 540, 290, paint_oval);
                RectF rect = new RectF(460, 260, 540, 320);
                c_oval.drawOval(rect, paint_oval);
                note_imageview.setImageBitmap(bmp_oval);
                break;
            }
            case R.id.L10: {
                textView.setText("Middle D");
                Bitmap bmp_oval = Bitmap.createBitmap(1000, 400, Bitmap.Config.ARGB_8888);
                Paint paint_oval = new Paint();
                paint_oval.setAntiAlias(true);
                paint_oval.setColor(Color.BLACK);
                Canvas c_oval = new Canvas(bmp_oval);
                c_oval.drawRect(530, 110, 540, 320, paint_oval);
                RectF rect = new RectF(460, 290, 540, 350);
                c_oval.drawOval(rect, paint_oval);
                note_imageview.setImageBitmap(bmp_oval);
                break;
            }
            case R.id.L11: {
                textView.setText("Middle C");
                Bitmap bmp_oval = Bitmap.createBitmap(1000, 400, Bitmap.Config.ARGB_8888);
                Paint paint_oval = new Paint();
                paint_oval.setAntiAlias(true);
                paint_oval.setColor(Color.BLACK);
                Canvas c_oval = new Canvas(bmp_oval);
                c_oval.drawRect(530, 140, 540, 350, paint_oval);
                RectF rect = new RectF(460, 320, 540, 380);
                c_oval.drawOval(rect, paint_oval);
                note_imageview.setImageBitmap(bmp_oval);
                break;
            }
            case R.id.record: {
                Context context = getContext();
                if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MIDI)) {
                    MidiManager m = (MidiManager)context.getSystemService(Context.MIDI_SERVICE);
                    MidiDeviceInfo[] infos = m.getDevices();
                    final MidiDeviceInfo info = infos[0];
                    m.openDevice(info, new MidiManager.OnDeviceOpenedListener() {
                        @Override
                        public void onDeviceOpened(MidiDevice device) {
                            if (device == null) {
                                Log.e(TAG, "could not open device " + info);
                            } else {
                                final int index = 0;
                                MidiInputPort inputPort = device.openInputPort(index);
                                byte[] buffer = new byte[32];
                                int numBytes = 0;
                                int channel = 3; // MIDI channels 1-16 are encoded as 0-15.
                                buffer[numBytes++] = (byte)(0x90 + (channel - 1)); // note on
                                buffer[numBytes++] = (byte)60; // pitch is middle C
                                buffer[numBytes++] = (byte)127; // max velocity
                                int offset = 0;
                                // post is non-blocking
                                try {
                                    inputPort.send(buffer, offset, numBytes);
                                    Toast.makeText(getContext(),"Hi",Toast.LENGTH_SHORT).show();
                                    inputPort.flush();
                                    inputPort.close();
                                } catch (Exception e) {
                                    Log.e(TAG, "Exception");
                                    if (inputPort == null) Toast.makeText(getContext(),"AA", Toast.LENGTH_SHORT).show();

                                    Toast.makeText(getContext(),e.toString(), Toast.LENGTH_SHORT).show();

                                }
                            }
                        }}, new Handler(Looper.getMainLooper())
                    );
                }
                break;
            }
            default:
                break;
        }
    }
}

