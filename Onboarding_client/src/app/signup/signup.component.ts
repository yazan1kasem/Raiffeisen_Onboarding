import { Component } from '@angular/core';
import { Router, RouterLink } from "@angular/router";
import { FormsModule } from "@angular/forms";
import { CommonModule } from "@angular/common";
import { AuthService } from "../auth.service";

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterLink
  ],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.css'
})
export class SignupComponent {
  name: string = '';
  password: string = '';
  confirmPassword: string = '';
  errorMessage: string = '';
  successMessage: string = '';

  constructor(private router: Router, private authservice: AuthService) {}

  // Submit handler for form
  onSubmit(): void {
    // Clear previous error or success messages
    this.errorMessage = '';
    this.successMessage = '';

    // Validate form
    if (!this.name || !this.password || !this.confirmPassword) {
      this.errorMessage = 'All fields are required.';
      return;
    }

    if (this.password !== this.confirmPassword) {
      this.errorMessage = 'Passwords do not match.';
      return;
    }

    this.authservice.signup(this.name, this.password).subscribe({
      next: () => {
        this.authservice.login(this.name, this.password).subscribe({
          next: () => {
            this.router.navigate(['/checklist']);
          },
          error: (loginError) => {
            this.errorMessage = 'Login failed: ' + loginError.message;
          }
        });
      },
      error: (signupError) => {
        this.errorMessage = 'Signup failed: ' + signupError.message;
      }
    });
  }
}
