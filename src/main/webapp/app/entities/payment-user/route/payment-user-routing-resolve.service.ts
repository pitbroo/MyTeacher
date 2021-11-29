import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPaymentUser, PaymentUser } from '../payment-user.model';
import { PaymentUserService } from '../service/payment-user.service';

@Injectable({ providedIn: 'root' })
export class PaymentUserRoutingResolveService implements Resolve<IPaymentUser> {
  constructor(protected service: PaymentUserService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPaymentUser> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((paymentUser: HttpResponse<PaymentUser>) => {
          if (paymentUser.body) {
            return of(paymentUser.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PaymentUser());
  }
}
