package com.StanleyDavid.flashchatnewfirebase;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by StevenSeagul on 11/30/2017.
 */

public class ChatListAdapter extends BaseAdapter {

    private Activity mActivity;
    private DatabaseReference mDatabaseReference;
    private String mDisplayName;
    private ArrayList<DataSnapshot> mDataSnapshotList;


    private ChildEventListener mListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
           mDataSnapshotList.add(dataSnapshot);
           notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public ChatListAdapter(Activity activity, DatabaseReference reference, String name) {
        mActivity = activity;
        mDatabaseReference = reference.child("messages");
        mDisplayName = name;
        mDataSnapshotList = new ArrayList<>();
        mDatabaseReference.addChildEventListener(mListener);
    }

    static class ViewHolder {
        TextView authorName;
        TextView body;
        LinearLayout.LayoutParams params;
    }

    @Override
    public int getCount() {
        return mDataSnapshotList.size();
    }

    @Override
    public InstantMessage getItem(int position) {
        DataSnapshot snapshot = mDataSnapshotList.get(position);
        return snapshot.getValue(InstantMessage.class);
    }

    @Override
    public long getItemId(int position) {
        DataSnapshot snapshot = mDataSnapshotList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.chat_msg_row, parent, false);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.authorName = (TextView) convertView.findViewById(R.id.author);
            viewHolder.body = (TextView) convertView.findViewById(R.id.message);
            viewHolder.params = (LinearLayout.LayoutParams) convertView.getLayoutParams();
            convertView.setTag(viewHolder);
        }

        final InstantMessage message = getItem(position);
        final ViewHolder tag = (ViewHolder) convertView.getTag();

        String author = message.getAuthor();
        tag.authorName.setText(author);

        String body = message.getMessage();
        tag.body.setText(body);


            return convertView;
        }

    private void setChatRowAppearance(boolean isItMe, ChatListAdapter.ViewHolder holder){
        if(isItMe){
            holder.params.gravity = Gravity.END;
            holder.authorName.setTextColor(Color.GREEN);
        } else {
            holder.params.gravity = Gravity.START;
            holder.authorName.setTextColor(Color.RED);
        }

        holder.authorName.setLayoutParams(holder.params);
        holder.body.setLayoutParams(holder.params);

    }

    public void cleanup(){
        mDatabaseReference.removeEventListener(mListener);
    }
        
    }

