import {RouterModule, Routes} from '@angular/router';
import {ChecklistComponent} from "./checklist/checklist.component";

import { AuthGuard } from './auth.guard';
import {ProtectedComponent} from "./protected/protected.component";
import {LoginComponent} from "./login/login.component";
import {NgModule} from "@angular/core";

export const routes: Routes = [
  { path: 'protected', component: ProtectedComponent, canActivate: [AuthGuard] },
  { path: 'login', component: LoginComponent },
  { path: '', redirectTo: 'checklist', pathMatch: 'full' },
  { path: 'checklist', title: "Checklisten", component: ChecklistComponent },
  { path: '**', redirectTo: 'checklist' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
