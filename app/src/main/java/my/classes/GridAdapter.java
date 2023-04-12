package my.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pokemonapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

public class GridAdapter extends ArrayAdapter<Pokemon> {

    public GridAdapter(@NonNull Context context, ArrayList<Pokemon> pokemons) {
        super(context, R.layout.list_item, pokemons);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        Pokemon pokemon = getItem(position);
        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);

        }
        ImageView image = view.findViewById(R.id.listItemImage);
        TextView name = view.findViewById(R.id.listItemName);

        /*String imageName = pokemon.getName().toLowerCase(Locale.ROOT);
        int resourceId = view.getResources().getIdentifier(imageName, "drawable", view.getContext().getPackageName());
        image.setImageResource(resourceId);*/
        //https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/
        String imageName = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" + pokemon.getId() + ".png";
        Picasso.get().load(imageName).into(image);

        name.setText(pokemon.getName().toUpperCase());

        return view;
    }
}
