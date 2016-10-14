package es.esy.rafaelsilva.tcc.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.DAO.UsuarioDao;
import es.esy.rafaelsilva.tcc.modelo.Usuario;
import es.esy.rafaelsilva.tcc.task.UtilTask;
import es.esy.rafaelsilva.tcc.util.DadosUsuario;

public class CadastroUsuarioActivity extends AppCompatActivity {
    private FloatingActionButton fabCadastrarUsuario;
    EditText txtEmail;
    EditText txtSenha;
    EditText txtConfirmSenha;
    EditText txtProfissao;
    EditText txtAlimentacao;
//    CircleImageView imgUser;
    Usuario usuario;
    UsuarioDao usuarioDao;
    DadosUsuario userCurrent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fabCadastrarUsuario = (FloatingActionButton) findViewById(R.id.fabCadastrarUsuario);
        fabCadastrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                imgUser = (CircleImageView) findViewById(R.id.imgUsuario);
                txtEmail        =   (EditText) findViewById(R.id.txtEmail);
                txtSenha        =   (EditText) findViewById(R.id.txtSenha);
                txtConfirmSenha =   (EditText) findViewById(R.id.txtConfirmSenha);
                txtProfissao    =   (EditText) findViewById(R.id.txtProfissao);
                txtAlimentacao  =   (EditText) findViewById(R.id.txtAlimentacao);

//force push


                //verifica no bd se o usuario existe
                UtilTask thread = new UtilTask(CadastroUsuarioActivity.this, "C", "usuario");
                String campos = "email, senha, proficao, alimentacao";//colocar nome dos campos
                String values = "'" + txtEmail.getText().toString() + "','" + txtSenha.getText().toString() + "','"
                        + txtProfissao.getText().toString() + "','" + txtAlimentacao.getText().toString() + "'";
                System.out.println("valores" +values);
                thread.execute(campos, values);


//                usuario = new Usuario();
//
//                usuario.setEmail(txtEmail.getText().toString());
//                usuario.setSenha(txtSenha.getText().toString());
//                usuario.setProfissao(txtProfissao.getText().toString());
//                usuario.setAlimentacao(txtAlimentacao.getText().toString());
//                UtilTask util = new UtilTask(
//                        es.esy.rafaelsilva.tcc.activity.CadastroUsuarioActivity.this,  // contexto
//                        "C",                // acão
//                        "usuario"       // tabela que sera salva
//                );
//                util.execute(c.nomes(), c.valores());
//                if (usuarioDao.inserir(usuario) >= 0)
//                    Toast.makeText(es.esy.rafaelsilva.tcc.activity.CadastroUsuarioActivity.this, "Cadastrado com Sucesso!", Toast.LENGTH_LONG).show();
//                 //= txtEmail.getText().toString();
//
//                else
//                    Toast.makeText(es.esy.rafaelsilva.tcc.activity.CadastroUsuarioActivity.this, "Erro ao tentar cadastrar produto", Toast.LENGTH_LONG).show();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                       .setAction("Action", null).show();
            }
        });
    }

}
