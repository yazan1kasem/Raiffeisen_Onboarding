import { Component, Inject, OnInit } from '@angular/core';
import {
  MatDialogRef,
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogContent,
  MatDialogTitle
} from "@angular/material/dialog";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import { User } from "../models/user";
import {MatError, MatFormField, MatLabel} from "@angular/material/form-field";
import {MatButton} from "@angular/material/button";
import {MatInput} from "@angular/material/input";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-passwort-dialog',
  standalone: true,
  templateUrl: './passwort-dialog.component.html',
  imports: [
    MatError,
    MatLabel,
    MatFormField,
    MatDialogActions,
    MatButton,
    MatDialogContent,
    MatDialogTitle,
    ReactiveFormsModule,
    MatInput,
    NgIf
  ],
  styleUrl: './passwort-dialog.component.css'
})
export class PasswortDialogComponent implements OnInit {
  passwordForm!: FormGroup;
  user: User;
  minLength = 10;
  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<PasswortDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { user: User }
  ) {
    this.user = data.user;
  }

  ngOnInit(): void {
    this.passwordForm = this.fb.group({
      newPassword: ['', [Validators.required, Validators.minLength(this.minLength)]],
      confirmPassword: ['', Validators.required]
    }, { validators: this.passwordMatchValidator }); // ✅ Use `validators` instead of `validator`
  }

  passwordMatchValidator(form: FormGroup) {
    return form.get('newPassword')?.value === form.get('confirmPassword')?.value
      ? null : { 'mismatch': true }; // ✅ Fixed: Ensure it's an object
  }

  onSubmit(): void {
    if (this.passwordForm.valid) {
      console.log(`User ${this.user.username} is changing their password.`);
      this.dialogRef.close({ newPassword: this.passwordForm.value.newPassword });
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
