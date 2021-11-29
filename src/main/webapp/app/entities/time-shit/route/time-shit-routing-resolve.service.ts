import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITimeShit, TimeShit } from '../time-shit.model';
import { TimeShitService } from '../service/time-shit.service';

@Injectable({ providedIn: 'root' })
export class TimeShitRoutingResolveService implements Resolve<ITimeShit> {
  constructor(protected service: TimeShitService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITimeShit> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((timeShit: HttpResponse<TimeShit>) => {
          if (timeShit.body) {
            return of(timeShit.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TimeShit());
  }
}
