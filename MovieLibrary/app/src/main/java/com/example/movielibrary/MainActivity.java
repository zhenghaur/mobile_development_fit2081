package com.example.movielibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.movielibrary.provider.Movie;
import com.example.movielibrary.provider.MovieViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Locale;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {
    DrawerLayout myDrawer;
    ArrayList<String> movieList = new ArrayList<String>();
    ArrayAdapter myAdapter;
//    ArrayList<Movie> movieListRecycle = new ArrayList<>();

    private MovieViewModel mMovieViewModel;
    MyRecyclerViewAdapter adapter;

    DatabaseReference myRef;

    View frameLayout;
    int x_down;
    int y_down;

    BroadcastReceiver myReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent){
            StringTokenizer token = new StringTokenizer(intent.getStringExtra("key1"), ";");

            EditText title = findViewById(R.id.txtTitleField);
            title.setText(token.nextToken());

            EditText year = findViewById(R.id.txtYearField);
            year.setText(token.nextToken());

            EditText country = findViewById(R.id.txtCountryField);
            country.setText(token.nextToken());

            EditText genre = findViewById(R.id.txtGenreField);
            genre.setText(token.nextToken());

            EditText cost = findViewById(R.id.txtCostField);
            int int1 = Integer.parseInt(token.nextToken().toString());

            EditText keywords = findViewById(R.id.txtKeywordField);
            keywords.setText(token.nextToken());

            int int2 = Integer.parseInt(token.nextToken().toString());
            int1 = int1 + int2;
            cost.setText(Integer.toString(int1));
        }
    };

    class MyNavigationListener implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.addMovie){
                EditText titleField = findViewById(R.id.txtTitleField);
                String title = titleField.getText().toString();
                EditText yearField = findViewById(R.id.txtYearField);
                int year = Integer.parseInt(yearField.getText().toString());
                EditText countryField = findViewById(R.id.txtCountryField);
                String country = countryField.getText().toString();
                EditText genreField = findViewById(R.id.txtGenreField);
                String genre = genreField.getText().toString();
                EditText costField = findViewById(R.id.txtCostField);
                int cost = Integer.parseInt(costField.getText().toString());
                EditText keywordField = findViewById(R.id.txtKeywordField);
                String keyword = keywordField.getText().toString();

                movieList.add(title + " | " + year);
                myAdapter.notifyDataSetChanged();

                //week 6

                Movie movie = new Movie(title, year, country, genre, cost, keyword);
                mMovieViewModel.insert(movie);

                myRef.push().setValue(movie);

            } else if (id == R.id.removeLast){
//                movieList.remove(movieList.size() - 1);
//                myAdapter.notifyDataSetChanged();
//
//                //week 6
//                movieListRecycle.remove(movieListRecycle.size() - 1);
            } else if (id == R.id.removeAll){
                movieList.removeAll(movieList);
                myAdapter.notifyDataSetChanged();

                myRef.removeValue();

                //week 6
                mMovieViewModel.deleteAll();
            } else if (id == R.id.closeApp){
                finish();
            } else if (id == R.id.allMovie){
//                Gson gson = new Gson();
//                String dbStr = gson.toJson(movieListRecycle);
//                SharedPreferences sp = getSharedPreferences("db1", 0);
//                SharedPreferences.Editor edit = sp.edit();
//                edit.putString("MOV_LIST", dbStr);
//                edit.apply();

                Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                startActivity(intent);
            }
            myDrawer.closeDrawers();
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("lab3", "onCreate");
        setContentView(R.layout.main_drawer);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 0);
        IntentFilter intentFilter = new IntentFilter("MySMS");
        registerReceiver(myReceiver, intentFilter);

        Toolbar myToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);

        myDrawer = findViewById(R.id.mainDrawer);
        ActionBarDrawerToggle myToggle = new ActionBarDrawerToggle(this, myDrawer, myToolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        myDrawer.addDrawerListener(myToggle);
        myToggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new MyNavigationListener());

        ListView listView = findViewById(R.id.movieList);
        myAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, movieList);
        listView.setAdapter(myAdapter);

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                EditText titleField = findViewById(R.id.txtTitleField);
//                String title = titleField.getText().toString();
                EditText yearField = findViewById(R.id.txtYearField);
//                int year = Integer.parseInt(yearField.getText().toString());
                EditText countryField = findViewById(R.id.txtCountryField);
//                String country = countryField.getText().toString();
                EditText genreField = findViewById(R.id.txtGenreField);
//                String genre = genreField.getText().toString();
                EditText costField = findViewById(R.id.txtCostField);
//                int cost = Integer.parseInt(costField.getText().toString());
                EditText keywordField = findViewById(R.id.txtKeywordField);
//                String keyword = keywordField.getText().toString();

                titleField.setText("titanic");
                yearField.setText("2020");
                countryField.setText("usa");
                genreField.setText("romance");
                costField.setText("1000");
                keywordField.setText("ship");

//                movieList.add(title + " | " + year);
//                myAdapter.notifyDataSetChanged();
//
//                //week 6
//                Movie movie = new Movie(title, year, country, genre, cost, keyword);
//                movieListRecycle.add(movie);

            }
        });

        adapter = new MyRecyclerViewAdapter();

        mMovieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        mMovieViewModel.getAllMovies().observe(this, newData -> {
            adapter.setMovie(newData);
            adapter.notifyDataSetChanged();
        });

        FirebaseDatabase database =  FirebaseDatabase.getInstance();
        myRef = database.getReference("movies");

        frameLayout = findViewById(R.id.frame_layout);
        frameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getActionMasked();

                switch(action) {
                    case MotionEvent.ACTION_DOWN:
                        x_down = (int) motionEvent.getX();
                        y_down = (int) motionEvent.getY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        if (Math.abs(y_down - motionEvent.getY()) < 40) {
                            if (x_down-motionEvent.getX() < -50) {
                                Toast.makeText(getApplicationContext(), "left to right swipe detected", Toast.LENGTH_SHORT).show();
                                addMovie(view);
                            } else if (x_down-motionEvent.getX() > 50){
                                Toast.makeText(getApplicationContext(), "right to left swipe detected", Toast.LENGTH_SHORT).show();
                            }
                        } else if (Math.abs(x_down - motionEvent.getX()) < 40) {
                            if (y_down - motionEvent.getY() < -50) {
                                Toast.makeText(getApplicationContext(), "top to bottom swipe detected", Toast.LENGTH_SHORT).show();
                                reset(view);
                            } else if (y_down - motionEvent.getY() > 50){
                                Toast.makeText(getApplicationContext(), "bottom to top swipe detected", Toast.LENGTH_SHORT).show();
                            }
                        }
                        EditText costField = findViewById(R.id.txtCostField);
                        if (Math.abs(x_down - motionEvent.getX()) < 50 && Math.abs(y_down - motionEvent.getY()) < 50) {
                            if (motionEvent.getX() > 1000 && motionEvent.getY() < 150) {
                                int cost = Integer.parseInt(costField.getText().toString());
                                cost += 50;
                                costField.setText(Integer.toString(cost));
                                Toast.makeText(getApplicationContext(), "cost +50", Toast.LENGTH_SHORT).show();
                            } else if (motionEvent.getX() < 400 && motionEvent.getY() < 150) {
                                int cost = Integer.parseInt(costField.getText().toString());
                                cost -= 50;
                                if (cost > 0) {
                                    costField.setText(Integer.toString(cost));
                                    Toast.makeText(getApplicationContext(), "cost -50", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    costField.setText("0");
                                    Toast.makeText(getApplicationContext(), "cost minimum is 0", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.clearField) {
            EditText titleField = findViewById(R.id.txtTitleField);
            titleField.setText("");
            EditText yearField = findViewById(R.id.txtYearField);
            yearField.setText("");
            EditText countryField = findViewById(R.id.txtCountryField);
            countryField.setText("");
            EditText genreField = findViewById(R.id.txtGenreField);
            genreField.setText("");
            EditText costField = findViewById(R.id.txtCostField);
            costField.setText("");
            EditText keywordField = findViewById(R.id.txtKeywordField);
            keywordField.setText("");
        } else if (id == R.id.totalMovie){
            Toast message = Toast.makeText(this, Integer.toString(movieList.size()) + " movies in total", Toast.LENGTH_SHORT);
            message.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart(){
        super.onStart();

        // week 3 task 2
        SharedPreferences myData = getSharedPreferences("f1", 0);
        EditText titleField = findViewById(R.id.txtTitleField);
        titleField.setText(myData.getString("title", ""));
        EditText yearField = findViewById(R.id.txtYearField);
        yearField.setText(myData.getString("year", ""));
        EditText countryField = findViewById(R.id.txtCountryField);
        countryField.setText(myData.getString("country", ""));
        EditText genreField = findViewById(R.id.txtGenreField);
        genreField.setText(myData.getString("genre", ""));
        EditText costField = findViewById(R.id.txtCostField);
        costField.setText(myData.getString("cost", ""));
        EditText keywordField = findViewById(R.id.txtKeywordField);
        keywordField.setText(myData.getString("keyword", ""));


        Log.i("lab3", "onStart");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.i("lab3", "onPause");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.i("lab3", "onResume");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.i("lab3", "onStop");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.i("lab3", "onDestroy");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outstate){
        super.onSaveInstanceState(outstate);

        // week 3  task 1
        EditText genreField = findViewById(R.id.txtGenreField);
        outstate.putString("genre", genreField.getText().toString().toLowerCase(Locale.ROOT));
        Log.i("lab3", "onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        savedInstanceState.getString("key");

        // week 3 task 1
        EditText genreField = findViewById(R.id.txtGenreField);
        genreField.setText(savedInstanceState.getString("genre", ""));
        EditText titleField = findViewById(R.id.txtTitleField);
        titleField.setText(titleField.getText().toString().toUpperCase());

        Log.i("lab3", "onRestoreInstanceState");
    }

    // week 3 extra task
    public void clearPreference(View view){
        SharedPreferences myData = getSharedPreferences("f1", 0);
        SharedPreferences.Editor myEditor = myData.edit();
        myEditor.clear();
        myEditor.commit();
    }

    // week 3 extra task
    public void loadTwoTimes(View view){
        SharedPreferences myData = getSharedPreferences("f1", 0);
        SharedPreferences.Editor myEditor = myData.edit();
        String costString = myData.getString("cost", "");
        if (costString != ""){
            int newCost = Integer.parseInt(costString);
            newCost = newCost * 2;
            myEditor.putString("cost", Integer.toString(newCost));
            myEditor.commit();
            EditText costField = findViewById(R.id.txtCostField);
            costField.setText(Integer.toString(newCost));
        }
        else{
            Toast message = Toast.makeText(this, "No cost", Toast.LENGTH_SHORT);
            message.show();
        }

    }

    // week 2
    public void addMovie(View view){
        EditText titleField = findViewById(R.id.txtTitleField);
        if (titleField.getText().length() > 0) {
            Toast message = Toast.makeText(this, "Movie " + titleField.getText() + " added", Toast.LENGTH_SHORT);
            message.show();
            // week 3 task2
            SharedPreferences myData = getSharedPreferences("f1", 0);
            SharedPreferences.Editor myEditor = myData.edit();

            String title = titleField.getText().toString();
            EditText yearField = findViewById(R.id.txtYearField);
            int year = Integer.parseInt(yearField.getText().toString());
            EditText countryField = findViewById(R.id.txtCountryField);
            String country = countryField.getText().toString();
            EditText genreField = findViewById(R.id.txtGenreField);
            String genre = genreField.getText().toString();
            EditText costField = findViewById(R.id.txtCostField);
            int cost = Integer.parseInt(costField.getText().toString());
            EditText keywordField = findViewById(R.id.txtKeywordField);
            String keyword = keywordField.getText().toString();

//        myEditor.putString("title", title);
//        myEditor.putString("year", Integer.toString(year));
//        myEditor.putString("country", country);
//        myEditor.putString("genre", genre);
//        myEditor.putString("cost", Integer.toString(cost));
//        myEditor.putString("keyword", keyword);
//        myEditor.commit();

            // week 5
            movieList.add(titleField.getText().toString() + " | " + yearField.getText().toString());
            myAdapter.notifyDataSetChanged();

            //week 6
            Movie movie = new Movie(title, year, country, genre, cost, keyword);
            mMovieViewModel.insert(movie);

            myRef.push().setValue(movie);
        }
        else{
            Toast.makeText(this, "please input movie", Toast.LENGTH_SHORT).show();
        }
    }

    // week 2
    public void addCost(View view){
        EditText costField = findViewById(R.id.txtCostField);
        int finalCost = Integer.parseInt(costField.getText().toString());
        finalCost = finalCost * 2;
        costField.setText(Integer.toString(finalCost));
    }

    // week 2
    public void reset(View view){
        Toast.makeText(this, "field cleared", Toast.LENGTH_SHORT).show();
        EditText titleField = findViewById(R.id.txtTitleField);
        titleField.setText("");
        EditText yearField = findViewById(R.id.txtYearField);
        yearField.setText("");
        EditText countryField = findViewById(R.id.txtCountryField);
        countryField.setText("");
        EditText genreField = findViewById(R.id.txtGenreField);
        genreField.setText("");
        EditText costField = findViewById(R.id.txtCostField);
        costField.setText("");
        EditText keywordField = findViewById(R.id.txtKeywordField);
        keywordField.setText("");

        SharedPreferences myData = getSharedPreferences("f1", 0);
        SharedPreferences.Editor myEditor = myData.edit();
        myEditor.clear();
        myEditor.commit();
    }

}