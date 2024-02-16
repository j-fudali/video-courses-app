import { Injectable, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { jwtDecode } from 'jwt-decode';
import { CookieService } from 'ngx-cookie-service';
import { JwtToken } from '../interfaces/JwtToken';
import { ShoppingCartService } from './shopping-cart.service';
import { UserRole } from '../util/roles.enum';
import { environment } from '../../../environments/environment.development';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private cookies = inject(CookieService);
  private router = inject(Router);
  private shoppingCartService = inject(ShoppingCartService);
  private readonly _isLoggedIn = signal<boolean>(false);
  public readonly $isLoggedIn = this._isLoggedIn.asReadonly();
  private readonly _isAdmin = signal<boolean>(false);
  public readonly $isAdmin = this._isAdmin.asReadonly();
  checkIsLoggedIn(): void {
    const token = this.cookies.get('token');
    const isLoggedIn = token != null && token != '';
    if (!isLoggedIn) this.shoppingCartService.clearCart();
    this._isLoggedIn.set(isLoggedIn);
  }
  checkIsAdmin() {
    this._isAdmin.set(this.getUserRole() === UserRole.Admin);
  }
  getToken() {
    return this.cookies.get('token');
  }
  getUser() {
    return jwtDecode<JwtToken>(this.getToken()).sub;
  }
  getUserId() {
    const token = this.getToken();
    return token ? jwtDecode<JwtToken>(this.getToken()).userId : null;
  }
  getUserRole() {
    return this.getToken() ? jwtDecode<JwtToken>(this.getToken()).role : null;
  }
  decodeAndStoreTokenData(token: string) {
    const expiration = new Date();
    expiration.setDate(expiration.getDate() + 1);
    this.cookies.set('token', token, expiration, '/');
    this.checkIsLoggedIn();
    this.checkIsAdmin();
  }

  logout() {
    this._isLoggedIn.set(false);
    this._isAdmin.set(false);
    this.cookies.delete('token', '/', environment.domain);
    this.router.navigate(['/']);
  }
}
