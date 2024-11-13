import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Checklist } from './models/checklist';

@Injectable({
  providedIn: 'root'
})
export class ChecklistService {
  private apiUrl = 'http://localhost:8080/checklists';

  constructor(private http: HttpClient) {}

  getChecklists(): Observable<Checklist[]> {
    return this.http.get<Checklist[]>(this.apiUrl);
  }

  createChecklist(checklist: Checklist): Observable<Checklist> {
    return this.http.post<Checklist>(this.apiUrl, checklist);
  }

  updateChecklist(checklist: Checklist): Observable<Checklist> {
    return this.http.put<Checklist>(`${this.apiUrl}/${checklist.id}`, checklist);
  }

  deleteChecklist(checklistId: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${checklistId}`);
  }
}
