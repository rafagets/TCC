package es.esy.rafaelsilva.tcc.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import es.esy.rafaelsilva.tcc.task.ComentariosPostTask;
import es.esy.rafaelsilva.tcc.task.UtilTask;
import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.util.DadosUsuario;

public class ComentariosPostActivity extends AppCompatActivity {

    Context contexto = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentarios_post);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // acrecentar tambem overite no fim

        setTitle("Comentários");

        final int codigoPost = getIntent().getIntExtra("post", 0);
        loadComentarios(codigoPost);

        final EditText edit = (EditText) findViewById(R.id.txtComentario);

        edit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == keyEvent.ACTION_DOWN)
                    if (i == KeyEvent.KEYCODE_ENTER){
                        UtilTask util = new UtilTask(ComentariosPostActivity.this, "C", "comentariopost");
                        String campos = "comentario,usuario,coment";
                        String values = "\""+edit.getText().toString() + "\"," + DadosUsuario.codigo + "," +codigoPost;
                        util.execute(campos, values);
                        //Toast.makeText(contexto, "enter", Toast.LENGTH_LONG).show();

                        edit.setText("");
                        loadComentarios(codigoPost);
                    }
                return false;
            }
        });

    }

    private void loadComentarios(int codigoPost) {
        ComentariosPostTask loadPosts = new ComentariosPostTask(contexto);
        loadPosts.execute("R", "comentariopost", " WHERE coment = " + codigoPost + " ORDER BY data ASC");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) { // manipula o menu back
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
