// auth.service.ts
import { Injectable } from '@angular/core';
import { HttpClient,HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = 'http://localhost:8081/auth'; // Ersetze mit deiner API-URL

  constructor(private http: HttpClient) {}

  login(username: string, password: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, { username, password }).pipe(
      tap((response: any) => {
        localStorage.setItem('token', response.token);// Speichere den JWT-Token im Local Storage
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
    const token = this.getToken();
    if (token && this.tokenExpired(token)) {
      this.logout(); // Remove expired token
      return false;
    }
    return !!token && !this.tokenExpired(token);
  }

  private tokenExpired(token: string): boolean {
    const expiry = JSON.parse(atob(token.split('.')[1])).exp;
    return Math.floor(new Date().getTime() / 1000) >= expiry;
  }
}


