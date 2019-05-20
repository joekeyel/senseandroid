package my.com.tm.sense;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class RewardActivity extends AppCompatActivity {


    TextView name;
    TextView email;
    TextView division;
    TextView staffid;
    TextView averagerate;
    TextView countrater;
    ImageView image;
    Button showRewardBtn;
    Button rateUserBtn;


    RecyclerView rcReward;
    SectionedRecyclerViewAdapter sectionAdaptor;
    ArrayList<categotymodel> category1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rewardactivitylayout);


        Intent i = getIntent();
        String namestr = i.getStringExtra("employeename");
        String divisionstr = i.getStringExtra("division");
        String staffidstr = i.getStringExtra("staffid");
        String emailstr = i.getStringExtra("email");
        String uidstr = i.getStringExtra("uid");



        name = (TextView)findViewById(R.id.employeename);
        email = (TextView)findViewById(R.id.employeeemail);
        division = (TextView)findViewById(R.id.employeediv);
        staffid = (TextView)findViewById(R.id.employeestaffid);
        image = (ImageView)findViewById(R.id.imageViewrow);
        averagerate = (TextView)findViewById(R.id.averagerating);
        countrater = (TextView) findViewById(R.id.totalrater);
        showRewardBtn = (Button)findViewById(R.id.showReward);
        rateUserBtn = (Button)findViewById(R.id.rateUserBtn);

        rcReward = (RecyclerView)findViewById(R.id.rewardRecycleView);


        name.setText(namestr);
        email.setText(emailstr);
        division.setText(divisionstr);
        staffid.setText(staffidstr);


        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();


        storageRef.child("senseprofile" + "/"+uidstr+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
                Log.e("Load Image","No Image exist");


                image.setImageResource(R.mipmap.nine_smiley_selected_round);
            }
        });


        categotymodel category = new categotymodel();
        category.setItem("Achievement 1");
        category.setPoint("200");

        category1.add(category);

        category.setItem("Achievement 2");
        category.setPoint("300");

        category1.add(category);
        category.setItem("Achievement 3");
        category.setPoint("250");

        category1.add(category);

        sectionAdaptor.addSection(new SectionCategoryReward("CATEGORY 1", category1, sectionAdaptor, getApplicationContext()));

        rcReward.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rcReward.setItemAnimator(new DefaultItemAnimator());
        rcReward.setAdapter(sectionAdaptor);
    }



}
