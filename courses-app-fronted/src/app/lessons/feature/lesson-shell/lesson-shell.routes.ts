import { Route } from '@angular/router';

export default [
  {
    path: '',
    loadComponent: () =>
      import('../lesson-detail/lesson-detail.component').then(
        (c) => c.LessonDetailComponent
      ),
  },
] as Route[];
