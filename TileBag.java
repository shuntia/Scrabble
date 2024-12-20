
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;

public class TileBag {
    HashSet<Tile> tiles = new HashSet<>();
    static HashMap<Character, Byte> letterValues = new HashMap<>();
    static{
        try {
            InputStream fi;
            fi = TileBag.class.getResourceAsStream("resources/letter_points.txt");
            byte points=0;
            while(fi.available()>0){
                char letter = (char)fi.read();
                if(letter=='\n'){
                    points++;
                }else if(Character.isAlphabetic(letter)){
                    letterValues.put(letter, points);
                }
            }
            fi.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public TileBag(){
        for(char letter : letterValues.keySet()){
            for(int i=0; i<12; i++){
                tiles.add(new Tile(letter));
            }
        }
    }
    public TileBag(int sets, boolean superScrabble){
        InputStream fi = null;
        byte distribution[] = new byte[26];
        try {
            fi = TileBag.class.getResourceAsStream(superScrabble?"resources/tile_super.txt":"resources/tile.txt");
            byte count=1;
            while(fi.available()>0){
                char letter = (char)fi.read();
                if(letter=='\n'){
                    count++;
                }else if(Character.isAlphabetic(letter)){
                    distribution[letter-'a'] = count;
                }
            }
            for(int i=0; i<sets; i++){
                for(int j=0; j<26; j++){
                    for(int k=0; k<distribution[j]; k++){
                        tiles.add(new Tile((char)('a'+j)));
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                fi.close();
            } catch (IOException ex) {
            } catch (NullPointerException ex) {
            }
        }
    }
    public Tile draw(){
        Tile tile = null;
        if(!tiles.isEmpty()){
            int index = (int)(Math.random()*tiles.size());
            int i=0;
            for(Tile t : tiles){
                if(i==index){
                    tile = t;
                    break;
                }
                i++;
            }
            tiles.remove(tile);
        }else{
            Log.log("TileBag is empty");
        }
        return tile;
    }
    public void add(Tile tile){
        tiles.add(tile);
    }
    public Tile[] draw(int n){
        Tile[] drawn = new Tile[n];
        for(int i=0; i<n; i++){
            drawn[i] = draw();
        }
        return drawn;
    }
    public void fill(Hand hand){
        while(hand.tiles.size()<hand.size){
            hand.add(draw());
        }
    }
    public byte valueOf(char letter){
        return letterValues.get(letter);
    }
    public byte valueOf(Tile tile){
        return letterValues.get(tile.letter);
    }
    public int size(){
        return tiles.size();
    }
    public void exchange(Hand hand, int index){
        for(Tile t : hand.tiles){
            if(t!=null){
                tiles.add(t);
            }
            t=draw();
        }
    }
    public void exchange(Hand hand){
        for(Tile t : hand.tiles){
            if(t!=null){
                tiles.add(t);
            }
        }
        hand.tiles.clear();
        this.fill(hand);
    }
    @Override
    public String toString(){
        String str = "";
        for(Tile t : tiles){
            str += t + " ";
        }
        return str;
    }
    public int[] occurences(){
        int[] occurences = new int[26];
        for(Tile t : tiles){
            occurences[t.letter-'a']++;
        }
        return occurences;
    }
}
