package es.esy.rafaelsilva.tcc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.interfaces.CallbackView;
import es.esy.rafaelsilva.tcc.modelo.Post;
import es.esy.rafaelsilva.tcc.views.ViewAmizade;
import es.esy.rafaelsilva.tcc.views.ViewAvaliacao;
import es.esy.rafaelsilva.tcc.views.ViewComentario;

/**
 * Criado por Rafael em 29/10/2016, enjoy it.
 */
public class Atividades extends BaseAdapter {
    private Context contexto;
    private List<Post> lista;

    public Atividades(Context contexto, List<Post> lista) {
        this.contexto = contexto;
        this.lista = lista;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int i) {
        return lista.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = LayoutInflater.from(contexto).inflate(R.layout.adapter_amigos, viewGroup, false);

        for (Post post : lista){
            if (post.getTipo() == 1) {

                new ViewComentario(contexto, v, post).getView(new CallbackView() {
                    @Override
                    public void view(View view) {
                        //return view;
                    }
                });

            }else if (post.getTipo() == 2){

                new ViewAmizade(contexto, v, post).getView(new CallbackView() {
                    @Override
                    public void view(View view) {
                        if (view != null) {

                        }
                    }
                });

            }else if (post.getTipo() == 3){

                new ViewAvaliacao(contexto, v, post).getView(new CallbackView() {
                    @Override
                    public void view(View view) {
                        if (view != null) {

                        }
                    }
                });

            }
        }
        return null;
    }
}
