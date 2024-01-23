import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  OnInit,
  Output,
} from '@angular/core';
import { MatStepperModule } from '@angular/material/stepper';
import { Quiz } from '../../../shared/interfaces/Quiz';
import { MatCardModule } from '@angular/material/card';
import { MatListModule, MatSelectionListChange } from '@angular/material/list';
import {
  MatCheckboxChange,
  MatCheckboxModule,
} from '@angular/material/checkbox';
import {
  AbstractControl,
  FormArray,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { UserQuestionAnswers } from '../../../shared/interfaces/UserQuizAnswers';
import { requriedAnswersSeleceted } from '../../util/requiredAnswersSelected.validator';
import { HttpErrorMessage } from '../../../shared/interfaces/HttpErrorMessage';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-quiz',
  standalone: true,
  imports: [
    CommonModule,
    MatStepperModule,
    MatCardModule,
    MatListModule,
    MatButtonModule,
    ReactiveFormsModule,
    MatCheckboxModule,
    MatIconModule,
  ],
  template: `
    <mat-card>
      <mat-card-header>
        <h2>
          {{ quiz.title }}
        </h2>
      </mat-card-header>
      <mat-card-content>
        <form [formGroup]="form" (ngSubmit)="checkAnswers()">
          <mat-vertical-stepper formArrayName="userQuestionsAnswers" linear>
            @for(question of quiz.questions; track question.idquestion; let i =
            $index, last = $last, first = $first){
            <mat-step
              label="Question {{ i + 1 }}"
              [formGroupName]="i"
              [stepControl]="getSelectedAnswers(i)"
              errorMessage="No answer selected"
            >
              <mat-card>
                <mat-card-header>
                  <h4>{{ question.text }}</h4>
                </mat-card-header>
                <mat-card-content formArrayName="selectedAnswers">
                  @for(answer of question.answers; track answer; let answerIdx =
                  $index){
                  <form [formGroupName]="answerIdx">
                    <mat-checkbox formControlName="checked">{{
                      answer.text
                    }}</mat-checkbox>
                  </form>
                  }
                </mat-card-content>
                <mat-card-actions>
                  @if(!last){
                  <button class="next" matStepperNext mat-button type="button">
                    Next
                  </button>
                  } @if(!first){
                  <button
                    class="previous"
                    matStepperPrevious
                    mat-button
                    type="button"
                  >
                    Previous
                  </button>
                  }
                </mat-card-actions>
              </mat-card>
            </mat-step>

            }
          </mat-vertical-stepper>
          <button
            [disabled]="form.invalid"
            type="submit"
            mat-raised-button
            color="primary"
          >
            Check answers
          </button>
        </form>
      </mat-card-content>
      @if(completed != undefined){
      <mat-card-footer>
        <p>
          @if(completed === true){
          <mat-icon>check_circle</mat-icon>
          } @else {
          <mat-icon>check_circle_outline</mat-icon>
          }
          <span [style.color]="completed ? 'green' : 'red'">{{
            completed
              ? 'Congrats! You passed the quiz!'
              : 'Quiz is not completed, please try again'
          }}</span>
        </p>
      </mat-card-footer>
      }
    </mat-card>
  `,
  styleUrl: './quiz.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class QuizComponent implements OnInit {
  @Input({ required: true }) quiz: Quiz;
  @Input() completed: boolean | undefined;
  @Output() resultsEmit = new EventEmitter<UserQuestionAnswers[]>();
  form: FormGroup<{
    userQuestionsAnswers: FormArray<
      FormGroup<{
        questionId: FormControl<number>;
        selectedAnswers: FormArray<
          FormGroup<{
            answerId: FormControl<number>;
            checked: FormControl<boolean>;
          }>
        >;
      }>
    >;
  }>;

  ngOnInit(): void {
    this.form = new FormGroup({
      userQuestionsAnswers: new FormArray(
        this.quiz.questions.map((q) => {
          const group = new FormGroup({
            questionId: new FormControl(q.idquestion, { nonNullable: true }),
            selectedAnswers: new FormArray(
              q.answers.map(
                (a) =>
                  new FormGroup(
                    {
                      answerId: new FormControl(a.idanswer, {
                        nonNullable: true,
                      }),
                      checked: new FormControl(false, {
                        nonNullable: true,
                      }),
                    },
                    { validators: [Validators.required] }
                  )
              ),
              { validators: [requriedAnswersSeleceted(1)] }
            ),
          });
          return group;
        })
      ),
    });
  }
  get questions(): FormArray {
    return this.form.get('userQuestionsAnswers') as FormArray;
  }
  getQuestion(index: number) {
    return this.questions.at(index) as FormGroup;
  }
  getSelectedAnswers(questionIdx: number) {
    return this.getQuestion(questionIdx).get('selectedAnswers') as FormArray;
  }

  checkAnswers() {
    const results: UserQuestionAnswers[] = this.form
      .getRawValue()
      .userQuestionsAnswers.map((q) => {
        const selectedAnswers = q.selectedAnswers
          .filter((a) => a.checked)
          .map((v) => v.answerId);
        return {
          questionId: q.questionId,
          selectedAnswers,
        };
      });
    this.resultsEmit.emit(results);
  }
}
