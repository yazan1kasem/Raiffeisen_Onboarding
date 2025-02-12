import {ApplicationConfig, importProvidersFrom} from '@angular/core';
import { provideRouter } from '@angular/router';
import {provideHttpClient, HTTP_INTERCEPTORS, withInterceptorsFromDi} from '@angular/common/http';
import {routes} from "./app/app.routes";
import {AuthInterceptor} from "./app/auth.interceptor";
import {AppComponent} from "./app/app.component";
import {bootstrapApplication} from "@angular/platform-browser";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";



export const appConfig: ApplicationConfig = {
  providers: [
    importProvidersFrom([BrowserAnimationsModule]),
    provideRouter(routes),
    provideHttpClient(
      withInterceptorsFromDi(),
    ),
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true,
    },
  ],
};
bootstrapApplication(AppComponent, appConfig)
  .catch((err) => console.error(err));
