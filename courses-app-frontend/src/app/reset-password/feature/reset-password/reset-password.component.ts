import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { FormControl, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { ResetPasswordService } from '../../data-access/reset-password.service';
import { Observable, tap } from 'rxjs';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-reset-password',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
  ],
  template: `
    <mat-card>
      <mat-card-header>
        <h2 mat-card-title>Reset password</h2>
      </mat-card-header>
      <mat-card-content>
        <mat-form-field>
          <mat-label>E-mail</mat-label>
          <input type="email" matInput [formControl]="email" />
          @if(email.invalid){
          <mat-error>
            @if(email.hasError('required')){ E-mail is required } @else{ This is
            not a valid e-mail }
          </mat-error>
          }
        </mat-form-field>
        @if(resetPasswordMessage$ | async; as resetPasswordMessage){
        <p>{{ resetPasswordMessage.message }}</p>
        }
      </mat-card-content>
      <mat-card-actions>
        <button
          [disabled]="showLoader || resetPasswordMessage$"
          mat-raised-button
          color="primary"
          (click)="resetPassword()"
        >
          @if(showLoader){
          <mat-spinner [diameter]="25" mode="indeterminate"></mat-spinner>
          }@else {@if(!resetPasswordMessage$){Send}@else{Sended!}}
        </button>
      </mat-card-actions>
    </mat-card>
  `,
  styleUrl: './reset-password.component.scss',
})
export class ResetPasswordComponent {
  private resetPasswordService = inject(ResetPasswordService);
  email = new FormControl('', {
    nonNullable: true,
    validators: [Validators.required, Validators.email],
  });
  resetPasswordMessage$: Observable<{ message: string }>;
  showLoader = false;
  resetPassword() {
    if (this.email.valid && (this.email.dirty || this.email.touched)) {
      this.showLoader = true;
      this.resetPasswordMessage$ = this.resetPasswordService
        .resetPassword(this.email.value)
        .pipe(tap(() => (this.showLoader = false)));
    }
  }
}
