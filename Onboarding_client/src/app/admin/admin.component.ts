import {
  Component,
  ElementRef,
  importProvidersFrom,
  inject,
  NgIterable,
  OnInit,
  TemplateRef,
  ViewChild,
  HostListener
} from '@angular/core';
import {ChecklistStatus, UserChecklist} from "../models/user_checklist";
import {Role, User} from "../models/user";
import {MatExpansionModule} from "@angular/material/expansion";
import {Checklist} from "../models/checklist";
import {FormsModule} from "@angular/forms";
import {CommonModule} from "@angular/common";
import {DataService} from "../data.service";
import {Item} from "../models/item";
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatListModule} from "@angular/material/list";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {MatCardModule} from "@angular/material/card";
import {MatButtonToggleModule} from "@angular/material/button-toggle";
import {MatIconModule} from "@angular/material/icon";
import {MatLineModule} from "@angular/material/core";
import {MatButtonModule} from "@angular/material/button";
import {BehaviorSubject, merge} from 'rxjs';
import { tap } from 'rxjs/operators';
import {MatDialog, MatDialogActions, MatDialogClose, MatDialogContent, MatDialogModule} from '@angular/material/dialog';
import {PasswortDialogComponent} from "../passwort-dialog/passwort-dialog.component";
import {MatMenu, MatMenuItem, MatMenuTrigger} from "@angular/material/menu";
import {MatSnackBar} from '@angular/material/snack-bar';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
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
    MatButtonToggleModule,
    MatIconModule,
    MatLineModule,
    MatButtonModule,
    MatInputModule,
    MatDialogClose,
    MatDialogContent,
    MatDialogActions,
    MatMenu,
    MatMenuTrigger,
    MatMenuItem
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
  loadinguser = true;
  loadingchecklist = true;
  protected readonly Role = Role;
  // Stacks zur Speicherung von Zuständen
  private undoStack: any[] = [];
  private redoStack: any[] = [];

  @ViewChild('scrollContainer', { static: false }) scrollContainer: ElementRef | undefined;

  constructor(
    private dialog: MatDialog,
    private dataservice: DataService,
    private snackBar: MatSnackBar
  ) {}

  filterUsers() {
    const searchTerm = this.search.toLowerCase();
    this.filteredUsers = this.users.filter(user =>
      user.username.toLowerCase().includes(searchTerm) ||
      user.role!.toLowerCase().includes(searchTerm)
    );
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
    if (this.undoStack.length > 0) {
      const lastState = this.undoStack.pop();
      // Aktuellen Zustand für Redo sichern
      this.redoStack.push(this.getCurrentState());
      this.restoreState(lastState);
      this.snackBar.open('Undo durchgeführt', 'Close', { duration: 3000 });
    } else {
      this.snackBar.open('Keine Aktionen zum Rückgängig machen', 'Close', { duration: 3000 });
    }
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
  /**
   * Synchronisiert den aktuellen Zustand mit dem Server.
   * Hier wird beispielhaft jede Checkliste einzeln upgedated.
   * Für eine flüssigere Lösung könnte man auch einen Bulk-Update-Endpoint implementieren
   * oder nur die geänderten Felder identifizieren.
   */
  private syncStateWithServer() {
    // Beispiel: Alle Checklisten synchronisieren
    this.checklists.forEach(checklist => {
      this.dataservice.updatechecklist(checklist).subscribe({
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

  loadData() {
    this.loadinguser = true;
    this.loadingchecklist = true;

    const users$ = this.dataservice.getallusers().pipe(
      tap(users => {
        this.users = users.sort((a, b) => a.username.localeCompare(b.username));
        this.loadinguser = false;
        this.filterUsers();
      })
    );

    const checklists$ = this.dataservice.getChecklists().pipe(
      tap(checklists => {
        this.checklists = checklists;
        this.loadingchecklist = false;
      })
    );

    const userChecklists$ = this.dataservice.getuserchecklists().pipe(
      tap(userChecklists => {
        this.user_checklists = userChecklists;
      })
    );

    const items$ = this.dataservice.getItems().pipe(
      tap(items => {
        this.items = items;
      })
    );

    merge(users$, checklists$, userChecklists$, items$).subscribe({
      error: error => {
        this.snackBar.open('Fehler beim Laden der Daten: ' + error, 'Close', { duration: 3000 });
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
    this.dataservice.promoteuser(userid.username).subscribe({
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
    this.dataservice.demoteuser(userid.username).subscribe({
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
    this.dataservice.deleteuser(userid.username).subscribe({
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
    this.dataservice.deleteuserchecklist(checklistid).subscribe({
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
    this.dataservice.blockUserChecklist(userchecklist.id).subscribe({
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
    this.dataservice.unblockUserChecklist(userchecklist.id).subscribe({
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
    this.dataservice.enableuser(userid.username, !isblocked).subscribe({
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
    this.recordState();
    checklist.items.push(new Item(Date.now().toString(), '', '', ''));
  }

  removeItem(checklist: Checklist, index: number) {
    this.recordState();
    checklist.items.splice(index, 1);
  }

  saveChecklist(checklist: Checklist) {
    this.recordState();
    console.log('Gespeicherte Checkliste:', checklist);
    this.dataservice.updatechecklist(checklist).subscribe({
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
          this.dataservice.changeuserpassword(user.username, result.newPassword).subscribe({
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
    this.dataservice.grantUserAccess(user.username, b).subscribe({
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


}

