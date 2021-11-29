import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEmailNotification } from '../email-notification.model';

@Component({
  selector: 'jhi-email-notification-detail',
  templateUrl: './email-notification-detail.component.html',
})
export class EmailNotificationDetailComponent implements OnInit {
  emailNotification: IEmailNotification | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ emailNotification }) => {
      this.emailNotification = emailNotification;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
