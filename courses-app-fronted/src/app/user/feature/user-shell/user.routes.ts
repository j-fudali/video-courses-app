import { Route } from '@angular/router';

export default [
  {
    path: 'courses',
    loadComponent: () =>
      import('../user-courses/user-courses.component').then(
        (c) => c.MyCoursesComponent
      ),
  },
  {
    path: 'profile',
    loadComponent: () =>
      import('../user-profile/user-profile.component').then(
        (c) => c.UserProfileComponent
      ),
  },
] as Route[];
