@JsModule("@equo/comm")
@JsNonModule
external class EquoComm {
    fun send(actionId: String, payload: String?)
    fun on(actionId: String, callback: (result: String) -> Unit)
}
