package enigma;

import java.util.Collection;

/** Class that represents a complete enigma machine.
 *  @author Aarini
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotor = numRotors;
        _pawl = pawls;
        _allRotors = allRotors.toArray();
        _rotors = new Rotor[numRotors];
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotor;
    }

    /** Return the rotors slots of this machine. */
    Rotor[] rotors() {
        return _rotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawl;
    }

    /** Return the ring setting of the rotors. */
    String ringSetting() {
        return _ringSetting;
    }

    /** Set the ring setting of the rotors to S. */
    void setRingSetting(String s) {
        _ringSetting = s;
        for (int i = 1, j = 0; i < numRotors(); i++, j++) {
            _rotors[i].setRingSetting(String.valueOf(s.charAt(j)));
        }
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        for (int i = 0; i < rotors.length; i++) {
            for (Object j : _allRotors) {
                if (rotors[i].equals(
                        ((Rotor) j).name())) {
                    _rotors[i] = (Rotor) j;
                }
            }
        }
        if (_rotors.length != rotors.length) {
            throw new EnigmaException("wrongly named rotors");
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (!(setting.length() == _numRotor - 1)) {
            throw new EnigmaException("wrong length for setting");
        } else {
            for (int i = 1; i <= setting.length(); i++) {
                char ab = setting.charAt(i - 1);
                if (!_alphabet.contains(ab)) {
                    throw new EnigmaException(
                            "Initial positions string not in alphabet");
                } else {
                    if (!this._rotors[i].reflecting()) {
                        _rotors[i].set(ab);
                    }
                }
            }
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing

     *  the machine. */
    int convert(int c) {

        Boolean[] rotorAtNotch = new Boolean[_rotors.length];

        for (int i = 0; i < _rotors.length; i++) {
            rotorAtNotch[i] = _rotors[i].atNotch();
        }

        for (int i = 0; i <= _rotors.length - 3; i++) {
            if (_rotors[i].rotates() && rotorAtNotch[i + 1]) {
                _rotors[i].advance();
                if (_rotors[i + 1].rotates() && !rotorAtNotch[i + 2]) {
                    _rotors[i + 1].advance();
                }
            }
        }

        if (rotorAtNotch[_rotors.length - 1]) {
            _rotors[_rotors.length - 2].advance();
        }

        _rotors[_rotors.length - 1].advance();


        int res = _plugboard.permute(c);

        for (int i = _numRotor - 1; i >= 0; i--) {
            res = _rotors[i].convertForward(res);
        }
        for (int i = 1; i < _numRotor; i++) {
            res = _rotors[i].convertBackward(res);
        }
        res = _plugboard.permute(res);
        return res;
    }


    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        if (msg.isEmpty() || msg.equals("")) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < msg.length(); i++) {
            int ch = _alphabet.toInt(msg.charAt(i));
            char res = _alphabet.toChar(convert(ch));
            result.append(String.valueOf(res));
        }
        return result.toString();
    }



    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** total no. of rotors. */
    private final int _numRotor;

    /** total no. of pawls. */
    private final int _pawl;

    /** object array of all rotors. */
    private Object[] _allRotors;

    /** initial plugboard. */
    private Permutation _plugboard;

    /** array of rotors formatting the machine. */
    private Rotor[] _rotors;

    /** Ring setting of the rotors. */
    private String _ringSetting;

}
