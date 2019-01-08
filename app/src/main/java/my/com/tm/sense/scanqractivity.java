package my.com.tm.sense;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class scanqractivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final String FLASH_STATE = "FLASH_STATE";
    private String JSON_STRING;
    private ZXingScannerView mScannerView;
    private boolean mFlash;
    private String activity,activityremark;

    private ArrayList<employeemodel> employeelist = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanqractivity);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                    new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    2);
        }
        else{

            ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
            mScannerView = new ZXingScannerView(getBaseContext());
            contentFrame.addView(mScannerView);

        }


    }

    @Override
    public void onResume() {
        super.onResume();

if(mScannerView!= null) {
    mScannerView.setResultHandler(this);
    // You can optionally set aspect ratio tolerance level
    // that is used in calculating the optimal Camera preview size
    mScannerView.setAspectTolerance(0.2f);
    mScannerView.startCamera();
    mScannerView.setFlash(mFlash);
}
    }

    @Override
    public void onPause() {
        super.onPause();

        if(mScannerView != null){
        mScannerView.stopCamera();
    }}

    @Override
    public void onStop() {
        super.onStop();
        mScannerView.setResultHandler(null);//prevent Camera is being used after Camera.release() was called
        mScannerView.stopCamera();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FLASH_STATE, mFlash);
    }


    @Override
    public void handleResult(Result result) {




        String scantexttr = result.getText();

        List<String> qrlist = Arrays.asList(scantexttr.split(","));

       // ArrayList<String> qrlist = (ArrayList<String>)Arrays.asList(scantexttr.split(","));


        if(qrlist.size() == 3) {
            String email = qrlist.get(0);
            activity = qrlist.get(1);
            activityremark = qrlist.get(2);


//        Toast.makeText(this, scantexttr, Toast.LENGTH_SHORT).show();

            getJSON(email);

        }else{

            Toast toast = Toast.makeText(getApplicationContext(), "Image is not readable", Toast.LENGTH_SHORT);
            toast.show();
        }

        mScannerView.stopCamera();
        finish();


    }


    private void getJSON(final String query){
        class GetJSON extends AsyncTask<Void,Void,String> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                loading = ProgressDialog.show(getBaseContext(),"Loading Data","Wait...",false,false);
                // loading.setIndeterminateDrawable(getResources().getDrawable(R.drawable.dashb));
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //   loading.dismiss();
                //   loading.setIndeterminateDrawable(getResources().getDrawable(R.drawable.dashb));
                JSON_STRING = s;
                //  showData();
                showEmployee();

            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler3 rh = new RequestHandler3();
                String s = rh.sendGetRequest(Config.URL_EMPLOYEE+"?query="+query);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }


    private void showEmployee(){



        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray("employeelist");

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String a = jo.getString("employeename");
                String b = jo.getString("email");
                String c = jo.getString("staffid");
                String d = jo.getString("division");
                String e = jo.getString("uid");





                employeemodel employeeeobject = new employeemodel();
                employeeeobject.setName(a);
                employeeeobject.setEmail(b);
                employeeeobject.setDivision(d);
                employeeeobject.setStaffid(c);
                employeeeobject.setUid(e);



                employeelist.add(employeeeobject);


            }

            if(employeelist.size()>0) {


                if(!FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(employeelist.get(0).getEmail())){

                    Intent nextpage = new Intent(getApplicationContext(), rateactivity.class);
                    nextpage.putExtra("employeename", employeelist.get(0).getName());
                    nextpage.putExtra("email", employeelist.get(0).getEmail());
                    nextpage.putExtra("division", employeelist.get(0).getDivision());
                    nextpage.putExtra("staffid", employeelist.get(0).getStaffid());
                    nextpage.putExtra("division", employeelist.get(0).getDivision());
                    nextpage.putExtra("staffid", employeelist.get(0).getStaffid());
                    nextpage.putExtra("activity", activity);
                    nextpage.putExtra("remarkactivity", activityremark);


                    startActivity(nextpage);
                }
                else{

                    Toast.makeText(this, "U Cannot Rate Yourself", Toast.LENGTH_SHORT).show();

                }


            }
            else
            {

                Toast.makeText(this, "User Not Found", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }




    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {



        switch (requestCode) {
            case 2: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
                    mScannerView = new ZXingScannerView(this);
                    contentFrame.addView(mScannerView);


                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }



            // other 'switch' lines to check for other
            // permissions this app might request
        }
    }
}
