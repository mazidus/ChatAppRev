package com.mzds.chat4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    String receiverImage,receiverName,receiverUid;
    CircleImageView profileImage;
    TextView receiverProfileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        receiverName=getIntent().getStringExtra("name");
        receiverUid=getIntent().getStringExtra("uid");
        receiverImage=getIntent().getStringExtra("receiverImg");

        profileImage=findViewById(R.id.profile_image);
        receiverProfileName=findViewById(R.id.receiverName);

        Picasso.get().load(receiverImage).into(profileImage);
        receiverProfileName.setText(""+receiverName);
    }
}