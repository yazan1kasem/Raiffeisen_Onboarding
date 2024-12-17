import {RouterModule, Routes} from '@angular/router';
import {ChecklistComponent} from "./checklist/checklist.component";

import { AuthGuard } from './auth.guard';
import {ProtectedComponent} from "./protected/protected.component";
import {LoginComponent} from "./login/login.component";
import {NgModule} from "@angular/core";
import {SignupComponent} from "./signup/signup.component";


export const routes: Routes = [
  { path: 'protected', component: ProtectedComponent, canActivate: [AuthGuard] },
  { path: 'login', component: LoginComponent },
  {path:'signup', component: SignupComponent},
  { path: '', redirectTo: 'checklist', pathMatch: 'full' },
  { path: 'checklist', title: "Checklisten", component: ChecklistComponent, canActivate: [AuthGuard] },
  { path: '**', redirectTo: 'checklist' },
];


