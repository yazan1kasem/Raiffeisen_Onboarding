import { Component, OnInit } from '@angular/core';
import { DataService } from '../data.service';
import { Checklist } from '../models/checklist';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-checklist',
  templateUrl: './checklist.component.html',
  styleUrls: ['./checklist.component.css'],
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class ChecklistComponent implements OnInit {
  checklists: Checklist[] = [];
  filteredChecklists: Checklist[] = [];
  abteilungen: string[] = [];
  positions: string[] = [];
  selectedDepartment: string = '';
  selectedPosition: string = '';

  constructor(
    private checklistService: DataService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadChecklists();
  }

  loadChecklists(): void {
    this.checklistService.getChecklists().subscribe((data: Checklist[]) => {
      this.checklists = data;
      this.filteredChecklists = data;

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

  openChecklist(id: string): void {
    this.router.navigate(['/checklistendetails', id]);
  }
}
