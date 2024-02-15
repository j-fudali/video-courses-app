import { Route } from '@angular/router';

export default [
  {
    path: '',
    loadComponent: () =>
      import('./page-not-found.component').then((c) => c.PageNotFoundComponent),
  },
] as Route[];
