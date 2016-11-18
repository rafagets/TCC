package es.esy.rafaelsilva.tcc.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import es.esy.rafaelsilva.tcc.DAO.UsuarioDao;
import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.controle.CtrlUsuario;
import es.esy.rafaelsilva.tcc.interfaces.CallbackTrazer;
import es.esy.rafaelsilva.tcc.modelo.Usuario;
import es.esy.rafaelsilva.tcc.util.DadosUsuario;

public class Login_Activity extends AppCompatActivity {

    EditText txtEmail, txtSenha;
    UsuarioDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Entrar");

        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtSenha = (EditText) findViewById(R.id.txtSenha);

        if (getIntent().getStringExtra("email") != null) {
            txtEmail.setText(getIntent().getStringExtra("email"));

            AlertDialog.Builder mensagem = new AlertDialog.Builder(Login_Activity.this);
            mensagem.setTitle("Cadastro Realizado com sucesso!");
            mensagem.setMessage("Para sua maior segurança, digite novamente a senha que criou anteriormente.");

            mensagem.setNeutralButton("Ok, vamos lá!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            mensagem.show();
        }

        Button btnEntrar = (Button) findViewById(R.id.btnEntrar);
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtEmail.getText().toString().equals("") || txtSenha.getText().toString().equals("")) {
                    Toast.makeText(Login_Activity.this, "Por fovaor preencha todos os dados", Toast.LENGTH_LONG).show();
                }else {
                    verificarUsuario();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { // manipula o menu back
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
                    e.printStackTrace();
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
