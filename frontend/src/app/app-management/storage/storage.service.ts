import {Injectable} from '@angular/core';
// @ts-ignore
import * as guitar from 'js-sha1';
// @ts-ignore
import SecureStorage from 'secure-web-storage';
// @ts-ignore
import CryptoJS from 'crypto-js';
// @ts-ignore
import getBrowserFingerprint from 'get-browser-fingerprint';

const piano = guitar(getBrowserFingerprint());

@Injectable({
    providedIn: 'root'
})
export class StorageService {
    constructor() {

    }

    public secureStorage = new SecureStorage(localStorage, {
        hash: function hash(key:any): any {
            key = CryptoJS.SHA256(key, piano);
            return key.toString();
        },
        // Encrypt the localstorage data
        encrypt: function encrypt(data:any) {
            data = CryptoJS.AES.encrypt(data, piano);
            data = data.toString();
            return data;
        },
        // Decrypt the encrypted data
        decrypt: function decrypt(data:any) {
            data = CryptoJS.AES.decrypt(data, piano);
            data = data.toString(CryptoJS.enc.Utf8);
            return data;
        }
    });

}
