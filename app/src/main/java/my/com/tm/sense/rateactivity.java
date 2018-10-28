package my.com.tm.sense;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class rateactivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choosesmiley);

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


                        break;
                    case R.id.imagetoggle2:

                        selectecsmiley.setText("You Have selected level 2");

                        break;

                    case R.id.imagetoggle3:
                        selectecsmiley.setText("You Have selected level 3");

                        break;

                    case R.id.imagetoggle4:
                        selectecsmiley.setText("You Have selected level 4");

                        // Ninjas rule
                        break;


                    case R.id.imagetoggle5:
                        selectecsmiley.setText("You Have selected level 5");

                        // Ninjas rule
                        break;

                    case R.id.imagetoggle6:
                        selectecsmiley.setText("You Have selected level 6");

                        break;

                    case R.id.imagetoggle7:
                        selectecsmiley.setText("You Have selected level 7");

                        // Ninjas rule
                        break;

                    case R.id.imagetoggle8:
                        selectecsmiley.setText("You Have selected level 8");

                        // Ninjas rule
                        break;

                    case R.id.imagetoggle9:
                        selectecsmiley.setText("You Have selected level 9");

                        // Ninjas rule
                        break;

                    case R.id.imagetoggle10:
                        selectecsmiley.setText("You Have selected level 10");

                        // Ninjas rule
                        break;
                }



            }
        });

    }
}
