package es.esy.rafaelsilva.tcc.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.util.List;

import es.esy.rafaelsilva.tcc.DAO.UsuarioDao;
import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.fragment.BoasVindas;
import es.esy.rafaelsilva.tcc.fragment.BoasVindasAmizades;
import es.esy.rafaelsilva.tcc.fragment.BoasVindasEscolha;
import es.esy.rafaelsilva.tcc.fragment.BoasVindasEtapas;
import es.esy.rafaelsilva.tcc.modelo.Usuario;
import es.esy.rafaelsilva.tcc.util.DadosUsuario;

public class MainActivity extends AppCompatActivity {
    Button btnCadUser, btnLogin;
    UsuarioDao dao;
    List<Usuario> lista;
    private fragemnt mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        btnCadUser = (Button) findViewById(R.id.btnCadastrar);
        btnCadUser.setOnClickListener(cadastrarNovoUsuario());
        btnLogin = (Button) findViewById(R.id.btnAcessar);
        btnLogin.setOnClickListener(acessar());

        // caso entre aqui, ocorreu algum erro ao autenticar o usuario
        // com ele ja logado no sistema.
        if (getIntent().getBooleanExtra("error", false)){
            dao = new UsuarioDao(this);
            dao.excluirTudo();
        }

        /*faz a conexao com o banco de dados sql lite
        * se retornar algum valor ele abre a activity correspondente ao resultado
        * se re retornar um resultado, ja tem um usuario logado, e então abre a home
        * senão pede pra fazer o login*/

        // caso não tenha um email, a intenção é um novo cadastro
        if (getIntent().getStringExtra("email") == null || getIntent().getBooleanExtra("flag", false)) {
            if (getIntent().getBooleanExtra("flag", true)) {
                dao = new UsuarioDao(this);
                lista = dao.selecionarTodos();
                if (lista.size() == 1) {

                    for (Usuario usuario : lista) {
                        DadosUsuario.setUsuarioCorrente(usuario);
                    }

                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();

                }
            }
        }

        //inicializo o page adapter
        mSectionsPagerAdapter = new fragemnt(getSupportFragmentManager());
        // Inicio a view pager e seto o adpter
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }

    private View.OnClickListener acessar() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Login_Activity.class);
                intent.putExtra("flag", true);
                startActivity(intent);

            }
        };
    }

    private View.OnClickListener cadastrarNovoUsuario() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CadastroUsuarioActivity.class);
                startActivity(intent);
            }
        };
    }


    public class fragemnt extends FragmentPagerAdapter {

        public fragemnt(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0: return BoasVindas.newInstance(position);
                case 1: return BoasVindasAmizades.newInstance(position);
                case 2: return BoasVindasEtapas.newInstance(position);
                case 3: return BoasVindasEscolha.newInstance(position);
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

    }
}
