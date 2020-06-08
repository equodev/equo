export interface EquoService<T> {
    id: string;
    service: T;
}

export namespace EquoService {
    const global = window as any;

    export function get<T>(id: string, create: () => EquoService<T>): T {
        const globalService: T = global[id] as T;
        if (!globalService) {
            const newService: EquoService<T> = create();
            if (!newService) {
                throw new Error(id + ' has not been installed');
            }
            install<T>(newService);
        }
        return globalService;
    }

    export function install<T>(service: EquoService<T>): void {
        if (global[service.id]) {
            return;
        }
        global[service.id] = service;
    }
}