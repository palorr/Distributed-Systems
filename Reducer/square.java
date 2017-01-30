import java.io.Serializable;

public class square implements Serializable {
    public double maxlo,minlo,maxla,minla ; 
    ///////
    public square(double minla,double minlo , double maxla ,  double maxlo ){
         this.maxlo = maxlo ; 
         this.minlo = minlo ; 
         this.maxla = maxla ; 
         this.minla = minla ; 
    }
    public square(String string){
        String[] parts = string.split("/");
        this.maxlo= Double.parseDouble(parts[0]);
        this.minlo= Double.parseDouble(parts[1]);
        this.maxla= Double.parseDouble(parts[2]);
        this.minla= Double.parseDouble(parts[3]);
    }
    public boolean tester(double x , double y){
        if(x>=minla && x<=maxla && y<=maxlo && y>=minlo) return true ; 
        else return false ; 
    }
    
    
    @Override
    public String toString(){
        return maxlo+" "+minlo+" "+maxla+" "+minla ;
    }
    
    
}
