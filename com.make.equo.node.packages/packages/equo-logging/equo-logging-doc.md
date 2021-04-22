## Objects

<dl>
<dt><a href="#EquoLoggingService">EquoLoggingService</a> : <code>object</code></dt>
<dd><p>Equo Framework Javascript API.
Configure logs levels using <strong><em>equo-logging</em></strong></p>
</dd>
</dl>

## Functions

<dl>
<dt><a href="#logInfo">logInfo(message)</a> ⇒ <code>void</code></dt>
<dd><p>Handler for the <strong><em>info</em></strong> level.</p>
</dd>
<dt><a href="#logError">logError(message)</a> ⇒ <code>void</code></dt>
<dd><p>Handler for the <strong><em>error</em></strong> level.</p>
</dd>
<dt><a href="#logWarn">logWarn(message)</a> ⇒ <code>void</code></dt>
<dd><p>Handler for the <strong><em>warn</em></strong> level.</p>
</dd>
<dt><a href="#logDebug">logDebug(message)</a> ⇒ <code>void</code></dt>
<dd><p>Handler for the <strong><em>debug</em></strong> level.</p>
</dd>
<dt><a href="#logTrace">logTrace(message)</a> ⇒ <code>void</code></dt>
<dd><p>Handler for the <strong><em>trace</em></strong> level.</p>
</dd>
<dt><a href="#getJsLoggerLevel">getJsLoggerLevel(callback)</a> ⇒ <code>void</code></dt>
<dd><p>Get local log level.</p>
</dd>
<dt><a href="#setJsLoggerLevel">setJsLoggerLevel(level)</a> ⇒ <code>void</code></dt>
<dd><p>Set local log level.</p>
</dd>
<dt><a href="#getGlobalLoggerLevel">getGlobalLoggerLevel(callback)</a> ⇒ <code>void</code></dt>
<dd><p>Get global log level.</p>
</dd>
<dt><a href="#setGlobalLoggerLevel">setGlobalLoggerLevel(level)</a> ⇒ <code>void</code></dt>
<dd><p>Set global log level.</p>
</dd>
</dl>

<a name="EquoLoggingService"></a>

## EquoLoggingService : <code>object</code>
Equo Framework Javascript API.
Configure logs levels using ***equo-logging***

**Kind**: global namespace  

* [EquoLoggingService](#EquoLoggingService) : <code>object</code>
    * [.LOG_LEVEL_OFF](#EquoLoggingService.LOG_LEVEL_OFF) : <code>string</code>
    * [.LOG_LEVEL_ERROR](#EquoLoggingService.LOG_LEVEL_ERROR) : <code>string</code>
    * [.LOG_LEVEL_WARN](#EquoLoggingService.LOG_LEVEL_WARN) : <code>string</code>
    * [.LOG_LEVEL_INFO](#EquoLoggingService.LOG_LEVEL_INFO) : <code>string</code>
    * [.LOG_LEVEL_DEBUG](#EquoLoggingService.LOG_LEVEL_DEBUG) : <code>string</code>
    * [.LOG_LEVEL_TRACE](#EquoLoggingService.LOG_LEVEL_TRACE) : <code>string</code>
    * [.LOG_LEVEL_ALL](#EquoLoggingService.LOG_LEVEL_ALL) : <code>string</code>
    * [.LOG_LEVEL_NOT_CONFIGURED](#EquoLoggingService.LOG_LEVEL_NOT_CONFIGURED) : <code>string</code>

<a name="EquoLoggingService.LOG_LEVEL_OFF"></a>

### EquoLoggingService.LOG\_LEVEL\_OFF : <code>string</code>
**Kind**: static constant of [<code>EquoLoggingService</code>](#EquoLoggingService)  
**Default**: <code>&quot;OFF&quot;</code>  
<a name="EquoLoggingService.LOG_LEVEL_ERROR"></a>

### EquoLoggingService.LOG\_LEVEL\_ERROR : <code>string</code>
**Kind**: static constant of [<code>EquoLoggingService</code>](#EquoLoggingService)  
**Default**: <code>&quot;ERROR&quot;</code>  
<a name="EquoLoggingService.LOG_LEVEL_WARN"></a>

### EquoLoggingService.LOG\_LEVEL\_WARN : <code>string</code>
**Kind**: static constant of [<code>EquoLoggingService</code>](#EquoLoggingService)  
**Default**: <code>&quot;WARN&quot;</code>  
<a name="EquoLoggingService.LOG_LEVEL_INFO"></a>

### EquoLoggingService.LOG\_LEVEL\_INFO : <code>string</code>
**Kind**: static constant of [<code>EquoLoggingService</code>](#EquoLoggingService)  
**Default**: <code>&quot;INFO&quot;</code>  
<a name="EquoLoggingService.LOG_LEVEL_DEBUG"></a>

### EquoLoggingService.LOG\_LEVEL\_DEBUG : <code>string</code>
**Kind**: static constant of [<code>EquoLoggingService</code>](#EquoLoggingService)  
**Default**: <code>&quot;DEBUG&quot;</code>  
<a name="EquoLoggingService.LOG_LEVEL_TRACE"></a>

### EquoLoggingService.LOG\_LEVEL\_TRACE : <code>string</code>
**Kind**: static constant of [<code>EquoLoggingService</code>](#EquoLoggingService)  
**Default**: <code>&quot;TRACE&quot;</code>  
<a name="EquoLoggingService.LOG_LEVEL_ALL"></a>

### EquoLoggingService.LOG\_LEVEL\_ALL : <code>string</code>
**Kind**: static constant of [<code>EquoLoggingService</code>](#EquoLoggingService)  
**Default**: <code>&quot;ALL&quot;</code>  
<a name="EquoLoggingService.LOG_LEVEL_NOT_CONFIGURED"></a>

### EquoLoggingService.LOG\_LEVEL\_NOT\_CONFIGURED : <code>string</code>
Level use to disable special logger level for javascript and use the global level

**Kind**: static constant of [<code>EquoLoggingService</code>](#EquoLoggingService)  
**Default**: <code>&quot;NOT CONFIGURED&quot;</code>  
<a name="logInfo"></a>

## logInfo(message) ⇒ <code>void</code>
Handler for the ***info*** level.

**Kind**: global function  

| Param | Type |
| --- | --- |
| message | <code>string</code> | 

<a name="logError"></a>

## logError(message) ⇒ <code>void</code>
Handler for the ***error*** level.

**Kind**: global function  

| Param | Type |
| --- | --- |
| message | <code>string</code> | 

<a name="logWarn"></a>

## logWarn(message) ⇒ <code>void</code>
Handler for the ***warn*** level.

**Kind**: global function  

| Param | Type |
| --- | --- |
| message | <code>string</code> | 

<a name="logDebug"></a>

## logDebug(message) ⇒ <code>void</code>
Handler for the ***debug*** level.

**Kind**: global function  

| Param | Type |
| --- | --- |
| message | <code>string</code> | 

<a name="logTrace"></a>

## logTrace(message) ⇒ <code>void</code>
Handler for the ***trace*** level.

**Kind**: global function  

| Param | Type |
| --- | --- |
| message | <code>string</code> | 

<a name="getJsLoggerLevel"></a>

## getJsLoggerLevel(callback) ⇒ <code>void</code>
Get local log level.

**Kind**: global function  

| Param | Type |
| --- | --- |
| callback | <code>function</code> | 

<a name="setJsLoggerLevel"></a>

## setJsLoggerLevel(level) ⇒ <code>void</code>
Set local log level.

**Kind**: global function  

| Param | Type | Description |
| --- | --- | --- |
| level | <code>string</code> | Use constant log level. |

<a name="getGlobalLoggerLevel"></a>

## getGlobalLoggerLevel(callback) ⇒ <code>void</code>
Get global log level.

**Kind**: global function  

| Param | Type |
| --- | --- |
| callback | <code>function</code> | 

<a name="setGlobalLoggerLevel"></a>

## setGlobalLoggerLevel(level) ⇒ <code>void</code>
Set global log level.

**Kind**: global function  

| Param | Type | Description |
| --- | --- | --- |
| level | <code>string</code> | Use constant log level. |

