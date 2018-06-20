package inatel.com.br.restaurante.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import inatel.com.br.restaurante.R;
import inatel.com.br.restaurante.adapter.OrderAdapter;
import inatel.com.br.restaurante.controller.DataHandlerController;
import inatel.com.br.restaurante.controller.SharedPreferencesController;
import inatel.com.br.restaurante.model.Order;

public class ScanActivity extends AppCompatActivity {

    private FloatingActionButton exit;
    private FloatingActionButton scan;
    private FloatingActionButton check;

    private SurfaceView cameraPreview;

    private TextView textQrCode;
    private TextView mBillText;

    private TextView mFeedback;

    private String orderName;
    private String orderPrice;
    private String tableNumber;

    private String nameUser;
    private String timeOrder;
    private String orderObject;

    private JSONObject jsonResult;

    private BarcodeDetector barcodeDetector;

    private CameraSource cameraSource;

    private float mTotalBill = 0;

    private boolean readed = false;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference orderReference = database.getReference("Pedidos");
    private DatabaseReference timeReference = database.getReference("Pratos");

    final int RequestCameraPermissionID = 1001;

    private String cityName;

    private ArrayList<Order> listOffers = new ArrayList<Order>();

    OrderAdapter adapter;

    ListView lView;

    boolean confirmed = false;

    private DataHandlerController mHandler;

    @Override
    public void onBackPressed() {

        cameraPreview.setVisibility(View.INVISIBLE);
        textQrCode.setVisibility(View.VISIBLE);
        scan.setVisibility(View.VISIBLE);
        exit.setVisibility(View.VISIBLE);
        check.setVisibility(View.VISIBLE);
        mFeedback.setVisibility(View.VISIBLE);
        mBillText.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {

            case RequestCameraPermissionID: {

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    try {
                        cameraSource.start(cameraPreview.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        cityName = SharedPreferencesController.getString(this, "cityName");

        exit = (FloatingActionButton) findViewById(R.id.exit);
        scan = (FloatingActionButton) findViewById(R.id.scan);
        check = (FloatingActionButton) findViewById(R.id.check);

        cameraPreview = (SurfaceView) findViewById(R.id.cameraPreview);
        textQrCode = (TextView) findViewById(R.id.textQrCode);

        textQrCode.setText("Seja bem-vindo ao nosso restaurante em " + cityName + "! Clique no botão de scan de QRCode para fazer o seu pedido!!");

        mBillText = (TextView) findViewById(R.id.textBill);

        mFeedback = (TextView) findViewById(R.id.textFeedback);

        lView = (ListView) findViewById(R.id.my_list);
        adapter = new OrderAdapter(ScanActivity.this, listOffers);

        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector).setRequestedPreviewSize(640, 500).build();

        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ScanActivity.this, new String[]{Manifest.permission.CAMERA},
                            RequestCameraPermissionID);
                    return;
                }
                try {
                    cameraSource.start(cameraPreview.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {

                final SparseArray<Barcode> qrcodes = detections.getDetectedItems();
                if(qrcodes.size() != 0){

                    if (!readed) {
                        orderObject = qrcodes.valueAt(0).displayValue;

                        try {
                            jsonResult = new JSONObject(orderObject);
                            orderName = jsonResult.getJSONObject("data").getString("nome");
                            orderPrice = jsonResult.getJSONObject("data").getString("preco");
                            tableNumber = jsonResult.getJSONObject("data").getString("mesa");

                            Intent i = new Intent(ScanActivity.this, OrderActivity.class);

                            i.putExtra("orderName", orderName);
                            i.putExtra("orderPrice", orderPrice);
                            i.putExtra("tableNumber", tableNumber);

                            readed = true;

                            startActivityForResult(i, 1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        });

        mFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ScanActivity.this, FeedBackActivity.class);
                startActivity(i);

                finish();
            }
        });

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(SharedPreferencesController.getFloat(ScanActivity.this, "vConta") == 0){

                    Toast.makeText(ScanActivity.this, "Você ainda não realizou nenhum pedido", Toast.LENGTH_SHORT).show();
                }

                else{

                    Intent i = new Intent(ScanActivity.this, PaymentActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });


        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferencesController.putString(ScanActivity.this, "username", "-1");
                SharedPreferencesController.putString(ScanActivity.this, "password", "-1");

                Intent i = new Intent(ScanActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cameraPreview.setVisibility(View.VISIBLE);
                textQrCode.setVisibility(View.INVISIBLE);
                scan.setVisibility(View.INVISIBLE);
                exit.setVisibility(View.INVISIBLE);
                check.setVisibility(View.INVISIBLE);
                mFeedback.setVisibility(View.INVISIBLE);
                mBillText.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        readed = false;
        cameraPreview.setVisibility(View.INVISIBLE);
        textQrCode.setVisibility(View.VISIBLE);
        scan.setVisibility(View.VISIBLE);
        exit.setVisibility(View.VISIBLE);
        check.setVisibility(View.VISIBLE);
        mFeedback.setVisibility(View.VISIBLE);
        mBillText.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        cameraPreview.setVisibility(View.INVISIBLE);

        if (resultCode == ScanActivity.RESULT_OK && requestCode == 1) {
            String name = data.getStringExtra("orderName");
            String value = data.getStringExtra("orderPrice");
            String commentary = data.getStringExtra("orderCommentary");
            Order order;

            readed = false;

            float price = Float.parseFloat(value.replace(',', '.'));

            mTotalBill = mTotalBill + price;

            mBillText.setText("Total da conta: R$" + String.format("%.2f", mTotalBill));

            if (commentary != null) {
                order = new Order(name, value, commentary);
            } else {
                order = new Order(name, value, "");
            }

            listOffers.add(order);
            lView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
}
