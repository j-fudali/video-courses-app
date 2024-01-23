import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject, signal } from '@angular/core';
import { environment } from '../../../environments/environment.development';
import { SearchFilters } from '../../shared/interfaces/SearchFilters';
import { Observable, map, scan, shareReplay, tap } from 'rxjs';
import { Course } from '../../shared/interfaces/Course';
import { Pagination } from '../../shared/interfaces/Pagination';
import { PaginationRequest } from '../../shared/interfaces/PaginationRequest';
import { toSignal } from '@angular/core/rxjs-interop';
import { CourseDetail } from '../../shared/interfaces/CourseDetail';

@Injectable({
  providedIn: 'root',
})
export class CoursesService {
  private http = inject(HttpClient);
  private baseUrl = environment.url + '/courses';

  getCourses(filters?: SearchFilters, pagination?: PaginationRequest) {
    let params = new HttpParams();
    if (filters) {
      if (filters.category) params = params.set('category', filters.category);
      if (filters.name) params = params.set('name', filters.name);
    }
    if (pagination) {
      params = params.set('page', pagination.page);
      params = params.set('size', pagination.size);
    }
    return this.http.get<Pagination<Course>>(this.baseUrl, { params });
  }
  getCourse(courseId: number) {
    return this.http.get<CourseDetail>(this.baseUrl + '/' + courseId);
  }
}
