package enigma;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;


/** JUnit tests for the Machine class.
 *  @author Aarini
 */
public class MachineTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private ArrayList<Rotor> allRotors = new ArrayList<>(Arrays.asList(
            new Reflector("B", new Permutation(
                    "(AE) (BN) (CK) (DQ) (FU) (GY) (HW) (IJ) (LO) "
                            + "(MP) (RX) (SZ) (TV)", UPPER)),
            new FixedRotor("BETA", new Permutation(
                    "(ALBEVFCYODJWUGNMQTZSKPR) (HIX)",
                    UPPER)),
            new MovingRotor("I", new Permutation(
                    "(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) "
                            + "(JZ) (S)", UPPER), "Q"),
            new MovingRotor("II", new Permutation(
                    "(FIXVYOMW) (CDKLHUP) (ESZ) (BJ) "
                            + "(GR) (NT) (A) (Q)", UPPER), "E"),
            new MovingRotor("III", new Permutation(
                    "(ABDHPEJT) (CFLVMZOYQIRWUKXSG) (N)",
                    UPPER), "V"),
            new MovingRotor("IV", new Permutation(
                    "(AEPLIYWCOXMRFZBSTGJQNH) (DV) (KU)",
                    UPPER), "J")));

    private Machine machine;
    private String[] newRotors = {"B", "BETA", "III", "IV", "I"};



    /** Set the rotor to the one with given NAME and permutation as
     *  specified by the NAME entry in ROTORS, with given NOTCHES. */
    private void setMachine(Alphabet alpha, int numrotors,
                            int pawls, Collection<Rotor> allrotors) {
        machine = new Machine(alpha, numrotors, pawls, allrotors);
    }

    /* ***** TESTS ***** */
    @Test
    public void testInsertRotors() {
        setMachine(UPPER, 5, 3, allRotors);
        machine.insertRotors(newRotors);
        assertEquals("Wrong rotor placement",
                allRotors.get(0), machine.rotors()[0]);
        assertEquals("Wrong rotor placement",
                allRotors.get(4), machine.rotors()[2]);
    }

    @Test
    public void testSetRotors() {
        setMachine(UPPER, 5, 3, allRotors);
        machine.insertRotors(newRotors);
        machine.setRotors("ABCD");
        assertEquals("Wrong setting at 1", 0,
                machine.rotors()[1].setting());
        assertEquals("Wrong setting at 2", 1,
                machine.rotors()[2].setting());
        assertEquals("Wrong setting at 3", 2,
                machine.rotors()[3].setting());
        assertEquals("Wrong setting at 4", 3,
                machine.rotors()[4].setting());
    }

    @Test (expected = EnigmaException.class)
    public void testSetRotorsSetting() throws EnigmaException {
        setMachine(UPPER, 5, 3, allRotors);
        machine.insertRotors(newRotors);
        machine.setRotors("AXL");
    }

    @Test (expected = EnigmaException.class)
    public void testSetRotorsAlph() throws EnigmaException {
        setMachine(UPPER, 5, 3, allRotors);
        machine.insertRotors(newRotors);
        machine.setRotors("AXz");
    }

    @Test
    public void testConvert() {
        setMachine(UPPER, 5, 3, allRotors);
        machine.insertRotors(newRotors);
        machine.setRotors("ABCD");
        machine.setPlugboard(new Permutation(
                "(HQ) (EX) (IP) (TR) (BY)", UPPER));
        assertEquals("Wrong convert",
                "KGHX", machine.convert("FROM"));
        setMachine(UPPER, 5, 3, allRotors);
        machine.insertRotors(newRotors);
        machine.setRotors("ABCD");
        machine.setPlugboard(new Permutation(
                "(HQ) (EX) (IP) (TR) (BY)", UPPER));
        assertEquals("Wrong convert",
                "FROM", machine.convert("KGHX"));
    }
}
