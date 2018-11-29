package misc;

/**
 * @author Hayden Fields
 */
// (NotTestJava)(0)(10)(0~START)(1~..~6 2~..~6 3~..~6 4~..~6 0~TT~1 1~ee~2 2~ss~3 3~tt~4 0~!T~5 1~!e~5 2~!s~5 3~!t~5 5~**~5 5~..~6 6~jj~7 7~aa~8 8~vv~9 9~aa~10 6~!j~5 7~!a~5 8~!v~5 9~!a~5 10~**~5)
public class NotTestJava
{
    public static void main (String[] args)
    {
        System.out.println(NotTestJava.parse("Test.java"));
        System.out.println(NotTestJava.parse("Tes.java"));
        System.out.println(NotTestJava.parse("Test.jav"));
        System.out.println(NotTestJava.parse("Tesa.jav"));
        System.out.println(NotTestJava.parse("Tesjava"));
        System.out.println(NotTestJava.parse("Te.stjava"));
        System.out.println(NotTestJava.parse("Tesa.javaas"));
    }
    // An enumerated type for the states of a finite state automata
    private enum State
    {
        // set of non final states
        START                (Type.NON_FINAL),
        ONE                  (Type.NON_FINAL),
        TWO                  (Type.NON_FINAL),
        THREE                (Type.NON_FINAL),
        FOUR                 (Type.NON_FINAL),
        FIVE                 (Type.NON_FINAL),
        SIX                  (Type.NON_FINAL),
        SEVEN                (Type.NON_FINAL),
        EIGHT                (Type.NON_FINAL),
        NINE                 (Type.NON_FINAL),

        // set of final states
        TEN                  (Type.FINAL),

        // set of error states
        UNDEFINED_TRANSITION (Type.ERROR);

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
        System.out.println(input);
        return new NotTestJava().evaluate(input);
    }
    public boolean evaluate(String input)
    {
        return evaluate(input, 0);
    }
    public boolean evaluate(String input, int position)
    {
        State currentState = State.START;
        System.out.println(currentState);
        for (;currentState.type != State.Type.ERROR && position < input.length(); position++)
        {
            char symbol = input.charAt(position);
            System.out.printf("%s * %s -> ",currentState, symbol);
            currentState = transition(currentState, symbol);
            System.out.println(currentState);
        } // end for
        return (currentState.type == State.Type.FINAL);
    }

    public State transition(State state, char symbol)
    {
        switch (state)
        {
            case START:
                if (symbol != 'T')
                {
                    state = State.FIVE;
                }
                else if ('T' == symbol)
                {
                    state = State.ONE;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case ONE:
                if ('e' == symbol)
                {
                    state = State.TWO;
                }
                else if ('.' == symbol)
                {
                    state = State.SIX;
                }
                else if (symbol != 'e')
                {
                    state = State.FIVE;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case TWO:
                if ('s' == symbol)
                {
                    state = State.THREE;
                }
                else if ('.' == symbol)
                {
                    state = State.SIX;
                }
                else if (symbol != 's')
                {
                    state = State.FIVE;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case THREE:
                if ('t' == symbol)
                {
                    state = State.FOUR;
                }
                else if ('.' == symbol)
                {
                    state = State.SIX;
                }
                else if (symbol != 't')
                {
                    state = State.FIVE;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case FOUR:
                state = State.UNDEFINED_TRANSITION;
                break;
            case FIVE:
                if ('.' == symbol)
                {
                    state = State.SIX;
                }
                else if (true)
                {
                    state = State.FIVE;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case SIX:
                if (symbol != 'j')
                {
                    state = State.FIVE;
                }
                else if ('j' == symbol)
                {
                    state = State.SEVEN;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case SEVEN:
                if (symbol != 'a')
                {
                    state = State.FIVE;
                }
                else if ('a' == symbol)
                {
                    state = State.EIGHT;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case EIGHT:
                if ('v' == symbol)
                {
                    state = State.NINE;
                }
                else if (symbol != 'v')
                {
                    state = State.FIVE;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case NINE:
                if ('a' == symbol)
                {
                    state = State.TEN;
                }
                else if (symbol != 'a')
                {
                    state = State.FIVE;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case TEN:
                if (true)
                {
                    state = State.FIVE;
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
} // end NotTestJava