package com.example.pokemonapp;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import my.classes.Pokemon;
import my.classes.Stats;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class DetailedActivity extends AppCompatActivity {

    private TextView idTxt, nameTxt, weightTxt;
    private Button goBackBtn;
    private ImageView img;

    private ProgressBar hpPb, atkPb, defPb, spdPb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);


        getSupportActionBar().hide();

        goBackBtn = findViewById(R.id.detailedGoBackBtn);
        Intent intent = this.getIntent();
        if(intent != null){
            //Getting passed to intent values
            String id = intent.getStringExtra("id");
            String name = intent.getStringExtra("name");
            /*String weight = intent.getStringExtra("weight");
            int hp = Integer.parseInt(intent.getStringExtra("hp"));
            int atk = Integer.parseInt(intent.getStringExtra("atk"));
            int def = Integer.parseInt(intent.getStringExtra("def"));
            int spd = Integer.parseInt(intent.getStringExtra("spd"));*/
            String data = getData(Integer.parseInt(id));
            Pokemon pokemon = jsonToObject(data);



            //Setting id
            idTxt = findViewById(R.id.detailedIdTxt);
            //idTxt.setText("#00" + id);
            idTxt.setText("#0" + pokemon.getId());

            //Setting img
            img = findViewById(R.id.detailedImg);
            /*String imageName = name.toLowerCase(Locale.ROOT);
            int resID = getResources().getIdentifier(imageName, "drawable", getPackageName());
            img.setImageResource(resID);*/
            String imageName = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" + pokemon.getId() + ".png";
            Picasso.get().load(imageName).into(img);

            //Setting Name
            nameTxt = findViewById(R.id.detailedNameTxt);
            //nameTxt.setText(name);
            nameTxt.setText(name.toUpperCase());

            //Setting weight
            weightTxt = findViewById(R.id.detailedWeightTxt);
            //weightTxt.setText(weight + " KG");
            weightTxt.setText(pokemon.getWeight() + " KG");

            //Setting stats(hp)
            hpPb = findViewById(R.id.detailedHpPB);
            //float hpVal = 100 * hp / 15;
            float hpVal = 100 * pokemon.getStats().getHp() / 325;
            ObjectAnimator animationHp = ObjectAnimator.ofInt(hpPb, "progress", 0, (int) hpVal);
            animationHp.setDuration(2000);
            animationHp.setInterpolator(new AccelerateDecelerateInterpolator());
            animationHp.start();

            //Setting stats(atk)
            atkPb = findViewById(R.id.detailedAtkPB);
            //float atkVal = 100 * atk / 15;
            float atkVal = 100 * pokemon.getStats().getAttack() / 325;
            ObjectAnimator animationAtk = ObjectAnimator.ofInt(atkPb, "progress", 0, (int) atkVal);
            animationAtk.setDuration(2000);
            animationAtk.setInterpolator(new AccelerateDecelerateInterpolator());
            animationAtk.start();

            //Setting stats(def)
            defPb = findViewById(R.id.detailedDefPB);
            //float defVal = 100 * def / 15;
            float defVal = 100 * pokemon.getStats().getDefense() / 325;
            ObjectAnimator animationDef = ObjectAnimator.ofInt(defPb, "progress", 0, (int) defVal);
            animationDef.setDuration(2000);
            animationDef.setInterpolator(new AccelerateDecelerateInterpolator());
            animationDef.start();

            //Setting stats(spd)
            spdPb = findViewById(R.id.detailedSpeedPB);
            //float spdVal = 100 * spd / 15;
            float spdVal = 100 * pokemon.getStats().getSeed() / 325;
            ObjectAnimator animationSpd = ObjectAnimator.ofInt(spdPb, "progress", 0, (int) spdVal);
            animationSpd.setDuration(2000);
            animationSpd.setInterpolator(new AccelerateDecelerateInterpolator());
            animationSpd.start();

        }

        //Back button behavior
        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(DetailedActivity.this, MainActivity.class);
                startActivity(intent1);
            }
        });

    }

    private String getData(int id){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        String apiUrl = "https://pokeapi.co/api/v2/pokemon/" + id;

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
        try{
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

    private Pokemon jsonToObject(String json) {
        try{
            JSONObject globalObject = new JSONObject(json);
            JSONArray stats = globalObject.getJSONArray("stats");
            Pokemon pokemon = new Pokemon();
            Stats statsOfPokemon = new Stats();

            pokemon.setId(globalObject.getInt("id"));
            pokemon.setWeight(globalObject.getDouble("weight"));



            JSONObject hp = stats.getJSONObject(0);
            //Log.d("MSG", hp.toString());
            statsOfPokemon.setHp(hp.getInt("base_stat"));

            JSONObject atk = stats.getJSONObject(1);
            statsOfPokemon.setAttack(atk.getInt("base_stat"));

            JSONObject def = stats.getJSONObject(2);
            statsOfPokemon.setDefense(def.getInt("base_stat"));

            JSONObject spd = stats.getJSONObject(5);
            statsOfPokemon.setSeed(spd.getInt("base_stat"));

            pokemon.setStats(statsOfPokemon);
            return pokemon;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }
}