package inatel.com.br.restaurante.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import inatel.com.br.restaurante.R;
import inatel.com.br.restaurante.controller.SharedPreferencesController;

public class LoginActivity extends AppCompatActivity {

    private Button mLoginBtn;

    private EditText mEmail;
    private EditText mPassword;

    private TextView mRegister;

    private RelativeLayout mProgressBar;

    private CheckBox mKeepLogged;

    private String mTextEmail;
    private String mTextPassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FacebookSdk.sdkInitialize(getApplicationContext()); //Busca SDK do Facebook p/ login
        AppEventsLogger.activateApp(this);

        mAuth = FirebaseAuth.getInstance(); //Pega instância de autenticação do banco

        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);

        mRegister = (TextView) findViewById(R.id.register);
        mLoginBtn = (Button) findViewById(R.id.loginButton);

        mKeepLogged = (CheckBox) findViewById(R.id.keep_logged);

        mProgressBar = (RelativeLayout) findViewById(R.id.progressBar);

        mProgressBar.setVisibility(View.GONE); //Instancia barra de loading

        mRegister.setOnClickListener(new View.OnClickListener(){ //Seta listener de click para o texto de cadastro

            @Override
            public void onClick(View v){ //Quando clica

                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                mProgressBar.setVisibility(View.VISIBLE); //Deixa visivel a barra de loadin

                mTextEmail = mEmail.getText().toString(); //Pega o texto no campo
                mTextPassword = mPassword.getText().toString();

                //Função para realizar o login utilizando o Firebase, passando email e senha

                mAuth.signInWithEmailAndPassword(mTextEmail, mTextPassword).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            // there was an error
                            Toast.makeText(LoginActivity.this, "Algum erro ocorreu.", Toast.LENGTH_LONG).show();
                        } else {
                            if (mKeepLogged.isChecked()) { //Se o manter logado está marcado
                                SharedPreferencesController.putString(LoginActivity.this, "logado", "true");
                                SharedPreferencesController.putString(LoginActivity.this, "username", mTextEmail);
                                SharedPreferencesController.putString(LoginActivity.this, "password", mTextPassword);
                            }
                            Intent intent = new Intent(LoginActivity.this, ScanActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        mProgressBar.setVisibility(View.GONE); //Deixa invisivel a barra de loading
                    }
                });
            }
        });
    }
}

