import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  IsLoggin: boolean = false;
  roleName: string | null = null;

  constructor(private authService: AuthService, private router: Router) {
    this.IsLoggin = this.authService.getLoginStatus;
    this.roleName = this.authService.getRole;

    if (!this.IsLoggin) {
      this.router.navigate(['/login']);
    }
  }

  logout() {
    this.authService.logout();
    window.location.reload();
  }
}