package ca.qc.bdeb.c5gm.tp1moblie.BD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.UUID;

import ca.qc.bdeb.c5gm.tp1moblie.Activities.Entreprise;
import ca.qc.bdeb.c5gm.tp1moblie.Activities.Etudiant;

/**
 * Classe qui implemente SQLiteOpenHelper afin de manipuler la bd locale.
 */
public class Stockage extends SQLiteOpenHelper {

    private static final String DB_NAME = "TP1_entreprises.db"; // Nom du fichier de BD
    public static final int DB_VERSION = 1; // Numéro actuel de version de BD
    private static Stockage instance = null; //L’unique instance de DbHelper possible

    public static Stockage getInstance(Context context) {
        if (instance == null) {
            instance = new Stockage(context.getApplicationContext());
        }
        return instance;
    }

    private Stockage(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Contient la création des tables
        sqLiteDatabase.execSQL(SQL_CREATE_ENTREPRISES);
        sqLiteDatabase.execSQL(SQL_CREATE_ETUDIANT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // mise à jour de la structure des tables.
    }

    public ArrayList<Entreprise> getEntreprises() {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {
                Entreprises._ID,
                Entreprises.ENTREPRISE_NOM,
                Entreprises.ENTREPRISE_CONTACT,
                Entreprises.ENTREPRISE_COURRIEL,
                Entreprises.ENTREPRISE_TELEPHONE,
                Entreprises.ENTREPRISE_WEB,
                Entreprises.ENTREPRISE_ADRESSE,
                Entreprises.ENTREPRISE_DATE,
                Entreprises.ENTREPRISE_FAVORI
        };

        String selection = Entreprises._ID + " = ?";
        Cursor cursor = db.query(Entreprises.NOM_TABLE, columns, null, null,
                null, null, Entreprises.ENTREPRISE_NOM, null);

        ArrayList<Entreprise> entreprises = new ArrayList<>();

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                entreprises.add(new Entreprise(
                        UUID.fromString(cursor.getString(0)), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3),
                        cursor.getString(4), cursor.getString(5),
                        cursor.getString(6), cursor.getString(7),
                        cursor.getInt(8) == 1));
            } while (cursor.moveToNext());
            cursor.close();
        }

        return entreprises;
    }

    public Entreprise getEntreprise(UUID id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {
                Entreprises._ID,
                Entreprises.ENTREPRISE_NOM,
                Entreprises.ENTREPRISE_CONTACT,
                Entreprises.ENTREPRISE_COURRIEL,
                Entreprises.ENTREPRISE_TELEPHONE,
                Entreprises.ENTREPRISE_WEB,
                Entreprises.ENTREPRISE_ADRESSE,
                Entreprises.ENTREPRISE_DATE,
                Entreprises.ENTREPRISE_FAVORI
        };

        String selection = Entreprises._ID + " = ?";

        String[] selectionArgs = {id.toString()};

        Cursor cursor = db.query(Entreprises.NOM_TABLE, columns, selection, selectionArgs,
                null, null, null, null);

        Entreprise entreprise = null;

        if (cursor != null) {
            cursor.moveToFirst();

            entreprise = new Entreprise(
                    UUID.fromString(cursor.getString(0)), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3),
                    cursor.getString(4), cursor.getString(5),
                    cursor.getString(6), cursor.getString(7),
                    cursor.getInt(8) == 1);
        }

        cursor.close();

        return entreprise;
    }

    public boolean updateEntreprise(Entreprise entreprise) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Entreprises.ENTREPRISE_NOM, entreprise.getNom());
        values.put(Entreprises.ENTREPRISE_CONTACT, entreprise.getContact());
        values.put(Entreprises.ENTREPRISE_COURRIEL, entreprise.getEmail());
        values.put(Entreprises.ENTREPRISE_TELEPHONE, entreprise.getTelephone());
        values.put(Entreprises.ENTREPRISE_WEB, entreprise.getSiteWeb());
        values.put(Entreprises.ENTREPRISE_ADRESSE, entreprise.getAdresse());
        values.put(Entreprises.ENTREPRISE_DATE, entreprise.getDateContact());
        values.put(Entreprises.ENTREPRISE_FAVORI, entreprise.estFavorite());

        String whereClause = Entreprises._ID + " = ?";
        String[] whereArgs = {String.valueOf(entreprise.getId())};

        // MAJ de l’enregistrement
        int nbMAJ = db.update(Entreprises.NOM_TABLE, values, whereClause, whereArgs);

        return (nbMAJ > 0); // True si update, false sinon
    }

    public void ajouterEntreprise(Entreprise entreprise) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Entreprises._ID, entreprise.getId().toString());
        values.put(Entreprises.ENTREPRISE_NOM, entreprise.getNom());
        values.put(Entreprises.ENTREPRISE_CONTACT, entreprise.getContact());
        values.put(Entreprises.ENTREPRISE_COURRIEL, entreprise.getEmail());
        values.put(Entreprises.ENTREPRISE_TELEPHONE, entreprise.getTelephone());
        values.put(Entreprises.ENTREPRISE_WEB, entreprise.getSiteWeb());
        values.put(Entreprises.ENTREPRISE_ADRESSE, entreprise.getAdresse());
        values.put(Entreprises.ENTREPRISE_DATE, entreprise.getDateContact());
        values.put(Entreprises.ENTREPRISE_FAVORI, entreprise.estFavorite());

        db.insert(Entreprises.NOM_TABLE, null, values);
    }

    public void ajouterEtudiant(Etudiant etudiant) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Etudiants._ID, etudiant.getId().toString());
        values.put(Etudiants.ETUDIANT_NOM, etudiant.getNom());
        values.put(Etudiants.ETUDIANT_PRENOM, etudiant.getPrenom());
        values.put(Etudiants.ETUDIANT_COURRIEL, etudiant.getEmail());
        values.put(Etudiants.ETUDIANT_STAGE, etudiant.isStageTrouve());
        values.put(Etudiants.ETUDIANT_NOMBRE_ENTREPRISE, etudiant.getNombreEntreprise());

        db.insert(Etudiants.NOM_TABLE, null, values);
    }

    public void deleteEntreprise(Entreprise entreprise) {
        SQLiteDatabase db = this.getWritableDatabase(); // On veut écrire dans la BD
        String whereClause = Entreprises._ID + " = ?";
        String[] whereArgs = {String.valueOf(entreprise.getId())};
        db.delete(Entreprises.NOM_TABLE, whereClause, whereArgs);
    }

    public void deleteEtudiant(Etudiant etudiant) {
        SQLiteDatabase db = this.getWritableDatabase(); // On veut écrire dans la BD
        String whereClause = Etudiants._ID + " = ?";
        String[] whereArgs = {String.valueOf(etudiant.getId())};
        db.delete(Etudiants.NOM_TABLE, whereClause, whereArgs);
    }

    public void dropTables() {
        SQLiteDatabase db = this.getWritableDatabase(); // On veut écrire dans la BD
        db.execSQL(SQL_DELETE_ENTREPRISE);
        db.execSQL(SQL_DELETE_ETUDIENT);
    }

    public void clearTables(){
        ArrayList<Entreprise> entreprises = getEntreprises();
        ArrayList<Etudiant> etudiants = getEtudiants();

        for (Entreprise entreprise : entreprises) {
            deleteEntreprise(entreprise);
        }
        for (Etudiant etudiant : etudiants) {
            deleteEtudiant(etudiant);
        }
    }

    public Etudiant getEtudiant(UUID id ) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {
                Etudiants._ID,
                Etudiants.ETUDIANT_NOM,
                Etudiants.ETUDIANT_PRENOM,
                Etudiants.ETUDIANT_COURRIEL,
                Etudiants.ETUDIANT_STAGE,
                Etudiants.ETUDIANT_NOMBRE_ENTREPRISE
        };

        String selection = Etudiants._ID + " = ?";

        String[] selectionArgs = {id.toString()};

        Cursor cursor = db.query(Etudiants.NOM_TABLE, columns, selection, selectionArgs,
                null, null, null, null);

        Etudiant etudiant = null;

        if (cursor != null) {
            cursor.moveToFirst();

            etudiant = new Etudiant(
                    UUID.fromString(cursor.getString(0)), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3),
                    cursor.getInt(4)==1, cursor.getInt(5));
        }

        cursor.close();

        return etudiant;
    }

    public ArrayList<Etudiant> getEtudiants() {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {
                Etudiants._ID,
                Etudiants.ETUDIANT_NOM,
                Etudiants.ETUDIANT_PRENOM,
                Etudiants.ETUDIANT_COURRIEL,
                Etudiants.ETUDIANT_STAGE,
                Etudiants.ETUDIANT_NOMBRE_ENTREPRISE
        };


        Cursor cursor = db.query(Etudiants.NOM_TABLE, columns, null, null,
                null, null, null, null);

        ArrayList<Etudiant> etudiant = new ArrayList<>();


        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
        do{
            etudiant.add( new Etudiant(
                    UUID.fromString(cursor.getString(0)), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3),
                    cursor.getInt(4)==1, cursor.getInt(5)));
        } while (cursor.moveToNext());

    }

        cursor.close();

        return etudiant;
    }

    private static final String SQL_CREATE_ENTREPRISES =
            "CREATE TABLE " + Entreprises.NOM_TABLE + " (" +
                    Entreprises._ID + " TEXT PRIMARY KEY," +
                    Entreprises.ENTREPRISE_NOM + " TEXT," +
                    Entreprises.ENTREPRISE_CONTACT + " TEXT," +
                    Entreprises.ENTREPRISE_COURRIEL + " TEXT," +
                    Entreprises.ENTREPRISE_TELEPHONE + " TEXT," +
                    Entreprises.ENTREPRISE_WEB + " TEXT," +
                    Entreprises.ENTREPRISE_ADRESSE + " TEXT," +
                    Entreprises.ENTREPRISE_DATE + " TEXT," +
                    Entreprises.ENTREPRISE_FAVORI + " INTEGER)";

    private static final String SQL_DELETE_ENTREPRISE =
            "DROP TABLE IF EXISTS " + Entreprises.NOM_TABLE;


    private static final String SQL_CREATE_ETUDIANT =
            "CREATE TABLE " + Etudiants.NOM_TABLE + " (" +
                    Etudiants._ID + " TEXT PRIMARY KEY," +
                    Etudiants.ETUDIANT_NOM + " TEXT," +
                    Etudiants.ETUDIANT_PRENOM + " TEXT," +
                    Etudiants.ETUDIANT_COURRIEL + " TEXT," +
                    Etudiants.ETUDIANT_STAGE + " INTEGER," +
                    Etudiants.ETUDIANT_NOMBRE_ENTREPRISE+ " INTEGER)";

    private static final String SQL_DELETE_ETUDIENT =
            "DROP TABLE IF EXISTS " + Etudiants.NOM_TABLE;

    /**
     * Classe interne qui définit le contenu de notre table
     */
    public static class Entreprises implements BaseColumns {
        // Mettre ici toutes les constantes de noms de tables et de colonnes
        public static final String NOM_TABLE = "entreprises";
        public static final String ENTREPRISE_NOM = "nom";
        public static final String ENTREPRISE_CONTACT = "contact";
        public static final String ENTREPRISE_COURRIEL = "courriel";
        public static final String ENTREPRISE_TELEPHONE = "telephone";
        public static final String ENTREPRISE_WEB = "web";
        public static final String ENTREPRISE_ADRESSE = "adresse";
        public static final String ENTREPRISE_DATE = "date";
        public static final String ENTREPRISE_FAVORI = "favori";

    }
    /**
     * Classe interne qui définit le contenu de notre table
     */
    public static class Etudiants implements BaseColumns {
        // Mettre ici toutes les constantes de noms de tables et de colonnes
        public static final String NOM_TABLE = "etudiant";
        public static final String ETUDIANT_NOM = "nom";
        public static final String ETUDIANT_PRENOM = "contact";
        public static final String ETUDIANT_COURRIEL = "courriel";
        public static final String ETUDIANT_STAGE = "stage";
        public static final String ETUDIANT_NOMBRE_ENTREPRISE = "entreprises";

    }
}
