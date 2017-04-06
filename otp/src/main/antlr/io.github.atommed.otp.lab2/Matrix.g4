grammar Matrix;

@header{
package io.github.atommed.otp.lab2;
}

defProg: statement | (statement ';')+ statement?;
//defProg: statement (';' statement)+ ';'?;
//defProg: (statement ';')* statement?;

statement: assign | expr;

//group: '(' expr ')';
expr:
    expr '^t' #Transpose
    | expr '^1' #Inverse
    | '-' expr #Negate
    | expr op=(DIVIDE | MULTIPLY | VECMULT | MATMULT) expr #MulDiv
    | expr op=(PLUS | MINUS) expr #AddSub
    | ID '(' expr ')' #Funcall
    | literal #LiteralValue
    | ID #Variable
    | '|' expr '|' #Module
    | '(' expr ')' #Group;

literal:
 vector #VectorLiteral
 | NUMBER #NumLiteral;
//funcall: ID '(' expr (',' expr)* ')';
assign: ID '=' expr;

vector: '[' expr (',' expr)* ']';

DIVIDE: '/';
MULTIPLY: '*';
VECMULT: 'x';
MATMULT: '.';
PLUS: '+';
MINUS: '-';

NUMBER: [0-9]+ ('.' [0-9]+)?;
ID: [_a-zA-Z] [_a-zA-Z0-9]* ;

WS: [\r\n\t ]->skip;