package app.roque.com.munidenuncias_v2.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import app.roque.com.munidenuncias_v2.R;
import app.roque.com.munidenuncias_v2.models.Usuario;
import app.roque.com.munidenuncias_v2.service.ApiService;
import app.roque.com.munidenuncias_v2.service.ApiServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = CrearCuentaActivity.class.getSimpleName();
    private Button btnLogin;
    private Button btnGoRegister;
    private EditText inputUsername;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputUsername = (EditText) findViewById(R.id.username_input);
        inputPassword = (EditText) findViewById(R.id.password_input);
        btnLogin = (Button) findViewById(R.id.login_button);
        btnGoRegister = (Button) findViewById(R.id.goRegister_button);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // init SharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // username remember
        String username = sharedPreferences.getString("username", null);
        if(username != null){
            inputUsername.setText(username);
            inputPassword.requestFocus();
        }

        // islogged remember
        if(sharedPreferences.getBoolean("islogged", false)){
            // Go to Dashboard
            goDashboard();
        }


        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String username = inputUsername.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Check for empty data in the form
                if (!username.isEmpty() && !password.isEmpty()) {
                    // login user
                    checkLogin();

                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Campos incompletos!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        // Link to Register Screen
        btnGoRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        CrearCuentaActivity.class);
                startActivity(i);
            }
        });

    }

    private void checkLogin() {

        final String username = inputUsername.getText().toString();
        String password = inputPassword.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Debe completar todo los campos", Toast.LENGTH_SHORT).show();
        }

        ApiService service = ApiServiceGenerator.createService(ApiService.class);
        Call<Usuario> call = null;
        call = service.loginUser(username, password);

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                try {
                    int statusCode = response.code();
                    Log.d(TAG, "HTTP status code: " + statusCode);
                    if (response.isSuccessful()) {
                        Usuario responseMessage = response.body();
                        hideDialog();
                        assert responseMessage !=null;
                        Integer usuario_id = responseMessage.getId();
                        String nombres = responseMessage.getNombres();
                        String correo = responseMessage.getCorreo();
                        String imagen = responseMessage.getImagen();

                        Usuario.getInstance().setId(responseMessage.getId());
                        Usuario.getInstance().setUsername(responseMessage.getUsername());
                        Usuario.getInstance().setPassword(responseMessage.getPassword());
                        Usuario.getInstance().setNombres(responseMessage.getNombres());
                        Usuario.getInstance().setCorreo(responseMessage.getCorreo());
                        Usuario.getInstance().setImagen(responseMessage.getImagen());

                        Log.d(TAG, "responseMessage: " + responseMessage);
                        Log.d(TAG, "correo: " + correo);
                        Log.d(TAG, "usuario_id: " + usuario_id);
                        Log.d(TAG, "imagen: " + imagen);
                        // Save to SharedPreferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        boolean success = editor
                                .putInt("usuario_id", usuario_id)
                                .putString("username", username)
                                .putString("nombres", nombres)
                                .putString("correo", correo)
                                .putString("imagen", imagen)
                                .putBoolean("islogged", true)
                                .commit();
                        // Go to Dashboard


                        goDashboard();
                    } else {
                        //progressDialog.dismiss();
                        Log.e(TAG, "onError: " + response.errorBody().string());
                        Toast.makeText(LoginActivity.this, "Correo o contraseña incorrectos!", Toast.LENGTH_SHORT).show();
                        //throw new Exception();
                    }

                } catch (Throwable t) {
                    try {
                        Log.e(TAG, "onThrowable: " + t.toString(), t);
                        hideDialog();
                        Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    } catch (Throwable x) {
                    }
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
                hideDialog();
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });

    }

    private void showDialog(View view) {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private  void goDashboard(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
