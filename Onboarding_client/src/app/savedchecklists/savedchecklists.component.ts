import { Component, OnInit } from '@angular/core';
import { DataService } from '../data.service';
import { UserChecklist } from '../models/user_checklist';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

@Component({
  selector: 'app-saved-checklists',
  templateUrl: './savedchecklists.component.html',
  standalone: true,
  styleUrls: ['./savedchecklists.component.css'],
  imports: [CommonModule, MatSnackBarModule]
})
export class SavedChecklistsComponent implements OnInit {
  savedChecklists: UserChecklist[] = [];

  constructor(
    private dataService: DataService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.runSafely(() => {
      this.dataService.getUserChecklists().subscribe(
        (userChecklists: UserChecklist[]) => {
          this.savedChecklists = userChecklists;
        },
        error => this.showError('Fehler beim Laden der Checklisten.', error)
      );
    });
  }

  editChecklist(checklist: UserChecklist): void {
    this.runSafely(() => {
      if (!checklist.locked) {
        this.router.navigate(['/user-checklist', checklist.id]);
      } else {
        this.showError('Diese Checkliste ist gesperrt und kann nicht bearbeitet werden.');
      }
    });
  }

  downloadExcel(checklist: UserChecklist): void {
    this.runSafely(() => {
      this.dataService.generateExcel(checklist);
    });
  }

  downloadPdf(checklist: UserChecklist): void {
    this.runSafely(() => {
      this.dataService.generatePdf(checklist);
    });
  }

  downloadWord(checklist: UserChecklist): void {
    this.runSafely(() => {
      this.dataService.generateWord(checklist);
    });
  }

  /**
   * Führt die übergebene Funktion in einem try/catch-Block aus,
   * sodass alle synchronen Fehler abgefangen und in einer Snackbar angezeigt werden.
   */
  private runSafely(fn: () => void): void {
    try {
      fn();
    } catch (error) {
      this.showError('Ein unerwarteter Fehler ist aufgetreten.', error);
    }
  }

  /**
   * Zeigt eine Snackbar mit einer Fehlernachricht an.
   */
  private showError(message: string, error?: any): void {
    this.snackBar.open(message, 'Schließen', {
      duration: 3000,
      panelClass: ['mat-snack-bar-container'] // Nutzt das Dark Mode Styling
    });

  }
}
