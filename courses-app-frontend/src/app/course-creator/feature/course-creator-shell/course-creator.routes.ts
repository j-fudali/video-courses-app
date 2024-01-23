import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, ResolveFn, Route } from '@angular/router';
import { CoursesService } from '../../../courses/data-access/courses.service';
import { LessonsService } from '../../../lessons/data-access/lessons.service';
import { forkJoin, map, of, switchMap } from 'rxjs';
import { CourseWithLessons } from '../../../shared/interfaces/CourseWithLessons';

const fetchEntireCourse: ResolveFn<CourseWithLessons> = (
  route: ActivatedRouteSnapshot
) => {
  const coursesService = inject(CoursesService);
  const lessonsService = inject(LessonsService);
  const courseId = route.paramMap.get('courseId')!;
  return coursesService.getCourse(+courseId).pipe(
    switchMap(({ lessons, ...course }) =>
      lessons.length > 0
        ? forkJoin(
            lessons.map((l) =>
              lessonsService
                .getLesson(+courseId, l.idlesson)
                .pipe(map((lesson) => ({ idlesson: l.idlesson, ...lesson })))
            )
          ).pipe(
            map(
              (lessons) =>
                ({
                  course,
                  lessons,
                } as CourseWithLessons)
            )
          )
        : of({ course } as CourseWithLessons)
    )
  );
};

export default [
  {
    path: '',
    loadComponent: () =>
      import('../course-creator-editor/course-creator-editor.component').then(
        (c) => c.EditorComponent
      ),
  },
  {
    path: ':courseId/edit',
    loadComponent: () =>
      import('../course-creator-editor/course-creator-editor.component').then(
        (c) => c.EditorComponent
      ),
    resolve: {
      course: fetchEntireCourse,
    },
  },
] as Route[];
