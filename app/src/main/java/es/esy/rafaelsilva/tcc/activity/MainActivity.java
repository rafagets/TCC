package es.esy.rafaelsilva.tcc.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import es.esy.rafaelsilva.tcc.R;

public class MainActivity extends AppCompatActivity {
    Button btnCadUser, btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCadUser = (Button) findViewById(R.id.btnCadastrar);
        btnCadUser.setOnClickListener(cadastrarNovoUsuario());
        btnLogin = (Button) findViewById(R.id.btnAcessar);
        btnLogin.setOnClickListener(acessar());
    }

    private View.OnClickListener acessar() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Login_Activity.class);
                intent.putExtra("tela", "main");
                startActivity(intent);

            }
        };
    }

    private View.OnClickListener cadastrarNovoUsuario() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CadastroUsuarioActivity.class);
                startActivity(intent);
            }
        };
    }
}
