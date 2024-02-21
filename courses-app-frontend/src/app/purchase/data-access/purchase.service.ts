import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class PurchaseService {
  private http = inject(HttpClient);
  private baseUrl = environment.url + '/courses';

  buyCourse(courseId: number) {
    return this.http.post<{ message: string }>(
      `${this.baseUrl}/${courseId}/buy`,
      undefined
    );
  }
}
