import {
  Component,
  ElementRef,
  importProvidersFrom,
  inject,
  NgIterable,
  OnInit,
  TemplateRef,
  ViewChild
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
export class AdminComponent implements OnInit{
  search: string = "";
  user_checklists: UserChecklist[] = [];
  users: User[] = [];
  filteredUsers: User[] = [];
  checklists: Checklist[] = [];
  items: Item[] = [];
  loadinguser = true;
  loadingchecklist = true;
  scrollPosition = 0;
  @ViewChild('scrollContainer', { static: false }) scrollContainer: ElementRef | undefined;

  constructor(private dialog: MatDialog, private dataservice: DataService, private snackBar: MatSnackBar) {}

  // In `Onboarding_client/src/app/admin/admin.component.ts`
  filterUsers() {
    const searchTerm = this.search.toLowerCase();
    this.filteredUsers = this.users.filter(user =>
      user.username.toLowerCase().includes(searchTerm) ||
      user.role!.toLowerCase().includes(searchTerm)

    );
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
      tap(userChecklists => (this.user_checklists = userChecklists))
    );

    const items$ = this.dataservice.getItems().pipe(
      tap(items => (this.items = items))
    );

    merge(users$, checklists$, userChecklists$, items$).subscribe();
  }


  shouldshow(superadmin:boolean):boolean{
    return superadmin;
  }

  loadUserChecklistsperuser(user:User):NgIterable<UserChecklist>{
    return this.user_checklists.filter(user_checklist => user_checklist.user.username === user.username);
  }
  /*
  * SuperAdmin
   */

  promoteUser(userid: User) {
    this.dataservice.promoteuser(userid.username).subscribe(
    );
    userid.role=Role.ADMIN;
    this.snackBar.open(userid.username+' promoted to Admin', 'Close', { duration: 3000 });
  }
  demoteUser(userid: User) {
    this.dataservice.demoteuser(userid.username).subscribe();
    userid.role=Role.USER;
  }

  deleteUser(userid: User) {
    this.dataservice.deleteuser(userid.username). subscribe();
    this.users.splice(this.users.indexOf(userid), 1);
  }
  deleteUserChecklist( checklistid: string) {
    this.dataservice.deleteuserchecklist(checklistid). subscribe();
  }

  blockuserchecklist(userchecklistid: string) {
    this.dataservice.blockUserChecklist(userchecklistid). subscribe();
  }
  unblockuserchecklist(userchecklistid: string) {
    this.dataservice.unblockUserChecklist(userchecklistid). subscribe();
  }
  enableuser(userid: User,isblocked:boolean) {
    this.dataservice.enableuser(userid.username,!isblocked).subscribe();
  }

  reloadpage(){
    this.loadData()
  }




  addItem(checklist: Checklist) {
    checklist.items.push(new Item(Date.now().toString(), '', '', ''));
  }

  removeItem(checklist: Checklist, index: number) {
    checklist.items.splice(index, 1);
  }

  saveChecklist(checklist: Checklist) {
    console.log('Gespeicherte Checkliste:', checklist);
    this.dataservice.updatechecklist(checklist).subscribe();
  }

  changeUserPassword(user: User) {
    const dialogRef = this.dialog.open(PasswortDialogComponent, {
      width: '400px',
      panelClass: 'custom-dialog',
      disableClose: false,
      data: { user } // ✅ Pass the user object to the dialog
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.dataservice.changeuserpassword(user.username,result.newPassword).subscribe();
        console.log('Password change data:', result);
      }
    });
  }


  grantaccess(user: User, b: boolean) {
    this.dataservice.grantUserAccess(user.username, b).subscribe();
  }

  protected readonly Role = Role;
}
