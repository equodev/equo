import { EquoFramework } from '../../../com.make.equo.node.packages/packages/equo-framework/lib/index';
import { EquoLoggingService } from '../../../com.make.equo.node.packages/packages/equo-logging/lib/index';
import { EquoAnalyticsService } from '../../../com.make.equo.node.packages/packages/equo-analytics/lib/index';
import { EquoMonaco } from '../../../com.make.equo.node.packages/packages/equo-monaco-editor/lib/index';
import { EquoWebSocketService, EquoWebSocket } from '../../../com.make.equo.node.packages/packages/equo-websocket/lib/index';

var websocket: EquoWebSocket = EquoWebSocketService.get();

websocket.on('_getDom', () => {
    websocket.send('_doGetDom', {
        dom: document.documentElement.innerHTML
    });
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