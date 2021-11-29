import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEmailNotification, EmailNotification } from '../email-notification.model';
import { EmailNotificationService } from '../service/email-notification.service';

@Injectable({ providedIn: 'root' })
export class EmailNotificationRoutingResolveService implements Resolve<IEmailNotification> {
  constructor(protected service: EmailNotificationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEmailNotification> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((emailNotification: HttpResponse<EmailNotification>) => {
          if (emailNotification.body) {
            return of(emailNotification.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new EmailNotification());
  }
}
