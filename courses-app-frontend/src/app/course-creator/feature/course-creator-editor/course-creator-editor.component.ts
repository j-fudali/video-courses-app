import { CommonModule } from '@angular/common';
import {
  ChangeDetectorRef,
  Component,
  DestroyRef,
  OnInit,
  inject,
} from '@angular/core';
import {
  FormArray,
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { CategoriesService } from '../../../shared/data-access/categories.service';
import { CourseDetailsFormComponent } from '../../ui/course-details-form/course-details-form.component';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatExpansionModule } from '@angular/material/expansion';
import { AddLessonComponent } from '../../ui/add-lesson/add-lesson.component';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { minimumOneAnswerCorrect } from '../../util/minOneAnswerCorrect.validator';
import { NewCourse } from '../../../shared/interfaces/NewCourse';
import { NewLesson } from '../../../shared/interfaces/NewLesson';
import { CourseCreatorService } from '../../data-access/course-creator.service';
import { catchError, forkJoin, of, switchMap, throwError } from 'rxjs';
import { QuestionToSet } from '../../../shared/interfaces/QuestionToSet';
import { ActivatedRoute, Router } from '@angular/router';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { Category } from '../../../shared/interfaces/Category';
import { Lesson } from '../../../shared/interfaces/Lesson';
import { LessonDetail } from '../../../shared/interfaces/LessonDetail';
import { Quiz } from '../../../shared/interfaces/Quiz';
import { Question } from '../../../shared/interfaces/Question';
import { Answer } from '../../../shared/interfaces/Answer';
import { UpdateCourse } from '../../../shared/interfaces/UpdateCourse';
import { HttpErrorResponse } from '@angular/common/http';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

@Component({
  selector: 'app-editor',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    CourseDetailsFormComponent,
    MatButtonModule,
    MatIconModule,
    MatExpansionModule,
    AddLessonComponent,
    MatProgressSpinnerModule,
    MatSnackBarModule,
  ],
  templateUrl: './course-creator-editor.component.html',
  styleUrl: './course-creator-editor.component.scss',
})
export class EditorComponent implements OnInit {
  private categoriesService = inject(CategoriesService);
  private fb = inject(FormBuilder);
  private courseCreatorService = inject(CourseCreatorService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private destroyRef = inject(DestroyRef);
  private snackbar = inject(MatSnackBar);
  breakpointObs = inject(BreakpointObserver);
  small = Breakpoints.Small;
  isGtSm$ = this.breakpointObs.observe([
    Breakpoints.Small,
    Breakpoints.Medium,
    Breakpoints.Large,
    Breakpoints.XLarge,
  ]);
  loadingText: string | undefined = undefined;
  step = 0;
  categories$ = this.categoriesService.getCategories();
  newCourseForm = this.fb.nonNullable.group({
    courseDetailsGroup: this.fb.nonNullable.group({
      name: ['', [Validators.required, Validators.maxLength(45)]],
      cost: [0.01, [Validators.required, Validators.min(0.01)]],
      categoryId: [0, Validators.required],
      description: ['', [Validators.required, Validators.maxLength(500)]],
    }),
    lessons: this.fb.array<FormGroup>([], Validators.required),
  });
  editedCourse: number | undefined = undefined;
  lessonsToRemove: number[] = [];
  lessonIdOfQuizToRemove: number[] = [];
  ngOnInit(): void {
    if (this.router.url.endsWith('edit')) {
      const { course, lessons } = this.route.snapshot.data['course'];
      const { category, ...courseRest } = course;
      this.editedCourse = +this.route.snapshot.paramMap.get('courseId')!;
      this.newCourseForm.controls.courseDetailsGroup.patchValue({
        ...courseRest,
        categoryId: (category as Category).idcategory,
      });
      if (lessons.length > 0) {
        (lessons as (Lesson & LessonDetail)[]).forEach(
          (l: Lesson & LessonDetail, index) => {
            this.addLesson(l);
            if (l.quiz) {
              const lessonGroup = this.newCourseForm.controls.lessons.at(index);
              this.addQuiz(lessonGroup, l.quiz);
              l.quiz.questions.forEach((q, index) => {
                const quizGroup = lessonGroup.get('quiz') as FormGroup;
                this.addQuestion(quizGroup, q);
                const questionGroup = (
                  quizGroup.get('questions') as FormArray
                ).at(index) as FormGroup;
                q.answers.forEach((a: Answer) => {
                  this.addAnswer(questionGroup, a);
                });
              });
            }
          }
        );
      }
    }
  }

  get lessons() {
    return this.newCourseForm.get('lessons') as FormArray<FormGroup>;
  }
  setStep(index: number) {
    this.step = index;
  }
  nextStep() {
    this.step++;
  }
  previousStep() {
    this.step--;
  }
  addLesson(initialData?: Lesson & LessonDetail) {
    const lesson = this.fb.nonNullable.group({
      idlesson: this.fb.control<number | null>(null),
      title: this.fb.control('', {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      description: this.fb.control('', {
        validators: [Validators.required, Validators.maxLength(350)],
      }),
      video: this.fb.control<string | File | null>(null, Validators.required),
      editMode: this.fb.control<boolean>(false),
    });
    if (initialData) {
      lesson.patchValue(initialData);
      lesson.controls.editMode.setValue(true);
      lesson.controls.idlesson.setValue(initialData.idlesson);
    }
    this.newCourseForm.controls.lessons.push(lesson);
  }

  addQuiz(lesson: FormGroup, initialData?: Quiz) {
    const quiz = this.fb.nonNullable.group(
      {
        title: this.fb.control('', {
          validators: [Validators.required, Validators.maxLength(100)],
        }),
        questions: this.fb.array<FormGroup>([], Validators.required),
      },
      {
        validators: [Validators.required],
      }
    );
    if (initialData) quiz.patchValue(initialData);
    lesson.addControl('quiz', quiz);
  }

  addQuestion(quizGroup: FormGroup, initialData?: Question) {
    const questions = quizGroup.get('questions') as FormArray<FormGroup>;
    const question = this.fb.nonNullable.group({
      text: this.fb.control('', [
        Validators.required,
        Validators.maxLength(200),
      ]),
      answers: this.fb.array<FormGroup>([], {
        validators: [Validators.required, minimumOneAnswerCorrect],
      }),
    });
    if (initialData) question.patchValue(initialData);
    questions.push(question);
  }
  addAnswer(questionGroup: FormGroup, initialData?: Answer) {
    const answers = questionGroup.get('answers') as FormArray;
    const answer = this.fb.group({
      text: this.fb.control('', [
        Validators.required,
        Validators.maxLength(100),
      ]),
      isCorrect: this.fb.control(false, Validators.required),
    });
    if (initialData) answer.patchValue(initialData);
    answers.push(answer);
  }
  removeLesson(lessonId: number | undefined, lessonIdx: number) {
    this.newCourseForm.controls.lessons.markAsDirty();
    this.newCourseForm.controls.lessons.removeAt(lessonIdx);
    if (lessonId) this.lessonsToRemove.push(lessonId);
  }
  removeQuiz(lessonId: number) {
    this.lessonIdOfQuizToRemove.push(lessonId);
  }
  createCourse() {
    const newCourse = this.newCourseForm.value.courseDetailsGroup as NewCourse;
    const lessons = this.newCourseForm.value.lessons;
    let courseIdToRedirect: number | undefined = undefined;
    this.loadingText = 'Creating course';
    this.courseCreatorService
      .createCourse(newCourse)
      .pipe(
        takeUntilDestroyed(this.destroyRef),
        switchMap(({ courseId }) => {
          courseIdToRedirect = courseId;
          return forkJoin(
            lessons!.map((l) =>
              this.courseCreatorService.uploadVideo(l.video as File).pipe(
                switchMap(({ url }) =>
                  this.courseCreatorService.createLesson(courseId, {
                    title: l.title,
                    description: l.description,
                    video: url,
                  } as NewLesson)
                ),
                switchMap(({ lessonId }) =>
                  l.quiz
                    ? this.courseCreatorService
                        .createQuiz(courseId, lessonId, l.quiz.title as string)
                        .pipe(
                          switchMap(() =>
                            this.courseCreatorService.setQuestions(
                              courseId,
                              lessonId,
                              l.quiz.questions as QuestionToSet[]
                            )
                          )
                        )
                    : of(null)
                )
              )
            )
          );
        })
      )
      .subscribe(() => {
        this.router.navigate(['/courses', courseIdToRedirect]);
      });
  }
  saveChanges() {
    const courseDetailsChanges = this.newCourseForm.value
      .courseDetailsGroup as UpdateCourse;
    const lessons = this.newCourseForm.get('lessons') as FormArray<FormGroup>;
    this.loadingText = 'Editing course';

    const lessonsToRemove = this.lessonsToRemove.map((l) =>
      this.courseCreatorService.deleteLesson(this.editedCourse!, l)
    );
    const quizzesToRemove = this.lessonIdOfQuizToRemove.map((l) =>
      this.courseCreatorService.deleteQuiz(this.editedCourse!, l)
    );
    const courseUpdateCall = this.courseCreatorService.updateCourse(
      this.editedCourse!,
      courseDetailsChanges
    );
    const lessonsHttpCalls = forkJoin(
      lessons.controls.map((c) => {
        if (c.dirty) {
          const uploadedFile =
            c.get('video')?.dirty && c.get('video')?.value instanceof File
              ? this.courseCreatorService.uploadVideo(c.value.video as File)
              : undefined;
          return uploadedFile != undefined
            ? uploadedFile.pipe(
                switchMap(({ url }) =>
                  (c.value as Lesson & LessonDetail & { editMode: boolean })
                    .editMode
                    ? this.courseCreatorService
                        .updateLesson(
                          this.editedCourse!,
                          c.value.idlesson as number,
                          {
                            title: c.value.title,
                            description: c.value.description,
                            video: url,
                          }
                        )
                        .pipe(switchMap(() => this.setQuizRequests(c)))
                    : this.courseCreatorService
                        .createLesson(this.editedCourse!, {
                          title: c.value.title,
                          description: c.value.description,
                          video: url,
                        })
                        .pipe(
                          switchMap(({ lessonId }) => {
                            return c.get('quiz')
                              ? this.courseCreatorService
                                  .createQuiz(
                                    this.editedCourse!,
                                    lessonId,
                                    c.value.quiz.title as string
                                  )
                                  .pipe(
                                    switchMap(() =>
                                      this.courseCreatorService.setQuestions(
                                        this.editedCourse!,
                                        lessonId,
                                        c.value.quiz
                                          .questions as QuestionToSet[]
                                      )
                                    )
                                  )
                              : of(null);
                          })
                        )
                )
              )
            : this.courseCreatorService
                .updateLesson(this.editedCourse!, c.value.idlesson as number, {
                  title: c.value.title,
                  description: c.value.description,
                })
                .pipe(switchMap(() => this.setQuizRequests(c)));
        }
        return of(null);
      })
    );
    const requestsToSend = [];
    if (this.newCourseForm.controls.courseDetailsGroup.dirty) {
      requestsToSend.push(courseUpdateCall);
    }
    if (this.newCourseForm.controls.lessons.dirty) {
      requestsToSend.push(lessonsHttpCalls);
    }
    forkJoin([...requestsToSend, ...lessonsToRemove, ...quizzesToRemove])
      .pipe(
        takeUntilDestroyed(this.destroyRef),
        catchError((err) => {
          this.loadingText = undefined;
          return throwError(() => err);
        })
      )
      .subscribe(() => {
        this.loadingText = undefined;
        this.snackbar.open('Course has been updated', 'X', { duration: 5000 });
      });
  }
  private setQuizRequests(lessonGroup: FormGroup) {
    const quiz = lessonGroup.get('quiz');
    const quizRequests = [];
    if (quiz) {
      if (quiz.dirty) {
        if (quiz.get('title')?.dirty) {
          quizRequests.push(
            this.courseCreatorService.updateQuiz(
              this.editedCourse!,
              lessonGroup.value.idlesson,
              quiz.value.title
            )
          );
        }
        if (quiz.dirty && !quiz.get('title')?.dirty) {
          quizRequests.push(
            this.courseCreatorService.setQuestions(
              this.editedCourse!,
              lessonGroup.value.idlesson,
              quiz.value.questions as QuestionToSet[]
            )
          );
        }
        return forkJoin(quizRequests).pipe(
          catchError((err: HttpErrorResponse) =>
            err.status == 404
              ? this.courseCreatorService
                  .createQuiz(
                    this.editedCourse!,
                    lessonGroup.value.idlesson,
                    quiz.value.title
                  )
                  .pipe(
                    switchMap(() =>
                      this.courseCreatorService.setQuestions(
                        this.editedCourse!,
                        lessonGroup.value.idlesson,
                        quiz.value.questions as QuestionToSet[]
                      )
                    )
                  )
              : throwError(() => err)
          )
        );
      }
      return of(null);
    }
    return of(null);
  }
}
