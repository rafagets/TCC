package es.esy.rafaelsilva.tcc.DAO;

/**
 * Created by Rafa on 31/08/2016.
 */
public class ScriptSQL {

    public static String getCreateDataBase(){
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("CREATE TABLE IF NOT EXISTS USUARIO ( " );
        sqlBuilder.append("_id            INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
        sqlBuilder.append("NOME           VARCHAR(40), ");
        sqlBuilder.append("EMAIL          VARCHAR (255), ");
        sqlBuilder.append("SENHA          VARCHAR(300) ");
        sqlBuilder.append("IMAGEM         VARCHAR(500)");
        sqlBuilder.append("TIPO_IMAGEM     INTEGER NOT NULL");
        sqlBuilder.append("); ");

        return sqlBuilder.toString();

    }
}
