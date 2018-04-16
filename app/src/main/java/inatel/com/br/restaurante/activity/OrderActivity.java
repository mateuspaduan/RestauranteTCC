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
    DatabaseReference ordersReference = database.getReference("Pedidos");
    DatabaseReference tableReference = ordersReference.child("Mesa 01");

    private Uri mImageUri;

    private String value;

    //private ImageView imageView;

    private String orderName;
    private String orderPrice;

    private Order order;

    private TextView mTextView;

    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        //imageView = (ImageView) findViewById(R.id.imgView);
        mTextView = (TextView) findViewById(R.id.textView);
        mTextView.setGravity(View.TEXT_ALIGNMENT_GRAVITY);

        mButton = (Button) findViewById(R.id.btn1);

        orderName = SharedPreferencesController.getString(this, "orderName");
        orderPrice = SharedPreferencesController.getString(this, "orderPrice");
        productReference = imageReference.child(orderName + ".jpg");

        foodReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                value = dataSnapshot.child(orderName).getValue(String.class);
                //productReference = imageReference.child(beaconChild + ".jpg");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mTextView.setText("Você pediu " + orderName + "," + " esse pedido vai demorar " + value +
        " minutos e vai custar R$" + orderPrice + ", deseja confirmar?");

        mButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

                order = new Order(orderName,orderPrice);
                orderReference.child("Mesa 01").setValue(order);

                Intent i = new Intent(OrderActivity.this, ScanActivity.class);
                startActivity(i);
            }
        });

        /*productReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.i("Uri address: ", uri.toString());
                mImageUri = uri;
                Glide.with(getApplicationContext()).load(mImageUri).override(40,40).into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });*/
    }
}
