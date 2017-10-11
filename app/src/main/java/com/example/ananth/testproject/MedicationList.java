package com.example.ananth.testproject;


import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by Ananth on 12/28/2015.
 */
public class MedicationList extends ListFragment {
    String[] names;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View view = inflater.inflate(R.layout.medication_list, container, false);
        //Puts Med History into array
        names = new String[Medication.completeTakeHistory.size()];
        for(int i =0; i < names.length; i ++){
            names[i]=Medication.completeTakeHistory.get(i);
        }
        //Tells list to display the array
        ListAdapter adapter = new ArrayAdapter(
                inflater.getContext(), android.R.layout.simple_list_item_1,
                names);
        setListAdapter(adapter);
        return super.onCreateView(inflater, container, savedInstanceState);

       // return view;
    }
    //handles clicks
    @Override
    public void onListItemClick(ListView parent, View view, int position, long id) {
        Log.v("THING TAPPED",id+"<------- ID");
        Log.v("THING TAPPED",position+"<------- ID");

    }
}
