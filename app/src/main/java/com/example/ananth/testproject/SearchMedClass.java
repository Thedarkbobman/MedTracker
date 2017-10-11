package com.example.ananth.testproject;

/**
 * Created by Ananth on 11/26/2015.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;

import java.util.concurrent.ExecutionException;

import static com.example.ananth.testproject.R.id.editText;

public class SearchMedClass extends Fragment {

    static private TextView formatTxt, contentTxt;
    static String scanFormat,scanContent;


    EditText input;
    Button sumbitButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View view = inflater.inflate(R.layout.fragment_foo, container, false);
        //Defines TextField
        input = (EditText) view.findViewById(editText);
        //Uncomment for debug purposes
        //input.setText("5058067925");\
        //Defines the submit button
        sumbitButton = (Button) view.findViewById(R.id.scan_button);
        //Searches for Medication on Click
        sumbitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("NDC", input.getText().toString());
                //Retrieves data and checks if it is successful.
                boolean check = false;
                try {
                    check = GetMedData.SetMedData(input.getText().toString(), getActivity().getApplicationContext());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(check) {
                  //Move to Information Screen if data was successfully retrieved
                  Fragment fragment = new InformationScreen();
                  int index = Medication.searchList.size() - 1;
                  Bundle b = new Bundle();

                  b.putInt("index", index);
                  fragment.setArguments(b);
                  FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                  FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                  fragmentTransaction.replace(((ViewGroup) getView().getParent()).getId(), (Fragment) fragment);
                  fragmentTransaction.addToBackStack(null);
                  fragmentTransaction.commit();
              }
            }
        });
        return view;
    }

}