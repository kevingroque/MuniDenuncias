package app.roque.com.munidenuncias_v2.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import app.roque.com.munidenuncias_v2.R;
import app.roque.com.munidenuncias_v2.models.Denuncia;
import app.roque.com.munidenuncias_v2.service.ApiService;
import app.roque.com.munidenuncias_v2.service.ApiServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleDenunciaActivity extends AppCompatActivity {

    private static final String TAG = DetalleDenunciaActivity.class.getSimpleName();

    private Integer id;

    private ImageView fotoImage;
    private TextView tituloText, descripcionText, ubicacionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_denuncia);

        showToolbar("",true);
        fotoImage = (ImageView)findViewById(R.id.imageDetalle);
        tituloText = (TextView)findViewById(R.id.tituloDetalle);
        descripcionText = (TextView)findViewById(R.id.descripcionDetalle);
        ubicacionText = (TextView)findViewById(R.id.ubicacionDetalle);

        id = getIntent().getExtras().getInt("ID");
        Log.e(TAG, "id:" + id);

        initialize();
    }

    private void initialize() {

        ApiService service = ApiServiceGenerator.createService(ApiService.class);

        Call<Denuncia> call = service.showDenuncias(id);

        call.enqueue(new Callback<Denuncia>() {
            @Override
            public void onResponse(Call<Denuncia> call, Response<Denuncia> response) {
                try {

                    int statusCode = response.code();
                    Log.d(TAG, "HTTP status code: " + statusCode);

                    if (response.isSuccessful()) {

                        Denuncia denuncia = response.body();
                        Log.d(TAG, "denuncia: " + denuncia);

                        String url = ApiService.API_BASE_URL + "images/" + denuncia.getImagen();
                        Picasso.with(DetalleDenunciaActivity.this).load(url).into(fotoImage);

                        tituloText.setText(denuncia.getTitulo());
                        descripcionText.setText(denuncia.getDescripcion());
                        ubicacionText.setText(denuncia.getUbicacion());


                    } else {
                        Log.e(TAG, "onError: " + response.errorBody().string());
                        throw new Exception("Error en el servicio");
                    }

                } catch (Throwable t) {
                    try {
                        Log.e(TAG, "onThrowable: " + t.toString(), t);
                        Toast.makeText(DetalleDenunciaActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }catch (Throwable x){}
                }
            }

            @Override
            public void onFailure(Call<Denuncia> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
                Toast.makeText(DetalleDenunciaActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
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
