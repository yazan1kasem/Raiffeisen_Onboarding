import { Component, OnInit } from '@angular/core';
import { DataService } from '../data.service';
import { Checklist } from '../models/checklist';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-saved-checklists',
  templateUrl: './savedchecklists.component.html',
  standalone: true,
  styleUrls: ['./savedchecklists.component.css'],
  imports: [CommonModule]
})
export class SavedChecklistsComponent implements OnInit {
  savedChecklists: Checklist[] = [];

  constructor(private dataService: DataService) {}

  ngOnInit(): void {
    this.dataService.getSavedChecklists().subscribe((checklists: Checklist[]) => {
      this.savedChecklists = checklists;
    });
  }
}
