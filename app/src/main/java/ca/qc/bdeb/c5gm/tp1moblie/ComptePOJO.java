package ca.qc.bdeb.c5gm.tp1moblie;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.UUID;

public class ComptePOJO implements Serializable {
    @SerializedName("id")
    private UUID uuid;
    @SerializedName("nom")
    private String nom;
    @SerializedName("prenom")
    private String prenom;
    @SerializedName("email")
    private String email;

    @Override
    public String toString() {
        return  nom + " " +
                prenom + " : " +
                email;
    }
}
