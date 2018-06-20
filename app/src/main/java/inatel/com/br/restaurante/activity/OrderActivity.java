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

import static android.R.attr.order;

public class OrderActivity extends AppCompatActivity {

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    StorageReference imageReference = storageReference.child("images");
    StorageReference productReference;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference orderReference = database.getReference("Pedidos");

    DatabaseReference foodReference = database.getReference("Pratos");
    DatabaseReference massReference = foodReference.child("Massas");
    DatabaseReference meatReference = foodReference.child("Carnes");
    DatabaseReference ordersReference = database.getReference("Pedidos");
    DatabaseReference tableReference = ordersReference.child("Mesa 01");

    DatabaseReference food;

    private Uri mImageUri;

    private String value;

    //private ImageView imageView;

    private String orderName;
    private String orderPrice;
    private String tableNumber;

    private Order order;

    private TextView mTextView;

    private EditText editText;

    private Button mButton;

    private float mBill;
    private float mTotal = 0;

    private float mOtherTotal;

    private String mCommentary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        //imageView = (ImageView) findViewById(R.id.imgView);
        mTextView = (TextView) findViewById(R.id.textView);
        mTextView.setGravity(View.TEXT_ALIGNMENT_GRAVITY);

        editText = (EditText) findViewById(R.id.editText);

        mButton = (Button) findViewById(R.id.btn1);

        orderName = getIntent().getExtras().getString("orderName");
        orderPrice = getIntent().getExtras().getString("orderPrice");
        tableNumber = getIntent().getExtras().getString("tableNumber");

        mTotal = SharedPreferencesController.getFloat(this, "vConta");

        //productReference = imageReference.child(orderName + ".jpg");

        if(orderName.equals("Macarrao") || orderName.equals("Lasanha")){

            food = massReference;
        }

        else if(orderName.equals("Picanha") || orderName.equals("Alcatra")){

            food = meatReference;
        }

        food.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                value = dataSnapshot.child(orderName).getValue(String.class);

                mTextView.setText("Você pediu " + orderName + "," + " esse pedido vai demorar " + value +
                        " e vai custar R$" + orderPrice + ", deseja confirmar?");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        mButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

                mCommentary = editText.getText().toString();

                orderReference.child(tableNumber).child(Calendar.getInstance().getTime().toString()).setValue(order);

                Intent i = new Intent(OrderActivity.this, ScanActivity.class);
                i.putExtra("orderName", orderName);
                i.putExtra("orderPrice", orderPrice);
                if (mCommentary != null) {
                    i.putExtra("orderCommentary", mCommentary);
                }
                setResult(RESULT_OK, i);
                finish();
            }
        });
    }

}
