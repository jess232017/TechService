package com.koopers.techservice.ui.activities;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.LinearInterpolator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.balysv.materialripple.MaterialRippleLayout;
import com.evolve.backdroplibrary.BackdropContainer;
import com.ferfalk.simplesearchview.SimpleSearchView;
import com.ferfalk.simplesearchview.utils.DimensUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.koopers.techservice.R;
import com.koopers.techservice.data.local.entity.Categy;
import com.koopers.techservice.data.local.entity.Estado;
import com.koopers.techservice.ui.adapters.MyEstado;
import com.koopers.techservice.ui.sheets.Create_Fragment;
import com.koopers.techservice.utils.QuerySearch;
import com.koopers.techservice.utils.view.CustomTabLayout;
import com.koopers.techservice.vm.CategoriaViewModel;
import com.koopers.techservice.vm.EstadoViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Status_Activity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.backdropcontainer)
    BackdropContainer backdropcontainer;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.tablayout)
    CustomTabLayout tablayout;
    @BindView(R.id.container)
    ViewPager container;
    @BindView(R.id.searchView)
    SimpleSearchView searchView;
    @BindView(R.id.card_view)
    CardView cardView;
    @BindView(R.id.btn_back)
    MaterialRippleLayout btnBack;
    @BindView(R.id.btn_estado)
    MaterialRippleLayout btnEstado;
    @BindView(R.id.btn_categoria)
    MaterialRippleLayout btnCategoria;

    private EventBus bus = EventBus.getDefault();
    private List<String> filterbyList;
    private List<Estado> estadoList;
    private List<Categy> categyList;
    private MyEstado myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        ButterKnife.bind(this);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        loadViewModel();

        backdropcontainer.attachToolbar(toolbar)
                .dropInterpolator(new LinearInterpolator())
                .dropHeight(getSize())
                .build();

        fab.setOnClickListener(v -> {
            Create_Fragment sheet = Create_Fragment.newInstance(estadoList, categyList, null);
            sheet.show(getSupportFragmentManager(), "CreateDevice");
        });

        btnBack.setOnClickListener(v -> onSupportNavigateUp());
        btnEstado.setOnClickListener(v -> {
            ArrayList<String> filterby = new ArrayList<>();
            for (Estado estado : estadoList) {
                filterby.add(estado.getEstado());
            }
            CargarTab(filterby, true);
        });
        btnCategoria.setOnClickListener(v ->{
            ArrayList<String> filterby = new ArrayList<>();
            for (Categy categy : categyList) {
                filterby.add(categy.getCategoria());
            }
            CargarTab(filterby, true);
        });
    }

    private void loadViewModel() {
        CategoriaViewModel categoriaViewModel = ViewModelProviders.of(this).get(CategoriaViewModel.class);
        categoriaViewModel.getCategory().observe(this, listResource -> categyList = listResource.data);

        EstadoViewModel estadoViewModel = ViewModelProviders.of(this).get(EstadoViewModel.class);
        estadoViewModel.getStatus().observe(this, listResource -> {
            if(listResource.data != null){
                estadoList = listResource.data;
                ArrayList<String> filterby = new ArrayList<>();
                for (Estado estado : estadoList) {
                    filterby.add(estado.getEstado());
                }
                CargarTab(filterby, false);
            }
        });
    }

    private void CargarTab(List<String> filterby, boolean force) {
        runOnUiThread(() -> {
            if (filterby != null) {
                backdropcontainer.closeBackview();

                if (filterbyList == null)
                    filterbyList = filterby;
                else if (filterbyList.equals(filterby) && !force)
                    return;

                filterbyList = filterby;
                tablayout.removeAllTabs();

                for (String title : filterbyList) {
                    tablayout.addTab(tablayout.newTab().setText(title));
                }

                myAdapter = new MyEstado(getSupportFragmentManager(), filterbyList);
                container.setAdapter(myAdapter);
                container.setCurrentItem(0, true);
                // Listeners
                container.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));
                tablayout.addOnTabSelectedListener(myAdapter.SelectedListener(container));
            }
        });
    }

    private int getSize() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels - 510; // alto absoluto en pixels
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);
        setupSearchView(menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (searchView.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    private void setupSearchView(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        searchView.setHint(getString(R.string.searchview));
        searchView.setTabLayout(tablayout);

        searchView.setOnSearchViewListener(new SimpleSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                backdropcontainer.closeBackview();
                bus.post(new QuerySearch("", true));
                cardView.setRadius(DimensUtils.convertDpToPx(0, Status_Activity.this));
            }

            @Override
            public void onSearchViewClosed() {
                bus.post(new QuerySearch("", false));
                cardView.setRadius(DimensUtils.convertDpToPx(23, Status_Activity.this));
            }

            @Override
            public void onSearchViewShownAnimation() {
                fab.hide();
            }

            @Override
            public void onSearchViewClosedAnimation() {
                fab.show();
            }
        });

        searchView.setOnQueryTextListener(new SimpleSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("SimpleSearchView", "Submit:" + query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                bus.post(new QuerySearch(newText, true));
                return false;
            }

            @Override
            public boolean onQueryTextCleared() {
                Log.d("SimpleSearchView", "Text cleared");
                return false;
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        bus.unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        bus.register(this);
    }

    @Subscribe
    public void ShownQuery(QuerySearch search) {

    }
}
