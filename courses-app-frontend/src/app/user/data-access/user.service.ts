import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, shareReplay } from 'rxjs';
import { environment } from '../../../environments/environment.development';
import { Pagination } from '../../shared/interfaces/Pagination';
import { PaginationRequest } from '../../shared/interfaces/PaginationRequest';
import { UserCourse } from '../../shared/interfaces/UserCourse';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private http = inject(HttpClient);
  private baseUrl = environment.url + '/users/me/courses';
  getCourses(
    type: string,
    paginationRequest?: PaginationRequest
  ): Observable<Pagination<UserCourse>> {
    let params = new HttpParams().set('type', type);
    if (paginationRequest) {
      if (paginationRequest.page)
        params = params.set('page', paginationRequest.page);
      if (paginationRequest.size)
        params = params.set('size', paginationRequest.size);
    }
    return this.http.get<Pagination<UserCourse>>(this.baseUrl, { params });
  }
  checkUserAlreadyBoughtCourse(courseId: number) {
    return this.http.get<{ message: string }>(this.baseUrl + '/' + courseId);
  }
}
