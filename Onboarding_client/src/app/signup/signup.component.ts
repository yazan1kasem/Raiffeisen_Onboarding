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
  styleUrls: ['./signup.component.css']
})
export class SignupComponent {
  name: string = '';
  password: string = '';
  confirmPassword: string = '';
  errorMessage: string = '';
  successMessage: string = '';
  maxUsernameLength: number = 65;

  constructor(private router: Router, private authService: AuthService) {
  }

  onSubmit(): void {
    this.errorMessage = '';
    this.successMessage = '';

    const usernameRegex = /^[a-zA-Z0-9]+$/;

    if (!this.name || !usernameRegex.test(this.name)) {
      this.errorMessage = 'Benutzername ist leer oder enthält unzulässige Zeichen.';
      return;
    }

    if (!this.password) {
      this.errorMessage = 'Passwort ist leer.';
      return;
    }

    if (!this.confirmPassword) {
      this.errorMessage = 'Passwort bestätigen ist leer.';
      return;
    }

    if (this.password !== this.confirmPassword) {
      this.errorMessage = 'Passwort und Passwort bestätigen stimmen nicht überein.';
      return;
    }

    this.authService.signup(this.name, this.password).subscribe({
      next: () => {
        this.authService.login(this.name, this.password).subscribe({
          next: () => {
            this.router.navigate(['/checklist']);
          },
          error: (loginError) => {
            this.errorMessage = 'Technischer/Serverseitiger Fehler: ' + loginError.message;
          }
        });
      },
      error: (signupError) => {
        if (signupError.status === 409) {
          this.errorMessage = 'Benutzername bereits vergeben.';
        } else {
          this.errorMessage = 'Benutzername ist zu lang. Max. ' + this.maxUsernameLength + ' Zeichen.';
        }
      }
    });
  }
}
