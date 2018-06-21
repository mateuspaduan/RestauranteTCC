package inatel.com.br.restaurante.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import inatel.com.br.restaurante.R;
import inatel.com.br.restaurante.controller.SharedPreferencesController;
import inatel.com.br.restaurante.model.Feedback;

public class FeedBackActivity extends AppCompatActivity {

    private EditText textFeedback;

    private Button sendFeedback;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    private DatabaseReference mFeedbackReference = database.getReference("Feedbacks");

    private String mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        textFeedback = (EditText) findViewById(R.id.editText);
        sendFeedback = (Button) findViewById(R.id.button2);

        //Pega email do usuário para criar o objeto informando quem foi que deu o Feedback
        mEmail = SharedPreferencesController.getString(this, "username");

        sendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Feedback não está vazio?
                if(textFeedback!=null){

                    //Cria objeto Feedback, sobe para o banco, espera um com a barra de loading e volta para a tela principal
                    Feedback feedback = new Feedback(mEmail, textFeedback.getText().toString());
                    mFeedbackReference.child(Calendar.getInstance().getTime().toString()).setValue(feedback);
                    Toast.makeText(getApplicationContext(), "Enviando feedback..", Toast.LENGTH_LONG).show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {

                            Intent i = new Intent(FeedBackActivity.this, ScanActivity.class);
                            startActivity(i);

                            finish();
                        }
                    }, 5000);
                }
            }
        });
    }

    //Volta para a tela principal
    @Override
    public void onBackPressed() {

        Intent i = new Intent(FeedBackActivity.this, ScanActivity.class);
        startActivity(i);

        finish();
    }
}
