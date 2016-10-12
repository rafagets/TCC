package es.esy.rafaelsilva.tcc.modelo;

public class Produtor {


    /**
     * codigo : 9
     * nome : Fazenda Santa Gertrudes
     * email : fazenda@gmail.com
     * senha : 1234
     * descricao : A Fazenda Santa Gertrudes, com mais de um seculo de existencia, 1854 - 1998, ao contrario de muitas fazendas cafeeiras que tiveram um curto periodo de vida, venceu todas as crises e mudancas sociais e economicas advindas durante todo esse periodo constituindo-se num caso raro deste periodo.
     * cidade : 1
     */

    private int codigo;
    private String nome;
    private String email;
    private String senha;
    private String descricao;
    private int cidade;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getCidade() {
        return cidade;
    }

    public void setCidade(int cidade) {
        this.cidade = cidade;
    }
}
