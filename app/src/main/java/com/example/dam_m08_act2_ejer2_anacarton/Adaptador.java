package com.example.dam_m08_act2_ejer2_anacarton;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Adaptador extends ArrayAdapter<Cube> {
    //El adaptador propio extiende de un adaptador de tipo ArrayAdapter<Cube>


    public Adaptador(Context context, ArrayList<Cube> cubes) {

        super(context, 0, cubes);
    }

    private static class ViewHolder {
        TextView currency;
        TextView rate;
    }
    // El método getView se llama tantas veces como registros tengan los datos a visualizar (el arraylist, en este caso).
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // position: posición del array que estoy pintando.
        // getItem() es un método propio de ArrayAdapter
        // en este caso, el adaptador es de tipo "Cube", así que getItem() devolverá el objeto "Cube"
        // que está en la posición "position"

        Cube cube = getItem(position);

        // Se valida si se pasa por parámetro la View a visualizar
        //Si no, se usa la vista (el layout) que hemos creado para visualizar los elementos
        // el inflater se encarga de pintarlo
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_cube, parent, false);
        }
        // Se instancian e inicializan las variables de tipo TextView que apuntan a los TextView
        // del layout (item_user.xml)
        TextView textViewName = convertView.findViewById(R.id.textViewCurrency);
        TextView textViewHome = convertView.findViewById(R.id.textViewRate);

        // Se setean los textos de los TextViews
        textViewName.setText("Currency: " + cube.currency);
        textViewHome.setText("Ratio: " + cube.rate);


        // Defino una variable para poder saber el contexto
        View finalConvertView = convertView;


        // Se devuelve la vista para que se renderice por la pantalla
        return convertView;
    }
}

