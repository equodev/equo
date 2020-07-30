// @ts-ignore
import { EquoFramework } from '@equo/framework';
// @ts-ignore
import { EquoLoggingService } from '@equo/logging';
// @ts-ignore
import { EquoAnalyticsService } from '@equo/analytics';

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