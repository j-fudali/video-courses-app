import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  OnInit,
  Output,
  Signal,
  computed,
} from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { RouterModule } from '@angular/router';
import { Course } from '../../../shared/interfaces/Course';
import { MatChipsModule } from '@angular/material/chips';
import { MatTooltipModule } from '@angular/material/tooltip';
import { ShoppingCartButtonComponent } from '../shopping-cart-button/shopping-cart-button.component';

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
    ShoppingCartButtonComponent,
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
        <p>
          Cost:
          <b
            ><i>{{ course.cost | currency : 'PLN' : 'symbol' }}</i></b
          >
        </p>
      </mat-card-content>
      <mat-card-actions class="actions">
        @if(isLoggedIn){
        <div
          [matTooltip]="
            course.isBought
              ? 'You already bought this course'
              : 'You are a creator of this course'
          "
          matTooltipShowDelay="500"
          [matTooltipDisabled]="!(isCreator || course.isBought)"
        >
          <app-shopping-cart-button
            [disabled]="isCreator || inCart() || course.isBought ? true : false"
            [isAvailable]="inCart() || course.isBought ? true : false"
            (onAddToShoppingCart)="addToShoppingCart()"
          ></app-shopping-cart-button>
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
  @Input({ required: true }) shoppingCart: Signal<Course[]>;
  @Output() onAddToCart = new EventEmitter<Course>();
  inCart = computed(
    () =>
      this.shoppingCart().filter((c) => c.idcourse == this.course.idcourse)
        .length > 0
  );
  addToShoppingCart() {
    this.onAddToCart.emit(this.course);
  }
}
