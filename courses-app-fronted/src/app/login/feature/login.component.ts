import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { LoginService } from '../data-access/login.service';
import { LoginCredentials } from '../../shared/interfaces/loginCredentials';
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
  ],
  template: ` <mat-card>
    <mat-card-header>
      <mat-card-title>Login</mat-card-title>
    </mat-card-header>
    <mat-card-content>
      <form [formGroup]="loginForm">
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
      </form>
    </mat-card-content>
    <mat-card-actions>
      <button
        (click)="login()"
        [disabled]="!loginForm.valid"
        mat-button
        type="button"
      >
        Login
      </button>
    </mat-card-actions>
    <mat-card-footer>
      <mat-progress-bar
        *ngIf="showLoader"
        mode="indeterminate"
      ></mat-progress-bar>
    </mat-card-footer>
  </mat-card>`,
  styles: `
    :host{
      display: block;
      height: 85vh;
      display: flex;
      justify-content: center;
      align-items: center
    }
    form{
      display: flex;
      flex-direction: column;
      gap: 5px;
    }
    button{
      width: 100%;
    }
  `,
})
export class LoginComponent {
  private loginService = inject(LoginService);
  private fb = inject(FormBuilder);
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
    this.showLoader = true;
    if (this.loginForm.valid && this.loginForm.dirty) {
      const loginCredentials: LoginCredentials = this.loginForm.getRawValue();
      this.loginService.login(loginCredentials).subscribe(({ token }) => {
        this.showLoader = false;
        console.log(token);
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
