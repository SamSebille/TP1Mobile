package ca.qc.bdeb.c5gm.tp1moblie;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Classe pour le menu principal de l'application, affichant la liste d'entreprise
 * et redirigant vers les autres pages.
 */
public class MenuActivity extends AppCompatActivity {

    private Stockage stockage;

    private static ArrayList<Entreprise> entreprises;

    private RecyclerView recyclerView;
    private StringListAdapter entrepriseListAdapter;

    // Si les entreprise doivent être triées par date, triées par nom par defaut.
    private static boolean triParDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(R.string.title_activity_menu);

        stockage = Stockage.getInstance(getApplicationContext());

        // initialiser le recyclerView
        recyclerView = findViewById(R.id.liste_entreprises);
        // Créer l'adapter et lui fournir les données.
        entrepriseListAdapter = new StringListAdapter(this, entreprises);
        // Connecter l'adapter au RecyclerView.
        recyclerView.setAdapter(entrepriseListAdapter);
        // Donner au RecyclerView un layout manager par défaut.
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
        // On met la liste a jour a chaque lancement de l'activité.
        majListBDEntreprise();
    }

    /**
     * Met a jour le tableau d'entreprise et actualise RecycleView.
     */
    public void majListBDEntreprise() {
        entreprises = stockage.getEntreprises();

        if (triParDate)
            entreprises.sort(new SortByDate());
        else
            entreprises.sort(new SortByName());

        // Ne marche pas sans actualiser la liste de l'adapter à la main
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

    /**
     * Adapter pour la RecycleView
     */
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
            holder.tv_entreprise.setText(entreprise.getNom());
            if (entreprise.isFavori())
                holder.ib_favori.setImageResource(R.mipmap.ic_favorite_24px);
            else
                holder.ib_favori.setImageResource(R.mipmap.ic_favorite_border_24px);
        }

        @Override
        public int getItemCount() {
            return entreprises.size();
        }

        public class StringViewHolder extends RecyclerView.ViewHolder {

            private TextView tv_entreprise;
            private ImageButton ib_favori;

            private View.OnClickListener onClickEntreprise = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Récupère la position de l'item item clické.
                    int mPosition = getLayoutPosition();
                    // Accède l'item dans stringList avec l'info de position.
                    Entreprise element = entreprises.get(mPosition);

                    // On passe les informations de l'entreprise a l'activité de modification.
                    Intent intent = new Intent(getBaseContext(), EntrepriseActivity.class);
                    intent.putExtra("ENTREPRISE_ID", element.getId());
                    intent.putExtra("ISMODIFIER", true);
                    startActivity(intent);
                }
            };

            private View.OnClickListener onClickFavori = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Récupère la position de l'item item clické.
                    int mPosition = getLayoutPosition();
                    // Accède l'item dans stringList avec l'info de position.
                    Entreprise element = entreprises.get(mPosition);

                    if (element.isFavori()){
                        element.setFavori(false);
                        ib_favori.setImageResource(R.mipmap.ic_favorite_border_24px);
                        stockage.updateFavori(element);
                    } else {
                        element.setFavori(true);
                        ib_favori.setImageResource(R.mipmap.ic_favorite_24px);
                        stockage.updateFavori(element);
                    }
                }
            };
            final StringListAdapter adapter;

            public StringViewHolder(@NonNull View itemView, StringListAdapter adapter) {
                super(itemView);
                tv_entreprise = (TextView) itemView.findViewById(R.id.menu_nom_entreprise);
                tv_entreprise.setOnClickListener(onClickEntreprise);

                ib_favori = itemView.findViewById(R.id.btn_favori);
                ib_favori.setOnClickListener(onClickFavori);

                this.adapter = adapter;
            }
        }
    }
}

