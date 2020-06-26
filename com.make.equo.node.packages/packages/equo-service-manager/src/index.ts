export interface EquoService<T> {
    id: string;
    service: T;
}

export namespace EquoService {
    const global = window as any;

    export function get<T>(id: string, create?: () => EquoService<T>): T {
        const globalService: T = global[id] as T;
        if (!globalService && create) {
            const newService: EquoService<T> = create();
            if (!newService) {
                throw new Error(id + ' couldn\'t be created');
            }
            install<T>(newService);
            return newService.service;
        } else if (globalService) {
            return globalService;
        } else {
            throw new Error(id + ' has not been installed');
        }
    }

    export function install<T>(service: EquoService<T>): void {
        if (global[service.id]) {
            return;
        }
        global[service.id] = service.service;
    }
}