import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DataService } from '../data.service';
import { Checklist } from '../models/checklist';
import { Item } from '../models/item';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { UserChecklist, ChecklistStatus } from "../models/user_checklist";
import { User } from "../models/user";
import { AuthService } from "../auth.service";
import { UserChecklistItems } from "../models/user_checklist_items";

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
  ueberschrift: string = 'new Checklist';
  user?: User;

  constructor(
    private route: ActivatedRoute,
    private dataService: DataService,
    private router: Router,
    private authservice: AuthService
  ) {}

  ngOnInit(): void {
    this.authservice.getUser().subscribe({
      next: (user: User) => {
        this.user = user;
        console.log('User loaded:', this.user);
      },
      error: (err) => {
        console.error('Error fetching user:', err);
      }
    });

    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      console.log('Checklist ID:', id);
      this.dataService.getChecklist(id).subscribe({
        next: (checklist: Checklist) => {
          this.checklist = checklist;
          this.filteredItems = checklist.items;
          this.selectedItems = new Set<Item>();
          console.log('Checklist loaded:', this.checklist);
        },
        error: (err) => {
          console.error('Error loading checklist:', err);
        }
      });
    } else {
      console.error('No checklist ID found in route');
    }
  }

  toggleItemSelection(item: Item): void {
    if (this.selectedItems.has(item)) {
      this.selectedItems.delete(item);
    } else {
      this.selectedItems.add(item);
    }
  }

  createUserChecklist(): void {
    if (!this.user) {
      console.error('Error: User is still null before saving.');
      return;
    }
    if (!this.checklist) {
      console.error('Error: Checklist is null');
      return;
    }

    console.log('User:', this.user);
    console.log('Checklist:', this.checklist);

    const userChecklistItems: UserChecklistItems[] = this.filteredItems.map((item) => {
      return new UserChecklistItems(
        item,
        this.selectedItems.has(item),
        undefined,
        new Date(),
        new Date()
      );
    });

    const updatedUserChecklist = new UserChecklist(
      "",
      this.checklist,
      ChecklistStatus.IN_PROGRESS,
      this.user,
      this.ueberschrift,
      false,
      new Date(),
      new Date(),
      userChecklistItems
    );
    console.log('User Checklist:', updatedUserChecklist);
    this.dataService.createUserChecklist(updatedUserChecklist).subscribe({
      next: () => this.router.navigate(['/saved-checklists']),
      error: (err) => console.error('Error creating user checklist:', err)
    });
  }
}
