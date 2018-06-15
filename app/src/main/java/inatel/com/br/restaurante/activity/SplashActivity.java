package inatel.com.br.restaurante.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import inatel.com.br.restaurante.R;
import inatel.com.br.restaurante.controller.SharedPreferencesController;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;

    private RelativeLayout mProgressBar;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mProgressBar = (RelativeLayout) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                String user = SharedPreferencesController.getString(SplashActivity.this, "username");

                SharedPreferencesController.putFloat(SplashActivity.this, "vConta", 0);

                if (!user.equals("-1")) {
                    SharedPreferencesController.putString(SplashActivity.this, "logado", "true");
                    Intent i = new Intent(SplashActivity.this, WelcomeActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    SharedPreferencesController.putString(SplashActivity.this, "logado", "false");
                    Intent i = new Intent(SplashActivity.this, WelcomeActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }
}
