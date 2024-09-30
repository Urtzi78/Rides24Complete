import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.junit.Test;

import dataAccess.DataAccess;
import testOperations.TestDataAccess;
import domain.Driver;

public class GauzatuEragiketaBDWhiteTest {
	
	//sut:system under test
		 static DataAccess sut=new DataAccess();
		 
		 //additional operations needed to execute the test 
		 static TestDataAccess testDA=new TestDataAccess();

		@SuppressWarnings("unused")
		private Driver driver; 
		
		@Test
		//sut.GauzatuEragiketa. username==null
		public void test1() {
			try{
				String username=null;
				Double amount=5.9;
			
				sut.open();
				boolean emaitza=sut.gauzatuEragiketa(username, amount, true);
				
				assertFalse(emaitza);
				
			} catch(Exception e) {
				fail();
			}
		}
		

}
