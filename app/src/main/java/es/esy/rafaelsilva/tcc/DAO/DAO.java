package es.esy.rafaelsilva.tcc.DAO;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Rafael on 10/04/2016.
 */
public class Dao {

    public JSONObject getJSONOject(String acao, String serverUrl, String tabela, String[] valores) {
        JSONObject jsonObject = null;

        try {
            //um "Uniform Resource Locator" que identifica a localização de um recurso da Internet
            URL url = new URL(serverUrl);
            //obtém uma conexão para o recurso indicado por "URL" e instancia um HttpURLConnection
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            //configura o método de requisição
            http.setRequestMethod("POST");
            //estabelece que a requisição tem um corpo, ou seja, dados serão enviados na requisição
            //true indicates that the application intends to write data to the URL connection
            http.setDoOutput(true);

            //obtém um OutputStream para que dados possam ser escritos e transmitidos
            OutputStream out = http.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));

            String condicao;
            String valor = null;
            condicao = valores[0];

            for (int i=1; i < valores.length; i++)
                valor = valores[i];


            String data = URLEncoder.encode("condicao", "UTF-8") + "=" + URLEncoder.encode(condicao, "UTF-8");
                    data = URLEncoder.encode("valores", "UTF-8") + "=" + URLEncoder.encode(valor, "UTF-8") +"&"+ data;
                    data = "acao="+acao+"&"+"tabela="+tabela+"&"+data;

            Log.i("********** data", data);
            bw.write(data);
            bw.flush();
            bw.close();

            //lê e retorna os dados vindos como resposta do recurso apontado por HttpURLConnection
            InputStream is = http.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
            String response = "", line;

            while ((line = bufferedReader.readLine()) != null)
            {
                response += line;
            }

            bufferedReader.close();
            is.close();
            http.disconnect();

            try {
                jsonObject = new JSONObject(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public JSONArray getJSONArray(String serverUrl, String[] params, String[] values) {
        JSONArray jsonArray = null;

        try {
            //um "Uniform Resource Locator" que identifica a localização de um recurso da Internet
            URL url = new URL(serverUrl);
            //obtém uma conexão para o recurso indicado por "URL" e instancia um HttpURLConnection
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            //configura o método de requisição
            http.setRequestMethod("POST");
            //estabelece que a requisição tem um corpo, ou seja, dados serão enviados na requisição
            //true indicates that the application intends to write data to the URL connection
            http.setDoOutput(true);

            //obtém um OutputStream para que dados possam ser escritos e transmitidos
            OutputStream out = http.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));

            String data = "";

            for (int i = 0; i < values.length; i++)
                data += URLEncoder.encode(params[i], "UTF-8") + "=" + URLEncoder.encode(values[i], "UTF-8") + "&";
                //retira o último & inserido nos dados de requisição
                data = data.substring(0, data.lastIndexOf("&"));

            Log.i("********** data", data);
            bw.write(data);
            bw.flush();
            bw.close();

            //lê e retorna os dados vindos como resposta do recurso apontado por HttpURLConnection
            InputStream is = http.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is,"UTF-8"));

            String response = "", line;

            while ((line = bufferedReader.readLine()) != null)
            {
                response += line;
            }

            Log.e("********** data", response);

            bufferedReader.close();
            is.close();
            http.disconnect();

            try {
                jsonArray = new JSONArray(response.toString());
            } catch (JSONException e) {
                jsonArray = null;
                //e.printStackTrace();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonArray;
    }
}
