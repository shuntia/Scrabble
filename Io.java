
import java.awt.event.KeyEvent;



public class Io {
    public static final Object lock=new Object();
    public static final boolean GUI=true;
    public static void print(Object msg){
        synchronized (lock) {
            if(GUI){
                Graphics.showMessage(msg.toString());;
            }else{
                Log.print(msg);
            }
        }
    }
    public static void println(Object msg){
        synchronized (lock) {
            if(GUI){
                Graphics.showMessage(msg.toString());
            }else{
                Log.println(msg);
            }
        }
    }
    public static String readLine(){
        synchronized (lock) {
            if(GUI){
                String result = Graphics.input();
                Log.log(result);
                Log.logger.flush();
                return result;
            }else{
                return System.console().readLine();
            }
        }
    }
    public static void printc(Object msg){
        synchronized (lock) {
            if(GUI){
                Graphics.showCheats(msg.toString());
            }else{
                Log.print(msg);
            }
        }
    }
    public static void printcln(Object msg){
        synchronized (lock) {
            if(GUI){
                Graphics.showCheats(msg.toString());
            }else{
                Log.println(msg);
            }
        }
    }
    public static int getKeyCode(){
        synchronized (lock) {
            if(GUI){
                return Graphics.getKey().getKeyCode();
            }else{
                return 0;
            }
        }
    }
    public static KeyEvent getKey(){
        synchronized (lock) {
            if(GUI){
                return Graphics.getKey();
            }else{
                return null;
            }
        }
    }
}
