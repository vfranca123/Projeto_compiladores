# Especificação Léxica

## 1. Regras gerais

- **Alfabeto de entrada:** O conjunto de caracteres ASCII válidos permitidos no código-fonte, incluindo letras maiúsculas e minúsculas (`a-z`, `A-Z`), dígitos (`0-9`), o caractere sublinhado (`_`), operadores matemáticos/lógicos, pontuações, além de caracteres de formatação (espaços, tabulações `\t` e quebras de linha `\n`, `\r`).
- **Sensibilidade a maiúsculas/minúsculas (Case-sensitivity):** A linguagem **não** é case-sensitive. 
- **Identificadores:** Devem começar obrigatoriamente com uma letra ou com o caractere sublinhado (`_`), seguidos por zero ou mais letras, dígitos ou sublinhados (`(letra | '_') (letra | digito | '_')*`). *Nota: Identificadores não podem começar com números para evitar ambiguidades com literais numéricos.*
- **Comentários:** 
  - Comentários de linha iniciados por `//` (vêm da barra dupla até o final da linha).
  - Comentários de bloco delimitados por `/*` e `*/`.
  - Comentários de bloco **não** podem ser aninhados.
- **Strings:** Delimitadas por aspas duplas (`"`). Podem conter quebras de linha. Sequências de escape suportadas: `\"`, `\n`, `\\`. Caso o arquivo termine (EOF) sem que a string seja fechada, será gerado um erro léxico.
- **Literais numéricos:** Podem ser inteiros (`digito+`) ou decimais com ponto flutuante (`digito+ '.' digito+`). Não são aceitas notações científicas ou hexadecimais nesta versão.
- **Desambiguação de operadores (Maximal Munch):** Para operadores que possuem formas compostas (como `=` vs `==`, ou `<` vs `<=`), o scanner sempre tentará consumir o prefixo válido mais longo possível antes de emitir o token.

---

## 2. Categorias Obrigatórias de Tokens

        | Token             | Regra                                           | Exemplos                      |
        |-------------------|-------------------------------------------------|-------------------------------|
        | Identificador     | letras, números e `_`                           | `nome`, `_total`, `1aluno`    |
        | Palavra reservada | mesmo padrão do identificador + tabela de busca | `if`, `while`, `int`          |
        | String            | texto entre `"`                                 | `"Olá"`, `"linha 1\nlinha 2"` |
        | Operador          | 1 ou 2 caracteres                               | `+`, `==`, `<=`,'&&'.         |
        | Literal numérico  | inteiro ou decimal                              | `10`, `3.14`, `0`             |
---

## 3. Lista Fechada de Palavras Reservadas
As palavras reservadas reconhecidas pela linguagem, abrangendo os requisitos de tipos básicos, estruturas de controle e funções, são:

- **Tipos básicos:** `int`, `double`, `bool`, `char`, `string`
- **Estruturas de controle:** `if`, `else`, `while`
- **Funções e escopo:** `function`, `return`