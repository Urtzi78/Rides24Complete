import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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

public class GauzatuEragiketaMockWhiteTest {
	
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
	
	@Test
	//sut.GauzatuEragiketa. user ez dago datu basean
	public void test2() {
		try{
			String username="Dani";
			Double amount= 7.4;
			
			Mockito.when(db.find(User.class, username)).thenReturn(null);
			
			sut.open();
			boolean emaitza=sut.gauzatuEragiketa(username,amount, true);
			
			assertFalse(emaitza);
			
		} catch(Exception e) {
			fail();
		} finally {
			sut.close();
		}
	}
	
	public void test3() {
		try{
			String username="Urtzi";
			String password="123";
			Double money=5.9;
			Double amount=20.0;
			User user=new User(username, password, null);
			user.setMoney(money);
			
			Mockito.when(db.find(User.class, null)).thenReturn(user);
			
			sut.open();
			boolean emaitza=sut.gauzatuEragiketa(username, amount, true);
			
			assertTrue(emaitza);
			
		} catch(Exception e) {
			fail();
		} finally {
			sut.close();
		}
	}
	
	
	
	
	
	
	
	
}
