package com.koopers.techservice.ui.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;
import androidx.palette.graphics.Palette;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.koopers.techservice.R;
import com.koopers.techservice.data.local.entity.Categy;
import com.koopers.techservice.data.local.entity.Equipo;
import com.koopers.techservice.data.local.entity.Estado;
import com.koopers.techservice.ui.adapters.MyDetalles;
import com.koopers.techservice.ui.sheets.Create_Fragment;
import com.koopers.techservice.utils.image.QrScan;
import com.koopers.techservice.vm.CategoriaViewModel;
import com.koopers.techservice.vm.EstadoViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Detail_Activity extends AppCompatActivity {
    @BindView(R.id.image_large)
    ImageView imageLarge;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.card_main)
    CardView cardMain;
    @BindView(R.id.setQrCode)
    ImageView setQrCode;
    @BindView(R.id.setEstado)
    TextView setEstado;
    @BindView(R.id.card_price)
    CardView cardPrice;
    @BindView(R.id.setMarca)
    TextView setMarca;
    @BindView(R.id.setModelo)
    TextView setModelo;
    @BindView(R.id.setCategoria)
    TextView setCategoria;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private List<Estado> estadoList;
    private List<Categy> categyList;
    private QrScan qrScan;
    private Equipo equipo;
    private int mutedColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        qrScan = new QrScan(this);

        loadViewModel();
        supportPostponeEnterTransition();
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image_example);
        Palette.from(bitmap).generate(palette -> {
            assert palette != null;
            mutedColor = palette.getMutedColor(R.attr.color);
            collapsingToolbar.setContentScrimColor(mutedColor);
        });

        equipo = (Equipo) getIntent().getSerializableExtra("equipo");

        if (equipo != null) {
            setMarca.setText(equipo.getMarca());
            setModelo.setText(equipo.getModelo());
            setEstado.setText(equipo.getEstado());
            setCategoria.setText(equipo.getCategoria());
            setQrCode.setOnClickListener(v -> qrScan.CustomDialog(equipo, false));
            setQrCode.setImageBitmap(qrScan.StringToQr(String.valueOf(equipo.getId())));
        }

        CargarFragment();
    }

    private void loadViewModel(){
        CategoriaViewModel categoriaViewModel = ViewModelProviders.of(this).get(CategoriaViewModel.class);
        categoriaViewModel.getCategory().observe(this, listResource -> categyList = listResource.data);

        EstadoViewModel estadoViewModel = ViewModelProviders.of(this).get(EstadoViewModel.class);
        estadoViewModel.getStatus().observe(this, listResource -> estadoList =listResource.data);
    }

    private void CargarFragment() {
        runOnUiThread(() -> {
            MyDetalles myAdapter = new MyDetalles(getSupportFragmentManager(), equipo);
            viewPager.setAdapter(myAdapter);
            viewPager.setCurrentItem(0, true);
            // Listeners
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));
            tablayout.addOnTabSelectedListener(myAdapter.SelectedListener(viewPager));

        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.edit) {
            ShowEdition();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void ShowEdition(){
        Create_Fragment sheet = Create_Fragment.newInstance(estadoList, categyList, equipo);
        sheet.show(getSupportFragmentManager(), "DemoBottomSheetFragment");
    }
}
