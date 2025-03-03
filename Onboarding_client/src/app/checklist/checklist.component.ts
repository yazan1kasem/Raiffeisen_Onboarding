import { Component, OnInit } from '@angular/core';
import { DataService } from '../data.service';
import { Checklist } from '../models/checklist';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-checklist',
  templateUrl: './checklist.component.html',
  styleUrls: ['./checklist.component.css'],
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class ChecklistComponent implements OnInit {
  checklists: Checklist[] = [];
  filteredChecklists: Checklist[] = [];
  abteilungen: string[] = [];
  positions: string[] = [];
  selectedDepartment: string = '';
  selectedPosition: string = '';

  constructor(
    private checklistService: DataService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadChecklists();
  }

  loadChecklists(): void {
    this.checklistService.getChecklists().subscribe((data: Checklist[]) => {
      // Gespeicherte Checklisten und Filterliste setzen
      this.checklists = data;
      this.filteredChecklists = data;

      // Eindeutige Abteilungen extrahieren
      this.abteilungen = [...new Set(data.map(checklist => checklist.abteilungsname))];

      // Filteroptionen aktualisieren
      this.updatePositions();
      this.updateDepartments();
    });
  }

  onFilterChange(): void {
    // Aktualisiere die Positionen, wende Filter an und extrahiere Abteilungen neu
    this.updatePositions();
    this.filterChecklists();
    this.updateDepartments();
  }

  updatePositions(): void {
    if (this.selectedDepartment) {
      // Filtere Positionen anhand der ausgewählten Abteilung und entferne Duplikate
      this.positions = [...new Set(
        this.checklists
          .filter(checklist => checklist.abteilungsname === this.selectedDepartment)
          .map(checklist => checklist.position)
      )];
    } else {
      // Alle Positionen ohne Filterung als eindeutige Werte
      this.positions = [...new Set(this.checklists.map(checklist => checklist.position))];
    }
  }

// Aktualisierte updateDepartments()-Funktion:
  updateDepartments(): void {
    if (this.selectedPosition) {
      // Falls eine Position ausgewählt wurde, werden nur die Abteilungen ermittelt,
      // in denen Checklisten mit dieser Position vorkommen
      this.abteilungen = [...new Set(
        this.checklists
          .filter(checklist => checklist.position === this.selectedPosition)
          .map(checklist => checklist.abteilungsname)
      )];
    } else {
      // Andernfalls alle Abteilungen aus allen Checklisten
      this.abteilungen = [...new Set(this.checklists.map(checklist => checklist.abteilungsname))];
    }
  }


  filterChecklists(): void {
    this.filteredChecklists = this.checklists.filter((checklist) => {
      const matchesDepartment = this.selectedDepartment
        ? checklist.abteilungsname === this.selectedDepartment
        : true;

      const matchesPosition = this.selectedPosition
        ? checklist.position === this.selectedPosition
        : true;

      return matchesDepartment && matchesPosition;
    });
  }

  openChecklist(id: string): void {
    this.router.navigate(['/checklistendetails', id]);
  }
}
