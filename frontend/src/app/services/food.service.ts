import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class FoodService {
  private apiUrl = environment.backendApiUrl+'/api/v1/project/auth/food/findFilter?text=';

  constructor(private http: HttpClient) {}

  // ✅ Hàm lấy danh sách món ăn từ API
  getAllFoods(text: String): Observable<any> {
    return this.http.get<any>(this.apiUrl+text);
  }

  
  
  
}
