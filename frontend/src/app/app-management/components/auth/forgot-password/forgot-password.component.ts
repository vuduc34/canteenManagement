import { async } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html'
})
export class ForgotPasswordComponent implements OnInit {

  constructor(private httpClient: HttpClient,private router: Router,private messageService: MessageService) { }

  ngOnInit(): void {
  }
  email: any;
  loading: boolean = false;
  async forgotPassword () {
    this.loading = true;
    await this.httpClient.post<any>(environment.backendApiUrl+"/api/v1/project/auth/forgotpw",{userName:this.email}).toPromise().then(
      data => {
        if(data.resultCode == "0") {
          this.messageService.add({severity:'success',summary:'Thành công',detail:'Vui lòng kiểm tra email của bạn.'});
          setTimeout(() => {
            this.router.navigate(['/auth/khoi-phuc-mat-khau']);
          }, 3000); 
        }
        else 
        this.messageService.add({severity:"error", summary:data.message});

      },
      error => {
        this.messageService.add({severity:"error", summary:error.title});

      }
    )
    this.loading = false;
    
  }

}
