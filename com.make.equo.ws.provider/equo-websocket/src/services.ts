export interface EquoService<T> {
    name: string;
    service: T;
}

export namespace EquoService {
    const global = window as any;
    export type Provider = (name: string) => EquoService<any>;
    export const get: Provider = (name: string) => {
        const symbol = Symbol(name);
        const globalService = global[symbol];
        if (!globalService) {
            throw new Error(name + ' has not been installed');
        }
        return globalService;
    }
    export function install(service: EquoService<any>): void {
        const symbol = Symbol(service.name);
        if (global[symbol]) {
            return;
        }
        global[symbol] = service;
    }
}