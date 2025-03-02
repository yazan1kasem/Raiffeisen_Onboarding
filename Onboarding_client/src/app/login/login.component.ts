import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from "@angular/forms";
import { CommonModule } from "@angular/common";
import { DataService } from "../data.service";
import { AuthService } from "../auth.service";

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
  errorMessage: string = '';

  constructor(private router: Router, private checklistService: DataService, private authService: AuthService) {}

  onSubmit(): void {
    if (!this.username) {
      this.errorMessage = "Benutzername ist leer.";
      return;
    }
    if (!this.password) {
      this.errorMessage = "Passwort ist leer.";
      return;
    }

    this.authService.login(this.username, this.password).subscribe(
      (response) => {
        if (response && response.token) {
          this.router.navigate(['/checklist']);
        } else {
          this.errorMessage = "Benutzername oder Passwort ist nicht korrekt.";
        }
      },
      (error) => {
        if (error.status === 0) {
          this.errorMessage = "Keine Internetverbindung.";
        } else if (error.status === 401) {
          this.errorMessage = "Benutzername oder Passwort ist nicht korrekt.";
        } else if (error.status === 403) {
          this.errorMessage = "Sie wurden vom Superadmin gebannt. Wende dich an [Support/Kontaktperson].";
        } else if (error.status >= 500) {
          this.errorMessage = "Server Error";
        } else {
          this.errorMessage = "Ein unbekannter Fehler ist aufgetreten.";
        }
      }
    );
  }
}
