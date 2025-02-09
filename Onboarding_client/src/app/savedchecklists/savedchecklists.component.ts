import { Component, OnInit } from '@angular/core';
import { DataService } from '../data.service';
import { Checklist } from '../models/checklist';
import { CommonModule } from '@angular/common';
import {UserChecklist} from "../models/user_checklist";

@Component({
  selector: 'app-saved-checklists',
  templateUrl: './savedchecklists.component.html',
  standalone: true,
  styleUrls: ['./savedchecklists.component.css'],
  imports: [CommonModule]
})
export class SavedChecklistsComponent implements OnInit {
  savedChecklists: UserChecklist[] = [];

  constructor(private dataService: DataService) {}

  ngOnInit(): void {
    this.dataService.getUserChecklists().subscribe((userChecklists: UserChecklist[]) => {
      this.savedChecklists = userChecklists;
    });
  }

  openChecklist(checklistId: string): void {
    this.router.navigate(['/checklist', checklistId]); // Navigiert zur Detailseite
  }
  downloadExcel(checklist: UserChecklist): void {
    this.dataService.generateExcel(checklist);
  }

  downloadPdf(checklist: UserChecklist): void {
    this.dataService.generatePdf(checklist);
  }

  downloadWord(checklist: UserChecklist): void {
    this.dataService.generateWord(checklist);
  }
}
