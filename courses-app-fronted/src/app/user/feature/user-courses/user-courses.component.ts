import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { Observable, map, switchMap, tap } from 'rxjs';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import { MatProgressSpinnerModule } from '@Angular/material/progress-spinner';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { MatDividerModule } from '@angular/material/divider';
import { UserService } from '../../data-access/user.service';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { UserCourse } from '../../../shared/interfaces/UserCourse';
import { UserCoursesListItemComponent } from '../../ui/user-courses-list-item/user-courses-list-item.component';

@Component({
  standalone: true,
  imports: [
    CommonModule,
    MatListModule,
    MatCardModule,
    MatButtonModule,
    MatPaginatorModule,
    MatFormFieldModule,
    MatSelectModule,
    MatInputModule,
    MatProgressSpinnerModule,
    MatDividerModule,
    UserCoursesListItemComponent,
  ],
  template: `
    @if(isGtSm$ | async; as isGtSm){
    <div class="container" [style.width]="isGtSm.matches ? '60%' : '90%'">
      <mat-form-field appearance="outline">
        <mat-label>Type</mat-label>
        <mat-select [(value)]="type" (selectionChange)="changeType()">
          <mat-option value="owned">Owned</mat-option>
          <mat-option value="created">Created</mat-option>
        </mat-select>
      </mat-form-field>
      <mat-divider></mat-divider>
      <mat-nav-list class="courses-list">
        @if(courses$ | async; as courses){ @for(course of courses; track
        course.idcourse){
        <app-user-courses-list-item
          [course]="course"
          [type]="type"
          (onEdit)="edit($event)"
        ></app-user-courses-list-item>

        } @empty{
        <div class="no-content">
          <h2><i>No courses here</i></h2>
        </div>

        } } @if(loading){
        <div class="spinner">
          <mat-spinner mode="indeterminate"></mat-spinner>
        </div>
        }
      </mat-nav-list>
      <mat-paginator
        class="paginator"
        [showFirstLastButtons]="true"
        [pageSizeOptions]="[5, 10, 15, 25, 50]"
        [pageSize]="size"
        [pageIndex]="page - 1"
        [length]="totalElements"
        (page)="changePage($event)"
      ></mat-paginator>
    </div>
    }
  `,
  styleUrl: './user-courses.component.scss',
})
export class MyCoursesComponent {
  private userService = inject(UserService);
  private breakpointObs = inject(BreakpointObserver);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  isGtSm$ = this.breakpointObs.observe([
    Breakpoints.Medium,
    Breakpoints.Large,
    Breakpoints.XLarge,
  ]);

  courses$: Observable<UserCourse[]>;
  type: string = 'owned';
  size: number = 5;
  page: number = 1;
  totalPages: number;
  totalElements: number;

  loading: boolean = false;
  ngOnInit(): void {
    this.type = this.route.snapshot.queryParamMap.get('type') || 'owned';
    this.page = +(this.route.snapshot.queryParamMap.get('page') || 1);
    this.size = +(this.route.snapshot.queryParamMap.get('size') || 5);
    this.courses$ = this.route.queryParams.pipe(
      switchMap((params) => {
        this.loading = true;
        if (params['type']) this.type = params['type'];
        if (params['page']) this.page = +params['page'];
        if (params['size']) this.size = +params['size'];
        return this.userService
          .getCourses(this.type, {
            page: this.page - 1,
            size: this.size,
          })
          .pipe(tap(() => (this.loading = false)));
      }),
      tap((response) => {
        this.totalElements = response.totalElements;
        this.totalPages = response.totalPages;
      }),
      map((response) => response.content)
    );
  }
  changeType() {
    this.router.navigate(['/me/courses'], {
      queryParams: { type: this.type, page: 1, size: this.size },
    });
  }
  changePage(e: PageEvent) {
    if (e.pageIndex <= this.totalPages && e.pageIndex > -1)
      this.router.navigate(['/me/courses'], {
        queryParams: {
          type: this.type,
          page: e.pageIndex + 1,
          size: e.pageSize,
        },
      });
  }
  edit(courseId: number) {
    console.log('Edit course number: ' + courseId);
  }
}
