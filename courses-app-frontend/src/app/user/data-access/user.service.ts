import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, shareReplay } from 'rxjs';
import { environment } from '../../../environments/environment.development';
import { Pagination } from '../../shared/interfaces/Pagination';
import { PaginationRequest } from '../../shared/interfaces/PaginationRequest';
import { UserCourse } from '../../shared/interfaces/UserCourse';
import { UserProfile } from '../../shared/interfaces/UserProfile';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private http = inject(HttpClient);
  private baseUrl = environment.url + '/users/me';

  getUserProfile() {
    return this.http.get<UserProfile>(this.baseUrl);
  }
  changePassword(newPassword: string) {
    return this.http.post<{ message: string }>(
      this.baseUrl + '/change-password',
      { newPassword }
    );
  }
  resetPassword() {}
  updateUserProfle(firstname: string | null, lastname: string | null) {
    if (!firstname)
      return this.http.patch<{ message: string }>(this.baseUrl, { lastname });
    if (!lastname)
      return this.http.patch<{ message: string }>(this.baseUrl, { firstname });
    return this.http.patch<{ message: string }>(this.baseUrl, {
      firstname,
      lastname,
    });
  }
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
    return this.http.get<Pagination<UserCourse>>(this.baseUrl + '/courses', {
      params,
    });
  }
  checkUserAlreadyBoughtCourse(courseId: number) {
    return this.http.get<{ message: string }>(
      this.baseUrl + '/courses/' + courseId
    );
  }
}
