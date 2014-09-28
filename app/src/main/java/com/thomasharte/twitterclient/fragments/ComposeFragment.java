package com.thomasharte.twitterclient.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.thomasharte.twitterclient.R;


public class ComposeFragment extends DialogFragment {

    private EditText etBody;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().setTitle("Compose Tweet");

        View view = inflater.inflate(R.layout.fragment_compose, container, false);
        etBody = (EditText)view.findViewById(R.id.etBody);

        Button sendButton = (Button)view.findViewById(R.id.btnSend);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ComposeFragmentListener activity = (ComposeFragmentListener) getActivity();
                activity.onPostTweet(etBody.getText().toString());
                getDialog().dismiss();
            }
        });

        return view;
    }

    public interface ComposeFragmentListener {
        public void onPostTweet(String statusMessage);
    }

}
