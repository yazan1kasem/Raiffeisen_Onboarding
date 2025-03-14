import {
  Component,
  OnInit,
  HostListener,
  NgIterable
} from '@angular/core';
import { forkJoin } from 'rxjs';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';

import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatListModule } from '@angular/material/list';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatMenu } from '@angular/material/menu';
import { MatMenuItem } from '@angular/material/menu';
import { MatMenuTrigger } from '@angular/material/menu';
import { MatTooltip } from '@angular/material/tooltip';

import { DataService } from '../data.service';
import { PasswortDialogComponent } from '../passwort-dialog/passwort-dialog.component';

import { UserChecklist } from '../models/user_checklist';
import { Role, User } from '../models/user';
import { Checklist } from '../models/checklist';
import { Item } from '../models/item';
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";

/**
 * The AdminComponent provides an administrative interface for:
 *  - Managing users (create, promote, demote, enable/disable)
 *  - Managing checklists (view, add/remove items, save)
 *  - Handling user checklists (block/unblock, delete)
 *
 * Requires:
 *   ng add @angular/material
 * for Angular Material components such as MatExpansionModule, MatDialog, etc.
 */
@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [
    // Angular & Common
    CommonModule,
    FormsModule,
    MatProgressSpinnerModule,

    // Angular Material modules
    MatExpansionModule,
    MatListModule,
    MatCheckboxModule,
    MatCardModule,
    MatIconModule,
    MatButtonModule,
    MatMenu,
    MatMenuTrigger,
    MatMenuItem,
    MatTooltip
  ],
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {

  /** Search term used to filter the user list. */
  search: string = '';

  /** Arrays holding server-side data. */
  user_checklists: UserChecklist[] = [];
  users: User[] = [];
  filteredUsers: User[] = [];
  checklists: Checklist[] = [];
  items: Item[] = [];

  /** Loading states for conditionally showing spinners or placeholders. */
  loadingUser = true;
  loadingChecklist = true;

  /** Expose Role enum so it can be used in the template. */
  protected readonly Role = Role;

  /**
   * Stacks to store application states for undo/redo functionality.
   * push() states before changes, pop() them during undo/redo.
   */
  private undoStack: any[] = [];
  private redoStack: any[] = [];

  /** Toggles for showing user or checklist sections. */
  showUser: boolean = false;
  showChecklist: boolean = true;

  /** Toggles display of floating action buttons at the bottom. */
  showButtons: boolean = true;

  /**
   * @param dialog     Angular Material Dialog service for password changes, etc.
   * @param dataService   Custom service for data retrieval & server sync.
   * @param snackBar   Angular Material Snackbar for quick messages.
   */
  constructor(
    private dialog: MatDialog,
    private dataService: DataService,
    private snackBar: MatSnackBar
  ) {}

  /**
   * Filters the user list based on `this.search`. Considers username, role,
   * and any matching user checklists' headlines.
   */
  filterUsers(): void {
    const searchTerm = this.search.toLowerCase();

    this.filteredUsers = this.users.filter(user => {
      // (1) Match by username or role
      const userMatches =
        user.username.toLowerCase().includes(searchTerm) ||
        user.role?.toLowerCase().includes(searchTerm);

      // (2) Get checklists belonging to this user
      const userChecklists = this.user_checklists.filter(
        uc => uc.user.username === user.username
      );

      // (3) Match by the 'ueberschrift' (headline) of each user checklist
      const checklistsMatch = userChecklists.some(uc =>
        uc.ueberschrift?.toLowerCase().includes(searchTerm)
      );

      // (4) Return if match is found in user fields OR checklist headlines
      return userMatches || checklistsMatch;
    });
    this.pageIndex = 0; // Reset to page 1 (index 0) on every new filter

  }

  /**
   * Keydown listener for Undo/Redo functionality:
   * - Ctrl+Z => undo()
   * - Ctrl+Y => redo()
   */
  @HostListener('window:keydown', ['$event'])
  handleKeyboardEvent(event: KeyboardEvent): void {
    if (event.ctrlKey && event.key.toLowerCase() === 'z') {
      this.undo();
      event.preventDefault();
    } else if (event.ctrlKey && event.key.toLowerCase() === 'y') {
      this.redo();
      event.preventDefault();
    }
  }

  /**
   * Returns a deep copy of the current component state for undo/redo operations.
   */
  private getCurrentState(): any {
    return JSON.parse(
      JSON.stringify({
        checklists: this.checklists,
        items: this.items,
        users: this.users,
        user_checklists: this.user_checklists
      })
    );
  }

  /**
   * Restores the component state (checklists, items, users, user_checklists)
   * and re-applies filter logic and server sync.
   */
  private restoreState(state: any): void {
    this.checklists = state.checklists;
    this.items = state.items;
    this.users = state.users;
    this.user_checklists = state.user_checklists;
    this.filterUsers();
    this.syncStateWithServer();
  }

  /**
   * Records the current state onto the undo stack before a mutation,
   * and clears the redo stack.
   */
  private recordState(): void {
    this.undoStack.push(this.getCurrentState());
    this.redoStack = [];
  }

  /**
   * Undo the last recorded change (if any).
   * Example shows a ‘checklist-update’ case.
   */
  undo(): void {
    if (this.undoStack.length === 0) {
      this.snackBar.open('Keine Aktionen zum Rückgängig machen', 'Close', {
        duration: 3000
      });
      return;
    }

    const lastAction = this.undoStack.pop();

    if (lastAction.type === 'checklist-update') {
      // Prepare for redo
      this.redoStack.push({
        type: 'checklist-update',
        oldChecklist: JSON.parse(
          JSON.stringify(
            this.checklists.find(c => c.id === lastAction.oldChecklist.id)
          )
        )
      });

      // Revert to old checklist
      const index = this.checklists.findIndex(
        c => c.id === lastAction.oldChecklist.id
      );
      if (index !== -1) {
        this.checklists[index] = lastAction.oldChecklist;
        // Optionally sync with server
        this.dataService.updatechecklist(this.checklists[index]).subscribe({
          next: () => {
            this.snackBar.open('Undo durchgeführt.', 'Close', {
              duration: 3000
            });
          },
          error: () => {
            this.snackBar.open('Fehler beim Undo-Sync.', 'Close', {
              duration: 3000
            });
          }
        });
      }
    }

    // ...Handle other action types here as needed
  }

  /**
   * Redo the last undone action (if any).
   */
  redo(): void {
    if (this.redoStack.length > 0) {
      const redoState = this.redoStack.pop();
      // Record current state for a possible future undo
      this.undoStack.push(this.getCurrentState());
      this.restoreState(redoState);
      this.snackBar.open('Wiederhergestellt', 'Close', { duration: 3000 });
    } else {
      this.snackBar.open('Keine Aktionen zum Wiederherstellen', 'Close', {
        duration: 3000
      });
    }
  }

  /**
   * Records the current (single) checklist state before modifications
   * so it can be reverted with undo.
   */
  private recordChecklistState(checklist: Checklist): void {
    const checklistClone = JSON.parse(JSON.stringify(checklist));
    this.undoStack.push({
      type: 'checklist-update',
      oldChecklist: checklistClone
    });
    this.redoStack = [];
  }

  /**
   * Synchronizes the current state with the server.
   * Example: Iterates through all checklists and updates each individually.
   * In a production setting, consider implementing bulk endpoints or partial updates.
   */
  private syncStateWithServer(): void {
    this.checklists.forEach(checklist => {
      this.dataService.updatechecklist(checklist).subscribe({
        next: () => {
          console.log(
            `Checkliste "${checklist.abteilungsname}" synchronisiert.`
          );
        },
        error: err => {
          console.error(
            `Fehler beim Synchronisieren der Checkliste "${checklist.abteilungsname}":`,
            err
          );
        }
      });
    });
    // For other objects (users, user_checklists, etc.), call corresponding APIs here.
  }

  /**
   * Lifecycle hook: Loads data (checklists, user checklists, users)
   * from the DataService upon component initialization.
   */
  ngOnInit(): void {
    this.loadData();
  }

  /**
   * Fetches checklists, user checklists, and users from the server in parallel,
   * then sorts the user list and sets loading states to false.
   */
  private loadData(): void {
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

        // Filter the loaded data if needed
        this.filterUsers();
        console.log(userChecklists);


        this.loadingChecklist = false;
        this.loadingUser = false;
      },
      error: error => {
        this.snackBar.open('Fehler beim Laden: ' + error, 'Close', {
          duration: 3000
        });
      }
    });
  }

  /**
   * Determines if superadmin-exclusive actions should be shown.
   * @param superadmin - boolean (unused example logic).
   */
  shouldshow(): boolean {
    if(localStorage.getItem('role') === 'SUPER_ADMIN'){
      return true;
    }
    return false;
  }

  /**
   * Returns all user checklists belonging to a specific user.
   */
  loadUserChecklistsperuser(user: User): NgIterable<UserChecklist> {
    return this.user_checklists.filter(
      user_checklist => user_checklist.user.username === user.username
    );
  }

  /**
   * Promotes a user to Admin.
   */
  promoteUser(userid: User): void {
    this.recordState();
    this.dataService.promoteuser(userid.username).subscribe({
      next: () => {
        userid.role = Role.ADMIN;
        this.snackBar.open(
          userid.username + ' promoted to Admin',
          'Close',
          { duration: 3000 }
        );
      },
      error: error => {
        this.snackBar.open(
          'Fehler beim Promoten des Nutzers: ' + error,
          'Close',
          { duration: 3000 }
        );
      }
    });
  }

  /**
   * Demotes an Admin user back to standard User role.
   */
  demoteUser(userid: User): void {
    this.recordState();
    this.dataService.demoteuser(userid.username).subscribe({
      next: () => {
        userid.role = Role.USER;
        this.snackBar.open(
          userid.username + ' demoted to User',
          'Close',
          { duration: 3000 }
        );
      },
      error: error => {
        this.snackBar.open(
          'Fehler beim Demoten des Nutzers: ' + error,
          'Close',
          { duration: 3000 }
        );
      }
    });
  }

  /**
   * Permanently deletes a user from the system.
   */
  deleteUser(userid: User): void {
    this.recordState();
    this.dataService.deleteuser(userid.username).subscribe({
      next: () => {
        const index = this.filteredUsers.indexOf(userid);
        if (index > -1) {
          this.filteredUsers.splice(index, 1);
        }
        this.snackBar.open(userid.username + ' gelöscht', 'Close', {
          duration: 3000
        });
      },
      error: error => {
        this.snackBar.open(
          'Fehler beim Löschen des Nutzers: ' + error,
          'Close',
          { duration: 3000 }
        );
      }
    });
  }

  /**
   * Deletes a specific user checklist by ID.
   */
  deleteUserChecklist(checklistid: string): void {
    this.recordState();
    this.dataService.deleteuserchecklist(checklistid).subscribe({
      next: () => {
        this.snackBar.open('User Checklist gelöscht', 'Close', {
          duration: 3000
        });
      },
      error: error => {
        this.snackBar.open(
          'Fehler beim Löschen der User Checklist: ' + error,
          'Close',
          { duration: 3000 }
        );
      }
    });
  }

  /**
   * Blocks (locks) a user's checklist.
   */
  blockuserchecklist(userchecklist: UserChecklist): void {
    this.recordState();
    this.dataService.blockUserChecklist(userchecklist.id).subscribe({
      next: () => {
        this.snackBar.open('User Checklist blockiert', 'Close', {
          duration: 3000
        });
        userchecklist.locked = true;
      },
      error: error => {
        this.snackBar.open(
          'Fehler beim Blockieren der User Checklist: ' + error,
          'Close',
          { duration: 3000 }
        );
      }
    });
  }

  /**
   * Unblocks (unlocks) a user's checklist.
   */
  unblockuserchecklist(userchecklist: UserChecklist): void {
    this.recordState();
    this.dataService.unblockUserChecklist(userchecklist.id).subscribe({
      next: () => {
        this.snackBar.open('User Checklist entblockiert', 'Close', {
          duration: 3000
        });
        userchecklist.locked = false;
      },
      error: error => {
        this.snackBar.open(
          'Fehler beim Entblockieren der User Checklist: ' + error,
          'Close',
          { duration: 3000 }
        );
      }
    });
  }

  /**
   * Enables or disables a user (passed as `isblocked` boolean).
   * If `isblocked` is true, sets user to disabled; otherwise re-enables them.
   */
  enableuser(userid: User, isblocked: boolean): void {
    this.recordState();
    this.dataService.enableuser(userid.username, !isblocked).subscribe({
      next: () => {
        this.snackBar.open(
          'User ' + userid.username + ' Status geändert',
          'Close',
          { duration: 3000 }
        );
        userid.enabled = !isblocked;
      },
      error: error => {
        this.snackBar.open(
          'Fehler beim Ändern des Nutzerstatus: ' + error,
          'Close',
          { duration: 3000 }
        );
      }
    });
  }

  /**
   * Adds a new blank item to the specified checklist.
   */
  addItem(checklist: Checklist): void {
    this.recordChecklistState(checklist);
    checklist.items.push(
      new Item(Date.now().toString(), '', '', '')
    );
  }

  /**
   * Removes an item from the specified checklist at a given index.
   */
  removeItem(checklist: Checklist, index: number): void {
    this.recordChecklistState(checklist);
    checklist.items.splice(index, 1);
  }

  /**
   * Persists the changes in a checklist to the server.
   */
  saveChecklist(checklist: Checklist): void {
    this.recordState();
    console.log('Gespeicherte Checkliste:', checklist);
    this.dataService.updatechecklist(checklist).subscribe({
      next: () => {
        this.snackBar.open('Checkliste gespeichert', 'Close', {
          duration: 3000
        });
      },
      error: error => {
        this.snackBar.open(
          'Fehler beim Speichern der Checkliste: ' + error,
          'Close',
          { duration: 3000 }
        );
      }
    });
  }

  /**
   * Opens a dialog to change a user’s password.
   * If a new password is provided, calls the service to update it.
   */
  changeUserPassword(user: User): void {
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
              this.snackBar.open(
                'Fehler beim Ändern des Passworts: ' + error,
                'Close',
                { duration: 3000 }
              );
            }
          });
          console.log('Password change data:', result);
        }
      },
      error: error => {
        this.snackBar.open('Fehler im Dialog: ' + error, 'Close', {
          duration: 3000
        });
      }
    });
  }

  /**
   * Grant or revoke (block) access to all checklists for a particular user.
   * If `b` is true, grant access; otherwise revoke it.
   */
  grantaccess(user: User, b: boolean): void {
    this.recordState();
    this.dataService.grantUserAccess(user.username, b).subscribe({
      next: () => {
        this.snackBar.open(
          user.username +
          ' Zugriff auf alle seine Checklisten ' +
          (b ? 'erlaubt' : 'blockiert'),
          'Close',
          { duration: 3000 }
        );
        // Update local user_checklists for this user
        this.user_checklists
          .filter(uc => uc.user.username === user.username)
          .forEach(uc => {
            uc.locked = !b;
          });
      },
      error: error => {
        this.snackBar.open(
          'Fehler beim Ändern des Zugriffs: ' + error,
          'Close',
          { duration: 3000 }
        );
      }
    });
  }

  /**
   * Shows the user management section and hides the checklists view.
   */
  Showuser(): void {
    this.showChecklist = false;
    this.showUser = true;
  }

  /**
   * Shows the checklist management section and hides the user view.
   */
  ShowChecklists(): void {
    this.showChecklist = true;
    this.showUser = false;
  }

  // Pagination properties
  pageIndex: number = 0;
  pageSize: number = 20;

  /**
   * Computes the total number of pages based on the length
   * of filteredUsers and the pageSize (20).
   */
  get totalPages(): number {
    return Math.ceil(this.filteredUsers.length / this.pageSize);
  }

  /**
   * Returns only the slice of filteredUsers that should
   * appear on the current page.
   */
  get pagedFilteredUsers(): User[] {
    const start = this.pageIndex * this.pageSize;
    const end = start + this.pageSize;
    return this.filteredUsers.slice(start, end);
  }

  /** Navigates to the previous page if not on the first page already. */
  previousPage(): void {
    if (this.pageIndex > 0) {
      this.pageIndex--;

    }
    window.scrollTo({ top: document.body.scrollHeight});

  }

  /** Navigates to the next page if not on the last page already. */
  nextPage(): void {
    if (this.pageIndex < this.totalPages - 1) {
      this.pageIndex++;
    }
    window.scrollTo({ top: document.body.scrollHeight});
  }

// Speichert, welche Panels geöffnet sind (ID -> boolean)
  panelExpanded: { [key: string]: boolean } = {};

  trackByChecklist(index: number, checklist: Checklist): any {
    return checklist.id; // Eindeutige Kennung
  }

  trackByItem(index: number, item: Item): any {
    return item.id || index;
  }



}
