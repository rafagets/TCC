package es.esy.rafaelsilva.tcc.activity;

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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.esy.rafaelsilva.tcc.DAO.UsuarioDao;
import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.adapters.Pesquisa;
import es.esy.rafaelsilva.tcc.controle.CtrlUsuario;
import es.esy.rafaelsilva.tcc.interfaces.CallbackListar;
import es.esy.rafaelsilva.tcc.interfaces.CallbackSalvar;
import es.esy.rafaelsilva.tcc.modelo.Usuario;
import es.esy.rafaelsilva.tcc.util.DadosUsuario;
import  static android.Manifest.permission.CAMERA;
import static  android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ListView listView;
    private List<Usuario> listaUsuarios;
    private Pesquisa adapter;
    private View fragmentCabecalho;
    private View fragmentCorpo;
    private RelativeLayout layoutHome;
    TextView txtUsuarioNome;
    TextView txtUsuarioEmail;
    private CircleImageView imgUsuario;
    ProgressDialog dialog;
    Bitmap img;
    UsuarioDao dao;
    String path;
    private final int MY_PERMISSIONS = 100;
    private final int FOTO_CAMERA = 200;
    private final int FOTO_SD = 300;
    private final String APP_DIRECTORIO = "myPictureApp/";
    private final String MEDIA_DIRECTORY = APP_DIRECTORIO + "media";
    private final String NOME_IMAGEM = DadosUsuario.getUsuario().getCodigo() + "_" + DadosUsuario.getUsuario().getNome()+".jpg";

    public CircleImageView getImgUsuario() {
        return imgUsuario;
    }

    public void setImgUsuario(CircleImageView imgUsuario) {
        this.imgUsuario = imgUsuario;
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

                Intent intent = new Intent(HomeActivity.this, HistoricoActivity.class);
                intent.putExtra("lote", String.valueOf(3));
                startActivity(intent);

//                QRCode qrCode = new QRCode(HomeActivity.this);
//                qrCode.lerQrcode();

//                Intent intent = new Intent(HomeActivity.this, QrcodeActivity.class);
//                startActivity(intent);

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


        imgUsuario.setOnClickListener(foto());


        if (DadosUsuario.getUsuario() != null) {
            txtUsuarioNome.setText(DadosUsuario.getUsuario().getNome());
            txtUsuarioEmail.setText(DadosUsuario.getUsuario().getEmail());
           if(carregarImgUsuario() > 1){
               imgUsuario.setImageResource(R.drawable.ic_account_circle_black_24dp);
           }
        }
        navigationView.setNavigationItemSelectedListener(this);

        fragmentCabecalho = findViewById(R.id.cabecalho_post);
        fragmentCorpo =  findViewById(R.id.corpo_home);

    }

    private View.OnClickListener salvarFotoBDLocal() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(img != null) {
                dialog = new ProgressDialog(HomeActivity.this);
                dialog.setMessage("Salvando, aguarde...");
                dialog.show();
//                Bitmap photo = (Bitmap) data.getExtras().get("data");
//                imageView.setImageBitmap(photo);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                img.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                    dao = new UsuarioDao(HomeActivity.this);
                   // long result = dao.updateImg(DadosUsuario.getUsuario().getCodigo(), byteArray);

                    //chamar a tela de login passando o usuario/email que acabou de cadastrar
                    //if (result > 0) {
                      //  dialog.dismiss();
                      //  Toast.makeText(HomeActivity.this, "Imagem salva no bd Local \uD83D\uDE09", Toast.LENGTH_LONG).show();
//                if (byteArray != null) {
//
//                    String campos = "codigo="+ DadosUsuario.getUsuario().getCodigo();
//                    String values = "";
//
//                    new CtrlUsuario(HomeActivity.this).salvarAtualizacao(values, campos, new CallbackSalvar() {
//
//                        @Override
//                        public void resultadoSalvar(Object obj) {
//                            dialog.dismiss();
//                            Resposta resposta = (Resposta) obj;
//                            if (resposta.isFlag()) {
//                                Toast.makeText(HomeActivity.this, "Dados atualizados com sucesso", Toast.LENGTH_LONG).show();
//                            } else {
//                                Toast.makeText(HomeActivity.this, "Falha ao atualizar usuario", Toast.LENGTH_LONG).show();
//                            }
//                        }
//
//                        @Override
//                        public void falha() {
//                            dialog.dismiss();
//                            Toast.makeText(HomeActivity.this, "Falha ao cadastrar usuario", Toast.LENGTH_LONG).show();
//                        }
//                    });
////            }else{
////                dialog.dismiss();
////                Toast.makeText(AtualizaCadastroUsuarioActivity.this, "Senhas diferentes. Verifique.", Toast.LENGTH_LONG).show();
////            }
//
//                } else {
//                    dialog.dismiss();
//                    Toast.makeText(HomeActivity.this, "Preencha todos os campos.", Toast.LENGTH_LONG).show();
//                }

                    //}else{
                    //    dialog.dismiss();
                     //   Toast.makeText(HomeActivity.this, "Erro ao tentar salvar imagem\nTente novamente!", Toast.LENGTH_LONG).show();
                   // }


                }else{
                    Toast.makeText(HomeActivity.this, "img vazio", Toast.LENGTH_LONG).show();

                }
            }
        };
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
        final CharSequence[] opcoes = {"ESCOLHER FOTO", "NOVA FOTO", "CANCELAR"};
        final AlertDialog.Builder  builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setTitle("Opções");
        builder.setItems(opcoes, new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int op) {
                if(opcoes[op] == "NOVA FOTO"){
                    abrirCamera();
                }else if(opcoes[op] == "ESCOLHER FOTO"){
                    selectFoto();
                }else if (opcoes[op] == "CANCELAR"){
                    dialogInterface.dismiss();
                }
            }

        });
        builder.show();
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
            path = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY +
                    File.separator + NOME_IMAGEM;

            File newFile = new File(path);

            Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            camIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));
            startActivityForResult(camIntent, FOTO_CAMERA);
        }
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
                    //salvarFotoBDLocal();
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
                    System.out.println("PATH: " + path);
                   //salvarFotoBDLocal();
                }
                break;
        }
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


                    if (listaUsuarios != null){
                        adapter = new Pesquisa(HomeActivity.this, listaUsuarios);
                        listView.setAdapter(adapter);
                        //arrayAdapter = new UsuarioAdapter(HomeActivity.this, android.R.layout.simple_list_item_1, listaUsuarios);
                        listView.setAdapter(adapter);

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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

            Intent intent = new Intent(HomeActivity.this, Welcome_Activity.class);
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }else if (id == R.id.nav_edit_user){
    Intent intent = new Intent(HomeActivity.this, AtualizaCadastroUsuarioActivity.class);
            startActivity(intent);

            //aqui abre a tela de cadastro de usuario
//            Intent intent = new Intent(HomeActivity.this, CadastroUsuarioActivity.class);
//            startActivity(intent);
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
    private boolean verificarPermissoes(){// incompleto
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            return true;
        }
        if((checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED )){
            return true;
        }

        if(shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE) || (shouldShowRequestPermissionRationale(CAMERA))){
            Snackbar.make(layoutHome, "Necessita de permissão no sistema para esta operação",
                    Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok, new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onClick(View view) {
                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
                }
            }).show();
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS){
            if(grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                                             grantResults[1] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(HomeActivity.this,"Permissões aceitas!", Toast.LENGTH_LONG).show();
            }else{
                explicação();
            }
        }
    }

    private void explicação() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setTitle("Solicitação de Permissão");
        builder.setMessage("Para utilizar algumas funções este app necessita de permissão.");
        builder.setPositiveButton("Permitir", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogI, int wich){
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);

            }
        });
        builder.setNegativeButton("Negar", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogI, int wich){
                dialogI.dismiss();
//                finish();
            }
        });
    }

    public void updateFoto(Bitmap bmp){
        new CtrlUsuario(this).atualizar(NOME_IMAGEM, "imagem", new CallbackSalvar() {
            @Override
            public void resultadoSalvar(Object obj) {

            }

            @Override
            public void falha() {

            }
        });
    }
}
