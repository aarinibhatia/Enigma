package enigma;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;

import static enigma.TestUtils.*;

/** JUnit tests for the Reflector class.
 *  @author Aarini
 */

public class ReflectorTest {

    private Rotor rotor;
    private String alpha = UPPER_STRING;

    /** Set the rotor to the one with given NAME and permutation as
     *  specified by the NAME entry in ROTORS, with given NOTCHES. */
    private void setRotor(String name, HashMap<String, String> rotors) {
        rotor = new Reflector(name, new Permutation(rotors.get(name), UPPER));
    }

    @Test (expected = EnigmaException.class)
    public void testReflectorDerangement() throws EnigmaException {
        rotor = new Reflector("B", new Permutation(
                "(AE) (BN) (CK) (DQ) (FU) (MP) (RX) (SZ) (T)", UPPER));
    }

    @Test (expected = EnigmaException.class)
    public void testConvertBackward() throws EnigmaException {
        setRotor("B", NAVALA);
        rotor.convertBackward(2);
    }

    @Test (expected = EnigmaException.class)
    public void testSet() throws EnigmaException {
        setRotor("B", NAVALA);
        rotor.set(2);
    }

    @Test
    public void testSetZero() {
        setRotor("B", NAVALA);
        rotor.set(0);
        assertEquals(rotor.setting(), 0);
    }


    @Test (expected = EnigmaException.class)
    public void testRingSet() throws EnigmaException {
        setRotor("B", NAVALA);
        rotor.setRingSetting("D");
    }

    @Test
    public void testRingSetA() {
        setRotor("B", NAVALA);
        rotor.setRingSetting("A");
        assertEquals(rotor.ringSetting(), 0);
    }


}
