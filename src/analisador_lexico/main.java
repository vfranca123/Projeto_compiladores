package analisador_lexico;
public class main {
    public static void main(String[] args) {
        String codigoExemplo = "int x = 42;\n" +
                               "double pi = 3.14;\n" +
                               "string msg = \"Olá, mundo!\";\n" +
                               "// Comentário de linha\n" +
                               "if (x == 42) { \n" +
                               "    pi = pi + 1;\n" +
                               "}";

        Scanner scanner = new Scanner(codigoExemplo);
        Token token;

        System.out.println("--- INICIANDO ANÁLISE LÉXICA ---");
        do {
            token = scanner.nextToken();
            System.out.println(token);
        } while (token.tipo != TokenType.EOF);
    }
}