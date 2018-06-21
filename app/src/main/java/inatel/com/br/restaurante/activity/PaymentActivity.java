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
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import inatel.com.br.restaurante.R;
import inatel.com.br.restaurante.controller.SharedPreferencesController;
import inatel.com.br.restaurante.model.Payment;

public class PaymentActivity extends AppCompatActivity {

    private CheckBox checkBoxDinheiro;
    private CheckBox checkBoxCartao;
    private CheckBox checkBoxCheque;

    private TextView mTotalBill;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference paymentReference = database.getReference("Pagamentos");

    private Button button;

    private String payment = "0";

    private float totalBill;

    private String tableNumber;

    private boolean paid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        checkBoxDinheiro = (CheckBox) findViewById(R.id.checkBox);
        checkBoxCartao = (CheckBox) findViewById(R.id.checkBox2);
        checkBoxCheque = (CheckBox) findViewById(R.id.checkBox4);

        mTotalBill = (TextView) findViewById(R.id.textView4);

        button = (Button) findViewById(R.id.button3);

        totalBill = getIntent().getExtras().getFloat("totalBill");
        tableNumber = getIntent().getExtras().getString("tableNumber");

        mTotalBill.setText("Total da conta: R$" + String.format("%.2f", totalBill));

        if(checkBoxDinheiro.isChecked() == true){

            checkBoxCartao.setChecked(false);
            checkBoxCheque.setChecked(false);
        }

        if(checkBoxCartao.isChecked() == true){

            checkBoxDinheiro.setChecked(false);
            checkBoxCheque.setChecked(false);
        }

        if(checkBoxCheque.isChecked() == true){

            checkBoxCartao.setChecked(false);
            checkBoxDinheiro.setChecked(false);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkBoxDinheiro.isChecked() == true){

                    payment = "Dinheiro";
                }

                else if(checkBoxCartao.isChecked() == true){

                    payment = "Cartão";
                }

                else if(checkBoxCheque.isChecked() == true){

                    payment = "Cheque";
                }

                else Toast.makeText(PaymentActivity.this, "Favor selecionar o método de pagamento", Toast.LENGTH_SHORT).show();

                if(!payment.equals("0")){

                    //Cria objeto pagamento, sobe para o banco de dados com a referência do NÚMERO DA MESA e seta o valor da conta
                    Payment finalPayment = new Payment(payment, "R$"+String.format("%.2f", totalBill));
                    paymentReference.child(tableNumber).setValue(finalPayment);
                    Toast.makeText(getApplicationContext(), "Pedindo conta..", Toast.LENGTH_LONG).show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {

                            Intent i = new Intent(PaymentActivity.this, ScanActivity.class);
                            paid = true;
                            i.putExtra("paid",paid);
                            startActivity(i);

                            finish();
                        }
                    }, 5000);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {

        Intent i = new Intent(PaymentActivity.this, ScanActivity.class);
        startActivity(i);

        finish();
    }
}
