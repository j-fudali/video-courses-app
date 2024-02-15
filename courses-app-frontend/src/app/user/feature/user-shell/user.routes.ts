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
  {
    path: 'change-password',
    loadComponent: () =>
      import('../user-change-password/user-change-password.component').then(
        (c) => c.UserChangePasswordComponent
      ),
  },
] as Route[];
