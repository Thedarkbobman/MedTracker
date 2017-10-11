package com.example.ananth.testproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentIntegratorSupportV4;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;

import java.util.concurrent.ExecutionException;

public class StartScanningFromFragmentActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_activity);
    }

    public static class StartScanningFragment extends android.support.v4.app.Fragment {

        private TextView scanDataTextView;
        private Button startScanButton;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.scan_activity, container, false);
            IntentIntegrator scanIntegrator = new IntentIntegratorSupportV4(StartScanningFragment.this);

            scanIntegrator.initiateScan();

            return view;
        }
        public View onResume(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            // Defines the xml file for the fragment
            View view = inflater.inflate(R.layout.scan_activity, container, false);

            IntentIntegrator scanIntegrator = new IntentIntegratorSupportV4(StartScanningFragment.this);

            scanIntegrator.initiateScan();
            return view;
        }
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            String scanContent = (scanningResult != null) ? scanningResult.getContents() : "";

            if (scanContent == null || scanContent.isEmpty()) {
                Log.v("Scan", "No Data");
            } else {
                Log.v("Scan",scanContent);

                //  ((InformationScreen)fragment).setIndex(Medication.searchList.size() - 1);
                String ndc = scanContent.substring(1, scanContent.length() - 1);
                boolean check = false;
                try {
                    check = GetMedData.SetMedData(ndc, getActivity().getApplicationContext());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                android.support.v4.app.Fragment fragment = null;
                if(check) {
                     fragment = new InformationScreen();
                }
                else{
                   // Intent intent = new Intent(getActivity(), MainActivity.class);
                   // startActivity(intent);
                    fragment = new SearchMedClass();
                }
                int index = Medication.searchList.size() - 1;
                Bundle b = new Bundle();
                b.putInt("index", index);
               // fragment.setArguments(b);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Log.v("Fragment", ((ViewGroup) getView().getParent()).getId() + " ");
                Log.v("Fragment", (android.support.v4.app.Fragment) fragment + "");

                fragmentTransaction.replace(((ViewGroup) getView().getParent()).getId(), (android.support.v4.app.Fragment) fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }

    }

}