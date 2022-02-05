package com.example.web_scrap;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
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

public class MainActivity extends AppCompatActivity {

    TextView text_view;

    ImageView image;
    FloatingActionButton refresh_btn;
    FloatingActionButton list_btn;
    FloatingActionButton add_btn;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text_view = findViewById(R.id.textview);
        refresh_btn = findViewById(R.id.refresh);
        list_btn = findViewById(R.id.pub_list);
        add_btn = findViewById(R.id.add_pub);

        //On click listener for refresh button
        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new doIT().execute();
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
                            content = content + xmlPullParser.getText() + "\n";
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
            text_view.setText(text);

            progressDialog.dismiss();
        }

    }
}