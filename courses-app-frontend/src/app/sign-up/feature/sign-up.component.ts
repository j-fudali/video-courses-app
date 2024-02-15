import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
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
import { MatButtonModule } from '@angular/material/button';
import { SignUpService } from '../data-access/sign-up.service';
import { SignUpCredentials } from '../../shared/interfaces/SignUpCredentials';
import { AuthService } from '../../shared/data-access/auth.service';
import { Router, RouterModule } from '@angular/router';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { catchError, map, throwError } from 'rxjs';
import { confirmPasswordValidator } from '../../shared/util/passwordValidator';
import { DirtyTouchedErrorStateMatcher } from '../../shared/util/DirtyTouchedErrorStateMatcher';

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
    <mat-card [style.width]="(isGtSm$ | async) ? '50%' : '90%'">
      <mat-card-header>
        <mat-card-title>Sign-up</mat-card-title>
      </mat-card-header>
      <mat-card-content>
        <form [formGroup]="signUpForm" (ngSubmit)="signUp()">
          <mat-form-field>
            <mat-label>First name</mat-label>
            <input type="text" matInput formControlName="firstname" />
            @if(firstname.hasError('required')){
            <mat-error>First name is required</mat-error>
            }@else{
            <mat-error>First name length is max. 70 characters</mat-error>
            }
          </mat-form-field>
          <mat-form-field>
            <mat-label>Last name</mat-label>
            <input type="text" matInput formControlName="lastname" />
            @if(lastname.hasError('required')){
            <mat-error>Last name is required</mat-error>
            } @else{
            <mat-error>Last name length is max. 100 characters</mat-error>

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
            @if(password.invalid){
            <mat-error>
              @if(password.hasError('required')){ Password is required} @else{
              Password requires min. 8 characters: 1 letter, 1 number and 1
              special character }
            </mat-error>
            }
          </mat-form-field>
          <mat-form-field>
            <mat-label>Repeat password</mat-label>
            <input
              type="password"
              matInput
              formControlName="rePassword"
              [errorStateMatcher]="matcher"
            />
            @if(signUpForm.invalid){<mat-error>
              @if(signUpForm.hasError('notSame')){ Passwords not match }
              @if(rePassword.hasError('required') &&
              !signUpForm.hasError('notSame')){ You have to repeat password } </mat-error
            >}
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
  private breakpointObs = inject(BreakpointObserver);
  matcher = new DirtyTouchedErrorStateMatcher();
  isGtSm$ = this.breakpointObs
    .observe([Breakpoints.Medium, Breakpoints.Large, Breakpoints.XLarge])
    .pipe(map((bp) => bp.matches));
  showLoading = false;
  signUpForm = this.fb.nonNullable.group(
    {
      firstname: ['', [Validators.required, Validators.maxLength(70)]],
      lastname: ['', [Validators.required, Validators.maxLength(100)]],
      email: ['', [Validators.required, Validators.email]],
      password: [
        '',
        [
          Validators.required,
          Validators.pattern(
            /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$/
          ),
        ],
      ],
      rePassword: ['', [Validators.required]],
    },
    { validators: confirmPasswordValidator('password', 'rePassword') }
  );

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
