package es.esy.rafaelsilva.tcc.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.adapters.Pesquisa;
import es.esy.rafaelsilva.tcc.controle.CtrlUsuario;
import es.esy.rafaelsilva.tcc.interfaces.CallbackListar;
import es.esy.rafaelsilva.tcc.interfaces.CallbackSalvar;
import es.esy.rafaelsilva.tcc.modelo.Usuario;
import es.esy.rafaelsilva.tcc.util.DadosUsuario;
import es.esy.rafaelsilva.tcc.util.Resposta;

public class AtualizaCadastroUsuarioActivity extends AppCompatActivity {
    EditText txtNome;
    EditText txtSobrenome;
    EditText txtRua;
    EditText txtNumero;
    EditText txtCep;
    EditText txtDataNasc;
    EditText txtProfissao;
    Spinner alimentacao;
    Button btnAtualizar;
    ProgressDialog dialog;
    List<Usuario> listaUsuarios;
    private Pesquisa adapter;

    public static final int IMG_SDCARD = 1;
    public static final int IMG_CAM = 1;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atualiza_cadastro_usuario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Meus Dados");

        txtNome = (EditText) findViewById(R.id.txtNome);
        txtSobrenome = (EditText) findViewById(R.id.txtSobrenome);
        txtRua = (EditText) findViewById(R.id.txtRua);
        txtNumero = (EditText) findViewById(R.id.txtNumero);
        txtCep = (EditText) findViewById(R.id.txtCep);
        txtDataNasc = (EditText) findViewById(R.id.txtDataNasc);
        txtProfissao = (EditText) findViewById(R.id.txtProfissao);
        alimentacao = (Spinner) findViewById(R.id.spinner);
        btnAtualizar = (Button) findViewById(R.id.btnCadastrar);

        /*popula o spinner
        * os valores se encontram em um xml no endereço:
        * re/values/alimentacao*/
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.alimentacao_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        alimentacao.setAdapter(adapter);
        //carrega as informações do usuario
        new CtrlUsuario(this).listar("codigo='"+ DadosUsuario.getUsuario().getCodigo()+"'", new CallbackListar() {
            @Override
            public void resultadoListar(List<Object> lista) {
                listaUsuarios = new ArrayList<>();

                for (Object obj : lista) {
                    listaUsuarios.add((Usuario) obj);
                }


                if (listaUsuarios != null){

                    txtNome.setText(listaUsuarios.get(0).getNome());
                    txtSobrenome.setText(listaUsuarios.get(0).getSobrenome());
                    txtRua.setText(listaUsuarios.get(0).getRua());
                    txtNumero.setText(listaUsuarios.get(0).getNumero());
                    txtCep.setText(listaUsuarios.get(0).getCep());
                    txtDataNasc.setText(listaUsuarios.get(0).getDataNasc());
                    txtProfissao.setText(listaUsuarios.get(0).getProfissao());

                }

            }

            @Override
            public void falha() {

            }
        });




        btnAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                atualizarCadastro();
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void atualizarCadastro() {
        dialog = new ProgressDialog(AtualizaCadastroUsuarioActivity.this);
        dialog.setMessage("Atualizando, aguarde...");
        dialog.show();

        if (    !txtNome.getText().toString().equals("") ||
                !txtSobrenome.getText().toString().equals("") ||
                !txtRua.getText().toString().equals("")
                || !txtNumero.getText().toString().equals("") ||
                !txtCep.getText().toString().equals("") || !txtDataNasc.getText().toString().equals("") ||
                !txtProfissao.getText().toString().equals("")) {

            //if (txtConfirmSenha.getText().toString().equals(txtSenha.getText().toString())){

            String campos = "codigo='"+ listaUsuarios.get(0).getCodigo();
//                    "sobrenome, " +
//                    "rua, " +
//                    "numero, " +
//                    "cep, " +
//                    "dataNasc, " +
//                    "alimentacao";

            String values = "nome='" + txtNome.getText().toString() + "'," +
                    "sobrenome='" + txtSobrenome.getText().toString()+"'," +
                    "rua='" + txtRua.getText().toString() + "',"+
                    "numero='" + txtNumero.getText().toString() + "'," +
                    "cep='" + txtCep.getText().toString() + "'," +
                    "datanasc='" + txtDataNasc.getText().toString() + "'," +
                    "profissao='" + txtProfissao.getText().toString() + "'," +
                    "alimentacao='" + alimentacao.getSelectedItem().toString() + "'";

            new CtrlUsuario(AtualizaCadastroUsuarioActivity.this).salvarAtualizacao(values, campos, new CallbackSalvar() {

                @Override
                public void resultadoSalvar(Object obj) {
                    dialog.dismiss();
                    Resposta resposta = (Resposta) obj;
                    if (resposta.isFlag()) {
//                        Intent intent = new Intent(AtualizaCadastroUsuarioActivity.this, Login_Activity.class);
//                        intent.putExtra("email", txtEmail.getText().toString());
//                        startActivity(intent);
                    } else {
                        Toast.makeText(AtualizaCadastroUsuarioActivity.this, "Falha ao cadastrar usuario", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void falha() {
                    dialog.dismiss();
                    Toast.makeText(AtualizaCadastroUsuarioActivity.this, "Falha ao cadastrar usuario", Toast.LENGTH_LONG).show();
                }
            });
//            }else{
//                dialog.dismiss();
//                Toast.makeText(AtualizaCadastroUsuarioActivity.this, "Senhas diferentes. Verifique.", Toast.LENGTH_LONG).show();
//            }

        } else {
            dialog.dismiss();
            Toast.makeText(AtualizaCadastroUsuarioActivity.this, "Preencha todos os campos.", Toast.LENGTH_LONG).show();
        }
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

    private View.OnClickListener OpcoesImagem() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                escolhaImagem();
            }
        };
    }

    private void escolhaImagem() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //Titulo da janela
        builder.setTitle("Opções de Imagem do Perfil");
        //Conteudo da msg
        builder.setMessage("Para adicionar uma imagem no seu perfil\n basta escolher uma das opções abaixo!");

        builder.setPositiveButton("Imagem Existente", escolherImagem());
        builder.setNegativeButton("Nova Imagem", tirarFoto());

        //cria o alertDialog a partir do builder
        AlertDialog alert = builder.create();
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

    public void salvarImagem() {
        //aqui implementar a lógica de gravação da imagem no banco de dados
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == IMG_SDCARD) {
            if (resultCode == RESULT_OK) {
                Uri imgSelecionada = intent.getData();

                String[] colunas = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(imgSelecionada, colunas, null, null, null);
                cursor.moveToFirst();

                int indexColuna = cursor.getColumnIndex(colunas[0]);
                String pathImg = cursor.getString(indexColuna);

                Bitmap bitmap = BitmapFactory.decodeFile(pathImg);

                //imgUser.setImageBitmap(bitmap);

            }
        }
    }

    private void abrirCamera() {
        File file = new File(Environment.getExternalStorageDirectory(), "img.png");
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, IMG_CAM);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "AtualizaCadastroUsuario Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://es.esy.rafaelsilva.tcc.activity/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "AtualizaCadastroUsuario Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://es.esy.rafaelsilva.tcc.activity/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
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
