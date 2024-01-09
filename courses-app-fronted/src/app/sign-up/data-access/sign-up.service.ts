import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { environment } from '../../../environments/environment.development';
import { SignUpCredentials } from '../../shared/interfaces/SignUpCredentials';

@Injectable({
  providedIn: 'root',
})
export class SignUpService {
  private http = inject(HttpClient);
  private baseUrl = environment.url + '/auth/register';

  signUp(credentials: SignUpCredentials) {
    return this.http.post<{ token: string }>(this.baseUrl, { ...credentials });
  }
}
