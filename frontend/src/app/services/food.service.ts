import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FoodService {
  private apiUrl = 'http://13.239.169.8:8080/api/v1/project/auth/food/findAllAvailable';

  constructor(private http: HttpClient) {}

  // ✅ Hàm lấy danh sách món ăn từ API
  getAllFoods(): Observable<any> {
    return this.http.get<any>(this.apiUrl);
  }

  
  
  
}
