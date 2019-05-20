package my.com.tm.sense;

import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.List;

public class employeeRecycleAdaptor extends RecyclerView.Adapter<employeeRecycleAdaptor.MyViewHolder> implements Filterable {

    private List<employeemodel> ttmodellist;
    private List<employeemodel> orig;
    RequestQueue rq;
    Context ctx;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView email;
        TextView division;
        TextView staffid;
        TextView averagerate;
        TextView countrater;
        ImageView image;
        Button showRewardBtn;
        Button rateUserBtn;

        public MyViewHolder(View view) {
            super(view);

            name = (TextView)view.findViewById(R.id.employeename);
            email = (TextView)view.findViewById(R.id.employeeemail);
            division = (TextView)view.findViewById(R.id.employeediv);
            staffid = (TextView)view.findViewById(R.id.employeestaffid);
            image = (ImageView)view.findViewById(R.id.imageViewrow);
            averagerate = (TextView)view.findViewById(R.id.averagerating);
            countrater = (TextView) view.findViewById(R.id.totalrater);
            showRewardBtn = (Button)view.findViewById(R.id.showReward);
            rateUserBtn = (Button)view.findViewById(R.id.rateUserBtn);



        }
    }


    public employeeRecycleAdaptor(List<employeemodel> employeelist,Context ctx) {

        this.ttmodellist = employeelist;
        this.ctx = ctx;
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
                .inflate(R.layout.employee_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        employeemodel movie = ttmodellist.get(position);





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

        holder.showRewardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast toast = Toast.makeText(ctx, "Click Button", Toast.LENGTH_SHORT);
//                toast.show();

                Intent nextpage = new Intent(ctx, RewardActivity.class);
                nextpage.putExtra("employeename", ttmodellist.get(position).getName());
                nextpage.putExtra("division",ttmodellist.get(position).getDivision());
                nextpage.putExtra("staffid",ttmodellist.get(position).getStaffid());
                nextpage.putExtra("email",ttmodellist.get(position).getEmail());
                nextpage.putExtra("uid",ttmodellist.get(position).getUid());
                ctx.startActivity(nextpage);

            }
        });


        holder.rateUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                String emailselected = ttmodellist.get(position).getEmail();
                String useremail = FirebaseAuth.getInstance().getCurrentUser().getEmail();



                Context context = ctx;

                int duration = Toast.LENGTH_SHORT;


                if(!emailselected.equals(useremail)) {



                    Intent nextpage = new Intent(ctx, selectactivityrate.class);
                    nextpage.putExtra("employeename", ttmodellist.get(position).getName());
                    nextpage.putExtra("division",ttmodellist.get(position).getDivision());
                    nextpage.putExtra("staffid",ttmodellist.get(position).getStaffid());
                    nextpage.putExtra("email",ttmodellist.get(position).getEmail());
                    ctx.startActivity(nextpage);
                }else{
                    Toast toast2 = Toast.makeText(context, "You cannot rate yourself", duration);
                    toast2.show();

                }





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
}