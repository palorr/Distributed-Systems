import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;


public class RThread implements Runnable {
    protected Socket socket;
    protected Object message = null ;
    arrPack tmp ; 	
    
    private volatile arrPack value ; //the value we need to reducer
    
    public RThread(Socket clientSocket) {
        this.socket = clientSocket;
    }
    @Override
    public void run() {
       
        try {
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            message = in.readObject();
            tmp = (arrPack)message ; 
            //System.out.println("ok") ; 
            this.value = tmp ;  
            //System.out.println(value.toString());
        } 
        catch (IOException e) {
            return;
        }
		catch (ClassNotFoundException classnot) {
            System.err.println("Data received in unknown format");
		}
        
        
    }
    public arrPack getValue(){
        return value ;
    }
    
}
    


