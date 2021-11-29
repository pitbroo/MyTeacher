import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEmailNotificationUser, EmailNotificationUser } from '../email-notification-user.model';
import { EmailNotificationUserService } from '../service/email-notification-user.service';

@Injectable({ providedIn: 'root' })
export class EmailNotificationUserRoutingResolveService implements Resolve<IEmailNotificationUser> {
  constructor(protected service: EmailNotificationUserService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEmailNotificationUser> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((emailNotificationUser: HttpResponse<EmailNotificationUser>) => {
          if (emailNotificationUser.body) {
            return of(emailNotificationUser.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new EmailNotificationUser());
  }
}
