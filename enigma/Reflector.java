package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a reflector in the enigma.
 *  @author aarini
 */
class Reflector extends FixedRotor {

    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is PERM. */
    Reflector(String name, Permutation perm) {
        super(name, perm);
        if (!perm.derangement()) {
            throw new EnigmaException("reflector perm must be a derangement");
        }
    }

    @Override
    boolean reflecting() {
        return _reflecting;
    }

    @Override
    int convertBackward(int e) {
        throw error("Reflector do not convert backward");
    }
    @Override
    void set(int posn) {
        if (posn != 0) {
            throw error("reflector has only one position");
        } else {
            _setting = 0;
        }
    }

    @Override
    void setRingSetting(String rset) {
        if (this.permutation().alphabet().toInt(rset.charAt(0)) != 0) {
            throw error("reflector has only one ring setting");
        } else {
            _ringSetting = 0;
        }
    }

    /** setting of rotor. */
    private int _setting = 0;

    /** reflecting ability of rotor. */
    private Boolean _reflecting = true;

    /** Ring setting of rotor. */
    private int _ringSetting = 0;

}
