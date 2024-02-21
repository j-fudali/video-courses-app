import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoginCredentials } from '../../shared/interfaces/LoginCredentials';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class LoginService {
  private url = environment.url + '/auth/';
  private http = inject(HttpClient);

  login(loginCredentials: LoginCredentials): Observable<{ token: string }> {
    return this.http.post<{ token: string }>(
      this.url + 'login',
      loginCredentials
    );
  }
  resetPassword(email: string) {
    return this.http.post(this.url + 'reset-password', { email });
  }
}
