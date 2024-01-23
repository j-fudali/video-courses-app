import { ApplicationConfig } from '@angular/core';
import { provideRouter, withComponentInputBinding } from '@angular/router';

import { routes } from './app.routes';
import { provideAnimations } from '@angular/platform-browser/animations';
import {
  HTTP_INTERCEPTORS,
  provideHttpClient,
  withInterceptors,
} from '@angular/common/http';
import { authInterceptor } from './shared/interceptors/auth.interceptor';
import { httpErrorInterceptor } from './shared/interceptors/httpError.interceptor';
import { MAT_SNACK_BAR_DEFAULT_OPTIONS } from '@angular/material/snack-bar';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes, withComponentInputBinding()),
    provideAnimations(),
    provideHttpClient(
      withInterceptors([authInterceptor, httpErrorInterceptor])
    ),
    { provide: MAT_SNACK_BAR_DEFAULT_OPTIONS, useValue: { duration: 5000 } },
  ],
};
