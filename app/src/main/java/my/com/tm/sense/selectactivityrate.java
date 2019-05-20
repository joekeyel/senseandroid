package my.com.tm.sense;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class selectactivityrate extends AppCompatActivity {

    TextView selectecsmiley;
    String employeename,division,staffid;
    String activityselected = "";
    String remarkactivitystr = "";

    private String emailselected;
    public static selectactivityrate myactivitymain;
    public static selectactivityrate getInstance() {

        return myactivitymain;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectactivityrate);



        myactivitymain = this;

        Intent i = getIntent();
        employeename = i.getStringExtra("employeename");
        division = i.getStringExtra("division");
        staffid = i.getStringExtra("staffid");
        emailselected = i.getStringExtra("email");


        TextView titlepage = (TextView)findViewById(R.id.tittle);
        titlepage.setText("Please Select Activity For "+employeename);

         selectecsmiley = (TextView)findViewById(R.id.selectedsmiley);



        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.toggleGroup);

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

                        selectecsmiley.setText("You Have selected call Acivity");
                        activityselected = "call";




                        break;
                    case R.id.imagetoggle2:

                        selectecsmiley.setText("You Have selected Email activity");
                        activityselected = "email";

                        break;

                    case R.id.imagetoggle3:
                        selectecsmiley.setText("You Have selected Meeting Activity");
                        activityselected = "meeting";

                        break;

                    case R.id.imagetoggle4:
                        selectecsmiley.setText("You Have selected Workshop Activity");
                        activityselected = "workshop";

                        // Ninjas rule
                        break;


                    case R.id.imagetoggle5:
                        selectecsmiley.setText("You Have selected Others");
                        activityselected = "others";

                        // Ninjas rule
                        break;




                }



            }
        });


        Button nextpage = (Button)findViewById(R.id.gotoratebutton);
        nextpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText remarkactitivty = (EditText)findViewById(R.id.remarkactitivty);
                remarkactivitystr =  remarkactitivty.getText().toString();

                if(!activityselected.isEmpty() && !remarkactivitystr.isEmpty()) {

                    Toast toast2 = Toast.makeText(getApplicationContext(), activityselected, Toast.LENGTH_SHORT);
                    toast2.show();

                    Intent nextpage = new Intent(getApplicationContext(), rateactivity.class);
                    nextpage.putExtra("employeename", employeename);
                    nextpage.putExtra("activity", activityselected);
                    nextpage.putExtra("remarkactivity", remarkactivitystr);
                    nextpage.putExtra("division", division);
                    nextpage.putExtra("staffid", staffid);
                    nextpage.putExtra("email", emailselected);
                    startActivity(nextpage);
                }else{


                    Toast toast2 = Toast.makeText(getApplicationContext(), "Please Select Your Activity and Remark cannot be Empty", Toast.LENGTH_SHORT);
                    toast2.show();
                }

            }
        });



    }

    @Override
    public void onBackPressed() {

        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        String email = user.getEmail();

        if(email.equals("isyraf@tm.com.my")) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
        }
        return true;
    }
}
