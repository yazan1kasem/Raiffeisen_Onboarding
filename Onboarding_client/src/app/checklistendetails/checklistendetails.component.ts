import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DataService } from '../data.service';
import { Checklist } from '../models/checklist';
import { Item } from '../models/item';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-checklistendetails',
  templateUrl: './checklistendetails.component.html',
  standalone: true,
  imports: [
    FormsModule, CommonModule
  ],
  styleUrls: ['./checklistendetails.component.css'],
})
export class ChecklistendetailsComponent implements OnInit {
  checklist: Checklist | null = null;
  filteredItems: Item[] = [];
  selectedItems: Set<Item> = new Set<Item>();

  constructor(
    private route: ActivatedRoute,
    private dataService: DataService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.dataService.getChecklist(id).subscribe((checklist: Checklist) => {
        this.checklist = checklist;
        this.filteredItems = checklist.items;
        this.selectedItems = new Set<Item>();
      });
    }
  }

  toggleItemSelection(item: Item): void {
    if (this.selectedItems.has(item)) {
      this.selectedItems.delete(item);
    } else {
      this.selectedItems.add(item);
    }
  }

  saveChecklist(): void {
    if (this.checklist) {
      const updatedChecklist = new Checklist(
        this.checklist.id,
        this.checklist.abteilungsname,
        this.checklist.position,
        Array.from(this.selectedItems)
      );
      this.dataService.saveChecklist(updatedChecklist).subscribe(() => {
        this.router.navigate(['/saved-checklists']);
      });
    }
  }
}
