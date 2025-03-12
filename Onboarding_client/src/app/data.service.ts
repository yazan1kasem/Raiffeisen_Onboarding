import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, Observable, of } from 'rxjs';
import { MatSnackBar } from '@angular/material/snack-bar';

import { Checklist } from './models/checklist';
import { Item } from './models/item';
import { UserChecklist } from './models/user_checklist';
import { User } from './models/user';

@Injectable({ providedIn: 'root' })
export class DataService {
  private apiUrl = 'http://localhost:8081/checklisten';
  private apiUserChecklistUrl = 'http://localhost:8081/userchecklist';
  private apiUserUrl = 'http://localhost:8081/user';
  private Adminurl = 'http://localhost:8081/admin';
  private superAdminurl = 'http://localhost:8081/superadmin';

  constructor(private http: HttpClient, private snackBar: MatSnackBar) {}

  // -----------------------------------
  // HTTP Helper Methods
  // -----------------------------------

  getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    if (!token) {
      this.showError('Es scheint, dass du nicht eingeloggt bist. Bitte melde dich an.');
      throw new Error('Kein Token im Local Storage gefunden.');
    }
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
  }

  showError(message: string): void {
    this.snackBar.open(message, 'Schließen', {
      duration: 4000,
      panelClass: ['error-snackbar']
    });
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      let msg = `Fehler bei ${operation}`;
      if (error.status === 403) msg = 'Zugriff verweigert! Du hast nicht die notwendigen Rechte.';
      else if (error.status === 404) msg = 'Die angeforderte Ressource wurde nicht gefunden.';
      else if (error.status === 500) msg = 'Interner Serverfehler! Bitte versuche es später erneut.';
      else if (error.message) msg = `Fehler: ${error.message}`;
      console.error(`${operation} failed: ${error.message}`);
      this.showError(msg);
      return of(result as T);
    };
  }

  // -----------------------------------
  // Admin
  // -----------------------------------

  updatechecklist(checklist: Checklist): Observable<Checklist> {
    return this.http.put<Checklist>(`${this.Adminurl}/checklist`, checklist, { headers: this.getAuthHeaders() })
      .pipe(catchError(this.handleError<Checklist>('updatechecklist')));
  }

  changeuserpassword(id: string, password: string): Observable<void> {
    return this.http.put<void>(`${this.Adminurl}/user/${id}?password=${password}`, { headers: this.getAuthHeaders() })
      .pipe(catchError(this.handleError<void>('changeuserpassword')));
  }

  getuserchecklists(): Observable<UserChecklist[]> {
    return this.http.get<UserChecklist[]>(`${this.Adminurl}/userchecklist`, { headers: this.getAuthHeaders() })
      .pipe(catchError(this.handleError<UserChecklist[]>('getuserchecklists', [])));
  }

  getallusers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.Adminurl}/users`, { headers: this.getAuthHeaders() })
      .pipe(catchError(this.handleError<User[]>('getallusers', [])));
  }

  // -----------------------------------
  // SuperAdmin
  // -----------------------------------

  promoteuser(id: string): Observable<void> {
    return this.http.put<void>(`${this.superAdminurl}/userpromote/${id}`, {}, { headers: this.getAuthHeaders() })
      .pipe(catchError(this.handleError<void>('promoteuser')));
  }

  demoteuser(id: string): Observable<void> {
    return this.http.put<void>(`${this.superAdminurl}/userdemote/${id}`, {}, { headers: this.getAuthHeaders() })
      .pipe(catchError(this.handleError<void>('demoteuser')));
  }

  deleteuser(id: string): Observable<void> {
    return this.http.delete<void>(`${this.superAdminurl}/userdelete/${id}`, { headers: this.getAuthHeaders() })
      .pipe(catchError(this.handleError<void>('deleteuser')));
  }

  deleteuserchecklist(id: string): Observable<void> {
    return this.http.delete<void>(`${this.superAdminurl}/userchecklistdelete/${id}`, { headers: this.getAuthHeaders() })
      .pipe(catchError(this.handleError<void>('deleteuserchecklist')));
  }

  blockUserChecklist(id: string): Observable<void> {
    return this.http.put<void>(`${this.superAdminurl}/blockuserchecklist/${id}`, {}, { headers: this.getAuthHeaders() })
      .pipe(catchError(this.handleError<void>('blockuserchecklist')));
  }

  unblockUserChecklist(id: string): Observable<void> {
    return this.http.put<void>(`${this.superAdminurl}/unblockuserchecklist/${id}`, {}, { headers: this.getAuthHeaders() })
      .pipe(catchError(this.handleError<void>('unblockuserchecklist')));
  }

  grantUserAccess(id: string, isActive: boolean): Observable<void> {
    return this.http.put<void>(`${this.superAdminurl}/users/${id}/grant-access/${isActive}`, { headers: this.getAuthHeaders() })
      .pipe(catchError(this.handleError<void>('grantuseraccess')));
  }

  enableuser(id: string, isActive: boolean): Observable<void> {
    return this.http.put<void>(`${this.superAdminurl}/users/${id}/blockuser/${isActive}`, { headers: this.getAuthHeaders() })
      .pipe(catchError(this.handleError<void>('enableuser')));
  }

  // -----------------------------------
  // Checklist
  // -----------------------------------

  getChecklists(): Observable<Checklist[]> {
    return this.http.get<Checklist[]>(this.apiUrl, { headers: this.getAuthHeaders() })
      .pipe(catchError(this.handleError<Checklist[]>('getChecklists', [])));
  }

  getChecklist(id: string): Observable<Checklist> {
    return this.http.get<Checklist>(`${this.apiUrl}/${id}`, { headers: this.getAuthHeaders() })
      .pipe(catchError(this.handleError<Checklist>('getChecklists', new Checklist('dummyId', 'dummyAbteilungsname', 'dummyPosition', []))));
  }

  // -----------------------------------
  // Userchecklist
  // -----------------------------------

  getUserChecklists(): Observable<UserChecklist[]> {
    return this.http.get<UserChecklist[]>(`${this.apiUserChecklistUrl}`, { headers: this.getAuthHeaders() })
      .pipe(catchError(this.handleError<UserChecklist[]>('getUserChecklists', [])));
  }

  getUserChecklist(id: string): Observable<UserChecklist> {
    return this.http.get<UserChecklist>(`${this.apiUserChecklistUrl}/${id}`, { headers: this.getAuthHeaders() })
      .pipe(catchError(this.handleError<UserChecklist>('getUserChecklist')));
  }

  createUserChecklist(userChecklist: UserChecklist): Observable<UserChecklist> {
    return this.http.post<UserChecklist>(`${this.apiUserChecklistUrl}`, userChecklist, { headers: this.getAuthHeaders() })
      .pipe(catchError(this.handleError<UserChecklist>('createUserChecklist')));
  }

  updateUserChecklist(userChecklist: UserChecklist): Observable<void> {
    return this.http.put<void>(`${this.apiUserChecklistUrl}/${userChecklist.id}`, userChecklist, { headers: this.getAuthHeaders() })
      .pipe(catchError(this.handleError<void>('updateUserChecklist')));
  }

  deleteUserChecklist(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUserChecklistUrl}/${id}`, { headers: this.getAuthHeaders() })
      .pipe(catchError(this.handleError<void>('deleteUserChecklist')));
  }

  // -----------------------------------
  // Converters
  // -----------------------------------

  private getFormattedDate(): string {
    const now = new Date();
    return `${now.getDate().toString().padStart(2, '0')}-${(now.getMonth() + 1)
      .toString().padStart(2, '0')}-${now.getFullYear()}, ${now.getHours()
      .toString().padStart(2, '0')}h_${now.getMinutes().toString().padStart(2, '0')}`;
  }

  private generateDocument(checklist: UserChecklist, url: string, ext: string): void {
    this.http.post(url, checklist, {
      responseType: 'blob',
      headers: this.getAuthHeaders()
    }).subscribe(blob => {
      this.downloadFile(blob, `${checklist.ueberschrift}_${this.getFormattedDate()}.${ext}`);
    });
  }

  generateExcel(checklist: UserChecklist): void {
    this.generateDocument(checklist, 'http://localhost:8081/api/excel/generate', 'xlsx');
  }

  generatePdf(checklist: UserChecklist): void {
    this.generateDocument(checklist, 'http://localhost:8081/api/pdf/generate', 'pdf');
  }

  generateWord(checklist: UserChecklist): void {
    this.generateDocument(checklist, 'http://localhost:8081/api/word/generate', 'docx');
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
