import { CommonModule } from '@angular/common';
import { Component, Input, OnInit, computed, inject } from '@angular/core';
import { CourseDetail } from '../../../shared/interfaces/CourseDetail';
import { Observable, map, tap } from 'rxjs';
import { MatDividerModule } from '@angular/material/divider';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { MatTabsModule } from '@angular/material/tabs';
import { MatListModule } from '@angular/material/list';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from '../../../shared/data-access/auth.service';
import { LessonListItemComponent } from '../../ui/lesson-list-item/lesson-list-item.component';
import { ShoppingCartButtonComponent } from '../../ui/shopping-cart-button/shopping-cart-button.component';
import { ShoppingCartService } from '../../../shared/data-access/shopping-cart.service';
import { Course } from '../../../shared/interfaces/Course';

@Component({
  selector: 'app-course-detail',
  standalone: true,
  imports: [
    CommonModule,
    MatDividerModule,
    MatTabsModule,
    MatListModule,
    LessonListItemComponent,
    ShoppingCartButtonComponent,
  ],
  templateUrl: './course-detail.component.html',
  styleUrl: './course-detail.component.scss',
})
export class CourseDetailComponent implements OnInit {
  @Input() courseId!: number;
  private breakpointObs = inject(BreakpointObserver);
  private authService = inject(AuthService);
  private route = inject(ActivatedRoute);
  private shoppingCartService = inject(ShoppingCartService);
  private cart = this.shoppingCartService.coursesInCart;
  inCart = computed(
    () => this.cart().filter((c) => c.idcourse == this.courseId).length > 0
  );
  userLoggedIn = this.authService.$isLoggedIn;
  isGtSm$ = this.breakpointObs
    .observe([Breakpoints.Medium, Breakpoints.Large, Breakpoints.XLarge])
    .pipe(map((bp) => bp.matches));
  course$: Observable<CourseDetail>;
  userId = this.authService.getUserId();
  ngOnInit(): void {
    this.course$ = (this.route.data as Observable<{ data: CourseDetail }>).pipe(
      map(({ data }) => ({ ...data }))
    );
  }
  addToShoppingCart(course: CourseDetail) {
    this.shoppingCartService.addToCart({
      idcourse: this.courseId,
      ...course,
    } as Course);
  }
}
