package com.example.web_scrap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView list_view;

    ImageView image;
    FloatingActionButton refresh_btn;
    FloatingActionButton list_btn;
    FloatingActionButton add_btn;
    public ArrayList<String> article_array;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        article_array = new ArrayList<>();



        list_view = findViewById(R.id.article_list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        list_view.setLayoutManager(linearLayoutManager);
        //  call the constructor of CustomAdapter to send the reference and data to Adapter
        CustomAdapter customAdapter;
        customAdapter = new CustomAdapter(MainActivity.this, article_array);
        list_view.setAdapter(customAdapter); // set the Adapter to RecyclerView


        refresh_btn = findViewById(R.id.refresh);
        list_btn = findViewById(R.id.pub_list);
        add_btn = findViewById(R.id.add_pub);

        new doIT().execute();

        //customAdapter.notifyDataSetChanged();
        customAdapter.notifyDataSetChanged();
        //On click listener for refresh button
        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new doIT().execute();
                customAdapter.notifyDataSetChanged();

            }
        });

        //On click listener for publication list button
        list_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

            }
        });

        //On click listener for the add publication button
        add_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                inflate_adder();
            }
        });




    }

    private void inflate_adder(){
        //setting up url adder layout
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View view2 = LayoutInflater.from(MainActivity.this).inflate(R.layout.url_adder, null);

        //inflating url adder layout
        builder.setView(view2);
        AlertDialog dialog = builder.show();

        //initializing value of edit text and btn
        Button url_adder_btn = (Button) view2.findViewById(R.id.adder_button);
        EditText url_adder_edtx = (EditText) view2.findViewById(R.id.url_adder_edit);

        url_adder_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast message to let the user know the rss feed was added
                Toast toast = Toast.makeText(MainActivity.this, "RSS feed added", Toast.LENGTH_LONG);
                toast.show();
                dialog.dismiss();

            }
        });
    }

    public class doIT extends AsyncTask<Void,Void,Void> {
        String text;
        Bitmap bitmap;
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            try{
                String urls = "https://www.sportsnet.ca/feed/";
                Document doc = Jsoup.connect(urls).get();
                //Element content = doc.select("img").first();
                //String imgsrc = content.absUrl("src");
                //InputStream input = new java.net.URL(imgsrc).openStream();
                //bitmap = BitmapFactory.decodeStream(input);
                URL url = new URL(urls);
                InputStream inputStream = url.openConnection().getInputStream();

                XmlPullParser xmlPullParser = Xml.newPullParser();
                xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,false);
                xmlPullParser.setInput(inputStream,null);

                //removes all elements from the current article array
                while(article_array.isEmpty() == false){
                    article_array.remove(0);
                }


                String content = "";
                int i = 0;
                int eventType = xmlPullParser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT){
                    String tagname = xmlPullParser.getName();
                    switch(eventType){
                        case XmlPullParser.START_TAG:
                            if (tagname.equalsIgnoreCase("title")){
                                i = 1;
                            }
                        break;
                        case XmlPullParser.TEXT:
                            if(i == 1){
                            content = xmlPullParser.getText();
                            article_array.add(content);

                            i = 0;}
                            break;
                        case XmlPullParser.END_TAG:
                            break;


                    }
                    eventType = xmlPullParser.next();
                    }


                XmlPullParser xml_pull = Xml.newPullParser();
                text = content;


            }catch(IOException | XmlPullParserException e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid){
            super.onPostExecute(aVoid);


            progressDialog.dismiss();
        }

    }
    public class CustomAdapter extends RecyclerView.Adapter<com.example.web_scrap.MainActivity.MyViewHolder> {
        ArrayList article_array;
        Context context;


        public CustomAdapter(Context context, ArrayList personNames) {
            this.context = context;
            this.article_array = personNames;
        }
        @Override
        public com.example.web_scrap.MainActivity.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // infalte the item Layout
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_row_layout, parent, false);
            // set the view's size, margins, paddings and layout parameters
            com.example.web_scrap.MainActivity.MyViewHolder vh = new com.example.web_scrap.MainActivity.MyViewHolder(v); // pass the view to View Holder
            return vh;
        }



        @Override
        public void onBindViewHolder(com.example.web_scrap.MainActivity.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.headline.setText((CharSequence) article_array.get(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send the article name to the view article activity to display it
                    Intent i = new Intent(MainActivity.this ,view_article.class);
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