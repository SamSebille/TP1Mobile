package ca.qc.bdeb.c5gm.tp1moblie.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ca.qc.bdeb.c5gm.tp1moblie.BD.Stockage;
import ca.qc.bdeb.c5gm.tp1moblie.R;
import ca.qc.bdeb.c5gm.tp1moblie.REST.ConnectUtils;

/**
 * Classe pour le menu principal de l'application, affichant la liste d'entreprise
 * et redirigant vers les autres pages.
 */
public class MenuActivity extends AppCompatActivity {

    private Stockage stockage;

    private static ArrayList<Entreprise> entreprises;

    private RecyclerView recyclerView;
    private EntrepriseListAdapter entrepriseListAdapter;

    SearchView searchView;

    RadioGroup radioGroupFavori;
    RadioGroup radioGroupTri;

    // Si les entreprise doivent être triées par date, triées par nom par defaut.
    private static boolean triParDate;
    // Si seules les entreprise favorites doivent être affichées, toutes par defaut.
    private static boolean triParFavori;

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
        entrepriseListAdapter = new EntrepriseListAdapter(this, entreprises);
        // Connecter l'adapter au RecyclerView.
        recyclerView.setAdapter(entrepriseListAdapter);
        // Donner au RecyclerView un layout manager par défaut.
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        radioGroupFavori = findViewById(R.id.toggle_favori);
        radioGroupTri = findViewById(R.id.toggle_date);

        searchView = findViewById(R.id.sai_recherche);

        // below line is to call set on query text listener method.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // inside on query text change method we are
                // calling a method to filter our recycler view.
                filter(newText);
                return false;
            }
        });

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        // On met la liste a jour avec la bd a chaque lancement de l'activité.
        majListBDEntreprise();

        if (triParFavori)
            radioGroupFavori.check(R.id.btn_favoris);
        if (triParDate)
            radioGroupTri.check(R.id.btn_tri_date);
    }

    /**
     * Met a jour le tableau d'entreprise avec la bd et actualise le RecycleView.
     */
    public void majListBDEntreprise() {
        entreprises = stockage.getEntreprises();
        entrepriseListAdapter.entreprises = entreprises;
        majListEntreprise();
    }

    /**
     * Actualise la liste du RecycleView selon les tris.
     */
    public void majListEntreprise() {

        if (triParFavori) {
            entrepriseListAdapter.entreprises.removeIf(entreprise -> !entreprise.estFavorite());
        }

        if (triParDate)
            entrepriseListAdapter.entreprises.sort(new SortByDate());
        else
            entrepriseListAdapter.entreprises.sort(new SortByName());

        entrepriseListAdapter.notifyDataSetChanged();
    }

    public void onClickTriNom(View view) {
        triParDate = false;
        majListEntreprise();
    }

    public void onClickTriDate(View view) {
        triParDate = true;
        majListEntreprise();
    }

    public void onClickTriToutes(View view) {
        triParFavori = false;
        majListBDEntreprise();
    }

    public void onClickTriFavorites(View view) {
        triParFavori = true;
        majListEntreprise();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_btn_deconnexion) {
            ConnectUtils.deconnexion(this);
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    private void filter(String text) {
        ArrayList<Entreprise> filteredlist = new ArrayList<Entreprise>();

        for (Entreprise item : entreprises) {
            if (item.getNom().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(this, "Aucune entreprise trouvée", Toast.LENGTH_SHORT).show();
        } else {
            entrepriseListAdapter.filterList(filteredlist);
        }
    }

    public void updateFavori(ImageButton ib_favori, Entreprise entreprise) {
        if (entreprise.estFavorite()) {
            entreprise.setEstFavorite(false);
            ib_favori.setImageResource(R.mipmap.ic_favorite_border_24px);
            ConnectUtils.modifierEntreprise(this, entreprise, false);
        } else {
            entreprise.setEstFavorite(true);
            ib_favori.setImageResource(R.mipmap.ic_favorite_24px);
            ConnectUtils.modifierEntreprise(this, entreprise, false);
        }
    }

    /**
     * Adapter pour la RecycleView
     */
    public class EntrepriseListAdapter extends
            RecyclerView.Adapter<EntrepriseListAdapter.EntrepriseViewHolder> {

        private ArrayList<Entreprise> entreprises;
        private LayoutInflater inflater;

        public EntrepriseListAdapter(Context context,
                                     ArrayList<Entreprise> entreprises) {
            inflater = LayoutInflater.from(context);
            this.entreprises = entreprises;
        }

        // method for filtering our recyclerview items.
        public void filterList(ArrayList<Entreprise> filterList) {
            entreprises = filterList;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public EntrepriseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View mItemView = inflater.inflate(R.layout.entreprise_item,
                    parent, false);
            return new EntrepriseViewHolder(mItemView, this);
        }

        @Override
        public void onBindViewHolder(@NonNull EntrepriseViewHolder holder, int position) {
            Entreprise entreprise = entreprises.get(position);
            holder.tv_entreprise.setText(entreprise.getNom());
            if (entreprise.estFavorite())
                holder.ib_favori.setImageResource(R.mipmap.ic_favorite_24px);
            else
                holder.ib_favori.setImageResource(R.mipmap.ic_favorite_border_24px);
        }

        @Override
        public int getItemCount() {
            return entreprises.size();
        }

        public class EntrepriseViewHolder extends RecyclerView.ViewHolder {

            private final TextView tv_entreprise;
            private final ImageButton ib_favori;

            private View.OnClickListener onClickEntreprise = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Récupère la position de l'item item clické.
                    int mPosition = getLayoutPosition();
                    // Accède l'item dans stringList avec l'info de position.
                    Entreprise entreprise = entreprises.get(mPosition);

                    // On passe les informations de l'entreprise a l'activité de modification.
                    Intent intent = new Intent(getBaseContext(), EntrepriseActivity.class);
                    intent.putExtra("ENTREPRISE_ID", entreprise.getId().toString());
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
                    Entreprise entreprise = entreprises.get(mPosition);

                    updateFavori(ib_favori, entreprise);
                }
            };
            final EntrepriseListAdapter adapter;

            public EntrepriseViewHolder(@NonNull View itemView, EntrepriseListAdapter adapter) {
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

