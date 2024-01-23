import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  Output,
} from '@angular/core';
import { MatListModule } from '@angular/material/list';
import { RouterModule } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { UserCourse } from '../../../shared/interfaces/UserCourse';

@Component({
  selector: 'app-user-courses-list-item',
  standalone: true,
  imports: [
    CommonModule,
    MatListModule,
    RouterModule,
    MatIconModule,
    MatButtonModule,
  ],
  template: `
    <mat-list-item [routerLink]="['/courses', course.idcourse]">
      @if(type == 'owned'){
      <mat-icon matListItemIcon>{{
        course.completed ? 'check_circle' : 'check_circle_outline'
      }}</mat-icon>
      }
      <h3 matListItemTitle>{{ course.name }}</h3>
      <p matListItemLine>
        <i>{{ course.category.name }}</i>
      </p>
      @if(type == 'owned'){
      <span matListItemMeta>{{
        course.completed ? 'Completed' : 'Not completed'
      }}</span>
      } @else{
      <button
        (click)="edit($event, course.idcourse)"
        mat-icon-button
        matListItemMeta
      >
        <mat-icon>edit</mat-icon>
      </button>
      }
    </mat-list-item>
  `,
  styleUrl: './user-courses-list-item.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class UserCoursesListItemComponent {
  @Input({ required: true }) type: string;
  @Input({ required: true }) course: UserCourse;
  @Output() onEdit = new EventEmitter<number>();
  edit(e: Event, courseId: number) {
    e.stopPropagation();
    this.onEdit.emit(courseId);
  }
}
