import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.scss']
})
export class ResetPasswordComponent implements OnInit {
  token = '';
  newPassword = '';
  confirmPassword = '';
  successMessage: string | null = null;
  errorMessage: string | null = null;
  loading = false;
  tokenMissing = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.token = this.route.snapshot.queryParamMap.get('token') || '';
    if (!this.token) {
      this.tokenMissing = true;
      this.errorMessage = 'Invalid or missing reset token. Please request a new reset link.';
    }
  }

  onSubmit(): void {
    this.errorMessage = null;
    this.successMessage = null;

    if (!this.newPassword || !this.confirmPassword) {
      this.errorMessage = 'Both password fields are required.';
      return;
    }
    if (this.newPassword.length < 6) {
      this.errorMessage = 'Password must be at least 6 characters.';
      return;
    }
    if (this.newPassword !== this.confirmPassword) {
      this.errorMessage = 'Passwords do not match.';
      return;
    }

    this.loading = true;
    this.authService.resetPassword(this.token, this.newPassword).subscribe({
      next: (res: any) => {
        this.loading = false;
        this.successMessage = res.message || 'Password reset successful!';
        // Redirect to login after 2 seconds
        setTimeout(() => this.router.navigate(['/auth/login']), 2000);
      },
      error: (err: any) => {
        this.loading = false;
        this.errorMessage = err?.error?.message || 'Failed to reset password. The link may have expired.';
      }
    });
  }
}
