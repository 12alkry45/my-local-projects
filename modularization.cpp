#include<iostream>
#include<vector>
#include<queue>
#include<string>
#include<stack>
#include<map>
using namespace std;
const int NUMBER = 1;          
const int IDENT = 2;            
const int PLUS = 3;              
const int MINUS  = 11;             
const int MUL = 4;               
const int DIV = 4;               
const int LPAREN = 5;                
const int RPAREN = 6;
const int SR = 7;
const int DEF = 8;
const int TER = 9;
const int ZN = 10;
//const int SPEC = 3;
////////////////////   lexer /////////////////

// <lexems> ::= <lexem> <spaces> <lexems> | empty
// <spaces> ::= SPACE <spaces> | empty
// <lexem> ::= <ident> | <number> | <special>
// <ident> ::= LETTER <ident-tail>
// <ident-tail> ::= LETTER <ident-tail> | NUM <ident-tail> | empty
// <number> ::= DIGIT <number-tail>
// <number-tail> ::= DIGIT <number-tail> | empty
// <special> ::= («+», «-», «*», «/»),  («=», «<>», «<», «>», «<=», «>=»), ("?", ":"), (":="), (",", ";"), ("(", ")")

bool is_error = false;
struct Lex{
    int tag; 
    string s;
};

void scan_lexems(queue<char> &stream, vector<Lex> &lexems);
void scan_lexem(queue<char> &stream, vector<Lex> &lexems);
void scan_spaces(queue<char> &stream, vector<Lex> &lexems);
Lex scan_ident(queue<char> &stream, vector<Lex> &lexems);
string scan_ident_tail(queue<char> &stream, vector<Lex> &lexems);
Lex scan_number(queue<char> &stream, vector<Lex> &lexems);
string scan_number_tail(queue<char> &stream, vector<Lex> &lexems);
Lex scan_special(queue<char> &stream, vector<Lex> &lexems);

void lexer(queue<char> &stream, vector<Lex> &lexems){
    scan_lexems(stream, lexems);
    if(stream.front() != (char)0) is_error = true;
}

bool is_spec(char x){
    vector<char> specials = {'+', '-', '*', '/','=','<','>', '?', ':', ',', ';','(', ')' };
    bool is_inspecials = false;
    for(int i = 0; i < specials.size(); i++){
        if(x == specials[i]) is_inspecials = true;
    }
    return is_inspecials;
}
bool start_lexem(char x){
    return isalpha(x) != 0 || isdigit(x) != 0 || is_spec(x);
}
void scan_lexems(queue<char> &stream, vector<Lex> &lexems){
    if(start_lexem(stream.front())){
        scan_lexem(stream, lexems);
        if(!is_error) scan_lexems(stream, lexems);
    }
    else if(isspace(stream.front()) != 0){
        scan_spaces(stream, lexems);
        scan_lexems(stream, lexems);
    }
}
void scan_spaces(queue<char> &stream, vector<Lex> &lexems){
    if(isspace(stream.front()) != 0){
        stream.pop();
        scan_spaces(stream, lexems);
    }
}
void scan_lexem(queue<char> &stream, vector<Lex> &lexems){
    if(isalpha(stream.front()) != 0) lexems.push_back(scan_ident(stream, lexems));
    else if(isdigit(stream.front()) != 0) lexems.push_back(scan_number(stream, lexems));
    else if(is_spec(stream.front())) lexems.push_back(scan_special(stream, lexems));
    else is_error = true;
}
Lex scan_ident(queue<char> &stream, vector<Lex> &lexems){
    Lex ident;
    ident.tag = IDENT;
    char letter = stream.front();
    stream.pop();
    ident.s = string(1, letter) + scan_ident_tail(stream, lexems);
    return ident;
}
string scan_ident_tail(queue<char> &stream, vector<Lex> &lexems){
    if(isalpha(stream.front()) != 0 || isdigit(stream.front()) != 0){
        char symb = stream.front();
        stream.pop();
        return string(1, symb) + scan_ident_tail(stream, lexems);
    }
    else return "";
}
Lex scan_number(queue<char> &stream, vector<Lex> &lexems){
    char digit = stream.front();
    stream.pop();
    Lex number;
    number.tag = NUMBER;
    number.s = string(1, digit) + scan_number_tail(stream, lexems);
    return number; 
}
string scan_number_tail(queue<char> &stream, vector<Lex> &lexems){
    if(isdigit(stream.front()) != 0){
        char digit = stream.front();
        stream.pop();
        return string(1, digit) + scan_number_tail(stream, lexems);
    }
    else return "";
}
Lex scan_special(queue<char> &stream, vector<Lex> &lexems){
    char x = stream.front();
    int tag;
    string symb;
    Lex sp;
    if(x == '+')       { tag = PLUS; symb = "+"; stream.pop(); }
    else if(x == '-')  { tag = MINUS; symb = "-"; stream.pop(); }
    else if(x == '*')  { tag = MUL; symb = "*"; stream.pop(); }
    else if(x == '/')  { tag = DIV; symb = "/"; stream.pop(); }
    else if(x == '(')  { tag = LPAREN; symb = "("; stream.pop(); }
    else if(x == ')')  { tag = RPAREN; symb = ")"; stream.pop(); }
    else if(x == ',')  { tag = ZN; symb = ","; stream.pop(); }
    else if(x == ';')  { tag = ZN; symb = ";"; stream.pop(); }
    else if(x == '?')  { tag = TER; symb = "?"; stream.pop(); }
    else if(x == '=')  { tag = SR; symb = "="; stream.pop(); }
    else{
        stream.pop();
        char y = stream.front();
        if(isspace(y) != 0) scan_spaces(stream, lexems);
        if(x == '<' && y == '=')      { tag = SR; symb = "<="; stream.pop(); }
        else if(x == '>' && y == '=') { tag = SR; symb = ">="; stream.pop(); }
        else if(x == '<' && y == '>') { tag = SR; symb = "<>"; stream.pop(); }
        else if(x == '<')             { tag = SR; symb = "<"; }
        else if(x == '>')             { tag = SR; symb = ">"; }
        else if(x == ':' && y == '=') { tag = DEF; symb = ":="; stream.pop(); }
        else if(x == ':')             { tag = TER; symb = ":"; }
        else { is_error = true; tag = 0; symb = "error"; }
    }
    sp.tag = tag;
    sp.s = symb;
    return sp;
}

/////////////////////////////        parser         ////////////////////////////////

vector<vector<int>> graph;
map<string, int> display;
map<string, bool> is_defining;
map<int, int> number_of_param;
int count_of_func = 0, cur_function = 0;
//  <program> ::= <function> <program> | empty
//  <function> ::= <ident> ( <formal-args-list> ) := <expr> ;
//  <formal-args-list> ::= <ident-list> | empty
//  <ident-list> ::= <ident> <ident-list-tail>
//  <ident-list-tail> ::= , <ident> <ident-list-tail> | empty
//  <expr> ::= <comparison_expr> <expr_tail>
//  <expr_tail> ::= ? <comparison_expr> : <expr> | empty
//  <comparison_expr> ::= <arith_expr> <comparison_expr_tail>
//  <comparison_expr_tail> ::= <comparison_op> <arith_expr> | empty 
//  <comparison_op> ::= = | <> | < | > | <= | >= .
//  <arith_expr>  ::= <term> <arith_expr'>.
//  <arith_expr'> ::= + <term> <arith_expr'> | - <term> <arith_expr'> | .
//  <trem>  ::= <factor> <term'>.
//  <trem'> ::= * <factor> <trem'> | / <factor> <term'> | .
//  <factor> ::= <number> | <ident> <ident_continue> | ( <expr> ) | - <factor>.
//  <ident_continue> ::= ( <actual_args_list> ) | empty
//  <actual_args_list> ::= <expr-list> | .
//  <expr-list> ::= <expr><expr-list-tail>
//  <expr-list-tail> ::=  , <expr><expr-list-tail> | empty

void parse_program(queue<Lex> &stream);
void parse_function(queue<Lex> &stream);
void parse_formal_args_list(queue<Lex> &stream, int &n, map<string, bool> &args);
void parse_ident_list(queue<Lex> &stream, int &n, map<string, bool> &args);
void parse_ident_list_tail(queue<Lex> &stream, int &n, map<string, bool> &args);
void parse_expr(queue<Lex> &stream, map<string, bool> &args);
void parse_expr_tail(queue<Lex> &stream, map<string, bool> &args);
void parse_comparison_expr(queue<Lex> &stream, map<string, bool> &args);
void parse_comparison_expr_tail(queue<Lex> &stream, map<string, bool> &args);
void parse_arith_expr(queue<Lex> &stream, map<string, bool> &args);
void parse_arith_expr1(queue<Lex> &stream, map<string, bool> &args);
void parse_term(queue<Lex> &stream, map<string, bool> &args);
void parse_term1(queue<Lex> &stream, map<string, bool> &args);
void parse_factor(queue<Lex> &stream, map<string, bool> &args);
void parse_ident_continue(queue<Lex> &stream, Lex name, map<string, bool> &args);
void parse_actual_args_list(queue<Lex> &stream, int &n, map<string, bool> &args);
void parse_expr_list(queue<Lex> &stream, int &n, map<string, bool> &args);
void parse_expr_list_tail(queue<Lex> &stream, int &n, map<string, bool> &args);

Lex next(queue<Lex> &stream){
    Lex x = stream.front();
    stream.pop();
    return x;
}
Lex expect(queue<Lex>& stream, Lex expected){
    Lex token = stream.front();
    if(token.tag != expected.tag || token.s != expected.s){
        is_error = true;
    }
    return next(stream);
}
void parser(queue<Lex> &stream){
    is_error = false;
    parse_program(stream);
    if(stream.front().tag != -1) is_error = true;
}
void parse_program(queue<Lex> &stream){
    if(stream.front().tag == IDENT){
        parse_function(stream);
        if(!is_error) parse_program(stream);
    }
}
void parse_function(queue<Lex> &stream){
    if(stream.front().tag == IDENT){
        Lex function_name = next(stream);
        if(!display.count(function_name.s)) {
            display[function_name.s] = count_of_func++;
            graph.push_back(vector<int>());
        }
        cur_function = display[function_name.s];
        if(is_defining.count(function_name.s)) is_error = true;
        else is_defining[function_name.s] = true;

        expect(stream, Lex{LPAREN, "("});
        int count_of_arg = 0;
        map<string, bool> args;
        parse_formal_args_list(stream, count_of_arg, args);
        if(!number_of_param.count(cur_function)) number_of_param[cur_function]=count_of_arg;
        if(number_of_param[cur_function]!=count_of_arg || count_of_arg != args.size()) is_error = true;
        expect(stream, Lex{RPAREN, ")"});
        expect(stream, Lex{DEF, ":="});
        parse_expr(stream, args);
        expect(stream, Lex{ZN, ";"});
    }
    else is_error = true;
}
void parse_formal_args_list(queue<Lex> &stream, int &n, map<string, bool> &args){
    if(stream.front().tag == IDENT){
        parse_ident_list(stream, n, args);
    }
}
void parse_ident_list(queue<Lex> &stream, int &n, map<string, bool> &args){
    if(stream.front().tag == IDENT){
        Lex l = next(stream);
        args[l.s] = true;
        n++;
        parse_ident_list_tail(stream, n, args);
    }
    else is_error = true;
}
void parse_ident_list_tail(queue<Lex> &stream, int &n, map<string, bool> &args){
    if(stream.front().s == ","){
        next(stream);
        Lex l =expect(stream, Lex{IDENT, stream.front().s});
        args[l.s] = true;
        n++;
        if(!is_error) parse_ident_list_tail(stream, n, args);
    }
}
bool start_token(Lex x){
    return x.tag == IDENT || x.tag == NUMBER || x.tag == LPAREN || x.tag == MINUS;
}
void parse_expr(queue<Lex> &stream, map<string, bool> &args){
    if(start_token(stream.front())){
        parse_comparison_expr(stream, args);
        if(!is_error) parse_expr_tail(stream, args);
    }
    else is_error = true;
}
void parse_expr_tail(queue<Lex> &stream, map<string, bool> &args){
    if(stream.front().s == "?"){
        expect(stream, Lex{TER, "?"});
        parse_comparison_expr(stream, args);
        expect(stream, Lex{TER, ":"});
        if(!is_error) parse_expr(stream, args);
    }
}
void parse_comparison_expr(queue<Lex> &stream, map<string, bool> &args){
    if(start_token(stream.front())){
        parse_arith_expr(stream, args);
        if(!is_error) parse_comparison_expr_tail(stream, args);
    }
    else is_error = true;
}
void parse_comparison_expr_tail(queue<Lex> &stream, map<string, bool> &args){
    if(stream.front().tag == SR){
        expect(stream, Lex{SR, stream.front().s});
        parse_arith_expr(stream, args);
    }
}
//  <arith_expr>  ::= <term> <arith_expr'>.
//  <arith_expr'> ::= + <term> <arith_expr'> | - <term> <arith_expr'> | .
//  <trem>  ::= <factor> <term'>.
//  <trem'> ::= * <factor> <trem'> | / <factor> <term'> | .
//  <factor> ::= <number> | <ident> <ident_continue> | ( <expr> ) | - <factor>.
//  <ident_continue> ::= ( <actual_args_list> ) | empty
void parse_arith_expr(queue<Lex> &stream, map<string, bool> &args){
    if(start_token(stream.front())){
        parse_term(stream, args);
        parse_arith_expr1(stream, args);
    }
    else is_error = true;
}
void parse_arith_expr1(queue<Lex> &stream, map<string, bool> &args){
    Lex x = stream.front();
    if(x.s == "+" || x.s == "-"){
        next(stream);
        parse_term(stream, args);
        parse_arith_expr1(stream, args);
    }
}
void parse_term(queue<Lex> &stream, map<string, bool> &args){
    if(start_token(stream.front())){
        parse_factor(stream, args);
        parse_term1(stream, args);
    }
    else is_error = true;
}
void parse_term1(queue<Lex> &stream, map<string, bool> &args){
    Lex x = stream.front();
    if(x.s == "*" || x.s == "/"){
        next(stream);
        parse_factor(stream, args);
        parse_term1(stream, args);
    }
}
void parse_factor(queue<Lex> &stream, map<string, bool> &args){
    Lex x = stream.front();
    if(x.tag == NUMBER) next(stream);
    else if(x.tag == IDENT){
        Lex name = next(stream);
        parse_ident_continue(stream, name, args);
    }
    else if(x.tag == LPAREN){
        expect(stream, Lex{LPAREN, "("});
        parse_expr(stream, args);
        expect(stream, Lex{RPAREN, ")"});
    }
    else if(x.tag == MINUS){
        next(stream);
        parse_factor(stream, args);
    }
    else is_error = true;
}
void parse_ident_continue(queue<Lex> &stream, Lex name, map<string, bool> &args){
    if(stream.front().tag == LPAREN){
        if(!display.count(name.s)){
            display[name.s] = count_of_func++;
            graph.push_back(vector<int>());
        }
        graph[cur_function].push_back(display[name.s]);

        expect(stream, Lex{LPAREN, "("});
        int count_of_arg = 0;
        parse_actual_args_list(stream, count_of_arg, args);
        if(!number_of_param.count(display[name.s])) number_of_param[display[name.s]]=count_of_arg;
        if(number_of_param[display[name.s]]!=count_of_arg) is_error = true;
        expect(stream, Lex{RPAREN, ")"});
    }else{
        if(!args[name.s]) is_error = true;
    }
}
//  <actual_args_list> ::= <expr-list> | .
//  <expr-list> ::= <expr> <expr-list-tail>
//  <expr-list-tail> ::=  , <expr> <expr-list-tail>| empty
void parse_actual_args_list(queue<Lex> &stream, int &n, map<string, bool> &args){
    if(start_token(stream.front())){
        parse_expr_list(stream, n, args);
    }
}
void parse_expr_list(queue<Lex> &stream, int &n, map<string, bool> &args){
    if(start_token(stream.front())){
        parse_expr(stream, args);
        n++;
        if(!is_error) parse_expr_list_tail(stream, n, args);
    }
    else is_error = true;
}
void parse_expr_list_tail(queue<Lex> &stream, int &n, map<string, bool> &args){
    if(stream.front().s == ","){
        next(stream);
        parse_expr(stream, args);
        n++;
        if(!is_error) parse_expr_list_tail(stream, n, args);
    }
}
/////////////////////////   conmponenets  /////////////////////////////////
int time_visit = 1, countComp = 1;
struct Vert{
    int t1, low, comp;
};
void Visit(vector<vector<int>> g, vector<Vert> &v, int v1, stack<int> &s){
    v[v1].t1 = v[v1].low = time_visit++;
    s.push(v1);
    for(int u: g[v1]){
        if(v[u].t1 == 0) Visit(g, v, u, s);
        if(v[u].comp == 0 && v[v1].low > v[u].low) v[v1].low = v[u].low;
    }
    if(v[v1].t1 == v[v1].low){
        int u;
        do {
            u = s.top();
            s.pop();
            v[u].comp = countComp;
        } while (u != v1);
        countComp++;
    }
}
void Tarjan(vector<vector<int>> g, vector<Vert> &v, int n){
    for(int i = 0; i < n; i++) v[i].t1 = v[i].comp = 0;
    stack<int> s;
    for(int i = 0; i < n; i++){
        if(v[i].t1 == 0){
            Visit(g, v, i, s);
        }
    }
}

int main(){
    string s;
    queue<char> stream;
    vector<Lex> lexems;
    while(getline(cin, s)) {
        for(int i = 0; i < s.size(); i++) stream.push(s[i]);
    }
    stream.push((char)0);
    lexer(stream, lexems);
    if(is_error) cout << "error";
    else{
        queue<Lex> stream_parse;
        for(Lex t: lexems) stream_parse.push(t);
        stream_parse.push(Lex{-1, "end"});
        parser(stream_parse);
        if(!is_error && is_defining.size() == display.size()){
            vector<Vert> v(display.size());
            Tarjan(graph, v, display.size());
            cout << countComp - 1;
        }
        else{
            cout << "error";
        }

    }
}
