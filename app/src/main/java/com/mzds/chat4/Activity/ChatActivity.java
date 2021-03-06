package com.mzds.chat4.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mzds.chat4.Adapter.MessagesAdapter;
import com.mzds.chat4.ModelClass.Messages;
import com.mzds.chat4.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    String receiverImage,receiverName,receiverUid;
    String senderUid;
    CircleImageView profileImage;
    TextView receiverProfileName;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    public static String sImage;
    public static String rImage;

    CardView sendBtn;
    EditText editMessage;

    String senderRoom,receiverRoom;

    RecyclerView messageAdapter;
    ArrayList<Messages> messagesArrayList;

    MessagesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        database=FirebaseDatabase.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();

        receiverName=getIntent().getStringExtra("name");
        receiverUid=getIntent().getStringExtra("uid");
        receiverImage=getIntent().getStringExtra("receiverImg");

        messagesArrayList=new ArrayList<>();

        profileImage=findViewById(R.id.profile_image);
        receiverProfileName=findViewById(R.id.receiverName);

        messageAdapter=findViewById(R.id.message_adapter);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messageAdapter.setLayoutManager(linearLayoutManager);
        adapter=new MessagesAdapter(ChatActivity.this,messagesArrayList);
        messageAdapter.setAdapter(adapter);

        sendBtn=findViewById(R.id.send_button);
        editMessage=findViewById(R.id.editText_message);

        Picasso.get().load(receiverImage).into(profileImage);
        receiverProfileName.setText(""+receiverName);

        senderUid=firebaseAuth.getUid();

        senderRoom=senderUid+receiverUid;
        receiverRoom=receiverUid+senderUid;

        DatabaseReference reference=database.getReference().child("users").child(firebaseAuth.getUid());
        DatabaseReference chatReference=database.getReference().child("chats").child(senderRoom).child("messages");

        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Messages messages = dataSnapshot.getValue(Messages.class);
                    messagesArrayList.add(messages);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                sImage=snapshot.child("imageUri").getValue().toString();
                rImage=receiverImage;
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message=editMessage.getText().toString();
                if (message.isEmpty()){
                    Toast.makeText(ChatActivity.this, "message empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                editMessage.setText("");
                Date date =new Date();

                Messages messages = new Messages(message,senderUid,date.getTime());

                database=FirebaseDatabase.getInstance();
                database.getReference().child("chats")
                        .child(senderRoom)
                        .child("messages")
                        .push()
                        .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        database.getReference().child("chats")
                                .child(receiverRoom)
                                .child("messages")
                                .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {

                            }
                        });
                    }
                });
            }
        });
    }
}