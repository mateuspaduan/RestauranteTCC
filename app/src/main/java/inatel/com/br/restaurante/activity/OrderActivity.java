package inatel.com.br.restaurante.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import inatel.com.br.restaurante.R;
import inatel.com.br.restaurante.controller.SharedPreferencesController;
import inatel.com.br.restaurante.model.Order;

import static android.R.attr.data;
import static android.R.attr.order;

public class OrderActivity extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference orderReference = database.getReference("Pedidos");

    DatabaseReference foodReference = database.getReference("Pratos");
    DatabaseReference drinkReference = database.getReference("Bebidas");
    DatabaseReference massReference = foodReference.child("Massas");
    DatabaseReference meatReference = foodReference.child("Carnes");
    DatabaseReference ordersReference = database.getReference("Pedidos");
    DatabaseReference tableReference = ordersReference.child("Mesa 01");

    DatabaseReference food;

    private String value;

    private String orderName;
    private String orderPrice;
    private String tableNumber;

    private TextView mTextView;

    private EditText editText;

    private Button mButton;

    private String mCommentary;

    private Order order;

    private int numberOrders = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        //imageView = (ImageView) findViewById(R.id.imgView);
        mTextView = (TextView) findViewById(R.id.textView);
        mTextView.setGravity(View.TEXT_ALIGNMENT_GRAVITY);

        editText = (EditText) findViewById(R.id.editText);

        mButton = (Button) findViewById(R.id.btn1);

        //Pega informações passadas pelo INTENT
        orderName = getIntent().getExtras().getString("orderName");
        orderPrice = getIntent().getExtras().getString("orderPrice");
        tableNumber = getIntent().getExtras().getString("tableNumber");
        numberOrders = getIntent().getExtras().getInt("orderSize");

        //productReference = imageReference.child(orderName + ".jpg");

        if(orderName.equals("Macarrao alho e oleo") || orderName.equals("Lasanha a Bolonhesa")){

            food = massReference;
            //Pega os valores que estão na referência food de acordo com o If/Else de cima
            food.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    //Pega o tempo do pedido de acordo com o nome dele
                    value = dataSnapshot.child(orderName).getValue(String.class);

                    //Informa usuário através de texto
                    mTextView.setText("Você pediu " + orderName + "," + " esse pedido vai demorar " + value +
                            " e vai custar R$" + orderPrice + ", deseja confirmar?");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        else if(orderName.equals("Picanha na Chapa com Fritas") || orderName.equals("Filet ao Molho Madeira")){

            food = meatReference;
            //Pega os valores que estão na referência food de acordo com o If/Else de cima
            food.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    //Pega o tempo do pedido de acordo com o nome dele
                    value = dataSnapshot.child(orderName).getValue(String.class);

                    //Informa usuário através de texto
                    mTextView.setText("Você pediu " + orderName + "," + " esse pedido vai demorar " + value +
                            " e vai custar R$" + orderPrice + ", deseja confirmar?");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        else if(orderName.equals("Suco natural de laranja") || orderName.equals("Coca-Cola") || orderName.equals("Cerveja")) {

            mTextView.setText("Você pediu " + orderName + "," + " e vai custar R$" + orderPrice + ", deseja confirmar?");
        }

        mButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

                //Pega comentário, SE HOUVER
                mCommentary = editText.getText().toString();

                order = new Order(orderName, orderPrice, mCommentary);

                //Cria referência dentro de Pedidos e coloca como chave o Datastamp do pedido
                orderReference.child(tableNumber).child("" + numberOrders).setValue(order);

                numberOrders = numberOrders + 1;

                //Cria Intent com as informações
                Intent i = new Intent(OrderActivity.this, ScanActivity.class);

                //Adiciona informações
                i.putExtra("orderName", orderName);
                i.putExtra("orderPrice", orderPrice);
                if (mCommentary != null) {
                    i.putExtra("orderCommentary", mCommentary);
                }

                //Seta resultado para outra tela receber informado que está tudo certo
                setResult(RESULT_OK, i);
                finish();
            }
        });
    }

}
