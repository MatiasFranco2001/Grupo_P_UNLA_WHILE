package GrupoP.TP.ast;

import java.util.Map;

import GrupoP.TP.Interpreter;

public class Not implements ASTNode {
	
	private ASTNode expression;
	
	

	public Not(ASTNode expression) {
		super();
		this.expression = expression;
	}

	@Override
    public Object execute(Map<String, Object> symbolTable) {
        Object value = expression.execute(symbolTable);

        return Interpreter.evaluarLogica(value, "!");
    }



}
