import { Quiz } from './Quiz';

export interface LessonDetail {
  title: string;
  description: string;
  video: string;
  quiz: Quiz;
}
