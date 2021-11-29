import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITaskSolved, TaskSolved } from '../task-solved.model';
import { TaskSolvedService } from '../service/task-solved.service';

@Injectable({ providedIn: 'root' })
export class TaskSolvedRoutingResolveService implements Resolve<ITaskSolved> {
  constructor(protected service: TaskSolvedService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITaskSolved> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((taskSolved: HttpResponse<TaskSolved>) => {
          if (taskSolved.body) {
            return of(taskSolved.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TaskSolved());
  }
}
