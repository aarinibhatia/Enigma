package enigma;

import java.util.ArrayList;

import static enigma.EnigmaException.*;

/** An Alphabet consisting of the Unicode characters in a certain range in
 *  order.
 *  @author Aarini
 */
class CharacterRange extends Alphabet {

    /** An alphabet consisting of all characters in ALPH,
     *  inclusive. */
    CharacterRange(String alph) {
        for (int c = 0; c < alph.length(); c++) {
            _charArray.add(c, alph.charAt(c));
        }
    }

    /** An alphabet consisting of all characters between FIRST
     * and LAST (Strings), inclusive. */
    CharacterRange(char first, char last) {
        _first = first;
        _last = last;
        if (_first > _last) {
            throw error("empty range of characters");
        }
        int i = 0;

        for (char c = _first; c <= _last; c++) {
            _charArray.add(i, c);
            i++;
        }
    }

    @Override
    int size() {
        int result = _charArray.size();
        return result;
    }

    @Override
    boolean contains(char ch) {
        return _charArray.contains(ch);
    }

    @Override
    char toChar(int index) {
        if (index >= size()) {
            throw error("character index out of range");
        }
        return _charArray.get(index);
    }

    @Override
    int toInt(char ch) {
        if (!contains(ch)) {
            throw error("character out of range");
        }
        int res = _charArray.indexOf(ch);
        return res;
    }

    /** Range of characters in this Alphabet. */
    private char _first, _last;

    /** Arraylist of characters in this Alphabet. */
    private ArrayList<Character> _charArray = new ArrayList<>();

}
