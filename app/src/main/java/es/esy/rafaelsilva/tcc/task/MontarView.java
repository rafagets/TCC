package es.esy.rafaelsilva.tcc.task;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

import es.esy.rafaelsilva.tcc.activity.PerfilActivity;
import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.activity.HomeActivity;
import es.esy.rafaelsilva.tcc.interfaces.CallbackView;
import es.esy.rafaelsilva.tcc.modelo.Post;

import static java.lang.Thread.sleep;

/**
 * Created by Rafael on 23/10/2016.
 */
public class MontarView extends AsyncTask<Void, Void, Integer> {
    private Context contexto;
    private List<Post> post;
    private LinearLayout layout;
    private SwipeRefreshLayout recarregar;
    private View view;
    private boolean flag = false;

    public MontarView(Context contexto, LinearLayout layout, List<Post> post, SwipeRefreshLayout recarregar) {
        this.contexto = contexto;
        this.layout = layout;
        this.post = post;
        this.recarregar = recarregar;
    }

    @Override
    protected Integer doInBackground(Void... voids) {

        final int[] viewInseridas = {0};
        if (post.size() > 0)
            for (Post p : post){

                if (p.getTipo() == 1) {

                    try {
                        view = ((PerfilActivity) contexto).getLayoutInflater().inflate(R.layout.inflater_post, null);
                    }catch (Exception e){
                        view = ((HomeActivity) contexto).getLayoutInflater().inflate(R.layout.inflater_post, null);
                    }

                    new ViewComentario(contexto, view, p).getView(new CallbackView() {
                        @Override
                        public void view(View view) {
                            if (view != null) {
                                layout.addView(view);
                                viewInseridas[0]++;
                            }
                            flag = true;
                        }
                    });

                    try {
                        sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    while (!flag) {
                        if (flag == false)
                            flag = true;

                        try {
                            sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }else if (p.getTipo() == 2){

                    try {
                        view = ((PerfilActivity) contexto).getLayoutInflater().inflate(R.layout.inflater_add_amigo, null);
                    }catch (Exception e){
                        view = ((HomeActivity) contexto).getLayoutInflater().inflate(R.layout.inflater_add_amigo, null);
                    }

                    new ViewAmizade(contexto, view, p).getView(new CallbackView() {
                        @Override
                        public void view(View view) {
                            if (view != null) {
                                layout.addView(view);
                                viewInseridas[0]++;
                            }
                            flag = true;
                        }
                    });

                    try {
                        sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    while (!flag) {
                        if (flag == false)
                            flag = true;

                        try {
                            sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }else if (p.getTipo() == 3){

                    try {
                        view = ((PerfilActivity) contexto).getLayoutInflater().inflate(R.layout.inflater_avaliacao, null);
                    }catch (Exception e){
                        view = ((HomeActivity) contexto).getLayoutInflater().inflate(R.layout.inflater_avaliacao, null);
                    }

                    new ViewAvaliacao(contexto, view, p).getView(new CallbackView() {
                        @Override
                        public void view(View view) {
                            if (view != null) {
                                layout.addView(view);
                                viewInseridas[0]++;
                            }
                            flag = true;
                        }
                    });

                    try {
                        sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    while (!flag) {
                        if (flag == false)
                            flag = true;

                        try {
                            sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }

        return viewInseridas[0];
    }

    @Override
    protected void onPostExecute(Integer viewsInseridas) {
        if (viewsInseridas == 0){
            ImageView falha = new ImageView(contexto);
            falha.setImageResource(R.drawable.back_falha_carregar);
            layout.addView(falha);

            falha.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    layout.removeAllViews();
                    recarregar.setRefreshing(true);
                }
            });
        }
        recarregar.setRefreshing(false);
    }
}
