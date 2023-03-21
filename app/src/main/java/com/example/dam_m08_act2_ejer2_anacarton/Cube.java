package com.example.dam_m08_act2_ejer2_anacarton;

import androidx.annotation.NonNull;

public class Cube {
    String currency;
    String rate;

    public Cube(String currency, String rate) {
        this.currency = currency;
        this.rate = rate;
    }

    public String getCurrency() {
        return currency;
    }

    public String getRate() {
        return rate;
    }

    //Sobreescribimos el método toString() para visualizar
    // la impresión del contenido de cada objeto de la clase como se quiere
    // (así se va a mostrar en el ListView)
    @NonNull
    @Override
    public String toString(){
        return "Currency: " + this.currency + " - Rate: " + this.rate;
    }
}
