import { Injectable, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { jwtDecode } from 'jwt-decode';
import { CookieService } from 'ngx-cookie-service';
import { JwtToken } from '../interfaces/JwtToken';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private cookies = inject(CookieService);
  private router = inject(Router);
  private readonly _isLoggedIn = signal<boolean>(false);
  public readonly $isLoggedIn = this._isLoggedIn.asReadonly();
  checkIsLoggedIn(): void {
    const token = this.cookies.get('token');
    const isLoggedIn = token != null && token != '';
    this._isLoggedIn.set(isLoggedIn);
  }
  getToken() {
    return this.cookies.get('token');
  }
  getUser() {
    return jwtDecode<JwtToken>(this.getToken()).sub;
  }
  getUserId() {
    return jwtDecode<JwtToken>(this.getToken()).userId;
  }
  getUserRole() {
    return jwtDecode<JwtToken>(this.getToken()).role;
  }
  decodeAndStoreTokenData(token: string) {
    const expiration = new Date();
    expiration.setHours(expiration.getHours() + 1);
    this.cookies.set('token', token, expiration, '/');
    this.checkIsLoggedIn();
  }

  logout() {
    this._isLoggedIn.set(false);
    this.cookies.delete('token', '/', 'localhost');
    this.router.navigate(['/']);
  }
}
