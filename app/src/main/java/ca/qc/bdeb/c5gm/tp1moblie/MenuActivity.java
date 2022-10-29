package ca.qc.bdeb.c5gm.tp1moblie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    private Stockage stockage;

    private static ArrayList<Entreprise> entreprises;

    private RecyclerView recyclerView;
    private StringListAdapter stringListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        stockage = Stockage.getInstance(getApplicationContext());

        entreprises = new ArrayList<>();
        majListBDEntreprise();

        // initialiser le recyclerView
        recyclerView = findViewById(R.id.liste_entreprises);
        // Créer l'adapter et lui fournir les données.
        stringListAdapter = new StringListAdapter(this, entreprises);
        // Connecter l'adapter au RecyclerView.
        recyclerView.setAdapter(stringListAdapter);
        // Donner au RecyclerView un layout manager par défaut.
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void majListBDEntreprise(){
        ArrayList<Entreprise> entreprises = stockage.getEntreprises();
    }

    public void onClickMaps(View view) {
        startActivity(new Intent(this, MapsActivity.class));
    }

    public void onClickPlus(View view) {
        startActivity(new Intent(this, NouvelleEntrepriseActivity.class));
    }

    public class StringListAdapter extends
            RecyclerView.Adapter<StringListAdapter.StringViewHolder> {

        private final ArrayList<Entreprise> entreprises;
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

        public class StringViewHolder extends RecyclerView.ViewHolder{
            TextView tv;
            final StringListAdapter adapter;

            public StringViewHolder(@NonNull View itemView, StringListAdapter adapter) {
                super(itemView);
                tv = (TextView) itemView.findViewById(R.id.textViewMenuNameEntrprise);
                this.adapter = adapter;
            }
        }

    }
}