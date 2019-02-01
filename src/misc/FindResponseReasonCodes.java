package misc;

/**
 * @author Hayden Fields
 */
// (FindResponseReasonCodes)(0)(23)()(0~PP~1 1~HH~2 2~XX~3 3~RR~4 4~ee~5 5~ss~6 6~pp~7 7~oo~8 8~nn~9 9~ss~10 10~ee~11 11~RR~12 12~ee~13 13~aa~14 14~ss~15 15~oo~16 16~nn~17 17~CC~18 18~oo~19 19~dd~20 20~ee~21 21~**~21 21~..~22 22~**~22 22~!!~23 23~!!~23)
public class FindResponseReasonCodes
{
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
        TEN                  (Type.NON_FINAL),
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
        TWENTY_ONE           (Type.NON_FINAL),
        TWENTY_TWO           (Type.NON_FINAL),

        // set of final states
        TWENTY_THREE         (Type.FINAL),

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
        return new FindResponseReasonCodes().evaluate(input);
    }
    public boolean evaluate(String input)
    {
        return evaluate(input, 0);
    }
    public boolean evaluate(String input, int position)
    {
        State currentState = State.ZERO;
        for (;currentState.type == State.Type.NON_FINAL && position < input.length(); position++)
        {
            char symbol = input.charAt(position);
            currentState = transition(currentState, symbol);
        } // end for
        return (currentState.type == State.Type.FINAL);
    }

    public State transition(State state, char symbol)
    {
        switch (state)
        {
            case ZERO:
                if ('P' == symbol)
                {
                    state = State.ONE;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case ONE:
                if ('H' == symbol)
                {
                    state = State.TWO;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case TWO:
                if ('X' == symbol)
                {
                    state = State.THREE;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case THREE:
                if ('R' == symbol)
                {
                    state = State.FOUR;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case FOUR:
                if ('e' == symbol)
                {
                    state = State.FIVE;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case FIVE:
                if ('s' == symbol)
                {
                    state = State.SIX;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case SIX:
                if ('p' == symbol)
                {
                    state = State.SEVEN;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case SEVEN:
                if ('o' == symbol)
                {
                    state = State.EIGHT;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case EIGHT:
                if ('n' == symbol)
                {
                    state = State.NINE;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case NINE:
                if ('s' == symbol)
                {
                    state = State.TEN;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case TEN:
                if ('e' == symbol)
                {
                    state = State.ELEVEN;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case ELEVEN:
                if ('R' == symbol)
                {
                    state = State.TWELVE;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case TWELVE:
                if ('e' == symbol)
                {
                    state = State.THIRTEEN;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case THIRTEEN:
                if ('a' == symbol)
                {
                    state = State.FOURTEEN;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case FOURTEEN:
                if ('s' == symbol)
                {
                    state = State.FIFTEEN;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case FIFTEEN:
                if ('o' == symbol)
                {
                    state = State.SIXTEEN;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case SIXTEEN:
                if ('n' == symbol)
                {
                    state = State.SEVENTEEN;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case SEVENTEEN:
                if ('C' == symbol)
                {
                    state = State.EIGHTEEN;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case EIGHTEEN:
                if ('o' == symbol)
                {
                    state = State.NINETEEN;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case NINETEEN:
                if ('d' == symbol)
                {
                    state = State.TWENTY;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case TWENTY:
                if ('e' == symbol)
                {
                    state = State.TWENTY_ONE;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case TWENTY_ONE:
                if ('*' == symbol)
                {
                    state = State.TWENTY_ONE;
                }
                else if ('.' == symbol)
                {
                    state = State.TWENTY_TWO;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case TWENTY_TWO:
                if ('!' == symbol)
                {
                    state = State.TWENTY_THREE;
                }
                else if ('*' == symbol)
                {
                    state = State.TWENTY_TWO;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case TWENTY_THREE:
                if ('!' == symbol)
                {
                    state = State.TWENTY_THREE;
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
} // end FindResponseReasonCodes