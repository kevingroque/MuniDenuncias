package app.roque.com.munidenuncias_v2.service;

import java.util.List;

import app.roque.com.munidenuncias_v2.models.Denuncia;
import app.roque.com.munidenuncias_v2.models.Usuario;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {

    String API_BASE_URL = "http://munidenuncias-kevinghanz.c9users.io/";

    @FormUrlEncoded
    @POST("/api/v1/login")
    Call<Usuario> loginUser
            (@Field("username")String username,
             @Field("password") String password
            );

    @GET("api/v1/usuarios")
    Call<List<Usuario>> getUsuarios();

    @FormUrlEncoded
    @POST("/api/v1/usuarios")
    Call<ResponseMessage> createUser(@Field("nombres") String nombre,
                                        @Field("correo") String correo,
                                        @Field("username") String username,
                                        @Field("password") String password);

    @Multipart
    @POST("/api/v1/usuarios")
    Call<ResponseMessage> createUserWithImage(
            @Part("username") RequestBody username,
            @Part("password") RequestBody password,
            @Part("nombres") RequestBody nombres,
            @Part("correo") RequestBody correo,
            @Part MultipartBody.Part imagen
    );

    @GET("api/v1/denuncias")
    Call<List<Denuncia>> getDenuncias();

    @GET("api/v1/denuncias/{id}")
    Call<Denuncia> showDenuncias(@Path("id") Integer id);

    @FormUrlEncoded
    @POST("/api/v1/denuncias")
    Call<ResponseMessage> createDenuncia(@Field("usuarios_id") String usuarios_id,
                                         @Field("titulo") String titulo,
                                         @Field("descripcion") String descripcion,
                                         @Field("ubicacion") String ubicacion,
                                         @Field("latitud") String latitud,
                                         @Field("longitud") String longitud
                                         );

    @Multipart
    @POST("/api/v1/denuncias")
    Call<ResponseMessage> createDenunciaWithImage(
            @Part("usuarios_id") RequestBody usuarios_id,
            @Part("titulo") RequestBody titulo,
            @Part("descripcion") RequestBody descripcion,
            @Part("ubicacion") RequestBody address,
            @Part("latitud") RequestBody latitud,
            @Part("longitud") RequestBody longitud,
            @Part MultipartBody.Part imagen
    );
}
