import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpService } from '../../services/http.service';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {
  itemForm: FormGroup;
  formModel: any = { role: null, email: '', password: '', username: '' };
  showMessage: boolean = false;
  responseMessage: any;

  constructor(
    public router: Router,
    private bookService: HttpService,
    private formBuilder: FormBuilder
  ) {
    this.itemForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      role: [null, Validators.required],
      specialty: [''],
      availability: ['']
    });
  }

  ngOnInit(): void {
    this.onRoleChange();
  }

  onRoleChange() {
    this.itemForm.get('role')?.valueChanges.subscribe(role => {
      if (role === 'DOCTOR') {
        this.itemForm.get('specialty')?.setValidators(Validators.required);
        this.itemForm.get('availability')?.setValidators(Validators.required);
      } else {
        this.itemForm.get('specialty')?.clearValidators();
        this.itemForm.get('availability')?.clearValidators();
      }
      this.itemForm.get('specialty')?.updateValueAndValidity();
      this.itemForm.get('availability')?.updateValueAndValidity();
    });
  }

  onRegister() {
    if (this.itemForm.valid) {
      const role = this.itemForm.value.role;
      let registration$: any;

      if (role === 'PATIENT') {
        registration$ = this.bookService.registerPatient(this.itemForm.value);
      } else if (role === 'DOCTOR') {
        registration$ = this.bookService.registerDoctors(this.itemForm.value);
      } else if (role === 'RECEPTIONIST') {
        registration$ = this.bookService.registerReceptionist(this.itemForm.value);
      }

      if (registration$) {
        registration$.subscribe(
          (response: any) => {
            this.showMessage = true;
            this.responseMessage = 'Registration successful! You can now login.';
            this.itemForm.reset();
          },
          (error: any) => {
            this.showMessage = true;
            this.responseMessage = error.error?.message || 'Registration failed.';
          }
        );
      }
    }
  }
}
