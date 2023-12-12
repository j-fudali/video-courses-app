import { Injectable, inject } from '@angular/core';
import { environment } from '../../../environments/environment.development';
import { LoginCredentials } from '../../shared/interfaces/loginCredentials';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class LoginService {
  private url = environment.url + '/login';
  private http = inject(HttpClient);

  login(loginCredentials: LoginCredentials): Observable<{ token: string }> {
    return this.http.post<{ token: string }>(this.url, loginCredentials);
  }
}
