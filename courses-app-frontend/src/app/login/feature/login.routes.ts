import { Route } from '@angular/router';

export default [
  {
    path: '',
    loadComponent: () =>
      import('./login.component').then((c) => c.LoginComponent),
  },
] as Route[];
