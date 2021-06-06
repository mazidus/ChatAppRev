package com.mzds.chat4.Activity;

import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mzds.chat4.R;
import com.mzds.chat4.Adapter.UserAdapter;
import com.mzds.chat4.ModelClass.Users;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth auth;
    RecyclerView mainUserRecycleView;
    UserAdapter adapter;
    FirebaseDatabase database;
    ArrayList<Users> usersArrayList;
    ImageView imgLogout,imgSetting;

    private long backPressTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        auth= FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        usersArrayList=new ArrayList<>();

        DatabaseReference reference = database.getReference().child("users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                usersArrayList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Users users=dataSnapshot.getValue(Users.class);
                    usersArrayList.add(users);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        mainUserRecycleView=findViewById(R.id.mainUserRecyclerView);
        mainUserRecycleView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new UserAdapter(HomeActivity.this,usersArrayList);
        mainUserRecycleView.setAdapter(adapter);

        imgLogout=findViewById(R.id.img_logout);
        imgSetting=findViewById(R.id.img_setting);

        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(HomeActivity.this,R.style.Dialog);
                dialog.setContentView(R.layout.dialog_layout);

                TextView yesBtn,noBtn;
                yesBtn=dialog.findViewById(R.id.yes_btn);
                noBtn=dialog.findViewById(R.id.no_btn);

                yesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(HomeActivity.this,RegisterActivity.class));
                    }
                });

                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        imgSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,SettingActivity.class));
            }
        });

        if(auth.getCurrentUser()==null)
        {
            startActivity(new Intent(HomeActivity.this,RegisterActivity.class));
        }
    }

    @Override
    public void onBackPressed() {
        if(backPressTime+2000>System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }else {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        backPressTime=System.currentTimeMillis();
    }
}