package analisador_lexico;
import java.util.Set;
import java.util.HashSet;

public class Scanner {
    private final String codigoFonte;
    private int ponteiro = 0;
    private int linha = 1;
    private int coluna = 1;

    //final diz que a variavel não pode ser reatribuída depois de inicializada, Set<String> é uma coleção que não permite elementos duplicados
    private static final Set<String> PALAVRAS_RESERVADAS = new HashSet<>();
    //o static define que o bloco de codigo seja executada apenas uma vez quando a classe é carregada 
    static {
        String[] palavras = { "int", "double", "bool", "char", "string", "if", "else", "while", "function", "return" };
        for (String p : palavras) {
            PALAVRAS_RESERVADAS.add(p);
        }
    }

    public Scanner(String codigoFonte) {
        this.codigoFonte = codigoFonte;
    }

}
