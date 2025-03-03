import {
  Component,
  NgIterable,
  OnInit,
  HostListener
} from '@angular/core';
import { UserChecklist} from "../models/user_checklist";
import {Role, User} from "../models/user";
import {MatExpansionModule} from "@angular/material/expansion";
import {Checklist} from "../models/checklist";
import {FormsModule} from "@angular/forms";
import {CommonModule} from "@angular/common";
import {DataService} from "../data.service";
import {Item} from "../models/item";
import {MatListModule} from "@angular/material/list";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {MatCardModule} from "@angular/material/card";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {forkJoin} from 'rxjs';
import {MatDialog, MatDialogActions, MatDialogClose, MatDialogContent} from '@angular/material/dialog';
import {PasswortDialogComponent} from "../passwort-dialog/passwort-dialog.component";
import {MatMenu, MatMenuItem, MatMenuTrigger} from "@angular/material/menu";
import {MatSnackBar} from '@angular/material/snack-bar';
import {MatTooltip} from "@angular/material/tooltip";

/*
*  ng add @angular/material must be installed to use the MatExpansionModule
* */
@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [
    MatExpansionModule,
    FormsModule,
    CommonModule,
    MatListModule,
    MatCheckboxModule,
    MatCardModule,
    MatIconModule,
    MatButtonModule,
    MatDialogClose,
    MatDialogContent,
    MatDialogActions,
    MatMenu,
    MatMenuTrigger,
    MatMenuItem,
    MatTooltip,

  ],
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.css',

})
export class AdminComponent implements OnInit {
  search: string = "";
  user_checklists: UserChecklist[] = [];
  users: User[] = [];
  filteredUsers: User[] = [];
  checklists: Checklist[] = [];
  items: Item[] = [];
  loadingUser = true;
  loadingChecklist = true;
  protected readonly Role = Role;
  // Stacks zur Speicherung von Zuständen
  private undoStack: any[] = [];
  private redoStack: any[] = [];
  showUser: boolean = false;
  showChecklist: boolean = true;

  showButtons = true;


  constructor(
    private dialog: MatDialog,
    private dataService: DataService,
    private snackBar: MatSnackBar
  ) {}

  filterUsers() {
    const searchTerm = this.search.toLowerCase();

    this.filteredUsers = this.users.filter(user => {
      // 1) Passt Benutzername oder Rolle?
      const userMatches = user.username.toLowerCase().includes(searchTerm)
        || user.role?.toLowerCase().includes(searchTerm);

      // 2) Checklists dieses Nutzers finden
      const userChecklists = this.user_checklists.filter(
        uc => uc.user.username === user.username
      );

      // 3) Stimmt die Überschrift?
      const checklistsMatch = userChecklists.some(uc =>
        uc.ueberschrift?.toLowerCase().includes(searchTerm)
      );

      // 4) User kommt in die gefilterte Liste,
      //    wenn entweder userMatches ODER checklistsMatch true ist.
      return userMatches || checklistsMatch;
    });
  }

  @HostListener('window:keydown', ['$event'])
  handleKeyboardEvent(event: KeyboardEvent) {
    if (event.ctrlKey && event.key.toLowerCase() === 'z') {
      this.undo();
      event.preventDefault();
    } else if (event.ctrlKey && event.key.toLowerCase() === 'y') {
      this.redo();
      event.preventDefault();
    }
  }
  private getCurrentState() {
    return JSON.parse(JSON.stringify({
      checklists: this.checklists,
      items: this.items,
      users: this.users,
      user_checklists: this.user_checklists
    }));
  }
  // Zustand wiederherstellen
  private restoreState(state: any) {
    this.checklists = state.checklists;
    this.items = state.items;
    this.users = state.users;
    this.user_checklists = state.user_checklists;
    this.filterUsers();
    this.syncStateWithServer();
  }

  // Vor jeder Änderung aufrufen, um den aktuellen Zustand zu speichern
  private recordState() {
    this.undoStack.push(this.getCurrentState());
    // Nach einer neuen Aktion den Redo-Stack leeren
    this.redoStack = [];
  }

  // Undo-Funktion: Letzten Zustand wiederherstellen
  undo() {
    if (this.undoStack.length === 0) {
      this.snackBar.open('Keine Aktionen zum Rückgängig machen', 'Close', { duration: 3000 });
      return;
    }

    const lastAction = this.undoStack.pop();

    if (lastAction.type === 'checklist-update') {
      // Für Redo vormerken
      this.redoStack.push({
        type: 'checklist-update',
        oldChecklist: JSON.parse(JSON.stringify(
          this.checklists.find(c => c.id === lastAction.oldChecklist.id)
        ))
      });

      // Alte Checklist wieder einsetzen
      const index = this.checklists.findIndex(c => c.id === lastAction.oldChecklist.id);
      if (index !== -1) {
        this.checklists[index] = lastAction.oldChecklist;
        // Optional: Sofort zum Server syncen
        this.dataService.updatechecklist(this.checklists[index]).subscribe({
          next: () => {
            this.snackBar.open('Undo durchgeführt.', 'Close', { duration: 3000 });
          },
          error: () => {
            this.snackBar.open('Fehler beim Undo-Sync.', 'Close', { duration: 3000 });
          }
        });
      }
    }
    // weitere Action-Typen ...
  }


  // Redo-Funktion: Letzten rückgängig gemachten Zustand wiederherstellen
  redo() {
    if (this.redoStack.length > 0) {
      const redoState = this.redoStack.pop();
      // Aktuellen Zustand für Undo sichern
      this.undoStack.push(this.getCurrentState());
      this.restoreState(redoState);
      this.snackBar.open('Wiederhergestellt', 'Close', { duration: 3000 });
    } else {
      this.snackBar.open('Keine Aktionen zum Wiederherstellen', 'Close', { duration: 3000 });
    }
  }
  private recordChecklistState(checklist: Checklist) {
    // Kopie nur dieser Checkliste machen
    const checklistClone = JSON.parse(JSON.stringify(checklist));
    this.undoStack.push({
      type: 'checklist-update',
      oldChecklist: checklistClone
    });
    this.redoStack = [];
  }

  /**
   * Synchronisiert den aktuellen Zustand mit dem Server.
   * Hier wird beispielhaft jede Checkliste einzeln upgedated.
   * Für eine flüssigere Lösung könnte man auch einen Bulk-Update-Endpoint implementieren
   * oder nur die geänderten Felder identifizieren.
   */
  private syncStateWithServer() {
    // Beispiel: Alle Checklisten synchronisieren
    this.checklists.forEach(checklist => {
      this.dataService.updatechecklist(checklist).subscribe({
        next: () => {
          console.log(`Checkliste "${checklist.abteilungsname}" wurde erfolgreich synchronisiert.`);
        },
        error: err => {
          console.error(`Fehler beim Synchronisieren der Checkliste "${checklist.abteilungsname}":`, err);
        }
      });
    });
    // Falls auch andere Objekte (z.B. users, user_checklists) geändert wurden,
    // können hier ebenfalls entsprechende Update-Aufrufe erfolgen.
  }

  ngOnInit(): void {
    this.loadData();
  }

  private loadData() {
    this.loadingUser = true;
    this.loadingChecklist = true;

    forkJoin([
      this.dataService.getChecklists(),
      this.dataService.getuserchecklists(),
      this.dataService.getallusers()
    ]).subscribe({
      next: ([checklists, userChecklists, users]) => {
        this.checklists = checklists;
        this.user_checklists = userChecklists;
        this.users = users.sort((a, b) => a.username.localeCompare(b.username));

        // Filter bei Bedarf
        this.filterUsers();

        this.loadingChecklist = false;
        this.loadingUser = false;
      },
      error: (error) => {
        this.snackBar.open('Fehler beim Laden: ' + error, 'Close', { duration: 3000 });
      }
    });
  }

  shouldshow(superadmin: boolean): boolean {
    return superadmin;
  }

  loadUserChecklistsperuser(user: User): NgIterable<UserChecklist> {
    return this.user_checklists.filter(user_checklist => user_checklist.user.username === user.username);
  }

  promoteUser(userid: User) {
    this.recordState();
    this.dataService.promoteuser(userid.username).subscribe({
      next: () => {
        userid.role = Role.ADMIN;
        this.snackBar.open(userid.username + ' promoted to Admin', 'Close', { duration: 3000 });
      },
      error: error => {
        this.snackBar.open('Fehler beim Promoten des Nutzers: ' + error, 'Close', { duration: 3000 });
      }
    });
  }

  demoteUser(userid: User) {
    this.recordState();
    this.dataService.demoteuser(userid.username).subscribe({
      next: () => {
        userid.role = Role.USER;
        this.snackBar.open(userid.username + ' demoted to User', 'Close', { duration: 3000 });
      },
      error: error => {
        this.snackBar.open('Fehler beim Demoten des Nutzers: ' + error, 'Close', { duration: 3000 });
      }
    });
  }

  deleteUser(userid: User) {
    this.recordState();
    this.dataService.deleteuser(userid.username).subscribe({
      next: () => {
        const index = this.filteredUsers.indexOf(userid);
        if (index > -1) {
          this.filteredUsers.splice(index, 1);
        }
        this.snackBar.open(userid.username + ' gelöscht', 'Close', { duration: 3000 });
      },
      error: error => {
        this.snackBar.open('Fehler beim Löschen des Nutzers: ' + error, 'Close', { duration: 3000 });
      }
    });
  }

  deleteUserChecklist(checklistid: string) {
    this.recordState();
    this.dataService.deleteuserchecklist(checklistid).subscribe({
      next: () => {
        this.snackBar.open('User Checklist gelöscht', 'Close', { duration: 3000 });
      },
      error: error => {
        this.snackBar.open('Fehler beim Löschen der User Checklist: ' + error, 'Close', { duration: 3000 });
      }
    });
  }

  blockuserchecklist(userchecklist: UserChecklist) {
    this.recordState();
    this.dataService.blockUserChecklist(userchecklist.id).subscribe({
      next: () => {
        this.snackBar.open('User Checklist blockiert', 'Close', { duration: 3000 });
        userchecklist.isLocked=true;
      },
      error: error => {
        this.snackBar.open('Fehler beim Blockieren der User Checklist: ' + error, 'Close', { duration: 3000 });
      }
    });
  }

  unblockuserchecklist(userchecklist: UserChecklist) {
    this.recordState();
    this.dataService.unblockUserChecklist(userchecklist.id).subscribe({
      next: () => {
        this.snackBar.open('User Checklist entblockiert', 'Close', { duration: 3000 });
        userchecklist.isLocked=false;
      },
      error: error => {
        this.snackBar.open('Fehler beim Entblockieren der User Checklist: ' + error, 'Close', { duration: 3000 });
      }
    });
  }

  enableuser(userid: User, isblocked: boolean) {
    this.recordState();
    this.dataService.enableuser(userid.username, !isblocked).subscribe({
      next: () => {
        this.snackBar.open('User ' + userid.username + ' Status geändert', 'Close', { duration: 3000 });
        userid.enabled = !isblocked;
      },
      error: error => {
        this.snackBar.open('Fehler beim Ändern des Nutzerstatus: ' + error, 'Close', { duration: 3000 });
      }
    });
  }



  addItem(checklist: Checklist) {
    this.recordChecklistState(checklist);
    checklist.items.push(new Item(Date.now().toString(), '', '', ''));
  }


  removeItem(checklist: Checklist, index: number) {
    this.recordChecklistState(checklist);
    checklist.items.splice(index, 1);
  }

  saveChecklist(checklist: Checklist) {
    this.recordState();
    console.log('Gespeicherte Checkliste:', checklist);
    this.dataService.updatechecklist(checklist).subscribe({
      next: () => {
        this.snackBar.open('Checkliste gespeichert', 'Close', { duration: 3000 });
      },
      error: error => {
        this.snackBar.open('Fehler beim Speichern der Checkliste: ' + error, 'Close', { duration: 3000 });
      }
    });
  }

  changeUserPassword(user: User) {
    this.recordState();
    const dialogRef = this.dialog.open(PasswortDialogComponent, {
      width: '400px',
      panelClass: 'custom-dialog',
      disableClose: false,
      data: { user }
    });

    dialogRef.afterClosed().subscribe({
      next: result => {
        if (result) {
          this.dataService.changeuserpassword(user.username, result.newPassword).subscribe({
            next: () => {
              this.snackBar.open('Passwort geändert', 'Close', { duration: 3000 });
            },
            error: error => {
              this.snackBar.open('Fehler beim Ändern des Passworts: ' + error, 'Close', { duration: 3000 });
            }
          });
          console.log('Password change data:', result);
        }
      },
      error: error => {
        this.snackBar.open('Fehler im Dialog: ' + error, 'Close', { duration: 3000 });
      }
    });
  }

  grantaccess(user: User, b: boolean) {
    this.recordState();
    this.dataService.grantUserAccess(user.username, b).subscribe({
      next: () => {
        this.snackBar.open(
          user.username + ' Zugriff auf alle seine Checklisten ' + (b ? 'erlaubt': 'blockiert'),
          'Close',
          { duration: 3000 }
        );
        this.user_checklists.filter(user_checklist => user_checklist.user.username === user.username).forEach(user_checklist => {
          user_checklist.isLocked = !b;
        });
        },
      error: error => {
        this.snackBar.open('Fehler beim Ändern des Zugriffs: ' + error, 'Close', { duration: 3000 });
      }
    });
  }


  Showuser() {
    this.showChecklist=false;
    this.showUser=true;
  }

  ShowChecklists() {
    this.showChecklist=true;
    this.showUser=false;
  }
}

