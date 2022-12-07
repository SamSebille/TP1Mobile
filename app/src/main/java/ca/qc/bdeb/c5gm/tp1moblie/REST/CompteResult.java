package ca.qc.bdeb.c5gm.tp1moblie.REST;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CompteResult implements Serializable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("nom")
    @Expose
    private String nom;
    @SerializedName("prenom")
    @Expose
    private String prenom;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("type_compte")
    @Expose
    private ComptePOJO.TypeUtilisateur typeCompte;
    @SerializedName("access_token")
    @Expose
    private String accessToken;
    @SerializedName("expires_at")
    @Expose
    private String expiresAt;
    private final static long serialVersionUID = -4644635962262338195L;

    /**
     * No args constructor for use in serialization
     *
     */
    public CompteResult() {
    }

    /**
     *
     * @param typeCompte
     * @param id
     * @param accessToken
     * @param nom
     * @param prenom
     * @param email
     */
    public CompteResult(String id, String nom, String prenom, String email, ComptePOJO.TypeUtilisateur typeCompte, String accessToken) {
        super();
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.typeCompte = typeCompte;
        this.accessToken = accessToken;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ComptePOJO.TypeUtilisateur getTypeCompte() {
        return typeCompte;
    }

    public void setTypeCompte(ComptePOJO.TypeUtilisateur typeCompte) {
        this.typeCompte = typeCompte;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }

}