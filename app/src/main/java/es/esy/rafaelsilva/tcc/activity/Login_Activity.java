package es.esy.rafaelsilva.tcc.activity;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.dao.DataBase;

public class Login_Activity extends AppCompatActivity {
    private DataBase database;
    private SQLiteDatabase connection;
    private Button btnEntrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //conexao com banco de dados local
        try {
            database = new DataBase(this);
            connection = database.getWritableDatabase();
            Toast.makeText(Login_Activity.this, "Conexão estabelecida!", Toast.LENGTH_LONG).show();
        }catch(SQLException ex){
            Toast.makeText(Login_Activity.this, "Conexão não estabelecida!", Toast.LENGTH_LONG).show();
        }

        btnEntrar = (Button) findViewById(R.id.btnEntrar);
        btnEntrar.setOnClickListener(entrar());
    }

    private View.OnClickListener entrar() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login_Activity.this, HomeActivity.class);
                startActivity(intent);
            }
        };
    }
}
