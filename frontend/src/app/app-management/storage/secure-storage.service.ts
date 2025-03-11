import {Injectable} from '@angular/core';
import {Table} from "primeng/table/table";
import {StorageService} from "./storage.service";

@Injectable({
    providedIn: 'root'
})
export class SecureStorageService {

    constructor(private storageService: StorageService) {
    }

    setItem(key: string, value: any) {
        this.storageService.secureStorage.setItem(key, value);
    }

    getItem(key: string) {
        return this.storageService.secureStorage.getItem(key);
    }

    removeItem(key: string) {
        return this.storageService.secureStorage.removeItem(key);
    }
}
