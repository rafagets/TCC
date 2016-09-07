package es.esy.rafaelsilva.tcc.GenericDAO;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import es.esy.rafaelsilva.tcc.DAO.DAO;

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


public class UtilCUD extends AsyncTask<String, Integer, Boolean> {

    private ProgressDialog dialog;
    private String acao;
    private String url = "http://rafaelsilva.esy.es/tcc/crud.php";
    private String tabela;
    private Context contexto;


    public UtilCUD(Context contexto, String acao, String tabela) {
        this.dialog = new ProgressDialog(contexto);
        this.contexto = contexto;
        this.acao   = acao;
        this.tabela = tabela;
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage("Trabalhando, espere um pouco.");
        dialog.show();
    }

    @Override
    protected Boolean doInBackground(String... valores) {

        JSONObject jsonObject;
        DAO helper;

        helper = new DAO();
        jsonObject = helper.getJSONOject(acao, url, tabela, valores);

        try {
            return jsonObject.getBoolean("flag");
        } catch (Exception e) {
            Log.e("ERRO", "Falha. =(");
        }

        return false;

    }

    @Override
    protected void onPostExecute(Boolean flag) {
        dialog.dismiss();

        if (flag)
            Toast.makeText(contexto, "Sucesso! ;)", Toast.LENGTH_LONG).show();
        else {
            Toast.makeText(contexto, "Falha. =(", Toast.LENGTH_LONG).show();
        }

    }

}
