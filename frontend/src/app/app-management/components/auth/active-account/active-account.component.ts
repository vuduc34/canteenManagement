import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-active-account',
  templateUrl: './active-account.component.html',
  styles: [
      `
          :host ::ng-deep .p-password input {
              width: 100%;
              padding: 1rem;
          }

          :host ::ng-deep .pi-eye {
              transform: scale(1.6);
              margin-right: 1rem;
              color: var(--primary-color) !important;
          }

          :host ::ng-deep .pi-eye-slash {
              transform: scale(1.6);
              margin-right: 1rem;
              color: var(--primary-color) !important;
          }
          * {
              box-sizing: border-box;
          }
      `,
  ],
})
export class ActiveAccountComponent implements OnInit {
  otpCode: string = '';
  loading: boolean = false;
  isSubmitted: boolean = false;

  constructor(
    private httpClient: HttpClient,
    private router: Router,
    private messageService: MessageService) { }
  ngOnInit(){
  }
  async activeAccount () {
    this.isSubmitted = true;
    if (!this.otpCode) {
      this.messageService.add({ severity: 'error', summary: 'Vui lòng nhập mã OTP!' });
      return;
    }

    this.loading = true;
    
    await this.httpClient.post<any>(environment.backendApiUrl+"/api/v1/project/auth/active?code="+this.otpCode,null).toPromise().then(
      data => {
        if(data.resultCode == "0") {
          this.messageService.add({severity:"success", summary:data.message});
          setTimeout(() => {
            this.router.navigate(['/auth/dang-nhap']);
          }, 2000);
        }
        else 
        this.messageService.add({severity:"error", summary:data.message});

      },
      error => {
        this.messageService.add({severity:"error", summary:'Đã xảy ra lỗi'});

      }
    )
    this.loading = false;
    
  }
}
