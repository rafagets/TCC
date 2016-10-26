package es.esy.rafaelsilva.tcc.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.DAO.UsuarioDao;
import es.esy.rafaelsilva.tcc.controle.CtrlUsuario;
import es.esy.rafaelsilva.tcc.interfaces.CallbackSalvar;
import es.esy.rafaelsilva.tcc.modelo.Usuario;
import es.esy.rafaelsilva.tcc.util.DadosUsuario;
import es.esy.rafaelsilva.tcc.util.Resposta;

public class CadastroUsuarioActivity extends AppCompatActivity {
    private FloatingActionButton fabCadastrarUsuario;
    EditText txtNome;
    EditText txtEmail;
    EditText txtSenha;
    EditText txtConfirmSenha;
    EditText txtProfissao;
    CheckBox chkCarne, chkVegano, chkVegetariano;
    String alimentacao = "";
    CircleImageView imgUser;
    Usuario usuario;
    UsuarioDao usuarioDao;
    DadosUsuario userCurrent;
    public static final int IMG_SDCARD = 1;
    public static final int IMG_CAM = 1;

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
        chkCarne = (CheckBox) findViewById(R.id.chkCarnivoro);
        chkVegano = (CheckBox) findViewById(R.id.chkVegano);
        chkVegetariano = (CheckBox) findViewById(R.id.chkVegetariano);
        imgUser = (CircleImageView) findViewById(R.id.imgUser);
        //imgUser.setOnClickListener(OpcoesImagem());
        imgUser.setOnClickListener(OpcoesImagem());

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

                    if (alimentacao.substring(alimentacao.length() -1).equals(",")) {
                        alimentacao = alimentacao.substring(0, alimentacao.length() - 1);
                    }
                    //

                    if (txtConfirmSenha.getText().toString().equals(txtSenha.getText().toString())){
                        //txtNome.setText(txtNome.getText().toString().substring(0).toUpperCase()).toString().substring(0) =
                        //verifica no bd (webservice) se o usuario existe, caso nao exista já cadastra
//                        UtilTask thread = new UtilTask(CadastroUsuarioActivity.this, "C", "usuario");
                        String campos = "nome, email, senha, profissao, alimentacao";//colocar nome dos campos
                        String values = "'" + txtNome.getText().toString() + "','" + txtEmail.getText().toString() + "','" + txtSenha.getText().toString() + "','"
                                + txtProfissao.getText().toString() + "','" + alimentacao + "'";
                        new CtrlUsuario(CadastroUsuarioActivity.this).salvar(values, campos, new CallbackSalvar() {
                            @Override
                            public void resultadoSalvar(Object obj) {

                                Resposta resposta = (Resposta) obj;

                                Intent intent = new Intent(CadastroUsuarioActivity.this, Login_Activity.class);
                                intent.putExtra("email",txtEmail.getText().toString());
                                startActivity(intent);
                            }

                            @Override
                            public void falha() {
                                    Toast.makeText(CadastroUsuarioActivity.this, "Falha ao cadastrar usuario",Toast.LENGTH_LONG).show();
                            }
                        });
//                        thread.execute(campos, values);
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



    private View.OnClickListener OpcoesImagem() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                escolhaImagem();
            }
        };
    }

//    private View.OnClickListener O)pcoesImagem() {
//        return new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                escolhaImagem();
//            }
//        };
//    }
    private void escolhaImagem() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);

        //Titulo da janela
        builder.setTitle("Opções de Imagem do Perfil");
        //Conteudo da msg
        builder.setMessage("Para adicionar uma imagem no seu perfil\n basta escolher uma das opções abaixo!");

        builder.setPositiveButton("Imagem Existente",escolherImagem());
        builder.setNegativeButton("Nova Imagem",tirarFoto());

        //cria o alertDialog a partir do builder
        android.app.AlertDialog alert = builder.create();
        alert.show();
    }

    private DialogInterface.OnClickListener tirarFoto() {
       // Toast.makeText(this, "Implementar acesso a Camera", Toast.LENGTH_SHORT).show();
            abrirCamera();
     return null;
    }



    private DialogInterface.OnClickListener escolherImagem() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, IMG_SDCARD);

       // Toast.makeText(this, "Implementar ao micro sd\n e memoria interna Storage", Toast.LENGTH_SHORT).show();
        return null;
    }

    public void salvarImagem(){
        //aqui implementar a lógica de gravação da imagem no banco de dados
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
    if (requestCode == IMG_SDCARD){
            if (resultCode == RESULT_OK){
                Uri imgSelecionada = intent.getData();

                String[] colunas = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(imgSelecionada, colunas, null, null, null);
                cursor.moveToFirst();

                int indexColuna = cursor.getColumnIndex(colunas[0]);
                String pathImg = cursor.getString(indexColuna);

                Bitmap bitmap = BitmapFactory.decodeFile(pathImg);

                imgUser.setImageBitmap(bitmap);

            }
        }
    }
    private void abrirCamera() {
        File file = new File(android.os.Environment.getExternalStorageDirectory(), "img.png");
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, IMG_CAM);
    }
    /*	// CALL IN CAM
		public void callIntentImgCam(View view){

		}

	// IMG IN SDCARD
		public void callIntentImgSDCard(View view){
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");
			startActivityForResult(intent, IMG_SDCARD);
		}

	// IMG IN SDCARD
		public void sendData(View view){
			enableViews(false);

			wd.setUrl("http://www.villopim.com.br/android/ExemploSendImageForPhp/ctrl/CtrlForm.php");
			wd.setMethod("save-form");
			wd.setName(etName.getText().toString());
			wd.setEmail(etEmail.getText().toString());
			wd.setAge(Integer.parseInt(etAge.getText().toString()));

			new Thread(){
				public void run(){
					answer = HttpConnection.getSetDataWeb(wd);

					runOnUiThread(new Runnable(){
						public void run(){
							enableViews(true);
							try{
								answer = Integer.parseInt(answer) == 1 ? "Form enviado com sucesso!" : "FAIL!";
								Toast.makeText(MainActivity.this, answer, Toast.LENGTH_SHORT).show();
							}
							catch(NumberFormatException e){ e.printStackTrace(); }
						}
					});
				}
			}.start();
		}

	public void enableViews(boolean status){
		btTakePhoto.setEnabled(status);
		btSdcardPhoto.setEnabled(status);
		btSend.setEnabled(status);
		etName.setEnabled(status);
		etEmail.setEnabled(status);
		etAge.setEnabled(status);

		btSend.setText(status ? "Enviar" : "Enviando...");
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		File file = null;

		if(data != null && requestCode == IMG_SDCARD && resultCode == RESULT_OK){
			Uri img = data.getData();
			String[] cols = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(img, cols, null, null, null);
			cursor.moveToFirst();

			int indexCol = cursor.getColumnIndex(cols[0]);
			String imgString = cursor.getString(indexCol);
			cursor.close();

			file = new File(imgString);
			if(file != null){
				wd.getImage().setResizedBitmap(file, 300, 300);
				wd.getImage().setMimeFromImgPath(file.getPath());
			}
		}
		else if(requestCode == IMG_CAM && resultCode == RESULT_OK){
			file = new File(android.os.Environment.getExternalStorageDirectory(), "img.png");
			if(file != null){
				wd.getImage().setResizedBitmap(file, 300, 300);
				wd.getImage().setMimeFromImgPath(file.getPath());
			}
		}


		if(wd.getImage().getBitmap() != null){
			imageView.setImageBitmap(wd.getImage().getBitmap());
		}
	}*/


}
