package my.com.tm.sense;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class generateqrcodestart extends Fragment {

    TextView selectecsmiley;
    String employeename,division,staffid;
    String activityselected = "";
    String remarkactivitystr = "";

    private String emailselected;
    public static generateqrcodestart myactivitymain;
    public static generateqrcodestart getInstance() {

        return myactivitymain;
    }


    View myView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.activity_selectactivityrate_qrcode, container, false);

        myactivitymain = this;

        TextView titlepage = (TextView)myView.findViewById(R.id.tittle);
        titlepage.setText("Please Select Activity For "+FirebaseAuth.getInstance().getCurrentUser().getEmail());

        selectecsmiley = (TextView)myView.findViewById(R.id.selectedsmiley);



        RadioGroup radioGroup = (RadioGroup) myView.findViewById(R.id.toggleGroup);

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


        Button nextpage = (Button)myView.findViewById(R.id.gotoratebutton);
        nextpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText remarkactitivty = (EditText)myView.findViewById(R.id.remarkactitivty);
                remarkactivitystr =  remarkactitivty.getText().toString();

                if(!activityselected.isEmpty() && !remarkactivitystr.isEmpty()) {

//                    Toast toast2 = Toast.makeText(getActivity(), activityselected, Toast.LENGTH_SHORT);
//                    toast2.show();

                    Intent nextpage = new Intent(getActivity(), generateqrcode.class);

                    nextpage.putExtra("activity", activityselected);
                    nextpage.putExtra("remarkactivity", remarkactivitystr);

                    nextpage.putExtra("email", emailselected);
                    startActivity(nextpage);
                }else{


                    Toast toast2 = Toast.makeText(getActivity(), "Please Select Your Activity and Remark cannot be Empty", Toast.LENGTH_SHORT);
                    toast2.show();
                }

            }
        });



        return  myView;
    }


}
