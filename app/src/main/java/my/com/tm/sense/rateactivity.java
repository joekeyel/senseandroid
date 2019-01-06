package my.com.tm.sense;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    android.app.AlertDialog alert;

    public static rateactivity getInstance() {

        return myactivitymain;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choosesmiley);

        myactivitymain = this;

        Intent i = getIntent();
         activity = i.getStringExtra("activity");
         activityremark = i.getStringExtra("remarkactivity");
         employeename = i.getStringExtra("employeename");
         staffid = i.getStringExtra("staffid");
         division = i.getStringExtra("division");
         emailselected = i.getStringExtra("email");

         rating = "";



        RadioGroup radioGroup = (RadioGroup)  findViewById(R.id.toggleGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {


                // checkedId is the RadioButton selected
                for (int j = 0; j < group.getChildCount(); j++) {
                    final RadioButton view = (RadioButton) group.getChildAt(j);
                    view.setChecked(view.getId() == checkedId);



                }

                Intent nextpage = new Intent(getApplicationContext(), saveactitivty.class);
                nextpage.putExtra("employeename", employeename);
                nextpage.putExtra("activity", activity);
                nextpage.putExtra("remarkactivity", activityremark);
                nextpage.putExtra("division", division);
                nextpage.putExtra("staffid", staffid);
                nextpage.putExtra("email", emailselected);

                switch(checkedId) {
                    case R.id.imagetoggle:

//                        Toast.makeText(getActivity(), "1", Toast.LENGTH_SHORT).show();


                        rating = "1";
                        nextpage.putExtra("rating", rating);
                        startActivity(nextpage);


                        break;
                    case R.id.imagetoggle2:


                        rating = "2";
                        nextpage.putExtra("rating", rating);
                        startActivity(nextpage);

                        break;

                    case R.id.imagetoggle3:

                        rating = "3";
                        nextpage.putExtra("rating", rating);
                        startActivity(nextpage);

                        break;

                    case R.id.imagetoggle4:

                        rating = "4";
                        nextpage.putExtra("rating", rating);
                        startActivity(nextpage);
                        // Ninjas rule
                        break;


                    case R.id.imagetoggle5:

                        rating = "5";
                        nextpage.putExtra("rating", rating);
                        startActivity(nextpage);
                        // Ninjas rule
                        break;

                    case R.id.imagetoggle6:

                        rating = "6";
                        nextpage.putExtra("rating", rating);
                        startActivity(nextpage);
                        break;

                    case R.id.imagetoggle7:

                        rating = "7";
                        nextpage.putExtra("rating", rating);
                        startActivity(nextpage);
                        // Ninjas rule
                        break;

                    case R.id.imagetoggle8:

                        rating = "8";
                        nextpage.putExtra("rating", rating);
                        startActivity(nextpage);
                        // Ninjas rule
                        break;

                    case R.id.imagetoggle9:

                        rating = "9";
                        nextpage.putExtra("rating", rating);
                        startActivity(nextpage);
                        // Ninjas rule
                        break;

                    case R.id.imagetoggle10:

                        rating = "10";
                        nextpage.putExtra("rating", rating);
                        startActivity(nextpage);
                        // Ninjas rule
                        break;
                }



            }
        });

        showalert();


    }





        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_main, menu);

            // return true so that the menu pop up is opened
            return true;
        }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch(item.getItemId())
        {
            case R.id.showinfo:

                showalert();

                break;

        }
        return true;
    }

    private void showalert() {

        final android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(this);

        alert = alertDialog.create();

        LayoutInflater inflater1 = getLayoutInflater();
        final View convertView1 = inflater1.inflate(R.layout.edit_approval_header, null);
        alert.setCustomTitle(convertView1);

        Button closed = (Button)convertView1.findViewById(R.id.closed);
        closed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });

        LayoutInflater inflater = getLayoutInflater();

        // inflate the custom popup layout
        final View convertView = inflater.inflate(R.layout.edit_approval, null);


        String text = "Welcome ''"+FirebaseAuth.getInstance().getCurrentUser().getEmail()+"'' to "+employeename+" Survey Portal!\n" +
                "Are you happy to work with me in the last engagement?\n" +
                "Please rate from 0 (Extremely Difficult) to 10 (Extremely Easy)\n"+
                "Activity:"+activity+"\n"+
                "Description:"+activityremark;

        TextView desc = (TextView)convertView.findViewById(R.id.description);
        desc.setText(text);

        alert.setView(convertView);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.show();

    }


    @Override
    public void onBackPressed() {
        finish();
    }
}
