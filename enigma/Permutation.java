package enigma;


/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author aarini
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        String temp = cycles.trim();
        temp = temp.replace("(", "");
        temp = temp.replace(" ", "");
        int n = temp.length() - 1;
        String temp3 = method(temp);
        _cycles = temp3.split("\\)");

    }
    /** Helper method that returns a substring
     * mof STR with the last ")" removed. */
    public String method(String str) {
        if (str != null && str.length() > 0
                && str.charAt(str.length() - 1) == ')') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        String[] tempCycle = new String[_cycles.length + 1];
        for (int i = 0; i < _cycles.length; i++) {
            tempCycle[i] = _cycles[i];
        }
        tempCycle[_cycles.length] = cycle;
        _cycles = tempCycle;
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % this.size();
        if (r < 0) {
            r += this.size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        char c = _alphabet.toChar(wrap(p));
        char newChar;
        for (int i = 0; i < _cycles.length; i++) {
            for (int j = 0; j < _cycles[i].length(); j++) {
                if (_cycles[i].charAt(j) == c) {
                    newChar = _cycles[i].charAt(mod(j + 1,
                            _cycles[i].length()));
                    return _alphabet.toInt(newChar);
                }
            }
        }
        return p;
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        char n = _alphabet.toChar(wrap(c));
        char newChar;
        for (int i = 0; i < _cycles.length; i++) {
            for (int j = 0; j < _cycles[i].length(); j++) {
                if (_cycles[i].charAt(j) == n) {
                    newChar = _cycles[i].charAt(mod(j - 1,
                            _cycles[i].length()));
                    return _alphabet.toInt(newChar);
                }
            }
        }
        return c;
    }
    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        return _alphabet.toChar(permute(_alphabet.toInt(p)));
    }


    /** Return the result of applying the inverse of this permutation to C. */
    int invert(char c) {
        return _alphabet.toChar(invert(_alphabet.toInt(c)));
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        int count = 0;
        for (int i = 0; i < _cycles.length; i++) {
            if (_cycles[i].length() == 1) {
                return false;
            }
            count += _cycles[i].length();
        }
        return (count == _alphabet.size());
    }

    /** self made mod func that returns the mod of P and SIZE. */
    private int mod(int p, int size) {
        int r = p % size;
        if (r < 0) {
            r += size;
        }
        return r;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** array of cycles. */
    private String[] _cycles;

}
