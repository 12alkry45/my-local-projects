import java.util.*;
public class Parser {
    public boolean ok = true, lexerOk = true;
    StringBuilder sb = new StringBuilder();
    public List<Lexem> lexems = new ArrayList<Lexem>();
    int pos = 0, posError = -1, depth = 0;
    Parser(String[] s){
        for(int i = 0; i < s.length; i++){
            Lexer l = new Lexer(s[i], i);
            if(!l.ok) {
                lexerOk = false;
                sb.append("Lexical Error in (line: ").append(i);
                sb.append(" , col: ")
                        .append(l.posError)
                        .append(")\nFOUND NONE EXPECTED SYMBOL: \"")
                        .append(Character.toString(s[i].charAt(l.posError)))
                        .append("\"");
                break;
            }
            lexems.addAll(l.lexems);
        }
        ok = lexerOk;
        lexems.add(new Lexem(0, "END", -1, -1));
        if(ok) {
            add("LEXEMS SUCCESS!\n");
            stmt();
            if(ok && peek().val.equals("END")) add("SYNTAX SUCCESS!\n");
        }
    }
    Lexem peek() { return lexems.get(pos); }
    Lexem next() {
        generateSpaces();
        add(lexems.get(pos).toString()+"\n");
        return lexems.get(pos++);
    }
    void add(String s) { sb.append(s); }
    void generateSyntaxError(int p, String exp, String found) {
        sb.setLength(0);
        ok = false;
        posError = p;
        sb.append("Syntax Error in (line: ").append(lexems.get(pos - 1).line)
                .append(" , col: ")
                .append(lexems.get(posError).col)
                .append(")\n")
                .append("EXPECTED:   \"")
                .append(exp).append("\"\nFOUND:   \"")
                .append(found).append("\"");
    }
    void expected(String exp){
        Lexem l = next();
        if(!exp.equals(l.val)) {
            generateSyntaxError(pos - 1, exp, l.val);
        }
    }
    void generateSpaces() {
        for(int i = 0; i < depth; i++) add("    ");
    }
    void stmt(){
        generateSpaces(); depth++; add("stmt {\n");
        Lexem x = peek();
        if(x.val.equals("if")){
            expected("if");
            expected("(");
            if(ok && peek().type == 1) next();
            else if(ok) generateSyntaxError(pos, "IDENT", peek().val);
            if(ok) expected(")");
            if(ok) stmt();
        }else  if(x.type == 1) {
            next();
            expected("=");
            if(ok && peek().type == 2) next();
            else if(ok) generateSyntaxError(pos,"NUMBER", peek().val);
            if(ok) expected(";");
        } else if(x.val.equals("{")){
            expected("{");
            if(ok) seq();
            if(ok) expected("}");
        } else {
            generateSyntaxError(pos, "IF or IDENT or \"{\"", x.val);
        }
        if(ok) {
            depth--;
            generateSpaces(); add("} stmt\n");
        }
    }
    void seq(){
        Lexem x = peek();
        generateSpaces(); depth++; add("seq {\n");
        if(x.val.equals("if") || x.type == 1 || x.val.equals("{")){
            stmt();
            if(ok) seq();
        }
        if(ok) {
            depth--;
            generateSpaces(); add("} seq\n");
        }
    }
}
