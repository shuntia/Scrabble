
import java.util.Arrays;


public class ComputerPlayer extends Player{
    public ComputerPlayer(TileBag bag, WordTrie trie, Board board){
        super(bag, trie, board);
    }
    @Override
    public void takeTurn() {
        bag.fill(hand);
        Log.println("Computer has hand " + hand);
        Log.println(board);
        Log.println("Played tiles: " + Arrays.toString(board.getPlayedTiles()));
        WordPosition best = bestMove();
        if(best!=null){
            Log.log("Computer: "+best.word+" at "+best.x+","+best.y+" "+best.orientation+" for "+best.score+" points");
            Log.println("Computer plays "+best.word+" at "+best.x+","+best.y+" "+best.orientation+" for "+best.score+" points");
            score+=board.play(best.x, best.y, best.word, best.orientation);
            hand.play(best.word);
        }else{
            Log.log("best move is null");
            Log.println("Computer cannot play any words. Exchanging hand");
            bag.exchange(hand);
        }
    }

    WordPosition bestMove(){
        StringBuilder str = hand.toStringBuilder();
        WordPosition best = null;
        int maxPoints=0;
        for(PlayedTile t : board.getPlayedTiles()){
            Log.log("checking tile "+t.letter);
            str.append(t.letter);
            log("checking with hand: "+str);
            String[] words = trie.viable(str.toString()).toArray(new String[0]);
            log("viable words: "+Arrays.toString(words));
            Orientation wordOrientation = board.adjacent(t.x, t.y);
            for(String w : words){
                int[] pos = board.computeOffset(t.x, t.y, wordOrientation, w.indexOf(t.letter));
                boolean placeable = board.placeable(pos[0], pos[1], w, wordOrientation.opposite());
                if(placeable){
                    int points = trie.checkPoint(w);
                    if(points>maxPoints){
                        maxPoints = points;
                        best = new WordPosition(pos[0], pos[1], wordOrientation.opposite(), w, points);
                    }
                }
            }
            str.deleteCharAt(str.length()-1);
        }
        if(board.getPlayedTiles().length==0){
            Log.log("no played tiles: checking best move with "+str.toString());
            best= new WordPosition(0, 0, Orientation.HORIZONTAL, trie.best(str.toString()), trie.checkPoint(str.toString()));
        }
        return best;
    }



    class WordPosition{
        int x, y;
        Orientation orientation;
        String word;
        int score;
        public WordPosition(int x, int y, Orientation orientation, String word, int score){
            this.x = x;
            this.y = y;
            this.orientation = orientation;
            this.word = word;
            this.score = score;
        }
    }

    public void log(Object msg){
        Log.log(msg);
    }
    
}
