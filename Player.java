abstract class Player {
    Hand hand;
    int score;
    TileBag bag;
    WordTrie trie;
    Board board;
    public Player(TileBag bag, WordTrie trie, Board board){
        this.bag = bag;
        this.trie = trie;
        hand = new Hand(bag);
        score = 0;
        this.board = board;
    }
    public void takeTurn(){
        throw new UnsupportedOperationException();
    };
}
