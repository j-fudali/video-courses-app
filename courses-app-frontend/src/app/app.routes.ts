import { Routes } from '@angular/router';
import { authGuard } from './shared/guards/auth.guard';
import { creatorGuard } from './shared/guards/creator.guard';
import { alreadyLoggedIn } from './shared/guards/alreadyLoggedIn';

export const routes: Routes = [
  {
    path: 'editor',
    loadChildren: () =>
      import(
        './course-creator/feature/course-creator-shell/course-creator.routes'
      ),
    canActivate: [authGuard, creatorGuard],
  },
  {
    path: 'purchase',
    loadChildren: () => import('./purchase/feature/purchase.routes'),
  },
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
