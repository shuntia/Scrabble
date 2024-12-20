import java.util.Arrays;

public class Main {
    private static WordTrie trie;
    private static TileBag bag;
    private static Board board;

    public static void main(String[] args) {
        log("init");
        log("loading dictionary");
        trie = new WordTrie();
        trie.loadEverything();
        log("dictionary loaded");
        log("loading bag");
        bag = new TileBag();
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
        log("What can be made from 'scrabble'? " + trie.viable("scrabble"));
        log("making board");
        board = new Board(20, 20, trie);
        log("board made");
        log("creating player");
        Player[] players;
        log("player created");
        log("intro");
        Graphics.showBoard("Welcome to Scrabble!\n!exchange to exchange\n!pass to pass\n!quit to quit\nPrompt is at the bottom.\n\nPress Enter to continue");
        Graphics.showMessage("Enter to continue");
        try {
            synchronized (Graphics.enterLock) {
                Graphics.enterLock.wait();
            }
        } catch (InterruptedException e) {
            log(e.toString());
        }
        try{players=new Player[]{new HumanPlayer("Player 1", bag, trie, board), new ComputerPlayer( bag, trie, board)};}
        catch(Exception e){log(e.toString());players = new Player[]{new HumanPlayer(bag, trie, board)};}
        log(players.length + " Players created: " + Arrays.toString(players));
        log("player taking turn");
        for (Player p : players) {
            p.takeTurn();
        }
    }

    public static void log(String msg) {
        Log.log(msg);
    }
}
