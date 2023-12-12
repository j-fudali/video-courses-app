import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: 'home',
    loadChildren: () => import('./home/feature/home-page.routes'),
  },
  {
    path: 'login',
    loadChildren: () => import('./login/feature/login.routes'),
  },
  { path: '', redirectTo: '/home', pathMatch: 'full' },
];
