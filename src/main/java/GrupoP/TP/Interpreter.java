package GrupoP.TP;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import GrupoP.TP.ast.*;

public class Interpreter extends SimpleGrammarBaseVisitor<Object>{

	private Map<String, Object> symbolTable = new HashMap<>();

    @Override//recorre los nodos del AST
    public Object visitPrograma(SimpleGrammarParser.ProgramaContext ctx) {
        for (SimpleGrammarParser.SentenciaContext s : ctx.sentencia()) {
            if (s.node != null) {
                s.node.execute(symbolTable);
            }
        }
        return null;
    }
    ///validadores
    //operaciones aritmeticas
    public static Object evaluarOperacion(Object v1, Object v2, String op) {
        // Concatenar strings
        if (op.equals("+") && (v1 instanceof String || v2 instanceof String)) {
            return String.valueOf(v1) + String.valueOf(v2);
        }

        // operaciones de numeros
        if (v1 instanceof Number && v2 instanceof Number) {
            double d1 = ((Number) v1).doubleValue();
            double d2 = ((Number) v2).doubleValue();

            switch (op) {//tipo operacion
                case "+": return (v1 instanceof Double || v2 instanceof Double) ? (d1 + d2) : (((Number) v1).intValue() + ((Number) v2).intValue());
                case "-": return (v1 instanceof Double || v2 instanceof Double) ? (d1 - d2) : (((Number) v1).intValue() - ((Number) v2).intValue());
                case "*": return (v1 instanceof Double || v2 instanceof Double) ? (d1 * d2) : (((Number) v1).intValue() * ((Number) v2).intValue());
                case "/": 
                    if (d2 == 0) throw new RuntimeException("Error: División por cero");
                    return d1 / d2;
                case "%": return d1 % d2;
                default: throw new RuntimeException("Operador no soportado: " + op);
            }
        }

        // operaciones invalidas
        throw new RuntimeException("Error: Operación '" + op + "' inválida entre " 
            + v1.getClass().getSimpleName() + " y " + v2.getClass().getSimpleName());
    }
    //operaciones comparativas
    public static Object evaluarComparacion(Object v1, Object v2, String op) {
        // Validación de que ambos sean números
        if (v1 instanceof Number && v2 instanceof Number) {
            double d1 = ((Number) v1).doubleValue();
            double d2 = ((Number) v2).doubleValue();

            switch (op) {
                case "==": return d1 == d2;
                case "!=": return d1 != d2;
                case "<":  return d1 < d2;
                case "<=": return d1 <= d2;
                case ">":  return d1 > d2;
                case ">=": return d1 >= d2;
                default: throw new RuntimeException("Operador de comparación no soportado: " + op);
            }
        }
        throw new RuntimeException("Error: Comparación no numérica entre " 
            + v1.getClass().getSimpleName() + " y " + v2.getClass().getSimpleName());
    }
    //not
    public static Object evaluarLogica(Object v1, String op) {
        if (op.equals("!")) {
            if (v1 instanceof Boolean) {
                return !(boolean) v1;
            }
            throw new RuntimeException("Error: La negación '!' solo se puede aplicar a Booleanos, no a " 
                + v1.getClass().getSimpleName());
        }
        throw new RuntimeException("Operador lógico no soportado: " + op);
    }
}

