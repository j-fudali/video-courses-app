import { inject } from '@angular/core';
import { AuthService } from '../data-access/auth.service';
import { UserRole } from '../util/roles.enum';
import { Router } from '@angular/router';

export const creatorGuard = () => {
  const authService = inject(AuthService);
  const router = inject(Router);
  return authService.getUserRole() &&
    authService.getUserRole() === UserRole.Admin
    ? true
    : router.navigate(['/']);
};
