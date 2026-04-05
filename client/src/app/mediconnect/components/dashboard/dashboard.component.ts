import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Appointment } from '../../models/Appointment';
import { Clinic } from '../../models/Clinic';
import { Doctor } from '../../models/Doctor';
import { Patient } from '../../models/Patient';
import { MediConnectService } from '../../services/mediconnect.service';

@Component({
    selector: 'app-dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit {
    doctorDetails: any;
    patientDetails: any;
    doctors: Doctor[] = [];
    clinics: Clinic[] = [];
    appointments: Appointment[] = [];
    patients: Patient[] = [];

    role!: string | null;
    userId!: number;
    doctorId!: number;
    patientId!: number;

    selectedClinicId: number | undefined;
    selectClinicAppointments: Appointment[] = [];

    constructor(private mediconnectService: MediConnectService, private router: Router) { }

    ngOnInit(): void {
        this.role = localStorage.getItem("role");
        this.userId = Number(localStorage.getItem("user_id"));
        this.doctorId = Number(localStorage.getItem("doctor_id"));
        this.patientId = Number(localStorage.getItem("patient_id"));
        if (this.role === 'DOCTOR') {
            console.log('loadDoctorData');
            this.loadDoctorData();
        }
        else if (this.role === 'PATIENT') {
            console.log('loadPatientData');
            this.loadPatientData();
        }
        else if (this.role === 'RECEPTIONIST') {
            console.log('loadReceptionistData');
            this.loadReceptionistData();
        }
    }

    loadDoctorData(): void {
        this.mediconnectService.getDoctorById(this.doctorId).subscribe({
            next: (response: any) => {
                this.doctorDetails = response;
            },
            error: (error: any) => console.log('Error loading loggedIn doctor details', error)
        });

        this.mediconnectService.getClinicsByDoctorId(this.doctorId).subscribe({
            next: (response: any) => {
                this.clinics = response;
                if (this.clinics.length > 0) {
                    this.selectedClinicId = this.clinics[0].clinicId;
                    this.loadAppointments(this.selectedClinicId);
                }
            },
            error: (error: any) => console.log('Error loading clinics', error)
        });

        this.mediconnectService.getAllPatients().subscribe({
            next: (response: any) => {
                this.patients = response;
            },
            error: (error: any) => console.log('Error loading all patients.', error)
        });

        this.loadDoctorAvailability();
    }

    loadAppointments(clinicId: number): void {
        this.mediconnectService.getAppointmentsByClinic(clinicId).subscribe({
            next: (response: any) => {
                this.selectClinicAppointments = response;
            },
            error: (error: any) => console.log('Error loading appointments', error),
        });
    }

    onClinicSelect(clinic: Clinic): void {
        this.selectedClinicId = clinic.clinicId;
        this.loadAppointments(this.selectedClinicId);
    }

    loadPatientData(): void {
        this.mediconnectService.getPatientById(this.patientId).subscribe({
            next: (response: any) => {
                this.patientDetails = response;
            },
            error: (error: any) => console.log('Error loading loggedIn patient details', error)
        });

        this.mediconnectService.getAppointmentsByPatient(this.patientId).subscribe({
            next: (response: any) => {
                this.appointments = response;
            },
            error: (error: any) => console.log('Error loading existing appointments.', error)
        });

        this.mediconnectService.getAllClinics().subscribe({
            next: (response: any) => {
                this.clinics = response;
            },
            error: (error: any) => console.log('Error loading clinics', error)
        });

        this.mediconnectService.getAllDoctors().subscribe({
            next: (response: any) => {
                this.doctors = response;
            },
            error: (error: any) => console.log('Error loading doctors', error)
        });
    }

    // --- RECEPTIONIST DATA ---
    allAppointments: Appointment[] = [];
    filteredAppointments: Appointment[] = [];
    receptionistFilters = { status: '', date: '', doctorName: '', patientName: '' };

    loadReceptionistData(): void {
        this.mediconnectService.getAllAppointments().subscribe({
            next: (response: any) => {
                this.allAppointments = response;
                this.filteredAppointments = response;
            },
            error: (error: any) => console.log('Error loading all appointments', error)
        });
    }

    applyFilters(): void {
        this.filteredAppointments = this.allAppointments.filter(app => {
            let match = true;
            if(this.receptionistFilters.status && app.status.toLowerCase() !== this.receptionistFilters.status.toLowerCase()) {
                match = false;
            }
            if(this.receptionistFilters.date && app.appointmentDate && !app.appointmentDate.toString().startsWith(this.receptionistFilters.date)) {
                match = false;
            }
            if(this.receptionistFilters.doctorName && app.clinic.doctor.fullName.toLowerCase().indexOf(this.receptionistFilters.doctorName.toLowerCase()) === -1) {
                match = false;
            }
            if(this.receptionistFilters.patientName && app.patient.fullName.toLowerCase().indexOf(this.receptionistFilters.patientName.toLowerCase()) === -1) {
                match = false;
            }
            return match;
        });
    }

    clearFilters(): void {
        this.receptionistFilters = { status: '', date: '', doctorName: '', patientName: '' };
        this.filteredAppointments = this.allAppointments;
    }

    navigateToEditPatient(): void {
        this.router.navigate(['mediconnect/patient/edit', this.patientDetails.patientId]);
    }

    deletePatient(): void {
        if (confirm('Are you sure you want to delete this patient profile?')) {
            this.mediconnectService.deletePatient(this.patientId).subscribe({
                next: () => {
                    this.router.navigate(['/']);
                },
                error: (error: any) => console.error('Error deleting patient:', error)

            })
        }
    }

    navigateToEditDoctor(): void {
        this.router.navigate(['mediconnect/doctor/edit', this.doctorDetails.doctorId]);
    }

    deleteDoctor(): void {
        if (confirm('Are you sure you want to delete this doctor profile?')) {
            this.mediconnectService.deleteDoctor(this.doctorId).subscribe({
                next: () => {
                    this.router.navigate(['/']);
                },
                error: (error: any) => console.error('Error deleting doctor:', error)

            })
        }
    }

    navigateToEditClinic(clinicId: number): void {
        this.router.navigate(['mediconnect/clinic/edit', clinicId]);
    }

    deleteClinic(clinicId: number): void {
        if (confirm('Are you sure you want to delete this clinic?')) {
            this.mediconnectService.deleteClinic(clinicId).subscribe({
                next: () => {
                    this.loadDoctorData();
                },
                error: (error: any) => console.error('Error deleting clinic:', error)

            })
        }
    }

    cancelAppointment(appointment: Appointment): void {
        if (confirm('Are you sure you want to cancel this appointment?')) {
            appointment.status = "Cancel"; // Old method, keeping for legacy
            if(this.role === 'PATIENT') {
                this.mediconnectService.cancelAppointmentPatient(appointment.appointmentId, "User cancelled").subscribe({
                   next: () => this.loadPatientData(),
                   error: (err: any) => console.log(err)
                });
            } else {
                this.mediconnectService.updateAppointment(appointment).subscribe({
                    next: () => {
                        if (this.role === 'DOCTOR') this.loadDoctorData();
                        if (this.role === 'RECEPTIONIST') this.loadReceptionistData();
                    },
                    error: (error: any) => console.error('Error cancelling appointment:', error)
                });
            }
        }
    }

    // --- MEDICAL RECORDS ---
    selectedRecordAppointmentId: number | null = null;
    currentRecord: any = {};

    openRecord(appointmentId: number): void {
        this.selectedRecordAppointmentId = appointmentId;
        this.currentRecord = {};
        
        // Try fetch existing
        if (this.role === 'PATIENT') {
            this.mediconnectService.getPatientMedicalRecords(this.patientId).subscribe((records: any[]) => {
                const rec = records.find((r: any) => r.appointmentId === appointmentId);
                if(rec) this.currentRecord = rec;
            });
        }
    }

    loadDoctorRecordFor(appointmentId: number): void {
        this.selectedRecordAppointmentId = appointmentId;
        this.currentRecord = { appointmentId: appointmentId };
        // Ideally we'd have a GET /api/doctor/medicalrecords/:appointmentId
        // The backend exposes: GET /api/doctor/medicalrecords/{appointmentId}
        this.mediconnectService['http'].get(`${this.mediconnectService['baseUrl']}/api/doctor/medicalrecords/${appointmentId}`).subscribe({
            next: (rec: any) => {
                if(rec) this.currentRecord = rec;
            },
            error: (err: any) => console.log('No record yet or error', err)
        });
    }

    // --- DOCTOR AVAILABILITY ---
    availabilitySlots: any[] = [];
    newSlot: any = { availableDate: '', startTime: '', endTime: '', status: 'AVAILABLE' };

    loadDoctorAvailability(): void {
        this.mediconnectService.getDoctorAvailability(this.doctorId).subscribe({
            next: (slots: any[]) => this.availabilitySlots = slots,
            error: (err: any) => console.log('Error loading slots', err)
        });
    }

    createAvailabilitySlot(): void {
        // Validate inputs before sending
        if(!this.newSlot.availableDate || !this.newSlot.startTime || !this.newSlot.endTime) {
            alert('Please fill out all slot fields!');
            return;
        }
        
        // DoctorAvailability expects doctorId inside it
        const payload = {
            doctor: { doctorId: this.doctorId },
            availableDate: this.newSlot.availableDate,
            startTime: this.newSlot.startTime,
            endTime: this.newSlot.endTime,
            status: this.newSlot.status
        };

        this.mediconnectService.updateDoctorAvailability(payload).subscribe({
            next: (res: any) => {
                alert('Slot added!');
                this.loadDoctorAvailability();
                this.newSlot = { availableDate: '', startTime: '', endTime: '', status: 'AVAILABLE' };
            },
            error: (err: any) => alert('Error creating slot.')
        });
    }

    saveMedicalRecord(): void {
        this.mediconnectService.saveMedicalRecord(this.doctorId, this.currentRecord).subscribe({
            next: (res: any) => {
                alert('Record saved successfully!');
                this.selectedRecordAppointmentId = null;
            },
            error: (err: any) => {
                alert('Error saving record: ' + (err.error || err.message));
            }
        });
    }

    closeRecord(): void {
        this.selectedRecordAppointmentId = null;
        this.currentRecord = {};
    }
}