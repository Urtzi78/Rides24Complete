package testOperations;

import org.mockito.Mockito;

import businessLogic.BLFacade;
import gui.MainGUI;

public class GuiTest {

	static BLFacade appFacadeInterface = Mockito.mock(BLFacade.class);

	
	public static void main(String[] args) {
		Mockito.doReturn(true).when(appFacadeInterface).isRegistered("a","a");
		Mockito.doReturn("Traveler").when(appFacadeInterface).getMotaByUsername(Mockito.anyString());
		MainGUI a = new MainGUI();
		MainGUI.setBussinessLogic(appFacadeInterface);
		a.setVisible(true);
		
		//System.out.println(appFacadeInterface.isRegistered("a", "a"));

	}

}
