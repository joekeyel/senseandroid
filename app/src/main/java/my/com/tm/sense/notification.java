package my.com.tm.sense;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class notification extends Fragment {

    private ProgressDialog loading;

    private RecyclerView listView;

    View myView;
    private String JSON_STRING;



    private ArrayList<notificationmodel> notificationlist = new ArrayList<>();





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.notification_layout, container, false);




        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();






        getJSON(email);



        return myView;


    }


    private void showEmployee(){
        JSONObject jsonObject = null;

         notificationlist = new ArrayList<>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray("notificationlist");

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String a = jo.getString("msg");
                String b = jo.getString("sender");
                String c = jo.getString("receiver");
                String d = jo.getString("idnotification");
                String e = jo.getString("notificationdate");
                String f = jo.getString("activity");
                String g = jo.getString("activityremark");


                notificationmodel employeeeobject = new notificationmodel();
                employeeeobject.setActivity(f);
                employeeeobject.setActivityremark(g);
                employeeeobject.setSender(b);
                employeeeobject.setReceiver(c);
                employeeeobject.setId(d);
                employeeeobject.setMsg(a);
                employeeeobject.setNotificationdate(e);





                notificationlist.add(employeeeobject);


            }



        } catch (JSONException e) {
            e.printStackTrace();
        }


        listView = (RecyclerView)myView.findViewById(R.id.list);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        listView.setLayoutManager(mLayoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
        NotificationRecycleAdaptor adapter2 =  new NotificationRecycleAdaptor(notificationlist,getActivity());


        listView.setAdapter(adapter2);



    }


    private void getJSON(final String query){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;

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
                String s = rh.sendGetRequest(Config.URL_NOTIFICATION_LIST+"?email="+query);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }






}
