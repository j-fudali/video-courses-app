import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../data-access/auth.service';

export const alreadyLoggedIn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);
  return authService.$isLoggedIn() ? router.navigate(['..']) : true;
};
