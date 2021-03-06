package es.esy.rafaelsilva.tcc.adapters;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "userLocal.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "usuario";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //cria a tabela, se ainda não existir no banco de dados
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
            "(codigo INTEGER PRIMARY KEY," +
                " nome STRING NOT NULL," +
                " email STRING NOT NULL," +
                " senha STRING NOT NULL," +
                " profissao STRING NOT NULL," +
                " alimentacao STRING NOT NULL," +
                " imagem BLOB," +
                " TIPO_IMAGEM INTEGER DEFAULT 2 )");
    }
//force push
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //atualiza o banco de dados, caso o número de versão seja incrementado
        Log.w(DbHelper.class.getName(), "Atualização de versão do BD: " + oldVersion + " para " + newVersion);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
