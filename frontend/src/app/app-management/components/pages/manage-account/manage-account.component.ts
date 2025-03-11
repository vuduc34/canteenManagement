import { async } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService, ConfirmationService } from 'primeng/api';
import { Table } from 'primeng/table';
import { storageKey } from 'src/app/app-constant';
import { Account } from 'src/app/app-management/Model/Account';
import { AuthService } from 'src/app/app-management/service/auth.service';
import { LayoutService } from 'src/app/layout/service/app.layout.service';
import { environment } from 'src/environments/environment';
import { ResponseMessage } from 'src/app/app-management/Model/ResponsMessage';
import { createAccountModel } from 'src/app/app-management/Model/createAccountModel';

@Component({
    selector: 'app-manage-account',
    templateUrl: './manage-account.component.html',
})
export class ManageAccountComponent implements OnInit {
    @ViewChild('dt1') dt1: Table | undefined;
    loading: boolean = false;
    listAccount: Account[] = [];
    accountSelected: Account = {};
    createAccountModel: createAccountModel = {};
    roleSelected: string = 'ROLE_USER';
    isShowAccountDetail: boolean = false;
    isShowCreateAccount: boolean = false;
    status = [
        {
            name: 'Chưa kích hoạt',
            value: 0,
        },
        {
            name: 'Đã kích hoạt',
            value: 1,
        },
    ];
    listRole: any[] = [];

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
    role: string = '';
    ngOnInit(): void {
        this.header = new HttpHeaders().set(
            storageKey.AUTHORIZATION,
            this.authService.getToken()
        );
        this.role = this.authService.getRole();
        console.log(this.role);
        this.loadData();
    }
    applyFilterGlobal($event: any, stringVal: any) {
        this.dt1!.filterGlobal(
            ($event.target as HTMLInputElement).value,
            stringVal
        );
    }
    async loadData() {
        this.loading = true;
        await this.http
            .get<ResponseMessage>(environment.backendApiUrl+'/api/v1/project/account/findAll?role='+this.role, {
                headers: this.header,
            }).toPromise()
            .then(
                (data) => {
                    if (data?.resultCode == 0) {
                        this.listAccount = data.data;
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
        await this.http
            .get<ResponseMessage>(environment.backendApiUrl+'/api/v1/project/account/findAllRole', {
                headers: this.header,
            })
            .toPromise().then(
                (data) => {
                    if (data?.resultCode == 0) {
                        this.listRole = data.data;
                        // console.log(this.listRole);
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
    showDetailAccount(object: any) {
        this.accountSelected = object;
        this.isShowAccountDetail = true;
    }

    async changeRole() {
        this.loading = true;
        await this.http
            .get<ResponseMessage>(environment.backendApiUrl+
                '/api/v1/project/account/changeRole?username=' +
                    this.accountSelected.username +
                    '&role=' +
                    this.accountSelected.role,

                { headers: this.header }
            )
            .toPromise()
            .then(
                (data) => {
                    if (data?.resultCode == 0) {
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Cập nhật thành công',
                        });
                    } else {
                        this.messageService.add({
                            severity: 'error',
                            summary: data?.message,
                        });
                        this.showDetailAccount(this.accountSelected);
                    }
                },
                (error) => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error occur',
                    });
                    this.showDetailAccount(this.accountSelected);
                }
            );
        this.loading = false;
    }
    async changeStatus() {
        this.loading = true;
        if (this.accountSelected.status == 0) {
            await this.http
                .get<ResponseMessage>(environment.backendApiUrl+
                    '/api/v1/project/account/deactiveAccount?username=' +
                        this.accountSelected.username,

                    { headers: this.header }
                )
                .toPromise()
                .then(
                    (data) => {
                        if (data?.resultCode == 0) {
                            this.messageService.add({
                                severity: 'success',
                                summary: 'Cập nhật thành công',
                            });
                        } else {
                            this.messageService.add({
                                severity: 'error',
                                summary: data?.message,
                            });
                            this.showDetailAccount(this.accountSelected);
                        }
                    },
                    (error) => {
                        this.messageService.add({
                            severity: 'error',
                            summary: 'Error occur',
                        });
                        this.showDetailAccount(this.accountSelected);
                    }
                );
        } else {
            await this.http
                .get<ResponseMessage>(environment.backendApiUrl+
                    '/api/v1/project/account/activeAccount?username=' +
                        this.accountSelected.username,

                    { headers: this.header }
                )
                .toPromise()
                .then(
                    (data) => {
                        if (data?.resultCode == 0) {
                            this.messageService.add({
                                severity: 'success',
                                summary: 'Cập nhật thành công',
                            });
                        } else {
                            this.messageService.add({
                                severity: 'error',
                                summary: data?.message,
                            });
                            this.showDetailAccount(this.accountSelected);
                        }
                    },
                    (error) => {
                        this.messageService.add({
                            severity: 'error',
                            summary: 'Error occur',
                        });
                        this.showDetailAccount(this.accountSelected);
                    }
                );
        }

        this.loading = false;
    }
    async deleteAccount() {
        this.loading = true;
        this.isShowAccountDetail = false
        await this.http
            .delete<ResponseMessage>(environment.backendApiUrl+
                '/api/v1/project/account/delete?username=' +
                    this.accountSelected.username,

                { headers: this.header }
            )
            .toPromise()
            .then(
                (data) => {
                    if (data?.resultCode == 0) {
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Xóa tài khoản thành công',
                        });
                        this.loadData();
                        this.isShowAccountDetail = false;
                    } else {
                        this.messageService.add({
                            severity: 'error',
                            summary: 'Không thể xóa tài khoản',
                        });
                        this.isShowAccountDetail = true;
                        // this.showDetailAccount(this.accountSelected);
                    }
                },
                (error) => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error occur',
                    });
                    this.isShowAccountDetail = true;
                    // this.showDetailAccount(this.accountSelected);
                }
            );
        this.loading = false;
    }

    async createAccount() {
      this.loading = true;
      this.isShowCreateAccount = false;
      await this.http
          .post<ResponseMessage>(environment.backendApiUrl+
              '/api/v1/project/account/create?roleName='+this.roleSelected,this.createAccountModel,

              { headers: this.header }
          )
          .toPromise()
          .then(
              (data) => {
                  if (data?.resultCode == 0) {
                      this.messageService.add({
                          severity: 'success',
                          summary: 'Tạo tài khoản thành công',
                      });
                      this.loadData();
                      this.isShowCreateAccount = false;
                  } else {
                      this.messageService.add({
                          severity: 'error',
                          summary: data?.message
                      });
                      this.isShowCreateAccount = true;
                      // this.showDetailAccount(this.accountSelected);
                  }
              },
              (error) => {
                  this.messageService.add({
                      severity: 'error',
                      summary: 'Error occur',
                  });
                  this.isShowCreateAccount = true;
                  // this.showDetailAccount(this.accountSelected);
              }
          );
      this.loading = false;
  }
    confirmDelete() {
      // console.log("delete")
      this.confirmationService.confirm({
          message: 'Xác nhận xóa tài  khoản?',
          header: 'Xác nhận xóa',
          icon: 'pi pi-exclamation-triangle',
          accept: () => {
            this.deleteAccount();
          }
      });
  }

  showCreateAccount() {
    this.isShowCreateAccount = true;
    this.accountSelected = {};
  }
}
