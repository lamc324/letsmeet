package com.example.luism.letsmeet;

/**
 * Created by luism on 25/05/2018.
 */

public class Marcador {
    String email,latMarcador,lngMarcador;

    public Marcador(String email, String latMarcador, String lngMarcador) {
        this.email = email;
        this.latMarcador = latMarcador;
        this.lngMarcador = lngMarcador;
    }

    public Marcador() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLatMarcador() {
        return latMarcador;
    }

    public void setLatMarcador(String latMarcador) {
        this.latMarcador = latMarcador;
    }

    public String getLngMarcador() {
        return lngMarcador;
    }

    public void setLngMarcador(String lngMarcador) {
        this.lngMarcador = lngMarcador;
    }
}
