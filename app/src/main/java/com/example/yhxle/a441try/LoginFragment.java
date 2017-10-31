package com.example.yhxle.a441try;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by yhxle on 10/30/2017.
 */

public class LoginFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = "LoginFragment";

    public LoginFragment() { super(); }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        Button login = (Button) view.findViewById(R.id.login);
        login.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                Toast.makeText(v.getContext(),"Log in", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}

