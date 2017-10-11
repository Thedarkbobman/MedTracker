package com.example.ananth.testproject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Ananth on 12/30/2015.
 */
public class PersonalMedList extends ListFragment {
String names[];
    ArrayList nameList;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View view = inflater.inflate(R.layout.personal_med_list, container, false);
        //Log.v("PersMedListSize", Medication.medications.size()+"");
        //Puts information from global arraylist into an array
       // names = new String[Medication.medications.size()];
        names = new String[Medication.serverMed.size()];
        nameList = new ArrayList<String>();
        for(int i =0; i < names.length; i ++){
          //  names[i]=Medication.medications.get(i).getName();
           // nameList.add(Medication.medications.get(i).getName());
            names[i]=Medication.serverMed.get(i).getName();
             nameList.add(Medication.serverMed.get(i).getName());
        }
        //Displays the array
        ListAdapter adapter = new ArrayAdapter(
                inflater.getContext(), android.R.layout.simple_list_item_1,
                names);
        setListAdapter(adapter);


        return super.onCreateView(inflater, container, savedInstanceState);


    }
//Handles Click
    @Override
    public void onListItemClick(ListView parent, View view, int position, long id) {
        Log.v("THING TAPPED", id + "<------- ID");
        Log.v("THING TAPPED",position+"<------- ID");
        Context context = getActivity().getApplicationContext();
        CharSequence text = id+" was tapped";
        int duration = Toast.LENGTH_SHORT;
        PersonalMedInformation.personalMedIndex = (int)id;
        //Moves to the personal med information screen with the index of the medication
        Fragment fragment = new PersonalMedInformation();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(((ViewGroup)getView().getParent()).getId(), fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
