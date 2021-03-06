package es.esy.rafaelsilva.tcc.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.location.places.NearbyAlertFilter;
import com.google.android.gms.plus.model.people.Person;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.esy.rafaelsilva.tcc.BuildConfig;
import es.esy.rafaelsilva.tcc.DAO.UsuarioDao;
import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.adapters.Pesquisa;
import es.esy.rafaelsilva.tcc.controle.CtrlUsuario;
import es.esy.rafaelsilva.tcc.interfaces.CallbackListar;
import es.esy.rafaelsilva.tcc.interfaces.CallbackSalvar;
import es.esy.rafaelsilva.tcc.modelo.Usuario;
import es.esy.rafaelsilva.tcc.util.DadosUsuario;
import es.esy.rafaelsilva.tcc.util.UploadDeImagens;

import  static android.Manifest.permission.CAMERA;
import static  android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ListView listView;
    private List<Usuario> listaUsuarios;
    private Pesquisa adapter;
    private View fragmentCabecalho;
    private View fragmentCorpo;
    private RelativeLayout layoutHome;
    private TextView txtUsuarioNome;
    private TextView txtUsuarioEmail;
    private CircleImageView imgUsuario;
    private CircleImageView imgUsuarioPrincipal;
    private UsuarioDao dao;
    private String path;
    private final int FOTO_CAMERA = 200;
    private final int FOTO_SD = 300;
    private final String APP_DIRECTORIO = "myPictureApp/";
    private final String MEDIA_DIRECTORY = APP_DIRECTORIO + "media";
    private final String NOME_IMAGEM = DadosUsuario.getUsuario().getCodigo() + "_.jpg";

    public CircleImageView getImgUsuarioPrincipal() {
        return imgUsuario;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        layoutHome = (RelativeLayout) findViewById(R.id.layoutHome);
        if (DadosUsuario.getUsuario() != null){
            setTitle("Olá " + DadosUsuario.getUsuario().getNome() + "!");
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent intent = new Intent(HomeActivity.this, HistoricoActivity.class);
//                intent.putExtra("lote", String.valueOf(4));
//                startActivity(intent);

                Intent intent = new Intent(HomeActivity.this, QrcodeActivity.class);
                startActivity(intent);

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //Colocando o nome e email do usuario atual no menu lateral
        View view = navigationView.getHeaderView(0);

        txtUsuarioNome = (TextView) view.findViewById(R.id.txtUsuarioNome);
        txtUsuarioEmail = (TextView) view.findViewById(R.id.txtUsuarioEmail);
        imgUsuario = (CircleImageView) view.findViewById(R.id.imageViewUsuarioCorrente);
        imgUsuarioPrincipal = (CircleImageView) findViewById(R.id.imgUsuarioPrincipal);


        imgUsuario.setOnClickListener(foto());


        if (DadosUsuario.getUsuario() != null) {
            txtUsuarioNome.setText(DadosUsuario.getUsuario().getNome());
            txtUsuarioEmail.setText(DadosUsuario.getUsuario().getEmail());
           if(carregarImgUsuario() > 1){
               imgUsuario.setImageResource(R.drawable.ic_usuario);
           }
        }
        navigationView.setNavigationItemSelectedListener(this);

        fragmentCabecalho = findViewById(R.id.cabecalho_post);
        fragmentCorpo =  findViewById(R.id.corpo_home);

    }

    private View.OnClickListener foto() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showOptions();
            }
        };

    }
    public void showOptions(){
        if (this.verificaPermissao()) {
            final CharSequence[] opcoes = {"ESCOLHER FOTO", "NOVA FOTO", "CANCELAR"};
            final AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
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
    private void selectFoto() {
        Intent galeriaIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galeriaIntent.setType("image/*");
        startActivityForResult(galeriaIntent.createChooser(galeriaIntent, "SELECIONA APP DE IMAGEM"), FOTO_SD);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("file_path", path);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        path = savedInstanceState.getString("file_path");
    }

    @Override
    protected void onActivityResult(int requestCode, int result, Intent data){

        switch (requestCode){
            case FOTO_CAMERA:
                if (result == RESULT_OK){
                    String dir = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY +
                            File.separator + NOME_IMAGEM;
                    System.out.println("NOME IMG: " + dir);
                    decodeBitMap(dir);
                    dao = new UsuarioDao(this);
                    if(dao.updateImg(DadosUsuario.getUsuario().getCodigo(), dir, 0) > 0){
                        Toast.makeText(this, "SALVO NO SQLITE", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(this, "NAO SALVO NO SQLITE", Toast.LENGTH_LONG).show();
                    }

                    File imgFile = new  File(dir);
                    if(imgFile.exists()){
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        imgUsuarioPrincipal.setImageBitmap(myBitmap);
                    }

                    upload(dir);
                }
                break;
            case FOTO_SD:
                if (result == RESULT_OK){
                    Uri path = data.getData();
                    if(dao.updateImg(DadosUsuario.getUsuario().getCodigo(), String.valueOf(path), 1) > 0 ){
                        Toast.makeText(this, "SALVO NO SQLITE", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(this, "NAO SALVO NO SQLITE", Toast.LENGTH_LONG).show();
                    }
                    imgUsuario.setImageURI(path);
                    imgUsuarioPrincipal.setImageURI(path);
                    System.out.println("PATH: " + path);

                    upload(getPastaUrl(path));
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

    private int decodeURI(String uri){
        int x = 1;
        Uri path = Uri.parse(uri);
        imgUsuario.setImageURI(path);
        return x;
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder mensagem = new AlertDialog.Builder(this);
        mensagem.setTitle("Não vá!");
        mensagem.setMessage("Mas se quiser realmente sair \nfique á vontade \uD83D\uDC4D");

        mensagem.setPositiveButton("Permanecer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(HomeActivity.this, "\uD83D\uDE09", Toast.LENGTH_SHORT).show();
            }
        });

        mensagem.setNegativeButton("Sair", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        mensagem.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search){
            listarUsuarios(item);
        }
        return super.onOptionsItemSelected(item);
    }

    private void listarUsuarios(final MenuItem item) {
        listView = (ListView) findViewById(R.id.listView);

        if (listaUsuarios != null) {
            adapter = new Pesquisa(HomeActivity.this, listaUsuarios);
            listView.setAdapter(adapter);
            //arrayAdapter = new UsuarioAdapter(HomeActivity.this, android.R.layout.simple_list_item_1, listaUsuarios);
            listView.setAdapter(adapter);
        }

        new CtrlUsuario(this).listar("", new CallbackListar() {
            @Override
            public void resultadoListar(List<Object> lista) {
                listaUsuarios = new ArrayList<>();
                for (Object obj : lista) {
                    listaUsuarios.add((Usuario) obj);
                }

                if (listaUsuarios != null){
                    adapter = new Pesquisa(HomeActivity.this, listaUsuarios);
                    listView.setAdapter(adapter);
                    //arrayAdapter = new UsuarioAdapter(HomeActivity.this, android.R.layout.simple_list_item_1, listaUsuarios);
                    listView.setAdapter(adapter);

                    listView.setVisibility(View.VISIBLE);
                    fragmentCabecalho.setVisibility(View.GONE);
                    fragmentCorpo.setVisibility(View.GONE);

                    monitorarPesquisa(item);
                }
            }
            @Override
            public void falha() {

            }
        });
    }

    /*Aqui é onde a grande magica da pesquisa acontece!*/
    private void monitorarPesquisa(MenuItem item){
        final SearchView searchViewAndroidActionBar = (SearchView) MenuItemCompat.getActionView(item);
        searchViewAndroidActionBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchViewAndroidActionBar.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                /*Atravez do adapter criado especialmente para esse fim
                * o componente pega os cliques do teclado e manda para
                * o adapter filtara as opçoes dinamicamente */
                adapter.getFilter().filter(newText);
                return false;
            }

        });

        searchViewAndroidActionBar.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }
        });

        /*Aqui verifica se o usuario iniciou ou saiu da busca
        * assim, utiliza-se manipulaçoes de visibilidade das view*/
        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                listView.setVisibility(View.GONE);
                fragmentCabecalho.setVisibility(View.VISIBLE);
                fragmentCorpo.setVisibility(View.VISIBLE);
                return true;
            }
        });

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_pesq_prod) {
            Intent intent = new Intent(HomeActivity.this, ListarProdutoActivity.class);
            startActivity(intent);

        }else if (id == R.id.nav_edit_user) {
            Intent intent = new Intent(HomeActivity.this, AtualizaCadastroUsuarioActivity.class);
            startActivity(intent);

        }else if(id == R.id.nav_logout){
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("error", true);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public int carregarImgUsuario(){
        int ret = 2;
        dao = new UsuarioDao(this);
        String dire = dao.loadImg();
        System.out.println("DIRETÓRIO: " + dire);
        if(DadosUsuario.getUsuario().getTipoImg() == 0){
            ret = decodeBitMap(dire);
        }else if (DadosUsuario.getUsuario().getTipoImg() == 1){
            ret = decodeURI(dire);
        }
        return ret;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                if (grantResults.length == 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)  {
                    // Usuário aceitou a permissão!
                    showOptions();
                } else {
                    // Usuário negou a permissão.
                    // Não podemos utilizar esta funcionalidade.
                    Toast.makeText(this,"Permissão negada", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void upload(final String path){
        new CtrlUsuario(this).fotoPerfil(path, NOME_IMAGEM, new CallbackSalvar() {
            @Override
            public void resultadoSalvar(Object obj) {
                Toast.makeText(HomeActivity.this, "Enviando imagem...", Toast.LENGTH_LONG).show();
            }

            @Override
            public void falha() {
                Toast.makeText(HomeActivity.this, "Falha ao enviar", Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean verificaPermissao(){
        // Se não possui permissão
        if (ContextCompat.checkSelfPermission(this,WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Verifica se já mostramos o alerta e o usuário negou na 1ª vez.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Caso o usuário tenha negado a permissão anteriormente, e não tenha marcado o check "nunca mais mostre este alerta"
                // Podemos mostrar um alerta explicando para o usuário porque a permissão é importante.
                return false;
            } else {
                // Solicita a permissão
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},0);
            }
        } else {
            return true;
        }

        return true;
    }
}
