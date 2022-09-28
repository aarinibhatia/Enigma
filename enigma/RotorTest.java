package enigma;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;

import static enigma.TestUtils.*;

/** JUnit tests for the Rotor class.
 *  @author Aarini
 */
public class RotorTest {

    private Rotor rotor;
    private String alpha = UPPER_STRING;

    /** Check that rotor has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkRotor(String testId,
                            String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, rotor.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d (%c)", ci, c),
                    ei, rotor.convertForward(ci));
            assertEquals(msg(testId, "wrong inverse of %d (%c)", ei, e),
                    ci, rotor.convertBackward(ei));
        }
    }

    /** Set the rotor to the one with given NAME and permutation as
     *  specified by the NAME entry in ROTORS, with given NOTCHES. */
    private void setRotor(String name, HashMap<String, String> rotors,
                          String notches) {
        rotor = new MovingRotor(name, new Permutation(rotors.get(name), UPPER),
                notches);
    }

    @Test
    public void testOverallSetting() {
        setRotor("I", NAVALA, "");
        rotor.set(4);
        rotor.setRingSetting("C");
        assertEquals(rotor.overallSetting(), 2);
    }

    @Test
    public void testConvertForward() {
        setRotor("I", NAVALA, "");
        int param = UPPER.toInt("B".charAt(0));
        int res = rotor.convertForward(param);
        int expected = UPPER.toInt((NAVALA_MAP.get("I")).charAt(param));
        assertEquals(res, expected);

    }

    @Test
    public void testConvertBackward() {
        setRotor("I", NAVALA, "");
        int param = UPPER.toInt("B".charAt(0));
        int res = rotor.convertBackward(param);
        int expected = UPPER.toInt("W".charAt(0));
        assertEquals(res, expected);
    }
}
