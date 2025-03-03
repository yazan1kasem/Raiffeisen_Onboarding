import {Component, EventEmitter, Output} from '@angular/core';
import {Router, RouterModule} from "@angular/router";
import {AuthService} from "../auth.service";
import {NgClass, NgIf, NgOptimizedImage} from "@angular/common";
import {DataService} from "../data.service";

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [RouterModule, NgClass, NgIf, NgOptimizedImage],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css'
})
export class SidebarComponent {
  isCollapsed = false;
  @Output() toggle = new EventEmitter<boolean>();
  isMobileOpen: any;
  toggleSidebar() {
    this.isCollapsed = !this.isCollapsed;
    this.toggle.emit(this.isCollapsed);
  }

  constructor(private router: Router,private authservice:AuthService, dataservice:DataService ) {

  }
  logout() {
    this.authservice.logout();
    this.router.navigate(['/login']);
  }

  isAdmin():boolean{
    return localStorage.getItem('role') === 'ADMIN' || localStorage.getItem('role') === 'SUPER_ADMIN';
  }

}
