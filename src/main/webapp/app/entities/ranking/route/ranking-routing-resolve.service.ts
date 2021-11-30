import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRanking, Ranking } from '../ranking.model';
import { RankingService } from '../service/ranking.service';

@Injectable({ providedIn: 'root' })
export class RankingRoutingResolveService implements Resolve<IRanking> {
  constructor(protected service: RankingService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRanking> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((ranking: HttpResponse<Ranking>) => {
          if (ranking.body) {
            return of(ranking.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Ranking());
  }
}
