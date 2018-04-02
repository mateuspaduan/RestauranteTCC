package inatel.com.br.restaurante.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

import inatel.com.br.restaurante.R;
import inatel.com.br.restaurante.controller.SharedPreferencesController;
import inatel.com.br.restaurante.model.Order;

public class ScanActivity extends AppCompatActivity {

    private FloatingActionButton exit;
    private FloatingActionButton scan;

    private SurfaceView cameraPreview;

    private TextView textQrCode;

    private String nameUser;
    private String timeOrder;
    private String orderResult;

    private BarcodeDetector barcodeDetector;

    private CameraSource cameraSource;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference orderReference = database.getReference("Pedidos");
    private DatabaseReference timeReference = database.getReference("Pratos");

    final int RequestCameraPermissionID = 1001;

    @Override
    public void onBackPressed() {

        cameraPreview.setVisibility(View.INVISIBLE);
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

        exit = (FloatingActionButton) findViewById(R.id.exit);
        scan = (FloatingActionButton) findViewById(R.id.scan);

        cameraPreview = (SurfaceView) findViewById(R.id.cameraPreview);
        textQrCode = (TextView) findViewById(R.id.textQrCode);

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

                    orderResult = qrcodes.valueAt(0).displayValue;
                    SharedPreferencesController.putString(getApplicationContext(), "orderResult", orderResult);

                    Intent i = new Intent(ScanActivity.this, OrderActivity.class);
                    startActivity(i);

                    finish();
                    //final Order order = new Order(qrcodes.valueAt(0).displayValue);
                    //orderReference.child("Mesa 01").setValue(order);



                    /*textQrCode.post(new Runnable() {
                        @Override
                        public void run() {

                            timeReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    timeOrder = dataSnapshot.child(order.getCustOrder()).getValue(String.class);
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            textQrCode.setVisibility(View.VISIBLE);
                            textQrCode.setText("O seu pedido vai demorar aproximadamente " + timeOrder);
                        }
                    });*/
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
            }
        });
    }
}
