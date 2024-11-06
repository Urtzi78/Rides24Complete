package businessLogic;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import configuration.ConfigXML;
import dataAccess.DataAccess;

public class FacadeFactory {

	public static BLFacade createBLFacade(DataAccess da) {
		ConfigXML c = ConfigXML.getInstance();
		boolean isLocal = c.isBusinessLogicLocal();

		if (isLocal) {
			return new BLFacadeImplementation(da);
		} else {
			try {
				String serviceName = "http://" + c.getBusinessLogicNode() + ":" + c.getBusinessLogicPort() + "/ws/"
						+ c.getBusinessLogicName() + "?wsdl";
				URL url = new URL(serviceName);
				// 1st argument refers to wsdl document above
				// 2nd argument is service name, refer to wsdl document above
				QName qname = new QName("http://businessLogic/", "BLFacadeImplementationService");
				Service service = Service.create(url, qname);
				return service.getPort(BLFacade.class);
			} catch (MalformedURLException e) {
				System.out.println("Error creating remote BLFacade: " + e.getMessage());
				e.printStackTrace();
				return null;
			}
		}
	}

}