import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FoodService {
  private baseUrl = 'http://13.239.169.8:8080/api/v1/project/auth';

  constructor(private http: HttpClient) {}

  // Gọi API để lấy danh sách món ăn
  getAllFoods(): Observable<any> {
    return this.http.get(`${this.baseUrl}/food/findAll`);
  }

  // Trả về đường dẫn ảnh đầy đủ
  getImageUrl(imagePath: string): string {
    return `${this.baseUrl}/${imagePath}`;
  }
}
