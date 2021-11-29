import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EmailNotificationComponent } from '../list/email-notification.component';
import { EmailNotificationDetailComponent } from '../detail/email-notification-detail.component';
import { EmailNotificationUpdateComponent } from '../update/email-notification-update.component';
import { EmailNotificationRoutingResolveService } from './email-notification-routing-resolve.service';

const emailNotificationRoute: Routes = [
  {
    path: '',
    component: EmailNotificationComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EmailNotificationDetailComponent,
    resolve: {
      emailNotification: EmailNotificationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EmailNotificationUpdateComponent,
    resolve: {
      emailNotification: EmailNotificationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EmailNotificationUpdateComponent,
    resolve: {
      emailNotification: EmailNotificationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(emailNotificationRoute)],
  exports: [RouterModule],
})
export class EmailNotificationRoutingModule {}
