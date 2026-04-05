import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavBarComponent implements OnInit {

  role!: string | null;
  userId!: number | null;
  notifications: any[] = [];
  unreadCount: number = 0;
  showDropdown: boolean = false;

  constructor(private mediconnectService: import('../../mediconnect/services/mediconnect.service').MediConnectService) { }

  ngOnInit(): void {
    console.log(localStorage.getItem("role"));
    this.role = localStorage.getItem("role");
    const id = localStorage.getItem("user");
    if(id) {
        this.userId = JSON.parse(id).userId;
        this.loadNotifications();
    }
  }

  loadNotifications(): void {
    if(!this.userId) return;
    this.mediconnectService.getNotifications(this.userId).subscribe((notes: any[]) => {
      this.notifications = notes;
      this.unreadCount = this.notifications.filter(n => !n.isRead).length;
    });
  }

  toggleDropdown(): void {
    this.showDropdown = !this.showDropdown;
  }

  markAsRead(notificationId: number, event: Event): void {
      event.stopPropagation();
      this.mediconnectService.markNotificationRead(notificationId).subscribe(() => {
          this.loadNotifications();
      });
  }

}