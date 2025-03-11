import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { AppRoutingUrl } from 'src/app/app-management/app-routing.url';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
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
export class SignUpComponent implements OnInit {
  userName: string = "";
  passWord: string = "";
  confirmPassWord: string = "";
  email: string = "";
  phoneNumber: string = "";
  fullname: string = "";
  loading: boolean = false;


  constructor(private messageService: MessageService, private router: Router, public httpClient: HttpClient) { }
  ngOnInit(): void {
    // throw new Error('Method not implemented.');
  }

  async signup() {
    if (!this.isPasswordMatch()) {
      this.messageService.add({ severity: "error", summary: "Mật khẩu xác nhận không khớp!" });
      return;
    }
    // console.log(this.rememberMe)
    this.loading = true;

    try {
      // Gửi yêu cầu đăng ký tài khoản
      const signUpResponse = await this.httpClient.post<any>(
        environment.backendApiUrl + '/api/v1/project/auth/signup',
        {
          userName: this.userName,
          passWord: this.passWord,
          email: this.email,
          phoneNumber: this.phoneNumber,
          fullname: this.fullname
        }).toPromise();
        console.log('API Response:', signUpResponse);
        
      if (signUpResponse.resultCode == "0") {
        console.log("signUpResponse.resultCode "+ signUpResponse.resultCode);
        this.messageService.add({ severity: "success", summary: "Đăng ký tài khoản thành công" });

        setTimeout(() => {
          console.log("Chuyển hướng...");
          this.router.navigate(['/auth/kich-hoat-tai-khoan']).then(success => {
            if (success) {
              console.log("Chuyển hướng thành công!");
            } else {
              console.log("Chuyển hướng thất bại!");
            }
          });
          
        }, 2000);
        
      } else {
        console.log("signUpResponse.resultCode "+ signUpResponse.resultCode);
        this.messageService.add({ severity: "error", summary: signUpResponse.message });
      }
    } catch (error) {
      this.messageService.add({ severity: "error", summary: "Có lỗi xảy ra, vui lòng thử lại!" });
      this.router.navigate([AppRoutingUrl.common.error]);
    } finally {
      this.loading = false;
    }

    this.loading = false;
  }

  isPasswordMatch(): boolean {
    return this.passWord === this.confirmPassWord;
  }

}
