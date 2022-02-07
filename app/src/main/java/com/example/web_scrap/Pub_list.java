package com.example.web_scrap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class Pub_list extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub_list);
    }

    public class CustomAdapter extends RecyclerView.Adapter<com.example.web_scrap.Pub_list.MyViewHolder> {
        ArrayList article_array;
        Context context;


        public CustomAdapter(Context context, ArrayList personNames) {
            this.context = context;
            this.article_array = personNames;
        }
        @Override
        public com.example.web_scrap.Pub_list.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // infalte the item Layout
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_row_layout, parent, false);
            // set the view's size, margins, paddings and layout parameters
            com.example.web_scrap.Pub_list.MyViewHolder vh = new com.example.web_scrap.Pub_list.MyViewHolder(v); // pass the view to View Holder
            return vh;
        }



        @Override
        public void onBindViewHolder(com.example.web_scrap.Pub_list.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.headline.setText((CharSequence) article_array.get(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send the article name to the view article activity to display it
                    Intent i = new Intent(Pub_list.this ,view_article.class);
                    i.putExtra("Article_title", article_array.get(position).toString());
                    startActivity(i);

                }
            });

        }

        @Override
        public int getItemCount() {
            return article_array.size();

        }


    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView headline;// init the item view's
        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            headline = (TextView) itemView.findViewById(R.id.headline);
        }
    }

}