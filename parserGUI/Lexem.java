import java.util.*;
public class Lexem {
    public int type;
    public String val;
    public int line, col;
    Lexem(int t, String s, int l, int c){
        this.type = t;
        this.val = s;
        this.line = l;
        this.col = c;
    }
    private String choosingType(){
        String s = "END";
        switch(type){
            case 1:
                s = "IDENT";
                break;
            case 2:
                s = "NUMBER";
                break;
            case 3:
                switch(val){
                    case "{":
                        s = "LBRACE";
                        break;
                    case "}":
                        s = "RBRACE";
                        break;
                    case "(":
                        s = "LPAREN";
                        break;
                    case ")":
                        s = "RPAREN";
                        break;
                    case "=":
                        s = "EQUAL";
                        break;
                    case ";":
                        s = "SEMICOLON";
                        break;
                }
        }
        return s;
    }
    public String toString(){
        String s = choosingType();
        return "type " + s + " string: \"" + val + "\"  (line: " + line + " col: " + col+")";
    }
}

