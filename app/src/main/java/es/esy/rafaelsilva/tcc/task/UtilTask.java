package es.esy.rafaelsilva.tcc.task;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import es.esy.rafaelsilva.tcc.DAO.DAO;
import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.activity.HomeActivity;
import es.esy.rafaelsilva.tcc.activity.Login_Activity;
import es.esy.rafaelsilva.tcc.fragment.CabecalhoPost;
import es.esy.rafaelsilva.tcc.fragment.CorpoHome;
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
    String[] valoresTemp;


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

        valoresTemp = valores[1].split(",");

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

        if (flag) {

            if (acao.equals("C")) {
                if(contexto.getClass().getSimpleName().equals("CadastroUsuarioActivity")) {
                    Toast.makeText(contexto, "Cadastro efetuado com sucesso!", Toast.LENGTH_LONG).show();
                    //prepara o email para setar txtEmail login

                    //
                    Intent intent = new Intent(contexto, Login_Activity.class);
                    intent.putExtra("email",valoresTemp[0]);

                    contexto.startActivity(intent);
                    System.out.println("emailTemp: " + valoresTemp[0]);
                }
            }

            Toast.makeText(contexto, "\uD83D\uDC4D", Toast.LENGTH_LONG).show();
            Log.e("OK", String.valueOf(flag));
        }else {
            Toast.makeText(contexto, "Falha. Tente mais tarde =(", Toast.LENGTH_LONG).show();
        }

    }

}
