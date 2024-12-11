import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {catchError, map, Observable, of} from 'rxjs';
import { Checklist } from './models/checklist';
import { User } from "./models/user";

@Injectable({
  providedIn: 'root'
})
export class ChecklistService {
  private apiUrl = 'http://localhost:8081/checklisten';

  constructor(private http: HttpClient) {}

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

  getChecklists(): Observable<Checklist[]> {
    return this.http.get<Checklist[]>(this.apiUrl, { headers: this.getAuthHeaders() });
  }

  createChecklist(checklist: Checklist): Observable<Checklist> {
    return this.http.post<Checklist>(this.apiUrl, checklist, { headers: this.getAuthHeaders() });
  }

  updateChecklist(checklist: Checklist): Observable<Checklist> {
    return this.http.put<Checklist>(`${this.apiUrl}/${checklist.id}`, checklist, { headers: this.getAuthHeaders() });
  }

  deleteChecklist(checklistId: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${checklistId}`, { headers: this.getAuthHeaders() });
  }





  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(error);
      console.log(`${operation} failed: ${error.message}`);
      return of(result as any);
    };
  }
}
