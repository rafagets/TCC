package es.esy.rafaelsilva.tcc.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import es.esy.rafaelsilva.tcc.BuildConfig;
import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.controle.CtrlGaleriaImgUsuario;
import es.esy.rafaelsilva.tcc.controle.CtrlUsuario;
import es.esy.rafaelsilva.tcc.interfaces.CallbackSalvar;
import es.esy.rafaelsilva.tcc.interfaces.CallbackTrazer;
import es.esy.rafaelsilva.tcc.modelo.Usuario;
import es.esy.rafaelsilva.tcc.util.DadosUsuario;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class PostarFotoActivity extends AppCompatActivity {
    private ImageView imgPost;
    private TextView txtPost, nome;
    private CircleImageView imgUsuario;
    private Spinner privacidade;
    private Usuario usuario;

    private String path;
    private final String APP_DIRECTORIO = "myPictureApp/";
    private final String MEDIA_DIRECTORY = APP_DIRECTORIO + "media";
    private final int FOTO_CAMERA = 200;
    private final int FOTO_SD = 300;
    private final String NOME_IMAGEM = DadosUsuario.getUsuario().getCodigo() + "_.jpg"; // mudar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(es.esy.rafaelsilva.tcc.R.layout.activity_postar_foto);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Postar Foto");

        imgPost = (ImageView) findViewById(R.id.imgPost);
        txtPost = (TextView) findViewById(R.id.txtPost);
        nome = (TextView) findViewById(R.id.txtNomeUsuario);
        imgUsuario = (CircleImageView) findViewById(R.id.imgUsuario);
        privacidade = (Spinner) findViewById(R.id.status);


        String[] status = new String[] {"Público","Amigos","Privado"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.estilo_spinner, status);
        privacidade.setAdapter(adapter);

        imgPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOptions();
            }
        });

        carregarUsuario();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (path != null){
                    upload(path);
                }else{
                    Toast.makeText(PostarFotoActivity.this,"Escolha uma foto", Toast.LENGTH_LONG).show();
                }
            }
        });
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

    private void carregarUsuario() {
        new CtrlUsuario(this).trazer(DadosUsuario.codigo, new CallbackTrazer() {
            @Override
            public void resultadoTrazer(Object obj) {
                usuario = (Usuario) obj;
                nome.setText(usuario.getNome());
                usuario.setImagemPerfil(imgUsuario, PostarFotoActivity.this);
            }

            @Override
            public void falha() {
                Toast.makeText(PostarFotoActivity.this, "Ocorreu uma falha\nTente novamente mais tarde", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }


    public void showOptions(){
        if (this.verificaPermissao()) {
            final CharSequence[] opcoes = {"ESCOLHER FOTO", "NOVA FOTO", "CANCELAR"};
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Opções");
            builder.setItems(opcoes, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int op) {
                    if (opcoes[op] == "NOVA FOTO") {
                        abrirCamera();
                    } else if (opcoes[op] == "ESCOLHER FOTO") {
                        selectFoto();
                    } else if (opcoes[op] == "CANCELAR") {
                        dialogInterface.dismiss();
                    }
                }

            });
            builder.show();
        }else{
            Toast.makeText(this,"Permissão negada", Toast.LENGTH_LONG).show();
        }
    }

    private boolean verificaPermissao(){
        // Se não possui permissão
        if (ContextCompat.checkSelfPermission(this,WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Verifica se já mostramos o alerta e o usuário negou na 1ª vez.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Caso o usuário tenha negado a permissão anteriormente, e não tenha marcado o check "nunca mais mostre este alerta"
                // Podemos mostrar um alerta explicando para o usuário porque a permissão é importante.
                return false;
            } else {
                // Solicita a permissão
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA},0);
            }
        } else {
            return true;
        }

        return true;
    }

    private void abrirCamera() {
        File file = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean dirCreated = file.exists();
        if (!dirCreated) {
            dirCreated = file.mkdirs();
        }
        if(dirCreated) {
            Uri uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", criarImagem());
            Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            camIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(camIntent, FOTO_CAMERA);
        }
    }

    private File criarImagem(){
        path = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY +
                File.separator + NOME_IMAGEM;

        return new File(path);
    }

    private void selectFoto() {
        Intent galeriaIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galeriaIntent.setType("image/*");
        startActivityForResult(galeriaIntent.createChooser(galeriaIntent, "SELECIONA APP DE IMAGEM"), FOTO_SD);
    }


    @Override
    protected void onActivityResult(int requestCode, int result, Intent data){

        switch (requestCode){
            case FOTO_CAMERA:
                if (result == RESULT_OK){
                    String dir = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY +
                            File.separator + NOME_IMAGEM;
                    decodeBitMap(dir);

                    File imgFile = new  File(dir);
                    if(imgFile.exists()){
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        imgPost.setImageBitmap(myBitmap);
                    }

                    //imgPost.setImageResource();
                    //upload(dir);
                    this.path = dir;
                }
                break;
            case FOTO_SD:
                if (result == RESULT_OK){
                    Uri path = data.getData();
                    imgPost.setImageURI(path);

                    //upload(getPastaUrl(path));
                    this.path = getPastaUrl(path);
                }
                break;
        }
    }

    public String getPastaUrl(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    private int decodeBitMap(String dir) {
        int x = 0;
        Bitmap bitMap;
        bitMap = BitmapFactory.decodeFile(dir);
        imgUsuario.setImageBitmap(bitMap);
        //updateFoto(bitMap);
        return x;
    }



    private void upload(final String path){
        int status = privacidade.getSelectedItemPosition() + 1;
        new CtrlGaleriaImgUsuario(this).postar(status, txtPost.getText().toString(), path, new CallbackSalvar() {
            @Override
            public void resultadoSalvar(Object obj) {
                Toast.makeText(PostarFotoActivity.this,"Fazendo upload", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void falha() {
                Toast.makeText(PostarFotoActivity.this,"Falha ao postar", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

}
