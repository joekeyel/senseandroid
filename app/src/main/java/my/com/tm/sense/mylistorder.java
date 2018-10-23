package my.com.tm.sense;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

public class mylistorder extends Fragment {

    private ListView listView;
    EditText editext;
    Button back;


    View myView;
    private String JSON_STRING;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.choosesmiley, container, false);

        final TextView selectecsmiley = (TextView)myView.findViewById(R.id.selectedsmiley);
        final EditText reasoncode = (EditText)myView.findViewById(R.id.reasoncode);


        RadioGroup radioGroup = (RadioGroup) myView .findViewById(R.id.toggleGroup);

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
                        reasoncode.setVisibility(View.VISIBLE);

                        break;
                    case R.id.imagetoggle2:

                        selectecsmiley.setText("You Have selected level 2");
                        reasoncode.setVisibility(View.VISIBLE);
                        break;

                    case R.id.imagetoggle3:
                        selectecsmiley.setText("You Have selected level 3");
                        reasoncode.setVisibility(View.VISIBLE);
                        break;

                    case R.id.imagetoggle4:
                        selectecsmiley.setText("You Have selected level 4");
                        reasoncode.setVisibility(View.VISIBLE);
                        // Ninjas rule
                        break;


                    case R.id.imagetoggle5:
                        selectecsmiley.setText("You Have selected level 5");
                        reasoncode.setVisibility(View.VISIBLE);
                        // Ninjas rule
                        break;

                    case R.id.imagetoggle6:
                        selectecsmiley.setText("You Have selected level 6");
                        reasoncode.setVisibility(View.VISIBLE);
                        break;

                    case R.id.imagetoggle7:
                        selectecsmiley.setText("You Have selected level 7");
                        reasoncode.setVisibility(View.VISIBLE);
                        // Ninjas rule
                        break;

                    case R.id.imagetoggle8:
                        selectecsmiley.setText("You Have selected level 8");
                        reasoncode.setVisibility(View.INVISIBLE);
                        // Ninjas rule
                        break;

                    case R.id.imagetoggle9:
                        selectecsmiley.setText("You Have selected level 9");
                        reasoncode.setVisibility(View.INVISIBLE);
                        // Ninjas rule
                        break;

                    case R.id.imagetoggle10:
                        selectecsmiley.setText("You Have selected level 10");
                        reasoncode.setVisibility(View.INVISIBLE);
                        // Ninjas rule
                        break;
                }



            }
        });


        return myView;
    }





}
