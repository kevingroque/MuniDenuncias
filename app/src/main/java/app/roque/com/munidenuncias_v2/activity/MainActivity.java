package app.roque.com.munidenuncias_v2.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import app.roque.com.munidenuncias_v2.R;
import app.roque.com.munidenuncias_v2.fragements.ListDenunciasFragment;
import app.roque.com.munidenuncias_v2.service.ApiService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView txtName;
    private TextView txtCorreo;
    private ImageView imgPreofile;
    private NavigationView navigationView;

    // SharedPreferences
    private SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //con esto agregamos datos en el header del menu-------------------------------
        View hView = navigationView.getHeaderView(0);
        txtName = (TextView) hView.findViewById(R.id.nonmbre_nav);
        txtCorreo = (TextView)hView.findViewById(R.id.correo_nav);
        imgPreofile = (ImageView)hView.findViewById(R.id.imageView);

        // init SharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // get id from SharedPreferences
        String imagen = sharedPreferences.getString("imagen", null);
        Log.d(TAG, "imagen: " + imagen);
        String url = ApiService.API_BASE_URL + "/images/" + imagen;
        Picasso.with(this).load(url).into(imgPreofile);

        String nombres = sharedPreferences.getString("nombres", null);
        Log.d(TAG, "nombres: " + nombres);

        String correo = sharedPreferences.getString("correo", null);
        Log.d(TAG, "correo: " + correo);

        txtName.setText(nombres);
        txtCorreo.setText(correo);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        ListDenunciasFragment listDenunciasFragment = new ListDenunciasFragment();
        transaction.replace(R.id.content, listDenunciasFragment);
        transaction.commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (id == R.id.nav_list) {
            ListDenunciasFragment fragment = new ListDenunciasFragment();
            transaction.replace(R.id.content, fragment);
            transaction.commit();

        } else if (id == R.id.nav_new) {
            Intent intent = new Intent(this, CrearDenunciaActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_send) {
            showDialog(navigationView);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logoutUser() {

        // remove from SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        boolean success = editor.putBoolean("islogged", false).commit();
        boolean success2 = editor
                .putString("user_nombre", "")
                .commit();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();

    }



    public void showDialog(final View view){

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Cerrar Sesión");
        alertDialog.setMessage("Esta seguro de que quiere cerrar sesion??");
        // Alert dialog button
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OKEY",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        logoutUser();
                    }
                });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Alert dialog action goes here
                        dialog.dismiss();
                    }
                });

        alertDialog.show();

    }
}
