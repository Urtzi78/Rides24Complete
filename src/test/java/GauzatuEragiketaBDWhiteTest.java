import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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
			} finally {
				sut.close();
			}
		} 
		@Test
		// Username ez dago datu basean. False itzuli behar du.
		public void test2() {
			try {
				String username="Jon";
				Double amount=5.9;
			
				sut.open();
				boolean emaitza=sut.gauzatuEragiketa(username, amount, false);
				
				assertFalse(emaitza);
			} catch(Exception e) {
				fail();
			} finally {
			sut.close();
			}
		}
		
		@Test
		//user datu basean dago, deposit true
		public void test3() {
			String username="Martin";
			String password="454";
			Double money=6.7;
			try {
				Double amount=5.9;
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
		//user datu basean dago, deposit false, amount>currentMoney
		public void test4() {
			String username="Aimar";
			String password="454";
			Double money=6.7;
			try {
				Double amount=20.5;
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
		//user datu basean dago, deposit false, amount<=currentMoney
		public void test5() {
			String username="Asier";
			String password="454";
			Double money=6.7;
			try {
				Double amount=5.9;
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

}
