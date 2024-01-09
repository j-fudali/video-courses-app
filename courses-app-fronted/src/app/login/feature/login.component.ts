import { CommonModule } from '@angular/common';
import { Component, DestroyRef, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { LoginService } from '../data-access/login.service';
import { LoginCredentials } from '../../shared/interfaces/LoginCredentials';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../shared/data-access/auth.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { jwtDecode } from 'jwt-decode';
import { JwtToken } from '../../shared/interfaces/JwtToken';
import { catchError, throwError } from 'rxjs';
@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatProgressBarModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    RouterModule,
  ],
  template: `
    <mat-card>
      <mat-card-header>
        <mat-card-title>Login</mat-card-title>
      </mat-card-header>
      <mat-card-content>
        <form [formGroup]="loginForm" (ngSubmit)="login()">
          <mat-form-field>
            <mat-label>E-mail</mat-label>
            <input
              matInput
              type="email"
              name="email"
              id="email"
              formControlName="email"
            />
            @if(email && email.invalid){
            <mat-error>{{ getEmailError() }}</mat-error>
            }
          </mat-form-field>
          <mat-form-field>
            <mat-label>Password</mat-label>
            <input
              matInput
              type="password"
              name="password"
              id="password"
              formControlName="password"
            />
            @if(password && password.invalid){
            <mat-error>{{ getPasswordError() }}</mat-error>
            }
          </mat-form-field>
          <button
            type="submit"
            [disabled]="loginForm.invalid"
            mat-raised-button
            color="primary"
          >
            Login
          </button>
        </form>
      </mat-card-content>
      <mat-card-actions>
        <a mat-button [routerLink]="['/sign-up']">Create account</a>
      </mat-card-actions>
      <mat-card-footer>
        @if(showLoader){
        <mat-progress-bar mode="indeterminate"></mat-progress-bar>
        }
      </mat-card-footer>
    </mat-card>
  `,
  styleUrl: './login.component.scss',
})
export class LoginComponent {
  private loginService = inject(LoginService);
  private authService = inject(AuthService);
  private router = inject(Router);
  private fb = inject(FormBuilder);
  private destroyRef = inject(DestroyRef);
  showLoader = false;
  loginForm = this.fb.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', Validators.required],
  });

  get email() {
    return this.loginForm.get('email');
  }
  get password() {
    return this.loginForm.get('password');
  }

  login() {
    if (this.loginForm.valid && this.loginForm.dirty) {
      this.showLoader = true;
      const loginCredentials: LoginCredentials = this.loginForm.getRawValue();
      this.loginService
        .login(loginCredentials)
        .pipe(
          takeUntilDestroyed(this.destroyRef),
          catchError((err) => {
            this.showLoader = false;
            return throwError(() => err);
          })
        )
        .subscribe(({ token }) => {
          this.showLoader = false;
          this.authService.decodeAndStoreTokenData(token);
          this.router.navigate(['/me/courses'], {
            queryParams: {
              type:
                this.authService.getUserRole() === 'ADMIN'
                  ? 'created'
                  : 'owned',
            },
          });
        });
    }
  }
  getEmailError() {
    if (this.email?.hasError('required')) {
      return 'E-mail is required';
    }
    return 'Field must be a valid e-mail';
  }
  getPasswordError() {
    return 'Password is required';
  }
}
