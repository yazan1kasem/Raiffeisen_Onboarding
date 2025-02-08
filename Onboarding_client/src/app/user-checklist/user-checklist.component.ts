import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DataService } from '../data.service';
import { Checklist } from '../models/checklist';

@Component({
  selector: 'app-user-checklist',
  templateUrl: './user-checklist.component.html',
  standalone: true,
  styleUrls: ['./user-checklist.component.css']
})
export class UserChecklistComponent implements OnInit {
  checklist: Checklist | null = null;

  constructor(private route: ActivatedRoute, private dataService: DataService) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.dataService.getChecklist(id).subscribe((checklist: Checklist) => {
        this.checklist = checklist;
      });
    }
  }
}
