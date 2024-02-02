import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { MatListModule } from '@angular/material/list';
import { Lesson } from '../../../shared/interfaces/Lesson';
import { MatIconModule } from '@angular/material/icon';
import { RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-lesson-list-item',
  standalone: true,
  imports: [
    CommonModule,
    MatListModule,
    MatIconModule,
    RouterModule,
    MatIconModule,
    MatButtonModule,
  ],
  template: `
    <mat-list-item
      [disabled]="disabled"
      [routerLink]="!disabled ? ['lessons', lesson.idlesson] : null"
    >
      @if(!isCreator){
      <mat-icon matListItemIcon>
        @if(lesson.isCompleted){ check_circle }@else{ check_circle_outline }
      </mat-icon>
      }
      <h3 matListItemTitle>
        {{ lesson.title }}
      </h3>
      <span matListItemLine>{{ lesson.description }}</span>
    </mat-list-item>
  `,
  styleUrl: './lesson-list-item.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class LessonListItemComponent {
  @Input() isCreator: boolean;
  @Input() disabled: boolean;
  @Input({ required: true }) lesson: Lesson;
}
