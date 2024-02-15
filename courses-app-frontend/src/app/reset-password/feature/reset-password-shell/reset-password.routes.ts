import { Route } from '@angular/router';

export default [
  {
    path: '',
    loadComponent: () =>
      import('../reset-password/reset-password.component').then(
        (c) => c.ResetPasswordComponent
      ),
  },
  {
    path: 'confirm',
    loadComponent: () =>
      import('../reset-password-confirm/reset-password-confirm.component').then(
        (c) => c.ResetPasswordConfirmComponent
      ),
  },
] as Route[];
