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
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;

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
        Expression intExpr = parser.parse(ParseStart.EXPRESSION, Providers.provider("(String)(true?4:7) + (String)5")).getResult().get();
//        stringExpr.ifStringLiteralExpr(System.out::println);
//        stringExpr.walk(System.out::println);
        
//        System.out.println(eval(intExpr));
        
//        System.out.println(((CastExpr)parser.parse(ParseStart.EXPRESSION, Providers.provider("(Bagas)3")).getResult().get()).getTypeAsString());
        
        
        System.out.println(eval(parser.parse(ParseStart.EXPRESSION, Providers.provider("(double)((\"asdf\" == \"\")?4:7) + 5 + 3.5")).getResult().get()));
        System.out.println(eval(parser.parse(ParseStart.EXPRESSION, Providers.provider("((true || false)?4:7) + 5")).getResult().get()));
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
            Object value = eval(ce.getExpression());
             
            return convert(value, ce.getTypeAsString());
        }
        return null;
    }

    public static Object convert(Object value, String type)
    {
        if ((type.equals("Boolean") || type.equals("boolean")))
            {
                if (value instanceof Boolean)
                    return (Boolean)value;
                else if (value instanceof Integer)
                    return (Boolean)((Integer)value == 0);
                else if (value instanceof Double)
                    return (Boolean)((Double)value == 0);
                else if (value instanceof String)
                    return (Boolean)(((String)value).equalsIgnoreCase("true")?true:((String)value).equalsIgnoreCase("false")?false:null);
                else return null;
            }
            else if ((type.equals("Integer") || type.equals("int")))
            {
                if (value instanceof Boolean)
                    return (Integer)(((Boolean)value)?1:0);
                else if (value instanceof Integer)
                    return (Integer)value;
                else if (value instanceof Double)
                    return (Integer)(int)((Double)value).doubleValue();
                else if (value instanceof String)
                    return Integer.parseInt((String)value); // can replace with dfa
                else return null;
            }
            else if ((type.equals("Double") || type.equals("double")))
            {
                if (value instanceof Boolean)
                    return (Boolean)value?1:0;
                else if (value instanceof Integer)
                    return (Double)(double)((Integer) value).intValue();
                else if (value instanceof Double)
                    return (Double)value;
                else if (value instanceof String)
                    return Double.parseDouble((String)value); // can replace with dfa
                else return null;
            }
            else if (type.equals("String"))
            {
                return String.format("%s", value);
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
    
    // boolean integer float string
    
    // 0001 -> do nothing
    // 0010 -> do nothing
    // 0011 -> convert both to String
    // 0100 -> do nothing
    // 0101 -> convert both to String
    // 0110 -> convert both to Float
    // 1000 -> do nothing
    // 1001 -> convert both to String
    // 1010 -> convert both to float
    // 1100 -> convert both to integer
    
    // allowed values {boolean, int, float, String}
    private static Object performOperation (BinaryExpr.Operator operator, Expression leftExpr, Expression rightExpr)
    {
        Object left = eval(leftExpr);
        Object right = eval(rightExpr);
        
        if (left instanceof String || right instanceof String)
        {
            left = convert(left, "String");
            right = convert(right, "String");
        }
        else if (right instanceof Double || left instanceof Double)
        {
            left = convert(left, "Double");
            right = convert(right, "Double");
        }
        else if (right instanceof Integer || left instanceof Integer)
        {
            left = convert(left, "Integer");
            right = convert(right, "Integer");
        }
        
        
        switch (operator)
        {
            case OR: return ((Boolean) left) || ((Boolean)right);
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
