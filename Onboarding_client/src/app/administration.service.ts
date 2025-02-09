import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, Observable, of } from 'rxjs';
import { Item } from './models/item';
import { User } from './models/user';

@Injectable({
  providedIn: 'root'
})
export class AdministrationService {
  private adminApiUrl = 'http://localhost:8081/admin';
  private superAdminApiUrl = 'http://localhost:8081/superadmin';

  constructor(private http: HttpClient) {}

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    if (!token) {
      throw new Error('No token found in local storage.');
    }
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
  }

  // Admin endpoints
  createItem(item: Item): Observable<Item> {
    return this.http.post<Item>(`${this.adminApiUrl}/items`, item, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError<Item>('createItem'))
    );
  }

  updateItem(id: string, item: Item): Observable<Item> {
    return this.http.put<Item>(`${this.adminApiUrl}/items/${id}`, item, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError<Item>('updateItem'))
    );
  }

  deleteItem(id: string): Observable<void> {
    return this.http.delete<void>(`${this.adminApiUrl}/items/${id}`, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError<void>('deleteItem'))
    );
  }

  checkIfAdmin(): Observable<string> {
    return this.http.get<string>(`${this.adminApiUrl}/test`, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError<string>('checkIfAdmin'))
    );
  }

  // SuperAdmin endpoints
  promoteUserToAdmin(id: string): Observable<User> {
    return this.http.put<User>(`${this.superAdminApiUrl}/users/${id}/promote`, {}, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError<User>('promoteUserToAdmin'))
    );
  }

  demoteUserToUser(id: string): Observable<User> {
    return this.http.put<User>(`${this.superAdminApiUrl}/users/${id}/demote`, {}, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError<User>('demoteUserToUser'))
    );
  }

  setActiveStatus(id: string, isActive: boolean): Observable<User> {
    return this.http.put<User>(`${this.superAdminApiUrl}/users/${id}/status`, null, {
      headers: this.getAuthHeaders(),
      params: { isActive: isActive.toString() }
    }).pipe(
      catchError(this.handleError<User>('setActiveStatus'))
    );
  }

  deleteUser(id: string): Observable<void> {
    return this.http.delete<void>(`${this.superAdminApiUrl}/users/${id}`, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError<void>('deleteUser'))
    );
  }

  grantChecklistAccess(id: string): Observable<User> {
    return this.http.put<User>(`${this.superAdminApiUrl}/users/${id}/grant-access`, {}, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError<User>('grantChecklistAccess'))
    );
  }

  revokeChecklistAccess(id: string): Observable<User> {
    return this.http.put<User>(`${this.superAdminApiUrl}/users/${id}/revoke-access`, {}, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError<User>('revokeChecklistAccess'))
    );
  }

  checkIfSuperAdmin(): Observable<string> {
    return this.http.get<string>(`${this.superAdminApiUrl}/test`, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError<string>('checkIfSuperAdmin'))
    );
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(`${operation} failed: ${error.message}`);
      return of(result as T);
    };
  }
}
