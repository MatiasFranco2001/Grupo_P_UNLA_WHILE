package GrupoP.TP.ast;

import java.util.Map;
//reserva memoria para generar el ast
public interface ASTNode {
	public Object execute(Map<String, Object> symbolTable);
}
