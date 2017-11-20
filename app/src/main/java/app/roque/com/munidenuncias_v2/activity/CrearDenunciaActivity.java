package app.roque.com.munidenuncias_v2.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import app.roque.com.munidenuncias_v2.R;

public class CrearDenunciaActivity extends AppCompatActivity {

    private Button btnGoMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_denuncia);

        showToolbar("Nueva Denuncia",true);

        btnGoMap = (Button)findViewById(R.id.btnGoMap);

        btnGoMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(CrearDenunciaActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

    }

    public void showToolbar(String title, boolean upButton){
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }
}
