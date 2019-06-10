package my.com.tm.sense;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class RewardActivity extends AppCompatActivity {



    TextView email;
    TextView division;
    TextView staffid;

    String JSON_RESULT;
    AlertDialog alert2;
    AlertDialog alert3;

    public static RewardActivity RewardActivityInstance;
    public static RewardActivity getInstance() {

        return RewardActivityInstance;
    }

    RecyclerView rcReward;
    SectionedRecyclerViewAdapter sectionAdaptor;
    ArrayList<rewardmodel> rewardlist1 = new ArrayList<>();
    ArrayList<rewardmodel> rewardlist2 = new ArrayList<>();
    ArrayList<rewardmodel> rewardlist3 = new ArrayList<>();
    ArrayList<rewardmodel> rewardlist4 = new ArrayList<>();
    ArrayList<rewardmodel> rewardlist5 = new ArrayList<>();

    employeemodel employeepbject;

    String namestr;
    String divisionstr ;
    String staffidstr ;
    String emailstr ;
    String uidstr;
    String updateby;
    String positionstr = "";

  String dateinputstr = "";
  String pointstr = "";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rewardactivitylayout);

        RewardActivityInstance = this;

        Intent i = getIntent();
         namestr = i.getStringExtra("employeename");
         divisionstr = i.getStringExtra("division");
         staffidstr = i.getStringExtra("staffid");
         emailstr = i.getStringExtra("email");
         uidstr = i.getStringExtra("uid");
         positionstr = i.getStringExtra("position");

         updateby = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        employeepbject = new employeemodel();
        employeepbject.setName(namestr);
        employeepbject.setDivision(divisionstr);
        employeepbject.setEmail(emailstr);
        employeepbject.setStaffid(staffidstr);
        employeepbject.setUid(uidstr);





        rcReward = (RecyclerView)findViewById(R.id.rewardRecycleView);




        sectionAdaptor = new SectionedRecyclerViewAdapter();


        sectionAdaptor.addSection(new SectionProfileReward("GENERAL", employeepbject, sectionAdaptor, getApplicationContext()));



        rcReward.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rcReward.setItemAnimator(new DefaultItemAnimator());
        rcReward.setAdapter(sectionAdaptor);

        queryApiReward LoadReward = new queryApiReward();
        LoadReward.execute(Config.URL_LOAD_REWARD, emailstr);

    }


    public void addItem(final String categorystr){

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alert2 = alertDialog.create();

        // alert.setTitle("APPROVAL");
        LayoutInflater inflater1 = getLayoutInflater();
        final View convertView1 = inflater1.inflate(R.layout.addnewrewardheader, null);
        alert2.setCustomTitle(convertView1);


        Button closed = (Button)convertView1.findViewById(R.id.closed);
        closed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert2.dismiss();
            }
        });



        LayoutInflater inflater = getLayoutInflater();

        // inflate the custom popup layout
        final View convertView = inflater.inflate(R.layout.addrewreward, null);


        ArrayList<String> listissueheader = new ArrayList<>();



        if(categorystr.equals("Category 1")){

            listissueheader.clear();
            listissueheader.add("Zero Medical Certificate");
        }

        if(categorystr.equals("Category 2")){

            listissueheader.clear();
            listissueheader.add("High Performer Per Division (9-10)");
            listissueheader.add("Moderate per Division (7-8)");
        }

        if(categorystr.equals("Category 3")){

            listissueheader.clear();
            listissueheader.add("360 > 4 TM Award (GLT,GCEO,CIIC,NPC)");

        }
        if(categorystr.equals("Category 4")){

            listissueheader.clear();
            listissueheader.add("Per 1s Sort,Set in Order, Shine, Standardize,Sustain");

        }

        if(categorystr.equals("Category 5")){

            listissueheader.clear();
            listissueheader.add("Participant of the Event");
            listissueheader.add("Organizer Of the Event/Volunteers");
            listissueheader.add("Supporters of the Event");

        }

        final Spinner spinner1 = (Spinner) convertView.findViewById(R.id.spinner);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,
                        listissueheader);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(adapter2);


        final TextView dateinputtv = (TextView)convertView.findViewById(R.id.dateInput);

        dateinputtv.setText("Select Date");
        dateinputtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(RewardActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                                Calendar c = Calendar.getInstance();
                                c.set(year, month, day);

                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                String formattedDate = df.format(c.getTime());

                                dateinputtv.setText(formattedDate);
                                dateinputstr = formattedDate;

                            }
                        }, year, month, dayOfMonth);

                datePickerDialog.show();
            }


        });


        Button complete = (Button)convertView.findViewById(R.id.complete_button);
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                EditText points = (EditText)convertView.findViewById(R.id.numberinput);
                 pointstr = points.getText().toString();

                String itemstr = spinner1.getSelectedItem().toString();

                if(dateinputstr.equals("") || pointstr.equals("")){

                    Toast toast1 = Toast.makeText(getApplicationContext(), "Pls Insert all fields", Toast.LENGTH_SHORT);
                    toast1.show();


                }
                else{
                 updatereward(namestr,categorystr,pointstr,emailstr,itemstr,updateby);

                alert2.dismiss();
              }
            }
        });



        Button cancel = (Button)convertView.findViewById(R.id.cancel_action);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert2.dismiss();
            }
        });

        alert2.setView(convertView);
        alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));




        alert2.show();




    }


    public void deleteItem(final String id){

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alert3 = alertDialog.create();

        // alert.setTitle("APPROVAL");
        LayoutInflater inflater1 = getLayoutInflater();
        final View convertView1 = inflater1.inflate(R.layout.addnewrewardheader, null);
        alert3.setCustomTitle(convertView1);


        Button closed = (Button)convertView1.findViewById(R.id.closed);
        closed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert3.dismiss();
            }
        });

        TextView headertxt = (TextView)convertView1.findViewById(R.id.title);
        headertxt.setText("DELETE");


        LayoutInflater inflater = getLayoutInflater();

        // inflate the custom popup layout
        final View convertView = inflater.inflate(R.layout.deletereward, null);


        Button complete = (Button)convertView.findViewById(R.id.complete_button);
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                deleteItemApi(id);

                alert3.dismiss();
            }
        });

        Button cancel = (Button)convertView.findViewById(R.id.cancel_action);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert3.dismiss();
            }
        });

        alert3.setView(convertView);
        alert3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));




        alert3.show();




    }

    public void deleteItemApi(String id) {

        class deletereward extends AsyncTask<String,Void,String> {


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON_RESULT = s;
//                Toast toast2 = Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT);
//                toast2.show();

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


                queryApiReward LoadReward = new queryApiReward();
                LoadReward.execute(Config.URL_LOAD_REWARD, emailstr);



            }

            @Override
            protected String doInBackground(String... params) {


                String emailuser = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                StringBuilder sb = new StringBuilder();
                try {
                    URL url = new URL(Config.URL_DELETE_REWARD);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);

                    OutputStream os = conn.getOutputStream();
                    BufferedWriter bufferwriter = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));

                    String data = URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(params[0],"UTF-8")+
                            "&"+URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(emailuser,"UTF-8");

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

        deletereward gaban = new deletereward();
        gaban.execute(id);


    }

    public class queryApiReward extends AsyncTask<String, Integer, String > {


        @Override
        protected String   doInBackground(String... params) {
            HttpURLConnection conn = null;
            BufferedReader reader = null;

            params[1] = params[1].replaceAll(" ", "%20");

            String query = "?email=" + params[1] ;


            try {
                URL url = new URL(params[0] + query);

                conn = (HttpURLConnection) url.openConnection();


                conn.connect();

                InputStream stream = conn.getInputStream();


                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();


                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "");
                }

                String finalJson = buffer.toString();





                return finalJson;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }


        @Override
        protected void onPostExecute(String finaljson) {

//            Log.e("JSON",finaljson);


            if(finaljson != null && finaljson != "") {





                JSONObject parentObject = null;
                try {
                    parentObject = new JSONObject(finaljson);




                    //list approval
                    JSONArray parentArray = parentObject.getJSONArray("category1");

                    rewardlist1.clear();

                    for (int i = 0; i < parentArray.length(); i++) {

                        rewardmodel rewardobject = new rewardmodel();
                        JSONObject finalObject = parentArray.getJSONObject(i);

                        rewardobject.setItem(finalObject.getString("item"));
                        rewardobject.setPoint(finalObject.getString("points"));
                        rewardobject.setId(finalObject.getString("idemployeeReward"));


                        rewardlist1.add(rewardobject);

                    }

                    JSONArray parentArray2 = parentObject.getJSONArray("category2");
                    rewardlist2.clear();
                    for (int i = 0; i < parentArray2.length(); i++) {

                        rewardmodel rewardobject = new rewardmodel();
                        JSONObject finalObject = parentArray2.getJSONObject(i);

                        rewardobject.setItem(finalObject.getString("item"));
                        rewardobject.setPoint(finalObject.getString("points"));
                        rewardobject.setId(finalObject.getString("idemployeeReward"));


                        rewardlist2.add(rewardobject);

                    }


                    JSONArray parentArray3 = parentObject.getJSONArray("category3");
                    rewardlist3.clear();
                    for (int i = 0; i < parentArray3.length(); i++) {

                        rewardmodel rewardobject = new rewardmodel();
                        JSONObject finalObject = parentArray3.getJSONObject(i);

                        rewardobject.setItem(finalObject.getString("item"));
                        rewardobject.setPoint(finalObject.getString("points"));
                        rewardobject.setId(finalObject.getString("idemployeeReward"));


                        rewardlist3.add(rewardobject);

                    }

                    JSONArray parentArray4 = parentObject.getJSONArray("category4");
                    rewardlist4.clear();
                    for (int i = 0; i < parentArray4.length(); i++) {

                        rewardmodel rewardobject = new rewardmodel();
                        JSONObject finalObject = parentArray4.getJSONObject(i);

                        rewardobject.setItem(finalObject.getString("item"));
                        rewardobject.setPoint(finalObject.getString("points"));
                        rewardobject.setId(finalObject.getString("idemployeeReward"));


                        rewardlist4.add(rewardobject);

                    }

                    JSONArray parentArray5 = parentObject.getJSONArray("category5");
                    rewardlist5.clear();


                    for (int i = 0; i < parentArray5.length(); i++) {

                        rewardmodel rewardobject = new rewardmodel();
                        JSONObject finalObject = parentArray5.getJSONObject(i);

                        rewardobject.setItem(finalObject.getString("item"));
                        rewardobject.setPoint(finalObject.getString("points"));
                        rewardobject.setId(finalObject.getString("idemployeeReward"));


                        rewardlist5.add(rewardobject);

                    }







                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("ERROR_JSON",e.getLocalizedMessage());


                }




                sectionAdaptor.removeAllSections();

                sectionAdaptor.addSection(new SectionProfileReward("GENERAL", employeepbject, sectionAdaptor, getApplicationContext()));
// Add your Sections
                sectionAdaptor.addSection(new SectionCategoryReward("Category 1", rewardlist1,sectionAdaptor, getApplicationContext(),positionstr));

                sectionAdaptor.addSection(new SectionCategoryReward("Category 2", rewardlist2,sectionAdaptor, getApplicationContext(),positionstr));

                sectionAdaptor.addSection(new SectionCategoryReward("Category 3", rewardlist3,sectionAdaptor, getApplicationContext(),positionstr));

                sectionAdaptor.addSection(new SectionCategoryReward("Category 4", rewardlist4,sectionAdaptor, getApplicationContext(),positionstr));


                sectionAdaptor.addSection(new SectionCategoryReward("Category 5", rewardlist5,sectionAdaptor, getApplicationContext(),positionstr));

                sectionAdaptor.notifyDataSetChanged();

                

            }


        }
    }


    private void updatereward( String employeename,String category,String points,String email,String item,String updatedby){



        class insertuserreward extends AsyncTask<String,Void,String> {


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


                queryApiReward LoadReward = new queryApiReward();
                LoadReward.execute(Config.URL_LOAD_REWARD, emailstr);



            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> map = new HashMap<>();

                map.put("item",params[4]);
                map.put("email",params[3]);
                map.put("category",params[1]);
                map.put("points",params[2]);
                map.put("employeename",params[0]);


                StringBuilder sb = new StringBuilder();
                try {
                    URL url = new URL(Config.URL_INSERT_REWARD);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);

                    OutputStream os = conn.getOutputStream();
                    BufferedWriter bufferwriter = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));

                    String data = URLEncoder.encode("achievement","UTF-8")+"="+URLEncoder.encode(params[4],"UTF-8")+

                            "&"+URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(params[3],"UTF-8")+

                            "&"+URLEncoder.encode("category","UTF-8")+"="+URLEncoder.encode(params[1],"UTF-8")+
                            "&"+URLEncoder.encode("points","UTF-8")+"="+URLEncoder.encode(params[2],"UTF-8")+
                            "&"+URLEncoder.encode("employeename","UTF-8")+"="+URLEncoder.encode(params[0], "UTF-8") +
                            "&"+URLEncoder.encode("updatedby","UTF-8")+"="+URLEncoder.encode(params[5], "UTF-8")+
                            "&"+URLEncoder.encode("month","UTF-8")+"="+URLEncoder.encode(params[6], "UTF-8");

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


        insertuserreward gaban = new insertuserreward();
        gaban.execute(employeename,category,points,email,item,updatedby,dateinputstr);
    }


}
