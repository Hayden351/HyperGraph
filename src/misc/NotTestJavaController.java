package misc;

/**
 * @author Hayden Fields
 */
// (NotTestJavaController)(0)(10)()(0~CC~11 1~CC~11 2~CC~11 3~CC~11 0~TT~1 1~ee~2 2~ss~3 3~tt~4 0~!T~5 1~!e~5 2~!s~5 3~!t~5 5~!C~5 5~CC~11 11~!o~5 11~oo~12 12~!n~5 12~nn~13 13~!t~5 13~tt~14 14~!r~5 14~rr~15 15~!o~5 15~oo~16 16~!l~5 16~ll~17 17~!l~5 17~ll~18 18~!e~5 18~ee~19 19~!r~5 19~rr~20 20~!.~20 20~..~6 6~jj~7 7~aa~8 8~vv~9 9~aa~10 6~!j~20 7~!a~20 8~!v~20 9~!a~20 10~**~20)
public class NotTestJavaController
{
    public static void main (String[] args)
    {
        System.out.println(NotTestJavaController.parse("Controller.java"));
        System.out.println(NotTestJavaController.parse("Contaoller.java"));
        System.out.println(NotTestJavaController.parse("TestController.java"));
        System.out.println(NotTestJavaController.parse("Controller.javaasd"));
        System.out.println(NotTestJavaController.parse("Test"));
        System.out.println(NotTestJavaController.parse("TesController"));
        System.out.println(NotTestJavaController.parse("TesController.java"));
    }
    // An enumerated type for the states of a finite state automata
    private enum State
    {
        // set of non final states
        ZERO                 (Type.NON_FINAL),
        ONE                  (Type.NON_FINAL),
        TWO                  (Type.NON_FINAL),
        THREE                (Type.NON_FINAL),
        FOUR                 (Type.NON_FINAL),
        FIVE                 (Type.NON_FINAL),
        SIX                  (Type.NON_FINAL),
        SEVEN                (Type.NON_FINAL),
        EIGHT                (Type.NON_FINAL),
        NINE                 (Type.NON_FINAL),
        ELEVEN               (Type.NON_FINAL),
        TWELVE               (Type.NON_FINAL),
        THIRTEEN             (Type.NON_FINAL),
        FOURTEEN             (Type.NON_FINAL),
        FIFTEEN              (Type.NON_FINAL),
        SIXTEEN              (Type.NON_FINAL),
        SEVENTEEN            (Type.NON_FINAL),
        EIGHTEEN             (Type.NON_FINAL),
        NINETEEN             (Type.NON_FINAL),
        TWENTY               (Type.NON_FINAL),

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
        System.out.println("");
        System.out.println(input);
        return new NotTestJavaController().evaluate(input);
    }
    public boolean evaluate(String input)
    {
        return evaluate(input, 0);
    }
    public boolean evaluate(String input, int position)
    {
        State currentState = State.ZERO;
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
            case ZERO:
                if ('C' == symbol)
                {
                    state = State.ELEVEN;
                }
                else if (symbol != 'T')
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
                if ('C' == symbol)
                {
                    state = State.ELEVEN;
                }
                else if ('e' == symbol)
                {
                    state = State.TWO;
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
                if ('C' == symbol)
                {
                    state = State.ELEVEN;
                }
                else if ('s' == symbol)
                {
                    state = State.THREE;
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
                if ('C' == symbol)
                {
                    state = State.ELEVEN;
                }
                else if ('t' == symbol)
                {
                    state = State.FOUR;
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
                if ('C' == symbol)
                {
                    state = State.ELEVEN;
                }
                else if (symbol != 'C')
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
                    state = State.TWENTY;
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
                if ('a' == symbol)
                {
                    state = State.EIGHT;
                }
                else if (symbol != 'a')
                {
                    state = State.TWENTY;
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
                    state = State.TWENTY;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case NINE:
                if (symbol != 'a')
                {
                    state = State.TWENTY;
                }
                else if ('a' == symbol)
                {
                    state = State.TEN;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case TEN:
                if (true)
                {
                    state = State.TWENTY;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case ELEVEN:
                if (symbol != 'o')
                {
                    state = State.FIVE;
                }
                else if ('o' == symbol)
                {
                    state = State.TWELVE;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case TWELVE:
                if (symbol != 'n')
                {
                    state = State.FIVE;
                }
                else if ('n' == symbol)
                {
                    state = State.THIRTEEN;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case THIRTEEN:
                if ('t' == symbol)
                {
                    state = State.FOURTEEN;
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
            case FOURTEEN:
                if ('r' == symbol)
                {
                    state = State.FIFTEEN;
                }
                else if (symbol != 'r')
                {
                    state = State.FIVE;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case FIFTEEN:
                if (symbol != 'o')
                {
                    state = State.FIVE;
                }
                else if ('o' == symbol)
                {
                    state = State.SIXTEEN;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case SIXTEEN:
                if ('l' == symbol)
                {
                    state = State.SEVENTEEN;
                }
                else if (symbol != 'l')
                {
                    state = State.FIVE;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case SEVENTEEN:
                if (symbol != 'l')
                {
                    state = State.FIVE;
                }
                else if ('l' == symbol)
                {
                    state = State.EIGHTEEN;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case EIGHTEEN:
                if ('e' == symbol)
                {
                    state = State.NINETEEN;
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
            case NINETEEN:
                if (symbol != 'r')
                {
                    state = State.FIVE;
                }
                else if ('r' == symbol)
                {
                    state = State.TWENTY;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case TWENTY:
                if ('.' == symbol)
                {
                    state = State.SIX;
                }
                else if (symbol != '.')
                {
                    state = State.TWENTY;
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
} // end NotTestJavaController
