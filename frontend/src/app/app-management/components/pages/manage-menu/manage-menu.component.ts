import { async } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService, ConfirmationService } from 'primeng/api';
import { Table } from 'primeng/table';
import { storageKey } from 'src/app/app-constant';
import { Food } from 'src/app/app-management/Model/Food';
import { AuthService } from 'src/app/app-management/service/auth.service';
import { LayoutService } from 'src/app/layout/service/app.layout.service';
import { environment } from 'src/environments/environment';
import { ResponseMessage } from 'src/app/app-management/Model/ResponsMessage';
import { createFoodModel } from 'src/app/app-management/Model/createFoodModel';

@Component({
    selector: 'app-manage-menu',
    templateUrl: './manage-menu.component.html',
    styleUrls: ['./manage-account.scss']
  })
export class ManageMenuComponent implements OnInit {
    @ViewChild('dt1') dt1: Table | undefined;
    loading: boolean = false;
    listFood : Food[] = [];
    foodSelected: Food = {};
    createFoodModel: createFoodModel = {};
    categorySelected: number = 1;
    isShowFoodDetail: boolean = false;
    isShowCreateFood: boolean = false;
    backendApiUrl: string = '';

    status = [
        {
            name: 'Khả dụng',
            value: 'available',
        },
        {
            name: 'Không khả dụng',
            value: 'unavailable',
        },
    ];
    listCategory: any[] = [];

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
    categories: any[] = [];
    ngOnInit(): void {
        this.header = new HttpHeaders().set(
            storageKey.AUTHORIZATION,
            this.authService.getToken()
        );
        this.backendApiUrl = environment.backendApiUrl;
        this.loadData();
    }

    getAllCategories(): void {
        const apiUrl = environment.backendApiUrl+'/api/v1/project/auth/category/findAll';
    
        this.http.get<any[]>(apiUrl).subscribe({
          next: (data) => {
            this.categories = data; 
            // Gán dữ liệu API trả về vào biến categories
            console.log('Categories:', this.categories);
          },
          error: (err) => {
            console.error('Error fetching categories:', err);
          }
        });
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
            .get<ResponseMessage>(environment.backendApiUrl+'/api/v1/project/auth/food/findAll', {
                headers: this.header,
            }).toPromise()
            .then(
                (data) => {
                    if (data?.resultCode == 0) {
                        this.listFood = data.data;
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
            .get<ResponseMessage>(environment.backendApiUrl+'/api/v1/project/auth/category/findAll', {
                headers: this.header,
            })
            .toPromise().then(
                (data) => {
                    if (data?.resultCode == 0) {
                        this.listCategory = data.data;
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
    showDetailFood(object: any) {
        this.foodSelected = object;
        this.isShowFoodDetail = true;
    }
    
    async deleteFood() {
        this.loading = true;
        this.isShowFoodDetail = false
        await this.http
            .delete<ResponseMessage>(environment.backendApiUrl+
                '/api/v1/project/food/delete?foodId=' +
                    this.foodSelected.id,

                { headers: this.header }
            )
            .toPromise()
            .then(
                (data) => {
                    if (data?.resultCode == 0) {
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Xóa món ăn thành công',
                        });
                        this.loadData();
                        this.isShowFoodDetail = false;
                    } else {
                        this.messageService.add({
                            severity: 'error',
                            summary: 'Không thể xóa món ăn',
                        });
                        this.isShowFoodDetail = true;
                    }
                },
                (error) => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error occur',
                    });
                    this.isShowFoodDetail = true;
                }
            );
        this.loading = false;
    }

    onFileSelected(event: any) {
        const file = event.target.files[0]; // Lấy file đầu tiên
        if (file) {
            const formData = new FormData();
            formData.append('file', file); // API yêu cầu gửi file dưới dạng FormData
    
            // Gửi ảnh lên server trước
            this.http.post<any>(environment.backendApiUrl+'/api/v1/project/auth/upload', formData)
                .toPromise()
                .then(response => {
                    if (response.resultCode === 0) {
                        // Gán giá trị imageUrl là tên file trả về từ API
                        this.createFoodModel.imageUrl = response.data;

                    } else {
                        this.messageService.add({
                            severity: 'error',
                            summary: 'Tải ảnh thất bại!',
                            detail: response.message
                        });
                    }
                })
                .catch(error => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Lỗi khi tải ảnh lên',
                        detail: error.message
                    });
                });
        }
    }

    onFileSelectedUpdate(event: any) {
        const file = event.target.files[0]; // Lấy file đầu tiên
        if (file) {
            const formData = new FormData();
            formData.append('file', file); // API yêu cầu gửi file dưới dạng FormData
    
            // Gửi ảnh lên server trước
            this.http.post<any>(environment.backendApiUrl+'/api/v1/project/auth/upload', formData)
                .toPromise()
                .then(response => {
                    if (response.resultCode === 0) {
                        // Gán giá trị imageUrl là tên file trả về từ API
                        this.foodSelected.imageUrl = response.data;

                    } else {
                        this.messageService.add({
                            severity: 'error',
                            summary: 'Tải ảnh thất bại!',
                            detail: response.message
                        });
                    }
                })
                .catch(error => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Lỗi khi tải ảnh lên',
                        detail: error.message
                    });
                });
        }
    }
    
    async createFood() {
        this.loading = true;
        this.isShowCreateFood = false;
        console.log(this.createFoodModel);
    
        try {
            let response = await this.http.post<ResponseMessage>(
                environment.backendApiUrl + '/api/v1/project/food/create?category_id=' + this.categorySelected,
                this.createFoodModel,
                { headers: this.header }
            ).toPromise();
            console.log(response);
            console.log(this.header);
    
            if (response?.resultCode === 0) {
                this.messageService.add({
                    severity: 'success',
                    summary: 'Thêm món ăn thành công!',
                });
                this.loadData();
                this.isShowCreateFood = false;
            } else {
                this.messageService.add({
                    severity: 'error',
                    summary: response?.message || 'Thêm món ăn thất bại!',
                });
                this.isShowCreateFood = true;
            }
        } catch (error) {
            this.messageService.add({
                severity: 'error',
                summary: 'Lỗi xảy ra khi tạo món ăn!',
            });
            this.isShowCreateFood = true;
        }
    
        this.loading = false;
    }
    
    confirmDelete() {
      // console.log("delete")
      this.confirmationService.confirm({
          message: 'Xác nhận xóa món ăn này?',
          header: 'Xác nhận xóa',
          icon: 'pi pi-exclamation-triangle',
          accept: () => {
            this.deleteFood();
          }
      });
  }

  showCreateFood() {
    this.isShowCreateFood = true;
    this.foodSelected = {};
  }

  updateFood() {

    this.http.put<ResponseMessage>(environment.backendApiUrl+'/api/v1/project/food/update', this.foodSelected,{ headers: this.header }).subscribe({
        next: (response) => {
            console.log('Cập nhật thành công:', response);
            this.loadData();
            this.isShowFoodDetail = false;
            this.messageService.add({ severity: 'success', summary: 'Cập nhật thành công!' });
        },
        error: (err) => {
            console.error('Lỗi cập nhật:', err);
            this.loadData();
            this.messageService.add({ severity: 'error', summary: 'Cập nhật thất bại!' });
        }
    });
}

}
