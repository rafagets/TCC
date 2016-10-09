package es.esy.rafaelsilva.tcc.task;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import es.esy.rafaelsilva.tcc.DAO.DAO;
import es.esy.rafaelsilva.tcc.activity.Login_Activity;
import es.esy.rafaelsilva.tcc.util.Config;

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


    public UtilTask(Context contexto, String acao, String tabela) {
        this.contexto = contexto;
        this.acao   = acao;
        this.tabela = tabela;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Boolean doInBackground(String... valores) {

        JSONObject jsonObject;
        DAO helper;

        helper = new DAO();
        jsonObject = helper.getJSONOject(acao, Config.urlMaster, tabela, valores);

        try {
            return jsonObject.getBoolean("flag");
        } catch (Exception e) {
            Log.e("ERRO", "Falha. =(");
        }

        return false;

    }

    @Override
    protected void onPostExecute(Boolean flag) {
        System.out.println("CONTEXTO--->" + contexto.getClass().getCanonicalName());
        if (flag) {
            if (acao.equals("C")) {
                if(contexto.getClass().getSimpleName().equals("CadastroUsuarioActivity")) {
                    Toast.makeText(contexto, "Cadastro efetuado com sucesso!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(contexto, Login_Activity.class);
                    contexto.startActivity(intent);
                }
            }
            Log.e("OK", String.valueOf(flag));
        }else {
            Toast.makeText(contexto, "Falha. Tente mais tarde =(", Toast.LENGTH_LONG).show();
        }

    }

}
