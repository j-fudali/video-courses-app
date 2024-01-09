import type { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../data-access/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  if (!authService.getToken()) {
    return next(req);
  }
  const newReq = req.clone({
    headers: req.headers.set(
      'Authorization',
      `Bearer ${authService.getToken()}`
    ),
  });
  return next(newReq);
};
