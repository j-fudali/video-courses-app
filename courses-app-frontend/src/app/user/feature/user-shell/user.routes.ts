import { Route } from '@angular/router';

export default [
  {
    path: 'courses',
    loadComponent: () =>
      import('../user-courses/user-courses.component').then(
        (c) => c.MyCoursesComponent
      ),
  },
] as Route[];
