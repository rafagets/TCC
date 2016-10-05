package es.esy.rafaelsilva.tcc.DAO;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import es.esy.rafaelsilva.tcc.activity.HistoricoActivity;
import es.esy.rafaelsilva.tcc.activity.HomeActivity;

/**
 * Created by Rafael on 13/09/2016.
 */
public class QRCode extends AppCompatActivity {

    private Context contexto;
    private HomeActivity home;

    public QRCode(Context contexto) {
        this.contexto = contexto;
        this.home = ((HomeActivity) contexto);
    }

    public void lerQrcode(){

        final Activity activity = home;
        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Enquadre o QRCode na camera");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult resultado = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(resultado!= null){
            if(resultado.getContents() == null){

                Toast.makeText(contexto, "Leitura cancelada =(", Toast.LENGTH_SHORT).show();

            }else{

                Toast.makeText(contexto, "resultado: " + resultado.getContents(), Toast.LENGTH_SHORT).show();
                Log.d("QrcodeActyvity", "Resultado: " + resultado.getContents());

                Intent intent = new Intent(home, HistoricoActivity.class);
                intent.putExtra("lote", resultado.getContents());
                startActivity(intent);

            }

        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
