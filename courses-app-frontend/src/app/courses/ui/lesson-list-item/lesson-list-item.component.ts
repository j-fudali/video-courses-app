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
  @Input() disabled: boolean;
  @Input({ required: true }) lesson: Lesson;
}
