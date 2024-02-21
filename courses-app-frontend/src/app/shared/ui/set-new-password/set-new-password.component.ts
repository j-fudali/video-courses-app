import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  OnInit,
  Output,
  inject,
} from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { DirtyTouchedErrorStateMatcher } from '../../util/DirtyTouchedErrorStateMatcher';
import { confirmPasswordValidator } from '../../util/passwordValidator';

@Component({
  selector: 'app-set-new-password',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatSnackBarModule,
  ],
  template: `
    <mat-card>
      <form [formGroup]="setPasswordForm" (ngSubmit)="submit()">
        <mat-card-header>
          <h2 mat-card-title>{{ title }}</h2>
        </mat-card-header>
        <mat-card-content>
          @if(showOldPasswordField){
          <mat-form-field>
            <mat-label>Old password</mat-label>
            <input type="password" matInput formControlName="oldPassword" />
            @if(oldPassword.invalid){
            <mat-error> Old password is required </mat-error>
            }
          </mat-form-field>

          }
          <mat-form-field>
            <mat-label>New password</mat-label>
            <input type="password" matInput formControlName="newPassword" />
            @if(newPassword.invalid){
            <mat-error>
              @if(newPassword.hasError('required')){ New password is required }
              @else{ New password requires min. 8 characters: 1 letter, 1 number
              and 1 special character }
            </mat-error>
            }
          </mat-form-field>
          <mat-form-field>
            <mat-label>Repeat new password</mat-label>
            <input
              type="password"
              matInput
              formControlName="reNewPassword"
              [errorStateMatcher]="matcher"
            />
            @if(setPasswordForm.invalid){<mat-error>
              @if(setPasswordForm.hasError('notSame')){ Passwords not match }
              @if(repeatNewPassword.hasError('required') &&
              !setPasswordForm.hasError('notSame')){ You have to repeat new
              password } </mat-error
            >}
          </mat-form-field>
        </mat-card-content>
        <mat-card-actions>
          <button type="submit" mat-raised-button color="primary">
            Change
          </button>
        </mat-card-actions>
      </form>
    </mat-card>
  `,
  styleUrl: './set-new-password.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SetNewPasswordComponent implements OnInit {
  @Input({ required: true }) title: string;
  @Input() showOldPasswordField = false;
  @Output() onSetPassword = new EventEmitter<{
    newPassword: string;
    oldPassword?: string;
  }>();
  private fb = inject(FormBuilder);
  matcher = new DirtyTouchedErrorStateMatcher();
  setPasswordForm: FormGroup;
  ngOnInit(): void {
    this.setPasswordForm = this.fb.nonNullable.group(
      {
        newPassword: [
          '',
          [
            Validators.required,
            Validators.pattern(
              /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$/
            ),
          ],
        ],
        reNewPassword: ['', [Validators.required]],
      },
      { validators: confirmPasswordValidator('newPassword', 'reNewPassword') }
    );
    if (this.showOldPasswordField) {
      this.setPasswordForm.addControl(
        'oldPassword',
        this.fb.control('', Validators.required)
      );
    }
  }
  get oldPassword() {
    return this.setPasswordForm.get('oldPassword') as FormControl;
  }
  get newPassword() {
    return this.setPasswordForm.get('newPassword') as FormControl;
  }
  get repeatNewPassword() {
    return this.setPasswordForm.get('reNewPassword') as FormControl;
  }
  submit() {
    if (this.setPasswordForm.valid && this.setPasswordForm.dirty) {
      this.onSetPassword.emit({
        newPassword: this.setPasswordForm.get('newPassword')?.value,
        oldPassword: this.setPasswordForm.get('oldPassword')?.value,
      });
    }
  }
}
