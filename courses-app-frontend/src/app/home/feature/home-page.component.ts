import { CommonModule } from '@angular/common';
import { Component, DestroyRef, OnInit, inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { SearchCoursesComponent } from '../../shared/ui/search-courses/search-courses.component';
import { MatDividerModule } from '@angular/material/divider';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';
import { CategoriesService } from '../../shared/data-access/categories.service';
import { SearchFilters } from '../../shared/interfaces/SearchFilters';
@Component({
  standalone: true,
  imports: [
    CommonModule,
    MatButtonModule,
    SearchCoursesComponent,
    MatDividerModule,
    MatIconModule,
  ],
  templateUrl: './home-page.component.html',
  styleUrl: './home-page.component.scss',
})
export class HomePageComponent implements OnInit {
  private breakpointObserver = inject(BreakpointObserver);
  private destroyRef = inject(DestroyRef);
  private router = inject(Router);
  private categoriesService = inject(CategoriesService);
  categories$ = this.categoriesService.getCategories();
  isGtSm: boolean = false;
  ngOnInit(): void {
    this.breakpointObserver
      .observe([Breakpoints.Medium, Breakpoints.Large, Breakpoints.XLarge])
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((v) => (this.isGtSm = v.matches));
  }
  search(filters: SearchFilters) {
    this.router.navigate(['/courses'], { queryParams: { ...filters } });
  }
  showAll() {
    this.router.navigate(['/courses']);
  }
}
