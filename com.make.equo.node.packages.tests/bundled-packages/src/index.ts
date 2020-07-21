// @ts-ignore
import { EquoFramework } from '@equo/framework';
// @ts-ignore
import { EquoLoggingService } from '@equo/logging';
// @ts-ignore
import { EquoAnalyticsService } from '@equo/analytics';
// @ts-ignore
import { EquoMonaco } from '@equo/equo-monaco-editor';
// @ts-ignore
import { EquoWebSocketService, EquoWebSocket } from '@equo/websocket';

var websocket: EquoWebSocket = EquoWebSocketService.get();

websocket.on('_getIsEditorCreated', () => {
    if (document.getElementsByClassName('monaco-editor').length > 0) {
        websocket.send('_doGetIsEditorCreated', {
            created: true
        });
    }
});

EquoMonaco.create(document.getElementById('container')!);

EquoLoggingService.logInfo('testInfo');
EquoLoggingService.logWarn('testWarn');
EquoLoggingService.logError('testError');

EquoAnalyticsService.registerEvent({
    key: 'testEvent',
    segmentation: {
        testKey: 'testValue'
    }
});

EquoFramework.openBrowser('');