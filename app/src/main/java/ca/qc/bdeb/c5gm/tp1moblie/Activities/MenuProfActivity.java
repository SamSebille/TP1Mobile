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

public class MenuProfActivity extends AppCompatActivity {

    private Stockage stockage;

    private static ArrayList<Etudiant> etudiants;

    private RecyclerView recyclerView;
    private EtudiantListAdapter etudiantListAdapter;

    SearchView searchView;

    RadioGroup radioGroupStage;
    RadioGroup radioGroupTri;

    // Si les entreprise doivent être triées par date, triées par nom par defaut.
    private static boolean triParNom;
    // Si seules les entreprise favorites doivent être affichées, toutes par defaut.
    private static boolean triParStage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_prof);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(R.string.title_activity_menu);

        stockage = Stockage.getInstance(getApplicationContext());

        // initialiser le recyclerView
        recyclerView = findViewById(R.id.liste_etudiant);
        // Créer l'adapter et lui fournir les données.
        etudiantListAdapter = new EtudiantListAdapter(this, etudiants);
        // Connecter l'adapter au RecyclerView.
        recyclerView.setAdapter(etudiantListAdapter);
        // Donner au RecyclerView un layout manager par défaut.
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        radioGroupStage = findViewById(R.id.toggle_stage);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_btn_deconnexion){
            ConnectUtils.deconnexion(this);
            return true;
        }
        else
            return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // On met la liste a jour avec la bd a chaque lancement de l'activité.
        majListBDEtudiant();

        if (triParStage)
            radioGroupStage.check(R.id.btn_tri_stage);
    }

    /**
     * Met a jour le tableau d'entreprise avec la bd et actualise le RecycleView.
     */
    public void majListBDEtudiant() {
        etudiants = stockage.getEtudiants();
        etudiantListAdapter.etudiants = etudiants;
        majListEtudiant();
    }

    /**
     * Actualise la liste du RecycleView selon les tris.
     */
    public void majListEtudiant() {

        if (triParNom)
            etudiantListAdapter.etudiants.sort(new SortByStudentName());
        else if (triParStage)
            etudiantListAdapter.etudiants.removeIf(etudiant -> !etudiant.isStageTrouve());

        etudiantListAdapter.notifyDataSetChanged();
    }

    public void onClickTriNom(View view) {
        triParNom = false;
        majListEtudiant();
    }

    public void onClickTriStage(View view) {
        triParNom = true;
        majListEtudiant();
    }

    public void onClickMaps(View view) {
        startActivity(new Intent(this,
                MapsActivity.class));
    }

    public void onClickPlus(View view) {
        Intent intent = new Intent(getBaseContext(), EtudintActivity.class);
        intent.putExtra("ISMODIFIER", false);
        startActivity(intent);
    }

    private void filter(String text) {
        ArrayList<Etudiant> filteredlist = new ArrayList<Etudiant>();

        for (Etudiant item : etudiants) {
            if (item.getNom().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(this, "Aucune entreprise trouvée", Toast.LENGTH_SHORT).show();
        } else {
            etudiantListAdapter.filterList(filteredlist);
        }
    }

    /**
     * Adapter pour la RecycleView
     */
    public class EtudiantListAdapter extends
            RecyclerView.Adapter<EtudiantListAdapter.EtudiantViewHolder> {

        private ArrayList<Etudiant> etudiants;
        private LayoutInflater inflater;

        public EtudiantListAdapter(Context context,
                                   ArrayList<Etudiant> etudiants) {
            inflater = LayoutInflater.from(context);
            this.etudiants = etudiants;
        }

        // method for filtering our recyclerview items.
        public void filterList(ArrayList<Etudiant> filterList) {
            etudiants = filterList;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public EtudiantListAdapter.EtudiantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View mItemView = inflater.inflate(R.layout.entreprise_item,
                    parent, false);
            return new EtudiantViewHolder(mItemView, this);
        }

        @Override
        public void onBindViewHolder(@NonNull EtudiantListAdapter.EtudiantViewHolder holder, int position) {
            Etudiant etudiant = etudiants.get(position);
            holder.tv_etudiant.setText(etudiant.getNom());
            if (etudiant.isStageTrouve())
                holder.ib_stage.setImageResource(R.mipmap.ic_favorite_24px);
            else
                holder.ib_stage.setImageResource(R.mipmap.ic_favorite_border_24px);
        }

        @Override
        public int getItemCount() {
            return etudiants.size();
        }

        public class EtudiantViewHolder extends RecyclerView.ViewHolder {

            private final TextView tv_etudiant;
            private final ImageButton ib_stage;

            private View.OnClickListener onClickEtudiant = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Récupère la position de l'item item clické.
                    int mPosition = getLayoutPosition();
                    // Accède l'item dans stringList avec l'info de position.
                    Etudiant element = etudiants.get(mPosition);

                    // On passe les informations de l'entreprise a l'activité de modification.
                    Intent intent = new Intent(getBaseContext(), EtudiantActivity.class);
                    intent.putExtra("ENTREPRISE_ID", element.getId());
                    intent.putExtra("ISMODIFIER", true);
                    startActivity(intent);
                }
            };

            final EtudiantListAdapter adapter;

            public EtudiantViewHolder(@NonNull View itemView, EtudiantListAdapter adapter) {
                super(itemView);
                tv_etudiant = (TextView) itemView.findViewById(R.id.menu_nom_entreprise);
                tv_etudiant.setOnClickListener(onClickEtudiant);

                ib_stage = itemView.findViewById(R.id.btn_favori);


                this.adapter = adapter;
            }
        }
    }

}