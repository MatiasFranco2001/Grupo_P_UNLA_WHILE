package GrupoP.TP;
import java.io.IOException;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class Main {

    private static final String EXTENSION = "simp";

    public static void main(String[] args) throws IOException {
    	String program = args.length > 0 ? args[0] : "test/test." + EXTENSION;

        System.out.println("Procesando archivo: " + program);

        // 1. Lexer y Parser
        SimpleGrammarLexer lexer = new SimpleGrammarLexer(new ANTLRFileStream(program));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SimpleGrammarParser parser = new SimpleGrammarParser(tokens);

        // 2. Construcción del árbol
        ParseTree tree = parser.programa();

        // 3. ANÁLISIS SEMÁNTICO
        System.out.println("--- Iniciando Análisis Semántico ---");
        SemanticAnalyzer semanticVisitor = new SemanticAnalyzer();
        semanticVisitor.visit(tree);
        
        // --- VERIFICACIÓN DE SEGURIDAD ---
        if (semanticVisitor.hasErrors()) {
            System.err.println("!!! Se encontraron errores semánticos. La ejecución fue cancelada.");
            return; // Detenemos el programa aquí si hubo errores
        }
        System.out.println("--- Análisis semántico exitoso ---");

        // 4. INTERPRETACIÓN (Ejecución)
        System.out.println("--- Iniciando Ejecución ---");
        Interpreter interpreterVisitor = new Interpreter();
        interpreterVisitor.visit(tree);

        System.out.println("--- Proceso finalizado exitosamente ---");
    }
    
}
