
public class ComputerPlayer extends Player{
    public ComputerPlayer(TileBag bag, WordTrie trie, Board board){
        super(bag, trie, board);
    }
    @Override
    public void takeTurn() {
        bag.fill(hand);
        Log.log("Computer has hand " + hand);
    }

    WordPosition bestMove(){
        StringBuilder str = hand.toStringBuilder();
        WordPosition best = null;
        for(PlayedTile t : board.getPlayedTiles()){
            str.append(t.letter);
            String[] words = trie.viable(str.toString()).toArray(new String[0]);
            Orientation wordOrientation = board.adjacent(t.x, t.y);
            for(String w : words){
                int[] pos = board.computeOffset(t.x, t.y, wordOrientation, w.indexOf(t.letter));
                boolean placeable = board.placeable(pos[0], pos[1], w, wordOrientation.opposite());
            }
            str.deleteCharAt(str.length()-1);
        }
        if(board.getPlayedTiles().length==0){
            return new WordPosition(0, 0, Orientation.HORIZONTAL, trie.best(str.toString()), trie.checkPoint(str.toString()));
        }
        return null;
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
