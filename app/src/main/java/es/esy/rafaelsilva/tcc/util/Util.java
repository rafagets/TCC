package es.esy.rafaelsilva.tcc.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Rafael on 12/09/2016.
 */
public class Util {

    public static List<Double> list;

    public static String formatDateToDB(String data){
        data = data.toString().replace("/","");
        data = (data.toString().substring(4) +
                data.toString().substring(2,4) +
                data.toString().substring(0,2));//   99999999
        return data;
    }
    public static String formatDateOfDB(String data){
        data = data.toString().replace("-","/");
        data = (data.toString().substring(8) + "/"+
                data.toString().substring(5,7) + "/"+
                data.toString().substring(0,4));//   9999-99-99
                                                //   0123456789
        return data;
    }

    public static String formatHoraHHMM(String data){
        String[]    temp = data.split(" ");
        temp = temp[1].split(":");
        String      retorno = ("às " + temp[0] + ":" + temp[1]);
        return      retorno;
    }

    public static String formatDataDDMM(String data){
        String[]    temp = data.split(" ");
                    temp = temp[0].split("-");
        String      retorno = ("em " + temp[2] + "/" + temp[1]);
        return      retorno;
    }

    public static String formatDataDDMMYY(String data){
        String[]    temp = data.split(" ");
        temp = temp[0].split("-");
        String      retorno = ("em " + temp[2] + "/" + temp[1] + "/" + temp[0]);
        return      retorno;
    }

    public static String formatDataDDmesYYYY(String data){
        String[]    temp = data.split(" ");
        temp = temp[0].split("-");

        String retorno = "";
        switch (temp[1]){
            case "01": retorno = "jan"; break;
            case "02": retorno = "fev"; break;
            case "03": retorno = "mar"; break;
            case "04": retorno = "abr"; break;
            case "05": retorno = "mai"; break;
            case "06": retorno = "jun"; break;
            case "07": retorno = "jul"; break;
            case "08": retorno = "ago"; break;
            case "09": retorno = "set"; break;
            case "10": retorno = "out"; break;
            case "11": retorno = "nov"; break;
            case "12": retorno = "dez"; break;
        }


        retorno = (temp[2] + " de "+ retorno + " " + temp[0]);
        return retorno;
    }

    public static boolean existeConexao(Context contexto){
        ConnectivityManager connectivity = (ConnectivityManager)
                contexto.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo netInfo = connectivity.getActiveNetworkInfo();

            // Se não existe nenhum tipo de conexão retorna false
            if (netInfo == null) {
                return false;
            }

            int netType = netInfo.getType();

            // Verifica se a conexão é do tipo WiFi ou Mobile e
            // retorna true se estiver conectado ou false em
            // caso contrário
            if (netType == ConnectivityManager.TYPE_WIFI ||
                    netType == ConnectivityManager.TYPE_MOBILE) {
                return netInfo.isConnected();

            } else {
                return false;
            }
        }else{
            return false;
        }
    }

    public static List<Double> getList() {
        return list;
    }

    public static void setList(List<Double> list) {
        Util.list = list;
    }
}
