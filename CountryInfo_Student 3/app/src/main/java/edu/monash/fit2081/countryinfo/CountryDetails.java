package edu.monash.fit2081.countryinfo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.core.app.NavUtils;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Handler;
import android.os.Looper;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.net.ssl.HttpsURLConnection;

public class CountryDetails extends AppCompatActivity {

    private TextView name;
    private TextView capital;
    private TextView code;
    private TextView population;
    private TextView area;
    private TextView language;
    private TextView currency;
    private TextView region;
    private ImageView flag;
    private Button wiki;
    private String selectedCountry;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_details);

        getSupportActionBar().setTitle(R.string.title_activity_country_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        selectedCountry = getIntent().getStringExtra("country");

        name = findViewById(R.id.country_name);
        capital = findViewById(R.id.capital);
        code = findViewById(R.id.country_code);
        population = findViewById(R.id.population);
        area = findViewById(R.id.area);
        currency = findViewById(R.id.currency);
        language = findViewById(R.id.language);
        region = findViewById(R.id.region);
        flag = findViewById(R.id.flag);
        wiki = findViewById(R.id.wiki);
        wiki.setText("WIKI " + selectedCountry);


        ExecutorService executor = Executors.newSingleThreadExecutor();
        ExecutorService executorImage = Executors.newSingleThreadExecutor();

        //Executor handler = ContextCompat.getMainExecutor(this);
        Handler uiHandler=new Handler(Looper.getMainLooper());



        executor.execute(() -> {
            //Background work here
            CountryInfo countryInfo = new CountryInfo();

            try {
                // Create URL
                URL webServiceEndPoint = new URL("https://restcountries.com/v2/name/" + selectedCountry); //

                // Create connection
                HttpsURLConnection myConnection = (HttpsURLConnection) webServiceEndPoint.openConnection();

                if (myConnection.getResponseCode() == 200) {
                    //JSON data has arrived successfully, now we need to open a stream to it and get a reader
                    InputStream responseBody = myConnection.getInputStream();
                    InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");

                    //now use a JSON parser to decode data
                    JsonReader jsonReader = new JsonReader(responseBodyReader);
                    jsonReader.beginArray(); //consume arrays's opening JSON brace
                    String keyName;
                    // countryInfo = new CountryInfo(); //nested class (see below) to carry Country Data around in
                    boolean countryFound = false;
                    while (jsonReader.hasNext() && !countryFound) { //process array of objects
                        jsonReader.beginObject(); //consume object's opening JSON brace
                        while (jsonReader.hasNext()) {// process key/value pairs inside the current object
                            keyName = jsonReader.nextName();
                            if (keyName.equals("name")) {
                                countryInfo.setName(jsonReader.nextString());
                                if (countryInfo.getName().equalsIgnoreCase(selectedCountry)) {
                                    countryFound = true;
                                }
                            } else if (keyName.equals("alpha3Code")) {
                                countryInfo.setAlpha3Code(jsonReader.nextString());
                            } else if (keyName.equals("capital")) {
                                countryInfo.setCapital(jsonReader.nextString());
                            } else if (keyName.equals("population")) {
                                countryInfo.setPopulation(jsonReader.nextInt());
                            } else if (keyName.equals("area")) {
                                countryInfo.setArea(jsonReader.nextDouble());
                            } else if (keyName.equals("currencies")) {
                                String currencyKey;
                                jsonReader.beginArray();
                                while (jsonReader.hasNext()) {
                                    jsonReader.beginObject();
                                    while (jsonReader.hasNext()) {
                                        currencyKey = jsonReader.nextName();
                                        if (currencyKey.equals("name")) {
                                            countryInfo.addCurrency(jsonReader.nextString());
                                        } else {
                                            jsonReader.skipValue();
                                        }
                                    }
                                    jsonReader.endObject();
                                }
                                jsonReader.endArray();
                            } else if (keyName.equals("languages")) {
                                String languageKey;
                                jsonReader.beginArray();
                                while (jsonReader.hasNext()) {
                                    jsonReader.beginObject();
                                    while (jsonReader.hasNext()) {
                                        languageKey = jsonReader.nextName();
                                        if (languageKey.equals("name")) {
                                            countryInfo.addLanguage(jsonReader.nextString());
                                        } else {
                                            jsonReader.skipValue();
                                        }
                                    }
                                    jsonReader.endObject();
                                }
                                jsonReader.endArray();
                            } else if (keyName.equals("region")) {
                                countryInfo.setRegion(jsonReader.nextString());
                            } else if (keyName.equals("alpha2Code")) {
                                String alpha2code = jsonReader.nextString().toLowerCase();
                                String request = "https://flagcdn.com/144x108/" + alpha2code + ".png";

                                executorImage.execute(() -> {
                                    // this code will run in the background
                                    try {
                                        java.net.URL url = new java.net.URL(request);
                                        HttpURLConnection connection = (HttpURLConnection) url
                                                .openConnection();
                                        connection.setDoInput(true);
                                        connection.connect();
                                        InputStream input = connection.getInputStream();

                                        Bitmap myBitmap = BitmapFactory.decodeStream(input);

                                        // now lets update the UI
                                        uiHandler.post(() -> {
                                            flag.setImageBitmap(myBitmap);
                                        });
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                });

                            } else {
                                jsonReader.skipValue();
                            }
                        }
                        jsonReader.endObject();
                    }
                    jsonReader.endArray();
                    uiHandler.post(()->{
                        name.setText(countryInfo.getName());
                        capital.setText(countryInfo.getCapital());
                        code.setText(countryInfo.getAlpha3Code());
                        population.setText(Integer.toString(countryInfo.getPopulation()));
                        area.setText(Double.toString(countryInfo.getArea()));
                        currency.setText(countryInfo.getCurrency().get(0));
                        for (int i=1; i < countryInfo.getCurrency().size(); i++) {
                            currency.setText(currency.getText().toString() + ", " + countryInfo.getCurrency().get(i));
                        }

                        language.setText(countryInfo.getLanguage().get(0));
                        for (int i=1; i < countryInfo.getLanguage().size(); i++) {
                            language.setText(language.getText().toString() + ", " + countryInfo.getLanguage().get(i));
                        }

//                        currency.setText(countryInfo.getCurrency().get(0));
//                        int i = 1;
//                        while (i < countryInfo.getCurrency().size()){
//                            currency.setText(currency.getText().toString() + ", " + countryInfo.getCurrency().get(i));
//                            i++;
//                        }
//
//                        language.setText(countryInfo.getLanguage().get(0));
//                        i = 1;
//                        while (i < countryInfo.getLanguage().size()){
//                            language.setText(language.getText().toString() + ", " + countryInfo.getLanguage().get(i));
//                            i++;
//                        }
                        region.setText(countryInfo.getRegion());
                    });


                } else {
                    Log.i("INFO", "Error:  No response");
                }

                // All your networking logic should be here
            } catch (Exception e) {
                Log.i("INFO", "Error " + e.toString());
            }

        });


    }

    public void openWiki(View v) {
        Intent intent = new Intent(this, WebWiki.class);
        intent.putExtra("country", selectedCountry);
        startActivity(intent);
    }

    private class CountryInfo {
        private String name;
        private String alpha3Code;
        private String capital;
        private int population;
        private double area;
        private ArrayList<String> language = new ArrayList<>();
        private ArrayList<String> currency = new ArrayList<>();
        private String region;

        public ArrayList<String> getLanguage() {
            return language;
        }

        public void addLanguage(String language) {
            this.language.add(language);
        }

        public ArrayList<String> getCurrency() {
            return currency;
        }

        public void addCurrency(String currency) {
            this.currency.add(currency);
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAlpha3Code() {
            return alpha3Code;
        }

        public void setAlpha3Code(String alpha3Code) {
            this.alpha3Code = alpha3Code;
        }

        public String getCapital() {
            return capital;
        }

        public void setCapital(String capital) {
            this.capital = capital;
        }

        public int getPopulation() {
            return population;
        }

        public void setPopulation(int population) {
            this.population = population;
        }

        public double getArea() {
            return area;
        }

        public void setArea(double area) {
            this.area = area;
        }
    }
}
