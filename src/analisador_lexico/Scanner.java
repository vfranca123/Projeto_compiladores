package analisador_lexico;

import java.util.Set;
import java.util.HashSet;

public class Scanner {
    private final String codigoFonte;
    private int ponteiro = 0;
    private int linha = 1;
    private int coluna = 1;

    // Tabela de palavras reservadas exigidas no escopo da linguagem, Set<String> é uma coleção que não permite elementos duplicados eu 
    private static final Set<String> PALAVRAS_RESERVADAS = new HashSet<>();
    static {
        String[] palavras = { "int", "double", "bool", "char", "string", "if", "else", "while", "function", "return" };
        for (String p : palavras) {
            PALAVRAS_RESERVADAS.add(p);
        }
    }

    public Scanner(String codigoFonte) {
        this.codigoFonte = codigoFonte;
    }

    // Retorna o caractere atual sem avançar o cursor (peek)
    private char peek() {
        if (isAtEnd()) return '\0';
        return codigoFonte.charAt(ponteiro);
    }

    // Retorna o caractere atual e avança o cursor (advance)
    private char advance() {
        char atual = peek();
        ponteiro++;
        if (atual == '\n') {
            linha++;
            coluna = 1;
        } else {
            coluna++;
        }
        return atual;
    }

    private boolean isAtEnd() {
        return ponteiro >= codigoFonte.length();
    }

    // Método principal que retorna o próximo token (aplicando Maximal Munch)
    public Token nextToken() {
        while (!isAtEnd()) {
            char c = peek();

            // 1. Ignorar espaços em branco, tabs e quebras de linha fora de strings
            if (c == ' ' || c == '\r' || c == '\t' || c == '\n') {
                advance();
                continue;
            }

            // 2. Ignorar Comentários (linha // e bloco /* */)
            if (c == '/') {
                if (ponteiro + 1 < codigoFonte.length() && codigoFonte.charAt(ponteiro + 1) == '/') {
                    // Comentário de linha
                    while (!isAtEnd() && peek() != '\n') {
                        advance();
                    }
                    continue;
                } else if (ponteiro + 1 < codigoFonte.length() && codigoFonte.charAt(ponteiro + 1) == '*') {
                    // Comentário de bloco
                    int linhaInicio = linha;
                    int colunaInicio = coluna;
                    advance(); advance(); // consome "/*"
                    boolean fechou = false;
                    while (!isAtEnd()) {
                        if (peek() == '*' && ponteiro + 1 < codigoFonte.length() && codigoFonte.charAt(ponteiro + 1) == '/') {
                            advance(); advance(); // consome "*/"
                            fechou = true;
                            break;
                        }
                        advance();
                    }
                    if (!fechou) {
                        return new Token(TokenType.ERRO, "Comentário de bloco não fechado até o EOF", linhaInicio, colunaInicio);
                    }
                    continue;
                }
            }

            int inicioLinha = linha;
            int inicioColuna = coluna;

            // 3. Identificadores e Palavras Reservadas (Começam com letra ou '_')
            if (Character.isLetter(c) || c == '_') {
                StringBuilder lexema = new StringBuilder();
                while (!isAtEnd() && (Character.isLetterOrDigit(peek()) || peek() == '_')) {
                    lexema.append(advance());
                }
                
                String texto = lexema.toString();
                // Como a linguagem não é case-sensitive, podemos checar em minúsculo na tabela
                if (PALAVRAS_RESERVADAS.contains(texto.toLowerCase())) {
                    return new Token(TokenType.PALAVRA_RESERVADA, texto, inicioLinha, inicioColuna);
                } else {
                    return new Token(TokenType.IDENTIFICADOR, texto, inicioLinha, inicioColuna);
                }
            }

            // 4. Literais Numéricos (Inteiros ou Decimais)
            if (Character.isDigit(c)) {
                StringBuilder lexema = new StringBuilder();
                while (!isAtEnd() && Character.isDigit(peek())) {
                    lexema.append(advance());
                }

                // Parte decimal opcional
                if (!isAtEnd() && peek() == '.') {
                    lexema.append(advance()); // consome o ponto
                    while (!isAtEnd() && Character.isDigit(peek())) {
                        lexema.append(advance());
                    }
                }
                return new Token(TokenType.LITERAL_NUMERICO, lexema.toString(), inicioLinha, inicioColuna);
            }

            // 5. Strings (Delimitadas por aspas duplas)
            if (c == '"') {
                StringBuilder lexema = new StringBuilder();
                lexema.append(advance()); // consome a aspa inicial
                boolean fechou = false;

                while (!isAtEnd()) {
                    char atual = peek();
                    if (atual == '"') {
                        lexema.append(advance()); // consome a aspa final
                        fechou = true;
                        break;
                    } else if (atual == '\\') {
                        lexema.append(advance()); // consome a barra de escape
                        if (!isAtEnd()) {
                            lexema.append(advance()); // consome o caractere escapado (\n, \", etc)
                        }
                    } else {
                        lexema.append(advance());
                    }
                }

                if (!fechou) {
                    return new Token(TokenType.ERRO, "String não fechada", inicioLinha, inicioColuna);
                }
                return new Token(TokenType.STRING, lexema.toString(), inicioLinha, inicioColuna);
            }

            // 6. Operadores (com Maximal Munch para compostos como == e <=)
            if (c == '=' || c == '<' || c == '>' || c == '!' || c == '+' || c == '-' || c == '*' || c == '/') {
                char primeiro = advance();
                if (!isAtEnd() && peek() == '=') { // Operador composto (ex: ==, <=, >=, !=)
                    char segundo = advance();
                    return new Token(TokenType.OPERADOR, "" + primeiro + segundo, inicioLinha, inicioColuna);
                }
                return new Token(TokenType.OPERADOR, String.valueOf(primeiro), inicioLinha, inicioColuna);
            }

            // 7. Erro Léxico (Caractere inválido fora do alfabeto)
            char invalido = advance();
            return new Token(TokenType.ERRO, "Caractere inválido: " + invalido, inicioLinha, inicioColuna);
        }

        return new Token(TokenType.EOF, "", linha, coluna);
    }
}