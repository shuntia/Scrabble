
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
@Deprecated
public class Dictionary {
    static final HashSet<String> words = new HashSet<>();
    static final HashMap<Character, Byte> letterValues = new HashMap<>();
    static {
        System.out.println("Loading dictionary...");
        try (Scanner sc = new Scanner(Dictionary.class.getResourceAsStream("resources/words_alpha.txt"))) {
            while(sc.hasNext()){
                words.add(sc.nextLine());
            }
        }catch(Exception ex){
            ex.printStackTrace(Log.logstream);
        }
        System.out.println("Loading letter points...");
        try {
            InputStream fi = Dictionary.class.getResourceAsStream("resources/letter_points.txt");
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
            ex.printStackTrace(Log.logstream);
        }
    }
    public static boolean isWord(String word){
        return words.contains(word);
    }
    public static void addWord(String word){
        words.add(word);
    }
    public static void removeWord(String word){
        words.remove(word);
    }
    public static void clear(){
        words.clear();
    }
    public static HashMap<Character, Byte> getLetterValues(){
        return letterValues;
    }
    public static void setLetterValue(char letter, byte points){
        letterValues.put(letter, points);
    }  
    public static void removeLetterValue(char letter){
        letterValues.remove(letter);
    }
    public static void clearLetterValues(){
        letterValues.clear();
    }
    public static HashSet<String> getWords(){
        return words;
    }
    public static void addWords(HashSet<String> words){
        words.addAll(words);
    }
    public static void removeWords(HashSet<String> words){
        words.removeAll(words);
    }
    public static void clearWords(){
        words.clear();
    }
    public static int valueOf(String word){
        int points = 0;
        for(char c : word.toCharArray()){
            points += letterValues.get(c);
        }
        return points;
    }
}
