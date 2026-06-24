package GrupoP.TP;

import java.util.HashSet;
import java.util.Set;
import GrupoP.TP.ast.*; 

public class SemanticAnalyzer extends SimpleGrammarBaseVisitor<Object> {

	private Set<String> declaredVariables = new HashSet<>();
    private boolean hasErrors = false;

    public boolean hasErrors() {
        return hasErrors;
    }

    @Override//se encarga de eliminar duplicidades en las variables
    public Object visitVar_decl(SimpleGrammarParser.Var_declContext ctx) {
        String varName = ctx.ID().getText();
        if (declaredVariables.contains(varName)) {
            System.err.println("ERROR SEMÁNTICO: La variable '" + varName + "' ya fue declarada.");
            hasErrors = true;
        } else {
            declaredVariables.add(varName);
        }
        return null;
    }

    @Override//verifica existencia de la variable(escritura)
    public Object visitVar_assign(SimpleGrammarParser.Var_assignContext ctx) {
        String varName = ctx.ID().getText();

        if (!declaredVariables.contains(varName)) {
            System.err.println("ERROR SEMÁNTICO: La variable '" + varName + "' se intenta asignar sin haber sido declarada.");
            hasErrors = true;
        }
        return visit(ctx.expresion());
    }

    @Override//verifica existencia de la variable(lectura)
    public Object visitTermino(SimpleGrammarParser.TerminoContext ctx) {
        if (ctx.ID() != null) {
            String varName = ctx.ID().getText();
            if (!declaredVariables.contains(varName)) {
                System.err.println("ERROR SEMÁNTICO: Variable '" + varName + "' utilizada sin previa declaración.");
                hasErrors = true;
            }
        }
        return super.visitTermino(ctx);
    }

    @Override
    public Object visitMostrar(SimpleGrammarParser.MostrarContext ctx) {
        return visit(ctx.expresion());
    }

    @Override//valida estructura interna del if
    public Object visitCondicional(SimpleGrammarParser.CondicionalContext ctx) {
        visit(ctx.condicion());
        // verifica ambos bloques
        for (SimpleGrammarParser.SentenciaContext s : ctx.sentencia()) {
            visit(s);
        }
        return null;
    }

    @Override//valida estructura interna del while
    public Object visitBucle_mientras(SimpleGrammarParser.Bucle_mientrasContext ctx) {
        visit(ctx.condicion());
        for (SimpleGrammarParser.SentenciaContext s : ctx.sentencia()) {
            visit(s);
        }
        return null;
    }
}
