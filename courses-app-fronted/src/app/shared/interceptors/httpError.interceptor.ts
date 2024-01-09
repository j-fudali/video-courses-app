import type {
  HttpErrorResponse,
  HttpInterceptorFn,
} from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError } from 'rxjs';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { Location } from '@angular/common';
import { AuthService } from '../data-access/auth.service';
export const httpErrorInterceptor: HttpInterceptorFn = (req, next) => {
  const snackbar = inject(MatSnackBar);
  const router = inject(Router);
  const location = inject(Location);
  const authService = inject(AuthService);
  return next(req).pipe(
    catchError((err: HttpErrorResponse) => {
      switch (err.status) {
        case 401:
          authService.logout();
          snackbar.open('You are not logged in', 'X');
          router.navigate(['/login']);
          throw err;
        case 403:
          if (!req.url.endsWith('/quiz/verify')) {
            location.back();
            snackbar.open('Access denied', 'X');
          } else {
            snackbar.open(
              'You cannot solve the quiz, because you are the creator of this course',
              'X'
            );
          }
          throw err;
        default:
          if (err.error.message === 'User not found') {
            router.navigate(['/login']);
          }
          if (err.error.message == 'Course not found') {
            router.navigate(['/home']);
          }
      }
      if (!(err.status == 404 && err.error.message == 'Ownership not found'))
        snackbar.open(err.error.message, 'X');
      throw err;
    })
  );
};
