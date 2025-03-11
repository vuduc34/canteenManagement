import { AuthService } from 'src/app/app-management/service/auth.service';
import { Component, ElementRef, ViewChild, OnInit } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { LayoutService } from "./service/app.layout.service";
import { Router } from '@angular/router';

@Component({
    selector: 'app-topbar',
    templateUrl: './app.topbar.component.html'
})
export class AppTopBarComponent implements OnInit {

    items!: MenuItem[];

    @ViewChild('menubutton') menuButton!: ElementRef;

    @ViewChild('topbarmenubutton') topbarMenuButton!: ElementRef;

    @ViewChild('topbarmenu') menu!: ElementRef;

    constructor(public layoutService: LayoutService,private authService: AuthService,private router: Router) { }
    ngOnInit() {
       this.username=this.authService.getUsername();
    }
    isShowDialog: boolean = false;
    username:any;
    position: string ='top-right';
    logout() {
        this.authService.setToken('');
        this.router.navigate(['']);
    }
    
}
