package enigma;


/** Class that represents a rotating rotor in the enigma machine.
 *  @author aarini
 */
class MovingRotor extends Rotor {

    /** List of notches of rotor.*/
    private String _notches;

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;
    }

    @Override
    boolean rotates() {
        return true;
    }

    @Override
    boolean atNotch() {
        return _notches.indexOf
                (permutation().alphabet().toChar(setting())) != -1;
    }


    @Override
    void advance() {
        this.set(this.setting() + 1);
    }


}
