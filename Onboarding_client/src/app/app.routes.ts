import { Routes } from '@angular/router';
import {ChecklistComponent} from "./checklist/checklist.component";

export const routes: Routes = [
  {
    path:'',
    title:"Checklist",
    component:ChecklistComponent
  },
  {
  path:'checklist',
  title:"Checklisten",
  component:ChecklistComponent
},];
