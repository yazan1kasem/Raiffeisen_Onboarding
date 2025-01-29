import { Component } from '@angular/core';
import {Router, RouterLink} from '@angular/router';
import { FormsModule } from "@angular/forms";
import { CommonModule } from "@angular/common";
import { DataService } from "../data.service";
import {AuthService} from "../auth.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  standalone: true,
  imports: [
    FormsModule, CommonModule, RouterLink
  ],
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  username: string = '';
  password: string = '';
  errorMessage: string ='';

  constructor(private router: Router, private checklistService: DataService, private authService: AuthService) {}

  onSubmit(): void {
    this.authService.login(this.username, this.password).subscribe(
      (response) => {
        if (response && response.token) {
          this.router.navigate(['/checklist']);
        } else {
          this.errorMessage="Login failed. Please try again.";
        }
      },
      (error) => {
        this.errorMessage="Invalid username or password.";
      }
    );
  }
}
