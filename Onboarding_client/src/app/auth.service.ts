// auth.service.ts
import { Injectable } from '@angular/core';
import { HttpClient,HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { CanActivate, Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/auth'; // Ersetze mit deiner API-URL

  constructor(private http: HttpClient) {}

  login(username: string, password: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, { username, password }).pipe(
      tap((response: any) => {
        localStorage.setItem('token', response.token); // Speichere den JWT-Token im Local Storage
      })
    );
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  logout(): void {
    localStorage.removeItem('token'); // Entferne den Token beim Logout
  }

  isAuthenticated(): boolean {
    return !!this.getToken(); // Prüft, ob ein Token vorhanden ist
  }
}


