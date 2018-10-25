package my.com.tm.sense;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class searchemployee extends Fragment implements ListView.OnItemClickListener{


    private ProgressDialog loading;

    private ListView listView;
    EditText editext;
    Button btnsearch,back;
    SearchView searchView;
    View myView;
    private String JSON_STRING;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.search_employee_layout, container, false);

        listView = (ListView) myView.findViewById(R.id.list);

        listView.setOnItemClickListener(this);

        back = (Button) myView.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(),Ipmsansite.class));
                Fragment fragment = new Ipmsansite();


                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.commit();

            }
        });

        searchView = (SearchView)myView.findViewById(R.id.searchemployee);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // collapse the view ?
                //menu.findItem(R.id.menu_search).collapseActionView();

//               if(query.length()>3){
                Log.e("queryText",query);
              // }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // search goes here !!
                // listAdapter.getFilter().filter(query);
                if(newText.length()>3) {
                    Log.e("queryTextSubmit", newText);

                }
                return false;
            }
        });


        btnsearch = (Button)myView.findViewById(R.id.searchbutton);
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchname = String.valueOf(searchView.getQuery());

                Log.e("queryTextSubmitButton", searchname);

                if(searchname.isEmpty()){
                    Toast toast = Toast.makeText(getActivity(), "Pls enter employee name", Toast.LENGTH_SHORT);
                    toast.show();

                }else {
                    getJSON();

                }
            }
        });



        return myView;
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





                employeemodel employeeeobject = new employeemodel();
                employeeeobject.setName(a);
                employeeeobject.setEmail(b);
                employeeeobject.setDivision(d);
                employeeeobject.setStaffid(c);


                employeelist.add(employeeeobject);


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
//
//        ListAdapter adapter = new SimpleAdapter(
//                getActivity(), list, R.layout.totalpstnsite,
//                new String[]{"STATE","total"},
//
//                new int[]{R.id.satu, R.id.dua});

        employeeadaptor adapter2 =  new employeeadaptor(getActivity(),R.layout.employee_row,employeelist);


        listView.setAdapter(adapter2);

    }


    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                loading = ProgressDialog.show(getApplicationContext(),"Loading Data","Wait...",false,false);
                // loading.setIndeterminateDrawable(getResources().getDrawable(R.drawable.dashb));
            }

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
                String s = rh.sendGetRequest(Config.URL_EMPLOYEE);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//
//       Fragment fragment1 = null;

        employeemodel obj = (employeemodel) listView.getAdapter().getItem(position);

        String empId = obj.getName();



//         fragment1 = new rateemployee ();
//
//        Bundle args = new Bundle();
//        args.putString("employeename", empId);
//        fragment1.setArguments(args);

        Context context = getActivity();
        CharSequence text = empId;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
//
//        if(fragment1 != null){
//            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//            ft.replace(R.id.content_frame, fragment1);
//            ft.commit();
//        }



        Intent nextpage = new Intent(getActivity(),selectactivityrate.class);
        nextpage.putExtra("employeename",empId);
        startActivity(nextpage);

     //   intent.putExtra("STATE", empId);




    }


}
