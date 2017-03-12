package com.randomanimals.www.randomanimals.fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.randomanimals.www.randomanimals.Constants;
import com.randomanimals.www.randomanimals.R;
import com.randomanimals.www.randomanimals.models.UserInfo;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserDialogFragment extends DialogFragment {
    private static final String TAG = "UserDialogFragment";

    public UserDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_dialog, container, false);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.fragment_user_dialog, null);
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                        TextView userText = (TextView) view.findViewById(R.id.userEditText);
                        final String userName = userText.getText().toString();
                        saveUserName(userName);

                        UserDialogFragment.this.getDialog().cancel();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        UserDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }


    private void saveUserName(String username) {
        if (username.length() < Constants.MIN_USERNAME_LENGTH) {
            Toast.makeText(getActivity(),
                    "Username must be at least " + Constants.MIN_USERNAME_LENGTH + " characters.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString("username", username);
        prefsEditor.apply();
        Log.d(TAG, "Set username: " + UserInfo.username);
        UserInfo.username = username;
    }
}
