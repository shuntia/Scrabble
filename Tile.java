public class Tile {
    char letter;
    byte points;
    public Tile(char letter, byte points){
        this.letter = letter;
    }
    public Tile(char letter){
        this.letter = letter;
    }

    public String toString(){
        return ""+Character.toUpperCase(letter);
    }
}
