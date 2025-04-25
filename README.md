# My local projects
### Parser with GUI form according to this grammar(java):

\<Stmt\> ::= if ( IDENT ) \<Stmt\> | IDENT = NUMBER ; | { \<Seq\> }

\<Seq\>::= \<Stmt\> \<Seq\> | empty

Number is a sequence of digits, IDENT is a sequence of digits and letters, starting from letter;

The program creates one window with two panels, one for inputing program, another for outputting the result of syntax analyse. Also there is a small semantic analysis: the programm is printing, where you have done mistake.

### Interesting task (c++)
The task is a mini-project. The program takes as input strings written in a functional language (see the LL(1) grammar inside the commented code). The goal is to parse this program, which consists of functions, ensuring that only declared variables are used within the functions (a small semantic analysis is involved).

Next, the functions of the program need to be split into modules that depend on each other. This must be done in such a way that no cyclic dependencies between modules are possible (i.e., mutually recursive functions must reside in the same module).

Finally, the program should output an estimate of the maximum number of such modules (using a dependency graph construction and Tarjan's algorithm to find strongly connected components).
