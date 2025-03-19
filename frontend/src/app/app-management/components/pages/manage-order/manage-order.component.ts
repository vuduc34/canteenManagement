import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit, ViewChild,HostListener } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Client } from '@stomp/stompjs';
import { MessageService, ConfirmationService } from 'primeng/api';
import { Table } from 'primeng/table';
import { storageKey } from 'src/app/app-constant';
import { ResponseMessage } from 'src/app/app-management/Model/ResponsMessage';
import { AuthService } from 'src/app/app-management/service/auth.service';
import { LayoutService } from 'src/app/layout/service/app.layout.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-manage-order',
  templateUrl: './manage-order.component.html',
  styleUrls: ['./manage-account.scss']
})
export class ManageOrderComponent implements OnInit {
   @ViewChild('dt1') dt1: Table | undefined;


   @HostListener('document:keydown', ['$event'])
    onKeyPress(event: KeyboardEvent) {
    this.isShowOrderDetail = false;
  }
    loading: boolean = false;

    private stompClient: Client;
    isShowOrderDetail: boolean = false;
    loadAllOrder: boolean =  false;
    backendApiUrl: string = '';



    constructor(
          private route: ActivatedRoute,
          public layoutService: LayoutService,
          public router: Router,
          private messageService: MessageService,
          private http: HttpClient,
          private authService: AuthService,
          private confirmationService: ConfirmationService
      ) {
        this.backendApiUrl = environment.backendApiUrl;
        this.stompClient = new Client({
              brokerURL:environment.backendApiUrl+ '/ws?token='+this.authService.getToken(), 
            //   connectHeaders: {
            //     Authorization: `${this.authService.getToken()}` // Gửi JWT trong header (dự phòng)
            // },
              debug: (msg: string) => console.log(msg), // Debug log
              reconnectDelay: 5000, // Tự động kết nối lại sau 5s nếu mất kết nối
              heartbeatIncoming: 4000,
              heartbeatOutgoing: 4000
            });
        
            this.stompClient.onConnect = (frame) => {
              this.stompClient.subscribe('/topic/public', message => {
                if(message.body == 'newOrder') {
                  this.messageService.add({
                    severity: 'success',
                    summary: 'Có đơn hàng mới',
                });
                } else if(message.body == 'cancelOrder') {
                  this.messageService.add({
                    severity: 'warn',
                    summary: 'Đơn hàng đã bị hủy',
                });
                }
                this.loadData();
              });
            };
        
            this.stompClient.onDisconnect = () => {
              console.log('Disconnected!');
            };

            this.connect();
      }
      header: any;
      role: string = '';
      listOrder: any[] = [];
      orderSelected: any;
      listOrderDetail: any[] = [];
      ngOnInit(): void {
          this.header = new HttpHeaders().set(
              storageKey.AUTHORIZATION,
              this.authService.getToken()
          );
          this.loadData();
      }
      ngOnDestroy() {
        this.disconnect();
       }
      applyFilterGlobal($event: any, stringVal: any) {
          this.dt1!.filterGlobal(
              ($event.target as HTMLInputElement).value,
              stringVal
          );
      }

      connect() {
        this.stompClient.activate(); // Thay vì connect()
      }
    
      // Ngắt kết nối
      disconnect() {
        this.stompClient.deactivate();
      }

      async loadData() {
        if(this.loadAllOrder) {
          await this.http
                    .get<ResponseMessage>(environment.backendApiUrl+'/api/v1/project/order/getAllOrder', {
                        headers: this.header,
                    }).toPromise()
                    .then(
                        (data) => {
                            if (data?.resultCode == 0) {
                                this.listOrder = data.data;
                                this.listOrder = this.listOrder.sort((a, b) => b.id - a.id)
                                // console.log(this.listAccount);
                            } else {
                                this.messageService.add({
                                    severity: 'error',
                                    summary: data?.message,
                                });
                            }
                            // console.log(data)
                        },
                        (error) => {
                            this.messageService.add({
                                severity: 'error',
                                summary: 'Error occur',
                            });
                        }
                    );
        } else {
          await this.http
                    .get<ResponseMessage>(environment.backendApiUrl+'/api/v1/project/order/getOrderPreparingOrUnconfirmed', {
                        headers: this.header,
                    }).toPromise()
                    .then(
                        (data) => {
                            if (data?.resultCode == 0) {
                                this.listOrder = data.data;
                                // console.log(this.listAccount);
                            } else {
                                this.messageService.add({
                                    severity: 'error',
                                    summary: data?.message,
                                });
                            }
                            // console.log(data)
                        },
                        (error) => {
                            this.messageService.add({
                                severity: 'error',
                                summary: 'Error occur',
                            });
                        }
                    );
        }
      }
      async showOrderDetail(obj: any) {
        this.orderSelected = obj;
                this.loading = true;
                await this.http
                    .get<ResponseMessage>(environment.backendApiUrl+'/api/v1/project/orderItem/findOrderItemByOrderId?orderId='+this.orderSelected.id, {
                        headers: this.header,
                    }).toPromise()
                    .then(
                        (data) => {
                            if (data?.resultCode == 0) {
                                this.listOrderDetail = data.data;
                                // console.log(this.listAccount);
                            } else {
                                this.messageService.add({
                                    severity: 'error',
                                    summary: data?.message,
                                });
                            }
                            // console.log(data)
                        },
                        (error) => {
                            this.messageService.add({
                                severity: 'error',
                                summary: 'Error occur',
                            });
                        }
                    );
                    this.loading = false;       
        this.isShowOrderDetail = true;
      }
    async confirmOrder() {
      await this.http
      .put<ResponseMessage>(environment.backendApiUrl+'/api/v1/project/order/preparing?orderId='+this.orderSelected.id, null,{
          headers: this.header,
      }).toPromise()
      .then(
          (data) => {
              if (data?.resultCode == 0) {
                this.messageService.add({
                  severity: 'success',
                  summary: "Xác nhận đơn hàng thành công",
              });
                  this.loadData();
                  this.isShowOrderDetail = false;
                  // console.log(this.listAccount);
              } else {
                  this.messageService.add({
                      severity: 'error',
                      summary: data?.message,
                  });
              }
            //   console.log(data)
          },
          (error) => {
              this.messageService.add({
                  severity: 'error',
                  summary: 'Error occur',
              });
          }
      );
    }

    async doneOrder() {
      await this.http
      .put<ResponseMessage>(environment.backendApiUrl+'/api/v1/project/order/done?orderId='+this.orderSelected.id, null,{
          headers: this.header,
      }).toPromise()
      .then(
          (data) => {
              if (data?.resultCode == 0) {
                this.messageService.add({
                  severity: 'success',
                  summary: "Đơn  hàng đã hoàn thành",
              });
                  this.loadData();
                  this.isShowOrderDetail = false;
                  // console.log(this.listAccount);
              } else {
                  this.messageService.add({
                      severity: 'error',
                      summary: data?.message,
                  });
              }
            //   console.log(data)
          },
          (error) => {
              this.messageService.add({
                  severity: 'error',
                  summary: 'Error occur',
              });
          }
      );
    }
    async paidOrder() {
        await this.http
        .put<ResponseMessage>(environment.backendApiUrl+'/api/v1/project/order/paid?orderId='+this.orderSelected.id, null,{
            headers: this.header,
        }).toPromise()
        .then(
            (data) => {
                if (data?.resultCode == 0) {
                  this.messageService.add({
                    severity: 'success',
                    summary: "Đơn hàng đã thanh toán",
                });
                    this.loadData();
                    this.isShowOrderDetail = false;
                    // console.log(this.listAccount);
                } else {
                    this.messageService.add({
                        severity: 'error',
                        summary: data?.message,
                    });
                }
              //   console.log(data)
            },
            (error) => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error occur',
                });
            }
        );
      }



    confirmCancel() {
      this.confirmationService.confirm({
        message: 'Xác nhận hủy đơn hàng?',
        header: 'Xác nhận',
        icon: 'pi pi-exclamation-triangle',
        accept: () => {
          this.cancelOrder();
        }
    });

    }

    confirmReject() {
        this.confirmationService.confirm({
          message: 'Xác nhận là khách hàng không nhận hàng?',
          header: 'Xác nhận',
          icon: 'pi pi-exclamation-triangle',
          accept: () => {
            this.rejectOrder();
          }
      });
  
      }

    async cancelOrder() {
      await this.http
      .put<ResponseMessage>(environment.backendApiUrl+'/api/v1/project/order/cancel?orderId='+this.orderSelected.id, null,{
          headers: this.header,
      }).toPromise()
      .then(
          (data) => {
              if (data?.resultCode == 0) {
                  // this.loadData();
                  this.isShowOrderDetail = false;
                  // console.log(this.listAccount);
              } else {
                  this.messageService.add({
                      severity: 'error',
                      summary: data?.message,
                  });
              }
            //   console.log(data)
          },
          (error) => {
              this.messageService.add({
                  severity: 'error',
                  summary: 'Error occur',
              });
          }
      );
    }

    async rejectOrder() {
        await this.http
        .put<ResponseMessage>(environment.backendApiUrl+'/api/v1/project/order/rejected?orderId='+this.orderSelected.id, null,{
            headers: this.header,
        }).toPromise()
        .then(
            (data) => {
                if (data?.resultCode == 0) {
                    // this.loadData();
                    this.isShowOrderDetail = false;
                    this.messageService.add({
                        severity: 'success',
                        summary: 'Xác nhận thành công',
                    });
                    this.loadData()
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
      }
      
      checkTimeDifference(timeString: string): boolean {
        // Chuyển chuỗi thành đối tượng Date
        const givenTime = new Date(timeString);
        const currentTime = new Date();
      
        // Tính sự chênh lệch (đơn vị: milliseconds)
        const timeDifference = currentTime.getTime() - givenTime.getTime();
      
        // Đổi 12 giờ ra milliseconds
        const twelveHours = 12 * 60 * 60 * 1000;
      
        // So sánh và trả về kết quả
        return timeDifference > twelveHours;
      }
}
