## Classes

<dl>
<dt><a href="#EquoWebSocket">EquoWebSocket</a> ⇐ <code>WebSocket</code></dt>
<dd></dd>
</dl>

## Objects

<dl>
<dt><a href="#EquoWebSocketService">EquoWebSocketService</a> : <code>object</code></dt>
<dd><p>Websocket API for usage within the Equo Framework.</p>
<p>This document specifies the usage methods for equo-websocket.
See <a href="how-to-include-equo-components.md">more</a> about how to include Equo component in your projects.</p>
</dd>
</dl>

## Functions

<dl>
<dt><a href="#create">create()</a> ⇒ <code><a href="#EquoWebSocket">EquoService.&lt;EquoWebSocket&gt;</a></code></dt>
<dd><p>Create a EquoWebSocketService instance.</p>
</dd>
<dt><a href="#get">get()</a> ⇒ <code><a href="#EquoWebSocket">EquoWebSocket</a></code></dt>
<dd><p>Obtain existing instance service for EquoWebSocket or create new instance if not exists.</p>
</dd>
</dl>

<a name="EquoWebSocket"></a>

## EquoWebSocket ⇐ <code>WebSocket</code>
**Kind**: global class  
**Extends**: <code>WebSocket</code>  

* [EquoWebSocket](#EquoWebSocket) ⇐ <code>WebSocket</code>
    * [.send(actionId, [payload])](#EquoWebSocket+send) ⇒ <code>void</code>
    * [.on(userEvent, callback)](#EquoWebSocket+on) ⇒ <code>void</code>

<a name="EquoWebSocket+send"></a>

### equoWebSocket.send(actionId, [payload]) ⇒ <code>void</code>
Send action to execute in Framework.

**Kind**: instance method of [<code>EquoWebSocket</code>](#EquoWebSocket)  

| Param | Type | Description |
| --- | --- | --- |
| actionId | <code>string</code> |  |
| [payload] | <code>JSON</code> | Optional |

<a name="EquoWebSocket+on"></a>

### equoWebSocket.on(userEvent, callback) ⇒ <code>void</code>
Manage user events.

**Kind**: instance method of [<code>EquoWebSocket</code>](#EquoWebSocket)  

| Param | Type |
| --- | --- |
| userEvent | <code>string</code> | 
| callback | <code>function</code> | 

<a name="EquoWebSocketService"></a>

## EquoWebSocketService : <code>object</code>
Websocket API for usage within the Equo Framework.

This document specifies the usage methods for equo-websocket.
See [more](how-to-include-equo-components.md) about how to include Equo component in your projects.

**Kind**: global namespace  
<a name="create"></a>

## create() ⇒ [<code>EquoService.&lt;EquoWebSocket&gt;</code>](#EquoWebSocket)
Create a EquoWebSocketService instance.

**Kind**: global function  
<a name="get"></a>

## get() ⇒ [<code>EquoWebSocket</code>](#EquoWebSocket)
Obtain existing instance service for EquoWebSocket or create new instance if not exists.

**Kind**: global function  
