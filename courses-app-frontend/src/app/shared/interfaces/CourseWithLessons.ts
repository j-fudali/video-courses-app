import { CourseDetail } from './CourseDetail';
import { Lesson } from './Lesson';
import { LessonDetail } from './LessonDetail';

export interface CourseWithLessons {
  course: Omit<CourseDetail, 'lessons'>;
  lessons: (Lesson & LessonDetail)[];
}
