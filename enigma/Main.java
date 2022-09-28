package enigma;


import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Aarini
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named FILENAME. */
    private Scanner getInput(String fileName) {
        try {
            return new Scanner(new File(fileName));
        } catch (IOException excp) {
            throw error("could not open %s", fileName);
        }
    }

    /** Return a PrintStream writing to the file named FILENAME. */
    private PrintStream getOutput(String fileName) {
        try {
            return new PrintStream(new File(fileName));
        } catch (IOException excp) {
            throw error("could not open %s", fileName);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        String nextInputLine;
        Machine m = readConfig();
        nextInputLine = _input.nextLine();
        while (nextInputLine.isEmpty()) {
            nextInputLine = _input.nextLine();
        }
        if (!nextInputLine.contains("*")) {
            throw new EnigmaException("bad setting format.");
        }
        setUp(m, nextInputLine);

        while (_input.hasNextLine()) {
            nextInputLine = _input.nextLine();
            if (nextInputLine.isEmpty()
                    || Pattern.matches("\\s", nextInputLine)) {
                _output.println(" ");
            } else if (nextInputLine.contains("*")) {
                setUp(m, nextInputLine);
            } else {
                String result = m.convert(nextInputLine.replaceAll(" ", ""));
                printMessageLine(result);
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            String conAL = _config.next();
            if (conAL.contains("*") || conAL.contains(",")
                    || conAL.contains("(") || conAL.contains(")")) {
                throw new EnigmaException("bad alphabet format");
            }
            _alphabet = new CharacterRange(conAL);
            if (!_config.hasNextInt()) {
                throw new EnigmaException("bad rotor format.");
            }
            numRotors = _config.nextInt();
            if (!_config.hasNextInt()) {
                throw new EnigmaException("bad pawl format.");
            }
            numPawls = _config.nextInt();
            if (numRotors < numPawls) {
                throw new EnigmaException("bad rotor pawl ratio.");
            }
            if (!_config.hasNext()) {
                throw new EnigmaException("no rotors present");
            }
            name = _config.next();
            while (_config.hasNext()) {
                String type = _config.next();
                String temp2 = _config.next();
                perm = "";
                if (!temp2.contains("(")) {
                    throw new EnigmaException("bad cycle format");
                } else {
                    perm = perm.concat(temp2 + " ");
                    while (_config.hasNext() && temp2.contains("(")) {
                        temp2 = _config.next();
                        if (temp2.contains("(")) {
                            perm = perm.concat(temp2 + " ");
                        }
                    }
                }
                perm = perm.substring(0, perm.length() - 1);
                Permutation roPerm = new Permutation(perm, _alphabet);
                try {
                    if (type.charAt(0) == 'N') {
                        _allRots.add(new FixedRotor(name, roPerm));
                    } else if (type.charAt(0) == 'M') {
                        _allRots.add(new MovingRotor(name, roPerm,
                                type.substring(1)));
                    } else if (type.charAt(0) == 'R') {
                        _allRots.add(new Reflector(name, roPerm));
                    }
                } catch (NoSuchElementException excp) {
                    throw error("configuration file truncated");
                }
                name = temp2;
            }
            return new Machine(_alphabet, numRotors, numPawls, _allRots);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        String[] set = settings.split(" ");
        if (set.length == 0) {
            throw new EnigmaException("no rotors in setting.");
        }
        if (set.length < M.numRotors() + 2) {
            throw new EnigmaException("Not enough arguments in setting");
        }
        String[] rotorNames = new String[M.numRotors()];

        for (Rotor rot : _allRots) {
            allRotorsNames.add(rot.name());
        }

        for (int i = 1; i < M.numRotors() + 1; i++) {
            if (!allRotorsNames.contains(set[i])) {
                throw new EnigmaException("rotors misnamed in setting");
            } else {
                rotorNames[i - 1] = set[i];
            }
        }

        for (int i = 0; i < rotorNames.length - 1; i++) {
            for (int j = i + 1; j < rotorNames.length; j++) {
                if (rotorNames[i].equals(rotorNames[j])) {
                    throw new EnigmaException("Duplicate Rotor");
                }
            }
        }

        M.insertRotors(rotorNames);
        if (!M.rotors()[0].reflecting()) {
            throw new EnigmaException("First Rotor should be a reflector");
        }

        M.setRotors(set[M.numRotors() + 1]);
        String steckeredPairs = "";

        if (set.length > M.numRotors() + 2) {

            if (!set[M.numRotors() + 2].contains("(")
                    && set[M.numRotors() + 2].length() == M.numRotors() - 1) {
                M.setRingSetting(set[M.numRotors() + 2]);
            }

            for (int i = M.numRotors() + 2; i < set.length; i++) {
                if (set[i].contains("(")) {
                    steckeredPairs = steckeredPairs.concat(set[i] + " ");
                }
            }
            if (steckeredPairs.length() > 0) {
                steckeredPairs = steckeredPairs.substring(0,
                        steckeredPairs.length() - 1);
            }
        }
        M.setPlugboard(new Permutation(steckeredPairs, _alphabet));
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        for (int i = 0; i < msg.length(); i += 5) {
            if (msg.length() - i <= 5) {
                _output.println(msg.substring(i, msg.length()) + " ");
            } else {
                _output.print(msg.substring(i, i + 5) + " ");
            }
        }
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** arraylist of all the rotors that can be used. */
    private ArrayList<Rotor> _allRots = new ArrayList<>();

    /** arraylist of the names of all the rotors that can be used. */
    private ArrayList<String> allRotorsNames = new ArrayList<>();


    /** A String containing cycles which readConfig() appends to. */
    private String perm;

    /** Name of current rotor. */
    private String _name;

    /** Temporary string that is the name of the next rotor in _config. */
    private String name;

    /** Type and notches of current rotor. */
    private String notches;

    /** Ring setting of the rotors. */
    private String _ringSetting;

    /** Number of rotors. */
    private int numRotors;

    /** Number of pawls. */
    private int numPawls;

}
