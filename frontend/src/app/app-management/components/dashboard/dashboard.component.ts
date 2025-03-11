import { Component, OnInit, OnDestroy } from '@angular/core';
import { LayoutService } from 'src/app/layout/service/app.layout.service';
import { MessageService } from 'primeng/api';
import { WebSocketService } from '../../service/websocketService';
import { AuthService } from '../../service/auth.service';
import { environment } from 'src/environments/environment';
import { ResponseMessage } from '../../Model/ResponsMessage';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { storageKey } from 'src/app/app-constant';
@Component({
    templateUrl: './dashboard.component.html',
})
export class DashboardComponent implements OnInit {
    chartData: any;

    chartOptions: any;
    loading: boolean = false;
    header: any;
    totalRevenueToday: number = 0;
    totalOrderDoneToday: number = 0;
    totalOrderUnconfirmToday: number = 0;
    totalOrderPreparingToday: number = 0;
    dataChartByDay: any[] = [];
    dataChartByMonth: any[] = [];
    chartLabelDay: any[] = [];
    chartLabelMonth: any[] = [];
    chartDataDay: any[] = [];
    chartDataMonth: any[] = [];

    chartDay: any;
    chartMonth: any;

    constructor(
        public layoutService: LayoutService,
        private messageService: MessageService,
        private websocketService: WebSocketService,
        private authService: AuthService,
        private http: HttpClient
    ) {
        // this.websocketService.connect();
    }

    ngOnInit() {
        this.header = new HttpHeaders().set(
            storageKey.AUTHORIZATION,
            this.authService.getToken()
        );
        this.loadData();
    }

    async loadData() {
        this.loading = true;
        await this.http
            .get<ResponseMessage>(
                environment.backendApiUrl +
                    '/api/v1/project/order/getTotalRevenueToday',
                {
                    headers: this.header,
                }
            )
            .toPromise()
            .then(
                (data) => {
                    if (data?.resultCode == 0) {
                        this.totalRevenueToday =
                            data.data == null ? 0 : data.data;
                        // console.log(this.listAccount);
                    } else {
                        this.messageService.add({
                            severity: 'error',
                            summary: data?.message,
                        });
                    }
                    console.log(data);
                },
                (error) => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error occur',
                    });
                }
            );

        await this.http
            .get<ResponseMessage>(
                environment.backendApiUrl +
                    '/api/v1/project/order/getTotalDoneOrdersToday',
                {
                    headers: this.header,
                }
            )
            .toPromise()
            .then(
                (data) => {
                    if (data?.resultCode == 0) {
                        this.totalOrderDoneToday = data.data;
                        // console.log(this.listAccount);
                    } else {
                        this.messageService.add({
                            severity: 'error',
                            summary: data?.message,
                        });
                    }
                    console.log(data);
                },
                (error) => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error occur',
                    });
                }
            );

        await this.http
            .get<ResponseMessage>(
                environment.backendApiUrl +
                    '/api/v1/project/order/getTotalPreparingOrdersToday',
                {
                    headers: this.header,
                }
            )
            .toPromise()
            .then(
                (data) => {
                    if (data?.resultCode == 0) {
                        this.totalOrderPreparingToday = data.data;
                        // console.log(this.listAccount);
                    } else {
                        this.messageService.add({
                            severity: 'error',
                            summary: data?.message,
                        });
                    }
                    console.log(data);
                },
                (error) => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error occur',
                    });
                }
            );

        await this.http
            .get<ResponseMessage>(
                environment.backendApiUrl +
                    '/api/v1/project/order/getTotalUnConfirmOrdersToday',
                {
                    headers: this.header,
                }
            )
            .toPromise()
            .then(
                (data) => {
                    if (data?.resultCode == 0) {
                        this.totalOrderUnconfirmToday = data.data;
                        // console.log(this.listAccount);
                    } else {
                        this.messageService.add({
                            severity: 'error',
                            summary: data?.message,
                        });
                    }
                    console.log(data);
                },
                (error) => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error occur',
                    });
                }
            );

        await this.http
            .get<ResponseMessage>(
                environment.backendApiUrl +
                    '/api/v1/project/order/getTotalRevenueLastDays?day=7',
                {
                    headers: this.header,
                }
            )
            .toPromise()
            .then(
                (data) => {
                    if (data?.resultCode == 0) {
                        this.dataChartByDay = data.data;
                        this.initChartByDay();
                        // console.log(this.listAccount);
                    } else {
                        this.messageService.add({
                            severity: 'error',
                            summary: data?.message,
                        });
                    }
                    console.log(data);
                },
                (error) => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error occur',
                    });
                }
            );

        await this.http
            .get<ResponseMessage>(
                environment.backendApiUrl +
                    '/api/v1/project/order/getTotalRevenueLastMonths?month=12',
                {
                    headers: this.header,
                }
            )
            .toPromise()
            .then(
                (data) => {
                    if (data?.resultCode == 0) {
                        this.dataChartByMonth = data.data;
                        this.initChartByMonth();
                        // console.log(this.listAccount);
                    } else {
                        this.messageService.add({
                            severity: 'error',
                            summary: data?.message,
                        });
                    }
                    console.log(data);
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

    initChartByDay() {
        const documentStyle = getComputedStyle(document.documentElement);
        const textColor = documentStyle.getPropertyValue('--text-color');
        const textColorSecondary = documentStyle.getPropertyValue(
            '--text-color-secondary'
        );
        const surfaceBorder =
            documentStyle.getPropertyValue('--surface-border');
        this.chartDataDay = [];
        this.chartLabelDay = [];
        for (let i = 0; i < this.dataChartByDay.length; i++) {
            this.chartLabelDay.push(this.dataChartByDay[i].orderDate);
            this.chartDataDay.push(this.dataChartByDay[i].totalRevenue);
        }

        this.chartDay = {
            labels: this.chartLabelDay,
            datasets: [
                {
                    label: 'Doanh số',
                    data: this.chartDataDay,
                    fill: false,
                    backgroundColor:
                        documentStyle.getPropertyValue('--green-500'),
                    borderColor: documentStyle.getPropertyValue('--green-500'),
                    tension: 0.4,
                },
            ],
        };

        this.chartOptions = {
            plugins: {
                legend: {
                    labels: {
                        color: textColor,
                    },
                },
            },
            scales: {
                x: {
                    ticks: {
                        color: textColorSecondary,
                    },
                    grid: {
                        color: surfaceBorder,
                        drawBorder: false,
                    },
                },
                y: {
                    ticks: {
                        color: textColorSecondary,
                    },
                    grid: {
                        color: surfaceBorder,
                        drawBorder: false,
                    },
                },
            },
        };
    }

    initChartByMonth() {
        const documentStyle = getComputedStyle(document.documentElement);
        this.chartDataMonth = [];
        this.chartLabelMonth = [];
        for (let i = 0; i < this.dataChartByMonth.length; i++) {
            this.chartLabelMonth.push(this.dataChartByMonth[i].orderMonth);
            this.chartDataMonth.push(this.dataChartByMonth[i].totalRevenue);
        }
        this.chartMonth = {
            labels: this.chartLabelMonth,
            datasets: [
                {
                    label: 'Doanh số',
                    data: this.chartDataMonth,
                    fill: false,
                    backgroundColor:
                        documentStyle.getPropertyValue('--blue-500'),
                    borderColor: documentStyle.getPropertyValue('--blue-500'),
                    tension: 0.4,
                },
            ],
        };
        console.log(this.chartMonth);
        console.log(this.chartDay);
    }
}
