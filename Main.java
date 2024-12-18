


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
        log("is 'hello' a word? " + trie.isWord("hello"));
        log("is 'helloo' a word? " + trie.isWord("helloo"));
        log("how many points is 'hello'? " + trie.checkPoint("hello"));
        log("What can be made from 'hello'? " + trie.viable("hello"));
        log("is 'world' a word? " + trie.isWord("world"));
        log("is 'scrabble' a word? " + trie.isWord("scrabble"));
        log("how many points is 'world'? " + trie.checkPoint("world"));
        log("how many points is 'scrabble'? " + trie.checkPoint("scrabble"));
        log("What can be made from 'world'? " + trie.viable("world"));
        log("What can be made from 'scrabble'? " + trie.viable("scrabble"));      log("making board");
        Board board = new Board(20,20,trie);
        log("board made");
        log("creating player");
        Player player = new HumanPlayer(bag, trie, board);
        log("player created");
        log("player taking turn");
        while(true){
            Graphics.showBoard(board);
            Graphics.showHand(player.hand);
            player.takeTurn();
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