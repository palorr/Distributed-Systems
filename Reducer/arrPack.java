import java.io.Serializable;
public class arrPack implements Serializable {
    String[] str  ; 
    int[] i ; 
    public arrPack(String[] str , int[] i ){
        this.str = str ; 
        this.i = i ; 
    }
    public String[] getStr(){
        return this.str ; 
    }
    public int[] getI(){
        return this.i ; 
    }
	
    @Override
    public String toString(){
		return str[1]; 
    }
            
}
