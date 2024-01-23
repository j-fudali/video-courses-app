import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { environment } from '../../../environments/environment.development';
import { NewCourse } from '../../shared/interfaces/NewCourse';
import { NewLesson } from '../../shared/interfaces/NewLesson';
import { NewQuiz } from '../../shared/interfaces/NewQuiz';
import { QuestionToSet } from '../../shared/interfaces/QuestionToSet';
import { UpdateCourse } from '../../shared/interfaces/UpdateCourse';

@Injectable({
  providedIn: 'root',
})
export class CourseCreatorService {
  private http = inject(HttpClient);
  private baseUrl = environment.url + '/courses';

  createCourse(newCourse: NewCourse) {
    return this.http.post<{ courseId: number }>(this.baseUrl, newCourse);
  }
  updateCourse(courseId: number, updateCourse: UpdateCourse) {
    return this.http.patch(`${this.baseUrl}/${courseId}`, updateCourse);
  }

  createLesson(courseId: number, newLesson: NewLesson) {
    return this.http.post<{ lessonId: number }>(
      this.baseUrl + '/' + courseId + '/lessons',
      newLesson
    );
  }
  updateLesson(
    courseId: number,
    lessonId: number,
    updateLesson: Partial<NewLesson>
  ) {
    return this.http.patch(
      `${this.baseUrl}/${courseId}/lessons/${lessonId}`,
      updateLesson
    );
  }
  deleteLesson(courseId: number, lessonId: number) {
    return this.http.delete(`${this.baseUrl}/${courseId}/lessons/${lessonId}`);
  }
  createQuiz(courseId: number, lessonId: number, title: string) {
    return this.http.post(
      this.baseUrl + `/${courseId}/lessons/${lessonId}/quiz`,
      { title }
    );
  }
  updateQuiz(courseId: number, lessonId: number, title: string) {
    return this.http.patch(
      this.baseUrl + `/${courseId}/lessons/${lessonId}/quiz`,
      { title }
    );
  }
  deleteQuiz(courseId: number, lessonId: number) {
    console.log('quiz delete');
    return this.http.delete(
      `${this.baseUrl}/${courseId}/lessons/${lessonId}/quiz`
    );
  }
  setQuestions(courseId: number, lessonId: number, questions: QuestionToSet[]) {
    return this.http.put(
      this.baseUrl + `/${courseId}/lessons/${lessonId}/quiz/questions`,
      { questions }
    );
  }
  uploadVideo(video: File) {
    const formdata = new FormData();
    formdata.set('video', video);
    return this.http.post<{ url: string }>(
      environment.url + '/upload',
      formdata
    );
  }
}
