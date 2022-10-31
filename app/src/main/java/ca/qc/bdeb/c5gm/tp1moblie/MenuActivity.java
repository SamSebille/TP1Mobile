package ca.qc.bdeb.c5gm.tp1moblie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    private Stockage stockage;

    private static ArrayList<Entreprise> entreprises;

    private RecyclerView recyclerView;
    private StringListAdapter entrepriseListAdapter;

    private static boolean triParDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        stockage = Stockage.getInstance(getApplicationContext());

        entreprises = stockage.getEntreprises();

        // initialiser le recyclerView
        recyclerView = findViewById(R.id.liste_entreprises);
        // Créer l'adapter et lui fournir les données.
        entrepriseListAdapter = new StringListAdapter(this, entreprises);
        // Connecter l'adapter au RecyclerView.
        recyclerView.setAdapter(entrepriseListAdapter);
        // Donner au RecyclerView un layout manager par défaut.
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_main_menu, menu);
        return true;
    }
    @Override
    protected void onStart() {
        super.onStart();
        majListBDEntreprise();
    }

    public void majListBDEntreprise(){
        entreprises = stockage.getEntreprises();

        if (triParDate)
            entreprises.sort(new SortByDate());
        else
            entreprises.sort(new SortByName());

        entrepriseListAdapter.entreprises = entreprises;

        if (entreprises != null)
            entrepriseListAdapter.notifyDataSetChanged();

    }

    public void onClickTriNom(View view) {
        triParDate = false;
        majListBDEntreprise();
    }

    public void onClickTriDate(View view) {
        triParDate = true;
        majListBDEntreprise();
    }

    public void onClickMaps(View view) {
        startActivity(new Intent(this,
                MapsActivity.class));
    }

    public void onClickPlus(View view) {
        Intent intent = new Intent(getBaseContext(), EntrepriseActivity.class);
        intent.putExtra("ISMODIFIER", false);
        startActivity(intent);
    }

    public class StringListAdapter extends
            RecyclerView.Adapter<StringListAdapter.StringViewHolder> {

        private ArrayList<Entreprise> entreprises;
        private LayoutInflater inflater;

        public StringListAdapter(Context context,
                                 ArrayList<Entreprise> entreprises) {
            inflater = LayoutInflater.from(context);
            this.entreprises = entreprises;
        }

        @NonNull
        @Override
        public StringViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View mItemView = inflater.inflate(R.layout.entreprise_item,
                    parent, false);
            return new StringViewHolder(mItemView, this);
        }

        @Override
        public void onBindViewHolder(@NonNull StringViewHolder holder, int position) {
            Entreprise entreprise = entreprises.get(position);
            holder.tv.setText(entreprise.getNom());
        }

        @Override
        public int getItemCount() {
            return entreprises.size();
        }

        public class StringViewHolder extends RecyclerView.ViewHolder 
                implements View.OnClickListener{
            TextView tv;
            final StringListAdapter adapter;

            public StringViewHolder(@NonNull View itemView, StringListAdapter adapter) {
                super(itemView);
                tv = (TextView) itemView.findViewById(R.id.menu_nom_entreprise);
                tv.setOnClickListener(this);
                this.adapter = adapter;
            }

            @Override
            public void onClick(View view) {
                // Récupère la position de l'item item clické.
                int mPosition = getLayoutPosition();
                // Accède l'item dans stringList avec l'info de position.
                Entreprise element = entreprises.get(mPosition);

                Intent intent = new Intent(getBaseContext(), EntrepriseActivity.class);
                intent.putExtra("ENTREPRISE_ID", element.getId());
                intent.putExtra("ISMODIFIER", true);
                startActivity(intent);

                majListBDEntreprise();
            }


        }

    }
}

