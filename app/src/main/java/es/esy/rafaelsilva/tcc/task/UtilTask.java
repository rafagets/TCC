package es.esy.rafaelsilva.tcc.task;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import es.esy.rafaelsilva.tcc.DAO.DAO;
import es.esy.rafaelsilva.tcc.DAO.UsuarioDao;
import es.esy.rafaelsilva.tcc.activity.Login_Activity;
import es.esy.rafaelsilva.tcc.modelo.Usuario;
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
    Usuario usuario;
    UsuarioDao dao;


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
        //System.out.println("CONTEXTO--->" + contexto.getClass().getSimpleName());
        if (flag) {
            if (acao.equals("C")) {
                if(contexto.getClass().getSimpleName().equals("CadastroUsuarioActivity")) {
                    Toast.makeText(contexto, "Cadastro efetuado com sucesso!", Toast.LENGTH_LONG).show();
                    //prepara o email para setar txtEmail login
                    usuario = new Usuario();
//
                    usuario.setNome(valoresTemp[0].substring(1,valoresTemp[0].length() - 1));
                    usuario.setEmail(valoresTemp[1].substring(1,valoresTemp[1].length() - 1));
                    usuario.setSenha(valoresTemp[2].substring(1,valoresTemp[2].length() - 1));
                    usuario.setProfissao(valoresTemp[3].substring(1,valoresTemp[3].length() - 1));
                    usuario.setAlimentacao(valoresTemp[4].substring(1,valoresTemp[4].length() - 1));
                    System.out.println("Nome: " + usuario.getNome()+"\nEmail: "+ usuario.getEmail()+"\nSenha: "+
                            usuario.getProfissao()+ "\nAlimentação: "+usuario.getAlimentacao());


                    dao = new UsuarioDao(contexto);


                    if (dao.inserir(usuario) >= 0){

                        Intent intent = new Intent(contexto, Login_Activity.class);
                        intent.putExtra("nome", valoresTemp[0].substring(1,valoresTemp[0].length() - 1));
                        intent.putExtra("email",valoresTemp[1].substring(1,valoresTemp[1].length() - 1));
                        //
                        contexto.startActivity(intent);
//                    System.out.println("emailTemp sem editar: " + valoresTemp[1]);
//                    System.out.println("editado: "+valoresTemp[1].substring(1,valoresTemp[1].length() - 1));
                    }

                    //chamo a tela de login passando o usuario/email que acabou de cadastrar

                }
            }
            Log.e("OK", String.valueOf(flag));
        }else {
            Toast.makeText(contexto, "Falha. Tente mais tarde =(", Toast.LENGTH_LONG).show();
        }

    }

}
