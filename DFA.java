/***********-------------------                 DFA                 -------------------***************
 * 
 * *Name: Marian Rempola
 * Date: 05 April 2024
 * Class: CMPSC 464 
 * Project: Simulate a DFA and a NFA given a .txt file
 * 
 * First : first line has str of chars allowed in Alphabet
 * Second : second line has number of states n, followed by n lines each containing names of states
 * Third : start state
 * Fourth : number of accept states m, followed by m lines each containing names of accept states
 * Fifth: number of transitions t, followed by t lines of transitions (from P to Q on reading L)
 *   form: <P>,<Q> = from list of states; <L> = from Alphabet and can be epsilon
 * 
 * Goal: given file with FA and str w, decides if the FA will accept w
 * ************************************/ 

import java.io.*;
import java.util.*;

/******************   HELPER CLASSES *******************/

//Creates a class Transition that creates a 'Transition' object to help break up the transtion descriptions
class Transition {
    //<P>,<Q>,<L>
    String stateFrom;
    String stateTo;
    String input;

    //constructor of Transition class
    public Transition (String stateFrom, String stateTo, String input) {
        //init variables
        this.stateFrom = stateFrom;
        this.stateTo = stateTo;
        this.input = input;
    }
}

//set all of the components of the 5-tuple DFA/NFA
class Automata {
    Set<String> alphabet;
    Set<String> states;
    String startState;
    Set<String> acceptStates;
    List<Transition> transitions;

    //constructor of Automata class
    Automata() {
        alphabet = new HashSet<>();
        states = new HashSet<>();
        acceptStates = new HashSet<>();
        transitions = new ArrayList<>();
    }
}


/******************   MAIN CLASS (SIMULATION) *******************/

public class MrempolaDFA {
    public static void main(String[] args) {
        /**************************** DFA ******************************/
        //Open path to the dfa descriptor file --> create DFA by calling helper function
        Automata dfa = readAutomata("dfadesc.txt");

        //Ask for input w 
        System.out.print("Enter input string to test if it belongs in the DFA: ");
        //Read w
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        //close scanner
        scanner.close();

        /* Simualte DFA by calling helper function */
        boolean accepted = simulateDFA(dfa, input);
        if (accepted == true) {
            System.out.println("Input was ACCEPTED in DFA");
        } else {
            System.out.println("Input was REJECTED in DFA");
        }
    }
    
    /**************************** HELPER FUNCTIONS ******************************/
    /*---- Read DFA or NFA and separate into 5-tuple accordingly by reading a file as a param.----*/

    private static Automata readAutomata(String file) {
        //create new Automata object
        Automata automata = new Automata();

        try {
            //open path to file descriptor 
            Scanner scanner = new Scanner(new File(file));

            //1. Read Alphabet
            String alphabet = scanner.nextLine();
            //assign alphabet to automata
            for (char c : alphabet.toCharArray()) {
                //add every symbol of the alphabet array in the alphabet of the automata!! 
                automata.alphabet.add(String.valueOf(c));
            }

            //2. Read States (n is the number of states)
            int n = Integer.parseInt(scanner.nextLine());
            //for n lines, read each and assign it to the states of the automata
            for(int i = 0; i < n; i++) {
                automata.states.add(scanner.nextLine());
            }

            //3. Read and Assign Start State to automata
            automata.startState = scanner.nextLine();

            //4. Read Accept States (m is the number of accept states)
            int m = Integer.parseInt(scanner.nextLine());
            //for m lines, read each and assign it to the states of the automata
            for(int i = 0; i < m; i++) {
                automata.acceptStates.add(scanner.nextLine());
            }

            //5. Read Transitions (t is the number of transitions)
            int t = Integer.parseInt(scanner.nextLine());
            //for t lines, read each and assign it to the states of the automata
            for(int i = 0; i < t; i++) {
                //each line contains a description of transition --> Call Transition method (in helper classes) and separate PQL
                String[] tDescription = scanner.nextLine().split(",");
                //create new Transition object based on <P>, <Q>, <L>
                Transition transition = new Transition(tDescription[0], tDescription[1], tDescription[2]);
                //assign to automata
                automata.transitions.add(transition);
            }

            //Close Scanner
            scanner.close();
        } 
        catch(FileNotFoundException e) {
            e.printStackTrace(); //This will print the error and stack to show where it went wrong // how file was not found! :) 
        }

        //Returning Automata back to Simulation part of code!
        return automata;
    }

    /*---- [SIMULATE DFA] to see if it accepts input string w!! True = accepts w, False o/w----*/
    private static boolean simulateDFA(Automata automata, String w) {
        //Var to keep track of current state: start at Start State
        String currentState = automata.startState;

        //See where input w lies!
        for(char symbol : w.toCharArray()) {
            //Go to next state according to input w
            String nextState = getNext(automata, currentState, symbol);

            //if there is no next state, then it fails
            if (nextState == null) {
                return false; 
            }

            //do it for every state
            currentState = nextState;
        }
        // See if we end up in an accept state
        return automata.acceptStates.contains(currentState);
    }

    /*---- Helper function to get the Next State---*/
    private static String getNext(Automata automata, String currentState, char symbol) {
        //traverse through all transitions to find the input symbol and what state it is from
        for (Transition transition : automata.transitions) {
            if(transition.stateFrom.equals(currentState) && transition.input.equals(String.valueOf(symbol))) {
                //toState is the next state
                return transition.stateTo;
            }
        }
        //if there is no toState found --> then thre is no next state
        return null;
    }
}
