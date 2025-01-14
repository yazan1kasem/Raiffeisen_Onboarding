import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, Observable, of } from 'rxjs';
import { Checklist } from './models/checklist';

@Injectable({
  providedIn: 'root'
})
export class ChecklistService {
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



  // Fehlerbehandlungsmethode
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(`${operation} failed: ${error.message}`);
      return of(result as T); // Rückgabe eines sicheren Ergebnisses
    };
  }
}
