import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DataService } from '../data.service';
import { UserChecklist } from '../models/user_checklist';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-user-checklist',
  templateUrl: './user-checklist.component.html',
  standalone: true,
  styleUrls: ['./user-checklist.component.css'],
  imports: [FormsModule, CommonModule]
})
export class UserChecklistComponent implements OnInit {
  userChecklist: UserChecklist | null = null;

  constructor(
    private route: ActivatedRoute,
    private dataService: DataService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.dataService.getUserChecklist(id).subscribe({
        next: (userChecklist: UserChecklist) => {
          this.userChecklist = userChecklist;
        },
        error: (err) => {
          console.error('Error loading user checklist:', err);
        }
      });
    } else {
      console.error('No user checklist ID found in route');
    }
  }

  saveUserChecklist(): void {
    if (!this.userChecklist) {
      console.error('Error: User checklist is null');
      return;
    }

    this.dataService.updateUserChecklist(this.userChecklist).subscribe({
      next: () => this.router.navigate(['/saved-checklists']),
      error: (err) => console.error('Error saving user checklist:', err)
    });
  }
}
