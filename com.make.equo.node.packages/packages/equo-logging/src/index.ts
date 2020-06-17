import { EquoWebSocket, EquoWebSocketService } from '@equo/websocket';

export interface LogPayload {
	message: string;
	type: string;
}

export namespace EquoLoggingService {
	var websocket: EquoWebSocket = EquoWebSocketService.get();

	export function logInfo(message: string): void {
		var payload: LogPayload = {
			message: message,
			type: 'info'
		};
		websocket.send('loggingEvent', payload);
	};

	export function logError(message: string): void {
		var payload: LogPayload = {
			message: message,
			type: 'error'
		};
		websocket.send('loggingEvent', payload);
	};

	export function logWarn(message: string): void {
		var payload: LogPayload = {
			message: message,
			type: 'warning'
		};
		websocket.send('loggingEvent', payload);
	};
}
