import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { environment } from 'src/environments/environment';
import { Router } from '@angular/router';

@Component({
  selector: 'app-introduce',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './introduce.component.html',
  styleUrls: ['./introduce.component.scss']
})



export class IntroduceComponent implements OnInit {
  canteenInfo: any = {};

  constructor(
      private http: HttpClient,
      private router: Router,
    ) { }

    navigateTo(path: string) {
      this.router.navigate([path]);
    }
    
  ngOnInit(): void {
    this.getCanteenInfo();
  }

  getCanteenInfo(): void {
      this.http.get(environment.backendApiUrl +'/api/v1/project/auth/canteenInfo/get').subscribe(
        (data) => {
          this.canteenInfo = data;
          console.log('Canteen Info:', this.canteenInfo);
        },
        (error) => {
          console.error('Error fetching canteen info:', error);
        }
      );
    }
    
}
