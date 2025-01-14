import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ChecklistComponent } from './checklist/checklist.component';
import { LoginComponent } from './login/login.component';
import { SignupComponent } from './signup/signup.component';
import { ProtectedComponent } from './protected/protected.component';
import { AuthGuard } from './auth.guard';

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
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
