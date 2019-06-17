package my.com.tm.sense;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
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
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class NotificationRecycleAdaptor extends RecyclerView.Adapter<NotificationRecycleAdaptor.MyViewHolder> implements Filterable {

    private List<notificationmodel> notificationlist;
    private List<notificationmodel> orig;

    RequestQueue rq;
    RequestQueue rq2;
    Context ctx;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView msg;
        TextView notificationdate;




        public MyViewHolder(View view) {
            super(view);

            msg = (TextView)view.findViewById(R.id.msg);
            notificationdate = (TextView)view.findViewById(R.id.notificationdate);



        }
    }


    public NotificationRecycleAdaptor(List<notificationmodel> notificationlist, Context ctx) {

        this.notificationlist = notificationlist;
        this.ctx = ctx;

    }


    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<notificationmodel> results = new ArrayList<notificationmodel>();
                if (orig == null)
                    orig = notificationlist;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final notificationmodel g : orig) {
                            if (g.getMsg().toLowerCase()
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
                notificationlist = (ArrayList<notificationmodel>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {



        holder.msg.setText(notificationlist.get(position).getMsg());
        holder.notificationdate.setText(notificationlist.get(position).getNotificationdate());




    }

    @Override
    public int getItemCount() {

        return notificationlist.size();
    }



}