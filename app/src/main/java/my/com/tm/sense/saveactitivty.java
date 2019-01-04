package my.com.tm.sense;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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

public class saveactitivty extends AppCompatActivity {

    String rating,activity,activityremark,staffid,division,emailselected,employeename,JSON_RESULT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saveactitivty);

        Intent i = getIntent();
         rating = i.getStringExtra("rating");
          employeename = i.getStringExtra("employeename");
          activity = i.getStringExtra("activity");
          activityremark = i.getStringExtra("remarkactivity");
          staffid = i.getStringExtra("staffid");
          division = i.getStringExtra("division");
          emailselected = i.getStringExtra("email");

        TextView desc = (TextView)findViewById(R.id.description);




        if(rating.equals("1")) {
            ImageView imagesmiley = (ImageView) findViewById(R.id.smileyimage);
            imagesmiley.setImageResource(R.mipmap.one_smiley);
            desc.setText("I am sorry to disappoint you. I would love to know how can I improve on what I am doing?");
        }

        if(rating.equals("2")) {
            ImageView imagesmiley = (ImageView) findViewById(R.id.smileyimage);
            imagesmiley.setImageResource(R.mipmap.two_smiley);
            desc.setText("I am sorry to disappoint you. I would love to know how can I improve on what I am doing?");
        }

        if(rating.equals("3")) {
            ImageView imagesmiley = (ImageView) findViewById(R.id.smileyimage);
            imagesmiley.setImageResource(R.mipmap.three_smiley);
            desc.setText("I am sorry to disappoint you. I would love to know how can I improve on what I am doing?");

        }

        if(rating.equals("4")) {
            ImageView imagesmiley = (ImageView) findViewById(R.id.smileyimage);
            imagesmiley.setImageResource(R.mipmap.four_smiley);
            desc.setText("I am sorry to disappoint you. I would love to know how can I improve on what I am doing?");
        }

        if(rating.equals("5")) {
            ImageView imagesmiley = (ImageView) findViewById(R.id.smileyimage);
            imagesmiley.setImageResource(R.mipmap.five_smiley);
            desc.setText("I am sorry to disappoint you. I would love to know how can I improve on what I am doing?");
        }

        if(rating.equals("6")) {
            ImageView imagesmiley = (ImageView) findViewById(R.id.smileyimage);
            imagesmiley.setImageResource(R.mipmap.six_smiley);
            desc.setText("I am sorry to disappoint you. I would love to know how can I improve on what I am doing?");
        }

        if(rating.equals("7")) {
            ImageView imagesmiley = (ImageView) findViewById(R.id.smileyimage);
            imagesmiley.setImageResource(R.mipmap.seven_smiley);
            desc.setText("Thanks for taking the time to share your feedback with me. If I could do just one thing to make you extremely satisfied, what would it be?");
        }


         if(rating.equals("8")) {
             ImageView imagesmiley = (ImageView) findViewById(R.id.smileyimage);
             imagesmiley.setImageResource(R.mipmap.eight_smiley);
             desc.setText("Thanks for taking the time to share your feedback with me. If I could do just one thing to make you extremely satisfied, what would it be?");

         }

        if(rating.equals("9")) {
            ImageView imagesmiley = (ImageView) findViewById(R.id.smileyimage);
            imagesmiley.setImageResource(R.mipmap.nine_smiley);
            desc.setText("Awesome, thanks for your feedback! Would you tell me why you feel that way?");

        }

        if(rating.equals("10")) {
            ImageView imagesmiley = (ImageView) findViewById(R.id.smileyimage);
            imagesmiley.setImageResource(R.mipmap.ten_smiley);
            desc.setText("Awesome, thanks for your feedback! Would you tell me why you feel that way?");
        }


        Button savebutton = (Button)findViewById(R.id.savebutton);
        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText remark = (EditText)findViewById(R.id.reasoncode);
                final String remarkrating = remark.getText().toString();



                if(!remarkrating.isEmpty() && !rating.isEmpty()){
                    LayoutInflater factory = LayoutInflater.from(saveactitivty.this);
                    final View view = factory.inflate(R.layout.alertdialogupdate,null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(saveactitivty.this);
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

                if(rateactivity.getInstance()!=null){

                    rateactivity.getInstance().finish();
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
