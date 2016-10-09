package es.esy.rafaelsilva.tcc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.esy.rafaelsilva.tcc.task.ImageLoaderTask;
import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.modelo.Comentario;
import es.esy.rafaelsilva.tcc.util.Config;
import es.esy.rafaelsilva.tcc.util.DadosUsuario;

/**
 * Created by Rafael on 06/09/2016.
 */
public class HomeAdapter extends BaseAdapter {

    private Context contexto;
    private List<Comentario> timeLineList;
    private int tipo = 1;

    public HomeAdapter(Context contexto, List<Comentario> timeLineList) {
        this.contexto = contexto;
        this.timeLineList = timeLineList;
    }

    @Override
    public int getCount() {
        return timeLineList.size();
    }

    @Override
    public Object getItem(int i) {
        return timeLineList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = null;

        switch (tipo){
            case 1:

                Comentario c = timeLineList.get(i);
                v = LayoutInflater.from(contexto).inflate(R.layout.inflater_post, viewGroup, false);

                CircleImageView imgUsuario = (CircleImageView) v.findViewById(R.id.imgUsuario);
                ImageLoaderTask downImg = new ImageLoaderTask(imgUsuario);
                downImg.execute(Config.caminhoImageTumb + c.getUsuario().getImagem());

                TextView nome = (TextView) v.findViewById(R.id.lbNome);
                nome.setText(c.getUsuario().getNome());

                TextView post = (TextView) v.findViewById(R.id.lbPost);
                post.setText(c.getComentario());

                TextView data = (TextView) v.findViewById(R.id.lbData);
                String[] temp = c.getData().split(" ");
                long date = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String dataForm = sdf.format(date);

                if (temp[0].equals(dataForm)) {
                    temp = temp[1].split(":");
                    data.setText("às " + temp[0] + ":" + temp[1]);
                }else{
                    temp = temp[0].split("-");
                    data.setText("em " + temp[2] + "/" + temp[1]);
                }

                // verifica se o usuario ja curtiu o comentario
                final ImageView addOne = (ImageView) v.findViewById(R.id.imgAddOne);
                if (c.getCurtidaComentario() != null)
                    for (int x=0; x < c.getCurtidaComentario().length; x++) {
                        if (c.getCurtidaComentario()[x].getUsuario() == DadosUsuario.codigo){
                            addOne.setBackgroundResource(R.drawable.ic_added);
                        }
                    }

                addOne.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addOne.setImageResource(R.drawable.ic_added);
                        //Toast.makeText(contexto, "add one.", Toast.LENGTH_LONG).show();
                    }
                });

                TextView qtdAddOne = (TextView) v.findViewById(R.id.lbAddOne);
                if (c.getCurtidaComentario() != null)
                    qtdAddOne.setText(String.valueOf(c.getCurtidaComentario().length) + " curtiu");
                else
                    qtdAddOne.setText("");

        }


        return v;
    }

//    private View.OnClickListener cliclAddOne(final ImageView addOne) {
//        return new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                addOne.setImageResource(R.drawable.ic_added);
//                Toast.makeText(contexto, "add one.", Toast.LENGTH_LONG).show();
//            }
//        };
//    }
}
