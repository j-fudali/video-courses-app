import { CommonModule } from '@angular/common';
import { Component, Input, OnInit, inject } from '@angular/core';
import { CourseDetail } from '../../../shared/interfaces/CourseDetail';
import { Observable, map, tap } from 'rxjs';
import { MatDividerModule } from '@angular/material/divider';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { MatTabsModule } from '@angular/material/tabs';
import { MatListModule } from '@angular/material/list';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from '../../../shared/data-access/auth.service';
import { LessonListItemComponent } from '../../ui/lesson-list-item/lesson-list-item.component';

interface CourseDetailsData {
  alreadyBought: boolean;
  course: CourseDetail;
}
@Component({
  selector: 'app-course-detail',
  standalone: true,
  imports: [
    CommonModule,
    MatDividerModule,
    MatTabsModule,
    MatListModule,
    LessonListItemComponent,
  ],
  templateUrl: './course-detail.component.html',
  styleUrl: './course-detail.component.scss',
})
export class CourseDetailComponent implements OnInit {
  @Input() courseId!: number;
  private breakpointObs = inject(BreakpointObserver);
  private authService = inject(AuthService);
  private route = inject(ActivatedRoute);
  userLoggedIn = this.authService.$isLoggedIn;
  isGtSm$ = this.breakpointObs
    .observe([Breakpoints.Medium, Breakpoints.Large, Breakpoints.XLarge])
    .pipe(map((bp) => bp.matches));
  data$: Observable<CourseDetailsData>;
  userId = this.authService.getUserId();
  ngOnInit(): void {
    this.data$ = (
      this.route.data as Observable<{ data: CourseDetailsData }>
    ).pipe(map(({ data }) => ({ ...data })));
  }
}
