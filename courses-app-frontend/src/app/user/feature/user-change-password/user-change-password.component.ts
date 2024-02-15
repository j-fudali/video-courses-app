import { CommonModule } from '@angular/common';
import { Component, DestroyRef, inject } from '@angular/core';
import { UserService } from '../../data-access/user.service';
import { Router } from '@angular/router';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SetNewPasswordComponent } from '../../../shared/ui/set-new-password/set-new-password.component';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { map } from 'rxjs';

@Component({
  selector: 'app-user-change-password',
  standalone: true,
  imports: [CommonModule, SetNewPasswordComponent],
  template: `<app-set-new-password
    [title]="'Change password'"
    class="form"
    (onSetPassword)="change($event)"
    [style.width]="(isXSm$ | async) ? '90%' : '45%'"
  ></app-set-new-password>`,
  styleUrl: './user-change-password.component.scss',
})
export class UserChangePasswordComponent {
  private userService = inject(UserService);
  private destroyRef = inject(DestroyRef);
  private snakcbar = inject(MatSnackBar);
  private router = inject(Router);
  private breakpointObs = inject(BreakpointObserver);
  isXSm$ = this.breakpointObs
    .observe([Breakpoints.XSmall])
    .pipe(map((v) => v.matches));

  change(password: string) {
    this.userService
      .changePassword(password)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(({ message }) => {
        this.snakcbar.open(message, 'X');
        this.router.navigate(['/me/profile']);
      });
  }
}
