import { Component } from '@angular/core';
import {Router, RouterModule} from "@angular/router";
import {AuthService} from "../auth.service";

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css'
})
export class SidebarComponent {

  constructor(private router: Router,private authservice:AuthService ) {

  }
  logout() {
    this.authservice.logout();
    this.router.navigate(['/login']);
  }
}
