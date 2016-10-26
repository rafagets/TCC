package es.esy.rafaelsilva.tcc.activity;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
    private DataBase database;
    private SQLiteDatabase connection;
    private Button btnEntrar;
    EditText txtEmail, txtSenha;
    TextView lblRedefSenha;
    UsuarioDao dao;
    List<Usuario> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtSenha = (EditText) findViewById(R.id.txtSenha);
        lblRedefSenha = (TextView) findViewById(R.id.lblRedefinirSenha);
        lblRedefSenha.setOnLongClickListener(verMapa());
        if(getIntent().getStringExtra("email") != null){
            txtEmail.setText(getIntent().getStringExtra("email"));
        }
        if (DadosUsuario.getUsuarioCorrente() != null){
            txtEmail.setText(DadosUsuario.getUsuarioCorrente().getEmail());
        }


        //conexao com banco de dados local 
        try {
//            database = new DataBase(this);
//            connection = database.getWritableDatabase();
            if (getIntent().getStringExtra("tela") == null) {


                dao = new UsuarioDao(this);
                lista = dao.selecionarTodos();

                if (lista.size() > 0) {

                    for (Usuario usuario : lista) {
                        System.out.println("Codigo: "+usuario.getCodigo()+"\nNome: " + usuario.getNome() + "\nEmail: " + usuario.getEmail() + "\nSenha: " +
                                usuario.getSenha() + "\nProfissao: " + usuario.getProfissao() + "\nAlimentação: " + usuario.getAlimentacao());
                            DadosUsuario.setUsuarioCorrente(usuario);
// DadosUsuario.usuarioCorrente.setCodigo(usuario.getCodigo());
//                        DadosUsuario.usuarioCorrente.setNome(usuario.getNome());
//                        DadosUs

                    }

                    Intent intent = new Intent(Login_Activity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(Login_Activity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }



            //Toast.makeText(Login_Activity.this, "Conexão estabelecida!", Toast.LENGTH_LONG).show();

        }catch(SQLException ex){
            Toast.makeText(Login_Activity.this, "Conexão não estabelecida!", Toast.LENGTH_LONG).show();
        }

        btnEntrar = (Button) findViewById(R.id.btnEntrar);
        btnEntrar.setOnClickListener(entrar());
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

    private View.OnClickListener entrar() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                verificarUsuario();
//                Intent intent = new Intent(Login_Activity.this, HomeActivity.class);
//                startActivity(intent);

//                Intent intent = new Intent(Login_Activity.this, PerfilActivity.class);
//                intent.putExtra("usuario", 1);
//                startActivity(intent);
            }
        };
    }
    public void verificarUsuario(){
        new CtrlUsuario(Login_Activity.this).logar(txtEmail.getText().toString(), txtSenha.getText().toString(), new CallbackTrazer() {
            @Override
            public void resultadoTrazer(Object obj) {
                Usuario usuario = (Usuario) obj;
                dao = new UsuarioDao(Login_Activity.this);

                long result = dao.inserir(usuario);
                //chamar a tela de login passando o usuario/email que acabou de cadastrar
                if (result >= 0){
                    System.out.println("Resultado dao.inserir : " + result);
                    DadosUsuario.setUsuarioCorrente(usuario);
                    Intent intent = new Intent(Login_Activity.this, HomeActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void falha() {
                Toast.makeText(Login_Activity.this, "Email ou Senha Incorretos.\n Verifique os dados e tente novamente!", Toast.LENGTH_LONG).show();
            }
        });
    }

}
