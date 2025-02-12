import {Component} from '@angular/core';
import {ChecklistStatus, UserChecklist} from "../models/user_checklist";
import {Role, User} from "../models/user";
import {MatExpansionModule} from "@angular/material/expansion";
import {Checklist} from "../models/checklist";
import {FormsModule} from "@angular/forms";
import {CommonModule} from "@angular/common";
import {DataService} from "../data.service";
import {Item} from "../models/item";
/*
*  ng add @angular/material must be installed to use the MatExpansionModule
* */
@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [
    MatExpansionModule,
    FormsModule,
    CommonModule
  ],
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.css'
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

  /*
  * Admin
  * Load all users, checklists, user_checklists and items
   */

  loadUsers(){
    this.dataservice.getallusers().subscribe(users => {
      this.users = users;
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
    this.dataservice.changeuserpassword(userid, password).subscribe();
  }
  updateChecklist(checklistid: string) {
    this.dataservice.updatechecklist(checklistid).subscribe();
  }

  /*
  * SuperAdmin
   */

  promoteUser(userid: string) {
    this.dataservice.promoteuser(userid).subscribe();
  }
  demoteUser(userid: string) {
    this.dataservice.demoteuser(userid).subscribe();
  }
  setUserStatus(userid: string, status: boolean) {
    this.dataservice.setuserstatus(userid, status).subscribe();
  }
  deleteUser(userid: string) {
    this.dataservice.deleteuser(userid).subscribe();
  }
  deleteUserChecklist( checklistid: string) {
    this.dataservice.deleteuserchecklist(checklistid).subscribe();
  }
  deleteallUserChecklists(userid:string) {
    this.dataservice.deleteAllUserChecklistFromUser(userid).subscribe();
  }
  blockuserchecklist(userchecklistid: string) {
    this.dataservice.blockUserChecklist(userchecklistid).subscribe();
  }
  unblockuserchecklist(userchecklistid: string) {
    this.dataservice.unblockUserChecklist(userchecklistid).subscribe();
  }
  grantuseraccess(userid: string, isGranted: boolean) {
    this.dataservice.grantUserAccess(userid, isGranted).subscribe();
  }




}
