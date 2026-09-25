package analisador_lexico;

enum TokenType{
    IDENTIFICADOR,
    PALAVRA_RESERVADA,
    STRING,
    OPERADOR,
    LITERAL_NUMERICO,
    EOF, // Fim do arquivo
    ERRO // Erro léxico
}

public class Token{
    public TokenType tipo;
    public String lexema;
    public int linha; 
    public int coluna;
    public Token(TokenType tipo, String lexema, int linha, int coluna){
        this.tipo = tipo;
        this.lexema = lexema;
        this.linha = linha;
        this.coluna = coluna;
    }
    @Override 
    public String toString(){
        return String.format("[%s] '%s' (Linha: %d, Coluna: %d)", tipo, lexema, linha, coluna);
    
    }
}
