import { Route } from '@angular/router';

export default [
  {
    path: '',
    loadComponent: () =>
      import('./purchase.component').then((c) => c.PurchaseComponent),
  },
] as Route[];
