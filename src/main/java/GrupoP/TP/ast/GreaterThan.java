package GrupoP.TP.ast;

import java.util.Map;

import GrupoP.TP.Interpreter;

public class GreaterThan implements ASTNode {
	
	private ASTNode operand1;
	private ASTNode operand2;
	
	

	public GreaterThan(ASTNode operand1, ASTNode operand2) {
		super();
		this.operand1 = operand1;
		this.operand2 = operand2;
	}

	@Override
    public Object execute(Map<String, Object> symbolTable) {
        Object v1 = operand1.execute(symbolTable);
        Object v2 = operand2.execute(symbolTable);
        return Interpreter.evaluarComparacion(v1, v2, ">");
    }

}
