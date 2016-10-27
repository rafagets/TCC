package es.esy.rafaelsilva.tcc.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.controle.CtrlAmigos;
import es.esy.rafaelsilva.tcc.controle.CtrlAvaliacao;
import es.esy.rafaelsilva.tcc.controle.CtrlUsuario;
import es.esy.rafaelsilva.tcc.fragment.PerfilAmigos;
import es.esy.rafaelsilva.tcc.fragment.PerfilAtividade;
import es.esy.rafaelsilva.tcc.fragment.PerfilSobre;
import es.esy.rafaelsilva.tcc.interfaces.CallbackTrazer;
import es.esy.rafaelsilva.tcc.modelo.Post;
import es.esy.rafaelsilva.tcc.modelo.Usuario;
import es.esy.rafaelsilva.tcc.task.ImageLoaderTask;
import es.esy.rafaelsilva.tcc.util.Config;
import es.esy.rafaelsilva.tcc.util.DadosUsuario;
import es.esy.rafaelsilva.tcc.util.Resposta;

public class PerfilActivity extends AppCompatActivity {

    // dados usuario logado
    private Usuario usuario;
    private TextView nome, profissao, estilo, totalAmigos, totalAvaliacoes;
    CircleImageView imgUsuario;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        nome = (TextView) findViewById(R.id.lbNome);
        profissao = (TextView) findViewById(R.id.lbProfissao);
        estilo = (TextView) findViewById(R.id.lbEstilo);
        totalAmigos = (TextView) findViewById(R.id.lbTotalAmigos);
        totalAvaliacoes = (TextView) findViewById(R.id.lbTotalAvaliacoes);
        imgUsuario = (CircleImageView) findViewById(R.id.imgUsuario);

        if (getIntent().getIntExtra("usuario", 0) == 0)
            getUsuario(DadosUsuario.codigo);
        else
            getUsuario(getIntent().getIntExtra("usuario", 0));

      FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
      fab.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                      .setAction("Action", null).show();
          }
      });

    }



    // seta os dados principais do usuario alvo
    private void getUsuario(final int usu) {

        new CtrlUsuario(this).trazer(usu, new CallbackTrazer() {
            @Override
            public void resultadoTrazer(Object obj) {
                usuario = (Usuario) obj;
                setTotalAvaliacoes();
            }

            @Override
            public void falha() {
                Toast.makeText(PerfilActivity.this,"Erro ao carregar usuario", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void setTotalAvaliacoes() {
        new CtrlAvaliacao(this).contar("usuario = " + usuario.getCodigo(), new CallbackTrazer() {
            @Override
            public void resultadoTrazer(Object obj) {
                Resposta resp = (Resposta) obj;
                totalAvaliacoes.setText(String.valueOf(resp.getValor()));
                setTotalAmigos();
            }

            @Override
            public void falha() {
                totalAvaliacoes.setText("0");
                setTotalAmigos();
            }
        });
    }

    private void setTotalAmigos() {

        new CtrlAmigos(this).contar("amigoAdd = " + usuario.getCodigo(), new CallbackTrazer() {
            @Override
            public void resultadoTrazer(Object obj) {
                Resposta resp = (Resposta) obj;
                totalAmigos.setText(String.valueOf(resp.getValor()));
                setDadosUsuario();
            }

            @Override
            public void falha() {
                totalAmigos.setText("0");
                setDadosUsuario();
            }
        });

    }

    private void setDadosUsuario(){
        setTitle(usuario.getNome());
        nome.setText(usuario.getNome());
        profissao.setText(usuario.getProfissao());
        estilo.setText(usuario.getAlimentacao());
        usuario.setImagemPerfil(imgUsuario);
    }
    // *****************************************



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_perfil, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position){
                case 0: return PerfilSobre.newInstance(position);
                case 1: return PerfilAtividade.newInstance(position);
                case 2: return PerfilAmigos.newInstance(position);
            }
            return null;
            //return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SOBRE";
                case 1:
                    return "ATIVIDADE";
                case 2:
                    return "AMIGOS";
            }
            return null;
        }
    }

}
