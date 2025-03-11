import { HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Table } from "primeng/table/table";
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { getServerApiUrl, storageKey, authAPI } from 'src/app/app-constant';
import { SecureStorageService } from '../storage/secure-storage.service';
import { JwtHelperService } from '@auth0/angular-jwt';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    public loginUrl = getServerApiUrl() + authAPI.loginUrl;
    public logoutUrl = getServerApiUrl() + authAPI.logoutUrl;

    constructor(
        protected httpClient: HttpClient,
        protected router: Router,
        private secureStorageService: SecureStorageService,
        public jwtHelper: JwtHelperService
    ) {

    }

    public isAuthenticated(): boolean {
        // return true;
        try {
            let token = this.secureStorageService.getItem(storageKey.TOKEN)
            if (token === undefined) {
                token = '';
                return false;
            }
            return !this.jwtHelper.isTokenExpired(token);

        } catch (e) {
            return false;
        }
    }
    public isAuthorized(): boolean {
        try {
            let token = this.secureStorageService.getItem(storageKey.TOKEN)
            let username = this.jwtHelper.decodeToken(token).sub;
            if (username.includes('admin')) {
                return true;
            }
            // console.log(username);
            return false;

        } catch (ex) {
            return false;
        }
    }
    getToken(): string {
        return this.secureStorageService.getItem(storageKey.TOKEN);
    }



    setToken(token: string) {
        this.secureStorageService.setItem(storageKey.TOKEN, token);
    }

    getRedirectUrl(): string {
        const role = this.getRole(); // Lấy role từ SecureStorage

        if (role === 'ROLE_ADMIN' || role === 'ROLE_STAFF' ) {
            return '/pages/dashboard'; // Điều hướng đến trang admin
        } else {
            return ''; // Điều hướng đến trang home
        }
        // return this.secureStorageService.getItem(storageKey.REFERER);
    }

    setRedirectUrl(url: string) {
        // url = url === "/" ? "" : url;
        this.secureStorageService.setItem(storageKey.REFERER, url);
    }

    setUsername(username: string) {
        // console.log("SET USERNAME TRONG AUTH.SERVICE.TS "+username); 
        this.secureStorageService.setItem(storageKey.USER_NAME, username)
        // console.log("SET USERNAME TRONG AUTH.SERVICE.TS 2 "+ storageKey.USER_NAME); 

    }
    getUsername(): string {
        // console.log("Trong getUsername "+this.secureStorageService.getItem(storageKey.USER_NAME)); 
        return this.secureStorageService.getItem(storageKey.USER_NAME)
    }

    setFullname(fullname: string) {
        // console.log("SET USERNAME TRONG AUTH.SERVICE.TS "+fullname); 
        this.secureStorageService.setItem(storageKey.FULL_NAME, fullname)
        // console.log("SET USERNAME TRONG AUTH.SERVICE.TS 2 "+ storageKey.FULL_NAME); 

    }
    getFullname(): string {
        // console.log("Trong getUsername "+this.secureStorageService.getItem(storageKey.FULL_NAME)); 
        return this.secureStorageService.getItem(storageKey.FULL_NAME)
    }

    setEmail(email: string) {
        // console.log("SET USERNAME TRONG AUTH.SERVICE.TS "+fullname); 
        this.secureStorageService.setItem(storageKey.EMAIL, email)
        // console.log("SET USERNAME TRONG AUTH.SERVICE.TS 2 "+ storageKey.FULL_NAME); 

    }
    getEmail(): string {
        // console.log("Trong getUsername "+this.secureStorageService.getItem(storageKey.FULL_NAME)); 
        return this.secureStorageService.getItem(storageKey.EMAIL)
    }

    setPhonenumber(phonenumber: string) {
        // console.log("SET USERNAME TRONG AUTH.SERVICE.TS "+fullname); 
        this.secureStorageService.setItem(storageKey.PHONE_NUMBER, phonenumber)
        // console.log("SET USERNAME TRONG AUTH.SERVICE.TS 2 "+ storageKey.FULL_NAME); 

    }
    getPhonenumber(): string {
        console.log("storageKey "+storageKey.PHONE_NUMBER);
        console.log("storageKey "+storageKey.EMAIL);
        console.log("storageKey "+storageKey.FULL_NAME);
        // console.log("Trong getUsername "+this.secureStorageService.getItem(storageKey.FULL_NAME)); 
        return this.secureStorageService.getItem(storageKey.PHONE_NUMBER)
    }


    setAccountid(accountid: string) {
        // console.log("ACCOUNT_ID "+ accountid); 
        this.secureStorageService.setItem(storageKey.ACCOUNT_ID, accountid)
        // console.log("ACCOUNT_ID "+ storageKey.accountid); 

    }
    getAccountid(): string {
        // console.log("ACCOUNT_ID "+this.secureStorageService.getItem(storageKey.accountid)); 
        return this.secureStorageService.getItem(storageKey.ACCOUNT_ID)
    }

    setCartid(cartid: string) {
        // console.log("ACCOUNT_ID "+ accountid); 
        this.secureStorageService.setItem(storageKey.CART_ID, cartid)
        // console.log("ACCOUNT_ID "+ storageKey.accountid); 

    }
    getCartid(): string {
        // console.log("CART_ID "+this.secureStorageService.getItem(storageKey.CART_ID)); 
        return this.secureStorageService.getItem(storageKey.CART_ID)
    }

    setRole(role: string) {
        this.secureStorageService.setItem(storageKey.ROLE, role)
    }
    getRole(): string {
        return this.secureStorageService.getItem(storageKey.ROLE)
    }

    logout(): void {
        this.secureStorageService.removeItem(storageKey.TOKEN);
        this.secureStorageService.removeItem(storageKey.USER_NAME);
        this.secureStorageService.removeItem(storageKey.ROLE);
        window.location.reload(); // Load lại trang để cập nhật giao diện
        // this.router.navigate(['/auth/login']); // Chuyển hướng về trang đăng nhập
    }
    

}
