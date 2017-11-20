package app.roque.com.munidenuncias_v2.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import app.roque.com.munidenuncias_v2.R;
import app.roque.com.munidenuncias_v2.activity.DetalleDenunciaActivity;
import app.roque.com.munidenuncias_v2.models.Denuncia;
import app.roque.com.munidenuncias_v2.service.ApiService;

public class DenunciasAdapter extends RecyclerView.Adapter<DenunciasAdapter.ViewHolder> {

    private static final String TAG = DenunciasAdapter.class.getSimpleName();
    private List<Denuncia> denuncias;
    private Activity activity;

    public DenunciasAdapter(Activity activity){
        this.denuncias = new ArrayList<>();
        this.activity = activity;
    }

    public void setDenuncias(List<Denuncia> denuncias){
        this.denuncias = denuncias;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView image;
        public TextView titulo;
        public TextView autor;
        public TextView ubicacion;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.imagen_img);
            titulo = (TextView)itemView.findViewById(R.id.titulo_text);
            autor = (TextView) itemView.findViewById(R.id.autor_text);
            ubicacion = (TextView) itemView.findViewById(R.id.ubicacion_text);
        }
    }

    @Override
    public DenunciasAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_denuncias, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DenunciasAdapter.ViewHolder viewHolder, int position) {

        final Denuncia denuncia = this.denuncias.get(position);

        viewHolder.titulo.setText(denuncia.getTitulo());
        viewHolder.autor.setText("Por " + denuncia.getUsuario_id());
        viewHolder.ubicacion.setText(denuncia.getUbicacion());

        String url = ApiService.API_BASE_URL + "/images/" + denuncia.getImagen();
        Picasso.with(viewHolder.itemView.getContext()).load(url).into(viewHolder.image);


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, DetalleDenunciaActivity.class);
                intent.putExtra("ID", denuncia.getId());
                activity.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return this.denuncias.size();
    }

}
