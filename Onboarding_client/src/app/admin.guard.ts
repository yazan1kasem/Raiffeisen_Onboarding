import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AdminGuard implements CanActivate {
  constructor(private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    if (localStorage.getItem('role') === 'ADMIN'|| localStorage.getItem('role') === 'SUPER_ADMIN') {
      return true;
    } else {
      console.log("Du bist kein Admin oder Superadmin!");
      this.router.navigate(['/checklist']);
      return false;
    }
  }
}
