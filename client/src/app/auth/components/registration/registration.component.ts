import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';

@Component({
    selector: 'app-registration',
    templateUrl: './registration.component.html',
    styleUrls: ['./registration.component.scss'],
})
export class RegistrationComponent {
    isOpen = false;
    registrationForm!: FormGroup;
    successMessage: string | null = null;
    errorMessage: string | null = null;
    selectedRole: string | null = null;

    constructor(private formBuilder: FormBuilder, private authService: AuthService) { }

    ngOnInit(): void {
        this.registrationForm = this.formBuilder.group({
            username: ['', [Validators.required, Validators.pattern(/^[a-zA-Z0-9]+$/)]],
            email: ['', [Validators.required, Validators.email]],
            password: ['', [Validators.required, Validators.minLength(8), Validators.pattern(/^(?=.*[A-Z])(?=.*\d).+$/)]],
            role: ['', [Validators.required]],
            fullName: ['', Validators.required],
            contactNumber: ['', Validators.required],
            specialty: [''],
            yearsOfExperience: [null], 
            dateOfBirth: [null,this.futureDateValidator], 
            address: [''], 
        });
    }

    futureDateValidator(control:any){
        if(!control.value)
        {
            return null;
        }
        const selectedDate=new Date(control.value);
        const today=new Date();
        today.setHours(0,0,0,0);
        return selectedDate>today?{futureDate:true}:null;
    }

    onRoleChange(event: Event): void {
        const selectElement = event.target as HTMLSelectElement;
        const role = selectElement.value;
        this.selectedRole = role;

        if (role === 'DOCTOR') {
            this.registrationForm.patchValue({ dateOfBirth: null, address: '' });
            this.registrationForm.get('specialty')?.setValidators([Validators.required]);
            this.registrationForm.get('specialty')?.updateValueAndValidity();
        } else if (role === 'PATIENT') {
            this.registrationForm.patchValue({ specialty: '', yearsOfExperience: null });
            this.registrationForm.get('specialty')?.clearValidators();
            this.registrationForm.get('specialty')?.updateValueAndValidity();
        } else {
            this.registrationForm.get('specialty')?.clearValidators();
            this.registrationForm.get('specialty')?.updateValueAndValidity();
        }
    }


    onSubmit(): void {
        if (this.registrationForm.valid) {
            const role = this.registrationForm.get('role')?.value;
            let req$;
            if(role === 'PATIENT') {
                req$ = this.authService.registerPatient(this.registrationForm.value);
            } else if (role === 'DOCTOR') {
                req$ = this.authService.registerDoctors(this.registrationForm.value);
            } else if (role === 'RECEPTIONIST') {
                req$ = this.authService.registerReceptionist(this.registrationForm.value);
            } else {
                this.errorMessage = 'Invalid role selected.';
                return;
            }

            req$.subscribe(
                (response: any) => {
                    this.successMessage = "User successfully registered";
                    this.errorMessage = null;
                    this.resetForm();
                    console.log('Success:', this.successMessage);
                },
                (error: HttpErrorResponse) => {
                    if (error.error && typeof error.error === 'string') {
                        this.errorMessage = error.error;
                    } else if (error.error && error.error.message) {
                        this.errorMessage = error.error.message;
                    } else {
                        this.errorMessage = 'An unexpected error occurred. Please try again later.';
                    }
                    this.successMessage = null;
                    console.error('Error:', this.errorMessage);
                }
            );
        } else {
            this.errorMessage = 'Please fill out all fields correctly.';
            this.successMessage = null;
        }
    }

    resetForm(): void {
        this.registrationForm.reset();
    }
}