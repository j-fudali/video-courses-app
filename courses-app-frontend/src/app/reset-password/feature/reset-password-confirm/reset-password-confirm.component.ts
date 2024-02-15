import { CommonModule } from '@angular/common';
import { Component, DestroyRef, inject } from '@angular/core';
import { SetNewPasswordComponent } from '../../../shared/ui/set-new-password/set-new-password.component';
import { ResetPasswordService } from '../../data-access/reset-password.service';
import { ActivatedRoute, Router } from '@angular/router';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { map } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

@Component({
  selector: 'app-reset-password-confirm',
  standalone: true,
  imports: [CommonModule, SetNewPasswordComponent, MatSnackBarModule],
  template: `
    <app-set-new-password
      class="form"
      [title]="'Set new password'"
      (onSetPassword)="setPassword($event)"
      [style.width]="(isXSm$ | async) ? '90%' : '45%'"
    ></app-set-new-password>
  `,
  styleUrl: './reset-password-confirm.component.scss',
})
export class ResetPasswordConfirmComponent {
  private resetPasswordService = inject(ResetPasswordService);
  private breakpointObs = inject(BreakpointObserver);
  private route = inject(ActivatedRoute);
  private destroyRef = inject(DestroyRef);
  private snackbar = inject(MatSnackBar);
  private router = inject(Router);
  isXSm$ = this.breakpointObs
    .observe([Breakpoints.XSmall])
    .pipe(map((v) => v.matches));
  setPassword(password: string) {
    const token = this.route.snapshot.queryParamMap.get('token') as string;
    this.resetPasswordService
      .resetPasswordConfirm(token, password)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(({ message }) => {
        this.router.navigate(['/login']);
        this.snackbar.open(message);
      });
  }
}
