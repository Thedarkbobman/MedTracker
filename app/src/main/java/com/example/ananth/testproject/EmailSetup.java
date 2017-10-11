package com.example.ananth.testproject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

/**
 * Created by Ananth on 2/14/2016.
 */
public class EmailSetup extends Fragment {
    public static String email;
    public static boolean useEmail,useBetaEmail;
    EditText emailText;
    Button setEmail;
    Switch useEmailSwitch,useBetaSwitch;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View view = inflater.inflate(R.layout.emailsetup, container, false);

        //Defines Text Field
        emailText = (EditText) view.findViewById(R.id.EmailText);
        //Sets Stored Email. It is empty string if there is no stored email.
        emailText.setText(email);
        //Defines the set email button
        setEmail = (Button) view.findViewById(R.id.setEmail);
        //Defines Use Email Switch
        useEmailSwitch = (Switch) view.findViewById(R.id.useEmailButton);
        useBetaSwitch = (Switch) view.findViewById(R.id.betaEmailSwitch);
        //Sets email toggle to the saved preference
        Log.v("useemail",useEmail+"");
        if(useEmail!=useEmailSwitch.isChecked()) {
            useEmailSwitch.toggle();
        }
        if(useBetaEmail!=useBetaSwitch.isChecked()) {
            useBetaSwitch.toggle();
        }
        //Switch listener. Changes setting on switch change
        useEmailSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                useEmail = !useEmail;

                SharedPreferences settings = MainActivity.settings;
                SharedPreferences.Editor prefEditor = settings.edit();
                prefEditor.putBoolean("ShouldUseEmail", useEmail);
                prefEditor.commit();

                Toast toast = Toast.makeText(getContext(), "SettingSaved", Toast.LENGTH_SHORT);
                toast.show();


            }
        });
        useBetaSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    useBetaEmail = !useBetaEmail;
                    SharedPreferences settings = MainActivity.settings;
                    SharedPreferences.Editor prefEditor = settings.edit();
                    prefEditor.putBoolean("ShouldUseBetaEmail", useEmail);
                    prefEditor.commit();
                    Toast toast = Toast.makeText(getContext(), "SettingSaved", Toast.LENGTH_SHORT);
                    toast.show();

            }
        });

        //Button Listener. Sets email setting on click
        setEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailText.getText().toString();
                SharedPreferences settings = MainActivity.settings;
                SharedPreferences.Editor prefEditor = settings.edit();
                prefEditor.putString("UserEmail", email);
                prefEditor.commit();
                Toast toast = Toast.makeText(getContext(), "Email Set!", Toast.LENGTH_SHORT);
                toast.show();

            }
        });

        return view;
    }
}
