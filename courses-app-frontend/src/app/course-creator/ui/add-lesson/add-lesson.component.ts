import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  OnInit,
  Output,
} from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { AddQuizComponent } from '../add-quiz/add-quiz.component';
import { environment } from '../../../../environments/environment';

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
        <mat-panel-title>{{ title.value || 'Lesson title' }}</mat-panel-title>
      </mat-expansion-panel-header>
      <form [formGroup]="lessonGroup">
        <div
          class="upload-video"
          [style.flexDirection]="isGtXs ? 'row' : 'column'"
          [style.alignItems]="isGtXs ? 'center' : 'flex-start'"
          [style.justifyContent]="isGtXs ? 'space-between' : 'flex-start'"
        >
          <div
            class="upload-video-button"
            [ngStyle]="
              videoControl.value === null
                ? { padding: '50px', width: '100%' }
                : { padding: 0, width: 'auto' }
            "
          >
            <button
              #uploadVideoButton
              type="button"
              mat-fab
              extended
              color="primary"
              (click)="file.click()"
            >
              <mat-icon>attach_file</mat-icon>Upload video
            </button>
            @if(videoControl.invalid && (videoControl.touched ||
            videoControl.dirty) ){ @if(videoControl.hasError('required')){
            <mat-error>Field is required</mat-error>
            } @else {

            <mat-error>Video max. size is 100MB</mat-error>
            } }
          </div>
          <input
            (cancel)="createPreview($event)"
            (change)="createPreview($event)"
            #file
            type="file"
            accept="video/*video/mp4,video/x-m4v,video/*"
          />
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
          @if(title.invalid){
          <mat-error>
            @if(title.hasError('required')){ Field is required } @else{ Title
            max. length is 100 characters }
          </mat-error>
          }
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
  get videoControl() {
    return this.lessonGroup.get('video') as FormControl;
  }
  get title() {
    return this.lessonGroup.get('title') as FormControl;
  }
  get description() {
    return this.lessonGroup.get('description') as FormControl;
  }
  get quiz() {
    return this.lessonGroup.get('quiz') as FormGroup;
  }
  createPreview(e: Event) {
    const file = ((e.currentTarget as HTMLInputElement).files as FileList).item(
      0
    );
    if (file) {
      if (file.size > 100000000) {
        this.lessonGroup.get('video')?.setErrors({ toBig: true });
      } else {
        this.video = URL.createObjectURL(file);
        this.videoControl.patchValue(file);
        this.videoControl.markAsDirty();
      }
    }
    this.videoControl.markAsTouched();
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
