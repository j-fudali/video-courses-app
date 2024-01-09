import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { environment } from '../../../environments/environment.development';
import { Observable } from 'rxjs';
import { Category } from '../interfaces/Category';

@Injectable({
  providedIn: 'root',
})
export class CategoriesService {
  private http = inject(HttpClient);
  private baseUrl = environment.url + '/categories';

  getCategories(): Observable<Category[]> {
    return this.http.get<Category[]>(this.baseUrl);
  }
}
