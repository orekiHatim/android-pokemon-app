package com.example.pokemonapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.VoiceInteractor;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.pokemonapp.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import my.classes.GridAdapter;
import my.classes.Pokemon;
import my.classes.Stats;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private GridView listView;
    private GridAdapter gridAdapter;

    private String nextUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String json = getData();
        ArrayList<Pokemon> pokemons = jsonToObjects(json);

        listView = findViewById(R.id.listview);
        gridAdapter = new GridAdapter(this, pokemons);

        binding.listview.setAdapter(gridAdapter);
        binding.listview.setClickable(true);

        //initializing the view
        binding.listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount >= totalItemCount) {
                    // The GridView has been scrolled to the bottom
                    // Your code here
                    String json = getNextData(nextUrl);
                    ArrayList<Pokemon> nextPokemons = jsonToObjects(json);
                    pokemons.addAll(nextPokemons);
                    changeGridAdapter(pokemons);
                    //Log.d("POKEMONS : ", pokemons.toString());
                }
            }
        });

        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, DetailedActivity.class);
                Pokemon pokemon = pokemons.get(i);
                intent.putExtra("id", "" + pokemon.getId());
                intent.putExtra("name", pokemon.getName());
                startActivity(intent);
            }
        });


    }

    private String getData(){
        ExecutorService executor = Executors.newSingleThreadExecutor();

        //String apiUrl = "https://da87e5a6-d3c3-457b-b5ee-05847d5ecb36.mock.pstmn.io/pokedex";
        String apiUrl = "https://pokeapi.co/api/v2/pokemon/";
        Request request = new Request.Builder().url(apiUrl).build();

        Callable<ResponseBody> apiCallable = new Callable<ResponseBody>() {
            @Override
            public ResponseBody call() throws Exception {
                OkHttpClient client = new OkHttpClient();
                Response response = client.newCall(request).execute();
                return response.body();
            }
        };

        Future<ResponseBody> future = executor.submit(apiCallable);

        try {
            ResponseBody responseBody = future.get();
            String responseString = responseBody.string();
            return responseString;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    private String getNextData(String next){
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Request request = new Request.Builder().url(next).build();

        Callable<ResponseBody> apiCallable = new Callable<ResponseBody>() {
            @Override
            public ResponseBody call() throws Exception {
                OkHttpClient client = new OkHttpClient();
                Response response = client.newCall(request).execute();
                return response.body();
            }
        };

        Future<ResponseBody> future = executor.submit(apiCallable);

        try {
            ResponseBody responseBody = future.get();
            String responseString = responseBody.string();
            return responseString;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private ArrayList<Pokemon> jsonToObjects(String json){

        try {
            JSONObject globalObject = new JSONObject(json);
            //JSONArray jsonArray = new JSONArray(globalObject.getString("pokemons"));
            nextUrl = globalObject.getString("next");
            JSONArray jsonArray = new JSONArray(globalObject.getString("results"));
            ArrayList<Pokemon> pokemons = new ArrayList<Pokemon>();


            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject object = jsonArray.getJSONObject(i);
                Pokemon pokemon = new Pokemon();
                pokemon.setName(object.getString("name"));
                String toExtractIdFrom = object.getString("url");
                String toExtractIdFrom_1 = toExtractIdFrom.substring(34);
                int id = Integer.parseInt(toExtractIdFrom_1.replace("/", ""));

                pokemon.setId(id);


                pokemons.add(pokemon);
            }
            return pokemons;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void changeGridAdapter(ArrayList<Pokemon> pokemonArrayList){
        gridAdapter = new GridAdapter(this, pokemonArrayList);
        listView.setAdapter(gridAdapter);
    }
}