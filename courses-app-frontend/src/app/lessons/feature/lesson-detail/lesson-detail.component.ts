import { CommonModule } from '@angular/common';
import { Component, Input, OnInit, inject } from '@angular/core';
import { Observable, catchError, ignoreElements, of, tap } from 'rxjs';
import { LessonDetail } from '../../../shared/interfaces/LessonDetail';
import { LessonsService } from '../../data-access/lessons.service';
import { MatDividerModule } from '@angular/material/divider';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { environment } from '../../../../environments/environment.development';
import { QuizComponent } from '../../ui/quiz/quiz.component';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { UserQuestionAnswers } from '../../../shared/interfaces/UserQuizAnswers';
import { HttpErrorResponse } from '@angular/common/http';
import { HttpErrorMessage } from '../../../shared/interfaces/HttpErrorMessage';
@Component({
  selector: 'app-lesson-detail',
  standalone: true,
  imports: [
    CommonModule,
    MatDividerModule,
    MatButtonModule,
    MatIconModule,
    RouterModule,
    QuizComponent,
  ],
  templateUrl: './lesson-detail.component.html',
  styleUrl: './lesson-detail.component.scss',
})
export class LessonDetailComponent implements OnInit {
  @Input() lessonId!: number;
  @Input() courseId!: number;
  private lessonsService = inject(LessonsService);
  private breakpointObs = inject(BreakpointObserver);
  isGtSm$ = this.breakpointObs.observe([
    Breakpoints.Medium,
    Breakpoints.Large,
    Breakpoints.XLarge,
  ]);
  videoUrl = environment.videoUrl;
  lesson$: Observable<LessonDetail>;
  hasVideoError = false;

  passedQuiz$: Observable<{ completed: boolean }>;
  ngOnInit(): void {
    this.lesson$ = this.lessonsService.getLesson(this.courseId, this.lessonId);
  }
  showVideoError() {
    this.hasVideoError = true;
  }
  verifyResults(results: UserQuestionAnswers[]) {
    this.passedQuiz$ = this.lessonsService.verifyQuiz(
      this.courseId,
      this.lessonId,
      results
    );
  }
}
