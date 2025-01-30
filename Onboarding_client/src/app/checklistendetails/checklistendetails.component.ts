import {Component, Input, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {DataService} from '../data.service';
import {Checklist} from '../models/checklist';
import {Item} from '../models/item';
import {AuthService} from '../auth.service';
import {CommonModule, NgClass, NgForOf} from "@angular/common";
import {ChecklistStatus, UserChecklist} from "../models/user_checklist";
import {User} from "../models/user";
import {firstValueFrom} from "rxjs";

@Component({
  selector: 'app-checklistendetails',
  templateUrl: './checklistendetails.component.html',
  styleUrls: ['./checklistendetails.component.css'],
  standalone: true,
  imports: [
    NgForOf,
    NgClass,
    CommonModule
  ]
})
export class ChecklistendetailsComponent implements OnInit {
  checklist!: Checklist;
  selectedItems: Set<Item> = new Set<Item>();
  currentuser!: User; // Typ und Variable deklarieren
  @Input() id: string = "";
  constructor(
    private route: ActivatedRoute,
    private dataservice: DataService,
    private authService: AuthService
  ) {}

  async ngOnInit(): Promise<void> {


    if (this.id) {
      this.loadChecklist(this.id);
    }
  }

  loadChecklist(id: string): void {
    this.dataservice.getChecklist(id).subscribe((data: Checklist) => {
      this.checklist = data;
    });
  }

  toggleItemSelection(item: Item): void {
    if (this.selectedItems.has(item)) {
      this.selectedItems.delete(item);
    } else {
      this.selectedItems.add(item);
    }
  }



  saveSelectedItems(): void {

  }
}
