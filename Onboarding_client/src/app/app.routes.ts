import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ChecklistComponent } from './checklist/checklist.component';
import { LoginComponent } from './login/login.component';
import { SignupComponent } from './signup/signup.component';
import { ProtectedComponent } from './protected/protected.component';
import { AuthGuard } from './auth.guard';
import { ChecklistendetailsComponent } from './checklistendetails/checklistendetails.component';
import { SavedChecklistsComponent } from './savedchecklists/savedchecklists.component';
import {AdminComponent} from "./admin/admin.component";

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignupComponent },
  { path: 'protected', component: ProtectedComponent, canActivate: [AuthGuard] },
  {
    path: 'checklist',
    title: 'Checklisten',
    component: ChecklistComponent,
    canActivate: [AuthGuard]
  },
  {
    path: '',
    redirectTo: '/checklist',
    pathMatch: 'full'
  },
  { path: 'checklistendetails/:id', component: ChecklistendetailsComponent, canActivate: [AuthGuard] },
  { path: 'saved-checklists', component: SavedChecklistsComponent, canActivate: [AuthGuard] },
  {path: '/admin', redirectTo: '/admin', component: AdminComponent, canActivate: [AuthGuard]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
