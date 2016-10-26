package es.esy.rafaelsilva.tcc.DAO;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.esy.rafaelsilva.tcc.interfaces.VolleyCallback;
import es.esy.rafaelsilva.tcc.util.App;
import es.esy.rafaelsilva.tcc.util.Config;

/**
 * Created by Rafael on 18/10/2016.
 */
public class GetData<T>{

    private RequestQueue requisicao;
    private Map<String, String> params;
    private String intencao;

    public GetData(String intencao, Context contexto, Map<String, String> params) {
        this.params = params;
        //this.requisicao = Volley.newRequestQueue(contexto);
        this.intencao = intencao;
    }

    public void executar(final Class<T> clazz, final VolleyCallback callback){

        StringRequest request = new StringRequest(
                Request.Method.POST,
                Config.urlMaster,

                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {

                        if (!response.equals("\uFEFF\"ERROR_LOGIN\"")) {

                            JSONArray array;
                            if (intencao.equals("lista")) {

                                try {
                                    array = new JSONArray(response.toString());

                                    List<Object> lista = new ArrayList<>();
                                    for (int i = 0; i < array.length(); i++) {
                                        String json = array.get(i).toString();
                                        T obj;
                                        Gson gson = new Gson();
                                        obj = gson.fromJson(json, clazz);

                                        lista.add(obj);
                                    }

                                    //Log.i("*** "+clazz.getSimpleName(), response);
                                    callback.sucessoLista(lista);

                                } catch (JSONException e) {
                                    //e.printStackTrace();
                                    //Log.e("*** " + clazz.getSimpleName(), "null list");
                                    callback.erro(clazz.getSimpleName() + ": não foi possivel trazer a lista: ");
                                }

                            }else if (intencao.equals("objeto")){

                                try {
                                    T obj;
                                    try {
                                        String json = response.toString();
                                        Gson gson = new Gson();
                                        obj = gson.fromJson(json, clazz);
                                    }catch (Exception e){
                                        array = new JSONArray(response.toString());

                                        String json = array.getJSONObject(0).toString();
                                        Gson gson = new Gson();
                                        obj = gson.fromJson(json, clazz);
                                    }

                                    //Log.i("*** "+clazz.getSimpleName(), response);
                                    callback.sucesso(obj);
                                } catch (JSONException e) {
                                    //e.printStackTrace();
                                    //Log.e("*** " + clazz.getSimpleName(), "null object");
                                    callback.erro(clazz.getSimpleName() + ": não foi possivel trazer o objeto");
                                }

                            }else{
                                callback.erro("por favor verificar a intencao");
                                Log.d("*** Atenção ***", "verificar o parametro intencao");
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
                params.put("email", "sabrina@gmail.com");
                params.put("senha", "123456");
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
