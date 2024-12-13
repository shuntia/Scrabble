import java.util.Arrays;
import java.util.HashSet;

public class Hand {
    HashSet<Tile> tiles = new HashSet<>();
    public final int size = 7;

    public Hand(TileBag bag) {
        Tile[] drawnTiles = bag.draw(size);
        tiles.addAll(Arrays.asList(drawnTiles));
    }

    public Hand(Tile[] tiles) {
        this.tiles.addAll(Arrays.asList(tiles));
    }

    public Hand(char[] letters) {
        for (char letter : letters) {
            tiles.add(new Tile(letter));
        }
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        for (Tile t : tiles) {
            str.append(t).append(" ");
        }
        return str.toString();
    }

    public HashSet<Tile> getTiles() {
        return tiles;
    }

    public boolean contains(String word) {
        HashSet<Tile> tempTiles = new HashSet<>(tiles);
        for (char c : word.toCharArray()) {
            boolean found = false;
            for (Tile t : tempTiles) {
                if (t != null && t.letter == c) {
                    tempTiles.remove(t);
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }

    public void play(String word) {
        if (contains(word)) {
            for (char c : word.toCharArray()) {
                for (Tile t : tiles) {
                    if (t != null && t.letter == c) {
                        tiles.remove(t);
                        break;
                    }
                }
            }
        }
    }

    public char[] toCharArray() {
        char[] chars = new char[tiles.size()];
        int i = 0;
        for (Tile t : tiles) {
            if (t != null) {
                chars[i++] = t.letter;
            }
        }
        return chars;
    }

    public void add(Tile tile) {
        tiles.add(tile);
    }
}
