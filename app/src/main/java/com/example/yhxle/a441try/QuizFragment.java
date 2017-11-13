package com.example.yhxle.a441try;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by yhxle on 10/30/2017.
 */

public class QuizFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = "QuizFragment";

    public QuizFragment() { super(); }

    public static QuizFragment newInstance() {
        QuizFragment fragment = new QuizFragment();
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
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);
    //    Button practice_button = (Button) view.findViewById(R.id.practice_button);
        Button quiz_button = (Button) view.findViewById(R.id.quiz_button);
    //    practice_button.setOnClickListener(this);
        quiz_button.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        //    case R.id.practice_button: {
        //        Intent intent = new Intent (getContext(), QuizActivity.class);
        //        intent.putExtra ("theme", 0);
        //        intent.putExtra ("practice", true);
        //        startActivity (intent);
        //        break; }
            case R.id.quiz_button:{
                Intent intent = new Intent (getContext(), QuizActivity.class);
                intent.putExtra ("theme", 0);
                intent.putExtra ("practice", false);
                startActivity (intent);
                break; }
            default:
                break;
        }
    }
}

