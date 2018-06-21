package inatel.com.br.restaurante.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import inatel.com.br.restaurante.R;
import inatel.com.br.restaurante.controller.SharedPreferencesController;

public class WelcomeActivity extends AppCompatActivity {

    private JSONObject jsonResult;

    private BarcodeDetector barcodeDetector;

    private CameraSource cameraSource;
    private SurfaceView cameraPreview;

    final int RequestCameraPermissionID = 1001;

    private String cityObject;
    private String cityName;

    private String loginStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //Cria todos os elementos da tela
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        cameraPreview = (SurfaceView) findViewById(R.id.cameraPreview); //Associa elementos do Layout XML no código Java

        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector).setRequestedPreviewSize(640, 500).build(); //Seta propriedades do detector, tipo, etc

        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() { //Chama função para abrir câmera
            public void surfaceCreated(SurfaceHolder holder) {

                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) { //Check de permissão para uso da câmera
                    ActivityCompat.requestPermissions(WelcomeActivity.this, new String[]{Manifest.permission.CAMERA},
                            RequestCameraPermissionID);
                    return;
                }
                try {
                    cameraSource.start(cameraPreview.getHolder()); //Se concede a permissão, abre a câmera
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

                cameraSource.stop(); //Mata a instânica se fechar a tela
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) { //Callback quando é detectado algum padrão

                final SparseArray<Barcode> qrcodes = detections.getDetectedItems(); //Cria array para informações detectadas
                if(qrcodes.size() != 0){ //Se o array não for nulo

                    cityObject = qrcodes.valueAt(0).displayValue; //Pega primeiro elemento do vetor (primeira info detectada)

                    try {
                      /*{
                        "data":{
	                        "nome":"Macarrao",
	                        "preco":"R$25,00",
	                        "mesa":"Mesa 01"
	                        }
                        }*/
                        jsonResult = new JSONObject(cityObject); //Cria objeto JSON com a String detectada com os dados
                        cityName = jsonResult.getJSONObject("data").getString("nomecidade");//Capta informação do JSON
                        SharedPreferencesController.putString(WelcomeActivity.this, "cityName", cityName); //Coloca no banco geral do Android para usar em outra tela
                        //Log.d("Json", jsonResult.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    loginStatus = SharedPreferencesController.getString(WelcomeActivity.this, "logado");  //Busca info de logado

                    if(loginStatus.equals("true")){ //Se estiver logado, chama tela principal

                        //Cria um Intent, um objeto para pegar o contexto de uma Activity e informar para onde o contexto deve ser levado
                        //No caso, estamos em WelcomeActivity, e estamos indo para o ScanActivity

                        Intent i = new Intent(WelcomeActivity.this, ScanActivity.class);
                        startActivity(i);

                        finish();
                    }

                    else { //Senão, tela de login

                        Intent i = new Intent(WelcomeActivity.this, LoginActivity.class);
                        startActivity(i);

                        finish();
                    }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) { //Callback para check de permissão de câmera

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
}
