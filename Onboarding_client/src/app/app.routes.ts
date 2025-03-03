import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ChecklistComponent } from './checklist/checklist.component';
import { LoginComponent } from './login/login.component';
import { SignupComponent } from './signup/signup.component';
import { AuthGuard } from './auth.guard';
import { ChecklistendetailsComponent } from './checklistendetails/checklistendetails.component';
import { SavedChecklistsComponent } from './savedchecklists/savedchecklists.component';
import { AdminComponent } from "./admin/admin.component";
import { UserChecklistComponent } from "./user-checklist/user-checklist.component";
import { AdminGuard } from './admin.guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignupComponent },
  {
    path: 'checklist',
    title: 'Checklisten',
    component: ChecklistComponent,
    canActivate: [AuthGuard]
  },
  { path: 'checklistendetails/:id', component: ChecklistendetailsComponent, canActivate: [AuthGuard] },
  { path: 'saved-checklists', component: SavedChecklistsComponent, canActivate: [AuthGuard] },
  { path: 'admin', component: AdminComponent, canActivate: [AuthGuard, AdminGuard] },
  { path: 'user-checklist/:id', component: UserChecklistComponent, canActivate: [AuthGuard] },
  { path: '', redirectTo: '/checklist', pathMatch: 'full' },
  { path: '**', redirectTo: '/checklist' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
