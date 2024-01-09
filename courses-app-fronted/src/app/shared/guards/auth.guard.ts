import { inject } from '@angular/core';
import { AuthService } from '../data-access/auth.service';
import { Router } from '@angular/router';

export const authGuard = () => {
  const authService = inject(AuthService);
  const router = inject(Router);
  authService.$isLoggedIn() ? true : router.navigate(['/login']);
};
