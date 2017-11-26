package com.example.yhxle.a441try;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by yhxle on 10/30/2017.
 */

public class SignupFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = "SignupFragment";
    private TextView LoginTab;

    public SignupFragment() { super(); }

    public static SignupFragment newInstance() {
        SignupFragment fragment = new SignupFragment();
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
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        Button signup = (Button) view.findViewById(R.id.signup);
        signup.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signup:
                Intent intent = new Intent (getActivity(), OverviewActivity.class);
                startActivity (intent);
                break;
            default:
                break;
        }
    }
}
