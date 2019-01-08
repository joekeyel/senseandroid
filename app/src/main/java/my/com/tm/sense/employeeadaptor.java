package my.com.tm.sense;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

/**
 * Created by joe on 1/25/2016.
 */
public class employeeadaptor extends ArrayAdapter implements Filterable {

    private List<employeemodel> ttmodellist;
    private List<employeemodel> orig;
    private int resource;
    private LayoutInflater inflator;
    RequestQueue rq;

    public employeeadaptor(Context context, int resource, List<employeemodel> objects) {
        super(context, resource, objects);

        ttmodellist = objects;
        this.resource = resource;
        inflator = (LayoutInflater)getContext().getSystemService(context.LAYOUT_INFLATER_SERVICE);
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

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return ttmodellist.size();
    }

    @Override
    public Object getItem(int position) {
        return ttmodellist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){

            convertView = inflator.inflate(resource,null);
        }


        TextView name;
        TextView email;
        TextView division;
        TextView staffid;
        TextView averagerate;
        TextView countrater;
        final ImageView image;

        name = (TextView)convertView.findViewById(R.id.employeename);
        email = (TextView)convertView.findViewById(R.id.employeeemail);
        division = (TextView)convertView.findViewById(R.id.employeediv);
        staffid = (TextView)convertView.findViewById(R.id.employeestaffid);
        image = (ImageView)convertView.findViewById(R.id.imageViewrow);
        averagerate = (TextView)convertView.findViewById(R.id.averagerating);
        countrater = (TextView) convertView.findViewById(R.id.totalrater);

        name.setText(ttmodellist.get(position).getName());
        email.setText(ttmodellist.get(position).getEmail());
        division.setText(ttmodellist.get(position).getDivision());
        staffid.setText(ttmodellist.get(position).getStaffid());


        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();


        storageRef.child("senseprofile" + "/"+ttmodellist.get(position).getUid()+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // TODO: handle uri

                Context context = image.getContext();

                image.invalidate();

                Picasso.with(context).load(uri).networkPolicy(NetworkPolicy.NO_CACHE).into(image);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Hand
                //
                //
                // le any errors

               // image.setImageDrawable(getContext().getResources().getDrawable(R.drawable.profileicon));

//                Toast toast1 = Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT);
//                toast1.show();
            }
        });


        rq = Volley.newRequestQueue(getContext());
        sendrequest1(averagerate,countrater,ttmodellist.get(position).getEmail());


        return convertView;
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