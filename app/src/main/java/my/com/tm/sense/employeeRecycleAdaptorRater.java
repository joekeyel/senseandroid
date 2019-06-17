package my.com.tm.sense;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class employeeRecycleAdaptorRater extends RecyclerView.Adapter<employeeRecycleAdaptorRater.MyViewHolder> implements Filterable {

    private List<employeemodel> ttmodellist;
    private List<employeemodel> orig;
    private List<employeemodel> listcheckemployee;
    RequestQueue rq;
    RequestQueue rq2;
    Context ctx;
    private String userposition,activity,activityremark;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView email;
        TextView division;
        TextView staffid;
        TextView averagerate;
        TextView countrater;

        ImageView image;
        Button notifybtn;
        ImageView imagestatus;


        public MyViewHolder(View view) {
            super(view);

            name = (TextView)view.findViewById(R.id.employeename);
            email = (TextView)view.findViewById(R.id.employeeemail);
            division = (TextView)view.findViewById(R.id.employeediv);
            staffid = (TextView)view.findViewById(R.id.employeestaffid);
            image = (ImageView)view.findViewById(R.id.imageViewrow);
            averagerate = (TextView)view.findViewById(R.id.averagerating);
            countrater = (TextView) view.findViewById(R.id.totalrater);
            notifybtn = (Button) view.findViewById(R.id.notify);
            imagestatus = (ImageView)view.findViewById(R.id.imageStatus);


        }
    }


    public employeeRecycleAdaptorRater(List<employeemodel> employeelist, Context ctx, String userposition,String activity,String activityremark) {

        this.ttmodellist = employeelist;
        this.ctx = ctx;
        this.userposition = userposition;
        this.activity = activity;
        this.activityremark = activityremark;
    }


    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<employeemodel> results = new ArrayList<employeemodel>();
                if (orig == null)
                    orig = ttmodellist;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final employeemodel g : orig) {
                            if (g.getName().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);
                            else if (g.getDivision().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);
                            else if(g.getEmail().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);

                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                ttmodellist = (ArrayList<employeemodel>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.employee_row_rater, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {



        holder.name.setText(ttmodellist.get(position).getName());
        holder.email.setText(ttmodellist.get(position).getEmail());
        holder.division.setText(ttmodellist.get(position).getDivision());
        holder.staffid.setText(ttmodellist.get(position).getStaffid());


        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();


        storageRef.child("senseprofile" + "/"+ttmodellist.get(position).getUid()+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // TODO: handle uri

                Context context = holder.image.getContext();

                holder.image.invalidate();

                Picasso.with(context).load(uri).networkPolicy(NetworkPolicy.NO_CACHE).into(holder.image);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("Load Image","No Image exist");


                holder.image.setImageResource(R.mipmap.nine_smiley_selected_round);
            }
        });


        rq = Volley.newRequestQueue(ctx);
        sendrequest1(holder.averagerate,holder.countrater,ttmodellist.get(position).getEmail());



       holder.notifybtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               holder.notifybtn.setVisibility(View.INVISIBLE);


               sendrequest3(holder.imagestatus,ttmodellist.get(position).getEmail());
           }
       });

    }

    @Override
    public int getItemCount() {

        return ttmodellist.size();
    }




    public void sendrequest1(final TextView avg, final TextView rater, final String email){



        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest
                (Request.Method.GET,Config.URL_GET_RATING+"?email="+email , null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            String avgrate = response.getString("average");
                            String numberofrater = response.getString("numberofrater");


                            if(email.equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){


                                avg.setText("Average Rating: "+avgrate);
                                rater.setText("Number Of Rater: "+numberofrater);
                            }else {

                                avg.setVisibility(View.INVISIBLE);
                                rater.setText("Number Of Rater: "+numberofrater);
                            }




                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }

                        , new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        rq.add(jsonObjectRequest);
    }





    private void sendrequest3(final ImageView statusimg, final String email){


        class GetJSON extends AsyncTask<String,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                loading = ProgressDialog.show(ctx,"Loading Data","Wait...",false,false);


            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
//                Toast toast1 = Toast.makeText(ctx, s, Toast.LENGTH_LONG);
//                toast1.show();

                if(!s.isEmpty()){


                    try {
                        JSONObject jsonreturn = new JSONObject(s);

                        String status = jsonreturn.getString("success");
//
//                    Toast toast1 = Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT);
//                    toast1.show();
                        if(status.equals("1")) {

                            statusimg.setVisibility(View.VISIBLE);
                        }
                        else{

                            Toast toast2 = Toast.makeText(ctx, status, Toast.LENGTH_LONG);
                            toast2.show();
                        }



                    } catch (JSONException e) {
                        e.printStackTrace();

                        Toast toast = Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_LONG);
                        toast.show();

                    }

                }
                loading.dismiss();
            }

            @Override
            protected String doInBackground(String... params) {


                StringBuilder sb = new StringBuilder();
                try {
                    URL url = new URL(Config.URL_SEND_NOTIFICATION);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);

                    OutputStream os = conn.getOutputStream();
                    BufferedWriter bufferwriter = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));

                    String data = URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(params[0],"UTF-8")+

                            "&"+URLEncoder.encode("activity","UTF-8")+"="+URLEncoder.encode(params[1], "UTF-8")+
                            "&"+URLEncoder.encode("activityremark","UTF-8")+"="+URLEncoder.encode(params[2], "UTF-8")+
                            "&"+URLEncoder.encode("emailrequestor","UTF-8")+"="+URLEncoder.encode(params[3], "UTF-8");


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
        GetJSON gj = new GetJSON();
        gj.execute(email,activity,activityremark,FirebaseAuth.getInstance().getCurrentUser().getEmail());
    }
}