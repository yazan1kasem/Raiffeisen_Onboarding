import { Component, OnInit } from '@angular/core';
import { Checklist } from '../models/checklist';
import { ChecklistService } from '../data.service';
import { FormsModule } from "@angular/forms";
import {CommonModule} from "@angular/common";

@Component({
  selector: 'app-checklist',
  templateUrl: './checklist.component.html',
  styleUrls: ['./checklist.component.css'],
  imports: [
    FormsModule,
    CommonModule
  ],
  standalone: true
})
export class ChecklistComponent implements OnInit {
  checklists: Checklist[] = [];
  filteredChecklists: Checklist[] = [];
  abteilungen: string[] = [];
  positions: string[] = [];
  selectedDepartment: string = '';
  selectedPosition: string = '';

  constructor(private checklistService: ChecklistService) {}

  ngOnInit(): void {
    this.loadChecklists();
  }

  loadChecklists(): void {
    this.checklistService.getChecklists().subscribe((data: Checklist[]) => {
      this.checklists = data;
      this.filteredChecklists = data;

      // Extrahiere Abteilungen und Positionen ohne Duplikate
      this.abteilungen = [...new Set(data.map((checklist) => checklist.abteilungsname))];
      this.positions = [...new Set(data.map((checklist) => checklist.position))];
    });
  }

  onFilterChange(): void {
    this.filterChecklists();
  }

  filterChecklists(): void {
    this.filteredChecklists = this.checklists.filter((checklist) => {
      const matchesDepartment = this.selectedDepartment
        ? checklist.abteilungsname === this.selectedDepartment
        : true;

      const matchesPosition = this.selectedPosition
        ? checklist.position === this.selectedPosition
        : true;

      return matchesDepartment && matchesPosition;
    });
  }

  openChecklist(checklist: Checklist) {

  }
}
