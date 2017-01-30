import java.io.Serializable;
import java.util.Date;


public class doubledate implements Serializable{
    Date d1,d2 ; 
    public doubledate(Date d1 , Date d2){
        this.d1= d1 ; 
        this.d2 = d2 ; 
    }
    public Date getD1(){
        return d1 ; 
    }
    public Date getD2(){
        return d2 ; 
    }
}
