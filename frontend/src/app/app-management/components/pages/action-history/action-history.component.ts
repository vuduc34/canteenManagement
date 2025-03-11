import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService, ConfirmationService } from 'primeng/api';
import { Table } from 'primeng/table';
import { storageKey } from 'src/app/app-constant';
import { Account } from 'src/app/app-management/Model/Account';
import { ResponseMessage } from 'src/app/app-management/Model/ResponsMessage';
import { AuthService } from 'src/app/app-management/service/auth.service';
import { LayoutService } from 'src/app/layout/service/app.layout.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-action-history',
  templateUrl: './action-history.component.html'
})
export class ActionHistoryComponent implements OnInit {

   @ViewChild('dt1') dt1: Table | undefined;
   @ViewChild('dt2') dt2: Table | undefined;
    loading: boolean = false;
     listAccount: Account[] = [];
     isShowHistory: boolean = false;
     accountSelected: Account = {};
     staffNameSelected: string = '';
     listHistory: any[] = [];
   constructor(
         private route: ActivatedRoute,
         public layoutService: LayoutService,
         public router: Router,
         private messageService: MessageService,
         private http: HttpClient,
         private authService: AuthService,
         private confirmationService: ConfirmationService
     ) {}
     header: any;
 
     ngOnInit(): void {
         this.header = new HttpHeaders().set(
             storageKey.AUTHORIZATION,
             this.authService.getToken()
         );
         this.loadData();
     }
     applyFilterGlobal($event: any, stringVal: any) {
         this.dt1!.filterGlobal(
             ($event.target as HTMLInputElement).value,
             stringVal
         );
     }
     applyFilterGlobal2($event: any, stringVal: any) {
      this.dt2!.filterGlobal(
          ($event.target as HTMLInputElement).value,
          stringVal
      );
  }

      async loadData() {
             this.loading = true;
             await this.http
                 .get<ResponseMessage>(environment.backendApiUrl+'/api/v1/project/account/findAll?role='+this.authService.getRole(), {
                     headers: this.header,
                 }).toPromise()
                 .then(
                     (data) => {
                         if (data?.resultCode == 0) {
                             this.listAccount = data.data;
                             this.listAccount = this.listAccount.filter(account => account.role !== "ROLE_USER")
                             // console.log(this.listAccount);
                         } else {
                             this.messageService.add({
                                 severity: 'error',
                                 summary: data?.message,
                             });
                         }
                         console.log(data)
                     },
                     (error) => {
                         this.messageService.add({
                             severity: 'error',
                             summary: 'Error occur',
                         });
                     }
                 );
            
                 this.loading = false;
         }
        

         async showDetailAccount(object: any) {
          this.loading = true;
          this.accountSelected = object;
          this.staffNameSelected = object.fullname
          await this.http
                 .get<ResponseMessage>(environment.backendApiUrl+'/api/v1/project/actionDetail/findByUsername?username='+this.accountSelected.username, {
                     headers: this.header,
                 }).toPromise()
                 .then(
                     (data) => {
                         if (data?.resultCode == 0) {
                             this.listHistory = data.data;
                             this.listHistory = this.listHistory.sort((a, b) => new Date(b.timeCreate).getTime() - new Date(a.timeCreate).getTime());
                             // console.log(this.listAccount);
                         } else {
                             this.messageService.add({
                                 severity: 'error',
                                 summary: data?.message,
                             });
                         }
                         console.log(data)
                     },
                     (error) => {
                         this.messageService.add({
                             severity: 'error',
                             summary: 'Error occur',
                         });
                     }
                 );
          this.isShowHistory = true;
          this.loading = false;
      }         
}

