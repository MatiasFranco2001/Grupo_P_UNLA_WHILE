grammar SimpleGrammar;

@parser::header {
    import java.util.Map;
    import java.util.HashMap;
    import java.util.List;
    import java.util.ArrayList;
    import GrupoP.TP.ast.*;
}


programa: PROGRAMA ID LLAVE_ABIERTO 
    (s=sentencia)*
    LLAVE_CERRADO
    ;
//sentencias
sentencia returns [ASTNode node]: 
    mostrar {$node = $mostrar.node;} 
    | condicional {$node = $condicional.node;}
    | bucle_mientras {$node = $bucle_mientras.node;}
    | var_decl {$node = $var_decl.node;}
    | var_assign {$node = $var_assign.node;};
    
//if
condicional returns [ASTNode node]: SI PARENTESIS_ABIERTO c1=condicion PARENTESIS_CERRADO
				
				{
					List<ASTNode> body = new ArrayList<ASTNode>();
				}
				
				LLAVE_ABIERTO (s1=sentencia{body.add($s1.node);})* LLAVE_CERRADO
				SINO
				{
					List<ASTNode> elseBody = new ArrayList<ASTNode>();
				}
				LLAVE_ABIERTO (s2=sentencia{elseBody.add($s2.node);})* LLAVE_CERRADO
				{
					$node = new If($c1.node, body, elseBody);
				};
   
    
//while
bucle_mientras returns [ASTNode node]: 
    MIENTRAS PARENTESIS_ABIERTO c1=condicion PARENTESIS_CERRADO
    { List<ASTNode> body = new ArrayList<ASTNode>(); }
    LLAVE_ABIERTO (s1=sentencia{body.add($s1.node);})* LLAVE_CERRADO
    { $node = new While($c1.node, body); };

//comparaciones
condicion returns [ASTNode node]: 
    e1=expresion {ASTNode current = $e1.node;}
    ( GT e2=expresion {current = new GreaterThan(current, $e2.node);}
    | LT e2=expresion {current = new LessThan(current, $e2.node);}
    | EQ e2=expresion {current = new Equals(current, $e2.node);}
    | NEQ e2=expresion {current = new NotEqual(current, $e2.node);}
    | LEQ e2=expresion {current = new LessEqual(current, $e2.node);}
    | GEQ e2=expresion {current = new GreaterEqual(current, $e2.node);}
    )* { $node = current; };

//operaciones aritmeticas
expresion returns [ASTNode node]: 
    t1=factor {$node = $t1.node;}
    ( SUMA t2=factor {$node = new Addition($node, $t2.node);}
    | RESTA t2=factor {$node = new Subtraction($node, $t2.node);}
    )*;

factor returns [ASTNode node]: 
    t1=unario {$node = $t1.node;}
    ( MULTIPLICACION t2=unario {$node = new Multiplication($node,$t2.node);}
    | DIVISION t2=unario {$node = new Division($node,$t2.node);}
    | MOD t2=unario {$node = new Modulo($node, $t2.node);}
    )*;
    
 //negacion   
unario returns [ASTNode node]:
	NEGACION u=unario { $node = new Not($u.node);}
	| t=termino {$node = $t.node;}
	;
//variables
termino returns [ASTNode node]: 
    NUMERO {$node = new Constant(Integer.parseInt($NUMERO.text));}
    | BOOLEANO {$node = new Constant(Boolean.parseBoolean($BOOLEANO.text));}
    | ID {$node = new VarRef($ID.text);}
    | REAL{$node = new Constant(Double.parseDouble($REAL.text));}
    | STRING{$node = new Constant($STRING.text.replace("\"", ""));} // omite comillas
    | PARENTESIS_ABIERTO expresion {$node = $expresion.node;} PARENTESIS_CERRADO;

mostrar returns [ASTNode node]: MOSTRAR expresion PUNTO_COMA {$node = new Println($expresion.node);};
var_decl returns [ASTNode node]: VAR ID PUNTO_COMA {$node = new VarDecl($ID.text);};
var_assign returns [ASTNode node]: ID ASIGNACION expresion PUNTO_COMA {$node = new VarAssign($ID.text, $expresion.node);};

//tokens
PROGRAMA: 'programa'; 
VAR: 'var'; 
MOSTRAR: 'mostrar'; 
SI: 'si'; 
SINO: 'sino'; 
MIENTRAS: 'mientras';
SUMA: '+';
RESTA: '-'; 
MULTIPLICACION: '*'; 
DIVISION: '/';
MOD: '%';
GT:'>'; 
LT:'<'; 
GEQ: '>=';
LEQ: '<=';
EQ: '==';
NEQ: '!=';
NEGACION: '!';
ASIGNACION: '=';
LLAVE_ABIERTO: '{'; 
LLAVE_CERRADO: '}';
PARENTESIS_ABIERTO:'('; 
PARENTESIS_CERRADO:')';
PUNTO_COMA: ';';
BOOLEANO: 'true' | 'false';
REAL:[0-9]+'.'[0-9]+;
ID: [a-zA-Z_][a-zA-Z0-9]*;
NUMERO: [0-9]+;
STRING: '"' ( ~["\\] | '\\' . )* '"';
//~[\r\n]* esto quiere decir cualquier caracter que NO sea un salto de linea o retorno.
COMENTARIO_LINEA: '//' ~[\r\n]* -> skip ;
COMENTARIO_BLOQUE: '/*' .*? '*/' -> skip;
ESPACIO_BLANCO:[ \t\n\r]+ -> skip ;