package enigma;


/** Superclass that represents a rotor in the enigma machine.
 *  @author aarini
 */
class Rotor {

    /** A rotor named NAME whose permutation is
     * given by PERM, and default ring setting.. */
    Rotor(String name, Permutation perm) {
        _name = name;
        _permutation = perm;
        _setting = 0;
        _ringSetting = 0;
    }

    /** Return my name. */
    String name() {
        return _name;
    }

    /** Return my alphabet. */
    Alphabet alphabet() {
        return _permutation.alphabet();
    }

    /** Return my permutation. */
    Permutation permutation() {
        return _permutation;
    }

    /** Return the size of my alphabet. */
    int size() {
        return _permutation.size();
    }

    /** Return true iff I have a ratchet and can move. */
    boolean rotates() {
        return false;
    }

    /** Return true iff I reflect. */
    boolean reflecting() {
        return false;
    }

    /** Return my current setting. */
    int setting() {
        return _setting;
    }

    /** Return my current ring setting. */
    int ringSetting() {
        return _ringSetting;
    }

    /** Return my current overall setting (setting
     *  and ring setting's effect combined). */
    int overallSetting() {
        return (setting() - _ringSetting);
    }

    /** Set setting() to POSN.  */
    void set(int posn) {
        final int sz = this.size();
        _setting = mod(posn, sz);
    }

    /** Set setting() to character CPOSN. */
    void set(char cposn) {
        int a = alphabet().toInt(cposn);
        set(a);
    }

    /** Set ringsetting() to RSET.  */
    void setRingSetting(String rset) {
        _ringSetting = _permutation.alphabet().toInt(rset.charAt(0));
    }

    /** Set ringsetting() to RSET char.  */
    void setRingSetting(char rset) {
        _ringSetting = _permutation.alphabet().toInt(rset);
    }

    /** Return the conversion of P (an integer in the range 0..size()-1)
     *  according to my permutation. */
    int convertForward(int p) {
        return mod(((_permutation.permute(
                mod((p + overallSetting()), size())))
                - overallSetting()), size());
    }

    /** Return the conversion of E (an integer in the range 0..size()-1)
     *  according to the inverse of my permutation. */
    int convertBackward(int e) {
        return mod(((_permutation.invert(
                mod((e + overallSetting()), size())))
                - overallSetting()), size());
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        return false;
    }

    /** Advance me one position, if possible. By default, does nothing. */
    void advance() {
    }

    /** helper modulo func that Returns the value of P modulo the input SIZE. */
    private int mod(int p, int size) {
        int r = p % size;
        if (r < 0) {
            r += size;
        }
        return r;
    }

    @Override
    public String toString() {
        return "Rotor " + _name;
    }

    /** My name. */
    private final String _name;

    /** The permutation implemented by this rotor in its 0 position. */
    private Permutation _permutation;

    /** setting of rotor. */
    private int _setting;

    /** Ring setting of rotor. */
    private int _ringSetting;


}
