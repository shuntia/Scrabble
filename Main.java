


public class Main{
    public static void main(String[] args){
        log("init");
        log("loading dictionary");
        WordTrie trie = new WordTrie();
        trie.loadEverything();
        log("dictionary loaded");
        log("loading bag");
        TileBag bag = new TileBag();
        log("bag loaded");
        log("Starting tests...");
        log("is 'hello' a word? "+trie.isWord("hello"));
        log("is 'helloo' a word? "+trie.isWord("helloo"));
        log("how many points is 'hello'? "+trie.checkPoint("hello"));
        log("making board");
        Board board = new Board(20,20,trie);
        log("board made");
        log("creating player");
        HumanPlayer player = new HumanPlayer("test",bag, trie, board);
        HumanPlayer player2 = new HumanPlayer("test2",bag, trie, board);
        log("player created");
        log("player taking turn");
        while(true){
            Log.log(board);
            player.takeTurn();
            player2.takeTurn();
        }
        /*
        TileBag bag = new TileBag();
        Hand hand = new Hand(bag);
        Board board = new Board(20,20);
        System.out.println(board);
        System.out.println(hand);
        boolean valid=false;
        while(!valid){
            System.out.println("Enter a word:");
            String word = System.console().readLine();
            valid = Dictionary.isWord(word) && hand.contains(word);
            if(valid){
                System.out.println("Valid word!");
                hand.play(word);
                System.out.println(Dictionary.valueOf(word));
                board.play(0,0,word,0);
                System.out.println(board);
            }else{
                System.out.println("Invalid word!");
            }
        }*/
    }
    public static void log(String msg){
        Log.log(msg);
    }
}