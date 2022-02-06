package com.example.web_scrap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.util.Xml;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class view_article extends AppCompatActivity {

    String title;
    TextView article_title;
    TextView body_article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_article);

        Bundle b = this.getIntent().getExtras();
        title = b.getString("Article_title");


        article_title = findViewById(R.id.article_title);
        article_title.setText(title);
        body_article = findViewById(R.id.article_body);

        AsyncTask<Void, Void, Void> async = new doThis().execute();

    }


    public class doThis extends AsyncTask<Void,Void,Void> {
        String text;
        Bitmap bitmap;
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(view_article.this);
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
                            if (tagname.equalsIgnoreCase("content:encoded") && i == 2){
                                i = 3;
                            }


                            break;
                        case XmlPullParser.TEXT:
                            if(i == 1){

                                if(xmlPullParser.getText().equalsIgnoreCase(title)){

                                    i = 2;
                                }
                                else{
                                    i = 0;
                                }}
                                if(i == 3){
                                    //Log.i("NumberGenerated", "testing");
                                    content = xmlPullParser.getText();
                                    Log.i("NumberGenerated", content);
                                    i = 0;
                                }

                            break;
                        case XmlPullParser.END_TAG:
                            break;


                    }
                    eventType = xmlPullParser.next();
                }


                XmlPullParser xml_pull = Xml.newPullParser();
                body_article.setText(HtmlCompat.fromHtml(content, 0));
                //Linkify.addLinks(body_article, Linkify.ALL);
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




}
