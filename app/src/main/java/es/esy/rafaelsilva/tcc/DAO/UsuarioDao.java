package es.esy.rafaelsilva.tcc.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import es.esy.rafaelsilva.tcc.adapters.DbHelper;
import es.esy.rafaelsilva.tcc.modelo.Usuario;
import es.esy.rafaelsilva.tcc.util.DadosUsuario;

public class UsuarioDao {

    private SQLiteDatabase db;
    private DbHelper helper;

    Usuario usuario;
    //instancia um objeto da classe es.esy.rafaelsilva.tcc.adapters.DbHelper
    public UsuarioDao(Context context) {
        helper = new DbHelper(context);
    }

    public long inserir(Usuario usuario) {
        ContentValues valores = new ContentValues();
        valores.put("codigo", usuario.getCodigo());
        valores.put("nome", usuario.getNome());
        valores.put("email", usuario.getEmail());
        valores.put("senha", usuario.getSenha());
        valores.put("profissao", usuario.getProfissao());
        valores.put("alimentacao", usuario.getAlimentacao());

        if (usuario.getImgBitmap() != null) {
            valores.put("imagem", usuario.getImgBitmap());
        }
        if(usuario.getTipoImg() != 0 && usuario.getTipoImg() != 1) {
            valores.put("tipo_imagem", usuario.getTipoImg());
        }
        db = helper.getWritableDatabase();
        long rowid = db.insert(DbHelper.TABLE_NAME, null, valores);
        helper.close();
        System.out.println("****************************\nMostrando o rowID(" + rowid + ")\n***************************");
        return rowid;
    }

    public List<Usuario> selecionarTodos() {
        List<Usuario> lista = new ArrayList<Usuario>();

        db = helper.getReadableDatabase();

        Cursor cursor = db.query(DbHelper.TABLE_NAME,
                new String[] {"codigo, nome, email, senha, profissao, alimentacao", "imagem", "tipo_imagem"}, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            usuario = new Usuario();
            usuario.setCodigo(cursor.getInt(0));
            usuario.setNome(String.valueOf(cursor.getString(1)));
            usuario.setEmail(String.valueOf(cursor.getString(2)));
            usuario.setSenha(String.valueOf(cursor.getString(3)));
            usuario.setProfissao(String.valueOf(cursor.getString(4)));
            usuario.setAlimentacao(String.valueOf(cursor.getString(5)));
            if (cursor.getString(6) != null){
                System.out.println("IMAGEM STRING: " + String.valueOf(cursor.getString(6)));
                usuario.setImgBitmap(String.valueOf(cursor.getString(6)));
            }


            lista.add(usuario);
            cursor.moveToNext();
        }
        cursor.close();
        helper.close();

        return lista;
    }
    public String loadImg(){
        String dir = "";
        db = helper.getReadableDatabase();

        Cursor cursor = db.query(DbHelper.TABLE_NAME,
                new String[] {"codigo, nome, email, senha, profissao, alimentacao", "imagem", "tipo_imagem"}, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            if (cursor.getString(6) != null){
                System.out.println("IMAGEM STRING: " + String.valueOf(cursor.getString(6)));
                dir = String.valueOf(cursor.getString(6));
                DadosUsuario.getUsuario().setTipoImg(cursor.getInt(7));
                System.out.println("tipeIMG:" + cursor.getInt(7));
            }

        cursor.moveToNext();
        }
        cursor.close();
        helper.close();
        return dir;
    }
    public int updateImg(long codigo, String dir, int op){
        ContentValues valores = new ContentValues();

        valores.put("imagem", dir);
        valores.put("tipo_imagem", op);

        db = helper.getWritableDatabase();
        int rows = db.update(DbHelper.TABLE_NAME, valores, "codigo = " + codigo, null);
        helper.close();

        return rows;

    }

    public int excluir(int codigo) {
        db = helper.getWritableDatabase();
        int rows = db.delete(DbHelper.TABLE_NAME, "email = ?", new String[] {String.valueOf(codigo)});
        helper.close();

        return rows;
    }

    public void excluirTudo(){
        db = helper.getWritableDatabase();
        db.delete(DbHelper.TABLE_NAME,"",null);
        helper.close();
    }

    public int alterar(Usuario usuario) {
        ContentValues valores = new ContentValues();
        valores.put("email", usuario.getEmail());
        valores.put("senha", usuario.getSenha());

        db = helper.getWritableDatabase();
        int rows = db.update(DbHelper.TABLE_NAME, valores, "codigo = " + usuario.getEmail(), null);
        helper.close();

        return rows;
    }

}
