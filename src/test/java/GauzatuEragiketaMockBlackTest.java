import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import dataAccess.DataAccess;
import domain.Driver;
import domain.User;

public class GauzatuEragiketaMockBlackTest {
	
static DataAccess sut;
	
	protected MockedStatic<Persistence> persistenceMock;

	@Mock
	protected  EntityManagerFactory entityManagerFactory;
	@Mock
	protected  EntityManager db;
	@Mock
    protected  EntityTransaction  et;
	

	@Before
    public  void init() {
        MockitoAnnotations.openMocks(this);
        persistenceMock = Mockito.mockStatic(Persistence.class);
		persistenceMock.when(() -> Persistence.createEntityManagerFactory(Mockito.any()))
        .thenReturn(entityManagerFactory);
        
        Mockito.doReturn(db).when(entityManagerFactory).createEntityManager();
		Mockito.doReturn(et).when(db).getTransaction();
	    sut=new DataAccess(db);
    }
	@After
    public  void tearDown() {
		persistenceMock.close();
    }
	
	
	User user;
	
	@Test
	// u!=null, amount!=null, username!=null, deposit!=null, amount>=0, deposit==true 
	public void test1() {
		String username="Jon";
		String password="454";
		Double money=6.7;
		try {
			Double amount=15.0;
			boolean deposit=true;
			
			
			user= new User(username, password, null);
			user.setMoney(money);
			
			Mockito.when(db.find(User.class, username)).thenReturn(user);
			
			sut.open();
			boolean emaitza=sut.gauzatuEragiketa(username, amount, deposit);
			

			assertTrue(emaitza);
		} catch(Exception e) {
			fail();
		} finally {
		sut.close();
		
		
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
			
			user= new User(username, password, null);
			user.setMoney(money);
			
			Mockito.when(db.find(User.class, username)).thenReturn(user);
			
			sut.open();
			boolean emaitza=sut.gauzatuEragiketa(username, amount, deposit);
			

			assertTrue(emaitza);
		} catch(Exception e) {
			fail();
		} finally {
		sut.close();
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
			
			user= new User(username, password, null);
			user.setMoney(money);
			
			Mockito.when(db.find(User.class, username)).thenReturn(user);
			
			sut.open();
			boolean emaitza=sut.gauzatuEragiketa(username, amount, deposit);
			

			assertTrue(emaitza);
		} catch(Exception e) {
			fail();
		} finally {
		sut.close();
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
			
			user= new User(username, password, null);
			user.setMoney(money);
			
			when(db.find(User.class, null)).thenThrow(new IllegalArgumentException("Cannot find user with null username"));
			
			sut.open();
			boolean emaitza=sut.gauzatuEragiketa(null, amount, deposit);

			assertFalse(emaitza);
		} catch(Exception e) {
			fail();
		} finally {
		sut.close();		
		}
	}		
	
	
	
	@Test
	//amount<0 
	public void test6() {
		String username="Jon";
		String password="454";
		Double money=20.0;
		try {
			Double amount=-15.0;
			boolean deposit=false;
			
			user= new User(username, password, null);
			user.setMoney(money);
			
			Mockito.when(db.find(User.class, username)).thenReturn(user);
			
			sut.open();
			boolean emaitza=sut.gauzatuEragiketa(username, amount, deposit);
			

			assertFalse(emaitza);
		} catch(Exception e) {
			fail();
		} finally {
		sut.close();
		}
	
}

}
