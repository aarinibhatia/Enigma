package enigma;

import org.junit.Test;
import static org.junit.Assert.*;

/** JUnit tests for the CharacterRange class.
 *  @author Aarini
 */
public class CharacterRangeTest {

    @Test
    public void testCharacterRange() {
        Alphabet alph = new CharacterRange('A', 'X');
        assertTrue(alph.contains("C".charAt(0)));
        assertFalse(alph.contains("Y".charAt(0)));
        assertEquals(alph.size(), 24);
        assertEquals(alph.toChar(3), "D".charAt(0));
        assertEquals(alph.toInt("E".charAt(0)), 4);
    }


    @Test (expected = EnigmaException.class)
    public void testToIntExceptionHandling() throws EnigmaException {
        Alphabet alph = new CharacterRange('A', 'X');
        alph.toInt("Y".charAt(0));
    }

    @Test (expected = EnigmaException.class)
    public void testToCharExceptionHandling() throws EnigmaException {
        Alphabet alph = new CharacterRange('A', 'X');
        alph.toChar(25);
    }




}
