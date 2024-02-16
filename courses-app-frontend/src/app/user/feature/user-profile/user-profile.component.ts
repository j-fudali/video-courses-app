import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  OnInit,
  inject,
} from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { UserService } from '../../data-access/user.service';
import { Observable, tap } from 'rxjs';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    ReactiveFormsModule,
    RouterModule,
  ],
  template: `
    @if(userProfile$ | async; as userProfile){
    <mat-card [formGroup]="profileForm">
      <mat-card-header>
        <h2 mat-card-title>Profile</h2>
        <h3 mat-card-subtitle>{{ userProfile.email }}</h3>
      </mat-card-header>
      <mat-card-content>
        <mat-form-field>
          <mat-label>First name</mat-label>
          <input matInput type="text" formControlName="firstname" />
          @if(firstname.invalid){
          <mat-error>
            @if(firstname.hasError('required')){ First name is required } @else{
            First name length is max. 70 characters } </mat-error
          >}
        </mat-form-field>
        <mat-form-field>
          <mat-label>Last name</mat-label>
          <input matInput type="text" formControlName="lastname" />
          @if(lastname.invalid){
          <mat-error>
            @if(lastname.hasError('required')){ Last name is required } @else{
            Last name length is max. 100 characters } </mat-error
          >}
        </mat-form-field>
        @if(successfulUpdate$ | async; as successfulUpdate){

        <p class="successful-update">{{ successfulUpdate.message }}</p>
        }
      </mat-card-content>
      <mat-card-actions>
        <button mat-raised-button (click)="update()" color="primary">
          Update
        </button>
        <a routerLink="/me/change-password" mat-stroked-button type="button"
          >Change password</a
        >
      </mat-card-actions>
    </mat-card>
    }
  `,
  styleUrl: './user-profile.component.scss',
})
export class UserProfileComponent {
  private fb = inject(FormBuilder);
  private userService = inject(UserService);
  successfulUpdate$: Observable<{ message: string }>;
  userProfile$ = this.userService.getUserProfile().pipe(
    tap(({ firstname, lastname }) => {
      this.profileForm.controls.firstname.setValue(firstname),
        this.profileForm.controls.lastname.setValue(lastname);
    })
  );
  profileForm = this.fb.group({
    firstname: ['', [Validators.required, Validators.maxLength(70)]],
    lastname: ['', [Validators.required, Validators.maxLength(100)]],
  });

  get firstname() {
    return this.profileForm.controls.firstname;
  }
  get lastname() {
    return this.profileForm.controls.lastname;
  }
  update() {
    if (this.profileForm.valid && this.profileForm.dirty) {
      const firstname = this.profileForm.controls.firstname;
      const lastname = this.profileForm.controls.lastname;
      this.successfulUpdate$ = this.userService.updateUserProfle(
        firstname.dirty && firstname.value
          ? firstname.value[0].toUpperCase() + firstname.value.substring(1)
          : null,
        lastname.dirty && lastname.value
          ? lastname.value[0].toUpperCase() + lastname.value.substring(1)
          : null
      );
    }
  }
}
