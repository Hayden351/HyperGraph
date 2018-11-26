package misc;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseStart;
import com.github.javaparser.Providers;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.expr.DoubleLiteralExpr;
import com.github.javaparser.ast.expr.EnclosedExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.PrimitiveType.Primitive;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.Type;
import java.util.Comparator;
import java.util.Objects;

/**
 * @author Hayden Fields
 */
public class JavaParserMisc
{
    public static void main (String[] args)
    {
        String str = "";
        
        JavaParser parser = new JavaParser();
        Expression stringExpr = parser.parse(ParseStart.EXPRESSION, Providers.provider("(int)\"asdf\" + \"sdg\"")).getResult().get();
        Expression intExpr = parser.parse(ParseStart.EXPRESSION, Providers.provider("(true?4:7) + 5")).getResult().get();
        stringExpr.ifStringLiteralExpr(System.out::println);
        stringExpr.walk(System.out::println);
        
        System.out.println(eval(intExpr));
        
    }
    
    public static Object eval(Expression expr)
    {
        if (expr.isBinaryExpr())
        {
            BinaryExpr op = (BinaryExpr)expr;
            return performOperation(op.getOperator(), op.getLeft(), op.getRight());
        }
        else if (expr.isConditionalExpr())
        {
            ConditionalExpr condExpr = (ConditionalExpr)expr;
            if ((Boolean)eval(condExpr.getCondition()))
                return eval(condExpr.getThenExpr());
            else
                return eval(condExpr.getElseExpr());
                    
        }
        else if (expr.isBooleanLiteralExpr())
            return ((BooleanLiteralExpr)expr).getValue();
        else if (expr.isIntegerLiteralExpr())
            return Integer.parseInt(((IntegerLiteralExpr)expr).getValue());
        else if (expr.isDoubleLiteralExpr())
            return Double.parseDouble(((DoubleLiteralExpr)expr).getValue());
        else if (expr.isStringLiteralExpr())
            return ((StringLiteralExpr)expr).getValue();
        else if (expr.isEnclosedExpr())
            return eval(((EnclosedExpr)expr).getInner());
        else if (expr.isCastExpr())
        {
            CastExpr ce = (CastExpr)expr;
            ;
            convertJavaParserTypeToType(ce.getType());
            return eval(ce.getExpression());
        }
        return null;
    }

    // https://stackoverflow.com/questions/41535413/how-to-infer-the-types-of-all-the-parameters-of-a-function-using-java-parser-and
    // https://github.com/javaparser/javasymbolsolver
    private static Class<?> convertJavaParserTypeToType (Type type)
    {
        if (type.isPrimitiveType())
        {
            Primitive primitiveType = ((PrimitiveType)type).getType();
            
            switch (primitiveType)
            {
                case BOOLEAN: return boolean.class;
                case CHAR: return char.class;
                case BYTE: return byte.class;
                case SHORT: return short.class;
                case INT: return int.class;
                case LONG: return long.class;
                case FLOAT: return float.class;
                case DOUBLE: return double.class;
            }
            
        }
        else if (type.isReferenceType())
        {
            
            ReferenceType referenceType = (ReferenceType)type;
            
            
            referenceType.asClassOrInterfaceType();
        }
        throw new IllegalArgumentException(type.toString());
    }

    public static class Comp implements Comparator
    {
        @Override
        public int compare (Object o1, Object o2)
        {
            Comparable left = (Comparable)o1;
            Comparable right = (Comparable)o2;
            return left.compareTo(right);
          
        }
    }
    
    // allowed values {boolean, int, float, String}
    private static Object performOperation (BinaryExpr.Operator operator, Expression leftExpr, Expression rightExpr)
    {
        Object left = eval(leftExpr);
        Object right = eval(rightExpr);
        switch (operator)
        {
            case OR: return ((Boolean)left) || ((Boolean)right);
            case AND: return ((Boolean)left) && ((Boolean)right);
            case BINARY_OR:
            {
                if (left instanceof Integer && right instanceof Integer)
                    return ((Integer) left) | ((Integer) right);
                else
                    return ((Boolean) left) | ((Boolean) right);
            }
            case BINARY_AND:
                if (left instanceof Integer && right instanceof Integer)
                    return ((Integer) left) & ((Integer) right);
                else
                    return ((Boolean) left) & ((Boolean) right);
            case XOR:
                if (left instanceof Integer && right instanceof Integer)
                    return ((Integer) left) ^ ((Integer) right);
                else
                    return ((Boolean) left) ^ ((Boolean) right);
            case EQUALS:
                return Objects.equals(left, right);
            case NOT_EQUALS:
                return !Objects.equals(left, right);
            case LESS:
                return Objects.compare(left, right, new Comp()) < 0;
            case GREATER:
                return Objects.compare(left, right, new Comp()) > 0;
            case LESS_EQUALS:
                return Objects.compare(left, right, new Comp()) <= 0;
            case GREATER_EQUALS:
                return Objects.compare(left, right, new Comp()) >= 0;
            case LEFT_SHIFT:
                return ((Integer)left) << ((Integer)right);
            case SIGNED_RIGHT_SHIFT:
                return ((Integer)left) >> ((Integer)right);
            case UNSIGNED_RIGHT_SHIFT:
                return ((Integer)left) >>> ((Integer)right);
            case PLUS:
                if (left instanceof Integer && right instanceof Integer)
                    return ((Integer) left) + ((Integer) right);
                else if (left instanceof Double && right instanceof Double)
                    return ((Double) left) + ((Double) right);
                else
                    return ((String) left) + ((String) right);
            case MINUS:
                if (left instanceof Integer && right instanceof Integer)
                    return ((Integer) left) + ((Integer) right);
                else
                    return ((Double) left) + ((Double) right);
            case MULTIPLY:
                if (left instanceof Integer && right instanceof Integer)
                    return ((Integer) left) + ((Integer) right);
                else
                    return ((Double) left) + ((Double) right);
            case DIVIDE:
                if (left instanceof Integer && right instanceof Integer)
                    return ((Integer) left) + ((Integer) right);
                else
                    return ((Double) left) + ((Double) right);
            case REMAINDER:
                if (left instanceof Integer && right instanceof Integer)
                    return ((Integer) left) + ((Integer) right);
                else
                    return ((Double) left) + ((Double) right);
            default: return null;
        }
    }
}
