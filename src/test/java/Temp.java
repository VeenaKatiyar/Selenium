import java.util.Map;
import java.util.Hashtable;
import java.util.HashMap;


	 
	public class Temp {
	 public static void main(String[] args) {
		 HashMap<String, String> TestData = new HashMap<String, String>();
	 
	 
	  TestData.put("CustomerName", "Swati");
	  
	  TestData.put("PhoneNo", "9960562879");
	  TestData.put("City", "Nagpur");
	  TestData.put("BillAmount", "5000");
	  TestData.put("CustomrtType", "Private");
	  TestData.put("Nationality", "Indian");	 
	 
	  //print size
	  System.out.println(TestData.size());
	  
	  System.out.println(TestData.get("CustomerName"));
	 }
	}
