import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, Observable, of } from 'rxjs';
import { Checklist } from './models/checklist';
import {UserChecklist} from "./models/user_checklist";
import {User} from "./models/user";
import {tap} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class Dataservice {
  private apiUrl = 'http://localhost:8081/checklisten'; // Deine API-URL

  constructor(private http: HttpClient) {}

  // Hilfsmethode zum Abrufen der Auth-Header mit Bearer Token
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
  getChecklistById(id: number): Observable<Checklist> {
    return this.http.get<Checklist>(`${this.apiUrl}/${id}`, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError<Checklist>('getChecklistById'))
    );
  }

  // Liste aller Checklisten abrufen
  getChecklists(): Observable<Checklist[]> {
    return this.http.get<Checklist[]>(this.apiUrl, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError<Checklist[]>('getChecklists', [])) // Fehlerbehandlung hinzufügen
    );
  }

  // Einzelne Checkliste erstellen
  createChecklist(checklist: Checklist): Observable<Checklist> {
    return this.http.post<Checklist>(this.apiUrl, checklist, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError<Checklist>('createChecklist'))
    );
  }

  // Checkliste aktualisieren
  updateChecklist(checklist: Checklist): Observable<Checklist> {
    return this.http.put<Checklist>(`${this.apiUrl}/${checklist.id}`, checklist, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError<Checklist>('updateChecklist'))
    );
  }

  // Checkliste löschen
  deleteChecklist(checklistId: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${checklistId}`, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError<void>('deleteChecklist'))
    );
  }
  private UserchecklistLink = 'http://localhost:8081/api/user-checklists'; // Deine API-URL

  createUserChecklist(userChecklist:UserChecklist): Observable<UserChecklist> {
    return this.http.post<UserChecklist>(`${this.UserchecklistLink}`, userChecklist, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError<UserChecklist>('createUserChecklist'))
    );
  }


  private UserLink = 'http://localhost:8081/users/me'; // Deine API-URL

  getCurrentuser(): Observable<User> {
    return this.http.get<User>(this.UserLink, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError<User>('getCurrentUser'))
    );
  }

  // Fehlerbehandlungsmethode
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(`${operation} failed: ${error.message}`);
      return of(result as T); // Rückgabe eines sicheren Ergebnisses
    };
  }
}
