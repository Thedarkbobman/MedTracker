package com.example.ananth.testproject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
//https://api.fda.gov/drug/event.json?api_key=aSbbD4xehyS7OVQfwEVwqUWYncaQ8oeotBzwzkh8&search=product_ndc:50580-679&limit=1
/**
 * Created by Ananth on 12/26/2015.
 */
public class GetMedData  extends Activity{
   static String ndc;
    public static final String APP_PREFERENCES = "AppPrefs";
    public static final String PERSONAL_MED_TAG = "personalMed";
    public static final String HISTORY_MED_TAG = "historyMed";
    public static final String COMPLETE_TAKE_HISTORY = "completeTakeHistory";
    public static boolean SetMedData(String n,Context context) throws ExecutionException, InterruptedException, JSONException {
        int duration = Toast.LENGTH_SHORT;
        if(!isNetworkConnected(context)){
            CharSequence error = "Connection Could Not Be Established!";


            Toast toast = Toast.makeText(context, error, duration);
            toast.show();
        return false;
        }
        ndc = n;

        String text = ndc;


        Log.v("NDC",text);
        String placeholder = text;
        if(text.length()!=10){
            CharSequence error = "NDC NOT 10 CHARACTERS!";


            Toast toast = Toast.makeText(context, error, duration);
            toast.show();
            return false;
        }
        text = text.substring(0, 5)+"-"+text.substring(5, 8)+"-"+text.substring(8, 10);
        Log.v("NDC",text);
        GetRawData theRawData = new GetRawData("https://dailymed.nlm.nih.gov/dailymed/services/v1/ndc/"+text+"/spls.xml");
        String s = null;
        try {
            s = theRawData.execute();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(s.indexOf("<setid>")==-1){
            text = placeholder.substring(0, 4)+"-"+placeholder.substring(4, 8)+"-"+placeholder.substring(8, 10);
            Log.v("NDC",text);
             theRawData = new GetRawData("https://dailymed.nlm.nih.gov/dailymed/services/v1/ndc/"+text+"/spls.xml");
             s = null;
            try {
                s = theRawData.execute();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
        if(s.indexOf("<setid>")==-1){

        CharSequence error = "NDC Not Found!";


        Toast toast = Toast.makeText(context, error, duration);
        toast.show();
        return false;

        }
        Log.v("STRING DATA",s);

        String name="";
        String spl="";

        Log.v("Data", "Data1");
        Log.v("Data","Data2");
        name =  s;


        Log.v("STRING DATA",s);
        s = s.substring(s.indexOf("<setid>")+7,s.indexOf("</setid>"));
        Log.v("STRING DATA", s);
        name=name.substring(name.indexOf("<title>")+7,name.indexOf("</title>"));
        Log.v("NAME DATA", name + " " + name.indexOf("<title>") + " " + name.indexOf("</title>"));

        theRawData.reset();
        theRawData = new GetRawData("https://dailymed.nlm.nih.gov/dailymed/services/v2/spls/"+s+"/packaging.json");
        String packagingData = "";
        try {
            packagingData = theRawData.execute();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.v("PACKAGING", packagingData);

        try {
            JSONObject jsonObject = new JSONObject(packagingData);
            JSONObject dataObject = jsonObject.getJSONObject("data");
            JSONArray productArray = dataObject.getJSONArray("products");
            JSONObject productObject = productArray.getJSONObject(0);
            JSONArray packagingArray = productObject.getJSONArray("packaging");
            JSONArray ingredientsArray = productObject.getJSONArray("active_ingredients");
            JSONObject packagingObject = packagingArray.getJSONObject(0);
            JSONObject ingredientsObject = ingredientsArray.getJSONObject(0);
            String ing = "";
            //JSONObject[] objects = new JSONObject[ingredientsArray.length()];
            for(int i =0; i < ingredientsArray.length();i++){
            JSONObject object = ingredientsArray.getJSONObject(i);
            String objString = object.toString();
                String strength = objString.substring(objString.indexOf(":")+2,objString.indexOf(",")-1);

                String nameI = objString.substring(objString.indexOf("name")+7,objString.indexOf("}")-2);
                ing+= strength + " of " + nameI+", ";

            }
            ing.substring(0,ing.length()-2);
            name = productObject.getString("product_name");
            Medication.searchList.add(new Medication(name,s,ndc,ing));
            Log.v("MEDICATION ADDED",Medication.searchList.size()+"");
            //   String ndc = packagingObject.getString("ndc");

            Log.v("JSON OBJECT", jsonObject.toString());
            Log.v("JSON OBJECT",productArray.toString());
            Log.v("JSON OBJECT",productObject.toString());
            Log.v("JSON OBJECT",packagingArray.toString());
            Log.v("JSON OBJECT Ingredients",ingredientsArray.toString());
            Log.v("JSON OBJECT",packagingObject.toString());
            Log.v("JSON OBJECT", ingredientsObject.toString());
            Log.v("NAME", name);
            //   Log.v("NDC",ndc+"<----- NDC");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    GetMedData medData = new GetMedData();
        medData.storeMedData(Medication.serverMed,PERSONAL_MED_TAG);
        medData.storeMedData(HISTORY_MED_TAG, Medication.completeTakeHistory);
   return true;
    }
    public static void storeMedData(ArrayList<Medication> m,String type) throws InterruptedException, ExecutionException, JSONException {
        String[] names = new String[m.size()];
        String[] ndcs = new String[m.size()];
        String[] spls = new String[m.size()];
        String[] ings = new String[m.size()];
        String[][] histories = new String[m.size()][];
        for(int i = 0; i < m.size(); i ++){
            names[i] = m.get(i).getName();
            ndcs[i] = m.get(i).getNDC();
            spls[i] = m.get(i).getSpl_id();
            histories[i] = m.get(i).getHistory();
            ings[i] = m.get(i).getActiveIngredients();
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < names.length; i++) {
            sb.append(names[i]).append(";");
        }
        String name = sb.toString();

        sb = new StringBuilder();
        for (int i = 0; i < ndcs.length; i++) {
            sb.append(ndcs[i]).append(";");
        }
        String ndc = sb.toString();

        sb = new StringBuilder();
        for (int i = 0; i < spls.length; i++) {
            sb.append(spls[i]).append(";");
        }
        String spl = sb.toString();
        sb = new StringBuilder();
        for (int i = 0; i < ings.length; i++) {
            sb.append(ings[i]).append(";");
        }
        String ing = sb.toString();
        String history = "";
        for(int i = 0; i < histories.length; i ++){
            history+="[";
             sb = new StringBuilder();
            for (int j = 0; j < histories[i].length; j++) {
                sb.append(histories[i][j]).append(",");
            }
            history+=sb.toString();
            history+="],";
        }

        SharedPreferences settings = MainActivity.settings;
        SharedPreferences.Editor prefEditor = settings.edit();
        prefEditor.putString("Name" + type, name);
        prefEditor.putString("NDC" + type, ndc);
        prefEditor.putString("SPL"+type, spl);
        prefEditor.putString("ING"+type, ing);
        prefEditor.putString("History" + type, history);
        prefEditor.putInt("MedNum" + type, m.size());
        prefEditor.commit();
        String s = settings.getString("Name"+GetMedData.PERSONAL_MED_TAG,"ERROR");
        Log.v("NAMES FROM STORED", s + "<---------------NAMES");
   //     databaseChange();
    }
    public static void storeMedData(String type, ArrayList<String> s) throws ExecutionException, InterruptedException, JSONException {
        String[] history = new String[s.size()];

        for(int i = 0; i < s.size(); i ++){
            history[i] = s.get(i);

        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < history.length; i++) {
            sb.append(history[i]).append(";");
        }
        String historyString = sb.toString();



        SharedPreferences settings = MainActivity.settings;
        SharedPreferences.Editor prefEditor = settings.edit();
        prefEditor.putString(GetMedData.COMPLETE_TAKE_HISTORY, historyString);
        Log.v("COMPLETETAKEHISTORY", historyString);
        prefEditor.commit();
        // post("http://"+serverIP+"/MedHelperApp/medhelper/user/detail","{email:\""+email+"\"}");
        //"+serverIP+"
        GetRawData theRawData = new GetRawData("http://\"+serverIP+\"/MedHelperApp/medhelper/usermed/detail?email="+SignInActivity.email);

        String data = null;
        try {
            data = theRawData.execute();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//databaseChange();

    }
    public static boolean isNetworkConnected(Context c) {
        ConnectivityManager conManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conManager.getActiveNetworkInfo();
        return ( netInfo != null && netInfo.isConnected() );
    }
    public static void databaseChange(Medication m) throws ExecutionException, InterruptedException, JSONException {
       // ArrayList medToAdd = new ArrayList();
    //    for (int i = 0; i < Medication.medications.size();i++){

            String ndc = m.getNDC();
            String name = m.getName();
          GetRawData  theRawData = new GetRawData("http://"+SignInActivity.serverIP+"/MedHelperApp/medhelper/medication/detail?ndc="+m.getNDC());
            String temp = theRawData.execute();
            if(temp.indexOf("ndc")==-1){
                // post("http://"+SignInActivity.serverIP+"/MedHelperApp/medhelper/user/detail","{email:\""+email+"\"}");
                PostClass post = new PostClass("http://"+SignInActivity.serverIP+"/MedHelperApp/medhelper/medication/detail","{\"ndc\":\""+ndc+"\", \"description\":\""+name+";"+m.getActiveIngredients()+"\"}");
                post.execute();
                Log.v("POST","POSTED");
            }
            theRawData = new GetRawData("http://"+SignInActivity.serverIP+"/MedHelperApp/medhelper/usermed/detail?email="+SignInActivity.email);
            // theRawData = new GetRawData("http://"+SignInActivity.serverIP+"/MedHelperApp/medhelper/usermed/detail?email="+"email@email.com");
            temp = theRawData.execute();
            JSONObject obj = new JSONObject(temp);
            JSONArray array = obj.getJSONArray("medications");
            Log.v("JSONOBJECT",obj.toString());
            Log.v("JSONARRAY",array.toString()+"<---");
            Log.v("JSONARRAYLENGTH",array.length()+"");
            boolean b = true;
            for(int j = 0; j < array.length();j++){
                JSONObject obj2 = array.getJSONObject(j);

                String dataNDC= obj2.getString("ndc");

                if(dataNDC.equals(ndc)){
                    b= false;
                    Log.v("BOOLEAN","IT WAS SET FALSE");
                }
            }
            if(b){
             //   medToAdd.add(ndc);
                String finalString = "{\"email\": \""+SignInActivity.email+"\", \"medications\": {\"ndc\": \""+ndc+"\"}}";
                Log.v("FinalString", finalString);
                PostClass post = new PostClass("http://"+SignInActivity.serverIP+"/MedHelperApp/medhelper/usermed/detail",finalString);
                post.execute();
            }
       // }
    }
}
