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
        ProgressBar bar = new ProgressBar(contexto);
        bar.incrementProgressBy(10);

    }

    @Override
    protected Boolean doInBackground(String... valores) {
//        if (acao == "R" && contexto.getClass().getSimpleName().equals("CadastroUsuarioActivity")){
//            String[] params;
//            JSONArray jsonArray;
//            DAO helper;
//
//            params = new String[] { "acao", "tabela","condicao","valores", "ordenacao" };
//
//            helper = new DAO();
//
//            try {
//                jsonArray = helper.getJSONArray(Config.urlMaster, params, valores);
//
//                try {
//                    lista = new ArrayList<>();
//
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        String json = jsonArray.get(i).toString();
//
//                        Usuario usuario;
//                        Gson gson = new Gson();
//                        usuario = gson.fromJson(json, Usuario.class);
//                        DadosUsuario.setUsuarioCorrente(usuario);
//                        lista.add(usuario);
//                    }
//                    return true;
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }catch (Exception e){
//
//            }
//
//            return false;
//        }
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
        //System.out.println("CONTEXTO--->" + contexto.getClass().getSimpleName());
        if (flag) {
            if (acao.equals("C")) {

                if(contexto.getClass().getSimpleName().equals("CadastroUsuarioActivity")) {

//                    usuario = new Usuario();
//                    usuario.getUsuarioObjEmail("'"+valoresTemp[1].substring(1,valoresTemp[1].length() - 1)+"'");


//                    RequestParams params = new RequestParams();
//                    params.put("acao", "R");
//                    params.put("tabela", "usuario");
//                    params.put("condicao", "email");
//                    params.put("valores", "'"+valoresTemp[1].substring(1,valoresTemp[1].length() - 1)+"'");
//                    //*****************************************************************************
//
//
//                        String url = Config.urlMaster;
//
//                        AsyncHttpClient client = new AsyncHttpClient();
//                        client.post(contexto, url, params, new AsyncHttpResponseHandler() {
//
//                            @Override
//                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                                String resposta =  new String(responseBody);
//                                Log.e("+++++", "resposta: "+ resposta);
//                                System.out.println("<<<<<<<< antes do try >>>>>>>");
//                                JSONArray array;
//                                try {
//                                    System.out.println("<<<<<< entrei no try >>>>>");
//                                    Usuario userCurrent;
//                                    array = new JSONArray(resposta);
//                                    String rr = array.get(0).toString();
//                                    Gson gson = new Gson();
//                                    userCurrent = gson.fromJson(rr, Usuario.class);
//                                    DadosUsuario.setUsuarioCorrente(userCurrent);
//                                    for(int i = 0; i <=  array.length(); i++ ){
//                                        System.out.println("teste>>>>>>>>>>"+DadosUsuario.getUsuarioCorrente());
//                                    }
//                                } catch (JSONException e) {
//                                    System.out.println("<<<<<<<< entrei no exception>>>>>>>>");
//                                    Log.e("+++++", "Erro ao ler usuario recentemente cadastrado: ");
//                                    e.printStackTrace();
//                                }
//                            }
//                            @Override
//                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                                System.out.println("<<<<<<<<<<<< on Failure >>>>>>>>>>>");
//                                Toast.makeText(contexto, "Falha ao carregar usuario", Toast.LENGTH_LONG).show();
//                            }
//                        });
                    System.out.println("<<<<<<<<<< fora do OnSuccess >>>>>>>>>");


                    //*****************************************************************************
                    Toast.makeText(contexto, "Cadastro efetuado com sucesso!", Toast.LENGTH_LONG).show();
                    //prepara o email para setar txtEmail login

//                    usuario = new Usuario();
//                    usuario = DadosUsuario.getUsuarioCorrente();

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
//                    System.out.println("emailTemp sem editar: " + valoresTemp[1]);
//                    System.out.println("editado: "+valoresTemp[1].substring(1,valoresTemp[1].length() - 1));
                    }
                }
            }
            Log.e("OK", String.valueOf(flag));
        }else {
            Toast.makeText(contexto, "Falha. Tente mais tarde =(", Toast.LENGTH_LONG).show();
        }

    }

}
