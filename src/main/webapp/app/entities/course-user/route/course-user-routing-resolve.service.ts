import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICourseUser, CourseUser } from '../course-user.model';
import { CourseUserService } from '../service/course-user.service';

@Injectable({ providedIn: 'root' })
export class CourseUserRoutingResolveService implements Resolve<ICourseUser> {
  constructor(protected service: CourseUserService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICourseUser> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((courseUser: HttpResponse<CourseUser>) => {
          if (courseUser.body) {
            return of(courseUser.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CourseUser());
  }
}
