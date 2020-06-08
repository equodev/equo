export interface EquoService {
    symbol: symbol;
}

export namespace EquoService {
    const global = window as any;
    export type Provider = (symbol: symbol) => EquoService;
    export const get: Provider = (symbol: symbol) => {
        const globalService: EquoService = global[symbol];
        if (!globalService) {
            throw new Error(name + ' has not been installed');
        }
        return globalService;
    }
    export function install(service: EquoService): void {
        if (global[service.symbol]) {
            return;
        }
        global[service.symbol] = service;
    }
}