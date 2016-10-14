package es.esy.rafaelsilva.tcc.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import es.esy.rafaelsilva.tcc.adapters.DbHelper;
import es.esy.rafaelsilva.tcc.modelo.Usuario;

public class UsuarioDao {

    private SQLiteDatabase db;
    private DbHelper helper;
    Usuario usuario;
//force push
    //instancia um objeto da classe es.esy.rafaelsilva.tcc.adapters.DbHelper
    public UsuarioDao(Context context) {
        helper = new DbHelper(context);
    }

    public long inserir(Usuario usuario) {
        ContentValues valores = new ContentValues();
        valores.put("email", usuario.getEmail());
        valores.put("senha", usuario.getSenha());


        db = helper.getWritableDatabase();
        long rowid = db.insert(DbHelper.TABLE_NOME, null, valores);
        helper.close();

        return rowid;
    }

    public List<Usuario> selecionarTodos() {
        List<Usuario> lista = new ArrayList<Usuario>();

        db = helper.getReadableDatabase();

        Cursor cursor = db.query(DbHelper.TABLE_NOME,
                new String[] {"email, senha"}, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String email = String.valueOf(cursor.getInt(0));
            String senha = String.valueOf(cursor.getInt(1));
            String profissao = String.valueOf(cursor.getInt(2));
            String alimentacao = String.valueOf(cursor.getInt(3));
            usuario = new Usuario();
            lista.add(usuario);
            cursor.moveToNext();
        }
        cursor.close();
        helper.close();

        return lista;
    }

    public int excluir(int codigo) {
        db = helper.getWritableDatabase();
        int rows = db.delete(DbHelper.TABLE_NOME, "email = ?", new String[] {String.valueOf(codigo)});
        helper.close();

        return rows;
    }

    public int alterar(Usuario usuario) {
        ContentValues valores = new ContentValues();
        valores.put("email", usuario.getEmail());
        valores.put("senha", usuario.getSenha());

        db = helper.getWritableDatabase();
        int rows = db.update(DbHelper.TABLE_NOME, valores, "codigo = " + usuario.getEmail(), null);
        helper.close();

        return rows;
    }

}
