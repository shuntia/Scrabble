import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Board {
    int width, height;
    char[][] board;
    WordTrie trie;
    public Board(int width, int height, WordTrie trie){
        this.width = width;
        this.height = height;
        board = new char[width][height];
    }

    public void set(int x, int y, char c) {
        board[x + width / 2][y + height / 2] = c;
    }

    public char get(int x, int y) {
        return board[x + width / 2][y + height / 2];
    }

    public int play(int x, int y, String word, Orientation orientation) {
        int pts=0;
        int dx = orientation.dx;
        int dy = orientation.dy;
        try {
            for (int i = 0; i < word.length(); i++) {
                if(board[x + i * dx + width / 2][y + i * dy + height / 2]!=0 && board[x + i * dx + width / 2][y + i * dy + height / 2]-'a'<0){
                    Log.log("point multiplier at "+(x + i * dx + width / 2)+", "+(y + i * dy + height / 2)+" is "+board[x + i * dx + width / 2][y + i * dy + height / 2]);
                    pts+=board[x + i * dx + width / 2][y + i * dy + height / 2]+trie.checkPoint(word.charAt(i));
                }
                board[x + i * dx + width / 2][y + i * dy + height / 2] = word.charAt(i);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            Log.log("Tried to fit using" + x + ", " + y + " " + orientation + " but failed");
        }
        return pts;
    }

    public int play(int x, int y, String word, Orientation orientation, int offset) {
        int dx = orientation.dx;
        int dy = orientation.dy;
        return play(x - offset * dx, y - offset * dy, word, orientation);
    }

    public boolean isOccupied(int x, int y) {
        return board[x + width / 2][y + height / 2] != 0;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("   ");
        for (int i = 0; i < width; i++) {
            str.append(String.format("%2d", i - width / 2)).append(" ");
        }
        str.append('\n');
        for (int i = 0; i < height; i++) {
            str.append(String.format("%2d", i - height / 2)).append(" ");
            for (int j = 0; j < width; j++) {
                str.append(board[j][i]-'a'>=0? board[j][i] : ' ');
                if(i==height/2 && j==width/2){
                    str.append("* ");
                }else{
                    switch(board[j][i]){
                        case 1 -> str.append("2 ");
                        case 2 -> str.append("3 ");
                        default -> {
                            if(board[j][i]-'a'<0 && board[j][i]!=0){
                                Log.log("Unrecognized flag in board at " + j + ", " + i + ": " + board[j][i]);
                            }
                            str.append("  ");
                        }
                    }
                }
            }
            str.append('\n');
        }
        return str.toString();
    }

    public boolean fits(int x, int y, String word, Orientation orientation) {
        int dx = orientation.dx;
        int dy = orientation.dy;

        for (int i = 0; i < word.length(); i++) {
            int nx = x + i * dx + width / 2;
            int ny = y + i * dy + height / 2;

            if (nx < 0 || nx >= width || ny < 0 || ny >= height) {
                return false;
            }

            if (board[nx][ny] != 0 && board[nx][ny] != word.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    public boolean fits(int x, int y, String word, Orientation orientation, int offset) {
        int dx = orientation.dx;
        int dy = orientation.dy;
        return fits(x - offset * dx, y - offset * dy, word, orientation);
    }

    public boolean placeable(int x, int y, String word, Orientation orientation){
        if(board[width/2][height/2]==0){
            switch(orientation){
                case HORIZONTAL -> {
                    return x<=width/2 && x+word.length()>=width/2;
                }
                case VERTICAL -> {
                    return y<=height/2 && y+word.length()>=height/2;
                }
                default -> throw new IllegalArgumentException("Unexpected orientation: " + orientation);
            }
        }
        if(!fits(x, y, word, orientation)){
            return false;
        }
        boolean overlap = false;
        boolean same=true;
        int dx = orientation.dx;
        int dy = orientation.dy;
        for(int i=0; i<word.length(); i++){
            int nx = x + i*dx + width/2;
            int ny = y + i*dy + height/2;
            if(board[nx][ny]!=0||(nx==width/2 && ny==height/2)){
                overlap=true;
            }
            if(board[nx][ny]!=word.charAt(i)){
                same=false;
            }
            String around = around(nx, ny, word.charAt(i));
            if(around!=null){
                if(!trie.isWord(around)){
                    Log.log("Word "+around+" is not a word");
                    return false;
                }
            }
        }
        if(same)Log.log("Word is the same");
        if(!overlap)Log.log("Word does not overlap at all");
        return (!same)&&overlap;
    }

    public String around(int x, int y, char center){
        StringBuilder str = new StringBuilder();
        //horizontally existing
        if(board[x-1][y]!=0||board[x+1][y]!=0){
            //traverse to the left edge
            while(x>0 && board[x-1][y]!=0){
                x--;
            }
            //traverse to the right edge
            while(x<width && board[x][y]!=0){
                str.append(board[x][y]);
                x++;
            }
        }
        //vertically existing
        if(board[x][y-1]!=0){
            //traverse to the top edge
            while(y>0 && board[x][y-1]!=0){
                y--;
            }
            //traverse to the bottom edge
            while(y<height && board[x][y]!=0){
                str.append(board[x][y]);
                y++;
            }
        }
        return str.isEmpty()?null:str.toString();
    }

    public PlayedTile[] getPlayedTiles() {
        List<PlayedTile> playedTiles = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (board[j][i] != 0) {
                    PlayedTile playedTile = new PlayedTile(board[j][i]);
                    playedTile.move(j - width / 2, i - height / 2);
                    playedTiles.add(playedTile);
                }
            }
        }
        return playedTiles.toArray(PlayedTile[]::new);
    }

    public HashMap<String, Object> fit(String word) {
        HashMap<String, Object> result = new HashMap<>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                for (Orientation orientation : Orientation.values()) {
                    for (int offset = 0; offset < word.length(); offset++) {
                        if (fits(i, j, word, orientation, offset)) {
                            result.put("x", i - width / 2);
                            result.put("y", j - height / 2);
                            result.put("orientation", orientation);
                            result.put("offset", offset);
                            return result;
                        }
                    }
                }
            }
        }
        return null;
    }
}
