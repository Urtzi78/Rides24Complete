import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import dataAccess.DataAccess;
import domain.User;
import testOperations.TestDataAccess;

public class GauzatuEragiketaBDBlackTest {
	
	//sut:system under test
		 static DataAccess sut=new DataAccess();
		 
		 //additional operations needed to execute the test 
		 static TestDataAccess testDA=new TestDataAccess();

		@SuppressWarnings("unused")
		private User user; 

		@Test
		// u!=null, amount!=null, username!=null, deposit!=null, amount>=0, deposit==true 
		public void test1() {
			String username="Jon";
			String password="454";
			Double money=6.7;
			try {
				Double amount=15.0;
				boolean deposit=true;
				
				testDA.open();
				testDA.createUser(username, password, money);
				testDA.close();
				
				sut.open();
				boolean emaitza=sut.gauzatuEragiketa(username, amount, deposit);
				

				assertTrue(emaitza);
			} catch(Exception e) {
				fail();
			} finally {
			sut.close();
			
			testDA.open();
			testDA.removeUser(username);
			testDA.close();
			}
		}
		
		@Test 
		//user ez dago DBn
		public void test2() {
			try {
				String username="Jon";
				Double amount=15.0;
			
				sut.open();
				boolean emaitza=sut.gauzatuEragiketa(username, amount, true);
				
				assertFalse(emaitza);
			} catch(Exception e) {
				fail();
			} finally {
			sut.close();
			}
		}
		
		@Test
		// deposit!=true && amount<currentMoney 
		public void test3() {
			String username="Jon";
			String password="454";
			Double money=26.7;
			try {
				Double amount=15.0;
				boolean deposit=false;
				
				testDA.open();
				testDA.createUser(username, password, money);
				testDA.close();
				
				sut.open();
				boolean emaitza=sut.gauzatuEragiketa(username, amount, deposit);
				

				assertTrue(emaitza);
			} catch(Exception e) {
				fail();
			} finally {
			sut.close();
			
			testDA.open();
			testDA.removeUser(username);
			testDA.close();
			}
		}
		
		@Test
		//deposit!=true && amount>=currentMoney
		public void test4() {
			String username="Jon";
			String password="454";
			Double money=6.7;
			try {
				Double amount=15.0;
				boolean deposit=false;
				
				testDA.open();
				testDA.createUser(username, password, money);
				testDA.close();
				
				sut.open();
				boolean emaitza=sut.gauzatuEragiketa(username, amount, deposit);
				

				assertTrue(emaitza);
			} catch(Exception e) {
				fail();
			} finally {
			sut.close();
			
			testDA.open();
			testDA.removeUser(username);
			testDA.close();
			}
		}
		
		
		
		@Test
		//username== null
		public void test5() {
			String username="Jon";
			String password="454";
			Double money=20.0;
			try {
				Double amount=15.0;
				boolean deposit=false;
				
				testDA.open();
				testDA.createUser(username, password, money);
				testDA.close();
				
				sut.open();
				boolean emaitza=sut.gauzatuEragiketa(null, amount, deposit);
				

				assertFalse(emaitza);
			} catch(Exception e) {
				fail();
			} finally {
			sut.close();
			
			testDA.open();
			testDA.removeUser(username);
			testDA.close();
			}
		}		
		
		
		
		@Test
		//amount<0 
		public void test7() {
			String username="Jon";
			String password="454";
			Double money=20.0;
			try {
				Double amount=-15.0;
				boolean deposit=false;
				
				testDA.open();
				testDA.createUser(username, password, money);
				testDA.close();
				
				sut.open();
				boolean emaitza=sut.gauzatuEragiketa(username, amount, deposit);
				

				assertFalse(emaitza);
			} catch(Exception e) {
				fail();
			} finally {
			sut.close();
			
			testDA.open();
			testDA.removeUser(username);
			testDA.close();
			}
		}
}
