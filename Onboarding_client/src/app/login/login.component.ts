import { Component } from '@angular/core';
import { Router } from '@angular/router';
import {FormsModule} from "@angular/forms";
import {CommonModule} from "@angular/common";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  standalone: true,
  imports: [
    FormsModule,CommonModule
  ],
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  email: string = '';
  password: string = '';

  constructor(private router: Router) {}

  onSubmit(): void {

    if (this.email && this.password) {
      console.log('Email:', this.email);
      console.log('Password:', this.password);

      this.router.navigate(['/home']);
    } else {
      alert("Please fill in both fields.");
    }
  }
}

