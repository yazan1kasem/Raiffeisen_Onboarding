import {Component} from '@angular/core';
import {ChecklistStatus, UserChecklist} from "../models/user_checklist";
import {Role, User} from "../models/user";
import {MatExpansionModule} from "@angular/material/expansion";
import {Checklist} from "../models/checklist";
import {FormsModule} from "@angular/forms";
import {CommonModule} from "@angular/common";
import {DataService} from "../data.service";
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




  constructor(private dataservice:DataService) { }

  ngOnInit(): void {

  }
  loadUsers(){

  }


}
