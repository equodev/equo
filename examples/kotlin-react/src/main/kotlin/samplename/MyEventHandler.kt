package samplename

import com.equo.comm.api.IEquoEventHandler
import com.equo.comm.api.actions.IActionHandler
import com.equo.comm.api.annotations.EventName
import org.osgi.service.component.annotations.Component
import org.osgi.service.component.annotations.Reference

@Component
class MyEventHandler : IActionHandler {

    @Reference
    private lateinit var equoEventHandler: IEquoEventHandler

    @EventName("MyEventHandler")
    fun myFirstEvent(payload: String): String {
        println("First event: $payload");
        equoEventHandler.send("MyKotlinEvent", "This is your first message received from Kotlin");
        return payload
    }
}