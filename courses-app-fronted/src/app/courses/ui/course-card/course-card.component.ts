import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  Output,
} from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { RouterModule } from '@angular/router';
import { Course } from '../../../shared/interfaces/Course';
import { MatChipsModule } from '@angular/material/chips';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-course-card',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    RouterModule,
    MatChipsModule,
    MatTooltipModule,
    MatIconModule,
  ],
  template: `
    <mat-card>
      <mat-card-header>
        <mat-card-title>{{ course.name }}</mat-card-title>
        <mat-card-subtitle
          >Author:
          {{
            course.creator.firstname + ' ' + course.creator.lastname
          }}</mat-card-subtitle
        >
        <mat-chip>{{ course.category.name }}</mat-chip>
      </mat-card-header>
      <mat-card-content>
        <p>{{ course.description }}</p>
      </mat-card-content>
      <mat-card-actions class="actions">
        @if(isLoggedIn){
        <div
          matTooltip="You are a creator of this course"
          matTooltipShowDelay="500"
          [matTooltipDisabled]="!isCreator"
        >
          <button [disabled]="isCreator" mat-mini-fab color="primary">
            <mat-icon>shopping_cart</mat-icon>
          </button>
        </div>
        }
        <a [routerLink]="['/courses', course.idcourse]" mat-stroked-button
          >More</a
        >
      </mat-card-actions>
    </mat-card>
  `,
  styleUrl: './course-card.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CourseCardComponent {
  @Input({ required: true }) course: Course;
  @Input({ required: true }) isLoggedIn: boolean;
  @Input({ required: true }) isCreator: boolean;
  // @Input() addedToCart = false;
  // @Output() onAddToCart = new EventEmitter<number>();

  buy() {
    // this.onBuy.emit(this.course.idcourse);
  }
}
