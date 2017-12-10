package com.teammatch.yhxle.musica;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.os.Bundle;

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
                Intent intent = new Intent (getActivity(), OverviewActivity.class);
                startActivity (intent);
                //Toast.makeText(getContext(),"Username does not exist!", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}

