package es.esy.rafaelsilva.tcc.task;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import es.esy.rafaelsilva.tcc.DAO.DAO;
import es.esy.rafaelsilva.tcc.DAO.UsuarioDao;
import es.esy.rafaelsilva.tcc.activity.Login_Activity;
import es.esy.rafaelsilva.tcc.modelo.Lote;
import es.esy.rafaelsilva.tcc.modelo.Usuario;
import es.esy.rafaelsilva.tcc.util.Config;
import es.esy.rafaelsilva.tcc.util.DadosUsuario;

/**
 * Created by Rafael on 19/08/2016.
 */

//                Usuario c = new Usuario();
//                c.setCodigo(11);
//                c.setNome("Maria guerra");
//                c.setTelefone(149978522);
//
//                //-------------------------------------------------------------------
//                    //Criando arquivo
//                UtilCUD util = new UtilCUD(
//                        MainActivity.this,  // contexto
//                        "C",                // acão
//                        c.getTabela()       // tabela que sera salva
//                );
//                util.execute(c.nomes(), c.valores());

//-------------------------------------------------------------------
//excluindo arquivo
//                UtilCUD util = new UtilCUD(
//                        MainActivity.this,  //contexto
//                        "D",                // acão
//                        c.getTabela()       // tabela que sera salva
//                );
//                util.execute("codigo", "10");

//-------------------------------------------------------------------
//update arquivo
//                UtilCUD util = new UtilCUD(
//                        MainActivity.this,  // contexto
//                        "U",                // acao
//                        c.getTabela()       // tabela que sera salva
//                );
//                util.execute(
//                        "codigo="+c.getCodigo(),
//                        "nome=\""+c.getNome()+"\""
//                        // colocar todos os valores que serao salvos segundo o padrao nome="valor"
//                        // *obs: seguindo o padrao MySQL, toda a variavel de texto deve estar entre "aspas"
//                );


public class UtilTask extends AsyncTask<String, Integer, Boolean> {

    private String acao;
    private String tabela;
    private Context contexto;
    public String[] valoresTemp;
    Usuario usuario;
    UsuarioDao dao;
    private List<Usuario> lista;


    public UtilTask(Context contexto, String acao, String tabela) {
        this.contexto = contexto;
        this.acao   = acao;
        this.tabela = tabela;

    }

    @Override
    protected void onPreExecute() {
//        ProgressBar bar = new ProgressBar(contexto);
//        bar.incrementProgressBy(10);

    }

    @Override
    protected Boolean doInBackground(String... valores) {

        valoresTemp = valores[1].split(",");

        JSONObject jsonObject;
        DAO helper;

        helper = new DAO();
        jsonObject = helper.getJSONOject(acao, Config.urlMaster, tabela, valores);

        if(contexto.getClass().getSimpleName().equals("CadastroUsuarioActivity")){
            usuario = new Usuario();
            usuario = new Usuario().getUsuarioObjEmail("'"+valoresTemp[1].substring(1,valoresTemp[1].length() - 1)+"'");
        }

        try {
            return jsonObject.getBoolean("flag");
        } catch (Exception e) {
            Log.e("ERRO", "Falha. =(");
        }

        return false;

    }

    @Override
    protected void onPostExecute(Boolean flag) {

        if (flag) {
            if (acao.equals("C")) {

                if(contexto.getClass().getSimpleName().equals("CadastroUsuarioActivity")) {

                    Toast.makeText(contexto, "Cadastro efetuado com sucesso!", Toast.LENGTH_LONG).show();

                    dao = new UsuarioDao(contexto);

                    long result = dao.inserir(usuario);
                    //chamar a tela de login passando o usuario/email que acabou de cadastrar
                    if (result >= 0){
                        System.out.println("Resultado dao.inserir : " + result);
                        DadosUsuario.setUsuarioCorrente(usuario);
                        Intent intent = new Intent(contexto, Login_Activity.class);
                        intent.putExtra("nome", valoresTemp[0].substring(1,valoresTemp[0].length() - 1));
                        intent.putExtra("email",valoresTemp[1].substring(1,valoresTemp[1].length() - 1));
                        //
                        contexto.startActivity(intent);
                    }
                }
            }
            Log.e("OK", String.valueOf(flag));
        }else {
            Toast.makeText(contexto, "Falha. Tente mais tarde =(", Toast.LENGTH_LONG).show();
        }

    }

}
