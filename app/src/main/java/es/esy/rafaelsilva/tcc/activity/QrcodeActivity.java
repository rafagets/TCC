package es.esy.rafaelsilva.tcc.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import es.esy.rafaelsilva.tcc.R;

public class QrcodeActivity extends AppCompatActivity {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        final Activity activity = this;
        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Enquadre o QRCode na camera");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();

//        button = (Button) findViewById(R.id.button);
//        final Activity activity = this;
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                IntentIntegrator integrator = new IntentIntegrator(activity);
//                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
//                integrator.setPrompt("Enquadre o QRCode na camera");
//                integrator.setCameraId(0);
//                integrator.setBeepEnabled(true);
//                integrator.setBarcodeImageEnabled(false);
//                integrator.initiateScan();
//            }
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult resultado = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(resultado!= null){
            if(resultado.getContents() == null){
                Log.d("QrcodeActyvity", "Scanner cancelado");
                Toast.makeText(this, "Leitura cancelada =(", Toast.LENGTH_SHORT).show();
            }else{
                Log.d("QrcodeActyvity", "Resultado: " + resultado.getContents());
                Toast.makeText(this, "Resultado " + resultado.getContents(), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(QrcodeActivity.this, HistoricoActivity.class);
                startActivity(intent);
            }

        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
