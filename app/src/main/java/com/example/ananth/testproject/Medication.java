package com.example.ananth.testproject;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Ananth on 12/26/2015.
 */
public class Medication {
    private String name;
    private String spl_id;
    private String ndc;
    private String activeIngredients;
    private String[] ingrediants;
    public static ArrayList<Medication> searchList  = new ArrayList<Medication>();
    //public static ArrayList<Medication> medications  = new ArrayList<Medication>();
    public static ArrayList<Medication> serverMed  = new ArrayList<Medication>();
    public static ArrayList<String> completeTakeHistory  = new ArrayList<String>();
    private ArrayList<String> takeHistory = new ArrayList<String>();
    private long takenNum;
    public Medication(String n, String spl, String ndc, String a){
        name = n;
        spl_id = spl;
        this.ndc = ndc;


        activeIngredients = a;
    }
    public Medication(){
       // medications.add(new Medication("My Medications","","",""));
        searchList.add(new Medication("Medication History","","",""));
    }
    public String getName(){
        return name;
    }
    public String getSpl_id(){
        return spl_id;
}
    public String getNDC(){
        return ndc;
    }

    public String getActiveIngredients() {
        return activeIngredients;
    }



    public void addToTakeHistory(){
        takenNum++;

        Calendar c = Calendar.getInstance();
        String time = c.getTime().toString();
        Log.v("Cal","Current time => " + time);
        takeHistory.add(time);
        this.completeTakeHistory.add(name+" Taken at:"+time);

    }
    public String[] getHistory(){
        String[] s = new String[takeHistory.size()];
    for(int i = 0; i<takeHistory.size(); i ++){
        s[i] = takeHistory.get(i);
    }
        return s;
    }
    public void setHistory(String[] s){
        for(int i = 0 ; i<s.length; i++){
            takeHistory.add(s[i]);
        }
    }
}
