package es.esy.rafaelsilva.tcc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.util.DadosUsuario;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView txtUsuarioNome, txtUsuarioEmail;
    ImageView imgUsuarioCorrente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (DadosUsuario.getUsuarioCorrente() != null){
            setTitle("Olá "+ DadosUsuario.getUsuarioCorrente().getNome() + "!");
            Toast.makeText(this, "Codigo: "+ DadosUsuario.codigo +"\nNome: "+DadosUsuario.nome+"\nEmail: "+DadosUsuario.email, Toast.LENGTH_LONG).show();

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
        //Colocando o nome e emaildo usuario atual no menu lateral
        View view = navigationView.getHeaderView(0);
        txtUsuarioNome = (TextView) view.findViewById(R.id.txtUsuarioNome);
        txtUsuarioEmail = (TextView) view.findViewById(R.id.txtUsuarioEmail);
        imgUsuarioCorrente = (ImageView) view.findViewById(R.id.imageViewUsuarioCorrente);
        if (DadosUsuario.getUsuarioCorrente() != null) {
            txtUsuarioNome.setText(DadosUsuario.getUsuarioCorrente().getNome());
            txtUsuarioEmail.setText(DadosUsuario.getUsuarioCorrente().getEmail());
        }
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search){
            final SearchView searchViewAndroidActionBar = (SearchView) MenuItemCompat.getActionView(item);
            searchViewAndroidActionBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    searchViewAndroidActionBar.clearFocus();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
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

        }else if (id == R.id.nav_user){


            //aqui abre a tela de cadastro de usuario
            Intent intent = new Intent(HomeActivity.this, CadastroUsuarioActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
