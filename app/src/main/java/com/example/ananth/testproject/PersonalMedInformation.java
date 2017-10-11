package com.example.ananth.testproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * Created by Ananth on 12/30/2015.
 */
public class PersonalMedInformation extends Fragment {
    static int personalMedIndex = 0;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View view = inflater.inflate(R.layout.personal_med_information, container, false);
        //Defines Textviews
        TextView name = (TextView) view.findViewById(R.id.medName);
        TextView ndc = (TextView) view.findViewById(R.id.ndc);
        TextView ing = (TextView) view.findViewById(R.id.activeIngredients);
        //Defines Button
        Button takeBttn = (Button) view.findViewById(R.id.takeButton);
        Button deleteBttn = (Button) view.findViewById(R.id.removeButton);
        //Sets text to information in the medication that is read
       // name.setText(Medication.medications.get(personalMedIndex).getName());
       // ndc.setText("NDC: "+Medication.medications.get(personalMedIndex).getNDC());
        name.setText(Medication.serverMed.get(personalMedIndex).getName());
         ndc.setText("NDC: "+Medication.serverMed.get(personalMedIndex).getNDC());
          // String  s = Medication.medications.get(personalMedIndex).getActiveIngredients();
        String  s = Medication.serverMed.get(personalMedIndex).getActiveIngredients();

        ing.setText("Active Ingredients: " + s);
       // Log.v("ndcTest", Medication.medications.get(personalMedIndex).getNDC());
      //  Log.v("ingredientsTest",s);
      //  Log.v("nameTest",Medication.medications.get(personalMedIndex).getName());
      // final Medication m = Medication.medications.get(personalMedIndex);
        final Medication m = Medication.serverMed.get(personalMedIndex);
        //Functions as it does in Information Screen
        takeBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Medication.medications.get(personalMedIndex).addToTakeHistory();
                Medication.serverMed.get(personalMedIndex).addToTakeHistory();
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
                Log.v("Date", date.toString());
                Log.v("Date", String.valueOf(date.getTime()));
                Calendar c = Calendar.getInstance();
                String postStart = c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DAY_OF_MONTH)+" "+c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND);
                Log.v("Date",postStart);
                String fullPost = "{\"userMedication\": {\"email\": \""+SignInActivity.email+"\", \"medications\": {\"ndc\": \""+m.getNDC()+"\"}}, \"timeTaken\": \""+postStart+"\"}";
                PostClass postClass = new PostClass("http://"+SignInActivity.serverIP+"/MedHelperApp/medhelper/usermedhistory/detail",fullPost);
                postClass.execute();
            }
        });
        //Removes the med from the global arraylist and then goes to the list screen
        deleteBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ndc = m.getNDC();
                String finalString = "{\"email\": \""+SignInActivity.email+"\", \"medications\": {\"ndc\": \""+ndc+"\"}}";
                DeleteClass delete = new DeleteClass("http://"+SignInActivity.serverIP+"/MedHelperApp/medhelper/usermed/detail",finalString);
                delete.execute();
               // Medication.medications.remove(m);
                Medication.serverMed.remove(m);
                Fragment fragment = new PersonalMedList();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(((ViewGroup) getView().getParent()).getId(), (Fragment) fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        //stores updated data to device

        try {
           //GetMedData.storeMedData(Medication.medications, GetMedData.PERSONAL_MED_TAG);
            GetMedData.storeMedData(Medication.serverMed, GetMedData.PERSONAL_MED_TAG);
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
    public void setPersonalMedIndex(int x){
        personalMedIndex = x;
    }

}
