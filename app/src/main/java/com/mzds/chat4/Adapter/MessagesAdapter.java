package com.mzds.chat4.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.mzds.chat4.ModelClass.Messages;
import com.mzds.chat4.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.mzds.chat4.Activity.ChatActivity.rImage;
import static com.mzds.chat4.Activity.ChatActivity.sImage;

public class MessagesAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Messages> messagesArrayList;
    int ITEM_SEND=1;
    int ITEM_RECEIVE=2;

    public MessagesAdapter(Context context, ArrayList<Messages> messagesArrayList) {
        this.context = context;
        this.messagesArrayList = messagesArrayList;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        if(viewType==ITEM_SEND){
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout_item,parent,false);
            return new SenderViewHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.receiver_layout_item,parent,false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        Messages messages= messagesArrayList.get(position);

        if(holder.getClass()==SenderViewHolder.class){
            SenderViewHolder viewHolder= (SenderViewHolder) holder;
            viewHolder.txtMessage.setText(messages.getMessage());
            Picasso.get().load(sImage).into(viewHolder.circleImageView);
        }else {
            ReceiverViewHolder viewHolder= (ReceiverViewHolder) holder;
            viewHolder.txtMessage.setText(messages.getMessage());
            Picasso.get().load(rImage).into(viewHolder.circleImageView);
        }
    }

    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Messages messages = messagesArrayList.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderId())){
            return ITEM_SEND;
        }else {
            return ITEM_RECEIVE;
        }
    }

    class SenderViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView txtMessage;

        public SenderViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            circleImageView=itemView.findViewById(R.id.profile_image);
            txtMessage=itemView.findViewById(R.id.text_messages);
        }
    }
    class ReceiverViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView txtMessage;

        public ReceiverViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            circleImageView=itemView.findViewById(R.id.profile_image);
            txtMessage=itemView.findViewById(R.id.text_messages);
        }
    }
}
