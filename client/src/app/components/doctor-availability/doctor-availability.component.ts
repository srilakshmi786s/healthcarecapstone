import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpService } from '../../services/http.service';

@Component({
  selector: 'app-doctor-availability',
  templateUrl: './doctor-availability.component.html',
  styleUrls: ['./doctor-availability.component.scss']
})
export class DoctorAvailabilityComponent implements OnInit {
  itemForm: FormGroup;
  formModel: any = {};
  responseMessage: any;
  isAdded: boolean = false;

  constructor(public httpService: HttpService, private formBuilder: FormBuilder) {
    this.itemForm = this.formBuilder.group({
      doctorId: ['', Validators.required],
      availability: ['', Validators.required]
    });
  }

  ngOnInit(): void {}

  onSubmit() {
    if (this.itemForm.valid) {
      const userIdStr = localStorage.getItem('userId');
      if (userIdStr) {
        const userId = parseInt(userIdStr, 10);
        this.itemForm.get('doctorId')?.setValue(userId);

        this.httpService.updateDoctorAvailability(userId, this.itemForm.value.availability).subscribe(
          (response: any) => {
            this.responseMessage = 'Availability updated successfully!';
            this.isAdded = true;
            this.itemForm.reset();
          },
          (error: any) => {
            this.responseMessage = 'Failed to update availability.';
            this.isAdded = false;
          }
        );
      }
    }
  }
}
