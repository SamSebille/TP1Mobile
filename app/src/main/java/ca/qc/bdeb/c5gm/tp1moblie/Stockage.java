package ca.qc.bdeb.c5gm.tp1moblie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.lang.reflect.Array;
import java.util.ArrayList;

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
                Entreprises.ENTREPRISE_DATE
        };

        String selection = Entreprises._ID + " = ?";
        Cursor cursor = db.query(Entreprises.NOM_TABLE, columns, null, null,
                null, null, Entreprises.ENTREPRISE_NOM, null);

        ArrayList<Entreprise> entreprises = new ArrayList<>();

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                entreprises.add(new Entreprise(
                        cursor.getInt(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3),
                        cursor.getString(4), cursor.getString(5),
                        cursor.getString(6), cursor.getString(7)));
            } while (cursor.moveToNext());
            cursor.close();
        }

        return entreprises;
    }

    public Entreprise getEntreprise(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {
                Entreprises._ID,
                Entreprises.ENTREPRISE_NOM,
                Entreprises.ENTREPRISE_CONTACT,
                Entreprises.ENTREPRISE_COURRIEL,
                Entreprises.ENTREPRISE_TELEPHONE,
                Entreprises.ENTREPRISE_WEB,
                Entreprises.ENTREPRISE_ADRESSE,
                Entreprises.ENTREPRISE_DATE
        };

        String selection = Entreprises._ID + " = ?";

        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = db.query(Entreprises.NOM_TABLE, columns, selection, selectionArgs,
                null, null, null, null);

        Entreprise entreprise = null;

        if (cursor != null) {
            cursor.moveToFirst();

            entreprise = new Entreprise(
                    cursor.getInt(0), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3),
                    cursor.getString(4), cursor.getString(5),
                    cursor.getString(6), cursor.getString(7));
        }

        cursor.close();

        return entreprise;
    }

    public boolean updateEntreprise(Entreprise entreprise) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Entreprises.ENTREPRISE_NOM, entreprise.getNom());
        values.put(Entreprises.ENTREPRISE_CONTACT, entreprise.getContact());
        values.put(Entreprises.ENTREPRISE_COURRIEL, entreprise.getCourriel());
        values.put(Entreprises.ENTREPRISE_TELEPHONE, entreprise.getTelephone());
        values.put(Entreprises.ENTREPRISE_WEB, entreprise.getWeb());
        values.put(Entreprises.ENTREPRISE_ADRESSE, entreprise.getAdresse());
        values.put(Entreprises.ENTREPRISE_DATE, entreprise.getDate());

        String whereClause = Entreprises._ID + " = ?";
        String[] whereArgs = {String.valueOf(entreprise.getId())};

        // MAJ de l’enregistrement
        int nbMAJ = db.update(Entreprises.NOM_TABLE, values, whereClause, whereArgs);

        return (nbMAJ > 0); // True si update, false sinon
    }

    public void ajouterEntreprise(Entreprise entreprise) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Entreprises.ENTREPRISE_NOM, entreprise.getNom());
        values.put(Entreprises.ENTREPRISE_CONTACT, entreprise.getContact());
        values.put(Entreprises.ENTREPRISE_COURRIEL, entreprise.getCourriel());
        values.put(Entreprises.ENTREPRISE_TELEPHONE, entreprise.getTelephone());
        values.put(Entreprises.ENTREPRISE_WEB, entreprise.getWeb());
        values.put(Entreprises.ENTREPRISE_ADRESSE, entreprise.getAdresse());
        values.put(Entreprises.ENTREPRISE_DATE, entreprise.getDate());

        long id = db.insert(Entreprises.NOM_TABLE, null, values);

        entreprise.setId((int) id);
    }

    public void deleteEntreprise(Entreprise entreprise) {
        SQLiteDatabase db = this.getWritableDatabase(); // On veut écrire dans la BD
        String whereClause = Entreprises._ID + " = ?";
        String[] whereArgs = {String.valueOf(entreprise.getId())};
        db.delete(Entreprises.NOM_TABLE, whereClause, whereArgs);
    }

    public void dropTable() {
        SQLiteDatabase db = this.getWritableDatabase(); // On veut écrire dans la B
        db.execSQL(SQL_DELETE_CLIENTS);
    }

    private static final String SQL_CREATE_ENTREPRISES =
            "CREATE TABLE " + Entreprises.NOM_TABLE + " (" +
                    Entreprises._ID + " INTEGER PRIMARY KEY," +
                    Entreprises.ENTREPRISE_NOM + " TEXT," +
                    Entreprises.ENTREPRISE_CONTACT + " TEXT," +
                    Entreprises.ENTREPRISE_COURRIEL + " TEXT," +
                    Entreprises.ENTREPRISE_TELEPHONE + " TEXT," +
                    Entreprises.ENTREPRISE_WEB + " TEXT," +
                    Entreprises.ENTREPRISE_ADRESSE + " TEXT," +
                    Entreprises.ENTREPRISE_DATE + " TEXT)";

    private static final String SQL_DELETE_CLIENTS =
            "DROP TABLE IF EXISTS " + Entreprises.NOM_TABLE;

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
    }
}
