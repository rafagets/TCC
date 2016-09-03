package es.esy.rafaelsilva.tcc.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by Rafa on 31/08/2016.
 */
public class DataBase extends SQLiteOpenHelper {

    public DataBase(Context context){
        super(context, "APP_INI", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //criar os scripts de criacao do banco
        db.execSQL(ScriptSQL.getCreateDataBase());
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {


    //manutencao no banco
    }
}
