import { AuthService } from './../../../service/auth.service';
import { getServerApiUrl,storageKey } from './../../../../app-constant';
import { Component } from '@angular/core';
import { LayoutService } from 'src/app/layout/service/app.layout.service';
import { HttpClient } from '@angular/common/http';
import { SecureStorageService } from 'src/app/app-management/storage/secure-storage.service';
import { Router } from '@angular/router';
import { MessageService} from 'primeng/api';
import { environment } from 'src/environments/environment';
@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styles: [`
        :host ::ng-deep .p-password input {
            width: 100%;
            padding:1rem;
        }

        :host ::ng-deep .pi-eye{
            transform:scale(1.6);
            margin-right: 1rem;
            color: var(--primary-color) !important;
        }

        :host ::ng-deep .pi-eye-slash{
            transform:scale(1.6);
            margin-right: 1rem;
            color: var(--primary-color) !important;
        }
    `]
})
export class LoginComponent {

    valCheck: string[] = ['remember'];
    loading: boolean = false;
    password: any;
    username: any;

    constructor(public layoutService: LayoutService,private messageService: MessageService,private authService:AuthService ,private router: Router,public httpClient: HttpClient, public secureStorageService : SecureStorageService ) { }
    // async login() {
    //     this.loading = true;
    //     await this.httpClient
    //         .post<any>(environment.backendApiUrl+'/api/v1/project/auth/signin', {
    //             userName: this.username,
    //             passWord: this.password,
    //         })
    //         .toPromise()
    //         .then(
    //             (data) => {
    //                 if (data.resultCode == '0') {
    //                     this.authService.setToken(data.data.token);
    //                     this.authService.setUsername(data.data.username);
    //                     this.authService.setFullname(data.data.fullname);
    //                     this.authService.setAccountid(data.data.account_id);
    //                     this.authService.setCartid(data.data.cart_id);
    //                     this.authService.setEmail(data.data.email);
    //                     this.authService.setPhonenumber(data.data.phoneNumber);
    //                     // console.log(data.data.cart_id);

    //                     this.authService.setRole(data.data.role); // Lưu role vào storage
    //                     this.messageService.add({ severity: "success", summary: "Đăng nhập thành công" });
                        
    //                     // Điều hướng dựa trên role
    //                     this.router.navigate([this.authService.getRedirectUrl()]);

                        
    //                 } else
    //                     this.messageService.add({
    //                         severity: 'error',
    //                         summary: "Tài khoản không tồn tại ",
    //                     });
    //                     // console.log(data)
    //             },
    //             (error) => {
    //                 this.messageService.add({
    //                     severity: 'error',
    //                     summary: 'Tên đăng nhập hoặc mật khẩu không đúng',
    //                 });
    //                 console.log(error)
    //             }
    //         );
    //     this.loading = false;
        

    // }

    async login() {
        this.loading = true;
        await this.httpClient
            .post<any>(environment.backendApiUrl+'/api/v1/project/auth/signin', {
                userName: this.username,
                passWord: this.password,
            })
            .toPromise()
            .then(
                (data) => {
                    if (data.resultCode == '0') {
                        this.authService.setToken(data.data.token);
                        this.authService.setUsername(data.data.username);
                        this.authService.setFullname(data.data.fullname);
                        this.authService.setAccountid(data.data.account_id);
                        this.authService.setCartid(data.data.cart_id);
                        this.authService.setEmail(data.data.email);
                        this.authService.setPhonenumber(data.data.phoneNumber);
                        // console.log(data.data.cart_id);

                        this.authService.setRole(data.data.role); // Lưu role vào storage
                        this.messageService.add({ severity: "success", summary: "Đăng nhập thành công" });
                        
                        // Điều hướng dựa trên role
                        this.router.navigate([this.authService.getRedirectUrl()]);

                        
                    } else if(data.resultCode == '-1'){
                        this.messageService.add({
                            severity: 'error',
                            summary: "Sai mật khẩu. Vui lòng thử lại!",
                        });
                    }else
                        this.messageService.add({
                            severity: 'error',
                            summary: "Lỗi. Vui lòng thử lại!",
                        });
                        // console.log(data)
                },
                (error) => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Tên đăng nhập hoặc mật khẩu không đúng',
                    });
                    console.log(error)
                }
            );
        this.loading = false;
        

    }
}
