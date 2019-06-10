package my.com.tm.sense;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

import java.util.ArrayList;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

class SectionProfileReward extends StatelessSection {
   employeemodel employeeobject;
    String title;
    SectionedRecyclerViewAdapter section;
    Context ctx;
    RequestQueue rq;
    RequestQueue rq2;

    public SectionProfileReward(String title, employeemodel employeeobject, SectionedRecyclerViewAdapter section, Context ctx) {


        super(SectionParameters.builder()
                .itemResourceId(R.layout.employee_row_reward)
                .headerResourceId(R.layout.profile_header)
                .build());
        this.employeeobject = employeeobject;
        this.title = title;
        this.section = section;
        this.ctx = ctx;
    }






    @Override
    public int getContentItemsTotal() {
        return 1;
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        // return a custom instance of ViewHolder for the items of this section
        return new MyItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MyItemViewHolder itemHolder = (MyItemViewHolder) holder;




        itemHolder.name.setText( employeeobject.getName());
        itemHolder.email.setText( employeeobject.getEmail());
        itemHolder.division.setText( employeeobject.getDivision());
        itemHolder.staffid.setText( employeeobject.getStaffid());

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();


        storageRef.child("senseprofile" + "/"+employeeobject.getUid()+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // TODO: handle uri

                Context context = itemHolder.image.getContext();

                itemHolder.image.invalidate();

                Picasso.with(context).load(uri).networkPolicy(NetworkPolicy.NO_CACHE).into(itemHolder.image);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("Load Image","No Image exist");


                itemHolder.image.setImageResource(R.mipmap.nine_smiley_selected_round);
            }
        });


        rq = Volley.newRequestQueue(ctx);
        sendrequest1(itemHolder.averagerate,itemHolder.countrater,employeeobject.getEmail());


    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

        headerHolder.tvTitle.setText(title);

        rq2 = Volley.newRequestQueue(ctx);
        sendrequest2(headerHolder.tvPoints,employeeobject.getEmail());



    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTitle;
        private final TextView tvPoints;

        HeaderViewHolder(View view) {
            super(view);

            tvTitle = (TextView) view.findViewById(R.id.titleCategory);
            tvPoints = (TextView)view.findViewById(R.id.totalScore);

        }
    }




    private class MyItemViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView email;
        TextView division;
        TextView staffid;
        TextView averagerate;
        TextView countrater;
        ImageView image;



        private View rootView;

        private MyItemViewHolder(View view) {
            super(view);


            name = (TextView)view.findViewById(R.id.employeename);
            email = (TextView)view.findViewById(R.id.employeeemail);
            division = (TextView)view.findViewById(R.id.employeediv);
            staffid = (TextView)view.findViewById(R.id.employeestaffid);
            image = (ImageView)view.findViewById(R.id.imageViewrow);
            averagerate = (TextView)view.findViewById(R.id.averagerating);
            countrater = (TextView) view.findViewById(R.id.totalrater);





            rootView = view;
        }
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

    public void sendrequest2(final TextView pointtv, final String email){



        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest
                (Request.Method.GET,Config.URL_GET_REWARD_POINT+"?email="+email , null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            String reward = response.getString("reward");


                                pointtv.setText("Point: "+reward);


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
        rq2.add(jsonObjectRequest);
    }
}
