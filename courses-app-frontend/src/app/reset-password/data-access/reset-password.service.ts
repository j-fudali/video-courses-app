import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { environment } from '../../../environments/environment.development';

@Injectable({
  providedIn: 'root',
})
export class ResetPasswordService {
  private http = inject(HttpClient);
  private baseUrl = environment.url + '/reset-password';

  resetPassword(email: string) {
    return this.http.post<{ message: string }>(this.baseUrl, { email });
  }
  resetPasswordConfirm(token: string, password: string) {
    const params = new HttpParams().set('token', token);
    return this.http.post<{ message: string }>(
      this.baseUrl + '/confirm',
      { password },
      { params }
    );
  }
}
