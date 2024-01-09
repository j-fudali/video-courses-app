import { inject } from '@angular/core';
import { Router, Routes } from '@angular/router';
import { AuthService } from './shared/data-access/auth.service';
import { authGuard } from './shared/guards/auth.guard';

const alreadyLoggedIn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);
  return authService.$isLoggedIn() ? router.navigate(['..']) : true;
};
export const routes: Routes = [
  {
    path: 'home',
    loadChildren: () => import('./home/feature/home-page.routes'),
  },
  {
    path: 'courses',
    loadChildren: () =>
      import('./courses/feature/course-shell/course-shell.routes'),
  },
  {
    path: 'me',
    loadChildren: () => import('./user/feature/user-shell/user.routes'),
    canActivate: [authGuard],
  },
  {
    path: 'login',
    loadChildren: () => import('./login/feature/login.routes'),
    canActivate: [alreadyLoggedIn],
  },
  {
    path: 'sign-up',
    loadChildren: () => import('./sign-up/feature/sign-up.routes'),
    canActivate: [alreadyLoggedIn],
  },
  { path: '', redirectTo: '/home', pathMatch: 'full' },
];
