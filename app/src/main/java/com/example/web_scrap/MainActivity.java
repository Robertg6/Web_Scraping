package com.example.web_scrap;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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
    Button button;
    ImageView image;
    FloatingActionButton btn;
    ImageButton btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text_view = findViewById(R.id.textview);
        image = findViewById(R.id.image);
        //btn = findViewById(R.id.floatingActionButton);
        btn2 = findViewById(R.id.imageButton);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new doIT().execute();
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