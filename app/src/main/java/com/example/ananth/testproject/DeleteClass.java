package com.example.ananth.testproject;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DeleteClass {
    private String data,url;
    Button btn;
    private String response;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    public DeleteClass(String u, String d){
        data = d;
        url = u;
    }
    public void execute(){
        LongOperation op = new LongOperation();
        op.execute();
    }
    private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
              // response = post("http://"+serverIP+"/MedHelperApp/medhelper/user/detail","{\"email\":\""+email+"\"}");
                response  = delete(url,data);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Done";
        }

        @Override
        protected void onPostExecute(String result) {
            Log.v("response",response);
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
    OkHttpClient client = new OkHttpClient();
    String delete(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .delete(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();

    }
}