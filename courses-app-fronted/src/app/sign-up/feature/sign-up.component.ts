import { BreakpointObserver } from '@angular/cdk/layout';
import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  inject,
} from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { passwordsMatchValidator } from '../util/passwordValidator';
import { MatButtonModule } from '@angular/material/button';
import { SignUpService } from '../data-access/sign-up.service';
import { SignUpCredentials } from '../../shared/interfaces/SignUpCredentials';
import { AuthService } from '../../shared/data-access/auth.service';
import { Router, RouterModule } from '@angular/router';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { catchError, throwError } from 'rxjs';

@Component({
  selector: 'app-sign-up',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatProgressBarModule,
    RouterModule,
  ],
  template: `
    <mat-card>
      <mat-card-header>
        <mat-card-title>Sign-up</mat-card-title>
      </mat-card-header>
      <mat-card-content>
        <form [formGroup]="signUpForm" (ngSubmit)="signUp()">
          <mat-form-field>
            <mat-label>First name</mat-label>
            <input type="text" matInput formControlName="firstname" />
            @if(firstname.hasError('required')){
            <mat-error>Firstname is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field>
            <mat-label>Last name</mat-label>
            <input type="text" matInput formControlName="lastname" />
            @if(lastname.hasError('required')){
            <mat-error>Lastname is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field>
            <mat-label>E-mail</mat-label>
            <input type="email" matInput formControlName="email" />
            <mat-error>
              @if(email.invalid){ @if(email.hasError('required')){ E-mail is
              required } @else { This is not a valid e-mail address } }
            </mat-error>
          </mat-form-field>
          <mat-form-field>
            <mat-label>Password</mat-label>
            <input type="password" matInput formControlName="password" />
            @if(password.hasError('required')){
            <mat-error>Password is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field>
            <mat-label>Repeat password</mat-label>
            <input type="password" matInput formControlName="rePassword" />
            <mat-error>
              @if(rePassword.invalid){ @if(password.touched &&
              rePassword.touched &&rePassword.hasError('notSame')){ Passwords
              not match } @else{ You have to repeat password } }
            </mat-error>
          </mat-form-field>
          <button
            mat-raised-button
            type="submit"
            color="primary"
            [disabled]="signUpForm.invalid"
          >
            Sign-up
          </button>
        </form>
      </mat-card-content>
      <mat-card-actions>
        <a mat-button [routerLink]="['/login']">Log-in</a>
      </mat-card-actions>
      <mat-card-footer>
        @if(showLoading){
        <mat-progress-bar mode="indeterminate"></mat-progress-bar>
        }
      </mat-card-footer>
    </mat-card>
  `,
  styleUrl: './sign-up.component.scss',
})
export class SignUpComponent {
  private fb = inject(FormBuilder);
  private signUpService = inject(SignUpService);
  private authService = inject(AuthService);
  private router = inject(Router);
  private destroyRef = inject(DestroyRef);
  showLoading = false;
  signUpForm = this.fb.group({
    firstname: ['', Validators.required],
    lastname: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    password: ['', Validators.required],
    rePassword: ['', [Validators.required, passwordsMatchValidator()]],
  });

  get firstname() {
    return this.signUpForm.get('firstname')!;
  }
  get lastname() {
    return this.signUpForm.get('lastname')!;
  }
  get email() {
    return this.signUpForm.get('email')!;
  }
  get password() {
    return this.signUpForm.get('password')!;
  }
  get rePassword() {
    return this.signUpForm.get('rePassword')!;
  }
  signUp() {
    if (this.signUpForm.dirty && this.signUpForm.valid) {
      this.showLoading = true;
      const credentials: SignUpCredentials = {
        firstname: this.firstname.value!,
        lastname: this.lastname.value!,
        email: this.email.value!,
        password: this.password.value!,
      };
      this.signUpService
        .signUp(credentials)
        .pipe(
          takeUntilDestroyed(this.destroyRef),
          catchError((err) => {
            this.showLoading = false;
            return throwError(() => err);
          })
        )
        .subscribe(({ token }) => {
          this.showLoading = false;
          this.authService.decodeAndStoreTokenData(token);
          this.router.navigate(['/']);
        });
    }
  }
}
