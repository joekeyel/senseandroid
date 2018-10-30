package my.com.tm.sense;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class rateactivity extends AppCompatActivity {

    String activity,activityremark,employeename,division,staffid,rating,ratingremark;
    String JSON_RESULT;
    public static rateactivity myactivitymain;
    private String emailselected;

    public static rateactivity getInstance() {

        return myactivitymain;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choosesmiley);


        Intent i = getIntent();
         activity = i.getStringExtra("activity");
         activityremark = i.getStringExtra("remarkactivity");
         employeename = i.getStringExtra("employeename");
         staffid = i.getStringExtra("staffid");
         division = i.getStringExtra("division");
         emailselected = i.getStringExtra("email");

         rating = "";

        final TextView selectecsmiley = (TextView)findViewById(R.id.selectedsmiley);
        final EditText reasoncode = (EditText)findViewById(R.id.reasoncode);


        RadioGroup radioGroup = (RadioGroup)  findViewById(R.id.toggleGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {


                // checkedId is the RadioButton selected
                for (int j = 0; j < group.getChildCount(); j++) {
                    final RadioButton view = (RadioButton) group.getChildAt(j);
                    view.setChecked(view.getId() == checkedId);



                }



                switch(checkedId) {
                    case R.id.imagetoggle1:

//                        Toast.makeText(getActivity(), "1", Toast.LENGTH_SHORT).show();

                        selectecsmiley.setText("You Have selected level 1");
                        rating = "1";


                        break;
                    case R.id.imagetoggle2:

                        selectecsmiley.setText("You Have selected level 2");
                        rating = "2";

                        break;

                    case R.id.imagetoggle3:
                        selectecsmiley.setText("You Have selected level 3");
                        rating = "3";

                        break;

                    case R.id.imagetoggle4:
                        selectecsmiley.setText("You Have selected level 4");
                        rating = "4";
                        // Ninjas rule
                        break;


                    case R.id.imagetoggle5:
                        selectecsmiley.setText("You Have selected level 5");
                        rating = "5";
                        // Ninjas rule
                        break;

                    case R.id.imagetoggle6:
                        selectecsmiley.setText("You Have selected level 6");
                        rating = "6";
                        break;

                    case R.id.imagetoggle7:
                        selectecsmiley.setText("You Have selected level 7");
                        rating = "7";
                        // Ninjas rule
                        break;

                    case R.id.imagetoggle8:
                        selectecsmiley.setText("You Have selected level 8");
                        rating = "8";
                        // Ninjas rule
                        break;

                    case R.id.imagetoggle9:
                        selectecsmiley.setText("You Have selected level 9");
                        rating = "9";
                        // Ninjas rule
                        break;

                    case R.id.imagetoggle10:
                        selectecsmiley.setText("You Have selected level 10");
                        rating = "10";
                        // Ninjas rule
                        break;
                }



            }
        });

        final Button updaterating = (Button)findViewById(R.id.updaterating);

        updaterating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText remark = (EditText)findViewById(R.id.reasoncode);
                final String remarkrating = remark.getText().toString();



                if(!remarkrating.isEmpty() && !rating.isEmpty()){
                LayoutInflater factory = LayoutInflater.from(rateactivity.this);
                final View view = factory.inflate(R.layout.alertdialogupdate,null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(rateactivity.this);
                alertDialogBuilder.setView(view);

                alertDialogBuilder.setMessage("Update Rating?");

                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                                updaterating(employeename,division,staffid,emailselected,uid,activity,activityremark,rating,remarkrating,email);

                            }
                        });

                alertDialogBuilder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
            else{

                    Toast toast1 = Toast.makeText(getApplicationContext(), "Pls select your rating and remark", Toast.LENGTH_SHORT);
                    toast1.show();

                }

            }


        });

    }


    private void updaterating( String employeename,String division,String staffid,String email,String uid,String activity,String activityremark,String rating,String ratingremark,String updatedby){



        class insertuserrating extends AsyncTask<String,Void,String> {


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON_RESULT = s;








                    try {
                        JSONObject jsonreturn = new JSONObject(s);

                        String result = jsonreturn.getString("result");

                        Toast toast1 = Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT);
                        toast1.show();




                    } catch (JSONException e) {
                        e.printStackTrace();

                        Toast toast = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
                        toast.show();

                    }




                if(selectactivityrate.getInstance()!=null){

                    selectactivityrate.getInstance().finish();
                }
                finish();

            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> map = new HashMap<>();

                map.put("uid",params[4]);
                map.put("email",params[3]);
                map.put("division",params[1]);
                map.put("staffid",params[2]);
                map.put("employeename",params[0]);
                map.put("activity",params[5]);
                map.put("activityremark",params[6]);
                map.put("rating",params[7]);
                map.put("ratingremark",params[8]);

                StringBuilder sb = new StringBuilder();
                try {
                    URL url = new URL(Config.URL_EMPLOYEEINSERT_RATING);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);

                    OutputStream os = conn.getOutputStream();
                    BufferedWriter bufferwriter = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));

                    String data = URLEncoder.encode("uid","UTF-8")+"="+URLEncoder.encode(params[4],"UTF-8")+

                            "&"+URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(params[3],"UTF-8")+

                            "&"+URLEncoder.encode("division","UTF-8")+"="+URLEncoder.encode(params[1],"UTF-8")+
                            "&"+URLEncoder.encode("staffid","UTF-8")+"="+URLEncoder.encode(params[2],"UTF-8")+
                            "&"+URLEncoder.encode("employeename","UTF-8")+"="+URLEncoder.encode(params[0], "UTF-8") +
                            "&"+URLEncoder.encode("activity","UTF-8")+"="+URLEncoder.encode(params[5], "UTF-8")+
                            "&"+URLEncoder.encode("activityremark","UTF-8")+"="+URLEncoder.encode(params[6], "UTF-8")+
                            "&"+URLEncoder.encode("rating","UTF-8")+"="+URLEncoder.encode(params[7], "UTF-8")+
                            "&"+URLEncoder.encode("ratingremark","UTF-8")+"="+URLEncoder.encode(params[8], "UTF-8")+
                            "&"+URLEncoder.encode("updatedby","UTF-8")+"="+URLEncoder.encode(params[9], "UTF-8");

                    bufferwriter.write(data);
                    bufferwriter.flush();
                    bufferwriter.close();
                    os.close();

                    InputStream is = conn.getInputStream();



                    int responseCode = conn.getResponseCode();


                    if (responseCode == HttpsURLConnection.HTTP_OK) {

                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        sb = new StringBuilder();
                        String response;
                        //Reading server response
                        while ((response = br.readLine()) != null){
                            sb.append(response);
                        }
                    }

                    is.close();



                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return sb.toString();
            }


        }
        insertuserrating gaban = new insertuserrating();
        gaban.execute(employeename,division,staffid,email,uid,activity,activityremark,rating,ratingremark,updatedby);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
