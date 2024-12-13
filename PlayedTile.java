public class PlayedTile extends Tile {
    int x,y;
    public PlayedTile(char letter, byte points){
        super(letter, points);
    }
    public PlayedTile(char letter){
        super(letter);
    }
    public String toString(){
        return ""+Character.toUpperCase(letter);
    }
    public void move(int x, int y){
        this.x = x;
        this.y = y;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
}
