import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { LessonDetail } from '../../shared/interfaces/LessonDetail';
import { UserQuestionAnswers } from '../../shared/interfaces/UserQuizAnswers';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class LessonsService {
  private http = inject(HttpClient);
  private baseUrl = environment.url + '/courses';
  getLesson(courseId: number, lessonId: number) {
    return this.http.get<LessonDetail>(
      `${this.baseUrl}/${courseId}/lessons/${lessonId}`
    );
  }
  verifyQuiz(
    courseId: number,
    lessonId: number,
    userQuestionsAnswers: UserQuestionAnswers[]
  ) {
    return this.http.post<{ completed: boolean }>(
      this.baseUrl + `/${courseId}/lessons/${lessonId}/quiz/verify`,
      {
        userQuestionsAnswers,
      }
    );
  }
}
