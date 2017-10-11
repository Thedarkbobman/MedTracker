package com.example.ananth.testproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
  public static SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


//Sets Up Navigation Bar
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Gets the stored data and displays it
         settings = getApplicationContext().getSharedPreferences(GetMedData.APP_PREFERENCES, 0);
        String names = settings.getString("Name"+GetMedData.PERSONAL_MED_TAG,"");
        Log.v("NAMES FROM STORED", names + "<---------------NAMES");
        String history = settings.getString(GetMedData.COMPLETE_TAKE_HISTORY,"");
        Log.v("HISTORY FROM STORED", history + "<---------------HISTORY");
        String ndc = settings.getString("NDC"+GetMedData.PERSONAL_MED_TAG,"");
        Log.v("NDC FROM STORED", ndc + "<---------------NDC");
        String spl = settings.getString("SPL"+GetMedData.PERSONAL_MED_TAG,"");
        Log.v("SPL FROM STORED", spl + "<---------------SPL");
        String ing = settings.getString("ING"+GetMedData.PERSONAL_MED_TAG,"");
        Log.v("ING FROM STORED", ing + "<---------------ING");
        Log.v("PersMedListSize", Medication.serverMed.size()+"");
        Medication.serverMed.clear();
        Log.v("PersMedListSize", Medication.serverMed.size() + "");
        Medication.completeTakeHistory.clear();
        //Stores the saved information in arrays
        String[] namesArray = names.split(";", -1);
        String[] ndcArray = ndc.split(";", -1);
        String[] splArray = spl.split(";", -1);
        String[] historyArray = history.split(";", -1);
        String[] ingArray = ing.split(";", -1);
        Log.v("PersMedListSize", names + " s");
        Log.v("PersMedListSize", namesArray.length + " s");
        //Takes the data in Arrays and set the values around the app
    /*    for (int i = 0; i < namesArray.length; i++) {
            Log.v("Names",namesArray[i] +" " + namesArray[i].length());

        }
        if(!names.equals(";")) {
            for (int i = 0; i < namesArray.length; i++) {
                if(!namesArray[i].equals("")&&!namesArray[i].equals("ERROR"))
                Medication.serverMed.add(new Medication(namesArray[i], splArray[i], ndcArray[i],ingArray[i]));
                Log.v("ING FROM STORED", ingArray[i] + "<---------------ING");
            }
        }
        if(!names.equals(";")) {
            for (int i = 0; i < historyArray.length; i++) {
                if(!historyArray[i].equals("")&&!historyArray[i].equals("ERROR"))
                Medication.completeTakeHistory.add(historyArray[i]);
            }
        }*/


        //Moves from the main activity to the main screen

        /*Fragment fragment = new MainPage();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, fragment);
        ft.commit();*/
       /* GetRawData theRawData = new GetRawData("http://localhost:8080/MedHelperApp/medhelper/user/detail?email=test1@test.com");
        String s = null;
        try {
            s = theRawData.execute();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
            Log.v("LocalData",s);*/
        Intent firstpage= new Intent(this,SignInActivity.class);
        this.startActivity(firstpage);

    }






    public static SharedPreferences getSharedPref(){
        return settings;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_main) {


            Fragment fragment = new MainPage();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, fragment);
            ft.addToBackStack(null);
            ft.commit();

        }
        else if (id == R.id.nav_gallery) {
            Log.v("Delay","Delay of time");


            Fragment fragment = new SearchMedClass();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, fragment);
            ft.addToBackStack(null);
            ft.commit();

        }
        else if (id == R.id.nav_camera) {

            /*Intent intent = new Intent(MainActivity.this,ScanActivity.class);
            startActivity(intent);*/
            Fragment fragment = new StartScanningFromFragmentActivity.StartScanningFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }
        else if (id == R.id.nav_slideshow) {
            Fragment fragment = new MedicationList();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }
        else if (id == R.id.nav_manage) {
            Fragment fragment = new PersonalMedList();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }
        else if (id == R.id.nav_email){
            Fragment fragment = new EmailSetup();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }

       /* else if (id == R.id.nav_share) {
        } else if (id == R.id.nav_send) {
        }*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

public static void collectData(){
    String email = settings.getString("UserEmail","");
    Boolean shouldUseEmail = settings.getBoolean("ShouldUseEmail", false);
    Boolean shouldUseBeta = settings.getBoolean("ShouldUseBetaEmail",false);
    EmailSetup.useBetaEmail = shouldUseBeta;
    EmailSetup.email = email;
    if(email.equals(""))
        EmailSetup.useEmail = false;
    Log.v("EmailBool", EmailSetup.useEmail + "");
    Log.v("Email", EmailSetup.email + "");
    String googEmail = settings.getString("PersonalEmail","");
    SignInActivity.email = googEmail;
    GetRawData data = new GetRawData("http://"+SignInActivity.serverIP+"/MedHelperApp/medhelper/usermed/detail?email="+SignInActivity.email);
    String stringData=null;
    try {
        stringData = data.execute();
    } catch (ExecutionException e) {
        e.printStackTrace();
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    Log.v("USERDATASERVER",stringData);
    try {
        JSONObject object = new JSONObject(stringData);
        JSONArray array = object.getJSONArray("medications");
        Log.v("USERDATASERVER", array.toString());
        for(int i = 0; i < array.length();i++) {
            JSONObject dataObj = array.getJSONObject(i);
            String ndcObj = dataObj.getString("ndc");
            String nameAndIng = dataObj.getString("description");
            int index = nameAndIng.indexOf(";");
            String name = nameAndIng.substring(0, index);
            String ingred = nameAndIng.substring(index+1);
            Log.v("USERDATASERVER",ndcObj);
            Medication.serverMed.add(new Medication(name, "", ndcObj, ingred));
            Log.v("USERDATASERVER",Medication.serverMed.get(Medication.serverMed.size()-1).getNDC());
        }
    } catch (JSONException e) {
        e.printStackTrace();
    }
    for(int i = 0;i<Medication.serverMed.size();i++){
        data.reset();
        data = new GetRawData("http://"+SignInActivity.serverIP+"/MedHelperApp/medhelper/usermedhistory/detail?email="+SignInActivity.email+"&ndc="+Medication.serverMed.get(i).getNDC());
        String historyData = null;
        try {
            historyData = data.execute();
            Log.v("USERDATASERVER",historyData);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            JSONArray jsonArray = new JSONArray(historyData);
            for(int j = 0 ; j < jsonArray.length();j++){
                JSONObject obj = jsonArray.getJSONObject(j);
                Log.v("USERDATASERVER",obj.toString()+"<------OBJ");
                Medication.completeTakeHistory.add(Medication.serverMed.get(i).getName()+" taken on " + obj.getString("timeTaken"));
            }
            // Log.v("USERDATASERVER",object.toString()+"<------OBJ");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
}
