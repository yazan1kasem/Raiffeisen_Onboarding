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
  ueberschrift: string = 'Neue Checkliste';
  user?: User;
  selectedItem: Item | null = null;
  errorMessage: string = ''; // Fehlermeldung hinzufügen
  selectedType: string = '';
  itemTypes: string[] = [];

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
        console.log('Benutzer geladen:', this.user);
      },
      error: (err) => {
        console.error('Fehler beim Abrufen des Benutzers:', err);
      }
    });

    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      console.log('Checklisten-ID:', id);
      this.dataService.getChecklist(id).subscribe({
        next: (checklist: Checklist) => {
          this.checklist = checklist;
          this.filteredItems = checklist.items;
          this.selectedItems = new Set<Item>();
          this.itemTypes = [...new Set(checklist.items.map(item => item.type))];
          console.log('Checkliste geladen:', this.checklist);
        },
        error: (err) => {
          console.error('Fehler beim Laden der Checkliste:', err);
        }
      });
    } else {
      console.error('Keine Checklisten-ID in der Route gefunden');
    }
  }

  toggleItemSelection(item: Item): void {
    if (this.selectedItems.has(item)) {
      this.selectedItems.delete(item);
    } else {
      this.selectedItems.add(item);
    }
  }

  toggleInfoBox(item: Item): void {
    this.selectedItem = this.selectedItem === item ? null : item;
  }

  filterItemsByType(): void {
    if (this.selectedType) {
      this.filteredItems = this.checklist?.items.filter(item => item.type === this.selectedType) || [];
    } else {
      this.filteredItems = this.checklist?.items || [];
    }
  }

  createUserChecklist(): void {
    this.errorMessage = ''; // Fehlermeldung zurücksetzen

    const titleRegex = /^[a-zA-Z0-9\s]{1,25}$/;

    if (!titleRegex.test(this.ueberschrift)) {
      this.errorMessage = 'Der Titel darf maximal 25 Zeichen enthalten und keine unerwünschten Zeichen.';
      return;
    }

    if (!this.user) {
      console.error('Fehler: Benutzer ist vor dem Speichern noch null.');
      return;
    }
    if (!this.checklist) {
      console.error('Fehler: Checkliste ist null');
      return;
    }

    console.log('Benutzer:', this.user);
    console.log('Checkliste:', this.checklist);

    const currentDate = new Date();

    const userChecklistItems: UserChecklistItems[] = this.filteredItems.map((item) => {
      return new UserChecklistItems(
        item,
        this.selectedItems.has(item),
        undefined,
        currentDate, // Aktualisierungsdatum für jedes Item setzen
        currentDate
      );
    });

    const updatedUserChecklist = new UserChecklist(
      "",
      this.checklist,
      ChecklistStatus.IN_PROGRESS,
      this.user,
      this.ueberschrift,
      false,
      currentDate, // Aktualisierungsdatum für die Checkliste setzen
      currentDate,
      userChecklistItems
    );
    console.log('Benutzer-Checkliste:', updatedUserChecklist);
    this.dataService.createUserChecklist(updatedUserChecklist).subscribe({
      next: () => this.router.navigate(['/saved-checklists']),
      error: (err) => {
        if (err.status === 0) {
          this.errorMessage = 'Keine Internetverbindung.';
        } else if (err.status === 404) {
          this.errorMessage = 'Liste wurde nicht gefunden.';
        } else {
          this.errorMessage = `Fehler: ${err.status} - ${err.message}`;
        }
        console.error('Fehler beim Erstellen der Benutzer-Checkliste:', err);
      }
    });
  }
}
