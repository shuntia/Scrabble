
import java.util.Arrays;

public class HumanPlayer extends Player {
    String name;
    boolean cheats = false;
    public HumanPlayer(String name, TileBag bag, WordTrie trie, Board board) {
        super(bag, trie, board);
        this.name = name;
    }
    public HumanPlayer(String name, TileBag bag, WordTrie trie, Board board, boolean cheats) {
        super(bag, trie, board);
        this.name = name;
        this.cheats = cheats;
    }
    @Override
    public void takeTurn(){
        Log.println("Starting player turn");
        bag.fill(hand);
        Log.println(board);
        boolean valid = false;
        while(!valid){
            Log.println("Player "+name+" has hand " + hand);
            try{
                if(cheats){
                    Log.println(trie.viable(hand));
                    Log.println("best word is: "+trie.best(trie.viable(hand).toArray(new String[0]))+" worth "+trie.checkPoint(trie.best(trie.viable(hand).toArray(new String[0]))));
                }
                Log.print("Enter a word:");
                String word = System.console().readLine();
                if(word.charAt(0)=='!'){
                    if(word.contains("cheats")){
                        if(word.contains("on")){
                            cheats = true;
                            Log.println("Cheats enabled");
                        }else{
                            cheats = false;
                            Log.println("Cheats disabled");
                        }
                        continue;
                    }
                    if(word.contains("exchange")){
                        bag.exchange(hand);
                        Log.println("Hand exchanged");
                        Log.println("Hand is now "+hand);
                        return;
                    }
                }
                boolean alpha=true;
                for(char c : word.toCharArray()){
                    if(c<'a' || c>'z'){
                        Log.println("Invalid characters contained: "+c);
                        alpha = false;
                        break;
                    }
                }
                if(!alpha){
                    continue;
                }
                if(trie.isWord(word) && hand.contains(word)){
                    valid = true;
                    Log.println("Valid word!");
                    Log.println("Word is worth "+trie.checkPoint(word)+" points");
                    Log.print("Enter x:");
                    int x = Integer.parseInt(System.console().readLine());
                    Log.print("Enter y:");
                    int y = Integer.parseInt(System.console().readLine());
                    Log.print("Enter orientation:");
                    Orientation orientation=null;
                    while(true){
                        try{
                            orientation = Orientation.valueOf(System.console().readLine().toUpperCase());
                            break;
                        }catch(IllegalArgumentException e){
                            Log.println("Accepted orientations are: " + Arrays.toString(Orientation.values()));
                            Log.print("Invalid orientation, try again:");
                        }
                    }
                    Log.print("Enter offset:");
                    int offset = Integer.parseInt(System.console().readLine());
                    if(board.fits(x, y, word, orientation)){
                        board.play(x, y, word, orientation, offset);
                        hand.play(word);
                        Log.println("Word played!");
                        Log.log("\n"+board);
                    }else{
                        Log.println("Word does not fit!");
                        valid = false;
                        continue;
                    }
                    Log.println("Hand is now "+hand);
                }else{
                    Log.println(trie.isWord(word)?"You don't have enough letters!":"Word does not exist!");
                }
            }catch(NumberFormatException e){
                Log.println("Invalid input!");
                valid = false;
            }
            catch(Exception e){
                e.printStackTrace(Log.logstream);
                Log.println("Unexpected error: "+e.getMessage());
                Runtime.getRuntime().exit(1);
            }
        }
    }
}
