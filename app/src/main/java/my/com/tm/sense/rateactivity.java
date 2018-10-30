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

import java.util.HashMap;

public class rateactivity extends AppCompatActivity {

    String activity,activityremark,employeename,division,staffid,rating,ratingremark;
    String JSON_RESULT;
    public static rateactivity myactivitymain;
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

                                updaterating(employeename,division,staffid,email,uid,activity,activityremark,rating,ratingremark);

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
        });

    }


    private void updaterating(final String employeename,String division,String staffid,String email,String uid,String activity,String activityremark,String rating,String ratingremark){



        class insertuserrating extends AsyncTask<String,Void,String> {

            ProgressDialog loading;





            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON_RESULT = s;

                if(!s.isEmpty()){



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

                RequestHandler3 rh = new RequestHandler3();
                String s = rh.sendPostRequest(Config.URL_EMPLOYEEINSERT_RATING,map);
                return s;
            }
        }
        insertuserrating gj = new insertuserrating();
        gj.execute(employeename,division,staffid,email,uid,activity,activityremark,rating,ratingremark);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
