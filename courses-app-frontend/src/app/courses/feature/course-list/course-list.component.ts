import { CommonModule } from '@angular/common';
import {
  Component,
  DestroyRef,
  ElementRef,
  Input,
  OnInit,
  ViewChild,
  inject,
} from '@angular/core';
import { SearchCoursesComponent } from '../../../shared/ui/search-courses/search-courses.component';
import { CategoriesService } from '../../../shared/data-access/categories.service';
import { CoursesService } from '../../data-access/courses.service';
import { Router } from '@angular/router';
import { SearchFilters } from '../../../shared/interfaces/SearchFilters';
import { BehaviorSubject, Observable, concatMap, map, scan } from 'rxjs';
import { Course } from '../../../shared/interfaces/Course';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { MatDividerModule } from '@angular/material/divider';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { CourseCardComponent } from '../../ui/course-card/course-card.component';
import { AuthService } from '../../../shared/data-access/auth.service';
import { ShoppingCartService } from '../../../shared/data-access/shopping-cart.service';
@Component({
  standalone: true,
  imports: [
    CommonModule,
    SearchCoursesComponent,
    MatDividerModule,
    InfiniteScrollModule,
    CourseCardComponent,
  ],
  templateUrl: './course-list.component.html',
  styleUrl: './course-list.component.scss',
})
export class CoursesListComponent implements OnInit {
  @ViewChild('top') top: ElementRef;
  @Input() name: string | undefined;
  @Input() category: string | undefined;
  private categoriesService = inject(CategoriesService);
  private coursesService = inject(CoursesService);
  private router = inject(Router);
  private breakpoints = inject(BreakpointObserver);
  private destroyRef = inject(DestroyRef);
  private authService = inject(AuthService);
  private shoppinCartService = inject(ShoppingCartService);

  shoppingCart = this.shoppinCartService.coursesInCart;
  isLoggedIn = this.authService.$isLoggedIn;

  userId = this.authService.getUserId() || null;
  breakpoints$ = this.breakpoints.observe([
    Breakpoints.Medium,
    Breakpoints.Large,
    Breakpoints.XLarge,
  ]);

  categories$ = this.categoriesService.getCategories();
  courses$: Observable<Course[]>;
  page$ = new BehaviorSubject(0);
  size: number = 10;
  totalElements: number;
  totalPages: number;

  ngOnInit(): void {
    this.courses$ = this.page$.pipe(
      takeUntilDestroyed(this.destroyRef),
      concatMap((page) =>
        this.coursesService.getCourses(
          { name: this.name, category: this.category },
          { page, size: this.size }
        )
      ),
      map((response) => {
        this.totalElements = response.totalElements;
        this.totalPages = response.totalPages;
        return response.content;
      }),
      scan((acc, val) => (this.page$.getValue() == 0 ? val : acc.concat(val)))
    );
  }

  search(filters: SearchFilters) {
    (this.top.nativeElement as HTMLDivElement).scrollIntoView({
      behavior: 'smooth',
    });
    this.name = filters.name;
    this.category = filters.category;
    this.router.navigate(['/courses'], { queryParams: { ...filters } });
    this.page$.next(0);
  }
  showAll() {
    (this.top.nativeElement as HTMLDivElement).scrollIntoView({
      behavior: 'smooth',
    });
    this.name = undefined;
    this.category = undefined;
    this.router.navigate(['/courses']);
    this.page$.next(0);
  }
  onScroll() {
    if (this.page$.getValue() < this.totalPages - 1) {
      this.page$.next(this.page$.getValue() + 1);
    }
  }
  addToCart(course: Course) {
    this.shoppinCartService.addToCart(course);
  }
}
