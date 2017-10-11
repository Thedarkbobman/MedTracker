package com.example.ananth.testproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

/**
 * Created by Ananth on 3/28/2016.
 */
public class SignOut extends Fragment{
    Button signOut;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View view = inflater.inflate(R.layout.sign_out, container, false);
        signOut = (Button) view.findViewById(R.id.signOutButton);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signOut();
            }
        });
        return view;
    }
    private void signOut() {
        Auth.GoogleSignInApi.signOut(SignInActivity.mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        Intent intent = new Intent(getActivity(), SignInActivity.class);
                        startActivity(intent);
                        // [END_EXCLUDE]
                    }
                });
    }
}
