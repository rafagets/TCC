package es.esy.rafaelsilva.tcc;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import es.esy.rafaelsilva.tcc.GenericDAO.LoadComentsPost;
import es.esy.rafaelsilva.tcc.GenericDAO.UtilCUD;
import es.esy.rafaelsilva.tcc.configuracao.DadosUsuario;
import es.esy.rafaelsilva.tcc.home.HomeActivity;

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
                        UtilCUD util = new UtilCUD(ComentariosPostActivity.this, "C", "comentariopost");
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
        LoadComentsPost loadPosts = new LoadComentsPost(contexto);
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
