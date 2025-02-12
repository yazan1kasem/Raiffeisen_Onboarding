import {Component, importProvidersFrom, NgIterable} from '@angular/core';
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
    MatButtonModule
  ],
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.css',

})
export class AdminComponent {
  user_checklists: UserChecklist[] =[];
  users: User[] = [];
  checklists:Checklist[]=[];
  items:Item[]=[];



  constructor(private dataservice:DataService) { }

  ngOnInit(): void {
    this.loadChecklists();
    this.loadUsers();
    this.loadUserChecklists();
    this.loadItems();
  }

  shouldshow(superadmin:boolean):boolean{
    return superadmin;
  }

  loadUserChecklistsperuser(user:User):NgIterable<UserChecklist>{
    return this.user_checklists.filter(user_checklist => user_checklist.user.username === user.username);
  }

  /*
  * Admin
  * Load all users, checklists, user_checklists and items
   */

  loadUsers(){
    this.dataservice.getallusers().subscribe(users => {
      this.users = users.sort((a, b) => a.username.localeCompare(b.username));
    });
  }
  loadChecklists(){
    this.dataservice.getChecklists().subscribe(checklists => {
      this.checklists = checklists;
    });
  }
  loadUserChecklists(){
    this.dataservice.getuserchecklists().subscribe(user_checklists => {
      this.user_checklists = user_checklists;
    });
  }
  loadItems(){
    this.dataservice.getItems().subscribe(items => {
      this.items = items;
    });
  }

  changeUserPassword(userid: string, password: string) {
    this.dataservice.changeuserpassword(userid, password).  subscribe();
  this.reloadpage();

  }
  updateChecklist(checklistid: string) {
    this.dataservice.updatechecklist(checklistid).  subscribe();
  this.reloadpage();

  }

  /*
  * SuperAdmin
   */

  promoteUser(userid: string) {
    this.dataservice.promoteuser(userid).  subscribe();
  this.reloadpage();

  }
  demoteUser(userid: string) {
    this.dataservice.demoteuser(userid).  subscribe();
  this.reloadpage();

  }
  setUserStatus(userid: string, status: boolean) {
    this.dataservice.setuserstatus(userid, status).  subscribe();
  this.reloadpage();

  }
  deleteUser(userid: string) {
    this.dataservice.deleteuser(userid).  subscribe();
  this.reloadpage();

  }
  deleteUserChecklist( checklistid: string) {
    this.dataservice.deleteuserchecklist(checklistid).  subscribe();
  this.reloadpage();

  }
  deleteallUserChecklists(userid:string) {
    this.dataservice.deleteAllUserChecklistFromUser(userid).  subscribe();

    this.reloadpage();

  }
  blockuserchecklist(userchecklistid: string) {
    this.dataservice.blockUserChecklist(userchecklistid).  subscribe();

    this.reloadpage();

  }
  unblockuserchecklist(userchecklistid: string) {
    this.dataservice.unblockUserChecklist(userchecklistid).  subscribe();

    this.reloadpage();

  }
  grantuseraccess(userid: string, isGranted: boolean) {
    this.dataservice.grantUserAccess(userid, isGranted).  subscribe();

    this.reloadpage();
  }

  reloadpage(){
    this.loadChecklists();
    this.loadUsers();
    this.loadUserChecklists();
    this.loadItems();
    window.location.reload();
  }



}
