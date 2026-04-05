import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.scss']
})
export class ForgotPasswordComponent {
  username = '';
  successMessage: string | null = null;
  errorMessage: string | null = null;
  loading = false;

  constructor(private authService: AuthService) {}

  onSubmit(): void {
    if (!this.username.trim()) {
      this.errorMessage = 'Please enter your email address.';
      return;
    }
    this.loading = true;
    this.successMessage = null;
    this.errorMessage = null;

    this.authService.forgotPassword(this.username.trim()).subscribe({
      next: (res: any) => {
        this.loading = false;
        this.successMessage = res.message || 'If that email is registered, a reset link has been sent.';
      },
      error: () => {
        this.loading = false;
        // Still show success-like message to avoid email enumeration
        this.successMessage = 'If that email is registered, a reset link has been sent.';
      }
    });
  }
}
