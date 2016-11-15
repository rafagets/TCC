package es.esy.rafaelsilva.tcc.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.activity.PerfilActivity;
import es.esy.rafaelsilva.tcc.controle.CtrlAmigos;
import es.esy.rafaelsilva.tcc.controle.CtrlPost;
import es.esy.rafaelsilva.tcc.controle.CtrlUsuario;
import es.esy.rafaelsilva.tcc.interfaces.CallbackExcluir;
import es.esy.rafaelsilva.tcc.interfaces.CallbackListar;
import es.esy.rafaelsilva.tcc.interfaces.CallbackSalvar;
import es.esy.rafaelsilva.tcc.interfaces.CallbackTrazer;
import es.esy.rafaelsilva.tcc.modelo.Amigos;
import es.esy.rafaelsilva.tcc.modelo.Post;
import es.esy.rafaelsilva.tcc.modelo.Usuario;
import es.esy.rafaelsilva.tcc.util.DadosUsuario;
import es.esy.rafaelsilva.tcc.util.Resposta;

/**
 * Criado por Rafael em 26/10/2016, enjoy it.
 */
public class AmigosAdapter extends BaseAdapter {
    private Context contexto;
    private List<Amigos> amigos;
    private List<Amigos> meusAmigos;
    private int usuario;

    public AmigosAdapter(List<Amigos> amigos, Context contexto) {
        this.amigos = amigos;
        this.contexto = contexto;
    }

    @Override
    public int getCount() {
        return amigos.size();
    }

    @Override
    public Object getItem(int i) {
        return amigos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, final View view, ViewGroup viewGroup) {
        View v = LayoutInflater.from(contexto).inflate(R.layout.adapter_amigos, viewGroup, false);

        usuario = ((PerfilActivity) contexto).getUsuarioCodigo();

        trazerMeusAmigos(v, i);

        return v;
    }

    /* Tras a lista de amizades do usuario logado no sistema
    *  para comparar se a amizade do amigo contem na sua lista de amizades*/
    private void trazerMeusAmigos(final View v, final int i){
        new CtrlAmigos(contexto).listar("WHERE amigoAdd = " + DadosUsuario.codigo, new CallbackListar() {
            @Override
            public void resultadoListar(List<Object> lista) {
                meusAmigos = new ArrayList<>();
                for (Object obj : lista)
                    meusAmigos.add((Amigos) obj);
                trazerAmigo(v, i);
            }

            @Override
            public void falha() {
                System.out.println("falha trazer amigos usuario");
                trazerAmigo(v, i);
            }
        });
    }

    /* Traz a lista de amizades do amigo */
    private void trazerAmigo(final View v, final int i){
        new CtrlUsuario(contexto).trazer(amigos.get(i).getAmigoAce(), new CallbackTrazer() {
            @Override
            public void resultadoTrazer(Object obj) {
                amigos.get(i).setAmigoAceObj((Usuario) obj);
                if (amigos.get(i).getAmigoAceObj() != null){
                    setarDados(v, i);
                }
            }

            @Override
            public void falha() {
                System.out.println("falha trazer amigos");
            }
        });
    }

    /* seta os dados do amigo em questão */
    private void setarDados(View v, int i){

        final TextView nome = (TextView) v.findViewById(R.id.lbNome);
        final CircleImageView imgAmigo = (CircleImageView) v.findViewById(R.id.imgUsuario) ;
        ImageView ic_status = (ImageView) v.findViewById(R.id.imgAddAmigo);
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.linear_notificacao);

        /* Verifica se o nome é maior que o espaço designado
        * se sim, adapta o nome para caber no campo*/
        if (amigos.get(i).getAmigoAceObj().getNome().length() > 13)
            nome.setText(amigos.get(i).getAmigoAceObj().getNome().substring(0, 10) + "...");
        else
            nome.setText(amigos.get(i).getAmigoAceObj().getNome());

        /* Seta a imagem do perfil do usuario */
        amigos.get(i).getAmigoAceObj().setImagemPerfil(imgAmigo, contexto);

        /*
        * Verifica se o usuario tem uma solicitação de amizade pendente
        * Caso a solicitação for para o usuario logado, aparece a opção para aceitar*/
        if (amigos.get(i).getAmigoAce() == DadosUsuario.codigo){
            if (amigos.get(i).getStatusAmizade() == 1){
                layout.setVisibility(View.VISIBLE);
                this.monitorarCliqueAceitarAmizade(layout, amigos.get(i).getCodigo(), amigos.get(i).getPai());
            }
        }

        if (amigos.get(i).getAmigoAdd() == DadosUsuario.codigo){
            if (amigos.get(i).getStatusAmizade() == 1){
                layout.setVisibility(View.VISIBLE);
                ic_status.setImageResource(android.R.drawable.ic_dialog_info);
            }
        }

        monitorarCliqueImgUsuario(imgAmigo, i);
    }

    private void monitorarCliqueAceitarAmizade(final LinearLayout layout, final int codigoAmizade, final int post) {
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CtrlAmigos(contexto).aceitarAmizade(codigoAmizade, post, new CallbackSalvar() {
                    @Override
                    public void resultadoSalvar(Object obj) {
                        layout.setVisibility(View.GONE);
                    }

                    @Override
                    public void falha() {
                        Toast.makeText(contexto, "Falha ao aceitar amizade\nTente novamente mais tarde", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    /*Monitora o clique em cima da foto do perfil do usuario*/
    private void monitorarCliqueImgUsuario(CircleImageView img, final int i) {
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(contexto, PerfilActivity.class);
                intent.putExtra("usuario", amigos.get(i).getAmigoAce());
                contexto.startActivity(intent);
            }
        });
    }
}
