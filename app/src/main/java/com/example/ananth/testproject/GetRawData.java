package com.example.ananth.testproject;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by dev on 12/5/14.
 * Updated to API 22 on 5/6/15
 */

enum DownloadStatus { IDLE, PROCESSING, NOT_INITIALISED, FAILED_OR_EMPTY, OK}

public class GetRawData {
    private String LOG_TAG = GetRawData.class.getSimpleName();
    private String mRawUrl;
    public String mData;
    private DownloadStatus mDownloadStatus;
    public boolean ok;
    public GetRawData(String mRawUrl) {
        this.mRawUrl = mRawUrl;
        this.mDownloadStatus = DownloadStatus.IDLE;
        ok = true;
    }

    public void reset() {
        this.mDownloadStatus = DownloadStatus.IDLE;
        this.mRawUrl = null;
        this.mData = null;
    }

    public String getmData() {
        return mData;
    }

    public DownloadStatus getmDownloadStatus() {
        return mDownloadStatus;
    }

    public String execute() throws ExecutionException, InterruptedException {
        ok = false;
        Log.v("dStatus",mDownloadStatus+"");
        this.mDownloadStatus = DownloadStatus.PROCESSING;
        Log.v("dStatus",mDownloadStatus+"");
        DownloadRawData downloadRawData = new DownloadRawData();
        //Execute the call to get Data
       String s = downloadRawData.execute(mRawUrl).get();
        Log.v("dStatus", mDownloadStatus + "");
        Log.v("TEST", "LOOK AT ME!!!");
        if(s!=null) {
            Log.v("DATATATATAT", s);
        }
        return s;
    }
//Class that can get data Asynchronously
    public class DownloadRawData extends AsyncTask<String, Void, String> {
//Executed After Data is Retrieved
        protected void onPostExecute(String webData) {
            mData = webData;
            Log.v(LOG_TAG, "Data returned was: " +mData);
            //Changes Info Variables
            if(mData == null) {
                if(mRawUrl == null) {
                    mDownloadStatus = DownloadStatus.NOT_INITIALISED;
                } else {
                    mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
                }
            } else {
                // Success
                mDownloadStatus = DownloadStatus.OK;
            }
            Log.v("dStatus",mDownloadStatus+"");
            ok = true;

        }
//Executed by line 54
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
//Checks to see if a url was passed
            if(params == null)
                return null;

            try {
                //Creates a url object with parameter
                URL url = new URL(params[0]);
                //Creates a URL connection
                urlConnection = (HttpURLConnection) url.openConnection();
                //Sets Request Method to GET. This means that data will be retrieved.
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                //Gets the input stream from the URL Connection. This is the data, but it has to be read first.
                InputStream inputStream = urlConnection.getInputStream();
                if(inputStream == null) {
                    return null;
                }
                //Takes information from the inputstream and returns a String
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                return buffer.toString();

            } catch(IOException e) {
                Log.e(LOG_TAG, "Error", e);
                return null;
            } finally {
                if(urlConnection != null) {
                    urlConnection.disconnect();
                }
                if(reader != null) {
                    try {
                        reader.close();
                    } catch(final IOException e) {
                        Log.e(LOG_TAG,"Error closing stream", e);
                    }
                }
            }
        }
        public Status retrieveStatus(){
            return getStatus();
        }
    }
}
