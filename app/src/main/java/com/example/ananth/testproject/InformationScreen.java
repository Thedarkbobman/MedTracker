package com.example.ananth.testproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * Created by Ananth on 12/26/2015.
 */
public class InformationScreen extends Fragment{
   static int index,index2;
    Button addButton,takeButton;
    Activity activity;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View view = inflater.inflate(R.layout.information_screen, container, false);
        Bundle b = getArguments();
        //Index is set to thee Medication most recently added to the search list.
        index = Medication.searchList.size() - 1;
        Log.v("index",Medication.searchList.size() - 1+"");
       // index = index2;
        //Defines TextViews that will display information
        TextView name = (TextView) view.findViewById(R.id.Name);
        TextView spl = (TextView) view.findViewById(R.id.SPL);
        TextView ing = (TextView) view.findViewById(R.id.activeIng);
        Log.v("Med List Size",Medication.searchList.size()+"");
        Log.v("Index",index+"");
        //Takes the Medication object that information was put into
        final Medication m = Medication.searchList.get(index);
        Log.v("Med", m.toString());
        //Uses the defined Medication to set the labels' text
        name.setText("NAME:" + m.getName());
        spl.setText("NDC:" + m.getNDC());
        ing.setText("Ingredients:" + m.getActiveIngredients());
        //Defines a button and sets a listener
        addButton = (Button) view.findViewById(R.id.addToListButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMedication(m);
            }
        });
        //Defines a button and sets a listener
        takeButton = (Button) view.findViewById(R.id.takeButton);
        takeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Checks if the medication the user wants to take is in their med list. If it isn't it adds it.
                if (!Medication.serverMed.contains(m)) {
                    addMedication(m);
                }
                //Adds the medication to the taken history
                m.addToTakeHistory();
                //If they have it set to use email then this is activated
                if(EmailSetup.useBetaEmail) {
                    String end = m.getName() + " was taken on " + m.getHistory()[m.getHistory().length - 1] + " by " + SignInActivity.email;
                    end = end.replaceAll(" ", "%20");
                    GetRawData data = new GetRawData("https://script.google.com/macros/s/AKfycbx-cK88cTPOQy4or6YRUyHbt7-9a-FVAZQfb3-1-Pj3t_3AraOR/exec?email=" + EmailSetup.email + "&subject=MedHelper%20Notification&body=" + end);
                    try {
                        String stringData = data.execute();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else if (EmailSetup.useEmail) {
                    //Starts an intent to the stored email with the message being the name of the med and when it was taken
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", EmailSetup.email, null));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "MedHelper Notification");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, m.getName() + " was taken on " + m.getHistory()[m.getHistory().length - 1]);
                    startActivity(Intent.createChooser(emailIntent, "Send email..."));

                }
                Date date = new Date(System.currentTimeMillis());
                Log.v("Date",date.toString());
                Log.v("Date", String.valueOf(date.getTime()));
                Calendar c = Calendar.getInstance();
                String postStart = c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DAY_OF_MONTH)+" "+c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND);
                Log.v("Date",postStart);
                String fullPost = "{\"userMedication\": {\"email\": \""+SignInActivity.email+"\", \"medications\": {\"ndc\": \""+m.getNDC()+"\"}}, \"timeTaken\": \""+postStart+"\"}";
                PostClass postClass = new PostClass("http://"+SignInActivity.serverIP+"/MedHelperApp/medhelper/usermedhistory/detail",fullPost);
                postClass.execute();

            }
        });
        //Stores the personal med data nad personal take history
        try {
            GetMedData.storeMedData(Medication.serverMed, GetMedData.PERSONAL_MED_TAG);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            GetMedData.storeMedData(GetMedData.COMPLETE_TAKE_HISTORY, Medication.completeTakeHistory);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;

    }
    public  Activity returnActivity(){
        return getActivity();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }
    public void addMedication(Medication m){

        boolean b = true;
        //Checks if the medication is already in the User's medication list
        for (int i = 0; i < Medication.serverMed.size(); i++) {
            if (Medication.serverMed.get(i).getNDC().equals(m.getNDC())) {
                b = false;
            }
        }
        if (b) {
            //Adds the medication to the user's med list.
            Medication.serverMed.add(m);
            Context context = getActivity().getApplicationContext();
            CharSequence text = "Medication Added!";
            int duration = Toast.LENGTH_SHORT;
            Log.v("B", m.getName());
            //Displays a message that the medication was added.
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            GetMedData med = new GetMedData();
            //Stores the Medication to device memory

            try {
                med.storeMedData(Medication.serverMed, GetMedData.PERSONAL_MED_TAG);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            //Alerts the User that medication is already in their list.
            Context context = getActivity().getApplicationContext();
            CharSequence text = "This Medication is Already In Your List!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        try {
            GetMedData.databaseChange(m);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
