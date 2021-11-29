import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TimeShitComponent } from '../list/time-shit.component';
import { TimeShitDetailComponent } from '../detail/time-shit-detail.component';
import { TimeShitUpdateComponent } from '../update/time-shit-update.component';
import { TimeShitRoutingResolveService } from './time-shit-routing-resolve.service';

const timeShitRoute: Routes = [
  {
    path: '',
    component: TimeShitComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TimeShitDetailComponent,
    resolve: {
      timeShit: TimeShitRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TimeShitUpdateComponent,
    resolve: {
      timeShit: TimeShitRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TimeShitUpdateComponent,
    resolve: {
      timeShit: TimeShitRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(timeShitRoute)],
  exports: [RouterModule],
})
export class TimeShitRoutingModule {}
