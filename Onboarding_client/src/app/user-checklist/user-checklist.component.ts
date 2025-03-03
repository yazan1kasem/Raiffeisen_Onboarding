import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DataService } from '../data.service';
import { ChecklistStatus, UserChecklist } from '../models/user_checklist';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { UserChecklistItems } from '../models/user_checklist_items';

@Component({
  selector: 'app-user-checklist',
  templateUrl: './user-checklist.component.html',
  standalone: true,
  styleUrls: ['./user-checklist.component.css'],
  imports: [FormsModule, CommonModule]
})
export class UserChecklistComponent implements OnInit {
  userChecklist: UserChecklist | null = null;
  selectedItem: UserChecklistItems | null = null;

  constructor(
    private route: ActivatedRoute,
    private dataService: DataService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.dataService.getUserChecklist(id).subscribe({
        next: (userChecklist: UserChecklist) => {
          this.userChecklist = userChecklist;
          this.updateChecklistStatus();
        },
        error: (err) => {
          console.error('Fehler beim Laden der Benutzer-Checkliste:', err);
        }
      });
    } else {
      console.error('Keine Benutzer-Checklisten-ID in der Route gefunden');
    }
  }

  toggleInfoBox(item: UserChecklistItems): void {
    if (!this.userChecklist?.locked) {
      this.selectedItem = this.selectedItem === item ? null : item;
    }
    item.isChecked=!item.isChecked;
  }

  saveUserChecklist(): void {
    if (!this.userChecklist || this.userChecklist.locked) {
      console.error('Fehler: Benutzer-Checkliste ist null oder gesperrt');
      return;
    }

    const updatedChecklist = { ...this.userChecklist};

    this.dataService.updateUserChecklist(updatedChecklist).subscribe({
      next: () => this.router.navigate(['/saved-checklists']),
      error: (err) => console.error('Fehler beim Speichern der Benutzer-Checkliste:', err)
    });
  }

  deleteUserChecklist(): void {
    if (!this.userChecklist || this.userChecklist.locked) {
      console.error('Fehler: Benutzer-Checkliste ist null oder gesperrt');
      return;
    }

    this.dataService.deleteUserChecklist(this.userChecklist.id).subscribe({
      next: () => this.router.navigate(['/saved-checklists']),
      error: (err) => console.error('Fehler beim Löschen der Benutzer-Checkliste:', err)
    });
  }

  downloadExcel(): void {
    if (this.userChecklist) {
      this.dataService.generateExcel(this.userChecklist);
    } else {
      console.error('Keine Benutzer-Checkliste zum Herunterladen verfügbar');
    }
  }

  downloadPdf(): void {
    if (this.userChecklist) {
      this.dataService.generatePdf(this.userChecklist);
    } else {
      console.error('Keine Benutzer-Checkliste zum Herunterladen verfügbar');
    }
  }

  downloadWord(): void {
    if (this.userChecklist) {
      this.dataService.generateWord(this.userChecklist);
    } else {
      console.error('Keine Benutzer-Checkliste zum Herunterladen verfügbar');
    }
  }

  protected updateChecklistStatus(): void {
    if (this.userChecklist) {
      const allItemsChecked = this.userChecklist.useritems.every(item => item.isChecked);
      this.userChecklist.status = allItemsChecked ? ChecklistStatus.COMPLETED : ChecklistStatus.IN_PROGRESS;
    }
  }

  protected readonly ChecklistStatus = ChecklistStatus;
}
