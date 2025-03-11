import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html'
})
export class ResetPasswordComponent implements OnInit {

  constructor(private httpClient: HttpClient,private router: Router,private messageService: MessageService) { }

  ngOnInit(): void {
  }
  code:any;
  newPassword:any;
  confirmPassword: any;
  loading:boolean = false;
  async resetPassword() {
    this.loading = true;
    if (this.newPassword !== this.confirmPassword) {
      this.messageService.add({severity:'warning',summary:'Thông báo', detail:'Mật khẩu xác nhận không khớp!'});

      // alert("Mật khẩu xác nhận không khớp!");
      return;
  }
    await this.httpClient.post<any>(environment.backendApiUrl+"/api/v1/project/auth/resetpw",{"token":this.code,"password":this.newPassword}).toPromise().then(
      data => {
        if(data.resultCode == "0") {
          this.messageService.add({severity:'success',summary:'Thành công', detail:'Đã đặt lại mật khẩu thành công. Đăng nhập ngay!'});
          // this.messageService.add({severity:"success", summary:"Reset password successfully"});
          setTimeout( () => {
            this.router.navigate(['/auth/dang-nhap'])
          }, 2000 );

        }
        else 
        this.messageService.add({severity:"error", summary:data.message});

      },
      error => {
        console.log(error)
        this.messageService.add({severity:"error", summary:error.error.title});
      }
    )
    this.loading = false;

  }


}
