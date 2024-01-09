import { Route } from '@angular/router';

export default [
  {
    path: '',
    loadComponent: () =>
      import('./sign-up.component').then((c) => c.SignUpComponent),
  },
] as Route[];
