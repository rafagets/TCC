package es.esy.rafaelsilva.tcc.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import es.esy.rafaelsilva.tcc.DAO.UsuarioDao;
import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.DAO.DataBase;
import es.esy.rafaelsilva.tcc.controle.CtrlUsuario;
import es.esy.rafaelsilva.tcc.interfaces.CallbackTrazer;
import es.esy.rafaelsilva.tcc.modelo.Usuario;
import es.esy.rafaelsilva.tcc.util.DadosUsuario;

public class Login_Activity extends AppCompatActivity {

    private Button btnEntrar;
    EditText txtEmail, txtSenha;
    TextView lblRedefSenha;
    UsuarioDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtSenha = (EditText) findViewById(R.id.txtSenha);
        lblRedefSenha = (TextView) findViewById(R.id.lblRedefinirSenha);
        lblRedefSenha.setOnLongClickListener(verMapa());

        if (getIntent().getStringExtra("email") != null) {
            txtEmail.setText(getIntent().getStringExtra("email"));

            AlertDialog.Builder mensagem = new AlertDialog.Builder(Login_Activity.this);
            mensagem.setTitle("Cadastro Realizado com sucesso!");
            mensagem.setMessage("Para sua maior segurança, digite novamente a senha que criou anteriormente.");

            mensagem.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            mensagem.show();
        }

        btnEntrar = (Button) findViewById(R.id.btnEntrar);
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verificarUsuario();;
            }
        });
    }

    private View.OnLongClickListener verMapa() {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(Login_Activity.this, Mapa_Activity.class);
                startActivity(intent);
                return false;
            }
        };
    }

    public void verificarUsuario(){

        new CtrlUsuario(Login_Activity.this).logar(txtEmail.getText().toString(), txtSenha.getText().toString(), new CallbackTrazer() {
            @Override
            public void resultadoTrazer(Object obj) {
                try {
                    Usuario usuario = (Usuario) obj;
                    // faz a inserção no Sql lite
                    dao = new UsuarioDao(Login_Activity.this);
                    long result = dao.inserir(usuario);

                    //chamar a tela de login passando o usuario/email que acabou de cadastrar
                    if (result > 0) {
                        DadosUsuario.setUsuarioCorrente(usuario);
                        Intent intent = new Intent(Login_Activity.this, HomeActivity.class);
                        startActivity(intent);

                        // finaliza a activity login;
                        finish();
                    }
                }catch (Exception e){
                    Toast.makeText(Login_Activity.this, "Email ou Senha Incorretos.\n Verifique os dados e tente novamente!", Toast.LENGTH_LONG).show();
                    // limpa o campo senha
                    txtSenha.setText("");
                }
            }

            @Override
            public void falha() {
                Toast.makeText(Login_Activity.this, "Email ou Senha Incorretos.\n Verifique os dados e tente novamente!", Toast.LENGTH_LONG).show();
                // limpa o campo senha
                txtSenha.setText("");
            }
        });

    }

}
