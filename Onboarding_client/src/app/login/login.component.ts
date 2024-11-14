import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from "@angular/forms";
import { CommonModule } from "@angular/common";
import { ChecklistService } from "../data.service";
import {AuthService} from "../auth.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  standalone: true,
  imports: [
    FormsModule, CommonModule
  ],
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  username: string = '';
  password: string = '';

  constructor(private router: Router, private checklistService: ChecklistService,private authService: AuthService) {}

  onSubmit(): void {
    this.authService.login(this.username, this.password).subscribe(
      (response) => {
        if (response && response.token) {
          this.router.navigate(['/checklist']);
        } else {
          alert("Invalid username or password.");
        }
      },
      (error) => {
        alert("Login failed. Please try again.");
      }
    );
  }
}
