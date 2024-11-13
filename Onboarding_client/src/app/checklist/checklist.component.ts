import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ChecklistService } from '../data.service';
import { Checklist } from '../models/checklist';
import { CommonModule } from "@angular/common";

@Component({
  selector: 'app-checklist',
  standalone: true,
  templateUrl: './checklist.component.html',
  styleUrls: ['./checklist.component.css'],
  imports: [CommonModule, FormsModule],
})
export class ChecklistComponent implements OnInit {
  checklists: Checklist[] = [];
  editingChecklist: Checklist | null = null;


  checklistName: string = '';
  checklistDepartment: string = '';
  checklistPosition: string = '';

  constructor(private checklistService: ChecklistService) {}

  ngOnInit(): void {
    this.loadChecklists();
  }

  loadChecklists(): void {
    this.checklistService.getChecklists().subscribe(data => {
      this.checklists = data;
    });
  }

  onSubmit(): void {
    const newChecklist = {
      name: this.checklistName,
      department: this.checklistDepartment,
      position: this.checklistPosition
    };


  }

  editChecklist(checklist: Checklist): void {
    this.editingChecklist = checklist;


  }

  deleteChecklist(checklistId: string): void {
    this.checklistService.deleteChecklist(checklistId).subscribe(() => {
      this.loadChecklists();
    });
  }

  resetForm(): void {
    this.editingChecklist = null;
    this.checklistName = '';
    this.checklistDepartment = '';
    this.checklistPosition = '';
  }
}
