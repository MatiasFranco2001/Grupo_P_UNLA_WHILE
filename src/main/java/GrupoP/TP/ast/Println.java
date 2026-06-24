package GrupoP.TP.ast;

import java.util.Map;

public class Println implements ASTNode {

	private ASTNode data;
	
	public Println(ASTNode data) {
		super();
		this.data = data;
	}

	@Override//no necesita llamar al interprete
	public Object execute(Map<String, Object> symbolTable) {
		System.out.println(data.execute(symbolTable));
		return null;
	}
}
