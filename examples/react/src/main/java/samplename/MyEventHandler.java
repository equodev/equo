package samplename;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.equo.comm.api.IEquoEventHandler;
import com.equo.comm.api.actions.IActionHandler;
import com.equo.comm.api.annotations.EventName;

@Component
public class MyEventHandler implements IActionHandler {

	@Reference
	private IEquoEventHandler equoEventHandler;

	@EventName("MyEventHandler")
	public String myFirstEvent(String payload) {
		System.out.println("First event: " + payload);
		equoEventHandler.send("MyJavaEvent", "This is your first message received from Java");
		return payload;
	}
}
