import java.awt.event.KeyEvent;



public class Io {
    private static boolean GUI=true;
    public static void print(Object msg){
        if(GUI){
            Graphics.showMessage(msg.toString());
        }else{
            Log.print(msg);
        }
    }
    public static void println(Object msg){
        if(GUI){
            Graphics.showMessage(msg.toString());
        }else{
            Log.println(msg);
        }
    }
    public static String readLine(){
        if(GUI){
            String result = Graphics.input();
            while (result == null || result.isEmpty()) {
                result = Graphics.input();
            }
            return result;
        }else{
            return System.console().readLine();
        }
    }
    public static void printc(Object msg){
        if(GUI){
            Graphics.showCheats(msg.toString());
        }else{
            Log.print(msg);
        }
    }
    public static void printcln(Object msg){
        if(GUI){
            Graphics.showCheats(msg.toString());
        }else{
            Log.println(msg);
        }
    }
    public static int getKeyCode(){
        if(GUI){
            return Graphics.getKey().getKeyCode();
        }else{
            return 0;
        }
    }
    public static KeyEvent getKey(){
        if(GUI){
            return Graphics.getKey();
        }else{
            return null;
        }
    }
    public static void flush(){
        if(!GUI){
            Log.logger.flush();
        }
    }
    public static KeyEvent[] getBuffer(){
        if(GUI){
            return Graphics.getBuffer();
        }else{
            return null;
        }
    }
    public static String transcribe(KeyEvent[] keys){
        StringBuilder result = new StringBuilder();
        for(KeyEvent k:keys){
            if(k==null)continue;
            if(k.getKeyCode()==KeyEvent.VK_BACK_SPACE){
                result.setLength(result.length()!=0?result.length() - 1:0);
            }
            if(Character.isAlphabetic(k.getKeyChar())){
                result.append(k.getKeyChar());
            }else if(Character.isDigit(k.getKeyChar())){
                result.append(k.getKeyChar());
            }else if(k.getExtendedKeyCode()==KeyEvent.VK_SPACE){
                result.append(" ");
            }else if(k.getKeyChar()=='!'){
                result.append("!");
            }else if(k.getExtendedKeyCode()==KeyEvent.VK_BACK_SPACE){
                result.setLength(result.length()!=0?result.length() - 1:0);
            }
        }
        return result.toString();
    }
    public static void setGUI(boolean gui){
        if(!gui){
            Graphics.dispose();
        }
        GUI = gui;
    }
    public static boolean isGUI(){
        return GUI;
    }
}
