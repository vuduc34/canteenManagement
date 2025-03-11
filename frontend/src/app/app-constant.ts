import { AuthService } from './app-management/service/auth.service';
import { HttpHeaders } from "@angular/common/http";
import { environment } from "src/environments/environment";

export const getServerApiUrl = (): string => {
    return environment.backendApiUrl;
};
// export const header =  (): HttpHeaders => {
//     return AuthService
// }
export const storageKey: any = {
    USER: 'user',
    TOKEN: 'token',
    LANGUAGES: 'languages',
    AUTHORIZATION: 'Authorization',
    USER_INFO: 'userInfo',
    REFERER: 'redirect',
    USER_NAME: 'username',
    FULL_NAME: 'fullname',
    ACCOUNT_ID: 'accountid',
    CART_ID: 'cartid',
    EMAIL: 'email',
    PHONE_NUMBER: 'phonenumber'
};
export const name: any = {
    App: 'APPLICATION',
    Group: 'GROUP',
    User: 'USER'
}

export const authAPI: any = {
    loginUrl: '/auth/login',
    logoutUrl: '/auth/logout'
}

