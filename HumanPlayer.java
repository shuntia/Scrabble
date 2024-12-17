
import java.awt.event.KeyEvent;
import java.util.Arrays;

public class HumanPlayer extends Player {
    String name;
    boolean cheats = false;
    public HumanPlayer(String name, TileBag bag, WordTrie trie, Board board) {
        super(bag, trie, board);
        this.name = name;
    }
    public HumanPlayer(TileBag bag, WordTrie trie, Board board) {
        super(bag, trie, board);
        this.name = "Human";
    }
    public HumanPlayer(String name, TileBag bag, WordTrie trie, Board board, boolean cheats) {
        super(bag, trie, board);
        this.name = name;
        this.cheats = cheats;
    }
    @Override
    public void takeTurn(){
        Log.log("Starting player turn");
        bag.fill(hand);
        boolean valid = false;
        while(!valid){
            if(!Io.GUI){
                Io.println("Hand is "+hand);
            }
            try{
                if(cheats){
                    Io.println(trie.viable(hand));
                    Io.println("best word is: "+trie.best(trie.viable(hand).toArray(new String[0]))+" worth "+trie.checkPoint(trie.best(trie.viable(hand).toArray(new String[0]))));
                }
                String word = Io.readLine();
                if(word.charAt(0)=='!'){
                    if(word.contains("cheats")){
                        if(word.contains("on")){
                            cheats = true;
                            Io.println("Cheats enabled");
                        }else{
                            cheats = false;
                            Io.println("Cheats disabled");
                        }
                        continue;
                    }
                    if(word.contains("exchange")){
                        bag.exchange(hand);
                        Io.println("Hand exchanged");
                        Io.println("Hand is now "+hand);
                        return;
                    }
                    if(word.contains("pass")){
                        Io.println("Player "+name+" has passed");
                        return;
                    }
                    if(word.contains("quit")){
                        Io.println("Player "+name+" has quit");
                        Runtime.getRuntime().exit(0);
                    }
                    if(word.contains("isword")){
                        Io.println("Word is "+trie.isWord(word.substring(8)));
                        continue;
                    }
                }
                boolean alpha=true;
                for(char c : word.toCharArray()){
                    if(c<'a' || c>'z'){
                        Io.println("Invalid characters contained: "+c);
                        alpha = false;
                        break;
                    }
                }
                if(!alpha){
                    continue;
                }
                if(trie.isWord(word) && hand.contains(word)){
                    valid = true;
                    Io.println("Valid word!\nWord is worth "+trie.checkPoint(word)+" points");
                    if(!Io.GUI){
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
                                Io.println("Accepted orientations are: " + Arrays.toString(Orientation.values()));
                                Log.print("Invalid orientation, try again:");
                            }
                        }
                        Log.print("Enter offset:");
                        int offset = Integer.parseInt(System.console().readLine());
                        if(board.fits(x, y, word, orientation)){
                            board.play(x, y, word, orientation, offset);
                            hand.play(word);
                            Io.println("Word played!");
                            Log.log("\n"+board);
                        }else{
                            Io.println("Word does not fit!");
                            valid = false;
                            continue;
                        }
                        Io.println("Hand is now "+hand);
                    }else{
                        Io.println("move with arrow keys, spin with z, enter to confirm");
                        //set position
                        int x=0;
                        int y=0;
                        Orientation orientation = Orientation.HORIZONTAL;
                        int k;
                        while (true) { 
                            k=Io.getKeyCode();
                            if(k==0){
                                continue;
                            }else{
                                Log.log("Key pressed: "+k);
                            }
                            if(k==KeyEvent.VK_UP){
                                y--;
                            }
                            if(k==KeyEvent.VK_DOWN){
                                y++;
                            }
                            if(k==KeyEvent.VK_LEFT){
                                x--;
                            }
                            if(k==KeyEvent.VK_RIGHT){
                                x++;
                            }
                            if(k==KeyEvent.VK_Z){
                                orientation = orientation.opposite();
                            }
                            if(k==KeyEvent.VK_ENTER){
                                break;
                            }
                            Graphics.showBoard(board.overlay(x, y, orientation, word));
                        }
                    }
                }else{
                    Io.println(trie.isWord(word)?"You don't have enough letters!":"Word does not exist!");
                }
            }catch(NumberFormatException e){
                Io.println("Invalid input!");
                valid = false;
            }
            catch(StringIndexOutOfBoundsException e){
                Io.println("Invalid input!");
                valid = false;
            }
            catch(Exception e){
                e.printStackTrace(Log.logstream);
                Io.println("Unexpected error: "+e.getMessage());
                Runtime.getRuntime().exit(1);
            }
        }
    }
}
