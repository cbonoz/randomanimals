package com.randomanimals.www.randomanimals.fragments.dialogs;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.randomanimals.www.randomanimals.MainActivity;
import com.randomanimals.www.randomanimals.R;
import com.randomanimals.www.randomanimals.services.ValidatorUtil;
import com.randomanimals.www.randomanimals.services.WebService;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserDialogFragment extends DialogFragment {
    private static final String TAG = "UserDialogFragment";

    public UserDialogFragment() {
        // Required empty public constructor
    }


    private void postUsername(String username) {
        Intent userIntent = new Intent(getActivity(), WebService.class);
        try {
            JSONObject userJson = new JSONObject();
            final MainActivity context = (MainActivity) getActivity();
            String androidId = context.getAndroidId();
            userJson.put("userId", androidId);
            userJson.put("username", username);

            userIntent.putExtra("url", Constants.USERNAME_URL);
            userIntent.putExtra("body", userJson.toString());
            context.startService(userIntent);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
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
                        final String username = userText.getText().toString();
                        if (!ValidatorUtil.validateUsername(username)) {
                            String error = String.format("Username must be between %s and %s characters",
                                    Constants.MIN_USERNAME_LENGTH, Constants.MAX_USERNAME_LENGTH);
                            Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        postUsername(username);
                        UserDialogFragment.this.getDialog().dismiss();

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        UserDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }


}
