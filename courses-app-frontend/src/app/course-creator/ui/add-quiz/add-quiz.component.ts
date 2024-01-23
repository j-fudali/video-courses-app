import { BreakpointState } from '@angular/cdk/layout';
import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  Output,
} from '@angular/core';
import {
  AbstractControl,
  FormArray,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
} from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatDividerModule } from '@angular/material/divider';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatTooltipModule } from '@angular/material/tooltip';

@Component({
  selector: 'app-add-quiz',
  standalone: true,
  imports: [
    CommonModule,
    MatExpansionModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatTooltipModule,
    ReactiveFormsModule,
    MatDividerModule,
    MatCheckboxModule,
  ],
  template: `
    <mat-expansion-panel class="add-quiz" hideToggle expanded>
      <mat-expansion-panel-header>
        <mat-panel-title>Create quiz</mat-panel-title>
      </mat-expansion-panel-header>
      <form [formGroup]="quizGroup">
        <mat-form-field>
          <mat-label>Quiz title</mat-label>
          <input type="text" matInput formControlName="title" />
        </mat-form-field>
        <div class="questions" formArrayName="questions">
          @for (question of questions.controls; track question; let questionIdx
          = $index) {
          <div class="question-container" [formGroupName]="questionIdx">
            <div class="question">
              <mat-form-field subscriptSizing="dynamic" appearance="outline">
                <mat-label>Question nr {{ questionIdx + 1 }}</mat-label>
                <input type="text" matInput formControlName="text" />
              </mat-form-field>
              <button
                (click)="removeQuestion(questionIdx)"
                mat-mini-fab
                color="warn"
                matTooltip="Remove question"
              >
                <mat-icon>remove</mat-icon>
              </button>
            </div>
            <div class="answers" formArrayName="answers">
              @for(answer of getAnswers(question).controls; track answer; let
              answerIdx = $index){
              <div class="answer" [formGroupName]="answerIdx">
                <mat-checkbox formControlName="isCorrect">
                  <mat-form-field
                    appearance="outline"
                    subscriptSizing="dynamic"
                    color="accent"
                  >
                    <mat-label>Answer nr {{ answerIdx + 1 }}</mat-label>
                    <input type="text" matInput formControlName="text" />
                    <button
                      mat-icon-button
                      matSuffix
                      type="button"
                      (click)="removeAnswer(question, answerIdx)"
                    >
                      <mat-icon>remove</mat-icon>
                    </button>
                  </mat-form-field>
                </mat-checkbox>
              </div>
              }
            </div>
          </div>
          <button
            type="button"
            class="add-answer"
            (click)="addAnswer(question)"
            matTooltip="Add answer"
            mat-mini-fab
            color="primary"
          >
            <mat-icon>add</mat-icon>
          </button>
          <mat-divider></mat-divider>
          }
        </div>
        <button
          (click)="addQuestion()"
          matTooltip="Add question"
          type="button"
          mat-mini-fab
        >
          <mat-icon>add</mat-icon>
        </button>
      </form>
    </mat-expansion-panel>
  `,
  styleUrl: './add-quiz.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AddQuizComponent {
  @Input({ required: true }) quizGroup: FormGroup;
  @Output() onAddQuestion = new EventEmitter<FormGroup>();
  @Output() onAddAnswer = new EventEmitter<FormGroup>();
  get questions() {
    return this.quizGroup.get('questions') as FormArray<FormGroup>;
  }
  getAnswers(question: FormGroup) {
    return question.get('answers') as FormArray<FormGroup>;
  }
  addQuestion() {
    this.onAddQuestion.emit(this.quizGroup);
  }
  removeQuestion(questionIdx: number) {
    this.quizGroup.markAsDirty();
    const questions = this.quizGroup.get('questions') as FormArray;
    questions.removeAt(questionIdx);
  }
  addAnswer(questionGroup: FormGroup) {
    this.onAddAnswer.emit(questionGroup);
  }
  removeAnswer(questionGroup: FormGroup, answerIdx: number) {
    this.quizGroup.get('questions')?.markAsDirty();
    const answers = questionGroup.get('answers') as FormArray;
    answers.removeAt(answerIdx);
  }
}
