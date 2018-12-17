package misc;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hayden Fields
 */
// (FindIdentifiers)(0)(0 1)(0~WHITESPACE_AND_OPERATIONS 1~IDENTIFIER)(0~  ~0 0~||~0 0~&&~0 0~^^~0 0~==~0 0~!!~0 0~<<~0 0~>>~0 0~++~0 0~--~0 0~**~0 0~//~0 0~%%~0 0~09~0 0~::~0 0~??~0 0~az~1 0~AZ~1 1~az~1 1~AZ~1 1~09~1 1~  ~0 1~||~0 1~&&~0 1~^^~0 1~==~0 1~!!~0 1~<<~0 1~>>~0 1~++~0 1~--~0 1~**~0 1~//~0 1~%%~0 0~((~0 0~))~0 1~((~0 1~))~0 1~::~0 1~??~0)
public class FindIdentifiers
{
    public static void main (String[] args)
    {
        FindIdentifiers fi = new FindIdentifiers();
        fi.map.put("asdf", "12354");
        fi.map.put("x", "3.4");
        fi.map.put("y", "5");
        fi.map.put("asdf1234", "5");
        fi.evaluate("asdf++ + asdf - (x - y) * (a & b ) ? 123 : 14 / asdf1234");
        System.out.println(fi.result);
        
    }
    // An enumerated type for the states of a finite state automata
    private enum State
    {
        // set of non final states

        // set of final states
        WHITESPACE_AND_OPERATIONS (Type.FINAL),
        IDENTIFIER                (Type.FINAL),

        // set of error states
        UNDEFINED_TRANSITION      (Type.ERROR);

        // indicates whether the state is a final, nonfinal or error state 
        private enum Type
        {
            FINAL,
            NON_FINAL,
            ERROR;    
        }

        private final Type type;

        // Initialize state with its type
        State(Type type)
        {
            this.type = type;
        }
    }

    public static boolean parse(String input)
    {
        return new FindIdentifiers().evaluate(input);
    }
    public boolean evaluate(String input)
    {
        return evaluate(input, 0);
    }
    public boolean evaluate(String input, int position)
    {
        State currentState = State.WHITESPACE_AND_OPERATIONS;
        for (;currentState.type != State.Type.ERROR && position < input.length(); position++)
        {
            char symbol = input.charAt(position);
            currentState = transition(currentState, symbol);
        } // end for
        
        switch (currentState)
        {
            case WHITESPACE_AND_OPERATIONS:
            {
            } break;
            case IDENTIFIER:
            {
                result.append(map.get(identifier.toString()));
            } break;
        }
        
        return (currentState.type == State.Type.FINAL);
    }

    StringBuilder result = new StringBuilder();
    StringBuilder identifier = new StringBuilder();
    Map<String, String> map = new HashMap<>();
    public State transition(State state, char symbol)
    {
        switch (state)
        {
            case WHITESPACE_AND_OPERATIONS:
                if ('^' == symbol)
                {
                    state = State.WHITESPACE_AND_OPERATIONS;
                    result.append(symbol);
                }
                else if ('!' == symbol)
                {
                    state = State.WHITESPACE_AND_OPERATIONS;
                    result.append(symbol);
                }
                else if ('+' == symbol)
                {
                    state = State.WHITESPACE_AND_OPERATIONS;
                    result.append(symbol);
                }
                else if ('*' == symbol)
                {
                    state = State.WHITESPACE_AND_OPERATIONS;
                    result.append(symbol);
                }
                else if ('A' <= symbol && symbol <= 'Z')
                {
                    state = State.IDENTIFIER;
                    identifier.append(symbol);
                }
                else if ('|' == symbol)
                {
                    state = State.WHITESPACE_AND_OPERATIONS;
                    result.append(symbol);
                }
                else if ('<' == symbol)
                {
                    state = State.WHITESPACE_AND_OPERATIONS;
                    result.append(symbol);
                }
                else if ('%' == symbol)
                {
                    state = State.WHITESPACE_AND_OPERATIONS;
                    result.append(symbol);
                }
                else if ('-' == symbol)
                {
                    state = State.WHITESPACE_AND_OPERATIONS;
                    result.append(symbol);
                }
                else if ('a' <= symbol && symbol <= 'z')
                {
                    state = State.IDENTIFIER;
                    identifier.append(symbol);
                }
                else if (')' == symbol)
                {
                    state = State.WHITESPACE_AND_OPERATIONS;
                    result.append(symbol);
                }
                else if ('/' == symbol)
                {
                    state = State.WHITESPACE_AND_OPERATIONS;
                    result.append(symbol);
                }
                else if (' ' == symbol)
                {
                    state = State.WHITESPACE_AND_OPERATIONS;
                    result.append(symbol);
                }
                else if ('(' == symbol)
                {
                    state = State.WHITESPACE_AND_OPERATIONS;
                    result.append(symbol);
                }
                else if ('&' == symbol)
                {
                    state = State.WHITESPACE_AND_OPERATIONS;
                    result.append(symbol);
                }
                else if ('=' == symbol)
                {
                    state = State.WHITESPACE_AND_OPERATIONS;
                    result.append(symbol);
                }
                else if ('>' == symbol)
                {
                    state = State.WHITESPACE_AND_OPERATIONS;
                    result.append(symbol);
                }
                else if (':' == symbol)
                {
                    state = State.WHITESPACE_AND_OPERATIONS;
                    result.append(symbol);
                }
                else if ('?' == symbol)
                {
                    state = State.WHITESPACE_AND_OPERATIONS;
                    result.append(symbol);
                }
                else if ('0' <= symbol && symbol <= '9')
                {
                    state = State.WHITESPACE_AND_OPERATIONS;
                    result.append(symbol);
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case IDENTIFIER:
                if ('|' == symbol)
                {
                    state = State.WHITESPACE_AND_OPERATIONS;
                    result.append(map.get(identifier.toString()));
                    identifier = new StringBuilder();
                    result.append(symbol);
                }
                else if ('=' == symbol)
                {
                    state = State.WHITESPACE_AND_OPERATIONS;
                    result.append(map.get(identifier.toString()));
                    identifier = new StringBuilder();
                    result.append(symbol);
                }
                else if ('-' == symbol)
                {
                    state = State.WHITESPACE_AND_OPERATIONS;
                    result.append(map.get(identifier.toString()));
                    identifier = new StringBuilder();
                    result.append(symbol);
                }
                else if ('(' == symbol)
                {
                    state = State.WHITESPACE_AND_OPERATIONS;
                    result.append(map.get(identifier.toString()));
                    identifier = new StringBuilder();
                    result.append(symbol);
                }
                else if ('>' == symbol)
                {
                    state = State.WHITESPACE_AND_OPERATIONS;
                    result.append(map.get(identifier.toString()));
                    identifier = new StringBuilder();
                    result.append(symbol);
                }
                else if ('&' == symbol)
                {
                    state = State.WHITESPACE_AND_OPERATIONS;
                    result.append(map.get(identifier.toString()));
                    identifier = new StringBuilder();
                    result.append(symbol);
                }
                else if ('A' <= symbol && symbol <= 'Z')
                {
                    state = State.IDENTIFIER;
                    identifier.append(symbol);
                }
                else if ('/' == symbol)
                {
                    state = State.WHITESPACE_AND_OPERATIONS;
                    result.append(map.get(identifier.toString()));
                    identifier = new StringBuilder();
                    result.append(symbol);
                }
                else if ('0' <= symbol && symbol <= '9')
                {
                    state = State.IDENTIFIER;
                    identifier.append(symbol);
                }
                else if (' ' == symbol)
                {
                    state = State.WHITESPACE_AND_OPERATIONS;
                    result.append(map.get(identifier.toString()));
                    identifier = new StringBuilder();
                    result.append(symbol);
                }
                else if (')' == symbol)
                {
                    state = State.WHITESPACE_AND_OPERATIONS;
                    result.append(map.get(identifier.toString()));
                    identifier = new StringBuilder();
                    result.append(symbol);
                }
                else if ('!' == symbol)
                {
                    state = State.WHITESPACE_AND_OPERATIONS;
                    result.append(map.get(identifier.toString()));
                    identifier = new StringBuilder();
                    result.append(symbol);
                }
                else if ('+' == symbol)
                {
                    state = State.WHITESPACE_AND_OPERATIONS;
                    result.append(map.get(identifier.toString()));
                    identifier = new StringBuilder();
                    result.append(symbol);
                }
                else if ('<' == symbol)
                {
                    state = State.WHITESPACE_AND_OPERATIONS;
                    result.append(map.get(identifier.toString()));
                    identifier = new StringBuilder();
                    result.append(symbol);
                }
                else if ('%' == symbol)
                {
                    state = State.WHITESPACE_AND_OPERATIONS;
                    result.append(map.get(identifier.toString()));
                    identifier = new StringBuilder();
                    result.append(symbol);
                }
                else if ('^' == symbol)
                {
                    state = State.WHITESPACE_AND_OPERATIONS;
                    result.append(map.get(identifier.toString()));
                    identifier = new StringBuilder();
                    result.append(symbol);
                }
                else if ('a' <= symbol && symbol <= 'z')
                {
                    state = State.IDENTIFIER;
                    identifier.append(symbol);
                }
                else if ('*' == symbol)
                {
                    state = State.WHITESPACE_AND_OPERATIONS;
                    result.append(map.get(identifier.toString()));
                    identifier = new StringBuilder();
                    result.append(symbol);
                }
                else if (':' == symbol)
                {
                    state = State.WHITESPACE_AND_OPERATIONS;
                    result.append(map.get(identifier.toString()));
                    identifier = new StringBuilder();
                    result.append(symbol);
                }
                else if ('?' == symbol)
                {
                    state = State.WHITESPACE_AND_OPERATIONS;
                    result.append(map.get(identifier.toString()));
                    identifier = new StringBuilder();
                    result.append(symbol);
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            default:
                state = State.UNDEFINED_TRANSITION;
        } // end switch
        return state;
    } // end transition()
} // end FindIdentifiers