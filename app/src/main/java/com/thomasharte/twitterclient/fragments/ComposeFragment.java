package com.thomasharte.twitterclient.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.thomasharte.twitterclient.R;


public class ComposeFragment extends DialogFragment {

    private EditText etBody;
    private TextView tvCharactersRemaining;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().setTitle("Compose Tweet");

        View view = inflater.inflate(R.layout.fragment_compose, container, false);
        etBody = (EditText)view.findViewById(R.id.etBody);
        tvCharactersRemaining = (TextView)view.findViewById(R.id.tvCharactersRemaining);
        Button sendButton = (Button)view.findViewById(R.id.btnSend);

        // if send is pressed, we'll pass the string back to the activity and
        // dismiss this fragment
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ComposeFragmentListener activity = (ComposeFragmentListener) getActivity();
                activity.onPostTweet(etBody.getText().toString());
                getDialog().dismiss();
            }
        });

        // we'll want to update the characters remaining count, seeding it at 140
        etBody.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

            @Override
            public void afterTextChanged(Editable editable) {
                Integer charactersRemaining = Math.max(140 - etBody.getText().length(), 0);
                tvCharactersRemaining.setText(charactersRemaining.toString());
            }
        });


        return view;
    }

    public interface ComposeFragmentListener {
        public void onPostTweet(String statusMessage);
    }

}
