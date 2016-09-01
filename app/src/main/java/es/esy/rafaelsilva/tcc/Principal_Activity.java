package es.esy.rafaelsilva.tcc;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import es.esy.rafaelsilva.tcc.DAO.DataBase;

public class Principal_Activity extends AppCompatActivity {
    private DataBase database;
    private SQLiteDatabase connection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_);


        //conexao com banco de dados local
        try {
            database = new DataBase(this);
            connection = database.getWritableDatabase();
        }catch(SQLException ex){

        }

    }
}
