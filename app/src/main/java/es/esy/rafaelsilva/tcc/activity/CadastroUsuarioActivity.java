package es.esy.rafaelsilva.tcc.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.DAO.UsuarioDao;
import es.esy.rafaelsilva.tcc.modelo.Usuario;
import es.esy.rafaelsilva.tcc.task.UtilTask;
import es.esy.rafaelsilva.tcc.util.DadosUsuario;

public class CadastroUsuarioActivity extends AppCompatActivity {
    private FloatingActionButton fabCadastrarUsuario;
    EditText txtNome;
    EditText txtEmail;
    EditText txtSenha;
    EditText txtConfirmSenha;
    EditText txtProfissao;
    EditText txtAlimentacao;
    CheckBox chkCarne, chkVegano, chkVegetariano;
    String alimentacao = "";
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
        txtNome         =   (EditText) findViewById(R.id.txtNome);
        txtEmail        =   (EditText) findViewById(R.id.txtEmail);
        txtSenha        =   (EditText) findViewById(R.id.txtSenha);
        txtConfirmSenha =   (EditText) findViewById(R.id.txtConfirmSenha);
        txtProfissao    =   (EditText) findViewById(R.id.txtProfissao);
        //txtAlimentacao  =   (EditText) findViewById(R.id.txtAlimentacao);
        chkCarne = (CheckBox) findViewById(R.id.chkCarnivoro);
        chkVegano = (CheckBox) findViewById(R.id.chkVegano);
        chkVegetariano = (CheckBox) findViewById(R.id.chkVegetariano);

        fabCadastrarUsuario = (FloatingActionButton) findViewById(R.id.fabCadastrarUsuario);
        fabCadastrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                imgUser = (CircleImageView) findViewById(R.id.imgUsuario);
                if(!txtNome.getText().toString().equals("") || !txtEmail.getText().toString().equals("") || !txtSenha.getText().toString().equals("")
                        || !txtConfirmSenha.getText().toString().equals("") || !txtProfissao.getText().toString().equals("")){

                    //monto a string alimentacao
                    if (!chkVegetariano.isChecked() && !chkCarne.isChecked() && !chkVegano.isChecked()){
                        Toast.makeText(CadastroUsuarioActivity.this, "Informe qual sua alimentação!\ne Tente novamente", Toast.LENGTH_LONG).show();
                        return;
                    }
                    alimentacao = "";
                    if (chkCarne.isChecked()) {
                        alimentacao = alimentacao + chkCarne.getText().toString().toLowerCase() + ",";
                    }
                    if (chkVegano.isChecked()) {
                        alimentacao = alimentacao + chkVegano.getText().toString().toLowerCase() + ",";
                    }
                    if (chkVegetariano.isChecked()) {
                        alimentacao = alimentacao + chkVegetariano.getText().toString().toLowerCase() + ",";
                    }
                    System.out.println("variavel alimentacao antes de ficar pronta "+ alimentacao);
                    System.out.println("tamanho: " + alimentacao.length());
                    System.out.println("ultimo caracter: " + alimentacao.substring(alimentacao.length()-1));
                    if (alimentacao.substring(alimentacao.length() -1).equals(",")) {
                        alimentacao = alimentacao.substring(0, alimentacao.length() - 1);
                        System.out.println("variavel alimentacao pronta "+ alimentacao);
                    }
                    //

                    if (txtConfirmSenha.getText().toString().equals(txtSenha.getText().toString())){
                        //verifica no bd se o usuario existe, caso nao exista já cadastra
                        UtilTask thread = new UtilTask(CadastroUsuarioActivity.this, "C", "usuario");
                        String campos = "nome, email, senha, profissao, alimentacao";//colocar nome dos campos
                        String values = "'" + txtNome.getText().toString() + "','" + txtEmail.getText().toString() + "','" + txtSenha.getText().toString() + "','"
                                + txtProfissao.getText().toString() + "','" + alimentacao + "'";
                        System.out.println("valores" + values);
                        thread.execute(campos, values);
                    }else{
                        Toast.makeText(CadastroUsuarioActivity.this, "Confirmação da senha não coincidecom a senha!!!", Toast.LENGTH_LONG).show();
                    }

                }else{
                    Snackbar.make(view, "Verifique se preencheu todas informações", Snackbar.LENGTH_LONG)
                       .setAction("Action", null).show();
                }


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
