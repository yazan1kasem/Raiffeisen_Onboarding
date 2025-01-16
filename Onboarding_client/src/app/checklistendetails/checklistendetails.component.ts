import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Dataservice} from '../data.service';
import {Checklist} from '../models/checklist';
import {Item} from '../models/item';
import {user_Items} from '../models/user_Items';
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

  constructor(
    private route: ActivatedRoute,
    private dataservice: Dataservice,
    private authService: AuthService
  ) {}

  async ngOnInit(): Promise<void> {
    // Benutzer laden
    this.currentuser = await this.getUser();

    // Checkliste laden
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loadChecklist(parseInt(id, 10));
    }
  }

  loadChecklist(id: number): void {
    this.dataservice.getChecklistById(id).subscribe((data: Checklist) => {
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

  async getUser(): Promise<User> {
    return firstValueFrom(this.dataservice.getCurrentuser());
  }

  saveSelectedItems(): void {
    if (!this.currentuser) {
      console.error("Benutzer nicht geladen.");
      return;
    }

    const userChecklist: UserChecklist = new UserChecklist(
      "",
      this.checklist,
      this.currentuser,
      Array.from(this.selectedItems).map(item => {
        return new user_Items(
          "",
          item,
          true,
          new Date(),
          new Date()
        );
      }),
      ChecklistStatus.COMPLETED,
      {},
      false,
      new Date(),
      new Date()
    );

    console.log("User Checklist to Save:", userChecklist);

    this.dataservice.createUserChecklist(userChecklist).subscribe(
      response => console.log(response),
      error => console.error("Fehler beim Speichern der Checkliste:", error)
    );
  }
}
