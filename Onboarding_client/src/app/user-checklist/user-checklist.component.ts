import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DataService } from '../data.service';
import { UserChecklist } from '../models/user_checklist';
import { UserChecklistItems } from '../models/user_checklist_items';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-user-checklist',
  templateUrl: './user-checklist.component.html',
  styleUrls: ['./user-checklist.component.css'],
  standalone: true,
  imports: [CommonModule]
})
export class UserChecklistComponent implements OnInit {
  userChecklist: UserChecklist | null = null;
  userchecklistItems: UserChecklistItems[] = [];

  constructor(private route: ActivatedRoute, private dataService: DataService) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.dataService.getUserChecklist(id).subscribe((userChecklist: UserChecklist) => {
        this.userChecklist = userChecklist;
        this.userchecklistItems = userChecklist.useritems || []; // Ensure items is defined
      });
    }
  }
}
