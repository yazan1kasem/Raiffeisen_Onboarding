import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, Observable, of } from 'rxjs';
import { Checklist } from './models/checklist';
import { Item } from './models/item';
import { UserChecklist } from './models/user_checklist';

@Injectable({
  providedIn: 'root'
})
export class DataService {
  private apiUrl = 'http://localhost:8081/checklisten';
  private apiUserUrl = 'http://localhost:8081/userchecklist';
  private savedItems: Item[] = [];
  private savedChecklists: Checklist[] = [];

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

  getItems(): Observable<Item[]> {
    return this.http.get<Item[]>(`${this.apiUrl}/items`, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError<Item[]>('getItems', []))
    );
  }

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

  createChecklist(checklist: Checklist): Observable<Checklist> {
    return this.http.post<Checklist>(this.apiUrl, checklist, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError<Checklist>('createChecklist'))
    );
  }

  updateChecklist(checklist: Checklist): Observable<Checklist> {
    return this.http.put<Checklist>(`${this.apiUrl}/${checklist.id}`, checklist, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError<Checklist>('updateChecklist'))
    );
  }

  deleteChecklist(checklistId: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${checklistId}`, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError<void>('deleteChecklist'))
    );
  }

  saveSelectedItems(items: Item[]): void {
    this.savedItems = items;
  }

  getSavedItems(): Item[] {
    return this.savedItems;
  }

  saveChecklist(checklist: Checklist): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/userchecklist`, checklist, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError<void>('saveChecklist'))
    );
  }

  getUserChecklists(): Observable<UserChecklist[]> {
    return this.http.get<UserChecklist[]>(`${this.apiUserUrl}`, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError<UserChecklist[]>('getUserChecklists', []))
    );
  }

  getUserChecklist(id: string): Observable<UserChecklist> {
    return this.http.get<UserChecklist>(`${this.apiUserUrl}/${id}`, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError<UserChecklist>('getUserChecklist'))
    );
  }

  createUserChecklist(userChecklist: UserChecklist): Observable<UserChecklist> {
    return this.http.post<UserChecklist>(`${this.apiUserUrl}`, userChecklist, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError<UserChecklist>('createUserChecklist'))
    );
  }

  updateUserChecklist(userChecklist: UserChecklist): Observable<void> {
    return this.http.put<void>(`${this.apiUserUrl}/${userChecklist.id}`, userChecklist, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError<void>('updateUserChecklist'))
    );
  }

  deleteUserChecklist(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUserUrl}/${id}`, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError<void>('deleteUserChecklist'))
    );
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(`${operation} failed: ${error.message}`);
      return of(result as T);
    };
  }

  generateExcel(checklist: UserChecklist): void {
    const url = 'http://localhost:8081/api/excel/generate';
    this.http.post(url, checklist, {
      responseType: 'blob',
      headers: this.getAuthHeaders()
    }).subscribe(blob => {
      this.downloadFile(blob, 'checklist_report.xlsx');
    });
  }

  generatePdf(checklist: UserChecklist): void {
    const url = 'http://localhost:8081/api/pdf/generate';
    this.http.post(url, checklist, {
      responseType: 'blob',
      headers: this.getAuthHeaders()
    }).subscribe(blob => {
      this.downloadFile(blob, 'checklist_report.pdf');
    });
  }

  generateWord(checklist: UserChecklist): void {
    const url = 'http://localhost:8081/api/word/generate';
    this.http.post(url, checklist, {
      responseType: 'blob',
      headers: this.getAuthHeaders()
    }).subscribe(blob => {
      this.downloadFile(blob, 'checklist_report.docx');
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

}
