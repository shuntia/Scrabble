import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class ComputerPlayer extends Player{
    boolean deepsearch=false;
    public ComputerPlayer(TileBag bag, WordTrie trie, Board board){
        super(bag, trie, board);
    }
    public ComputerPlayer(TileBag bag, WordTrie trie, Board board, boolean deepsearch){
        super(bag, trie, board);
        this.deepsearch = deepsearch;
    }
    @Override
    public void takeTurn() {
        bag.fill(hand);
        Log.log("Computer has hand " + hand);
        HashSet<String> viable = trie.viable(hand);
        viable.removeIf(word -> word.contains(" ")); // Remove words containing spaces
        if (viable.isEmpty()) {
            Log.println("Computer cannot play.");
            if (deepsearch) {
                Log.println("Deep search enabled, finding best play. This may take a while...");
                PlayedTile[] tiles = board.getPlayedTiles();
                ArrayList<String> words = new ArrayList<>();
                for (PlayedTile tile : tiles) {
                    HashSet<Tile> temp = new HashSet<>();
                    temp.add(tile);
                    temp.addAll(hand.getTiles());
                    words.addAll(trie.viable(temp));
                }
                words.removeIf(word -> word.contains(" ")); // Remove words containing spaces
                if (words.isEmpty()) {
                    Log.println("No viable words found. Exchanging hand.");
                    bag.exchange(hand);
                    return;
                } else {
                    Log.log("Deepsearch Found " + words.size() + " viable words.");
                    viable.addAll(words);
                }
            } else {
                Log.println("Deep search disabled, exchanging hand.");
                bag.exchange(hand);
                return;
            }
        }
        // Map to store results for each word
        Map<String, HashMap<String, Object>> results = new HashMap<>();
        Iterator<String> iterator = viable.iterator();
        while (iterator.hasNext()) {
            String word = iterator.next();
            HashMap<String, Object> result = board.fit(word);
            if (result != null) {
                Log.log("Computer can play " + word + " at " + result.get("x") + ", " + result.get("y") + " " + result.get("orientation") + " for " + trie.checkPoint(word) + " points");
                results.put(word, result);
            } else {
                iterator.remove(); // Remove words that can't fit
            }
        }
        if (viable.isEmpty()) {
            Log.println("Computer did find viable word, but cannot fit it on the board. Exchanging hand.");
            bag.exchange(hand);
            return;
        }
        String best = trie.best(viable.toArray(new String[0]));
        HashMap<String, Object> bestResult = results.get(best);
        Log.println("Computer plays " + best + " for " + trie.checkPoint(best) + " points");
        hand.play(best);
        board.play((int) bestResult.get("x"), (int) bestResult.get("y"), best, (Orientation) bestResult.get("orientation"));
    }
    public void log(Object msg){
        Log.log(msg);
    }
    
}
