package app.roque.com.munidenuncias_v2.fragements;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import app.roque.com.munidenuncias_v2.R;
import app.roque.com.munidenuncias_v2.activity.CrearDenunciaActivity;
import app.roque.com.munidenuncias_v2.adapter.DenunciasAdapter;
import app.roque.com.munidenuncias_v2.models.Denuncia;
import app.roque.com.munidenuncias_v2.service.ApiService;
import app.roque.com.munidenuncias_v2.service.ApiServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListDenunciasFragment extends Fragment {

    private static final String TAG = ListDenunciasFragment.class.getSimpleName();

    private RecyclerView denunciasList;
    private FloatingActionButton RegisterDenuncia;
    private Integer id;



    public ListDenunciasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_denuncias_fragment, container, false);

        RegisterDenuncia = (FloatingActionButton) view.findViewById(R.id.btnRegisterTienda);
        denunciasList = (RecyclerView) view.findViewById(R.id.recyclerview);
        denunciasList.setLayoutManager(new LinearLayoutManager(getActivity()));

        denunciasList.setAdapter(new DenunciasAdapter(getActivity()));

        RegisterDenuncia.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CrearDenunciaActivity.class);
                startActivity(intent);
            }
        });

        initialize();

        return view;
    }

    private void initialize() {

        ApiService service = ApiServiceGenerator.createService(ApiService.class);

        Call<List<Denuncia>> call = service.getDenuncias();

        call.enqueue(new Callback<List<Denuncia>>() {
            @Override
            public void onResponse(Call<List<Denuncia>> call, Response<List<Denuncia>> response) {
                try {

                    int statusCode = response.code();
                    Log.d(TAG, "HTTP status code: " + statusCode);

                    if (response.isSuccessful()) {

                        List<Denuncia> denuncias = response.body();
                        Log.d(TAG, "items: " + denuncias);

                        DenunciasAdapter adapter = (DenunciasAdapter) denunciasList.getAdapter();
                        adapter.setDenuncias(denuncias);
                        adapter.notifyDataSetChanged();


                    } else {
                        Log.e(TAG, "onError: " + response.errorBody().string());
                        throw new Exception("Error en el servicio");
                    }

                } catch (Throwable t) {
                    try {
                        Log.e(TAG, "onThrowable: " + t.toString(), t);
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }catch (Throwable x){}
                }
            }

            @Override
            public void onFailure(Call<List<Denuncia>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }

}
