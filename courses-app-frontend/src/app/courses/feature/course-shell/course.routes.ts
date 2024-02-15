import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, ResolveFn, Route } from '@angular/router';
import { UserService } from '../../../user/data-access/user.service';
import { CourseDetail } from '../../../shared/interfaces/CourseDetail';
import { EMPTY, catchError, combineLatest, map, of, tap } from 'rxjs';
import { AuthService } from '../../../shared/data-access/auth.service';
import { CoursesService } from '../../data-access/courses.service';
import { authGuard } from '../../../shared/guards/auth.guard';

export const resolveCourseDetails: ResolveFn<CourseDetail> = (
  route: ActivatedRouteSnapshot
) => {
  const coursesService = inject(CoursesService);
  return coursesService.getCourse(+route.paramMap.get('courseId')!);
};

export default [
  {
    path: '',
    loadComponent: () =>
      import('../course-list/course-list.component').then(
        (c) => c.CoursesListComponent
      ),
  },
  {
    path: ':courseId',
    loadComponent: () =>
      import('../course-detail/course-detail.component').then(
        (c) => c.CourseDetailComponent
      ),
    resolve: {
      data: resolveCourseDetails,
    },
  },
  {
    path: ':courseId/lessons/:lessonId',
    loadChildren: () =>
      import('../../../lessons/feature/lesson-shell/lesson.routes'),
    canActivate: [authGuard],
  },
] as Route[];
