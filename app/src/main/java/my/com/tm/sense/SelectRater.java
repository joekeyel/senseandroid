package my.com.tm.sense;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;

public class SelectRater extends AppCompatActivity {

    private ProgressDialog loading;

    private RecyclerView listView;
    EditText editext;
    Button btnsearch,back;
    SearchView searchView;
    View myView;
    private String JSON_STRING;
    private String JSON_RESULT;
    private AlertDialog.Builder alertDialogform;
    AlertDialog alertform = null;

    private ArrayList<employeemodel> employeelist = new ArrayList<>();
    private String activity,activityremark;
    private String userposition;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_rater);



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        String email = user.getEmail();

        Intent i = getIntent();
        activity = i.getStringExtra("activity");
        activityremark = i.getStringExtra("activityremark");

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + activity);




        searchView = (SearchView)findViewById(R.id.searchemployee);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                Log.e("queryText",query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if(newText.length()>3) {
                    Log.e("queryTextSubmit", newText);

                }
                return false;
            }
        });


        //getJSON(email);


        btnsearch = (Button)findViewById(R.id.searchbutton);
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchname = String.valueOf(searchView.getQuery());

                Log.e("queryTextSubmitButton", searchname);

                if(searchname.isEmpty()){
                    Toast toast = Toast.makeText(getApplicationContext(), "Pls enter employee name", Toast.LENGTH_SHORT);
                    toast.show();

                }else {
                    getJSON(searchname);

                }
            }
        });









    }


    private void showEmployee(){
        JSONObject jsonObject = null;

        ArrayList<employeemodel> employeelist = new ArrayList<>();
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
                String f = jo.getString("position");


                employeemodel employeeeobject = new employeemodel();
                employeeeobject.setName(a);
                employeeeobject.setEmail(b);
                employeeeobject.setDivision(d);
                employeeeobject.setStaffid(c);
                employeeeobject.setUid(e);
                employeeeobject.setPosition(f);

                if(b.equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){

                    userposition = f;
                }



                employeelist.add(employeeeobject);


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        listView = (RecyclerView)findViewById(R.id.list);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(mLayoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
        employeeRecycleAdaptorRater adapter2 =  new employeeRecycleAdaptorRater(employeelist,this,userposition,activity,activityremark);


        listView.setAdapter(adapter2);



    }


    private void getJSON(final String query){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;

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






}
