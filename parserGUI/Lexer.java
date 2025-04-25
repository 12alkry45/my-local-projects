import java.util.*;
public class Lexer {
    public List<Lexem> lexems = new ArrayList<Lexem>();
    String stream = "if( x ) { x= 0; if( y)  z = 10; } ";
    int pos = 0, textline, posError = -1, countLexem = 0;
    public boolean ok = true;
    Lexer(String s, int line){
        stream = s;
        textline = line;
        stream = stream + (char)0;
        scan_lexems();
        if(pos != stream.length() - 1) {
            ok = false;
            posError = pos;
        }
    }
    void add(Lexem l) { lexems.add(l); }
    char peek() { return stream.charAt(pos);}
    char next() { return stream.charAt(pos++);}
  
    // <lexems> ::= <lexem> <spaces> <lexems> | empty
    // <spaces > ::= SPACE <spaces>
    // <lexem>  ::= <ident> | <number> | = | ( | ) | { | } | ;
    // <ident> ::= LETTER <ident_tail>
    // <ident-tail> ::= LETTER <ident-tail> | DIGIT <ident-tail> | empty
    // <number> ::= DIGIT <number-tail>
    // <number-tail> ::= DIGIT <number-tail> | empty
  
    boolean start_lexem(char x){
        return Character.isLetter(x) || Character.isDigit(x) || x == '=' || x == '(' || x == ')' || x == '}' || x == '{' || x == ';';
    }
    void scan_lexems(){
        char x = peek();
        if(Character.isWhitespace(x)){
            scan_spaces();
            scan_lexems();
        }
        else if(start_lexem(x)){
            scan_lexem();
            scan_lexems();
        }
    }
    void scan_lexem(){
        char x = peek();
        if(Character.isLetter(x)) scan_ident();
        else if(Character.isDigit(x)) scan_number();
        else if(x == '=' || x == '(' || x == ')' || x == '}' || x == '{' || x == ';'){
            add(new Lexem(3, Character.toString(next()), textline, countLexem++));
        }
        else{
            posError = pos;
            ok = false;
        }
    }
    void scan_spaces(){
        char x = peek();
        if(Character.isWhitespace(x)) {
            next();
            scan_spaces();
        }
    }
    void scan_ident(){
        char x = peek();
        if(Character.isLetter(x)){
            String ident = Character.toString(next()) + scan_ident_tail();
            add(new Lexem(1, ident, textline, countLexem++));
        }else{
            posError = pos;
            ok = false;
        }
    }
    String scan_ident_tail(){
        char x = peek();
        if(Character.isLetter(x) || Character.isDigit(x)) return Character.toString(next()) + scan_ident_tail();
        else return "";
    }
    void scan_number(){
        char x = peek();
        if(Character.isDigit(x)){
            String num = Character.toString(next()) + scan_number_tail();
            add(new Lexem(2, num, textline, countLexem++));
        }else{
            posError = pos;
            ok = false;
        }
    }
    String scan_number_tail(){
        char x = peek();
        if(Character.isDigit(x)) return Character.toString(next()) + scan_number_tail();
        else return "";
    }
    void print(){
        for(Lexem l: lexems) System.out.println(l);
    }
}

