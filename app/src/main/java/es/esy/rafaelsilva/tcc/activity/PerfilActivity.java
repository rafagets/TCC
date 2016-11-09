package es.esy.rafaelsilva.tcc.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.controle.CtrlAmigos;
import es.esy.rafaelsilva.tcc.controle.CtrlAvaliacao;
import es.esy.rafaelsilva.tcc.controle.CtrlUsuario;
import es.esy.rafaelsilva.tcc.fragment.PerfilAmigos;
import es.esy.rafaelsilva.tcc.fragment.PerfilAtividade;
import es.esy.rafaelsilva.tcc.fragment.PerfilSobre;
import es.esy.rafaelsilva.tcc.interfaces.CallbackExcluir;
import es.esy.rafaelsilva.tcc.interfaces.CallbackListar;
import es.esy.rafaelsilva.tcc.interfaces.CallbackSalvar;
import es.esy.rafaelsilva.tcc.interfaces.CallbackTrazer;
import es.esy.rafaelsilva.tcc.modelo.Amigos;
import es.esy.rafaelsilva.tcc.modelo.Usuario;
import es.esy.rafaelsilva.tcc.util.DadosUsuario;
import es.esy.rafaelsilva.tcc.util.Resposta;
import es.esy.rafaelsilva.tcc.util.Util;

public class PerfilActivity extends AppCompatActivity {

    // dados usuario logado
    private List<Amigos> meusAmigos;
    private Usuario usuario;
    private int usu;
    private TextView nome, profissao, estilo, totalAmigos, totalAvaliacoes;
    private CircleImageView imgUsuario;
    private ImageView desfazerAmizade;
    private RelativeLayout relativeLayout;
    private ProgressBar progressBar;
    private FloatingActionButton fab;
    private Button btnEditarDados;

    public Usuario getUsuarioRequisitado(){
        return usuario;
    }

    public int getUsuarioCodigo(){
        return usu;
    }

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

        /*Verifica se o usuario tem conexao com a internet*/
        if (!Util.existeConexao(this)) {
            Toast.makeText(this,"Que pena, você está sem Internet. \nTente mais tarde \uD83D\uDC4D", Toast.LENGTH_LONG).show();
            this.finish();
        }

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

        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        nome = (TextView) findViewById(R.id.lbNome);
        profissao = (TextView) findViewById(R.id.lbProfissao);
        estilo = (TextView) findViewById(R.id.lbEstilo);
        totalAmigos = (TextView) findViewById(R.id.lbTotalAmigos);
        totalAvaliacoes = (TextView) findViewById(R.id.lbTotalAvaliacoes);
        imgUsuario = (CircleImageView) findViewById(R.id.imgUsuario);
        desfazerAmizade = (ImageView) findViewById(R.id.imgDesfazerAmizade);
        btnEditarDados = (Button) findViewById(R.id.btnEditarDados);
        btnEditarDados.setOnClickListener(editarDados());

        if (getIntent().getIntExtra("usuario", 0) == 0) {
            getUsuario(DadosUsuario.codigo);
            usu = DadosUsuario.codigo;
        }else {
            getUsuario(getIntent().getIntExtra("usuario", 0));
            usu = getIntent().getIntExtra("usuario", 0);
        }

        /* Adiciona a pessoa como amigo */
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

              AlertDialog.Builder mensagem = new AlertDialog.Builder(PerfilActivity.this);
              mensagem.setTitle("Criar amizade");
              mensagem.setMessage("Voce criará um laço de amizade com "+usuario.getNome()+" \nÉ isso mesmo que deseja?");

              mensagem.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialogInterface, int i) {
                      new CtrlAmigos(PerfilActivity.this).AddAmigo(usuario.getCodigo(), new CallbackSalvar() {
                          @Override
                          public void resultadoSalvar(Object obj) {
                              Resposta resp = (Resposta) obj;
                              if (resp.isFlag()) {
                                  fab.setVisibility(View.GONE);
                                  totalAmigos.setText(String.valueOf(Integer.parseInt(totalAmigos.getText().toString()) + 1));
                                  Toast.makeText(PerfilActivity.this, "\uD83D\uDC4D", Toast.LENGTH_LONG).show();
                                  trazerMeusAmigos();
                              }
                          }

                          @Override
                          public void falha() {
                              Toast.makeText(PerfilActivity.this, "Não foi possível, tente mais tarde.", Toast.LENGTH_LONG).show();
                          }
                      });
                  }
              });

              mensagem.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialogInterface, int i) {
                      Toast.makeText(PerfilActivity.this, "A amizade com "+usuario.getNome()+" NÃO foi feita.", Toast.LENGTH_SHORT).show();
                  }
              });
              mensagem.show();

          }
        });

    }

    private View.OnClickListener editarDados() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PerfilActivity.this, AtualizaCadastroUsuarioActivity.class);
                startActivity(intent);
            }
        };
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
        trazerMeusAmigos();
        setTitle(usuario.getNome());
        nome.setText(usuario.getNome());
        profissao.setText(usuario.getProfissao());
        estilo.setText(usuario.getAlimentacao());
        if (usuario.getImagem() != null)
            usuario.setImagemPerfil(imgUsuario, this);
        else
            imgUsuario.setImageResource(R.drawable.ic_usuario_white);

        monitorarCliqueImagemPerfil();

        relativeLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private void trazerMeusAmigos(){
        new CtrlAmigos(this).listar("WHERE amigoAdd = " + DadosUsuario.codigo, new CallbackListar() {
            @Override
            public void resultadoListar(List<Object> lista) {
                if (meusAmigos == null)
                    meusAmigos = new ArrayList<>();
                else
                    meusAmigos.clear();

                for (Object obj : lista)
                    meusAmigos.add((Amigos) obj);

                if (verificarPerfilEmFoco()){
                    if (usu != DadosUsuario.codigo)
                        fab.setVisibility(View.VISIBLE);
                }else{
                    desfazerAmizade.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void falha() {
                System.out.println("falha trazer amigos usuario");
            }
        });
    }

    private boolean verificarPerfilEmFoco(){
        if (meusAmigos != null && meusAmigos.size() > 0) {
            for (Amigos amigos : meusAmigos){
                if (amigos.getAmigoAce() == usu){
                    monitorarBtRemoverAmizade(amigos.getCodigo(), amigos.getPai());
                    return false;
                }
            }
            return true;
        }
        return true;
    }

    private void monitorarCliqueImagemPerfil(){
        imgUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mensagem = new AlertDialog.Builder(PerfilActivity.this);
//                View v = getLayoutInflater().inflate(R.layout.inflater_dialog_image_full, null);
//                ImageView imgUsuarioFull = (ImageView) v.findViewById(R.id.imgUsuarioFull);
                ImageView imgUsuarioFull = new ImageView(PerfilActivity.this);
                usuario.setImagemGrande(imgUsuarioFull, PerfilActivity.this);
                mensagem.setView(imgUsuarioFull);
                mensagem.show();

            }
        });
    }

    private void monitorarBtRemoverAmizade(final int codigo, final int pai){
        desfazerAmizade.setImageResource(R.drawable.ic_exclude_amigo);
        desfazerAmizade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder mensagem = new AlertDialog.Builder(PerfilActivity.this);
                mensagem.setTitle("Desfazer amizade");
                mensagem.setMessage("Seu laço de amizade com "+usuario.getNome()+" será desfeito \nÉ isso mesmo que deseja?");

                mensagem.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new CtrlAmigos(PerfilActivity.this).excluir(codigo, pai, new CallbackExcluir() {
                            @Override
                            public void resultadoExcluir(boolean flag) {
                                if (flag) {
                                    Toast.makeText(PerfilActivity.this, "Amizade desfeita", Toast.LENGTH_SHORT).show();
                                    totalAmigos.setText(String.valueOf(Integer.parseInt(totalAmigos.getText().toString()) - 1));
                                    fab.setVisibility(View.VISIBLE);
                                    desfazerAmizade.setVisibility(View.INVISIBLE);
                                    trazerMeusAmigos();
                                }else
                                    Toast.makeText(PerfilActivity.this, "Falha, amizade não desfeita", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void falha() {
                                Toast.makeText(PerfilActivity.this, "Falha, amizade não desfeita", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                mensagem.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(PerfilActivity.this, usuario.getNome()+" ainda é seu amigo \uD83D\uDC4D", Toast.LENGTH_SHORT).show();
                    }
                });
                mensagem.show();
            }
        });
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
                case 0: return PerfilAtividade.newInstance(position);
                case 1: return PerfilAmigos.newInstance(position);
                case 2: return PerfilSobre.newInstance(position);
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
                    return "ATIVIDADES";
                case 1:
                    return "AMIGOS";
                case 2:
                    return "SOBRE";
            }
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
