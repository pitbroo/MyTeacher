import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PaymentUserComponent } from '../list/payment-user.component';
import { PaymentUserDetailComponent } from '../detail/payment-user-detail.component';
import { PaymentUserUpdateComponent } from '../update/payment-user-update.component';
import { PaymentUserRoutingResolveService } from './payment-user-routing-resolve.service';

const paymentUserRoute: Routes = [
  {
    path: '',
    component: PaymentUserComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PaymentUserDetailComponent,
    resolve: {
      paymentUser: PaymentUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PaymentUserUpdateComponent,
    resolve: {
      paymentUser: PaymentUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PaymentUserUpdateComponent,
    resolve: {
      paymentUser: PaymentUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(paymentUserRoute)],
  exports: [RouterModule],
})
export class PaymentUserRoutingModule {}
