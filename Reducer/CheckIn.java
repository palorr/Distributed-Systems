import java.sql.Timestamp;
import java.util.Date;


public class CheckIn {
	
		   int id ; 
		   int user ; 
		   String POI ; 
		   String POI_name ; 
		   String POI_category ; 
		   int POI_category_id ; 
		   double latitude ; 
		   double longitude ; 
		   Timestamp time ; 
		   String photos ;
		   Date date ; 
		   public CheckIn(int id, int user, String POI, String POI_name, String POI_category, int POI_category_id, double latitude, double longitude, Timestamp time, String photos){
			   this.id = id ; 
			   this.user = user ; 
			   this.POI = POI ; 
			   this.POI_name = POI_name ; 
			   this.POI_category = POI_category ; 
			   this.POI_category_id = POI_category_id ; 
			   this.latitude = latitude ; 
			   this.longitude = longitude ; 
			   this.time = time ;
			   this.photos = photos ; 
                           date = new java.util.Date(time.getTime());
                           //System.out.println(date) ;
                   }
                   public String toString(){
                        return id+" "+user+" "+POI+" "+POI_name+" "+POI_category+" "+POI_category_id+" "+latitude+" "+longitude+" "+time+" "+photos ; 
                   }
                   public Date getDate(){
                       return date ; 
                   }
                   public double getX(){
                       return this.latitude ; 
                   }
                   public double getY(){
                       return this.longitude ; 
                   }
                   public String getPOI(){
                       return this.POI ; 
                   }
		   
		    
		   
		   
			
}


		//id,user,POI,POI_name,POI_category,POI_category_id,latitude,longitude,time,photos

