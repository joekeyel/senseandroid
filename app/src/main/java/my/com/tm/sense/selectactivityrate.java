package my.com.tm.sense;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class selectactivityrate extends AppCompatActivity {

    TextView selectecsmiley;
    String employeename;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectactivityrate);


        Intent i = getIntent();
        employeename = i.getStringExtra("employeename");



         selectecsmiley = (TextView)findViewById(R.id.selectedsmiley);

        selectecsmiley.setText(employeename);

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




                }



            }
        });


        Button nextpage = (Button)findViewById(R.id.gotoratebutton);
        nextpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent nextpage = new Intent(getApplicationContext(),rateactivity.class);
                nextpage.putExtra("employeename",employeename);
                startActivity(nextpage);

            }
        });



    }
}
