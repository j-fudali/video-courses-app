import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  OnInit,
  Output,
} from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { AddQuizComponent } from '../add-quiz/add-quiz.component';
import { BreakpointState } from '@angular/cdk/layout';
import { environment } from '../../../../environments/environment.development';

@Component({
  selector: 'app-add-lesson',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatExpansionModule,
    MatIconModule,
    MatButtonModule,
    AddQuizComponent,
  ],
  template: `
    <mat-expansion-panel hideToggle>
      <mat-expansion-panel-header>
        <mat-panel-title>{{ title?.value || 'Lesson title' }}</mat-panel-title>
      </mat-expansion-panel-header>
      <form [formGroup]="lessonGroup">
        <div
          class="upload-video"
          [style.flexDirection]="isGtXs ? 'row' : 'column'"
          [style.alignItems]="isGtXs ? 'center' : 'flex-start'"
          [style.justifyContent]="isGtXs ? 'space-between' : 'flex-start'"
        >
          <button
            type="button"
            mat-fab
            extended
            color="primary"
            (click)="file.click()"
          >
            <mat-icon>attach_file</mat-icon>Upload video
          </button>
          <input (change)="createPreview($event)" #file type="file" />
          @if(video){
          <div class="video-wrapper" [style.width]="isGtXs ? '60%' : '100%'">
            <video
              (contextmenu)="(false)"
              controls
              [controlsList]="'nodownload'"
              [src]="video"
            ></video>
          </div>
          }
        </div>
        <mat-form-field>
          <mat-label>Lesson title</mat-label>
          <input type="text" matInput formControlName="title" />
        </mat-form-field>
        <mat-form-field>
          <mat-label>Lesson description</mat-label>
          <textarea matInput formControlName="description"></textarea>
        </mat-form-field>
        @if(lessonGroup.get('quiz')){
        <app-add-quiz
          (onAddQuestion)="addQuestion($event)"
          (onAddAnswer)="addAnswer($event)"
          [quizGroup]="quiz"
        ></app-add-quiz>
        }
      </form>
      <mat-action-row class="buttons">
        @if(quizButtonToggle){
        <button color="warn" mat-fab extended (click)="removeQuiz()">
          <mat-icon>remove</mat-icon>Remove quiz
        </button>
        }@else {
        <button mat-fab extended (click)="addQuiz()">
          <mat-icon>add</mat-icon>Add quiz
        </button>
        }
        <button mat-fab extended color="warn" (click)="removeLesson()">
          <mat-icon>remove</mat-icon>Remove lesson
        </button>
      </mat-action-row>
    </mat-expansion-panel>
  `,
  styleUrl: './add-lesson.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AddLessonComponent implements OnInit {
  @Input() lessonId: number;
  @Input({ required: true }) lessonGroup: FormGroup;
  @Input({ required: true }) isGtXs: boolean;
  @Output() onAddQuiz = new EventEmitter<FormGroup>();
  @Output() onAddQuestion = new EventEmitter<FormGroup>();
  @Output() onAddAnswer = new EventEmitter<FormGroup>();
  @Output() onRemoveLesson = new EventEmitter<number>();
  @Output() onRemoveQuiz = new EventEmitter<number>();
  video: string | File | undefined = undefined;
  quizButtonToggle = false;
  quizOnInit = false;
  ngOnInit(): void {
    if (this.lessonGroup.get('quiz')) {
      this.quizButtonToggle = true;
      this.quizOnInit = true;
    }
    if (this.lessonGroup.get('video')?.value)
      this.video = environment.videoUrl + this.lessonGroup.get('video')?.value;
  }
  get title() {
    return this.lessonGroup.get('title');
  }
  get description() {
    return this.lessonGroup.get('description');
  }
  get quiz() {
    return this.lessonGroup.get('quiz') as FormGroup;
  }
  createPreview(e: Event) {
    const file = ((e.currentTarget as HTMLInputElement).files as FileList).item(
      0
    );
    if (file) {
      this.video = URL.createObjectURL(file);
      this.lessonGroup.get('video')?.patchValue(file);
    }
    this.lessonGroup.get('video')?.markAsDirty();
  }
  removeLesson() {
    this.onRemoveLesson.emit(this.lessonId);
  }
  addQuiz() {
    this.quizButtonToggle = true;
    this.onAddQuiz.emit(this.lessonGroup);
  }
  removeQuiz() {
    this.quizButtonToggle = false;
    this.lessonGroup.removeControl('quiz');
    this.lessonGroup.markAsDirty();
    if (this.lessonId && this.quizOnInit) this.onRemoveQuiz.emit(this.lessonId);
  }
  addQuestion(quizGroup: FormGroup) {
    this.onAddQuestion.emit(quizGroup);
  }
  addAnswer(questionGroup: FormGroup) {
    this.onAddAnswer.emit(questionGroup);
  }
}
