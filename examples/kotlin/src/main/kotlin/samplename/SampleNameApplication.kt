package samplename

import com.equo.application.api.IEquoApplication
import com.equo.application.model.EquoApplicationBuilder
import com.equo.dev.EquoApp
import org.osgi.service.component.annotations.Component

fun main(args: Array<String>) {
    EquoApp.launch()
}

@Component
class SampleNameApplication : IEquoApplication {
    override fun buildApp(appBuilder: EquoApplicationBuilder): EquoApplicationBuilder {
        return appBuilder
            .withUI("index.html")
            .start()
    }
}
