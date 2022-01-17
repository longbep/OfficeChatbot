package com.example.officechatbot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.officechatbot.Model.Message;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    public static final int MSG_TYPE_USER = 0;
    public static final int MSG_TYPE_BOT = 1;

    private Context context;
    private List<Message> messageList;
    private String avatar;

    public MessageAdapter(Context context, List<Message> messageList,String avatar) {
        this.context = context;
        this.messageList = messageList;
        this.avatar = avatar;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_BOT){
            View view = LayoutInflater.from(context).inflate(R.layout.their_message,parent,false);
            return new MessageAdapter.ViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(context).inflate(R.layout.my_message,parent,false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        Message message = messageList.get(position);
        holder.setIsRecyclable(false);
        if(position>0){
            Message previousMessage = messageList.get(position - 1);
            if(!message.getDate().equals(previousMessage.getDate())){
                holder.tvDate.setText(message.getDate());
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                holder.tvDate.setLayoutParams(params);
            }
        }
        else{
            holder.tvDate.setText(message.getDate());
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            holder.tvDate.setLayoutParams(params);
        }

        holder.tvMessage.setText(message.getText());
        holder.tvTime.setText(message.getTime());
        if(!message.isBot()){
            if(avatar.equals("default")){
                holder.avatar.setImageResource(R.drawable.default_avatar);
            }
            else{
                GlideApp.with(context).load(avatar).into(holder.avatar);
            }
        }
        else{
            holder.avatar.setImageResource(R.drawable.botavatar);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(messageList.get(position).isBot())
            return MSG_TYPE_BOT;
        else
            return MSG_TYPE_USER;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvMessage, tvTime,tvDate;
        public ImageView avatar;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvMessage = (TextView) itemView.findViewById(R.id.tvMessageBody);
            tvTime = (TextView) itemView.findViewById(R.id.tvMessageTime);
            avatar = (ImageView) itemView.findViewById(R.id.avatar);
        }
    }
}
