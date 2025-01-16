import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ChecklistComponent } from './checklist/checklist.component';
import { LoginComponent } from './login/login.component';
import { SignupComponent } from './signup/signup.component';
import { ProtectedComponent } from './protected/protected.component';
import { AuthGuard } from './auth.guard';
import { ChecklistendetailsComponent } from './checklistendetails/checklistendetails.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignupComponent },
  { path: 'protected', component: ProtectedComponent, canActivate: [AuthGuard] },
  {
    path: 'checklist',
    title: 'Checklisten',
    component: ChecklistComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'checklistdetail/:id',
    title: 'Checklistendetails',
    component: ChecklistendetailsComponent,
    canActivate: [AuthGuard],
  },
  {
    path: '', // Root path
    redirectTo: 'checklist',
    pathMatch: 'full',
  },
  {
    path: '**', // Fallback for undefined routes
    redirectTo: 'login',
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
