import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEmailNotificationUser } from '../email-notification-user.model';

@Component({
  selector: 'jhi-email-notification-user-detail',
  templateUrl: './email-notification-user-detail.component.html',
})
export class EmailNotificationUserDetailComponent implements OnInit {
  emailNotificationUser: IEmailNotificationUser | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ emailNotificationUser }) => {
      this.emailNotificationUser = emailNotificationUser;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
