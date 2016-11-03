package es.esy.rafaelsilva.tcc.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.esy.rafaelsilva.tcc.DAO.DAO;
import es.esy.rafaelsilva.tcc.fragment.CabecalhoPost;
import es.esy.rafaelsilva.tcc.util.Config;
import es.esy.rafaelsilva.tcc.util.DadosUsuario;

/**
 * Criado por Rafael em 14/10/2016, enjoy it.
 */
public class PostComentarioTask extends AsyncTask<String, Integer, Boolean> {
    private String acao;
    private String tabela;
    private String comentario;
    private Context contexto;

    public PostComentarioTask(String acao, String comentario, String tabela, Context contexto) {
        this.acao = acao;
        this.comentario = comentario;
        this.tabela = tabela;
        this.contexto = contexto;
    }

    @Override
    protected Boolean doInBackground(String... valores) {

        JSONArray array;
        DAO helper;
        boolean flag = false;

        helper = new DAO();
        JSONObject resposta = helper.getJSONOject(acao, Config.urlMaster, tabela, valores);

        try {
            flag = resposta.getBoolean("flag");
        } catch (JSONException e) {
            return false;
        }

        int val = 0;
        if (flag){
            String[] p = new String[]{"acao", "tabela", "asteristico", "condicao", "valores", "ordenacao"};
            String[] v = new String[]{"R", "post", "codigo", "usuario", String.valueOf(DadosUsuario.codigo), "ORDER BY data DESC LIMIT 1"};
            array = helper.getJSONArray(Config.urlMaster, p, v);
            try {
                val = array.getJSONObject(0).getInt("valor");
                if (val != 0)
                    flag = true;

            } catch (JSONException e) {
                return false;
            }
        }

        if ((flag) && (val != 0)){

            String campo = "comentario,usuarioPost,pai";
            String valor = "\"" + comentario + "\"," + DadosUsuario.codigo + "," + String.valueOf(val);
            String[] dados = new String[] {campo, valor};
            resposta = helper.getJSONOject("C", Config.urlMaster, "comentario", dados);

            try {

                flag = resposta.getBoolean("flag");
                if (flag){
                    return flag;
                }else {
                    campo = "codigo";
                    valor = String.valueOf(val);
                    dados = new String[] {campo, valor};
                    resposta = helper.getJSONOject("D", Config.urlMaster, "post", dados);

                    try {
                        flag = resposta.getBoolean("flag");
                        if (!flag){
                            Log.e("***FALHA EM COMENTAR***", "Todos os requicios foram escluidos");
                            return false;
                        }
                    } catch (JSONException e) {
                        Log.e("***FALHA EM COMENTAR***", "os requicios NÃO foram escluidos");
                        return false;
                    }
                }

            } catch (JSONException e) {

                campo = "codigo";
                valor = String.valueOf(val);
                dados = new String[] {campo, valor};
                resposta = helper.getJSONOject("D", Config.urlMaster, "post", dados);

                try {
                    flag = resposta.getBoolean("flag");
                    if (!flag){
                        Log.e("***FALHA EM COMENTAR***", "Todos os requicios foram escluidos");
                    }
                } catch (JSONException ee) {
                    Log.e("***FALHA EM COMENTAR***", "os requicios NÃO foram escluidos");
                    return false;
                }

            }


        }

        return null;
    }

    @Override
    protected void onPostExecute(Boolean flag) {
        if (flag)
            Toast.makeText(contexto, "\uD83D\uDC4D", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(contexto, "\uD83D\uDC4D", Toast.LENGTH_LONG).show();
    }
}
