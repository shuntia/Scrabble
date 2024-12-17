import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class WordTrie {
    public final byte[] letterValues = new byte[26];
    final TrieNode root = new TrieNode();
    public WordTrie(){
    }
    public void loadEverything(){
        loadPoints();
        loadFile("resources/scrabble_words.txt", false);
    }
    public void addWord(String word){
        TrieNode node = root;
        for(char c : word.toCharArray()){
            if(node.children==null){
                node.children = new TrieNode[26];
            }
            if(node.children[c-'a']==null){
                node.children[c-'a'] = new TrieNode();
            }
            node = node.children[c-'a'];
        }
    }
    public int sameTo(String w1, String w2){
        int i=0;
        while(i<w1.length() && i<w2.length() && w1.charAt(i)==w2.charAt(i)){
            i++;
        }
        return i;
    }
    public void loadFile(String filename, Boolean isOrdered){
        try {
            InputStream sc = WordTrie.class.getResourceAsStream(filename);
            ArrayList<TrieNode> nodes = new ArrayList<>();
            nodes.add(root);
            TrieNode node = root;
            String word="", lastWord="";
            while(sc.available()>0){
                StringBuilder sb = new StringBuilder();
                char ch;
                while(sc.available()>0 && Character.isAlphabetic(ch=(char)sc.read())){
                    if(ch=='\n'){
                        break;
                    }
                    sb.append(ch);
                }
                word = sb.toString();
                if(isOrdered){
                    if(word.compareTo(lastWord)<0){
                        nodes.clear();
                        nodes.add(root);
                        node=root;
                    }
                    int i = sameTo(word, lastWord);
                    for(int j=nodes.size()-1; j>=i; j--){
                        nodes.remove(j);
                    }
                    node = nodes.isEmpty()?root:nodes.get(nodes.size()-1);
                }else{
                    node = root;
                }
                for(char c : word.toCharArray()){
                    if(node.children==null){
                        node.children = new TrieNode[26];
                    }
                    if(node.children[c-'a']==null){
                        node.children[c-'a'] = new TrieNode();
                    }
                    node = node.children[c-'a'];
                    nodes.add(node);
                }
                node.endOfWord = true;
                lastWord = word;
            }
        } catch (Exception e) {
            Log.log("Failed to load file "+filename+"!");
            e.printStackTrace(Log.logstream);
        }
        root.endOfWord = false;
    }

    public void loadFile(String filename){
        loadFile(filename, true);
    }

    public void loadPoints(){
        try {
            InputStream fi = WordTrie.class.getResourceAsStream("resources/letter_points.txt");
            byte pts = 0;
            while(fi.available()>0){
                char letter = (char)fi.read();
                if(Character.isAlphabetic(letter)){
                    letterValues[letter-'a'] = pts;
                }else{
                    pts++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String toString(){
        return root.toString();
    }
    public String[] startsWith(String prefix){
        TrieNode node = root;
        for(char c : prefix.toCharArray()){
            if(node.children==null || node.children[c-'a']==null){
                return new String[0];
            }
            node = node.children[c-'a'];
        }
        ArrayList<String> words = new ArrayList<>();
        wordsUnder(node, prefix, words);
        return words.toArray(String[]::new);
    }
    public void wordsUnder(TrieNode node, String prefix, ArrayList<String> words){
        if(node.endOfWord){
            words.add(prefix);
        }
        if(node.children!=null){
            for(int i=0; i<26; i++){
                if(node.children[i]!=null){
                    wordsUnder(node.children[i], prefix+(char)(i+'a'), words);
                }
            }
        }
    }
    public HashSet<String> viable(Hand hand) {
        HashSet<String> words = new HashSet<>();
        if (hand != null) {
            words.addAll(viableRecurse("", root, hand.toCharArray()));
        }
        words.removeIf(word -> word.contains(" ")); // Remove words containing spaces
        return words;
    }

    public HashSet<String> viable(HashSet<Tile> hand) {
        char[] handChars = new char[hand.size()];
        int i = 0;
        for (Tile tile : hand) {
            handChars[i++] = tile.letter;
        }
        HashSet<String> words = new HashSet<>();
        words.addAll(viableRecurse("", root, handChars));
        words.removeIf(word -> word.contains(" ")); // Remove words containing spaces
        return words;
    }

    public HashSet<String> viable(char[] hand) {
        HashSet<String> words = new HashSet<>();
        words.addAll(viableRecurse("", root, hand));
        words.removeIf(word -> word.contains(" ")); // Remove words containing spaces
        return words;
    }

    public HashSet<String> viable(String hand) {
        HashSet<String> words = new HashSet<>();
        words.addAll(viableRecurse("", root, hand.toCharArray()));
        words.removeIf(word -> word.contains(" ")); // Remove words containing spaces
        return words;
    }
    
    private HashSet<String> viableRecurse(String prefix, TrieNode node, char[] hand) {
        HashSet<String> words = new HashSet<>();
        if (node.endOfWord || node.children == null || node.children.length == 0) {
            words.add(prefix);
        }
        for (int i = 0; i < hand.length; i++) {
            char c = hand[i];
            if (c != 0 && node.children != null && node.children[c - 'a'] != null) {
                char[] cloned=hand.clone();
                cloned[i]=0;
                words.addAll(viableRecurse(prefix + c, node.children[c - 'a'], cloned));
            }
        }
        return words;
    }

    public boolean isWord(String word){
        TrieNode node = root;
        for(char c : word.toCharArray()){
            if(node.children==null || node.children[c-'a']==null){
                return false;
            }
            node = node.children[c-'a'];
        }
        return node.endOfWord;
    }

    public int checkPoint(String word){
        int points = 0;
        for(char c : word.toCharArray()){
            points += letterValues[c-'a'];
        }
        return points;
    }

    public int checkPoint(char c){
        return letterValues[c-'a'];
    }

    public String best(String[] words){
        String maxWord = "";
        int maxPoints = 0;
        for(String word : words){
            int points = checkPoint(word);
            if(points>maxPoints){
                maxPoints = points;
                maxWord = word;
            }
        }
        return maxWord;
    }

    public String best(String word){
        return best(viable(word).toArray(String[]::new));
    }
}

class TrieNode{
    public TrieNode[] children;
    public boolean endOfWord;
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        toStringHelper(this, new StringBuilder(), str);
        return str.toString();
    }

    public String toStringArray(){
        return Arrays.toString(children);
    }

    private void toStringHelper(TrieNode node, StringBuilder prefix, StringBuilder result) {
        if (node.endOfWord) {
            result.append(prefix.toString()).append("\n");
        }
        if (node.children != null) {
            for (int i = 0; i < 26; i++) {
                if (node.children[i] != null) {
                    prefix.append((char) (i + 'a'));
                    toStringHelper(node.children[i], prefix, result);
                    prefix.deleteCharAt(prefix.length() - 1);
                }
            }
        }
    }
}
