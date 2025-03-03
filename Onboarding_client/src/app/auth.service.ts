// auth.service.ts
import { Injectable } from '@angular/core';
import {HttpClient, HttpRequest, HttpHandler, HttpEvent, HttpInterceptor, HttpHeaders} from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import {User} from "./models/user";

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = 'http://localhost:8081/auth'; // Ersetze mit deiner API-URL
  private allurl = 'http://localhost:8081/'; // Ersetze mit deiner API-URL

  constructor(private http: HttpClient) {}

  signup(username: string, password: string): Observable<any> {
    console.log("ein neuer Account wird erstellt...")
    return this.http.post(`${this.apiUrl}/signup`, { username, password });
  }

  login(username: string, password: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, { username, password }).pipe(
      tap((response: any) => {
        localStorage.setItem('token', response.token);// Speichere den JWT-Token im Local Storage
        localStorage.setItem('role', response.role);
      })
    );
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  logout(): void {
    localStorage.removeItem('token'); // Entferne den Token beim Logout
    console.log("token wurde entfernt!")
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

  // New Method: Get User Role
  getRole(): string | null {
    const token = this.getToken();
    if (token) {
      const payload = JSON.parse(atob(token.split('.')[1])); // Decode JWT payload
      return payload.role; // Assumes role is stored in token payload
    }
    return null;
  }

  // New Method: Check if user has a specific role
  hasRole(requiredRole: string): boolean {
    const role = this.getRole();
    return role === requiredRole;
  }

  // New Method: Check if user has any role in a list of roles
  hasAnyRole(requiredRoles: string[]): boolean {
    const role = this.getRole();
    return requiredRoles.includes(role || '');
  }

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    if (!token) {
      throw new Error('Kein Token im Local Storage gefunden.');
    }
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
  }

  getUser(): Observable<User> {
    return this.http.get<User>(`${this.allurl}users/me`, { headers: this.getAuthHeaders() });
  }
}


