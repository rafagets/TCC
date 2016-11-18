package es.esy.rafaelsilva.tcc.DAO;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.esy.rafaelsilva.tcc.interfaces.CallBackDAO;
import es.esy.rafaelsilva.tcc.util.App;
import es.esy.rafaelsilva.tcc.util.Config;
import es.esy.rafaelsilva.tcc.util.DadosUsuario;

/**
 * Criado por Rafael em 18/10/2016, enjoy it.
 */
public class GetData<T>{

    private Map<String, String> params;
    private String intencao;

    public GetData(String intencao, Map<String, String> params) {
        this.params = params;
        //this.requisicao = Volley.newRequestQueue(contexto);
        this.intencao = intencao;
    }

    public void executar(final Class<T> clazz, final CallBackDAO callback){

        StringRequest request = new StringRequest(
                Request.Method.POST,
                Config.urlMaster,

                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        System.out.println("response::::" +response);

                        if (!response.equals("\uFEFF\"ERROR_LOGIN\"")) {

                            JSONArray array;
                            switch (intencao) {
                                case "lista":

                                    try {
                                        array = new JSONArray(response);

                                        List<Object> lista = new ArrayList<>();
                                        for (int i = 0; i < array.length(); i++) {
                                            String json = array.get(i).toString();
                                            T obj;
                                            Gson gson = new Gson();
                                            obj = gson.fromJson(json, clazz);

                                            lista.add(obj);
                                        }

                                        Log.i("*** " + clazz.getSimpleName(), response);
                                        callback.sucessoLista(lista);

                                    } catch (JSONException e) {
                                        //e.printStackTrace();
                                        //Log.e("*** " + clazz.getSimpleName(), "null list");
                                        callback.erro(clazz.getSimpleName() + ": não foi possivel trazer a lista: ");
                                    }

                                    break;
                                case "objeto":

                                    try {
                                        T obj;
                                        try {
                                            Gson gson = new Gson();
                                            obj = gson.fromJson(response, clazz);
                                        } catch (Exception e) {
                                            array = new JSONArray(response);
                                            String json = array.getJSONObject(0).toString();
                                            Gson gson = new Gson();
                                            obj = gson.fromJson(json, clazz);
                                        }

                                        Log.i("*** " + clazz.getSimpleName(), response);
                                        callback.sucesso(obj);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        //Log.e("*** " + clazz.getSimpleName(), "null object");
                                        callback.erro(clazz.getSimpleName() + ": não foi possivel trazer o objeto");
                                    }

                                    break;
                                default:
                                    callback.erro("por favor verificar a intencao");
                                    Log.d("*** Atenção ***", "verificar o parametro intencao");
                                    break;
                            }

                        }else{
                            callback.erro("Erro de login");
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.erro("Não foi possível realizar a conexão com webserver.");
                    }
                })

        {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                //params = new HashMap<>();
                if (DadosUsuario.email != null && DadosUsuario.senha != null) {
                    params.put("email", DadosUsuario.email);
                    params.put("senha", DadosUsuario.senha);
                }else{
                    params.put("email", "cadastro");
                    params.put("senha", "masterkey");
                }

                return(params);
            }

//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError{
//                HashMap<String, String> header = new HashMap<>();
//                header.put("apiKey", "API KEY: sdvkjbsdjvkbskdv");
//
//                return(header);
//            }
//
//            @Override
//            public Priority getPriority(){
//                return(Priority.NORMAL);
//            }
        };

        request.setTag("tag");
        //requisicao.add(request);
        App.getInstance().addToRequestQueue(request);

    }

}
