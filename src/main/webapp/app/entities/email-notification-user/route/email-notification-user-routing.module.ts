import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EmailNotificationUserComponent } from '../list/email-notification-user.component';
import { EmailNotificationUserDetailComponent } from '../detail/email-notification-user-detail.component';
import { EmailNotificationUserUpdateComponent } from '../update/email-notification-user-update.component';
import { EmailNotificationUserRoutingResolveService } from './email-notification-user-routing-resolve.service';

const emailNotificationUserRoute: Routes = [
  {
    path: '',
    component: EmailNotificationUserComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EmailNotificationUserDetailComponent,
    resolve: {
      emailNotificationUser: EmailNotificationUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EmailNotificationUserUpdateComponent,
    resolve: {
      emailNotificationUser: EmailNotificationUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EmailNotificationUserUpdateComponent,
    resolve: {
      emailNotificationUser: EmailNotificationUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(emailNotificationUserRoute)],
  exports: [RouterModule],
})
export class EmailNotificationUserRoutingModule {}
