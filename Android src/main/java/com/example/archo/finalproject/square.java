package com.example.archo.finalproject;

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
    public boolean tester(double x , double y){
        if(x>=minla && x<=maxla && y<=maxlo && y>=minlo) return true ; 
        else return false ; 
    }
    public String getString(){
        String tmp = this.maxlo+"/"+this.minlo+"/"+this.maxla+"/"+this.minla ;
        return tmp ;
    }

    
}
