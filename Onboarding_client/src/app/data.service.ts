import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, Observable, of } from 'rxjs';
import { Checklist } from './models/checklist';
import { Item } from './models/item';
import { UserChecklist } from './models/user_checklist';
import {User} from "./models/user";

@Injectable({
  providedIn: 'root'
})
export class DataService {
  private apiUrl = 'http://localhost:8081/checklisten';
  private apiUserChecklistUrl = 'http://localhost:8081/userchecklist';
  private apiUserUrl = 'http://localhost:8081/user';


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

  /*
  * Items
   */

  getItems(): Observable<Item[]> {
    return this.http.get<Item[]>(`${this.apiUrl}/items`, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError<Item[]>('getItems', []))
    );
  }

  /*
  * Checklist
  */

  getChecklists(): Observable<Checklist[]> {
    return this.http.get<Checklist[]>(this.apiUrl, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError<Checklist[]>('getChecklists', []))
    );
  }

  getChecklist(id: string): Observable<Checklist> {
    return this.http.get<Checklist>(`${this.apiUrl}/${id}`, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError<Checklist>('getChecklists', new Checklist('dummyId', 'dummyAbteilungsname', 'dummyPosition', [])))
    );
  }

  updateChecklist(checklist: Checklist): Observable<Checklist> {
    return this.http.put<Checklist>(`${this.apiUrl}/${checklist.id}`, checklist, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError<Checklist>('updateChecklist'))
    );
  }

  /*
  * Userchecklist
  */

  getUserChecklists(): Observable<UserChecklist[]> {
    return this.http.get<UserChecklist[]>(`${this.apiUserChecklistUrl}`, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError<UserChecklist[]>('getUserChecklists', []))
    );
  }

  getUserChecklist(id: string): Observable<UserChecklist> {
    return this.http.get<UserChecklist>(`${this.apiUserChecklistUrl}/${id}`, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError<UserChecklist>('getUserChecklist'))
    );
  }

  createUserChecklist(userChecklist: UserChecklist): Observable<UserChecklist> {
    return this.http.post<UserChecklist>(`${this.apiUserChecklistUrl}`, userChecklist, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError<UserChecklist>('createUserChecklist'))
    );
  }

  updateUserChecklist(userChecklist: UserChecklist): Observable<void> {
    return this.http.put<void>(`${this.apiUserChecklistUrl}/${userChecklist.id}`, userChecklist, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError<void>('updateUserChecklist'))
    );
  }

  deleteUserChecklist(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUserChecklistUrl}/${id}`, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError<void>('deleteUserChecklist'))
    );
  }

  /*
  * Converters
  */

  generateExcel(checklist: UserChecklist): void {
    const url = 'http://localhost:8081/api/excel/generate';
    this.http.post(url, checklist, {
      responseType: 'blob',
      headers: this.getAuthHeaders()
    }).subscribe(blob => {
      const now = new Date();
      const formattedDate =
        now.getDate().toString().padStart(2, '0') + '-' +
        (now.getMonth() + 1).toString().padStart(2, '0') + '-' +
        now.getFullYear() + ', ' +
        now.getHours().toString().padStart(2, '0') + 'h_' +
        now.getMinutes().toString().padStart(2, '0');

      this.downloadFile(blob, `${checklist.ueberschrift}_${formattedDate}.xlsx`);
    });
  }

  generatePdf(checklist: UserChecklist): void {
    const url = 'http://localhost:8081/api/pdf/generate';
    this.http.post(url, checklist, {
      responseType: 'blob',
      headers: this.getAuthHeaders()
    }).subscribe(blob => {
      const now = new Date();
      const formattedDate =
        now.getDate().toString().padStart(2, '0') + '-' +
        (now.getMonth() + 1).toString().padStart(2, '0') + '-' +
        now.getFullYear() + ', ' +
        now.getHours().toString().padStart(2, '0') + 'h_' +
        now.getMinutes().toString().padStart(2, '0');

      this.downloadFile(blob, `${checklist.ueberschrift}_${formattedDate}.pdf`);
    });
  }

  generateWord(checklist: UserChecklist): void {
    const url = 'http://localhost:8081/api/word/generate';
    this.http.post(url, checklist, {
      responseType: 'blob',
      headers: this.getAuthHeaders()
    }).subscribe(blob => {
      const now = new Date();
      const formattedDate =
        now.getDate().toString().padStart(2, '0') + '-' +
        (now.getMonth() + 1).toString().padStart(2, '0') + '-' +
        now.getFullYear() + ', ' +
        now.getHours().toString().padStart(2, '0') + 'h_' +
        now.getMinutes().toString().padStart(2, '0');
      this.downloadFile(blob, `${checklist.ueberschrift}_${formattedDate}.docx`);
    });
  }

  private downloadFile(blob: Blob, filename: string): void {
    const link = document.createElement('a');
    const url = window.URL.createObjectURL(blob);
    link.href = url;
    link.download = filename;
    link.click();
    window.URL.revokeObjectURL(url);
  }

  /*
  User
   */

  private getUser(id: string): Observable<User> {
    return this.http.get<User>(`${this.apiUserUrl}/${id}`, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError<User>('getUser'))
    );
  }

  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUserUrl, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError<User[]>('getUsers', []))
    );
  }

  /*
  * Error Handling
  */

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(`${operation} failed: ${error.message}`);
      return of(result as T);
    };
  }

}
